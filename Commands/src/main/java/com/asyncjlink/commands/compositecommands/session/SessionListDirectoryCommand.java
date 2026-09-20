package com.asyncjlink.commands.compositecommands.session;

import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonArray;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.cipjava.stringseq;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModel.Models;
import com.ptc.pfc.pfcSession.FileListOpt;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Session.ListDirectory — what models exist on disk.
 *
 * <p>Distinct from {@code BaseSession.ListModels}, which only sees what is already in session. The
 * two are constantly confused and the difference matters: a model on disk cannot be acted on until
 * it has been retrieved, so each entry says whether it is loaded.
 */
public final class SessionListDirectoryCommand extends Composite {

    @Override public String name() { return "Session.ListDirectory"; }
    @Override public String receiverType() { return "Session"; }
    @Override public String signature() { return "ListDirectory([path, filter]) — composite"; }

    @Override
    public String description() {
        return "List Creo model files on disk in a directory, each flagged with whether it is "
                + "already loaded in session. Use this to find a model before Session.LoadModel.";
    }

    @Override
    public JsonSchema paramSchema() {
        return sessionScoped(name())
                .optional("path", JsonSchema.string(),
                        "Directory to list. Defaults to the session's working directory.")
                .optional("filter", JsonSchema.string(),
                        "File name filter, e.g. \"*.prt\". Defaults to every model type.")
                .optional("includeSubdirectories", JsonSchema.bool(),
                        "Also list subdirectory names. Default false.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        String dir = params.getString("path", null);
        if (dir == null || dir.isEmpty()) {
            dir = ctx.session().GetCurrentDirectory();
        }
        String filter = params.getString("filter", "*");

        Set<String> inSession = new HashSet<>();
        Models loaded = ctx.session().ListModels();
        if (loaded != null) {
            for (int i = 0; i < loaded.getarraysize(); i++) {
                Model m = loaded.get(i);
                if (m != null) {
                    String n = m.GetFullName();
                    if (n != null) {
                        inSession.add(n.toUpperCase(java.util.Locale.ROOT));
                    }
                }
            }
        }

        JsonObject out = JsonObject.of("directory", dir);
        JsonArray files = new JsonArray();
        stringseq listed = ctx.session().ListFiles(filter, FileListOpt.FILE_LIST_LATEST, dir);
        for (String path : strings(listed)) {
            JsonObject entry = JsonObject.of("path", path);
            String base = baseName(path);
            entry.put("name", base);
            entry.put("loaded", Boolean.valueOf(inSession.contains(stripVersion(base))));
            files.add(entry);
        }
        out.put("fileCount", Integer.valueOf(files.size()));
        out.put("files", files);

        if (params.getBoolean("includeSubdirectories", false)) {
            List<String> subs = strings(ctx.session().ListSubdirectories(dir));
            out.put("subdirectories", toJsonArray(subs));
        }
        return out;
    }

    private static String baseName(String path) {
        if (path == null) {
            return null;
        }
        int cut = Math.max(path.lastIndexOf('\\'), path.lastIndexOf('/'));
        return cut < 0 ? path : path.substring(cut + 1);
    }

    /** Creo file names on disk carry a version suffix (bracket.prt.3) that a model name does not. */
    private static String stripVersion(String fileName) {
        if (fileName == null) {
            return "";
        }
        String s = fileName;
        int last = s.lastIndexOf('.');
        if (last > 0 && isDigits(s.substring(last + 1))) {
            s = s.substring(0, last);
        }
        return s.toUpperCase(java.util.Locale.ROOT);
    }

    private static boolean isDigits(String s) {
        if (s.isEmpty()) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
