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
import com.ptc.pfc.pfcSession.Session;
import com.ptc.pfc.pfcUI.FileSaveOptions;

/**
 * Session.UISaveFile &mdash; pfcSession.
 *
 * <pre>
 * String UISaveFile(FileSaveOptions) throws jxthrowable
 * </pre>
 */
public final class SessionUISaveFileCommand implements Command {

    @Override public String name() { return "Session.UISaveFile"; }
    @Override public String jlinkPackage() { return "pfcSession"; }
    @Override public String receiverType() { return "Session"; }
    @Override public String signature() { return "String UISaveFile(FileSaveOptions) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Session.UISaveFile \u2014 pfcSession")
                .optional("fileSaveOptions", JsonSchema.dataObject("FileSaveOptions"),
                        "FileSaveOptions options object; its fields are passed to the pfc factory and setters.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Session target = ctx.session();
        FileSaveOptions fileSaveOptions = Marshal.in(ctx, params.get("fileSaveOptions"),
                "FileSaveOptions", FileSaveOptions.class, "fileSaveOptions");
        return Marshal.result(ctx, target.UISaveFile(fileSaveOptions), "String");
    }
}
