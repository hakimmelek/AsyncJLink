package com.asyncjlink.commands.compositecommands.selection;

import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Enums;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonArray;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcFeature.Feature;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModelItem.ModelItem;
import com.ptc.pfc.pfcModelItem.ModelItemType;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Model.FindItems — the programmatic counterpart to selection.
 *
 * <p>Finds any model item by type, name pattern or status: surfaces, edges, datums, axes,
 * coordinate systems, features, dimensions. Without this, nothing that mutates geometry can say
 * which geometry it means.
 */
public final class ModelFindItemsCommand extends Composite {

    @Override public String name() { return "Model.FindItems"; }
    @Override public String receiverType() { return "Model"; }
    @Override public String signature() { return "FindItems(target, itemType[, namePattern]) — composite"; }

    @Override
    public String description() {
        return "Find model items by type and name pattern — surfaces, edges, datums, axes, "
                + "coordinate systems, features, dimensions. The addressing layer for everything "
                + "that acts on geometry.";
    }

    @Override
    public JsonSchema paramSchema() {
        return targeted(name(), "Model", "The model to search.")
                .required("itemType", JsonSchema.string(),
                        "ModelItemType constant, e.g. ITEM_SURFACE, ITEM_FEATURE, ITEM_AXIS, "
                                + "ITEM_DIMENSION, ITEM_COORD_SYS.")
                .optional("namePattern", JsonSchema.string(),
                        "Name filter; * and ? are wildcards. Items with no name are excluded when set.")
                .optional("limit", JsonSchema.integer(),
                        "Stop after this many matches. Default 500.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Model model = requireModel(ctx, params);

        String typeName = params.getString("itemType", null);
        if (typeName == null || typeName.isEmpty()) {
            throw new CommandException(
                    "Field 'itemType' is required, e.g. ITEM_SURFACE or ITEM_FEATURE",
                    "invalid_params");
        }
        ModelItemType type = (ModelItemType) Enums.fromJson("ModelItemType", typeName, "itemType");

        Pattern pattern = glob(params.getString("namePattern", null));
        int limit = params.getInt("limit", 500);

        List<ModelItem> found = items(model, type);
        JsonArray out = new JsonArray();
        boolean truncated = false;
        for (ModelItem item : found) {
            if (out.size() >= limit) {
                truncated = true;
                break;
            }
            // Read the name once and hand it to itemRef; each accessor is a Creo round trip, and
            // this loop can run over several hundred items.
            String itemName = safeName(item);
            if (pattern != null && !matches(pattern, itemName)) {
                continue;
            }
            JsonObject entry = itemRef(ctx, item, itemName);
            if (item instanceof Feature) {
                entry.putIfPresent("status", featureStatus((Feature) item));
            }
            out.add(entry);
        }

        JsonObject result = JsonObject.of(
                "itemType", typeName,
                "matched", Integer.valueOf(out.size()),
                "scanned", Integer.valueOf(found.size()),
                "items", out);
        if (truncated) {
            result.put("truncated", Boolean.TRUE);
        }
        return result;
    }
}
