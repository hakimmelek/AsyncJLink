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
import com.ptc.pfc.pfcFeature.FeatureOperations;
import com.ptc.pfc.pfcSolid.RegenInstructions;
import com.ptc.pfc.pfcSolid.Solid;

/**
 * Solid.ExecuteFeatureOps &mdash; pfcSolid.
 *
 * <pre>
 * void ExecuteFeatureOps(FeatureOperations, RegenInstructions) throws jxthrowable
 * </pre>
 */
public final class SolidExecuteFeatureOpsCommand implements Command {

    @Override public String name() { return "Solid.ExecuteFeatureOps"; }
    @Override public String jlinkPackage() { return "pfcSolid"; }
    @Override public String receiverType() { return "Solid"; }
    @Override public String signature() { return "void ExecuteFeatureOps(FeatureOperations, RegenInstructions) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Solid.ExecuteFeatureOps \u2014 pfcSolid")
                .required("target", JsonSchema.handle("Solid"),
                        "The Solid to act on.")
                .optional("featureOperations", JsonSchema.sequence("FeatureOperations", JsonSchema.handle("FeatureOperation")),
                        "Array of FeatureOperation.")
                .optional("regenInstructions", JsonSchema.dataObject("RegenInstructions"),
                        "RegenInstructions options object; its fields are passed to the pfc factory and setters.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Solid target = Marshal.in(ctx, params.get("target"),
                "Solid", Solid.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        FeatureOperations featureOperations = Marshal.in(ctx, params.get("featureOperations"),
                "FeatureOperations", FeatureOperations.class, "featureOperations");
        RegenInstructions regenInstructions = Marshal.in(ctx, params.get("regenInstructions"),
                "RegenInstructions", RegenInstructions.class, "regenInstructions");
        target.ExecuteFeatureOps(featureOperations, regenInstructions);
        return Marshal.ok();
    }
}
