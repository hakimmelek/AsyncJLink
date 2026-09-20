/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.part;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcPart.Material;
import com.ptc.pfc.pfcPart.MaterialPropertyType;

/**
 * Material.RemoveProperty &mdash; pfcPart.
 *
 * <pre>
 * void RemoveProperty(MaterialPropertyType) throws jxthrowable
 * </pre>
 */
public final class MaterialRemovePropertyCommand implements Command {

    @Override public String name() { return "Material.RemoveProperty"; }
    @Override public String jlinkPackage() { return "pfcPart"; }
    @Override public String receiverType() { return "Material"; }
    @Override public String signature() { return "void RemoveProperty(MaterialPropertyType) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Material.RemoveProperty \u2014 pfcPart")
                .required("target", JsonSchema.handle("Material"),
                        "The Material to act on.")
                .optional("materialPropertyType", JsonSchema.enumOf("MaterialPropertyType", "MTL_PROP_FATIGUE_STRENGTH_REDUCTION_FACTOR", "MTL_PROP_TSAI_WU_INTERACTION_TERM_F12", "MTL_PROP_TENSILE_YIELD_STRESS", "MTL_PROP_TENSILE_ULTIMATE_STRESS_ST1", "MTL_PROP_TENSILE_ULTIMATE_STRESS_ST2", "MTL_PROP_COMPRESSION_ULTIMATE_STRESS_SC1", "MTL_PROP_COMPRESSION_ULTIMATE_STRESS_SC2", "MTL_PROP_POISSON_RATIO_NU21", "MTL_PROP_POISSON_RATIO_NU31", "MTL_PROP_POISSON_RATIO_NU32", "MTL_PROP_YOUNG_MODULUS_E1", "MTL_PROP_YOUNG_MODULUS_E2", "MTL_PROP_YOUNG_MODULUS_E3", "MTL_PROP_SHEAR_MODULUS_G12", "MTL_PROP_SHEAR_MODULUS_G13", "MTL_PROP_SHEAR_MODULUS_G23", "MTL_PROP_THERMAL_EXPANSION_COEFFICIENT_A1", "MTL_PROP_THERMAL_EXPANSION_COEFFICIENT_A2", "MTL_PROP_THERMAL_EXPANSION_COEFFICIENT_A3", "MTL_PROP_THERMAL_CONDUCTIVITY_K1", "MTL_PROP_THERMAL_CONDUCTIVITY_K2", "MTL_PROP_THERMAL_CONDUCTIVITY_K3", "MTL_PROP_STRESS_LIMIT_FOR_FATIGUE", "MTL_PROP_TEMPERATURE", "MTL_PROP_YOUNG_MODULUS", "MTL_PROP_POISSON_RATIO", "MTL_PROP_MASS_DENSITY", "MTL_PROP_THERMAL_EXPANSION_COEFFICIENT", "MTL_PROP_TENSILE_ULTIMATE_STRESS", "MTL_PROP_COMPRESSION_ULTIMATE_STRESS", "MTL_PROP_SHEAR_ULTIMATE_STRESS", "MTL_PROP_THERMAL_CONDUCTIVITY", "MTL_PROP_SPECIFIC_HEAT", "MTL_PROP_HARDNESS", "MTL_PROP_INITIAL_BEND_Y_FACTOR", "MTL_PROP_SHEAR_MODULUS", "MTL_PROP_THERM_EXPANSION_REF_TEMPERATURE", "MTL_PROP_STRUCTURAL_DAMPING_COEFFICIENT", "MTL_PROP_EMISSIVITY", "MTL_PROP_MODEL_COEF_MU", "MTL_PROP_MODEL_COEF_LM", "MTL_PROP_MODEL_COEF_C01", "MTL_PROP_MODEL_COEF_C02", "MTL_PROP_MODEL_COEF_C10", "MTL_PROP_MODEL_COEF_C11", "MTL_PROP_MODEL_COEF_C20", "MTL_PROP_MODEL_COEF_C30", "MTL_PROP_MODEL_COEF_D", "MTL_PROP_MODEL_COEF_D1", "MTL_PROP_MODEL_COEF_D2", "MTL_PROP_MODEL_COEF_D3", "MTL_PROP_COST_TYPE", "MTL_PROP_FATIGUE_CUT_OFF_CYCLES", "MTL_PROP_STRESS_LIMIT_FOR_TENSION", "MTL_PROP_STRESS_LIMIT_FOR_COMPRESSION", "MTL_PROP_STRESS_LIMIT_FOR_SHEAR", "MTL_PROP_HARDENING", "MTL_PROP_TANGENT_MODULUS", "MTL_PROP_MODIFIED_MODULUS", "MTL_PROP_POWER_LAW_EXPONENT", "MTL_PROP_EXP_LAW_EXPONENT", "MTL_PROP_HARDENING_LIMIT", "MTL_PROP_THERMAL_SOFTENING_COEF", "MTL_PROP_MECHANISMS_DAMPING"),
                        "One of the MaterialPropertyType constants.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Material target = Marshal.in(ctx, params.get("target"),
                "Material", Material.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        MaterialPropertyType materialPropertyType = Marshal.in(ctx, params.get("materialPropertyType"),
                "MaterialPropertyType", MaterialPropertyType.class, "materialPropertyType");
        target.RemoveProperty(materialPropertyType);
        return Marshal.ok();
    }
}
