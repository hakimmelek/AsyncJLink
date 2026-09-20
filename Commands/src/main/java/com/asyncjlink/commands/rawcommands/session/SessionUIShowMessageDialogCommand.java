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
import com.ptc.pfc.pfcSession.Session;
import com.ptc.pfc.pfcUI.MessageDialogOptions;

/**
 * Session.UIShowMessageDialog &mdash; pfcSession.
 *
 * <pre>
 * MessageButton UIShowMessageDialog(String, MessageDialogOptions) throws jxthrowable
 * </pre>
 */
public final class SessionUIShowMessageDialogCommand implements Command {

    @Override public String name() { return "Session.UIShowMessageDialog"; }
    @Override public String jlinkPackage() { return "pfcSession"; }
    @Override public String receiverType() { return "Session"; }
    @Override public String signature() { return "MessageButton UIShowMessageDialog(String, MessageDialogOptions) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Session.UIShowMessageDialog \u2014 pfcSession")
                .optional("value", JsonSchema.string(),
                        "String value.")
                .optional("messageDialogOptions", JsonSchema.dataObject("MessageDialogOptions"),
                        "MessageDialogOptions options object; its fields are passed to the pfc factory and setters.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Session target = ctx.session();
        String value = Marshal.in(ctx, params.get("value"),
                "String", String.class, "value");
        MessageDialogOptions messageDialogOptions = Marshal.in(ctx, params.get("messageDialogOptions"),
                "MessageDialogOptions", MessageDialogOptions.class, "messageDialogOptions");
        return Marshal.result(ctx, target.UIShowMessageDialog(value, messageDialogOptions), "MessageButton");
    }
}
