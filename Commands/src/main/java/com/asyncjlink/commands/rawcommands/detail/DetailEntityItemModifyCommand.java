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
import com.ptc.pfc.pfcDetail.DetailEntityInstructions;
import com.ptc.pfc.pfcDetail.DetailEntityItem;

/**
 * DetailEntityItem.Modify &mdash; pfcDetail.
 *
 * <pre>
 * void Modify(DetailEntityInstructions) throws jxthrowable
 * </pre>
 */
public final class DetailEntityItemModifyCommand implements Command {

    @Override public String name() { return "DetailEntityItem.Modify"; }
    @Override public String jlinkPackage() { return "pfcDetail"; }
    @Override public String receiverType() { return "DetailEntityItem"; }
    @Override public String signature() { return "void Modify(DetailEntityInstructions) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("DetailEntityItem.Modify \u2014 pfcDetail")
                .required("target", JsonSchema.handle("DetailEntityItem"),
                        "The DetailEntityItem to act on.")
                .optional("detailEntityInstructions", JsonSchema.dataObject("DetailEntityInstructions"),
                        "DetailEntityInstructions options object; its fields are passed to the pfc factory and setters.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        DetailEntityItem target = Marshal.in(ctx, params.get("target"),
                "DetailEntityItem", DetailEntityItem.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        DetailEntityInstructions detailEntityInstructions = Marshal.in(ctx, params.get("detailEntityInstructions"),
                "DetailEntityInstructions", DetailEntityInstructions.class, "detailEntityInstructions");
        target.Modify(detailEntityInstructions);
        return Marshal.ok();
    }
}
