/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.model;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcModel.Model;

/**
 * Model.GetInstanceName &mdash; pfcModel.
 *
 * <pre>
 * String GetInstanceName() throws jxthrowable
 * </pre>
 */
public final class ModelGetInstanceNameCommand implements Command {

    @Override public String name() { return "Model.GetInstanceName"; }
    @Override public String jlinkPackage() { return "pfcModel"; }
    @Override public String receiverType() { return "Model"; }
    @Override public String signature() { return "String GetInstanceName() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Model.GetInstanceName \u2014 pfcModel")
                .required("target", JsonSchema.handle("Model"),
                        "The Model to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Model target = Marshal.in(ctx, params.get("target"),
                "Model", Model.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetInstanceName(), "String");
    }
}
