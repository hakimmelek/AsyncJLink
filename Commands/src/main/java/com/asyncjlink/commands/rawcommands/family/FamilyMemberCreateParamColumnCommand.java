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
import com.ptc.pfc.pfcModelItem.Parameter;

/**
 * FamilyMember.CreateParamColumn &mdash; pfcFamily.
 *
 * <pre>
 * FamColParam CreateParamColumn(Parameter) throws jxthrowable
 * </pre>
 */
public final class FamilyMemberCreateParamColumnCommand implements Command {

    @Override public String name() { return "FamilyMember.CreateParamColumn"; }
    @Override public String jlinkPackage() { return "pfcFamily"; }
    @Override public String receiverType() { return "FamilyMember"; }
    @Override public String signature() { return "FamColParam CreateParamColumn(Parameter) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("FamilyMember.CreateParamColumn \u2014 pfcFamily")
                .required("target", JsonSchema.handle("FamilyMember"),
                        "The FamilyMember to act on.")
                .optional("parameter", JsonSchema.handle("Parameter"),
                        "Handle to a Parameter, as returned by an earlier command.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        FamilyMember target = Marshal.in(ctx, params.get("target"),
                "FamilyMember", FamilyMember.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        Parameter parameter = Marshal.in(ctx, params.get("parameter"),
                "Parameter", Parameter.class, "parameter");
        return Marshal.result(ctx, target.CreateParamColumn(parameter), "FamColParam");
    }
}
