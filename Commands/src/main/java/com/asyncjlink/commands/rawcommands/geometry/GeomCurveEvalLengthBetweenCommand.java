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
 * GeomCurve.EvalLengthBetween &mdash; pfcGeometry.
 *
 * <pre>
 * double EvalLengthBetween(double, double) throws jxthrowable
 * </pre>
 */
public final class GeomCurveEvalLengthBetweenCommand implements Command {

    @Override public String name() { return "GeomCurve.EvalLengthBetween"; }
    @Override public String jlinkPackage() { return "pfcGeometry"; }
    @Override public String receiverType() { return "GeomCurve"; }
    @Override public String signature() { return "double EvalLengthBetween(double, double) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("GeomCurve.EvalLengthBetween \u2014 pfcGeometry")
                .required("target", JsonSchema.handle("GeomCurve"),
                        "The GeomCurve to act on.")
                .required("value1", JsonSchema.number(),
                        "double value.")
                .required("value2", JsonSchema.number(),
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
        double value1 = Marshal.in(ctx, params.get("value1"),
                "double", double.class, "value1");
        double value2 = Marshal.in(ctx, params.get("value2"),
                "double", double.class, "value2");
        return Marshal.result(ctx, target.EvalLengthBetween(value1, value2), "double");
    }
}
