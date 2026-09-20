package com.asyncjlink.commands.compositecommands.features;

import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonArray;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcFeature.Feature;
import com.ptc.pfc.pfcFeature.FeatureOperation;
import com.ptc.pfc.pfcFeature.FeatureOperations;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcSolid.Solid;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Shared machinery for the suppress, resume and delete feature commands.
 *
 * <p>All three follow the same J-Link shape — create an operation object per feature, configure its
 * group and clip behaviour, collect them, and execute the batch — which is tedious and easy to get
 * wrong in ways that quietly take out child features. Doing it once here means the three commands
 * differ only in the operation they build and the words they use.
 */
abstract class FeatureOpCommand extends Composite {

    /** Builds the per-feature operation, already configured from {@code params}. */
    protected abstract FeatureOperation operationFor(Feature feature, JsonObject params)
            throws jxthrowable;

    /** Past-tense word for the report, e.g. "suppressed". */
    protected abstract String pastTense();

    /** Whether this operation destroys work irrecoverably, which changes the dryRun default. */
    protected boolean destructive() {
        return false;
    }

    @Override
    public String receiverType() {
        return "Solid";
    }

    protected JsonSchema featureSelectionSchema() {
        return targeted(name(), "Solid", "The part or assembly to modify.")
                .optional("features", JsonSchema.array(JsonSchema.string()),
                        "Feature names or numeric ids to act on.")
                .optional("namePattern", JsonSchema.string(),
                        "Select features by name instead; * and ? are wildcards.")
                .optional("dryRun", JsonSchema.bool(),
                        destructive()
                                ? "Report what would happen without doing it. Defaults to TRUE for "
                                        + "this command, because the change cannot be undone."
                                : "Report what would happen without doing it. Default false.")
                .optional("regenerate", JsonSchema.bool(),
                        "Regenerate afterwards and report failures. Default true.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Solid solid = requireSolid(ctx, params);
        boolean dryRun = params.getBoolean("dryRun", destructive());

        List<Feature> selected = select(solid, params);
        if (selected.isEmpty()) {
            return JsonObject.of(
                    "model", modelRef(ctx, (Model) solid),
                    "matched", Integer.valueOf(0),
                    "note", "No features matched. Solid.FindFeatures lists what is there.");
        }

        JsonArray report = new JsonArray();
        for (Feature f : selected) {
            JsonObject entry = itemRef(ctx, f);
            entry.putIfPresent("statusBefore", featureStatus(f));
            report.add(entry);
        }

        JsonObject out = JsonObject.of(
                "model", modelRef(ctx, (Model) solid),
                "dryRun", Boolean.valueOf(dryRun),
                "matched", Integer.valueOf(selected.size()),
                "features", report);

        if (dryRun) {
            out.put("action", "would" + Character.toUpperCase(pastTense().charAt(0))
                    + pastTense().substring(1));
            return out;
        }

        FeatureOperations ops = FeatureOperations.create();
        int built = 0;
        for (Feature f : selected) {
            FeatureOperation op = operationFor(f, params);
            if (op != null) {
                ops.append(op);
                built++;
            }
        }
        if (built == 0) {
            throw new CommandException(
                    "Could not build any " + pastTense() + " operation for the selected features",
                    "creo_error");
        }

        try {
            solid.ExecuteFeatureOps(ops, null);
            out.put("action", pastTense());
        } catch (jxthrowable e) {
            throw new CommandException(
                    "Creo refused to " + verb() + " these features: " + rootMessage(e)
                            + ". Children may depend on them; try clip=true.",
                    "creo_error", e);
        }

        for (int i = 0; i < report.size(); i++) {
            Object entry = report.get(i);
            if (entry instanceof JsonObject && i < selected.size()) {
                ((JsonObject) entry).putIfPresent("statusAfter", featureStatus(selected.get(i)));
            }
        }

        if (params.getBoolean("regenerate", true)) {
            JsonObject regen = regenerate(ctx, solid);
            out.put("regeneration", regen);
            out.put("ok", Boolean.valueOf(regen.getBoolean("regenerated", false)
                    && regen.getInt("failedFeatureCount", 0) == 0));
        }
        return out;
    }

    /**
     * Present-tense verb for the "Creo refused to ..." error message, e.g. {@code "suppress"}.
     *
     * <p>Defaults to stripping a regular {@code "-ed"} suffix off {@link #pastTense()}, which is
     * correct for a stem that takes the full suffix ({@code suppress + ed}). It is wrong whenever the
     * stem itself already ends in {@code "e"} ({@code resume + d}, {@code delete + d}): stripping
     * {@code "ed"} then removes one letter too many ({@code "resumed"} &rarr; {@code "resum"}). Those
     * commands override this instead of relying on the derivation.
     */
    protected String verb() {
        String p = pastTense();
        return p.endsWith("ed") ? p.substring(0, p.length() - 2) : p;
    }

    /** Resolves the caller's feature selection into live features. */
    private static List<Feature> select(Solid solid, JsonObject params) throws jxthrowable {
        List<Feature> all = features(solid);
        List<Feature> out = new ArrayList<>();

        JsonArray asked = params.getArray("features");
        Pattern pattern = glob(params.getString("namePattern", null));

        if ((asked == null || asked.size() == 0) && pattern == null) {
            throw new CommandException(
                    "Select features with 'features' (names or ids) or with 'namePattern'",
                    "invalid_params");
        }

        if (asked != null && asked.size() > 0) {
            for (int i = 0; i < asked.size(); i++) {
                String ref = String.valueOf(asked.get(i));
                Feature match = find(all, ref);
                if (match == null) {
                    throw new CommandException(
                            "No feature named or numbered '" + ref + "' in this model",
                            "invalid_params");
                }
                if (!out.contains(match)) {
                    out.add(match);
                }
            }
        }
        if (pattern != null) {
            for (Feature f : all) {
                if (matches(pattern, safeName(f)) && !out.contains(f)) {
                    out.add(f);
                }
            }
        }
        return out;
    }

    private static Feature find(List<Feature> all, String ref) throws jxthrowable {
        Integer id = null;
        try {
            id = Integer.valueOf(ref.trim());
        } catch (NumberFormatException ignored) {
            // Not an id, so it is a name.
        }
        for (Feature f : all) {
            if (id != null && f.GetId() == id.intValue()) {
                return f;
            }
            String n = safeName(f);
            if (n != null && n.equalsIgnoreCase(ref)) {
                return f;
            }
        }
        return null;
    }
}
