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
import com.ptc.pfc.pfcModel.ModelDescriptor;
import com.ptc.pfc.pfcSession.BaseSession;
import com.ptc.pfc.pfcSession.Session;

/**
 * BaseSession.RetrieveModel &mdash; pfcSession.
 *
 * <pre>
 * Model RetrieveModel(ModelDescriptor) throws jxthrowable
 * </pre>
 */
public final class BaseSessionRetrieveModelCommand implements Command {

    @Override public String name() { return "BaseSession.RetrieveModel"; }
    @Override public String jlinkPackage() { return "pfcSession"; }
    @Override public String receiverType() { return "BaseSession"; }
    @Override public String signature() { return "Model RetrieveModel(ModelDescriptor) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("BaseSession.RetrieveModel \u2014 pfcSession")
                .optional("modelDescriptor", JsonSchema.dataObject("ModelDescriptor"),
                        "ModelDescriptor options object; its fields are passed to the pfc factory and setters.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Session target = ctx.session();
        ModelDescriptor modelDescriptor = Marshal.in(ctx, params.get("modelDescriptor"),
                "ModelDescriptor", ModelDescriptor.class, "modelDescriptor");
        return Marshal.result(ctx, target.RetrieveModel(modelDescriptor), "Model");
    }
}
