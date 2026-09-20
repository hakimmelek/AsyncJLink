# Command naming

Every command in AsyncJLink — generated or hand-written — is named the same way.

```
Receiver.Method          the command name (creoctl)
Receiver_Method          the same command as an MCP tool
```

`Receiver` is the J-Link type the command acts on. `Method` is a verb-first PascalCase name.
The receiver is part of the name because 50 method names recur across classes within a single
package, so `GetName` alone would be ambiguous.

## The receiver decides `--target`

This is the rule that matters most, and it is what keeps the two layers interchangeable:

**`--target` is always an instance of the receiver.** It takes either a handle returned by an
earlier command (`Part@3`), or, for models, a name (`bracket_01.prt`).

```
creoctl Solid.GetMassProperty   --target bracket_01.prt
creoctl Assembly.GetBOM         --target top_level.asm
```

A reader who knows what the receiver is already knows what to pass. A new command that breaks this
correspondence — a `Model.*` command that wants an assembly, say — is misnamed, not merely awkward.

Commands whose receiver is `Session` or `BaseSession` act on the connection itself and take no
`--target`.

## Verbs

Use the vocabulary the J-Link dictionary already uses, and nothing else:

| Verb | Means |
|---|---|
| `Get` | Returns one thing about the target. |
| `List` | Returns many things. |
| `Check` | Returns a verdict — a boolean or a report of findings. |
| `Set` | Mutates the target. |
| `Create` | Makes something new and returns it. |
| `Export` | Writes to disk. |

No new verb styles, no `Fetch`, `Fix`, `Do`, `Run`, or `Query`. If a name does not fall naturally
out of this list, the command is usually doing two things and should be two commands.

## Two layers, one namespace

There are two kinds of command and they are deliberately indistinguishable to call:

**Raw** — 937 of them, generated from `docs/jlink-api-asynchronous.md` into
`Commands/.../rawcommands/`. Each is exactly one J-Link call. Nothing here is written by hand;
`tools/generate_commands.py` overwrites the whole tree on every run.

**Composite** — hand-written, in `Commands/.../composites/`. Each one does what a J-Link user
would otherwise do with five to several hundred raw calls, and returns a single shaped result.
Composites exist because the CLI cannot chain — handles die when a `creoctl` process exits — and
because an agent choosing between 937 near-identical tools chooses badly.

They share the naming scheme, the `--target` rule and the result envelope. What separates them is
the package they are listed under:

```
pfcSolid                     <- raw, the J-Link package the call comes from
  Solid.GetMassProperty      MassProperty GetMassProperty(String) throws jxthrowable

asyncjlink                   <- composite
  Solid.GetMassPropertyReport
```

So `creoctl --list` and `--stats` group them separately, and a user can always tell whether a
command is one API call or a workflow, without that affecting how it is invoked.

## Rules for a new composite

1. **Reuse the J-Link receiver.** If it acts on an assembly, it is `Assembly.*`. Invent a receiver
   only when no J-Link type fits, which should be close to never.
2. **Never collide with a raw name.** The raw layer is regenerated from PTC's dictionary, so a
   future Creo release could introduce a real `Assembly.GetBOM` and silently shadow ours. This is
   checked at build time in `tools/validate_generated.py`; a collision fails the build rather than
   changing behaviour quietly.
3. **Report units and provenance.** Anything returning a physical quantity states its unit system,
   and anything derived from a material states whether that material was actually assigned. Silent
   unit and density errors are the most expensive mistakes in this domain.
4. **Mutations are opt-in.** Any command that changes a model supports `dryRun` and reports what it
   changed, before and after.
5. **Return handles next to data.** A composite that summarises objects still returns their
   handles, so the caller can drill into the raw layer without starting over.
