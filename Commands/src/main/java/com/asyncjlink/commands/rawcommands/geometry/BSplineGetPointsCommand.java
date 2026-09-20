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
import com.ptc.pfc.pfcGeometry.BSpline;

/**
 * BSpline.GetPoints &mdash; pfcGeometry.
 *
 * <pre>
 * BSplinePoints GetPoints() throws jxthrowable
 * </pre>
 */
public final class BSplineGetPointsCommand implements Command {

    @Override public String name() { return "BSpline.GetPoints"; }
    @Override public String jlinkPackage() { return "pfcGeometry"; }
    @Override public String receiverType() { return "BSpline"; }
    @Override public String signature() { return "BSplinePoints GetPoints() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("BSpline.GetPoints \u2014 pfcGeometry")
                .required("target", JsonSchema.handle("BSpline"),
                        "The BSpline to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        BSpline target = Marshal.in(ctx, params.get("target"),
                "BSpline", BSpline.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetPoints(), "BSplinePoints");
    }
}
