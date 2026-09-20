package com.asyncjlink.commands.compositecommands.inspection;

import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonArray;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcBase.Point3D;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcPart.Material;
import com.ptc.pfc.pfcSolid.MassProperty;
import com.ptc.pfc.pfcSolid.Solid;

/**
 * Solid.GetMassPropertyReport — mass properties, with units and the density's provenance.
 *
 * <p>The raw call returns numbers with no indication of what they are in or where the density came
 * from. A part with no assigned material reports a default density — often producing a plausible but
 * wrong mass — and that error then propagates silently into every assembly rollup above it. This
 * command says so instead.
 */
public final class SolidGetMassPropertyReportCommand extends Composite {

    @Override public String name() { return "Solid.GetMassPropertyReport"; }
    @Override public String receiverType() { return "Solid"; }
    @Override public String signature() { return "GetMassPropertyReport(target[, coordSys]) — composite"; }

    @Override
    public String description() {
        return "Mass, volume, centre of gravity and inertia together with the unit system, the "
                + "assigned material and a warning when the density was never actually assigned.";
    }

    @Override
    public JsonSchema paramSchema() {
        return targeted(name(), "Solid", "The part or assembly to measure.")
                .optional("coordSys", JsonSchema.string(),
                        "Coordinate system to report about. Defaults to the model's default.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Solid solid = requireSolid(ctx, params);
        String coordSys = params.getString("coordSys", null);

        MassProperty mp;
        try {
            mp = solid.GetMassProperty(coordSys);
        } catch (jxthrowable e) {
            throw new CommandException(
                    "Creo could not compute mass properties: " + rootMessage(e)
                            + (coordSys == null ? "" : " (coordinate system '" + coordSys + "')"),
                    "creo_error", e);
        }
        if (mp == null) {
            throw new CommandException("Creo returned no mass properties", "creo_error");
        }

        JsonObject out = JsonObject.of("model", modelRef(ctx, (Model) solid));
        out.putIfPresent("coordSys", coordSys);
        out.putIfPresent("unitSystem", unitSystem(solid));

        double mass = mp.GetMass();
        double volume = mp.GetVolume();
        double density = mp.GetDensity();
        out.put("mass", Double.valueOf(mass));
        out.put("volume", Double.valueOf(volume));
        out.put("surfaceArea", Double.valueOf(mp.GetSurfaceArea()));
        out.put("density", Double.valueOf(density));
        out.putIfPresent("centreOfGravity", point(mp.GetGravityCenter()));

        JsonArray warnings = new JsonArray();

        Material material = currentMaterial(solid);
        if (material == null) {
            out.put("material", null);
            // This is the failure that matters. Creo still reports a mass, so nothing looks wrong
            // until the number is used for something.
            warnings.add("No material is assigned to this model, so the density above is Creo's "
                    + "default rather than the real one. The mass is not trustworthy.");
        } else {
            JsonObject m = new JsonObject();
            m.putIfPresent("name", materialName(material));
            Double materialDensity = materialDensity(material);
            m.putIfPresent("density", materialDensity);
            out.put("material", m);
            if (materialDensity != null && materialDensity.doubleValue() > 0
                    && Math.abs(materialDensity.doubleValue() - density)
                            > Math.abs(density) * 1e-6) {
                warnings.add("The density used for this calculation (" + density
                        + ") does not match the assigned material's density ("
                        + materialDensity + ").");
            }
        }

        if (mass <= 0) {
            warnings.add("Mass is zero or negative, which usually means the model has no solid "
                    + "geometry or no usable density.");
        }

        if (warnings.size() > 0) {
            out.put("warnings", warnings);
        }
        out.put("trustworthy", Boolean.valueOf(warnings.size() == 0));
        return out;
    }

    private static String materialName(Material material) {
        try {
            return material.GetName();
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

    private static JsonArray point(Point3D p) {
        if (p == null) {
            return null;
        }
        try {
            JsonArray a = new JsonArray();
            for (int i = 0; i < 3; i++) {
                a.add(Double.valueOf(p.get(i)));
            }
            return a;
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }
}
