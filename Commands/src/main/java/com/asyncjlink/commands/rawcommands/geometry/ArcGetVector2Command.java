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
import com.ptc.pfc.pfcGeometry.Arc;

/**
 * Arc.GetVector2 &mdash; pfcGeometry.
 *
 * <pre>
 * Vector3D GetVector2() throws jxthrowable
 * </pre>
 */
public final class ArcGetVector2Command implements Command {

    @Override public String name() { return "Arc.GetVector2"; }
    @Override public String jlinkPackage() { return "pfcGeometry"; }
    @Override public String receiverType() { return "Arc"; }
    @Override public String signature() { return "Vector3D GetVector2() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Arc.GetVector2 \u2014 pfcGeometry")
                .required("target", JsonSchema.handle("Arc"),
                        "The Arc to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Arc target = Marshal.in(ctx, params.get("target"),
                "Arc", Arc.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetVector2(), "Vector3D");
    }
}
