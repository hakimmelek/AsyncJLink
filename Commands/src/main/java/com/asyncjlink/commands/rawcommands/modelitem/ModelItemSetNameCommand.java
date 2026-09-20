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
import com.ptc.pfc.pfcModelItem.ModelItem;

/**
 * ModelItem.SetName &mdash; pfcModelItem.
 *
 * <pre>
 * void SetName(String) throws jxthrowable
 * </pre>
 */
public final class ModelItemSetNameCommand implements Command {

    @Override public String name() { return "ModelItem.SetName"; }
    @Override public String jlinkPackage() { return "pfcModelItem"; }
    @Override public String receiverType() { return "ModelItem"; }
    @Override public String signature() { return "void SetName(String) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("ModelItem.SetName \u2014 pfcModelItem")
                .required("target", JsonSchema.handle("ModelItem"),
                        "The ModelItem to act on.")
                .optional("value", JsonSchema.string(),
                        "String value.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        ModelItem target = Marshal.in(ctx, params.get("target"),
                "ModelItem", ModelItem.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        String value = Marshal.in(ctx, params.get("value"),
                "String", String.class, "value");
        target.SetName(value);
        return Marshal.ok();
    }
}
