package com.asyncjlink.commands.compositecommands.assembly;

import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcAssembly.Assembly;
import com.ptc.pfc.pfcComponentFeat.CompModelReplace;
import com.ptc.pfc.pfcComponentFeat.ComponentFeat;
import com.ptc.pfc.pfcFeature.Feature;
import com.ptc.pfc.pfcFeature.FeatureOperations;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModel.ModelDescriptor;
import com.ptc.pfc.pfcModel.pfcModel;

/**
 * Assembly.ReplaceComponent — swap a component for another.
 *
 * <p>A revision bump or a variant swap: common, and error-prone by hand because constraints may or
 * may not survive the substitution. Whether they did is reported.
 */
public final class AssemblyReplaceComponentCommand extends Composite {

    @Override public String name() { return "Assembly.ReplaceComponent"; }
    @Override public String receiverType() { return "Assembly"; }
    @Override public String signature() { return "ReplaceComponent(target, component, replacement) — composite"; }

    @Override
    public String description() {
        return "Replace an assembly component with a different model (revision bump or variant), "
                + "then regenerate and report whether constraints survived. Supports dryRun.";
    }

    @Override
    public JsonSchema paramSchema() {
        return targeted(name(), "Assembly", "The assembly to modify.")
                .required("component", JsonSchema.string(),
                        "The component to replace: a component feature name or numeric id.")
                .required("replacement", JsonSchema.string(),
                        "The model to put in its place, by name or handle.")
                .optional("dryRun", JsonSchema.bool(),
                        "Report what would be replaced without replacing. Default false.")
                .optional("regenerate", JsonSchema.bool(),
                        "Regenerate afterwards. Default true.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Assembly assembly = requireAssembly(ctx, params);
        String componentRef = params.getString("component", null);
        String replacementRef = params.getString("replacement", null);
        if (componentRef == null || componentRef.isEmpty()) {
            throw new CommandException("Field 'component' is required", "invalid_params");
        }
        if (replacementRef == null || replacementRef.isEmpty()) {
            throw new CommandException("Field 'replacement' is required", "invalid_params");
        }
        boolean dryRun = dryRun(params);

        ComponentFeat target = findComponent(assembly, componentRef);
        if (target == null) {
            throw new CommandException(
                    "No component feature named or numbered '" + componentRef + "' in this assembly. "
                            + "Assembly.GetBOM lists them.", "invalid_params");
        }
        Model replacement = resolveModel(ctx, replacementRef);

        JsonObject out = JsonObject.of(
                "assembly", modelRef(ctx, (Model) assembly),
                "dryRun", Boolean.valueOf(dryRun),
                "component", itemRef(ctx, target),
                "replacement", modelRef(ctx, replacement));

        Model existing = currentModel(ctx, target);
        out.putIfPresent("replacing", modelRef(ctx, existing));

        if (dryRun) {
            out.put("action", "wouldReplace");
            return out;
        }

        CompModelReplace op;
        try {
            op = target.CreateReplaceOp(replacement);
        } catch (jxthrowable e) {
            throw new CommandException(
                    "Creo would not build a replace operation: " + rootMessage(e)
                            + ". The replacement may be incompatible with this component.",
                    "creo_error", e);
        }
        if (op == null) {
            throw new CommandException("Creo returned no replace operation", "creo_error");
        }

        try {
            FeatureOperations ops = FeatureOperations.create();
            ops.append(op);
            assembly.ExecuteFeatureOps(ops, null);
            out.put("action", "replaced");
        } catch (jxthrowable e) {
            throw new CommandException(
                    "Creo refused the replacement: " + rootMessage(e), "creo_error", e);
        }

        try {
            out.put("constraintsSurvived",
                    Boolean.valueOf(!target.GetIsUnderconstrained() && !target.GetIsPackaged()));
        } catch (jxthrowable | RuntimeException ignored) {
            // The component may no longer answer after replacement; the regeneration report below
            // is the more reliable signal anyway.
        }

        if (params.getBoolean("regenerate", true)) {
            JsonObject regen = regenerate(ctx, assembly);
            out.put("regeneration", regen);
            out.put("ok", Boolean.valueOf(regen.getBoolean("regenerated", false)
                    && regen.getInt("failedFeatureCount", 0) == 0));
        }
        return out;
    }

    private static ComponentFeat findComponent(Assembly assembly, String ref) throws jxthrowable {
        Integer id = null;
        try {
            id = Integer.valueOf(ref.trim());
        } catch (NumberFormatException ignored) {
            // Not an id, so it is a name.
        }
        for (Feature f : features(assembly)) {
            if (!(f instanceof ComponentFeat)) {
                continue;
            }
            if (id != null && f.GetId() == id.intValue()) {
                return (ComponentFeat) f;
            }
            String n = safeName(f);
            if (n != null && n.equalsIgnoreCase(ref)) {
                return (ComponentFeat) f;
            }
        }
        return null;
    }

    private static Model currentModel(CreoContext ctx, ComponentFeat comp) {
        try {
            ModelDescriptor descr = comp.GetModelDescr();
            return descr == null ? null : ctx.session().GetModelFromDescr(descr);
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }

    private static Model resolveModel(CreoContext ctx, String ref) throws jxthrowable {
        Object handle = ctx.handles().lookup(ref);
        if (handle instanceof Model) {
            return (Model) handle;
        }
        Model m = ctx.resolveModel(ref);
        if (m == null) {
            ModelDescriptor descr = pfcModel.ModelDescriptor_CreateFromFileName(ref);
            try {
                m = ctx.session().RetrieveModel(descr);
            } catch (jxthrowable e) {
                throw new CommandException(
                        "Could not retrieve replacement '" + ref + "': " + rootMessage(e),
                        "creo_error", e);
            }
        }
        if (m == null) {
            throw new CommandException(
                    "Field 'replacement': no model named '" + ref + "'", "unknown_handle");
        }
        return m;
    }
}
