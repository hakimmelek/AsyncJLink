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
import com.ptc.pfc.pfcSession.BaseSession;
import com.ptc.pfc.pfcSession.FileListOpt;
import com.ptc.pfc.pfcSession.Session;

/**
 * BaseSession.ListFiles &mdash; pfcSession.
 *
 * <pre>
 * stringseq ListFiles(String, FileListOpt, String) throws jxthrowable
 * </pre>
 */
public final class BaseSessionListFilesCommand implements Command {

    @Override public String name() { return "BaseSession.ListFiles"; }
    @Override public String jlinkPackage() { return "pfcSession"; }
    @Override public String receiverType() { return "BaseSession"; }
    @Override public String signature() { return "stringseq ListFiles(String, FileListOpt, String) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("BaseSession.ListFiles \u2014 pfcSession")
                .optional("value1", JsonSchema.string(),
                        "String value.")
                .optional("fileListOpt", JsonSchema.enumOf("FileListOpt", "FILE_LIST_ALL", "FILE_LIST_LATEST", "FILE_LIST_ALL_INST", "FILE_LIST_LATEST_INST"),
                        "One of the FileListOpt constants.")
                .optional("value2", JsonSchema.string(),
                        "String value.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Session target = ctx.session();
        String value1 = Marshal.in(ctx, params.get("value1"),
                "String", String.class, "value1");
        if (value1 == null) {
            value1 = ""; // avoids crashing the async connection
        }
        FileListOpt fileListOpt = Marshal.in(ctx, params.get("fileListOpt"),
                "FileListOpt", FileListOpt.class, "fileListOpt");
        String value2 = Marshal.in(ctx, params.get("value2"),
                "String", String.class, "value2");
        if (value2 == null) {
            value2 = ""; // avoids crashing the async connection
        }
        return Marshal.result(ctx, target.ListFiles(value1, fileListOpt, value2), "stringseq");
    }
}
