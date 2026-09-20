/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.view;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcView.ViewOwner;

/**
 * ViewOwner.ListViews &mdash; pfcView.
 *
 * <pre>
 * Views ListViews() throws jxthrowable
 * </pre>
 */
public final class ViewOwnerListViewsCommand implements Command {

    @Override public String name() { return "ViewOwner.ListViews"; }
    @Override public String jlinkPackage() { return "pfcView"; }
    @Override public String receiverType() { return "ViewOwner"; }
    @Override public String signature() { return "Views ListViews() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("ViewOwner.ListViews \u2014 pfcView")
                .required("target", JsonSchema.handle("ViewOwner"),
                        "The ViewOwner to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        ViewOwner target = Marshal.in(ctx, params.get("target"),
                "ViewOwner", ViewOwner.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.ListViews(), "Views");
    }
}
