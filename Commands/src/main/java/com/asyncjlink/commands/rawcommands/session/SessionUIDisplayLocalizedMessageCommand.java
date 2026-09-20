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
import com.ptc.cipjava.stringseq;
import com.ptc.pfc.pfcSession.Session;

/**
 * Session.UIDisplayLocalizedMessage &mdash; pfcSession.
 *
 * <pre>
 * void UIDisplayLocalizedMessage(String, String, stringseq) throws jxthrowable
 * </pre>
 */
public final class SessionUIDisplayLocalizedMessageCommand implements Command {

    @Override public String name() { return "Session.UIDisplayLocalizedMessage"; }
    @Override public String jlinkPackage() { return "pfcSession"; }
    @Override public String receiverType() { return "Session"; }
    @Override public String signature() { return "void UIDisplayLocalizedMessage(String, String, stringseq) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Session.UIDisplayLocalizedMessage \u2014 pfcSession")
                .optional("value1", JsonSchema.string(),
                        "String value.")
                .optional("value2", JsonSchema.string(),
                        "String value.")
                .optional("values", JsonSchema.sequence("stringseq", JsonSchema.string()),
                        "Array of String.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Session target = ctx.session();
        String value1 = Marshal.in(ctx, params.get("value1"),
                "String", String.class, "value1");
        String value2 = Marshal.in(ctx, params.get("value2"),
                "String", String.class, "value2");
        stringseq values = Marshal.in(ctx, params.get("values"),
                "stringseq", stringseq.class, "values");
        target.UIDisplayLocalizedMessage(value1, value2, values);
        return Marshal.ok();
    }
}
