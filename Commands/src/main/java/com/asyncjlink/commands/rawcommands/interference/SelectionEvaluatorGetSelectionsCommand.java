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
 * SelectionEvaluator.GetSelections &mdash; pfcInterference.
 *
 * <pre>
 * SelectionPair GetSelections() throws jxthrowable
 * </pre>
 */
public final class SelectionEvaluatorGetSelectionsCommand implements Command {

    @Override public String name() { return "SelectionEvaluator.GetSelections"; }
    @Override public String jlinkPackage() { return "pfcInterference"; }
    @Override public String receiverType() { return "SelectionEvaluator"; }
    @Override public String signature() { return "SelectionPair GetSelections() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("SelectionEvaluator.GetSelections \u2014 pfcInterference")
                .required("target", JsonSchema.handle("SelectionEvaluator"),
                        "The SelectionEvaluator to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        SelectionEvaluator target = Marshal.in(ctx, params.get("target"),
                "SelectionEvaluator", SelectionEvaluator.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetSelections(), "SelectionPair");
    }
}
