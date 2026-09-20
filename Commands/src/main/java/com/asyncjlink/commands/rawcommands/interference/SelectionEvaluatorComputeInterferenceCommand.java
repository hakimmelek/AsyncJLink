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
import com.ptc.pfc.pfcInterference.SelectionEvaluator;

/**
 * SelectionEvaluator.ComputeInterference &mdash; pfcInterference.
 *
 * <pre>
 * InterferenceVolume ComputeInterference(boolean) throws jxthrowable
 * </pre>
 */
public final class SelectionEvaluatorComputeInterferenceCommand implements Command {

    @Override public String name() { return "SelectionEvaluator.ComputeInterference"; }
    @Override public String jlinkPackage() { return "pfcInterference"; }
    @Override public String receiverType() { return "SelectionEvaluator"; }
    @Override public String signature() { return "InterferenceVolume ComputeInterference(boolean) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("SelectionEvaluator.ComputeInterference \u2014 pfcInterference")
                .required("target", JsonSchema.handle("SelectionEvaluator"),
                        "The SelectionEvaluator to act on.")
                .required("flag", JsonSchema.bool(),
                        "boolean value.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        SelectionEvaluator target = Marshal.in(ctx, params.get("target"),
                "SelectionEvaluator", SelectionEvaluator.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        boolean flag = Marshal.in(ctx, params.get("flag"),
                "boolean", boolean.class, "flag");
        return Marshal.result(ctx, target.ComputeInterference(flag), "InterferenceVolume");
    }
}
