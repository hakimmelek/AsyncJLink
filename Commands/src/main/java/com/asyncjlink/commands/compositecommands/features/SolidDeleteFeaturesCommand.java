package com.asyncjlink.commands.compositecommands.features;

import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcFeature.DeleteOperation;
import com.ptc.pfc.pfcFeature.Feature;
import com.ptc.pfc.pfcFeature.FeatureOperation;

/**
 * Solid.DeleteFeatures — remove features permanently.
 *
 * <p>{@code dryRun} defaults to <em>true</em> here, unlike every other command. There is no undo
 * anywhere in J-Link, so a mistaken delete is only recoverable from a backup taken beforehand
 * ({@code Model.CreateBackup}). Making the caller ask twice is worth the friction.
 */
public final class SolidDeleteFeaturesCommand extends FeatureOpCommand {

    @Override public String name() { return "Solid.DeleteFeatures"; }
    @Override public String signature() { return "DeleteFeatures(target, features|namePattern, dryRun=false) — composite"; }

    @Override
    public String description() {
        return "Permanently delete features by name, id or pattern. dryRun defaults to TRUE — pass "
                + "dryRun=false to actually delete. There is no undo; back up first with "
                + "Model.CreateBackup.";
    }

    @Override
    public JsonSchema paramSchema() {
        return featureSelectionSchema()
                .optional("clip", JsonSchema.bool(),
                        "Also delete everything after these features. Default false.")
                .optional("keepEmbeddedDatums", JsonSchema.bool(),
                        "Keep datums embedded in the deleted features. Default false.")
                .optional("allowGroupMembers", JsonSchema.bool(),
                        "Permit deleting individual members of a group. Default false.");
    }

    @Override
    protected boolean destructive() {
        return true;
    }

    @Override
    protected String pastTense() {
        return "deleted";
    }

    @Override
    protected String verb() {
        return "delete";
    }

    @Override
    protected FeatureOperation operationFor(Feature feature, JsonObject params) throws jxthrowable {
        DeleteOperation op = feature.CreateDeleteOp();
        if (op == null) {
            return null;
        }
        op.SetClip(Boolean.valueOf(params.getBoolean("clip", false)));
        op.SetKeepEmbeddedDatums(Boolean.valueOf(params.getBoolean("keepEmbeddedDatums", false)));
        op.SetAllowGroupMembers(Boolean.valueOf(params.getBoolean("allowGroupMembers", false)));
        return op;
    }
}
