/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.modelitem;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcModelItem.ModelItemOwner;
import com.ptc.pfc.pfcModelItem.ModelItemType;

/**
 * ModelItemOwner.GetItemById &mdash; pfcModelItem.
 *
 * <pre>
 * ModelItem GetItemById(ModelItemType, int) throws jxthrowable
 * </pre>
 */
public final class ModelItemOwnerGetItemByIdCommand implements Command {

    @Override public String name() { return "ModelItemOwner.GetItemById"; }
    @Override public String jlinkPackage() { return "pfcModelItem"; }
    @Override public String receiverType() { return "ModelItemOwner"; }
    @Override public String signature() { return "ModelItem GetItemById(ModelItemType, int) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("ModelItemOwner.GetItemById \u2014 pfcModelItem")
                .required("target", JsonSchema.handle("ModelItemOwner"),
                        "The ModelItemOwner to act on.")
                .optional("modelItemType", JsonSchema.enumOf("ModelItemType", "ITEM_FEATURE", "ITEM_SURFACE", "ITEM_EDGE", "ITEM_COORD_SYS", "ITEM_AXIS", "ITEM_POINT", "ITEM_QUILT", "ITEM_CURVE", "ITEM_LAYER", "ITEM_NOTE", "ITEM_DIMENSION", "ITEM_REF_DIMENSION", "ITEM_SIMPREP", "ITEM_SOLID_GEOMETRY", "ITEM_TABLE", "ITEM_DTL_ENTITY", "ITEM_DTL_NOTE", "ITEM_DTL_GROUP", "ITEM_DTL_SYM_DEFINITION", "ITEM_DTL_SYM_INSTANCE", "ITEM_DTL_OLE_OBJECT", "ITEM_EXPLODED_STATE", "ITEM_EDGE_START", "ITEM_LOG_EDGE", "ITEM_EDGE_END", "ITEM_XSEC", "ITEM_LAYER_STATE", "ITEM_COMBINED_STATE", "ITEM_STYLE_STATE", "ITEM_RP_MATERIAL", "ITEM_VIEW"),
                        "One of the ModelItemType constants.")
                .required("value", JsonSchema.integer(),
                        "int value.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        ModelItemOwner target = Marshal.in(ctx, params.get("target"),
                "ModelItemOwner", ModelItemOwner.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        ModelItemType modelItemType = Marshal.in(ctx, params.get("modelItemType"),
                "ModelItemType", ModelItemType.class, "modelItemType");
        int value = Marshal.in(ctx, params.get("value"),
                "int", int.class, "value");
        return Marshal.result(ctx, target.GetItemById(modelItemType, value), "ModelItem");
    }
}
