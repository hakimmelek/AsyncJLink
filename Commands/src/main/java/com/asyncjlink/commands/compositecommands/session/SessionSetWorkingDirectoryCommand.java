package com.asyncjlink.commands.compositecommands.session;

import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcSession.FileListOpt;

import java.util.List;

/**
 * Session.SetWorkingDirectory — change directory, and say what is now in scope.
 *
 * <p>The raw call tells the caller nothing about whether the move worked or what is there, which is
 * the whole question being asked.
 */
public final class SessionSetWorkingDirectoryCommand extends Composite {

    @Override public String name() { return "Session.SetWorkingDirectory"; }
    @Override public String receiverType() { return "Session"; }
    @Override public String signature() { return "SetWorkingDirectory(path) — composite"; }

    @Override
    public String description() {
        return "Change the session's working directory and report the models now visible from it.";
    }

    @Override
    public JsonSchema paramSchema() {
        return sessionScoped(name())
                .required("path", JsonSchema.string(), "Directory to change to.")
                .optional("listModels", JsonSchema.bool(),
                        "List the model files in the new directory. Default true.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        String path = params.getString("path", null);
        if (path == null || path.isEmpty()) {
            throw new CommandException("Field 'path' is required", "invalid_params");
        }

        String before = ctx.session().GetCurrentDirectory();
        try {
            ctx.session().ChangeDirectory(path);
        } catch (jxthrowable e) {
            throw new CommandException(
                    "Could not change to '" + path + "': " + rootMessage(e)
                            + ". Check that the directory exists and is readable.",
                    "creo_error", e);
        }
        String after = ctx.session().GetCurrentDirectory();

        JsonObject out = JsonObject.of(
                "previousDirectory", before,
                "workingDirectory", after);

        if (params.getBoolean("listModels", true)) {
            List<String> files = strings(
                    ctx.session().ListFiles("*", FileListOpt.FILE_LIST_LATEST, after));
            out.put("fileCount", Integer.valueOf(files.size()));
            out.put("files", toJsonArray(files));
        }
        return out;
    }
}
