package com.asyncjlink.commands.compositecommands.presentation;

import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonArray;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModel.Models;
import com.ptc.pfc.pfcModel2D.Model2D;

/**
 * Drawing.CheckUpToDate — whether a drawing still matches the solid it documents.
 *
 * <p>Stale drawings reaching a supplier are expensive and invisible. Creo exposes no direct
 * "drawing out of date" flag, so this is inferred from what <em>is</em> observable: whether the
 * drawing or any model it draws has unsaved changes. That is a weaker signal than a real flag and
 * the result says so, rather than implying a certainty the API cannot provide.
 */
public final class DrawingCheckUpToDateCommand extends Composite {

    @Override public String name() { return "Drawing.CheckUpToDate"; }
    @Override public String receiverType() { return "Drawing"; }
    @Override public String signature() { return "CheckUpToDate(target) — composite"; }

    @Override
    public String description() {
        return "Check whether a drawing still matches the models it documents, inferred from "
                + "modification state. Creo exposes no direct out-of-date flag, so this is "
                + "indicative rather than definitive.";
    }

    @Override
    public JsonSchema paramSchema() {
        return targeted(name(), "Drawing", "The drawing to check.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Model model = requireModel(ctx, params);
        if (!(model instanceof Model2D)) {
            throw new CommandException(
                    "Field 'target' must be a drawing, but was a " + typeOf(model),
                    "invalid_params");
        }
        Model2D drawing = (Model2D) model;

        JsonObject out = JsonObject.of("drawing", modelRef(ctx, model));
        out.put("method", "inferred from modification state; Creo exposes no out-of-date flag");

        boolean drawingModified = false;
        try {
            drawingModified = model.GetIsModified();
        } catch (jxthrowable | RuntimeException ignored) {
            // Treated as unmodified; the per-model results below still carry signal.
        }
        out.put("drawingModified", Boolean.valueOf(drawingModified));

        JsonArray documented = new JsonArray();
        boolean anyModelModified = false;

        Models drawn = safeListModels(drawing);
        if (drawn != null) {
            for (int i = 0; i < drawn.getarraysize(); i++) {
                Model m = drawn.get(i);
                if (m == null) {
                    continue;
                }
                JsonObject entry = modelRef(ctx, m);
                boolean modified = false;
                try {
                    modified = m.GetIsModified();
                } catch (jxthrowable | RuntimeException ignored) {
                    // As above.
                }
                entry.put("modified", Boolean.valueOf(modified));
                anyModelModified = anyModelModified || modified;
                documented.add(entry);
            }
        }
        out.put("documents", documented);

        try {
            out.putIfPresent("currentSolid", modelRef(ctx, drawing.GetCurrentSolid()));
        } catch (jxthrowable | RuntimeException ignored) {
            // A drawing with no current solid is unusual but not an error here.
        }

        try {
            out.put("viewCount", Integer.valueOf(drawing.List2DViews().getarraysize()));
        } catch (jxthrowable | RuntimeException ignored) {
            // View count is informational only.
        }

        boolean suspect = anyModelModified;
        out.put("upToDate", Boolean.valueOf(!suspect));
        if (suspect) {
            out.put("warning", "At least one documented model has unsaved changes, so this drawing "
                    + "may not reflect it. Regenerate the drawing and save both before releasing.");
        }
        return out;
    }

    private static Models safeListModels(Model2D drawing) {
        try {
            return drawing.ListModels();
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }
}
