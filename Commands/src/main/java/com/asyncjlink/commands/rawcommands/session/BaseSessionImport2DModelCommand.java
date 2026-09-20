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
import com.ptc.pfc.pfcModel.Import2DInstructions;
import com.ptc.pfc.pfcModel.ModelType;
import com.ptc.pfc.pfcSession.BaseSession;
import com.ptc.pfc.pfcSession.Session;

/**
 * BaseSession.Import2DModel &mdash; pfcSession.
 *
 * <pre>
 * Model Import2DModel(String, ModelType, String, Import2DInstructions) throws jxthrowable
 * </pre>
 */
public final class BaseSessionImport2DModelCommand implements Command {

    @Override public String name() { return "BaseSession.Import2DModel"; }
    @Override public String jlinkPackage() { return "pfcSession"; }
    @Override public String receiverType() { return "BaseSession"; }
    @Override public String signature() { return "Model Import2DModel(String, ModelType, String, Import2DInstructions) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("BaseSession.Import2DModel \u2014 pfcSession")
                .optional("value1", JsonSchema.string(),
                        "String value.")
                .optional("modelType", JsonSchema.enumOf("ModelType", "MDL_ASSEMBLY", "MDL_PART", "MDL_DRAWING", "MDL_2D_SECTION", "MDL_LAYOUT", "MDL_DWG_FORMAT", "MDL_MFG", "MDL_REPORT", "MDL_MARKUP", "MDL_DIAGRAM", "MDL_UNSPECIFIED", "MDL_CE_SOLID", "MDL_CE_DRAWING"),
                        "One of the ModelType constants.")
                .optional("value2", JsonSchema.string(),
                        "String value.")
                .optional("import2DInstructions", JsonSchema.dataObject("Import2DInstructions"),
                        "Import2DInstructions options object; its fields are passed to the pfc factory and setters.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Session target = ctx.session();
        String value1 = Marshal.in(ctx, params.get("value1"),
                "String", String.class, "value1");
        ModelType modelType = Marshal.in(ctx, params.get("modelType"),
                "ModelType", ModelType.class, "modelType");
        String value2 = Marshal.in(ctx, params.get("value2"),
                "String", String.class, "value2");
        Import2DInstructions import2DInstructions = Marshal.in(ctx, params.get("import2DInstructions"),
                "Import2DInstructions", Import2DInstructions.class, "import2DInstructions");
        return Marshal.result(ctx, target.Import2DModel(value1, modelType, value2, import2DInstructions), "Model");
    }
}
