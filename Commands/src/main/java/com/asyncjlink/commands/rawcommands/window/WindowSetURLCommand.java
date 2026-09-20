/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.window;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcWindow.Window;

/**
 * Window.SetURL &mdash; pfcWindow.
 *
 * <pre>
 * void SetURL(String) throws jxthrowable
 * </pre>
 */
public final class WindowSetURLCommand implements Command {

    @Override public String name() { return "Window.SetURL"; }
    @Override public String jlinkPackage() { return "pfcWindow"; }
    @Override public String receiverType() { return "Window"; }
    @Override public String signature() { return "void SetURL(String) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Window.SetURL \u2014 pfcWindow")
                .required("target", JsonSchema.handle("Window"),
                        "The Window to act on.")
                .optional("value", JsonSchema.string(),
                        "String value.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Window target = Marshal.in(ctx, params.get("target"),
                "Window", Window.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        String value = Marshal.in(ctx, params.get("value"),
                "String", String.class, "value");
        target.SetURL(value);
        return Marshal.ok();
    }
}
