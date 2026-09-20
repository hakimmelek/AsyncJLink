package com.asyncjlink.commands.compositecommands.inspection;

import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonArray;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcFeature.Feature;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModelItem.Parameter;
import com.ptc.pfc.pfcPart.Material;
import com.ptc.pfc.pfcSolid.Solid;

import java.util.ArrayList;
import java.util.List;

/**
 * Model.Audit — the release gate.
 *
 * <p>One verdict covering the failure modes that actually reach production: failed features,
 * unexpected suppression, missing material, unpopulated parameters, the wrong unit system, unsaved
 * changes. Each check is reported separately with the offending items named, so a failure is
 * actionable rather than merely discouraging.
 *
 * <p>The rules are parameters rather than hardcoded, so a team can encode its own standards. Making
 * them a project-level standards file is the obvious next step.
 */
public final class ModelAuditCommand extends Composite {

    @Override public String name() { return "Model.Audit"; }
    @Override public String receiverType() { return "Model"; }
    @Override public String signature() { return "Audit(target[, requiredParameters, expectedUnits]) — composite"; }

    @Override
    public String description() {
        return "Release-readiness check: failed features, suppressed features, missing material or "
                + "density, required parameters, unit system, unsaved changes. Returns pass/fail "
                + "per check with the offending items named.";
    }

    @Override
    public JsonSchema paramSchema() {
        return targeted(name(), "Model", "The model to audit.")
                .optional("requiredParameters", JsonSchema.array(JsonSchema.string()),
                        "Parameters that must exist and be non-empty, e.g. [\"PART_NO\",\"REVISION\"].")
                .optional("expectedUnits", JsonSchema.string(),
                        "Unit system the model should be in, e.g. \"millimeter Newton Second (mmNs)\".")
                .optional("allowSuppressed", JsonSchema.bool(),
                        "Treat suppressed features as acceptable. Default false.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Model model = requireModel(ctx, params);
        JsonArray checks = new JsonArray();

        checkModified(model, checks);

        if (model instanceof Solid) {
            Solid solid = (Solid) model;
            checkFeatures(ctx, solid, params.getBoolean("allowSuppressed", false), checks);
            checkMaterial(solid, checks);
            checkUnits(solid, params.getString("expectedUnits", null), checks);
            checkAccuracy(solid, checks);
        }
        checkRequiredParameters(model, params.getArray("requiredParameters"), checks);
        checkInstance(model, checks);

        int failed = 0;
        for (int i = 0; i < checks.size(); i++) {
            Object c = checks.get(i);
            if (c instanceof JsonObject && !((JsonObject) c).getBoolean("pass", true)) {
                failed++;
            }
        }

        return JsonObject.of(
                "model", modelRef(ctx, model),
                "pass", Boolean.valueOf(failed == 0),
                "checkCount", Integer.valueOf(checks.size()),
                "failedCount", Integer.valueOf(failed),
                "checks", checks);
    }

    private static void add(JsonArray checks, String id, boolean pass, String detail) {
        JsonObject c = JsonObject.of("check", id, "pass", Boolean.valueOf(pass));
        c.putIfPresent("detail", detail);
        checks.add(c);
    }

    private static void checkModified(Model model, JsonArray checks) {
        try {
            boolean modified = model.GetIsModified();
            add(checks, "saved", !modified,
                    modified ? "The model has unsaved changes." : null);
        } catch (jxthrowable | RuntimeException e) {
            add(checks, "saved", false, "Could not read modified state: " + rootMessage(e));
        }
    }

    private static void checkFeatures(CreoContext ctx, Solid solid, boolean allowSuppressed,
            JsonArray checks) {
        JsonArray failedItems = new JsonArray();
        JsonArray suppressedItems = new JsonArray();
        try {
            for (Feature f : features(solid)) {
                String status = featureStatus(f);
                if (status == null || "FEAT_ACTIVE".equals(status)) {
                    continue;
                }
                JsonObject entry = itemRef(ctx, f);
                entry.put("status", status);
                if (status.contains("SUPPRESSED") || "FEAT_INACTIVE".equals(status)) {
                    suppressedItems.add(entry);
                } else {
                    failedItems.add(entry);
                }
            }
        } catch (jxthrowable | RuntimeException e) {
            add(checks, "features", false, "Could not list features: " + rootMessage(e));
            return;
        }

        JsonObject failedCheck = JsonObject.of(
                "check", "noFailedFeatures",
                "pass", Boolean.valueOf(failedItems.size() == 0));
        if (failedItems.size() > 0) {
            failedCheck.put("detail", failedItems.size() + " feature(s) are in a failed state.");
            failedCheck.put("items", failedItems);
        }
        checks.add(failedCheck);

        JsonObject suppressedCheck = JsonObject.of(
                "check", "noSuppressedFeatures",
                "pass", Boolean.valueOf(allowSuppressed || suppressedItems.size() == 0));
        if (suppressedItems.size() > 0) {
            suppressedCheck.put("detail", suppressedItems.size() + " feature(s) are suppressed"
                    + (allowSuppressed ? " (allowed by request)." : "."));
            suppressedCheck.put("items", suppressedItems);
        }
        checks.add(suppressedCheck);
    }

    private static void checkMaterial(Solid solid, JsonArray checks) {
        Material material = currentMaterial(solid);
        if (material == null) {
            // Only parts carry a material; an assembly's mass comes from its children.
            boolean isPart = solid instanceof com.ptc.pfc.pfcPart.Part;
            add(checks, "materialAssigned", !isPart,
                    isPart ? "No material is assigned, so mass properties use a default density "
                            + "and cannot be trusted." : "Not a part; material is not applicable.");
            return;
        }
        try {
            double density = material.GetMassDensity();
            add(checks, "materialAssigned", density > 0,
                    density > 0 ? material.GetName() + " (density " + density + ")"
                            : "Material " + material.GetName() + " has no usable density.");
        } catch (jxthrowable | RuntimeException e) {
            add(checks, "materialAssigned", false, "Could not read density: " + rootMessage(e));
        }
    }

    private static void checkUnits(Solid solid, String expected, JsonArray checks) {
        String actual = unitSystem(solid);
        if (expected == null || expected.isEmpty()) {
            add(checks, "unitSystem", true, actual == null ? "unknown" : actual);
            return;
        }
        boolean ok = expected.equalsIgnoreCase(actual);
        add(checks, "unitSystem", ok,
                ok ? actual : "Expected '" + expected + "' but the model is in '" + actual + "'.");
    }

    private static void checkAccuracy(Solid solid, JsonArray checks) {
        try {
            Double relative = solid.GetRelativeAccuracy();
            if (relative != null) {
                add(checks, "accuracy", true, "relative " + relative);
                return;
            }
        } catch (jxthrowable | RuntimeException ignored) {
            // Fall through to absolute.
        }
        try {
            Double absolute = solid.GetAbsoluteAccuracy();
            add(checks, "accuracy", true,
                    absolute == null ? "not reported" : "absolute " + absolute);
        } catch (jxthrowable | RuntimeException e) {
            add(checks, "accuracy", true, "not reported");
        }
    }

    private static void checkRequiredParameters(Model model, JsonArray required, JsonArray checks) {
        if (required == null || required.size() == 0) {
            return;
        }
        List<String> missing = new ArrayList<>();
        List<String> empty = new ArrayList<>();
        for (int i = 0; i < required.size(); i++) {
            String pname = String.valueOf(required.get(i));
            try {
                Parameter p = model.GetParam(pname);
                if (p == null) {
                    missing.add(pname);
                    continue;
                }
                Object value = paramValue(p.GetValue());
                if (value == null || String.valueOf(value).trim().isEmpty()) {
                    empty.add(pname);
                }
            } catch (jxthrowable | RuntimeException e) {
                missing.add(pname);
            }
        }
        JsonObject c = JsonObject.of(
                "check", "requiredParameters",
                "pass", Boolean.valueOf(missing.isEmpty() && empty.isEmpty()));
        if (!missing.isEmpty()) {
            c.put("missing", toJsonArray(missing));
        }
        if (!empty.isEmpty()) {
            c.put("empty", toJsonArray(empty));
        }
        checks.add(c);
    }

    private static void checkInstance(Model model, JsonArray checks) {
        try {
            String instance = model.GetInstanceName();
            if (instance != null && !instance.isEmpty()) {
                add(checks, "familyTableInstance", true, "Instance '" + instance + "'.");
            }
        } catch (jxthrowable | RuntimeException ignored) {
            // A model that is not a family table instance simply has nothing to report here.
        }
    }
}
