package com.asyncjlink.commands.compositecommands.creation;

import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonArray;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcBase.Point3D;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModel2D.Model2D;
import com.ptc.pfc.pfcView.View;
import com.ptc.pfc.pfcView.ViewOwner;
import com.ptc.pfc.pfcView2D.GeneralViewCreateInstructions;
import com.ptc.pfc.pfcView2D.ProjectionViewCreateInstructions;
import com.ptc.pfc.pfcView2D.View2D;
import com.ptc.pfc.pfcView2D.pfcView2D;

import java.util.ArrayList;
import java.util.List;

/**
 * Drawing.CreateStandardViews — populate a drawing with the standard orthographic views.
 *
 * <p><strong>Implemented from the dictionary but not yet exercised live</strong> — this touches the
 * same {@code pfcDrawing}/{@code pfcModel2D} package where {@code Session.CreateDrawing}'s {@code
 * DrawingCreateOptions} argument was found to crash the whole async connection when passed as
 * {@code null}; every optional argument below is built explicitly for that reason, but confirm this
 * command against a real session before relying on it, the same way {@code Drawing.GetDimensions}
 * was hand-verified before being written up.
 *
 * <p>The first view is a {@code GeneralViewCreateInstructions} built from the documented model's own
 * saved view ({@code Model} is a {@code ViewOwner}; {@code "FRONT"}/{@code "TOP"}/{@code "RIGHT"} are
 * Creo's own default saved view names) — its {@code Transform3D} is what actually sets the
 * orientation, not the factory's {@code int} argument, which the dictionary's own field list next to
 * the factory shows is the sheet number. Every further view is a {@code ProjectionViewCreateInstructions}
 * anchored to the first, which is how Creo derives top/right from a front view without needing a
 * second orientation lookup.
 */
public final class DrawingCreateStandardViewsCommand extends Composite {

    @Override public String name() { return "Drawing.CreateStandardViews"; }
    @Override public String receiverType() { return "Drawing"; }
    @Override public String signature() { return "CreateStandardViews(target[, views, model, ...]) — composite"; }

    @Override
    public String description() {
        return "Create the standard orthographic views (front, top, right by default) on a "
                + "drawing sheet from a model. The first view carries the orientation; every "
                + "further one is a projection off it.";
    }

    @Override
    public JsonSchema paramSchema() {
        return targeted(name(), "Drawing", "The drawing to add views to.")
                .optional("model", JsonSchema.string(),
                        "The model to document. Defaults to the drawing's current solid.")
                .optional("views", JsonSchema.array(JsonSchema.string()),
                        "Saved view names on the model, first is the base orientation and the "
                                + "rest are projected from it. Default [\"FRONT\", \"TOP\", \"RIGHT\"].")
                .optional("sheet", JsonSchema.integer(), "Sheet number for the first view. Default 1.")
                .optional("scale", JsonSchema.number(), "View scale. Defaults to Creo's own choice.")
                .optional("x", JsonSchema.number(), "Placement X for the first view. Default 0.")
                .optional("y", JsonSchema.number(), "Placement Y for the first view. Default 0.")
                .optional("spacing", JsonSchema.number(),
                        "Distance between the base view and each projection. Default 10.");
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

        String modelRefName = params.getString("model", null);
        Model documented = modelRefName != null && !modelRefName.isEmpty()
                ? ctx.resolveModel(modelRefName)
                : drawing.GetCurrentSolid();
        if (documented == null) {
            throw new CommandException(
                    "No documented model; pass 'model' explicitly", "invalid_params");
        }
        if (!(documented instanceof ViewOwner)) {
            throw new CommandException(
                    "Field 'model' has no saved views (it is a " + typeOf(documented) + ")",
                    "invalid_params");
        }
        ViewOwner viewOwner = (ViewOwner) documented;

        List<String> viewNames = names(params);
        int sheet = params.getInt("sheet", 1);
        double x = params.getDouble("x", 0);
        double y = params.getDouble("y", 0);
        double spacing = params.getDouble("spacing", 10);
        Double scale = params.get("scale") == null ? null : Double.valueOf(params.getDouble("scale", 1));

        JsonArray created = new JsonArray();
        View2D previous = null;
        for (int i = 0; i < viewNames.size(); i++) {
            String viewName = viewNames.get(i);
            View2D view2d;
            if (i == 0) {
                View savedView = viewOwner.GetView(viewName);
                if (savedView == null) {
                    throw new CommandException(
                            "Model has no saved view named '" + viewName + "'", "invalid_params");
                }
                GeneralViewCreateInstructions instructions = pfcView2D.GeneralViewCreateInstructions_Create(
                        documented, sheet, point(x, y), savedView.GetTransform());
                if (scale != null) {
                    instructions.SetScale(scale);
                }
                view2d = drawing.CreateView(instructions);
            } else {
                double px = x + spacing * i;
                ProjectionViewCreateInstructions instructions =
                        pfcView2D.ProjectionViewCreateInstructions_Create(previous, point(px, y));
                view2d = drawing.CreateView(instructions);
            }
            if (view2d == null) {
                throw new CommandException(
                        "Creo returned no view for '" + viewName + "'", "creo_error");
            }
            JsonObject entry = JsonObject.of("requestedView", viewName);
            entry.putIfPresent("name", safeName(view2d));
            entry.putIfPresent("scale", safeScale(view2d));
            created.add(entry);
            previous = view2d;
        }

        return JsonObject.of(
                "drawing", modelRef(ctx, drawing),
                "documents", modelRef(ctx, documented),
                "views", created);
    }

    private static List<String> names(JsonObject params) {
        JsonArray requested = params.getArray("views");
        List<String> out = new ArrayList<>();
        if (requested == null || requested.size() == 0) {
            out.add("FRONT");
            out.add("TOP");
            out.add("RIGHT");
            return out;
        }
        for (int i = 0; i < requested.size(); i++) {
            out.add(String.valueOf(requested.get(i)));
        }
        return out;
    }

    private static Point3D point(double x, double y) throws jxthrowable {
        Point3D p = Point3D.create();
        p.set(0, x);
        p.set(1, y);
        p.set(2, 0);
        return p;
    }

    private static String safeName(View2D view) {
        try {
            return view.GetName();
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }

    private static Double safeScale(View2D view) {
        try {
            return Double.valueOf(view.GetScale());
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }
}
