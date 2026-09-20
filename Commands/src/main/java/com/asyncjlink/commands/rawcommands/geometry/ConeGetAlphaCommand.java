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
import com.ptc.pfc.pfcGeometry.Cone;

/**
 * Cone.GetAlpha &mdash; pfcGeometry.
 *
 * <pre>
 * double GetAlpha() throws jxthrowable
 * </pre>
 */
public final class ConeGetAlphaCommand implements Command {

    @Override public String name() { return "Cone.GetAlpha"; }
    @Override public String jlinkPackage() { return "pfcGeometry"; }
    @Override public String receiverType() { return "Cone"; }
    @Override public String signature() { return "double GetAlpha() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Cone.GetAlpha \u2014 pfcGeometry")
                .required("target", JsonSchema.handle("Cone"),
                        "The Cone to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Cone target = Marshal.in(ctx, params.get("target"),
                "Cone", Cone.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetAlpha(), "double");
    }
}
