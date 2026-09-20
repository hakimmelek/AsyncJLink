/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.command;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcCommand.UICommand;

/**
 * UICommand.Designate &mdash; pfcCommand.
 *
 * <pre>
 * void Designate(String, String, String, String) throws jxthrowable
 * </pre>
 */
public final class UICommandDesignateCommand implements Command {

    @Override public String name() { return "UICommand.Designate"; }
    @Override public String jlinkPackage() { return "pfcCommand"; }
    @Override public String receiverType() { return "UICommand"; }
    @Override public String signature() { return "void Designate(String, String, String, String) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("UICommand.Designate \u2014 pfcCommand")
                .required("target", JsonSchema.handle("UICommand"),
                        "The UICommand to act on.")
                .optional("value1", JsonSchema.string(),
                        "String value.")
                .optional("value2", JsonSchema.string(),
                        "String value.")
                .optional("value3", JsonSchema.string(),
                        "String value.")
                .optional("value4", JsonSchema.string(),
                        "String value.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        UICommand target = Marshal.in(ctx, params.get("target"),
                "UICommand", UICommand.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        String value1 = Marshal.in(ctx, params.get("value1"),
                "String", String.class, "value1");
        String value2 = Marshal.in(ctx, params.get("value2"),
                "String", String.class, "value2");
        String value3 = Marshal.in(ctx, params.get("value3"),
                "String", String.class, "value3");
        String value4 = Marshal.in(ctx, params.get("value4"),
                "String", String.class, "value4");
        target.Designate(value1, value2, value3, value4);
        return Marshal.ok();
    }
}
