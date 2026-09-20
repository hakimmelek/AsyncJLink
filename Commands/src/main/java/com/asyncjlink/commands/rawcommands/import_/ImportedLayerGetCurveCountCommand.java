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
 * ImportedLayer.GetCurveCount &mdash; pfcImport.
 *
 * <pre>
 * int GetCurveCount() throws jxthrowable
 * </pre>
 */
public final class ImportedLayerGetCurveCountCommand implements Command {

    @Override public String name() { return "ImportedLayer.GetCurveCount"; }
    @Override public String jlinkPackage() { return "pfcImport"; }
    @Override public String receiverType() { return "ImportedLayer"; }
    @Override public String signature() { return "int GetCurveCount() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("ImportedLayer.GetCurveCount \u2014 pfcImport")
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
        return Marshal.result(ctx, target.GetCurveCount(), "int");
    }
}
