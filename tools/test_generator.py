"""Tests for the dictionary parser and the code generator.

Runs without a JDK and without Creo, so it is the check that works on a development machine:

    python tools/test_generator.py
"""

from __future__ import annotations

import json
import sys
from pathlib import Path

from jlink_dictionary import Dictionary
import generate_commands as gen

ROOT = Path(__file__).resolve().parent.parent
DOC = ROOT / "docs" / "jlink-api-asynchronous.md"

# One fixture, read by both implementations of the parameter-name algorithm: this suite reads it from
# disk, and `creoctl --selfcheck` reads the same file as a classpath resource.
PARAMNAME_CASES = (ROOT / "Commands" / "src" / "main" / "resources" / "com" / "asyncjlink"
                   / "commands" / "paramname-cases.json")

failures: list[str] = []
checks = 0


def check(condition: bool, label: str) -> None:
    global checks
    checks += 1
    if not condition:
        failures.append(label)


def eq(actual, expected, label: str) -> None:
    global checks
    checks += 1
    if actual != expected:
        failures.append(f"{label}: expected {expected!r}, got {actual!r}")


# ---------------------------------------------------------------------------

def test_param_names() -> None:
    cases = json.loads(PARAMNAME_CASES.read_text(encoding="utf-8"))
    for type_name, expected in cases["single"].items():
        eq(gen.single_name(type_name), expected, f"single_name({type_name})")
    for case in cases["lists"]:
        eq(gen.param_names(case["types"]), case["names"], f"param_names({case['types']})")


def test_decapitalise() -> None:
    eq(gen.decapitalise("Model"), "model", "decapitalise(Model)")
    eq(gen.decapitalise("ExportInstructions"), "exportInstructions", "decapitalise(ExportInstructions)")
    eq(gen.decapitalise("UVParams"), "uvParams", "decapitalise(UVParams)")
    eq(gen.decapitalise("OId"), "oId", "decapitalise(OId)")
    eq(gen.decapitalise("UDF"), "udf", "decapitalise(UDF)")
    eq(gen.decapitalise(""), "", "decapitalise('')")


def test_dictionary(d: Dictionary) -> None:
    eq(len(d.classes), 927, "class count")

    # The counts stated in the dictionary's own header, recomputed independently.
    total_members = sum(len(c.members) for c in d.classes.values())
    eq(total_members, 6807, "member count")

    # Spot-check the classification, one per kind.
    eq(d.kind_of("Solid"), "live", "Solid is live")
    eq(d.kind_of("Session"), "live", "Session is live")
    eq(d.kind_of("ModelType"), "enum", "ModelType is an enum")
    eq(d.kind_of("Selections"), "sequence", "Selections is a sequence")
    eq(d.kind_of("stringseq"), "sequence", "stringseq is a sequence")
    eq(d.kind_of("ExportInstructions"), "data", "ExportInstructions is data")
    eq(d.kind_of("DisplayListener"), "listener", "DisplayListener is a listener")

    # `Object` in this dictionary is com.ptc.pfc.pfcObject.Object, not java.lang.Object. Getting
    # this wrong silently drops a whole package's commands.
    eq(d.kind_of("Object"), "live", "pfcObject.Object is live, not java.lang.Object")
    eq(d.java_fqn("Object"), "com.ptc.pfc.pfcObject.Object", "Object resolves to the pfc type")

    eq(d.java_fqn("Solid"), "com.ptc.pfc.pfcSolid.Solid", "Solid FQN")
    eq(d.java_fqn("stringseq"), "com.ptc.cipjava.stringseq", "stringseq FQN")
    eq(d.java_fqn("String"), None, "String has no FQN")

    eq(d.element_type("Selections"), "Selection", "Selections element type")
    eq(d.element_type("stringseq"), "String", "stringseq element type")
    eq(d.element_type("Point3D"), "double", "Point3D element type")


def test_commands(d: Dictionary) -> None:
    commands = d.commands()
    check(len(commands) > 900, f"expected >900 commands, got {len(commands)}")

    # Command identity is Receiver.Method, and it must be globally unique -- the generated class
    # names and the registry both depend on it.
    names = [f"{m.owner}.{m.name}" for m in commands]
    eq(len(names), len(set(names)), "command names are unique")

    classes = [gen.class_name(m) for m in commands]
    eq(len(classes), len(set(classes)), "generated class names are unique")

    # Every type the command surface mentions must resolve, or generation emits an uncompilable
    # reference to a class that does not exist.
    unresolved = set()
    for m in commands:
        for t in m.args + [m.ret]:
            if d.kind_of(t) == "unknown":
                unresolved.add(t)
    eq(sorted(unresolved), [], "all parameter and return types resolve")

    # Known operations that must be present, including the ones the architecture sketch names.
    for expected in ("Solid.GetMassProperty", "Solid.Regenerate", "Model.Export", "Model.Import",
                     "Model.Save", "BaseSession.GetModel", "BaseSession.ListModels",
                     "BaseSession.ImportNewModel", "Assembly.AssembleComponent",
                     "BaseSession.ExecuteModelCheck"):
        check(expected in names, f"{expected} is in the command set")

    # Regenerate is declared on Solid, not on Model; asserting the real shape keeps the test honest.
    check("Model.Regenerate" not in names,
          "Regenerate is not on Model (it is inherited from Solid)")


def test_rendering(d: Dictionary) -> None:
    by_name = {f"{m.owner}.{m.name}": m for m in d.commands()}

    # A receiver-plus-argument command.
    src = gen.render_command(d, by_name["Solid.GetMassProperty"])
    check("class SolidGetMassPropertyCommand implements Command" in src, "class declaration")
    check("import com.ptc.pfc.pfcSolid.Solid;" in src, "receiver import")
    check('return "Solid.GetMassProperty";' in src, "command name")
    check("target.GetMassProperty(value)" in src, "the call is emitted")
    check('Marshal.result(ctx, target.GetMassProperty(value), "MassProperty")' in src,
          "return type is carried to the marshaller")

    # A session command takes no target.
    src = gen.render_command(d, by_name["BaseSession.ListModels"])
    check("Session target = ctx.session();" in src, "session receiver")
    check('.required("target"' not in src, "session commands have no target parameter")

    # A void command returns the empty envelope.
    src = gen.render_command(d, by_name["Model.Save"])
    check("return Marshal.ok();" in src, "void command returns ok()")

    # A listener command is catalogued but refuses to run.
    src = gen.render_command(d, by_name["Session.UICreateCommand"])
    check("isInvocable() { return false; }" in src, "listener command is not invocable")
    check("throw CommandException.unsupported(" in src, "listener command throws")

    # Enum parameters publish their constants.
    src = gen.render_command(d, by_name["BaseSession.GetModel"])
    check('JsonSchema.enumOf("ModelType"' in src, "enum schema")
    check('"MDL_PART"' in src, "enum constants are inlined")

    # Only true primitives are required; a nullable String is not.
    src = gen.render_command(d, by_name["Solid.GetMassProperty"])
    check('.optional("value", JsonSchema.string()' in src,
          "String parameters are optional, matching CIP nullability")


def test_generated_tree_matches(d: Dictionary) -> None:
    """The checked-in tree must be what the generator produces right now."""
    base = ROOT / "Commands" / "src" / "main" / "java" / "com" / "asyncjlink" / "commands"
    raw = base / "rawcommands"
    if not raw.exists():
        failures.append("rawcommands/ has not been generated")
        return

    commands = d.commands()
    on_disk = sorted(p.stem for p in raw.rglob("*.java"))
    expected = sorted(gen.class_name(m) for m in commands)
    eq(on_disk, expected, "files on disk match the dictionary")

    # Re-render a sample and compare byte for byte, so a generator change that was never re-run is
    # caught here rather than at compile time.
    drifted = []
    for m in commands[::97]:
        path = raw / gen.short_pkg(m.pkg) / f"{gen.class_name(m)}.java"
        if path.read_text(encoding="utf-8") != gen.render_command(d, m):
            drifted.append(path.name)
    eq(drifted, [], "checked-in files match current generator output")


def main() -> int:
    if not DOC.exists():
        print(f"error: {DOC} not found", file=sys.stderr)
        return 2
    d = Dictionary(DOC)

    test_param_names()
    test_decapitalise()
    test_dictionary(d)
    test_commands(d)
    test_rendering(d)
    test_generated_tree_matches(d)

    if failures:
        print(f"FAILED {len(failures)} of {checks} checks:\n", file=sys.stderr)
        for f in failures:
            print(f"  - {f}", file=sys.stderr)
        return 1
    print(f"OK: {checks} checks passed.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
