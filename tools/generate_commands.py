"""Generates the Commands module's raw command layer from the J-Link dictionary.

Emits, under ``Commands/src/main/java/com/asyncjlink/commands``:

* ``rawcommands/<package>/<Receiver><Method>Command.java`` -- one file per J-Link operation
* ``RawCommandIndex.java``                                 -- the generated registry index
* ``TypeRegistry.java``                                    -- type name to kind/package lookup

Run from the repository root::

    python tools/generate_commands.py

Everything it writes is overwritten wholesale; nothing under ``rawcommands/`` should be hand-edited.
"""

from __future__ import annotations

import argparse
import shutil
import sys
from pathlib import Path

from jlink_dictionary import Dictionary

BANNER = (
    "/*\n"
    " * GENERATED FILE -- DO NOT EDIT.\n"
    " *\n"
    " * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.\n"
    " * Edit the generator, not this file; every run overwrites the whole rawcommands tree.\n"
    " */\n"
)

JAVA_KEYWORDS = {
    "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const",
    "continue", "default", "do", "double", "else", "enum", "extends", "final", "finally", "float",
    "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native",
    "new", "package", "private", "protected", "public", "return", "short", "static", "strictfp",
    "super", "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void",
    "volatile", "while", "true", "false", "null", "var", "record", "yield",
}

# Receivers that are the session itself: these commands take no `target`, they act on the connection.
SESSION_RECEIVERS = {"Session", "BaseSession"}

ROOT_PKG = "com.asyncjlink.commands"
RAW_PKG = ROOT_PKG + ".rawcommands"

# A handful of raw J-Link calls crash the whole async connection when handed a Java `null` for one
# of their String arguments, instead of raising a normal jxthrowable the way the rest of the API
# does -- confirmed live for BaseSession.ListFiles, whose two String parameters (directory,
# extension filter) J-Link itself documents as accepting "" for "current directory" / "no filter".
# The general rule that an absent String argument marshals to `null` and means "default behaviour"
# (see `is_required`) is correct for the other ~900 commands; this table overrides it for the
# specific (receiver, method, argument) tuples known to take the connection down with them, rather
# than changing that rule everywhere.
NULL_STRING_DEFAULTS: dict[tuple[str, str], dict[str, str]] = {
    ("BaseSession", "ListFiles"): {"value1": "", "value2": ""},
}


# ---------------------------------------------------------------------------
# name synthesis -- must stay identical to ParamNames.java
# ---------------------------------------------------------------------------

def decapitalise(s: str) -> str:
    if not s:
        return s
    upper = 0
    while upper < len(s) and s[upper].isupper():
        upper += 1
    if upper == 0:
        return s
    if upper == len(s):
        return s.lower()
    if upper == 1:
        return s[0].lower() + s[1:]
    return s[:upper - 1].lower() + s[upper - 1:]


def single_name(type_name: str) -> str:
    t = (type_name or "").strip().replace("[]", "")
    t = t.rsplit(".", 1)[-1]
    if not t:
        return "arg"
    if t in ("String", "int", "Integer", "double", "Double"):
        return "value"
    if t in ("boolean", "Boolean"):
        return "flag"
    if t in ("stringseq", "intseq", "realseq"):
        return "values"
    return decapitalise(t)


def param_names(type_names: list[str]) -> list[str]:
    base = [single_name(t) for t in type_names]
    total: dict[str, int] = {}
    for b in base:
        total[b] = total.get(b, 0) + 1
    out: list[str] = []
    seen: dict[str, int] = {}
    for b in base:
        if total[b] > 1:
            seen[b] = seen.get(b, 0) + 1
            out.append(f"{b}{seen[b]}")
        else:
            out.append(b)
    return out


def safe(name: str) -> str:
    return name + "_" if name in JAVA_KEYWORDS else name


# ---------------------------------------------------------------------------
# Java rendering helpers
# ---------------------------------------------------------------------------

def java_simple(d: Dictionary, type_name: str) -> str:
    """The Java type to write in generated source (simple name; the import carries the package)."""
    return type_name.replace("[]", "").strip()


def class_literal(d: Dictionary, type_name: str) -> str:
    t = java_simple(d, type_name)
    return f"{t}.class"


def schema_expr(d: Dictionary, type_name: str) -> str:
    """A ``JsonSchema`` factory call describing one parameter."""
    t = type_name.replace("[]", "").strip()
    kind = d.kind_of(t)
    if t == "String":
        return "JsonSchema.string()"
    if t in ("int", "Integer"):
        return "JsonSchema.integer()"
    if t in ("double", "Double"):
        return "JsonSchema.number()"
    if t in ("boolean", "Boolean"):
        return "JsonSchema.bool()"
    if kind == "enum":
        consts = d.classes[t].constants
        args = ", ".join(f'"{c}"' for c in consts)
        return f'JsonSchema.enumOf("{t}"{", " + args if args else ""})'
    if kind == "sequence":
        elem = d.element_type(t)
        return f'JsonSchema.sequence("{t}", {schema_expr(d, elem)})'
    if kind == "live":
        return f'JsonSchema.handle("{t}")'
    if kind == "listener":
        return f'JsonSchema.dataObject("{t}")'
    return f'JsonSchema.dataObject("{t}")'


def param_doc(d: Dictionary, type_name: str) -> str:
    t = type_name.replace("[]", "").strip()
    kind = d.kind_of(t)
    if kind == "live":
        return f"Handle to a {t}, as returned by an earlier command."
    if kind == "data":
        return f"{t} options object; its fields are passed to the pfc factory and setters."
    if kind == "sequence":
        return f"Array of {d.element_type(t)}."
    if kind == "enum":
        return f"One of the {t} constants."
    if kind == "listener":
        return f"{t} callback (not supplyable over JSON)."
    return f"{t} value."


def is_required(type_name: str) -> bool:
    """Only true primitives are required.

    Boxed types and object references are CIP's way of spelling "optional" -- passing null for a
    String or an instructions object is how a J-Link caller asks for the default behaviour, so the
    schema must allow their absence.
    """
    return type_name.strip() in ("int", "double", "boolean", "float", "long", "short", "byte", "char")


def short_pkg(pfc_pkg: str) -> str:
    pkg = pfc_pkg[3:].lower() if pfc_pkg.startswith("pfc") else pfc_pkg.lower()
    return safe(pkg)


def javadoc_escape(s: str) -> str:
    return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("@", "&#64;")


# ---------------------------------------------------------------------------
# command generation
# ---------------------------------------------------------------------------

def class_name(method) -> str:
    return f"{method.owner}{method.name}Command"


def render_command(d: Dictionary, m) -> str:
    pkg_short = short_pkg(m.pkg)
    cls = class_name(m)
    names = param_names(m.args)
    is_session = m.owner in SESSION_RECEIVERS

    # `target` is the receiver's parameter name; make sure no argument shadows it.
    if not is_session:
        names = [f"{n}_" if n == "target" else n for n in names]
    names = [safe(n) for n in names]

    listener_args = [a for a in m.args if d.kind_of(a) == "listener"]
    invocable = not listener_args

    imports: set[str] = set()
    for t in [m.owner] + m.args:
        fqn = d.java_fqn(t)
        if fqn:
            imports.add(fqn)
    if is_session:
        imports.add("com.ptc.pfc.pfcSession.Session")
    imports.add("com.ptc.cipjava.jxthrowable")

    lines: list[str] = [BANNER, f"package {RAW_PKG}.{pkg_short};", ""]
    for imp in [f"{ROOT_PKG}.Command", f"{ROOT_PKG}.CommandException", f"{ROOT_PKG}.CreoContext",
                f"{ROOT_PKG}.Marshal", "com.asyncjlink.json.JsonObject",
                "com.asyncjlink.json.JsonSchema"]:
        lines.append(f"import {imp};")
    lines.append("")
    for imp in sorted(imports):
        lines.append(f"import {imp};")
    lines.append("")

    # -- javadoc
    lines.append("/**")
    lines.append(f" * {javadoc_escape(m.owner + '.' + m.name)} &mdash; {m.pkg}.")
    lines.append(" *")
    lines.append(" * <pre>")
    lines.append(f" * {javadoc_escape(m.signature)}")
    lines.append(" * </pre>")
    if not invocable:
        lines.append(" *")
        lines.append(f" * <p>Catalogued but not invocable: it requires a live "
                     f"{javadoc_escape(listener_args[0])} callback.")
    lines.append(" */")
    lines.append(f"public final class {cls} implements Command {{")
    lines.append("")
    lines.append(f'    @Override public String name() {{ return "{m.owner}.{m.name}"; }}')
    lines.append(f'    @Override public String jlinkPackage() {{ return "{m.pkg}"; }}')
    lines.append(f'    @Override public String receiverType() {{ return "{m.owner}"; }}')
    lines.append(f'    @Override public String signature() {{ return "{m.signature}"; }}')

    if not invocable:
        reason = (f"it takes a {listener_args[0]} callback, which has no JSON representation; "
                  f"drive it from an in-process J-Link application instead")
        lines.append("")
        lines.append("    @Override public boolean isInvocable() { return false; }")
        lines.append(f'    @Override public String unsupportedReason() {{ return "{reason}"; }}')

    # -- schema
    lines.append("")
    lines.append("    @Override")
    lines.append("    public JsonSchema paramSchema() {")
    lines.append("        return JsonSchema.object()")
    desc = f"{m.owner}.{m.name} \\u2014 {m.pkg}"
    lines.append(f'                .describedAs("{desc}")')
    if not is_session:
        lines.append(f'                .required("target", JsonSchema.handle("{m.owner}"),')
        lines.append(f'                        "The {m.owner} to act on.")')
    for name, t in zip(names, m.args):
        kw = "required" if is_required(t) else "optional"
        lines.append(f'                .{kw}("{name}", {schema_expr(d, t)},')
        lines.append(f'                        "{param_doc(d, t)}")')
    lines.append("                ;")
    lines.append("    }")

    # -- execute
    lines.append("")
    lines.append("    @Override")
    lines.append("    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {")
    if not invocable:
        lines.append("        throw CommandException.unsupported(")
        lines.append(f'                "{m.owner}.{m.name}", unsupportedReason());')
        lines.append("    }")
        lines.append("}")
        return "\n".join(lines) + "\n"

    if is_session:
        lines.append("        Session target = ctx.session();")
    else:
        owner_java = java_simple(d, m.owner)
        lines.append(f"        {owner_java} target = Marshal.in(ctx, params.get(\"target\"),")
        lines.append(f'                "{m.owner}", {class_literal(d, m.owner)}, "target");')
        lines.append("        if (target == null) {")
        lines.append('            throw new CommandException("Field \'target\' is required", "invalid_params");')
        lines.append("        }")

    null_defaults = NULL_STRING_DEFAULTS.get((m.owner, m.name), {})
    for name, t in zip(names, m.args):
        jt = java_simple(d, t)
        lines.append(f"        {jt} {name} = Marshal.in(ctx, params.get(\"{name}\"),")
        lines.append(f'                "{t}", {class_literal(d, t)}, "{name}");')
        if name in null_defaults:
            lines.append(f"        if ({name} == null) {{")
            lines.append(f'            {name} = "{null_defaults[name]}"; // avoids crashing the async connection')
            lines.append("        }")

    call = f"target.{m.name}({', '.join(names)})"
    if m.ret == "void":
        lines.append(f"        {call};")
        lines.append("        return Marshal.ok();")
    else:
        lines.append(f'        return Marshal.result(ctx, {call}, "{m.ret}");')
    lines.append("    }")
    lines.append("}")
    return "\n".join(lines) + "\n"


# ---------------------------------------------------------------------------
# TypeRegistry
# ---------------------------------------------------------------------------

KIND_JAVA = {
    "primitive": "PRIMITIVE", "void": "VOID", "enum": "ENUM", "sequence": "SEQUENCE",
    "live": "LIVE", "data": "DATA", "listener": "LISTENER", "exception": "UNKNOWN",
    "unknown": "UNKNOWN",
}


def render_type_registry(d: Dictionary) -> str:
    rows: list[tuple[str, str, str]] = []
    for name in sorted(d.classes):
        cls = d.classes[name]
        rows.append((name, cls.pkg, KIND_JAVA[d.kind_of(name)]))
    for name in ("stringseq", "intseq", "realseq"):
        rows.append((name, "cipjava", "SEQUENCE"))

    chunk = 120
    chunks = [rows[i:i + chunk] for i in range(0, len(rows), chunk)]

    out = [BANNER, f"package {ROOT_PKG};", ""]
    out += [
        "import java.util.HashMap;",
        "import java.util.Map;",
        "import java.util.concurrent.ConcurrentHashMap;",
        "",
        "/**",
        " * Every type the J-Link dictionary declares, with the classification that decides how it",
        f" * crosses the JSON boundary. {len(rows)} entries.",
        " *",
        " * <p>Registration is split across several methods because a single static initialiser "
        "holding",
        " * this many entries would exceed the JVM's 64KB per-method bytecode limit.",
        " */",
        "public final class TypeRegistry {",
        "",
        "    private TypeRegistry() {",
        "    }",
        "",
        "    private static final Map<String, TypeKind> KINDS = new HashMap<>();",
        "    private static final Map<String, String> PACKAGES = new HashMap<>();",
        "    private static final Map<String, Class<?>> CLASSES = new ConcurrentHashMap<>();",
        "",
        "    static {",
    ]
    for i in range(len(chunks)):
        out.append(f"        register{i}();")
    out.append("    }")
    out.append("")
    for i, rows_chunk in enumerate(chunks):
        out.append(f"    private static void register{i}() {{")
        for name, pkg, kind in rows_chunk:
            out.append(f'        put("{name}", "{pkg}", TypeKind.{kind});')
        out.append("    }")
        out.append("")
    out += [
        "    private static void put(String name, String pkg, TypeKind kind) {",
        "        KINDS.put(name, kind);",
        "        PACKAGES.put(name, pkg);",
        "    }",
        "",
        "    /** How {@code typeName} travels over JSON. Unregistered names report {@code UNKNOWN}. */",
        "    public static TypeKind kind(String typeName) {",
        "        if (typeName == null) {",
        "            return TypeKind.UNKNOWN;",
        "        }",
        "        switch (typeName) {",
        '            case "void":',
        "                return TypeKind.VOID;",
        '            case "String":',
        '            case "int":',
        '            case "Integer":',
        '            case "double":',
        '            case "Double":',
        '            case "boolean":',
        '            case "Boolean":',
        "                return TypeKind.PRIMITIVE;",
        "            default:",
        "                break;",
        "        }",
        "        TypeKind k = KINDS.get(typeName);",
        "        return k == null ? TypeKind.UNKNOWN : k;",
        "    }",
        "",
        "    /** The J-Link package a type belongs to, e.g. {@code pfcSolid}. */",
        "    public static String packageOf(String typeName) {",
        "        return PACKAGES.get(typeName);",
        "    }",
        "",
        "    /** The fully-qualified Java name, or {@code null} for primitives and java.lang types. */",
        "    public static String javaFqn(String typeName) {",
        "        String pkg = PACKAGES.get(typeName);",
        "        if (pkg == null) {",
        "            return null;",
        "        }",
        '        if (pkg.equals("cipjava")) {',
        '            return "com.ptc.cipjava." + typeName;',
        "        }",
        '        return "com.ptc.pfc." + pkg + "." + typeName;',
        "    }",
        "",
        "    /** Resolves and caches the {@link Class} for a J-Link type name. */",
        "    public static Class<?> classFor(String typeName) {",
        "        Class<?> cached = CLASSES.get(typeName);",
        "        if (cached != null) {",
        "            return cached;",
        "        }",
        "        String fqn = javaFqn(typeName);",
        "        if (fqn == null) {",
        "            throw new CommandException(",
        '                    "Unknown J-Link type \'" + typeName + "\'", "internal");',
        "        }",
        "        try {",
        "            Class<?> c = Class.forName(fqn);",
        "            CLASSES.put(typeName, c);",
        "            return c;",
        "        } catch (ClassNotFoundException e) {",
        "            throw new CommandException(",
        '                    "pfcasync.jar does not contain " + fqn + ". Check that the jar on the '
        'classpath matches the Creo version in paths.yaml.",',
        '                    "internal", e);',
        "        }",
        "    }",
        "",
        "    /** The {@code pfcXxx} class that holds a package's static factories. */",
        "    public static Class<?> packageClassFor(String typeName) {",
        "        String pkg = PACKAGES.get(typeName);",
        '        if (pkg == null || pkg.equals("cipjava")) {',
        "            return null;",
        "        }",
        "        try {",
        '            return Class.forName("com.ptc.pfc." + pkg + "." + pkg);',
        "        } catch (ClassNotFoundException e) {",
        "            return null;",
        "        }",
        "    }",
        "",
        "    /** Number of registered types; used by the build-time consistency check. */",
        "    public static int size() {",
        "        return KINDS.size();",
        "    }",
        "}",
    ]
    return "\n".join(out) + "\n"


# ---------------------------------------------------------------------------
# RawCommandIndex
# ---------------------------------------------------------------------------

def render_index(d: Dictionary, methods) -> str:
    chunk = 100
    groups = [methods[i:i + chunk] for i in range(0, len(methods), chunk)]
    out = [BANNER, f"package {ROOT_PKG};", ""]
    out += [
        "import java.util.ArrayList;",
        "import java.util.List;",
        "",
        "/**",
        " * The generated index of every raw command.",
        " *",
        f" * <p>{len(methods)} commands across {len({m.pkg for m in methods})} J-Link packages, one",
        " * per operation the dictionary declares on a live Creo object.",
        " *",
        " * <p>Registration is split across several methods to stay under the JVM's 64KB per-method",
        " * bytecode limit.",
        " */",
        "public final class RawCommandIndex {",
        "",
        "    private RawCommandIndex() {",
        "    }",
        "",
        f"    /** Total number of generated commands: {len(methods)}. */",
        f"    public static final int COUNT = {len(methods)};",
        "",
        "    /** Instantiates every generated command. */",
        "    public static List<Command> all() {",
        f"        List<Command> out = new ArrayList<>({len(methods)});",
    ]
    for i in range(len(groups)):
        out.append(f"        add{i}(out);")
    out.append("        return out;")
    out.append("    }")
    out.append("")
    for i, group in enumerate(groups):
        out.append(f"    private static void add{i}(List<Command> out) {{")
        for m in group:
            fq = f"{RAW_PKG}.{short_pkg(m.pkg)}.{class_name(m)}"
            out.append(f"        out.add(new {fq}());")
        out.append("    }")
        out.append("")
    out.append("}")
    return "\n".join(out) + "\n"


# ---------------------------------------------------------------------------
# entry point
# ---------------------------------------------------------------------------

def main(argv: list[str]) -> int:
    ap = argparse.ArgumentParser(description=__doc__)
    ap.add_argument("--root", default=str(Path(__file__).resolve().parent.parent),
                    help="repository root (default: parent of tools/)")
    ap.add_argument("--check", action="store_true",
                    help="report what would be written without touching the tree")
    args = ap.parse_args(argv)

    root = Path(args.root)
    doc = root / "docs" / "jlink-api-asynchronous.md"
    if not doc.exists():
        print(f"error: dictionary not found at {doc}", file=sys.stderr)
        return 2

    d = Dictionary(doc)
    methods = d.commands()
    base = root / "Commands" / "src" / "main" / "java" / "com" / "asyncjlink" / "commands"
    raw = base / "rawcommands"

    packages = sorted({short_pkg(m.pkg) for m in methods})
    listener_blocked = [m for m in methods
                        if any(d.kind_of(a) == "listener" for a in m.args)]

    print(f"dictionary : {doc.relative_to(root)}")
    print(f"types      : {len(d.classes)}")
    print(f"commands   : {len(methods)} across {len(packages)} packages")
    print(f"  invocable: {len(methods) - len(listener_blocked)}")
    print(f"  listener : {len(listener_blocked)} (catalogued, not invocable)")

    if args.check:
        return 0

    if raw.exists():
        shutil.rmtree(raw)
    for p in packages:
        (raw / p).mkdir(parents=True, exist_ok=True)

    written = 0
    for m in methods:
        path = raw / short_pkg(m.pkg) / f"{class_name(m)}.java"
        path.write_text(render_command(d, m), encoding="utf-8")
        written += 1

    (base / "TypeRegistry.java").write_text(render_type_registry(d), encoding="utf-8")
    (base / "RawCommandIndex.java").write_text(render_index(d, methods), encoding="utf-8")

    print(f"wrote      : {written} command files + TypeRegistry.java + RawCommandIndex.java")
    print(f"             under {raw.relative_to(root)}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main(sys.argv[1:]))
