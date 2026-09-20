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
import com.ptc.pfc.pfcModel.ModelDescriptors;
import com.ptc.pfc.pfcSession.BaseSession;
import com.ptc.pfc.pfcSession.RelCriterion;
import com.ptc.pfc.pfcSession.Session;

/**
 * BaseSession.CheckoutToWS &mdash; pfcSession.
 *
 * <pre>
 * void CheckoutToWS(ModelDescriptors, String, boolean, RelCriterion) throws jxthrowable
 * </pre>
 */
public final class BaseSessionCheckoutToWSCommand implements Command {

    @Override public String name() { return "BaseSession.CheckoutToWS"; }
    @Override public String jlinkPackage() { return "pfcSession"; }
    @Override public String receiverType() { return "BaseSession"; }
    @Override public String signature() { return "void CheckoutToWS(ModelDescriptors, String, boolean, RelCriterion) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("BaseSession.CheckoutToWS \u2014 pfcSession")
                .optional("modelDescriptors", JsonSchema.sequence("ModelDescriptors", JsonSchema.dataObject("ModelDescriptor")),
                        "Array of ModelDescriptor.")
                .optional("value", JsonSchema.string(),
                        "String value.")
                .required("flag", JsonSchema.bool(),
                        "boolean value.")
                .optional("relCriterion", JsonSchema.enumOf("RelCriterion", "FILE_INCLUDE_ALL", "FILE_INCLUDE_REQUIRED", "FILE_INCLUDE_NONE"),
                        "One of the RelCriterion constants.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Session target = ctx.session();
        ModelDescriptors modelDescriptors = Marshal.in(ctx, params.get("modelDescriptors"),
                "ModelDescriptors", ModelDescriptors.class, "modelDescriptors");
        String value = Marshal.in(ctx, params.get("value"),
                "String", String.class, "value");
        boolean flag = Marshal.in(ctx, params.get("flag"),
                "boolean", boolean.class, "flag");
        RelCriterion relCriterion = Marshal.in(ctx, params.get("relCriterion"),
                "RelCriterion", RelCriterion.class, "relCriterion");
        target.CheckoutToWS(modelDescriptors, value, flag, relCriterion);
        return Marshal.ok();
    }
}
