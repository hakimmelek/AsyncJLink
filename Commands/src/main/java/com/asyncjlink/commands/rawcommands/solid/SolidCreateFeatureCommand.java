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
import com.ptc.pfc.pfcFeature.FeatureCreateInstructions;
import com.ptc.pfc.pfcSolid.Solid;

/**
 * Solid.CreateFeature &mdash; pfcSolid.
 *
 * <pre>
 * Feature CreateFeature(FeatureCreateInstructions) throws jxthrowable
 * </pre>
 */
public final class SolidCreateFeatureCommand implements Command {

    @Override public String name() { return "Solid.CreateFeature"; }
    @Override public String jlinkPackage() { return "pfcSolid"; }
    @Override public String receiverType() { return "Solid"; }
    @Override public String signature() { return "Feature CreateFeature(FeatureCreateInstructions) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Solid.CreateFeature \u2014 pfcSolid")
                .required("target", JsonSchema.handle("Solid"),
                        "The Solid to act on.")
                .optional("featureCreateInstructions", JsonSchema.dataObject("FeatureCreateInstructions"),
                        "FeatureCreateInstructions options object; its fields are passed to the pfc factory and setters.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Solid target = Marshal.in(ctx, params.get("target"),
                "Solid", Solid.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        FeatureCreateInstructions featureCreateInstructions = Marshal.in(ctx, params.get("featureCreateInstructions"),
                "FeatureCreateInstructions", FeatureCreateInstructions.class, "featureCreateInstructions");
        return Marshal.result(ctx, target.CreateFeature(featureCreateInstructions), "Feature");
    }
}
