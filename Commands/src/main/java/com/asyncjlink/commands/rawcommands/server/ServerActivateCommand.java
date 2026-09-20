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
 * Server.Activate &mdash; pfcServer.
 *
 * <pre>
 * void Activate() throws jxthrowable
 * </pre>
 */
public final class ServerActivateCommand implements Command {

    @Override public String name() { return "Server.Activate"; }
    @Override public String jlinkPackage() { return "pfcServer"; }
    @Override public String receiverType() { return "Server"; }
    @Override public String signature() { return "void Activate() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Server.Activate \u2014 pfcServer")
                .required("target", JsonSchema.handle("Server"),
                        "The Server to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Server target = Marshal.in(ctx, params.get("target"),
                "Server", Server.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        target.Activate();
        return Marshal.ok();
    }
}
