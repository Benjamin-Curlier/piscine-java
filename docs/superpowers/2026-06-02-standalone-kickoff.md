# #56 — Mode STANDALONE (ZIP autonome stagiaire) : brief de démarrage

> **But de ce document** : permettre à une **nouvelle session** de lancer l'itération « mode standalone » sans re-découvrir le contexte. Ce n'est PAS une spec : c'est le point de départ du cycle habituel **brainstorm → spec → plan → exécution** (exécution **inline**, pas de sous-agents — contrainte projet, voir mémoire).
>
> Lire d'abord : ce brief, [`scripts/build-bundle.sh`](../../scripts/build-bundle.sh) / [`.ps1`](../../scripts/build-bundle.ps1), [`docs/piscine-stagiaire.md`](../piscine-stagiaire.md), [`docs/setup-dev.md`](../setup-dev.md), et la façade d'outillage [`moulinette/console/.../checkers/JavaToolkit.java`](../../moulinette/console/src/main/java/etnc/piscine/moulinette/console/checkers/JavaToolkit.java).

## 0. Où on en est (au 2026-06-02)

- **Module 2 terminé** (PR #18→#21 mergées). Prochain module pédagogique = **#19 (module 3)**, mais cette itération **#56 passe avant** (à la demande du formateur).
- **La piscine a deux modes d'utilisation** (à formaliser dans la doc — voir §6) :
  - **Mode dépôt** (existant, documenté) : on clone le repo, on installe Java/git, `piscine-bootstrap` compile la console depuis les sources et crée le workspace. Public : formateurs, contributeurs, stagiaires « connectés ».
  - **Mode standalone** (objet de cette itération) : un **ZIP autonome et hors-ligne** que le stagiaire décompresse et lance sans rien installer.
- **Acquis réutilisables (déjà en place)** :
  - `scripts/build-bundle.sh` / `.ps1` produit déjà un ZIP `piscine-etnc-stagiaire` : uber-jar `moulinette-console.jar` (commandes `init` / `repl`), `exercises/`, **JDK portable**, launcher `piscine.sh`, `LISEZMOI.txt`.
  - **La moulinette grade SANS Maven** : `JavaToolkit` lance `javac` puis le **JUnit `ConsoleLauncher`** avec l'uber-jar comme classpath (JUnit/AssertJ/Checkstyle **shadés** dedans). → la boucle de feedback est **déjà offline**, sans `.m2` ni Maven.
  - `courses/build/` : **site statique Docusaurus déjà généré** (régénérable par `npm run build`).

## 1. 🔒 Décisions DÉJÀ tranchées (ne pas re-brainstormer)

Issues du brainstorm du 2026-06-02. **À appliquer telles quelles** :

1. **Site de cours hors-ligne** : servir le **build statique** `courses/build/` via le **serveur HTTP intégré au JDK embarqué** (`jwebserver`, JDK 18+) sur `localhost`. **Zéro Node au runtime stagiaire.** (Le `npm run build` se fait côté **formateur**, au moment de construire le bundle — pas chez le stagiaire.)
2. **Maven / `.m2` : HORS SCOPE v1.** La console moulinette (javac + JUnit `ConsoleLauncher`, offline via l'uber-jar) **EST** la boucle de feedback. Pas de Maven ni de dépôt `.m2` dans le ZIP.
3. **Windows d'abord** : launchers `.ps1` / `.bat` + **JDK portable Windows**. Le launcher bash (`piscine.sh`) reste pour le formateur / Unix.
4. **git portable Windows embarqué** : le rendu est git-based (`ProcessGitClient` shelle vers `git`). On embarque un git portable pour que le ZIP fonctionne sur une machine **sans git ni droits admin**.

## 2. Périmètre de l'itération (ce qu'il faut construire)

### A. Service du site de cours hors-ligne
- Démarrer `jwebserver` (du JDK embarqué) sur `courses/build/`, port fixe (à choisir, ex. 8800), lié à `127.0.0.1`.
- Ouvrir le navigateur par défaut sur l'URL locale (cross-OS : `start` Windows / `xdg-open` / `open`).
- Arrêt propre du serveur à la fermeture de la console (ou commande dédiée).

### B. Extension du bundle (`build-bundle`)
- Inclure `courses/build/` dans le ZIP (régénéré via `npm run build` côté formateur au moment du build du bundle — **vérifier la fraîcheur**).
- Inclure un **git portable** (Windows) ; câbler `PATH`/`GIT_EXEC_PATH` pour que `ProcessGitClient` le trouve.
- Conserver l'existant (uber-jar, `exercises/`, JDK, guide).

### C. Launcher stagiaire unique (Windows) + guide
- `piscine.bat` (ou `.ps1`) qui : prépare l'environnement (PATH local JDK + git, `JAVA_HOME`), **démarre le site** (B/A), **lance le REPL** moulinette (`init` au 1er lancement puis `repl`).
- Mettre à jour `LISEZMOI.txt` + le **guide stagiaire** (voir §6) pour le parcours standalone.

### D. Validation sur machine cible
- Tester le ZIP sur une **machine Windows propre** (sans Java/git/Node), comme le « run formateur » des tâches #40 / #54. Critère : décompresser → lancer → site visible + REPL + un rendu d'exercice évalué, **hors-ligne**.

## 3. Découpage proposé

Itération **cohérente et compacte** (ce n'est pas un module) : **un seul cycle** spec → plan → exécution inline → **1 PR**. Tâches internes = A, B, C, D ci-dessus, dans cet ordre (A et B sont les briques, C les assemble, D valide). Branche : `feature/standalone-bundle`.

## 4. Validation EN LOCAL

- Build du bundle : `bash scripts/build-bundle.sh --jdk "E:/java/jdk-25.0.3+9"` (ou `.ps1`). Vérifier que le ZIP contient site + git + jdk + jar + exercices.
- `jwebserver` : vérifier qu'il sert `courses/build/index.html` et que la navigation Docusaurus fonctionne (routing).
- Boucle moulinette inchangée : voir `docs/piscine-stagiaire.md` (REPL, `submit-start`, rendu, rapport).
- **CI** : le job `build-docusaurus` garantit déjà que `courses/build/` est générable ; ne pas casser les 4 jobs.

## 5. Questions ouvertes à trancher au démarrage

- **`jwebserver`** : port par défaut, gestion du « port déjà pris », arrêt propre, ouverture navigateur cross-OS.
- **git portable** : quelle distribution (MinGit ?), comment le formateur l'obtient/le stocke offline, où il vit dans le repo/bundle, licence à respecter.
- **`courses/build/`** : le **committer** dans le repo (simplifie le bundle, mais artefact volumineux versionné) **ou** le régénérer au build (impose Node côté formateur) ? Recommandation à acter dans la spec.
- **Taille finale du ZIP** (JDK + git + site + jar) : acceptable pour distribution ? Compression ?
- **Sous-commande moulinette `site`** vs simple script launcher : garder la console focalisée → plutôt un script.
- **Numéro de port / navigateur** : faut-il un fallback « ouvrez vous-même http://127.0.0.1:8800 » si l'ouverture auto échoue.

## 6. Documentation à mettre à jour (les DEUX modes)

> ⚠️ Une partie est faite **dans la session de cadrage** (ce commit) : `README.md` et `docs/piscine-stagiaire.md` présentent désormais les deux modes, le mode standalone étant marqué **en cours de finalisation (#56)**. La session d'implémentation devra **lever cette mention** une fois le bundle complet livré et validé (§2.D), et compléter le pas-à-pas standalone (lancement réel, captures de commandes).

- `README.md` : section « Deux modes d'utilisation » (fait, à confirmer).
- `docs/piscine-stagiaire.md` : parcours **mode dépôt** (existant) **+ mode standalone (ZIP)** (ébauche posée ; à finaliser avec le launcher réel).
- `docs/setup-dev.md` : préciser que le mode standalone **n'exige aucune install** (à compléter).
- `CONTRIBUTING.md` : documenter `build-bundle` (comment produire et tester le ZIP).

## 7. Premiers pas de la nouvelle session

- [ ] Lire ce brief + `build-bundle.sh`/`.ps1` + `JavaToolkit.java` + `docs/piscine-stagiaire.md`.
- [ ] `git checkout main && git pull` ; brancher `feature/standalone-bundle`.
- [ ] **Brainstorm** ciblé sur les questions §5 (skill `superpowers:brainstorming`).
- [ ] Spec : `docs/superpowers/specs/<date>-standalone-design.md`. Plan : `docs/superpowers/plans/<date>-standalone.md`.
- [ ] Exécuter inline (A→B→C→D), valider le ZIP en local, **lever la mention « en cours » dans la doc** (§6), PR unique, MAJ `docs/backlog.md` (#56).

---

*Brief préparé le 2026-06-02 après clôture du module 2 (#18). Référence backlog : #56. Itération insérée avant le module 3 (#19).*
