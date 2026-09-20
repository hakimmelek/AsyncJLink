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
import com.ptc.pfc.pfcBase.UnitType;
import com.ptc.pfc.pfcSolid.Solid;

/**
 * Solid.ListUnits &mdash; pfcSolid.
 *
 * <pre>
 * Units ListUnits(UnitType) throws jxthrowable
 * </pre>
 */
public final class SolidListUnitsCommand implements Command {

    @Override public String name() { return "Solid.ListUnits"; }
    @Override public String jlinkPackage() { return "pfcSolid"; }
    @Override public String receiverType() { return "Solid"; }
    @Override public String signature() { return "Units ListUnits(UnitType) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Solid.ListUnits \u2014 pfcSolid")
                .required("target", JsonSchema.handle("Solid"),
                        "The Solid to act on.")
                .optional("unitType", JsonSchema.enumOf("UnitType", "UNIT_LENGTH", "UNIT_MASS", "UNIT_FORCE", "UNIT_TIME", "UNIT_TEMPERATURE", "UNIT_ANGLE"),
                        "One of the UnitType constants.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Solid target = Marshal.in(ctx, params.get("target"),
                "Solid", Solid.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        UnitType unitType = Marshal.in(ctx, params.get("unitType"),
                "UnitType", UnitType.class, "unitType");
        return Marshal.result(ctx, target.ListUnits(unitType), "Units");
    }
}
