/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.detail;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcDetail.DetailSymbolGroup;

/**
 * DetailSymbolGroup.GetParentGroup &mdash; pfcDetail.
 *
 * <pre>
 * DetailSymbolGroup GetParentGroup() throws jxthrowable
 * </pre>
 */
public final class DetailSymbolGroupGetParentGroupCommand implements Command {

    @Override public String name() { return "DetailSymbolGroup.GetParentGroup"; }
    @Override public String jlinkPackage() { return "pfcDetail"; }
    @Override public String receiverType() { return "DetailSymbolGroup"; }
    @Override public String signature() { return "DetailSymbolGroup GetParentGroup() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("DetailSymbolGroup.GetParentGroup \u2014 pfcDetail")
                .required("target", JsonSchema.handle("DetailSymbolGroup"),
                        "The DetailSymbolGroup to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        DetailSymbolGroup target = Marshal.in(ctx, params.get("target"),
                "DetailSymbolGroup", DetailSymbolGroup.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetParentGroup(), "DetailSymbolGroup");
    }
}
