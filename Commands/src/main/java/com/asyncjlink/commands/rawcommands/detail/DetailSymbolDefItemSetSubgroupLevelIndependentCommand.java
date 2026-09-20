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
import com.ptc.pfc.pfcDetail.DetailSymbolDefItem;
import com.ptc.pfc.pfcDetail.DetailSymbolGroup;

/**
 * DetailSymbolDefItem.SetSubgroupLevelIndependent &mdash; pfcDetail.
 *
 * <pre>
 * void SetSubgroupLevelIndependent(DetailSymbolGroup) throws jxthrowable
 * </pre>
 */
public final class DetailSymbolDefItemSetSubgroupLevelIndependentCommand implements Command {

    @Override public String name() { return "DetailSymbolDefItem.SetSubgroupLevelIndependent"; }
    @Override public String jlinkPackage() { return "pfcDetail"; }
    @Override public String receiverType() { return "DetailSymbolDefItem"; }
    @Override public String signature() { return "void SetSubgroupLevelIndependent(DetailSymbolGroup) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("DetailSymbolDefItem.SetSubgroupLevelIndependent \u2014 pfcDetail")
                .required("target", JsonSchema.handle("DetailSymbolDefItem"),
                        "The DetailSymbolDefItem to act on.")
                .optional("detailSymbolGroup", JsonSchema.handle("DetailSymbolGroup"),
                        "Handle to a DetailSymbolGroup, as returned by an earlier command.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        DetailSymbolDefItem target = Marshal.in(ctx, params.get("target"),
                "DetailSymbolDefItem", DetailSymbolDefItem.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        DetailSymbolGroup detailSymbolGroup = Marshal.in(ctx, params.get("detailSymbolGroup"),
                "DetailSymbolGroup", DetailSymbolGroup.class, "detailSymbolGroup");
        target.SetSubgroupLevelIndependent(detailSymbolGroup);
        return Marshal.ok();
    }
}
