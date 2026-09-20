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

/**
 * Unit.SetName &mdash; pfcUnits.
 *
 * <pre>
 * void SetName(String) throws jxthrowable
 * </pre>
 */
public final class UnitSetNameCommand implements Command {

    @Override public String name() { return "Unit.SetName"; }
    @Override public String jlinkPackage() { return "pfcUnits"; }
    @Override public String receiverType() { return "Unit"; }
    @Override public String signature() { return "void SetName(String) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Unit.SetName \u2014 pfcUnits")
                .required("target", JsonSchema.handle("Unit"),
                        "The Unit to act on.")
                .optional("value", JsonSchema.string(),
                        "String value.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Unit target = Marshal.in(ctx, params.get("target"),
                "Unit", Unit.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        String value = Marshal.in(ctx, params.get("value"),
                "String", String.class, "value");
        target.SetName(value);
        return Marshal.ok();
    }
}
