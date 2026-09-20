/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.model;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcModel.ImportInstructions;
import com.ptc.pfc.pfcModel.Model;

/**
 * Model.Import &mdash; pfcModel.
 *
 * <pre>
 * void Import(String, ImportInstructions) throws jxthrowable
 * </pre>
 */
public final class ModelImportCommand implements Command {

    @Override public String name() { return "Model.Import"; }
    @Override public String jlinkPackage() { return "pfcModel"; }
    @Override public String receiverType() { return "Model"; }
    @Override public String signature() { return "void Import(String, ImportInstructions) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Model.Import \u2014 pfcModel")
                .required("target", JsonSchema.handle("Model"),
                        "The Model to act on.")
                .optional("value", JsonSchema.string(),
                        "String value.")
                .optional("importInstructions", JsonSchema.dataObject("ImportInstructions"),
                        "ImportInstructions options object; its fields are passed to the pfc factory and setters.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Model target = Marshal.in(ctx, params.get("target"),
                "Model", Model.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        String value = Marshal.in(ctx, params.get("value"),
                "String", String.class, "value");
        ImportInstructions importInstructions = Marshal.in(ctx, params.get("importInstructions"),
                "ImportInstructions", ImportInstructions.class, "importInstructions");
        target.Import(value, importInstructions);
        return Marshal.ok();
    }
}
