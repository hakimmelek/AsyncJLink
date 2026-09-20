/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.object;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcObject.Child;

/**
 * Child.GetOId &mdash; pfcObject.
 *
 * <pre>
 * OId GetOId() throws jxthrowable
 * </pre>
 */
public final class ChildGetOIdCommand implements Command {

    @Override public String name() { return "Child.GetOId"; }
    @Override public String jlinkPackage() { return "pfcObject"; }
    @Override public String receiverType() { return "Child"; }
    @Override public String signature() { return "OId GetOId() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Child.GetOId \u2014 pfcObject")
                .required("target", JsonSchema.handle("Child"),
                        "The Child to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Child target = Marshal.in(ctx, params.get("target"),
                "Child", Child.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetOId(), "OId");
    }
}
