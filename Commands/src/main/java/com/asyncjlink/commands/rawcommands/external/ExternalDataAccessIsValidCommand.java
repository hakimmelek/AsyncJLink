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
import com.ptc.pfc.pfcExternal.ExternalDataAccess;

/**
 * ExternalDataAccess.IsValid &mdash; pfcExternal.
 *
 * <pre>
 * boolean IsValid() throws jxthrowable
 * </pre>
 */
public final class ExternalDataAccessIsValidCommand implements Command {

    @Override public String name() { return "ExternalDataAccess.IsValid"; }
    @Override public String jlinkPackage() { return "pfcExternal"; }
    @Override public String receiverType() { return "ExternalDataAccess"; }
    @Override public String signature() { return "boolean IsValid() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("ExternalDataAccess.IsValid \u2014 pfcExternal")
                .required("target", JsonSchema.handle("ExternalDataAccess"),
                        "The ExternalDataAccess to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        ExternalDataAccess target = Marshal.in(ctx, params.get("target"),
                "ExternalDataAccess", ExternalDataAccess.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.IsValid(), "boolean");
    }
}
