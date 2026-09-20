package com.asyncjlink.commands.compositecommands.drawing;

import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonArray;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcDetail.DetailSymbolInstItem;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModelItem.ModelItem;
import com.ptc.pfc.pfcModelItem.ModelItemType;

/**
 * Drawing.DeleteSymbols — remove symbol instances by id.
 *
 * <p>Same shape and {@code dryRun}-defaults-{@code true} reasoning as {@code Drawing.DeleteNotes}.
 */
public final class DrawingDeleteSymbolsCommand extends Composite {

    @Override public String name() { return "Drawing.DeleteSymbols"; }
    @Override public String receiverType() { return "Drawing"; }
    @Override public String signature() { return "DeleteSymbols(target, ids[, dryRun]) — composite"; }

    @Override
    public String description() {
        return "Remove drawing symbol instances by id. dryRun defaults to TRUE -- pass "
                + "dryRun=false to actually delete. There is no undo.";
    }

    @Override
    public JsonSchema paramSchema() {
        return targeted(name(), "Drawing", "The drawing to remove symbols from.")
                .required("ids", JsonSchema.array(JsonSchema.integer()), "Symbol instance ids to remove.")
                .optional("dryRun", JsonSchema.bool(),
                        "Report what would happen without doing it. Defaults to TRUE, because the "
                                + "change cannot be undone.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Model drawing = requireModel(ctx, params);
        JsonArray ids = params.getArray("ids");
        if (ids == null || ids.size() == 0) {
            throw new CommandException("Field 'ids' must list at least one symbol id", "invalid_params");
        }
        boolean dryRun = params.getBoolean("dryRun", true);

        java.util.Map<Integer, DetailSymbolInstItem> byId = new java.util.LinkedHashMap<>();
        for (ModelItem item : items(drawing, ModelItemType.ITEM_DTL_SYM_INSTANCE)) {
            if (item instanceof DetailSymbolInstItem) {
                byId.put(Integer.valueOf(item.GetId()), (DetailSymbolInstItem) item);
            }
        }

        JsonArray results = new JsonArray();
        int removed = 0;
        int rejected = 0;

        for (int i = 0; i < ids.size(); i++) {
            int id = ((Number) ids.get(i)).intValue();
            JsonObject entry = JsonObject.of("id", Integer.valueOf(id));
            DetailSymbolInstItem symbol = byId.get(Integer.valueOf(id));
            if (symbol == null) {
                entry.put("action", "rejected");
                entry.put("reason", "no symbol instance with this id on this drawing");
                rejected++;
                results.add(entry);
                continue;
            }

            if (dryRun) {
                entry.put("action", "wouldRemove");
                removed++;
                results.add(entry);
                continue;
            }
            try {
                symbol.Remove();
                entry.put("action", "removed");
                removed++;
            } catch (jxthrowable | RuntimeException e) {
                entry.put("action", "failed");
                entry.put("error", rootMessage(e));
                rejected++;
            }
            results.add(entry);
        }

        return JsonObject.of(
                "drawing", modelRef(ctx, drawing),
                "dryRun", Boolean.valueOf(dryRun),
                "removed", Integer.valueOf(removed),
                "rejected", Integer.valueOf(rejected),
                "symbols", results);
    }
}
