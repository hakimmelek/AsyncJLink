/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.modelitem;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcModelItem.NamedModelItem;

/**
 * NamedModelItem.GetName &mdash; pfcModelItem.
 *
 * <pre>
 * String GetName() throws jxthrowable
 * </pre>
 */
public final class NamedModelItemGetNameCommand implements Command {

    @Override public String name() { return "NamedModelItem.GetName"; }
    @Override public String jlinkPackage() { return "pfcModelItem"; }
    @Override public String receiverType() { return "NamedModelItem"; }
    @Override public String signature() { return "String GetName() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("NamedModelItem.GetName \u2014 pfcModelItem")
                .required("target", JsonSchema.handle("NamedModelItem"),
                        "The NamedModelItem to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        NamedModelItem target = Marshal.in(ctx, params.get("target"),
                "NamedModelItem", NamedModelItem.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetName(), "String");
    }
}
