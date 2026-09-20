/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.ui;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcUI.Popupmenu;
import com.ptc.pfc.pfcUI.PopupmenuOptions;

/**
 * Popupmenu.AddMenu &mdash; pfcUI.
 *
 * <pre>
 * Popupmenu AddMenu(PopupmenuOptions) throws jxthrowable
 * </pre>
 */
public final class PopupmenuAddMenuCommand implements Command {

    @Override public String name() { return "Popupmenu.AddMenu"; }
    @Override public String jlinkPackage() { return "pfcUI"; }
    @Override public String receiverType() { return "Popupmenu"; }
    @Override public String signature() { return "Popupmenu AddMenu(PopupmenuOptions) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Popupmenu.AddMenu \u2014 pfcUI")
                .required("target", JsonSchema.handle("Popupmenu"),
                        "The Popupmenu to act on.")
                .optional("popupmenuOptions", JsonSchema.dataObject("PopupmenuOptions"),
                        "PopupmenuOptions options object; its fields are passed to the pfc factory and setters.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Popupmenu target = Marshal.in(ctx, params.get("target"),
                "Popupmenu", Popupmenu.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        PopupmenuOptions popupmenuOptions = Marshal.in(ctx, params.get("popupmenuOptions"),
                "PopupmenuOptions", PopupmenuOptions.class, "popupmenuOptions");
        return Marshal.result(ctx, target.AddMenu(popupmenuOptions), "Popupmenu");
    }
}
