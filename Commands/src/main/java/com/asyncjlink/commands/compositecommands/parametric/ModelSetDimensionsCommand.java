package com.asyncjlink.commands.compositecommands.parametric;

import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonArray;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcDimension.BaseDimension;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModelItem.ModelItem;
import com.ptc.pfc.pfcModelItem.ModelItemType;
import com.ptc.pfc.pfcSolid.Solid;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Model.SetDimensions — set dimensions by symbol, regenerate, report what broke.
 *
 * <p>The highest-value mutating composite: "make the bracket 5 mm thicker" becomes a single call.
 *
 * <p>It regenerates once at the end rather than per dimension, and a change that regenerates into a
 * broken model is reported as a failure rather than a success — in Creo an edit can succeed at the
 * API level and only surface as damage several operations later.
 */
public final class ModelSetDimensionsCommand extends Composite {

    @Override public String name() { return "Model.SetDimensions"; }
    @Override public String receiverType() { return "Model"; }
    @Override public String signature() { return "SetDimensions(target, dimensions[, dryRun]) — composite"; }

    @Override
    public String description() {
        return "Set dimensions by symbol (e.g. {\"d12\": 25.0}), regenerate once, and report both "
                + "before/after values and any features the change broke. Supports dryRun.";
    }

    @Override
    public JsonSchema paramSchema() {
        return targeted(name(), "Solid", "The part or assembly to modify.")
                .required("dimensions", JsonSchema.dataObject("Dimensions"),
                        "Symbol/value pairs, e.g. {\"d12\": 25.0}.")
                .optional("dryRun", JsonSchema.bool(),
                        "Report what would change without writing. Default false.")
                .optional("regenerate", JsonSchema.bool(),
                        "Regenerate after setting. Default true; turning it off leaves the model "
                                + "in an unregenerated state and is rarely what you want.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Solid solid = requireSolid(ctx, params);
        JsonObject wanted = params.getObject("dimensions");
        if (wanted == null || wanted.isEmpty()) {
            throw new CommandException(
                    "Field 'dimensions' must be an object of symbol/value pairs, "
                            + "e.g. {\"d12\": 25.0}", "invalid_params");
        }
        boolean dryRun = dryRun(params);

        Map<String, BaseDimension> bySymbol = index(solid);

        JsonArray results = new JsonArray();
        int changed = 0;
        int rejected = 0;

        for (String symbol : wanted.keys()) {
            JsonObject entry = JsonObject.of("symbol", symbol);
            Object requested = wanted.get(symbol);
            entry.put("requested", requested);

            BaseDimension dim = bySymbol.get(symbol.toUpperCase(java.util.Locale.ROOT));
            if (dim == null) {
                entry.put("action", "rejected");
                entry.put("reason", "no dimension with this symbol; Model.GetDimensions lists them");
                rejected++;
                results.add(entry);
                continue;
            }

            double value;
            try {
                value = number(requested, symbol);
            } catch (CommandException e) {
                entry.put("action", "rejected");
                entry.put("reason", e.getMessage());
                rejected++;
                results.add(entry);
                continue;
            }

            try {
                entry.put("before", Double.valueOf(dim.GetDimValue()));
            } catch (jxthrowable | RuntimeException ignored) {
                // A dimension that will not report its value can still usually be set.
            }

            if (dryRun) {
                entry.put("action", "wouldSet");
                entry.put("after", Double.valueOf(value));
                changed++;
                results.add(entry);
                continue;
            }

            try {
                dim.SetDimValue(value);
                entry.put("action", "set");
                entry.put("after", Double.valueOf(dim.GetDimValue()));
                changed++;
            } catch (jxthrowable | RuntimeException e) {
                entry.put("action", "failed");
                entry.put("error", rootMessage(e));
                rejected++;
            }
            results.add(entry);
        }

        JsonObject out = JsonObject.of(
                "model", modelRef(ctx, (Model) solid),
                "dryRun", Boolean.valueOf(dryRun),
                "changed", Integer.valueOf(changed),
                "rejected", Integer.valueOf(rejected),
                "dimensions", results);

        if (!dryRun && changed > 0 && params.getBoolean("regenerate", true)) {
            JsonObject regen = regenerate(ctx, solid);
            out.put("regeneration", regen);
            boolean ok = regen.getBoolean("regenerated", false)
                    && regen.getInt("failedFeatureCount", 0) == 0;
            out.put("ok", Boolean.valueOf(ok));
            if (!ok) {
                out.put("warning", "The model did not come back clean. Inspect "
                        + "regeneration.failedFeatures before relying on this geometry.");
            }
        }
        return out;
    }

    /** Symbols are matched case-insensitively; Creo reports them lower-case but users type either. */
    private static Map<String, BaseDimension> index(Solid solid) throws jxthrowable {
        Map<String, BaseDimension> out = new LinkedHashMap<>();
        for (ModelItem item : items(solid, ModelItemType.ITEM_DIMENSION)) {
            if (!(item instanceof BaseDimension)) {
                continue;
            }
            BaseDimension dim = (BaseDimension) item;
            try {
                String symbol = dim.GetSymbol();
                if (symbol != null) {
                    out.put(symbol.toUpperCase(java.util.Locale.ROOT), dim);
                }
            } catch (jxthrowable | RuntimeException ignored) {
                // Unnamed dimensions cannot be addressed by symbol, so they are simply not indexed.
            }
        }
        return out;
    }

    private static double number(Object json, String field) {
        if (json instanceof Number) {
            return ((Number) json).doubleValue();
        }
        try {
            return Double.parseDouble(String.valueOf(json).trim());
        } catch (NumberFormatException e) {
            throw new CommandException(
                    "Dimension '" + field + "' needs a number but was '" + json + "'",
                    "invalid_params");
        }
    }
}
