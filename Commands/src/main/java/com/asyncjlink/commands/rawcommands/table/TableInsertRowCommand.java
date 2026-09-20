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

/**
 * Table.InsertRow &mdash; pfcTable.
 *
 * <pre>
 * void InsertRow(double, Integer, Boolean) throws jxthrowable
 * </pre>
 */
public final class TableInsertRowCommand implements Command {

    @Override public String name() { return "Table.InsertRow"; }
    @Override public String jlinkPackage() { return "pfcTable"; }
    @Override public String receiverType() { return "Table"; }
    @Override public String signature() { return "void InsertRow(double, Integer, Boolean) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Table.InsertRow \u2014 pfcTable")
                .required("target", JsonSchema.handle("Table"),
                        "The Table to act on.")
                .required("value1", JsonSchema.number(),
                        "double value.")
                .optional("value2", JsonSchema.integer(),
                        "Integer value.")
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
        double value1 = Marshal.in(ctx, params.get("value1"),
                "double", double.class, "value1");
        Integer value2 = Marshal.in(ctx, params.get("value2"),
                "Integer", Integer.class, "value2");
        Boolean flag = Marshal.in(ctx, params.get("flag"),
                "Boolean", Boolean.class, "flag");
        target.InsertRow(value1, value2, flag);
        return Marshal.ok();
    }
}
