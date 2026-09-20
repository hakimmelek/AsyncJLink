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
 * Session.UIReadIntMessage &mdash; pfcSession.
 *
 * <pre>
 * Integer UIReadIntMessage(int, int) throws jxthrowable
 * </pre>
 */
public final class SessionUIReadIntMessageCommand implements Command {

    @Override public String name() { return "Session.UIReadIntMessage"; }
    @Override public String jlinkPackage() { return "pfcSession"; }
    @Override public String receiverType() { return "Session"; }
    @Override public String signature() { return "Integer UIReadIntMessage(int, int) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Session.UIReadIntMessage \u2014 pfcSession")
                .required("value1", JsonSchema.integer(),
                        "int value.")
                .required("value2", JsonSchema.integer(),
                        "int value.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Session target = ctx.session();
        int value1 = Marshal.in(ctx, params.get("value1"),
                "int", int.class, "value1");
        int value2 = Marshal.in(ctx, params.get("value2"),
                "int", int.class, "value2");
        return Marshal.result(ctx, target.UIReadIntMessage(value1, value2), "Integer");
    }
}
