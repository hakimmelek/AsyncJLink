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
import com.ptc.pfc.pfcDetail.DetailCreateInstructions;
import com.ptc.pfc.pfcDetail.DetailSymbolDefItem;

/**
 * DetailSymbolDefItem.CreateDetailItem &mdash; pfcDetail.
 *
 * <pre>
 * DetailItem CreateDetailItem(DetailCreateInstructions) throws jxthrowable
 * </pre>
 */
public final class DetailSymbolDefItemCreateDetailItemCommand implements Command {

    @Override public String name() { return "DetailSymbolDefItem.CreateDetailItem"; }
    @Override public String jlinkPackage() { return "pfcDetail"; }
    @Override public String receiverType() { return "DetailSymbolDefItem"; }
    @Override public String signature() { return "DetailItem CreateDetailItem(DetailCreateInstructions) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("DetailSymbolDefItem.CreateDetailItem \u2014 pfcDetail")
                .required("target", JsonSchema.handle("DetailSymbolDefItem"),
                        "The DetailSymbolDefItem to act on.")
                .optional("detailCreateInstructions", JsonSchema.dataObject("DetailCreateInstructions"),
                        "DetailCreateInstructions options object; its fields are passed to the pfc factory and setters.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        DetailSymbolDefItem target = Marshal.in(ctx, params.get("target"),
                "DetailSymbolDefItem", DetailSymbolDefItem.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        DetailCreateInstructions detailCreateInstructions = Marshal.in(ctx, params.get("detailCreateInstructions"),
                "DetailCreateInstructions", DetailCreateInstructions.class, "detailCreateInstructions");
        return Marshal.result(ctx, target.CreateDetailItem(detailCreateInstructions), "DetailItem");
    }
}
