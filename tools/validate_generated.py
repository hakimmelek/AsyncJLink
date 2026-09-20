"""Structural checks on the generated Java.

No JDK is required to run this, so it is the first line of defence on a machine without Creo. It does
not replace compilation -- it catches the mistakes a template generator actually makes: unbalanced
delimiters, a class name that disagrees with its file, a type used without an import, a duplicate
registration, an index that has drifted from the tree on disk.

    python tools/validate_generated.py
"""

from __future__ import annotations

import re
import sys
from collections import Counter
from pathlib import Path

from jlink_dictionary import Dictionary

ROOT = Path(__file__).resolve().parent.parent
BASE = ROOT / "Commands" / "src" / "main" / "java" / "com" / "asyncjlink" / "commands"
RAW = BASE / "rawcommands"

# Identifiers that appear in generated source but are never imported: java.lang, primitives, and the
# handful of names that are only ever string literals.
BUILTIN = {
    "String", "Integer", "Double", "Boolean", "Object", "Override", "Class",
    "int", "double", "boolean", "float", "long", "short", "byte", "char", "void",
    "public", "final", "class", "implements", "return", "throws", "import", "package",
    "new", "if", "null", "static",
}

errors: list[str] = []
warnings: list[str] = []


def err(msg: str) -> None:
    errors.append(msg)


def check_delimiters(path: Path, text: str) -> None:
    """Balanced braces/parens outside comments and literals.

    Character literals matter as much as strings here: `sb.append('}')` is ordinary Java and would
    otherwise read as an unbalanced brace.
    """
    depth = {"{": 0, "(": 0}
    pairs = {"}": "{", ")": "("}
    i = 0
    in_str = in_char = in_line_comment = in_block_comment = False
    while i < len(text):
        c = text[i]
        nxt = text[i + 1] if i + 1 < len(text) else ""
        if in_line_comment:
            if c == "\n":
                in_line_comment = False
        elif in_block_comment:
            if c == "*" and nxt == "/":
                in_block_comment = False
                i += 1
        elif in_str:
            if c == "\\":
                i += 1
            elif c == '"':
                in_str = False
        elif in_char:
            if c == "\\":
                i += 1
            elif c == "'":
                in_char = False
        else:
            if c == "/" and nxt == "/":
                in_line_comment = True
                i += 1
            elif c == "/" and nxt == "*":
                in_block_comment = True
                i += 1
            elif c == '"':
                in_str = True
            elif c == "'":
                in_char = True
            elif c in depth:
                depth[c] += 1
            elif c in pairs:
                depth[pairs[c]] -= 1
                if depth[pairs[c]] < 0:
                    err(f"{path.name}: unbalanced '{c}'")
                    return
        i += 1
    for k, v in depth.items():
        if v != 0:
            err(f"{path.name}: {v} unclosed '{k}'")
    if in_str:
        err(f"{path.name}: unterminated string literal")
    if in_char:
        err(f"{path.name}: unterminated character literal")


def check_file(path: Path, d: Dictionary) -> tuple[str, str]:
    text = path.read_text(encoding="utf-8")
    check_delimiters(path, text)

    m = re.search(r"^package ([\w.]+);", text, re.M)
    if not m:
        err(f"{path.name}: no package declaration")
        return "", ""
    pkg = m.group(1)

    expected_pkg = "com.asyncjlink.commands.rawcommands." + path.parent.name
    if pkg != expected_pkg:
        err(f"{path.name}: package '{pkg}' does not match directory (expected '{expected_pkg}')")

    m = re.search(r"^public final class (\w+) implements Command \{", text, re.M)
    if not m:
        err(f"{path.name}: no 'public final class ... implements Command' declaration")
        return pkg, ""
    cls = m.group(1)
    if cls != path.stem:
        err(f"{path.name}: class '{cls}' does not match file name")

    # Every J-Link type used as a Java token must be imported.
    imports = set(re.findall(r"^import ([\w.]+);", text, re.M))
    imported_simple = {i.rsplit(".", 1)[-1] for i in imports}
    body = text.split("public final class", 1)[1]
    body = re.sub(r'"(?:[^"\\]|\\.)*"', '""', body)  # drop string literals
    # J-Link method names are PascalCase too (target.GetMassProperty), so member access has to go
    # before type tokens can be identified. `Solid.class` survives as the bare receiver `Solid`.
    body = re.sub(r"\.\w+", "", body)
    for token in set(re.findall(r"\b([A-Z]\w+)\b", body)):
        if token in BUILTIN or token in imported_simple or token == cls:
            continue
        err(f"{path.name}: uses '{token}' without an import")

    # The five methods the Command contract requires.
    for required in ("public String name()", "public String jlinkPackage()",
                     "public String receiverType()", "public String signature()",
                     "public JsonSchema paramSchema()", "public JsonObject execute("):
        if required not in text:
            err(f"{path.name}: missing {required}")

    m = re.search(r'public String name\(\) \{ return "([^"]+)"; \}', text)
    return pkg, (m.group(1) if m else "")


def main() -> int:
    if not RAW.exists():
        print("error: rawcommands/ not generated; run tools/generate_commands.py first",
              file=sys.stderr)
        return 2

    d = Dictionary(ROOT / "docs" / "jlink-api-asynchronous.md")
    expected = d.commands()

    files = sorted(RAW.rglob("*.java"))
    print(f"checking {len(files)} generated command files ...")

    names: list[str] = []
    fqns: list[str] = []
    for path in files:
        pkg, name = check_file(path, d)
        if name:
            names.append(name)
        if pkg:
            fqns.append(f"{pkg}.{path.stem}")

    for label, values in (("command name", names), ("class", fqns)):
        dupes = [v for v, n in Counter(values).items() if n > 1]
        if dupes:
            err(f"duplicate {label}: {dupes[:5]}")

    if len(files) != len(expected):
        err(f"file count {len(files)} != dictionary command count {len(expected)}")

    expected_names = {f"{m.owner}.{m.name}" for m in expected}
    missing = expected_names - set(names)
    extra = set(names) - expected_names
    if missing:
        err(f"{len(missing)} dictionary commands have no file, e.g. {sorted(missing)[:5]}")
    if extra:
        err(f"{len(extra)} generated commands are not in the dictionary, e.g. {sorted(extra)[:5]}")

    # The index must instantiate exactly the classes on disk.
    index = (BASE / "RawCommandIndex.java").read_text(encoding="utf-8")
    registered = set(re.findall(r"out\.add\(new ([\w.]+)\(\)\);", index))
    if registered != set(fqns):
        only_index = sorted(registered - set(fqns))[:5]
        only_disk = sorted(set(fqns) - registered)[:5]
        err(f"RawCommandIndex disagrees with the tree "
            f"(index-only: {only_index}, disk-only: {only_disk})")
    m = re.search(r"COUNT = (\d+);", index)
    if m and int(m.group(1)) != len(files):
        err(f"RawCommandIndex.COUNT={m.group(1)} but {len(files)} files exist")

    registry = (BASE / "TypeRegistry.java").read_text(encoding="utf-8")
    check_delimiters(BASE / "TypeRegistry.java", registry)
    check_delimiters(BASE / "RawCommandIndex.java", index)
    declared = len(re.findall(r'^        put\("', registry, re.M))
    print(f"TypeRegistry : {declared} types")
    print(f"RawCommandIndex : {len(registered)} registrations")

    # The hand-written modules get the same delimiter check. It is cheap, and on a machine with no
    # JDK it is the only thing standing between a stray brace and a failed build somewhere else.
    hand = [p for p in sorted(ROOT.rglob("*.java")) if "rawcommands" not in p.parts]
    for path in hand:
        text = path.read_text(encoding="utf-8")
        check_delimiters(path, text)
        m = re.search(r"^package ([\w.]+);", text, re.M)
        if not m:
            err(f"{path.name}: no package declaration")
        elif not str(path.parent).replace("\\", "/").endswith(m.group(1).replace(".", "/")):
            err(f"{path.name}: package '{m.group(1)}' does not match its directory")
    print(f"hand-written : {len(hand)} files")

    for w in warnings:
        print(f"  warn: {w}")
    if errors:
        print(f"\nFAILED with {len(errors)} error(s):", file=sys.stderr)
        for e in errors[:40]:
            print(f"  - {e}", file=sys.stderr)
        if len(errors) > 40:
            print(f"  ... and {len(errors) - 40} more", file=sys.stderr)
        return 1
    print(f"\nOK: {len(files)} files, {len(set(names))} unique commands, no structural errors.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
