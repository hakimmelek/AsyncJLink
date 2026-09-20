package com.asyncjlink.commands.compositecommands.parametric;

import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Enums;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonArray;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcDimension.BaseDimension;
import com.ptc.pfc.pfcDimension.DimTolerance;
import com.ptc.pfc.pfcDimension2D.Dimension2D;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModel2D.Model2D;
import com.ptc.pfc.pfcModelItem.ModelItemType;
import com.ptc.pfc.pfcView2D.View2D;

/**
 * Drawing.GetDimensions — every dimension actually shown on a drawing.
 *
 * <p>As opposed to {@code Model.GetDimensions}, which lists every dimension the part has regardless
 * of whether it appears on paper. A drawing normally shows a curated subset, and that subset, with
 * its tolerances, is what a supplier or a checker actually reads.
 */
public final class DrawingGetDimensionsCommand extends Composite {

    @Override public String name() { return "Drawing.GetDimensions"; }
    @Override public String receiverType() { return "Drawing"; }
    @Override public String signature() { return "GetDimensions(target[, model]) — composite"; }

    @Override
    public String description() {
        return "Every dimension shown on a drawing, with value, type and tolerance. Different from "
                + "Model.GetDimensions, which lists every dimension a part has whether or not it is "
                + "on the sheet.";
    }

    @Override
    public JsonSchema paramSchema() {
        return targeted(name(), "Drawing", "The drawing whose shown dimensions to read.")
                .optional("model", JsonSchema.string(),
                        "Restrict to dimensions of this documented model, by name or handle. "
                                + "Defaults to the drawing's current solid.");
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
        Model documented;
        if (modelRef != null && !modelRef.isEmpty()) {
            documented = ctx.resolveModel(modelRef);
            if (documented == null) {
                throw new CommandException(
                        "Field 'model': no model named '" + modelRef + "' is in session",
                        "unknown_handle");
            }
        } else {
            documented = drawing.GetCurrentSolid();
            if (documented == null) {
                throw new CommandException(
                        "This drawing has no current solid; pass 'model' explicitly",
                        "invalid_params");
            }
        }

        JsonArray out = new JsonArray();
        int scanned = 0;
        for (Dimension2D dim : list(drawing, documented)) {
            scanned++;
            if (!(dim instanceof BaseDimension)) {
                continue;
            }
            BaseDimension base = (BaseDimension) dim;
            JsonObject entry = itemRef(ctx, dim, null);
            entry.putIfPresent("symbol", safeSymbol(base));
            entry.putIfPresent("value", safeValue(base));
            entry.putIfPresent("dimensionType", safeDimType(base));
            entry.putIfPresent("tolerance", safeTolerance(dim));
            entry.putIfPresent("toleranceDisplayed", safeToleranceDisplayed(dim));
            entry.putIfPresent("view", safeViewName(dim));
            out.add(entry);
        }

        return JsonObject.of(
                "drawing", modelRef(ctx, drawing),
                "documents", modelRef(ctx, documented),
                "matched", Integer.valueOf(out.size()),
                "scanned", Integer.valueOf(scanned),
                "dimensions", out);
    }

    @SuppressWarnings("unchecked")
    private static java.util.List<Dimension2D> list(Model2D drawing, Model documented) throws jxthrowable {
        java.util.List<Dimension2D> out = new java.util.ArrayList<>();
        com.ptc.pfc.pfcDimension2D.Dimension2Ds shown =
                drawing.ListShownDimensions(documented, ModelItemType.ITEM_DIMENSION);
        if (shown == null) {
            return out;
        }
        for (int i = 0; i < shown.getarraysize(); i++) {
            Dimension2D d = shown.get(i);
            if (d != null) {
                out.add(d);
            }
        }
        return out;
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

    private static JsonObject safeTolerance(Dimension2D dim) {
        try {
            DimTolerance t = dim.GetTolerance();
            if (t == null) {
                return null;
            }
            return DrawingSetDimensionTolerancesCommand.toJson(t);
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }

    private static Boolean safeToleranceDisplayed(Dimension2D dim) {
        try {
            return Boolean.valueOf(dim.GetIsToleranceDisplayed());
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }

    private static String safeViewName(Dimension2D dim) {
        try {
            View2D view = dim.GetView();
            return view == null ? null : view.GetName();
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }
}
