/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.solid;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcSimpRep.CreateNewSimpRepInstructions;
import com.ptc.pfc.pfcSolid.Solid;

/**
 * Solid.CreateSimpRep &mdash; pfcSolid.
 *
 * <pre>
 * SimpRep CreateSimpRep(CreateNewSimpRepInstructions) throws jxthrowable
 * </pre>
 */
public final class SolidCreateSimpRepCommand implements Command {

    @Override public String name() { return "Solid.CreateSimpRep"; }
    @Override public String jlinkPackage() { return "pfcSolid"; }
    @Override public String receiverType() { return "Solid"; }
    @Override public String signature() { return "SimpRep CreateSimpRep(CreateNewSimpRepInstructions) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Solid.CreateSimpRep \u2014 pfcSolid")
                .required("target", JsonSchema.handle("Solid"),
                        "The Solid to act on.")
                .optional("createNewSimpRepInstructions", JsonSchema.dataObject("CreateNewSimpRepInstructions"),
                        "CreateNewSimpRepInstructions options object; its fields are passed to the pfc factory and setters.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Solid target = Marshal.in(ctx, params.get("target"),
                "Solid", Solid.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        CreateNewSimpRepInstructions createNewSimpRepInstructions = Marshal.in(ctx, params.get("createNewSimpRepInstructions"),
                "CreateNewSimpRepInstructions", CreateNewSimpRepInstructions.class, "createNewSimpRepInstructions");
        return Marshal.result(ctx, target.CreateSimpRep(createNewSimpRepInstructions), "SimpRep");
    }
}
