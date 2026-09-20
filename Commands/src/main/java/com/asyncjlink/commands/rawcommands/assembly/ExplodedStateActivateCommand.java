/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.assembly;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcAssembly.ExplodedState;

/**
 * ExplodedState.Activate &mdash; pfcAssembly.
 *
 * <pre>
 * void Activate() throws jxthrowable
 * </pre>
 */
public final class ExplodedStateActivateCommand implements Command {

    @Override public String name() { return "ExplodedState.Activate"; }
    @Override public String jlinkPackage() { return "pfcAssembly"; }
    @Override public String receiverType() { return "ExplodedState"; }
    @Override public String signature() { return "void Activate() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("ExplodedState.Activate \u2014 pfcAssembly")
                .required("target", JsonSchema.handle("ExplodedState"),
                        "The ExplodedState to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        ExplodedState target = Marshal.in(ctx, params.get("target"),
                "ExplodedState", ExplodedState.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        target.Activate();
        return Marshal.ok();
    }
}
