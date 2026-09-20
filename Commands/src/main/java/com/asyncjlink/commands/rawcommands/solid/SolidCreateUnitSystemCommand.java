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
import com.ptc.pfc.pfcUnits.UnitSystemType;
import com.ptc.pfc.pfcUnits.Units;

/**
 * Solid.CreateUnitSystem &mdash; pfcSolid.
 *
 * <pre>
 * UnitSystem CreateUnitSystem(String, UnitSystemType, Units) throws jxthrowable
 * </pre>
 */
public final class SolidCreateUnitSystemCommand implements Command {

    @Override public String name() { return "Solid.CreateUnitSystem"; }
    @Override public String jlinkPackage() { return "pfcSolid"; }
    @Override public String receiverType() { return "Solid"; }
    @Override public String signature() { return "UnitSystem CreateUnitSystem(String, UnitSystemType, Units) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Solid.CreateUnitSystem \u2014 pfcSolid")
                .required("target", JsonSchema.handle("Solid"),
                        "The Solid to act on.")
                .optional("value", JsonSchema.string(),
                        "String value.")
                .optional("unitSystemType", JsonSchema.enumOf("UnitSystemType", "UNIT_SYSTEM_MASS_LENGTH_TIME", "UNIT_SYSTEM_FORCE_LENGTH_TIME"),
                        "One of the UnitSystemType constants.")
                .optional("units", JsonSchema.sequence("Units", JsonSchema.handle("Unit")),
                        "Array of Unit.")
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
        UnitSystemType unitSystemType = Marshal.in(ctx, params.get("unitSystemType"),
                "UnitSystemType", UnitSystemType.class, "unitSystemType");
        Units units = Marshal.in(ctx, params.get("units"),
                "Units", Units.class, "units");
        return Marshal.result(ctx, target.CreateUnitSystem(value, unitSystemType, units), "UnitSystem");
    }
}
