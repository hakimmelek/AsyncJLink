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
import com.ptc.pfc.pfcSelect.SelectionOptions;
import com.ptc.pfc.pfcSelect.Selections;
import com.ptc.pfc.pfcSession.BaseSession;
import com.ptc.pfc.pfcSession.Session;

/**
 * BaseSession.Select &mdash; pfcSession.
 *
 * <pre>
 * Selections Select(SelectionOptions, Selections) throws jxthrowable
 * </pre>
 */
public final class BaseSessionSelectCommand implements Command {

    @Override public String name() { return "BaseSession.Select"; }
    @Override public String jlinkPackage() { return "pfcSession"; }
    @Override public String receiverType() { return "BaseSession"; }
    @Override public String signature() { return "Selections Select(SelectionOptions, Selections) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("BaseSession.Select \u2014 pfcSession")
                .optional("selectionOptions", JsonSchema.dataObject("SelectionOptions"),
                        "SelectionOptions options object; its fields are passed to the pfc factory and setters.")
                .optional("selections", JsonSchema.sequence("Selections", JsonSchema.handle("Selection")),
                        "Array of Selection.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Session target = ctx.session();
        SelectionOptions selectionOptions = Marshal.in(ctx, params.get("selectionOptions"),
                "SelectionOptions", SelectionOptions.class, "selectionOptions");
        Selections selections = Marshal.in(ctx, params.get("selections"),
                "Selections", Selections.class, "selections");
        return Marshal.result(ctx, target.Select(selectionOptions, selections), "Selections");
    }
}
