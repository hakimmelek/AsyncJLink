/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.componentfeat;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcComponentFeat.ComponentFeat;
import com.ptc.pfc.pfcModel.Model;

/**
 * ComponentFeat.CreateReplaceOp &mdash; pfcComponentFeat.
 *
 * <pre>
 * CompModelReplace CreateReplaceOp(Model) throws jxthrowable
 * </pre>
 */
public final class ComponentFeatCreateReplaceOpCommand implements Command {

    @Override public String name() { return "ComponentFeat.CreateReplaceOp"; }
    @Override public String jlinkPackage() { return "pfcComponentFeat"; }
    @Override public String receiverType() { return "ComponentFeat"; }
    @Override public String signature() { return "CompModelReplace CreateReplaceOp(Model) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("ComponentFeat.CreateReplaceOp \u2014 pfcComponentFeat")
                .required("target", JsonSchema.handle("ComponentFeat"),
                        "The ComponentFeat to act on.")
                .optional("model", JsonSchema.handle("Model"),
                        "Handle to a Model, as returned by an earlier command.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        ComponentFeat target = Marshal.in(ctx, params.get("target"),
                "ComponentFeat", ComponentFeat.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        Model model = Marshal.in(ctx, params.get("model"),
                "Model", Model.class, "model");
        return Marshal.result(ctx, target.CreateReplaceOp(model), "CompModelReplace");
    }
}
