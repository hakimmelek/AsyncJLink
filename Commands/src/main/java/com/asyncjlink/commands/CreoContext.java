package com.asyncjlink.commands;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModel.ModelType;
import com.ptc.pfc.pfcSession.Session;

/**
 * Everything a {@link Command} needs: the live Creo {@link Session}, the user's registered paths, and
 * the {@link HandleStore} naming objects that cross the JSON boundary.
 *
 * <p>One instance per connection. {@code Cli} builds one per invocation; {@code Mcp} builds one and
 * keeps it for the AI session. There is no shared daemon — each front door owns its connection.
 */
public final class CreoContext {

    private final Session session;
    private final CreoPaths paths;
    private final HandleStore handles = new HandleStore();

    public CreoContext(Session session, CreoPaths paths) {
        if (session == null) {
            throw new IllegalArgumentException("session is required");
        }
        this.session = session;
        this.paths = paths;
    }

    public Session session() {
        return session;
    }

    public CreoPaths paths() {
        return paths;
    }

    public HandleStore handles() {
        return handles;
    }

    /**
     * Resolves a model reference that came in as a JSON string.
     *
     * <p>Tried in order, so that the common case stays ergonomic without making handles ambiguous:
     * <ol>
     *   <li>an existing handle, e.g. {@code Part@3};
     *   <li>a full file name, e.g. {@code bracket_01.prt} — {@code Session.GetModelFromFileName};
     *   <li>a bare name, e.g. {@code BRACKET_01} — tried against part, assembly then drawing.
     * </ol>
     *
     * @return the model, or {@code null} when nothing matches
     */
    public Model resolveModel(String reference) throws jxthrowable {
        if (reference == null || reference.isEmpty()) {
            return null;
        }
        Object handle = handles.lookup(reference);
        if (handle instanceof Model) {
            return (Model) handle;
        }
        if (reference.indexOf('.') >= 0) {
            Model m = session.GetModelFromFileName(reference);
            if (m != null) {
                return m;
            }
        }
        for (ModelType type : new ModelType[] {
                ModelType.MDL_PART, ModelType.MDL_ASSEMBLY, ModelType.MDL_DRAWING }) {
            Model m = session.GetModel(reference, type);
            if (m != null) {
                return m;
            }
        }
        return null;
    }

    /**
     * Resolves an export path: absolute paths are used as-is, relative ones land in the configured
     * output dir. The resolved path's parent directory is created if it does not exist yet, so a
     * fresh install's unconfigured or not-yet-created output directory fails with a clear message
     * here rather than as an opaque native write error from Creo.
     */
    public String resolveOutputPath(String path) {
        if (path == null || path.isEmpty()) {
            return path;
        }
        String resolved;
        if (isAbsolute(path)) {
            resolved = path;
        } else {
            String dir = paths == null ? null : paths.defaultOutputDir();
            if (dir == null || dir.isEmpty()) {
                resolved = path;
            } else {
                String sep = dir.endsWith("\\") || dir.endsWith("/") ? "" : "\\";
                resolved = dir + sep + path;
            }
        }
        ensureParentDirectory(resolved);
        return resolved;
    }

    private static void ensureParentDirectory(String path) {
        java.io.File parent = new java.io.File(path).getParentFile();
        if (parent == null || parent.exists()) {
            return;
        }
        if (!parent.mkdirs() && !parent.exists()) {
            throw new CommandException(
                    "Output directory '" + parent + "' does not exist and could not be created",
                    "invalid_params");
        }
    }

    private static boolean isAbsolute(String p) {
        return p.startsWith("\\") || p.startsWith("/")
                || (p.length() > 2 && p.charAt(1) == ':');
    }
}
