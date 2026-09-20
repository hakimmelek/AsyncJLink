package com.asyncjlink.commands.compositecommands.drawing;

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
 * Drawing.SetModels — add or remove the models a drawing documents, in bulk.
 *
 * <p>Adding models one at a time is a single {@code Model2D.AddModel} call each — real and already
 * generated — but a drawing documenting several components (a weldment's individual parts, say)
 * means N round trips for what is one intent. Listing alone needs no composite: {@code
 * Model2D.ListModels} already returns everything in one call.
 */
public final class DrawingSetModelsCommand extends Composite {

    @Override public String name() { return "Drawing.SetModels"; }
    @Override public String receiverType() { return "Drawing"; }
    @Override public String signature() { return "SetModels(target[, add, remove, dryRun]) — composite"; }

    @Override
    public String description() {
        return "Add or remove the models a drawing documents, in bulk, and confirm what is now on "
                + "it. Supports dryRun.";
    }

    @Override
    public JsonSchema paramSchema() {
        return targeted(name(), "Drawing", "The drawing to change.")
                .optional("add", JsonSchema.array(JsonSchema.string()),
                        "Models to add, by name or handle.")
                .optional("remove", JsonSchema.array(JsonSchema.string()),
                        "Models to remove, by name or handle.")
                .optional("dryRun", JsonSchema.bool(),
                        "Report what would change without writing. Default false.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Model drawingModel = requireModel(ctx, params);
        if (!(drawingModel instanceof Model2D)) {
            throw new CommandException(
                    "Field 'target' must be a drawing, but was a " + typeOf(drawingModel),
                    "invalid_params");
        }
        Model2D drawing = (Model2D) drawingModel;

        JsonArray toAdd = params.getArray("add");
        JsonArray toRemove = params.getArray("remove");
        if ((toAdd == null || toAdd.size() == 0) && (toRemove == null || toRemove.size() == 0)) {
            throw new CommandException("Pass 'add' and/or 'remove'", "invalid_params");
        }
        boolean dryRun = dryRun(params);

        JsonArray added = new JsonArray();
        JsonArray removed = new JsonArray();
        JsonArray failed = new JsonArray();

        if (toAdd != null) {
            for (int i = 0; i < toAdd.size(); i++) {
                String ref = String.valueOf(toAdd.get(i));
                Model model = ctx.resolveModel(ref);
                if (model == null) {
                    failed.add(JsonObject.of("model", ref, "action", "add",
                            "reason", "no such model in session"));
                    continue;
                }
                if (dryRun) {
                    added.add(JsonObject.of("model", ref, "action", "wouldAdd"));
                    continue;
                }
                try {
                    drawing.AddModel(model);
                    added.add(modelRef(ctx, model));
                } catch (jxthrowable | RuntimeException e) {
                    failed.add(JsonObject.of("model", ref, "action", "add", "reason", rootMessage(e)));
                }
            }
        }

        if (toRemove != null) {
            for (int i = 0; i < toRemove.size(); i++) {
                String ref = String.valueOf(toRemove.get(i));
                Model model = ctx.resolveModel(ref);
                if (model == null) {
                    failed.add(JsonObject.of("model", ref, "action", "remove",
                            "reason", "no such model in session"));
                    continue;
                }
                if (dryRun) {
                    removed.add(JsonObject.of("model", ref, "action", "wouldRemove"));
                    continue;
                }
                try {
                    drawing.DeleteModel(model);
                    removed.add(modelRef(ctx, model));
                } catch (jxthrowable | RuntimeException e) {
                    failed.add(JsonObject.of("model", ref, "action", "remove", "reason", rootMessage(e)));
                }
            }
        }

        JsonObject out = JsonObject.of(
                "drawing", modelRef(ctx, drawing),
                "dryRun", Boolean.valueOf(dryRun),
                "added", added,
                "removed", removed);
        if (failed.size() > 0) {
            out.put("failed", failed);
        }
        out.putIfPresent("documents", currentModels(ctx, drawing));
        return out;
    }

    private static JsonArray currentModels(CreoContext ctx, Model2D drawing) {
        try {
            Models list = drawing.ListModels();
            JsonArray out = new JsonArray();
            if (list != null) {
                for (int i = 0; i < list.getarraysize(); i++) {
                    Model m = list.get(i);
                    if (m != null) {
                        out.add(modelRef(ctx, m));
                    }
                }
            }
            return out;
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }
}
