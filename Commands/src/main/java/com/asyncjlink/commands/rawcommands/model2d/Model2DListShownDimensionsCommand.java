/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.model2d;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModel2D.Model2D;
import com.ptc.pfc.pfcModelItem.ModelItemType;

/**
 * Model2D.ListShownDimensions &mdash; pfcModel2D.
 *
 * <pre>
 * Dimension2Ds ListShownDimensions(Model, ModelItemType) throws jxthrowable
 * </pre>
 */
public final class Model2DListShownDimensionsCommand implements Command {

    @Override public String name() { return "Model2D.ListShownDimensions"; }
    @Override public String jlinkPackage() { return "pfcModel2D"; }
    @Override public String receiverType() { return "Model2D"; }
    @Override public String signature() { return "Dimension2Ds ListShownDimensions(Model, ModelItemType) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Model2D.ListShownDimensions \u2014 pfcModel2D")
                .required("target", JsonSchema.handle("Model2D"),
                        "The Model2D to act on.")
                .optional("model", JsonSchema.handle("Model"),
                        "Handle to a Model, as returned by an earlier command.")
                .optional("modelItemType", JsonSchema.enumOf("ModelItemType", "ITEM_FEATURE", "ITEM_SURFACE", "ITEM_EDGE", "ITEM_COORD_SYS", "ITEM_AXIS", "ITEM_POINT", "ITEM_QUILT", "ITEM_CURVE", "ITEM_LAYER", "ITEM_NOTE", "ITEM_DIMENSION", "ITEM_REF_DIMENSION", "ITEM_SIMPREP", "ITEM_SOLID_GEOMETRY", "ITEM_TABLE", "ITEM_DTL_ENTITY", "ITEM_DTL_NOTE", "ITEM_DTL_GROUP", "ITEM_DTL_SYM_DEFINITION", "ITEM_DTL_SYM_INSTANCE", "ITEM_DTL_OLE_OBJECT", "ITEM_EXPLODED_STATE", "ITEM_EDGE_START", "ITEM_LOG_EDGE", "ITEM_EDGE_END", "ITEM_XSEC", "ITEM_LAYER_STATE", "ITEM_COMBINED_STATE", "ITEM_STYLE_STATE", "ITEM_RP_MATERIAL", "ITEM_VIEW"),
                        "One of the ModelItemType constants.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Model2D target = Marshal.in(ctx, params.get("target"),
                "Model2D", Model2D.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        Model model = Marshal.in(ctx, params.get("model"),
                "Model", Model.class, "model");
        ModelItemType modelItemType = Marshal.in(ctx, params.get("modelItemType"),
                "ModelItemType", ModelItemType.class, "modelItemType");
        return Marshal.result(ctx, target.ListShownDimensions(model, modelItemType), "Dimension2Ds");
    }
}
