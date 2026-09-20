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
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcSession.BaseSession;
import com.ptc.pfc.pfcSession.Session;

/**
 * BaseSession.GetPrintPCFOptions &mdash; pfcSession.
 *
 * <pre>
 * PrinterPCFOptions GetPrintPCFOptions(String, Model) throws jxthrowable
 * </pre>
 */
public final class BaseSessionGetPrintPCFOptionsCommand implements Command {

    @Override public String name() { return "BaseSession.GetPrintPCFOptions"; }
    @Override public String jlinkPackage() { return "pfcSession"; }
    @Override public String receiverType() { return "BaseSession"; }
    @Override public String signature() { return "PrinterPCFOptions GetPrintPCFOptions(String, Model) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("BaseSession.GetPrintPCFOptions \u2014 pfcSession")
                .optional("value", JsonSchema.string(),
                        "String value.")
                .optional("model", JsonSchema.handle("Model"),
                        "Handle to a Model, as returned by an earlier command.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Session target = ctx.session();
        String value = Marshal.in(ctx, params.get("value"),
                "String", String.class, "value");
        Model model = Marshal.in(ctx, params.get("model"),
                "Model", Model.class, "model");
        return Marshal.result(ctx, target.GetPrintPCFOptions(value, model), "PrinterPCFOptions");
    }
}
