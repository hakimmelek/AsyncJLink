/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.geometry;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcGeometry.CoonsPatch;

/**
 * CoonsPatch.GetU1Profile &mdash; pfcGeometry.
 *
 * <pre>
 * CurveDescriptor GetU1Profile() throws jxthrowable
 * </pre>
 */
public final class CoonsPatchGetU1ProfileCommand implements Command {

    @Override public String name() { return "CoonsPatch.GetU1Profile"; }
    @Override public String jlinkPackage() { return "pfcGeometry"; }
    @Override public String receiverType() { return "CoonsPatch"; }
    @Override public String signature() { return "CurveDescriptor GetU1Profile() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("CoonsPatch.GetU1Profile \u2014 pfcGeometry")
                .required("target", JsonSchema.handle("CoonsPatch"),
                        "The CoonsPatch to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        CoonsPatch target = Marshal.in(ctx, params.get("target"),
                "CoonsPatch", CoonsPatch.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetU1Profile(), "CurveDescriptor");
    }
}
