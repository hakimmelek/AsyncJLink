package com.asyncjlink.config;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Re-executes the process with the environment PTC's native layer requires.
 *
 * <p>Asynchronous J-Link reads {@code PTCNMSPORT} and {@code PRO_COMM_MSG_EXE} from the native
 * environment when {@code pfcasyncmt} loads. A JVM cannot alter its own native environment, so the
 * values have to be present before it starts.
 *
 * <p>That collides with how an MCP server is launched: {@code mcp.json} carries a command and
 * arguments and nothing else, so there is nowhere for an AI client to put them. Rather than demand a
 * wrapper script, the process bootstraps itself — it reads {@code paths.yaml}, and if the environment
 * does not already match, it starts one child JVM that has the right environment and hands over.
 * {@link ProcessBuilder#inheritIO()} passes the real file descriptors straight through, so the MCP
 * stdio framing is untouched and the child is indistinguishable to the client.
 *
 * <p>A sentinel variable in the child's environment stops this from recursing.
 */
public final class Bootstrap {

    private Bootstrap() {
    }

    /** Set in the child so it never re-executes again. */
    public static final String SENTINEL = "ASYNCJLINK_BOOTSTRAPPED";

    /**
     * Ensures the native environment is correct, re-executing if it is not.
     *
     * <p>When a child is started this method does not return: the parent waits for the child and
     * exits with its status.
     *
     * @param cfg       the loaded configuration
     * @param mainClass the class whose {@code main} should run in the child
     * @param args      the original command-line arguments
     */
    public static void ensureEnvironment(PathConfig cfg, Class<?> mainClass, String[] args) {
        if (System.getenv(SENTINEL) != null) {
            // We are the child. If anything is still wrong, re-executing again would loop.
            List<String> problems = cfg.environmentProblems();
            if (!problems.isEmpty()) {
                throw new ConfigException(
                        "The Creo environment is still wrong after bootstrapping:\n  - "
                                + String.join("\n  - ", problems)
                                + "\nSet these before launching, or check " + cfg.origin() + ".");
            }
            return;
        }
        if (cfg.environmentProblems().isEmpty() && pfcasyncOnClasspath()) {
            return; // already launched correctly, e.g. from creoctl.cmd
        }
        reexec(cfg, mainClass, args);
    }

    /**
     * Whether {@code pfcasync.jar} is already reachable.
     *
     * <p>{@code java -jar mcp.jar} gives the JVM a one-entry classpath, so the PTC jar usually is not
     * there. Checking by class name rather than by inspecting the classpath string also covers the
     * case where it arrived through a manifest {@code Class-Path}.
     */
    private static boolean pfcasyncOnClasspath() {
        try {
            Class.forName("com.ptc.pfc.pfcAsyncConnection.pfcAsyncConnection", false,
                    Bootstrap.class.getClassLoader());
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private static void reexec(PathConfig cfg, Class<?> mainClass, String[] args) {
        List<String> command = new ArrayList<>();
        command.add(javaExecutable());
        command.add("-cp");
        command.add(buildClasspath(cfg));

        // Carry over the settings that tell the child where its configuration and native libraries
        // are; without these it would repeat the parent's search from a different working state.
        propagateProperty(command, PathConfig.PROPERTY, cfg.origin().toString());
        propagateProperty(command, "java.library.path", libraryPath(cfg));
        // Any -Dasyncjlink.* tuning the user passed on the outer command line, e.g. the MCP page
        // size, has to survive the hand-over or it would silently stop applying.
        for (String name : System.getProperties().stringPropertyNames()) {
            if (name.startsWith("asyncjlink.") && !name.equals(PathConfig.PROPERTY)) {
                propagateProperty(command, name, System.getProperty(name));
            }
        }

        command.add(mainClass.getName());
        for (String a : args) {
            command.add(a);
        }

        ProcessBuilder pb = new ProcessBuilder(command);
        Map<String, String> env = pb.environment();
        env.putAll(cfg.requiredEnvironment());
        env.put(SENTINEL, "1");
        appendToPath(env, installBinDir(cfg));
        appendToPath(env, cfg.nativeLibDir());
        pb.inheritIO();

        try {
            Process child = pb.start();
            System.exit(child.waitFor());
        } catch (java.io.IOException e) {
            throw new ConfigException(
                    "Could not re-launch with the Creo environment: " + e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ConfigException("Interrupted while waiting for the bootstrapped process", e);
        }
    }

    private static void propagateProperty(List<String> command, String key, String value) {
        if (value != null && !value.isEmpty()) {
            command.add("-D" + key + "=" + value);
        }
    }

    /**
     * The child's classpath: this application plus PTC's {@code pfcasync.jar}.
     *
     * <p>{@code pfcasync.jar} is never bundled into the shipped jars. It belongs to the Creo
     * installation, it is version-matched to it, and it loads native libraries from the same tree, so
     * it is added from the path in {@code paths.yaml} at launch instead.
     */
    private static String buildClasspath(PathConfig cfg) {
        StringBuilder cp = new StringBuilder(System.getProperty("java.class.path", ""));
        Path jar = cfg.pfcasyncJar();
        if (jar == null) {
            throw new ConfigException(
                    "Cannot locate pfcasync.jar. Set creo.install_dir, or creo.async.pfcasync_jar "
                            + "directly, in " + cfg.origin() + ".");
        }
        if (!Files.isRegularFile(jar)) {
            throw new ConfigException(
                    "pfcasync.jar not found at " + jar + " (derived from " + cfg.origin()
                            + "). Set creo.async.pfcasync_jar to its real location.");
        }
        if (cp.length() > 0) {
            cp.append(File.pathSeparator);
        }
        cp.append(jar);
        return cp.toString();
    }

    /** {@code java.library.path} for the child, with Creo's native directory prepended. */
    private static String libraryPath(PathConfig cfg) {
        String current = System.getProperty("java.library.path", "");
        Path nativeDir = cfg.nativeLibDir();
        if (nativeDir == null) {
            return current;
        }
        return current.isEmpty() ? nativeDir.toString()
                : nativeDir + File.pathSeparator + current;
    }

    private static Path installBinDir(PathConfig cfg) {
        String install = cfg.creoInstallDir();
        if (install == null || install.trim().isEmpty()) {
            return null;
        }
        return Paths.get(install, "bin");
    }

    /**
     * Puts a Creo native directory on the child's PATH.
     *
     * <p>{@code pfcasyncmt}/{@code jnicipjavamtz} link against other DLLs that ship with Creo. Once the
     * JVM has located the library itself via {@code java.library.path}, Windows resolves *its*
     * dependencies through the process PATH, not {@code java.library.path} — so without this, the
     * child fails at {@code System.loadLibrary} with an {@code UnsatisfiedLinkError} ("Can't find
     * dependent libraries") that gives no hint as to which one is missing. Both the install's
     * {@code bin} and the native library directory itself are candidates, since which one actually
     * holds the shared runtime DLLs varies by installation layout.
     */
    private static void appendToPath(Map<String, String> env, Path dir) {
        if (dir == null || !Files.isDirectory(dir)) {
            return;
        }
        String key = "PATH";
        for (String existing : env.keySet()) {
            if (existing.equalsIgnoreCase("PATH")) {
                key = existing;
                break;
            }
        }
        String current = env.get(key);
        env.put(key, current == null || current.isEmpty()
                ? dir.toString()
                : dir + File.pathSeparator + current);
    }

    private static String javaExecutable() {
        String home = System.getProperty("java.home");
        String exe = System.getProperty("os.name", "").toLowerCase(java.util.Locale.ROOT)
                .contains("win") ? "java.exe" : "java";
        Path candidate = Paths.get(home, "bin", exe);
        return Files.isExecutable(candidate) ? candidate.toString() : "java";
    }
}
