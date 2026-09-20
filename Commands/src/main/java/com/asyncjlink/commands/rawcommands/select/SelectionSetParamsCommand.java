/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.select;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcBase.UVParams;
import com.ptc.pfc.pfcSelect.Selection;

/**
 * Selection.SetParams &mdash; pfcSelect.
 *
 * <pre>
 * void SetParams(UVParams) throws jxthrowable
 * </pre>
 */
public final class SelectionSetParamsCommand implements Command {

    @Override public String name() { return "Selection.SetParams"; }
    @Override public String jlinkPackage() { return "pfcSelect"; }
    @Override public String receiverType() { return "Selection"; }
    @Override public String signature() { return "void SetParams(UVParams) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Selection.SetParams \u2014 pfcSelect")
                .required("target", JsonSchema.handle("Selection"),
                        "The Selection to act on.")
                .optional("uvParams", JsonSchema.sequence("UVParams", JsonSchema.number()),
                        "Array of double.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Selection target = Marshal.in(ctx, params.get("target"),
                "Selection", Selection.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        UVParams uvParams = Marshal.in(ctx, params.get("uvParams"),
                "UVParams", UVParams.class, "uvParams");
        target.SetParams(uvParams);
        return Marshal.ok();
    }
}
