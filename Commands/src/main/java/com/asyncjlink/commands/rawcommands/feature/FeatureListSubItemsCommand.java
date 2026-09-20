/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.feature;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcFeature.Feature;
import com.ptc.pfc.pfcModelItem.ModelItemType;

/**
 * Feature.ListSubItems &mdash; pfcFeature.
 *
 * <pre>
 * ModelItems ListSubItems(ModelItemType) throws jxthrowable
 * </pre>
 */
public final class FeatureListSubItemsCommand implements Command {

    @Override public String name() { return "Feature.ListSubItems"; }
    @Override public String jlinkPackage() { return "pfcFeature"; }
    @Override public String receiverType() { return "Feature"; }
    @Override public String signature() { return "ModelItems ListSubItems(ModelItemType) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Feature.ListSubItems \u2014 pfcFeature")
                .required("target", JsonSchema.handle("Feature"),
                        "The Feature to act on.")
                .optional("modelItemType", JsonSchema.enumOf("ModelItemType", "ITEM_FEATURE", "ITEM_SURFACE", "ITEM_EDGE", "ITEM_COORD_SYS", "ITEM_AXIS", "ITEM_POINT", "ITEM_QUILT", "ITEM_CURVE", "ITEM_LAYER", "ITEM_NOTE", "ITEM_DIMENSION", "ITEM_REF_DIMENSION", "ITEM_SIMPREP", "ITEM_SOLID_GEOMETRY", "ITEM_TABLE", "ITEM_DTL_ENTITY", "ITEM_DTL_NOTE", "ITEM_DTL_GROUP", "ITEM_DTL_SYM_DEFINITION", "ITEM_DTL_SYM_INSTANCE", "ITEM_DTL_OLE_OBJECT", "ITEM_EXPLODED_STATE", "ITEM_EDGE_START", "ITEM_LOG_EDGE", "ITEM_EDGE_END", "ITEM_XSEC", "ITEM_LAYER_STATE", "ITEM_COMBINED_STATE", "ITEM_STYLE_STATE", "ITEM_RP_MATERIAL", "ITEM_VIEW"),
                        "One of the ModelItemType constants.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Feature target = Marshal.in(ctx, params.get("target"),
                "Feature", Feature.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        ModelItemType modelItemType = Marshal.in(ctx, params.get("modelItemType"),
                "ModelItemType", ModelItemType.class, "modelItemType");
        return Marshal.result(ctx, target.ListSubItems(modelItemType), "ModelItems");
    }
}
