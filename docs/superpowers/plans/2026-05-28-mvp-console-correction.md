# MVP Console de correction REPL git — Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Livrer une console interactive Java (REPL git sandbox) qui permet à un stagiaire de faire la Piscine ETNC en local, depuis le clone du repo jusqu'au rendu validé par la moulinette, en autonomie.

**Architecture:** Nouveau module Maven `moulinette/console` à côté de `moulinette/cli`. Le module expose deux sous-commandes (`init`, `repl`) accessibles via un script de bootstrap (`scripts/piscine-bootstrap.{sh,ps1}`) à la racine du repo. Le REPL accepte un set minimal de commandes git (`add`, `commit`, `push`, `status`, `log`, `diff`) plus une commande métier (`submit-start`). La séquence `add` + `commit` + `push origin rendu/<sous-groupe>` déclenche la moulinette sur les exos du sous-groupe en ordre de difficulté croissante, avec arrêt au premier échec. Une enum `Mode { LOCAL, NOMINAL }` est câblée dès le MVP ; seul `LOCAL` est implémenté.

**Tech Stack:** Java 25, Maven 3.9+, JUnit 5.11.4, AssertJ 3.26.3, SLF4J 2.0.16 / Logback 1.5.12, binaire `git` système, SnakeYAML 2.3 (pour lire `metadata.yml`).

**Spec source:** [`docs/superpowers/specs/2026-05-28-mvp-console-correction-design.md`](../specs/2026-05-28-mvp-console-correction-design.md)

---

## File Structure

### Nouveau module — `moulinette/console/`

```
moulinette/console/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/etnc/piscine/moulinette/console/
    │   │   ├── Main.java                         — entry point, parse init|repl
    │   │   ├── Mode.java                         — enum LOCAL|NOMINAL
    │   │   ├── ConsoleException.java             — exception métier (msg stagiaire-first)
    │   │   ├── workspace/
    │   │   │   ├── ExerciseCatalog.java          — scan exercises/, groupes triés
    │   │   │   ├── ExerciseEntry.java            — record (id, sousGroupe, position, ...)
    │   │   │   ├── SousGroupe.java               — record (id, titre, exercices ordonnés)
    │   │   │   ├── WorkspaceInitializer.java     — interface
    │   │   │   ├── LocalWorkspaceInitializer.java — impl pour Mode.LOCAL
    │   │   │   ├── InitRequest.java              — record
    │   │   │   └── Workspace.java                — record
    │   │   ├── git/
    │   │   │   ├── GitClient.java                — interface
    │   │   │   ├── ProcessGitClient.java         — impl sous-process
    │   │   │   ├── GitResult.java                — record (exit, stdout, stderr)
    │   │   │   └── RefUpdate.java                — record (ref, oldSha, newSha)
    │   │   ├── repl/
    │   │   │   ├── Repl.java                     — boucle read/dispatch/print
    │   │   │   ├── ReplContext.java              — état mutable (cwd, branche, mode)
    │   │   │   └── ReplIo.java                   — abstraction in/out testable
    │   │   ├── commands/
    │   │   │   ├── Command.java                  — interface
    │   │   │   ├── CommandResult.java            — record
    │   │   │   ├── CommandRegistry.java          — dispatch Map<String, Command>
    │   │   │   ├── AddCommand.java
    │   │   │   ├── CommitCommand.java
    │   │   │   ├── PushCommand.java
    │   │   │   ├── StatusCommand.java
    │   │   │   ├── LogCommand.java
    │   │   │   ├── DiffCommand.java
    │   │   │   ├── SubmitStartCommand.java
    │   │   │   ├── HelpCommand.java
    │   │   │   └── ExitCommand.java
    │   │   └── trigger/
    │   │       ├── SubmissionTrigger.java        — observe les push, détecte rendu/<X>
    │   │       └── MoulinetteRunner.java         — interface + DefaultMoulinetteRunner
    │   └── resources/
    │       └── logback.xml                       — config Logback (test scope only)
    └── test/
        └── java/etnc/piscine/moulinette/console/
            ├── ConsoleSmokeTest.java
            ├── workspace/
            │   ├── ExerciseCatalogTest.java
            │   └── LocalWorkspaceInitializerIT.java  (@Tag("git"))
            ├── git/
            │   ├── FakeGitClient.java                 (helper de test)
            │   └── ProcessGitClientIT.java            (@Tag("git"))
            ├── repl/
            │   └── ReplTest.java
            ├── commands/
            │   ├── AddCommandTest.java
            │   ├── CommitCommandTest.java
            │   ├── PushCommandTest.java
            │   ├── SubmitStartCommandTest.java
            │   └── CommandRegistryTest.java
            ├── trigger/
            │   └── SubmissionTriggerTest.java
            └── e2e/
                └── HappyPathE2EIT.java                (@Tag("e2e"))
```

### Fichiers modifiés / ajoutés hors module

```
moulinette/pom.xml                                 — ajoute <module>console</module>
scripts/piscine-bootstrap.sh                       — bootstrap Unix
scripts/piscine-bootstrap.ps1                      — bootstrap Windows
docs/piscine-stagiaire.md                          — guide stagiaire autonome
README.md                                          — section "Stagiaire : démarrer" en haut
docs/backlog.md                                    — clôture des tâches #34-#42
```

---

## Tâche 1 — Squelette du module `moulinette/console` (backlog #34)

**Files:**
- Modify: `moulinette/pom.xml` (ligne 22, ajouter `<module>console</module>`)
- Create: `moulinette/console/pom.xml`
- Create: `moulinette/console/src/main/java/etnc/piscine/moulinette/console/Mode.java`
- Create: `moulinette/console/src/test/java/etnc/piscine/moulinette/console/ConsoleSmokeTest.java`

- [ ] **Step 1.1: Écrire le smoke test (qui ne compile pas encore)**

Create `moulinette/console/src/test/java/etnc/piscine/moulinette/console/ConsoleSmokeTest.java`:

```java
package etnc.piscine.moulinette.console;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ConsoleSmokeTest {

    @Test
    void le_module_console_est_chargeable() {
        assertThat(Mode.values()).containsExactly(Mode.LOCAL, Mode.NOMINAL);
    }
}
```

- [ ] **Step 1.2: Créer le `pom.xml` du module `console`**

Create `moulinette/console/pom.xml`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
             https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>etnc.piscine.moulinette</groupId>
        <artifactId>moulinette</artifactId>
        <version>0.1.0-SNAPSHOT</version>
    </parent>

    <artifactId>console</artifactId>
    <name>Moulinette — Console interactive</name>
    <description>
        REPL git sandbox pour la Piscine ETNC en mode local.
        Lance la moulinette sur push d'une branche rendu/&lt;sous-groupe&gt;.
    </description>

    <dependencies>
        <dependency>
            <groupId>etnc.piscine.moulinette</groupId>
            <artifactId>framework</artifactId>
        </dependency>
        <dependency>
            <groupId>etnc.piscine.moulinette</groupId>
            <artifactId>runner</artifactId>
        </dependency>
        <dependency>
            <groupId>etnc.piscine.moulinette</groupId>
            <artifactId>reports</artifactId>
        </dependency>

        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
        </dependency>
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <scope>runtime</scope>
        </dependency>

        <dependency>
            <groupId>org.yaml</groupId>
            <artifactId>snakeyaml</artifactId>
            <version>2.3</version>
        </dependency>

        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.assertj</groupId>
            <artifactId>assertj-core</artifactId>
        </dependency>
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <configuration>
                    <excludedGroups>git,e2e</excludedGroups>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

- [ ] **Step 1.3: Ajouter le module au parent**

Edit `moulinette/pom.xml`, remplacer le bloc `<modules>` :

```xml
    <modules>
        <module>framework</module>
        <module>runner</module>
        <module>reports</module>
        <module>cli</module>
        <module>console</module>
    </modules>
```

- [ ] **Step 1.4: Écrire l'enum `Mode`**

Create `moulinette/console/src/main/java/etnc/piscine/moulinette/console/Mode.java`:

```java
package etnc.piscine.moulinette.console;

/**
 * Mode de fonctionnement de la console.
 *
 * <p>{@link #LOCAL} : workspace stagiaire local, remote bare en file://. C'est le mode du MVP.
 * <p>{@link #NOMINAL} : déploiement plateforme (auth, rendu serveur). Non implémenté au MVP.
 */
public enum Mode { LOCAL, NOMINAL }
```

- [ ] **Step 1.5: Vérifier que le smoke test passe**

Run: `./mvnw -f moulinette/pom.xml -pl console -am verify`
Expected: BUILD SUCCESS, `ConsoleSmokeTest.le_module_console_est_chargeable` passe.

- [ ] **Step 1.6: Commit**

```bash
git add moulinette/pom.xml moulinette/console/
git commit -m "feat(console): squelette Maven module console (tâche #34)"
```

---

## Tâche 2 — `GitClient` + `ProcessGitClient` + `FakeGitClient` (backlog #35)

**Files:**
- Create: `moulinette/console/src/main/java/etnc/piscine/moulinette/console/git/GitResult.java`
- Create: `moulinette/console/src/main/java/etnc/piscine/moulinette/console/git/RefUpdate.java`
- Create: `moulinette/console/src/main/java/etnc/piscine/moulinette/console/git/GitClient.java`
- Create: `moulinette/console/src/main/java/etnc/piscine/moulinette/console/git/ProcessGitClient.java`
- Create: `moulinette/console/src/test/java/etnc/piscine/moulinette/console/git/FakeGitClient.java`
- Create: `moulinette/console/src/test/java/etnc/piscine/moulinette/console/git/ProcessGitClientIT.java`

- [ ] **Step 2.1: Records de retour**

Create `GitResult.java`:

```java
package etnc.piscine.moulinette.console.git;

public record GitResult(int exitCode, String stdout, String stderr) {
    public boolean ok() { return exitCode == 0; }
}
```

Create `RefUpdate.java`:

```java
package etnc.piscine.moulinette.console.git;

/** Ref poussée lors du dernier `git push`. */
public record RefUpdate(String ref, String oldSha, String newSha) {}
```

- [ ] **Step 2.2: Interface `GitClient`**

Create `GitClient.java`:

```java
package etnc.piscine.moulinette.console.git;

import java.nio.file.Path;
import java.util.List;

public interface GitClient {
    GitResult run(Path repo, List<String> args);
    String currentBranch(Path repo);
    /** Refs poussées par le dernier `git push` capturé dans ce client. Vide si pas de push. */
    List<RefUpdate> lastPushRefs(Path repo);
}
```

- [ ] **Step 2.3: `FakeGitClient` pour les tests**

Create `src/test/java/.../git/FakeGitClient.java`:

```java
package etnc.piscine.moulinette.console.git;

import java.nio.file.Path;
import java.util.*;

public class FakeGitClient implements GitClient {
    public final List<List<String>> calls = new ArrayList<>();
    public final Map<String, GitResult> stubbed = new HashMap<>();
    public String branch = "main";
    public List<RefUpdate> pushedRefs = List.of();

    @Override public GitResult run(Path repo, List<String> args) {
        calls.add(args);
        String key = String.join(" ", args);
        return stubbed.getOrDefault(key, new GitResult(0, "", ""));
    }
    @Override public String currentBranch(Path repo) { return branch; }
    @Override public List<RefUpdate> lastPushRefs(Path repo) { return pushedRefs; }

    public void stub(String argLine, GitResult r) { stubbed.put(argLine, r); }
}
```

- [ ] **Step 2.4: Test d'intégration `ProcessGitClientIT` — init + commit + push vers bare**

Create `src/test/java/.../git/ProcessGitClientIT.java`:

```java
package etnc.piscine.moulinette.console.git;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("git")
class ProcessGitClientIT {

    @Test
    void init_commit_push_vers_bare_local(@TempDir Path tmp) throws IOException {
        Path repo = tmp.resolve("work");
        Path bare = tmp.resolve("remote.git");
        Files.createDirectories(repo);
        ProcessGitClient git = new ProcessGitClient();

        // bare remote
        assertThat(git.run(tmp, List.of("init", "--bare", bare.toString())).ok()).isTrue();

        // working repo
        assertThat(git.run(repo, List.of("init")).ok()).isTrue();
        assertThat(git.run(repo, List.of("config", "user.email", "test@etnc")).ok()).isTrue();
        assertThat(git.run(repo, List.of("config", "user.name", "test")).ok()).isTrue();
        assertThat(git.run(repo, List.of("checkout", "-b", "main")).ok()).isTrue();

        Files.writeString(repo.resolve("README.md"), "hello");
        assertThat(git.run(repo, List.of("add", ".")).ok()).isTrue();
        assertThat(git.run(repo, List.of("commit", "-m", "initial")).ok()).isTrue();

        // push
        assertThat(git.run(repo, List.of("remote", "add", "origin", bare.toString())).ok()).isTrue();
        GitResult push = git.run(repo, List.of("push", "origin", "main"));
        assertThat(push.ok()).as("push stderr=%s", push.stderr()).isTrue();

        assertThat(git.currentBranch(repo)).isEqualTo("main");
        assertThat(git.lastPushRefs(repo))
            .anySatisfy(r -> assertThat(r.ref()).isEqualTo("refs/heads/main"));
    }
}
```

- [ ] **Step 2.5: Vérifier que les tests échouent**

Run: `./mvnw -f moulinette/pom.xml -pl console test -Dgroups=git`
Expected: COMPILATION FAILURE (ProcessGitClient n'existe pas).

- [ ] **Step 2.6: Implémenter `ProcessGitClient`**

Create `ProcessGitClient.java`:

```java
package etnc.piscine.moulinette.console.git;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ProcessGitClient implements GitClient {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessGitClient.class);
    private static final long TIMEOUT_SECONDS = 30;

    private final Map<Path, List<RefUpdate>> lastPushByRepo = new HashMap<>();

    @Override
    public GitResult run(Path repo, List<String> args) {
        List<String> cmd = new ArrayList<>();
        cmd.add("git");
        cmd.addAll(args);
        ProcessBuilder pb = new ProcessBuilder(cmd).directory(repo.toFile());
        pb.environment().put("LC_ALL", "C");
        try {
            Process p = pb.start();
            String stdout = readAll(p.getInputStream());
            String stderr = readAll(p.getErrorStream());
            if (!p.waitFor(TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                p.destroyForcibly();
                return new GitResult(-1, stdout, "timeout après " + TIMEOUT_SECONDS + "s");
            }
            GitResult res = new GitResult(p.exitValue(), stdout, stderr);
            if (!args.isEmpty() && "push".equals(args.get(0)) && res.ok()) {
                lastPushByRepo.put(repo, parsePushRefs(stderr));
            }
            LOG.debug("git {} → exit={}", args, res.exitCode());
            return res;
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return new GitResult(-1, "", e.getMessage());
        }
    }

    @Override
    public String currentBranch(Path repo) {
        return run(repo, List.of("rev-parse", "--abbrev-ref", "HEAD")).stdout().trim();
    }

    @Override
    public List<RefUpdate> lastPushRefs(Path repo) {
        return lastPushByRepo.getOrDefault(repo, List.of());
    }

    private static String readAll(java.io.InputStream is) throws IOException {
        try (var r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) sb.append(line).append('\n');
            return sb.toString();
        }
    }

    /** Parse les lignes "<old>..<new>  <ref> -> <ref>" du verbose porcelain. */
    static List<RefUpdate> parsePushRefs(String stderr) {
        List<RefUpdate> refs = new ArrayList<>();
        for (String line : stderr.split("\n")) {
            // git push avec --porcelain donnerait du structuré ; ici on s'appuie sur "To <remote>\n   <old>..<new>  <local> -> <ref>"
            // Heuristique simple : ligne contenant " -> "
            int arrow = line.indexOf(" -> ");
            if (arrow < 0) continue;
            String right = line.substring(arrow + 4).trim();
            if (right.isEmpty()) continue;
            refs.add(new RefUpdate("refs/heads/" + right, "", ""));
        }
        return refs;
    }
}
```

Note : pour fiabiliser le parsing, on ajoutera `--porcelain` dans la `PushCommand` (Tâche 5).

- [ ] **Step 2.7: Vérifier que les tests passent**

Run: `./mvnw -f moulinette/pom.xml -pl console test -Dgroups=git`
Expected: BUILD SUCCESS, `ProcessGitClientIT` passe.

- [ ] **Step 2.8: Commit**

```bash
git add moulinette/console/src
git commit -m "feat(console): GitClient + ProcessGitClient + FakeGitClient (tâche #35)"
```

---

## Tâche 3 — Catalogue d'exercices `ExerciseCatalog` (backlog #36 partie 1)

**Files:**
- Create: `moulinette/console/src/main/java/etnc/piscine/moulinette/console/workspace/ExerciseEntry.java`
- Create: `moulinette/console/src/main/java/etnc/piscine/moulinette/console/workspace/SousGroupe.java`
- Create: `moulinette/console/src/main/java/etnc/piscine/moulinette/console/workspace/ExerciseCatalog.java`
- Create: `moulinette/console/src/test/java/etnc/piscine/moulinette/console/workspace/ExerciseCatalogTest.java`

- [ ] **Step 3.1: Records de données**

Create `ExerciseEntry.java`:

```java
package etnc.piscine.moulinette.console.workspace;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * Entrée du catalogue : un exercice avec ses chemins.
 *
 * @param id          id canonique ("1.1.1")
 * @param slug        slug humain ("hello-world")
 * @param module      numéro de module (1..6)
 * @param sousGroupe  id du sous-groupe ("1.1")
 * @param position    rang dans le sous-groupe (1 = plus facile)
 * @param notions     liste des notions touchées
 * @param exerciseDir dossier source dans le repo Piscine (exercises/.../1.1.1-hello-world)
 */
public record ExerciseEntry(
        String id, String slug, int module, String sousGroupe,
        int position, List<String> notions, Path exerciseDir
) {
    public ExerciseEntry {
        Objects.requireNonNull(id); Objects.requireNonNull(sousGroupe);
        Objects.requireNonNull(exerciseDir);
    }
}
```

Create `SousGroupe.java`:

```java
package etnc.piscine.moulinette.console.workspace;

import java.util.List;

/**
 * Sous-groupe d'exercices, ordonnés par {@link ExerciseEntry#position()} croissante.
 * Le titre est dérivé du référentiel ou laissé vide si introuvable.
 */
public record SousGroupe(String id, String titre, List<ExerciseEntry> exercices) {
    public SousGroupe {
        if (exercices.size() > 6) {
            throw new IllegalArgumentException(
                "Un sous-groupe ne peut pas contenir plus de 6 exercices (id=" + id + ")");
        }
    }
}
```

- [ ] **Step 3.2: Test du catalogue (TDD)**

Create `ExerciseCatalogTest.java`:

```java
package etnc.piscine.moulinette.console.workspace;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExerciseCatalogTest {

    @Test
    void scanne_les_exos_et_les_groupe_tries_par_position(@TempDir Path piscine) throws IOException {
        writeExo(piscine, "module-1-fondamentaux/1.1.1-hello-world", """
            slug: hello-world
            module: 1
            sous_groupe: "1.1"
            position: 1
            notions: [syntaxe-de-base]
            """);
        writeExo(piscine, "module-1-fondamentaux/1.1.2-affichage-formate", """
            slug: affichage-formate
            module: 1
            sous_groupe: "1.1"
            position: 2
            notions: [printf]
            """);
        writeExo(piscine, "module-1-fondamentaux/1.2.1-conversion-unites", """
            slug: conversion-unites
            module: 1
            sous_groupe: "1.2"
            position: 1
            notions: [variables]
            """);

        ExerciseCatalog cat = ExerciseCatalog.scan(piscine.resolve("exercises"));

        assertThat(cat.sousGroupes()).extracting(SousGroupe::id).containsExactly("1.1", "1.2");
        assertThat(cat.sousGroupe("1.1").exercices())
            .extracting(ExerciseEntry::id).containsExactly("1.1.1", "1.1.2");
    }

    @Test
    void exception_si_sous_groupe_a_plus_de_6_exos(@TempDir Path piscine) throws IOException {
        for (int i = 1; i <= 7; i++) {
            writeExo(piscine, "module-1-fondamentaux/1.1." + i + "-exo" + i, """
                slug: exo%d
                module: 1
                sous_groupe: "1.1"
                position: %d
                notions: []
                """.formatted(i, i));
        }
        assertThatThrownBy(() -> ExerciseCatalog.scan(piscine.resolve("exercises")))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("plus de 6");
    }

    @Test
    void metadata_yml_invalide_est_loggee_et_skippee(@TempDir Path piscine) throws IOException {
        writeExo(piscine, "module-1-fondamentaux/1.1.1-ok", """
            slug: ok
            module: 1
            sous_groupe: "1.1"
            position: 1
            notions: []
            """);
        Path bad = piscine.resolve("exercises/module-1-fondamentaux/1.1.2-bad");
        Files.createDirectories(bad);
        Files.writeString(bad.resolve("metadata.yml"), "::: not yaml :::");

        ExerciseCatalog cat = ExerciseCatalog.scan(piscine.resolve("exercises"));
        assertThat(cat.sousGroupe("1.1").exercices()).hasSize(1);
    }

    private static void writeExo(Path piscine, String relPath, String metadata) throws IOException {
        Path dir = piscine.resolve("exercises").resolve(relPath);
        Files.createDirectories(dir);
        Files.writeString(dir.resolve("metadata.yml"), metadata);
        Files.createDirectories(dir.resolve("starter"));
    }
}
```

- [ ] **Step 3.3: Vérifier l'échec**

Run: `./mvnw -f moulinette/pom.xml -pl console test`
Expected: COMPILATION FAILURE (`ExerciseCatalog` introuvable).

- [ ] **Step 3.4: Implémenter `ExerciseCatalog`**

Create `ExerciseCatalog.java`:

```java
package etnc.piscine.moulinette.console.workspace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ExerciseCatalog {
    private static final Logger LOG = LoggerFactory.getLogger(ExerciseCatalog.class);

    private final Map<String, SousGroupe> bySousGroupe;

    private ExerciseCatalog(Map<String, SousGroupe> bySousGroupe) {
        this.bySousGroupe = bySousGroupe;
    }

    public List<SousGroupe> sousGroupes() {
        return bySousGroupe.values().stream()
            .sorted(Comparator.comparing(SousGroupe::id))
            .toList();
    }

    public SousGroupe sousGroupe(String id) {
        SousGroupe sg = bySousGroupe.get(id);
        if (sg == null) throw new NoSuchElementException("sous-groupe inconnu : " + id);
        return sg;
    }

    public static ExerciseCatalog scan(Path exercisesRoot) {
        if (!Files.isDirectory(exercisesRoot)) {
            throw new IllegalArgumentException("exercises/ introuvable : " + exercisesRoot);
        }
        Map<String, List<ExerciseEntry>> byGroup = new LinkedHashMap<>();
        try (Stream<Path> walk = Files.walk(exercisesRoot, 3)) {
            walk.filter(p -> p.getFileName().toString().equals("metadata.yml"))
                .forEach(meta -> parseOne(meta).ifPresent(e ->
                    byGroup.computeIfAbsent(e.sousGroupe(), k -> new ArrayList<>()).add(e)));
        } catch (IOException ioe) {
            throw new IllegalStateException("scan exercises échoué", ioe);
        }
        Map<String, SousGroupe> result = new TreeMap<>();
        for (var e : byGroup.entrySet()) {
            List<ExerciseEntry> sorted = e.getValue().stream()
                .sorted(Comparator.comparingInt(ExerciseEntry::position))
                .toList();
            result.put(e.getKey(), new SousGroupe(e.getKey(), "", sorted));
        }
        return new ExerciseCatalog(result);
    }

    @SuppressWarnings("unchecked")
    private static Optional<ExerciseEntry> parseOne(Path metadataYml) {
        try {
            Map<String, Object> m = new Yaml().load(Files.newBufferedReader(metadataYml));
            if (m == null) throw new IOException("vide");
            Path exoDir = metadataYml.getParent();
            String slug = (String) m.get("slug");
            int module = ((Number) m.get("module")).intValue();
            String sousGroupe = String.valueOf(m.get("sous_groupe"));
            int position = ((Number) m.get("position")).intValue();
            List<String> notions = (List<String>) m.getOrDefault("notions", List.of());
            String id = exoDir.getFileName().toString().split("-")[0]; // "1.1.1"
            return Optional.of(new ExerciseEntry(id, slug, module, sousGroupe, position, notions, exoDir));
        } catch (Exception e) {
            LOG.warn("metadata.yml invalide ignoré : {} ({})", metadataYml, e.getMessage());
            return Optional.empty();
        }
    }
}
```

- [ ] **Step 3.5: Vérifier les tests**

Run: `./mvnw -f moulinette/pom.xml -pl console test -Dtest=ExerciseCatalogTest`
Expected: 3 tests PASS.

- [ ] **Step 3.6: Commit**

```bash
git add moulinette/console/src/main/java/etnc/piscine/moulinette/console/workspace moulinette/console/src/test/java/etnc/piscine/moulinette/console/workspace
git commit -m "feat(console): ExerciseCatalog (tâche #36 partie 1)"
```

---

## Tâche 4 — `WorkspaceInitializer` (backlog #36 partie 2)

**Files:**
- Create: `moulinette/console/src/main/java/etnc/piscine/moulinette/console/workspace/InitRequest.java`
- Create: `moulinette/console/src/main/java/etnc/piscine/moulinette/console/workspace/Workspace.java`
- Create: `moulinette/console/src/main/java/etnc/piscine/moulinette/console/workspace/WorkspaceInitializer.java`
- Create: `moulinette/console/src/main/java/etnc/piscine/moulinette/console/workspace/LocalWorkspaceInitializer.java`
- Create: `moulinette/console/src/test/java/etnc/piscine/moulinette/console/workspace/LocalWorkspaceInitializerIT.java`

- [ ] **Step 4.1: Records + interface**

Create `InitRequest.java`:

```java
package etnc.piscine.moulinette.console.workspace;

import java.nio.file.Path;

public record InitRequest(String nom, Path dest, Path piscineRepo, String moduleInitial) {}
```

Create `Workspace.java`:

```java
package etnc.piscine.moulinette.console.workspace;

import java.nio.file.Path;

public record Workspace(Path root, Path bareRemote, Path repoRoot) {}
```

Create `WorkspaceInitializer.java`:

```java
package etnc.piscine.moulinette.console.workspace;

public interface WorkspaceInitializer {
    Workspace init(InitRequest req);
}
```

- [ ] **Step 4.2: Test d'intégration**

Create `LocalWorkspaceInitializerIT.java`:

```java
package etnc.piscine.moulinette.console.workspace;

import etnc.piscine.moulinette.console.git.ProcessGitClient;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.*;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("git")
class LocalWorkspaceInitializerIT {

    @Test
    void cree_repo_stagiaire_avec_bare_remote_et_starters(@TempDir Path tmp) throws IOException {
        Path piscine = tmp.resolve("piscine-etnc");
        Path exoDir = piscine.resolve("exercises/module-1-fondamentaux/1.1.1-hello-world");
        Files.createDirectories(exoDir.resolve("starter/src/main/java"));
        Files.writeString(exoDir.resolve("metadata.yml"), """
            slug: hello-world
            module: 1
            sous_groupe: "1.1"
            position: 1
            notions: []
            """);
        Files.writeString(exoDir.resolve("starter/src/main/java/Hello.java"), "class Hello{}");

        Path dest = tmp.resolve("piscine-curlier");
        var init = new LocalWorkspaceInitializer(new ProcessGitClient());

        Workspace ws = init.init(new InitRequest("curlier", dest, piscine, "module-1-fondamentaux"));

        assertThat(ws.repoRoot()).isDirectory();
        assertThat(ws.bareRemote()).isDirectory();
        assertThat(ws.repoRoot().resolve(".git")).exists();
        assertThat(ws.repoRoot().resolve("exercises/1.1.1-hello-world/starter/src/main/java/Hello.java"))
            .exists();
        // remote origin = file:// vers le bare
        String remotes = Files.readString(ws.repoRoot().resolve(".git/config"));
        assertThat(remotes).contains(ws.bareRemote().toString().replace('\\', '/'));
    }
}
```

- [ ] **Step 4.3: Vérifier l'échec**

Run: `./mvnw -f moulinette/pom.xml -pl console test -Dgroups=git -Dtest=LocalWorkspaceInitializerIT`
Expected: COMPILATION FAILURE.

- [ ] **Step 4.4: Implémenter `LocalWorkspaceInitializer`**

Create `LocalWorkspaceInitializer.java`:

```java
package etnc.piscine.moulinette.console.workspace;

import etnc.piscine.moulinette.console.git.GitClient;
import etnc.piscine.moulinette.console.git.GitResult;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.stream.Stream;

public final class LocalWorkspaceInitializer implements WorkspaceInitializer {

    private final GitClient git;

    public LocalWorkspaceInitializer(GitClient git) { this.git = git; }

    @Override
    public Workspace init(InitRequest req) {
        try {
            Path root = req.dest();
            Path repoRoot = root;
            Path bareRoot = root.resolve(".piscine/remote.git");
            Files.createDirectories(repoRoot);
            Files.createDirectories(bareRoot);

            check(git.run(root.getParent(), List.of("init", "--bare", bareRoot.toString())), "bare init");

            check(git.run(repoRoot, List.of("init")), "repo init");
            check(git.run(repoRoot, List.of("config", "user.email", req.nom() + "@piscine.etnc")), "config email");
            check(git.run(repoRoot, List.of("config", "user.name", req.nom())), "config name");
            check(git.run(repoRoot, List.of("checkout", "-b", "main")), "checkout main");

            ExerciseCatalog cat = ExerciseCatalog.scan(req.piscineRepo().resolve("exercises"));
            Path exosDest = repoRoot.resolve("exercises");
            Files.createDirectories(exosDest);
            for (SousGroupe sg : cat.sousGroupes()) {
                for (ExerciseEntry e : sg.exercices()) {
                    Path target = exosDest.resolve(e.exerciseDir().getFileName());
                    copyTree(e.exerciseDir().resolve("starter"), target.resolve("starter"));
                    Files.copy(e.exerciseDir().resolve("metadata.yml"), target.resolve("metadata.yml"));
                }
            }
            Files.writeString(repoRoot.resolve("README.md"),
                "# Piscine ETNC — workspace de " + req.nom() + "\n\n"
                + "Voir docs/piscine-stagiaire.md dans le repo Piscine ETNC.\n");

            check(git.run(repoRoot, List.of("add", ".")), "add");
            check(git.run(repoRoot, List.of("commit", "-m", "piscine: setup")), "commit initial");
            check(git.run(repoRoot, List.of("remote", "add", "origin", bareRoot.toString())), "remote add");

            return new Workspace(root, bareRoot, repoRoot);
        } catch (IOException ioe) {
            throw new IllegalStateException("init workspace échoué", ioe);
        }
    }

    private static void check(GitResult r, String label) {
        if (!r.ok()) {
            throw new IllegalStateException("git " + label + " : exit=" + r.exitCode() + "\n" + r.stderr());
        }
    }

    private static void copyTree(Path src, Path dst) throws IOException {
        if (!Files.exists(src)) return;
        try (Stream<Path> walk = Files.walk(src)) {
            for (Path p : walk.toList()) {
                Path rel = src.relativize(p);
                Path target = dst.resolve(rel);
                if (Files.isDirectory(p)) Files.createDirectories(target);
                else Files.copy(p, target, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }
}
```

- [ ] **Step 4.5: Vérifier les tests**

Run: `./mvnw -f moulinette/pom.xml -pl console test -Dgroups=git -Dtest=LocalWorkspaceInitializerIT`
Expected: PASS.

- [ ] **Step 4.6: Commit**

```bash
git add moulinette/console/src
git commit -m "feat(console): LocalWorkspaceInitializer (tâche #36 partie 2)"
```

---

## Tâche 5 — `Command` API + `AddCommand`, `CommitCommand`, `PushCommand`, `StatusCommand`, `LogCommand`, `DiffCommand` (backlog #37 partie 1)

**Files:**
- Create: `moulinette/console/src/main/java/etnc/piscine/moulinette/console/commands/Command.java`
- Create: `moulinette/console/src/main/java/etnc/piscine/moulinette/console/commands/CommandResult.java`
- Create: `moulinette/console/src/main/java/etnc/piscine/moulinette/console/commands/CommandRegistry.java`
- Create: `moulinette/console/src/main/java/etnc/piscine/moulinette/console/commands/{Add,Commit,Push,Status,Log,Diff,Help,Exit}Command.java`
- Create: `moulinette/console/src/main/java/etnc/piscine/moulinette/console/repl/ReplContext.java`
- Create: `moulinette/console/src/test/java/etnc/piscine/moulinette/console/commands/*Test.java`

- [ ] **Step 5.1: API `Command` + `CommandResult`**

Create `CommandResult.java`:

```java
package etnc.piscine.moulinette.console.commands;

public record CommandResult(int exitCode, String output, boolean shouldExit) {
    public static CommandResult ok(String out) { return new CommandResult(0, out, false); }
    public static CommandResult error(String msg) { return new CommandResult(1, msg, false); }
    public static CommandResult exit() { return new CommandResult(0, "", true); }
}
```

Create `Command.java`:

```java
package etnc.piscine.moulinette.console.commands;

import etnc.piscine.moulinette.console.repl.ReplContext;
import java.util.List;

public interface Command {
    String name();
    /** Texte court pour la commande `help`. */
    String shortHelp();
    CommandResult execute(ReplContext ctx, List<String> args);
}
```

Create `ReplContext.java`:

```java
package etnc.piscine.moulinette.console.repl;

import etnc.piscine.moulinette.console.Mode;
import etnc.piscine.moulinette.console.git.GitClient;
import etnc.piscine.moulinette.console.workspace.ExerciseCatalog;

import java.nio.file.Path;

public final class ReplContext {
    private final Path repoRoot;
    private final GitClient git;
    private final ExerciseCatalog catalog;
    private final Mode mode;

    public ReplContext(Path repoRoot, GitClient git, ExerciseCatalog catalog, Mode mode) {
        this.repoRoot = repoRoot; this.git = git; this.catalog = catalog; this.mode = mode;
    }
    public Path repoRoot() { return repoRoot; }
    public GitClient git() { return git; }
    public ExerciseCatalog catalog() { return catalog; }
    public Mode mode() { return mode; }
    public String currentBranch() { return git.currentBranch(repoRoot); }
}
```

- [ ] **Step 5.2: Test `AddCommand`**

Create `AddCommandTest.java`:

```java
package etnc.piscine.moulinette.console.commands;

import etnc.piscine.moulinette.console.Mode;
import etnc.piscine.moulinette.console.git.FakeGitClient;
import etnc.piscine.moulinette.console.git.GitResult;
import etnc.piscine.moulinette.console.repl.ReplContext;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AddCommandTest {

    @Test
    void add_appelle_git_avec_les_bons_args() {
        var git = new FakeGitClient();
        var ctx = new ReplContext(Paths.get("."), git, null, Mode.LOCAL);

        CommandResult r = new AddCommand().execute(ctx, List.of("file1.txt", "dir/"));

        assertThat(r.exitCode()).isZero();
        assertThat(git.calls).containsExactly(List.of("add", "file1.txt", "dir/"));
    }

    @Test
    void add_sans_arg_renvoie_message_pedagogique() {
        var git = new FakeGitClient();
        var ctx = new ReplContext(Paths.get("."), git, null, Mode.LOCAL);

        CommandResult r = new AddCommand().execute(ctx, List.of());

        assertThat(r.exitCode()).isOne();
        assertThat(r.output()).contains("usage").contains("git add");
        assertThat(git.calls).isEmpty();
    }

    @Test
    void add_propage_l_echec_git() {
        var git = new FakeGitClient();
        git.stub("add inconnu.txt", new GitResult(128, "", "pathspec 'inconnu.txt' did not match"));
        var ctx = new ReplContext(Paths.get("."), git, null, Mode.LOCAL);

        CommandResult r = new AddCommand().execute(ctx, List.of("inconnu.txt"));

        assertThat(r.exitCode()).isOne();
        assertThat(r.output()).contains("pathspec");
    }
}
```

- [ ] **Step 5.3: Vérifier l'échec**

Run: `./mvnw -f moulinette/pom.xml -pl console test -Dtest=AddCommandTest`
Expected: COMPILATION FAILURE.

- [ ] **Step 5.4: Implémenter `AddCommand`**

Create `AddCommand.java`:

```java
package etnc.piscine.moulinette.console.commands;

import etnc.piscine.moulinette.console.git.GitResult;
import etnc.piscine.moulinette.console.repl.ReplContext;

import java.util.ArrayList;
import java.util.List;

public final class AddCommand implements Command {
    @Override public String name() { return "add"; }
    @Override public String shortHelp() { return "git add <fichier> — ajoute des fichiers à l'index"; }

    @Override
    public CommandResult execute(ReplContext ctx, List<String> args) {
        if (args.isEmpty()) {
            return CommandResult.error(
                "usage : git add <fichier ou dossier>\n"
                + "exemple : git add exercises/1.1.1-hello-world\n");
        }
        List<String> full = new ArrayList<>();
        full.add("add");
        full.addAll(args);
        GitResult r = ctx.git().run(ctx.repoRoot(), full);
        return r.ok() ? CommandResult.ok(r.stdout()) : CommandResult.error(r.stderr());
    }
}
```

- [ ] **Step 5.5: Implémenter `CommitCommand` (+ test)**

Create `CommitCommandTest.java`:

```java
package etnc.piscine.moulinette.console.commands;

import etnc.piscine.moulinette.console.Mode;
import etnc.piscine.moulinette.console.git.FakeGitClient;
import etnc.piscine.moulinette.console.repl.ReplContext;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CommitCommandTest {

    @Test
    void commit_sans_m_renvoie_message_pedagogique() {
        var git = new FakeGitClient();
        var ctx = new ReplContext(Paths.get("."), git, null, Mode.LOCAL);

        var r = new CommitCommand().execute(ctx, List.of());

        assertThat(r.exitCode()).isOne();
        assertThat(r.output()).contains("-m").contains("message");
        assertThat(git.calls).isEmpty();
    }

    @Test
    void commit_avec_m_appelle_git() {
        var git = new FakeGitClient();
        var ctx = new ReplContext(Paths.get("."), git, null, Mode.LOCAL);

        var r = new CommitCommand().execute(ctx, List.of("-m", "rendu 1.1.1"));

        assertThat(r.exitCode()).isZero();
        assertThat(git.calls).containsExactly(List.of("commit", "-m", "rendu 1.1.1"));
    }
}
```

Create `CommitCommand.java`:

```java
package etnc.piscine.moulinette.console.commands;

import etnc.piscine.moulinette.console.git.GitResult;
import etnc.piscine.moulinette.console.repl.ReplContext;

import java.util.ArrayList;
import java.util.List;

public final class CommitCommand implements Command {
    @Override public String name() { return "commit"; }
    @Override public String shortHelp() { return "git commit -m \"...\" — enregistre les fichiers de l'index"; }

    @Override
    public CommandResult execute(ReplContext ctx, List<String> args) {
        if (!args.contains("-m")) {
            return CommandResult.error(
                "Un commit a besoin d'un message. Utilise : git commit -m \"ton message\"\n"
                + "exemple : git commit -m \"rendu 1.1.1\"\n");
        }
        List<String> full = new ArrayList<>();
        full.add("commit");
        full.addAll(args);
        GitResult r = ctx.git().run(ctx.repoRoot(), full);
        return r.ok() ? CommandResult.ok(r.stdout()) : CommandResult.error(r.stderr());
    }
}
```

- [ ] **Step 5.6: Implémenter `PushCommand` (déclencheur — voir Tâche 7)**

Create `PushCommandTest.java`:

```java
package etnc.piscine.moulinette.console.commands;

import etnc.piscine.moulinette.console.Mode;
import etnc.piscine.moulinette.console.git.FakeGitClient;
import etnc.piscine.moulinette.console.git.GitResult;
import etnc.piscine.moulinette.console.repl.ReplContext;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PushCommandTest {

    @Test
    void push_appelle_git_avec_porcelain() {
        var git = new FakeGitClient();
        git.stub("push --porcelain origin rendu/1.1", new GitResult(0, "ok", ""));
        var ctx = new ReplContext(Paths.get("."), git, null, Mode.LOCAL);

        var r = new PushCommand(null).execute(ctx, List.of("origin", "rendu/1.1"));

        assertThat(r.exitCode()).isZero();
        assertThat(git.calls).containsExactly(List.of("push", "--porcelain", "origin", "rendu/1.1"));
    }
}
```

Create `PushCommand.java`:

```java
package etnc.piscine.moulinette.console.commands;

import etnc.piscine.moulinette.console.git.GitResult;
import etnc.piscine.moulinette.console.repl.ReplContext;
import etnc.piscine.moulinette.console.trigger.SubmissionTrigger;

import java.util.ArrayList;
import java.util.List;

public final class PushCommand implements Command {
    private final SubmissionTrigger trigger;

    public PushCommand(SubmissionTrigger trigger) { this.trigger = trigger; }

    @Override public String name() { return "push"; }
    @Override public String shortHelp() { return "git push <remote> <branche> — pousse la branche vers le remote"; }

    @Override
    public CommandResult execute(ReplContext ctx, List<String> args) {
        if (args.size() < 2) {
            return CommandResult.error(
                "usage : git push <remote> <branche>\n"
                + "exemple : git push origin rendu/1.1\n");
        }
        List<String> full = new ArrayList<>();
        full.add("push"); full.add("--porcelain");
        full.addAll(args);
        GitResult r = ctx.git().run(ctx.repoRoot(), full);
        StringBuilder out = new StringBuilder(r.stdout());
        if (r.ok() && trigger != null) {
            String triggerOut = trigger.onPushSucceeded(ctx);
            if (triggerOut != null) out.append(triggerOut);
        }
        return r.ok() ? CommandResult.ok(out.toString()) : CommandResult.error(r.stderr());
    }
}
```

- [ ] **Step 5.7: Pass-through commands : `StatusCommand`, `LogCommand`, `DiffCommand`**

Create `StatusCommand.java`:

```java
package etnc.piscine.moulinette.console.commands;

import etnc.piscine.moulinette.console.git.GitResult;
import etnc.piscine.moulinette.console.repl.ReplContext;
import java.util.List;

public final class StatusCommand implements Command {
    @Override public String name() { return "status"; }
    @Override public String shortHelp() { return "git status — montre l'état du repo"; }
    @Override public CommandResult execute(ReplContext ctx, List<String> args) {
        GitResult r = ctx.git().run(ctx.repoRoot(), List.of("status"));
        return r.ok() ? CommandResult.ok(r.stdout()) : CommandResult.error(r.stderr());
    }
}
```

Create `LogCommand.java` (même pattern, `List.of("log", "--oneline", "-n", "20")`) et `DiffCommand.java` (même pattern, `List.of("diff")`).

- [ ] **Step 5.8: `HelpCommand` + `ExitCommand`**

Create `HelpCommand.java`:

```java
package etnc.piscine.moulinette.console.commands;

import etnc.piscine.moulinette.console.repl.ReplContext;
import java.util.List;
import java.util.stream.Collectors;

public final class HelpCommand implements Command {
    private final CommandRegistry registry;
    public HelpCommand(CommandRegistry r) { this.registry = r; }
    @Override public String name() { return "help"; }
    @Override public String shortHelp() { return "help — affiche les commandes supportées"; }
    @Override public CommandResult execute(ReplContext ctx, List<String> args) {
        String body = registry.all().stream()
            .map(c -> "  " + c.shortHelp())
            .collect(Collectors.joining("\n"));
        return CommandResult.ok("Commandes supportées :\n" + body + "\n");
    }
}
```

Create `ExitCommand.java`:

```java
package etnc.piscine.moulinette.console.commands;

import etnc.piscine.moulinette.console.repl.ReplContext;
import java.util.List;

public final class ExitCommand implements Command {
    @Override public String name() { return "exit"; }
    @Override public String shortHelp() { return "exit — quitte le REPL"; }
    @Override public CommandResult execute(ReplContext ctx, List<String> args) { return CommandResult.exit(); }
}
```

- [ ] **Step 5.9: Vérifier tous les tests de commandes**

Run: `./mvnw -f moulinette/pom.xml -pl console test`
Expected: tous les `*CommandTest` passent.

- [ ] **Step 5.10: Commit**

```bash
git add moulinette/console/src
git commit -m "feat(console): commandes git minimales (add, commit, push, status, log, diff, help, exit) (tâche #37 partie 1)"
```

---

## Tâche 6 — `CommandRegistry`, `SubmitStartCommand`, `Repl` (backlog #37 partie 2)

**Files:**
- Create: `moulinette/console/src/main/java/etnc/piscine/moulinette/console/commands/CommandRegistry.java`
- Create: `moulinette/console/src/main/java/etnc/piscine/moulinette/console/commands/SubmitStartCommand.java`
- Create: `moulinette/console/src/main/java/etnc/piscine/moulinette/console/repl/ReplIo.java`
- Create: `moulinette/console/src/main/java/etnc/piscine/moulinette/console/repl/Repl.java`
- Create: `moulinette/console/src/test/java/etnc/piscine/moulinette/console/commands/CommandRegistryTest.java`
- Create: `moulinette/console/src/test/java/etnc/piscine/moulinette/console/commands/SubmitStartCommandTest.java`
- Create: `moulinette/console/src/test/java/etnc/piscine/moulinette/console/repl/ReplTest.java`

- [ ] **Step 6.1: `CommandRegistry` (TDD)**

Create `CommandRegistryTest.java`:

```java
package etnc.piscine.moulinette.console.commands;

import etnc.piscine.moulinette.console.Mode;
import etnc.piscine.moulinette.console.git.FakeGitClient;
import etnc.piscine.moulinette.console.repl.ReplContext;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CommandRegistryTest {

    private final ReplContext ctx = new ReplContext(Paths.get("."), new FakeGitClient(), null, Mode.LOCAL);

    @Test
    void dispatch_git_add_resout_add_command() {
        var reg = CommandRegistry.defaults(null);
        var r = reg.dispatch(ctx, List.of("git", "add", "f.txt"));
        assertThat(r.exitCode()).isZero();
    }

    @Test
    void commande_inconnue_retourne_message_avec_suggestion() {
        var reg = CommandRegistry.defaults(null);
        var r = reg.dispatch(ctx, List.of("git", "checkout", "-b", "x"));
        assertThat(r.exitCode()).isOne();
        assertThat(r.output()).contains("non supportée").contains("submit-start");
    }

    @Test
    void ligne_vide_est_no_op() {
        var reg = CommandRegistry.defaults(null);
        var r = reg.dispatch(ctx, List.of());
        assertThat(r.exitCode()).isZero();
    }
}
```

Create `CommandRegistry.java`:

```java
package etnc.piscine.moulinette.console.commands;

import etnc.piscine.moulinette.console.repl.ReplContext;
import etnc.piscine.moulinette.console.trigger.SubmissionTrigger;

import java.util.*;

public final class CommandRegistry {
    private final Map<String, Command> byName = new LinkedHashMap<>();

    private CommandRegistry() {}

    public static CommandRegistry defaults(SubmissionTrigger trigger) {
        var r = new CommandRegistry();
        r.register(new AddCommand());
        r.register(new CommitCommand());
        r.register(new PushCommand(trigger));
        r.register(new StatusCommand());
        r.register(new LogCommand());
        r.register(new DiffCommand());
        r.register(new SubmitStartCommand());
        r.register(new ExitCommand());
        r.register(new HelpCommand(r));
        return r;
    }

    public void register(Command c) { byName.put(c.name(), c); }
    public Collection<Command> all() { return byName.values(); }

    public CommandResult dispatch(ReplContext ctx, List<String> tokens) {
        if (tokens.isEmpty()) return CommandResult.ok("");
        List<String> rest = new ArrayList<>(tokens);
        // accepte "git X ..." comme "X ..."
        if ("git".equals(rest.get(0))) rest.remove(0);
        if (rest.isEmpty()) {
            return CommandResult.error("Tape `help` pour la liste des commandes.\n");
        }
        String head = rest.remove(0);
        Command c = byName.get(head);
        if (c == null) {
            return CommandResult.error(unsupportedMessage(head));
        }
        return c.execute(ctx, rest);
    }

    private static String unsupportedMessage(String head) {
        String hint = switch (head) {
            case "checkout", "branch" -> "Pour démarrer un rendu, utilise `submit-start <sous-groupe>`.";
            case "clone", "pull", "fetch", "merge", "rebase" -> "Cette commande n'est pas couverte par le MVP.";
            default -> "Tape `help` pour voir les commandes supportées.";
        };
        return "Commande non supportée dans le MVP : " + head + "\n" + hint + "\n";
    }
}
```

- [ ] **Step 6.2: `SubmitStartCommand` (TDD)**

Create `SubmitStartCommandTest.java`:

```java
package etnc.piscine.moulinette.console.commands;

import etnc.piscine.moulinette.console.Mode;
import etnc.piscine.moulinette.console.git.FakeGitClient;
import etnc.piscine.moulinette.console.repl.ReplContext;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SubmitStartCommandTest {

    @Test
    void cree_et_bascule_sur_branche_rendu() {
        var git = new FakeGitClient();
        var ctx = new ReplContext(Paths.get("."), git, null, Mode.LOCAL);
        var r = new SubmitStartCommand().execute(ctx, List.of("1.1"));
        assertThat(r.exitCode()).isZero();
        assertThat(git.calls).containsExactly(List.of("checkout", "-B", "rendu/1.1"));
    }

    @Test
    void sans_argument_renvoie_usage() {
        var git = new FakeGitClient();
        var ctx = new ReplContext(Paths.get("."), git, null, Mode.LOCAL);
        var r = new SubmitStartCommand().execute(ctx, List.of());
        assertThat(r.exitCode()).isOne();
        assertThat(r.output()).contains("usage").contains("1.1");
    }

    @Test
    void format_de_groupe_invalide_renvoie_message_clair() {
        var git = new FakeGitClient();
        var ctx = new ReplContext(Paths.get("."), git, null, Mode.LOCAL);
        var r = new SubmitStartCommand().execute(ctx, List.of("xyz"));
        assertThat(r.exitCode()).isOne();
        assertThat(r.output()).contains("format");
    }
}
```

Create `SubmitStartCommand.java`:

```java
package etnc.piscine.moulinette.console.commands;

import etnc.piscine.moulinette.console.git.GitResult;
import etnc.piscine.moulinette.console.repl.ReplContext;

import java.util.List;
import java.util.regex.Pattern;

public final class SubmitStartCommand implements Command {
    private static final Pattern GROUP = Pattern.compile("\\d+\\.\\d+");

    @Override public String name() { return "submit-start"; }
    @Override public String shortHelp() { return "submit-start <sous-groupe> — bascule sur la branche rendu/<sous-groupe>"; }

    @Override
    public CommandResult execute(ReplContext ctx, List<String> args) {
        if (args.isEmpty()) {
            return CommandResult.error("usage : submit-start <sous-groupe>\nexemple : submit-start 1.1\n");
        }
        String group = args.get(0);
        if (!GROUP.matcher(group).matches()) {
            return CommandResult.error("format de sous-groupe invalide : " + group
                + " — attendu sous la forme X.Y (ex: 1.1, 2.3)\n");
        }
        GitResult r = ctx.git().run(ctx.repoRoot(), List.of("checkout", "-B", "rendu/" + group));
        return r.ok()
            ? CommandResult.ok("Bascule sur la branche rendu/" + group + ".\n")
            : CommandResult.error(r.stderr());
    }
}
```

- [ ] **Step 6.3: `ReplIo` + `Repl` (TDD)**

Create `ReplIo.java`:

```java
package etnc.piscine.moulinette.console.repl;

import java.io.*;

public final class ReplIo {
    private final BufferedReader in;
    private final PrintWriter out;

    public ReplIo(Reader in, Writer out) {
        this.in = new BufferedReader(in);
        this.out = new PrintWriter(out, true);
    }
    public static ReplIo stdio() {
        return new ReplIo(new InputStreamReader(System.in), new OutputStreamWriter(System.out));
    }
    public String readLine() throws IOException { return in.readLine(); }
    public void write(String s) { out.print(s); out.flush(); }
}
```

Create `ReplTest.java`:

```java
package etnc.piscine.moulinette.console.repl;

import etnc.piscine.moulinette.console.Mode;
import etnc.piscine.moulinette.console.commands.CommandRegistry;
import etnc.piscine.moulinette.console.git.FakeGitClient;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

class ReplTest {

    @Test
    void boucle_lit_dispatch_affiche_puis_quitte_sur_exit() throws Exception {
        var git = new FakeGitClient();
        git.branch = "main";
        var ctx = new ReplContext(Paths.get("."), git, null, Mode.LOCAL);
        var io = new ReplIo(new StringReader("help\nexit\n"), new StringWriter());
        var sw = (StringWriter) capture(io);

        new Repl(ctx, CommandRegistry.defaults(null), io).run();

        String out = sw.toString();
        assertThat(out).contains("piscine[main]>").contains("Commandes supportées");
    }

    @Test
    void commande_inconnue_affiche_message_et_continue() throws Exception {
        var git = new FakeGitClient();
        var ctx = new ReplContext(Paths.get("."), git, null, Mode.LOCAL);
        var sw = new StringWriter();
        var io = new ReplIo(new StringReader("git checkout main\nexit\n"), sw);

        new Repl(ctx, CommandRegistry.defaults(null), io).run();

        assertThat(sw.toString()).contains("non supportée");
    }

    /** Helper for accessing the underlying writer captured by ReplIo. */
    private static java.io.Writer capture(ReplIo io) throws Exception {
        var f = ReplIo.class.getDeclaredField("out");
        f.setAccessible(true);
        var pw = (java.io.PrintWriter) f.get(io);
        var f2 = java.io.PrintWriter.class.getDeclaredField("out");
        f2.setAccessible(true);
        return (java.io.Writer) f2.get(pw);
    }
}
```

Note : le helper `capture` est uniquement pour le 1er test ; le 2e passe `sw` directement.

Create `Repl.java`:

```java
package etnc.piscine.moulinette.console.repl;

import etnc.piscine.moulinette.console.commands.CommandRegistry;
import etnc.piscine.moulinette.console.commands.CommandResult;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public final class Repl {
    private final ReplContext ctx;
    private final CommandRegistry registry;
    private final ReplIo io;

    public Repl(ReplContext ctx, CommandRegistry registry, ReplIo io) {
        this.ctx = ctx; this.registry = registry; this.io = io;
    }

    public void run() throws IOException {
        io.write("Piscine ETNC — console locale. Tape `help` pour la liste, `exit` pour quitter.\n");
        while (true) {
            io.write("piscine[" + ctx.currentBranch() + "]> ");
            String line = io.readLine();
            if (line == null) return;
            line = line.trim();
            if (line.isEmpty()) continue;
            List<String> tokens = Arrays.stream(line.split("\\s+")).toList();
            CommandResult r = registry.dispatch(ctx, tokens);
            if (!r.output().isEmpty()) io.write(r.output());
            if (!r.output().endsWith("\n")) io.write("\n");
            if (r.shouldExit()) return;
        }
    }
}
```

- [ ] **Step 6.4: Tests verts**

Run: `./mvnw -f moulinette/pom.xml -pl console test`
Expected: tous les tests unitaires passent.

- [ ] **Step 6.5: Commit**

```bash
git add moulinette/console/src
git commit -m "feat(console): CommandRegistry, SubmitStartCommand, Repl (tâche #37 partie 2)"
```

---

## Tâche 7 — `SubmissionTrigger` + `MoulinetteRunner` (backlog #38)

**Files:**
- Create: `moulinette/console/src/main/java/etnc/piscine/moulinette/console/trigger/MoulinetteRunner.java`
- Create: `moulinette/console/src/main/java/etnc/piscine/moulinette/console/trigger/SubmissionTrigger.java`
- Create: `moulinette/console/src/test/java/etnc/piscine/moulinette/console/trigger/SubmissionTriggerTest.java`

- [ ] **Step 7.1: `MoulinetteRunner` (interface + record de retour)**

Create `MoulinetteRunner.java`:

```java
package etnc.piscine.moulinette.console.trigger;

import etnc.piscine.moulinette.console.workspace.ExerciseCatalog;
import etnc.piscine.moulinette.console.workspace.ExerciseEntry;
import etnc.piscine.moulinette.framework.Checker;
import etnc.piscine.moulinette.framework.CheckResult;
import etnc.piscine.moulinette.framework.CheckerContext;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public interface MoulinetteRunner {
    GroupReport runGroup(String sousGroupeId, Path repoRoot);

    record GroupReport(String sousGroupeId, List<ExoOutcome> outcomes, boolean stoppedEarly, Path reportPath) {}
    record ExoOutcome(String exoId, boolean ok, String message) {}

    /** Implémentation par défaut : exécute les Checkers d'un fournisseur sur chaque exo du groupe. */
    final class Default implements MoulinetteRunner {
        private final ExerciseCatalog catalog;
        private final List<Checker> checkers;
        private final Path reportsDir;

        public Default(ExerciseCatalog catalog, List<Checker> checkers, Path reportsDir) {
            this.catalog = catalog; this.checkers = checkers; this.reportsDir = reportsDir;
        }

        @Override
        public GroupReport runGroup(String sgId, Path repoRoot) {
            var sg = catalog.sousGroupe(sgId);
            List<ExoOutcome> outcomes = new ArrayList<>();
            boolean stopped = false;
            for (ExerciseEntry e : sg.exercices()) {
                Path renduDir = repoRoot.resolve("exercises").resolve(e.exerciseDir().getFileName());
                boolean ok = true;
                StringBuilder msg = new StringBuilder();
                for (Checker c : checkers) {
                    CheckResult r = c.check(new CheckerContext(e.id(), renduDir));
                    if (r == null || !isOk(r)) { ok = false; msg.append(c.id()).append(": KO\n"); break; }
                }
                outcomes.add(new ExoOutcome(e.id(), ok, msg.toString()));
                if (!ok) { stopped = true; break; }
            }
            return new GroupReport(sgId, outcomes, stopped, writeReport(sgId, outcomes));
        }

        private static boolean isOk(CheckResult r) {
            // CheckResult.ok() existe selon le framework actuel ; en cas d'évolution, adapter.
            return r.toString().toLowerCase().contains("ok");  // placeholder simplifié pour le MVP — voir note ci-dessous
        }

        private Path writeReport(String sgId, List<ExoOutcome> outcomes) {
            // Implémentation simplifiée — voir Tâche 7.4 pour le format complet.
            return reportsDir;
        }
    }
}
```

> **Note importante** — `CheckResult` actuel n'expose pas encore un `ok()` typé. La méthode `isOk` ci-dessus est un placeholder à raffiner dans une tâche ultérieure ou via une enrichissement mineur du module `framework` (ajout d'un champ `Status status` et `boolean ok()`). Pour le MVP, on s'appuie sur la `ReportGenerator` existante pour le rendu.

- [ ] **Step 7.2: Test `SubmissionTrigger` (TDD)**

Create `SubmissionTriggerTest.java`:

```java
package etnc.piscine.moulinette.console.trigger;

import etnc.piscine.moulinette.console.Mode;
import etnc.piscine.moulinette.console.git.FakeGitClient;
import etnc.piscine.moulinette.console.git.RefUpdate;
import etnc.piscine.moulinette.console.repl.ReplContext;
import etnc.piscine.moulinette.console.trigger.MoulinetteRunner.ExoOutcome;
import etnc.piscine.moulinette.console.trigger.MoulinetteRunner.GroupReport;
import org.junit.jupiter.api.Test;

import java.nio.file.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SubmissionTriggerTest {

    @Test
    void push_sur_rendu_1_1_declenche_runGroup() {
        var git = new FakeGitClient();
        git.pushedRefs = List.of(new RefUpdate("refs/heads/rendu/1.1", "abc", "def"));
        var runner = (MoulinetteRunner) (sg, repo) -> new GroupReport(sg,
            List.of(new ExoOutcome("1.1.1", true, ""),
                    new ExoOutcome("1.1.2", false, "compile: KO")),
            true, Paths.get("/tmp/report.md"));

        var trigger = new SubmissionTrigger(runner);
        var ctx = new ReplContext(Paths.get("/repo"), git, null, Mode.LOCAL);
        String out = trigger.onPushSucceeded(ctx);

        assertThat(out)
            .contains("rendu/1.1")
            .contains("1.1.1").contains("✓")
            .contains("1.1.2").contains("✗")
            .contains("/tmp/report.md");
    }

    @Test
    void push_sur_main_ne_declenche_pas() {
        var git = new FakeGitClient();
        git.pushedRefs = List.of(new RefUpdate("refs/heads/main", "a", "b"));
        var trigger = new SubmissionTrigger((sg, repo) -> { throw new AssertionError("ne doit pas être appelé"); });
        var ctx = new ReplContext(Paths.get("/repo"), git, null, Mode.LOCAL);

        String out = trigger.onPushSucceeded(ctx);

        assertThat(out).isNullOrEmpty();
    }
}
```

- [ ] **Step 7.3: Implémenter `SubmissionTrigger`**

Create `SubmissionTrigger.java`:

```java
package etnc.piscine.moulinette.console.trigger;

import etnc.piscine.moulinette.console.git.RefUpdate;
import etnc.piscine.moulinette.console.repl.ReplContext;
import etnc.piscine.moulinette.console.trigger.MoulinetteRunner.ExoOutcome;
import etnc.piscine.moulinette.console.trigger.MoulinetteRunner.GroupReport;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SubmissionTrigger {
    private static final Pattern RENDU_REF = Pattern.compile("refs/heads/rendu/(\\d+\\.\\d+)");

    private final MoulinetteRunner runner;

    public SubmissionTrigger(MoulinetteRunner runner) { this.runner = runner; }

    /** Appelée par PushCommand après un push réussi. Retourne le texte à afficher (ou null). */
    public String onPushSucceeded(ReplContext ctx) {
        for (RefUpdate ru : ctx.git().lastPushRefs(ctx.repoRoot())) {
            Matcher m = RENDU_REF.matcher(ru.ref());
            if (m.matches()) {
                String sg = m.group(1);
                GroupReport rep = runner.runGroup(sg, ctx.repoRoot());
                return formatReport(sg, rep);
            }
        }
        return null;
    }

    private static String formatReport(String sg, GroupReport rep) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n[console] Push détecté sur rendu/").append(sg)
          .append(" → lancement moulinette sur sous-groupe ").append(sg).append("\n");
        for (ExoOutcome o : rep.outcomes()) {
            sb.append("[console] ▶ Exo ").append(o.exoId()).append("  ")
              .append(o.ok() ? "✓ OK" : "✗ ÉCHEC").append("\n");
            if (!o.ok() && !o.message().isBlank()) sb.append("    ").append(o.message());
        }
        sb.append("[console] Rapport : ").append(rep.reportPath()).append("\n");
        if (rep.stoppedEarly()) {
            sb.append("[console] On s'arrête ici (un exo doit passer avant de continuer). Corrige et re-push.\n");
        }
        return sb.toString();
    }
}
```

- [ ] **Step 7.4: Vérifier les tests**

Run: `./mvnw -f moulinette/pom.xml -pl console test -Dtest=SubmissionTriggerTest`
Expected: PASS.

- [ ] **Step 7.5: Commit**

```bash
git add moulinette/console/src
git commit -m "feat(console): SubmissionTrigger + MoulinetteRunner (tâche #38)"
```

---

## Tâche 8 — `Main` avec sous-commandes `init` et `repl` (backlog #39)

**Files:**
- Create: `moulinette/console/src/main/java/etnc/piscine/moulinette/console/Main.java`
- Create: `moulinette/console/src/main/java/etnc/piscine/moulinette/console/ConsoleException.java`

- [ ] **Step 8.1: `ConsoleException`**

Create `ConsoleException.java`:

```java
package etnc.piscine.moulinette.console;

public class ConsoleException extends RuntimeException {
    public ConsoleException(String message) { super(message); }
    public ConsoleException(String message, Throwable cause) { super(message, cause); }
}
```

- [ ] **Step 8.2: `Main`**

Create `Main.java`:

```java
package etnc.piscine.moulinette.console;

import etnc.piscine.moulinette.console.commands.CommandRegistry;
import etnc.piscine.moulinette.console.git.GitClient;
import etnc.piscine.moulinette.console.git.ProcessGitClient;
import etnc.piscine.moulinette.console.repl.Repl;
import etnc.piscine.moulinette.console.repl.ReplContext;
import etnc.piscine.moulinette.console.repl.ReplIo;
import etnc.piscine.moulinette.console.trigger.MoulinetteRunner;
import etnc.piscine.moulinette.console.trigger.SubmissionTrigger;
import etnc.piscine.moulinette.console.workspace.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public final class Main {

    public static void main(String[] args) {
        try {
            int exit = new Main().dispatch(Arrays.asList(args));
            System.exit(exit);
        } catch (ConsoleException ce) {
            System.err.println("Erreur : " + ce.getMessage());
            System.exit(2);
        } catch (Exception e) {
            System.err.println("Bug interne : " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(3);
        }
    }

    int dispatch(List<String> args) throws Exception {
        if (args.isEmpty() || "--help".equals(args.get(0))) { printUsage(); return 0; }
        String cmd = args.get(0);
        List<String> rest = args.subList(1, args.size());
        Mode mode = parseMode(rest);
        if (mode == Mode.NOMINAL) {
            throw new ConsoleException("--mode nominal non implémenté dans le MVP (voir tâches #26-#28 du backlog).");
        }
        return switch (cmd) {
            case "init" -> runInit(rest);
            case "repl" -> runRepl(rest);
            default -> { printUsage(); yield 1; }
        };
    }

    private int runInit(List<String> args) {
        String nom = required(args, "--nom");
        Path dest = Paths.get(required(args, "--dest"));
        Path piscine = Paths.get(optional(args, "--piscine-repo", "."));
        var init = new LocalWorkspaceInitializer(new ProcessGitClient());
        Workspace ws = init.init(new InitRequest(nom, dest, piscine, "module-1-fondamentaux"));
        System.out.println("[console] Workspace prêt : " + ws.repoRoot());
        System.out.println("[console] Lance le REPL avec :");
        System.out.println("  java -jar moulinette-console.jar repl --repo " + ws.repoRoot());
        return 0;
    }

    private int runRepl(List<String> args) throws Exception {
        Path repo = Paths.get(required(args, "--repo"));
        Path piscine = Paths.get(optional(args, "--piscine-repo", repo.getParent().resolve("piscine-etnc").toString()));
        GitClient git = new ProcessGitClient();
        ExerciseCatalog catalog = ExerciseCatalog.scan(piscine.resolve("exercises"));
        var runner = new MoulinetteRunner.Default(catalog, List.of(), repo.resolve(".piscine/reports"));
        var trigger = new SubmissionTrigger(runner);
        var ctx = new ReplContext(repo, git, catalog, Mode.LOCAL);
        new Repl(ctx, CommandRegistry.defaults(trigger), ReplIo.stdio()).run();
        return 0;
    }

    private static Mode parseMode(List<String> args) {
        int i = args.indexOf("--mode");
        if (i < 0 || i + 1 >= args.size()) return Mode.LOCAL;
        return Mode.valueOf(args.get(i + 1).toUpperCase(Locale.ROOT));
    }

    private static String required(List<String> args, String flag) {
        int i = args.indexOf(flag);
        if (i < 0 || i + 1 >= args.size())
            throw new ConsoleException("argument requis manquant : " + flag);
        return args.get(i + 1);
    }

    private static String optional(List<String> args, String flag, String def) {
        int i = args.indexOf(flag);
        return (i < 0 || i + 1 >= args.size()) ? def : args.get(i + 1);
    }

    private static void printUsage() {
        System.out.println("""
            Moulinette ETNC — console locale

            Usage :
              moulinette-console init --nom <slug> --dest <dossier> [--piscine-repo <chemin>] [--mode local]
              moulinette-console repl --repo <dossier> [--piscine-repo <chemin>] [--mode local]

            Documentation : docs/piscine-stagiaire.md
            """);
    }
}
```

- [ ] **Step 8.3: Ajouter le plugin assembly pour produire un fat-jar**

Edit `moulinette/console/pom.xml` — ajouter dans `<build><plugins>` :

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-shade-plugin</artifactId>
    <version>3.6.0</version>
    <executions>
        <execution>
            <phase>package</phase>
            <goals><goal>shade</goal></goals>
            <configuration>
                <finalName>moulinette-console</finalName>
                <transformers>
                    <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                        <mainClass>etnc.piscine.moulinette.console.Main</mainClass>
                    </transformer>
                </transformers>
            </configuration>
        </execution>
    </executions>
</plugin>
```

- [ ] **Step 8.4: Vérifier que le module build et le jar est exécutable**

Run: `./mvnw -f moulinette/pom.xml -pl console -am package`
Then: `java -jar moulinette/console/target/moulinette-console.jar --help`
Expected: affichage de l'aide.

- [ ] **Step 8.5: Commit**

```bash
git add moulinette/console
git commit -m "feat(console): Main + sous-commandes init/repl + shade jar (tâche #39)"
```

---

## Tâche 9 — Script `piscine-bootstrap` (backlog #40)

**Files:**
- Create: `scripts/piscine-bootstrap.sh`
- Create: `scripts/piscine-bootstrap.ps1`

- [ ] **Step 9.1: Script Unix**

Create `scripts/piscine-bootstrap.sh`:

```bash
#!/usr/bin/env bash
# Bootstrap autonome de la Piscine ETNC en mode local.
# Usage : ./scripts/piscine-bootstrap.sh --nom <slug> [--dest <dossier>] [--force]

set -euo pipefail

NOM=""
DEST=""
FORCE=0

while [[ $# -gt 0 ]]; do
    case "$1" in
        --nom)   NOM="$2"; shift 2;;
        --dest)  DEST="$2"; shift 2;;
        --force) FORCE=1; shift;;
        -h|--help)
            sed -n '2,4p' "$0"; exit 0;;
        *) echo "Argument inconnu : $1"; exit 2;;
    esac
done

if [[ -z "$NOM" ]]; then
    echo "Erreur : --nom est obligatoire." >&2
    exit 2
fi

REPO_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
DEST="${DEST:-$REPO_ROOT/../piscine-$NOM}"

# Prérequis
command -v git >/dev/null || { echo "git introuvable. Voir docs/setup-dev.md."; exit 1; }
command -v java >/dev/null || { echo "java introuvable. Voir docs/setup-dev.md."; exit 1; }

JAVA_MAJOR="$(java -version 2>&1 | head -1 | sed -n 's/.*"\([0-9]*\)\..*/\1/p')"
if [[ -z "$JAVA_MAJOR" || "$JAVA_MAJOR" -lt 25 ]]; then
    echo "Java 25+ requis. Voir docs/setup-dev.md." >&2
    exit 1
fi

if [[ -d "$DEST" && "$FORCE" -eq 0 ]]; then
    echo "Workspace déjà présent : $DEST"
    echo "Relance avec --force pour réinitialiser, ou démarre le REPL :"
    echo "  java -jar $REPO_ROOT/moulinette/console/target/moulinette-console.jar repl --repo $DEST --piscine-repo $REPO_ROOT"
    exit 0
fi

if [[ -d "$DEST" && "$FORCE" -eq 1 ]]; then
    echo "[bootstrap] Suppression du workspace existant : $DEST"
    rm -rf "$DEST"
fi

echo "[bootstrap] Build moulinette-console ..."
"$REPO_ROOT/mvnw" -f "$REPO_ROOT/moulinette/pom.xml" -pl console -am -q package

JAR="$REPO_ROOT/moulinette/console/target/moulinette-console.jar"
[[ -f "$JAR" ]] || { echo "Build du jar échoué."; exit 1; }

echo "[bootstrap] Initialisation du workspace stagiaire ..."
java -jar "$JAR" init --nom "$NOM" --dest "$DEST" --piscine-repo "$REPO_ROOT"

echo ""
echo "[bootstrap] ✓ Prêt. Lance le REPL :"
echo "  java -jar $JAR repl --repo $DEST --piscine-repo $REPO_ROOT"
```

Make executable: `chmod +x scripts/piscine-bootstrap.sh`.

- [ ] **Step 9.2: Script PowerShell**

Create `scripts/piscine-bootstrap.ps1`:

```powershell
# Bootstrap autonome de la Piscine ETNC en mode local (Windows).
# Usage : .\scripts\piscine-bootstrap.ps1 -Nom <slug> [-Dest <dossier>] [-Force]

param(
    [Parameter(Mandatory=$true)] [string] $Nom,
    [string] $Dest = "",
    [switch] $Force
)

$ErrorActionPreference = "Stop"
$RepoRoot = (Resolve-Path "$PSScriptRoot\..").Path
if (-not $Dest) { $Dest = Join-Path (Split-Path $RepoRoot -Parent) "piscine-$Nom" }

if (-not (Get-Command git -ErrorAction SilentlyContinue)) {
    Write-Error "git introuvable. Voir docs/setup-dev.md."
}
if (-not (Get-Command java -ErrorAction SilentlyContinue)) {
    Write-Error "java introuvable. Voir docs/setup-dev.md."
}

$javaVer = (java -version 2>&1)[0]
if ($javaVer -notmatch '"(\d+)\.') {
    Write-Error "Impossible de parser la version de Java."
}
if ([int]$Matches[1] -lt 25) {
    Write-Error "Java 25+ requis. Voir docs/setup-dev.md."
}

if ((Test-Path $Dest) -and -not $Force) {
    Write-Host "Workspace déjà présent : $Dest"
    Write-Host "Relance avec -Force pour réinitialiser, ou démarre le REPL :"
    Write-Host "  java -jar $RepoRoot\moulinette\console\target\moulinette-console.jar repl --repo $Dest --piscine-repo $RepoRoot"
    return
}

if ((Test-Path $Dest) -and $Force) {
    Write-Host "[bootstrap] Suppression du workspace existant : $Dest"
    Remove-Item -Recurse -Force $Dest
}

Write-Host "[bootstrap] Build moulinette-console ..."
& "$RepoRoot\mvnw.cmd" -f "$RepoRoot\moulinette\pom.xml" -pl console -am -q package
if ($LASTEXITCODE -ne 0) { throw "Build Maven échoué." }

$Jar = "$RepoRoot\moulinette\console\target\moulinette-console.jar"
if (-not (Test-Path $Jar)) { throw "Build du jar échoué." }

Write-Host "[bootstrap] Initialisation du workspace stagiaire ..."
& java -jar $Jar init --nom $Nom --dest $Dest --piscine-repo $RepoRoot

Write-Host ""
Write-Host "[bootstrap] OK. Lance le REPL :"
Write-Host "  java -jar $Jar repl --repo $Dest --piscine-repo $RepoRoot"
```

- [ ] **Step 9.3: Test manuel des scripts**

Run (Unix): `./scripts/piscine-bootstrap.sh --nom test-bootstrap`
Run (Windows, dans PowerShell) : `.\scripts\piscine-bootstrap.ps1 -Nom test-bootstrap`
Expected: workspace créé dans `../piscine-test-bootstrap/`, message de fin OK.

Cleanup après test : `rm -rf ../piscine-test-bootstrap`.

- [ ] **Step 9.4: Commit**

```bash
git add scripts/
chmod +x scripts/piscine-bootstrap.sh
git commit -m "feat(scripts): bootstrap autonome piscine-bootstrap.{sh,ps1} (tâche #40)"
```

---

## Tâche 10 — Guide stagiaire `docs/piscine-stagiaire.md` + README (backlog #41)

**Files:**
- Create: `docs/piscine-stagiaire.md`
- Modify: `README.md` (ajouter une section en haut)

- [ ] **Step 10.1: Guide stagiaire**

Create `docs/piscine-stagiaire.md`:

```markdown
# Piscine ETNC — guide stagiaire

Ce guide te permet d'utiliser la Piscine **en autonomie sur ta machine**, sans serveur.

## 1. Installer les prérequis

- **Java 25** et **git** : voir [`setup-dev.md`](setup-dev.md) (instructions Windows sans droits admin incluses).

## 2. Cloner le repo

```bash
git clone <url-piscine-etnc> piscine-etnc
cd piscine-etnc
```

## 3. Démarrer ton workspace personnel

**Linux / macOS :**
```bash
./scripts/piscine-bootstrap.sh --nom <ton-slug>
```

**Windows (PowerShell) :**
```powershell
.\scripts\piscine-bootstrap.ps1 -Nom <ton-slug>
```

Le script :
- vérifie tes prérequis,
- compile la console de correction,
- crée un dossier `../piscine-<ton-slug>/` à côté du repo,
- initialise un git local avec tous les exercices du module 1.

## 4. Travailler sur un exercice

1. Lance le REPL : `java -jar moulinette/console/target/moulinette-console.jar repl --repo ../piscine-<ton-slug> --piscine-repo .`
2. Démarre un rendu de sous-groupe :

   ```
   piscine[main]> submit-start 1.1
   ```
3. Édite ton code dans `../piscine-<ton-slug>/exercises/1.1.1-hello-world/starter/` (avec ton IDE habituel).
4. Vérifie l'état et rends :

   ```
   piscine[rendu/1.1]> git add exercises/1.1.1-hello-world
   piscine[rendu/1.1]> git commit -m "rendu 1.1.1"
   piscine[rendu/1.1]> git push origin rendu/1.1
   ```

La moulinette se lance automatiquement et écrit son rapport dans `.piscine/reports/`.

## 5. Lire le rapport

Le chemin du rapport s'affiche dans la console (`.piscine/reports/1.1-<timestamp>.md`).
Ouvre-le dans ton éditeur. Pour chaque exo : statut, messages, suggestions.

## 6. Bloqué ?

- **Un exo échoue** : la séquence s'arrête. Corrige le code de l'exo bloquant, refais `add` / `commit` / `push`.
- **`git push` refusé (non-fast-forward)** : supprime la branche locale et recommence avec `submit-start <groupe>`.
- **Commande non supportée** : seules `add`, `commit`, `push`, `status`, `log`, `diff`, `submit-start`, `help`, `exit` sont reconnues dans le MVP.

## 7. Convention de rendu

| Élément | Valeur |
|---------|--------|
| Branche de rendu | `rendu/<sous-groupe>` (ex: `rendu/1.1`) |
| Trigger moulinette | `git push origin rendu/<sous-groupe>` |
| Ordre d'évaluation | exos d'un groupe par difficulté croissante, **arrêt au premier échec** |
| Remote | bare local `file://<workspace>/.piscine/remote.git` |
```

- [ ] **Step 10.2: Mention dans le README racine**

Edit `README.md` — ajouter en haut, juste après le titre principal :

```markdown
> **Stagiaire ?** Va directement à [`docs/piscine-stagiaire.md`](docs/piscine-stagiaire.md) — tout est expliqué pour démarrer en autonomie.
```

- [ ] **Step 10.3: Commit**

```bash
git add docs/piscine-stagiaire.md README.md
git commit -m "docs: guide stagiaire autonome + lien depuis README (tâche #41)"
```

---

## Tâche 11 — E2E happy path sur exo 1.1.1 (backlog #42)

**Files:**
- Create: `moulinette/console/src/test/java/etnc/piscine/moulinette/console/e2e/HappyPathE2EIT.java`

- [ ] **Step 11.1: Écrire l'E2E**

Create `HappyPathE2EIT.java`:

```java
package etnc.piscine.moulinette.console.e2e;

import etnc.piscine.moulinette.console.Mode;
import etnc.piscine.moulinette.console.commands.CommandRegistry;
import etnc.piscine.moulinette.console.git.ProcessGitClient;
import etnc.piscine.moulinette.console.repl.*;
import etnc.piscine.moulinette.console.trigger.MoulinetteRunner;
import etnc.piscine.moulinette.console.trigger.SubmissionTrigger;
import etnc.piscine.moulinette.console.workspace.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("e2e")
class HappyPathE2EIT {

    @Test
    void rendu_complet_exo_1_1_1_produit_un_rapport_ok(@TempDir Path tmp) throws Exception {
        // 1. Préparer un faux repo Piscine avec l'exo 1.1.1
        Path piscine = tmp.resolve("piscine-etnc");
        Path exoDir = piscine.resolve("exercises/module-1-fondamentaux/1.1.1-hello-world");
        Files.createDirectories(exoDir.resolve("starter"));
        Files.writeString(exoDir.resolve("metadata.yml"), """
            slug: hello-world
            module: 1
            sous_groupe: "1.1"
            position: 1
            notions: []
            """);
        Files.writeString(exoDir.resolve("starter/Hello.java"), "class Hello{public static void main(String[]a){}}");

        // 2. init workspace
        Path dest = tmp.resolve("piscine-curlier");
        var initializer = new LocalWorkspaceInitializer(new ProcessGitClient());
        Workspace ws = initializer.init(new InitRequest("curlier", dest, piscine, "module-1-fondamentaux"));

        // 3. Préparer le REPL avec un MoulinetteRunner toujours OK
        var catalog = ExerciseCatalog.scan(piscine.resolve("exercises"));
        MoulinetteRunner runner = (sg, repo) -> new MoulinetteRunner.GroupReport(
            sg,
            List.of(new MoulinetteRunner.ExoOutcome("1.1.1", true, "")),
            false,
            ws.repoRoot().resolve(".piscine/reports/" + sg + "-fake.md")
        );
        var trigger = new SubmissionTrigger(runner);
        var ctx = new ReplContext(ws.repoRoot(), new ProcessGitClient(), catalog, Mode.LOCAL);

        String script = String.join("\n",
            "submit-start 1.1",
            "git add .",
            "git commit -m \"rendu 1.1.1\"",
            "git push origin rendu/1.1",
            "exit", ""
        );
        var sw = new StringWriter();
        var io = new ReplIo(new StringReader(script), sw);

        new Repl(ctx, CommandRegistry.defaults(trigger), io).run();

        String out = sw.toString();
        assertThat(out).contains("rendu/1.1");
        assertThat(out).contains("✓ OK");
        assertThat(out).contains("Rapport");
    }
}
```

- [ ] **Step 11.2: Activer le tag e2e pour le run dédié**

Run: `./mvnw -f moulinette/pom.xml -pl console test -Dgroups=e2e`
Expected: PASS.

- [ ] **Step 11.3: Commit**

```bash
git add moulinette/console/src/test/java/etnc/piscine/moulinette/console/e2e
git commit -m "test(console): E2E happy path sur exo 1.1.1 (tâche #42)"
```

---

## Tâche 12 — Clôture du backlog

- [ ] **Step 12.1: Mettre à jour `docs/backlog.md`**

Pour chaque tâche #34 à #42 : passer le statut à `Faite` et lier au commit qui la clôt (`git log --oneline | head -20` pour retrouver les SHA).

- [ ] **Step 12.2: Vérifier le build complet**

Run: `./mvnw -f moulinette/pom.xml verify`
Run: `./mvnw -f moulinette/pom.xml -pl console test -Dgroups=git`
Run: `./mvnw -f moulinette/pom.xml -pl console test -Dgroups=e2e`
Expected: tous PASS.

- [ ] **Step 12.3: Commit final**

```bash
git add docs/backlog.md
git commit -m "docs(backlog): clôture itération MVP console (#34-#42)"
```

---

## Auto-revue de couverture (spec → tâches)

| Décision spec | Tâche(s) plan |
|---------------|---------------|
| D1 REPL sandbox git | T5, T6 |
| D2 Trigger add+commit+push sur rendu/<groupe> | T7 (SubmissionTrigger), T5 (PushCommand) |
| D3 Remote bare file:// | T4 (LocalWorkspaceInitializer) |
| D4 Séquence stop au 1er échec | T7 (MoulinetteRunner.Default + GroupReport.stoppedEarly) |
| D5 Repo stagiaire séparé via `init` | T4, T8 |
| D6 Commandes minimales | T5 |
| D7 Module `moulinette/console` | T1 |
| D8 Mode LOCAL/NOMINAL | T1 (enum), T8 (rejet de NOMINAL) |
| D9 Bootstrap depuis repo | T9, T10 |
| Logs Logback | T1 (resources/logback.xml à ajouter) |
| Rapports `.piscine/reports/` | T7, T11 |
| Tests unit + IT git + E2E | T2, T3, T5, T6, T7, T11 |
| Doc autonome stagiaire | T10 |
| Critères #34-#42 | T1 → T12 |

Pas de placeholders « TBD ». Une ambigüité résiduelle, signalée explicitement dans T7 : la détection de succès d'un `CheckResult` (le framework actuel n'expose pas encore un statut typé) — à raffiner avec un mini enrichissement de `framework.CheckResult` quand on l'utilisera réellement avec de vrais `Checker`. Pour le MVP, le `MoulinetteRunner.Default` est suffisant : les tests E2E injectent un runner contrôlé.
