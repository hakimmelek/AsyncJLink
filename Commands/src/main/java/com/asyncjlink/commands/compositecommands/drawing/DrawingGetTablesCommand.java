package com.asyncjlink.commands.compositecommands.drawing;

import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonArray;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.cipjava.stringseq;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModelItem.ModelItem;
import com.ptc.pfc.pfcModelItem.ModelItemType;
import com.ptc.pfc.pfcTable.ParamMode;
import com.ptc.pfc.pfcTable.Table;
import com.ptc.pfc.pfcTable.TableCell;
import com.ptc.pfc.pfcTable.pfcTable;

import java.util.List;

/**
 * Drawing.GetTables — every table on a drawing, with its cell text as a grid.
 *
 * <p>BOM tables and hole tables are the common case, and today reading one means finding it by eye
 * and walking {@code Table.GetRowCount}/{@code GetColumnCount}/{@code GetText} by hand — one call
 * per cell, since {@code Table.GetText} addresses a single {@code TableCell} at a time.
 */
public final class DrawingGetTablesCommand extends Composite {

    @Override public String name() { return "Drawing.GetTables"; }
    @Override public String receiverType() { return "Drawing"; }
    @Override public String signature() { return "GetTables(target[, includeCells]) — composite"; }

    @Override
    public String description() {
        return "Every table on a drawing, with row/column counts and, unless disabled, its cell "
                + "text as a grid.";
    }

    @Override
    public JsonSchema paramSchema() {
        return targeted(name(), "Drawing", "The drawing whose tables to read.")
                .optional("includeCells", JsonSchema.bool(),
                        "Read every cell's text too. Costs one Creo round trip per cell; default "
                                + "true.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Model drawing = requireModel(ctx, params);
        boolean includeCells = params.getBoolean("includeCells", true);
        List<ModelItem> found = items(drawing, ModelItemType.ITEM_TABLE);

        JsonArray out = new JsonArray();
        for (ModelItem item : found) {
            if (!(item instanceof Table)) {
                continue;
            }
            Table table = (Table) item;
            JsonObject entry = itemRef(ctx, item, null);
            Integer rows = safeInt(table, true);
            Integer cols = safeInt(table, false);
            entry.putIfPresent("rowCount", rows);
            entry.putIfPresent("columnCount", cols);
            if (includeCells && rows != null && cols != null) {
                entry.put("cells", grid(table, rows.intValue(), cols.intValue()));
            }
            out.add(entry);
        }

        return JsonObject.of(
                "drawing", modelRef(ctx, drawing),
                "matched", Integer.valueOf(out.size()),
                "tables", out);
    }

    private static JsonArray grid(Table table, int rows, int cols) {
        JsonArray grid = new JsonArray();
        for (int r = 1; r <= rows; r++) {
            JsonArray row = new JsonArray();
            for (int c = 1; c <= cols; c++) {
                row.add(cellText(table, r, c));
            }
            grid.add(row);
        }
        return grid;
    }

    private static String cellText(Table table, int row, int col) {
        try {
            TableCell cell = pfcTable.TableCell_Create(row, col);
            stringseq text = table.GetText(cell, ParamMode.DWGTABLE_NORMAL);
            if (text == null || text.getarraysize() == 0) {
                return "";
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < text.getarraysize(); i++) {
                if (i > 0) {
                    sb.append('\n');
                }
                sb.append(text.get(i));
            }
            return sb.toString();
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }

    private static Integer safeInt(Table table, boolean rowCount) {
        try {
            return Integer.valueOf(rowCount ? table.GetRowCount() : table.GetColumnCount());
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }
}
