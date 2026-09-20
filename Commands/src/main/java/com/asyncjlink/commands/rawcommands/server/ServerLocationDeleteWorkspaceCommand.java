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
import com.ptc.pfc.pfcServer.ServerLocation;

/**
 * ServerLocation.DeleteWorkspace &mdash; pfcServer.
 *
 * <pre>
 * void DeleteWorkspace(String) throws jxthrowable
 * </pre>
 */
public final class ServerLocationDeleteWorkspaceCommand implements Command {

    @Override public String name() { return "ServerLocation.DeleteWorkspace"; }
    @Override public String jlinkPackage() { return "pfcServer"; }
    @Override public String receiverType() { return "ServerLocation"; }
    @Override public String signature() { return "void DeleteWorkspace(String) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("ServerLocation.DeleteWorkspace \u2014 pfcServer")
                .required("target", JsonSchema.handle("ServerLocation"),
                        "The ServerLocation to act on.")
                .optional("value", JsonSchema.string(),
                        "String value.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        ServerLocation target = Marshal.in(ctx, params.get("target"),
                "ServerLocation", ServerLocation.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        String value = Marshal.in(ctx, params.get("value"),
                "String", String.class, "value");
        target.DeleteWorkspace(value);
        return Marshal.ok();
    }
}
