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
import com.ptc.pfc.pfcGeometry.GeomCurve;

/**
 * GeomCurve.Eval3DData &mdash; pfcGeometry.
 *
 * <pre>
 * CurveXYZData Eval3DData(double) throws jxthrowable
 * </pre>
 */
public final class GeomCurveEval3DDataCommand implements Command {

    @Override public String name() { return "GeomCurve.Eval3DData"; }
    @Override public String jlinkPackage() { return "pfcGeometry"; }
    @Override public String receiverType() { return "GeomCurve"; }
    @Override public String signature() { return "CurveXYZData Eval3DData(double) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("GeomCurve.Eval3DData \u2014 pfcGeometry")
                .required("target", JsonSchema.handle("GeomCurve"),
                        "The GeomCurve to act on.")
                .required("value", JsonSchema.number(),
                        "double value.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        GeomCurve target = Marshal.in(ctx, params.get("target"),
                "GeomCurve", GeomCurve.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        double value = Marshal.in(ctx, params.get("value"),
                "double", double.class, "value");
        return Marshal.result(ctx, target.Eval3DData(value), "CurveXYZData");
    }
}
