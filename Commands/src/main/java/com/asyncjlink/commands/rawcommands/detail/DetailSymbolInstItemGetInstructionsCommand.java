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
import com.ptc.pfc.pfcDetail.DetailSymbolInstItem;

/**
 * DetailSymbolInstItem.GetInstructions &mdash; pfcDetail.
 *
 * <pre>
 * DetailSymbolInstInstructions GetInstructions(boolean) throws jxthrowable
 * </pre>
 */
public final class DetailSymbolInstItemGetInstructionsCommand implements Command {

    @Override public String name() { return "DetailSymbolInstItem.GetInstructions"; }
    @Override public String jlinkPackage() { return "pfcDetail"; }
    @Override public String receiverType() { return "DetailSymbolInstItem"; }
    @Override public String signature() { return "DetailSymbolInstInstructions GetInstructions(boolean) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("DetailSymbolInstItem.GetInstructions \u2014 pfcDetail")
                .required("target", JsonSchema.handle("DetailSymbolInstItem"),
                        "The DetailSymbolInstItem to act on.")
                .required("flag", JsonSchema.bool(),
                        "boolean value.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        DetailSymbolInstItem target = Marshal.in(ctx, params.get("target"),
                "DetailSymbolInstItem", DetailSymbolInstItem.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        boolean flag = Marshal.in(ctx, params.get("flag"),
                "boolean", boolean.class, "flag");
        return Marshal.result(ctx, target.GetInstructions(flag), "DetailSymbolInstInstructions");
    }
}
