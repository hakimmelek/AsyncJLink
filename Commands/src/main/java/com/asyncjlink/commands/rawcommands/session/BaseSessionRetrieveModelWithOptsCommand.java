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
import com.ptc.pfc.pfcSession.RetrieveModelOptions;
import com.ptc.pfc.pfcSession.Session;

/**
 * BaseSession.RetrieveModelWithOpts &mdash; pfcSession.
 *
 * <pre>
 * Model RetrieveModelWithOpts(ModelDescriptor, RetrieveModelOptions) throws jxthrowable
 * </pre>
 */
public final class BaseSessionRetrieveModelWithOptsCommand implements Command {

    @Override public String name() { return "BaseSession.RetrieveModelWithOpts"; }
    @Override public String jlinkPackage() { return "pfcSession"; }
    @Override public String receiverType() { return "BaseSession"; }
    @Override public String signature() { return "Model RetrieveModelWithOpts(ModelDescriptor, RetrieveModelOptions) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("BaseSession.RetrieveModelWithOpts \u2014 pfcSession")
                .optional("modelDescriptor", JsonSchema.dataObject("ModelDescriptor"),
                        "ModelDescriptor options object; its fields are passed to the pfc factory and setters.")
                .optional("retrieveModelOptions", JsonSchema.dataObject("RetrieveModelOptions"),
                        "RetrieveModelOptions options object; its fields are passed to the pfc factory and setters.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Session target = ctx.session();
        ModelDescriptor modelDescriptor = Marshal.in(ctx, params.get("modelDescriptor"),
                "ModelDescriptor", ModelDescriptor.class, "modelDescriptor");
        RetrieveModelOptions retrieveModelOptions = Marshal.in(ctx, params.get("retrieveModelOptions"),
                "RetrieveModelOptions", RetrieveModelOptions.class, "retrieveModelOptions");
        return Marshal.result(ctx, target.RetrieveModelWithOpts(modelDescriptor, retrieveModelOptions), "Model");
    }
}
