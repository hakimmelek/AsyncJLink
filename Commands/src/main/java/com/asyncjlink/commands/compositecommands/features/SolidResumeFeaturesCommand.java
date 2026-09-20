package com.asyncjlink.commands.compositecommands.features;

import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcFeature.Feature;
import com.ptc.pfc.pfcFeature.FeatureOperation;
import com.ptc.pfc.pfcFeature.ResumeOperation;

/** Solid.ResumeFeatures — bring suppressed features back, then regenerate and report. */
public final class SolidResumeFeaturesCommand extends FeatureOpCommand {

    @Override public String name() { return "Solid.ResumeFeatures"; }
    @Override public String signature() { return "ResumeFeatures(target, features|namePattern) — composite"; }

    @Override
    public String description() {
        return "Resume suppressed features by name, id or name pattern, optionally bringing their "
                + "parents back too, then regenerate and report. Supports dryRun.";
    }

    @Override
    public JsonSchema paramSchema() {
        return featureSelectionSchema()
                .optional("withParents", JsonSchema.bool(),
                        "Also resume any suppressed parents these features depend on. Default true, "
                                + "because resuming a feature without its parents usually fails.");
    }

    @Override
    protected String pastTense() {
        return "resumed";
    }

    @Override
    protected String verb() {
        return "resume";
    }

    @Override
    protected FeatureOperation operationFor(Feature feature, JsonObject params) throws jxthrowable {
        ResumeOperation op = feature.CreateResumeOp();
        if (op == null) {
            return null;
        }
        op.SetWithParents(Boolean.valueOf(params.getBoolean("withParents", true)));
        return op;
    }
}
