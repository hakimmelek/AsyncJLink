/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.solid;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcSolid.Solid;

/**
 * Solid.GetCrossSection &mdash; pfcSolid.
 *
 * <pre>
 * XSection GetCrossSection(String) throws jxthrowable
 * </pre>
 */
public final class SolidGetCrossSectionCommand implements Command {

    @Override public String name() { return "Solid.GetCrossSection"; }
    @Override public String jlinkPackage() { return "pfcSolid"; }
    @Override public String receiverType() { return "Solid"; }
    @Override public String signature() { return "XSection GetCrossSection(String) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Solid.GetCrossSection \u2014 pfcSolid")
                .required("target", JsonSchema.handle("Solid"),
                        "The Solid to act on.")
                .optional("value", JsonSchema.string(),
                        "String value.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Solid target = Marshal.in(ctx, params.get("target"),
                "Solid", Solid.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        String value = Marshal.in(ctx, params.get("value"),
                "String", String.class, "value");
        return Marshal.result(ctx, target.GetCrossSection(value), "XSection");
    }
}
