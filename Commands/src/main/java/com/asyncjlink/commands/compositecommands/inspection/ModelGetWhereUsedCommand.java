package com.asyncjlink.commands.compositecommands.inspection;

import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonArray;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcComponentFeat.ComponentFeat;
import com.ptc.pfc.pfcFeature.Feature;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModel.ModelDescriptor;
import com.ptc.pfc.pfcModel.Models;
import com.ptc.pfc.pfcSolid.Solid;

/**
 * Model.GetWhereUsed — which models in session reference this one.
 *
 * <p>Session-scoped by necessity: a full PDM where-used would need the server APIs and a connected
 * workspace. The result says so rather than implying it searched everywhere.
 */
public final class ModelGetWhereUsedCommand extends Composite {

    @Override public String name() { return "Model.GetWhereUsed"; }
    @Override public String receiverType() { return "Model"; }
    @Override public String signature() { return "GetWhereUsed(target) — composite"; }

    @Override
    public String description() {
        return "Find which assemblies currently in session use this model, and how many times. "
                + "Scoped to the session, not the whole PDM workspace.";
    }

    @Override
    public JsonSchema paramSchema() {
        return targeted(name(), "Model", "The model to look for.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Model target = requireModel(ctx, params);
        String targetName = target.GetFullName();

        JsonArray usedBy = new JsonArray();
        int scanned = 0;

        Models all = ctx.session().ListModels();
        if (all != null) {
            for (int i = 0; i < all.getarraysize(); i++) {
                Model candidate = all.get(i);
                if (!(candidate instanceof Solid) || candidate == target) {
                    continue;
                }
                scanned++;
                int uses = countUses(ctx, (Solid) candidate, targetName);
                if (uses > 0) {
                    JsonObject entry = modelRef(ctx, candidate);
                    entry.put("occurrences", Integer.valueOf(uses));
                    usedBy.add(entry);
                }
            }
        }

        return JsonObject.of(
                "model", modelRef(ctx, target),
                "scope", "session",
                "note", "Only models currently in session were searched; this is not a PDM "
                        + "where-used report.",
                "scanned", Integer.valueOf(scanned),
                "usedByCount", Integer.valueOf(usedBy.size()),
                "usedBy", usedBy);
    }

    private static int countUses(CreoContext ctx, Solid parent, String targetName) {
        if (targetName == null) {
            return 0;
        }
        int count = 0;
        try {
            for (Feature f : features(parent)) {
                if (!(f instanceof ComponentFeat)) {
                    continue;
                }
                try {
                    ModelDescriptor descr = ((ComponentFeat) f).GetModelDescr();
                    Model child = descr == null ? null : ctx.session().GetModelFromDescr(descr);
                    if (child != null && targetName.equalsIgnoreCase(child.GetFullName())) {
                        count++;
                    }
                } catch (jxthrowable | RuntimeException ignored) {
                    // A component whose model will not resolve simply is not counted.
                }
            }
        } catch (jxthrowable | RuntimeException e) {
            return count;
        }
        return count;
    }
}
