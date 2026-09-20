package com.asyncjlink.commands.compositecommands.inspection;

import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonArray;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcPart.Material;
import com.ptc.pfc.pfcPart.Part;

/**
 * Part.CreateMaterialWithProperties — define a new material and save it.
 *
 * <p>Named {@code CreateMaterialWithProperties} rather than {@code CreateMaterial}, which collides
 * with the real raw {@code Part.CreateMaterial(String)} — the raw call makes an empty material; this
 * one is a materially different operation, since an empty material under the right name would pass
 * {@code Model.Audit}'s {@code materialAssigned} check while still reporting a wrong mass, which is
 * worse than refusing to create it at all.
 *
 * <p>Materials are created in the context of a part per the raw API's own shape —
 * {@code Part.CreateMaterial} is not a session-level call — so this composite does not also assign
 * the new material as current; that stays {@link PartSetMaterialCommand}'s job, run afterward,
 * keeping each command doing one thing.
 */
public final class PartCreateMaterialWithPropertiesCommand extends Composite {

    @Override public String name() { return "Part.CreateMaterialWithProperties"; }
    @Override public String receiverType() { return "Part"; }
    @Override public String signature() { return "CreateMaterialWithProperties(target, name, properties) — composite"; }

    @Override
    public String description() {
        return "Define a new material (density, Young's modulus, Poisson ratio, ...) and save it, "
                + "in one call. Does not assign it to the part; use Part.SetMaterial for that.";
    }

    @Override
    public JsonSchema paramSchema() {
        return targeted(name(), "Part", "The part to create the material in the context of.")
                .required("name", JsonSchema.string(), "Name for the new material.")
                .required("properties", JsonSchema.dataObject("MaterialProperties"),
                        "Property values to set, e.g. "
                                + "{\"massDensity\": 7.85e-9, \"youngModulus\": 200000, "
                                + "\"poissonRatio\": 0.29, \"shearModulus\": 80000}. Units follow "
                                + "the model's own unit system, unchecked.")
                .optional("description", JsonSchema.string(), "Material description.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Model model = requireModel(ctx, params);
        if (!(model instanceof Part)) {
            throw new CommandException(
                    "Field 'target' must be a part, but was a " + typeOf(model), "invalid_params");
        }
        Part part = (Part) model;
        String materialName = params.getString("name", null);
        if (materialName == null || materialName.isEmpty()) {
            throw new CommandException("Field 'name' is required", "invalid_params");
        }
        JsonObject properties = params.getObject("properties");
        if (properties == null || properties.isEmpty()) {
            throw new CommandException(
                    "Field 'properties' must be an object of property name/value pairs",
                    "invalid_params");
        }

        Material material;
        try {
            material = part.CreateMaterial(materialName);
        } catch (jxthrowable e) {
            throw new CommandException(
                    "Could not create material '" + materialName + "': " + rootMessage(e),
                    "creo_error", e);
        }
        if (material == null) {
            throw new CommandException(
                    "Creo returned no material for '" + materialName + "'", "creo_error");
        }

        String description = params.getString("description", null);
        if (description != null) {
            try {
                material.SetDescription(description);
            } catch (jxthrowable | RuntimeException ignored) {
                // Non-essential; the property report below is what actually matters.
            }
        }

        JsonArray results = new JsonArray();
        int set = 0;
        int rejected = 0;
        for (String key : properties.keys()) {
            JsonObject entry = JsonObject.of("name", key);
            Object value = properties.get(key);
            try {
                double d = asDouble(value, key);
                applyProperty(material, key, d);
                entry.put("action", "set");
                entry.put("value", Double.valueOf(d));
                set++;
            } catch (CommandException e) {
                entry.put("action", "rejected");
                entry.put("reason", e.getMessage());
                rejected++;
            } catch (jxthrowable | RuntimeException e) {
                entry.put("action", "rejected");
                entry.put("reason", rootMessage(e));
                rejected++;
            }
            results.add(entry);
        }

        JsonObject out = JsonObject.of(
                "material", JsonObject.of("name", materialName),
                "set", Integer.valueOf(set),
                "rejected", Integer.valueOf(rejected),
                "properties", results);

        try {
            material.Save("");
            out.put("saved", Boolean.TRUE);
        } catch (jxthrowable | RuntimeException e) {
            out.put("saved", Boolean.FALSE);
            out.put("saveError", rootMessage(e));
        }
        return out;
    }

    /**
     * Maps the caller's own property names onto {@code Material.Set*}. A small, named set rather
     * than a generic reflective dispatch (as {@code DataObjects} uses for instruction objects)
     * because these are physical properties a caller decides between by name, not an arbitrary
     * struct — the same reasoning {@code Drawing.Export} gives for exposing {@code color} as a
     * boolean instead of a raw option object.
     */
    private static void applyProperty(Material material, String key, double value) throws jxthrowable {
        switch (key) {
            case "massDensity":
                material.SetMassDensity(value);
                return;
            case "youngModulus":
                material.SetYoungModulus(value);
                return;
            case "poissonRatio":
                material.SetPoissonRatio(value);
                return;
            case "shearModulus":
                material.SetShearModulus(value);
                return;
            case "stressLimTension":
                material.SetStressLimTension(value);
                return;
            case "stressLimCompress":
                material.SetStressLimCompress(value);
                return;
            case "stressLimShear":
                material.SetStressLimShear(value);
                return;
            default:
                throw new CommandException(
                        "Unknown material property '" + key + "'. Known: massDensity, "
                                + "youngModulus, poissonRatio, shearModulus, stressLimTension, "
                                + "stressLimCompress, stressLimShear.", "invalid_params");
        }
    }

    private static double asDouble(Object json, String field) {
        if (json instanceof Number) {
            return ((Number) json).doubleValue();
        }
        try {
            return Double.parseDouble(String.valueOf(json).trim());
        } catch (NumberFormatException e) {
            throw new CommandException(
                    "Property '" + field + "' needs a number but was '" + json + "'",
                    "invalid_params");
        }
    }
}
