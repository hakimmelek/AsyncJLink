/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.simprep;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcSimpRep.SimpRep;
import com.ptc.pfc.pfcSimpRep.SimpRepInstructions;

/**
 * SimpRep.SetInstructions &mdash; pfcSimpRep.
 *
 * <pre>
 * void SetInstructions(SimpRepInstructions) throws jxthrowable
 * </pre>
 */
public final class SimpRepSetInstructionsCommand implements Command {

    @Override public String name() { return "SimpRep.SetInstructions"; }
    @Override public String jlinkPackage() { return "pfcSimpRep"; }
    @Override public String receiverType() { return "SimpRep"; }
    @Override public String signature() { return "void SetInstructions(SimpRepInstructions) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("SimpRep.SetInstructions \u2014 pfcSimpRep")
                .required("target", JsonSchema.handle("SimpRep"),
                        "The SimpRep to act on.")
                .optional("simpRepInstructions", JsonSchema.dataObject("SimpRepInstructions"),
                        "SimpRepInstructions options object; its fields are passed to the pfc factory and setters.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        SimpRep target = Marshal.in(ctx, params.get("target"),
                "SimpRep", SimpRep.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        SimpRepInstructions simpRepInstructions = Marshal.in(ctx, params.get("simpRepInstructions"),
                "SimpRepInstructions", SimpRepInstructions.class, "simpRepInstructions");
        target.SetInstructions(simpRepInstructions);
        return Marshal.ok();
    }
}
