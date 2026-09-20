/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.import_;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcImport.ImportedLayer;

/**
 * ImportedLayer.GetTrimmedSurfaceCount &mdash; pfcImport.
 *
 * <pre>
 * int GetTrimmedSurfaceCount() throws jxthrowable
 * </pre>
 */
public final class ImportedLayerGetTrimmedSurfaceCountCommand implements Command {

    @Override public String name() { return "ImportedLayer.GetTrimmedSurfaceCount"; }
    @Override public String jlinkPackage() { return "pfcImport"; }
    @Override public String receiverType() { return "ImportedLayer"; }
    @Override public String signature() { return "int GetTrimmedSurfaceCount() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("ImportedLayer.GetTrimmedSurfaceCount \u2014 pfcImport")
                .required("target", JsonSchema.handle("ImportedLayer"),
                        "The ImportedLayer to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        ImportedLayer target = Marshal.in(ctx, params.get("target"),
                "ImportedLayer", ImportedLayer.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetTrimmedSurfaceCount(), "int");
    }
}
