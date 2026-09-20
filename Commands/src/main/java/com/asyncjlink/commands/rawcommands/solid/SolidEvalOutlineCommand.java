/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.solid;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcBase.Transform3D;
import com.ptc.pfc.pfcModelItem.ModelItemTypes;
import com.ptc.pfc.pfcSolid.Solid;

/**
 * Solid.EvalOutline &mdash; pfcSolid.
 *
 * <pre>
 * Outline3D EvalOutline(Transform3D, ModelItemTypes) throws jxthrowable
 * </pre>
 */
public final class SolidEvalOutlineCommand implements Command {

    @Override public String name() { return "Solid.EvalOutline"; }
    @Override public String jlinkPackage() { return "pfcSolid"; }
    @Override public String receiverType() { return "Solid"; }
    @Override public String signature() { return "Outline3D EvalOutline(Transform3D, ModelItemTypes) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Solid.EvalOutline \u2014 pfcSolid")
                .required("target", JsonSchema.handle("Solid"),
                        "The Solid to act on.")
                .optional("transform3D", JsonSchema.dataObject("Transform3D"),
                        "Transform3D options object; its fields are passed to the pfc factory and setters.")
                .optional("modelItemTypes", JsonSchema.sequence("ModelItemTypes", JsonSchema.enumOf("ModelItemType", "ITEM_FEATURE", "ITEM_SURFACE", "ITEM_EDGE", "ITEM_COORD_SYS", "ITEM_AXIS", "ITEM_POINT", "ITEM_QUILT", "ITEM_CURVE", "ITEM_LAYER", "ITEM_NOTE", "ITEM_DIMENSION", "ITEM_REF_DIMENSION", "ITEM_SIMPREP", "ITEM_SOLID_GEOMETRY", "ITEM_TABLE", "ITEM_DTL_ENTITY", "ITEM_DTL_NOTE", "ITEM_DTL_GROUP", "ITEM_DTL_SYM_DEFINITION", "ITEM_DTL_SYM_INSTANCE", "ITEM_DTL_OLE_OBJECT", "ITEM_EXPLODED_STATE", "ITEM_EDGE_START", "ITEM_LOG_EDGE", "ITEM_EDGE_END", "ITEM_XSEC", "ITEM_LAYER_STATE", "ITEM_COMBINED_STATE", "ITEM_STYLE_STATE", "ITEM_RP_MATERIAL", "ITEM_VIEW")),
                        "Array of ModelItemType.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Solid target = Marshal.in(ctx, params.get("target"),
                "Solid", Solid.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        Transform3D transform3D = Marshal.in(ctx, params.get("transform3D"),
                "Transform3D", Transform3D.class, "transform3D");
        ModelItemTypes modelItemTypes = Marshal.in(ctx, params.get("modelItemTypes"),
                "ModelItemTypes", ModelItemTypes.class, "modelItemTypes");
        return Marshal.result(ctx, target.EvalOutline(transform3D, modelItemTypes), "Outline3D");
    }
}
