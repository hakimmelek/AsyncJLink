package com.asyncjlink.config;

import com.asyncjlink.commands.CreoPaths;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The user's {@code paths.yaml}, loaded into typed getters.
 *
 * <p>Nothing about the local installation is hardcoded: Creo's location, the async connection
 * settings and the default output directories are all registered by the end user in
 * {@code paths.yaml}, from the template in {@code Config/paths.example.yaml}.
 *
 * <p>Implements {@link CreoPaths}, which is declared in the {@code Commands} module. That is what lets
 * {@code Commands} stay the base of the dependency graph while still receiving configuration.
 */
public final class PathConfig implements CreoPaths {

    /** Set this system property or environment variable to point at a specific paths.yaml. */
    public static final String PROPERTY = "asyncjlink.config";
    public static final String ENV_VAR = "ASYNCJLINK_CONFIG";

    private final Map<String, Object> root;
    private final Path origin;

    private PathConfig(Map<String, Object> root, Path origin) {
        this.root = root;
        this.origin = origin;
    }

    // ---- loading ---------------------------------------------------------

    /** Loads from the first location in {@link #searchPath()} that exists. */
    public static PathConfig load() {
        for (Path candidate : searchPath()) {
            if (candidate != null && Files.isRegularFile(candidate)) {
                return load(candidate);
            }
        }
        StringBuilder sb = new StringBuilder(
                "No paths.yaml found. Copy Config/paths.example.yaml to one of these and fill it in:\n");
        for (Path candidate : searchPath()) {
            if (candidate != null) {
                sb.append("  - ").append(candidate).append('\n');
            }
        }
        sb.append("Or set -D").append(PROPERTY).append("=<path> or ")
                .append(ENV_VAR).append("=<path>.");
        throw new ConfigException(sb.toString());
    }

    public static PathConfig load(Path file) {
        if (!Files.isRegularFile(file)) {
            throw new ConfigException("paths.yaml not found at " + file);
        }
        PathConfig cfg = new PathConfig(Yaml.load(file), file.toAbsolutePath());
        cfg.validate();
        return cfg;
    }

    /** Where {@link #load()} looks, in order. */
    public static List<Path> searchPath() {
        List<Path> out = new ArrayList<>();
        String prop = System.getProperty(PROPERTY);
        if (prop != null && !prop.isEmpty()) {
            out.add(Paths.get(prop));
        }
        String env = System.getenv(ENV_VAR);
        if (env != null && !env.isEmpty()) {
            out.add(Paths.get(env));
        }
        out.add(Paths.get("paths.yaml"));
        Path jarDir = installDirectory();
        if (jarDir != null) {
            out.add(jarDir.resolve("paths.yaml"));
            out.add(jarDir.resolve("Config").resolve("paths.yaml"));
        }
        String home = System.getProperty("user.home");
        if (home != null) {
            out.add(Paths.get(home, ".asyncjlink", "paths.yaml"));
        }
        return out;
    }

    /** The directory holding the running jar, so a deployment can keep paths.yaml beside it. */
    public static Path installDirectory() {
        try {
            Path self = Paths.get(PathConfig.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI());
            return Files.isDirectory(self) ? self : self.getParent();
        } catch (RuntimeException | java.net.URISyntaxException e) {
            return null;
        }
    }

    public Path origin() {
        return origin;
    }

    // ---- CreoPaths -------------------------------------------------------

    @Override
    public String creoInstallDir() {
        return string("creo.install_dir", null);
    }

    @Override
    public String proCommMsgExe() {
        return string("creo.async.pro_comm_msg_exe", null);
    }

    @Override
    public int nmsPort() {
        return integer("creo.async.nms_port", 1239);
    }

    @Override
    public String connectUserName() {
        return string("creo.async.connect.user_name", "");
    }

    @Override
    public String connectDisplayName() {
        return string("creo.async.connect.display_name", "");
    }

    @Override
    public String connectMessageMenuPath() {
        return string("creo.async.connect.message_menu_path", null);
    }

    @Override
    public int connectTimeoutSeconds() {
        return integer("creo.async.connect.timeout_seconds", 30);
    }

    @Override
    public String defaultOutputDir() {
        return string("export.default_output_dir", null);
    }

    @Override
    public String defaultWorkspaceDir() {
        return string("workspace.default_dir", null);
    }

    // ---- runtime wiring --------------------------------------------------

    /**
     * Locates {@code pfcasync.jar}.
     *
     * <p>Taken from {@code creo.async.pfcasync_jar} when set, otherwise from its standard place
     * inside the installation. It is never bundled into this project's jars: it is PTC's, it is
     * version-matched to the installation, and it needs native libraries from the same tree.
     */
    public Path pfcasyncJar() {
        String explicit = string("creo.async.pfcasync_jar", null);
        if (!isBlank(explicit)) {
            return Paths.get(explicit);
        }
        String install = creoInstallDir();
        if (isBlank(install)) {
            return null;
        }
        return Paths.get(install, "Common Files", "text", "java", "pfcasync.jar");
    }

    /**
     * Where {@code pfcasyncmt} lives, for {@code java.library.path}.
     *
     * <p>Defaults to the standard per-architecture location; override with
     * {@code creo.async.native_lib_dir} on installations that place it elsewhere.
     */
    public Path nativeLibDir() {
        String explicit = string("creo.async.native_lib_dir", null);
        if (!isBlank(explicit)) {
            return Paths.get(explicit);
        }
        String install = creoInstallDir();
        if (isBlank(install)) {
            return null;
        }
        Path base = Paths.get(install);
        for (String arch : new String[] {"x86e_win64", "i486_nt", "x86_win64"}) {
            Path candidate = base.resolve(arch).resolve("obj");
            if (Files.isDirectory(candidate)) {
                return candidate;
            }
        }
        return null;
    }

    // ---- generic access --------------------------------------------------

    /** Reads a dotted path such as {@code creo.async.nms_port}. */
    public Object get(String dotted) {
        Object node = root;
        for (String part : dotted.split("\\.")) {
            if (!(node instanceof Map)) {
                return null;
            }
            node = ((Map<?, ?>) node).get(part);
        }
        return node;
    }

    public String string(String dotted, String fallback) {
        Object v = get(dotted);
        if (v == null) {
            return fallback;
        }
        String s = String.valueOf(v);
        return s;
    }

    public int integer(String dotted, int fallback) {
        Object v = get(dotted);
        if (v == null) {
            return fallback;
        }
        if (v instanceof Number) {
            return ((Number) v).intValue();
        }
        try {
            return Integer.parseInt(String.valueOf(v).trim());
        } catch (NumberFormatException e) {
            throw new ConfigException(dotted + " must be an integer, but was '" + v + "'");
        }
    }

    // ---- validation ------------------------------------------------------

    private void validate() {
        List<String> problems = new ArrayList<>();
        if (isBlank(proCommMsgExe())) {
            problems.add("creo.async.pro_comm_msg_exe is required "
                    + "(the full path to pro_comm_msg.exe in Creo's bin directory)");
        } else if (!Files.exists(Paths.get(proCommMsgExe()))) {
            problems.add("creo.async.pro_comm_msg_exe does not exist: " + proCommMsgExe());
        }
        if (nmsPort() <= 0 || nmsPort() > 65535) {
            problems.add("creo.async.nms_port must be between 1 and 65535, but was " + nmsPort());
        }
        String install = creoInstallDir();
        if (!isBlank(install) && !Files.isDirectory(Paths.get(install))) {
            problems.add("creo.install_dir is not a directory: " + install);
        }
        if (!problems.isEmpty()) {
            StringBuilder sb = new StringBuilder(origin + " is not usable:\n");
            for (String p : problems) {
                sb.append("  - ").append(p).append('\n');
            }
            throw new ConfigException(sb.toString().trim());
        }
    }

    /**
     * Checks that the process environment matches {@code paths.yaml}.
     *
     * <p>{@code PTCNMSPORT} and {@code PRO_COMM_MSG_EXE} are read by PTC's native layer through the
     * C library's own {@code getenv}, not through {@code System.getenv}. A JVM cannot change its own
     * native environment, so these must be correct <em>before</em> the JVM starts — which is why
     * {@code Bootstrap} re-executes the process with them set rather than trying to patch them in.
     *
     * @return the problems found; empty when the environment is already correct
     */
    public List<String> environmentProblems() {
        List<String> problems = new ArrayList<>();
        String port = System.getenv("PTCNMSPORT");
        if (port == null || port.isEmpty()) {
            problems.add("PTCNMSPORT is not set (paths.yaml says " + nmsPort() + ")");
        } else if (!port.trim().equals(String.valueOf(nmsPort()))) {
            problems.add("PTCNMSPORT is " + port + " but paths.yaml says " + nmsPort()
                    + "; it must match the port Creo itself was launched with");
        }
        String exe = System.getenv("PRO_COMM_MSG_EXE");
        if (exe == null || exe.isEmpty()) {
            problems.add("PRO_COMM_MSG_EXE is not set (paths.yaml says " + proCommMsgExe() + ")");
        }
        Path nativeDir = nativeLibDir();
        if (nativeDir != null) {
            String libPath = System.getProperty("java.library.path", "");
            boolean present = false;
            for (String entry : libPath.split(File.pathSeparator)) {
                if (Paths.get(entry.trim()).equals(nativeDir)) {
                    present = true;
                    break;
                }
            }
            if (!present) {
                // java.library.path is a JVM startup flag, not an environment variable: no amount of
                // getenv agreement fixes it, so a mismatch here must still force Bootstrap's re-exec,
                // which is the only place that can pass -Djava.library.path to a fresh JVM.
                problems.add("java.library.path does not include " + nativeDir
                        + " (needed to load pfcasyncmt)");
            }
        }
        return problems;
    }

    /** The environment the process needs, as a map, for {@code Bootstrap} to hand to a child JVM. */
    public Map<String, String> requiredEnvironment() {
        Map<String, String> env = new LinkedHashMap<>();
        env.put("PTCNMSPORT", String.valueOf(nmsPort()));
        if (!isBlank(proCommMsgExe())) {
            env.put("PRO_COMM_MSG_EXE", proCommMsgExe());
        }
        return env;
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    @Override
    public String toString() {
        return "PathConfig(" + origin + ")";
    }
}
