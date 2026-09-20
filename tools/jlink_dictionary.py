"""Parses docs/jlink-api-asynchronous.md into a typed model of the J-Link API.

The dictionary is generated with ``javap -public`` straight from ``pfcasync.jar``, so it is an exact
record of the compiled surface: every method, parameter type and declared exception. What it does not
carry is parameter *names* -- ``pfcasync.jar`` is built without ``MethodParameters`` -- which is why
:mod:`generate_commands` synthesises them from types instead.

This module is import-only; :mod:`generate_commands` is the entry point.
"""

from __future__ import annotations

import re
from dataclasses import dataclass, field
from pathlib import Path

# Types referenced by the API but declared outside it. Verified exhaustive against the dictionary:
# every other referenced type resolves to a documented pfc* class.
CIPJAVA = {"stringseq", "intseq", "realseq"}
# NB: no "Object" here. In this dictionary a bare `Object` is com.ptc.pfc.pfcObject.Object -- a real
# J-Link interface with its own members -- not java.lang.Object.
JAVA_LANG = {"String", "Boolean", "Integer", "Double"}
PRIMITIVES = {"int", "double", "boolean", "float", "long", "short", "byte", "char", "void"}

# A class is a live Creo object -- something obtained from the session rather than constructed by the
# caller -- when it inherits from one of these. Everything else that is not an enum, sequence,
# exception or listener is an instruction/options struct.
LIVE_ROOTS = {
    "Model", "ModelItem", "Solid", "Session", "BaseSession", "Feature", "Object",
    "Window", "View", "View2D", "Layer", "Table", "Dimension", "Model2D", "Part", "Assembly",
}

_HEADING = re.compile(r"^## (\w+)\s*$")
_CLASS = re.compile(r"^#### `([^`]+)`\s*$")
_MEMBER = re.compile(r"^\| `(.+)` \|\s*$")
_SIGNATURE = re.compile(
    r"^(?P<mods>(?:static |native |final |abstract )*)"
    r"(?P<ret>[\w\[\]<>., ]+?)\s+(?P<name>\w+)\((?P<args>.*?)\)"
    r"(?: throws (?P<throws>.+))?$"
)
_CONSTANT = re.compile(r"^static final ")


@dataclass
class Method:
    owner: str
    pkg: str
    name: str
    ret: str
    args: list[str]
    static: bool
    signature: str


@dataclass
class JClass:
    name: str
    pkg: str
    decl: str
    members: list[str] = field(default_factory=list)
    methods: list[Method] = field(default_factory=list)
    constants: list[str] = field(default_factory=list)
    parents: set[str] = field(default_factory=set)

    @property
    def kind(self) -> str:
        if "implements jxenum" in self.decl:
            return "enum"
        if "extends jxobject_i" in self.decl:
            return "seq"
        if self.decl.startswith("interface"):
            return "interface"
        return "class"


class Dictionary:
    """The whole parsed API."""

    def __init__(self, path: Path):
        self.path = Path(path)
        self.classes: dict[str, JClass] = {}
        self.packages: dict[str, list[JClass]] = {}
        self._parse()
        self._link()

    # -- parsing ---------------------------------------------------------

    def _parse(self) -> None:
        pkg = None
        cls = None
        lines = self.path.read_text(encoding="utf-8").split("\n")
        i = 0
        while i < len(lines):
            line = lines[i]
            m = _HEADING.match(line)
            if m:
                pkg = m.group(1)
                cls = None
                i += 1
                continue
            m = _CLASS.match(line)
            if m and pkg:
                decl = lines[i + 1].strip().strip("*_") if i + 1 < len(lines) else ""
                cls = JClass(name=m.group(1), pkg=pkg, decl=decl)
                # "Implementation" is a synthetic section listing the jar's own loader classes.
                if pkg != "Implementation":
                    self.classes[cls.name] = cls
                    self.packages.setdefault(pkg, []).append(cls)
                i += 2
                continue
            m = _MEMBER.match(line)
            if m and cls is not None:
                cls.members.append(m.group(1))
            i += 1

    def _link(self) -> None:
        for cls in self.classes.values():
            for kw in ("extends", "implements"):
                m = re.search(kw + r" ([\w, ]+)", cls.decl)
                if m:
                    for parent in m.group(1).split(","):
                        parent = parent.strip()
                        if parent and parent not in {"jxobject", "jxobject_i", "jxobject_u", "jxenum"}:
                            cls.parents.add(parent)
            for raw in cls.members:
                if _CONSTANT.match(raw):
                    # Enum singletons are typed as the enum; the parallel `_NAME` ints are not.
                    name = raw.split()[-1]
                    if raw.startswith(f"static final {cls.name} "):
                        cls.constants.append(name)
                    continue
                sig = _SIGNATURE.match(raw)
                if not sig:
                    continue  # a constructor, e.g. `pfcModel()`
                args = [a.strip() for a in sig.group("args").split(",") if a.strip()]
                cls.methods.append(Method(
                    owner=cls.name,
                    pkg=cls.pkg,
                    name=sig.group("name"),
                    ret=sig.group("ret").strip(),
                    args=args,
                    static="static" in sig.group("mods"),
                    signature=raw,
                ))

    # -- classification --------------------------------------------------

    def ancestors(self, name: str) -> set[str]:
        seen: set[str] = set()
        stack = [name]
        while stack:
            cur = stack.pop()
            for parent in self.classes.get(cur, JClass(cur, "", "")).parents:
                if parent not in seen:
                    seen.add(parent)
                    stack.append(parent)
        return seen

    def kind_of(self, type_name: str) -> str:
        """Returns one of: primitive, void, enum, sequence, live, data, listener, unknown."""
        t = type_name.replace("[]", "").strip()
        if t == "void":
            return "void"
        if t in PRIMITIVES or t in JAVA_LANG:
            return "primitive"
        if t in CIPJAVA:
            return "sequence"
        cls = self.classes.get(t)
        if cls is None:
            return "unknown"
        if cls.kind == "enum":
            return "enum"
        if cls.kind == "seq":
            return "sequence"
        if t.endswith("Listener"):
            return "listener"
        if t.startswith("X") and cls.pkg == "pfcExceptions":
            return "exception"
        if cls.pkg == "pfcExceptions":
            return "exception"
        if (self.ancestors(t) | {t}) & LIVE_ROOTS:
            return "live"
        return "data"

    def java_fqn(self, type_name: str) -> str | None:
        """Fully-qualified Java name, or None for primitives and java.lang types."""
        t = type_name.replace("[]", "").strip()
        if t in PRIMITIVES or t in JAVA_LANG:
            return None
        if t in CIPJAVA:
            return f"com.ptc.cipjava.{t}"
        cls = self.classes.get(t)
        if cls is None:
            return None
        return f"com.ptc.pfc.{cls.pkg}.{t}"

    def element_type(self, seq_name: str) -> str:
        """The element type of a sequence, read off its own ``get(int)``."""
        if seq_name == "stringseq":
            return "String"
        if seq_name == "intseq":
            return "int"
        if seq_name == "realseq":
            return "double"
        cls = self.classes.get(seq_name)
        if cls:
            for m in cls.methods:
                if m.name == "get" and m.args == ["int"]:
                    return m.ret
        return "Object"

    # -- the command surface ---------------------------------------------

    def commands(self) -> list[Method]:
        """Every instance method on a live Creo object: the operations worth wrapping.

        Excludes enum and sequence plumbing, exception types, constructors, and the accessors of
        caller-constructed instruction structs -- those are the *vocabulary* of commands, not
        commands themselves, and are handled by the marshalling layer.
        """
        out: list[Method] = []
        for cls in self.classes.values():
            if self.kind_of(cls.name) != "live":
                continue
            for m in cls.methods:
                if m.static:
                    continue
                if m.name in {"getCipTypeName", "FromInt", "getValue", "clone",
                              "equals", "hashCode", "toString"}:
                    continue
                out.append(m)
        out.sort(key=lambda m: (m.pkg, m.owner, m.name))
        return out
