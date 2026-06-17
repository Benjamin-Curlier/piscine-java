# Changelog — Piscine Java

Format inspiré de [Keep a Changelog](https://keepachangelog.com/fr/). Versions en [SemVer](https://semver.org/lang/fr/).

## [Unreleased]

## [1.0.0-rc.1] — 2026-06-17

Première **release candidate** de la v1, issue de la remédiation d'un audit qualité
(expérience apprenant / couverture pédagogique / bonnes pratiques).

### Expérience apprenant
- **Démarrage corrigé** : la doc « standalone » pointait vers un `piscine.bat`/ZIP retiré en 0.7.0 ;
  remplacée par le parcours **installeur jpackage** (un apprenant autonome était bloqué dès l'étape 1).
- **Gamification rendue visible** : le retour d'un `submit` annonce désormais l'XP gagnée, les montées
  de niveau et les badges débloqués ; le tableau de bord affiche barre d'XP + badges (nouvel endpoint
  `/api/profil`) ; la bannière du REPL met en avant `submit` et `profil`.
- Docs alignées sur le comportement réel (évaluation de **tout** le sous-groupe, plus d'« arrêt au
  premier échec » ; commandes `submit`/`profil` documentées).

### Contenu pédagogique
- **Java 25 « hybride »** : introduction de `void main()` / `IO.println` (JEP 512) **à côté** du `main`
  classique ; idiomes modernisés (`var`, `Stream.toList()`, text blocks).
- **Bugs de cours corrigés** (dont des exemples qui ne compilaient pas : génériques 4-5, dates 5-8),
  marqueurs de conflit Git, immuabilité des `record`, `%` sur `double`, démo `==` sur chaînes.
- **Lacunes comblées** : contrat `equals`/`hashCode` complet, piège `List.of()` immuable, virtual threads
  (JEP 444), test doubles/mocking, `git fetch`/`pull`/`merge --abort`/`restore`, `finally`+`return`,
  inexactitude des flottants, locale de `Scanner`, varargs, utilitaires `Arrays`, `strip()`/`trim()`,
  `toString()` manuel, chapitre **dates/heures reconstruit**.
- **Nouvel exercice** `3.1.4-egalite-carte` (`equals`/`hashCode` + cohérence `HashSet`).

### Qualité / outillage
- Nouveau garde-fou CI **`scripts/check-lesson-snippets.py`** (job `exemples-cours`) : compile les blocs
  de cours marqués ` ```java compile ` pour empêcher tout exemple non compilable de passer.

## [0.7.0] — 2026-06-16

GUI de bureau (tableau de bord + terminal git intégré) packagée en installeurs jpackage, et
**rebranding complet** « Piscine ETNC » → **« Piscine Java »** : plateforme générique
d'apprentissage du Java pour l'onboarding de nouveaux développeurs, dépôt **public** sous licence **MIT**.

### Identité & licence
- **Rebranding** « Piscine ETNC » → **« Piscine Java »** : suppression de toute référence à l'ETNC
  et de l'habillage thématique militaire (cours et exercices réécrits en contexte d'équipe logicielle —
  `Soldat` → `Membre`, grades militaires → niveaux de séniorité, etc.). Namespace Java
  **`etnc.*` → `piscine.*`** (paquets, dossiers, coordonnées Maven/Gradle, ressources).
- **Licence MIT** (remplace l'en-tête provisoire « tous droits réservés / usage interne »).
- Retrait des artefacts internes de développement (`docs/superpowers/`, `.claude/`).

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

### Distribution
- **Installeur jpackage** « Piscine Java » : `.exe` Windows **par utilisateur** (sans admin,
  JRE + MinGit + exercices + site de cours embarqués, répertoire modifiable), `.deb` et
  app-image portable Linux — workflow CI **Release** sur tag `v*`. Mode zéro-argument de la
  GUI : workspace auto-initialisé dans `~/PiscineJava` (env `PISCINE_HOME`), données hors du
  répertoire d'installation. Remplace le bundle ZIP (`build-bundle` retiré).
- Résolution git explicite : propriété `piscine.git` (MinGit embarqué) → env
  `PISCINE_GIT_HOME` → `git` du PATH.
- **App invisible avec icône de zone de notification** : l'exe jpackage n'a aucune fenêtre
  console ; un tray « P » (Ouvrir / Quitter, double-clic = ouvrir) est le point de contrôle.

### Retiré (v1 : piscine 100 % locale)
- **Mode `nominal`** (plateforme serveur) et option `--mode` : abandonnés, l'enum `Mode` ne
  garde que `LOCAL`. Plus aucune mention de GitLab/forge distante/notation externe dans le
  parcours stagiaire (cours, README).

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
  GitLab CE on-premise (#26), branding Piscine Java du site (#33).

[0.7.0]: https://github.com/Benjamin-Curlier/piscine-java/releases/tag/v0.7.0
[0.6.0-rc.1]: https://github.com/Benjamin-Curlier/piscine-java
