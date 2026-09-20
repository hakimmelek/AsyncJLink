/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.geometry;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcGeometry.Quilt;

/**
 * Quilt.GetFeature &mdash; pfcGeometry.
 *
 * <pre>
 * Feature GetFeature() throws jxthrowable
 * </pre>
 */
public final class QuiltGetFeatureCommand implements Command {

    @Override public String name() { return "Quilt.GetFeature"; }
    @Override public String jlinkPackage() { return "pfcGeometry"; }
    @Override public String receiverType() { return "Quilt"; }
    @Override public String signature() { return "Feature GetFeature() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Quilt.GetFeature \u2014 pfcGeometry")
                .required("target", JsonSchema.handle("Quilt"),
                        "The Quilt to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Quilt target = Marshal.in(ctx, params.get("target"),
                "Quilt", Quilt.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetFeature(), "Feature");
    }
}
