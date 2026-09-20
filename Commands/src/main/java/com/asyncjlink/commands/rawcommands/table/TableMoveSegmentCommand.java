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
import com.ptc.pfc.pfcBase.Point3D;
import com.ptc.pfc.pfcTable.Table;

/**
 * Table.MoveSegment &mdash; pfcTable.
 *
 * <pre>
 * void MoveSegment(int, Point3D, Boolean) throws jxthrowable
 * </pre>
 */
public final class TableMoveSegmentCommand implements Command {

    @Override public String name() { return "Table.MoveSegment"; }
    @Override public String jlinkPackage() { return "pfcTable"; }
    @Override public String receiverType() { return "Table"; }
    @Override public String signature() { return "void MoveSegment(int, Point3D, Boolean) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Table.MoveSegment \u2014 pfcTable")
                .required("target", JsonSchema.handle("Table"),
                        "The Table to act on.")
                .required("value", JsonSchema.integer(),
                        "int value.")
                .optional("point3D", JsonSchema.sequence("Point3D", JsonSchema.number()),
                        "Array of double.")
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
        int value = Marshal.in(ctx, params.get("value"),
                "int", int.class, "value");
        Point3D point3D = Marshal.in(ctx, params.get("point3D"),
                "Point3D", Point3D.class, "point3D");
        Boolean flag = Marshal.in(ctx, params.get("flag"),
                "Boolean", Boolean.class, "flag");
        target.MoveSegment(value, point3D, flag);
        return Marshal.ok();
    }
}
