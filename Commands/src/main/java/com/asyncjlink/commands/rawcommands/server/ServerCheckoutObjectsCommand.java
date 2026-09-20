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
import com.ptc.pfc.pfcServer.CheckoutOptions;
import com.ptc.pfc.pfcServer.Server;

/**
 * Server.CheckoutObjects &mdash; pfcServer.
 *
 * <pre>
 * String CheckoutObjects(Model, String, boolean, CheckoutOptions) throws jxthrowable
 * </pre>
 */
public final class ServerCheckoutObjectsCommand implements Command {

    @Override public String name() { return "Server.CheckoutObjects"; }
    @Override public String jlinkPackage() { return "pfcServer"; }
    @Override public String receiverType() { return "Server"; }
    @Override public String signature() { return "String CheckoutObjects(Model, String, boolean, CheckoutOptions) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Server.CheckoutObjects \u2014 pfcServer")
                .required("target", JsonSchema.handle("Server"),
                        "The Server to act on.")
                .optional("model", JsonSchema.handle("Model"),
                        "Handle to a Model, as returned by an earlier command.")
                .optional("value", JsonSchema.string(),
                        "String value.")
                .required("flag", JsonSchema.bool(),
                        "boolean value.")
                .optional("checkoutOptions", JsonSchema.dataObject("CheckoutOptions"),
                        "CheckoutOptions options object; its fields are passed to the pfc factory and setters.")
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
        String value = Marshal.in(ctx, params.get("value"),
                "String", String.class, "value");
        boolean flag = Marshal.in(ctx, params.get("flag"),
                "boolean", boolean.class, "flag");
        CheckoutOptions checkoutOptions = Marshal.in(ctx, params.get("checkoutOptions"),
                "CheckoutOptions", CheckoutOptions.class, "checkoutOptions");
        return Marshal.result(ctx, target.CheckoutObjects(model, value, flag, checkoutOptions), "String");
    }
}
