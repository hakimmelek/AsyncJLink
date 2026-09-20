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
 * Session.NavigatorPaneBrowserIconSet &mdash; pfcSession.
 *
 * <pre>
 * void NavigatorPaneBrowserIconSet(String, String) throws jxthrowable
 * </pre>
 */
public final class SessionNavigatorPaneBrowserIconSetCommand implements Command {

    @Override public String name() { return "Session.NavigatorPaneBrowserIconSet"; }
    @Override public String jlinkPackage() { return "pfcSession"; }
    @Override public String receiverType() { return "Session"; }
    @Override public String signature() { return "void NavigatorPaneBrowserIconSet(String, String) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Session.NavigatorPaneBrowserIconSet \u2014 pfcSession")
                .optional("value1", JsonSchema.string(),
                        "String value.")
                .optional("value2", JsonSchema.string(),
                        "String value.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Session target = ctx.session();
        String value1 = Marshal.in(ctx, params.get("value1"),
                "String", String.class, "value1");
        String value2 = Marshal.in(ctx, params.get("value2"),
                "String", String.class, "value2");
        target.NavigatorPaneBrowserIconSet(value1, value2);
        return Marshal.ok();
    }
}
