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
import com.ptc.pfc.pfcModelItem.Parameter;

/**
 * Parameter.SetDescription &mdash; pfcModelItem.
 *
 * <pre>
 * void SetDescription(String) throws jxthrowable
 * </pre>
 */
public final class ParameterSetDescriptionCommand implements Command {

    @Override public String name() { return "Parameter.SetDescription"; }
    @Override public String jlinkPackage() { return "pfcModelItem"; }
    @Override public String receiverType() { return "Parameter"; }
    @Override public String signature() { return "void SetDescription(String) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Parameter.SetDescription \u2014 pfcModelItem")
                .required("target", JsonSchema.handle("Parameter"),
                        "The Parameter to act on.")
                .optional("value", JsonSchema.string(),
                        "String value.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Parameter target = Marshal.in(ctx, params.get("target"),
                "Parameter", Parameter.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        String value = Marshal.in(ctx, params.get("value"),
                "String", String.class, "value");
        target.SetDescription(value);
        return Marshal.ok();
    }
}
