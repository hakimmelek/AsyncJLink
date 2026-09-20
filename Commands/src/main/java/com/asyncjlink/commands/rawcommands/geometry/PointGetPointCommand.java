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
import com.ptc.pfc.pfcGeometry.Point;

/**
 * Point.GetPoint &mdash; pfcGeometry.
 *
 * <pre>
 * Point3D GetPoint() throws jxthrowable
 * </pre>
 */
public final class PointGetPointCommand implements Command {

    @Override public String name() { return "Point.GetPoint"; }
    @Override public String jlinkPackage() { return "pfcGeometry"; }
    @Override public String receiverType() { return "Point"; }
    @Override public String signature() { return "Point3D GetPoint() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Point.GetPoint \u2014 pfcGeometry")
                .required("target", JsonSchema.handle("Point"),
                        "The Point to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Point target = Marshal.in(ctx, params.get("target"),
                "Point", Point.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetPoint(), "Point3D");
    }
}
