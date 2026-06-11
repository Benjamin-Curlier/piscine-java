# Migration Maven → Gradle (v1 chantier 1) — Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Migration iso-fonctionnelle du build `moulinette/` de Maven vers Gradle (Kotlin DSL) : mêmes 5 modules, même uber-jar `moulinette-console.jar`, mêmes 4 suites de tests, CI vert.

**Architecture:** Gradle multi-module avec wrapper versionné (politique identique au `mvnw` actuel, cf. #54), version catalog (`gradle/libs.versions.toml`), conventions communes dans le `build.gradle.kts` racine (5 petits modules — pas de buildSrc). maven-shade → plugin **Shadow** (`com.gradleup.shadow`), suites taguées → tâches `Test` dédiées (`testGit`, `testTools`, `testE2e`).

**Tech Stack:** Gradle 9.2+ (support JDK 25 depuis 9.1), Shadow 9.x, JUnit 5.11.4, mêmes dépendances qu'aujourd'hui.

**Hors périmètre :** les 130 `pom.xml` des exercices (`starter/`, `solution/`) restent en Maven — ils sont le build *du stagiaire/contributeur*, pas celui de la moulinette. `scripts/valider-solutions.sh` et le job CI `valider-solutions` ne changent pas.

**Risque identifié à vérifier en Task 6 :** `JavaToolkit.toolingClasspath()` retombe sur `System.getProperty("java.class.path")` hors uber-jar ; sous Gradle les deps `runtimeOnly` doivent être présentes au classpath des tests (c'est le cas : `testRuntimeClasspath` étend `runtimeClasspath`), et les argfiles Windows sont expansés par le launcher (java.class.path reste complet). La suite `tools` est le test de non-régression de ce point.

---

### Task 1: Bootstrap Gradle + wrapper versionné

**Files:**
- Create: `moulinette/gradle/wrapper/gradle-wrapper.properties`, `gradle-wrapper.jar`, `moulinette/gradlew`, `moulinette/gradlew.bat`
- Modify: `.gitignore`

- [ ] **Step 1: Installer une distribution Gradle locale (une seule fois, pour générer le wrapper)**

```powershell
# Sans droits admin : zip dans E:\java, comme Maven
Invoke-WebRequest https://services.gradle.org/distributions/gradle-9.2-bin.zip -OutFile E:\java\gradle-9.2-bin.zip
Expand-Archive E:\java\gradle-9.2-bin.zip -DestinationPath E:\java
E:\java\gradle-9.2\bin\gradle.bat -v
```
Expected: `Gradle 9.2` (vérifier d'abord sur https://gradle.org/releases/ s'il existe une 9.x plus récente ; toute 9.1+ supporte le JDK 25). Si le réseau bloque services.gradle.org, miroir GitHub : `https://github.com/gradle/gradle-distributions/releases`.

- [ ] **Step 2: Générer le wrapper dans `moulinette/`**

```powershell
cd "E:\claude\Piscine ETNC\moulinette"
E:\java\gradle-9.2\bin\gradle.bat wrapper --gradle-version 9.2 --distribution-type bin
```
Expected: création de `gradlew`, `gradlew.bat`, `gradle/wrapper/gradle-wrapper.{jar,properties}`.

- [ ] **Step 3: Versionner le wrapper jar (même politique que maven-wrapper.jar)**

Dans `.gitignore`, sous la ligne `!.mvn/wrapper/maven-wrapper.jar`, ajouter :
```
# Le wrapper Gradle est volontairement versionné (build offline reproductible, même politique que #54)
!moulinette/gradle/wrapper/gradle-wrapper.jar
# Gradle
.gradle/
build/
```
Note : `build/` est déjà couvert nulle part ; vérifier qu'aucun dossier `build/` légitime n'existe à la racine (`git status` doit rester propre).

- [ ] **Step 4: Commit**

```bash
git add moulinette/gradlew moulinette/gradlew.bat moulinette/gradle .gitignore
git commit -m "build(gradle): bootstrap wrapper Gradle 9.2 (chantier 1 v1)"
```

---

### Task 2: Squelette multi-module (settings + version catalog + conventions racine)

**Files:**
- Create: `moulinette/settings.gradle.kts`
- Create: `moulinette/gradle/libs.versions.toml`
- Create: `moulinette/build.gradle.kts`

- [ ] **Step 1: Écrire `moulinette/settings.gradle.kts`**

```kotlin
rootProject.name = "moulinette"

include("framework", "runner", "reports", "cli", "console")
```

- [ ] **Step 2: Écrire `moulinette/gradle/libs.versions.toml`** (mêmes versions que le pom racine)

```toml
[versions]
junit = "5.11.4"
junit-platform = "1.11.4"
assertj = "3.26.3"
slf4j = "2.0.16"
logback = "1.5.12"
snakeyaml = "2.3"
checkstyle = "10.21.0"
shadow = "9.2.2"

[libraries]
junit-jupiter = { module = "org.junit.jupiter:junit-jupiter", version.ref = "junit" }
junit-platform-console = { module = "org.junit.platform:junit-platform-console", version.ref = "junit-platform" }
junit-platform-launcher = { module = "org.junit.platform:junit-platform-launcher", version.ref = "junit-platform" }
assertj-core = { module = "org.assertj:assertj-core", version.ref = "assertj" }
slf4j-api = { module = "org.slf4j:slf4j-api", version.ref = "slf4j" }
logback-classic = { module = "ch.qos.logback:logback-classic", version.ref = "logback" }
snakeyaml = { module = "org.yaml:snakeyaml", version.ref = "snakeyaml" }
checkstyle = { module = "com.puppycrawl.tools:checkstyle", version.ref = "checkstyle" }

[plugins]
shadow = { id = "com.gradleup.shadow", version.ref = "shadow" }
```
Note : vérifier la dernière version du plugin Shadow compatible Gradle 9 sur https://plugins.gradle.org/plugin/com.gradleup.shadow et ajuster `shadow = "9.2.2"` si besoin.

- [ ] **Step 3: Écrire `moulinette/build.gradle.kts`** (conventions communes)

```kotlin
plugins {
    java
}

subprojects {
    apply(plugin = "java")

    group = "etnc.piscine.moulinette"
    version = "0.1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    extensions.configure<JavaPluginExtension> {
        toolchain {
            languageVersion = JavaLanguageVersion.of(25)
        }
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
        // Sortie lisible comme surefire
        testLogging {
            events("failed", "skipped")
            showStandardStreams = false
        }
    }
}
```

- [ ] **Step 4: Vérifier que Gradle voit les 5 modules**

Run: `cd moulinette && ./gradlew projects` (ou `.\gradlew.bat projects` sous Windows)
Expected: liste `:framework`, `:runner`, `:reports`, `:cli`, `:console`. (Les modules n'ont pas encore de build.gradle.kts : Gradle les accepte comme projets java « vides » via le `apply` du parent — la compilation réelle arrive aux Tasks 3-5.)

- [ ] **Step 5: Commit**

```bash
git add moulinette/settings.gradle.kts moulinette/build.gradle.kts moulinette/gradle/libs.versions.toml
git commit -m "build(gradle): squelette multi-module + version catalog"
```

---

### Task 3: Modules bibliothèques — framework, runner, reports

**Files:**
- Create: `moulinette/framework/build.gradle.kts`
- Create: `moulinette/runner/build.gradle.kts`
- Create: `moulinette/reports/build.gradle.kts`

- [ ] **Step 1: Écrire `moulinette/framework/build.gradle.kts`**

```kotlin
dependencies {
    implementation(libs.slf4j.api)

    testImplementation(libs.junit.jupiter)
    testImplementation(libs.assertj.core)
    testRuntimeOnly(libs.junit.platform.launcher)
    testRuntimeOnly(libs.logback.classic)
}
```
Note : `junit-platform-launcher` en `testRuntimeOnly` est requis par Gradle 9 (il ne l'injecte plus automatiquement, contrairement à surefire) — à reproduire dans **tous** les modules.

- [ ] **Step 2: Écrire `moulinette/runner/build.gradle.kts`**

```kotlin
dependencies {
    implementation(project(":framework"))
    implementation(libs.slf4j.api)

    testImplementation(libs.junit.jupiter)
    testImplementation(libs.assertj.core)
    testRuntimeOnly(libs.junit.platform.launcher)
    testRuntimeOnly(libs.logback.classic)
}
```

- [ ] **Step 3: Écrire `moulinette/reports/build.gradle.kts`** — contenu identique au Step 2 (mêmes dépendances : `project(":framework")`, slf4j, junit, assertj, launcher, logback test).

```kotlin
dependencies {
    implementation(project(":framework"))
    implementation(libs.slf4j.api)

    testImplementation(libs.junit.jupiter)
    testImplementation(libs.assertj.core)
    testRuntimeOnly(libs.junit.platform.launcher)
    testRuntimeOnly(libs.logback.classic)
}
```

- [ ] **Step 4: Vérifier compilation + tests des 3 modules**

Run: `cd moulinette && ./gradlew :framework:test :runner:test :reports:test`
Expected: BUILD SUCCESSFUL, mêmes tests qu'avec `mvn -pl framework,runner,reports test`. Note : Maven expose les dépendances transitives à la compilation, Gradle `implementation` non — règle simple si une erreur de compilation apparaît : déclarer explicitement la dépendance manquante dans le module qui l'utilise (c'est déjà fait ci-dessus : chaque module déclare `framework` directement).

- [ ] **Step 5: Commit**

```bash
git add moulinette/framework/build.gradle.kts moulinette/runner/build.gradle.kts moulinette/reports/build.gradle.kts
git commit -m "build(gradle): modules framework, runner, reports"
```

---

### Task 4: Module cli (+ tâche d'exécution remplaçant exec:java)

**Files:**
- Create: `moulinette/cli/build.gradle.kts`

- [ ] **Step 1: Écrire `moulinette/cli/build.gradle.kts`**

```kotlin
plugins {
    application
}

dependencies {
    implementation(project(":framework"))
    implementation(project(":runner"))
    implementation(project(":reports"))
    implementation(libs.slf4j.api)
    runtimeOnly(libs.logback.classic)

    testImplementation(libs.junit.jupiter)
    testImplementation(libs.assertj.core)
    testRuntimeOnly(libs.junit.platform.launcher)
    testRuntimeOnly(libs.logback.classic)
}

application {
    mainClass = "etnc.piscine.moulinette.cli.Main"
}
```

- [ ] **Step 2: Vérifier tests + exécution**

Run: `cd moulinette && ./gradlew :cli:test`
Expected: BUILD SUCCESSFUL.
Run: `./gradlew :cli:run --args="run --help"` (adapter aux vrais args du Main ; l'équivalent de `mvn -pl cli exec:java`)
Expected: la CLI répond (même sortie qu'avant).

- [ ] **Step 3: Commit**

```bash
git add moulinette/cli/build.gradle.kts
git commit -m "build(gradle): module cli + tache run (remplace exec:java)"
```

---

### Task 5: Module console — dépendances, tags par défaut, Shadow (uber-jar)

**Files:**
- Create: `moulinette/console/build.gradle.kts`

- [ ] **Step 1: Écrire `moulinette/console/build.gradle.kts`**

```kotlin
plugins {
    application
    alias(libs.plugins.shadow)
}

dependencies {
    implementation(project(":framework"))
    implementation(project(":runner"))
    implementation(project(":reports"))
    implementation(libs.slf4j.api)
    implementation(libs.snakeyaml)
    runtimeOnly(libs.logback.classic)

    // Outillage d'évaluation : shadé dans l'uber-jar, invoqué en sous-processus
    // par les Checkers (l'uber-jar EST le classpath). Présent aussi au classpath
    // de test via testRuntimeClasspath (fallback java.class.path de JavaToolkit).
    runtimeOnly(libs.junit.jupiter)
    runtimeOnly(libs.junit.platform.console)
    runtimeOnly(libs.assertj.core)
    runtimeOnly(libs.checkstyle)

    testImplementation(libs.junit.jupiter)
    testImplementation(libs.assertj.core)
    testRuntimeOnly(libs.junit.platform.launcher)
}

application {
    mainClass = "etnc.piscine.moulinette.console.Main"
}

// Suite par défaut : exclut les tags lourds (équivalent surefire.excludedGroups=git,e2e,tools)
tasks.test {
    useJUnitPlatform {
        excludeTags("git", "e2e", "tools")
    }
}

tasks.shadowJar {
    archiveFileName = "moulinette-console.jar"
    // Exclut les fichiers de signature des dépendances shadées (checkstyle & transitives
    // sont signées) : sinon le manifeste fusionné déclenche "Invalid signature file digest".
    exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
    mergeServiceFiles()
}
```

- [ ] **Step 2: Vérifier la suite unitaire console**

Run: `cd moulinette && ./gradlew :console:test`
Expected: BUILD SUCCESSFUL, même nombre de tests que `mvn -pl console -am test` (comparer les comptes dans la sortie).

- [ ] **Step 3: Construire et tester l'uber-jar à la main**

Run: `./gradlew :console:shadowJar`
Expected: `moulinette/console/build/libs/moulinette-console.jar`.
Vérifications manuelles :
```powershell
java -jar moulinette/console/build/libs/moulinette-console.jar   # la console démarre (quitter avec exit)
# le jar embarque bien l'outillage :
jar -tf moulinette/console/build/libs/moulinette-console.jar | Select-String "ConsoleLauncher|puppycrawl" | Select-Object -First 2
```
Expected: la console démarre sans `Invalid signature file digest`, et les deux classes outillage sont présentes.

- [ ] **Step 4: Commit**

```bash
git add moulinette/console/build.gradle.kts
git commit -m "build(gradle): module console + shadowJar (uber-jar iso maven-shade)"
```

---

### Task 6: Suites taguées git / tools / e2e

**Files:**
- Modify: `moulinette/console/build.gradle.kts` (ajout en fin de fichier)

- [ ] **Step 1: Ajouter les trois tâches `Test` dédiées**

```kotlin
// Suites lourdes à la demande (équivalent mvn -Dgroups=git|tools|e2e)
listOf("git" to "testGit", "tools" to "testTools", "e2e" to "testE2e").forEach { (tag, taskName) ->
    tasks.register<Test>(taskName) {
        description = "Suite taguée '$tag'"
        group = "verification"
        testClassesDirs = sourceSets.test.get().output.classesDirs
        classpath = sourceSets.test.get().runtimeClasspath
        useJUnitPlatform { includeTags(tag) }
    }
}
```

- [ ] **Step 2: Lancer les trois suites (c'est LE test de non-régression de la migration)**

Run (depuis `moulinette/`) :
```bash
./gradlew :console:testGit
./gradlew :console:testTools
./gradlew :console:testE2e
```
Expected: BUILD SUCCESSFUL ×3, mêmes comptes de tests que les commandes Maven équivalentes. **Point de vigilance `testTools`** : ces tests exercent `JavaToolkit.toolingClasspath()` → fallback `java.class.path` ; s'ils échouent à trouver JUnit/Checkstyle, c'est que le classpath de test Gradle ne les contient pas — diagnostiquer avec `println(System.getProperty("java.class.path"))` dans un test jetable, et au besoin passer les libs outillage de `runtimeOnly` à `implementation`.

- [ ] **Step 3: Commit**

```bash
git add moulinette/console/build.gradle.kts
git commit -m "build(gradle): suites taguees testGit/testTools/testE2e"
```

---

### Task 7: CI GitHub Actions

**Files:**
- Modify: `.github/workflows/ci.yml:15-50` (job `moulinette` uniquement ; jobs `lint-exercices`, `build-docusaurus`, `valider-solutions` inchangés)

- [ ] **Step 1: Remplacer le job `moulinette`**

```yaml
  moulinette:
    name: Moulinette (build + tests)
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      - name: Set up JDK 25 (Temurin)
        uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: '25'

      - name: Set up Gradle
        uses: gradle/actions/setup-gradle@v4

      - name: Configurer l'identité git (suite @Tag("git"))
        run: |
          git config --global user.email "ci@etnc.local"
          git config --global user.name "CI ETNC"

      - name: Build + tests unitaires + uber-jar
        run: ./gradlew build shadowJar
        working-directory: moulinette

      - name: Suite d'intégration git
        run: ./gradlew :console:testGit
        working-directory: moulinette

      - name: Suite outillage (compile/test/style réels)
        run: ./gradlew :console:testTools
        working-directory: moulinette

      - name: Suite bout en bout
        run: ./gradlew :console:testE2e
        working-directory: moulinette
```
(Supprimer `cache: maven` ; `gradle/actions/setup-gradle` gère le cache Gradle.)

- [ ] **Step 2: Commit + push de la branche et vérifier le CI**

```bash
git add .github/workflows/ci.yml
git commit -m "ci: job moulinette sous Gradle (4 suites)"
git push -u origin <branche>
gh pr checks --watch   # une fois la PR ouverte
```
Expected: les 4 jobs verts (moulinette, lint-exercices, build-docusaurus, valider-solutions).

---

### Task 8: Scripts et docs

**Files:**
- Modify: `scripts/build-bundle.sh:27` (et `scripts/build-bundle.ps1` équivalent)
- Modify: `scripts/setup-dev.sh:117-118` (et `.ps1`)
- Modify: `README.md:69-72`, `CONTRIBUTING.md:34`, `moulinette/README.md`, `docs/architecture-moulinette.md`, `docs/setup-dev.md`

- [ ] **Step 1: `build-bundle.sh`** — remplacer la détection Maven et l'invocation du build par :

```bash
# Build de l'uber-jar via le wrapper Gradle (toujours présent, versionné)
"$REPO_ROOT/moulinette/gradlew" -p "$REPO_ROOT/moulinette" :console:shadowJar
```
et adapter le chemin du jar produit : `moulinette/console/build/libs/moulinette-console.jar` (au lieu de `target/`). Faire l'équivalent dans `build-bundle.ps1`.

- [ ] **Step 2: `setup-dev.sh` / `.ps1`** — remplacer la vérification `mvnw -v` par `"$REPO_ROOT/moulinette/gradlew" -p "$REPO_ROOT/moulinette" -v` (et retirer le `chmod +x mvnw`, ajouter `chmod +x moulinette/gradlew`).

- [ ] **Step 3: Docs** — dans `README.md`, `CONTRIBUTING.md`, `moulinette/README.md`, `docs/setup-dev.md`, `docs/architecture-moulinette.md` : remplacer chaque commande `mvn`/`./mvnw` du build *moulinette* par son équivalent Gradle :

| Avant | Après |
|---|---|
| `mvn -f moulinette/pom.xml verify` | `moulinette/gradlew -p moulinette build` |
| `mvn -pl framework test` | `moulinette/gradlew -p moulinette :framework:test` |
| `mvn -pl console -am test -Dgroups=git ...` | `moulinette/gradlew -p moulinette :console:testGit` |
| `mvn -pl cli exec:java ...` | `moulinette/gradlew -p moulinette :cli:run --args="..."` |
| `mvn package` (uber-jar) | `moulinette/gradlew -p moulinette :console:shadowJar` |

**Attention** : ne PAS toucher aux mentions `mvn test` concernant les **exercices** (`CONTRIBUTING.md:48`, `scripts/valider-solutions.sh`, `docs/format-exercice.md`) — elles restent Maven. Mettre à jour le tableau des suites de tests dans `docs/architecture-moulinette.md` (§Tests) avec les nouvelles commandes.

- [ ] **Step 4: Vérifier le bundle**

Run: `bash scripts/build-bundle.sh`
Expected: le bundle se construit comme avant (jar présent, lanceurs OK).

- [ ] **Step 5: Commit**

```bash
git add scripts/ README.md CONTRIBUTING.md moulinette/README.md docs/
git commit -m "build(gradle): scripts et docs migres (exercices restent Maven)"
```

---

### Task 9: Retrait de Maven (moulinette uniquement) + vérification finale

**Files:**
- Delete: `moulinette/pom.xml`, `moulinette/*/pom.xml` (5 fichiers), `mvnw`, `mvnw.cmd`, `.mvn/`
- Modify: `.gitignore` (retirer la ligne `!.mvn/wrapper/maven-wrapper.jar` ; **garder** `target/` car les poms d'exercices produisent toujours des `target/`)

- [ ] **Step 1: Supprimer les fichiers Maven du build moulinette**

```bash
git rm moulinette/pom.xml moulinette/framework/pom.xml moulinette/runner/pom.xml \
       moulinette/reports/pom.xml moulinette/cli/pom.xml moulinette/console/pom.xml \
       mvnw mvnw.cmd
git rm -r .mvn
```
(`mvnw` racine ne servait qu'au build moulinette ; les exercices utilisent le `mvn` du poste, comme documenté.)

- [ ] **Step 2: Vérification complète des 4 suites + jar (depuis un état propre)**

```bash
cd moulinette
./gradlew clean build shadowJar :console:testGit :console:testTools :console:testE2e
```
Expected: BUILD SUCCESSFUL, uber-jar présent, comptes de tests identiques aux exécutions Maven de référence (relever les comptes AVANT la Task 1 : lancer les 4 commandes Maven du tableau de `docs/architecture-moulinette.md` et noter les totaux — c'est la baseline).

- [ ] **Step 3: Grep de contrôle**

Run: `grep -rn "mvnw\|maven" README.md CONTRIBUTING.md docs/ scripts/ moulinette/ --include=*.md --include=*.sh --include=*.ps1 | grep -vi "exercice\|solution\|starter\|format-exercice\|valider-solutions"`
Expected: aucune référence résiduelle au build Maven de la moulinette.

- [ ] **Step 4: Commit final + CHANGELOG**

Ajouter au `CHANGELOG.md` (section Unreleased) : `- Build moulinette migré de Maven vers Gradle (wrapper versionné ; les exercices restent en Maven)`.
```bash
git add -A
git commit -m "build(gradle)!: retrait de Maven pour la moulinette (exos inchanges)"
```

---

## Baseline (à relever AVANT Task 1)

Lancer et noter les comptes de tests des 4 suites Maven actuelles — ils servent de référence d'iso-fonctionnalité aux Tasks 5, 6 et 9 :
```bash
mvn -B -f moulinette/pom.xml verify
mvn -B -f moulinette/pom.xml -pl console -am test -Dsurefire.excludedGroups=e2e,tools -Dgroups=git
mvn -B -f moulinette/pom.xml -pl console -am test -Dsurefire.excludedGroups=git,e2e -Dgroups=tools -Dsurefire.failIfNoSpecifiedTests=false
mvn -B -f moulinette/pom.xml -pl console -am test -Dsurefire.excludedGroups=git,tools -Dgroups=e2e
```
