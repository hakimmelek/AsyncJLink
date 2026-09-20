package com.asyncjlink.commands.compositecommands.parametric;

import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonArray;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcModel.Model;

import java.util.List;

/**
 * Model.GetRelations — a model's relations, as text.
 *
 * <p>Relations round-trip as plain string sequences, which makes design intent directly readable and
 * editable. Pre- and post-regeneration sets are kept apart because they behave differently and
 * conflating them is a good way to lose one of them.
 */
public final class ModelGetRelationsCommand extends Composite {

    @Override public String name() { return "Model.GetRelations"; }
    @Override public String receiverType() { return "Model"; }
    @Override public String signature() { return "GetRelations(target) — composite"; }

    @Override
    public String description() {
        return "A model's relations as lines of text, with pre- and post-regeneration sets kept "
                + "separate. Design intent in editable form.";
    }

    @Override
    public JsonSchema paramSchema() {
        return targeted(name(), "Model", "The model whose relations to read.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Model model = requireModel(ctx, params);

        JsonObject out = JsonObject.of("model", modelRef(ctx, model));

        List<String> relations = safeRelations(model);
        out.put("relationCount", Integer.valueOf(relations.size()));
        out.put("relations", toJsonArray(relations));

        List<String> post = safePostRelations(model);
        out.put("postRegenerationCount", Integer.valueOf(post.size()));
        JsonArray postArray = toJsonArray(post);
        if (postArray.size() > 0) {
            out.put("postRegenerationRelations", postArray);
        }
        return out;
    }

    private static List<String> safeRelations(Model model) throws jxthrowable {
        try {
            return strings(model.GetRelations());
        } catch (jxthrowable | RuntimeException e) {
            return java.util.Collections.emptyList();
        }
    }

    private static List<String> safePostRelations(Model model) throws jxthrowable {
        try {
            return strings(model.GetPostRegenerationRelations());
        } catch (jxthrowable | RuntimeException e) {
            return java.util.Collections.emptyList();
        }
    }
}
