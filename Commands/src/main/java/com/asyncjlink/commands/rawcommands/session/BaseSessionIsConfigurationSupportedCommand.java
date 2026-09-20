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
import com.ptc.pfc.pfcExport.AssemblyConfiguration;
import com.ptc.pfc.pfcModel.ExportType;
import com.ptc.pfc.pfcSession.BaseSession;
import com.ptc.pfc.pfcSession.Session;

/**
 * BaseSession.IsConfigurationSupported &mdash; pfcSession.
 *
 * <pre>
 * boolean IsConfigurationSupported(ExportType, AssemblyConfiguration) throws jxthrowable
 * </pre>
 */
public final class BaseSessionIsConfigurationSupportedCommand implements Command {

    @Override public String name() { return "BaseSession.IsConfigurationSupported"; }
    @Override public String jlinkPackage() { return "pfcSession"; }
    @Override public String receiverType() { return "BaseSession"; }
    @Override public String signature() { return "boolean IsConfigurationSupported(ExportType, AssemblyConfiguration) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("BaseSession.IsConfigurationSupported \u2014 pfcSession")
                .optional("exportType", JsonSchema.enumOf("ExportType", "EXPORT_RELATION", "EXPORT_MODEL_INFO", "EXPORT_PROGRAM", "EXPORT_IGES", "EXPORT_DXF", "EXPORT_RENDER", "EXPORT_STL_ASCII", "EXPORT_STL_BINARY", "EXPORT_BOM", "EXPORT_DWG_SETUP", "EXPORT_FEAT_INFO", "EXPORT_MFG_OPER_CL", "EXPORT_MFG_FEAT_CL", "EXPORT_MATERIAL", "EXPORT_IGES_3D", "EXPORT_STEP", "EXPORT_VDA", "EXPORT_CGM", "EXPORT_INVENTOR", "EXPORT_FIAT", "EXPORT_CONNECTOR_PARAMS", "EXPORT_CABLE_PARAMS", "EXPORT_CATIAFACETS", "EXPORT_PLOT", "EXPORT_VRML", "EXPORT_CATIA_MODEL", "EXPORT_ACIS", "EXPORT_CATIA_SESSION", "EXPORT_CADDS", "EXPORT_STEP_2D", "EXPORT_MEDUSA", "EXPORT_NEUTRAL", "EXPORT_PRODUCTVIEW", "EXPORT_PDF", "EXPORT_JT", "EXPORT_UG", "EXPORT_CATIA_PART", "EXPORT_CATIA_PRODUCT", "EXPORT_CATIA_CGR", "EXPORT_PARASOLID", "EXPORT_PRINT", "EXPORT_INTF_DXF", "EXPORT_INTF_DWG", "EXPORT_INTF_SW_PART", "EXPORT_INTF_SW_ASSEM"),
                        "One of the ExportType constants.")
                .optional("assemblyConfiguration", JsonSchema.enumOf("AssemblyConfiguration", "EXPORT_ASM_FLAT_FILE", "EXPORT_ASM_SINGLE_FILE", "EXPORT_ASM_MULTI_FILES", "EXPORT_ASM_ASSEMBLY_PARTS"),
                        "One of the AssemblyConfiguration constants.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Session target = ctx.session();
        ExportType exportType = Marshal.in(ctx, params.get("exportType"),
                "ExportType", ExportType.class, "exportType");
        AssemblyConfiguration assemblyConfiguration = Marshal.in(ctx, params.get("assemblyConfiguration"),
                "AssemblyConfiguration", AssemblyConfiguration.class, "assemblyConfiguration");
        return Marshal.result(ctx, target.IsConfigurationSupported(exportType, assemblyConfiguration), "boolean");
    }
}
