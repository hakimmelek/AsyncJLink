package com.asyncjlink.commands.compositecommands.session;

import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModel.ModelDescriptor;
import com.ptc.pfc.pfcModel.pfcModel;
import com.ptc.pfc.pfcWindow.Window;

/**
 * Session.LoadModel — retrieve a model into session, and optionally show it.
 *
 * <p>The receiver is {@code Session} rather than {@code Model} on purpose: the model is not in
 * session yet, so there is nothing for {@code --target} to refer to. It takes a name instead. That
 * falls out of the {@code --target} rule rather than being an exception to it.
 */
public final class SessionLoadModelCommand extends Composite {

    @Override public String name() { return "Session.LoadModel"; }
    @Override public String receiverType() { return "Session"; }
    @Override public String signature() { return "LoadModel(name[, display, activate]) — composite"; }

    @Override
    public String description() {
        return "Retrieve a model into session by file name and optionally window, display and "
                + "activate it. Replaces the four-call retrieve/window/display/activate sequence.";
    }

    @Override
    public JsonSchema paramSchema() {
        return sessionScoped(name())
                .required("name", JsonSchema.string(),
                        "Model file name, e.g. \"bracket_01.prt\".")
                .optional("display", JsonSchema.bool(),
                        "Open a window and display the model. Default true.")
                .optional("activate", JsonSchema.bool(),
                        "Make it the current model. Default true when displayed.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        String fileName = params.getString("name", null);
        if (fileName == null || fileName.isEmpty()) {
            throw new CommandException("Field 'name' is required", "invalid_params");
        }

        JsonObject out = new JsonObject();

        Model existing = ctx.session().GetModelFromFileName(fileName);
        boolean alreadyLoaded = existing != null;
        Model model = existing;

        if (model == null) {
            ModelDescriptor descr = pfcModel.ModelDescriptor_CreateFromFileName(fileName);
            try {
                model = ctx.session().RetrieveModel(descr);
            } catch (jxthrowable e) {
                throw new CommandException(
                        "Could not retrieve '" + fileName + "': " + rootMessage(e)
                                + ". Check the name and that it is in the working directory "
                                + "(Session.ListDirectory shows what is there).",
                        "creo_error", e);
            }
        }
        if (model == null) {
            throw new CommandException(
                    "Creo returned no model for '" + fileName + "'", "creo_error");
        }

        out.put("alreadyLoaded", Boolean.valueOf(alreadyLoaded));
        out.putIfPresent("model", modelRef(ctx, model));

        boolean display = params.getBoolean("display", true);
        if (display) {
            Window window = ctx.session().GetModelWindow(model);
            if (window == null) {
                window = ctx.session().CreateModelWindow(model);
            }
            model.Display();
            if (params.getBoolean("activate", true) && window != null) {
                window.Activate();
                ctx.session().SetCurrentWindow(window);
            }
            out.put("displayed", Boolean.TRUE);
        } else {
            out.put("displayed", Boolean.FALSE);
        }
        return out;
    }
}
