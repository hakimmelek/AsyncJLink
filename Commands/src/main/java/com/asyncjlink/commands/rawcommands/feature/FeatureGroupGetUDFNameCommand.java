/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.feature;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcFeature.FeatureGroup;

/**
 * FeatureGroup.GetUDFName &mdash; pfcFeature.
 *
 * <pre>
 * String GetUDFName() throws jxthrowable
 * </pre>
 */
public final class FeatureGroupGetUDFNameCommand implements Command {

    @Override public String name() { return "FeatureGroup.GetUDFName"; }
    @Override public String jlinkPackage() { return "pfcFeature"; }
    @Override public String receiverType() { return "FeatureGroup"; }
    @Override public String signature() { return "String GetUDFName() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("FeatureGroup.GetUDFName \u2014 pfcFeature")
                .required("target", JsonSchema.handle("FeatureGroup"),
                        "The FeatureGroup to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        FeatureGroup target = Marshal.in(ctx, params.get("target"),
                "FeatureGroup", FeatureGroup.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetUDFName(), "String");
    }
}
