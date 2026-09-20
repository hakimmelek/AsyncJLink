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
import com.ptc.pfc.pfcModel.ExportInstructions;
import com.ptc.pfc.pfcModel.Model;

/**
 * Model.Export &mdash; pfcModel.
 *
 * <pre>
 * void Export(String, ExportInstructions) throws jxthrowable
 * </pre>
 */
public final class ModelExportCommand implements Command {

    @Override public String name() { return "Model.Export"; }
    @Override public String jlinkPackage() { return "pfcModel"; }
    @Override public String receiverType() { return "Model"; }
    @Override public String signature() { return "void Export(String, ExportInstructions) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Model.Export \u2014 pfcModel")
                .required("target", JsonSchema.handle("Model"),
                        "The Model to act on.")
                .optional("value", JsonSchema.string(),
                        "String value.")
                .optional("exportInstructions", JsonSchema.dataObject("ExportInstructions"),
                        "ExportInstructions options object; its fields are passed to the pfc factory and setters.")
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
        ExportInstructions exportInstructions = Marshal.in(ctx, params.get("exportInstructions"),
                "ExportInstructions", ExportInstructions.class, "exportInstructions");
        target.Export(value, exportInstructions);
        return Marshal.ok();
    }
}
