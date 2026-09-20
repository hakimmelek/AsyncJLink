package com.asyncjlink.commands.compositecommands.presentation;

import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Enums;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonArray;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcLayer.DisplayStatus;
import com.ptc.pfc.pfcLayer.Layer;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModelItem.ModelItem;
import com.ptc.pfc.pfcModelItem.ModelItemType;
import com.ptc.pfc.pfcWindow.Window;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Model.SetLayerStatus — show or hide layers.
 *
 * <p>Needed for clean captures and for exports that should not carry construction geometry. Display
 * only: nothing about the model's content changes.
 */
public final class ModelSetLayerStatusCommand extends Composite {

    @Override public String name() { return "Model.SetLayerStatus"; }
    @Override public String receiverType() { return "Model"; }
    @Override public String signature() { return "SetLayerStatus(target, status[, layers, namePattern]) — composite"; }

    @Override
    public String description() {
        return "Show or hide layers by name or pattern (LAYER_NORMAL, LAYER_DISPLAY, LAYER_BLANK, "
                + "LAYER_HIDDEN). Display only — no geometry is changed.";
    }

    @Override
    public JsonSchema paramSchema() {
        return targeted(name(), "Model", "The model whose layers to change.")
                .required("status", JsonSchema.enumOf("DisplayStatus",
                        "LAYER_NORMAL", "LAYER_DISPLAY", "LAYER_BLANK", "LAYER_HIDDEN"),
                        "The display status to apply.")
                .optional("layers", JsonSchema.array(JsonSchema.string()),
                        "Layer names to change.")
                .optional("namePattern", JsonSchema.string(),
                        "Select layers by name instead; * and ? are wildcards.")
                .optional("dryRun", JsonSchema.bool(),
                        "Report what would change without changing it. Default false.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Model model = requireModel(ctx, params);
        String statusName = params.getString("status", null);
        if (statusName == null || statusName.isEmpty()) {
            throw new CommandException("Field 'status' is required", "invalid_params");
        }
        DisplayStatus status =
                (DisplayStatus) Enums.fromJson("DisplayStatus", statusName, "status");

        JsonArray asked = params.getArray("layers");
        Pattern pattern = glob(params.getString("namePattern", null));
        if ((asked == null || asked.size() == 0) && pattern == null) {
            throw new CommandException(
                    "Select layers with 'layers' or with 'namePattern'", "invalid_params");
        }
        boolean dryRun = dryRun(params);

        JsonArray results = new JsonArray();
        int changed = 0;

        List<ModelItem> layers = items(model, ModelItemType.ITEM_LAYER);
        for (ModelItem item : layers) {
            if (!(item instanceof Layer)) {
                continue;
            }
            Layer layer = (Layer) item;
            String lname = safeName(item);
            if (!selected(lname, asked, pattern)) {
                continue;
            }

            JsonObject entry = JsonObject.of("name", lname);
            try {
                entry.putIfPresent("before", Enums.toJson(layer.GetStatus()));
            } catch (jxthrowable | RuntimeException ignored) {
                // Some layers will not report status but can still be set.
            }

            if (dryRun) {
                entry.put("action", "wouldSet");
                entry.put("after", statusName);
                changed++;
            } else {
                try {
                    layer.SetStatus(status);
                    entry.put("action", "set");
                    entry.put("after", statusName);
                    changed++;
                } catch (jxthrowable | RuntimeException e) {
                    entry.put("action", "failed");
                    entry.put("error", rootMessage(e));
                }
            }
            results.add(entry);
        }

        if (!dryRun && changed > 0) {
            try {
                Window window = ctx.session().GetModelWindow(model);
                if (window != null) {
                    window.Repaint();
                }
            } catch (jxthrowable | RuntimeException ignored) {
                // The status is set regardless of whether the screen caught up.
            }
        }

        return JsonObject.of(
                "model", modelRef(ctx, model),
                "dryRun", Boolean.valueOf(dryRun),
                "changed", Integer.valueOf(changed),
                "scanned", Integer.valueOf(layers.size()),
                "layers", results);
    }

    private static boolean selected(String name, JsonArray asked, Pattern pattern) {
        if (asked != null) {
            for (int i = 0; i < asked.size(); i++) {
                if (String.valueOf(asked.get(i)).equalsIgnoreCase(name)) {
                    return true;
                }
            }
        }
        return pattern != null && matches(pattern, name);
    }
}
