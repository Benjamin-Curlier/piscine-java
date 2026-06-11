# Module gui : serveur + terminal web (v1 chantier 3) — Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Nouveau module Gradle `gui` : serveur HTTP local (`com.sun.net.httpserver`, JDK) exposant `ConsoleSession` en JSON + un faux terminal web (xterm.js vendoré). Critère : parcours complet d'un exo (init → submit-start → add/commit/push → rapport) via le navigateur.

**Architecture:** `gui` dépend de `console` et ne réimplémente rien. `GuiServer.start(session, port)` enregistre : `GET /` (statiques depuis le classpath `/web`), `POST /api/terminal` (`{"cmd":"..."}` → `CommandResult` en JSON), `GET /api/state` (branche courante, repo). JSON écrit à la main (même politique que `ReportGenerator`, pas de Jackson). Le `Main` de `gui` reprend les args de la console (`--repo`, `--piscine-repo`), démarre sur un port éphémère (`port 0`), ouvre le navigateur. Frontend : `index.html` + `app.js` + xterm.js/xterm.css vendorés — le prompt est rendu côté client, chaque ligne validée part en POST, la réponse s'affiche.

**Déviation vs spec (assumée, YAGNI) :** pas de SSE dans ce chantier — l'évaluation est synchrone et son rapport revient dans la réponse du push. Le streaming de progression sera ajouté quand le tableau de bord (chantier 4) en aura l'usage.

**Tech Stack:** Java 25 (`jdk.httpserver`, `java.net.http.HttpClient` pour les tests), xterm.js 5.x vendoré, Gradle.

---

### Task 1: Module `gui` (squelette Gradle)

**Files:**
- Modify: `moulinette/settings.gradle.kts` (ajouter `"gui"`)
- Create: `moulinette/gui/build.gradle.kts`

```kotlin
plugins {
    application
}

dependencies {
    implementation(project(":console"))
    implementation(project(":framework"))
    implementation(libs.slf4j.api)
    runtimeOnly(libs.logback.classic)

    testImplementation(libs.junit.jupiter)
    testImplementation(libs.assertj.core)
    testRuntimeOnly(libs.junit.platform.launcher)
}

application {
    mainClass = "etnc.piscine.moulinette.gui.Main"
}
```

Steps : modifier settings → `./gradlew projects` liste `:gui` → commit `build(gui): module gui (squelette)`.

### Task 2: JSON minimal + GuiServer (TDD)

**Files:**
- Create: `moulinette/gui/src/main/java/etnc/piscine/moulinette/gui/Json.java` (échappement + extraction de champ — utilitaire volontairement minimal)
- Create: `moulinette/gui/src/main/java/etnc/piscine/moulinette/gui/GuiServer.java`
- Test: `moulinette/gui/src/test/java/etnc/piscine/moulinette/gui/GuiServerTest.java`

Comportement testé (serveur démarré sur port 0 avec une `ConsoleSession.of(ctx FakeGit, registry)` — note : `FakeGitClient` est dans les tests de `console`, donc le test gui utilise un `GitClient` anonyme inline) :
- `POST /api/terminal` body `{"cmd":"help"}` → 200, JSON avec `"exitCode":0` et `Commandes supportées` dans `output`.
- `POST /api/terminal` body `{"cmd":"git checkout x"}` → 200, `"exitCode":1`.
- `GET /api/state` → 200, `{"branch":"main", ...}`.
- `GET /` → 200, contient `<title>`.

`GuiServer` : `HttpServer.create(loopback:port, 0)`, contexts `/`, `/api/terminal`, `/api/state` ; réponses UTF-8 ; `port()` expose le port réel ; `stop()`.

Steps : test rouge → implémentation → vert → commit `feat(gui): GuiServer, API terminal/state`.

### Task 3: Frontend statique (xterm vendoré)

**Files:**
- Create: `moulinette/gui/src/main/resources/web/index.html`, `app.js`, `style.css`
- Create: `moulinette/gui/src/main/resources/web/vendor/xterm.min.js`, `vendor/xterm.min.css` (téléchargés depuis jsDelivr `@xterm/xterm@5`, commit du fichier — AUCUN appel réseau à l'exécution)

`app.js` : initialise xterm, affiche bannière + prompt `piscine[<branch>]> `, bufferise la ligne au clavier (gestion Enter/Backspace), POST `/api/terminal`, écrit `output` (conversion `\n` → `\r\n`), rafraîchit la branche via `/api/state`, désactive l'entrée si `shouldExit`.

Steps : fichiers → vérif manuelle (`:gui:run` + navigateur) → commit `feat(gui): terminal web xterm.js vendore`.

### Task 4: Main gui + vérification e2e manuelle

**Files:**
- Create: `moulinette/gui/src/main/java/etnc/piscine/moulinette/gui/Main.java`

`Main` : args `--repo` (requis), `--piscine-repo` (défaut : `../piscine-etnc` comme la console) ; `ConsoleSession.open(...)` ; `GuiServer.start(session, 0)` ; affiche l'URL ; ouvre le navigateur (réutiliser la logique `tryOpenBrowser` de la console — la **déplacer** dans un util partagé `console` exporté, ou dupliquer 15 lignes : dupliquer, c'est plus simple et le code bouge au chantier 6) ; reste en vie jusqu'à Ctrl-C (`Thread.currentThread().join()`).

Vérification (critère du chantier) : workspace de test init via la console, puis `:gui:run --args="--repo <ws> --piscine-repo <repo>"` ; dans le navigateur : `submit-start 1.1` → éditer le fichier → `git add .` → `git commit` → `git push origin rendu/1.1` → le rapport moulinette s'affiche dans le terminal web.

Steps : Main → vérif manuelle complète → suites `build testGit testTools testE2e` toutes vertes → docs (`architecture-moulinette.md` : module `gui` dans le tableau + point d'entrée) + CHANGELOG → commit `feat(gui): Main + docs`.
