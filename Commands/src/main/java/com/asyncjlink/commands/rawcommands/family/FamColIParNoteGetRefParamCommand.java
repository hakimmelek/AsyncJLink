/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.family;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcFamily.FamColIParNote;

/**
 * FamColIParNote.GetRefParam &mdash; pfcFamily.
 *
 * <pre>
 * Parameter GetRefParam() throws jxthrowable
 * </pre>
 */
public final class FamColIParNoteGetRefParamCommand implements Command {

    @Override public String name() { return "FamColIParNote.GetRefParam"; }
    @Override public String jlinkPackage() { return "pfcFamily"; }
    @Override public String receiverType() { return "FamColIParNote"; }
    @Override public String signature() { return "Parameter GetRefParam() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("FamColIParNote.GetRefParam \u2014 pfcFamily")
                .required("target", JsonSchema.handle("FamColIParNote"),
                        "The FamColIParNote to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        FamColIParNote target = Marshal.in(ctx, params.get("target"),
                "FamColIParNote", FamColIParNote.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetRefParam(), "Parameter");
    }
}
