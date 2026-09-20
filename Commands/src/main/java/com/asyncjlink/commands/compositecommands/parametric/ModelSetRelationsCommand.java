package com.asyncjlink.commands.compositecommands.parametric;

import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonArray;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcSolid.Solid;

import java.util.ArrayList;
import java.util.List;

/**
 * Model.SetRelations — write a model's relations.
 *
 * <p>Relations are replaced wholesale by the underlying API, so the previous set is always returned:
 * without it, an accidental overwrite is unrecoverable, and there is no undo anywhere in J-Link.
 */
public final class ModelSetRelationsCommand extends Composite {

    @Override public String name() { return "Model.SetRelations"; }
    @Override public String receiverType() { return "Model"; }
    @Override public String signature() { return "SetRelations(target, relations[, append, dryRun]) — composite"; }

    @Override
    public String description() {
        return "Replace or append a model's relations, returning the previous set so the change can "
                + "be reversed by hand. Regenerates and reports afterwards. Supports dryRun.";
    }

    @Override
    public JsonSchema paramSchema() {
        return targeted(name(), "Model", "The model whose relations to write.")
                .required("relations", JsonSchema.array(JsonSchema.string()),
                        "Relation lines, e.g. [\"d12 = d5 * 2\"].")
                .optional("append", JsonSchema.bool(),
                        "Append to the existing relations instead of replacing them. Default false.")
                .optional("postRegeneration", JsonSchema.bool(),
                        "Write the post-regeneration relation set instead. Default false.")
                .optional("dryRun", JsonSchema.bool(),
                        "Report what would change without writing. Default false.")
                .optional("regenerate", JsonSchema.bool(),
                        "Regenerate after writing. Default true.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Model model = requireModel(ctx, params);
        JsonArray asked = params.getArray("relations");
        if (asked == null) {
            throw new CommandException(
                    "Field 'relations' must be an array of relation lines", "invalid_params");
        }
        boolean append = params.getBoolean("append", false);
        boolean post = params.getBoolean("postRegeneration", false);
        boolean dryRun = dryRun(params);

        List<String> before = post ? current(model, true) : current(model, false);

        List<String> next = new ArrayList<>();
        if (append) {
            next.addAll(before);
        }
        for (int i = 0; i < asked.size(); i++) {
            Object line = asked.get(i);
            next.add(line == null ? "" : String.valueOf(line));
        }

        JsonObject out = JsonObject.of(
                "model", modelRef(ctx, model),
                "dryRun", Boolean.valueOf(dryRun),
                "set", post ? "postRegeneration" : "relations",
                "before", toJsonArray(before),
                "after", toJsonArray(next));

        if (dryRun) {
            return out;
        }

        try {
            if (post) {
                model.SetPostRegenerationRelations(toStringseq(toJsonArray(next)));
            } else {
                model.SetRelations(toStringseq(toJsonArray(next)));
            }
        } catch (jxthrowable e) {
            throw new CommandException(
                    "Creo rejected the relations: " + rootMessage(e)
                            + ". The previous set is unchanged.", "creo_error", e);
        }
        out.put("written", Boolean.TRUE);

        if (params.getBoolean("regenerate", true) && model instanceof Solid) {
            JsonObject regen = regenerate(ctx, (Solid) model);
            out.put("regeneration", regen);
            boolean ok = regen.getBoolean("regenerated", false)
                    && regen.getInt("failedFeatureCount", 0) == 0;
            out.put("ok", Boolean.valueOf(ok));
            if (!ok) {
                out.put("warning", "The model did not come back clean. The previous relations are "
                        + "in 'before' if you need to put them back.");
            }
        }
        return out;
    }

    private static List<String> current(Model model, boolean post) throws jxthrowable {
        try {
            return post
                    ? strings(model.GetPostRegenerationRelations())
                    : strings(model.GetRelations());
        } catch (jxthrowable | RuntimeException e) {
            return new ArrayList<>();
        }
    }
}
