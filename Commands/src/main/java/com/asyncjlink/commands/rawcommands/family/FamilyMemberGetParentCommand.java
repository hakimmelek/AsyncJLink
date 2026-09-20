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

/**
 * FamilyMember.GetParent &mdash; pfcFamily.
 *
 * <pre>
 * FamilyMember GetParent() throws jxthrowable
 * </pre>
 */
public final class FamilyMemberGetParentCommand implements Command {

    @Override public String name() { return "FamilyMember.GetParent"; }
    @Override public String jlinkPackage() { return "pfcFamily"; }
    @Override public String receiverType() { return "FamilyMember"; }
    @Override public String signature() { return "FamilyMember GetParent() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("FamilyMember.GetParent \u2014 pfcFamily")
                .required("target", JsonSchema.handle("FamilyMember"),
                        "The FamilyMember to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        FamilyMember target = Marshal.in(ctx, params.get("target"),
                "FamilyMember", FamilyMember.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetParent(), "FamilyMember");
    }
}
