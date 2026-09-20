package com.asyncjlink.commands.compositecommands.lifecycle;

import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModel.ModelDescriptor;
import com.ptc.pfc.pfcModel.pfcModel;

/**
 * Model.CreateBackup — snapshot a model before mutating it.
 *
 * <p>There is no undo anywhere in J-Link: no {@code Undo} call appears in the whole 937-command
 * catalogue. A backup taken beforehand is therefore the <em>only</em> rollback that exists, which
 * makes this a precondition for trusting automated mutation rather than a nicety.
 *
 * <p>Named {@code CreateBackup} rather than {@code Backup} because {@code Model.Backup} is already a
 * raw J-Link command and composites must never shadow one.
 */
public final class ModelCreateBackupCommand extends Composite {

    @Override public String name() { return "Model.CreateBackup"; }
    @Override public String receiverType() { return "Model"; }
    @Override public String signature() { return "CreateBackup(target[, directory, name]) — composite"; }

    @Override
    public String description() {
        return "Write a backup copy of a model before changing it. There is no undo in J-Link, so "
                + "this is the only rollback available — call it before any destructive command.";
    }

    @Override
    public JsonSchema paramSchema() {
        return targeted(name(), "Model", "The model to back up.")
                .optional("directory", JsonSchema.string(),
                        "Where to write it. Defaults to the configured output directory, then the "
                                + "session's working directory.")
                .optional("name", JsonSchema.string(),
                        "Base name for the backup. Defaults to the model's own name.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Model model = requireModel(ctx, params);

        String directory = params.getString("directory", null);
        if (directory == null || directory.isEmpty()) {
            directory = ctx.resolveOutputPath("");
        }
        if (directory == null || directory.isEmpty()) {
            directory = ctx.session().GetCurrentDirectory();
        }

        String baseName = params.getString("name", null);
        ModelDescriptor descr;
        if (baseName == null || baseName.isEmpty()) {
            descr = model.GetDescr();
        } else {
            descr = pfcModel.ModelDescriptor_CreateFromFileName(withExtension(model, baseName));
        }
        if (descr == null) {
            throw new CommandException("Could not describe the model to back up", "creo_error");
        }
        if (directory != null && !directory.isEmpty()) {
            descr.SetPath(directory);
        }

        try {
            model.Backup(descr);
        } catch (jxthrowable e) {
            throw new CommandException(
                    "Creo could not back up the model: " + rootMessage(e)
                            + ". Check that '" + directory + "' exists and is writable.",
                    "creo_error", e);
        }

        return JsonObject.of(
                "model", modelRef(ctx, model),
                "backedUp", Boolean.TRUE,
                "directory", directory,
                "fileName", descr.GetFileName());
    }

    /** A descriptor built from a bare name needs the model's own extension to be meaningful. */
    private static String withExtension(Model model, String baseName) throws jxthrowable {
        if (baseName.indexOf('.') >= 0) {
            return baseName;
        }
        String full = model.GetFullName();
        String existing = model.GetDescr() == null ? null : model.GetDescr().GetFileName();
        String source = existing != null ? existing : full;
        int dot = source == null ? -1 : source.lastIndexOf('.');
        return dot < 0 ? baseName : baseName + source.substring(dot);
    }
}
