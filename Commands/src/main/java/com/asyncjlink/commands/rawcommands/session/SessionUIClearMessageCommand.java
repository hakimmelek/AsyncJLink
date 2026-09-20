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

/**
 * Session.UIClearMessage &mdash; pfcSession.
 *
 * <pre>
 * void UIClearMessage() throws jxthrowable
 * </pre>
 */
public final class SessionUIClearMessageCommand implements Command {

    @Override public String name() { return "Session.UIClearMessage"; }
    @Override public String jlinkPackage() { return "pfcSession"; }
    @Override public String receiverType() { return "Session"; }
    @Override public String signature() { return "void UIClearMessage() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Session.UIClearMessage \u2014 pfcSession")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Session target = ctx.session();
        target.UIClearMessage();
        return Marshal.ok();
    }
}
