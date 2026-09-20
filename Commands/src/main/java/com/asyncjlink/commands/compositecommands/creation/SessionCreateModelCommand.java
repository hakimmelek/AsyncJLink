package com.asyncjlink.commands.compositecommands.creation;

import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModel.ModelDescriptor;
import com.ptc.pfc.pfcModel.pfcModel;

/**
 * Session.CreateModel — a new part or assembly, from the site's template.
 *
 * <p>This command exists to work around a real gap in J-Link.
 * {@code BaseSession.CreatePart(String)} and {@code BaseSession.CreateAssembly(String)} take a name
 * and nothing else — there is <em>no</em> template parameter. A model created that way is empty: no
 * default datums, no standard parameters, no layers, none of the site's conventions. Only drawings
 * get native template support, through {@code CreateDrawingFromTemplate}.
 *
 * <p>So this reproduces what Creo's own New dialog does: resolve the template from configuration,
 * retrieve it, and copy it under the new name. Which template was used is reported, because a
 * silently wrong template is hard to notice and expensive later.
 */
public final class SessionCreateModelCommand extends Composite {

    private static final String PART_TEMPLATE_OPTION = "template_solidpart";
    private static final String ASSEMBLY_TEMPLATE_OPTION = "template_designasm";

    @Override public String name() { return "Session.CreateModel"; }
    @Override public String receiverType() { return "Session"; }
    @Override public String signature() { return "CreateModel(name, type[, template, parameters]) — composite"; }

    @Override
    public String description() {
        return "Create a part or assembly from the site's template (J-Link's CreatePart/CreateAssembly "
                + "take no template, so this copies the configured one) and optionally set parameters.";
    }

    @Override
    public JsonSchema paramSchema() {
        return sessionScoped(name())
                .required("name", JsonSchema.string(),
                        "Name for the new model, without extension.")
                .required("type", JsonSchema.enumOf("ModelType", "PART", "ASSEMBLY"),
                        "PART or ASSEMBLY.")
                .optional("template", JsonSchema.string(),
                        "Template model to copy. Defaults to the configured "
                                + PART_TEMPLATE_OPTION + " / " + ASSEMBLY_TEMPLATE_OPTION + ".")
                .optional("parameters", JsonSchema.dataObject("Parameters"),
                        "Parameters to set on the new model, as name/value pairs.")
                .optional("save", JsonSchema.bool(), "Save the new model. Default true.")
                .optional("display", JsonSchema.bool(), "Open and display it. Default false.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        String newName = params.getString("name", null);
        if (newName == null || newName.isEmpty()) {
            throw new CommandException("Field 'name' is required", "invalid_params");
        }
        String type = params.getString("type", "PART").trim().toUpperCase(java.util.Locale.ROOT);
        boolean assembly = type.startsWith("ASSEM") || type.equals("MDL_ASSEMBLY");

        JsonObject out = JsonObject.of("requestedName", newName, "type", assembly ? "ASSEMBLY" : "PART");

        String template = params.getString("template", null);
        if (template == null || template.isEmpty()) {
            String option = assembly ? ASSEMBLY_TEMPLATE_OPTION : PART_TEMPLATE_OPTION;
            template = configOption(ctx, option);
            out.putIfPresent("templateFrom", template == null ? null : option);
        } else {
            out.put("templateFrom", "argument");
        }

        Model created;
        if (template != null && !template.isEmpty()) {
            out.put("template", template);
            created = fromTemplate(ctx, template, newName);
        } else {
            // No template configured and none given. Creo's own bare create is the honest fallback,
            // but the caller needs to know the model will not carry the site's standards.
            out.put("template", null);
            out.put("warning", "No template was configured or supplied, so the model was created "
                    + "empty: no default datums, parameters or layers. Set "
                    + (assembly ? ASSEMBLY_TEMPLATE_OPTION : PART_TEMPLATE_OPTION)
                    + " in config.pro, or pass 'template'.");
            created = assembly
                    ? ctx.session().CreateAssembly(newName)
                    : ctx.session().CreatePart(newName);
        }

        if (created == null) {
            throw new CommandException("Creo returned no model for '" + newName + "'", "creo_error");
        }
        out.putIfPresent("model", modelRef(ctx, created));

        JsonObject wanted = params.getObject("parameters");
        if (wanted != null && !wanted.isEmpty()) {
            out.put("parameters", applyParameters(created, wanted));
        }

        if (params.getBoolean("display", false)) {
            created.Display();
            out.put("displayed", Boolean.TRUE);
        }
        if (params.getBoolean("save", true)) {
            try {
                created.Save();
                out.put("saved", Boolean.TRUE);
            } catch (jxthrowable | RuntimeException e) {
                out.put("saved", Boolean.FALSE);
                out.put("saveError", rootMessage(e));
            }
        }
        return out;
    }

    private static String configOption(CreoContext ctx, String option) {
        try {
            String v = ctx.session().GetConfigOption(option);
            return v == null || v.isEmpty() ? null : v;
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }

    private static Model fromTemplate(CreoContext ctx, String template, String newName)
            throws jxthrowable {
        String[] split = splitTemplatePath(ctx, template);
        String directory = split[0];
        String bareName = split[1];
        return inDirectory(ctx, directory, () -> {
            Model source = ctx.session().GetModelFromFileName(bareName);
            if (source == null) {
                ModelDescriptor descr = pfcModel.ModelDescriptor_CreateFromFileName(bareName);
                try {
                    source = ctx.session().RetrieveModel(descr);
                } catch (jxthrowable e) {
                    throw new CommandException(
                            "Could not retrieve template '" + bareName + "': " + rootMessage(e)
                                    + ". Check that it is on Creo's search path.",
                            "creo_error", e);
                }
            }
            if (source == null) {
                throw new CommandException(
                        "Template '" + bareName + "' could not be loaded", "creo_error");
            }
            try {
                return source.CopyAndRetrieve(newName, null);
            } catch (jxthrowable e) {
                throw new CommandException(
                        "Could not copy template '" + bareName + "' to '" + newName + "': "
                                + rootMessage(e), "creo_error", e);
            }
        });
    }
}
