/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.dimension;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcDimension.DimTolerance;
import com.ptc.pfc.pfcDimension.Dimension;

/**
 * Dimension.SetTolerance &mdash; pfcDimension.
 *
 * <pre>
 * void SetTolerance(DimTolerance) throws jxthrowable
 * </pre>
 */
public final class DimensionSetToleranceCommand implements Command {

    @Override public String name() { return "Dimension.SetTolerance"; }
    @Override public String jlinkPackage() { return "pfcDimension"; }
    @Override public String receiverType() { return "Dimension"; }
    @Override public String signature() { return "void SetTolerance(DimTolerance) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Dimension.SetTolerance \u2014 pfcDimension")
                .required("target", JsonSchema.handle("Dimension"),
                        "The Dimension to act on.")
                .optional("dimTolerance", JsonSchema.dataObject("DimTolerance"),
                        "DimTolerance options object; its fields are passed to the pfc factory and setters.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Dimension target = Marshal.in(ctx, params.get("target"),
                "Dimension", Dimension.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        DimTolerance dimTolerance = Marshal.in(ctx, params.get("dimTolerance"),
                "DimTolerance", DimTolerance.class, "dimTolerance");
        target.SetTolerance(dimTolerance);
        return Marshal.ok();
    }
}
