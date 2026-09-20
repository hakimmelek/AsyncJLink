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
 * UICommand.SetIcon &mdash; pfcCommand.
 *
 * <pre>
 * void SetIcon(String) throws jxthrowable
 * </pre>
 */
public final class UICommandSetIconCommand implements Command {

    @Override public String name() { return "UICommand.SetIcon"; }
    @Override public String jlinkPackage() { return "pfcCommand"; }
    @Override public String receiverType() { return "UICommand"; }
    @Override public String signature() { return "void SetIcon(String) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("UICommand.SetIcon \u2014 pfcCommand")
                .required("target", JsonSchema.handle("UICommand"),
                        "The UICommand to act on.")
                .optional("value", JsonSchema.string(),
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
        String value = Marshal.in(ctx, params.get("value"),
                "String", String.class, "value");
        target.SetIcon(value);
        return Marshal.ok();
    }
}
