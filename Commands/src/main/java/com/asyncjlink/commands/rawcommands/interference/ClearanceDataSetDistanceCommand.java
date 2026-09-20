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
import com.ptc.pfc.pfcInterference.ClearanceData;

/**
 * ClearanceData.SetDistance &mdash; pfcInterference.
 *
 * <pre>
 * void SetDistance(double) throws jxthrowable
 * </pre>
 */
public final class ClearanceDataSetDistanceCommand implements Command {

    @Override public String name() { return "ClearanceData.SetDistance"; }
    @Override public String jlinkPackage() { return "pfcInterference"; }
    @Override public String receiverType() { return "ClearanceData"; }
    @Override public String signature() { return "void SetDistance(double) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("ClearanceData.SetDistance \u2014 pfcInterference")
                .required("target", JsonSchema.handle("ClearanceData"),
                        "The ClearanceData to act on.")
                .required("value", JsonSchema.number(),
                        "double value.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        ClearanceData target = Marshal.in(ctx, params.get("target"),
                "ClearanceData", ClearanceData.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        double value = Marshal.in(ctx, params.get("value"),
                "double", double.class, "value");
        target.SetDistance(value);
        return Marshal.ok();
    }
}
