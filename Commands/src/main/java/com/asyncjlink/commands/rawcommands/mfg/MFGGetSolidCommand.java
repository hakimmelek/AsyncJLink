/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.mfg;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcMFG.MFG;

/**
 * MFG.GetSolid &mdash; pfcMFG.
 *
 * <pre>
 * Solid GetSolid() throws jxthrowable
 * </pre>
 */
public final class MFGGetSolidCommand implements Command {

    @Override public String name() { return "MFG.GetSolid"; }
    @Override public String jlinkPackage() { return "pfcMFG"; }
    @Override public String receiverType() { return "MFG"; }
    @Override public String signature() { return "Solid GetSolid() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("MFG.GetSolid \u2014 pfcMFG")
                .required("target", JsonSchema.handle("MFG"),
                        "The MFG to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        MFG target = Marshal.in(ctx, params.get("target"),
                "MFG", MFG.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetSolid(), "Solid");
    }
}
