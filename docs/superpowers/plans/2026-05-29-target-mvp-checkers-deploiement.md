# Target MVP — Checkers réels + déploiement stagiaire — Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Rendre la moulinette réellement évaluante (compile + tests publics + tests privés + style Checkstyle) et livrable à un stagiaire via un bundle ZIP portable auto-suffisant, avec une doc de déploiement (instructeur) et une doc d'architecture (devs).

**Architecture:** On enrichit `moulinette/console` d'un package `console.checkers` dont les `Checker` invoquent `javac` / le ConsoleLauncher JUnit / Checkstyle en sous-processus via `runner.ProcessRunner`. L'astuce centrale : l'uber-jar shadé contient déjà JUnit/AssertJ/Checkstyle (scope `runtime`), donc **le jar lui-même sert de classpath** pour compiler et exécuter le code stagiaire — zéro fichier annexe, zéro Maven, zéro internet à l'exécution. Chaque `Checker` est autonome (il recompile ce dont il a besoin dans `<workspace>/.piscine/build/<exoId>/`). Un script `build-bundle` assemble JDK portable + uber-jar + exos en un ZIP lançable.

**Tech Stack:** Java 25, Maven, JUnit 5.11.4 (`junit-jupiter` + `junit-platform-console` 1.11.4), AssertJ 3.26.3, Checkstyle 10.21.0, SLF4J/Logback, SnakeYAML 2.3.

**Spec source:** [`docs/superpowers/specs/2026-05-29-target-mvp-checkers-deploiement-design.md`](../specs/2026-05-29-target-mvp-checkers-deploiement-design.md)

**Décisions verrouillées (résolvant les points ouverts de la spec) :**
- **Chaque Checker est autonome** : il recompile le `main` stagiaire s'il en a besoin (pas d'état partagé entre Checkers). Coût négligeable (exos minuscules), contrat `Checker` propre.
- **Dépendances outillage en scope `runtime`** dans `console/pom.xml` : shadées dans l'uber-jar, absentes du classpath de compilation du code principal, présentes au classpath de test.

---

## Démarrage en session vierge (lire en premier)

Cette itération s'exécute **inline** dans une nouvelle session sans contexte. Pour démarrer :

1. **Invoquer la skill** `superpowers:executing-plans` et lui donner ce fichier.
2. **Brancher** : travailler sur `feature/mvp-console-correction` (la branche de l'itération précédente, déjà créée). Ne pas committer sur `main`.
3. **Suivre les tâches dans l'ordre** (Task 1 → 10), en TDD, un commit par tâche. Les commandes et le code sont fournis intégralement.

### Environnement de build (gotchas — vérifiés en session précédente)
- **JDK 25** : `E:\java\jdk-25.0.3+9` (= `JAVA_HOME`). Le `java` du PATH est Java 8 → pour lancer un jar, utiliser `"$JAVA_HOME/bin/java"`.
- **Maven** : utiliser le `mvn` **système** (`E:\java\apache-maven-3.9.9`, sur le PATH). **`./mvnw` échoue** (ne peut pas télécharger Maven : réseau restreint).
- **Sandbox** : tous les `mvn` doivent tourner avec `dangerouslyDisableSandbox: true` (sinon la résolution de dépendances time out). Cette itération télécharge de nouvelles deps (junit-platform-console, checkstyle) → premier build plus long.
- **`-Dtest=X`** s'applique à tout le reactor → toujours ajouter `-Dsurefire.failIfNoSpecifiedTests=false`.
- **Tags** : `surefire.excludedGroups` par défaut deviendra `git,e2e,tools`. Lancer une suite ciblée : `-Dsurefire.excludedGroups=<autres> -Dgroups=<tag>`.
- **CRLF** : warnings "LF will be replaced by CRLF" à chaque `git add` = normaux.
- **Trailer de commit** : `Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>`.

### État au démarrage (acquis de l'itération MVP précédente)
- `moulinette/console` existe : REPL git (`init`/`repl`), commandes `add/commit/push/status/log/diff/submit-start`, `SubmissionTrigger`, `MoulinetteRunner.Default` câblé **mais avec une liste de Checkers vide** (tout passe OK). C'est ce que cette itération corrige.
- Uber-jar via maven-shade déjà en place (`moulinette-console.jar`).
- Tests verts : suites unit + `@Tag("git")` + `@Tag("e2e")`.

### Définition de terminé
Les 4 suites passent (`verify` + `git` + `tools` + `e2e`), le bundle ZIP se construit et se lance hors du repo, et les deux docs existent. Détail en §9 de la spec.

---

## File Structure

### Modifié — `moulinette/framework`
```
framework/src/main/java/etnc/piscine/moulinette/framework/CheckerContext.java   (ajoute exerciseRefPath)
framework/src/test/java/etnc/piscine/moulinette/framework/FrameworkSmokeTest.java (si construit un CheckerContext)
```

### Enrichi — `moulinette/console`
```
console/pom.xml                                          (deps runtime : junit-jupiter, junit-platform-console, assertj, checkstyle ; tag surefire 'tools')
console/src/main/resources/checkstyle-etnc.xml           (NOUVEAU — config Checkstyle légère)
console/src/main/java/etnc/piscine/moulinette/console/checkers/
    FqcnExtractor.java        (NOUVEAU — .java → FQCN)
    JavaToolkit.java          (NOUVEAU — façade javac/junit/checkstyle sur ProcessRunner)
    CompileChecker.java       (NOUVEAU)
    PublicTestChecker.java    (NOUVEAU)
    PrivateTestChecker.java   (NOUVEAU)
    StyleChecker.java         (NOUVEAU)
console/src/main/java/etnc/piscine/moulinette/console/trigger/MoulinetteRunner.java  (rapport via ReportGenerator + exerciseRefPath + build dir)
console/src/main/java/etnc/piscine/moulinette/console/Main.java                       (câble les vrais Checkers)
console/src/test/java/etnc/piscine/moulinette/console/checkers/
    FqcnExtractorTest.java        (NOUVEAU, unit)
    JavaToolkitTest.java          (NOUVEAU, unit — toolingClasspath)
    CompileCheckerIT.java         (NOUVEAU, @Tag("tools"))
    TestCheckersIT.java           (NOUVEAU, @Tag("tools"))
    StyleCheckerIT.java           (NOUVEAU, @Tag("tools"))
console/src/test/java/etnc/piscine/moulinette/console/e2e/HappyPathE2EIT.java        (MAJ — vrais Checkers)
```

### Nouveaux — scripts & docs
```
scripts/build-bundle.ps1
scripts/build-bundle.sh
docs/deploiement-instructeur.md
docs/architecture-moulinette.md
```

---

## Task 1 (#43) : Enrichir `CheckerContext`

**Files:**
- Modify: `moulinette/framework/src/main/java/etnc/piscine/moulinette/framework/CheckerContext.java`
- Test: `moulinette/framework/src/test/java/etnc/piscine/moulinette/framework/CheckerContextTest.java` (créer)

- [ ] **Step 1.1 : Écrire le test du nouveau contrat**

Create `moulinette/framework/src/test/java/etnc/piscine/moulinette/framework/CheckerContextTest.java` :

```java
package etnc.piscine.moulinette.framework;

import org.junit.jupiter.api.Test;
import java.nio.file.Path;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CheckerContextTest {

    @Test
    void expose_les_trois_chemins() {
        var ctx = new CheckerContext("1.1.1", Path.of("/ws/exo"), Path.of("/piscine/exo"));
        assertThat(ctx.exerciseId()).isEqualTo("1.1.1");
        assertThat(ctx.renduPath()).isEqualTo(Path.of("/ws/exo"));
        assertThat(ctx.exerciseRefPath()).isEqualTo(Path.of("/piscine/exo"));
    }

    @Test
    void refuse_exerciseId_vide() {
        assertThatThrownBy(() -> new CheckerContext(" ", Path.of("/a"), Path.of("/b")))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void refuse_les_nulls() {
        assertThatThrownBy(() -> new CheckerContext("1.1.1", null, Path.of("/b")))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new CheckerContext("1.1.1", Path.of("/a"), null))
            .isInstanceOf(NullPointerException.class);
    }
}
```

- [ ] **Step 1.2 : Lancer le test → échec de compilation**

Run: `mvn -f moulinette/pom.xml -pl framework test -Dtest=CheckerContextTest -Dsurefire.failIfNoSpecifiedTests=false`
Expected: COMPILATION FAILURE (`exerciseRefPath()` n'existe pas).

- [ ] **Step 1.3 : Modifier `CheckerContext`**

Remplacer le contenu de `CheckerContext.java` par :

```java
package etnc.piscine.moulinette.framework;

import java.nio.file.Path;
import java.util.Objects;

/**
 * Contexte fourni à chaque {@link Checker} lors de l'évaluation d'un rendu.
 *
 * @param exerciseId      identifiant canonique de l'exercice (ex. {@code "1.1.1"})
 * @param renduPath       répertoire du rendu stagiaire (contient {@code starter/})
 * @param exerciseRefPath répertoire de référence de l'exercice dans le repo Piscine
 *                        (contient {@code tests/}, {@code tests-prives/}, {@code solution/}, {@code evaluation.yml})
 */
public record CheckerContext(
        String exerciseId,
        Path renduPath,
        Path exerciseRefPath
) {
    public CheckerContext {
        Objects.requireNonNull(exerciseId, "exerciseId ne peut pas être null");
        Objects.requireNonNull(renduPath,  "renduPath ne peut pas être null");
        Objects.requireNonNull(exerciseRefPath, "exerciseRefPath ne peut pas être null");
        if (exerciseId.isBlank()) {
            throw new IllegalArgumentException("exerciseId ne peut pas être vide");
        }
    }
}
```

- [ ] **Step 1.4 : Corriger les appelants existants**

Chercher les constructions de `CheckerContext` : `grep -rn "new CheckerContext" moulinette`. Au minimum `FrameworkSmokeTest` et `MoulinetteRunner.Default` (ce dernier sera retouché en Task 6 ; ici il faut juste qu'il compile). Si `MoulinetteRunner.Default` construit `new CheckerContext(e.id(), renduDir)`, le passer à `new CheckerContext(e.id(), renduDir, e.exerciseDir())`. Pour tout test qui construit un contexte à 2 args, ajouter un 3e argument `Path.of("/tmp")` ou un chemin pertinent.

- [ ] **Step 1.5 : Vérifier framework + reactor compile**

Run: `mvn -f moulinette/pom.xml -pl framework test -Dtest=CheckerContextTest -Dsurefire.failIfNoSpecifiedTests=false`
Expected: PASS (3 tests).
Run: `mvn -f moulinette/pom.xml -q -DskipTests compile`
Expected: BUILD SUCCESS (tous modules compilent).

- [ ] **Step 1.6 : Commit**

```bash
git add moulinette/framework moulinette/console/src/main/java/etnc/piscine/moulinette/console/trigger/MoulinetteRunner.java
git commit -m "feat(framework): CheckerContext porte exerciseRefPath (tâche #43)"
```

---

## Task 2 (#44) : Dépendances outillage + `FqcnExtractor` + `JavaToolkit`

**Files:**
- Modify: `moulinette/console/pom.xml`
- Create: `console/.../checkers/FqcnExtractor.java`
- Create: `console/.../checkers/JavaToolkit.java`
- Test: `console/.../checkers/FqcnExtractorTest.java`, `JavaToolkitTest.java`

- [ ] **Step 2.1 : Ajouter les dépendances outillage (scope runtime) au `console/pom.xml`**

Dans `<dependencies>`, après `snakeyaml`, ajouter :

```xml
        <!-- Outillage d'évaluation : shadé dans l'uber-jar (scope runtime),
             invoqué en sous-processus par les Checkers. Disponible aussi au classpath de test. -->
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.platform</groupId>
            <artifactId>junit-platform-console</artifactId>
            <version>1.11.4</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.assertj</groupId>
            <artifactId>assertj-core</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>com.puppycrawl.tools</groupId>
            <artifactId>checkstyle</artifactId>
            <version>10.21.0</version>
            <scope>runtime</scope>
        </dependency>
```

Et **supprimer** les deux déclarations existantes en `<scope>test</scope>` de `junit-jupiter` et `assertj-core` dans ce pom (les versions runtime ci-dessus les remplacent — runtime est visible au classpath de test). Garder logback en runtime.

> Note : `junit-jupiter` / `assertj` en scope `runtime` restent visibles pour compiler et exécuter les tests du module (le classpath de test inclut les dépendances runtime).

- [ ] **Step 2.2 : Ajouter le tag `tools` aux exclusions surefire**

Dans le `<properties>` du `console/pom.xml`, remplacer :

```xml
        <surefire.excludedGroups>git,e2e</surefire.excludedGroups>
```
par :
```xml
        <surefire.excludedGroups>git,e2e,tools</surefire.excludedGroups>
```

- [ ] **Step 2.3 : Vérifier que le module compile/teste encore**

Run: `mvn -f moulinette/pom.xml -pl console -am test`
Expected: BUILD SUCCESS, les tests unitaires existants passent (junit/assertj toujours résolus via scope runtime).

- [ ] **Step 2.4 : Test de `FqcnExtractor`**

Create `console/src/test/java/etnc/piscine/moulinette/console/checkers/FqcnExtractorTest.java` :

```java
package etnc.piscine.moulinette.console.checkers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.*;

import static org.assertj.core.api.Assertions.assertThat;

class FqcnExtractorTest {

    @Test
    void extrait_fqcn_avec_package(@TempDir Path dir) throws IOException {
        Path f = dir.resolve("HelloWorldTest.java");
        Files.writeString(f, "package etnc.m1;\n\nclass HelloWorldTest {}\n");
        assertThat(FqcnExtractor.fqcn(f)).isEqualTo("etnc.m1.HelloWorldTest");
    }

    @Test
    void extrait_fqcn_sans_package(@TempDir Path dir) throws IOException {
        Path f = dir.resolve("Foo.java");
        Files.writeString(f, "class Foo {}\n");
        assertThat(FqcnExtractor.fqcn(f)).isEqualTo("Foo");
    }

    @Test
    void package_apres_commentaires_et_lignes_vides(@TempDir Path dir) throws IOException {
        Path f = dir.resolve("Bar.java");
        Files.writeString(f, "// commentaire\n\npackage a.b.c;\nclass Bar {}\n");
        assertThat(FqcnExtractor.fqcn(f)).isEqualTo("a.b.c.Bar");
    }

    @Test
    void liste_les_fqcn_dun_repertoire(@TempDir Path dir) throws IOException {
        Path pkg = dir.resolve("src/etnc/m1");
        Files.createDirectories(pkg);
        Files.writeString(pkg.resolve("ATest.java"), "package etnc.m1;\nclass ATest {}\n");
        Files.writeString(pkg.resolve("BTest.java"), "package etnc.m1;\nclass BTest {}\n");
        assertThat(FqcnExtractor.fqcnsUnder(dir.resolve("src")))
            .containsExactlyInAnyOrder("etnc.m1.ATest", "etnc.m1.BTest");
    }
}
```

- [ ] **Step 2.5 : Lancer → échec compilation**

Run: `mvn -f moulinette/pom.xml -pl console -am test -Dtest=FqcnExtractorTest -Dsurefire.failIfNoSpecifiedTests=false`
Expected: COMPILATION FAILURE.

- [ ] **Step 2.6 : Implémenter `FqcnExtractor`**

Create `console/src/main/java/etnc/piscine/moulinette/console/checkers/FqcnExtractor.java` :

```java
package etnc.piscine.moulinette.console.checkers;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/** Reconstruit le nom pleinement qualifié d'une classe Java à partir de son fichier source. */
public final class FqcnExtractor {

    private static final Pattern PACKAGE = Pattern.compile("^\\s*package\\s+([\\w.]+)\\s*;");

    private FqcnExtractor() {}

    public static String fqcn(Path javaFile) {
        String fileName = javaFile.getFileName().toString();
        String className = fileName.endsWith(".java")
            ? fileName.substring(0, fileName.length() - ".java".length())
            : fileName;
        try {
            for (String line : Files.readAllLines(javaFile)) {
                Matcher m = PACKAGE.matcher(line);
                if (m.find()) return m.group(1) + "." + className;
            }
            return className;
        } catch (IOException e) {
            throw new UncheckedIOException("lecture FQCN échouée : " + javaFile, e);
        }
    }

    /** Tous les FQCN des fichiers .java sous un répertoire (récursif). Vide si le répertoire n'existe pas. */
    public static List<String> fqcnsUnder(Path root) {
        if (!Files.isDirectory(root)) return List.of();
        try (Stream<Path> walk = Files.walk(root)) {
            return walk.filter(p -> p.getFileName().toString().endsWith(".java"))
                       .map(FqcnExtractor::fqcn)
                       .sorted()
                       .toList();
        } catch (IOException e) {
            throw new UncheckedIOException("scan FQCN échoué : " + root, e);
        }
    }
}
```

- [ ] **Step 2.7 : Vérifier `FqcnExtractorTest` vert**

Run: `mvn -f moulinette/pom.xml -pl console -am test -Dtest=FqcnExtractorTest -Dsurefire.failIfNoSpecifiedTests=false`
Expected: PASS (4 tests).

- [ ] **Step 2.8 : Test de `JavaToolkit.toolingClasspath()`**

Create `console/src/test/java/etnc/piscine/moulinette/console/checkers/JavaToolkitTest.java` :

```java
package etnc.piscine.moulinette.console.checkers;

import etnc.piscine.moulinette.console.git.FakeProcessRunner;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JavaToolkitTest {

    @Test
    void tooling_classpath_non_vide() {
        var tk = new JavaToolkit(new etnc.piscine.moulinette.runner.ProcessRunner());
        // En dev (hors jar) : retombe sur java.class.path, jamais vide.
        assertThat(tk.toolingClasspath()).isNotBlank();
    }

    @Test
    void java_exe_pointe_vers_un_binaire_java() {
        var tk = new JavaToolkit(new etnc.piscine.moulinette.runner.ProcessRunner());
        assertThat(tk.javaExe()).contains("java");
    }
}
```

(Ce test n'utilise pas `FakeProcessRunner` finalement ; supprimer l'import s'il n'est pas créé. Garder le test minimal ci-dessus.)

Corriger l'import inutile : retirer la ligne `import etnc.piscine.moulinette.console.git.FakeProcessRunner;`.

- [ ] **Step 2.9 : Lancer → échec compilation**

Run: `mvn -f moulinette/pom.xml -pl console -am test -Dtest=JavaToolkitTest -Dsurefire.failIfNoSpecifiedTests=false`
Expected: COMPILATION FAILURE (`JavaToolkit` absent).

- [ ] **Step 2.10 : Implémenter `JavaToolkit`**

Create `console/src/main/java/etnc/piscine/moulinette/console/checkers/JavaToolkit.java` :

```java
package etnc.piscine.moulinette.console.checkers;

import etnc.piscine.moulinette.runner.ProcessResult;
import etnc.piscine.moulinette.runner.ProcessRunner;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/** Façade d'outillage Java (javac / JUnit ConsoleLauncher / Checkstyle) au-dessus de {@link ProcessRunner}. */
public final class JavaToolkit {

    private final ProcessRunner runner;

    public JavaToolkit(ProcessRunner runner) { this.runner = runner; }

    /** Chemin de l'exécutable {@code java} de la JVM courante. */
    public String javaExe() {
        return Path.of(System.getProperty("java.home"), "bin", "java").toString();
    }

    /** Chemin de l'exécutable {@code javac} de la JVM courante. */
    public String javacExe() {
        return Path.of(System.getProperty("java.home"), "bin", "javac").toString();
    }

    /**
     * Classpath d'outillage : l'uber-jar courant s'il s'agit d'un .jar (JUnit/AssertJ/Checkstyle shadés),
     * sinon {@code java.class.path} (dev / surefire).
     */
    public String toolingClasspath() {
        try {
            var loc = JavaToolkit.class.getProtectionDomain().getCodeSource().getLocation();
            Path p = Path.of(loc.toURI());
            if (p.toString().endsWith(".jar")) return p.toString();
        } catch (Exception ignore) {
            // retombe sur java.class.path
        }
        return System.getProperty("java.class.path");
    }

    /** {@code javac -d outDir -cp classpath <*.java sous srcDirs>}. */
    public ProcessResult compile(List<Path> srcDirs, Path outDir, String classpath) throws IOException {
        Files.createDirectories(outDir);
        List<String> files = javaFiles(srcDirs);
        if (files.isEmpty()) {
            return new ProcessResult(2, "", "aucun fichier .java à compiler dans " + srcDirs);
        }
        List<String> cmd = new ArrayList<>();
        cmd.add(javacExe());
        cmd.add("-d"); cmd.add(outDir.toString());
        if (classpath != null && !classpath.isBlank()) { cmd.add("-cp"); cmd.add(classpath); }
        cmd.addAll(files);
        return runner.run(outDir, cmd);
    }

    /**
     * Lance JUnit via le ConsoleLauncher.
     * {@code java -cp classpath org.junit.platform.console.ConsoleLauncher execute
     *        --disable-banner --details=summary --fail-if-no-tests (--select-class=<fqcn>)+}
     */
    public ProcessResult runJUnit(Path workDir, String classpath, List<String> selectClasses) throws IOException {
        List<String> cmd = new ArrayList<>();
        cmd.add(javaExe());
        cmd.add("-cp"); cmd.add(classpath);
        cmd.add("org.junit.platform.console.ConsoleLauncher");
        cmd.add("execute");
        cmd.add("--disable-banner");
        cmd.add("--details=summary");
        cmd.add("--fail-if-no-tests");
        for (String fqcn : selectClasses) cmd.add("--select-class=" + fqcn);
        return runner.run(workDir, cmd);
    }

    /** {@code java -cp <toolingClasspath> com.puppycrawl.tools.checkstyle.Main -c config <fichiers>}. */
    public ProcessResult checkstyle(Path workDir, Path config, List<Path> srcDirs) throws IOException {
        List<String> files = javaFiles(srcDirs);
        List<String> cmd = new ArrayList<>();
        cmd.add(javaExe());
        cmd.add("-cp"); cmd.add(toolingClasspath());
        cmd.add("com.puppycrawl.tools.checkstyle.Main");
        cmd.add("-c"); cmd.add(config.toString());
        cmd.addAll(files);
        return runner.run(workDir, cmd);
    }

    static List<String> javaFiles(List<Path> srcDirs) {
        List<String> out = new ArrayList<>();
        for (Path dir : srcDirs) {
            if (!Files.isDirectory(dir)) continue;
            try (Stream<Path> walk = Files.walk(dir)) {
                walk.filter(p -> p.getFileName().toString().endsWith(".java"))
                    .map(Path::toString).sorted().forEach(out::add);
            } catch (IOException e) {
                throw new UncheckedIOException("scan .java échoué : " + dir, e);
            }
        }
        return out;
    }
}
```

- [ ] **Step 2.11 : Vérifier `JavaToolkitTest` vert**

Run: `mvn -f moulinette/pom.xml -pl console -am test -Dtest=JavaToolkitTest -Dsurefire.failIfNoSpecifiedTests=false`
Expected: PASS (2 tests).

- [ ] **Step 2.12 : Commit**

```bash
git add moulinette/console/pom.xml moulinette/console/src/main/java/etnc/piscine/moulinette/console/checkers moulinette/console/src/test/java/etnc/piscine/moulinette/console/checkers
git commit -m "feat(console): JavaToolkit + FqcnExtractor + deps outillage runtime (tâche #44)"
```

---

## Task 3 (#45) : `CompileChecker`

**Files:**
- Create: `console/.../checkers/CompileChecker.java`
- Test: `console/.../checkers/CompileCheckerIT.java`

- [ ] **Step 3.1 : Test d'intégration `@Tag("tools")`**

Create `console/src/test/java/etnc/piscine/moulinette/console/checkers/CompileCheckerIT.java` :

```java
package etnc.piscine.moulinette.console.checkers;

import etnc.piscine.moulinette.framework.CheckResult;
import etnc.piscine.moulinette.framework.CheckerContext;
import etnc.piscine.moulinette.runner.ProcessRunner;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.*;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("tools")
class CompileCheckerIT {

    private final CompileChecker checker = new CompileChecker(new JavaToolkit(new ProcessRunner()));

    @Test
    void code_correct_compile(@TempDir Path tmp) throws IOException {
        Path rendu = ecrireMain(tmp, "public class HelloWorld { public static void main(String[] a){ System.out.println(\"hi\"); } }");
        CheckResult r = checker.check(new CheckerContext("1.1.1", rendu, tmp.resolve("ref")));
        assertThat(r.status()).isEqualTo(CheckResult.Status.OK);
    }

    @Test
    void code_avec_erreur_de_syntaxe_echoue(@TempDir Path tmp) throws IOException {
        Path rendu = ecrireMain(tmp, "public class HelloWorld { oops }");
        CheckResult r = checker.check(new CheckerContext("1.1.1", rendu, tmp.resolve("ref")));
        assertThat(r.status()).isEqualTo(CheckResult.Status.FAIL);
        assertThat(r.messages()).isNotEmpty();
    }

    private static Path ecrireMain(Path tmp, String code) throws IOException {
        Path rendu = tmp.resolve("exo");
        Path src = rendu.resolve("starter/src/main/java");
        Files.createDirectories(src);
        Files.writeString(src.resolve("HelloWorld.java"), code);
        return rendu;
    }
}
```

- [ ] **Step 3.2 : Lancer → échec compilation**

Run: `mvn -f moulinette/pom.xml -pl console -am test -Dsurefire.excludedGroups=git,e2e -Dgroups=tools -Dtest=CompileCheckerIT -Dsurefire.failIfNoSpecifiedTests=false`
Expected: COMPILATION FAILURE (`CompileChecker` absent).

- [ ] **Step 3.3 : Implémenter `CompileChecker`**

Create `console/src/main/java/etnc/piscine/moulinette/console/checkers/CompileChecker.java` :

```java
package etnc.piscine.moulinette.console.checkers;

import etnc.piscine.moulinette.framework.CheckResult;
import etnc.piscine.moulinette.framework.Checker;
import etnc.piscine.moulinette.framework.CheckerContext;
import etnc.piscine.moulinette.runner.ProcessResult;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/** Compile le code stagiaire (sans les tests). Premier gardien de la chaîne. */
public final class CompileChecker implements Checker {

    private final JavaToolkit toolkit;

    public CompileChecker(JavaToolkit toolkit) { this.toolkit = toolkit; }

    @Override public String id() { return "compile"; }

    @Override
    public CheckResult check(CheckerContext ctx) {
        Path mainSrc = ctx.renduPath().resolve("starter/src/main/java");
        Path out = ctx.renduPath().resolve(".piscine/build/classes-main");
        try {
            ProcessResult r = toolkit.compile(List.of(mainSrc), out, toolkit.toolingClasspath());
            if (r.exitCode() == 0) return CheckResult.ok();
            return CheckResult.fail(
                "Ton code ne compile pas :\n" + tronquer(r.stderr()),
                "Corrige les erreurs de compilation ci-dessus avant de continuer.");
        } catch (IOException e) {
            return CheckResult.error("Échec technique de la compilation : " + e.getMessage());
        }
    }

    static String tronquer(String s) {
        String[] lignes = s.split("\n");
        if (lignes.length <= 30) return s;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 30; i++) sb.append(lignes[i]).append('\n');
        sb.append("… (").append(lignes.length - 30).append(" lignes de plus)");
        return sb.toString();
    }
}
```

- [ ] **Step 3.4 : Vérifier `CompileCheckerIT` vert**

Run: `mvn -f moulinette/pom.xml -pl console -am test -Dsurefire.excludedGroups=git,e2e -Dgroups=tools -Dtest=CompileCheckerIT -Dsurefire.failIfNoSpecifiedTests=false`
Expected: PASS (2 tests).

- [ ] **Step 3.5 : Commit**

```bash
git add moulinette/console/src/main/java/etnc/piscine/moulinette/console/checkers/CompileChecker.java moulinette/console/src/test/java/etnc/piscine/moulinette/console/checkers/CompileCheckerIT.java
git commit -m "feat(console): CompileChecker (tâche #45)"
```

---

## Task 4 (#46) : `PublicTestChecker` + `PrivateTestChecker`

**Files:**
- Create: `console/.../checkers/AbstractTestChecker.java`, `PublicTestChecker.java`, `PrivateTestChecker.java`
- Test: `console/.../checkers/TestCheckersIT.java`

- [ ] **Step 4.1 : Test d'intégration `@Tag("tools")`**

Create `console/src/test/java/etnc/piscine/moulinette/console/checkers/TestCheckersIT.java` :

```java
package etnc.piscine.moulinette.console.checkers;

import etnc.piscine.moulinette.framework.CheckResult;
import etnc.piscine.moulinette.framework.CheckerContext;
import etnc.piscine.moulinette.runner.ProcessRunner;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.*;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("tools")
class TestCheckersIT {

    private final JavaToolkit tk = new JavaToolkit(new ProcessRunner());

    @Test
    void solution_de_reference_passe_les_tests_publics(@TempDir Path tmp) throws IOException {
        var ctx = scenario(tmp, SOLUTION_MAIN);
        CheckResult r = new PublicTestChecker(tk).check(ctx);
        assertThat(r.status()).isEqualTo(CheckResult.Status.OK);
    }

    @Test
    void main_vide_echoue_aux_tests_publics(@TempDir Path tmp) throws IOException {
        var ctx = scenario(tmp, MAIN_VIDE);
        CheckResult r = new PublicTestChecker(tk).check(ctx);
        assertThat(r.status()).isEqualTo(CheckResult.Status.FAIL);
        assertThat(r.messages()).isNotEmpty();
    }

    @Test
    void solution_de_reference_passe_les_tests_prives(@TempDir Path tmp) throws IOException {
        var ctx = scenario(tmp, SOLUTION_MAIN);
        CheckResult r = new PrivateTestChecker(tk).check(ctx);
        assertThat(r.status()).isEqualTo(CheckResult.Status.OK);
    }

    // ── Fixtures ────────────────────────────────────────────────────────────

    private static final String SOLUTION_MAIN =
        "package etnc.m1;\npublic class HelloWorld {\n public static void main(String[] a){\n  System.out.println(\"Hello, world!\");\n }\n}\n";
    private static final String MAIN_VIDE =
        "package etnc.m1;\npublic class HelloWorld {\n public static void main(String[] a){\n }\n}\n";

    /** Construit un rendu + un dossier de référence avec tests publics, privés et l'util CaptureSortie. */
    private static CheckerContext scenario(Path tmp, String mainCode) throws IOException {
        Path rendu = tmp.resolve("ws/exo");
        Path mainSrc = rendu.resolve("starter/src/main/java/etnc/m1");
        Files.createDirectories(mainSrc);
        Files.writeString(mainSrc.resolve("HelloWorld.java"), mainCode);

        Path ref = tmp.resolve("piscine/exo");

        Path pub = ref.resolve("tests/src/test/java/etnc/m1");
        Files.createDirectories(pub);
        Files.writeString(pub.resolve("HelloWorldTest.java"), """
            package etnc.m1;
            import etnc.util.CaptureSortie;
            import org.junit.jupiter.api.Test;
            import static org.assertj.core.api.Assertions.assertThat;
            class HelloWorldTest {
              @Test void affiche() {
                String s = CaptureSortie.capturer(() -> HelloWorld.main(new String[]{}));
                assertThat(s).isEqualTo("Hello, world!" + System.lineSeparator());
              }
            }
            """);

        Path util = ref.resolve("tests/src/test/java/etnc/util");
        Files.createDirectories(util);
        Files.writeString(util.resolve("CaptureSortie.java"), """
            package etnc.util;
            import java.io.ByteArrayOutputStream;
            import java.io.PrintStream;
            import java.nio.charset.StandardCharsets;
            public final class CaptureSortie {
              private CaptureSortie() {}
              public static String capturer(Runnable action) {
                PrintStream original = System.out;
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                try (PrintStream c = new PrintStream(buffer, true, StandardCharsets.UTF_8)) {
                  System.setOut(c); action.run(); System.out.flush();
                } finally { System.setOut(original); }
                return buffer.toString(StandardCharsets.UTF_8);
              }
            }
            """);

        Path priv = ref.resolve("tests-prives/src/test/java/etnc/m1");
        Files.createDirectories(priv);
        Files.writeString(priv.resolve("HelloWorldPriveTest.java"), """
            package etnc.m1;
            import etnc.util.CaptureSortie;
            import org.junit.jupiter.api.Test;
            import static org.assertj.core.api.Assertions.assertThat;
            class HelloWorldPriveTest {
              @Test void ignore_args() {
                String s = CaptureSortie.capturer(() -> HelloWorld.main(new String[]{"x"}));
                assertThat(s).isEqualTo("Hello, world!" + System.lineSeparator());
              }
            }
            """);

        return new CheckerContext("1.1.1", rendu, ref);
    }
}
```

- [ ] **Step 4.2 : Lancer → échec compilation**

Run: `mvn -f moulinette/pom.xml -pl console -am test -Dsurefire.excludedGroups=git,e2e -Dgroups=tools -Dtest=TestCheckersIT -Dsurefire.failIfNoSpecifiedTests=false`
Expected: COMPILATION FAILURE (`PublicTestChecker`/`PrivateTestChecker` absents).

- [ ] **Step 4.3 : Implémenter `AbstractTestChecker`**

Create `console/src/main/java/etnc/piscine/moulinette/console/checkers/AbstractTestChecker.java` :

```java
package etnc.piscine.moulinette.console.checkers;

import etnc.piscine.moulinette.framework.CheckResult;
import etnc.piscine.moulinette.framework.Checker;
import etnc.piscine.moulinette.framework.CheckerContext;
import etnc.piscine.moulinette.runner.ProcessResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Base des Checkers de test : compile le code stagiaire + les sources de test, puis exécute
 * via JUnit les classes du jeu de tests ciblé. Chaque sous-classe précise quel répertoire de
 * test sélectionner et lesquels compiler (pour les utilitaires partagés).
 */
abstract class AbstractTestChecker implements Checker {

    protected final JavaToolkit toolkit;

    protected AbstractTestChecker(JavaToolkit toolkit) { this.toolkit = toolkit; }

    /** Sous-dossier (relatif à exerciseRefPath) dont les classes seront SÉLECTIONNÉES à l'exécution. */
    protected abstract Path selectedTestDir(CheckerContext ctx);

    /** Sous-dossiers (relatifs à exerciseRefPath) à COMPILER (inclut les utilitaires partagés). */
    protected abstract List<Path> compiledTestDirs(CheckerContext ctx);

    /** Nom lisible du jeu de tests, pour les messages. */
    protected abstract String label();

    @Override
    public CheckResult check(CheckerContext ctx) {
        Path mainSrc = ctx.renduPath().resolve("starter/src/main/java");
        String tag = id();
        Path classesMain = ctx.renduPath().resolve(".piscine/build/" + tag + "/classes-main");
        Path classesTest = ctx.renduPath().resolve(".piscine/build/" + tag + "/classes-test");
        String tooling = toolkit.toolingClasspath();
        try {
            ProcessResult comMain = toolkit.compile(List.of(mainSrc), classesMain, tooling);
            if (comMain.exitCode() != 0) {
                return CheckResult.fail("Le code ne compile pas, impossible de lancer les " + label() + ".",
                    "Vérifie d'abord que ton code compile (checker `compile`).");
            }
            String cpCompileTests = tooling + File.pathSeparator + classesMain;
            ProcessResult comTest = toolkit.compile(compiledTestDirs(ctx), classesTest, cpCompileTests);
            if (comTest.exitCode() != 0) {
                return CheckResult.error("Compilation des " + label() + " impossible (problème côté référence) :\n"
                    + CompileChecker.tronquer(comTest.stderr()));
            }
            List<String> select = FqcnExtractor.fqcnsUnder(ctx.exerciseRefPath().resolve(selectedTestDir(ctx)));
            if (select.isEmpty()) {
                return CheckResult.error("Aucune classe de test trouvée pour les " + label() + ".");
            }
            String runCp = tooling + File.pathSeparator + classesMain + File.pathSeparator + classesTest;
            ProcessResult run = toolkit.runJUnit(classesTest, runCp, select);
            if (run.exitCode() == 0) return CheckResult.ok();
            return CheckResult.fail(
                "Des " + label() + " ont échoué :\n" + extraitEchecs(run.stdout()),
                "Relis le sujet et corrige ton code pour faire passer ces tests.");
        } catch (IOException e) {
            return CheckResult.error("Échec technique sur les " + label() + " : " + e.getMessage());
        }
    }

    /** Garde la section de synthèse du ConsoleLauncher (lignes 'tests ...' + 'Failures'). */
    static String extraitEchecs(String stdout) {
        List<String> garde = new ArrayList<>();
        for (String l : stdout.split("\n")) {
            String t = l.strip();
            if (t.contains("tests successful") || t.contains("tests failed")
                || t.startsWith("Failures") || t.startsWith("MethodSource")
                || t.contains("==> expected")) {
                garde.add(l);
            }
        }
        return garde.isEmpty() ? stdout : String.join("\n", garde);
    }
}
```

- [ ] **Step 4.4 : Implémenter `PublicTestChecker`**

Create `console/src/main/java/etnc/piscine/moulinette/console/checkers/PublicTestChecker.java` :

```java
package etnc.piscine.moulinette.console.checkers;

import etnc.piscine.moulinette.framework.CheckerContext;

import java.nio.file.Path;
import java.util.List;

/** Lance les tests publics (dossier {@code tests/}). */
public final class PublicTestChecker extends AbstractTestChecker {

    public PublicTestChecker(JavaToolkit toolkit) { super(toolkit); }

    @Override public String id() { return "tests-publics"; }
    @Override protected String label() { return "tests publics"; }

    @Override protected Path selectedTestDir(CheckerContext ctx) {
        return Path.of("tests/src/test/java");
    }
    @Override protected List<Path> compiledTestDirs(CheckerContext ctx) {
        return List.of(ctx.exerciseRefPath().resolve("tests/src/test/java"));
    }
}
```

- [ ] **Step 4.5 : Implémenter `PrivateTestChecker`**

Create `console/src/main/java/etnc/piscine/moulinette/console/checkers/PrivateTestChecker.java` :

```java
package etnc.piscine.moulinette.console.checkers;

import etnc.piscine.moulinette.framework.CheckerContext;

import java.nio.file.Path;
import java.util.List;

/** Lance les tests privés (dossier {@code tests-prives/}). Compile aussi {@code tests/} pour les utilitaires partagés. */
public final class PrivateTestChecker extends AbstractTestChecker {

    public PrivateTestChecker(JavaToolkit toolkit) { super(toolkit); }

    @Override public String id() { return "tests-prives"; }
    @Override protected String label() { return "tests privés"; }

    @Override protected Path selectedTestDir(CheckerContext ctx) {
        return Path.of("tests-prives/src/test/java");
    }
    @Override protected List<Path> compiledTestDirs(CheckerContext ctx) {
        return List.of(
            ctx.exerciseRefPath().resolve("tests/src/test/java"),        // utilitaires (CaptureSortie…)
            ctx.exerciseRefPath().resolve("tests-prives/src/test/java"));
    }
}
```

- [ ] **Step 4.6 : Vérifier `TestCheckersIT` vert**

Run: `mvn -f moulinette/pom.xml -pl console -am test -Dsurefire.excludedGroups=git,e2e -Dgroups=tools -Dtest=TestCheckersIT -Dsurefire.failIfNoSpecifiedTests=false`
Expected: PASS (3 tests). Si `--fail-if-no-tests` ou le format de sortie pose souci, vérifier la commande générée en lisant `ProcessResult.stderr()`.

- [ ] **Step 4.7 : Commit**

```bash
git add moulinette/console/src/main/java/etnc/piscine/moulinette/console/checkers moulinette/console/src/test/java/etnc/piscine/moulinette/console/checkers/TestCheckersIT.java
git commit -m "feat(console): PublicTestChecker + PrivateTestChecker (tâche #46)"
```

---

## Task 5 (#47) : `StyleChecker` + config Checkstyle

**Files:**
- Create: `console/src/main/resources/checkstyle-etnc.xml`
- Create: `console/.../checkers/StyleChecker.java`
- Test: `console/.../checkers/StyleCheckerIT.java`

- [ ] **Step 5.1 : Config Checkstyle bundlée**

Create `console/src/main/resources/checkstyle-etnc.xml` :

```xml
<?xml version="1.0"?>
<!DOCTYPE module PUBLIC
    "-//Checkstyle//DTD Checkstyle Configuration 1.3//EN"
    "https://checkstyle.org/dtds/configuration_1_3.dtd">
<!-- Config Checkstyle volontairement légère pour la Piscine ETNC (beta). -->
<module name="Checker">
    <property name="charset" value="UTF-8"/>
    <property name="severity" value="error"/>

    <module name="LineLength">
        <property name="max" value="120"/>
    </module>

    <module name="TreeWalker">
        <module name="UnusedImports"/>
        <module name="NeedBraces"/>
        <module name="LeftCurly"/>
        <module name="RightCurly"/>
        <module name="WhitespaceAround">
            <property name="allowEmptyConstructors" value="true"/>
            <property name="allowEmptyMethods" value="true"/>
        </module>
        <module name="TypeName"/>
    </module>
</module>
```

- [ ] **Step 5.2 : Test d'intégration `@Tag("tools")`**

Create `console/src/test/java/etnc/piscine/moulinette/console/checkers/StyleCheckerIT.java` :

```java
package etnc.piscine.moulinette.console.checkers;

import etnc.piscine.moulinette.framework.CheckResult;
import etnc.piscine.moulinette.framework.CheckerContext;
import etnc.piscine.moulinette.runner.ProcessRunner;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.*;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("tools")
class StyleCheckerIT {

    private StyleChecker checker() throws IOException {
        return new StyleChecker(new JavaToolkit(new ProcessRunner()), StyleChecker.extractBundledConfig());
    }

    @Test
    void code_propre_passe(@TempDir Path tmp) throws IOException {
        Path rendu = ecrireMain(tmp, """
            package etnc.m1;
            public class HelloWorld {
                public static void main(String[] args) {
                    System.out.println("hi");
                }
            }
            """);
        CheckResult r = checker().check(new CheckerContext("1.1.1", rendu, tmp.resolve("ref")));
        assertThat(r.status()).isEqualTo(CheckResult.Status.OK);
    }

    @Test
    void import_inutilise_echoue(@TempDir Path tmp) throws IOException {
        Path rendu = ecrireMain(tmp, """
            package etnc.m1;
            import java.util.List;
            public class HelloWorld {
                public static void main(String[] args) {
                    System.out.println("hi");
                }
            }
            """);
        CheckResult r = checker().check(new CheckerContext("1.1.1", rendu, tmp.resolve("ref")));
        assertThat(r.status()).isEqualTo(CheckResult.Status.FAIL);
        assertThat(r.messages()).isNotEmpty();
    }

    private static Path ecrireMain(Path tmp, String code) throws IOException {
        Path rendu = tmp.resolve("exo");
        Path src = rendu.resolve("starter/src/main/java/etnc/m1");
        Files.createDirectories(src);
        Files.writeString(src.resolve("HelloWorld.java"), code);
        return rendu;
    }
}
```

- [ ] **Step 5.3 : Lancer → échec compilation**

Run: `mvn -f moulinette/pom.xml -pl console -am test -Dsurefire.excludedGroups=git,e2e -Dgroups=tools -Dtest=StyleCheckerIT -Dsurefire.failIfNoSpecifiedTests=false`
Expected: COMPILATION FAILURE (`StyleChecker` absent).

- [ ] **Step 5.4 : Implémenter `StyleChecker`**

Create `console/src/main/java/etnc/piscine/moulinette/console/checkers/StyleChecker.java` :

```java
package etnc.piscine.moulinette.console.checkers;

import etnc.piscine.moulinette.framework.CheckResult;
import etnc.piscine.moulinette.framework.Checker;
import etnc.piscine.moulinette.framework.CheckerContext;
import etnc.piscine.moulinette.runner.ProcessResult;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/** Analyse de style avec Checkstyle (config bundlée). PMD viendra en fast-follow. */
public final class StyleChecker implements Checker {

    private final JavaToolkit toolkit;
    private final Path config;

    public StyleChecker(JavaToolkit toolkit, Path config) {
        this.toolkit = toolkit;
        this.config = config;
    }

    @Override public String id() { return "style"; }

    @Override
    public CheckResult check(CheckerContext ctx) {
        Path mainSrc = ctx.renduPath().resolve("starter/src/main/java");
        try {
            ProcessResult r = toolkit.checkstyle(ctx.renduPath(), config, List.of(mainSrc));
            if (r.exitCode() == 0) return CheckResult.ok();
            List<String> violations = new ArrayList<>();
            for (String l : r.stdout().split("\n")) {
                String t = l.strip();
                if (t.startsWith("[WARN]") || t.startsWith("[ERROR]")) violations.add(t);
            }
            String corps = violations.isEmpty() ? r.stdout() : String.join("\n", violations);
            return CheckResult.fail(
                "Des problèmes de style ont été détectés :\n" + corps,
                "Corrige l'indentation, les accolades et les imports inutilisés.");
        } catch (IOException e) {
            return CheckResult.error("Échec technique de l'analyse de style : " + e.getMessage());
        }
    }

    /** Extrait la config Checkstyle bundlée dans un fichier temporaire et renvoie son chemin. */
    public static Path extractBundledConfig() {
        try (InputStream in = StyleChecker.class.getResourceAsStream("/checkstyle-etnc.xml")) {
            if (in == null) throw new IllegalStateException("checkstyle-etnc.xml introuvable dans les resources");
            Path tmp = Files.createTempFile("checkstyle-etnc", ".xml");
            tmp.toFile().deleteOnExit();
            Files.copy(in, tmp, StandardCopyOption.REPLACE_EXISTING);
            return tmp;
        } catch (IOException e) {
            throw new UncheckedIOException("extraction config Checkstyle échouée", e);
        }
    }
}
```

- [ ] **Step 5.5 : Vérifier `StyleCheckerIT` vert**

Run: `mvn -f moulinette/pom.xml -pl console -am test -Dsurefire.excludedGroups=git,e2e -Dgroups=tools -Dtest=StyleCheckerIT -Dsurefire.failIfNoSpecifiedTests=false`
Expected: PASS (2 tests). Si Checkstyle 10.21.0 n'est pas résolvable, ajuster la version vers une release disponible (ex. 10.20.x) dans `console/pom.xml`.

- [ ] **Step 5.6 : Commit**

```bash
git add moulinette/console/src/main/resources/checkstyle-etnc.xml moulinette/console/src/main/java/etnc/piscine/moulinette/console/checkers/StyleChecker.java moulinette/console/src/test/java/etnc/piscine/moulinette/console/checkers/StyleCheckerIT.java
git commit -m "feat(console): StyleChecker + config Checkstyle bundlée (tâche #47)"
```

---

## Task 6 (#48) : Câblage `Main` / `MoulinetteRunner` + rapport `ReportGenerator` + E2E

**Files:**
- Modify: `console/.../trigger/MoulinetteRunner.java`
- Modify: `console/.../Main.java`
- Modify: `console/.../e2e/HappyPathE2EIT.java`

- [ ] **Step 6.1 : Adapter `MoulinetteRunner.Default` — contexte + build dir + rapport ReportGenerator**

Dans `MoulinetteRunner.java`, classe `Default` :

1. Ajouter les imports :
```java
import etnc.piscine.moulinette.framework.EvaluationReport;
import etnc.piscine.moulinette.reports.ReportGenerator;
import java.util.LinkedHashMap;
import java.util.Map;
```

2. Dans `runGroup`, pour chaque exo, avant la boucle des checkers, préparer le build dir et collecter les résultats par checker :

Remplacer le corps de la boucle `for (ExerciseEntry e : sg.exercices())` par :
```java
            for (ExerciseEntry e : sg.exercices()) {
                Path renduDir = repoRoot.resolve("exercises").resolve(e.exerciseDir().getFileName());
                nettoyerBuildDir(renduDir);
                Map<String, CheckResult> parChecker = new LinkedHashMap<>();
                boolean ok = true;
                StringBuilder msg = new StringBuilder();
                for (Checker c : checkers) {
                    CheckResult r = c.check(new CheckerContext(e.id(), renduDir, e.exerciseDir()));
                    parChecker.put(c.id(), r == null ? CheckResult.error("résultat null") : r);
                    if (r == null || r.status() != CheckResult.Status.OK) {
                        ok = false;
                        if (r != null) msg.append(c.id()).append(" : ")
                            .append(String.join(" / ", r.messages())).append('\n');
                        break;
                    }
                }
                ecrireRapportExo(renduDir, e.id(), parChecker);
                outcomes.add(new ExoOutcome(e.id(), ok, msg.toString()));
                if (!ok) { stopped = true; break; }
            }
```

3. Ajouter les imports nécessaires (`CheckerContext`) et les méthodes privées :
```java
        private void nettoyerBuildDir(Path renduDir) {
            Path build = renduDir.resolve(".piscine/build");
            if (Files.isDirectory(build)) {
                try (var walk = Files.walk(build)) {
                    walk.sorted(java.util.Comparator.reverseOrder())
                        .forEach(p -> { try { Files.deleteIfExists(p); } catch (IOException ignore) {} });
                } catch (IOException ignore) {}
            }
        }

        private void ecrireRapportExo(Path renduDir, String exoId, Map<String, CheckResult> parChecker) {
            try {
                Path dir = renduDir.resolve(".piscine/reports");
                Files.createDirectories(dir);
                var report = new EvaluationReport(exoId, parChecker);
                var gen = new ReportGenerator();
                Files.writeString(dir.resolve(exoId + ".md"), gen.toMarkdown(report));
                Files.writeString(dir.resolve(exoId + ".json"), gen.toJson(report));
            } catch (IOException ignore) { /* rapport best-effort */ }
        }
```

4. Ajouter l'import `import etnc.piscine.moulinette.framework.CheckerContext;` en tête.

> Le rapport agrégé par groupe (`writeReport`) existant reste pour la synthèse ; il peut continuer à pointer `reportsDir`. Les rapports par exo (md+json) via `ReportGenerator` sont la nouveauté.

- [ ] **Step 6.2 : Câbler les vrais Checkers dans `Main.runRepl`**

Dans `Main.java`, ajouter les imports :
```java
import etnc.piscine.moulinette.console.checkers.*;
import etnc.piscine.moulinette.runner.ProcessRunner;
import etnc.piscine.moulinette.framework.Checker;
import java.nio.file.Path;
```
Remplacer la ligne :
```java
        var runner = new MoulinetteRunner.Default(catalog, List.of(), repo.resolve(".piscine/reports"));
```
par :
```java
        var toolkit = new JavaToolkit(new ProcessRunner());
        Path styleConfig = StyleChecker.extractBundledConfig();
        List<Checker> checkers = List.of(
            new CompileChecker(toolkit),
            new PublicTestChecker(toolkit),
            new PrivateTestChecker(toolkit),
            new StyleChecker(toolkit, styleConfig));
        var runner = new MoulinetteRunner.Default(catalog, checkers, repo.resolve(".piscine/reports"));
```

- [ ] **Step 6.3 : Mettre à jour l'E2E avec les vrais Checkers**

Remplacer `HappyPathE2EIT.java` par une version `@Tag("e2e")` qui :
1. construit un faux repo Piscine avec l'exo 1.1.1 (main solution + tests publics + util + tests privés — réutiliser les fixtures de `TestCheckersIT`, copiées ici),
2. fait `init` du workspace,
3. câble les **vrais** Checkers,
4. script REPL `submit-start 1.1` → `git add .` → `git commit -m "rendu"` → `git push origin rendu/1.1` → `exit`,
5. vérifie le rapport ✓ ; puis un second scénario `main` vide → ✗ bloquant sur `tests-publics`.

```java
package etnc.piscine.moulinette.console.e2e;

import etnc.piscine.moulinette.console.Mode;
import etnc.piscine.moulinette.console.checkers.*;
import etnc.piscine.moulinette.console.commands.CommandRegistry;
import etnc.piscine.moulinette.console.git.ProcessGitClient;
import etnc.piscine.moulinette.console.repl.*;
import etnc.piscine.moulinette.console.trigger.MoulinetteRunner;
import etnc.piscine.moulinette.console.trigger.SubmissionTrigger;
import etnc.piscine.moulinette.console.workspace.*;
import etnc.piscine.moulinette.framework.Checker;
import etnc.piscine.moulinette.runner.ProcessRunner;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("e2e")
class HappyPathE2EIT {

    @Test
    void bonne_solution_donne_un_rapport_ok(@TempDir Path tmp) throws Exception {
        String out = jouerScenario(tmp, MAIN_OK);
        assertThat(out).contains("rendu/1.1").contains("✓ OK");
    }

    @Test
    void main_vide_bloque_sur_tests_publics(@TempDir Path tmp) throws Exception {
        String out = jouerScenario(tmp, MAIN_VIDE);
        assertThat(out).contains("✗ ÉCHEC");
    }

    private static final String MAIN_OK =
        "package etnc.m1;\npublic class HelloWorld { public static void main(String[] a){ System.out.println(\"Hello, world!\"); } }\n";
    private static final String MAIN_VIDE =
        "package etnc.m1;\npublic class HelloWorld { public static void main(String[] a){ } }\n";

    private static String jouerScenario(Path tmp, String mainCode) throws Exception {
        Path piscine = tmp.resolve("piscine-etnc");
        ecrireExoReference(piscine, mainCode);

        Path dest = tmp.resolve("piscine-curlier");
        var initializer = new LocalWorkspaceInitializer(new ProcessGitClient());
        Workspace ws = initializer.init(new InitRequest("curlier", dest, piscine, "module-1-fondamentaux"));

        var catalog = ExerciseCatalog.scan(piscine.resolve("exercises"));
        var toolkit = new JavaToolkit(new ProcessRunner());
        List<Checker> checkers = List.of(
            new CompileChecker(toolkit),
            new PublicTestChecker(toolkit),
            new PrivateTestChecker(toolkit),
            new StyleChecker(toolkit, StyleChecker.extractBundledConfig()));
        var runner = new MoulinetteRunner.Default(catalog, checkers, ws.repoRoot().resolve(".piscine/reports"));
        var trigger = new SubmissionTrigger(runner);
        var ctx = new ReplContext(ws.repoRoot(), new ProcessGitClient(), catalog, Mode.LOCAL);

        String script = String.join("\n",
            "submit-start 1.1", "git add .", "git commit -m \"rendu 1.1.1\"",
            "git push origin rendu/1.1", "exit", "");
        var sw = new StringWriter();
        new Repl(ctx, CommandRegistry.defaults(trigger), new ReplIo(new StringReader(script), sw)).run();
        return sw.toString();
    }

    /** Crée exercises/module-1-fondamentaux/1.1.1-hello-world avec starter (mainCode), tests, util, tests-prives. */
    private static void ecrireExoReference(Path piscine, String mainCode) throws Exception {
        Path exo = piscine.resolve("exercises/module-1-fondamentaux/1.1.1-hello-world");
        Path starter = exo.resolve("starter/src/main/java/etnc/m1");
        Files.createDirectories(starter);
        Files.writeString(starter.resolve("HelloWorld.java"), mainCode);
        Files.writeString(exo.resolve("metadata.yml"), """
            slug: hello-world
            module: 1
            sous_groupe: "1.1"
            position: 1
            notions: []
            """);
        Path pub = exo.resolve("tests/src/test/java/etnc/m1");
        Files.createDirectories(pub);
        Files.writeString(pub.resolve("HelloWorldTest.java"), """
            package etnc.m1;
            import etnc.util.CaptureSortie;
            import org.junit.jupiter.api.Test;
            import static org.assertj.core.api.Assertions.assertThat;
            class HelloWorldTest {
              @Test void affiche() {
                String s = CaptureSortie.capturer(() -> HelloWorld.main(new String[]{}));
                assertThat(s).isEqualTo("Hello, world!" + System.lineSeparator());
              }
            }
            """);
        Path util = exo.resolve("tests/src/test/java/etnc/util");
        Files.createDirectories(util);
        Files.writeString(util.resolve("CaptureSortie.java"), """
            package etnc.util;
            import java.io.ByteArrayOutputStream;
            import java.io.PrintStream;
            import java.nio.charset.StandardCharsets;
            public final class CaptureSortie {
              private CaptureSortie() {}
              public static String capturer(Runnable action) {
                PrintStream original = System.out;
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                try (PrintStream c = new PrintStream(buffer, true, StandardCharsets.UTF_8)) {
                  System.setOut(c); action.run(); System.out.flush();
                } finally { System.setOut(original); }
                return buffer.toString(StandardCharsets.UTF_8);
              }
            }
            """);
        Path priv = exo.resolve("tests-prives/src/test/java/etnc/m1");
        Files.createDirectories(priv);
        Files.writeString(priv.resolve("HelloWorldPriveTest.java"), """
            package etnc.m1;
            import etnc.util.CaptureSortie;
            import org.junit.jupiter.api.Test;
            import static org.assertj.core.api.Assertions.assertThat;
            class HelloWorldPriveTest {
              @Test void ignore_args() {
                String s = CaptureSortie.capturer(() -> HelloWorld.main(new String[]{"x"}));
                assertThat(s).isEqualTo("Hello, world!" + System.lineSeparator());
              }
            }
            """);
    }
}
```

- [ ] **Step 6.4 : Lancer unitaires + e2e**

Run: `mvn -f moulinette/pom.xml -pl console -am test`
Expected: BUILD SUCCESS (unitaires inchangés verts).
Run: `mvn -f moulinette/pom.xml -pl console -am test -Dsurefire.excludedGroups=git,tools -Dgroups=e2e`
Expected: PASS (2 tests E2E).

- [ ] **Step 6.5 : Vérifier le jar packagé fonctionne toujours**

Run: `mvn -f moulinette/pom.xml -pl console -am package -q`
Run (Windows bash): `"$JAVA_HOME/bin/java" -jar moulinette/console/target/moulinette-console.jar --help`
Expected: l'aide s'affiche (le jar contient désormais junit/checkstyle).

- [ ] **Step 6.6 : Commit**

```bash
git add moulinette/console/src
git commit -m "feat(console): câble les vrais Checkers + rapport ReportGenerator + E2E (tâche #48)"
```

---

## Task 7 (#49) : Script `build-bundle` + lanceurs + LISEZMOI

**Files:**
- Create: `scripts/build-bundle.ps1`, `scripts/build-bundle.sh`

- [ ] **Step 7.1 : Script PowerShell**

Create `scripts/build-bundle.ps1` :

```powershell
# Construit le bundle ZIP portable auto-suffisant de la Piscine ETNC.
# Usage : .\scripts\build-bundle.ps1 [-JdkPath <chemin JDK 25>] [-OutDir <dossier>]
param(
    [string] $JdkPath = $env:JAVA_HOME,
    [string] $OutDir = ""
)
$ErrorActionPreference = "Stop"
$RepoRoot = (Resolve-Path "$PSScriptRoot\..").Path
if (-not $OutDir) { $OutDir = Join-Path $RepoRoot "dist" }
$Stage = Join-Path $OutDir "piscine-etnc-stagiaire"

if (-not (Test-Path "$JdkPath\bin\java.exe")) {
    Write-Error "JDK introuvable à '$JdkPath'. Passe -JdkPath <chemin d'un JDK 25>."
}

# 1. Build de l'uber-jar (préfère mvn système)
$MvnCmd = if (Get-Command mvn -ErrorAction SilentlyContinue) { "mvn" } else { "$RepoRoot\mvnw.cmd" }
Write-Host "[bundle] Build de l'uber-jar (via $MvnCmd) ..."
& $MvnCmd -f "$RepoRoot\moulinette\pom.xml" -pl console -am -q -DskipTests package
if ($LASTEXITCODE -ne 0) { throw "Build Maven échoué." }
$Jar = "$RepoRoot\moulinette\console\target\moulinette-console.jar"
if (-not (Test-Path $Jar)) { throw "Uber-jar introuvable : $Jar" }

# 2. Staging
if (Test-Path $Stage) { Remove-Item -Recurse -Force $Stage }
New-Item -ItemType Directory -Force -Path "$Stage\app","$Stage\piscine\docs","$Stage\workspace" | Out-Null

Write-Host "[bundle] Copie de l'uber-jar ..."
Copy-Item $Jar "$Stage\app\moulinette-console.jar"

Write-Host "[bundle] Copie des exercices + guide stagiaire ..."
Copy-Item -Recurse "$RepoRoot\exercises" "$Stage\piscine\exercises"
Copy-Item "$RepoRoot\docs\piscine-stagiaire.md" "$Stage\piscine\docs\piscine-stagiaire.md"

Write-Host "[bundle] Copie du JDK portable (peut prendre un moment) ..."
Copy-Item -Recurse $JdkPath "$Stage\jdk"

# 3. Lanceurs + LISEZMOI
@'
@echo off
setlocal
set "JAVA=%~dp0jdk\bin\java.exe"
set "JAR=%~dp0app\moulinette-console.jar"
set "WS=%~dp0workspace"
if not exist "%WS%\.git" (
    "%JAVA%" -jar "%JAR%" init --nom stagiaire --dest "%WS%" --piscine-repo "%~dp0piscine"
)
"%JAVA%" -jar "%JAR%" repl --repo "%WS%" --piscine-repo "%~dp0piscine"
endlocal
'@ | Set-Content -Encoding ascii "$Stage\piscine.bat"

@'
#!/usr/bin/env bash
HERE="$(cd "$(dirname "$0")" && pwd)"
JAVA="$HERE/jdk/bin/java"
JAR="$HERE/app/moulinette-console.jar"
WS="$HERE/workspace"
if [ ! -d "$WS/.git" ]; then
  "$JAVA" -jar "$JAR" init --nom stagiaire --dest "$WS" --piscine-repo "$HERE/piscine"
fi
"$JAVA" -jar "$JAR" repl --repo "$WS" --piscine-repo "$HERE/piscine"
'@ | Set-Content -Encoding ascii "$Stage\piscine.sh"

@'
Piscine ETNC — console locale

Double-cliquez sur piscine.bat (Windows) pour demarrer.
Au premier lancement, votre espace de travail est cree automatiquement.
Tapez `help` dans la console pour la liste des commandes, `exit` pour quitter.
Vos rapports de correction : workspace\.piscine\reports\
Guide complet : piscine\docs\piscine-stagiaire.md
'@ | Set-Content -Encoding utf8 "$Stage\LISEZMOI.txt"

# 4. Zip
$Stamp = Get-Date -Format "yyyyMMdd"
$Zip = Join-Path $OutDir "piscine-etnc-stagiaire-$Stamp.zip"
if (Test-Path $Zip) { Remove-Item -Force $Zip }
Write-Host "[bundle] Compression -> $Zip"
Compress-Archive -Path $Stage -DestinationPath $Zip
Write-Host "[bundle] OK : $Zip"
```

- [ ] **Step 7.2 : Script Bash (variante Linux/macOS)**

Create `scripts/build-bundle.sh` :

```bash
#!/usr/bin/env bash
# Construit le bundle portable de la Piscine ETNC.
# Usage : ./scripts/build-bundle.sh [--jdk <chemin>] [--out <dossier>]
set -euo pipefail
JDK_PATH="${JAVA_HOME:-}"
OUT_DIR=""
while [[ $# -gt 0 ]]; do
  case "$1" in
    --jdk) JDK_PATH="$2"; shift 2;;
    --out) OUT_DIR="$2"; shift 2;;
    *) echo "Argument inconnu : $1"; exit 2;;
  esac
done
REPO_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
OUT_DIR="${OUT_DIR:-$REPO_ROOT/dist}"
STAGE="$OUT_DIR/piscine-etnc-stagiaire"

[[ -x "$JDK_PATH/bin/java" ]] || { echo "JDK introuvable à '$JDK_PATH' (utilise --jdk)."; exit 1; }

if command -v mvn >/dev/null 2>&1; then MVN=(mvn); else MVN=("$REPO_ROOT/mvnw"); fi
echo "[bundle] Build de l'uber-jar ..."
"${MVN[@]}" -f "$REPO_ROOT/moulinette/pom.xml" -pl console -am -q -DskipTests package
JAR="$REPO_ROOT/moulinette/console/target/moulinette-console.jar"
[[ -f "$JAR" ]] || { echo "Uber-jar introuvable : $JAR"; exit 1; }

rm -rf "$STAGE"
mkdir -p "$STAGE/app" "$STAGE/piscine/docs" "$STAGE/workspace"
cp "$JAR" "$STAGE/app/moulinette-console.jar"
cp -r "$REPO_ROOT/exercises" "$STAGE/piscine/exercises"
cp "$REPO_ROOT/docs/piscine-stagiaire.md" "$STAGE/piscine/docs/piscine-stagiaire.md"
echo "[bundle] Copie du JDK portable ..."
cp -r "$JDK_PATH" "$STAGE/jdk"

cat > "$STAGE/piscine.sh" <<'EOF'
#!/usr/bin/env bash
HERE="$(cd "$(dirname "$0")" && pwd)"
JAVA="$HERE/jdk/bin/java"
JAR="$HERE/app/moulinette-console.jar"
WS="$HERE/workspace"
if [ ! -d "$WS/.git" ]; then
  "$JAVA" -jar "$JAR" init --nom stagiaire --dest "$WS" --piscine-repo "$HERE/piscine"
fi
"$JAVA" -jar "$JAR" repl --repo "$WS" --piscine-repo "$HERE/piscine"
EOF
chmod +x "$STAGE/piscine.sh"

cat > "$STAGE/LISEZMOI.txt" <<'EOF'
Piscine ETNC — console locale
Lancez ./piscine.sh pour demarrer. Votre espace est cree au premier lancement.
Tapez `help` dans la console, `exit` pour quitter.
Rapports : workspace/.piscine/reports/  — Guide : piscine/docs/piscine-stagiaire.md
EOF

STAMP="$(date +%Y%m%d)"
ZIP="$OUT_DIR/piscine-etnc-stagiaire-$STAMP.zip"
rm -f "$ZIP"
( cd "$OUT_DIR" && zip -rq "$ZIP" "piscine-etnc-stagiaire" )
echo "[bundle] OK : $ZIP"
```

- [ ] **Step 7.3 : Test manuel du bundle**

Run (Windows PowerShell): `.\scripts\build-bundle.ps1`
Expected: `dist/piscine-etnc-stagiaire-<date>.zip` créé, dossier `dist/piscine-etnc-stagiaire/` contient `jdk/`, `app/moulinette-console.jar`, `piscine/exercises/...`, `piscine.bat`.

Vérification fonctionnelle (copier le dossier ailleurs, hors du repo, puis lancer) :
```
cd <un dossier hors repo>\piscine-etnc-stagiaire
.\piscine.bat
```
Dans le REPL : `submit-start 1.1`, éditer `workspace\exercises\1.1.1-hello-world\starter\src\main\java\etnc\m1\HelloWorld.java` avec la bonne solution, puis `git add .`, `git commit -m "rendu"`, `git push origin rendu/1.1` → rapport ✓.

- [ ] **Step 7.4 : Ignorer `dist/` dans git**

Vérifier/ajouter à `.gitignore` racine : `dist/`. Si le fichier n'existe pas, le créer avec cette ligne.

- [ ] **Step 7.5 : Commit**

```bash
git add scripts/build-bundle.ps1 scripts/build-bundle.sh .gitignore
git commit -m "feat(scripts): build-bundle ZIP portable auto-suffisant (tâche #49)"
```

---

## Task 8 (#50) : `docs/deploiement-instructeur.md`

**Files:**
- Create: `docs/deploiement-instructeur.md`

- [ ] **Step 8.1 : Rédiger le guide de déploiement**

Create `docs/deploiement-instructeur.md` avec les sections : prérequis machine instructeur (JDK 25, repo) ; construire le bundle (`build-bundle.ps1 [-JdkPath]`, emplacement du ZIP) ; déployer (copier ZIP sur PC stagiaire, décompresser) ; lancer (`piscine.bat`) ; premier lancement attendu ; **dépannage** (Java introuvable = bundle incomplet ; antivirus bloquant le .bat ; chemins avec espaces/accents ; rapports dans `workspace/.piscine/reports/` ; réinitialiser en supprimant `workspace/`) ; renvoi vers `piscine-stagiaire.md`.

Contenu :

````markdown
# Déploiement Piscine ETNC sur un PC stagiaire

Ce guide s'adresse à l'**instructeur** qui prépare et déploie l'application sur les postes des stagiaires. Le PC stagiaire n'a **rien à pré-installer** (ni Java, ni Maven, ni internet).

## 1. Prérequis (machine de l'instructeur uniquement)

- Le dépôt Piscine ETNC cloné.
- Un **JDK 25** (Temurin recommandé). Par défaut le script prend `JAVA_HOME` ; sinon passez `-JdkPath`.
- `git` et un Maven (`mvn` système ou le wrapper `./mvnw`).

## 2. Construire le bundle

```powershell
# Windows
.\scripts\build-bundle.ps1            # utilise %JAVA_HOME%
.\scripts\build-bundle.ps1 -JdkPath "E:\java\jdk-25.0.3+9"
```
```bash
# Linux/macOS
./scripts/build-bundle.sh --jdk /opt/jdk-25
```

Résultat : `dist/piscine-etnc-stagiaire-<date>.zip` (contient le JDK portable, l'uber-jar, les exercices et un lanceur).

## 3. Déployer sur le PC stagiaire

1. Copier le ZIP sur le poste (clé USB, partage réseau).
2. Décompresser **où on veut** (Bureau, `C:\Piscine`, une clé USB…). Aucun droit administrateur requis.

## 4. Lancer

- Windows : double-cliquer **`piscine.bat`**.
- Linux/macOS : `./piscine.sh`.

Au **premier lancement**, l'espace de travail du stagiaire est créé automatiquement (`workspace/`), puis la console interactive (REPL) démarre. Les lancements suivants reprennent le même espace.

## 5. Ce que fait le stagiaire ensuite

Voir [`piscine-stagiaire.md`](piscine-stagiaire.md) (inclus dans le bundle sous `piscine/docs/`). En résumé : `submit-start 1.1` → éditer le code → `git add` / `commit` / `push origin rendu/1.1` → lecture du rapport.

## 6. Dépannage

| Symptôme | Cause probable | Solution |
|---|---|---|
| « java introuvable » / la fenêtre se ferme | bundle incomplet (dossier `jdk/` manquant) | Reconstruire le bundle, vérifier que `jdk/bin/java.exe` existe dans le ZIP. |
| L'antivirus bloque `piscine.bat` | politique locale | Autoriser le script, ou lancer la commande du `.bat` à la main dans un terminal. |
| Erreurs sur des chemins avec accents/espaces | chemin d'extraction exotique | Extraire dans un chemin simple (ex. `C:\Piscine`). |
| « Où sont mes résultats ? » | — | `workspace\.piscine\reports\<groupe>.md` (et `.json`). |
| Repartir de zéro | — | Fermer la console, supprimer le dossier `workspace\`, relancer `piscine.bat`. |
| La correction est toujours OK alors que le code est faux | tests de référence absents pour cet exo | Vérifier que `piscine/exercises/.../<exo>/tests` et `tests-prives` sont bien dans le bundle. |

## 7. Limites connues (phase de test)

- Le bundle contient `tests-prives/` et `solution/` : un stagiaire curieux peut les ouvrir. Acceptable en phase de test ; l'anti-triche est prévu ultérieurement.
- Bundle Windows-first ; la variante `.sh` est fournie mais non durcie.
````

- [ ] **Step 8.2 : Commit**

```bash
git add docs/deploiement-instructeur.md
git commit -m "docs: guide de déploiement instructeur (tâche #50)"
```

---

## Task 9 (#51) : `docs/architecture-moulinette.md`

**Files:**
- Create: `docs/architecture-moulinette.md`

- [ ] **Step 9.1 : Rédiger la doc d'architecture dev**

Create `docs/architecture-moulinette.md` :

````markdown
# Architecture de la moulinette ETNC (pour les développeurs)

## Vue d'ensemble

Projet Maven multi-module sous `moulinette/` :

| Module | Rôle |
|---|---|
| `framework` | Contrats : `Checker`, `CheckerContext`, `CheckResult`, `EvaluationReport`. |
| `runner` | `ProcessRunner` : exécution de commandes système en sous-processus (timeout 30 s). |
| `reports` | `ReportGenerator` : rapport Markdown + JSON depuis un `EvaluationReport`. |
| `cli` | Point d'entrée headless (CI) : `run --exo X --rendu Y`. |
| `console` | Point d'entrée interactif stagiaire : REPL git + évaluation locale. |

Dépendances : `console`/`cli` → `framework` + `runner` + `reports`. Pas de cycle.

## Deux points d'entrée

- **`cli`** : usage machine/CI, sans interaction.
- **`console`** : REPL git sandbox pour le stagiaire (commandes `add/commit/push/...`, `submit-start`). Un `git push origin rendu/<sous-groupe>` déclenche l'évaluation.

## Flux d'évaluation (`console`)

```
git push origin rendu/1.1
  → PushCommand → SubmissionTrigger.onPushSucceeded
      → MoulinetteRunner.Default.runGroup("1.1", workspace)
          pour chaque exo (difficulté croissante) :
            CompileChecker → PublicTestChecker → PrivateTestChecker → StyleChecker
            (arrêt au 1er Checker non-OK ; arrêt au 1er exo non-OK)
            → EvaluationReport → ReportGenerator (md + json) dans .piscine/reports/
      → récap console + chemin du rapport
```

## L'astuce « l'uber-jar EST le classpath »

La console est packagée en uber-jar (maven-shade) qui embarque JUnit, AssertJ et Checkstyle (déclarés en scope `runtime`). À l'exécution, les Checkers compilent et lancent le code stagiaire avec ce **même jar** comme classpath : aucune dépendance réseau, aucun Maven requis sur le PC stagiaire.

- `JavaToolkit.toolingClasspath()` renvoie le chemin de l'uber-jar quand on tourne depuis un `.jar` ; sinon (dev/surefire) il retombe sur `System.getProperty("java.class.path")`, qui contient déjà ces dépendances (scope `runtime` ⇒ présent au classpath de test).
- Les outils sont invoqués par nom de classe : `org.junit.platform.console.ConsoleLauncher`, `com.puppycrawl.tools.checkstyle.Main`.

## Ajouter un Checker

1. Implémenter `framework.Checker` (méthode `id()` + `check(CheckerContext)`).
2. Utiliser `JavaToolkit` pour compiler/exécuter au besoin ; renvoyer `CheckResult.ok()/fail(...)/error(...)`.
3. L'enregistrer dans `Main.runRepl` (liste `checkers`, dans l'ordre voulu).

## Ajouter un exercice

Structure (voir `docs/format-exercice.md`) :
```
exercises/module-X/M.S.E-slug/
  starter/        code de départ (copié dans le workspace stagiaire)
  tests/          tests publics (+ utilitaires comme CaptureSortie)
  tests-prives/   tests privés (exécutés par la moulinette)
  solution/       solution de référence
  metadata.yml    sous_groupe, position (ordre de difficulté), notions
  evaluation.yml  critères + poids (note /20 à venir)
```
Le `MoulinetteRunner` évalue les exos d'un sous-groupe dans l'ordre `position` croissant.

## Tests

| Suite | Tag | Lancer |
|---|---|---|
| Unitaires | (aucun) | `mvn -f moulinette/pom.xml -pl console -am test` |
| Intégration git | `git` | `... -Dsurefire.excludedGroups=e2e,tools -Dgroups=git` |
| Outillage (compile/test/style réels) | `tools` | `... -Dsurefire.excludedGroups=git,e2e -Dgroups=tools` |
| Bout en bout | `e2e` | `... -Dsurefire.excludedGroups=git,tools -Dgroups=e2e` |

Exclusions par défaut : `surefire.excludedGroups = git,e2e,tools` (les suites lourdes ne tournent qu'à la demande).

**Maven** : `mvnw` peut échouer à télécharger Maven dans un réseau restreint ; préférer un `mvn` système si présent (les scripts le font).

## Limites connues & dette

- **PMD** différé (le `StyleChecker` est prévu pour l'accueillir ; Checkstyle seul aujourd'hui).
- **Note pondérée /20 + seuil** : pas encore calculée (pass/fail par Checker).
- **Anti-triche** (tests-prives visibles dans le bundle) : tâche #29.
- **Isolation Docker** du runner : tâche #30.
- **Mode `nominal`** (plateforme serveur) : rejeté pour l'instant (tâches #26-#28).
````

- [ ] **Step 9.2 : Commit**

```bash
git add docs/architecture-moulinette.md
git commit -m "docs: architecture moulinette pour devs (tâche #51)"
```

---

## Task 10 : Clôture backlog + vérification finale

- [ ] **Step 10.1 : Vérifier les 4 suites**

```bash
mvn -f moulinette/pom.xml verify                                                   # unitaires + reactor
mvn -f moulinette/pom.xml -pl console -am test -Dsurefire.excludedGroups=e2e,tools -Dgroups=git
mvn -f moulinette/pom.xml -pl console -am test -Dsurefire.excludedGroups=git,e2e   -Dgroups=tools
mvn -f moulinette/pom.xml -pl console -am test -Dsurefire.excludedGroups=git,tools -Dgroups=e2e
```
Expected: BUILD SUCCESS partout.

- [ ] **Step 10.2 : Mettre à jour `docs/backlog.md`**

Ajouter une section « Phase 1ter — Target MVP » avec les tâches #43 à #51 marquées `Faite` + lien vers la spec et le plan. Documenter les limites (PMD différé, note /20 future).

- [ ] **Step 10.3 : Commit**

```bash
git add docs/backlog.md
git commit -m "docs(backlog): clôture target MVP (#43-#51)"
```

---

## Auto-revue (spec → plan)

| Exigence spec | Tâche(s) |
|---|---|
| §3.1 `CheckerContext` enrichi | T1 |
| §3.2 `JavaToolkit` | T2 |
| §3.3 `FqcnExtractor` | T2 |
| §3.4 `CompileChecker` | T3 |
| §3.4 `PublicTestChecker` + `PrivateTestChecker` | T4 |
| §3.4 `StyleChecker` + config Checkstyle | T5 |
| §4 câblage Main/MoulinetteRunner + ReportGenerator | T6 |
| §5 bundle + lanceurs | T7 |
| §6.1 doc déploiement instructeur | T8 |
| §6.2 doc architecture dev | T9 |
| §8 tests unit/tools/e2e + tag `tools` exclu par défaut | T2 (tag), T2-T6 (tests) |
| §9 critères d'acceptation | T1-T10 |
| §10 PMD/score hors périmètre | documenté T9 + backlog T10 |

**Placeholders** : aucun (tout le code est fourni). **Risques signalés** (non bloquants) : (a) version Checkstyle à ajuster si 10.21.0 indisponible (Step 5.5) ; (b) si surefire isole le classpath via un jar manifest-only, `toolingClasspath()` en dev pourrait être incomplet — peu probable avec surefire 3.5.x par défaut ; le cas échéant, lancer les tests `tools` depuis le jar packagé. (c) Le format de sortie du ConsoleLauncher peut varier ; le parsing `extraitEchecs` est best-effort, l'OK/FAIL repose sur le code de sortie (fiable).
