# AsyncJLink — Setup

Drive Creo Parametric from the command line or from an AI agent.
**937 commands**, covering every operation the J-Link API exposes on a live Creo object.

This folder is everything you need:

| File | What it is |
|---|---|
| `paths.yaml` | **Edit this first.** Describes your machine. |
| `creoctl.jar` | Command-line tool. |
| `creoctl.cmd` | Optional faster launcher for `creoctl`. |
| `mcp.jar` | MCP server for AI clients. |
| `mcp.json` | Ready-made config, with the real paths already filled in. |
| `paths.example.yaml` | Untouched template, for reference. |

**Requires:** Java 8 or later on your `PATH`, and Creo Parametric.

---

## Step 1 — Edit `paths.yaml`

Two values must be right. Everything else can stay as it is.

```yaml
creo:
  install_dir: "D:\\appli\\Creo 3.0\\M120"

  async:
    pro_comm_msg_exe: "D:\\appli\\Creo 3.0\\M120\\bin\\pro_comm_msg.exe"
    nms_port: 1239
```

- **`pro_comm_msg_exe`** — full path to `pro_comm_msg.exe` in Creo's `bin` folder.
- **`nms_port`** — **must match the port the running Creo was launched with.** This is the single
  most common reason a connection fails. See *Troubleshooting* below.

> **Backslashes must be doubled** inside double quotes: `"D:\\appli\\..."`.
> Forward slashes also work and avoid the problem entirely: `"D:/appli/Creo 3.0/M120"`.

---

## Step 2 — Check the install

```
java -jar creoctl.jar --selfcheck
```

This needs neither Creo nor a connection. It loads all 937 commands and verifies every schema:

```
commands loaded : 937
checks run      : 975
result          : OK
```

---

## Step 3 — Connect

Start Creo Parametric, open a model, then:

```
java -jar creoctl.jar Session.GetCurrentModel
```

A successful reply looks like this:

```json
{
  "ok": true,
  "command": "BaseSession.GetCurrentModel",
  "result": {
    "$handle": "Model@1",
    "$type": "Model",
    "FullName": "BRACKET_01",
    "FileName": "bracket_01.prt"
  }
}
```

---

## Using the command line

```
creoctl --list                              list every command, by package
creoctl --list --search mass                find one
creoctl --describe Solid.GetMassProperty    show its full parameter schema
creoctl --stats                             counts per package
```

Commands are named `Receiver.Method`. The receiver is part of the name because the same method name
recurs across different classes.

**`--target` is the object to act on.** It accepts either:

- a **handle** from an earlier result, such as `Model@1`, or
- for models, just the **name**: `bracket_01.prt`, or bare `BRACKET_01`.

```
creoctl Solid.GetMassProperty --target bracket_01.prt
creoctl Model.Save --target BRACKET_01
```

Commands on `Session` act on the connection and take no `--target`.

Values are typed as you would expect — `true`/`false`, numbers, JSON for objects and arrays, anything
else a string. For scripting, `--raw '{...}'` supplies the whole parameter object at once.

### Handles

Most Creo objects have no name you can write down, so each result gives you a handle like
`Surface@41`. Pass it straight back into the next command:

```
creoctl Solid.ListFeatures --target bracket_01.prt
creoctl Feature.GetFeatType --target Feature@7
```

Handles last only as long as the connection — a `creoctl` run discards them on exit, an AI session
keeps them for as long as it stays connected.

---

## Using it with an AI client

`mcp.json` in this folder already has the correct absolute paths. Merge its `creo-jlink` entry into
your client's own MCP config:

```json
{
  "mcpServers": {
    "creo-jlink": {
      "command": "java",
      "args": [
        "-Dasyncjlink.config=C:\\Tools\\AsyncJLink\\release\\paths.yaml",
        "-jar",
        "C:\\Tools\\AsyncJLink\\release\\mcp.jar"
      ]
    }
  }
}
```

Every command becomes its own tool, named `Receiver_Method`.

Two things worth knowing:

- **Creo does not need to be running when the client starts.** The connection opens on the first
  actual tool call, not at launch, so starting your AI client before Creo is fine.
- **This publishes 937 tools.** That is a lot by MCP standards. If your client struggles with a tool
  list that size, add `"-Dasyncjlink.mcp.pageSize=100"` to `args` and the server will paginate.

If you move this folder, re-run `build-release.bat` or edit the paths in `mcp.json` by hand.

---

## Troubleshooting

### "Cannot reach Creo" / the connection times out

Almost always the port. Asynchronous J-Link is brokered by PTC's Name Service Message Daemon, and
**`PTCNMSPORT` must be identical for Creo and for this tool.** There is no host or port setting in
the connection call itself — `nms_port` in `paths.yaml` is the only one that matters.

Check what Creo was started with:

```
echo %PTCNMSPORT%
```

If that prints nothing, Creo is using the default and you should set `nms_port: 1239` — but confirm
with whoever configured the Creo installation, since sites often standardise on a different port.

Also confirm:

- Creo Parametric is actually running, with a window open.
- `pro_comm_msg_exe` points at a file that exists.
- Creo and this tool are running **as the same Windows user**.

### `UnsatisfiedLinkError` or a missing DLL

The native library `pfcasyncmt` could not load. Check that `install_dir` in `paths.yaml` is correct —
it is used to find both `pfcasync.jar` and the native library folder.

### "pfcasync.jar not found"

Set `install_dir` correctly, or point at the jar directly:

```yaml
creo:
  async:
    pfcasync_jar: "D:\\appli\\Creo 3.0\\M120\\Common Files\\text\\java\\pfcasync.jar"
```

### A command reports "cannot be invoked"

8 of the 937 commands take a live callback listener, which cannot be expressed as JSON. They are
listed for completeness but can only be used from an in-process J-Link application.

---

## How it finds things

You do not need this to use the tool, but it explains the behaviour.

`paths.yaml` is looked for in this order:

1. `-Dasyncjlink.config=<path>` — what `mcp.json` uses
2. the `ASYNCJLINK_CONFIG` environment variable
3. `paths.yaml` in the current directory
4. next to the jar, and in `<jar folder>\Config\`
5. `%USERPROFILE%\.asyncjlink\paths.yaml`

`PTCNMSPORT` and `PRO_COMM_MSG_EXE` have to be set in the environment *before* the JVM starts,
because PTC's native layer reads them directly and a running JVM cannot change its own environment.
Since `mcp.json` has nowhere to put environment variables, the program handles it itself: it reads
`paths.yaml` and, if the environment is not already correct, relaunches once with the right settings
and hands over. This is why `java -jar mcp.jar` works with no wrapper script.

`creoctl.cmd` sets the same variables up front and so skips that extra step — worth using if you call
`creoctl` in a loop. Edit the paths at the top of it to match `paths.yaml`.
