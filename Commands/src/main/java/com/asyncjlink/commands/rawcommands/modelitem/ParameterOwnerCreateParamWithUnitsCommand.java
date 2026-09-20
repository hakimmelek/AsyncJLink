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
import com.ptc.pfc.pfcModelItem.ParameterOwner;
import com.ptc.pfc.pfcUnits.Unit;

/**
 * ParameterOwner.CreateParamWithUnits &mdash; pfcModelItem.
 *
 * <pre>
 * Parameter CreateParamWithUnits(String, ParamValue, Unit) throws jxthrowable
 * </pre>
 */
public final class ParameterOwnerCreateParamWithUnitsCommand implements Command {

    @Override public String name() { return "ParameterOwner.CreateParamWithUnits"; }
    @Override public String jlinkPackage() { return "pfcModelItem"; }
    @Override public String receiverType() { return "ParameterOwner"; }
    @Override public String signature() { return "Parameter CreateParamWithUnits(String, ParamValue, Unit) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("ParameterOwner.CreateParamWithUnits \u2014 pfcModelItem")
                .required("target", JsonSchema.handle("ParameterOwner"),
                        "The ParameterOwner to act on.")
                .optional("value", JsonSchema.string(),
                        "String value.")
                .optional("paramValue", JsonSchema.sequence("ParamValue", JsonSchema.handle("Object")),
                        "Array of Object.")
                .optional("unit", JsonSchema.handle("Unit"),
                        "Handle to a Unit, as returned by an earlier command.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        ParameterOwner target = Marshal.in(ctx, params.get("target"),
                "ParameterOwner", ParameterOwner.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        String value = Marshal.in(ctx, params.get("value"),
                "String", String.class, "value");
        ParamValue paramValue = Marshal.in(ctx, params.get("paramValue"),
                "ParamValue", ParamValue.class, "paramValue");
        Unit unit = Marshal.in(ctx, params.get("unit"),
                "Unit", Unit.class, "unit");
        return Marshal.result(ctx, target.CreateParamWithUnits(value, paramValue, unit), "Parameter");
    }
}
