package com.asyncjlink.commands.compositecommands.creation;

import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonArray;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcDrawing.Drawing;
import com.ptc.pfc.pfcDrawing.DrawingCreateOptions;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModel.ModelDescriptor;

/**
 * Session.CreateDrawing — a drawing from a template, against a given model.
 *
 * <p>The one creation path J-Link supports natively: unlike parts and assemblies,
 * {@code CreateDrawingFromTemplate} really does take a template.
 */
public final class SessionCreateDrawingCommand extends Composite {

    private static final String TEMPLATE_OPTION = "template_drawing";

    @Override public String name() { return "Session.CreateDrawing"; }
    @Override public String receiverType() { return "Session"; }
    @Override public String signature() { return "CreateDrawing(name, model[, template]) — composite"; }

    @Override
    public String description() {
        return "Create a drawing from a template to document a given model. Unlike parts and "
                + "assemblies, drawings support templates natively. A format's title-block fields "
                + "resolve from the documented model's own parameters, so pass 'parameters' to fill "
                + "them in rather than leaving them for Creo to report as errors.";
    }

    @Override
    public JsonSchema paramSchema() {
        return sessionScoped(name())
                .required("name", JsonSchema.string(), "Name for the new drawing, without extension.")
                .required("model", JsonSchema.string(),
                        "The model to document, by name or handle. Must be in session.")
                .optional("template", JsonSchema.string(),
                        "Drawing template. Defaults to the configured " + TEMPLATE_OPTION + ".")
                .optional("parameters", JsonSchema.dataObject("Parameters"),
                        "Parameters to set on the documented model before creating the drawing, as "
                                + "name/value pairs -- this is how a format's title-block fields "
                                + "(part name, drawn-by, material, ...) get filled in. Existing "
                                + "parameter types are preserved: a value that does not match an "
                                + "existing parameter's type is rejected and reported rather than "
                                + "silently coerced, so check the 'parameters' result for 'failed' "
                                + "entries. There is no reliable way for this call to report which "
                                + "field names a given format actually needs; if unsure, create "
                                + "without 'parameters' first and read 'warning' for a hint, or open "
                                + "the format in Creo to see its title-block symbols.")
                .optional("save", JsonSchema.bool(), "Save the new drawing. Default true.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        String newName = params.getString("name", null);
        if (newName == null || newName.isEmpty()) {
            throw new CommandException("Field 'name' is required", "invalid_params");
        }
        String modelRef = params.getString("model", null);
        if (modelRef == null || modelRef.isEmpty()) {
            throw new CommandException("Field 'model' is required", "invalid_params");
        }

        Model model = ctx.resolveModel(modelRef);
        if (model == null) {
            Object handle = ctx.handles().lookup(modelRef);
            if (handle instanceof Model) {
                model = (Model) handle;
            }
        }
        if (model == null) {
            throw new CommandException(
                    "Field 'model': no model named '" + modelRef + "' is in session. "
                            + "Load it first with Session.LoadModel.", "unknown_handle");
        }

        String template = params.getString("template", null);
        String templateFrom = "argument";
        if (template == null || template.isEmpty()) {
            try {
                template = ctx.session().GetConfigOption(TEMPLATE_OPTION);
            } catch (jxthrowable | RuntimeException e) {
                template = null;
            }
            templateFrom = TEMPLATE_OPTION;
        }
        if (template == null || template.isEmpty()) {
            throw new CommandException(
                    "No drawing template: pass 'template', or set " + TEMPLATE_OPTION
                            + " in config.pro.", "invalid_params");
        }

        // Applied to the documented model before creation, not the drawing: a format's title-block
        // notes are &SYMBOL references resolved against the model being documented, so this is the
        // same "fill in the fields" step Session.CreateModel does, just on an existing model.
        JsonObject wantedParameters = params.getObject("parameters");
        JsonArray parameterResults = null;
        if (wantedParameters != null && !wantedParameters.isEmpty()) {
            parameterResults = applyParameters(model, wantedParameters);
        }

        // Expand config.pro's env tokens ($PRO_DIRECTORY, ...) and strip a trailing revision number,
        // same as Session.CreateModel, then use the bare file name -- CreateDrawingFromTemplate
        // resolves a standard template through Creo's own template search convention and rejects a
        // full path outright (XStringTooLong), so the directory half is discarded rather than acted
        // on the way Session.CreateModel's inDirectory does.
        String templateName = splitTemplatePath(ctx, template)[1];

        ModelDescriptor descr = model.GetDescr();
        Drawing drawing;
        String warning = null;
        try {
            // The fourth argument being `null` here, rather than an explicit empty
            // DrawingCreateOptions, was found live to crash the whole async connection outright
            // instead of raising an ordinary jxthrowable -- the same null-into-native-call failure
            // mode as BaseSession.ListFiles, just for a data-object argument instead of a String.
            // DRAWINGCREATE_PROMPT_UNKNOWN_PARAMS is deliberately left out: this call has no user to
            // prompt, so a template whose format needs unresolved parameters filled in should report
            // that as an error (XToolkitDrawingCreateErrors, caught below) rather than try to interact.
            drawing = ctx.session().CreateDrawingFromTemplate(
                    newName, templateName, descr, DrawingCreateOptions.create());
        } catch (jxthrowable e) {
            // Creo can throw here and still have created the drawing -- observed live with
            // XToolkitDrawingCreateErrors, where the drawing existed in session immediately
            // afterwards despite the exception. Recover it rather than reporting a hard failure that
            // leaves the caller unaware their drawing exists.
            Model found = null;
            try {
                found = ctx.resolveModel(newName);
            } catch (jxthrowable | RuntimeException ignored) {
                // Falls through to the hard failure below.
            }
            if (!(found instanceof Drawing)) {
                throw new CommandException(
                        "Could not create drawing '" + newName + "' from template '" + template
                                + "': " + rootMessage(e), "creo_error", e);
            }
            drawing = (Drawing) found;
            warning = "Creo reported an error creating this drawing, but it was created anyway: "
                    + rootMessage(e) + ". A template whose format has parameters nothing filled in "
                    + "commonly causes this; check the drawing before relying on it.";
        }
        if (drawing == null) {
            throw new CommandException("Creo returned no drawing for '" + newName + "'", "creo_error");
        }

        JsonObject out = JsonObject.of(
                "template", template,
                "templateFrom", templateFrom);
        out.putIfPresent("parameters", parameterResults);
        out.putIfPresent("warning", warning);
        out.putIfPresent("documents", modelRef(ctx, model));
        out.putIfPresent("drawing", modelRef(ctx, drawing));

        if (params.getBoolean("save", true)) {
            try {
                drawing.Save();
                out.put("saved", Boolean.TRUE);
            } catch (jxthrowable | RuntimeException e) {
                out.put("saved", Boolean.FALSE);
                out.put("saveError", rootMessage(e));
            }
        }
        return out;
    }
}
