/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.select;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcSelect.SelectionBuffer;

/**
 * SelectionBuffer.RemoveSelection &mdash; pfcSelect.
 *
 * <pre>
 * void RemoveSelection(int) throws jxthrowable
 * </pre>
 */
public final class SelectionBufferRemoveSelectionCommand implements Command {

    @Override public String name() { return "SelectionBuffer.RemoveSelection"; }
    @Override public String jlinkPackage() { return "pfcSelect"; }
    @Override public String receiverType() { return "SelectionBuffer"; }
    @Override public String signature() { return "void RemoveSelection(int) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("SelectionBuffer.RemoveSelection \u2014 pfcSelect")
                .required("target", JsonSchema.handle("SelectionBuffer"),
                        "The SelectionBuffer to act on.")
                .required("value", JsonSchema.integer(),
                        "int value.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        SelectionBuffer target = Marshal.in(ctx, params.get("target"),
                "SelectionBuffer", SelectionBuffer.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        int value = Marshal.in(ctx, params.get("value"),
                "int", int.class, "value");
        target.RemoveSelection(value);
        return Marshal.ok();
    }
}
