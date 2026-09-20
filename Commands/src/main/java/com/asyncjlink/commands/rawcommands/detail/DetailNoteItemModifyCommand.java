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
import com.ptc.pfc.pfcDetail.DetailNoteInstructions;
import com.ptc.pfc.pfcDetail.DetailNoteItem;

/**
 * DetailNoteItem.Modify &mdash; pfcDetail.
 *
 * <pre>
 * void Modify(DetailNoteInstructions) throws jxthrowable
 * </pre>
 */
public final class DetailNoteItemModifyCommand implements Command {

    @Override public String name() { return "DetailNoteItem.Modify"; }
    @Override public String jlinkPackage() { return "pfcDetail"; }
    @Override public String receiverType() { return "DetailNoteItem"; }
    @Override public String signature() { return "void Modify(DetailNoteInstructions) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("DetailNoteItem.Modify \u2014 pfcDetail")
                .required("target", JsonSchema.handle("DetailNoteItem"),
                        "The DetailNoteItem to act on.")
                .optional("detailNoteInstructions", JsonSchema.dataObject("DetailNoteInstructions"),
                        "DetailNoteInstructions options object; its fields are passed to the pfc factory and setters.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        DetailNoteItem target = Marshal.in(ctx, params.get("target"),
                "DetailNoteItem", DetailNoteItem.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        DetailNoteInstructions detailNoteInstructions = Marshal.in(ctx, params.get("detailNoteInstructions"),
                "DetailNoteInstructions", DetailNoteInstructions.class, "detailNoteInstructions");
        target.Modify(detailNoteInstructions);
        return Marshal.ok();
    }
}
