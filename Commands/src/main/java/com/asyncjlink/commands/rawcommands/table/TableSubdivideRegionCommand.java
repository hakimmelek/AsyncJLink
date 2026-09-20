/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.table;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcTable.Table;
import com.ptc.pfc.pfcTable.TableCell;

/**
 * Table.SubdivideRegion &mdash; pfcTable.
 *
 * <pre>
 * void SubdivideRegion(TableCell, TableCell, Boolean) throws jxthrowable
 * </pre>
 */
public final class TableSubdivideRegionCommand implements Command {

    @Override public String name() { return "Table.SubdivideRegion"; }
    @Override public String jlinkPackage() { return "pfcTable"; }
    @Override public String receiverType() { return "Table"; }
    @Override public String signature() { return "void SubdivideRegion(TableCell, TableCell, Boolean) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Table.SubdivideRegion \u2014 pfcTable")
                .required("target", JsonSchema.handle("Table"),
                        "The Table to act on.")
                .optional("tableCell1", JsonSchema.dataObject("TableCell"),
                        "TableCell options object; its fields are passed to the pfc factory and setters.")
                .optional("tableCell2", JsonSchema.dataObject("TableCell"),
                        "TableCell options object; its fields are passed to the pfc factory and setters.")
                .optional("flag", JsonSchema.bool(),
                        "Boolean value.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Table target = Marshal.in(ctx, params.get("target"),
                "Table", Table.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        TableCell tableCell1 = Marshal.in(ctx, params.get("tableCell1"),
                "TableCell", TableCell.class, "tableCell1");
        TableCell tableCell2 = Marshal.in(ctx, params.get("tableCell2"),
                "TableCell", TableCell.class, "tableCell2");
        Boolean flag = Marshal.in(ctx, params.get("flag"),
                "Boolean", Boolean.class, "flag");
        target.SubdivideRegion(tableCell1, tableCell2, flag);
        return Marshal.ok();
    }
}
