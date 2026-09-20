/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.componentfeat;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcComponentFeat.ComponentFeat;

/**
 * ComponentFeat.GetIsPlaced &mdash; pfcComponentFeat.
 *
 * <pre>
 * boolean GetIsPlaced() throws jxthrowable
 * </pre>
 */
public final class ComponentFeatGetIsPlacedCommand implements Command {

    @Override public String name() { return "ComponentFeat.GetIsPlaced"; }
    @Override public String jlinkPackage() { return "pfcComponentFeat"; }
    @Override public String receiverType() { return "ComponentFeat"; }
    @Override public String signature() { return "boolean GetIsPlaced() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("ComponentFeat.GetIsPlaced \u2014 pfcComponentFeat")
                .required("target", JsonSchema.handle("ComponentFeat"),
                        "The ComponentFeat to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        ComponentFeat target = Marshal.in(ctx, params.get("target"),
                "ComponentFeat", ComponentFeat.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetIsPlaced(), "boolean");
    }
}
