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
import com.ptc.cipjava.stringseq;
import com.ptc.pfc.pfcSession.BaseSession;
import com.ptc.pfc.pfcSession.RelCriterion;
import com.ptc.pfc.pfcSession.Session;

/**
 * BaseSession.ImportToCurrentWS &mdash; pfcSession.
 *
 * <pre>
 * WSImportExportMessages ImportToCurrentWS(stringseq, RelCriterion) throws jxthrowable
 * </pre>
 */
public final class BaseSessionImportToCurrentWSCommand implements Command {

    @Override public String name() { return "BaseSession.ImportToCurrentWS"; }
    @Override public String jlinkPackage() { return "pfcSession"; }
    @Override public String receiverType() { return "BaseSession"; }
    @Override public String signature() { return "WSImportExportMessages ImportToCurrentWS(stringseq, RelCriterion) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("BaseSession.ImportToCurrentWS \u2014 pfcSession")
                .optional("values", JsonSchema.sequence("stringseq", JsonSchema.string()),
                        "Array of String.")
                .optional("relCriterion", JsonSchema.enumOf("RelCriterion", "FILE_INCLUDE_ALL", "FILE_INCLUDE_REQUIRED", "FILE_INCLUDE_NONE"),
                        "One of the RelCriterion constants.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Session target = ctx.session();
        stringseq values = Marshal.in(ctx, params.get("values"),
                "stringseq", stringseq.class, "values");
        RelCriterion relCriterion = Marshal.in(ctx, params.get("relCriterion"),
                "RelCriterion", RelCriterion.class, "relCriterion");
        return Marshal.result(ctx, target.ImportToCurrentWS(values, relCriterion), "WSImportExportMessages");
    }
}
