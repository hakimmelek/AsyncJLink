package com.asyncjlink.commands.compositecommands.presentation;

import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.DataObjects;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonArray;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcModel.ExportInstructions;
import com.ptc.pfc.pfcModel.Model;

/**
 * Model.ExportPackage — the whole supplier package in one call, with a manifest.
 *
 * <p>Several formats under a consistent naming convention, and a record of exactly what was written.
 * The point is to remove the half-finished export set: when one format fails, the caller is told
 * which, rather than discovering it when the supplier does.
 */
public final class ModelExportPackageCommand extends Composite {

    @Override public String name() { return "Model.ExportPackage"; }
    @Override public String receiverType() { return "Model"; }
    @Override public String signature() { return "ExportPackage(target, formats[, directory, baseName]) — composite"; }

    @Override
    public String description() {
        return "Export a model to several formats at once (STEP, IGES, STL, DXF, PDF...) under one "
                + "naming convention, returning a manifest of what was written and what failed.";
    }

    @Override
    public JsonSchema paramSchema() {
        return targeted(name(), "Model", "The model to export.")
                .required("formats", JsonSchema.array(JsonSchema.string()),
                        "Format names, e.g. [\"STEP\",\"IGES\",\"STL\"]. Each maps to a J-Link "
                                + "ExportType such as EXPORT_STEP.")
                .optional("directory", JsonSchema.string(),
                        "Output directory. Defaults to the configured output directory.")
                .optional("baseName", JsonSchema.string(),
                        "Base file name. Defaults to the model's own name.")
                .optional("instructions", JsonSchema.dataObject("ExportInstructions"),
                        "Per-format instruction overrides, keyed by format name.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Model model = requireModel(ctx, params);
        JsonArray formats = params.getArray("formats");
        if (formats == null || formats.size() == 0) {
            throw new CommandException(
                    "Field 'formats' must list at least one format", "invalid_params");
        }

        String baseName = params.getString("baseName", null);
        if (baseName == null || baseName.isEmpty()) {
            baseName = stripExtension(model.GetFullName());
        }
        String directory = params.getString("directory", null);
        JsonObject overrides = params.getObject("instructions");

        JsonArray manifest = new JsonArray();
        int written = 0;
        int failed = 0;

        for (int i = 0; i < formats.size(); i++) {
            String format = String.valueOf(formats.get(i)).trim().toUpperCase(java.util.Locale.ROOT);
            String fileName = baseName + "." + extensionFor(format);
            String path = directory == null || directory.isEmpty()
                    ? ctx.resolveOutputPath(fileName)
                    : join(directory, fileName);

            JsonObject entry = JsonObject.of("format", format, "path", path);
            try {
                ExportInstructions instructions = instructionsFor(ctx, format, overrides);
                model.Export(path, instructions);
                entry.put("written", Boolean.TRUE);
                written++;
            } catch (CommandException e) {
                entry.put("written", Boolean.FALSE);
                entry.put("error", e.getMessage());
                failed++;
            } catch (jxthrowable | RuntimeException e) {
                entry.put("written", Boolean.FALSE);
                entry.put("error", rootMessage(e));
                failed++;
            }
            manifest.add(entry);
        }

        return JsonObject.of(
                "model", modelRef(ctx, model),
                "baseName", baseName,
                "written", Integer.valueOf(written),
                "failed", Integer.valueOf(failed),
                "complete", Boolean.valueOf(failed == 0),
                "manifest", manifest);
    }

    /**
     * Builds the instruction object for one format.
     *
     * <p>Each export type has its own instruction class with its own required fields, and there are
     * 55 of them. Rather than hardcode a mapping that would go stale, the caller's own instruction
     * object is marshalled through the same path the raw commands use, and a format with no
     * override is reported as needing one rather than silently producing nothing.
     */
    private static ExportInstructions instructionsFor(CreoContext ctx, String format,
            JsonObject overrides) {
        JsonObject spec = overrides == null ? null : overrides.getObject(format);
        if (spec == null) {
            throw new CommandException(
                    "Format '" + format + "' needs an instruction object. Pass it under "
                            + "instructions." + format + ", for example "
                            + "{\"instructions\":{\"" + format + "\":{\"$type\":\"STEP3DExportInstructions\"}}}. "
                            + "creoctl --describe Model.Export lists what each type takes.",
                    "invalid_params");
        }
        String type = spec.getString("$type", null);
        if (type == null || type.isEmpty()) {
            throw new CommandException(
                    "instructions." + format + " needs a \"$type\", naming the J-Link "
                            + "ExportInstructions subclass to build.", "invalid_params");
        }
        // "$type" is this command's own convention for picking the class to build, not one
        // DataObjects.fromJson knows about; left in place it survives into applySetters and fails
        // with "'$type' is not settable" as soon as the chosen factory doesn't itself consume every
        // key in spec (e.g. a zero-argument factory, or named fields alongside a $factory override).
        spec.remove("$type");
        Object built = DataObjects.fromJson(ctx, type, spec, "instructions." + format);
        if (!(built instanceof ExportInstructions)) {
            throw new CommandException(
                    "instructions." + format + ": '" + type + "' is not an ExportInstructions type",
                    "invalid_params");
        }
        return (ExportInstructions) built;
    }

    private static String extensionFor(String format) {
        switch (format) {
            case "STEP": return "stp";
            case "IGES": return "igs";
            case "STL": return "stl";
            case "DXF": return "dxf";
            case "DWG": return "dwg";
            case "PDF": return "pdf";
            case "VRML": return "wrl";
            case "CATIA": return "model";
            case "PARASOLID": return "x_t";
            case "NEUTRAL": return "neu";
            default: return format.toLowerCase(java.util.Locale.ROOT);
        }
    }

    private static String stripExtension(String name) {
        if (name == null) {
            return "export";
        }
        int dot = name.lastIndexOf('.');
        return dot <= 0 ? name : name.substring(0, dot);
    }

    private static String join(String directory, String fileName) {
        String sep = directory.endsWith("\\") || directory.endsWith("/") ? "" : "\\";
        return directory + sep + fileName;
    }
}
