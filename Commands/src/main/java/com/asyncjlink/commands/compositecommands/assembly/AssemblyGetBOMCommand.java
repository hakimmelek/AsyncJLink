package com.asyncjlink.commands.compositecommands.assembly;

import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonArray;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcAssembly.Assembly;
import com.ptc.pfc.pfcComponentFeat.ComponentFeat;
import com.ptc.pfc.pfcFeature.Feature;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModel.ModelDescriptor;
import com.ptc.pfc.pfcModelItem.Parameter;
import com.ptc.pfc.pfcSolid.MassProperty;
import com.ptc.pfc.pfcSolid.Solid;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Assembly.GetBOM — the indented bill of materials in one call.
 *
 * <p>Raw, this is a recursive descent costing tens to hundreds of calls, and it is the single most
 * requested operation in CAD automation. Mass is optional because it dominates the cost: every line
 * that wants it needs its own {@code GetMassProperty}.
 */
public final class AssemblyGetBOMCommand extends Composite {

    private static final int DEFAULT_DEPTH = 16;

    @Override public String name() { return "Assembly.GetBOM"; }
    @Override public String receiverType() { return "Assembly"; }
    @Override public String signature() { return "GetBOM(target[, depth, includeMass, rollUp]) — composite"; }

    @Override
    public String description() {
        return "The full indented bill of materials: structure, quantities, per-line parameters and "
                + "optional mass, in one call instead of a recursive walk of hundreds.";
    }

    @Override
    public JsonSchema paramSchema() {
        return targeted(name(), "Assembly", "The assembly to explode.")
                .optional("depth", JsonSchema.integer(),
                        "How many levels to descend. Default " + DEFAULT_DEPTH + ".")
                .optional("includeMass", JsonSchema.bool(),
                        "Compute mass per line. Expensive; default false.")
                .optional("includeSuppressed", JsonSchema.bool(),
                        "Include suppressed components. Default false.")
                .optional("parameters", JsonSchema.array(JsonSchema.string()),
                        "Parameter names to report per line, e.g. [\"PART_NO\",\"MATERIAL\"].")
                .optional("rollUp", JsonSchema.bool(),
                        "Also return a flat roll-up keyed by model name with total quantities. "
                                + "Default true.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Assembly assembly = requireAssembly(ctx, params);
        int depth = params.getInt("depth", DEFAULT_DEPTH);
        boolean includeMass = params.getBoolean("includeMass", false);
        boolean includeSuppressed = params.getBoolean("includeSuppressed", false);

        List<String> wantedParams = new ArrayList<>();
        JsonArray asked = params.getArray("parameters");
        if (asked != null) {
            for (int i = 0; i < asked.size(); i++) {
                wantedParams.add(String.valueOf(asked.get(i)));
            }
        }

        Walk walk = new Walk(ctx, includeMass, includeSuppressed, wantedParams);
        JsonArray children = walk.descend(assembly, depth);

        JsonObject out = JsonObject.of(
                "assembly", modelRef(ctx, (Model) assembly),
                "lineCount", Integer.valueOf(walk.lines),
                "maxDepthReached", Boolean.valueOf(walk.truncated),
                "components", children);

        if (params.getBoolean("rollUp", true)) {
            JsonArray rolled = new JsonArray();
            for (Map.Entry<String, int[]> e : walk.totals.entrySet()) {
                rolled.add(JsonObject.of(
                        "name", e.getKey(),
                        "quantity", Integer.valueOf(e.getValue()[0])));
            }
            out.put("rollUp", rolled);
        }
        return out;
    }

    /** Carries the recursion's shared state so the descent itself stays readable. */
    private static final class Walk {

        private final CreoContext ctx;
        private final boolean includeMass;
        private final boolean includeSuppressed;
        private final List<String> wantedParams;
        private final Map<String, int[]> totals = new LinkedHashMap<>();

        private int lines;
        private boolean truncated;

        Walk(CreoContext ctx, boolean includeMass, boolean includeSuppressed,
                List<String> wantedParams) {
            this.ctx = ctx;
            this.includeMass = includeMass;
            this.includeSuppressed = includeSuppressed;
            this.wantedParams = wantedParams;
        }

        JsonArray descend(Solid parent, int depth) throws jxthrowable {
            JsonArray out = new JsonArray();
            if (depth <= 0) {
                truncated = true;
                return out;
            }
            for (Feature f : features(parent)) {
                if (!(f instanceof ComponentFeat)) {
                    continue;
                }
                ComponentFeat comp = (ComponentFeat) f;
                String status = featureStatus(f);
                boolean suppressed = status != null && status.contains("SUPPRESSED");
                if (suppressed && !includeSuppressed) {
                    continue;
                }

                JsonObject line = itemRef(ctx, f);
                line.putIfPresent("status", status);
                line.put("suppressed", Boolean.valueOf(suppressed));

                Model child = resolve(comp);
                if (child != null) {
                    line.putIfPresent("model", modelRef(ctx, child));
                    String key = child.GetFullName();
                    if (key != null) {
                        totals.computeIfAbsent(key, k -> new int[1])[0]++;
                    }
                    addParameters(line, child);
                    if (includeMass && child instanceof Solid) {
                        addMass(line, (Solid) child);
                    }
                }
                addPlacement(line, comp);
                lines++;

                if (child instanceof Solid) {
                    JsonArray sub = descend((Solid) child, depth - 1);
                    if (sub.size() > 0) {
                        line.put("components", sub);
                    }
                }
                out.add(line);
            }
            return out;
        }

        private Model resolve(ComponentFeat comp) {
            try {
                ModelDescriptor descr = comp.GetModelDescr();
                return descr == null ? null : ctx.session().GetModelFromDescr(descr);
            } catch (jxthrowable | RuntimeException e) {
                // A component whose model is not in session cannot be described further; the line
                // still reports the feature itself.
                return null;
            }
        }

        private void addParameters(JsonObject line, Model model) {
            if (wantedParams.isEmpty()) {
                return;
            }
            JsonObject values = new JsonObject();
            for (String pname : wantedParams) {
                try {
                    Parameter p = model.GetParam(pname);
                    if (p != null) {
                        values.putIfPresent(pname, paramValue(p.GetValue()));
                    }
                } catch (jxthrowable | RuntimeException ignored) {
                    // A missing parameter on one line is normal, not an error.
                }
            }
            if (!values.isEmpty()) {
                line.put("parameters", values);
            }
        }

        private void addMass(JsonObject line, Solid solid) {
            try {
                MassProperty mp = solid.GetMassProperty(null);
                if (mp != null) {
                    line.put("mass", Double.valueOf(mp.GetMass()));
                    line.putIfPresent("unitSystem", unitSystem(solid));
                }
            } catch (jxthrowable | RuntimeException e) {
                line.put("massError", rootMessage(e));
            }
        }

        private static void addPlacement(JsonObject line, ComponentFeat comp) {
            try {
                line.put("packaged", Boolean.valueOf(comp.GetIsPackaged()));
            } catch (jxthrowable | RuntimeException ignored) {
                // Not every component reports placement state; omit rather than guess.
            }
            try {
                line.put("frozen", Boolean.valueOf(comp.GetIsFrozen()));
            } catch (jxthrowable | RuntimeException ignored) {
                // As above.
            }
            try {
                line.put("substitute", Boolean.valueOf(comp.GetIsSubstitute()));
            } catch (jxthrowable | RuntimeException ignored) {
                // As above.
            }
        }
    }
}
