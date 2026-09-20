/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.feature;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcFeature.FeatureOperation;

/**
 * FeatureOperation.GetOpFeature &mdash; pfcFeature.
 *
 * <pre>
 * Feature GetOpFeature() throws jxthrowable
 * </pre>
 */
public final class FeatureOperationGetOpFeatureCommand implements Command {

    @Override public String name() { return "FeatureOperation.GetOpFeature"; }
    @Override public String jlinkPackage() { return "pfcFeature"; }
    @Override public String receiverType() { return "FeatureOperation"; }
    @Override public String signature() { return "Feature GetOpFeature() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("FeatureOperation.GetOpFeature \u2014 pfcFeature")
                .required("target", JsonSchema.handle("FeatureOperation"),
                        "The FeatureOperation to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        FeatureOperation target = Marshal.in(ctx, params.get("target"),
                "FeatureOperation", FeatureOperation.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetOpFeature(), "Feature");
    }
}
