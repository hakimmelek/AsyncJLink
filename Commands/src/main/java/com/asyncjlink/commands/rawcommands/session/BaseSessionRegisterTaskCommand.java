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
import com.ptc.pfc.pfcJLink.JLinkTaskListener;
import com.ptc.pfc.pfcSession.BaseSession;
import com.ptc.pfc.pfcSession.Session;

/**
 * BaseSession.RegisterTask &mdash; pfcSession.
 *
 * <pre>
 * void RegisterTask(String, JLinkTaskListener) throws jxthrowable
 * </pre>
 *
 * <p>Catalogued but not invocable: it requires a live JLinkTaskListener callback.
 */
public final class BaseSessionRegisterTaskCommand implements Command {

    @Override public String name() { return "BaseSession.RegisterTask"; }
    @Override public String jlinkPackage() { return "pfcSession"; }
    @Override public String receiverType() { return "BaseSession"; }
    @Override public String signature() { return "void RegisterTask(String, JLinkTaskListener) throws jxthrowable"; }

    @Override public boolean isInvocable() { return false; }
    @Override public String unsupportedReason() { return "it takes a JLinkTaskListener callback, which has no JSON representation; drive it from an in-process J-Link application instead"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("BaseSession.RegisterTask \u2014 pfcSession")
                .optional("value", JsonSchema.string(),
                        "String value.")
                .optional("jLinkTaskListener", JsonSchema.dataObject("JLinkTaskListener"),
                        "JLinkTaskListener callback (not supplyable over JSON).")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        throw CommandException.unsupported(
                "BaseSession.RegisterTask", unsupportedReason());
    }
}
