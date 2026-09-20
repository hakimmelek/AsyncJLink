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
import com.ptc.pfc.pfcUnits.UnitConversionOptions;
import com.ptc.pfc.pfcUnits.UnitSystem;

/**
 * Solid.SetPrincipalUnits &mdash; pfcSolid.
 *
 * <pre>
 * void SetPrincipalUnits(UnitSystem, UnitConversionOptions) throws jxthrowable
 * </pre>
 */
public final class SolidSetPrincipalUnitsCommand implements Command {

    @Override public String name() { return "Solid.SetPrincipalUnits"; }
    @Override public String jlinkPackage() { return "pfcSolid"; }
    @Override public String receiverType() { return "Solid"; }
    @Override public String signature() { return "void SetPrincipalUnits(UnitSystem, UnitConversionOptions) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Solid.SetPrincipalUnits \u2014 pfcSolid")
                .required("target", JsonSchema.handle("Solid"),
                        "The Solid to act on.")
                .optional("unitSystem", JsonSchema.handle("UnitSystem"),
                        "Handle to a UnitSystem, as returned by an earlier command.")
                .optional("unitConversionOptions", JsonSchema.dataObject("UnitConversionOptions"),
                        "UnitConversionOptions options object; its fields are passed to the pfc factory and setters.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Solid target = Marshal.in(ctx, params.get("target"),
                "Solid", Solid.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        UnitSystem unitSystem = Marshal.in(ctx, params.get("unitSystem"),
                "UnitSystem", UnitSystem.class, "unitSystem");
        UnitConversionOptions unitConversionOptions = Marshal.in(ctx, params.get("unitConversionOptions"),
                "UnitConversionOptions", UnitConversionOptions.class, "unitConversionOptions");
        target.SetPrincipalUnits(unitSystem, unitConversionOptions);
        return Marshal.ok();
    }
}
