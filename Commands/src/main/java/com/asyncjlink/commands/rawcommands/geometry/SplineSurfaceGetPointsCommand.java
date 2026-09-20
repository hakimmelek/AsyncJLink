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
import com.ptc.pfc.pfcGeometry.SplineSurface;

/**
 * SplineSurface.GetPoints &mdash; pfcGeometry.
 *
 * <pre>
 * SplineSurfacePoints GetPoints() throws jxthrowable
 * </pre>
 */
public final class SplineSurfaceGetPointsCommand implements Command {

    @Override public String name() { return "SplineSurface.GetPoints"; }
    @Override public String jlinkPackage() { return "pfcGeometry"; }
    @Override public String receiverType() { return "SplineSurface"; }
    @Override public String signature() { return "SplineSurfacePoints GetPoints() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("SplineSurface.GetPoints \u2014 pfcGeometry")
                .required("target", JsonSchema.handle("SplineSurface"),
                        "The SplineSurface to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        SplineSurface target = Marshal.in(ctx, params.get("target"),
                "SplineSurface", SplineSurface.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetPoints(), "SplineSurfacePoints");
    }
}
