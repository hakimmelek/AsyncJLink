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
import com.ptc.pfc.pfcGeometry.Arrow;

/**
 * Arrow.GetEnd1 &mdash; pfcGeometry.
 *
 * <pre>
 * Point3D GetEnd1() throws jxthrowable
 * </pre>
 */
public final class ArrowGetEnd1Command implements Command {

    @Override public String name() { return "Arrow.GetEnd1"; }
    @Override public String jlinkPackage() { return "pfcGeometry"; }
    @Override public String receiverType() { return "Arrow"; }
    @Override public String signature() { return "Point3D GetEnd1() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Arrow.GetEnd1 \u2014 pfcGeometry")
                .required("target", JsonSchema.handle("Arrow"),
                        "The Arrow to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Arrow target = Marshal.in(ctx, params.get("target"),
                "Arrow", Arrow.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetEnd1(), "Point3D");
    }
}
