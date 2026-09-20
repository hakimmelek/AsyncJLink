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
 * Session.UIReadRealMessage &mdash; pfcSession.
 *
 * <pre>
 * Double UIReadRealMessage(double, double) throws jxthrowable
 * </pre>
 */
public final class SessionUIReadRealMessageCommand implements Command {

    @Override public String name() { return "Session.UIReadRealMessage"; }
    @Override public String jlinkPackage() { return "pfcSession"; }
    @Override public String receiverType() { return "Session"; }
    @Override public String signature() { return "Double UIReadRealMessage(double, double) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Session.UIReadRealMessage \u2014 pfcSession")
                .required("value1", JsonSchema.number(),
                        "double value.")
                .required("value2", JsonSchema.number(),
                        "double value.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Session target = ctx.session();
        double value1 = Marshal.in(ctx, params.get("value1"),
                "double", double.class, "value1");
        double value2 = Marshal.in(ctx, params.get("value2"),
                "double", double.class, "value2");
        return Marshal.result(ctx, target.UIReadRealMessage(value1, value2), "Double");
    }
}
