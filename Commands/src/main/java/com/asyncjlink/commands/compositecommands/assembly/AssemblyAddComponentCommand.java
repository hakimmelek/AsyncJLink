package com.asyncjlink.commands.compositecommands.assembly;

import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcAssembly.Assembly;
import com.ptc.pfc.pfcComponentFeat.ComponentFeat;
import com.ptc.pfc.pfcFeature.Feature;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModel.ModelDescriptor;
import com.ptc.pfc.pfcModel.pfcModel;
import com.ptc.pfc.pfcSolid.Solid;

/**
 * Assembly.AddComponent — place a component, and say how well it is actually constrained.
 *
 * <p>Packaged and underconstrained components are a common silent defect: the assembly looks right
 * until something moves. The placement state is reported rather than left to be discovered.
 */
public final class AssemblyAddComponentCommand extends Composite {

    @Override public String name() { return "Assembly.AddComponent"; }
    @Override public String receiverType() { return "Assembly"; }
    @Override public String signature() { return "AddComponent(target, component[, byCopy, dryRun]) — composite"; }

    @Override
    public String description() {
        return "Add a component to an assembly, retrieving it if needed, then report whether it "
                + "ended up placed, packaged or underconstrained. Supports dryRun.";
    }

    @Override
    public JsonSchema paramSchema() {
        return targeted(name(), "Assembly", "The assembly to add to.")
                .required("component", JsonSchema.string(),
                        "Model to assemble, by name or handle. Retrieved if not in session.")
                .optional("byCopy", JsonSchema.bool(),
                        "Assemble an independent copy under a new name. Default false.")
                .optional("copyName", JsonSchema.string(),
                        "Name for the copy when byCopy is set.")
                .optional("dryRun", JsonSchema.bool(),
                        "Report what would be added without adding. Default false.")
                .optional("regenerate", JsonSchema.bool(),
                        "Regenerate afterwards. Default true.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Assembly assembly = requireAssembly(ctx, params);
        String ref = params.getString("component", null);
        if (ref == null || ref.isEmpty()) {
            throw new CommandException("Field 'component' is required", "invalid_params");
        }
        boolean dryRun = dryRun(params);
        boolean byCopy = params.getBoolean("byCopy", false);

        Solid component = resolveSolid(ctx, ref);

        JsonObject out = JsonObject.of(
                "assembly", modelRef(ctx, (Model) assembly),
                "component", modelRef(ctx, (Model) component),
                "dryRun", Boolean.valueOf(dryRun));

        if (dryRun) {
            out.put("action", "wouldAdd");
            return out;
        }

        Feature placed;
        try {
            if (byCopy) {
                String copyName = params.getString("copyName", null);
                if (copyName == null || copyName.isEmpty()) {
                    throw new CommandException(
                            "Field 'copyName' is required when byCopy is set", "invalid_params");
                }
                placed = assembly.AssembleByCopy(copyName, component, true);
            } else {
                placed = assembly.AssembleComponent(component, null);
            }
        } catch (jxthrowable e) {
            throw new CommandException(
                    "Creo refused to assemble '" + ref + "': " + rootMessage(e), "creo_error", e);
        }

        if (placed == null) {
            throw new CommandException("Creo returned no component feature", "creo_error");
        }
        out.put("action", "added");
        out.put("feature", itemRef(ctx, placed));

        if (placed instanceof ComponentFeat) {
            ComponentFeat comp = (ComponentFeat) placed;
            JsonObject placement = new JsonObject();
            flag(placement, "placed", comp, Flag.PLACED);
            flag(placement, "packaged", comp, Flag.PACKAGED);
            flag(placement, "underconstrained", comp, Flag.UNDERCONSTRAINED);
            out.put("placement", placement);
            if (placement.getBoolean("packaged", false)
                    || placement.getBoolean("underconstrained", false)) {
                out.put("warning", "The component is not fully constrained. It will move when the "
                        + "assembly is regenerated or reorganised.");
            }
        }

        if (params.getBoolean("regenerate", true)) {
            out.put("regeneration", regenerate(ctx, assembly));
        }
        return out;
    }

    private enum Flag { PLACED, PACKAGED, UNDERCONSTRAINED }

    private static void flag(JsonObject out, String key, ComponentFeat comp, Flag flag) {
        try {
            switch (flag) {
                case PLACED:
                    out.put(key, Boolean.valueOf(comp.GetIsPlaced()));
                    break;
                case PACKAGED:
                    out.put(key, Boolean.valueOf(comp.GetIsPackaged()));
                    break;
                default:
                    out.put(key, Boolean.valueOf(comp.GetIsUnderconstrained()));
                    break;
            }
        } catch (jxthrowable | RuntimeException ignored) {
            // Omit rather than guess; a missing flag is less misleading than a wrong one.
        }
    }

    private static Solid resolveSolid(CreoContext ctx, String ref) throws jxthrowable {
        Object handle = ctx.handles().lookup(ref);
        if (handle instanceof Solid) {
            return (Solid) handle;
        }
        Model m = ctx.resolveModel(ref);
        if (m == null) {
            ModelDescriptor descr = pfcModel.ModelDescriptor_CreateFromFileName(ref);
            try {
                m = ctx.session().RetrieveModel(descr);
            } catch (jxthrowable e) {
                throw new CommandException(
                        "Could not retrieve component '" + ref + "': " + rootMessage(e),
                        "creo_error", e);
            }
        }
        if (!(m instanceof Solid)) {
            throw new CommandException(
                    "Field 'component': '" + ref + "' is not a part or assembly", "invalid_params");
        }
        return (Solid) m;
    }
}
