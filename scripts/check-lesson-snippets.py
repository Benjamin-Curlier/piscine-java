#!/usr/bin/env python3
"""Compile-checks the Java code blocks that lessons mark as self-contained.

Course lessons mix many kinds of code blocks: complete programs, fragments,
counter-examples (`// ne compile pas`), and classes that are progressively
*evolved* across several blocks (so a block alone won't compile). Compiling
everything automatically therefore produces false positives.

So this guard is **opt-in**: an author marks a block as a complete, standalone
compilation unit by adding `compile` to the fence info string:

    ```java compile
    public class Demo {
        public static void main(String[] args) { ... }
    }
    ```

Every marked block is compiled in isolation with `javac` (the `public`
modifier is stripped so the temp file name never matters). The build fails if
any marked block does not compile — catching the "this example doesn't even
compile" class of bug the 2026 audit found (e.g. the old `4-5` generics and
`5-8` dates snippets). Unmarked blocks are ignored.

Exit 0 if every marked block compiles, 1 otherwise. Needs `javac` on PATH or
`JAVA_HOME` set.
"""
from __future__ import annotations

import os
import re
import subprocess
import sys
import tempfile
from pathlib import Path

for _stream in (sys.stdout, sys.stderr):
    try:
        _stream.reconfigure(encoding="utf-8")  # type: ignore[attr-defined]
    except (AttributeError, ValueError):
        pass

DOCS = Path(__file__).resolve().parent.parent / "courses" / "docs"

# Fenced ```java<info> ... ``` blocks.
FENCE = re.compile(r"^```java([^\n]*)\n(.*?)^```", re.MULTILINE | re.DOTALL)
PUBLIC_TYPE = re.compile(
    r"^(?:public\s+)(?:final\s+|abstract\s+|sealed\s+|non-sealed\s+)*"
    r"(?:class|interface|enum|record)\s+(\w+)",
    re.MULTILINE,
)
# Drop a top-level `public` so several types can share one temp file and the
# file name is irrelevant.
STRIP_PUBLIC = re.compile(
    r"^public\s+(?=(?:final\s+|abstract\s+|sealed\s+|non-sealed\s+)*"
    r"(?:class|interface|enum|record)\b)",
    re.MULTILINE,
)


def javac() -> str:
    jh = os.environ.get("JAVA_HOME")
    if jh:
        cand = Path(jh) / "bin" / ("javac.exe" if os.name == "nt" else "javac")
        if cand.exists():
            return str(cand)
    return "javac"


def main() -> int:
    jc = javac()
    checked = failed = 0
    failures: list[str] = []

    for md in sorted(DOCS.rglob("*.md")):
        text = md.read_text(encoding="utf-8")
        for idx, m in enumerate(FENCE.finditer(text), start=1):
            info, body = m.group(1), m.group(2)
            if "compile" not in info.split():
                continue
            checked += 1
            pm = PUBLIC_TYPE.search(body)
            stem = pm.group(1) if pm else "Bloc"
            with tempfile.TemporaryDirectory() as tmp:
                src = Path(tmp) / f"{stem}.java"
                src.write_text(STRIP_PUBLIC.sub("", body), encoding="utf-8")
                proc = subprocess.run(
                    [jc, "-d", tmp, str(src)], capture_output=True, text=True,
                )
                if proc.returncode != 0:
                    failed += 1
                    rel = md.relative_to(DOCS.parent.parent)
                    err = proc.stderr.strip() or proc.stdout.strip()
                    failures.append(f"\n✗ {rel} (bloc #{idx}):\n{err}")

    print(f"Blocs Java marqués `compile` vérifiés : {checked}.")
    if failures:
        print(f"\n{failed} bloc(s) ne compilent pas :")
        for f in failures:
            print(f)
        return 1
    print("✓ Tous les blocs marqués `compile` compilent." if checked
          else "Aucun bloc marqué `compile` (rien à vérifier).")
    return 0


if __name__ == "__main__":
    sys.exit(main())
