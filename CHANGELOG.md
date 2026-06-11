# Changelog — Piscine ETNC

Format inspiré de [Keep a Changelog](https://keepachangelog.com/fr/). Versions en [SemVer](https://semver.org/lang/fr/).

## [Unreleased]

### Build
- Build de la moulinette migré de **Maven** vers **Gradle 9.5.1** (wrapper versionné
  `moulinette/gradlew`, uber-jar via plugin Shadow, suites taguées `testGit`/`testTools`/`testE2e`).
  Les projets des **exercices restent en Maven** (`./mvnw` conservé à la racine).

### Moulinette
- Logique console extraite en bibliothèque (**`ConsoleSession`**) — le REPL terminal devient
  un client de la façade ; prépare la GUI v1.
- Nouveau module **`gui`** : serveur HTTP local (JDK, lié à 127.0.0.1, port éphémère ou `--port`)
  + terminal web **xterm.js vendoré** (zéro réseau à l'exécution). Uber-jar `moulinette-gui.jar`
  (l'uber-jar reste le classpath d'outillage des checkers).
- **Tableau de bord** GUI : progression par module/sous-groupe/exo (✓/●/🔒, séquentiel),
  historique des rapports moulinette rendus en markdown (marked.js vendoré), boutons
  « Explorateur » / « Terminal système », site de cours Docusaurus monté sous `/cours/` (`--site`).
- La moulinette **persiste la progression** dans `<workspace>/.piscine/progress.json`
  (un exo validé ne redescend jamais).

## [0.6.0-rc.1] — Release Candidate (2026-06-08)

Première **release candidate** : tout le contenu pédagogique des 6 modules est livré, la
moulinette est évaluante, et le mode standalone hors-ligne est en place.

### Contenu pédagogique
- **6 modules de cours** complets (Docusaurus) : Fondamentaux, Tableaux/Chaînes/Méthodes, POO,
  Collections/Génériques/Lambdas, Exceptions/I/O, Tests/Git.
- **65 exercices** individuels (10 + 12 + 14 + 12 + 10 + 7) au format standardisé.
- **3 projets binôme** : mini-domaine OO, persistance fichier (TaskManager CLI), mini-moulinette
  pédagogique (projet final, avec exigences tests JaCoCo ≥ 70 % + workflow Git branches/PR).

### Moulinette
- Notation **hors-ligne** : `javac` + JUnit ConsoleLauncher embarqués dans l'uber-jar (zéro Maven,
  zéro réseau à l'exécution).
- Checkers : compilation, tests publics, tests privés, style (Checkstyle, *advisory*).
- **`MutationChecker`** (nouveau) : grade les exos « écriture de tests » (module 6.1) — les tests
  du stagiaire doivent passer sur l'impl correcte et **tuer** des mutants cachés. Sélection des
  checkers par exo via `Checker.appliesTo`.
- Exos Git (module 6.2) : notation par **état d'un dépôt-jouet** (le stagiaire pilote git depuis
  Java via un helper fourni).

### Mode standalone (cible de cette RC)
- ZIP autonome Windows : JDK 25 portable, MinGit épinglé (SHA-256 vérifié), moulinette
  pré-compilée, site de cours servi en local. Aucune installation, aucun réseau.
- Produit par `scripts/build-bundle.ps1` / `.sh`.

### Filets de sécurité
- CI GitHub Actions : build moulinette + suites taguées, `valider-solutions` (la solution de
  référence de chaque exo passe ses tests **et** — pour les exos à mutants — les tue), lint des
  exercices, build Docusaurus.
- E2E `BundleGradingE2EIT` : valide le **chemin de notation console** (celui du bundle) sur de
  vrais exos M4-M6, dont le `MutationChecker` (6.1) et les tests forkant git (6.2).

### Validation du bundle standalone (2026-06-08)
Le bundle a été **assemblé et exécuté de bout en bout** sur une machine Windows (build-bundle :
JDK 25 portable + **MinGit 2.54.0 SHA-256 vérifié** + uber-jar + site + 65 exos ; ZIP de ~200 Mo
produit). Depuis le **bundle assemblé** (toolchain embarquée), un `init` puis un rendu réel ont
**noté ✓ OK les 7 exercices du module 6** : 6.1 (4/4, via le `MutationChecker` du jar) et 6.2 (3/3,
tests forkant `git`). Correctif au passage : `build-bundle.sh` retombe sur `jar` du JDK quand `zip`
est absent (fréquent sous Git-Bash Windows).

### Known issues / à finaliser avant GA
- **Dernier smoke recommandé** : décompresser le ZIP et lancer **`piscine.bat`** sur une machine
  Windows *vierge* (sans Java/git) — le runtime et l'empaquetage sont validés ; ne reste que la
  double-cliquabilité du `.bat` sur poste neuf.
- **Style en mode advisory** (non bloquant) — choix beta ; durcissement prévu (#53).
- **Pas de note pondérée /20** : verdict binaire pass/fail par checker (la pondération
  `evaluation.yml` n'est pas encore consommée par le runtime).
- **À venir (Phase 4)** : isolation Docker du runner (#30), PMD/SpotBugs, anti-triche JPlag (#29),
  GitLab CE on-premise (#26), branding ETNC du site (#33).

[0.6.0-rc.1]: https://github.com/Benjamin-Curlier/Piscine-ETNC
