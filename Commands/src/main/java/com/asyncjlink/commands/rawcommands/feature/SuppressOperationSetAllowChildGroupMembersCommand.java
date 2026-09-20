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
import com.ptc.pfc.pfcFeature.SuppressOperation;

/**
 * SuppressOperation.SetAllowChildGroupMembers &mdash; pfcFeature.
 *
 * <pre>
 * void SetAllowChildGroupMembers(boolean) throws jxthrowable
 * </pre>
 */
public final class SuppressOperationSetAllowChildGroupMembersCommand implements Command {

    @Override public String name() { return "SuppressOperation.SetAllowChildGroupMembers"; }
    @Override public String jlinkPackage() { return "pfcFeature"; }
    @Override public String receiverType() { return "SuppressOperation"; }
    @Override public String signature() { return "void SetAllowChildGroupMembers(boolean) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("SuppressOperation.SetAllowChildGroupMembers \u2014 pfcFeature")
                .required("target", JsonSchema.handle("SuppressOperation"),
                        "The SuppressOperation to act on.")
                .required("flag", JsonSchema.bool(),
                        "boolean value.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        SuppressOperation target = Marshal.in(ctx, params.get("target"),
                "SuppressOperation", SuppressOperation.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        boolean flag = Marshal.in(ctx, params.get("flag"),
                "boolean", boolean.class, "flag");
        target.SetAllowChildGroupMembers(flag);
        return Marshal.ok();
    }
}
