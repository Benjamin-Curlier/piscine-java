# Changelog — Piscine ETNC

Format inspiré de [Keep a Changelog](https://keepachangelog.com/fr/). Versions en [SemVer](https://semver.org/lang/fr/).

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

### Known issues / à finaliser avant GA
- **Validation du bundle sur machine Windows propre** : le *chemin de notation* est validé
  automatiquement (E2E), mais l'**empaquetage** (MinGit/JDK/`.bat`, `PATH`) reste à fumer sur une
  Windows vierge (run formateur) — dernier gate avant de figer la RC.
- **Style en mode advisory** (non bloquant) — choix beta ; durcissement prévu (#53).
- **Pas de note pondérée /20** : verdict binaire pass/fail par checker (la pondération
  `evaluation.yml` n'est pas encore consommée par le runtime).
- **À venir (Phase 4)** : isolation Docker du runner (#30), PMD/SpotBugs, anti-triche JPlag (#29),
  GitLab CE on-premise (#26), branding ETNC du site (#33).

[0.6.0-rc.1]: https://github.com/Benjamin-Curlier/Piscine-ETNC
