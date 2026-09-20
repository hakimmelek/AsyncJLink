/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.model2d;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModel2D.Model2D;

/**
 * Model2D.DeleteModel &mdash; pfcModel2D.
 *
 * <pre>
 * void DeleteModel(Model) throws jxthrowable
 * </pre>
 */
public final class Model2DDeleteModelCommand implements Command {

    @Override public String name() { return "Model2D.DeleteModel"; }
    @Override public String jlinkPackage() { return "pfcModel2D"; }
    @Override public String receiverType() { return "Model2D"; }
    @Override public String signature() { return "void DeleteModel(Model) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Model2D.DeleteModel \u2014 pfcModel2D")
                .required("target", JsonSchema.handle("Model2D"),
                        "The Model2D to act on.")
                .optional("model", JsonSchema.handle("Model"),
                        "Handle to a Model, as returned by an earlier command.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Model2D target = Marshal.in(ctx, params.get("target"),
                "Model2D", Model2D.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        Model model = Marshal.in(ctx, params.get("model"),
                "Model", Model.class, "model");
        target.DeleteModel(model);
        return Marshal.ok();
    }
}
