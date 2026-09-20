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
 * Solid.GetUnit &mdash; pfcSolid.
 *
 * <pre>
 * Unit GetUnit(String, Boolean) throws jxthrowable
 * </pre>
 */
public final class SolidGetUnitCommand implements Command {

    @Override public String name() { return "Solid.GetUnit"; }
    @Override public String jlinkPackage() { return "pfcSolid"; }
    @Override public String receiverType() { return "Solid"; }
    @Override public String signature() { return "Unit GetUnit(String, Boolean) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Solid.GetUnit \u2014 pfcSolid")
                .required("target", JsonSchema.handle("Solid"),
                        "The Solid to act on.")
                .optional("value", JsonSchema.string(),
                        "String value.")
                .optional("flag", JsonSchema.bool(),
                        "Boolean value.")
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
        Boolean flag = Marshal.in(ctx, params.get("flag"),
                "Boolean", Boolean.class, "flag");
        return Marshal.result(ctx, target.GetUnit(value, flag), "Unit");
    }
}
