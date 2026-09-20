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
 * DetailGroupItem.Draw &mdash; pfcDetail.
 *
 * <pre>
 * void Draw() throws jxthrowable
 * </pre>
 */
public final class DetailGroupItemDrawCommand implements Command {

    @Override public String name() { return "DetailGroupItem.Draw"; }
    @Override public String jlinkPackage() { return "pfcDetail"; }
    @Override public String receiverType() { return "DetailGroupItem"; }
    @Override public String signature() { return "void Draw() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("DetailGroupItem.Draw \u2014 pfcDetail")
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
        target.Draw();
        return Marshal.ok();
    }
}
