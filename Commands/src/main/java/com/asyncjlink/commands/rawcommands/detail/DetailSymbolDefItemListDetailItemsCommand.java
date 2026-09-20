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
import com.ptc.pfc.pfcDetail.DetailSymbolDefItem;
import com.ptc.pfc.pfcDetail.DetailType;

/**
 * DetailSymbolDefItem.ListDetailItems &mdash; pfcDetail.
 *
 * <pre>
 * DetailItems ListDetailItems(DetailType) throws jxthrowable
 * </pre>
 */
public final class DetailSymbolDefItemListDetailItemsCommand implements Command {

    @Override public String name() { return "DetailSymbolDefItem.ListDetailItems"; }
    @Override public String jlinkPackage() { return "pfcDetail"; }
    @Override public String receiverType() { return "DetailSymbolDefItem"; }
    @Override public String signature() { return "DetailItems ListDetailItems(DetailType) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("DetailSymbolDefItem.ListDetailItems \u2014 pfcDetail")
                .required("target", JsonSchema.handle("DetailSymbolDefItem"),
                        "The DetailSymbolDefItem to act on.")
                .optional("detailType", JsonSchema.enumOf("DetailType", "DETAIL_ENTITY", "DETAIL_NOTE", "DETAIL_SYM_DEFINITION", "DETAIL_SYM_INSTANCE", "DETAIL_DRAFT_GROUP", "DETAIL_OLE_OBJECT"),
                        "One of the DetailType constants.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        DetailSymbolDefItem target = Marshal.in(ctx, params.get("target"),
                "DetailSymbolDefItem", DetailSymbolDefItem.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        DetailType detailType = Marshal.in(ctx, params.get("detailType"),
                "DetailType", DetailType.class, "detailType");
        return Marshal.result(ctx, target.ListDetailItems(detailType), "DetailItems");
    }
}
