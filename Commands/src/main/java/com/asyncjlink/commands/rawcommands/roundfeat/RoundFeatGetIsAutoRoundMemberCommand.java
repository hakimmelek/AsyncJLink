/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.roundfeat;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcRoundFeat.RoundFeat;

/**
 * RoundFeat.GetIsAutoRoundMember &mdash; pfcRoundFeat.
 *
 * <pre>
 * boolean GetIsAutoRoundMember() throws jxthrowable
 * </pre>
 */
public final class RoundFeatGetIsAutoRoundMemberCommand implements Command {

    @Override public String name() { return "RoundFeat.GetIsAutoRoundMember"; }
    @Override public String jlinkPackage() { return "pfcRoundFeat"; }
    @Override public String receiverType() { return "RoundFeat"; }
    @Override public String signature() { return "boolean GetIsAutoRoundMember() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("RoundFeat.GetIsAutoRoundMember \u2014 pfcRoundFeat")
                .required("target", JsonSchema.handle("RoundFeat"),
                        "The RoundFeat to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        RoundFeat target = Marshal.in(ctx, params.get("target"),
                "RoundFeat", RoundFeat.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetIsAutoRoundMember(), "boolean");
    }
}
