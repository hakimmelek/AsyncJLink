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
import com.ptc.pfc.pfcGeometry.Torus;

/**
 * Torus.GetRadius2 &mdash; pfcGeometry.
 *
 * <pre>
 * double GetRadius2() throws jxthrowable
 * </pre>
 */
public final class TorusGetRadius2Command implements Command {

    @Override public String name() { return "Torus.GetRadius2"; }
    @Override public String jlinkPackage() { return "pfcGeometry"; }
    @Override public String receiverType() { return "Torus"; }
    @Override public String signature() { return "double GetRadius2() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Torus.GetRadius2 \u2014 pfcGeometry")
                .required("target", JsonSchema.handle("Torus"),
                        "The Torus to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Torus target = Marshal.in(ctx, params.get("target"),
                "Torus", Torus.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetRadius2(), "double");
    }
}
