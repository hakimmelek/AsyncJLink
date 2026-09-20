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
import com.ptc.pfc.pfcGeometry.Edge;

/**
 * Edge.EvalUV &mdash; pfcGeometry.
 *
 * <pre>
 * EdgeEvalData EvalUV(double) throws jxthrowable
 * </pre>
 */
public final class EdgeEvalUVCommand implements Command {

    @Override public String name() { return "Edge.EvalUV"; }
    @Override public String jlinkPackage() { return "pfcGeometry"; }
    @Override public String receiverType() { return "Edge"; }
    @Override public String signature() { return "EdgeEvalData EvalUV(double) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Edge.EvalUV \u2014 pfcGeometry")
                .required("target", JsonSchema.handle("Edge"),
                        "The Edge to act on.")
                .required("value", JsonSchema.number(),
                        "double value.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Edge target = Marshal.in(ctx, params.get("target"),
                "Edge", Edge.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        double value = Marshal.in(ctx, params.get("value"),
                "double", double.class, "value");
        return Marshal.result(ctx, target.EvalUV(value), "EdgeEvalData");
    }
}
