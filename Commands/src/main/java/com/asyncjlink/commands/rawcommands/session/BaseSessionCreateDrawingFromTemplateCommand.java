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
import com.ptc.pfc.pfcDrawing.DrawingCreateOptions;
import com.ptc.pfc.pfcModel.ModelDescriptor;
import com.ptc.pfc.pfcSession.BaseSession;
import com.ptc.pfc.pfcSession.Session;

/**
 * BaseSession.CreateDrawingFromTemplate &mdash; pfcSession.
 *
 * <pre>
 * Drawing CreateDrawingFromTemplate(String, String, ModelDescriptor, DrawingCreateOptions) throws jxthrowable
 * </pre>
 */
public final class BaseSessionCreateDrawingFromTemplateCommand implements Command {

    @Override public String name() { return "BaseSession.CreateDrawingFromTemplate"; }
    @Override public String jlinkPackage() { return "pfcSession"; }
    @Override public String receiverType() { return "BaseSession"; }
    @Override public String signature() { return "Drawing CreateDrawingFromTemplate(String, String, ModelDescriptor, DrawingCreateOptions) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("BaseSession.CreateDrawingFromTemplate \u2014 pfcSession")
                .optional("value1", JsonSchema.string(),
                        "String value.")
                .optional("value2", JsonSchema.string(),
                        "String value.")
                .optional("modelDescriptor", JsonSchema.dataObject("ModelDescriptor"),
                        "ModelDescriptor options object; its fields are passed to the pfc factory and setters.")
                .optional("drawingCreateOptions", JsonSchema.sequence("DrawingCreateOptions", JsonSchema.enumOf("DrawingCreateOption", "DRAWINGCREATE_DISPLAY_DRAWING", "DRAWINGCREATE_SHOW_ERROR_DIALOG", "DRAWINGCREATE_WRITE_ERROR_FILE", "DRAWINGCREATE_PROMPT_UNKNOWN_PARAMS")),
                        "Array of DrawingCreateOption.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Session target = ctx.session();
        String value1 = Marshal.in(ctx, params.get("value1"),
                "String", String.class, "value1");
        String value2 = Marshal.in(ctx, params.get("value2"),
                "String", String.class, "value2");
        ModelDescriptor modelDescriptor = Marshal.in(ctx, params.get("modelDescriptor"),
                "ModelDescriptor", ModelDescriptor.class, "modelDescriptor");
        DrawingCreateOptions drawingCreateOptions = Marshal.in(ctx, params.get("drawingCreateOptions"),
                "DrawingCreateOptions", DrawingCreateOptions.class, "drawingCreateOptions");
        return Marshal.result(ctx, target.CreateDrawingFromTemplate(value1, value2, modelDescriptor, drawingCreateOptions), "Drawing");
    }
}
