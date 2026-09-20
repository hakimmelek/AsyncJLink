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
import com.ptc.pfc.pfcTable.RotationDegree;
import com.ptc.pfc.pfcTable.Table;

/**
 * Table.RotateClockwise &mdash; pfcTable.
 *
 * <pre>
 * void RotateClockwise(RotationDegree, Boolean) throws jxthrowable
 * </pre>
 */
public final class TableRotateClockwiseCommand implements Command {

    @Override public String name() { return "Table.RotateClockwise"; }
    @Override public String jlinkPackage() { return "pfcTable"; }
    @Override public String receiverType() { return "Table"; }
    @Override public String signature() { return "void RotateClockwise(RotationDegree, Boolean) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Table.RotateClockwise \u2014 pfcTable")
                .required("target", JsonSchema.handle("Table"),
                        "The Table to act on.")
                .optional("rotationDegree", JsonSchema.enumOf("RotationDegree", "ROTATE_90", "ROTATE_180", "ROTATE_270"),
                        "One of the RotationDegree constants.")
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
        RotationDegree rotationDegree = Marshal.in(ctx, params.get("rotationDegree"),
                "RotationDegree", RotationDegree.class, "rotationDegree");
        Boolean flag = Marshal.in(ctx, params.get("flag"),
                "Boolean", Boolean.class, "flag");
        target.RotateClockwise(rotationDegree, flag);
        return Marshal.ok();
    }
}
