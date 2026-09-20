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
import com.ptc.pfc.pfcBase.Point3D;
import com.ptc.pfc.pfcInterference.CriticalDistanceData;

/**
 * CriticalDistanceData.SetCriticalPoint2 &mdash; pfcInterference.
 *
 * <pre>
 * void SetCriticalPoint2(Point3D) throws jxthrowable
 * </pre>
 */
public final class CriticalDistanceDataSetCriticalPoint2Command implements Command {

    @Override public String name() { return "CriticalDistanceData.SetCriticalPoint2"; }
    @Override public String jlinkPackage() { return "pfcInterference"; }
    @Override public String receiverType() { return "CriticalDistanceData"; }
    @Override public String signature() { return "void SetCriticalPoint2(Point3D) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("CriticalDistanceData.SetCriticalPoint2 \u2014 pfcInterference")
                .required("target", JsonSchema.handle("CriticalDistanceData"),
                        "The CriticalDistanceData to act on.")
                .optional("point3D", JsonSchema.sequence("Point3D", JsonSchema.number()),
                        "Array of double.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        CriticalDistanceData target = Marshal.in(ctx, params.get("target"),
                "CriticalDistanceData", CriticalDistanceData.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        Point3D point3D = Marshal.in(ctx, params.get("point3D"),
                "Point3D", Point3D.class, "point3D");
        target.SetCriticalPoint2(point3D);
        return Marshal.ok();
    }
}
