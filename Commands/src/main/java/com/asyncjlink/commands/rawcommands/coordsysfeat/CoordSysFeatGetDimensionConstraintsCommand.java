/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.coordsysfeat;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcCoordSysFeat.CoordSysFeat;

/**
 * CoordSysFeat.GetDimensionConstraints &mdash; pfcCoordSysFeat.
 *
 * <pre>
 * DatumCsysDimensionConstraints GetDimensionConstraints() throws jxthrowable
 * </pre>
 */
public final class CoordSysFeatGetDimensionConstraintsCommand implements Command {

    @Override public String name() { return "CoordSysFeat.GetDimensionConstraints"; }
    @Override public String jlinkPackage() { return "pfcCoordSysFeat"; }
    @Override public String receiverType() { return "CoordSysFeat"; }
    @Override public String signature() { return "DatumCsysDimensionConstraints GetDimensionConstraints() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("CoordSysFeat.GetDimensionConstraints \u2014 pfcCoordSysFeat")
                .required("target", JsonSchema.handle("CoordSysFeat"),
                        "The CoordSysFeat to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        CoordSysFeat target = Marshal.in(ctx, params.get("target"),
                "CoordSysFeat", CoordSysFeat.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetDimensionConstraints(), "DatumCsysDimensionConstraints");
    }
}
