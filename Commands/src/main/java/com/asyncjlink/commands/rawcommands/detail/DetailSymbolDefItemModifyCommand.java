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
import com.ptc.pfc.pfcDetail.DetailSymbolDefInstructions;
import com.ptc.pfc.pfcDetail.DetailSymbolDefItem;

/**
 * DetailSymbolDefItem.Modify &mdash; pfcDetail.
 *
 * <pre>
 * void Modify(DetailSymbolDefInstructions) throws jxthrowable
 * </pre>
 */
public final class DetailSymbolDefItemModifyCommand implements Command {

    @Override public String name() { return "DetailSymbolDefItem.Modify"; }
    @Override public String jlinkPackage() { return "pfcDetail"; }
    @Override public String receiverType() { return "DetailSymbolDefItem"; }
    @Override public String signature() { return "void Modify(DetailSymbolDefInstructions) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("DetailSymbolDefItem.Modify \u2014 pfcDetail")
                .required("target", JsonSchema.handle("DetailSymbolDefItem"),
                        "The DetailSymbolDefItem to act on.")
                .optional("detailSymbolDefInstructions", JsonSchema.dataObject("DetailSymbolDefInstructions"),
                        "DetailSymbolDefInstructions options object; its fields are passed to the pfc factory and setters.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        DetailSymbolDefItem target = Marshal.in(ctx, params.get("target"),
                "DetailSymbolDefItem", DetailSymbolDefItem.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        DetailSymbolDefInstructions detailSymbolDefInstructions = Marshal.in(ctx, params.get("detailSymbolDefInstructions"),
                "DetailSymbolDefInstructions", DetailSymbolDefInstructions.class, "detailSymbolDefInstructions");
        target.Modify(detailSymbolDefInstructions);
        return Marshal.ok();
    }
}
