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
import com.ptc.pfc.pfcFeature.GroupPattern;

/**
 * GroupPattern.ListFeatMembers &mdash; pfcFeature.
 *
 * <pre>
 * Features ListFeatMembers() throws jxthrowable
 * </pre>
 */
public final class GroupPatternListFeatMembersCommand implements Command {

    @Override public String name() { return "GroupPattern.ListFeatMembers"; }
    @Override public String jlinkPackage() { return "pfcFeature"; }
    @Override public String receiverType() { return "GroupPattern"; }
    @Override public String signature() { return "Features ListFeatMembers() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("GroupPattern.ListFeatMembers \u2014 pfcFeature")
                .required("target", JsonSchema.handle("GroupPattern"),
                        "The GroupPattern to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        GroupPattern target = Marshal.in(ctx, params.get("target"),
                "GroupPattern", GroupPattern.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.ListFeatMembers(), "Features");
    }
}
