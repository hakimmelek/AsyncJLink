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
import com.ptc.pfc.pfcUnits.UnitSystem;

/**
 * UnitSystem.GetType &mdash; pfcUnits.
 *
 * <pre>
 * UnitSystemType GetType() throws jxthrowable
 * </pre>
 */
public final class UnitSystemGetTypeCommand implements Command {

    @Override public String name() { return "UnitSystem.GetType"; }
    @Override public String jlinkPackage() { return "pfcUnits"; }
    @Override public String receiverType() { return "UnitSystem"; }
    @Override public String signature() { return "UnitSystemType GetType() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("UnitSystem.GetType \u2014 pfcUnits")
                .required("target", JsonSchema.handle("UnitSystem"),
                        "The UnitSystem to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        UnitSystem target = Marshal.in(ctx, params.get("target"),
                "UnitSystem", UnitSystem.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetType(), "UnitSystemType");
    }
}
