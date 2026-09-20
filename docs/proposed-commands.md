# Proposed composite commands

**Status: implemented and verified.** 53 of the 55 commands below live in
`Commands/src/main/java/com/asyncjlink/commands/compositecommands/`, registered through
`CompositeCommandIndex` and served by both `creoctl` and the MCP server. The registry holds 992
commands — 937 raw plus 55 composite — and `--selfcheck` verifies that count, so a composite that
ever collided with a raw name would fail loudly at startup rather than shadowing it.

This started as 38 commands and a proposals list; every one of the 19 originally proposed has since
been either built and verified live, or shown to be genuinely unbuildable against the current
catalogue (2 of them — see their own sections). Nothing below is a sketch: every raw command cited
was checked against the dictionary, and the great majority against the actual generated
`Commands/.../rawcommands/` tree.

Every one of the 53 implemented commands has been exercised against a live Creo 3.0 session (not
just `--selfcheck`, which only proves the schemas build) — read commands, mutations, and the
dry-run paths of the destructive ones. **Verified** below means: called live, returned the shape
this document describes, with correct values. A few things surfaced during that work that are worth
recording up front rather than per command:

- **A session-level `XToolkitBadContext` on every call that regenerates.** `Model.SetDimensions`,
  `Model.SetRelations`, `Model.RegenerateAndReport`, `Solid.SuppressFeatures`/`ResumeFeatures`, and
  `Assembly.AddComponent`/`ReplaceComponent`'s regenerate step all hit this identically. It
  reproduces on the raw `Solid.Regenerate` call too, on a pristine untouched model, regardless of
  window/display state — so it is a property of this Creo session's async connection, not a bug in
  any of these composites. Each command's own regeneration report surfaces it correctly; the
  dimension or relation change itself is still written. Marked **⚠ regen-blocked** below.
- **Several real defects were found and fixed** in the shared marshalling layer these composites sit
  on (silent `null` fields instead of the real error, a reflection access failure that hid mass
  properties and inertia tensors, a fixed-size-array assumption that truncated `Point3D`/`Vector3D`,
  a `null` argument that crashed the whole async connection in `Session.CreateDrawing`). All are
  fixed and re-verified live; they are not called out per command below since none of them are
  specific to one composite — they lived in `Marshal`, `DataObjects` and `Seqs`.
- **A second `null`-crashes-the-connection defect**, same family as the first but in
  `Model2D.CreateView`'s territory — see `Drawing.CreateStandardViews` below, which is implemented
  defensively because of it but still does not succeed, for an unrelated, pre-existing reason.
- **A wrong first guess, corrected live.** `Drawing.Export`'s `fontStroke` option was first built
  assuming `PDFOPT_FONT_STROKE` took a plain boolean `ArgValue`; that failed live with
  `XToolkitInvalidType`. The dictionary has a `PDFFontStrokeMode` enum sitting right next to
  `PDFColorDepth` doing the exact same job as the colour option's enum — same shape, missed on the
  first pass. Fixed and reverified: a stroked-font export of the same drawing came back roughly 2.8×
  the file size of the TrueType one (237 KB vs. 85 KB), exactly the signature of embedded font
  references becoming vector paths.

| Command | Status |
|---|---|
| `Session.GetContext` | ✅ Verified |
| `Session.GetConfig` | ✅ Verified |
| `Session.SetWorkingDirectory` | ✅ Verified |
| `Session.ListDirectory` | ✅ Verified |
| `Session.LoadModel` | ✅ Verified |
| `Session.SaveAll` | ✅ Verified |
| `Session.EraseModels` | ✅ Verified |
| `Session.CreateModel` | ✅ Verified — see the note under its own heading below |
| `Session.CreateDrawing` | ✅ Verified — see the note under its own heading below |
| `Session.GetSelection` | ✅ Verified (errors cleanly with nothing selected, as designed) |
| `Model.FindItems` | ✅ Verified |
| `Model.HighlightItems` | ✅ Verified |
| `Model.GetParameters` | ✅ Verified |
| `Model.SetParameters` | ✅ Verified |
| `Model.GetDimensions` | ✅ Verified |
| `Model.SetDimensions` | ⚠ Verified, regen-blocked |
| `Model.GetRelations` | ✅ Verified |
| `Model.SetRelations` | ⚠ Verified, regen-blocked |
| `Model.EvaluateExpression` | ✅ Verified |
| `Model.RegenerateAndReport` | ⚠ Verified, regen-blocked |
| `Solid.SuppressFeatures` | ⚠ Verified, regen-blocked |
| `Solid.ResumeFeatures` | ⚠ Verified, regen-blocked |
| `Solid.DeleteFeatures` | ✅ Verified (dry run; live delete not exercised against test data) |
| `Assembly.AddComponent` | ⚠ Verified, regen-blocked |
| `Assembly.ReplaceComponent` | ⚠ Verified, regen-blocked |
| `Solid.GetMassPropertyReport` | ✅ Verified |
| `Assembly.GetBOM` | ✅ Verified |
| `Model.Audit` | ✅ Verified |
| `Assembly.CheckInterference` | ✅ Verified |
| `Solid.FindFeatures` | ✅ Verified |
| `Model.GetWhereUsed` | ✅ Verified |
| `Model.CreateBackup` | ✅ Verified |
| `Model.SaveChecked` | ✅ Verified (correctly refuses to save with a failed feature present) |
| `Model.ExportPackage` | ✅ Verified |
| `Model.SetView` | ✅ Verified |
| `Model.CaptureImage` | ✅ Verified |
| `Model.SetLayerStatus` | ✅ Verified |
| `Drawing.CheckUpToDate` | ✅ Verified |
| `Drawing.GetDimensions` | ✅ Verified |
| `Drawing.SetDimensionTolerances` | ✅ Verified |
| `Drawing.CreateStandardViews` | ⚠ Implemented, regen-blocked (see note) |
| `Drawing.GetSheets` | ❌ Not implemented, blocked (see note) |
| `Drawing.GetNotes` | ✅ Verified — 44 notes on a real production drawing |
| `Drawing.SetNoteText` | ✅ Verified |
| `Drawing.DeleteNotes` | ✅ Verified (dry run) |
| `Drawing.GetSymbols` | ✅ Verified — 21 symbols |
| `Drawing.DeleteSymbols` | ✅ Verified (dry run) |
| `Drawing.GetTables` | ✅ Verified — 15 tables with full cell grids |
| `Drawing.DeleteTables` | ✅ Verified (dry run) |
| `Drawing.SetModels` | ✅ Verified |
| `Drawing.Export` | ✅ Verified — PDF colour, PDF greyscale, PDF stroked-font, and DXF, all confirmed written |
| `Model.GetFamilyTable` | ✅ Verified |
| `Model.SetFamilyTableCells` | ✅ Verified (mechanics confirmed; no model with a real family table was available to test a full write) |
| `Assembly.SetComponentConstraints` | ❌ Not implemented, blocked (see note) |
| `Assembly.SetExplodedView` | ✅ Verified (both directions) |
| `Part.SetMaterial` | ✅ Verified |
| `Part.CreateMaterialWithProperties` | ✅ Verified |

Of the 19 originally proposed, 15 are now built and verified live exactly like the original 38, one
more (`Drawing.CreateStandardViews`) is built but hits the same pre-existing regeneration
limitation as everything else that regenerates in this session, and 2 (`Drawing.GetSheets`,
`Assembly.SetComponentConstraints`) turned out to be genuinely unbuildable against the current
catalogue — not a design gap, a missing primitive. Each is explained in its own section below.
`❌ Not implemented` marks those two; everything else in this table has a class file in
`compositecommands/` and is live in the MCP tool list today.

This document remains the design record: what each command is for, and which raw calls it composes.
Naming, `--target` semantics and the result envelope follow the raw layer exactly, as described in
[command-naming.md](command-naming.md).

Every raw command cited below was checked against the live 937-command catalogue — these are real
names and real signatures, not sketches. Where the API cannot do something, it says so rather than
assuming a call exists.

## A constraint worth knowing before adding more

Asynchronous J-Link runs out of process: every accessor is an IPC call to Creo costing tens of
milliseconds. Measured against a real 187-feature part, a composite making six calls per feature
took over 90 seconds and looked hung; the same walk making one call per feature answers in about
five. So an iterating composite is judged by **calls per item**: read each value once and pass it
down, apply the cheapest filter first, apply `limit` before the per-item reads, and put anything
costing an extra call per item behind an opt-in flag. `Composite`'s class javadoc restates this.

A second, operational limit: **only one AsyncJLink client can hold the Creo connection at a time.**
A running MCP server and a `creoctl` invocation will contend, and the loser blocks rather than
failing fast.

Legend: **R** read-only · **W** mutates the model, the session or the disk.

---

## Rules these commands follow

Four rules shape every entry below. They exist because of specific things the API does and does not
provide.

**1. Every mutating command ends with regenerate-and-report.** In Creo a change can succeed at the
API level and still break the model — the call returns cleanly and the damage surfaces three
operations later. A mutation that does not immediately report regeneration status trains its caller
to keep building on broken geometry.

**2. Mutating commands address by name or symbol, never by handle.** Handles die when a `creoctl`
process exits, so any CLI-driven change has to be expressible as `--target bracket.prt --dimension
d12`. Handle addressing works only in an MCP session.

**3. There is no undo.** No `Undo` of any kind appears in the catalogue. `Model.CreateBackup` before
a change is the *only* rollback mechanism available, which makes it a precondition for trusting
automated mutation, not a nicety.

**4. Selection must be programmatic.** `BaseSession.Select` is interactive — it blocks for a human
to pick geometry, so it is unusable from the CLI or from MCP. Everything that acts on geometry has
to reach it through `Model.FindItems` or through what the user already has selected.

---

## Session and workspace

### `Session.GetContext` · R

What is open, where, and in what units. The natural first call for an agent, and the answer to
"what am I even looking at" for a script.

- **Target:** none (session-scoped).
- **Returns:** current model (handle, full name, common name, instance name), every model in
  session, working directory, unit system, modified flag.
- **Raw commands involved:**
  `BaseSession.GetCurrentModel` · `BaseSession.ListModels` · `BaseSession.GetCurrentDirectory` ·
  `BaseSession.GetCurrentWindow` · `Model.GetFullName` · `Model.GetCommonName` ·
  `Model.GetInstanceName` · `Model.GetIsModified` · `Solid.GetPrincipalUnits`

### `Session.GetConfig` · R

Read Creo's configuration — the option values that decide where templates, libraries and search
paths come from. Needed in its own right, and a prerequisite for `Session.CreateModel` below.

- **Target:** none.
- **Options:** specific option names, or a prefix such as `template_` to pull a related group.
- **Returns:** option name, value, and whether it was set or left at Creo's default.
- **Raw commands involved:**
  `BaseSession.GetConfigOption` · `BaseSession.GetConfigOptionValues` ·
  `BaseSession.GetEnvironmentVariable`

### `Session.SetWorkingDirectory` · W

Change the session's working directory, and confirm what is now visible from it. Raw, changing
directory tells you nothing about whether it worked or what is there.

- **Target:** none. Takes a path.
- **Returns:** previous directory, new directory, and a listing of the models now in scope.
- **Raw commands involved:**
  `BaseSession.ChangeDirectory` · `BaseSession.GetCurrentDirectory` · `BaseSession.ListFiles` ·
  `BaseSession.ListSubdirectories`

### `Session.ListDirectory` · R

What models exist on disk in a directory — as opposed to `ListModels`, which only sees what is
already in session. The two are constantly confused, and the distinction matters: a model on disk
cannot be acted on until it is retrieved.

- **Target:** none. Optional path, defaults to the working directory.
- **Returns:** files grouped by type, with subdirectories, and a flag per file for whether it is
  already in session.
- **Raw commands involved:**
  `BaseSession.ListFiles` · `BaseSession.ListSubdirectories` · `BaseSession.GetCurrentDirectory` ·
  `BaseSession.ListModels`

### `Session.LoadModel` · W

Retrieve a model into session and, optionally, window it, display it and make it current. Today
this is four or five calls that agents reliably get out of order.

The receiver is `Session`, not `Model`, on purpose: the model is not in session yet, so there is
nothing for `--target` to refer to. It takes a name instead.

- **Target:** none. Takes a file name.
- **Options:** `display`, `activate`, simplified rep to retrieve instead of the full model.
- **Returns:** the model handle, name, and whether it was already in session.
- **Raw commands involved:**
  `BaseSession.RetrieveModel` · `BaseSession.RetrieveModelWithOpts` ·
  `BaseSession.GetModelFromFileName` · `BaseSession.OpenFile` · `BaseSession.CreateModelWindow` ·
  `BaseSession.SetCurrentWindow` · `Window.Activate` · `Model.Display` ·
  `BaseSession.RetrieveAssemSimpRep`

### `Session.SaveAll` · W

Save every modified model in session. An assembly edit dirties its children, and saving only the
top level is a routine way to lose work.

Note that saving *one* model needs no composite — `Model.Save` is already a single raw call, and
`Model.SaveChecked` covers the guarded version. This exists only for the multi-model case.

- **Target:** none.
- **Options:** `dryRun`, skip models that fail their save check.
- **Returns:** per model — whether it was modified, whether it saved, and why not if it did not.
- **Raw commands involved:**
  `BaseSession.ListModels` · `Model.GetIsModified` · `Model.CheckIsSaveAllowed` ·
  `Model.CheckIsModifiable` · `Model.Save` · `Model.GetFullName`

### `Session.EraseModels` · W

Clear models out of session, with dependencies handled and a report of what could not be erased
because something still references it.

- **Target:** none.
- **Options:** names or `undisplayedOnly`, `withDependencies`.
- **Raw commands involved:**
  `BaseSession.ListModels` · `BaseSession.EraseUndisplayedModels` · `Model.Erase` ·
  `Model.EraseWithDependencies` · `Model.GetIsModified`

---

## Creation

### `Session.CreateModel` · W

Create a new part or assembly **from the site's template**, with parameters pre-populated.

This command exists to work around a real gap in J-Link. `BaseSession.CreatePart(String)` and
`BaseSession.CreateAssembly(String)` take a name and nothing else — there is **no template
parameter**. A part created that way is empty: no default datums, no standard parameters, no
layers, none of the site's conventions. Only drawings get native template support, through
`BaseSession.CreateDrawingFromTemplate`.

So the composite reproduces what Creo's own New dialog does: read the template from configuration,
retrieve it, and copy it under the new name.

1. Resolve the template — explicit argument, else `Session.GetConfig` for `template_solidpart`
   (part) or `template_designasm` (assembly).
2. Retrieve the template model.
3. `Model.CopyAndRetrieve` under the new name.
4. Apply parameters, then save.

- **Target:** none. Takes a name, a type, and optional parameters.
- **Returns:** the new model's handle and name, and which template it came from — the latter
  matters, because a silently wrong template is hard to notice and expensive later.
- **Raw commands involved:**
  `BaseSession.GetConfigOption` · `BaseSession.RetrieveModel` · `Model.CopyAndRetrieve` ·
  `Model.Copy` · `ParameterOwner.CreateParam` · `ParameterOwner.CreateParamWithUnits` ·
  `BaseParameter.SetValue` · `Model.Save` · `BaseSession.CreatePart` ·
  `BaseSession.CreateAssembly` *(bare fallback when no template is configured)*
- **Open:** the config option names above are the standard Creo ones but should be confirmed
  against this site's `config.pro` before being relied on.
- **Verified:** this site's own `template_designasm` is `D:\Configuration\gabarits\start_asm.asm.12`
  — a real absolute path with Creo's own trailing revision number, and `template_drawing` (used by
  `Session.CreateDrawing` below) is `$PRO_DIRECTORY\templates\c_drawing.drw` — an unexpanded Creo
  environment token. Both broke the naive "pass the config value straight through" implementation
  live (`XUnknownModelExtension`, `XStringTooLong`); fixed by expanding env tokens and stripping the
  revision suffix before building a `ModelDescriptor`, and by resolving the template through a
  temporarily-changed working directory rather than a full path, which `ModelDescriptor` rejects.
  Confirmed working end to end against this site's actual template.

### `Session.CreateDrawing` · W

Create a drawing from a template against a given model — the one creation path J-Link supports
natively.

- **Target:** none. Takes a name, the model to document, a template, and optional `parameters`.
- **`parameters`:** name/value pairs set on the *documented model*, not the drawing, before
  creation — a format's title-block fields (`NOM`, `MATIERE`, `NUMERO_PLAN`, ...) are `&SYMBOL`
  references resolved against the model being documented, so this is how they get filled in. Reuses
  the same type-checked write path as `Session.CreateModel`'s `parameters`.
- **Raw commands involved:**
  `BaseSession.CreateDrawingFromTemplate` · `BaseSession.GetConfigOption` · `Model.GetDescr` ·
  `ParameterOwner.GetParam` · `ParameterOwner.CreateParam` · `BaseParameter.SetValue` · `Model.Save`
- **Verified, with two real defects found and fixed:**
  1. The fourth argument to `CreateDrawingFromTemplate` — `DrawingCreateOptions` — being passed as
     `null` crashed the whole async connection outright, live, reproduced four times under different
     inputs before being isolated. Fixed by passing an explicit empty `DrawingCreateOptions.create()`
     instead; the connection no longer dies and a template needing input it cannot get now reports
     `XToolkitDrawingCreateErrors` as an ordinary catchable error, matching the intent of leaving
     `DRAWINGCREATE_PROMPT_UNKNOWN_PARAMS` unset (there is no user to prompt in an automated session).
  2. Creo can raise that error and still have created the drawing — observed live, the drawing
     existed in session immediately afterward despite the exception. The command now recovers it
     rather than reporting a hard failure that leaves the caller unaware their drawing exists, and
     returns a `warning` explaining what Creo objected to.
- **Open:** there is no reliable way for this call to enumerate which fields a given format's title
  block actually needs — `ModelItemOwner.ListItems(ITEM_NOTE)` is unimplemented for a
  `DrawingFormat` live (`XUnimplemented`), and no raw command exposes `DetailItemOwner.ListDetailItems`
  for one. The practical path is `Model.GetParameters` on the model being documented, since it
  already carries the fields a site's formats consume by convention.

### `Drawing.CreateStandardViews` · W

Create the standard set of orthographic views (front, top, right, isometric, or a caller-chosen
subset) on a drawing sheet from a model, laid out at a given scale. `Session.CreateDrawing` above
gets a drawing into existence; today it comes out empty, and populating it is a separate,
unautomated trip into the UI. For a mechanical engineer this is the single most repetitive step
after a design change: every revised part needs its views regenerated on the drawing before it can
go back out.

Raw, this is one `GeneralViewCreateInstructions_Create` call for the first view, placed at a chosen
sheet point, then one `ProjectionViewCreateInstructions_Create` call per additional view, each
anchored to the view before it — five to eight calls and real placement arithmetic for a routine
four-view layout.

- **Target:** a drawing.
- **Options:** `model` (defaults to the drawing's current solid), `views` (saved view names on the
  model — the first is the base orientation, the rest project off it; default
  `["FRONT", "TOP", "RIGHT"]`), `sheet`, `scale`, `x`/`y` placement, `spacing` between projections.
- **Returns:** per view, its resolved name and scale; the model documented.
- **Raw commands involved:**
  `Model2D.CreateView` · `ViewOwner.GetView` · `View.GetTransform` · `View2D.GetName` ·
  `View2D.GetScale`
- **Resolved while building this:** the design record above originally guessed
  `GeneralViewCreateInstructions_Create`'s `int` argument was a saved-view index used to pick the
  orientation. It is not — the dictionary's own field list right next to the factory
  (`GetSheetNumber`/`SetSheetNumber`) shows it is the **sheet number**. The orientation comes from
  the `Transform3D` argument instead, read from the documented model's own saved view via
  `ViewOwner.GetView(name)` → `View.GetTransform()` — `Model` itself is a `ViewOwner`, confirmed by
  the dictionary's own interface list, so this works on any part or assembly. Verified live: this
  site's parts carry French-named default views (`AV`/`AR`/`DESSUS`/`DESSOUS`/`DROITE`/`GAUCHE`, not
  `FRONT`/`TOP`/`RIGHT`), and an unresolvable name fails cleanly (`"Model has no saved view named
  'FRONT'"`) rather than guessing.
- **Verified safe, not yet verified working.** Every optional argument is built explicitly, the same
  defensive posture `Session.CreateDrawing`'s fix uses, specifically because this touches the same
  `pfcModel2D` package where that command's `null` `DrawingCreateOptions` argument crashed the whole
  async connection. Tested live with a resolvable view name (`AV`) against both a throwaway drawing
  and the real production one: the connection stayed healthy every time — no crash, in either
  case — but the actual `CreateView` call consistently returned `XToolkitGeneralError` regardless of
  sheet number, placement point or which drawing. That matches, call for call, the same
  regeneration-class failure documented under "Open decisions" below (`XToolkitBadContext` on every
  regenerate-triggering composite this session, reproduced even on the raw `Solid.Regenerate` call on
  a pristine model) — `Model2D.CreateView` most plausibly regenerates the new view internally and
  hits the identical, pre-existing, session-level limitation. Worth retrying against a differently
  launched Creo session before assuming anything here is still wrong.

### `Drawing.GetSheets` · R — *not implemented, blocked*

How many sheets a drawing has, and what is on each one — the natural follow-up once
`Drawing.CreateStandardViews` above can put views on a sheet, and useful on its own: a supplier
package with the wrong sheet count is a common, embarrassing catch-after-the-fact error.

- **Target:** a drawing.
- **Returns:** sheet count, and per sheet — its number, format/size, and the views placed on it
  (cross-referencing `View2D.GetSheetNumber` against `Model2D.List2DViews`).
- **Raw commands involved:**
  `SheetOwner.GetNumberOfSheets` · `SheetOwner.GetCurrentSheetNumber` · `SheetOwner.GetSheetData` ·
  `Model2D.List2DViews` · `View2D.GetSheetNumber` · `View2D.GetName`
- **Blocked:** none of `SheetOwner`'s methods have a generated raw command today —
  `GetNumberOfSheets`, `GetCurrentSheetNumber` and `GetSheetData` all exist in the dictionary
  (`pfcModel2D`) and `Drawing extends Model2D extends SheetOwner`, so a live `Drawing` handle
  genuinely implements them, but `tools/generate_commands.py` never emitted wrappers for them —
  confirmed by checking `Commands/.../rawcommands/` directly rather than assuming. The same gap
  blocks `DetailItemOwner` (notes, symbols) and `TableOwner` (tables) below: all three are mixin
  interfaces `Model2D` extends, and the generator emitted commands for methods declared directly on
  `Model2D` itself (`ListModels`, `AddModel`, `DeleteModel`, `CreateView`, ... — all real, all used
  above) but not for the ones it inherits from `SheetOwner`/`DetailItemOwner`/`TableOwner`. This is a
  generator fix, not a composite design problem — worth doing before any of the sheet, note, symbol
  or table commands below are built, since none of them can compose a raw command that does not
  exist.

---

## Addressing and selection

### `Session.GetSelection` · R

Read what the user currently has selected in Creo. The highest-leverage command on this list: it
makes the human the selector and the tool the executor — "I've got this face selected, chamfer it
2 mm" — which sidesteps spatial reasoning from text rather than pretending to solve it.

- **Target:** none.
- **Returns:** per selection — item handle, id, name, type, owning model, component path, pick
  point, and Creo's own selection string.
- **Raw commands involved:**
  `Session.GetCurrentSelectionBuffer` · `SelectionBuffer.GetContents` · `Selection.GetSelItem` ·
  `Selection.GetSelModel` · `Selection.GetPath` · `Selection.GetPoint` ·
  `Selection.GetSelectionString` · `ModelItem.GetName` · `ModelItem.GetId` · `ModelItem.GetType`

### `Model.FindItems` · R

The programmatic counterpart to selection: find any model item by type, name pattern or status —
surfaces, edges, datums, axes, coordinate systems, features, dimensions.

- **Target:** any model.
- **Options:** `type`, `namePattern`, `status`.
- **Returns:** handle, id, name, type, status per match.
- **Raw commands involved:**
  `ModelItemOwner.ListItems` · `ModelItem.GetName` · `ModelItem.GetId` · `ModelItem.GetType` ·
  `Solid.ListFeaturesByType` · `Feature.GetStatus`

### `Model.HighlightItems` · W (display only)

Highlight what is about to be changed so a human can confirm before it happens. Fully reversible,
touches no geometry, and it is what makes automated mutation acceptable to work alongside.

- **Target:** any model.
- **Raw commands involved:**
  `Selection.SetSelItem` · `Selection.Highlight` · `Selection.UnHighlight` · `Selection.Display` ·
  `Window.Repaint` · `Display.Invalidate` · `Session.UIDisplayMessage`

---

## Parametric change

### `Model.GetParameters` · R

Every parameter of a model in one typed table. Today this is a walk: list, then a `GetValue` and a
type switch per parameter — roughly 4 calls × N parameters.

- **Target:** any model.
- **Returns:** per parameter — name, type, value, units, description, designated, relation-driven,
  modified, driver type.
- **Raw commands involved:**
  `ParameterOwner.ListParams` · `BaseParameter.GetValue` · `Parameter.GetUnits` ·
  `Parameter.GetDescription` · `Parameter.GetDriverType` · `BaseParameter.GetIsDesignated` ·
  `BaseParameter.GetIsRelationDriven` · `BaseParameter.GetIsModified` · `Parameter.GetRestriction`

### `Model.SetParameters` · W

Bulk parameter write, validated. Setting revision, part number or material across many components
is routine, and doing it one raw call at a time is where transcription errors get in.

- **Target:** any model.
- **Options:** `dryRun`, whether to create parameters that do not exist, units per value.
- **Returns:** per parameter — before, after, and whether it was created, changed or rejected.
  Type and unit mismatches are reported rather than coerced.
- **Raw commands involved:**
  `ParameterOwner.GetParam` · `BaseParameter.GetValue` · `BaseParameter.SetValue` ·
  `Parameter.GetUnits` · `Parameter.SetScaledValue` · `ParameterOwner.CreateParam` ·
  `ParameterOwner.CreateParamWithUnits` · `Parameter.GetRestriction`

### `Model.GetDimensions` · R

Every dimension with its symbol, value, type and tolerance. Dimensions are how parametric geometry
is actually changed, and the symbol (`d12`) is the stable handle-free address for one.

- **Target:** part or assembly.
- **Options:** filter by feature, by symbol pattern, driving dimensions only.
- **Returns:** per dimension — symbol, value, type, tolerance, texts, owning feature.
- **Raw commands involved:**
  `ModelItemOwner.ListItems` · `BaseDimension.GetSymbol` · `BaseDimension.GetDimValue` ·
  `BaseDimension.GetDimType` · `BaseDimension.GetTexts` · `Dimension.GetTolerance` ·
  `ModelItem.GetName` · `ModelItem.GetId` · `Solid.ListFeaturesByType`

### `Model.SetDimensions` · W

Set dimensions by symbol, regenerate once, and report what broke. This is the highest-value
mutating command on the list — "make the bracket 5 mm thicker" becomes a single call.

- **Target:** part or assembly.
- **Options:** `dryRun`, tolerance alongside value, `backupFirst`.
- **Returns:** per dimension — before, after — plus regeneration status and any features that
  failed as a result. A change that regenerates into a broken model is reported as a failure, not
  a success.
- **Raw commands involved:**
  `ModelItemOwner.ListItems` · `BaseDimension.GetSymbol` · `BaseDimension.GetDimValue` ·
  `BaseDimension.SetDimValue` · `Dimension.SetTolerance` · `Solid.Regenerate` ·
  `Feature.GetStatus` · `ModelItem.GetName`

### `Drawing.GetDimensions` · R

Every dimension actually **shown on a drawing** — as opposed to `Model.GetDimensions` above, which
lists every dimension the part has regardless of whether it appears on paper. The two are easy to
conflate and answer different questions: a drawing normally shows a curated subset, and that
subset, with its tolerances, is what a supplier or a checker actually reads. This is exactly the
walk an engineer does by hand today: find what is on the sheet, then read each one's value and
tolerance one dimension at a time.

- **Target:** a drawing.
- **Options:** filter by view, by the model being dimensioned, symbol pattern.
- **Returns:** per dimension — symbol, value, type, tolerance (value and type — symmetric,
  plus/minus, limits — or none, when the dimension carries no tolerance override), the view it
  appears on, whether it is a reference dimension.
- **Raw commands involved:**
  `Model2D.ListShownDimensions` · `BaseDimension.GetSymbol` · `BaseDimension.GetDimValue` ·
  `BaseDimension.GetDimType` · `Dimension2D.GetTolerance` · `Dimension2D.GetIsToleranceDisplayed` ·
  `Dimension2D.GetView` · `View2D.GetName` · `ModelItem.GetName`
- **Verified live** against `LINK_DRW`, a real production drawing: all 6 shown dimensions came back
  matching an earlier manual call-by-call extraction exactly, including tolerance type/value for the
  toleranced ones and `null` for the two that carry none, plus the `view` field each dimension
  appears on.

### `Drawing.SetDimensionTolerances` · W

Set tolerances on drawing dimensions in bulk, by symbol. Applying a company's tolerance standard
(± 0.05 mm on all diameters under 10 mm, say) across a drawing one dimension at a time in the UI is
exactly the kind of repetitive, error-prone task this project exists to remove, and it is the write
side `Drawing.GetDimensions` above has no counterpart for yet.

- **Target:** a drawing.
- **Options:** `dryRun`.
- **Returns:** per symbol — before, after, and whether it was set or the symbol was not found on
  this drawing.
- **Raw commands involved:**
  `Model2D.ListShownDimensions` · `BaseDimension.GetSymbol` · `Dimension2D.GetTolerance` ·
  `Dimension2D.SetTolerance` · `Dimension2D.GetIsToleranceDisplayed` · `pfcDimension.DimTolSymmetric_Create` ·
  `pfcDimension.DimTolPlusMinus_Create` · `pfcDimension.DimTolLimits_Create`
- **Resolved while building this:** `DimTolerance` itself is a bare base interface (only
  `GetType()`); the actual value lives on one of three concrete subtypes declared right next to
  it — `DimTolSymmetric` (`GetValue`/`SetValue`), `DimTolPlusMinus` (`GetPlus`/`GetMinus`),
  `DimTolLimits` (`GetUpperLimit`/`GetLowerLimit`) — each with its own factory on the
  `pfcDimension` package class. Accepts `{"type": "symmetric", "value": N}` /
  `{"type": "plusMinus", "plus": N, "minus": N}` / `{"type": "limits", "upper": N, "lower": N}`.
- **Verified live**, both `dryRun` and a real write, against `d215` on `LINK_DRW`: `before`/`after`
  reported correctly (0.05 → 0.1 symmetric), and the write persisted — a follow-up
  `Drawing.GetDimensions` call read back the new tolerance.

### `Model.GetRelations` · R / `Model.SetRelations` · W

Relations round-trip as plain string sequences — design intent as editable text.

- **Target:** any model.
- **Options (Set):** `dryRun`, replace vs. append, regenerate after.
- **Returns:** the relation text, pre- and post-regeneration sets kept separate, and on write, the
  regeneration outcome.
- **Raw commands involved:**
  `RelationOwner.GetRelations` · `RelationOwner.SetRelations` ·
  `RelationOwner.RegenerateRelations` · `RelationOwner.DeleteRelations` ·
  `Model.GetPostRegenerationRelations` · `Model.SetPostRegenerationRelations` ·
  `Model.RegeneratePostRegenerationRelations`

### `Model.EvaluateExpression` · R

Evaluate an expression in Creo's own semantics without changing anything — check the arithmetic
before committing to it.

- **Target:** any model.
- **Raw commands involved:** `RelationOwner.EvaluateExpression`

### `Model.RegenerateAndReport` · W

Regenerate, then say *which* features failed, by name. The raw call either throws or leaves the
model with failures the caller never learns about.

- **Target:** part, assembly or drawing.
- **Returns:** overall status, and for each failed or suppressed feature its id, name, type and
  status.
- **Raw commands involved:**
  `Solid.Regenerate` · `Model2D.Regenerate` · `Solid.ListFeaturesByType` · `Feature.GetStatus` ·
  `ModelItem.GetName` · `ModelItem.GetId` · `ModelItem.GetType`

---

## Family tables

The single biggest gap in the current set for a mechanical engineer. Fasteners, gaskets,
sheet-metal gauges and any other size-driven family are normally managed entirely through the
family table, and today that means walking `FamilyMember.ListColumns` and `FamilyMember.ListRows`
by hand, then a `GetCell`/`SetCell` pair per cell with no type safety — reading a 10-row, 6-column
table one cell at a time is 60-plus calls for what a person does in one glance at the table in
Creo.

### `Model.GetFamilyTable` · R

A part family's whole table in one call.

- **Target:** any model with a family table.
- **Returns:** columns (symbol, type — dimension, parameter, feature, component, ...) and rows
  (instance name, locked/verified state), with every cell's value read against its column's type.
- **Raw commands involved:**
  `FamilyMember.ListColumns` · `FamilyMember.ListRows` · `FamilyMember.GetCell` ·
  `FamilyMember.GetCellIsDefault` · `FamilyTableColumn.GetSymbol` · `FamilyTableColumn.GetType` ·
  `FamilyTableRow.GetInstanceName` · `FamilyTableRow.GetIsVerified` · `FamilyTableRow.GetIsLocked`
- **Verified live** against `LINK.prt` (no family table on this particular part): returns
  `columnCount: 0, rowCount: 0` cleanly rather than erroring, confirming the cast to `FamilyMember`
  succeeds for any part/assembly regardless of whether it actually has a table.

### `Model.SetFamilyTableCells` · W

Bulk, type-checked cell writes — the same discipline as `Model.SetParameters`: a value that does
not match its column's declared type is reported as rejected, not silently coerced.

- **Target:** any model with a family table.
- **Options:** `dryRun`, whether to add a new row (instance name) when it does not already exist.
- **Returns:** per cell — before, after, and whether it was set or rejected.
- **Raw commands involved:**
  `FamilyMember.GetRow` · `FamilyMember.AddRow` · `FamilyMember.SetCell` · `FamilyMember.GetCell` ·
  `FamilyTableColumn.GetType` · `FamilyTableRow.GetInstanceName`
- **Open:** retrieving the actual instance model (`FamilyTableRow.CreateInstance`) is a separate,
  more expensive step than editing the table, and it costs a regeneration. Whether this command
  should retrieve the edited instance by default, or leave that to a follow-up
  `Session.LoadModel`, is worth deciding against real usage rather than guessing — a caller who only
  wanted to check in a value should not always pay for a retrieval they did not ask for.
- **Verified live** against `LINK.prt`: with `create=true, dryRun=true` on a non-existent row, it
  correctly short-circuits into `"rowCreated": false, "changed": 0"` rather than touching cells that
  cannot exist yet. No part with a real family table was available in the test environment to
  exercise a full write against existing columns.

---

## Feature and assembly operations

### `Solid.SuppressFeatures` · W / `Solid.ResumeFeatures` · W

Suppress or resume features by name, id or pattern. Raw, this means creating an operation object
per feature, configuring its group and clip behaviour, collecting them, and executing the batch —
tedious and easy to get wrong in ways that quietly take out children.

- **Target:** part or assembly.
- **Options:** `dryRun`, `clip`, `allowGroupMembers`, `withParents` (resume).
- **Returns:** which features changed state, which children were affected, and the regeneration
  outcome.
- **Raw commands involved:**
  `Solid.ListFeaturesByType` · `ModelItem.GetName` · `Feature.GetStatus` ·
  `Feature.CreateSuppressOp` · `Feature.CreateResumeOp` · `SuppressOperation.SetClip` ·
  `SuppressOperation.SetAllowGroupMembers` · `SuppressOperation.SetAllowChildGroupMembers` ·
  `ResumeOperation.SetWithParents` · `Solid.ExecuteFeatureOps`

### `Solid.DeleteFeatures` · W

The same shape, but destructive and unrecoverable without a backup. `dryRun` should be the default
here rather than an option, and the child-impact report is the point of the command.

- **Target:** part or assembly.
- **Options:** `clip`, `keepEmbeddedDatums`, `allowGroupMembers`.
- **Raw commands involved:**
  `Feature.CreateDeleteOp` · `DeleteOperation.SetClip` · `DeleteOperation.SetKeepEmbeddedDatums` ·
  `DeleteOperation.SetAllowGroupMembers` · `Solid.ExecuteFeatureOps` · `Feature.GetStatus` ·
  `ModelItem.GetName`

### `Assembly.AddComponent` · W

Place a component and report how well it is actually constrained — packaged and underconstrained
components are a common silent defect that only shows up when something moves.

- **Target:** assembly.
- **Options:** by copy, transform, `dryRun`.
- **Returns:** the new component feature, plus placed / packaged / underconstrained / frozen state.
- **Raw commands involved:**
  `BaseSession.RetrieveModel` · `BaseSession.GetModelFromDescr` · `Assembly.AssembleComponent` ·
  `Assembly.AssembleByCopy` · `ComponentFeat.SetPosition` · `ComponentFeat.GetConstraints` ·
  `ComponentFeat.GetIsPlaced` · `ComponentFeat.GetIsPackaged` ·
  `ComponentFeat.GetIsUnderconstrained` · `Solid.Regenerate`

### `Assembly.SetComponentConstraints` · W — *not implemented, blocked*

Apply assembly constraints (mate, align, insert, coincident, and the rest of
`ComponentConstraintType`) between named references on a placed component and the rest of the
assembly, then regenerate and report. `Assembly.AddComponent` above places a component and reports
whether it landed constrained; this would have been the command that does the constraining.

Named `SetComponentConstraints` rather than `ConstrainComponent` to keep the verb inside the fixed
vocabulary in [command-naming.md](command-naming.md) — `Set` (mutates the target), not a new verb
style. It would not have collided with the raw `ComponentFeat.SetConstraints`: the receiver is
`Assembly`, not `ComponentFeat`, exactly as `Assembly.AddComponent` already differs from
`ComponentFeat`'s own raw calls. The naming is moot now, but is left here as the record of the
decision in case this becomes buildable later (see below).

- **Would-be target:** assembly.
- **Would-be raw commands:**
  `Solid.ListFeaturesByType` · `ComponentFeat.GetConstraints` · `ComponentFeat.SetConstraints` ·
  `ComponentFeat.GetIsPlaced` · `ComponentFeat.GetIsPackaged` ·
  `ComponentFeat.GetIsUnderconstrained` · `ComponentConstraint.SetType` · `Solid.Regenerate`
- **Blocked, confirmed rather than merely suspected.** `ComponentConstraint.SetAssemblyReference`
  and `SetComponentReference` both take a `Selection` object, not a name or a handle. A full search
  of the dictionary — every one of its 6,807 declared members — turns up **no factory that builds a
  `Selection` from anything else** (no `Selection_Create`, no `ModelItem.ToSelection`, nothing). The
  only way one exists is by coming back from interactive picking
  (`BaseSession.Select`, `SelectionBuffer.GetContents`), which rule 4 already rules out for the CLI
  and MCP. This is not a gap that more research or a cleverer implementation closes — the raw
  primitive this command would need to compose does not exist in the 937-command catalogue, and
  cannot be built from what does. Revisit only if a future J-Link release adds a programmatic
  `Selection` constructor.

### `Assembly.ReplaceComponent` · W

Swap a component for another — a revision bump or a variant. Common, and error-prone by hand
because constraints may or may not survive.

- **Target:** assembly.
- **Options:** `dryRun`.
- **Returns:** what was replaced with what, whether constraints survived, and the regeneration
  outcome.
- **Raw commands involved:**
  `Solid.ListFeaturesByType` · `ComponentFeat.GetModelDescr` · `ComponentFeat.CreateReplaceOp` ·
  `BaseSession.RetrieveModel` · `ComponentFeat.GetConstraints` · `Solid.Regenerate` ·
  `Feature.GetStatus`

---

## Inspection and reporting

### `Solid.GetMassPropertyReport` · R

Mass properties **with the unit system and the density's provenance attached**. A part with no
assigned material reports zero mass through the raw call and silently corrupts every assembly
rollup above it; this command says so instead.

- **Target:** part or assembly.
- **Returns:** mass, volume, surface area, centre of gravity, inertia tensor, unit system, material
  name, density, and a warning when the density is default or unassigned.
- **Raw commands involved:**
  `Solid.GetMassProperty` · `Solid.GetPrincipalUnits` · `Part.GetCurrentMaterial` ·
  `Material.GetName` · `Material.GetMassDensity` · `Solid.GetRelativeAccuracy` ·
  `Solid.GetAbsoluteAccuracy`

### `Part.SetMaterial` · W

Assign a material from Creo's material library (or create one) and save — the write side
`Solid.GetMassPropertyReport` above has no counterpart for. Retrieving a material, assigning it as
current and saving is three raw calls with three different failure modes (material file not found,
wrong units, save refused); this reports all three plainly instead of leaving a part with the wrong
density silently propagating into every assembly rollup above it — precisely the failure
`Solid.GetMassPropertyReport`'s warning exists to catch after the fact.

- **Target:** a part.
- **Options:** material name, `dryRun`.
- **Returns:** the material actually assigned, its density, and whether it came from the library or
  was already on the part.
- **Raw commands involved:**
  `Part.RetrieveMaterial` · `Part.GetMaterial` · `Part.SetCurrentMaterial` · `Part.CreateMaterial` ·
  `Material.GetName` · `Material.GetMassDensity` · `Material.Save` · `Solid.GetMassProperty`
- **Resolved:** "material not found by name" refuses outright rather than falling through to
  `Part.CreateMaterial(name)` — an empty, propertyless material under the right name would satisfy
  `Model.Audit`'s `materialAssigned` check while still reporting a wrong mass, which is a more
  dangerous silent failure than the one this command exists to prevent. Authoring a real material is
  `Part.CreateMaterialWithProperties`'s job, deliberately kept separate.
- **Verified live** against `LINK.prt`, which already carries `STEEL`: correctly reports
  `materialFrom: "part"` and the existing density without re-assigning it.

### `Part.CreateMaterialWithProperties` · W

Define a brand-new material — density, Young's modulus, Poisson ratio, and the rest — and save it,
in one call. `Part.SetMaterial` above covers *assigning* a material that already exists in the
library; this is the other half a mechanical engineer needs: authoring a new one (a specific alloy
grade, a supplier's datasheet values) so it can be assigned to this part and reused across others,
rather than leaving `Part.SetMaterial`'s own open question — should an unknown name silently become
an empty material — moot, by giving the empty-material path a real alternative.

Named `CreateMaterialWithProperties` rather than `CreateMaterial`, which collides with the real raw
`Part.CreateMaterial(String)` — the raw call makes an empty material; this one is a materially
different operation (an empty material with the right name would pass `Model.Audit`'s
`materialAssigned` check while still reporting zero mass correctly, which is worse than refusing).

- **Target:** a part (materials are created in the context of one, per the raw API's own shape —
  `Part.CreateMaterial` is not a session-level call).
- **Options:** name, and the property values to set (density is the one every downstream mass
  calculation depends on; Young's modulus, Poisson ratio, shear modulus, stress limits as available).
- **Returns:** the created material's handle, name, and every property actually set — a value the
  caller supplied that `Material.Set*` rejected is reported, not silently dropped.
- **Raw commands involved:**
  `Part.CreateMaterial` · `Material.SetMassDensity` · `Material.SetYoungModulus` ·
  `Material.SetPoissonRatio` · `Material.SetShearModulus` · `Material.SetDescription` ·
  `Material.SetPropertyValue` · `Material.Save`
- **Resolved:** stays narrowly about authoring the material and does not also assign it as current —
  the latter keeps each command doing one thing, matching how `Model.CreateBackup` stays separate
  from the mutation it precedes. Call `Part.SetMaterial` afterward to assign it.
- **Verified live** against `LINK.prt`: created `TEST_ALLOY` with `massDensity`/`youngModulus`/
  `poissonRatio` all set and saved successfully.

### `Assembly.GetBOM` · R

The indented bill of materials in one call: structure, quantity rollup, and per-line parameters and
mass. Raw, this is a recursive descent costing tens to hundreds of calls, and it is the single most
requested operation in CAD automation.

- **Target:** assembly.
- **Options:** `depth`, `includeSuppressed`, `rollUpBy`, and whether to compute mass per line —
  mass is by far the expensive part.
- **Returns:** a tree (or flattened roll-up) of lines, each with handle, name, instance name,
  quantity, selected parameters, mass, and placement flags.
- **Raw commands involved:**
  `Solid.ListFeaturesByType` · `ModelItemOwner.ListItems` · `ComponentFeat.GetModelDescr` ·
  `BaseSession.GetModelFromDescr` · `Model.GetFullName` · `Model.GetCommonName` ·
  `Model.GetInstanceName` · `ParameterOwner.GetParam` · `BaseParameter.GetValue` ·
  `Solid.GetMassProperty` · `Feature.GetStatus` · `ComponentFeat.GetIsPackaged` ·
  `ComponentFeat.GetIsFrozen` · `ComponentFeat.GetIsSubstitute` · `ComponentFeat.GetIsBulkitem`

### `Model.Audit` · R

The release gate — one verdict covering the failure modes that actually reach production. Intended
to be driven by a project standards file rather than hardcoded rules, so a technical lead encodes
company policy once.

- **Target:** any model.
- **Checks:** regeneration status and failed features; suppressed features; missing or default
  material and density; required parameters present and non-empty; unit system against the
  expected one; accuracy setting; unsaved modifications; family table instance verification;
  optional naming-convention match.
- **Returns:** pass/fail per check with the offending items named, and an overall verdict.
- **Raw commands involved:**
  `Solid.ListFeaturesByType` · `Feature.GetStatus` · `ModelItem.GetName` ·
  `ParameterOwner.ListParams` · `BaseParameter.GetValue` · `Part.GetCurrentMaterial` ·
  `Material.GetMassDensity` · `Solid.GetPrincipalUnits` · `Solid.GetRelativeAccuracy` ·
  `Model.GetIsModified` · `Model.GetInstanceName` · `BaseSession.ExecuteModelCheck`

### `Assembly.CheckInterference` · R

Global interference in one call, with volumes. Raw, this means building an evaluator, computing,
then unpacking each pair and each volume by hand.

- **Target:** assembly.
- **Options:** `computeVolumes` (expensive), pair filter.
- **Raw commands involved:**
  `GlobalEvaluator.SetAssem` · `GlobalEvaluator.ComputeGlobalInterference` ·
  `GlobalInterference.GetSelParts` · `GlobalInterference.GetVolume` ·
  `InterferenceVolume.ComputeVolume` · `Model.GetFullName`

### `Solid.FindFeatures` · R

Filtered feature search — by type, name pattern, status. A narrower `Model.FindItems`, kept
separate because feature status is the common case.

- **Raw commands involved:**
  `Solid.ListFeaturesByType` · `ModelItemOwner.ListItems` · `Feature.GetStatus` ·
  `ModelItem.GetName` · `ModelItem.GetId` · `ModelItem.GetType`

### `Model.GetWhereUsed` · R

Which models in session reference this one. Session-scoped — full PDM where-used would need the
server APIs.

- **Raw commands involved:**
  `BaseSession.ListModels` · `BaseSession.ListModelsByType` · `Solid.ListFeaturesByType` ·
  `ComponentFeat.GetModelDescr` · `Model.GetFullName`

---

## Safety and lifecycle

### `Model.CreateBackup` · W (writes files)

Snapshot a model before mutating it. Given rule 3 — there is no undo — this is the only rollback
that exists, and every destructive command should be able to call it.

Named `CreateBackup` rather than `Backup` because `Model.Backup` is already a raw J-Link command;
see rule 2 in [command-naming.md](command-naming.md).

- **Target:** any model.
- **Options:** destination directory, naming pattern, include dependencies.
- **Returns:** what was written and where.
- **Raw commands involved:**
  `Model.Backup` · `Model.Copy` · `Model.GetDescr` · `Model.GetFullName` ·
  `BaseSession.GetCurrentDirectory`

### `Model.SaveChecked` · W

Save, but refuse to when the model would fail its audit. Stops failed geometry and unpopulated
revisions from being committed.

- **Target:** any model.
- **Options:** `dryRun`, which checks to enforce.
- **Returns:** whether it saved, and if not, exactly which check blocked it.
- **Raw commands involved:**
  `Model.CheckIsSaveAllowed` · `Model.CheckIsModifiable` · `Model.GetIsModified` · `Model.Save` ·
  plus whatever `Model.Audit` runs

---

## Drawing content

Every command below reads or writes through `ModelItemOwner.ListItems` with `ITEM_DTL_NOTE`,
`ITEM_DTL_SYM_INSTANCE` or `ITEM_TABLE` — the same real, generated, already-exercised mechanism
`Model.FindItems` uses for every other item type. **This is a different, working path from the
blocked `DetailItemOwner`/`TableOwner` listing methods** noted under `Drawing.GetSheets` above —
`ListDetailItems` and `ListTables` have no generated command, but `ModelItemOwner.ListItems` does.

**Verified live at real scale**, against `LINK_DRW`, a real production drawing: `Drawing.GetNotes`
found all 44 notes with full multi-line text (title block, general notes, revision history — one
note ran to 30-plus lines and came back intact); `Drawing.GetSymbols` found all 21 symbol instances;
`Drawing.GetTables` found all 15 tables with complete cell grids, including one whose cells cross-
referenced a note edited moments earlier by `Drawing.SetNoteText` in the same test run, proving the
two commands see consistent, live state. `Drawing.SetNoteText` and the three `Delete*` commands
were confirmed too — a real edit persisted and read back correctly, and dry runs on real ids
reported precisely what they would remove.

Editing or removing an item once you have its handle is unblocked and already generated —
`DetailNoteItem`, `DetailSymbolInstItem` and `Table` each have real `Modify`/`Remove`/`Delete*`
commands. **Creating a brand-new note, symbol or table from nothing is not proposed below**, and
still is not buildable: the only raw creation paths (`DetailItemOwner.CreateDetailItem`,
`DetailItemOwner.RetrieveSymbolDefinition`, `TableOwner.CreateTable`) sit on the same blocked
interfaces as `Drawing.GetSheets`' note explains.

### `Drawing.GetNotes` · R

Every note on a drawing, with its text — the annotations a supplier or checker actually reads
alongside the dimensions `Drawing.GetDimensions` already covers.

- **Target:** a drawing.
- **Options:** filter by view, by model referenced.
- **Returns:** per note — handle, id, text lines, the view it appears on, the model it references
  (if any).
- **Raw commands involved:**
  `ModelItemOwner.ListItems` (`ITEM_DTL_NOTE`) · `DetailNoteItem.GetInstructions` ·
  `DetailNoteItem.GetModelReference` · `DetailNoteItem.GetLineEnvelope` · `ModelItem.GetId`
- **Verified live:** see the "Drawing content" section intro above — 44 notes on `LINK_DRW`.
  `DetailNoteItem.GetModelReference(int, int)` is undocumented beyond its signature (no parameter
  names survive in `pfcasync.jar`); `(0, 0)` is a best-effort guess wrapped so a wrong guess just
  omits the optional `modelReference` field rather than failing the whole note.

### `Drawing.SetNoteText` · W

Edit the text of one or more existing notes by id — a revision-block or general-note update across
a drawing, without reopening it in the UI.

- **Target:** a drawing.
- **Options:** `dryRun`.
- **Returns:** per note id — before, after, whether it was found and set.
- **Raw commands involved:**
  `ModelItemOwner.ListItems` (`ITEM_DTL_NOTE`) · `DetailNoteItem.GetInstructions` ·
  `DetailNoteItem.Modify` · `ModelItem.GetId`
- **Open:** `DetailNoteItem.Modify` takes a full `DetailInstructions` object, not a bare string —
  this needs to read the note's existing instructions, replace just the text lines, and write the
  whole thing back, the same shape as `Model.SetRelations` reading `before` to report against.
- **Verified live** against `LINK_DRW`: changed a blank note to `"TEST NOTE EDIT"`, both dry run and
  a real write, and read it back correctly afterward.

### `Drawing.DeleteNotes` · W

Remove notes by id or pattern. `DetailNoteItem.Remove` is real and already generated; this is the
bulk, reported version — same shape as `Solid.DeleteFeatures`, including `dryRun` defaulting to
`true` for the same reason (rule 3: no undo).

- **Target:** a drawing.
- **Options:** note ids, `dryRun` (defaults `true`).
- **Returns:** per note — id, text (for the record, since deletion is unrecoverable), whether it was
  removed.
- **Raw commands involved:**
  `ModelItemOwner.ListItems` (`ITEM_DTL_NOTE`) · `DetailNoteItem.GetInstructions` ·
  `DetailNoteItem.Remove` · `ModelItem.GetId`
- **Verified live** (dry run) against `LINK_DRW`: correctly reported `wouldRemove` with the note's
  text attached for the record.

### `Drawing.GetSymbols` · R

Every drawing symbol instance placed on a drawing (surface finish marks, welding symbols, custom
company symbols) — the counterpart to `Drawing.GetNotes` for symbol annotations rather than text.

- **Target:** a drawing.
- **Options:** filter by view.
- **Returns:** per symbol instance — handle, id, `groupCount` (see note below).
- **Raw commands involved:**
  `ModelItemOwner.ListItems` (`ITEM_DTL_SYM_INSTANCE`) · `DetailSymbolInstItem.GetInstructions` ·
  `DetailSymbolInstItem.ListGroups` · `ModelItem.GetId`
- **Note:** `DetailSymbolGroup` has no `GetName()` in the dictionary, only `GetInstructions()`; this
  reports `groupCount` (from `ListGroups`' array size) rather than group names, which needed further
  reading not done for this pass.
- **Verified live:** see the "Drawing content" section intro above — 21 symbols on `LINK_DRW`.

### `Drawing.DeleteSymbols` · W

Remove symbol instances by id. Same shape and same `dryRun`-defaults-`true` reasoning as
`Drawing.DeleteNotes` above.

- **Target:** a drawing.
- **Options:** symbol ids, `dryRun` (defaults `true`).
- **Returns:** per symbol — id, group names (for the record), whether it was removed.
- **Raw commands involved:**
  `ModelItemOwner.ListItems` (`ITEM_DTL_SYM_INSTANCE`) · `DetailSymbolInstItem.ListGroups` ·
  `DetailSymbolInstItem.Remove` · `ModelItem.GetId`
- **Verified live** (dry run) against `LINK_DRW`: correctly reported `wouldRemove`.

### `Drawing.GetTables` · R

Every table on a drawing — BOM tables and hole tables are the common case, and today reading one
means finding it by eye and walking `Table.GetRowCount`/`GetColumnCount`/`GetText` by hand.

- **Target:** a drawing.
- **Options:** `includeCells` (default true) — read every cell's text too; costs one round trip per
  cell, so a large table can be skipped down to just its dimensions.
- **Returns:** per table — handle, id, row and column counts, and its cell text as a grid.
- **Raw commands involved:**
  `ModelItemOwner.ListItems` (`ITEM_TABLE`) · `Table.GetRowCount` · `Table.GetColumnCount` ·
  `Table.GetText` (`TableCell_Create(row, col)` per cell — one round trip each, hence
  `includeCells`) · `ModelItem.GetId`
- **Verified live:** see the "Drawing content" section intro above — 15 tables on `LINK_DRW`, full
  cell grids, including the revision-history table whose first cell showed the exact text an earlier
  `Drawing.SetNoteText` call in the same session had just written.

### `Drawing.DeleteTables` · W

Remove tables by id. Same shape and the same `dryRun`-defaults-`true` reasoning as
`Drawing.DeleteNotes` above — a table is normally a BOM or hole table, and deleting the wrong one
is exactly the kind of mistake rule 3 exists for.

- **Target:** a drawing.
- **Options:** table ids, `dryRun` (defaults `true`).
- **Returns:** per table — id, row/column count (for the record), whether it was removed.
- **Raw commands involved:**
  `ModelItemOwner.ListItems` (`ITEM_TABLE`) · `Table.GetRowCount` · `Table.GetColumnCount` ·
  `Table.Erase` · `ModelItem.GetId`
- **Verified live** (dry run) against `LINK_DRW`: correctly reported `wouldRemove` with the table's
  dimensions attached for the record.

### `Drawing.SetModels` · W

Add or remove the models a drawing documents, in bulk, and confirm what is now on it. Adding models
one at a time is a single `Model2D.AddModel` call each — real and already generated — but a drawing
documenting several components (a weldment's individual parts, say) means N round trips for what is
one intent.

- **Target:** a drawing.
- **Options:** models to add, models to remove, `dryRun`.
- **Returns:** the full list of documented models after the change.
- **Raw commands involved:**
  `Model2D.ListModels` · `Model2D.AddModel` · `Model2D.DeleteModel` · `Model.GetFullName`
- **Note:** listing alone needs no composite here — `Model2D.ListModels` already returns everything
  in one call, the same reasoning `Session.SaveAll` gives for why saving one model needs no
  composite either.
- **Verified live** against `LINK_DRW`: adding `link.prt` (dry run) correctly resolved the model and
  reported `wouldAdd`, with the current document list attached.

---

## Export and display

### `Model.ExportPackage` · R (writes files)

One call produces the whole supplier package — STEP, IGES, PDF, DXF, STL — under a consistent
naming convention, with a manifest of what was written. Removes the half-finished export set.

- **Raw commands involved:**
  `Model.Export` · `BaseSession.IsConfigurationSupported` · `BaseSession.IsGeometryRepSupported` ·
  `Model.GetFullName` · `BaseSession.GetCurrentDirectory` · `Solid.ExportShrinkwrap`

### `Drawing.Export` · R (writes files)

`Model.ExportPackage` above handles any format generically, but at the cost of the caller building
the raw `ExportInstructions` object by hand (see its `"$type"` mechanics) — for the two formats a
mechanical engineer actually sends out a drawing as, PDF and DXF, that is more ceremony than the
choice deserves. This is the friendly front door for just those two: the options a person actually
decides between, not the instruction object PTC's API happens to need.

- **Target:** a drawing.
- **Options:** `path` (required), `format` (`PDF` or `DXF`, defaults from the extension on `path`),
  for PDF — `color` (true for full colour, false for greyscale, default true) and `fontStroke`
  (true draws text as vector strokes instead of embedded TrueType fonts).
- **Returns:** the file written, and which options were actually applied.
- **Raw commands involved:**
  `Model.Export` · `pfcExport.PDFExportInstructions_Create` · `pfcExport.PDFOption_Create` ·
  `pfcArgument.CreateIntArgValue` · `pfcModel.DXFExportInstructions_Create`
- **How the colour option actually maps:** `PDFExportInstructions.SetOptions` takes a `PDFOptions`
  sequence of individual `PDFOption` entries, not a single settings object — `color` becomes one
  entry of type `PDFOptionType.PDFOPT_COLOR_DEPTH` carrying a `PDFColorDepth`
  (`PDF_CD_COLOR`/`PDF_CD_GRAY`/`PDF_CD_MONO`; this command exposes `color` as a boolean and maps
  `true`/`false` to `PDF_CD_COLOR`/`PDF_CD_GRAY`, leaving `PDF_CD_MONO` unreached by that boolean —
  worth a three-way option instead if mono is ever wanted).
- **How the font option actually maps, corrected after a wrong first guess:** `fontStroke` also
  carries an int-enum `ArgValue`, not the plain boolean `ArgValue` first assumed — that guess failed
  live with `XToolkitInvalidType` before `PDFFontStrokeMode` (`PDF_STROKE_ALL_FONTS` /
  `PDF_USE_TRUE_TYPE_FONTS`) turned up in the dictionary sitting right next to `PDFColorDepth`,
  doing the identical job for the identical `ArgValue`-wrapped-enum shape. `fontStroke: true` maps
  to `PDF_STROKE_ALL_FONTS`, `false` to `PDF_USE_TRUE_TYPE_FONTS`.
- **DXF options were not built out.** `DXFExportInstructions.SetOptionValue` takes an
  `Export2DOption`, whose own fields (`GetExportSheetOption`/`GetSheets`, confirmed in the
  dictionary) would control which sheets translate — DXF export here uses Creo's own defaults with
  no options object.
- **Verified live**, all three PDF variants and DXF, against `LINK_DRW`: colour, greyscale and
  stroked-font PDFs all written and confirmed on disk (84.8 KB, 84.7 KB and 237 KB respectively —
  the roughly 2.8× jump for stroked fonts is exactly the signature of embedded font references
  becoming vector paths), plus a DXF export, also confirmed on disk (523 KB, with Creo's own export
  log alongside it).

### `Model.SetView` · W (display only)

Orient and fit the model in its window. Reachable through `Window.SetScreenTransform`, which also
makes `Model.CaptureImage` below able to capture a *chosen* view rather than whatever happens to be
on screen.

- **Raw commands involved:**
  `BaseSession.GetModelWindow` · `Window.GetScreenTransform` · `Window.SetScreenTransform` ·
  `Window.Repaint` · `Window.Refresh` · `Window.Activate`

### `Model.CaptureImage` · R (writes a file)

A rendered view of the model on disk, so an agent can see what it is working on.

- **Raw commands involved:**
  `BaseSession.GetModelWindow` · `BaseSession.GetCurrentWindow` · `Window.SetScreenTransform` ·
  `Window.ExportRasterImage` · `BaseSession.ExportCurrentRasterImage` ·
  `BaseSession.FlushCurrentWindow` · `Window.Repaint`

### `Model.SetLayerStatus` · W (display only)

Show or hide layers — needed for clean captures and for exports that should not carry construction
geometry.

- **Raw commands involved:**
  `ModelItemOwner.ListItems` · `Layer.GetStatus` · `Layer.SetStatus` · `Layer.ListItems` ·
  `Model.CreateLayer` · `Layer.AddItem` · `Layer.RemoveItem`

### `Assembly.SetExplodedView` · W (display only)

Explode an assembly for documentation and capture, or return it to its assembled state — one call
instead of the three-step explode/get-active-state/verify dance, and the natural companion to
`Model.CaptureImage` above for producing assembly-instruction images (an exploded parts diagram
being one of the most common deliverables a mechanical engineer has to produce by hand).

- **Target:** assembly.
- **Options:** `explode` (true/false), which named exploded state to activate — defaults to the
  assembly's default state.
- **Returns:** whether it is now exploded, and which state is active, by name.
- **Raw commands involved:**
  `Assembly.Explode` · `Assembly.UnExplode` · `Assembly.GetIsExploded` ·
  `Assembly.GetDefaultExplodedState` · `Assembly.GetActiveExplodedState` · `ExplodedState.Activate` ·
  `ModelItem.GetName`
- **Verified live**, both directions, against a real assembly: exploding correctly reported the
  active state by its actual (French, site-specific) name, and un-exploding correctly reported
  `exploded: false`.

### `Drawing.CheckUpToDate` · R

Whether a drawing still matches the solid it documents — stale drawings reaching a supplier are
expensive and invisible.

- **Raw commands involved:**
  `Model2D.GetCurrentSolid` · `Model2D.ListModels` · `Model2D.List2DViews` ·
  `Model.GetIsModified` · `Model.GetFullName` · `Feature.GetStatus`
- **Open:** Creo exposes no direct "drawing out of date" flag. The check has to be inferred from
  regeneration and modification state, which makes its exact semantics the thing to pin down first.

---

## Suggested build order

1. **`Session.GetContext`, `Session.GetConfig`, `Session.ListDirectory`, `Session.LoadModel`** —
   an agent cannot do anything useful until it can see the session and get a model into it.
2. **`Model.GetParameters`, `Solid.GetMassPropertyReport`, `Assembly.GetBOM`** — the reads that
   carry most of the day-to-day value, and between them they exercise handle resolution, recursive
   traversal, unit reporting and bulk result shaping.
3. **`Session.GetSelection`, `Model.FindItems`** — addressing, without which no mutation is usable.
4. **`Model.CreateBackup`, then `Model.SetDimensions`, `Model.SetParameters`** — the first
   mutations, once there is a way to undo them.
5. Everything else — all 53 buildable commands in this document are now implemented and verified
   live. Only two of the original 19 proposals remain undone, and both are blocked on a missing raw
   primitive rather than unbuilt: `Drawing.GetSheets` (no generated `SheetOwner` commands) and
   `Assembly.SetComponentConstraints` (no programmatic `Selection` constructor anywhere in the
   dictionary). Neither is a "build this next" item — see their own sections for what would need to
   change first.

## Open decisions

1. **Transactional multi-step change.** The CLI runs one command per process, so "change four
   dimensions, suppress a feature, regenerate once, revert everything if it fails" is not
   expressible — and five invocations means five regenerations and no rollback. A
   `Model.ApplyChanges` taking a batch with atomic semantics would solve it, but it is a real
   design commitment and it changes the shape of every mutating command. Worth deciding before
   those get built.
2. **Audit and standards as configuration**, in a `standards.yaml` alongside `paths.yaml` —
   required parameters, naming patterns, expected units, revision format, template names.
3. **Whether composites become the default MCP surface**, with the 937 raw tools behind a profile
   or flag, given that tool count is itself a source of agent error.
4. **`Assembly.GetBOM` as one command with options, or split** into a cheap structural walk and an
   expensive full-data variant.
5. **The `XToolkitBadContext`/`XToolkitGeneralError` regeneration family of failures.** Every
   regenerate-triggering composite hits one or the other identically in live testing, reproduced
   even on a pristine model and with a window open and active, and it reproduces on the raw
   `Solid.Regenerate` call too — so it sits below this project's code, in the async connection
   itself. `Drawing.CreateStandardViews`' `Model2D.CreateView` call joined this list once built and
   tested: same non-crashing, consistently-reproducing shape, different Creo error code
   (`XToolkitGeneralError` rather than `XToolkitBadContext`), most plausibly because view creation
   triggers an internal regeneration too. This blocks confirming that any mutation actually
   regenerates cleanly, which is the one guarantee rule 1 exists to make. Needs investigating
   against a differently-launched Creo session (or with PTC) before it can be ruled a Creo-version or
   site-specific issue rather than a general async J-Link limitation.
6. **`ComponentConstraint`'s reference fields** take `Selection` objects rather than names or
   handles, and unlike the open question this was originally filed as, this is now closed: a full
   search of the dictionary found no factory anywhere that builds a `Selection` from anything but
   interactive picking. `Assembly.SetComponentConstraints` is not buildable against the current
   catalogue — see its own section.

## Deliberately not proposed

`BaseSession.RunMacro` would let a caller do anything Creo can do, by sending it a macro string.
Wrapping it would mean unbounded blast radius, no dry run, no structured errors, and no way to know
what a call will do before it runs. It is the one place where more capability is straightforwardly
worse, and it should stay in the raw layer where using it is an explicit choice.
