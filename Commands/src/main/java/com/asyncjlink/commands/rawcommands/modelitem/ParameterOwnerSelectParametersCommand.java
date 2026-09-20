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
import com.ptc.pfc.pfcModelItem.ParameterSelectionOptions;

/**
 * ParameterOwner.SelectParameters &mdash; pfcModelItem.
 *
 * <pre>
 * Parameters SelectParameters(ParameterSelectionOptions) throws jxthrowable
 * </pre>
 */
public final class ParameterOwnerSelectParametersCommand implements Command {

    @Override public String name() { return "ParameterOwner.SelectParameters"; }
    @Override public String jlinkPackage() { return "pfcModelItem"; }
    @Override public String receiverType() { return "ParameterOwner"; }
    @Override public String signature() { return "Parameters SelectParameters(ParameterSelectionOptions) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("ParameterOwner.SelectParameters \u2014 pfcModelItem")
                .required("target", JsonSchema.handle("ParameterOwner"),
                        "The ParameterOwner to act on.")
                .optional("parameterSelectionOptions", JsonSchema.dataObject("ParameterSelectionOptions"),
                        "ParameterSelectionOptions options object; its fields are passed to the pfc factory and setters.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        ParameterOwner target = Marshal.in(ctx, params.get("target"),
                "ParameterOwner", ParameterOwner.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        ParameterSelectionOptions parameterSelectionOptions = Marshal.in(ctx, params.get("parameterSelectionOptions"),
                "ParameterSelectionOptions", ParameterSelectionOptions.class, "parameterSelectionOptions");
        return Marshal.result(ctx, target.SelectParameters(parameterSelectionOptions), "Parameters");
    }
}
