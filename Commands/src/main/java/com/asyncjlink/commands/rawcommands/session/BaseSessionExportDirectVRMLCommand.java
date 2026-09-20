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
import com.ptc.pfc.pfcModel.VRMLDirectExportInstructions;
import com.ptc.pfc.pfcSession.BaseSession;
import com.ptc.pfc.pfcSession.Session;

/**
 * BaseSession.ExportDirectVRML &mdash; pfcSession.
 *
 * <pre>
 * void ExportDirectVRML(VRMLDirectExportInstructions) throws jxthrowable
 * </pre>
 */
public final class BaseSessionExportDirectVRMLCommand implements Command {

    @Override public String name() { return "BaseSession.ExportDirectVRML"; }
    @Override public String jlinkPackage() { return "pfcSession"; }
    @Override public String receiverType() { return "BaseSession"; }
    @Override public String signature() { return "void ExportDirectVRML(VRMLDirectExportInstructions) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("BaseSession.ExportDirectVRML \u2014 pfcSession")
                .optional("vrmlDirectExportInstructions", JsonSchema.dataObject("VRMLDirectExportInstructions"),
                        "VRMLDirectExportInstructions options object; its fields are passed to the pfc factory and setters.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Session target = ctx.session();
        VRMLDirectExportInstructions vrmlDirectExportInstructions = Marshal.in(ctx, params.get("vrmlDirectExportInstructions"),
                "VRMLDirectExportInstructions", VRMLDirectExportInstructions.class, "vrmlDirectExportInstructions");
        target.ExportDirectVRML(vrmlDirectExportInstructions);
        return Marshal.ok();
    }
}
