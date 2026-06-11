# Design — Release v1 : GUI locale, auto-rythmée, sans notation distante

Date : 2026-06-11
Statut : validé en brainstorming (formateur)

## Objectif

Faire de la Piscine ETNC une application autonome installée sur le poste du
stagiaire : plus aucune notion de GitLab externe ni de notation distante.
Le stagiaire avance à son rythme (sans date limite), la moulinette locale
reste l'arbitre technique, et l'interface passe d'une CLI à une GUI.

## Décisions de cadrage (validées)

| Sujet | Décision |
|---|---|
| Forme de la GUI | **Appli web locale** : l'uber-jar embarque un serveur HTTP, le stagiaire utilise son navigateur sur `127.0.0.1:<port aléatoire>`. |
| Rituel git | **Conservé** : le remote simulé local reste, `git push origin rendu/X` déclenche l'évaluation. Seule la notation externe disparaît. |
| Auto-notation | **Tableau de bord de progression pur** : la moulinette donne le verdict (pass/fail), la GUI affiche la progression. Rien ne sort de la machine. |
| Rythme | **Déverrouillage séquentiel** (comme aujourd'hui) : exo N validé avant N+1, module fini avant le suivant. Le « à son rythme » porte sur le temps, pas l'ordre. |
| Runtime | **JRE embarqué** via `jpackage` (+ `jlink`), installeur par-utilisateur sans droits admin. |
| Git | **MinGit embarqué** dans l'installeur Windows ; git système sur Linux (avec détection et message clair). |
| Périmètre GUI | **Les trois blocs** : terminal git sandbox + lecteur de cours + tableau de bord. Portail unique. |
| Build | **Migration Maven → Gradle**, en chantier préliminaire isolé (repoussable si elle déraille). |
| Module `cli` | Conservé pour la CI interne du repo (valider-solutions) ; **jamais packagé ni documenté côté stagiaire**. |
| Packaging | **Installeur** (plus de zip) : Windows `.exe` per-user, Linux `.deb` + tar.gz autoportant (bonus). |
| Répertoires | Installation : `%LOCALAPPDATA%` par défaut, **modifiable** à l'installation (`--win-dir-chooser`). Données stagiaire : `~/PiscineETNC` par défaut, configurable. |

## Approche technique retenue

Serveur `com.sun.net.httpserver` (JDK, zéro dépendance) servant des ressources
statiques depuis le jar. Frontend vanilla **sans chaîne de build JS** :
bibliothèques vendorées dans les ressources — `xterm.js` (terminal),
`marked.js` (rendu markdown), `highlight.js` (coloration Java).
Communication : requête/réponse JSON pour le REPL (ligne-à-ligne),
**SSE** (`/api/events`) pour streamer la progression d'évaluation.

Approches écartées : Javalin + frontend buildé (introduit node/npm dans un
repo 100 % JVM) ; JavaFX (hors JDK, itérations lentes) — plan B si l'option
web rencontrait un mur.

## Architecture

```
moulinette/ (Gradle multi-module)
  framework/   inchangé (Checker, CheckResult, EvaluationReport)
  runner/      inchangé (ProcessRunner)
  reports/     inchangé (ReportGenerator md+json)
  cli/         conservé, interne CI uniquement
  console/     la logique REPL (commandes git, SubmissionTrigger,
               MoulinetteRunner) devient une bibliothèque appelable ;
               le REPL terminal devient un simple client (mode legacy)
  gui/         NOUVEAU : serveur HTTP, API JSON + SSE, statiques vendorés
```

Principes :
- **`gui` ne réimplémente rien** : il expose par HTTP ce que `console` sait
  faire. `POST /api/terminal {"cmd": "git push origin rendu/1.1"}` exécute la
  même chaîne `PushCommand → SubmissionTrigger → MoulinetteRunner`.
- Serveur lié à `127.0.0.1` uniquement, port aléatoire libre, ouverture du
  navigateur au lancement (`Desktop.browse`).
- **GUI sans état propre** : workspace, `.piscine/reports/` et progression
  vivent sur disque comme aujourd'hui ; la GUI relit le disque.
- Résolution de git : MinGit embarqué → `PISCINE_GIT_HOME` → git du PATH.

## Écrans (app monopage, barre latérale à 3 entrées)

### 1. Tableau de bord (accueil)
- Carte par module : progression (exos validés / total), verrouillé ou non.
- Module courant : sous-groupes et exos avec statut (validé / en cours /
  verrouillé) ; seul l'exo courant est tentable (séquentiel).
- Historique des rapports d'évaluation (relit `.piscine/reports/`), rendu
  markdown du rapport dans l'UI.
- Par exo en cours, deux boutons : **« Ouvrir dans l'explorateur »**
  (`explorer.exe` / `xdg-open` sur le workspace) et **« Ouvrir un terminal
  ici »** (PowerShell / terminal par défaut), exécutés via `ProcessRunner`.

### 2. Cours
- Navigation module → chapitre, rendu markdown + coloration Java.
- Chapitres lisibles dès que le module est déverrouillé, pas avant.

### 3. Terminal
- Faux terminal xterm.js, strictement équivalent au REPL actuel (mêmes
  commandes, mêmes messages). Après un push réussi : évaluation streamée
  dans le terminal + notification UI vers le rapport.
- Encart permanent : chemin du workspace de l'exo courant + les deux boutons
  explorateur/terminal.

Pas de compte/login : une installation = un stagiaire ; la progression est
l'état du disque.

## Installeur & packaging

- `jpackage` piloté par une tâche Gradle.
- **Windows** : `.exe` per-user (sans admin), `%LOCALAPPDATA%` par défaut,
  répertoire modifiable. Contenu : runtime `jlink`, uber-jar gui, MinGit,
  cours + exercices. Raccourci menu Démarrer « Piscine ETNC ».
- **Linux (bonus)** : `.deb` jpackage + tar.gz autoportant (app-image) comme
  voie universelle.
- Données stagiaire hors répertoire d'installation (`~/PiscineETNC`,
  configurable) : une mise à jour ne touche jamais au travail du stagiaire.
- Au lancement : démarrage serveur + ouverture navigateur ; un moyen visible
  d'arrêter l'app.
- `build-bundle.sh` et `piscine.bat` (zip actuel) retirés ; le CI GitHub
  produit les artefacts Windows + Linux.

## Nettoyage du legacy distant

- Purge de toute mention GitLab / remote externe / notation distante des
  docs et messages stagiaire.
- Metadata binôme et push « réel » retirés du parcours (les projets binôme
  restent, rendus localement) — résout au passage les 2 bugs moulinette
  connus liés à la metadata binôme (tests-prives optionnel bloquant,
  metadata binôme ignorée).
- `cli` marqué interne (non packagé, non documenté côté stagiaire).

## Chantiers (PR séquentielles, app fonctionnelle entre deux)

1. **Migration Gradle** — iso-fonctionnelle : 6 modules, plugin Shadow
   (avec exclusion des fichiers de signature, comme maven-shade), suites de
   tests taguées (`git`/`tools`/`e2e`) en tâches `Test`, CI adapté.
   Critère : les 4 suites passent comme avant.
2. **Extraction console → bibliothèque** — la logique REPL devient une API ;
   le REPL terminal en devient un client. Critère : tests `git`/`e2e`
   inchangés et verts.
3. **Module `gui` : serveur + terminal web** — httpserver, `/api/terminal`,
   SSE, xterm.js. Critère : parcours complet d'un exo (clone → code → push →
   rapport) via le navigateur.
4. **Cours + tableau de bord** — rendu markdown, progression, rapports,
   boutons explorateur/terminal.
5. **Nettoyage legacy distant** — purge docs/code, `cli` interne.
6. **Installeur jpackage** — Windows .exe per-user + MinGit, Linux .deb +
   tar.gz, CI de release.

## Tests

- Discipline actuelle conservée (suites taguées) sur chaque chantier.
- `gui` : tests d'API HTTP contre le serveur démarré sur port aléatoire.
- Frontend vanilla : pas de framework de test JS ; le parcours e2e existant
  est étendu par un test pilotant l'API comme le ferait le navigateur.

## Hors périmètre v1 (backlog conservé)

- Anti-triche tests-prives (#29), isolation Docker (#30), durcissement
  StyleChecker (#53), note pondérée /20.
