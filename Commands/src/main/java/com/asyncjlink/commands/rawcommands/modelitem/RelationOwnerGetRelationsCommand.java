/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.modelitem;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcModelItem.RelationOwner;

/**
 * RelationOwner.GetRelations &mdash; pfcModelItem.
 *
 * <pre>
 * stringseq GetRelations() throws jxthrowable
 * </pre>
 */
public final class RelationOwnerGetRelationsCommand implements Command {

    @Override public String name() { return "RelationOwner.GetRelations"; }
    @Override public String jlinkPackage() { return "pfcModelItem"; }
    @Override public String receiverType() { return "RelationOwner"; }
    @Override public String signature() { return "stringseq GetRelations() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("RelationOwner.GetRelations \u2014 pfcModelItem")
                .required("target", JsonSchema.handle("RelationOwner"),
                        "The RelationOwner to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        RelationOwner target = Marshal.in(ctx, params.get("target"),
                "RelationOwner", RelationOwner.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetRelations(), "stringseq");
    }
}
