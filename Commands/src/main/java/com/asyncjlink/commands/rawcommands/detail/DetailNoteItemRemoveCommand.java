/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.detail;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcDetail.DetailNoteItem;

/**
 * DetailNoteItem.Remove &mdash; pfcDetail.
 *
 * <pre>
 * void Remove() throws jxthrowable
 * </pre>
 */
public final class DetailNoteItemRemoveCommand implements Command {

    @Override public String name() { return "DetailNoteItem.Remove"; }
    @Override public String jlinkPackage() { return "pfcDetail"; }
    @Override public String receiverType() { return "DetailNoteItem"; }
    @Override public String signature() { return "void Remove() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("DetailNoteItem.Remove \u2014 pfcDetail")
                .required("target", JsonSchema.handle("DetailNoteItem"),
                        "The DetailNoteItem to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        DetailNoteItem target = Marshal.in(ctx, params.get("target"),
                "DetailNoteItem", DetailNoteItem.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        target.Remove();
        return Marshal.ok();
    }
}
