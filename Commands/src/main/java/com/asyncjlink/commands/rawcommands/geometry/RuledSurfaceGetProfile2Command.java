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
import com.ptc.pfc.pfcGeometry.RuledSurface;

/**
 * RuledSurface.GetProfile2 &mdash; pfcGeometry.
 *
 * <pre>
 * CurveDescriptor GetProfile2() throws jxthrowable
 * </pre>
 */
public final class RuledSurfaceGetProfile2Command implements Command {

    @Override public String name() { return "RuledSurface.GetProfile2"; }
    @Override public String jlinkPackage() { return "pfcGeometry"; }
    @Override public String receiverType() { return "RuledSurface"; }
    @Override public String signature() { return "CurveDescriptor GetProfile2() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("RuledSurface.GetProfile2 \u2014 pfcGeometry")
                .required("target", JsonSchema.handle("RuledSurface"),
                        "The RuledSurface to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        RuledSurface target = Marshal.in(ctx, params.get("target"),
                "RuledSurface", RuledSurface.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetProfile2(), "CurveDescriptor");
    }
}
