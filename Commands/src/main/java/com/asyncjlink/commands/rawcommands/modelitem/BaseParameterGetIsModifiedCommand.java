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
import com.ptc.pfc.pfcModelItem.BaseParameter;

/**
 * BaseParameter.GetIsModified &mdash; pfcModelItem.
 *
 * <pre>
 * boolean GetIsModified() throws jxthrowable
 * </pre>
 */
public final class BaseParameterGetIsModifiedCommand implements Command {

    @Override public String name() { return "BaseParameter.GetIsModified"; }
    @Override public String jlinkPackage() { return "pfcModelItem"; }
    @Override public String receiverType() { return "BaseParameter"; }
    @Override public String signature() { return "boolean GetIsModified() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("BaseParameter.GetIsModified \u2014 pfcModelItem")
                .required("target", JsonSchema.handle("BaseParameter"),
                        "The BaseParameter to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        BaseParameter target = Marshal.in(ctx, params.get("target"),
                "BaseParameter", BaseParameter.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetIsModified(), "boolean");
    }
}
