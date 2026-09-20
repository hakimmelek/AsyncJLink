package com.asyncjlink.commands.compositecommands.features;

import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcFeature.Feature;
import com.ptc.pfc.pfcFeature.FeatureOperation;
import com.ptc.pfc.pfcFeature.SuppressOperation;

/** Solid.SuppressFeatures — suppress features by name, id or pattern, then regenerate and report. */
public final class SolidSuppressFeaturesCommand extends FeatureOpCommand {

    @Override public String name() { return "Solid.SuppressFeatures"; }
    @Override public String signature() { return "SuppressFeatures(target, features|namePattern) — composite"; }

    @Override
    public String description() {
        return "Suppress features by name, id or name pattern, handling the suppress-operation "
                + "machinery, then regenerate and report what it affected. Supports dryRun.";
    }

    @Override
    public JsonSchema paramSchema() {
        return featureSelectionSchema()
                .optional("clip", JsonSchema.bool(),
                        "Also suppress everything after these features. Default false.")
                .optional("allowGroupMembers", JsonSchema.bool(),
                        "Permit suppressing individual members of a group. Default false.")
                .optional("allowChildGroupMembers", JsonSchema.bool(),
                        "Permit suppressing child group members. Default false.");
    }

    @Override
    protected String pastTense() {
        return "suppressed";
    }

    @Override
    protected FeatureOperation operationFor(Feature feature, JsonObject params) throws jxthrowable {
        SuppressOperation op = feature.CreateSuppressOp();
        if (op == null) {
            return null;
        }
        op.SetClip(Boolean.valueOf(params.getBoolean("clip", false)));
        op.SetAllowGroupMembers(Boolean.valueOf(params.getBoolean("allowGroupMembers", false)));
        op.SetAllowChildGroupMembers(
                Boolean.valueOf(params.getBoolean("allowChildGroupMembers", false)));
        return op;
    }
}
