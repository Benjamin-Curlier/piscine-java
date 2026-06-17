# Audit remediation plan

Plan to address the findings of the 2026-06-17 quality audit (UX, Java lesson coverage,
good-practice). Decisions taken with the maintainer:

- **Scope:** everything in the audit (bugs, UX ship-blockers, Java-25 modernization, coverage gaps).
- **Java 25 policy:** *hybrid — teach both.* Module 1 leads with classic `public static void main` +
  `System.out` so beginners see the real machinery, then introduces `void main()` / `IO.println`
  (JEP 512, final in 25) as the modern shortcut; lesson examples after that point use the modern form +
  `var` + `Stream.toList()` / text blocks idiomatically. **Reference solutions keep classic `main`**
  (the canonical real-world form) but get their *dated non-main idioms* modernized.
- **Delivery:** two PRs off `main` from an isolated worktree (`fix/audit-remediation`). The autonomous
  loop's `repo` clone is never touched.

Verification: `JAVA_HOME` = `bundle/piscine-etnc-stagiaire/jdk` (JDK 25.0.3). Exercises
`./mvnw -B -q -f exercises/<exo>/solution/pom.xml test`; moulinette `./gradlew :console:test`
(+`:console:testE2e`); docs `cd courses && npm run build`; lesson snippets via the new compile guard.

---

## PR-1 — Critical fixes (ship-blockers + correctness + gamification)

### P0 — UX ship-blockers & stale docs
- [x] `courses/docs/lancer-la-piscine.md` — replaced retired `piscine.bat`/ZIP flow with the jpackage
      installer flow (mirrors `docs/piscine-stagiaire.md`).
- [x] `docs/piscine-stagiaire.md` §6/§7 — "arrêt au premier échec" → grade-whole-subgroup; added the
      one-shot `submit`/`profil` commands the doc omitted.
- [x] `README.md` — dropped stale "bundle standalone" terminology; added module 7 + capstone games.
- [x] `courses/docs/intro.md` — links the 5-min `demarrage-rapide.md` quickstart; notes bonus module + games.
- [x] Moulinette REPL banner (`Repl.java`) — advertises `submit` + `profil` on first contact.

### P1 — Lesson correctness / compile bugs
- [ ] `1-1` — fix false "signature figée" claim. **→ folded into PR-2 hybrid entry-point rewrite (same section).**
- [x] `1-3-operateurs.md` — `%` works on `double`; reframed as a rounding caveat.
- [x] `2-3-chaines-de-caracteres.md` — `==` demo now uses a runtime-built string so it returns `false`.
- [x] `4-5-generiques.md` — snippet wrapped in `class Pile<T>` so `T` is in scope and it compiles.
- [x] `6-7-git-branches-collaboration.md` — conflict-marker side correctly labeled (incoming branch).
- [x] `7-1-concurrence.md` — `record` immutability qualified (mutable component ≠ thread-safe).
- [x] `4-8-streams-optional.md` — added `parallelStream()` caveat (expert-only).
- [ ] Wrap the bare top-level snippets in `4-4`/`4-5`/`4-7`. **→ folded into PR-2 module-4 modernization (same files).**

### P1 — Gamification surfacing
- [x] Compute gamification before/after the run in `SubmissionTrigger`; append
      `🎮 +XP · Niveau N ! · 🏅 Nouveau badge : …` to the submit output.
- [x] Surface XP bar + badges in the GUI dashboard (new `/api/profil` + `dashboard.js`/`style.css`).
- [x] Nudge `profil` from the REPL banner.
- [x] Unit tests for `resumeGamification` (XP, level-up, badge diff) — `:console:test` green.

---

## PR-2 — Modernization + coverage

### P2 — Java-25 hybrid modernization
- [x] `1-1`/`1-2` — `void main()` / `IO.println` introduced as the modern form; "figée" claim fixed; `var` seeded.
- [x] Lesson examples — `var` idiomatically, `Stream.toList()` over `Collectors.toList()`, text blocks in module 2.
- [x] Reference solutions — `4.3.1` (`Collectors.toList()` → `toList()`) and `4.1.1` (`Collections.sort` → `list.sort`)
      modernized, classic `main` kept; both re-tested green.

### P3 — Coverage-gap content
- [x] `4-2` — full `equals`/`hashCode` contract (reflexivity, symmetry, transitivity, consistency, non-null + coherence).
- [x] `4-1` — `List.of()` immutability trap warning + `new ArrayList<>(...)` fix.
- [x] `7-1` — virtual threads (JEP 444) "aperçu" section.
- [x] `6-3` — test doubles / mocking (concept + hand-rolled fake, no library).
- [x] `6-7` — `git fetch`/`pull`, `git merge --abort`, `git restore`/`reset`.
- [x] `1-2` — floating-point inexactness + integer overflow.
- [x] `5-2` — `finally` + `return` exception-swallowing pitfall.
- [x] `3-5` — manual `toString()` override demonstrated.
- [x] `1-4` — Scanner locale (`1,78` vs `1.78`) + don't-close-`System.in` note.
- [x] module 2 — varargs, `Arrays` utilities (`sort`/`fill`/`copyOf`/`deepToString`), `strip()` vs `trim()`, text blocks.
- [x] **Rebuilt `5-8-dates-et-heures.md`** to module standard (335 lines): complete imports, `Period` vs `Duration`,
      formatter immutability/thread-safety, exercise + self-check + common-errors.
- [x] **New exercise** `3.1.4-egalite-carte`: `equals`/`hashCode` + HashSet-consistency, public + private tests
      (solution green: 9 tests; passes `lint-exercices.sh`).

### Tooling
- [x] Snippet-compile guard `scripts/check-lesson-snippets.py` (opt-in via ```java compile``` fence), wired into CI
      as the `exemples-cours` job. Opt-in chosen over compile-everything because lessons legitimately evolve
      same-named classes across blocks (auto-compiling all blocks gives false positives). 3 high-value blocks tagged
      (the historical `4-5` generics + `5-8` dates bugs, and `4-6`).

---

## Status journal
- 2026-06-17: worktree created off `main` (`d555504`, v1 merged); plan written.
- 2026-06-17: PR-1 (critical fixes) committed + pushed → PR #70. Verified: `:console:test`, `:gui:compileJava`,
  Docusaurus build.
- 2026-06-17: PR-2 (modernization + coverage) implemented via parallel per-module authors + verified centrally
  (Docusaurus build, snippet guard, `lint-exercices.sh` → 66 exos / 0 errors, new exercise + modernized solutions
  green under JDK 25).
