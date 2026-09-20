/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.family;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcFamily.FamilyMember;
import com.ptc.pfc.pfcFeature.Feature;

/**
 * FamilyMember.CreateFeatureColumn &mdash; pfcFamily.
 *
 * <pre>
 * FamColFeature CreateFeatureColumn(Feature) throws jxthrowable
 * </pre>
 */
public final class FamilyMemberCreateFeatureColumnCommand implements Command {

    @Override public String name() { return "FamilyMember.CreateFeatureColumn"; }
    @Override public String jlinkPackage() { return "pfcFamily"; }
    @Override public String receiverType() { return "FamilyMember"; }
    @Override public String signature() { return "FamColFeature CreateFeatureColumn(Feature) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("FamilyMember.CreateFeatureColumn \u2014 pfcFamily")
                .required("target", JsonSchema.handle("FamilyMember"),
                        "The FamilyMember to act on.")
                .optional("feature", JsonSchema.handle("Feature"),
                        "Handle to a Feature, as returned by an earlier command.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        FamilyMember target = Marshal.in(ctx, params.get("target"),
                "FamilyMember", FamilyMember.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        Feature feature = Marshal.in(ctx, params.get("feature"),
                "Feature", Feature.class, "feature");
        return Marshal.result(ctx, target.CreateFeatureColumn(feature), "FamColFeature");
    }
}
