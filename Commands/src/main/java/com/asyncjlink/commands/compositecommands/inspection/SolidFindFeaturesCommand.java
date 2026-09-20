package com.asyncjlink.commands.compositecommands.inspection;

import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Enums;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonArray;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcFeature.Feature;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcSolid.Solid;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Solid.FindFeatures — filtered feature search.
 *
 * <p>A narrower {@code Model.FindItems}, kept separate because feature status — failed, suppressed —
 * is the common thing being looked for and deserves a first-class filter.
 */
public final class SolidFindFeaturesCommand extends Composite {

    @Override public String name() { return "Solid.FindFeatures"; }
    @Override public String receiverType() { return "Solid"; }
    @Override public String signature() { return "FindFeatures(target[, namePattern, status, featureType]) — composite"; }

    @Override
    public String description() {
        return "Find features by name pattern, status (e.g. FEAT_SUPPRESSED) or feature type. "
                + "Use status to locate everything failed or suppressed in a model.";
    }

    @Override
    public JsonSchema paramSchema() {
        return targeted(name(), "Solid", "The part or assembly to search.")
                .optional("namePattern", JsonSchema.string(),
                        "Name filter; * and ? are wildcards.")
                .optional("status", JsonSchema.string(),
                        "FeatureStatus constant to match, e.g. FEAT_SUPPRESSED or FEAT_ACTIVE.")
                .optional("featureType", JsonSchema.string(),
                        "FeatureType constant to match, e.g. FEATTYPE_HOLE.")
                .optional("problemsOnly", JsonSchema.bool(),
                        "Only features that are not active. Default false.")
                .optional("includeType", JsonSchema.bool(),
                        "Report each feature's type. Costs one extra Creo round trip per feature; "
                                + "default false. Implied when featureType is given.")
                .optional("limit", JsonSchema.integer(),
                        "Stop after this many matches. Default 200.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Solid solid = requireSolid(ctx, params);
        Pattern pattern = glob(params.getString("namePattern", null));
        String wantStatus = params.getString("status", null);
        String wantType = params.getString("featureType", null);
        boolean problemsOnly = params.getBoolean("problemsOnly", false);

        boolean includeType = wantType != null || params.getBoolean("includeType", false);
        int limit = params.getInt("limit", 200);

        List<Feature> all = features(solid);
        JsonArray out = new JsonArray();
        boolean truncated = false;

        // Ordered cheapest-filter-first on purpose: every accessor below is a round trip to Creo,
        // and this loop runs once per feature in the model.
        for (Feature f : all) {
            if (out.size() >= limit) {
                truncated = true;
                break;
            }
            String status = featureStatus(f);
            if (wantStatus != null && !wantStatus.equalsIgnoreCase(status)) {
                continue;
            }
            if (problemsOnly && (status == null || "FEAT_ACTIVE".equals(status))) {
                continue;
            }
            String fname = safeName(f);
            if (!matches(pattern, fname)) {
                continue;
            }
            String ftype = includeType ? featureType(f) : null;
            if (wantType != null && !wantType.equalsIgnoreCase(ftype)) {
                continue;
            }

            JsonObject entry = itemRef(ctx, f, fname);
            entry.putIfPresent("status", status);
            entry.putIfPresent("featureType", ftype);
            out.add(entry);
        }

        JsonObject result = JsonObject.of(
                "model", modelRef(ctx, (Model) solid),
                "matched", Integer.valueOf(out.size()),
                "scanned", Integer.valueOf(all.size()),
                "features", out);
        if (truncated) {
            result.put("truncated", Boolean.TRUE);
        }
        return result;
    }

    private static String featureType(Feature f) {
        try {
            return Enums.toJson(f.GetFeatType());
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }
}
