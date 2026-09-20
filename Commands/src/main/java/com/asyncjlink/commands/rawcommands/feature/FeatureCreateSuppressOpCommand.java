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
import com.ptc.pfc.pfcFeature.Feature;

/**
 * Feature.CreateSuppressOp &mdash; pfcFeature.
 *
 * <pre>
 * SuppressOperation CreateSuppressOp() throws jxthrowable
 * </pre>
 */
public final class FeatureCreateSuppressOpCommand implements Command {

    @Override public String name() { return "Feature.CreateSuppressOp"; }
    @Override public String jlinkPackage() { return "pfcFeature"; }
    @Override public String receiverType() { return "Feature"; }
    @Override public String signature() { return "SuppressOperation CreateSuppressOp() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Feature.CreateSuppressOp \u2014 pfcFeature")
                .required("target", JsonSchema.handle("Feature"),
                        "The Feature to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Feature target = Marshal.in(ctx, params.get("target"),
                "Feature", Feature.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.CreateSuppressOp(), "SuppressOperation");
    }
}
