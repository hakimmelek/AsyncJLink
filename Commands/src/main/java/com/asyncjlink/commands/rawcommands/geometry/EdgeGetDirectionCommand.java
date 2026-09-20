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
import com.ptc.pfc.pfcGeometry.Edge;
import com.ptc.pfc.pfcGeometry.Surface;

/**
 * Edge.GetDirection &mdash; pfcGeometry.
 *
 * <pre>
 * int GetDirection(Surface) throws jxthrowable
 * </pre>
 */
public final class EdgeGetDirectionCommand implements Command {

    @Override public String name() { return "Edge.GetDirection"; }
    @Override public String jlinkPackage() { return "pfcGeometry"; }
    @Override public String receiverType() { return "Edge"; }
    @Override public String signature() { return "int GetDirection(Surface) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Edge.GetDirection \u2014 pfcGeometry")
                .required("target", JsonSchema.handle("Edge"),
                        "The Edge to act on.")
                .optional("surface", JsonSchema.handle("Surface"),
                        "Handle to a Surface, as returned by an earlier command.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Edge target = Marshal.in(ctx, params.get("target"),
                "Edge", Edge.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        Surface surface = Marshal.in(ctx, params.get("surface"),
                "Surface", Surface.class, "surface");
        return Marshal.result(ctx, target.GetDirection(surface), "int");
    }
}
