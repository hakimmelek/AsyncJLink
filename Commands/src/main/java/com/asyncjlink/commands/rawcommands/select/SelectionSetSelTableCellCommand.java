/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.select;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcSelect.Selection;
import com.ptc.pfc.pfcTable.TableCell;

/**
 * Selection.SetSelTableCell &mdash; pfcSelect.
 *
 * <pre>
 * void SetSelTableCell(TableCell) throws jxthrowable
 * </pre>
 */
public final class SelectionSetSelTableCellCommand implements Command {

    @Override public String name() { return "Selection.SetSelTableCell"; }
    @Override public String jlinkPackage() { return "pfcSelect"; }
    @Override public String receiverType() { return "Selection"; }
    @Override public String signature() { return "void SetSelTableCell(TableCell) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Selection.SetSelTableCell \u2014 pfcSelect")
                .required("target", JsonSchema.handle("Selection"),
                        "The Selection to act on.")
                .optional("tableCell", JsonSchema.dataObject("TableCell"),
                        "TableCell options object; its fields are passed to the pfc factory and setters.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Selection target = Marshal.in(ctx, params.get("target"),
                "Selection", Selection.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        TableCell tableCell = Marshal.in(ctx, params.get("tableCell"),
                "TableCell", TableCell.class, "tableCell");
        target.SetSelTableCell(tableCell);
        return Marshal.ok();
    }
}
