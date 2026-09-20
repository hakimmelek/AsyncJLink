/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.session;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcCommand.UICommand;
import com.ptc.pfc.pfcSession.Session;

/**
 * Session.UIAddButton &mdash; pfcSession.
 *
 * <pre>
 * void UIAddButton(UICommand, String, String, String, String, String) throws jxthrowable
 * </pre>
 */
public final class SessionUIAddButtonCommand implements Command {

    @Override public String name() { return "Session.UIAddButton"; }
    @Override public String jlinkPackage() { return "pfcSession"; }
    @Override public String receiverType() { return "Session"; }
    @Override public String signature() { return "void UIAddButton(UICommand, String, String, String, String, String) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Session.UIAddButton \u2014 pfcSession")
                .optional("uiCommand", JsonSchema.handle("UICommand"),
                        "Handle to a UICommand, as returned by an earlier command.")
                .optional("value1", JsonSchema.string(),
                        "String value.")
                .optional("value2", JsonSchema.string(),
                        "String value.")
                .optional("value3", JsonSchema.string(),
                        "String value.")
                .optional("value4", JsonSchema.string(),
                        "String value.")
                .optional("value5", JsonSchema.string(),
                        "String value.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Session target = ctx.session();
        UICommand uiCommand = Marshal.in(ctx, params.get("uiCommand"),
                "UICommand", UICommand.class, "uiCommand");
        String value1 = Marshal.in(ctx, params.get("value1"),
                "String", String.class, "value1");
        String value2 = Marshal.in(ctx, params.get("value2"),
                "String", String.class, "value2");
        String value3 = Marshal.in(ctx, params.get("value3"),
                "String", String.class, "value3");
        String value4 = Marshal.in(ctx, params.get("value4"),
                "String", String.class, "value4");
        String value5 = Marshal.in(ctx, params.get("value5"),
                "String", String.class, "value5");
        target.UIAddButton(uiCommand, value1, value2, value3, value4, value5);
        return Marshal.ok();
    }
}
