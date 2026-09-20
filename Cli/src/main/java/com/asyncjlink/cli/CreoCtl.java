package com.asyncjlink.cli;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandRegistry;
import com.asyncjlink.commands.CreoConnection;
import com.asyncjlink.commands.SelfCheck;
import com.asyncjlink.config.Bootstrap;
import com.asyncjlink.config.PathConfig;
import com.asyncjlink.json.Json;
import com.asyncjlink.json.JsonObject;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * {@code creoctl} — the human- and script-facing front door.
 *
 * <p>Reads {@code paths.yaml}, opens a direct Creo connection, looks up one command by name, runs it,
 * prints the result as JSON and exits. It never imports {@code pfcasync.jar}: everything goes through
 * {@link CommandRegistry#invoke}, which is also what keeps {@code jxthrowable} out of this module.
 *
 * <pre>
 *   creoctl --list
 *   creoctl --list --search mass
 *   creoctl --describe Solid.GetMassProperty
 *   creoctl Session.GetCurrentModel
 *   creoctl Model.Export --target bracket.prt --value out.stp --exportInstructions '{...}'
 * </pre>
 */
public final class CreoCtl {

    private CreoCtl() {
    }

    private static final int EXIT_OK = 0;
    private static final int EXIT_USAGE = 2;
    private static final int EXIT_COMMAND_FAILED = 1;
    private static final int EXIT_CONFIG = 3;

    public static void main(String[] args) {
        System.exit(run(args));
    }

    static int run(String[] args) {
        ArgParser.Parsed parsed = ArgParser.parse(args);

        for (String e : parsed.errors) {
            System.err.println("error: " + e);
        }
        if (!parsed.errors.isEmpty()) {
            return EXIT_USAGE;
        }

        // CommandRegistry (via the generated command classes) touches pfcasync.jar's own types the
        // moment it loads, so pfcasync.jar must already be on the classpath before this runs. Bootstrap
        // is what puts it there when this process was not launched through creoctl.cmd.
        PathConfig config;
        try {
            config = parsed.configPath != null
                    ? PathConfig.load(Paths.get(parsed.configPath))
                    : PathConfig.load();
            Bootstrap.ensureEnvironment(config, CreoCtl.class, args);
        } catch (RuntimeException e) {
            System.err.println("error: " + e.getMessage());
            return EXIT_CONFIG;
        }

        CommandRegistry registry = CommandRegistry.load();

        // Catalogue queries need no Creo connection at all, which makes them usable for exploration
        // on a machine where Creo is not running.
        if (parsed.list || (parsed.help && parsed.command == null)) {
            if (parsed.help && parsed.command == null && !parsed.list) {
                printUsage(registry);
                return EXIT_OK;
            }
            printList(registry, parsed.search);
            return EXIT_OK;
        }
        if (parsed.stats) {
            System.out.println(Json.writePretty(registry.stats()));
            return EXIT_OK;
        }
        if (parsed.selfcheck) {
            SelfCheck.Result result = SelfCheck.run();
            System.out.println(SelfCheck.format(result));
            return result.ok() ? EXIT_OK : EXIT_COMMAND_FAILED;
        }
        if (parsed.command == null) {
            printUsage(registry);
            return EXIT_USAGE;
        }
        Command command = registry.get(parsed.command);
        if (command == null) {
            System.err.println("error: unknown command '" + parsed.command + "'");
            System.err.println("Run 'creoctl --list --search " + shortName(parsed.command)
                    + "' to find it.");
            return EXIT_USAGE;
        }
        if (parsed.describe || parsed.help) {
            System.out.println(Json.writePretty(registry.describe(command)));
            return EXIT_OK;
        }

        try (CreoConnection conn = CreoConnection.open(config)) {
            JsonObject result = registry.invoke(conn.context(), command.name(), parsed.params);
            System.out.println(parsed.pretty ? Json.writePretty(result) : Json.write(result));
            return result.getBoolean("ok", false) ? EXIT_OK : EXIT_COMMAND_FAILED;
        } catch (Exception e) {
            // Connection-level failure: the command never ran.
            JsonObject err = JsonObject.of(
                    "ok", Boolean.FALSE,
                    "error", JsonObject.of(
                            "code", "no_connection",
                            "message", e.getMessage() == null
                                    ? e.getClass().getSimpleName() : e.getMessage()));
            System.out.println(Json.writePretty(err));
            return EXIT_COMMAND_FAILED;
        }
    }

    private static String shortName(String name) {
        int dot = name.lastIndexOf('.');
        return dot >= 0 ? name.substring(dot + 1) : name;
    }

    private static void printList(CommandRegistry registry, String search) {
        List<Command> commands = search == null
                ? new java.util.ArrayList<>(registry.all())
                : registry.search(search);
        if (commands.isEmpty()) {
            System.out.println("No commands match '" + search + "'.");
            return;
        }
        String currentPackage = null;
        for (Command c : commands) {
            if (!c.jlinkPackage().equals(currentPackage)) {
                currentPackage = c.jlinkPackage();
                System.out.println();
                System.out.println(currentPackage);
            }
            System.out.printf("  %-52s %s%n", c.name(),
                    c.isInvocable() ? c.signature() : "[listener required] " + c.signature());
        }
        System.out.println();
        System.out.println(commands.size() + " command(s)"
                + (search == null ? "" : " matching '" + search + "'"));
    }

    private static void printUsage(CommandRegistry registry) {
        System.out.println("creoctl — run one Creo J-Link operation and print the result as JSON.");
        System.out.println();
        System.out.println("Usage:");
        System.out.println("  creoctl <Command.Name> [--param value ...]");
        System.out.println("  creoctl --list [--search <term>]");
        System.out.println("  creoctl --describe <Command.Name>");
        System.out.println("  creoctl --stats");
        System.out.println("  creoctl --selfcheck        Verify the install without needing Creo.");
        System.out.println();
        System.out.println("Options:");
        System.out.println("  --<name> <value>   A command parameter. Values are typed: true/false,");
        System.out.println("                     numbers, JSON for objects and arrays, else string.");
        System.out.println("  --raw '<json>'     Supply the whole parameter object verbatim.");
        System.out.println("  --config <path>    Use a specific paths.yaml.");
        System.out.println("  --compact          Print the result on one line.");
        System.out.println();
        System.out.printf("%d commands are available across %d J-Link packages.%n",
                registry.size(), registry.byPackage().size());
        System.out.println("The target object is passed as --target, either a handle from an earlier");
        System.out.println("command or, for models, a name such as bracket_01.prt.");
        System.out.println();
        System.out.println("Packages:");
        StringBuilder line = new StringBuilder("  ");
        for (Map.Entry<String, List<Command>> e : registry.byPackage().entrySet()) {
            String entry = e.getKey() + "(" + e.getValue().size() + ") ";
            if (line.length() + entry.length() > 78) {
                System.out.println(line);
                line = new StringBuilder("  ");
            }
            line.append(entry);
        }
        if (line.toString().trim().length() > 0) {
            System.out.println(line);
        }
    }
}
