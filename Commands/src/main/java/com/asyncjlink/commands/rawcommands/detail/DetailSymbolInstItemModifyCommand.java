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
import com.ptc.pfc.pfcDetail.DetailSymbolInstInstructions;
import com.ptc.pfc.pfcDetail.DetailSymbolInstItem;

/**
 * DetailSymbolInstItem.Modify &mdash; pfcDetail.
 *
 * <pre>
 * void Modify(DetailSymbolInstInstructions) throws jxthrowable
 * </pre>
 */
public final class DetailSymbolInstItemModifyCommand implements Command {

    @Override public String name() { return "DetailSymbolInstItem.Modify"; }
    @Override public String jlinkPackage() { return "pfcDetail"; }
    @Override public String receiverType() { return "DetailSymbolInstItem"; }
    @Override public String signature() { return "void Modify(DetailSymbolInstInstructions) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("DetailSymbolInstItem.Modify \u2014 pfcDetail")
                .required("target", JsonSchema.handle("DetailSymbolInstItem"),
                        "The DetailSymbolInstItem to act on.")
                .optional("detailSymbolInstInstructions", JsonSchema.dataObject("DetailSymbolInstInstructions"),
                        "DetailSymbolInstInstructions options object; its fields are passed to the pfc factory and setters.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        DetailSymbolInstItem target = Marshal.in(ctx, params.get("target"),
                "DetailSymbolInstItem", DetailSymbolInstItem.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        DetailSymbolInstInstructions detailSymbolInstInstructions = Marshal.in(ctx, params.get("detailSymbolInstInstructions"),
                "DetailSymbolInstInstructions", DetailSymbolInstInstructions.class, "detailSymbolInstInstructions");
        target.Modify(detailSymbolInstInstructions);
        return Marshal.ok();
    }
}
