/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.note;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcNote.Note;

/**
 * Note.SetURL &mdash; pfcNote.
 *
 * <pre>
 * void SetURL(String) throws jxthrowable
 * </pre>
 */
public final class NoteSetURLCommand implements Command {

    @Override public String name() { return "Note.SetURL"; }
    @Override public String jlinkPackage() { return "pfcNote"; }
    @Override public String receiverType() { return "Note"; }
    @Override public String signature() { return "void SetURL(String) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Note.SetURL \u2014 pfcNote")
                .required("target", JsonSchema.handle("Note"),
                        "The Note to act on.")
                .optional("value", JsonSchema.string(),
                        "String value.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Note target = Marshal.in(ctx, params.get("target"),
                "Note", Note.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        String value = Marshal.in(ctx, params.get("value"),
                "String", String.class, "value");
        target.SetURL(value);
        return Marshal.ok();
    }
}
