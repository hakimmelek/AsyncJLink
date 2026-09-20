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
import com.ptc.pfc.pfcInterference.GlobalInterference;
import com.ptc.pfc.pfcSelect.SelectionPair;

/**
 * GlobalInterference.SetSelParts &mdash; pfcInterference.
 *
 * <pre>
 * void SetSelParts(SelectionPair) throws jxthrowable
 * </pre>
 */
public final class GlobalInterferenceSetSelPartsCommand implements Command {

    @Override public String name() { return "GlobalInterference.SetSelParts"; }
    @Override public String jlinkPackage() { return "pfcInterference"; }
    @Override public String receiverType() { return "GlobalInterference"; }
    @Override public String signature() { return "void SetSelParts(SelectionPair) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("GlobalInterference.SetSelParts \u2014 pfcInterference")
                .required("target", JsonSchema.handle("GlobalInterference"),
                        "The GlobalInterference to act on.")
                .optional("selectionPair", JsonSchema.dataObject("SelectionPair"),
                        "SelectionPair options object; its fields are passed to the pfc factory and setters.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        GlobalInterference target = Marshal.in(ctx, params.get("target"),
                "GlobalInterference", GlobalInterference.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        SelectionPair selectionPair = Marshal.in(ctx, params.get("selectionPair"),
                "SelectionPair", SelectionPair.class, "selectionPair");
        target.SetSelParts(selectionPair);
        return Marshal.ok();
    }
}
