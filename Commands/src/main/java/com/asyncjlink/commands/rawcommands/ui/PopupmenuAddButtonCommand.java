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
import com.ptc.pfc.pfcCommand.UICommand;
import com.ptc.pfc.pfcUI.Popupmenu;
import com.ptc.pfc.pfcUI.PopupmenuOptions;

/**
 * Popupmenu.AddButton &mdash; pfcUI.
 *
 * <pre>
 * void AddButton(UICommand, PopupmenuOptions) throws jxthrowable
 * </pre>
 */
public final class PopupmenuAddButtonCommand implements Command {

    @Override public String name() { return "Popupmenu.AddButton"; }
    @Override public String jlinkPackage() { return "pfcUI"; }
    @Override public String receiverType() { return "Popupmenu"; }
    @Override public String signature() { return "void AddButton(UICommand, PopupmenuOptions) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Popupmenu.AddButton \u2014 pfcUI")
                .required("target", JsonSchema.handle("Popupmenu"),
                        "The Popupmenu to act on.")
                .optional("uiCommand", JsonSchema.handle("UICommand"),
                        "Handle to a UICommand, as returned by an earlier command.")
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
        UICommand uiCommand = Marshal.in(ctx, params.get("uiCommand"),
                "UICommand", UICommand.class, "uiCommand");
        PopupmenuOptions popupmenuOptions = Marshal.in(ctx, params.get("popupmenuOptions"),
                "PopupmenuOptions", PopupmenuOptions.class, "popupmenuOptions");
        target.AddButton(uiCommand, popupmenuOptions);
        return Marshal.ok();
    }
}
