package com.asyncjlink.commands.compositecommands.presentation;

import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcArgument.pfcArgument;
import com.ptc.pfc.pfcExport.PDFColorDepth;
import com.ptc.pfc.pfcExport.PDFExportInstructions;
import com.ptc.pfc.pfcExport.PDFFontStrokeMode;
import com.ptc.pfc.pfcExport.PDFOption;
import com.ptc.pfc.pfcExport.PDFOptionType;
import com.ptc.pfc.pfcExport.PDFOptions;
import com.ptc.pfc.pfcExport.pfcExport;
import com.ptc.pfc.pfcModel.ExportInstructions;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModel.pfcModel;

/**
 * Drawing.Export — a drawing to PDF or DXF, without building an instruction object by hand.
 *
 * <p>{@code Model.ExportPackage} handles any format generically, but at the cost of the caller
 * building the raw {@code ExportInstructions} object themselves — for the two formats a mechanical
 * engineer actually sends a drawing out as, that is more ceremony than the choice deserves. This is
 * the friendly front door for just those two: the options a person actually decides between.
 *
 * <p>{@code color} maps to {@code PDFOptionType.PDFOPT_COLOR_DEPTH} carrying a
 * {@code PDFColorDepth} wrapped in an {@code ArgValue} — {@code true}/{@code false} to
 * {@code PDF_CD_COLOR}/{@code PDF_CD_GRAY}; {@code PDF_CD_MONO} is not reachable through this
 * boolean and would need a three-way option instead.
 *
 * <p>{@code fontStroke} maps to {@code PDFOptionType.PDFOPT_FONT_STROKE} carrying a
 * {@code PDFFontStrokeMode} wrapped in an {@code ArgValue} — the same int-enum-in-an-ArgValue shape
 * as {@code color}, not the plain boolean {@code ArgValue} a first guess assumed (confirmed live:
 * that guess failed with {@code XToolkitInvalidType} before the enum was found in the dictionary).
 * {@code fontStroke: true} maps to {@code PDF_STROKE_ALL_FONTS}, {@code false} to
 * {@code PDF_USE_TRUE_TYPE_FONTS} — Creo's own "Use TrueType Fonts" export option, inverted.
 */
public final class DrawingExportCommand extends Composite {

    @Override public String name() { return "Drawing.Export"; }
    @Override public String receiverType() { return "Drawing"; }
    @Override public String signature() { return "Export(target, path[, format, color]) — composite"; }

    @Override
    public String description() {
        return "Export a drawing to PDF or DXF. For PDF, 'color' chooses full colour or "
                + "greyscale, and 'fontStroke' can turn off embedded TrueType text.";
    }

    @Override
    public JsonSchema paramSchema() {
        return targeted(name(), "Drawing", "The drawing to export.")
                .required("path", JsonSchema.string(), "Output file path.")
                .optional("format", JsonSchema.string(),
                        "\"PDF\" or \"DXF\". Defaults to the extension on 'path'.")
                .optional("color", JsonSchema.bool(),
                        "PDF only: full colour (true) or greyscale (false). Default true.")
                .optional("fontStroke", JsonSchema.bool(),
                        "PDF only: draw text as vector strokes instead of embedded TrueType "
                                + "fonts (true disables TrueType). Defaults to Creo's own setting "
                                + "when omitted.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Model drawing = requireModel(ctx, params);
        String path = params.getString("path", null);
        if (path == null || path.isEmpty()) {
            throw new CommandException("Field 'path' is required", "invalid_params");
        }
        String format = params.getString("format", null);
        if (format == null || format.isEmpty()) {
            format = guessFormat(path);
        }
        format = format.trim().toUpperCase(java.util.Locale.ROOT);

        Boolean fontStroke = params.get("fontStroke") == null
                ? null : Boolean.valueOf(params.getBoolean("fontStroke", false));

        String resolvedPath = ctx.resolveOutputPath(path);
        ExportInstructions instructions;
        switch (format) {
            case "PDF":
                instructions = pdfInstructions(
                        resolvedPath, params.getBoolean("color", true), fontStroke);
                break;
            case "DXF":
                instructions = pfcModel.DXFExportInstructions_Create();
                break;
            default:
                throw new CommandException(
                        "Field 'format' must be PDF or DXF, was '" + format + "'", "invalid_params");
        }

        try {
            drawing.Export(resolvedPath, instructions);
        } catch (jxthrowable e) {
            throw new CommandException(
                    "Could not export to " + format + ": " + rootMessage(e), "creo_error", e);
        }

        JsonObject out = JsonObject.of(
                "drawing", modelRef(ctx, drawing),
                "format", format,
                "path", resolvedPath,
                "written", Boolean.TRUE);
        if ("PDF".equals(format)) {
            out.put("color", Boolean.valueOf(params.getBoolean("color", true)));
            out.putIfPresent("fontStroke", fontStroke);
        }
        return out;
    }

    private static PDFExportInstructions pdfInstructions(String path, boolean color, Boolean fontStroke)
            throws jxthrowable {
        PDFExportInstructions instructions = pfcExport.PDFExportInstructions_Create();
        instructions.SetFilePath(path);

        PDFOptions options = PDFOptions.create();

        PDFOption colorOption = pfcExport.PDFOption_Create();
        colorOption.SetOptionType(PDFOptionType.PDFOPT_COLOR_DEPTH);
        PDFColorDepth depth = color ? PDFColorDepth.PDF_CD_COLOR : PDFColorDepth.PDF_CD_GRAY;
        colorOption.SetOptionValue(pfcArgument.CreateIntArgValue(depth.getValue()));
        options.append(colorOption);

        if (fontStroke != null) {
            PDFOption fontOption = pfcExport.PDFOption_Create();
            fontOption.SetOptionType(PDFOptionType.PDFOPT_FONT_STROKE);
            PDFFontStrokeMode mode = fontStroke.booleanValue()
                    ? PDFFontStrokeMode.PDF_STROKE_ALL_FONTS
                    : PDFFontStrokeMode.PDF_USE_TRUE_TYPE_FONTS;
            fontOption.SetOptionValue(pfcArgument.CreateIntArgValue(mode.getValue()));
            options.append(fontOption);
        }

        instructions.SetOptions(options);
        return instructions;
    }

    private static String guessFormat(String path) {
        String lower = path.toLowerCase(java.util.Locale.ROOT);
        if (lower.endsWith(".dxf")) {
            return "DXF";
        }
        if (lower.endsWith(".pdf")) {
            return "PDF";
        }
        throw new CommandException(
                "Cannot guess a format from '" + path + "'; pass 'format' explicitly",
                "invalid_params");
    }
}
