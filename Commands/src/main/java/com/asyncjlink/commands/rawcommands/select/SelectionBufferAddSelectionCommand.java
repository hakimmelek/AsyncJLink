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
import com.ptc.pfc.pfcSelect.Selection;
import com.ptc.pfc.pfcSelect.SelectionBuffer;

/**
 * SelectionBuffer.AddSelection &mdash; pfcSelect.
 *
 * <pre>
 * void AddSelection(Selection) throws jxthrowable
 * </pre>
 */
public final class SelectionBufferAddSelectionCommand implements Command {

    @Override public String name() { return "SelectionBuffer.AddSelection"; }
    @Override public String jlinkPackage() { return "pfcSelect"; }
    @Override public String receiverType() { return "SelectionBuffer"; }
    @Override public String signature() { return "void AddSelection(Selection) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("SelectionBuffer.AddSelection \u2014 pfcSelect")
                .required("target", JsonSchema.handle("SelectionBuffer"),
                        "The SelectionBuffer to act on.")
                .optional("selection", JsonSchema.handle("Selection"),
                        "Handle to a Selection, as returned by an earlier command.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        SelectionBuffer target = Marshal.in(ctx, params.get("target"),
                "SelectionBuffer", SelectionBuffer.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        Selection selection = Marshal.in(ctx, params.get("selection"),
                "Selection", Selection.class, "selection");
        target.AddSelection(selection);
        return Marshal.ok();
    }
}
