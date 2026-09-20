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
import com.ptc.pfc.pfcUI.DirectorySelectionOptions;

/**
 * Session.UISelectDirectory &mdash; pfcSession.
 *
 * <pre>
 * String UISelectDirectory(DirectorySelectionOptions) throws jxthrowable
 * </pre>
 */
public final class SessionUISelectDirectoryCommand implements Command {

    @Override public String name() { return "Session.UISelectDirectory"; }
    @Override public String jlinkPackage() { return "pfcSession"; }
    @Override public String receiverType() { return "Session"; }
    @Override public String signature() { return "String UISelectDirectory(DirectorySelectionOptions) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Session.UISelectDirectory \u2014 pfcSession")
                .optional("directorySelectionOptions", JsonSchema.dataObject("DirectorySelectionOptions"),
                        "DirectorySelectionOptions options object; its fields are passed to the pfc factory and setters.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Session target = ctx.session();
        DirectorySelectionOptions directorySelectionOptions = Marshal.in(ctx, params.get("directorySelectionOptions"),
                "DirectorySelectionOptions", DirectorySelectionOptions.class, "directorySelectionOptions");
        return Marshal.result(ctx, target.UISelectDirectory(directorySelectionOptions), "String");
    }
}
