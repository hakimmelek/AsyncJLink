package com.asyncjlink.commands.compositecommands.parametric;

import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonArray;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModelItem.Parameter;
import com.ptc.pfc.pfcModelItem.ParamValue;

/**
 * Model.SetParameters — bulk parameter write, validated.
 *
 * <p>Setting revision, part number or material across many components is routine, and doing it one
 * raw call at a time is where transcription errors get in. Type mismatches are reported rather than
 * coerced silently: writing {@code "12"} into a double parameter yields a double and says so.
 */
public final class ModelSetParametersCommand extends Composite {

    @Override public String name() { return "Model.SetParameters"; }
    @Override public String receiverType() { return "Model"; }
    @Override public String signature() { return "SetParameters(target, parameters[, create, dryRun]) — composite"; }

    @Override
    public String description() {
        return "Set several parameters at once, reporting before and after for each. Supports "
                + "dryRun. Existing parameter types are preserved rather than silently changed.";
    }

    @Override
    public JsonSchema paramSchema() {
        return targeted(name(), "Model", "The model whose parameters to set.")
                .required("parameters", JsonSchema.dataObject("Parameters"),
                        "Name/value pairs to write.")
                .optional("create", JsonSchema.bool(),
                        "Create parameters that do not exist yet. Default false.")
                .optional("dryRun", JsonSchema.bool(),
                        "Report what would change without writing. Default false.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Model model = requireModel(ctx, params);
        JsonObject wanted = params.getObject("parameters");
        if (wanted == null || wanted.isEmpty()) {
            throw new CommandException(
                    "Field 'parameters' must be an object of name/value pairs", "invalid_params");
        }
        boolean create = params.getBoolean("create", false);
        boolean dryRun = dryRun(params);

        JsonArray results = new JsonArray();
        int changed = 0;
        int created = 0;
        int rejected = 0;

        for (String key : wanted.keys()) {
            Object value = wanted.get(key);
            JsonObject entry = JsonObject.of("name", key, "requested", value);

            Parameter existing;
            try {
                existing = model.GetParam(key);
            } catch (jxthrowable | RuntimeException e) {
                existing = null;
            }

            if (existing == null) {
                if (!create) {
                    entry.put("action", "rejected");
                    entry.put("reason", "no such parameter; set create=true to add it");
                    rejected++;
                    results.add(entry);
                    continue;
                }
                if (dryRun) {
                    entry.put("action", "wouldCreate");
                    created++;
                    results.add(entry);
                    continue;
                }
                try {
                    model.CreateParam(key, toParamValue(value, null, key));
                    entry.put("action", "created");
                    created++;
                } catch (CommandException e) {
                    entry.put("action", "rejected");
                    entry.put("reason", e.getMessage());
                    rejected++;
                } catch (jxthrowable | RuntimeException e) {
                    entry.put("action", "failed");
                    entry.put("error", rootMessage(e));
                    rejected++;
                }
                results.add(entry);
                continue;
            }

            ParamValue before;
            try {
                before = existing.GetValue();
                entry.putIfPresent("before", paramValue(before));
                entry.putIfPresent("type", paramValueType(before));
            } catch (jxthrowable | RuntimeException e) {
                before = null;
            }

            ParamValue next;
            try {
                next = toParamValue(value, before, key);
            } catch (CommandException e) {
                entry.put("action", "rejected");
                entry.put("reason", e.getMessage());
                rejected++;
                results.add(entry);
                continue;
            }

            if (dryRun) {
                entry.put("action", "wouldSet");
                entry.putIfPresent("after", paramValue(next));
                changed++;
                results.add(entry);
                continue;
            }

            try {
                existing.SetValue(next);
                entry.put("action", "set");
                entry.putIfPresent("after", paramValue(existing.GetValue()));
                changed++;
            } catch (jxthrowable | RuntimeException e) {
                entry.put("action", "failed");
                entry.put("error", rootMessage(e));
                rejected++;
            }
            results.add(entry);
        }

        return JsonObject.of(
                "model", modelRef(ctx, model),
                "dryRun", Boolean.valueOf(dryRun),
                "changed", Integer.valueOf(changed),
                "created", Integer.valueOf(created),
                "rejected", Integer.valueOf(rejected),
                "parameters", results);
    }
}
