/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.modelitem;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcModelItem.ParamValue;
import com.ptc.pfc.pfcModelItem.Parameter;
import com.ptc.pfc.pfcUnits.Unit;

/**
 * Parameter.SetScaledValue &mdash; pfcModelItem.
 *
 * <pre>
 * void SetScaledValue(ParamValue, Unit) throws jxthrowable
 * </pre>
 */
public final class ParameterSetScaledValueCommand implements Command {

    @Override public String name() { return "Parameter.SetScaledValue"; }
    @Override public String jlinkPackage() { return "pfcModelItem"; }
    @Override public String receiverType() { return "Parameter"; }
    @Override public String signature() { return "void SetScaledValue(ParamValue, Unit) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Parameter.SetScaledValue \u2014 pfcModelItem")
                .required("target", JsonSchema.handle("Parameter"),
                        "The Parameter to act on.")
                .optional("paramValue", JsonSchema.sequence("ParamValue", JsonSchema.handle("Object")),
                        "Array of Object.")
                .optional("unit", JsonSchema.handle("Unit"),
                        "Handle to a Unit, as returned by an earlier command.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Parameter target = Marshal.in(ctx, params.get("target"),
                "Parameter", Parameter.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        ParamValue paramValue = Marshal.in(ctx, params.get("paramValue"),
                "ParamValue", ParamValue.class, "paramValue");
        Unit unit = Marshal.in(ctx, params.get("unit"),
                "Unit", Unit.class, "unit");
        target.SetScaledValue(paramValue, unit);
        return Marshal.ok();
    }
}
