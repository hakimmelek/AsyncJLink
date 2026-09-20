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
import com.ptc.pfc.pfcModelCheck.ModelCheckInstructions;
import com.ptc.pfc.pfcSession.BaseSession;
import com.ptc.pfc.pfcSession.Session;

/**
 * BaseSession.ExecuteModelCheck &mdash; pfcSession.
 *
 * <pre>
 * ModelCheckResults ExecuteModelCheck(Model, ModelCheckInstructions) throws jxthrowable
 * </pre>
 */
public final class BaseSessionExecuteModelCheckCommand implements Command {

    @Override public String name() { return "BaseSession.ExecuteModelCheck"; }
    @Override public String jlinkPackage() { return "pfcSession"; }
    @Override public String receiverType() { return "BaseSession"; }
    @Override public String signature() { return "ModelCheckResults ExecuteModelCheck(Model, ModelCheckInstructions) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("BaseSession.ExecuteModelCheck \u2014 pfcSession")
                .optional("model", JsonSchema.handle("Model"),
                        "Handle to a Model, as returned by an earlier command.")
                .optional("modelCheckInstructions", JsonSchema.dataObject("ModelCheckInstructions"),
                        "ModelCheckInstructions options object; its fields are passed to the pfc factory and setters.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Session target = ctx.session();
        Model model = Marshal.in(ctx, params.get("model"),
                "Model", Model.class, "model");
        ModelCheckInstructions modelCheckInstructions = Marshal.in(ctx, params.get("modelCheckInstructions"),
                "ModelCheckInstructions", ModelCheckInstructions.class, "modelCheckInstructions");
        return Marshal.result(ctx, target.ExecuteModelCheck(model, modelCheckInstructions), "ModelCheckResults");
    }
}
