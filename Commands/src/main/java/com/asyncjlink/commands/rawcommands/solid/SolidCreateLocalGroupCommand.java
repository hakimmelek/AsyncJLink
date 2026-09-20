/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.solid;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcFeature.Features;
import com.ptc.pfc.pfcSolid.Solid;

/**
 * Solid.CreateLocalGroup &mdash; pfcSolid.
 *
 * <pre>
 * FeatureGroup CreateLocalGroup(Features, String) throws jxthrowable
 * </pre>
 */
public final class SolidCreateLocalGroupCommand implements Command {

    @Override public String name() { return "Solid.CreateLocalGroup"; }
    @Override public String jlinkPackage() { return "pfcSolid"; }
    @Override public String receiverType() { return "Solid"; }
    @Override public String signature() { return "FeatureGroup CreateLocalGroup(Features, String) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Solid.CreateLocalGroup \u2014 pfcSolid")
                .required("target", JsonSchema.handle("Solid"),
                        "The Solid to act on.")
                .optional("features", JsonSchema.sequence("Features", JsonSchema.handle("Feature")),
                        "Array of Feature.")
                .optional("value", JsonSchema.string(),
                        "String value.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Solid target = Marshal.in(ctx, params.get("target"),
                "Solid", Solid.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        Features features = Marshal.in(ctx, params.get("features"),
                "Features", Features.class, "features");
        String value = Marshal.in(ctx, params.get("value"),
                "String", String.class, "value");
        return Marshal.result(ctx, target.CreateLocalGroup(features, value), "FeatureGroup");
    }
}
