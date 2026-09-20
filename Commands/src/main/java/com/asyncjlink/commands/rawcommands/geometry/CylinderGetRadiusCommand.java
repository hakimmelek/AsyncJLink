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
import com.ptc.pfc.pfcGeometry.Cylinder;

/**
 * Cylinder.GetRadius &mdash; pfcGeometry.
 *
 * <pre>
 * double GetRadius() throws jxthrowable
 * </pre>
 */
public final class CylinderGetRadiusCommand implements Command {

    @Override public String name() { return "Cylinder.GetRadius"; }
    @Override public String jlinkPackage() { return "pfcGeometry"; }
    @Override public String receiverType() { return "Cylinder"; }
    @Override public String signature() { return "double GetRadius() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Cylinder.GetRadius \u2014 pfcGeometry")
                .required("target", JsonSchema.handle("Cylinder"),
                        "The Cylinder to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Cylinder target = Marshal.in(ctx, params.get("target"),
                "Cylinder", Cylinder.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetRadius(), "double");
    }
}
