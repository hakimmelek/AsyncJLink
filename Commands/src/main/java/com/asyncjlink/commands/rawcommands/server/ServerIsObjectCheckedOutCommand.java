/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.server;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcServer.Server;

/**
 * Server.IsObjectCheckedOut &mdash; pfcServer.
 *
 * <pre>
 * boolean IsObjectCheckedOut(String, String) throws jxthrowable
 * </pre>
 */
public final class ServerIsObjectCheckedOutCommand implements Command {

    @Override public String name() { return "Server.IsObjectCheckedOut"; }
    @Override public String jlinkPackage() { return "pfcServer"; }
    @Override public String receiverType() { return "Server"; }
    @Override public String signature() { return "boolean IsObjectCheckedOut(String, String) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Server.IsObjectCheckedOut \u2014 pfcServer")
                .required("target", JsonSchema.handle("Server"),
                        "The Server to act on.")
                .optional("value1", JsonSchema.string(),
                        "String value.")
                .optional("value2", JsonSchema.string(),
                        "String value.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Server target = Marshal.in(ctx, params.get("target"),
                "Server", Server.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        String value1 = Marshal.in(ctx, params.get("value1"),
                "String", String.class, "value1");
        String value2 = Marshal.in(ctx, params.get("value2"),
                "String", String.class, "value2");
        return Marshal.result(ctx, target.IsObjectCheckedOut(value1, value2), "boolean");
    }
}
