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
import com.ptc.pfc.pfcModel.ModelType;
import com.ptc.pfc.pfcSession.BaseSession;
import com.ptc.pfc.pfcSession.Session;

/**
 * BaseSession.ListModelsByType &mdash; pfcSession.
 *
 * <pre>
 * Models ListModelsByType(ModelType) throws jxthrowable
 * </pre>
 */
public final class BaseSessionListModelsByTypeCommand implements Command {

    @Override public String name() { return "BaseSession.ListModelsByType"; }
    @Override public String jlinkPackage() { return "pfcSession"; }
    @Override public String receiverType() { return "BaseSession"; }
    @Override public String signature() { return "Models ListModelsByType(ModelType) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("BaseSession.ListModelsByType \u2014 pfcSession")
                .optional("modelType", JsonSchema.enumOf("ModelType", "MDL_ASSEMBLY", "MDL_PART", "MDL_DRAWING", "MDL_2D_SECTION", "MDL_LAYOUT", "MDL_DWG_FORMAT", "MDL_MFG", "MDL_REPORT", "MDL_MARKUP", "MDL_DIAGRAM", "MDL_UNSPECIFIED", "MDL_CE_SOLID", "MDL_CE_DRAWING"),
                        "One of the ModelType constants.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Session target = ctx.session();
        ModelType modelType = Marshal.in(ctx, params.get("modelType"),
                "ModelType", ModelType.class, "modelType");
        return Marshal.result(ctx, target.ListModelsByType(modelType), "Models");
    }
}
