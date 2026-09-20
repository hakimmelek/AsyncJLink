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
import com.ptc.pfc.pfcFamily.FamilyTableColumn;
import com.ptc.pfc.pfcFamily.FamilyTableRow;

/**
 * FamilyMember.GetCellIsDefault &mdash; pfcFamily.
 *
 * <pre>
 * boolean GetCellIsDefault(FamilyTableColumn, FamilyTableRow) throws jxthrowable
 * </pre>
 */
public final class FamilyMemberGetCellIsDefaultCommand implements Command {

    @Override public String name() { return "FamilyMember.GetCellIsDefault"; }
    @Override public String jlinkPackage() { return "pfcFamily"; }
    @Override public String receiverType() { return "FamilyMember"; }
    @Override public String signature() { return "boolean GetCellIsDefault(FamilyTableColumn, FamilyTableRow) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("FamilyMember.GetCellIsDefault \u2014 pfcFamily")
                .required("target", JsonSchema.handle("FamilyMember"),
                        "The FamilyMember to act on.")
                .optional("familyTableColumn", JsonSchema.handle("FamilyTableColumn"),
                        "Handle to a FamilyTableColumn, as returned by an earlier command.")
                .optional("familyTableRow", JsonSchema.handle("FamilyTableRow"),
                        "Handle to a FamilyTableRow, as returned by an earlier command.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        FamilyMember target = Marshal.in(ctx, params.get("target"),
                "FamilyMember", FamilyMember.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        FamilyTableColumn familyTableColumn = Marshal.in(ctx, params.get("familyTableColumn"),
                "FamilyTableColumn", FamilyTableColumn.class, "familyTableColumn");
        FamilyTableRow familyTableRow = Marshal.in(ctx, params.get("familyTableRow"),
                "FamilyTableRow", FamilyTableRow.class, "familyTableRow");
        return Marshal.result(ctx, target.GetCellIsDefault(familyTableColumn, familyTableRow), "boolean");
    }
}
