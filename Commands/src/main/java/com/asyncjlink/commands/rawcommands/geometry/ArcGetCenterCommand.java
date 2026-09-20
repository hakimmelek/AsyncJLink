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
 * Arc.GetCenter &mdash; pfcGeometry.
 *
 * <pre>
 * Point3D GetCenter() throws jxthrowable
 * </pre>
 */
public final class ArcGetCenterCommand implements Command {

    @Override public String name() { return "Arc.GetCenter"; }
    @Override public String jlinkPackage() { return "pfcGeometry"; }
    @Override public String receiverType() { return "Arc"; }
    @Override public String signature() { return "Point3D GetCenter() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Arc.GetCenter \u2014 pfcGeometry")
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
        return Marshal.result(ctx, target.GetCenter(), "Point3D");
    }
}
