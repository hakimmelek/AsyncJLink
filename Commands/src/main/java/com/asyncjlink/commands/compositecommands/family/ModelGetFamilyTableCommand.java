package com.asyncjlink.commands.compositecommands.family;

import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Enums;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonArray;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcFamily.FamilyMember;
import com.ptc.pfc.pfcFamily.FamilyTableColumn;
import com.ptc.pfc.pfcFamily.FamilyTableColumns;
import com.ptc.pfc.pfcFamily.FamilyTableRow;
import com.ptc.pfc.pfcFamily.FamilyTableRows;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModelItem.ParamValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Model.GetFamilyTable — a part family's whole table in one call.
 *
 * <p>Fasteners, gaskets, sheet-metal gauges and any other size-driven family are normally managed
 * entirely through the family table, and today that means walking {@code ListColumns}/{@code
 * ListRows} by hand, then a {@code GetCell} per cell with no type safety — reading a 10-row,
 * 6-column table one cell at a time is 60-plus calls for what a person does in one glance in Creo.
 */
public final class ModelGetFamilyTableCommand extends Composite {

    @Override public String name() { return "Model.GetFamilyTable"; }
    @Override public String receiverType() { return "Model"; }
    @Override public String signature() { return "GetFamilyTable(target) — composite"; }

    @Override
    public String description() {
        return "Every column, row and cell of a model's family table in one call, each cell typed "
                + "against its column.";
    }

    @Override
    public JsonSchema paramSchema() {
        return targeted(name(), "Model", "The model whose family table to read.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Model model = requireModel(ctx, params);
        FamilyMember table = requireFamilyMember(model);

        List<FamilyTableColumn> columns = columns(table);
        List<FamilyTableRow> rows = rows(table);

        JsonArray columnsOut = new JsonArray();
        for (FamilyTableColumn c : columns) {
            columnsOut.add(JsonObject.of(
                    "symbol", safeSymbol(c),
                    "type", safeColumnType(c)));
        }

        JsonArray rowsOut = new JsonArray();
        for (FamilyTableRow r : rows) {
            JsonObject rowOut = JsonObject.of("instanceName", safeInstanceName(r));
            rowOut.putIfPresent("locked", safeLocked(r));
            rowOut.putIfPresent("verified", safeVerified(r));
            JsonArray cells = new JsonArray();
            for (FamilyTableColumn c : columns) {
                JsonObject cell = JsonObject.of("symbol", safeSymbol(c));
                ParamValue v = safeCell(table, c, r);
                cell.putIfPresent("value", v == null ? null : paramValue(v));
                cell.putIfPresent("default", safeIsDefault(table, c, r));
                cells.add(cell);
            }
            rowOut.put("cells", cells);
            rowsOut.add(rowOut);
        }

        return JsonObject.of(
                "model", modelRef(ctx, model),
                "columnCount", Integer.valueOf(columns.size()),
                "rowCount", Integer.valueOf(rows.size()),
                "columns", columnsOut,
                "rows", rowsOut);
    }

    static FamilyMember requireFamilyMember(Model model) {
        if (!(model instanceof FamilyMember)) {
            throw new CommandException(
                    "Field 'target' has no family table (it is a " + typeOf(model) + ")",
                    "invalid_params");
        }
        return (FamilyMember) model;
    }

    private static List<FamilyTableColumn> columns(FamilyMember table) throws jxthrowable {
        List<FamilyTableColumn> out = new ArrayList<>();
        FamilyTableColumns cols = table.ListColumns();
        if (cols == null) {
            return out;
        }
        for (int i = 0; i < cols.getarraysize(); i++) {
            FamilyTableColumn c = cols.get(i);
            if (c != null) {
                out.add(c);
            }
        }
        return out;
    }

    private static List<FamilyTableRow> rows(FamilyMember table) throws jxthrowable {
        List<FamilyTableRow> out = new ArrayList<>();
        FamilyTableRows rws = table.ListRows();
        if (rws == null) {
            return out;
        }
        for (int i = 0; i < rws.getarraysize(); i++) {
            FamilyTableRow r = rws.get(i);
            if (r != null) {
                out.add(r);
            }
        }
        return out;
    }

    private static String safeSymbol(FamilyTableColumn c) {
        try {
            return c.GetSymbol();
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }

    private static String safeColumnType(FamilyTableColumn c) {
        try {
            return Enums.toJson(c.GetType());
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }

    private static String safeInstanceName(FamilyTableRow r) {
        try {
            return r.GetInstanceName();
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }

    private static Boolean safeLocked(FamilyTableRow r) {
        try {
            return Boolean.valueOf(r.GetIsLocked());
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }

    private static String safeVerified(FamilyTableRow r) {
        try {
            return Enums.toJson(r.GetIsVerified());
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }

    private static ParamValue safeCell(FamilyMember table, FamilyTableColumn c, FamilyTableRow r) {
        try {
            return table.GetCell(c, r);
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }

    private static Boolean safeIsDefault(FamilyMember table, FamilyTableColumn c, FamilyTableRow r) {
        try {
            return Boolean.valueOf(table.GetCellIsDefault(c, r));
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }
}
