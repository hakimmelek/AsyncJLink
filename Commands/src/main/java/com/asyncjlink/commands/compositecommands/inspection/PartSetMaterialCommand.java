package com.asyncjlink.commands.compositecommands.inspection;

import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcPart.Material;
import com.ptc.pfc.pfcPart.Part;

/**
 * Part.SetMaterial — assign a material from the library and save.
 *
 * <p>Retrieving a material, assigning it as current and saving is three raw calls with three
 * different failure modes (material file not found, save refused); this reports all three plainly
 * instead of leaving a part with the wrong density silently propagating into every assembly rollup
 * above it — precisely the failure {@link SolidGetMassPropertyReportCommand}'s warning exists to
 * catch after the fact.
 */
public final class PartSetMaterialCommand extends Composite {

    @Override public String name() { return "Part.SetMaterial"; }
    @Override public String receiverType() { return "Part"; }
    @Override public String signature() { return "SetMaterial(target, name[, dryRun]) — composite"; }

    @Override
    public String description() {
        return "Assign a material from Creo's material library and save. Reports the material "
                + "actually assigned and its density, rather than leaving a wrong or missing "
                + "density to surface later as a mass-property error.";
    }

    @Override
    public JsonSchema paramSchema() {
        return targeted(name(), "Part", "The part to assign a material to.")
                .required("material", JsonSchema.string(), "Material name, from the library.")
                .optional("dryRun", JsonSchema.bool(),
                        "Report what would change without saving. Default false.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Model model = requireModel(ctx, params);
        if (!(model instanceof Part)) {
            throw new CommandException(
                    "Field 'target' must be a part, but was a " + typeOf(model), "invalid_params");
        }
        Part part = (Part) model;
        String materialName = params.getString("material", null);
        if (materialName == null || materialName.isEmpty()) {
            throw new CommandException("Field 'material' is required", "invalid_params");
        }
        boolean dryRun = dryRun(params);

        JsonObject out = JsonObject.of("model", modelRef(ctx, model), "dryRun", Boolean.valueOf(dryRun));

        Material existing = safeGetMaterial(part, materialName);
        boolean alreadyOnPart = existing != null;
        Material material = existing;
        if (material == null) {
            try {
                material = part.RetrieveMaterial(materialName);
            } catch (jxthrowable e) {
                throw new CommandException(
                        "Could not find material '" + materialName
                                + "' in the library: " + rootMessage(e), "creo_error", e);
            }
        }
        if (material == null) {
            throw new CommandException(
                    "Creo found no material named '" + materialName + "'", "creo_error");
        }

        out.putIfPresent("materialFrom", alreadyOnPart ? "part" : "library");
        out.putIfPresent("material", JsonObject.of(
                "name", materialName,
                "density", materialDensity(material)));

        if (dryRun) {
            out.put("assigned", Boolean.FALSE);
            return out;
        }

        try {
            part.SetCurrentMaterial(material);
            material.Save("");
            out.put("assigned", Boolean.TRUE);
        } catch (jxthrowable | RuntimeException e) {
            out.put("assigned", Boolean.FALSE);
            out.put("error", rootMessage(e));
        }
        return out;
    }

    private static Material safeGetMaterial(Part part, String name) {
        try {
            return part.GetMaterial(name);
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }

    private static Double materialDensity(Material material) {
        try {
            return Double.valueOf(material.GetMassDensity());
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }
}
