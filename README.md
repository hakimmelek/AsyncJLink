# AsyncJLink

Drive Creo Parametric from a command line or from an AI agent, over PTC's **asynchronous J-Link**
API, through one shared command library.

**937 commands** — every operation the J-Link dictionary declares on a live Creo object — are
available identically to `creoctl` and to MCP clients.

---

## How it fits together

```
Commands/   Pure library. The only module that imports pfcasync.jar. Knows nothing about
            sockets, argv, stdio or MCP: given a context and parameters it does one Creo
            thing and returns JSON.
Config/     Reads the user's paths.yaml. Implements CreoPaths, which Commands declares.
Cli/        creoctl — connects, runs one command, prints JSON, exits.
Mcp/        MCP server over stdin/stdout — connects once, serves for the AI session.
tools/      The generator that writes Commands/.../rawcommands from the dictionary.
docs/       jlink-api-asynchronous.md — the API dictionary everything is generated from.
```

The dependency direction is enforced by the build:

```
Commands  <--  Config  <--  Cli
      ^                     Mcp
      |_____________________|
```

`Commands` depends on nothing in this project. `Cli` and `Mcp` never import `pfcasync.jar`; they only
call `CommandRegistry.invoke(...)`, which is also what keeps PTC's checked `jxthrowable` out of them.

**There are no third-party dependencies.** JSON and YAML are implemented in-tree. Creo sites are
frequently air-gapped, and the only external artifact on the classpath should be PTC's own jar.

---

## Setup

### 1. Configure

```
copy Config\paths.example.yaml paths.yaml
```

Then edit it. Every machine-specific value lives there and nowhere else — Creo's location, the async
connection settings, the default output directories. `paths.example.yaml` documents each key.

Two values matter more than the rest:

- **`pro_comm_msg_exe`** → exported as `PRO_COMM_MSG_EXE`
- **`nms_port`** → exported as `PTCNMSPORT`, and it **must match the port the running Creo was
  launched with**

### 2. Build

```
gradle build -PpfcasyncJar="D:/appli/Creo 3.0/M120/Common Files/text/java/pfcasync.jar"
```

No Gradle wrapper is checked in, since this repository was set up on a machine without a JDK. Run
`gradle wrapper` once to add one, after which `gradlew` works as usual. The jar path can also come
from the `PFCASYNC_JAR` environment variable, from `pfcasyncJar` in `gradle.properties`, or by
dropping the jar at `libs/pfcasync.jar`.

`pfcasync.jar` is `compileOnly` and is never bundled: it belongs to the Creo installation, it is
version-matched to it, and it loads native libraries from the same tree. At runtime it is added to
the classpath from the path in `paths.yaml`.

Produces `Cli/build/libs/creoctl.jar` and `Mcp/build/libs/mcp.jar`.

### Or build a release in one step

```
build-release.bat "D:\appli\Creo 3.0\M120\Common Files\text\java\pfcasync.jar"
```

Runs the validators, compiles, and assembles everything an end user needs into `release\`:

```
release/
├── creoctl.jar        ├── paths.yaml            (from the template, ready to edit)
├── creoctl.cmd        ├── paths.example.yaml    (untouched reference)
├── mcp.jar            ├── mcp.json              (real absolute paths already filled in)
└── SETUP.md           (end-user walkthrough, source in packaging/)
```

`pfcasync.jar` is *not* copied into the release — it belongs to the Creo installation and is found at
runtime via `paths.yaml`. An existing `release\paths.yaml` is preserved across rebuilds, so a
filled-in configuration is not lost.

### 3. Check the install — no Creo needed

```
java -jar creoctl.jar --selfcheck
```

Instantiates all 937 commands, builds every schema, and re-checks the parameter-naming algorithm
against its fixture.

---

## Using the CLI

```
creoctl --list                              # everything, grouped by J-Link package
creoctl --list --search mass                # find a command
creoctl --describe Solid.GetMassProperty    # full JSON schema
creoctl --stats                             # counts per package

creoctl Session.GetCurrentModel
creoctl Solid.GetMassProperty --target bracket_01.prt
creoctl Model.Export --target bracket_01.prt --value out.stp --raw '{"exportInstructions":{...}}'
```

Commands are named `Receiver.Method`. The receiver is part of the name because 50 method names recur
across classes within a single package.

**`--target` is the object to act on**, and it takes either:

- a **handle** returned by an earlier command, such as `Part@3`, or
- for models, a **name**: `bracket_01.prt`, or bare `BRACKET_01` (tried as part, then assembly, then
  drawing).

Commands on `Session` take no `--target`; they act on the connection.

Values are typed the way a shell user expects — `true`/`false`, numbers, JSON for objects and arrays,
everything else a string. `--raw '<json>'` supplies the whole parameter object verbatim and is what
scripts should use.

---

## Using the MCP server

Copy the `creo-jlink` entry from `Mcp/mcp.json.example` into your client's `mcp.json`:

```json
{
  "mcpServers": {
    "creo-jlink": {
      "command": "java",
      "args": ["-jar", "C:\\Tools\\AsyncJLink\\mcp.jar"]
    }
  }
}
```

Discovery carries a command and arguments and nothing else — no Creo details, no host, no port.
Everything installation-specific stays in `paths.yaml`.

Every command is published as its own tool, named `Receiver_Method`. `tools/list` supports cursor
pagination; if your client struggles with a list this large, cap it with
`-Dasyncjlink.mcp.pageSize=100`.

The Creo connection is opened **lazily**, on the first `tools/call`. AI clients launch their MCP
servers at startup, long before the user asks for anything; connecting eagerly would fail on every
launch where Creo was not yet running and leave the server dead for the session.

### How `java -jar mcp.jar` finds Creo

`PTCNMSPORT` and `PRO_COMM_MSG_EXE` are read by PTC's native layer through the C library's own
`getenv`, and a JVM cannot change its own native environment. Since `mcp.json` has nowhere to put
environment variables, the process **bootstraps itself**: it reads `paths.yaml` and, if the
environment does not already match, starts one child JVM that has the right environment and
`pfcasync.jar` on the classpath, then hands over. `ProcessBuilder.inheritIO()` passes the real file
descriptors through, so the stdio framing is untouched and the client sees no difference.

`Cli/creoctl.cmd` sets the same variables up front, which skips the extra process — worth using when
`creoctl` is called in a loop.

---

## Handles

Most J-Link calls return a live object with no serialisable identity, so each is registered and
named — `Surface@41` — and a later command passes that string back to get the same object. Handles
live and die with the connection: a CLI invocation discards them on exit, an MCP session keeps them
while the agent is connected. Results carry a short summary alongside the handle, because an agent
that gets back `Feature@7` alone has to make another call to learn what it is.

**8 of the 937 commands take a callback listener** and cannot be driven from JSON. They are still
catalogued — they appear in `--list` and in the tool list — but they refuse to run with an
explanation rather than pretending to work.

---

## Regenerating

`Commands/.../rawcommands/` is generated and checked in. Nothing in it should be edited by hand.

```
python tools/generate_commands.py     # rewrite the whole tree from docs/
python tools/test_generator.py        # 82 checks on the parser and generator
python tools/validate_generated.py    # structural check of all 937 files
```

The tests and the validator need **no JDK and no Creo**, so they work on a development machine. The
validator does not replace compilation; it catches what a template generator actually gets wrong —
unbalanced delimiters, a class name disagreeing with its file, a type used without an import, an
index that has drifted from the tree on disk.

Generation is not wired into `compileJava`, so the project builds without Python. See
[docs/command-naming.md](docs/command-naming.md) for how commands are named and how a command is
told apart from the API's other 5,870 members.

---

## Notes on the async connection

`AsyncConnection_Connect` takes `(userName, displayName, messageMenuPath, timeoutSeconds)` — **there
is no host or port argument**. The network endpoint is negotiated by PTC's NMSD (Name Service Message
Daemon); the only port that matters is `PTCNMSPORT`, and it must be identical on the session that
launched Creo and on whatever launches `creoctl`/`mcp.jar`. Closing a connection calls `Disconnect`,
never `End()`, so a tool that reads a model can't shut down the user's Creo session.

Sources: [Connecting to a Creo Parametric Process](https://support.ptc.com/help/creo_toolkit/otk_java_pma/r11.0/usascii/creo_toolkit/user_guide/Connecting_to_a_Creo_Parametric_Creo_Process.html)
· [Setting up an Asynchronous J-Link Application](https://support.ptc.com/help/creo_toolkit/otk_java_plus/usascii/creo_toolkit/user_guide/Setting_up_an_Asynchronous_J_Link_Application.html)

---

## Requirements

- **A JDK to build.** Output targets Java 8 bytecode, so it stays loadable by the JRE that ships
  with Creo 3.0. Build with JDK 8–21; on JDK 9+ the build uses `--release 8`, which also checks that
  only Java 8 APIs are used.
- **Java 8 or later to run.**
- **Creo Parametric**, running, started with the same `PTCNMSPORT`.
- **Python 3.8+** — only to regenerate and to run the test suites; not needed to build or run.

Nothing here has been compiled or run against a live Creo: this machine has neither a JDK nor Creo
installed. What *has* been verified is everything that can be checked without them — see
[Regenerating](#regenerating).
