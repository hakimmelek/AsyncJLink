package com.asyncjlink.commands.compositecommands.lifecycle;

import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonArray;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcFeature.Feature;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModelItem.Parameter;
import com.ptc.pfc.pfcSolid.Solid;

import java.util.ArrayList;
import java.util.List;

/**
 * Model.SaveChecked — save, but refuse when the model would fail its checks.
 *
 * <p>Stops failed geometry and unpopulated revisions from being committed. The blocking reason is
 * always named, because "refused to save" without a reason is worse than not checking at all.
 */
public final class ModelSaveCheckedCommand extends Composite {

    @Override public String name() { return "Model.SaveChecked"; }
    @Override public String receiverType() { return "Model"; }
    @Override public String signature() { return "SaveChecked(target[, requiredParameters, dryRun]) — composite"; }

    @Override
    public String description() {
        return "Save a model only if it passes its checks — no failed features, required parameters "
                + "populated, Creo willing. Names the blocking reason when it refuses.";
    }

    @Override
    public JsonSchema paramSchema() {
        return targeted(name(), "Model", "The model to save.")
                .optional("requiredParameters", JsonSchema.array(JsonSchema.string()),
                        "Parameters that must exist and be non-empty before saving.")
                .optional("allowFailedFeatures", JsonSchema.bool(),
                        "Save even with failed features. Default false.")
                .optional("dryRun", JsonSchema.bool(),
                        "Run the checks and report, without saving. Default false.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Model model = requireModel(ctx, params);
        boolean dryRun = dryRun(params);
        boolean allowFailed = params.getBoolean("allowFailedFeatures", false);

        List<String> blockers = new ArrayList<>();
        JsonArray failedItems = new JsonArray();

        if (!allowFailed && model instanceof Solid) {
            for (Feature f : features((Solid) model)) {
                String status = featureStatus(f);
                if (status == null || "FEAT_ACTIVE".equals(status) || status.contains("SUPPRESSED")
                        || "FEAT_INACTIVE".equals(status)) {
                    continue;
                }
                JsonObject entry = itemRef(ctx, f);
                entry.put("status", status);
                failedItems.add(entry);
            }
            if (failedItems.size() > 0) {
                blockers.add(failedItems.size() + " feature(s) are in a failed state");
            }
        }

        JsonArray missing = new JsonArray();
        JsonArray asked = params.getArray("requiredParameters");
        if (asked != null) {
            for (int i = 0; i < asked.size(); i++) {
                String pname = String.valueOf(asked.get(i));
                if (!hasValue(model, pname)) {
                    missing.add(pname);
                }
            }
            if (missing.size() > 0) {
                blockers.add(missing.size() + " required parameter(s) missing or empty");
            }
        }

        try {
            if (!model.CheckIsSaveAllowed(false)) {
                blockers.add("Creo does not allow saving this model");
            }
        } catch (jxthrowable | RuntimeException e) {
            blockers.add("save check failed: " + rootMessage(e));
        }

        JsonObject out = JsonObject.of(
                "model", modelRef(ctx, model),
                "dryRun", Boolean.valueOf(dryRun));
        if (failedItems.size() > 0) {
            out.put("failedFeatures", failedItems);
        }
        if (missing.size() > 0) {
            out.put("missingParameters", missing);
        }

        if (!blockers.isEmpty()) {
            out.put("saved", Boolean.FALSE);
            out.put("blocked", Boolean.TRUE);
            out.put("reasons", toJsonArray(blockers));
            return out;
        }

        out.put("blocked", Boolean.FALSE);
        if (dryRun) {
            out.put("saved", Boolean.FALSE);
            out.put("wouldSave", Boolean.TRUE);
            return out;
        }

        try {
            model.Save();
            out.put("saved", Boolean.TRUE);
        } catch (jxthrowable | RuntimeException e) {
            out.put("saved", Boolean.FALSE);
            out.put("error", rootMessage(e));
        }
        return out;
    }

    private static boolean hasValue(Model model, String pname) {
        try {
            Parameter p = model.GetParam(pname);
            if (p == null) {
                return false;
            }
            Object value = paramValue(p.GetValue());
            return value != null && !String.valueOf(value).trim().isEmpty();
        } catch (jxthrowable | RuntimeException e) {
            return false;
        }
    }
}
