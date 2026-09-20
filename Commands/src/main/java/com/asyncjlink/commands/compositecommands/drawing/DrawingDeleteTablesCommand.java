package com.asyncjlink.commands.compositecommands.drawing;

import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonArray;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModelItem.ModelItem;
import com.ptc.pfc.pfcModelItem.ModelItemType;
import com.ptc.pfc.pfcTable.Table;

/**
 * Drawing.DeleteTables — remove tables by id.
 *
 * <p>Same shape and the same {@code dryRun}-defaults-{@code true} reasoning as
 * {@code Drawing.DeleteNotes} — a table is normally a BOM or hole table, and deleting the wrong one
 * is exactly the kind of mistake rule 3 exists for.
 */
public final class DrawingDeleteTablesCommand extends Composite {

    @Override public String name() { return "Drawing.DeleteTables"; }
    @Override public String receiverType() { return "Drawing"; }
    @Override public String signature() { return "DeleteTables(target, ids[, dryRun]) — composite"; }

    @Override
    public String description() {
        return "Remove tables by id. dryRun defaults to TRUE -- pass dryRun=false to actually "
                + "delete. There is no undo.";
    }

    @Override
    public JsonSchema paramSchema() {
        return targeted(name(), "Drawing", "The drawing to remove tables from.")
                .required("ids", JsonSchema.array(JsonSchema.integer()), "Table ids to remove.")
                .optional("dryRun", JsonSchema.bool(),
                        "Report what would happen without doing it. Defaults to TRUE, because the "
                                + "change cannot be undone.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Model drawing = requireModel(ctx, params);
        JsonArray ids = params.getArray("ids");
        if (ids == null || ids.size() == 0) {
            throw new CommandException("Field 'ids' must list at least one table id", "invalid_params");
        }
        boolean dryRun = params.getBoolean("dryRun", true);

        java.util.Map<Integer, Table> byId = new java.util.LinkedHashMap<>();
        for (ModelItem item : items(drawing, ModelItemType.ITEM_TABLE)) {
            if (item instanceof Table) {
                byId.put(Integer.valueOf(item.GetId()), (Table) item);
            }
        }

        JsonArray results = new JsonArray();
        int removed = 0;
        int rejected = 0;

        for (int i = 0; i < ids.size(); i++) {
            int id = ((Number) ids.get(i)).intValue();
            JsonObject entry = JsonObject.of("id", Integer.valueOf(id));
            Table table = byId.get(Integer.valueOf(id));
            if (table == null) {
                entry.put("action", "rejected");
                entry.put("reason", "no table with this id on this drawing");
                rejected++;
                results.add(entry);
                continue;
            }
            entry.putIfPresent("rowCount", safeRowCount(table));
            entry.putIfPresent("columnCount", safeColumnCount(table));

            if (dryRun) {
                entry.put("action", "wouldRemove");
                removed++;
                results.add(entry);
                continue;
            }
            try {
                table.Erase();
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
                "tables", results);
    }

    private static Integer safeRowCount(Table table) {
        try {
            return Integer.valueOf(table.GetRowCount());
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }

    private static Integer safeColumnCount(Table table) {
        try {
            return Integer.valueOf(table.GetColumnCount());
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }
}
