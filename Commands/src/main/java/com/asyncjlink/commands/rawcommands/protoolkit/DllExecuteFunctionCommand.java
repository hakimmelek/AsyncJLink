/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.protoolkit;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcArgument.Arguments;
import com.ptc.pfc.pfcProToolkit.Dll;

/**
 * Dll.ExecuteFunction &mdash; pfcProToolkit.
 *
 * <pre>
 * FunctionReturn ExecuteFunction(String, Arguments) throws jxthrowable
 * </pre>
 */
public final class DllExecuteFunctionCommand implements Command {

    @Override public String name() { return "Dll.ExecuteFunction"; }
    @Override public String jlinkPackage() { return "pfcProToolkit"; }
    @Override public String receiverType() { return "Dll"; }
    @Override public String signature() { return "FunctionReturn ExecuteFunction(String, Arguments) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Dll.ExecuteFunction \u2014 pfcProToolkit")
                .required("target", JsonSchema.handle("Dll"),
                        "The Dll to act on.")
                .optional("value", JsonSchema.string(),
                        "String value.")
                .optional("arguments", JsonSchema.sequence("Arguments", JsonSchema.dataObject("Argument")),
                        "Array of Argument.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Dll target = Marshal.in(ctx, params.get("target"),
                "Dll", Dll.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        String value = Marshal.in(ctx, params.get("value"),
                "String", String.class, "value");
        Arguments arguments = Marshal.in(ctx, params.get("arguments"),
                "Arguments", Arguments.class, "arguments");
        return Marshal.result(ctx, target.ExecuteFunction(value, arguments), "FunctionReturn");
    }
}
