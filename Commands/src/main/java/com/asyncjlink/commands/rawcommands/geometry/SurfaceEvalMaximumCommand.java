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
import com.ptc.pfc.pfcBase.Vector3D;
import com.ptc.pfc.pfcGeometry.Surface;

/**
 * Surface.EvalMaximum &mdash; pfcGeometry.
 *
 * <pre>
 * Point3D EvalMaximum(Vector3D) throws jxthrowable
 * </pre>
 */
public final class SurfaceEvalMaximumCommand implements Command {

    @Override public String name() { return "Surface.EvalMaximum"; }
    @Override public String jlinkPackage() { return "pfcGeometry"; }
    @Override public String receiverType() { return "Surface"; }
    @Override public String signature() { return "Point3D EvalMaximum(Vector3D) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Surface.EvalMaximum \u2014 pfcGeometry")
                .required("target", JsonSchema.handle("Surface"),
                        "The Surface to act on.")
                .optional("vector3D", JsonSchema.sequence("Vector3D", JsonSchema.number()),
                        "Array of double.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Surface target = Marshal.in(ctx, params.get("target"),
                "Surface", Surface.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        Vector3D vector3D = Marshal.in(ctx, params.get("vector3D"),
                "Vector3D", Vector3D.class, "vector3D");
        return Marshal.result(ctx, target.EvalMaximum(vector3D), "Point3D");
    }
}
