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
import com.ptc.pfc.pfcGeometry.Line;

/**
 * Line.GetEnd1 &mdash; pfcGeometry.
 *
 * <pre>
 * Point3D GetEnd1() throws jxthrowable
 * </pre>
 */
public final class LineGetEnd1Command implements Command {

    @Override public String name() { return "Line.GetEnd1"; }
    @Override public String jlinkPackage() { return "pfcGeometry"; }
    @Override public String receiverType() { return "Line"; }
    @Override public String signature() { return "Point3D GetEnd1() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Line.GetEnd1 \u2014 pfcGeometry")
                .required("target", JsonSchema.handle("Line"),
                        "The Line to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Line target = Marshal.in(ctx, params.get("target"),
                "Line", Line.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetEnd1(), "Point3D");
    }
}
