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
import com.ptc.pfc.pfcFamily.FamilyTableColumn;

/**
 * FamilyTableColumn.GetSymbol &mdash; pfcFamily.
 *
 * <pre>
 * String GetSymbol() throws jxthrowable
 * </pre>
 */
public final class FamilyTableColumnGetSymbolCommand implements Command {

    @Override public String name() { return "FamilyTableColumn.GetSymbol"; }
    @Override public String jlinkPackage() { return "pfcFamily"; }
    @Override public String receiverType() { return "FamilyTableColumn"; }
    @Override public String signature() { return "String GetSymbol() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("FamilyTableColumn.GetSymbol \u2014 pfcFamily")
                .required("target", JsonSchema.handle("FamilyTableColumn"),
                        "The FamilyTableColumn to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        FamilyTableColumn target = Marshal.in(ctx, params.get("target"),
                "FamilyTableColumn", FamilyTableColumn.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetSymbol(), "String");
    }
}
