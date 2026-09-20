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
import com.ptc.pfc.pfcCommand.UICommandActionListener;
import com.ptc.pfc.pfcSession.Session;

/**
 * Session.UICreateCommand &mdash; pfcSession.
 *
 * <pre>
 * UICommand UICreateCommand(String, UICommandActionListener) throws jxthrowable
 * </pre>
 *
 * <p>Catalogued but not invocable: it requires a live UICommandActionListener callback.
 */
public final class SessionUICreateCommandCommand implements Command {

    @Override public String name() { return "Session.UICreateCommand"; }
    @Override public String jlinkPackage() { return "pfcSession"; }
    @Override public String receiverType() { return "Session"; }
    @Override public String signature() { return "UICommand UICreateCommand(String, UICommandActionListener) throws jxthrowable"; }

    @Override public boolean isInvocable() { return false; }
    @Override public String unsupportedReason() { return "it takes a UICommandActionListener callback, which has no JSON representation; drive it from an in-process J-Link application instead"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Session.UICreateCommand \u2014 pfcSession")
                .optional("value", JsonSchema.string(),
                        "String value.")
                .optional("uiCommandActionListener", JsonSchema.dataObject("UICommandActionListener"),
                        "UICommandActionListener callback (not supplyable over JSON).")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        throw CommandException.unsupported(
                "Session.UICreateCommand", unsupportedReason());
    }
}
