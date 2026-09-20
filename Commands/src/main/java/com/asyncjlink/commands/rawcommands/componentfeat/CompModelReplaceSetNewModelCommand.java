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
import com.ptc.pfc.pfcComponentFeat.CompModelReplace;
import com.ptc.pfc.pfcModel.Model;

/**
 * CompModelReplace.SetNewModel &mdash; pfcComponentFeat.
 *
 * <pre>
 * void SetNewModel(Model) throws jxthrowable
 * </pre>
 */
public final class CompModelReplaceSetNewModelCommand implements Command {

    @Override public String name() { return "CompModelReplace.SetNewModel"; }
    @Override public String jlinkPackage() { return "pfcComponentFeat"; }
    @Override public String receiverType() { return "CompModelReplace"; }
    @Override public String signature() { return "void SetNewModel(Model) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("CompModelReplace.SetNewModel \u2014 pfcComponentFeat")
                .required("target", JsonSchema.handle("CompModelReplace"),
                        "The CompModelReplace to act on.")
                .optional("model", JsonSchema.handle("Model"),
                        "Handle to a Model, as returned by an earlier command.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        CompModelReplace target = Marshal.in(ctx, params.get("target"),
                "CompModelReplace", CompModelReplace.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        Model model = Marshal.in(ctx, params.get("model"),
                "Model", Model.class, "model");
        target.SetNewModel(model);
        return Marshal.ok();
    }
}
