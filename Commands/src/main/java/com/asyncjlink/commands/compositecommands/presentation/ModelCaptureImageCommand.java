package com.asyncjlink.commands.compositecommands.presentation;

import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcWindow.RasterImageExportInstructions;
import com.ptc.pfc.pfcWindow.Window;
import com.ptc.pfc.pfcWindow.pfcWindow;

/**
 * Model.CaptureImage — write a rendered view of the model to disk.
 *
 * <p>So a caller that cannot see the screen can see the model. Pair with {@code Model.SetView} to
 * control what is framed.
 */
public final class ModelCaptureImageCommand extends Composite {

    private static final double DEFAULT_SIZE = 10.0;

    @Override public String name() { return "Model.CaptureImage"; }
    @Override public String receiverType() { return "Model"; }
    @Override public String signature() { return "CaptureImage(target, path[, format, width, height]) — composite"; }

    @Override
    public String description() {
        return "Export a raster image of the model's window to a file, so a caller with no screen "
                + "can see the geometry. Use Model.SetView first to frame it.";
    }

    @Override
    public JsonSchema paramSchema() {
        return targeted(name(), "Model", "The model to capture.")
                .required("path", JsonSchema.string(),
                        "Output file. Relative paths land in the configured output directory.")
                .optional("format", JsonSchema.enumOf("ImageFormat", "BITMAP", "TIFF"),
                        "Image format. Default BITMAP.")
                .optional("width", JsonSchema.number(), "Image width in Creo's units. Default 10.")
                .optional("height", JsonSchema.number(), "Image height in Creo's units. Default 10.")
                .optional("repaint", JsonSchema.bool(),
                        "Repaint before capturing. Default true.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Model model = requireModel(ctx, params);
        String path = params.getString("path", null);
        if (path == null || path.isEmpty()) {
            throw new CommandException("Field 'path' is required", "invalid_params");
        }
        String resolved = ctx.resolveOutputPath(path);

        Window window = ctx.session().GetModelWindow(model);
        if (window == null) {
            throw new CommandException(
                    "This model has no open window, so there is nothing to capture. "
                            + "Load it with Session.LoadModel first.", "invalid_params");
        }

        double width = params.getDouble("width", DEFAULT_SIZE);
        double height = params.getDouble("height", DEFAULT_SIZE);
        String format = params.getString("format", "BITMAP").trim().toUpperCase(java.util.Locale.ROOT);

        RasterImageExportInstructions instructions;
        if (format.startsWith("TIFF")) {
            instructions = pfcWindow.TIFFImageExportInstructions_Create(width, height);
        } else {
            instructions = pfcWindow.BitmapImageExportInstructions_Create(width, height);
        }

        if (params.getBoolean("repaint", true)) {
            try {
                window.Repaint();
            } catch (jxthrowable | RuntimeException ignored) {
                // A failed repaint does not prevent the export; the image may just be stale.
            }
        }

        try {
            window.ExportRasterImage(resolved, instructions);
        } catch (jxthrowable e) {
            throw new CommandException(
                    "Creo could not write the image to '" + resolved + "': " + rootMessage(e),
                    "creo_error", e);
        }

        return JsonObject.of(
                "model", modelRef(ctx, model),
                "path", resolved,
                "format", format.startsWith("TIFF") ? "TIFF" : "BITMAP",
                "written", Boolean.TRUE);
    }
}
