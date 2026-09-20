package com.asyncjlink.commands.compositecommands.family;

import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
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
import com.ptc.pfc.pfcModelItem.ParamValues;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Model.SetFamilyTableCells — bulk, type-checked family table cell writes.
 *
 * <p>The same discipline as {@code Model.SetParameters}: a value that does not match its column's
 * declared type is reported as rejected, not silently coerced.
 */
public final class ModelSetFamilyTableCellsCommand extends Composite {

    @Override public String name() { return "Model.SetFamilyTableCells"; }
    @Override public String receiverType() { return "Model"; }
    @Override public String signature() { return "SetFamilyTableCells(target, instance, cells[, create, dryRun]) — composite"; }

    @Override
    public String description() {
        return "Set several family table cells on one instance row at once, reporting before and "
                + "after for each. Supports dryRun and creating the row if it does not exist yet.";
    }

    @Override
    public JsonSchema paramSchema() {
        return targeted(name(), "Model", "The model whose family table to write.")
                .required("instance", JsonSchema.string(), "Instance (row) name.")
                .required("cells", JsonSchema.dataObject("Cells"),
                        "Column symbol/value pairs to write.")
                .optional("create", JsonSchema.bool(),
                        "Create the row if it does not exist yet. Default false.")
                .optional("dryRun", JsonSchema.bool(),
                        "Report what would change without writing. Default false.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Model model = requireModel(ctx, params);
        FamilyMember table = ModelGetFamilyTableCommand.requireFamilyMember(model);

        String instanceName = params.getString("instance", null);
        if (instanceName == null || instanceName.isEmpty()) {
            throw new CommandException("Field 'instance' is required", "invalid_params");
        }
        JsonObject wanted = params.getObject("cells");
        if (wanted == null || wanted.isEmpty()) {
            throw new CommandException(
                    "Field 'cells' must be an object of column-symbol/value pairs", "invalid_params");
        }
        boolean create = params.getBoolean("create", false);
        boolean dryRun = dryRun(params);

        Map<String, FamilyTableColumn> bySymbol = indexColumns(table);
        FamilyTableRow row = findRow(table, instanceName);

        JsonObject out = JsonObject.of(
                "model", modelRef(ctx, model),
                "instance", instanceName,
                "dryRun", Boolean.valueOf(dryRun));

        if (row == null) {
            if (!create) {
                throw new CommandException(
                        "No row named '" + instanceName + "' in this family table; "
                                + "set create=true to add it", "invalid_params");
            }
            out.put("rowCreated", Boolean.valueOf(!dryRun));
            if (dryRun) {
                out.put("changed", Integer.valueOf(0));
                out.put("rejected", Integer.valueOf(0));
                out.put("cells", new JsonArray());
                return out;
            }
            try {
                // An explicit empty sequence rather than null: Session.CreateDrawing's
                // DrawingCreateOptions crash showed that null for a data-object argument can take
                // the whole async connection down instead of raising an ordinary error, so this
                // codebase now avoids handing native calls null where a real empty value exists.
                row = table.AddRow(instanceName, ParamValues.create());
            } catch (jxthrowable e) {
                throw new CommandException(
                        "Could not create row '" + instanceName + "': " + rootMessage(e),
                        "creo_error", e);
            }
        }

        JsonArray results = new JsonArray();
        int changed = 0;
        int rejected = 0;

        for (String symbol : wanted.keys()) {
            JsonObject entry = JsonObject.of("symbol", symbol);
            Object requested = wanted.get(symbol);
            entry.put("requested", requested);

            FamilyTableColumn column = bySymbol.get(symbol.toUpperCase(java.util.Locale.ROOT));
            if (column == null) {
                entry.put("action", "rejected");
                entry.put("reason", "no column with this symbol; Model.GetFamilyTable lists them");
                rejected++;
                results.add(entry);
                continue;
            }

            ParamValue before;
            try {
                before = table.GetCell(column, row);
                entry.putIfPresent("before", before == null ? null : paramValue(before));
            } catch (jxthrowable | RuntimeException e) {
                before = null;
            }

            ParamValue next;
            try {
                next = toParamValue(requested, before, symbol);
            } catch (CommandException e) {
                entry.put("action", "rejected");
                entry.put("reason", e.getMessage());
                rejected++;
                results.add(entry);
                continue;
            }

            if (dryRun) {
                entry.put("action", "wouldSet");
                entry.putIfPresent("after", paramValue(next));
                changed++;
                results.add(entry);
                continue;
            }

            try {
                table.SetCell(column, row, next);
                entry.put("action", "set");
                entry.putIfPresent("after", paramValue(table.GetCell(column, row)));
                changed++;
            } catch (jxthrowable | RuntimeException e) {
                entry.put("action", "failed");
                entry.put("error", rootMessage(e));
                rejected++;
            }
            results.add(entry);
        }

        out.put("changed", Integer.valueOf(changed));
        out.put("rejected", Integer.valueOf(rejected));
        out.put("cells", results);
        return out;
    }

    private static Map<String, FamilyTableColumn> indexColumns(FamilyMember table) throws jxthrowable {
        Map<String, FamilyTableColumn> out = new LinkedHashMap<>();
        FamilyTableColumns cols = table.ListColumns();
        if (cols == null) {
            return out;
        }
        for (int i = 0; i < cols.getarraysize(); i++) {
            FamilyTableColumn c = cols.get(i);
            if (c == null) {
                continue;
            }
            try {
                String symbol = c.GetSymbol();
                if (symbol != null) {
                    out.put(symbol.toUpperCase(java.util.Locale.ROOT), c);
                }
            } catch (jxthrowable | RuntimeException ignored) {
                // A column that will not report its own symbol cannot be addressed by one.
            }
        }
        return out;
    }

    private static FamilyTableRow findRow(FamilyMember table, String instanceName) throws jxthrowable {
        FamilyTableRows rows = table.ListRows();
        if (rows == null) {
            return null;
        }
        for (int i = 0; i < rows.getarraysize(); i++) {
            FamilyTableRow r = rows.get(i);
            if (r == null) {
                continue;
            }
            try {
                if (instanceName.equalsIgnoreCase(r.GetInstanceName())) {
                    return r;
                }
            } catch (jxthrowable | RuntimeException ignored) {
                // Skip a row that will not report its own name.
            }
        }
        return null;
    }
}
