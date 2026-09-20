/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.session;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcImport.LayerImportFilter;
import com.ptc.pfc.pfcImport.NewModelImportType;
import com.ptc.pfc.pfcModel.ModelType;
import com.ptc.pfc.pfcSession.BaseSession;
import com.ptc.pfc.pfcSession.Session;

/**
 * BaseSession.ImportNewModel &mdash; pfcSession.
 *
 * <pre>
 * Model ImportNewModel(String, NewModelImportType, ModelType, String, LayerImportFilter) throws jxthrowable
 * </pre>
 */
public final class BaseSessionImportNewModelCommand implements Command {

    @Override public String name() { return "BaseSession.ImportNewModel"; }
    @Override public String jlinkPackage() { return "pfcSession"; }
    @Override public String receiverType() { return "BaseSession"; }
    @Override public String signature() { return "Model ImportNewModel(String, NewModelImportType, ModelType, String, LayerImportFilter) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("BaseSession.ImportNewModel \u2014 pfcSession")
                .optional("value1", JsonSchema.string(),
                        "String value.")
                .optional("newModelImportType", JsonSchema.enumOf("NewModelImportType", "IMPORT_NEW_IGES", "IMPORT_NEW_VDA", "IMPORT_NEW_NEUTRAL", "IMPORT_NEW_CADDS", "IMPORT_NEW_STEP", "IMPORT_NEW_STL", "IMPORT_NEW_VRML", "IMPORT_NEW_POLTXT", "IMPORT_NEW_CATIA_SESSION", "IMPORT_NEW_CATIA_MODEL", "IMPORT_NEW_DXF", "IMPORT_NEW_ACIS", "IMPORT_NEW_PARASOLID", "IMPORT_NEW_ICEM", "IMPORT_NEW_CATIA_PART", "IMPORT_NEW_CATIA_PRODUCT", "IMPORT_NEW_UG", "IMPORT_NEW_PRODUCTVIEW", "IMPORT_NEW_CATIA_CGR", "IMPORT_NEW_JT", "IMPORT_NEW_SW_PART", "IMPORT_NEW_SW_ASSEM", "IMPORT_NEW_INVENTOR_PART", "IMPORT_NEW_INVENTOR_ASSEM", "IMPORT_NEW_CC", "IMPORT_NEW_SEDGE_PART", "IMPORT_NEW_SEDGE_ASSEMBLY", "IMPORT_NEW_SEDGE_SHEETMETAL_PART"),
                        "One of the NewModelImportType constants.")
                .optional("modelType", JsonSchema.enumOf("ModelType", "MDL_ASSEMBLY", "MDL_PART", "MDL_DRAWING", "MDL_2D_SECTION", "MDL_LAYOUT", "MDL_DWG_FORMAT", "MDL_MFG", "MDL_REPORT", "MDL_MARKUP", "MDL_DIAGRAM", "MDL_UNSPECIFIED", "MDL_CE_SOLID", "MDL_CE_DRAWING"),
                        "One of the ModelType constants.")
                .optional("value2", JsonSchema.string(),
                        "String value.")
                .optional("layerImportFilter", JsonSchema.dataObject("LayerImportFilter"),
                        "LayerImportFilter options object; its fields are passed to the pfc factory and setters.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Session target = ctx.session();
        String value1 = Marshal.in(ctx, params.get("value1"),
                "String", String.class, "value1");
        NewModelImportType newModelImportType = Marshal.in(ctx, params.get("newModelImportType"),
                "NewModelImportType", NewModelImportType.class, "newModelImportType");
        ModelType modelType = Marshal.in(ctx, params.get("modelType"),
                "ModelType", ModelType.class, "modelType");
        String value2 = Marshal.in(ctx, params.get("value2"),
                "String", String.class, "value2");
        LayerImportFilter layerImportFilter = Marshal.in(ctx, params.get("layerImportFilter"),
                "LayerImportFilter", LayerImportFilter.class, "layerImportFilter");
        return Marshal.result(ctx, target.ImportNewModel(value1, newModelImportType, modelType, value2, layerImportFilter), "Model");
    }
}
