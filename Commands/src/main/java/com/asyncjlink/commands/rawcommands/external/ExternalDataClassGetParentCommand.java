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
import com.ptc.pfc.pfcExternal.ExternalDataClass;

/**
 * ExternalDataClass.GetParent &mdash; pfcExternal.
 *
 * <pre>
 * ExternalDataAccess GetParent() throws jxthrowable
 * </pre>
 */
public final class ExternalDataClassGetParentCommand implements Command {

    @Override public String name() { return "ExternalDataClass.GetParent"; }
    @Override public String jlinkPackage() { return "pfcExternal"; }
    @Override public String receiverType() { return "ExternalDataClass"; }
    @Override public String signature() { return "ExternalDataAccess GetParent() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("ExternalDataClass.GetParent \u2014 pfcExternal")
                .required("target", JsonSchema.handle("ExternalDataClass"),
                        "The ExternalDataClass to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        ExternalDataClass target = Marshal.in(ctx, params.get("target"),
                "ExternalDataClass", ExternalDataClass.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetParent(), "ExternalDataAccess");
    }
}
