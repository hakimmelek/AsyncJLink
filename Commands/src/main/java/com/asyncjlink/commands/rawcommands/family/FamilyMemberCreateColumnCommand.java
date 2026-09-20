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
import com.ptc.pfc.pfcFamily.FamilyColumnType;
import com.ptc.pfc.pfcFamily.FamilyMember;

/**
 * FamilyMember.CreateColumn &mdash; pfcFamily.
 *
 * <pre>
 * FamilyTableColumn CreateColumn(FamilyColumnType, String) throws jxthrowable
 * </pre>
 */
public final class FamilyMemberCreateColumnCommand implements Command {

    @Override public String name() { return "FamilyMember.CreateColumn"; }
    @Override public String jlinkPackage() { return "pfcFamily"; }
    @Override public String receiverType() { return "FamilyMember"; }
    @Override public String signature() { return "FamilyTableColumn CreateColumn(FamilyColumnType, String) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("FamilyMember.CreateColumn \u2014 pfcFamily")
                .required("target", JsonSchema.handle("FamilyMember"),
                        "The FamilyMember to act on.")
                .optional("familyColumnType", JsonSchema.enumOf("FamilyColumnType", "FAM_USER_PARAM", "FAM_DIMENSION", "FAM_IPAR_NOTE", "FAM_FEATURE", "FAM_ASMCOMP", "FAM_UDF", "FAM_ASMCOMP_MODEL", "FAM_GTOL", "FAM_TOL_PLUS", "FAM_TOL_MINUS", "FAM_TOL_PLUSMINUS", "FAM_SYSTEM_PARAM", "FAM_EXTERNAL_REFERENCE", "FAM_MERGE_PART_REF", "FAM_MASS_PROPS_USER_PARAM", "FAM_MASS_PROPS_SOURCE", "FAM_INH_PART_REF", "FAM_SIM_OBJ", "FAM_FEATURE_PARAM", "FAM_EDGE_PARAM", "FAM_SURFACE_PARAM", "FAM_CURVE_PARAM", "FAM_COMP_CURVE_PARAM", "FAM_QUILT_PARAM", "FAM_ANNOT_ELEM_PARAM", "FAM_CONNECTION_PARAM"),
                        "One of the FamilyColumnType constants.")
                .optional("value", JsonSchema.string(),
                        "String value.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        FamilyMember target = Marshal.in(ctx, params.get("target"),
                "FamilyMember", FamilyMember.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        FamilyColumnType familyColumnType = Marshal.in(ctx, params.get("familyColumnType"),
                "FamilyColumnType", FamilyColumnType.class, "familyColumnType");
        String value = Marshal.in(ctx, params.get("value"),
                "String", String.class, "value");
        return Marshal.result(ctx, target.CreateColumn(familyColumnType, value), "FamilyTableColumn");
    }
}
