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
import com.ptc.pfc.pfcGeometry.CompositeCurve;

/**
 * CompositeCurve.ListElements &mdash; pfcGeometry.
 *
 * <pre>
 * Curves ListElements() throws jxthrowable
 * </pre>
 */
public final class CompositeCurveListElementsCommand implements Command {

    @Override public String name() { return "CompositeCurve.ListElements"; }
    @Override public String jlinkPackage() { return "pfcGeometry"; }
    @Override public String receiverType() { return "CompositeCurve"; }
    @Override public String signature() { return "Curves ListElements() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("CompositeCurve.ListElements \u2014 pfcGeometry")
                .required("target", JsonSchema.handle("CompositeCurve"),
                        "The CompositeCurve to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        CompositeCurve target = Marshal.in(ctx, params.get("target"),
                "CompositeCurve", CompositeCurve.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.ListElements(), "Curves");
    }
}
