# Console en bibliothèque (v1 chantier 2) — Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Extraire la logique REPL du module `console` en API appelable (`ConsoleSession`), pour que le futur module `gui` (chantier 3) l'expose par HTTP sans rien réimplémenter. Le REPL terminal devient un client de cette API.

**Architecture:** L'état des lieux est favorable : toute la sortie passe déjà par `CommandResult` (aucun `System.out` dans la chaîne commande→trigger→runner). Il manque une **façade** : le câblage (checkers, catalog, runner, trigger, contexte, registry) vit dans `Main.runRepl` et la tokenisation dans `Repl`. On crée `ConsoleSession` (même package `console`) : factory `open(repo, piscineRepo)` qui fait ce câblage, et `execute(String line)` qui tokenise + dispatch. `Repl` et `Main` deviennent des clients ; aucun changement de comportement.

**Tech Stack:** Java 25, Gradle (acquis chantier 1), JUnit 5 + AssertJ, FakeGitClient existant pour les tests unitaires.

**Critère global (spec) :** suites `git`/`e2e` inchangées et vertes (3 et 7 tests), unitaires ≥ 34.

---

### Task 1: ConsoleSession (TDD)

**Files:**
- Create: `moulinette/console/src/main/java/etnc/piscine/moulinette/console/ConsoleSession.java`
- Test: `moulinette/console/src/test/java/etnc/piscine/moulinette/console/ConsoleSessionTest.java`

- [ ] **Step 1: Écrire le test qui échoue**

```java
package etnc.piscine.moulinette.console;

import etnc.piscine.moulinette.console.commands.CommandRegistry;
import etnc.piscine.moulinette.console.commands.CommandResult;
import etnc.piscine.moulinette.console.git.FakeGitClient;
import etnc.piscine.moulinette.console.repl.ReplContext;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

class ConsoleSessionTest {

    private ConsoleSession session(FakeGitClient git) {
        var ctx = new ReplContext(Paths.get("."), git, null, Mode.LOCAL);
        return new ConsoleSession(ctx, CommandRegistry.defaults(null));
    }

    @Test
    void execute_dispatch_une_ligne_et_rend_le_resultat() {
        var git = new FakeGitClient();
        CommandResult r = session(git).execute("help");
        assertThat(r.exitCode()).isZero();
        assertThat(r.output()).contains("Commandes supportées");
    }

    @Test
    void execute_respecte_les_guillemets() {
        var git = new FakeGitClient();
        CommandResult r = session(git).execute("git commit -m \"mon message\"");
        // FakeGitClient accepte commit → pas d'erreur de tokenisation (message en un seul jeton)
        assertThat(r.exitCode()).isZero();
    }

    @Test
    void execute_commande_inconnue_rend_une_erreur_sans_lever() {
        var git = new FakeGitClient();
        CommandResult r = session(git).execute("git checkout main");
        assertThat(r.exitCode()).isNotZero();
        assertThat(r.output()).contains("non supportée");
    }

    @Test
    void currentBranch_delegue_au_contexte() {
        var git = new FakeGitClient();
        git.branch = "rendu/1.1";
        assertThat(session(git).currentBranch()).isEqualTo("rendu/1.1");
    }
}
```
Adapter le 2e test au comportement réel de `FakeGitClient` (lire la classe avant) : l'essentiel est qu'aucune exception ne sorte et que le message soit un seul jeton.

- [ ] **Step 2: Vérifier qu'il échoue**

Run: `moulinette/gradlew -p moulinette :console:test --tests "*ConsoleSessionTest*"`
Expected: FAIL — `ConsoleSession` n'existe pas (erreur de compilation).

- [ ] **Step 3: Implémenter `ConsoleSession`**

```java
package etnc.piscine.moulinette.console;

import etnc.piscine.moulinette.console.checkers.*;
import etnc.piscine.moulinette.console.commands.CommandRegistry;
import etnc.piscine.moulinette.console.commands.CommandResult;
import etnc.piscine.moulinette.console.git.ProcessGitClient;
import etnc.piscine.moulinette.console.repl.ReplContext;
import etnc.piscine.moulinette.console.trigger.MoulinetteRunner;
import etnc.piscine.moulinette.console.trigger.SubmissionTrigger;
import etnc.piscine.moulinette.console.workspace.ExerciseCatalog;
import etnc.piscine.moulinette.framework.Checker;
import etnc.piscine.moulinette.runner.ProcessRunner;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Façade « bibliothèque » de la console : une session = un workspace stagiaire câblé
 * (git, catalogue d'exos, checkers, trigger d'évaluation). Tout front (REPL terminal,
 * GUI web) exécute des lignes de commande via {@link #execute(String)} et reçoit un
 * {@link CommandResult} — aucune écriture directe sur stdout.
 */
public final class ConsoleSession {

    private final ReplContext ctx;
    private final CommandRegistry registry;

    ConsoleSession(ReplContext ctx, CommandRegistry registry) {
        this.ctx = ctx;
        this.registry = registry;
    }

    /** Câblage complet sur un workspace réel (extrait de Main.runRepl). */
    public static ConsoleSession open(Path repo, Path piscineRepo) {
        var git = new ProcessGitClient();
        ExerciseCatalog catalog = ExerciseCatalog.scan(piscineRepo.resolve("exercises"));
        var toolkit = new JavaToolkit(new ProcessRunner());
        Path styleConfig = StyleChecker.extractBundledConfig();
        List<Checker> checkers = List.of(
            new CompileChecker(toolkit),
            new PublicTestChecker(toolkit),
            new PrivateTestChecker(toolkit),
            new MutationChecker(toolkit), // ne s'active que sur les exos « écriture de tests » (mutants/)
            new StyleChecker(toolkit, styleConfig));
        var runner = new MoulinetteRunner.Default(catalog, checkers, repo.resolve(".piscine/reports"));
        var trigger = new SubmissionTrigger(runner);
        var ctx = new ReplContext(repo, git, catalog, Mode.LOCAL);
        return new ConsoleSession(ctx, CommandRegistry.defaults(trigger));
    }

    /** Tokenise (guillemets doubles respectés) puis dispatch une ligne de commande. */
    public CommandResult execute(String line) {
        return registry.dispatch(ctx, tokenize(line));
    }

    public String currentBranch() { return ctx.currentBranch(); }

    public Path repoRoot() { return ctx.repoRoot(); }

    /**
     * Découpe une ligne en jetons, en respectant les guillemets doubles.
     * Exemple : {@code git commit -m "mon message"} → [git, commit, -m, mon message].
     */
    static List<String> tokenize(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        boolean hasToken = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
                hasToken = true;
            } else if (Character.isWhitespace(c) && !inQuotes) {
                if (hasToken) { tokens.add(cur.toString()); cur.setLength(0); hasToken = false; }
            } else {
                cur.append(c);
                hasToken = true;
            }
        }
        if (hasToken) tokens.add(cur.toString());
        return tokens;
    }
}
```
(`tokenize` est **déplacé** depuis `Repl` — la Task 2 supprime l'original.)

- [ ] **Step 4: Vérifier que le test passe**

Run: `moulinette/gradlew -p moulinette :console:test --tests "*ConsoleSessionTest*"`
Expected: PASS (4 tests).

- [ ] **Step 5: Commit**

```bash
git add moulinette/console/src
git commit -m "feat(console): ConsoleSession, facade bibliotheque de la console"
```

---

### Task 2: Repl devient client de ConsoleSession

**Files:**
- Modify: `moulinette/console/src/main/java/etnc/piscine/moulinette/console/repl/Repl.java`
- Modify: `moulinette/console/src/test/java/etnc/piscine/moulinette/console/repl/ReplTest.java`

- [ ] **Step 1: Réécrire `Repl`** (la boucle garde le prompt et l'affichage ; la tokenisation/dispatch part dans la session)

```java
package etnc.piscine.moulinette.console.repl;

import etnc.piscine.moulinette.console.ConsoleSession;
import etnc.piscine.moulinette.console.commands.CommandResult;

import java.io.IOException;

public final class Repl {
    private final ConsoleSession session;
    private final ReplIo io;

    public Repl(ConsoleSession session, ReplIo io) {
        this.session = session; this.io = io;
    }

    public void run() throws IOException {
        io.write("Piscine ETNC — console locale. Tape `help` pour la liste, `exit` pour quitter.\n");
        while (true) {
            io.write("piscine[" + session.currentBranch() + "]> ");
            String line = io.readLine();
            if (line == null) return;
            line = line.trim();
            if (line.isEmpty()) continue;
            CommandResult r = session.execute(line);
            if (!r.output().isEmpty()) {
                io.write(r.output());
                if (!r.output().endsWith("\n")) io.write("\n");
            }
            if (r.shouldExit()) return;
        }
    }
}
```
`ConsoleSession` n'a qu'un constructeur package-privé hors `open(...)` : ajouter une **factory de test** publique n'est pas souhaitable ; `ReplTest` est dans le package `repl`, donc il construira la session via un petit helper. Pour ça, ajouter dans `ConsoleSession` :

```java
    /** Assemblage manuel (tests, fronts custom) : contexte et registry fournis par l'appelant. */
    public static ConsoleSession of(ReplContext ctx, CommandRegistry registry) {
        return new ConsoleSession(ctx, registry);
    }
```
(et le constructeur reste package-privé ; `ConsoleSessionTest`, dans le même package, peut utiliser l'un ou l'autre).

- [ ] **Step 2: Adapter `ReplTest`**

Les deux tests de boucle construisent désormais la session :
```java
var ctx = new ReplContext(Paths.get("."), git, null, Mode.LOCAL);
var session = ConsoleSession.of(ctx, CommandRegistry.defaults(null));
new Repl(session, io).run();
```
Le test `tokenize_respecte_les_guillemets` **déménage** dans `ConsoleSessionTest` (remplacer `Repl.tokenize` par `ConsoleSession.tokenize`) ; le supprimer de `ReplTest`.

- [ ] **Step 3: Vérifier**

Run: `moulinette/gradlew -p moulinette :console:test`
Expected: PASS, total ≥ 35 (34 de base − 1 tokenize déplacé + 4 ConsoleSessionTest + 1 tokenize re-ajouté ⇒ 38).

- [ ] **Step 4: Commit**

```bash
git add moulinette/console/src
git commit -m "refactor(console): Repl devient client de ConsoleSession"
```

---

### Task 3: Main câble via ConsoleSession + vérification complète

**Files:**
- Modify: `moulinette/console/src/main/java/etnc/piscine/moulinette/console/Main.java:66-95` (méthode `runRepl`)
- Modify: `docs/architecture-moulinette.md` (§ Deux points d'entrée + § Flux)

- [ ] **Step 1: Simplifier `Main.runRepl`**

```java
    private int runRepl(List<String> args) throws Exception {
        Path repo = Paths.get(required(args, "--repo"));
        Path defaultPiscine = repo.toAbsolutePath().normalize().getParent().resolve("piscine-etnc");
        Path piscine = Paths.get(optional(args, "--piscine-repo", defaultPiscine.toString()));
        ConsoleSession session = ConsoleSession.open(repo, piscine);
        var repl = new Repl(session, ReplIo.stdio());

        String siteArg = optional(args, "--site", null);
        CourseSiteServer site = startSiteIfRequested(siteArg);
        try {
            repl.run();
        } finally {
            if (site != null) {
                site.stop();
            }
        }
        return 0;
    }
```
Supprimer les imports devenus inutiles dans `Main` (checkers, CommandRegistry, GitClient/ProcessGitClient, Checker, ProcessRunner, MoulinetteRunner, SubmissionTrigger, ReplContext, workspace.* sauf ce qu'utilise `runInit`).

- [ ] **Step 2: Vérification complète (critère du chantier)**

```bash
moulinette/gradlew -p moulinette build :console:testGit :console:testTools :console:testE2e
```
Expected: BUILD SUCCESSFUL ; suites `git` = 3, `tools` = 13, `e2e` = 7 (inchangées), unitaires console = 38.

- [ ] **Step 3: Mettre à jour `docs/architecture-moulinette.md`**

Dans « Deux points d'entrée », préciser que la logique est portée par **`ConsoleSession`** (façade bibliothèque : `open(repo, piscineRepo)` + `execute(ligne) → CommandResult`) et que le REPL terminal n'est qu'un client ; le flux d'évaluation commence donc à `ConsoleSession.execute("git push ...")`.

- [ ] **Step 4: Commit + CHANGELOG**

Ajouter au `CHANGELOG.md` (Unreleased) : `- Logique console extraite en bibliothèque (ConsoleSession) — prépare la GUI v1.`
```bash
git add -A
git commit -m "refactor(console): Main cable via ConsoleSession + docs"
```

---

## Hors périmètre (chantier 3)

- Streaming de progression d'évaluation (listener/SSE) : l'API actuelle rend le rapport d'un bloc ; le hook de progression sera ajouté quand la GUI définira son besoin réel (YAGNI).
- Module `gui` lui-même.
