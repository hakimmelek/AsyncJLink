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
import com.ptc.pfc.pfcPart.MaterialType;

/**
 * Material.SetThermalMaterialType &mdash; pfcPart.
 *
 * <pre>
 * void SetThermalMaterialType(MaterialType) throws jxthrowable
 * </pre>
 */
public final class MaterialSetThermalMaterialTypeCommand implements Command {

    @Override public String name() { return "Material.SetThermalMaterialType"; }
    @Override public String jlinkPackage() { return "pfcPart"; }
    @Override public String receiverType() { return "Material"; }
    @Override public String signature() { return "void SetThermalMaterialType(MaterialType) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Material.SetThermalMaterialType \u2014 pfcPart")
                .required("target", JsonSchema.handle("Material"),
                        "The Material to act on.")
                .optional("materialType", JsonSchema.enumOf("MaterialType", "MTL_ISOTROPIC", "MTL_ORTHOTROPIC", "MTL_TRANSVERSELY_ISOTROPIC"),
                        "One of the MaterialType constants.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Material target = Marshal.in(ctx, params.get("target"),
                "Material", Material.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        MaterialType materialType = Marshal.in(ctx, params.get("materialType"),
                "MaterialType", MaterialType.class, "materialType");
        target.SetThermalMaterialType(materialType);
        return Marshal.ok();
    }
}
