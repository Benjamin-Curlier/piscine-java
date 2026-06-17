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
- [ ] `courses/docs/lancer-la-piscine.md` — replace retired `piscine.bat`/ZIP flow with the jpackage
      installer flow (mirror `docs/piscine-stagiaire.md`).
- [ ] `docs/piscine-stagiaire.md` §7 — "arrêt au premier échec" → grade-whole-subgroup behaviour.
- [ ] `README.md` — drop stale "bundle standalone" / ZIP references; point at the installer.
- [ ] `courses/docs/intro.md` — link the 5-min `demarrage-rapide.md` quickstart.
- [ ] Moulinette REPL banner (`Repl.java`) — advertise `submit` on first contact.

### P1 — Lesson correctness / compile bugs
- [ ] `1-1` — fix false "signature figée" claim (ties to hybrid policy).
- [ ] `1-3-operateurs.md` — `%` works on `double`; reframe as a rounding caveat.
- [ ] `2-3-chaines-de-caracteres.md` — `==` demo uses interned literals (returns `true`); use a
      runtime-built string so the lesson holds.
- [ ] `4-5-generiques.md` — snippet uses `T` with no enclosing generic scope; won't compile.
- [ ] `6-7-git-branches-collaboration.md` — conflict-marker side mislabeled "version fusionnée".
- [ ] `7-1-concurrence.md` — "records immuables" oversimplification (mutable component ≠ thread-safe).
- [ ] `4-8-streams-optional.md` — add `parallelStream()` caveat (expert-only).
- [ ] Wrap the ~15 bare top-level snippets in `4-4`/`4-5`/`4-7` in class/`main` so they compile.

### P1 — Gamification surfacing
- [ ] Compute gamification before/after applying progress in the submit flow; append
      `+XP · Niveau N ! · 🏅 Nouveau badge : …` to `SubmissionTrigger.formatReport`.
- [ ] Surface XP bar + badges in the GUI dashboard (`GuiServer` `/api/progress` + `dashboard.js`).
- [ ] Nudge `profil` from REPL help/banner.
- [ ] Unit tests for the new submit-output summary and the badge-diff logic.

---

## PR-2 — Modernization + coverage

### P2 — Java-25 hybrid modernization
- [ ] `1-1`/`1-2` — introduce `void main()` / `IO.println` as the modern form; explain the relationship.
- [ ] Lesson examples after module 1 — use `var` idiomatically, `Stream.toList()` over
      `Collectors.toList()`, text blocks where natural.
- [ ] Reference solutions — modernize dated non-`main` idioms (`Collectors.toList()` → `toList()` in
      `4.3.1`, `Collections.sort` in `4.1.1`, etc.). Keep classic `main`.

### P3 — Coverage-gap content
- [ ] `4-2` — complete the `equals`/`hashCode` contract (add transitivity + consistency).
- [ ] `4-1` — `List.of()` immutability trap warning.
- [ ] `7-1` — virtual threads (JEP 444).
- [ ] `6-3` — mocking / test doubles (concept + simple hand-rolled stub).
- [ ] `6-7` — `git pull`/`fetch`, `git merge --abort`, `git restore`/`reset`.
- [ ] `1-2`/`1-3` — floating-point inexactness + integer overflow.
- [ ] `5-2` — `finally` + `return` exception-swallowing pitfall.
- [ ] `3-5` — demonstrate a manual `toString()` override (promised, never shown).
- [ ] `1-4` — Scanner locale (`1,78` vs `1.78`) + resource-leak note.
- [ ] module 2 — varargs, `Arrays` utilities, `strip()` vs `trim()`.
- [ ] **Rebuild `5-8-dates-et-heures.md`** to module standard: real imports, `Period` vs `Duration`,
      formatter thread-safety, exercise + self-check + common-errors sections.
- [ ] **New exercise** (module 3): `equals`/`hashCode` + HashSet-consistency, with public/private tests.

### Tooling
- [ ] Snippet-compile guard: extract fenced ```java blocks marked compilable from lessons and compile
      them in CI (would have caught the `4-5` and `5-8` bugs).

---

## Status journal
- 2026-06-17: worktree created off `main` (`d555504`, v1 merged); plan written.
