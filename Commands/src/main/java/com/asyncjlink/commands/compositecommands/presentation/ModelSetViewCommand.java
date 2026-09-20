package com.asyncjlink.commands.compositecommands.presentation;

import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcBase.ScreenTransform;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcWindow.Window;

/**
 * Model.SetView — pan and zoom the model in its window.
 *
 * <p>Reachable through {@code Window.SetScreenTransform}, which is also what lets
 * {@code Model.CaptureImage} capture a chosen view rather than whatever happens to be on screen.
 */
public final class ModelSetViewCommand extends Composite {

    @Override public String name() { return "Model.SetView"; }
    @Override public String receiverType() { return "Model"; }
    @Override public String signature() { return "SetView(target[, zoom, panX, panY]) — composite"; }

    @Override
    public String description() {
        return "Set the model's on-screen zoom and pan, then repaint. Use before Model.CaptureImage "
                + "to control what the captured image shows.";
    }

    @Override
    public JsonSchema paramSchema() {
        return targeted(name(), "Model", "The model whose window to adjust.")
                .optional("zoom", JsonSchema.number(), "Zoom factor. Omit to leave unchanged.")
                .optional("panX", JsonSchema.number(), "Horizontal pan. Omit to leave unchanged.")
                .optional("panY", JsonSchema.number(), "Vertical pan. Omit to leave unchanged.")
                .optional("repaint", JsonSchema.bool(), "Repaint afterwards. Default true.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Model model = requireModel(ctx, params);
        Window window = ctx.session().GetModelWindow(model);
        if (window == null) {
            throw new CommandException(
                    "This model has no open window. Load it with Session.LoadModel first.",
                    "invalid_params");
        }

        ScreenTransform transform = window.GetScreenTransform();
        if (transform == null) {
            throw new CommandException(
                    "Creo returned no screen transform for this window", "creo_error");
        }

        JsonObject before = describe(transform);

        boolean changed = false;
        if (params.has("zoom")) {
            transform.SetZoom(Double.valueOf(params.getDouble("zoom", 1.0)));
            changed = true;
        }
        if (params.has("panX")) {
            transform.SetPanX(Double.valueOf(params.getDouble("panX", 0.0)));
            changed = true;
        }
        if (params.has("panY")) {
            transform.SetPanY(Double.valueOf(params.getDouble("panY", 0.0)));
            changed = true;
        }

        if (changed) {
            window.SetScreenTransform(transform);
            if (params.getBoolean("repaint", true)) {
                window.Repaint();
            }
        }

        return JsonObject.of(
                "model", modelRef(ctx, model),
                "changed", Boolean.valueOf(changed),
                "before", before,
                "after", describe(window.GetScreenTransform()));
    }

    private static JsonObject describe(ScreenTransform t) {
        if (t == null) {
            return null;
        }
        JsonObject o = new JsonObject();
        try {
            o.putIfPresent("zoom", t.GetZoom());
            o.putIfPresent("panX", t.GetPanX());
            o.putIfPresent("panY", t.GetPanY());
        } catch (jxthrowable | RuntimeException ignored) {
            // A transform that will not describe itself is still usable for setting.
        }
        return o;
    }
}
