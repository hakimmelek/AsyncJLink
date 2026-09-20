package com.asyncjlink.commands.compositecommands.parametric;

import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonArray;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcDimension.BaseDimension;
import com.ptc.pfc.pfcDimension.DimTolLimits;
import com.ptc.pfc.pfcDimension.DimTolPlusMinus;
import com.ptc.pfc.pfcDimension.DimTolSymmetric;
import com.ptc.pfc.pfcDimension.DimTolerance;
import com.ptc.pfc.pfcDimension.pfcDimension;
import com.ptc.pfc.pfcDimension2D.Dimension2D;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModel2D.Model2D;
import com.ptc.pfc.pfcModelItem.ModelItemType;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Drawing.SetDimensionTolerances — set tolerances on drawing dimensions in bulk, by symbol.
 *
 * <p>Applying a company's tolerance standard across a drawing one dimension at a time in the UI is
 * exactly the kind of repetitive, error-prone task this project exists to remove.
 *
 * <p>{@code DimTolerance} is a base interface with no writable fields of its own — the value lives
 * on one of three concrete subtypes ({@code DimTolSymmetric}, {@code DimTolPlusMinus},
 * {@code DimTolLimits}), each built through its own factory on the {@code pfcDimension} package
 * class. There is no fourth "no tolerance" factory, so this command cannot clear a tolerance, only
 * set one.
 */
public final class DrawingSetDimensionTolerancesCommand extends Composite {

    @Override public String name() { return "Drawing.SetDimensionTolerances"; }
    @Override public String receiverType() { return "Drawing"; }
    @Override public String signature() { return "SetDimensionTolerances(target, tolerances[, dryRun]) — composite"; }

    @Override
    public String description() {
        return "Set tolerances on drawing dimensions in bulk, by symbol, e.g. "
                + "{\"d12\": {\"type\": \"symmetric\", \"value\": 0.05}}. Supports dryRun.";
    }

    @Override
    public JsonSchema paramSchema() {
        return targeted(name(), "Drawing", "The drawing whose dimension tolerances to set.")
                .required("tolerances", JsonSchema.dataObject("Tolerances"),
                        "Symbol/tolerance pairs. Each tolerance is "
                                + "{\"type\": \"symmetric\", \"value\": N} or "
                                + "{\"type\": \"plusMinus\", \"plus\": N, \"minus\": N} or "
                                + "{\"type\": \"limits\", \"upper\": N, \"lower\": N}.")
                .optional("model", JsonSchema.string(),
                        "Restrict to dimensions of this documented model. Defaults to the "
                                + "drawing's current solid.")
                .optional("dryRun", JsonSchema.bool(),
                        "Report what would change without writing. Default false.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Model drawingModel = requireModel(ctx, params);
        if (!(drawingModel instanceof Model2D)) {
            throw new CommandException(
                    "Field 'target' must be a drawing, but was a " + typeOf(drawingModel),
                    "invalid_params");
        }
        Model2D drawing = (Model2D) drawingModel;

        String modelRef = params.getString("model", null);
        Model documented = modelRef != null && !modelRef.isEmpty()
                ? ctx.resolveModel(modelRef)
                : drawing.GetCurrentSolid();
        if (documented == null) {
            throw new CommandException(
                    "No documented model to search dimensions on; pass 'model' explicitly",
                    "invalid_params");
        }

        JsonObject wanted = params.getObject("tolerances");
        if (wanted == null || wanted.isEmpty()) {
            throw new CommandException(
                    "Field 'tolerances' must be an object of symbol/tolerance pairs",
                    "invalid_params");
        }
        boolean dryRun = dryRun(params);

        Map<String, Dimension2D> bySymbol = index(drawing, documented);

        JsonArray results = new JsonArray();
        int changed = 0;
        int rejected = 0;

        for (String symbol : wanted.keys()) {
            JsonObject entry = JsonObject.of("symbol", symbol);
            Dimension2D dim = bySymbol.get(symbol.toUpperCase(java.util.Locale.ROOT));
            if (dim == null) {
                entry.put("action", "rejected");
                entry.put("reason", "no shown dimension with this symbol; "
                        + "Drawing.GetDimensions lists them");
                rejected++;
                results.add(entry);
                continue;
            }

            Object spec = wanted.get(symbol);
            if (!(spec instanceof JsonObject)) {
                entry.put("action", "rejected");
                entry.put("reason", "tolerance must be an object with a 'type'");
                rejected++;
                results.add(entry);
                continue;
            }

            entry.putIfPresent("before", toJson(safeTolerance(dim)));

            DimTolerance tol;
            try {
                tol = fromJson((JsonObject) spec, symbol);
            } catch (CommandException e) {
                entry.put("action", "rejected");
                entry.put("reason", e.getMessage());
                rejected++;
                results.add(entry);
                continue;
            }

            if (dryRun) {
                entry.put("action", "wouldSet");
                entry.putIfPresent("after", toJson(tol));
                changed++;
                results.add(entry);
                continue;
            }

            try {
                dim.SetTolerance(tol);
                entry.put("action", "set");
                entry.putIfPresent("after", toJson(safeTolerance(dim)));
                changed++;
            } catch (jxthrowable | RuntimeException e) {
                entry.put("action", "failed");
                entry.put("error", rootMessage(e));
                rejected++;
            }
            results.add(entry);
        }

        return JsonObject.of(
                "drawing", modelRef(ctx, drawing),
                "documents", modelRef(ctx, documented),
                "dryRun", Boolean.valueOf(dryRun),
                "changed", Integer.valueOf(changed),
                "rejected", Integer.valueOf(rejected),
                "tolerances", results);
    }

    private static DimTolerance fromJson(JsonObject spec, String symbol) {
        String type = spec.getString("type", null);
        if (type == null) {
            throw new CommandException(
                    "Tolerance for '" + symbol + "' needs a 'type': symmetric, plusMinus or limits",
                    "invalid_params");
        }
        try {
            switch (type) {
                case "symmetric":
                    return pfcDimension.DimTolSymmetric_Create(
                            Double.valueOf(spec.getDouble("value", 0)));
                case "plusMinus":
                    return pfcDimension.DimTolPlusMinus_Create(
                            Double.valueOf(spec.getDouble("plus", 0)),
                            Double.valueOf(spec.getDouble("minus", 0)));
                case "limits":
                    return pfcDimension.DimTolLimits_Create(
                            Double.valueOf(spec.getDouble("upper", 0)),
                            Double.valueOf(spec.getDouble("lower", 0)));
                default:
                    throw new CommandException(
                            "Tolerance type '" + type + "' for '" + symbol
                                    + "' is not one of symmetric, plusMinus, limits", "invalid_params");
            }
        } catch (jxthrowable e) {
            throw new CommandException(
                    "Creo rejected the tolerance for '" + symbol + "': " + rootMessage(e),
                    "invalid_params", e);
        }
    }

    /** Package-visible so {@link DrawingGetDimensionsCommand} can render the same shape. */
    static JsonObject toJson(DimTolerance t) {
        if (t == null) {
            return null;
        }
        try {
            if (t instanceof DimTolSymmetric) {
                return JsonObject.of("type", "symmetric",
                        "value", Double.valueOf(((DimTolSymmetric) t).GetValue()));
            }
            if (t instanceof DimTolPlusMinus) {
                DimTolPlusMinus pm = (DimTolPlusMinus) t;
                return JsonObject.of("type", "plusMinus",
                        "plus", Double.valueOf(pm.GetPlus()),
                        "minus", Double.valueOf(pm.GetMinus()));
            }
            if (t instanceof DimTolLimits) {
                DimTolLimits l = (DimTolLimits) t;
                return JsonObject.of("type", "limits",
                        "upper", Double.valueOf(l.GetUpperLimit()),
                        "lower", Double.valueOf(l.GetLowerLimit()));
            }
            return JsonObject.of("type", typeOf(t));
        } catch (jxthrowable | RuntimeException e) {
            return JsonObject.of("type", typeOf(t), "error", rootMessage(e));
        }
    }

    private static DimTolerance safeTolerance(Dimension2D dim) {
        try {
            return dim.GetTolerance();
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }

    private static Map<String, Dimension2D> index(Model2D drawing, Model documented) throws jxthrowable {
        Map<String, Dimension2D> out = new LinkedHashMap<>();
        com.ptc.pfc.pfcDimension2D.Dimension2Ds shown =
                drawing.ListShownDimensions(documented, ModelItemType.ITEM_DIMENSION);
        if (shown == null) {
            return out;
        }
        for (int i = 0; i < shown.getarraysize(); i++) {
            Dimension2D d = shown.get(i);
            if (d == null || !(d instanceof BaseDimension)) {
                continue;
            }
            try {
                String symbol = ((BaseDimension) d).GetSymbol();
                if (symbol != null) {
                    out.put(symbol.toUpperCase(java.util.Locale.ROOT), d);
                }
            } catch (jxthrowable | RuntimeException ignored) {
                // Unnamed dimensions cannot be addressed by symbol.
            }
        }
        return out;
    }
}
