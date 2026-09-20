/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.family;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcFamily.FamColModelItem;

/**
 * FamColModelItem.GetRefItem &mdash; pfcFamily.
 *
 * <pre>
 * ModelItem GetRefItem() throws jxthrowable
 * </pre>
 */
public final class FamColModelItemGetRefItemCommand implements Command {

    @Override public String name() { return "FamColModelItem.GetRefItem"; }
    @Override public String jlinkPackage() { return "pfcFamily"; }
    @Override public String receiverType() { return "FamColModelItem"; }
    @Override public String signature() { return "ModelItem GetRefItem() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("FamColModelItem.GetRefItem \u2014 pfcFamily")
                .required("target", JsonSchema.handle("FamColModelItem"),
                        "The FamColModelItem to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        FamColModelItem target = Marshal.in(ctx, params.get("target"),
                "FamColModelItem", FamColModelItem.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetRefItem(), "ModelItem");
    }
}
