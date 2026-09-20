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
import com.ptc.pfc.pfcDetail.DetailGroupItem;

/**
 * DetailGroupItem.GetInstructions &mdash; pfcDetail.
 *
 * <pre>
 * DetailGroupInstructions GetInstructions() throws jxthrowable
 * </pre>
 */
public final class DetailGroupItemGetInstructionsCommand implements Command {

    @Override public String name() { return "DetailGroupItem.GetInstructions"; }
    @Override public String jlinkPackage() { return "pfcDetail"; }
    @Override public String receiverType() { return "DetailGroupItem"; }
    @Override public String signature() { return "DetailGroupInstructions GetInstructions() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("DetailGroupItem.GetInstructions \u2014 pfcDetail")
                .required("target", JsonSchema.handle("DetailGroupItem"),
                        "The DetailGroupItem to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        DetailGroupItem target = Marshal.in(ctx, params.get("target"),
                "DetailGroupItem", DetailGroupItem.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetInstructions(), "DetailGroupInstructions");
    }
}
