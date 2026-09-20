/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.protoolkit;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcProToolkit.Dll;

/**
 * Dll.IsActive &mdash; pfcProToolkit.
 *
 * <pre>
 * boolean IsActive() throws jxthrowable
 * </pre>
 */
public final class DllIsActiveCommand implements Command {

    @Override public String name() { return "Dll.IsActive"; }
    @Override public String jlinkPackage() { return "pfcProToolkit"; }
    @Override public String receiverType() { return "Dll"; }
    @Override public String signature() { return "boolean IsActive() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Dll.IsActive \u2014 pfcProToolkit")
                .required("target", JsonSchema.handle("Dll"),
                        "The Dll to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Dll target = Marshal.in(ctx, params.get("target"),
                "Dll", Dll.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.IsActive(), "boolean");
    }
}
