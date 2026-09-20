/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.solid;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.cipjava.stringseq;
import com.ptc.pfc.pfcModelItem.ModelItem;
import com.ptc.pfc.pfcSolid.Solid;

/**
 * Solid.CreateNote &mdash; pfcSolid.
 *
 * <pre>
 * ModelItem CreateNote(stringseq, ModelItem) throws jxthrowable
 * </pre>
 */
public final class SolidCreateNoteCommand implements Command {

    @Override public String name() { return "Solid.CreateNote"; }
    @Override public String jlinkPackage() { return "pfcSolid"; }
    @Override public String receiverType() { return "Solid"; }
    @Override public String signature() { return "ModelItem CreateNote(stringseq, ModelItem) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Solid.CreateNote \u2014 pfcSolid")
                .required("target", JsonSchema.handle("Solid"),
                        "The Solid to act on.")
                .optional("values", JsonSchema.sequence("stringseq", JsonSchema.string()),
                        "Array of String.")
                .optional("modelItem", JsonSchema.handle("ModelItem"),
                        "Handle to a ModelItem, as returned by an earlier command.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Solid target = Marshal.in(ctx, params.get("target"),
                "Solid", Solid.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        stringseq values = Marshal.in(ctx, params.get("values"),
                "stringseq", stringseq.class, "values");
        ModelItem modelItem = Marshal.in(ctx, params.get("modelItem"),
                "ModelItem", ModelItem.class, "modelItem");
        return Marshal.result(ctx, target.CreateNote(values, modelItem), "ModelItem");
    }
}
