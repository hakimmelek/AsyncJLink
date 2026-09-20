/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.interference;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcBase.Point3Ds;
import com.ptc.pfc.pfcInterference.ClearanceData;

/**
 * ClearanceData.SetNearestPoints &mdash; pfcInterference.
 *
 * <pre>
 * void SetNearestPoints(Point3Ds) throws jxthrowable
 * </pre>
 */
public final class ClearanceDataSetNearestPointsCommand implements Command {

    @Override public String name() { return "ClearanceData.SetNearestPoints"; }
    @Override public String jlinkPackage() { return "pfcInterference"; }
    @Override public String receiverType() { return "ClearanceData"; }
    @Override public String signature() { return "void SetNearestPoints(Point3Ds) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("ClearanceData.SetNearestPoints \u2014 pfcInterference")
                .required("target", JsonSchema.handle("ClearanceData"),
                        "The ClearanceData to act on.")
                .optional("point3Ds", JsonSchema.sequence("Point3Ds", JsonSchema.sequence("Point3D", JsonSchema.number())),
                        "Array of Point3D.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        ClearanceData target = Marshal.in(ctx, params.get("target"),
                "ClearanceData", ClearanceData.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        Point3Ds point3Ds = Marshal.in(ctx, params.get("point3Ds"),
                "Point3Ds", Point3Ds.class, "point3Ds");
        target.SetNearestPoints(point3Ds);
        return Marshal.ok();
    }
}
