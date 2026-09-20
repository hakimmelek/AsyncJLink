/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.window;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcWindow.RasterImageExportInstructions;
import com.ptc.pfc.pfcWindow.Window;

/**
 * Window.ExportRasterImage &mdash; pfcWindow.
 *
 * <pre>
 * void ExportRasterImage(String, RasterImageExportInstructions) throws jxthrowable
 * </pre>
 */
public final class WindowExportRasterImageCommand implements Command {

    @Override public String name() { return "Window.ExportRasterImage"; }
    @Override public String jlinkPackage() { return "pfcWindow"; }
    @Override public String receiverType() { return "Window"; }
    @Override public String signature() { return "void ExportRasterImage(String, RasterImageExportInstructions) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Window.ExportRasterImage \u2014 pfcWindow")
                .required("target", JsonSchema.handle("Window"),
                        "The Window to act on.")
                .optional("value", JsonSchema.string(),
                        "String value.")
                .optional("rasterImageExportInstructions", JsonSchema.dataObject("RasterImageExportInstructions"),
                        "RasterImageExportInstructions options object; its fields are passed to the pfc factory and setters.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Window target = Marshal.in(ctx, params.get("target"),
                "Window", Window.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        String value = Marshal.in(ctx, params.get("value"),
                "String", String.class, "value");
        RasterImageExportInstructions rasterImageExportInstructions = Marshal.in(ctx, params.get("rasterImageExportInstructions"),
                "RasterImageExportInstructions", RasterImageExportInstructions.class, "rasterImageExportInstructions");
        target.ExportRasterImage(value, rasterImageExportInstructions);
        return Marshal.ok();
    }
}
