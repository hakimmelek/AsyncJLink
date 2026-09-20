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
import com.ptc.pfc.pfcSession.BaseSession;
import com.ptc.pfc.pfcSession.Session;
import com.ptc.pfc.pfcUI.FileOpenRegisterListener;
import com.ptc.pfc.pfcUI.FileOpenRegisterOptions;

/**
 * BaseSession.UIRegisterFileOpen &mdash; pfcSession.
 *
 * <pre>
 * void UIRegisterFileOpen(FileOpenRegisterOptions, FileOpenRegisterListener) throws jxthrowable
 * </pre>
 *
 * <p>Catalogued but not invocable: it requires a live FileOpenRegisterListener callback.
 */
public final class BaseSessionUIRegisterFileOpenCommand implements Command {

    @Override public String name() { return "BaseSession.UIRegisterFileOpen"; }
    @Override public String jlinkPackage() { return "pfcSession"; }
    @Override public String receiverType() { return "BaseSession"; }
    @Override public String signature() { return "void UIRegisterFileOpen(FileOpenRegisterOptions, FileOpenRegisterListener) throws jxthrowable"; }

    @Override public boolean isInvocable() { return false; }
    @Override public String unsupportedReason() { return "it takes a FileOpenRegisterListener callback, which has no JSON representation; drive it from an in-process J-Link application instead"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("BaseSession.UIRegisterFileOpen \u2014 pfcSession")
                .optional("fileOpenRegisterOptions", JsonSchema.dataObject("FileOpenRegisterOptions"),
                        "FileOpenRegisterOptions options object; its fields are passed to the pfc factory and setters.")
                .optional("fileOpenRegisterListener", JsonSchema.dataObject("FileOpenRegisterListener"),
                        "FileOpenRegisterListener callback (not supplyable over JSON).")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        throw CommandException.unsupported(
                "BaseSession.UIRegisterFileOpen", unsupportedReason());
    }
}
