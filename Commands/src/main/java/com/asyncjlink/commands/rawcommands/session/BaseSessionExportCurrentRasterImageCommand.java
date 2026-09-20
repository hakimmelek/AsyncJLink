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
import com.ptc.pfc.pfcSession.BaseSession;
import com.ptc.pfc.pfcSession.Session;
import com.ptc.pfc.pfcWindow.RasterImageExportInstructions;

/**
 * BaseSession.ExportCurrentRasterImage &mdash; pfcSession.
 *
 * <pre>
 * void ExportCurrentRasterImage(String, RasterImageExportInstructions) throws jxthrowable
 * </pre>
 */
public final class BaseSessionExportCurrentRasterImageCommand implements Command {

    @Override public String name() { return "BaseSession.ExportCurrentRasterImage"; }
    @Override public String jlinkPackage() { return "pfcSession"; }
    @Override public String receiverType() { return "BaseSession"; }
    @Override public String signature() { return "void ExportCurrentRasterImage(String, RasterImageExportInstructions) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("BaseSession.ExportCurrentRasterImage \u2014 pfcSession")
                .optional("value", JsonSchema.string(),
                        "String value.")
                .optional("rasterImageExportInstructions", JsonSchema.dataObject("RasterImageExportInstructions"),
                        "RasterImageExportInstructions options object; its fields are passed to the pfc factory and setters.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Session target = ctx.session();
        String value = Marshal.in(ctx, params.get("value"),
                "String", String.class, "value");
        RasterImageExportInstructions rasterImageExportInstructions = Marshal.in(ctx, params.get("rasterImageExportInstructions"),
                "RasterImageExportInstructions", RasterImageExportInstructions.class, "rasterImageExportInstructions");
        target.ExportCurrentRasterImage(value, rasterImageExportInstructions);
        return Marshal.ok();
    }
}
