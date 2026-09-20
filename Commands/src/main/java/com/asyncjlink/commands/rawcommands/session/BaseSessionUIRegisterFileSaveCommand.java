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
import com.ptc.pfc.pfcUI.FileSaveRegisterListener;
import com.ptc.pfc.pfcUI.FileSaveRegisterOptions;

/**
 * BaseSession.UIRegisterFileSave &mdash; pfcSession.
 *
 * <pre>
 * void UIRegisterFileSave(FileSaveRegisterOptions, FileSaveRegisterListener) throws jxthrowable
 * </pre>
 *
 * <p>Catalogued but not invocable: it requires a live FileSaveRegisterListener callback.
 */
public final class BaseSessionUIRegisterFileSaveCommand implements Command {

    @Override public String name() { return "BaseSession.UIRegisterFileSave"; }
    @Override public String jlinkPackage() { return "pfcSession"; }
    @Override public String receiverType() { return "BaseSession"; }
    @Override public String signature() { return "void UIRegisterFileSave(FileSaveRegisterOptions, FileSaveRegisterListener) throws jxthrowable"; }

    @Override public boolean isInvocable() { return false; }
    @Override public String unsupportedReason() { return "it takes a FileSaveRegisterListener callback, which has no JSON representation; drive it from an in-process J-Link application instead"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("BaseSession.UIRegisterFileSave \u2014 pfcSession")
                .optional("fileSaveRegisterOptions", JsonSchema.dataObject("FileSaveRegisterOptions"),
                        "FileSaveRegisterOptions options object; its fields are passed to the pfc factory and setters.")
                .optional("fileSaveRegisterListener", JsonSchema.dataObject("FileSaveRegisterListener"),
                        "FileSaveRegisterListener callback (not supplyable over JSON).")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        throw CommandException.unsupported(
                "BaseSession.UIRegisterFileSave", unsupportedReason());
    }
}
