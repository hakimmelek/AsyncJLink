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
import com.ptc.pfc.pfcObject.OId;
import com.ptc.pfc.pfcObject.Parent;

/**
 * Parent.GetChild &mdash; pfcObject.
 *
 * <pre>
 * Child GetChild(OId) throws jxthrowable
 * </pre>
 */
public final class ParentGetChildCommand implements Command {

    @Override public String name() { return "Parent.GetChild"; }
    @Override public String jlinkPackage() { return "pfcObject"; }
    @Override public String receiverType() { return "Parent"; }
    @Override public String signature() { return "Child GetChild(OId) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Parent.GetChild \u2014 pfcObject")
                .required("target", JsonSchema.handle("Parent"),
                        "The Parent to act on.")
                .optional("oId", JsonSchema.dataObject("OId"),
                        "OId options object; its fields are passed to the pfc factory and setters.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Parent target = Marshal.in(ctx, params.get("target"),
                "Parent", Parent.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        OId oId = Marshal.in(ctx, params.get("oId"),
                "OId", OId.class, "oId");
        return Marshal.result(ctx, target.GetChild(oId), "Child");
    }
}
