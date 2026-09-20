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
import com.ptc.pfc.pfcModelItem.ParameterOwner;

/**
 * ParameterOwner.GetParam &mdash; pfcModelItem.
 *
 * <pre>
 * Parameter GetParam(String) throws jxthrowable
 * </pre>
 */
public final class ParameterOwnerGetParamCommand implements Command {

    @Override public String name() { return "ParameterOwner.GetParam"; }
    @Override public String jlinkPackage() { return "pfcModelItem"; }
    @Override public String receiverType() { return "ParameterOwner"; }
    @Override public String signature() { return "Parameter GetParam(String) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("ParameterOwner.GetParam \u2014 pfcModelItem")
                .required("target", JsonSchema.handle("ParameterOwner"),
                        "The ParameterOwner to act on.")
                .optional("value", JsonSchema.string(),
                        "String value.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        ParameterOwner target = Marshal.in(ctx, params.get("target"),
                "ParameterOwner", ParameterOwner.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        String value = Marshal.in(ctx, params.get("value"),
                "String", String.class, "value");
        return Marshal.result(ctx, target.GetParam(value), "Parameter");
    }
}
