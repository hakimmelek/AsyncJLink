/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.external;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcExternal.ExternalData;
import com.ptc.pfc.pfcExternal.ExternalDataSlot;

/**
 * ExternalDataSlot.SetValue &mdash; pfcExternal.
 *
 * <pre>
 * void SetValue(ExternalData) throws jxthrowable
 * </pre>
 */
public final class ExternalDataSlotSetValueCommand implements Command {

    @Override public String name() { return "ExternalDataSlot.SetValue"; }
    @Override public String jlinkPackage() { return "pfcExternal"; }
    @Override public String receiverType() { return "ExternalDataSlot"; }
    @Override public String signature() { return "void SetValue(ExternalData) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("ExternalDataSlot.SetValue \u2014 pfcExternal")
                .required("target", JsonSchema.handle("ExternalDataSlot"),
                        "The ExternalDataSlot to act on.")
                .optional("externalData", JsonSchema.sequence("ExternalData", JsonSchema.handle("Object")),
                        "Array of Object.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        ExternalDataSlot target = Marshal.in(ctx, params.get("target"),
                "ExternalDataSlot", ExternalDataSlot.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        ExternalData externalData = Marshal.in(ctx, params.get("externalData"),
                "ExternalData", ExternalData.class, "externalData");
        target.SetValue(externalData);
        return Marshal.ok();
    }
}
