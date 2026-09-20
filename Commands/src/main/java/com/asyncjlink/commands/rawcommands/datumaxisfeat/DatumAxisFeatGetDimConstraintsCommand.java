/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.datumaxisfeat;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcDatumAxisFeat.DatumAxisFeat;

/**
 * DatumAxisFeat.GetDimConstraints &mdash; pfcDatumAxisFeat.
 *
 * <pre>
 * DatumAxisDimensionConstraints GetDimConstraints() throws jxthrowable
 * </pre>
 */
public final class DatumAxisFeatGetDimConstraintsCommand implements Command {

    @Override public String name() { return "DatumAxisFeat.GetDimConstraints"; }
    @Override public String jlinkPackage() { return "pfcDatumAxisFeat"; }
    @Override public String receiverType() { return "DatumAxisFeat"; }
    @Override public String signature() { return "DatumAxisDimensionConstraints GetDimConstraints() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("DatumAxisFeat.GetDimConstraints \u2014 pfcDatumAxisFeat")
                .required("target", JsonSchema.handle("DatumAxisFeat"),
                        "The DatumAxisFeat to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        DatumAxisFeat target = Marshal.in(ctx, params.get("target"),
                "DatumAxisFeat", DatumAxisFeat.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetDimConstraints(), "DatumAxisDimensionConstraints");
    }
}
