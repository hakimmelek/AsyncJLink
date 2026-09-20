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
import com.ptc.pfc.pfcGeometry.CylindricalSplineSurface;

/**
 * CylindricalSplineSurface.GetSplineSurfaceData &mdash; pfcGeometry.
 *
 * <pre>
 * SplineSurfaceDescriptor GetSplineSurfaceData() throws jxthrowable
 * </pre>
 */
public final class CylindricalSplineSurfaceGetSplineSurfaceDataCommand implements Command {

    @Override public String name() { return "CylindricalSplineSurface.GetSplineSurfaceData"; }
    @Override public String jlinkPackage() { return "pfcGeometry"; }
    @Override public String receiverType() { return "CylindricalSplineSurface"; }
    @Override public String signature() { return "SplineSurfaceDescriptor GetSplineSurfaceData() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("CylindricalSplineSurface.GetSplineSurfaceData \u2014 pfcGeometry")
                .required("target", JsonSchema.handle("CylindricalSplineSurface"),
                        "The CylindricalSplineSurface to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        CylindricalSplineSurface target = Marshal.in(ctx, params.get("target"),
                "CylindricalSplineSurface", CylindricalSplineSurface.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetSplineSurfaceData(), "SplineSurfaceDescriptor");
    }
}
