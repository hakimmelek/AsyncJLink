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
 * CoordSysFeat.GetOrientByMethod &mdash; pfcCoordSysFeat.
 *
 * <pre>
 * DatumCsysOrientByMethod GetOrientByMethod() throws jxthrowable
 * </pre>
 */
public final class CoordSysFeatGetOrientByMethodCommand implements Command {

    @Override public String name() { return "CoordSysFeat.GetOrientByMethod"; }
    @Override public String jlinkPackage() { return "pfcCoordSysFeat"; }
    @Override public String receiverType() { return "CoordSysFeat"; }
    @Override public String signature() { return "DatumCsysOrientByMethod GetOrientByMethod() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("CoordSysFeat.GetOrientByMethod \u2014 pfcCoordSysFeat")
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
        return Marshal.result(ctx, target.GetOrientByMethod(), "DatumCsysOrientByMethod");
    }
}
