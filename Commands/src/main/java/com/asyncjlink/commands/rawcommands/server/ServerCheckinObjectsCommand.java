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
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcServer.CheckinOptions;
import com.ptc.pfc.pfcServer.Server;

/**
 * Server.CheckinObjects &mdash; pfcServer.
 *
 * <pre>
 * void CheckinObjects(Model, CheckinOptions) throws jxthrowable
 * </pre>
 */
public final class ServerCheckinObjectsCommand implements Command {

    @Override public String name() { return "Server.CheckinObjects"; }
    @Override public String jlinkPackage() { return "pfcServer"; }
    @Override public String receiverType() { return "Server"; }
    @Override public String signature() { return "void CheckinObjects(Model, CheckinOptions) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Server.CheckinObjects \u2014 pfcServer")
                .required("target", JsonSchema.handle("Server"),
                        "The Server to act on.")
                .optional("model", JsonSchema.handle("Model"),
                        "Handle to a Model, as returned by an earlier command.")
                .optional("checkinOptions", JsonSchema.dataObject("CheckinOptions"),
                        "CheckinOptions options object; its fields are passed to the pfc factory and setters.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Server target = Marshal.in(ctx, params.get("target"),
                "Server", Server.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        Model model = Marshal.in(ctx, params.get("model"),
                "Model", Model.class, "model");
        CheckinOptions checkinOptions = Marshal.in(ctx, params.get("checkinOptions"),
                "CheckinOptions", CheckinOptions.class, "checkinOptions");
        target.CheckinObjects(model, checkinOptions);
        return Marshal.ok();
    }
}
