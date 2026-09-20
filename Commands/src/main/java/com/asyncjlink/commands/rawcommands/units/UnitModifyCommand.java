/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.units;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcUnits.Unit;
import com.ptc.pfc.pfcUnits.UnitConversionFactor;

/**
 * Unit.Modify &mdash; pfcUnits.
 *
 * <pre>
 * void Modify(UnitConversionFactor, Unit) throws jxthrowable
 * </pre>
 */
public final class UnitModifyCommand implements Command {

    @Override public String name() { return "Unit.Modify"; }
    @Override public String jlinkPackage() { return "pfcUnits"; }
    @Override public String receiverType() { return "Unit"; }
    @Override public String signature() { return "void Modify(UnitConversionFactor, Unit) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Unit.Modify \u2014 pfcUnits")
                .required("target", JsonSchema.handle("Unit"),
                        "The Unit to act on.")
                .optional("unitConversionFactor", JsonSchema.dataObject("UnitConversionFactor"),
                        "UnitConversionFactor options object; its fields are passed to the pfc factory and setters.")
                .optional("unit", JsonSchema.handle("Unit"),
                        "Handle to a Unit, as returned by an earlier command.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Unit target = Marshal.in(ctx, params.get("target"),
                "Unit", Unit.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        UnitConversionFactor unitConversionFactor = Marshal.in(ctx, params.get("unitConversionFactor"),
                "UnitConversionFactor", UnitConversionFactor.class, "unitConversionFactor");
        Unit unit = Marshal.in(ctx, params.get("unit"),
                "Unit", Unit.class, "unit");
        target.Modify(unitConversionFactor, unit);
        return Marshal.ok();
    }
}
