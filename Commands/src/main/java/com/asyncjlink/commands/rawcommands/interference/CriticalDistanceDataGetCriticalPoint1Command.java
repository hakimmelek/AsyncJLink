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
import com.ptc.pfc.pfcInterference.CriticalDistanceData;

/**
 * CriticalDistanceData.GetCriticalPoint1 &mdash; pfcInterference.
 *
 * <pre>
 * Point3D GetCriticalPoint1() throws jxthrowable
 * </pre>
 */
public final class CriticalDistanceDataGetCriticalPoint1Command implements Command {

    @Override public String name() { return "CriticalDistanceData.GetCriticalPoint1"; }
    @Override public String jlinkPackage() { return "pfcInterference"; }
    @Override public String receiverType() { return "CriticalDistanceData"; }
    @Override public String signature() { return "Point3D GetCriticalPoint1() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("CriticalDistanceData.GetCriticalPoint1 \u2014 pfcInterference")
                .required("target", JsonSchema.handle("CriticalDistanceData"),
                        "The CriticalDistanceData to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        CriticalDistanceData target = Marshal.in(ctx, params.get("target"),
                "CriticalDistanceData", CriticalDistanceData.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetCriticalPoint1(), "Point3D");
    }
}
