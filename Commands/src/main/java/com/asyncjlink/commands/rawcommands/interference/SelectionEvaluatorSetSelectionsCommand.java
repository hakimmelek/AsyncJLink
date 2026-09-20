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
import com.ptc.pfc.pfcSelect.SelectionPair;

/**
 * SelectionEvaluator.SetSelections &mdash; pfcInterference.
 *
 * <pre>
 * void SetSelections(SelectionPair) throws jxthrowable
 * </pre>
 */
public final class SelectionEvaluatorSetSelectionsCommand implements Command {

    @Override public String name() { return "SelectionEvaluator.SetSelections"; }
    @Override public String jlinkPackage() { return "pfcInterference"; }
    @Override public String receiverType() { return "SelectionEvaluator"; }
    @Override public String signature() { return "void SetSelections(SelectionPair) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("SelectionEvaluator.SetSelections \u2014 pfcInterference")
                .required("target", JsonSchema.handle("SelectionEvaluator"),
                        "The SelectionEvaluator to act on.")
                .optional("selectionPair", JsonSchema.dataObject("SelectionPair"),
                        "SelectionPair options object; its fields are passed to the pfc factory and setters.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        SelectionEvaluator target = Marshal.in(ctx, params.get("target"),
                "SelectionEvaluator", SelectionEvaluator.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        SelectionPair selectionPair = Marshal.in(ctx, params.get("selectionPair"),
                "SelectionPair", SelectionPair.class, "selectionPair");
        target.SetSelections(selectionPair);
        return Marshal.ok();
    }
}
