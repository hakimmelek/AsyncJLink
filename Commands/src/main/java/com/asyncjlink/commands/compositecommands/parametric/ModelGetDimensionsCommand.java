package com.asyncjlink.commands.compositecommands.parametric;

import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Enums;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonArray;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcDimension.BaseDimension;
import com.ptc.pfc.pfcDimension.Dimension;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModelItem.ModelItem;
import com.ptc.pfc.pfcModelItem.ModelItemType;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Model.GetDimensions — every dimension with its symbol, value and tolerance.
 *
 * <p>Dimensions are how parametric geometry is actually changed, and the symbol ({@code d12}) is the
 * stable, handle-free address for one — which is what makes {@code Model.SetDimensions} usable from
 * the CLI, where handles do not survive between invocations.
 */
public final class ModelGetDimensionsCommand extends Composite {

    @Override public String name() { return "Model.GetDimensions"; }
    @Override public String receiverType() { return "Model"; }
    @Override public String signature() { return "GetDimensions(target[, symbolPattern]) — composite"; }

    @Override
    public String description() {
        return "Every dimension of a model with symbol (d12), value, type and tolerance. The symbol "
                + "is the address used by Model.SetDimensions.";
    }

    @Override
    public JsonSchema paramSchema() {
        return targeted(name(), "Model", "The model whose dimensions to read.")
                .optional("symbolPattern", JsonSchema.string(),
                        "Symbol filter, e.g. \"d*\"; * and ? are wildcards.")
                .optional("includeReference", JsonSchema.bool(),
                        "Also include reference dimensions (ITEM_REF_DIMENSION). Default false.")
                .optional("detail", JsonSchema.bool(),
                        "Also report display texts and tolerance. Costs two extra Creo round trips "
                                + "per dimension; default false.")
                .optional("limit", JsonSchema.integer(),
                        "Stop after this many matches. Default 500.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Model model = requireModel(ctx, params);
        Pattern pattern = glob(params.getString("symbolPattern", null));
        boolean detail = params.getBoolean("detail", false);
        int limit = params.getInt("limit", 500);

        JsonArray out = new JsonArray();
        int scanned = 0;

        scanned += collect(ctx, model, ModelItemType.ITEM_DIMENSION, pattern, out, false, detail,
                limit);
        if (params.getBoolean("includeReference", false)) {
            scanned += collect(ctx, model, ModelItemType.ITEM_REF_DIMENSION, pattern, out, true,
                    detail, limit);
        }

        JsonObject result = JsonObject.of(
                "model", modelRef(ctx, model),
                "matched", Integer.valueOf(out.size()),
                "scanned", Integer.valueOf(scanned),
                "dimensions", out);
        if (out.size() >= limit) {
            result.put("truncated", Boolean.TRUE);
        }
        return result;
    }

    private static int collect(CreoContext ctx, Model model, ModelItemType type, Pattern pattern,
            JsonArray out, boolean reference, boolean detail, int limit) throws jxthrowable {
        List<ModelItem> found = items(model, type);
        for (ModelItem item : found) {
            if (out.size() >= limit) {
                break;
            }
            if (!(item instanceof BaseDimension)) {
                continue;
            }
            BaseDimension dim = (BaseDimension) item;
            String symbol = safeSymbol(dim);
            if (!matches(pattern, symbol)) {
                continue;
            }
            // A dimension is addressed by its symbol, never by a name — most have none, and asking
            // for one costs a round trip that throws. itemRef is told so rather than finding out.
            JsonObject entry = itemRef(ctx, item, null);
            entry.putIfPresent("symbol", symbol);
            entry.putIfPresent("value", safeValue(dim));
            entry.putIfPresent("dimensionType", safeDimType(dim));
            entry.put("reference", Boolean.valueOf(reference));
            if (detail) {
                entry.putIfPresent("texts", texts(dim));
                if (dim instanceof Dimension) {
                    entry.putIfPresent("tolerance", tolerance((Dimension) dim));
                }
            }
            out.add(entry);
        }
        return found.size();
    }

    private static String safeSymbol(BaseDimension dim) {
        try {
            return dim.GetSymbol();
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }

    private static Double safeValue(BaseDimension dim) {
        try {
            return Double.valueOf(dim.GetDimValue());
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }

    private static String safeDimType(BaseDimension dim) {
        try {
            return Enums.toJson(dim.GetDimType());
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }

    private static JsonArray texts(BaseDimension dim) {
        try {
            return toJsonArray(strings(dim.GetTexts()));
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }

    private static String tolerance(Dimension dim) {
        try {
            Object t = dim.GetTolerance();
            // DimTolerance is a union of several shapes; naming the kind is more useful to a caller
            // than an expanded struct they then have to interpret.
            return t == null ? null : typeOf(t);
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }
}
