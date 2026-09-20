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
import com.ptc.pfc.pfcDetail.DetailSymbolInstItem;
import com.ptc.pfc.pfcDetail.SymbolGroupFilter;

/**
 * DetailSymbolInstItem.ListGroups &mdash; pfcDetail.
 *
 * <pre>
 * DetailSymbolGroups ListGroups(SymbolGroupFilter) throws jxthrowable
 * </pre>
 */
public final class DetailSymbolInstItemListGroupsCommand implements Command {

    @Override public String name() { return "DetailSymbolInstItem.ListGroups"; }
    @Override public String jlinkPackage() { return "pfcDetail"; }
    @Override public String receiverType() { return "DetailSymbolInstItem"; }
    @Override public String signature() { return "DetailSymbolGroups ListGroups(SymbolGroupFilter) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("DetailSymbolInstItem.ListGroups \u2014 pfcDetail")
                .required("target", JsonSchema.handle("DetailSymbolInstItem"),
                        "The DetailSymbolInstItem to act on.")
                .optional("symbolGroupFilter", JsonSchema.enumOf("SymbolGroupFilter", "DTLSYMINST_ALL_GROUPS", "DTLSYMINST_ACTIVE_GROUPS", "DTLSYMINST_INACTIVE_GROUPS"),
                        "One of the SymbolGroupFilter constants.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        DetailSymbolInstItem target = Marshal.in(ctx, params.get("target"),
                "DetailSymbolInstItem", DetailSymbolInstItem.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        SymbolGroupFilter symbolGroupFilter = Marshal.in(ctx, params.get("symbolGroupFilter"),
                "SymbolGroupFilter", SymbolGroupFilter.class, "symbolGroupFilter");
        return Marshal.result(ctx, target.ListGroups(symbolGroupFilter), "DetailSymbolGroups");
    }
}
