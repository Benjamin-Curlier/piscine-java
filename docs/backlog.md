# Backlog — Piscine ETNC

> Liste des tâches à venir, organisée en phases. Tenue à jour à chaque session.
> Une session reprend ce backlog, choisit une tâche **non bloquée**, suit le [workflow par validation](#convention) et coche les critères d'acceptation avant de marquer la tâche `Faite`.

## Convention

- **Statut** : `À faire` / `En cours` / `Faite` / `Bloquée` / `Abandonnée`.
- **Workflow par session** :
  1. Lire la tâche complète et ses pré-requis.
  2. **Proposer** un plan d'exécution détaillé.
  3. **Attendre la validation** du formateur avant d'écrire / modifier des fichiers.
  4. Exécuter, cocher les critères d'acceptation.
  5. Commit + push.
  6. Mettre à jour ce fichier (statut + lien vers le commit qui clôt la tâche).
- **ID** : continu, jamais réutilisé. Les tâches abandonnées gardent leur ID.
- **Numérotation** : les tâches #1 à #8 sont déjà faites — voir l'historique git (commits `a74d3be` → `4a92d36`).

---

## 🎯 Roadmap jusqu'au MVP (ordre d'exécution validé le 2026-06-01)

> Issue d'une remontée d'action : maintenant que le reactor compile, que des tests existent et que de vrais Checkers arrivent, on installe les filets de sécurité **avant** d'accumuler du contenu, on resynchronise `main`, puis on produit le module 1 complet en s'appuyant sur la boucle vertueuse « écris l'exo → la moulinette valide sa solution ».
>
> **Définition du MVP** : chaîne d'évaluation réelle (Checkers) + filets CI + `main` à jour + déploiement stagiaire + **module 1 pédagogiquement complet** (7 chapitres + 10 exercices validés par la moulinette).

Séquence :

1. **#11a — CI minimal** (filet anti-régression, *le plus rentable*). `verify` du reactor + les 4 suites taguées (`git`/`tools`/`e2e`) + lint-exercices + build Docusaurus. **Avant** les Checkers : c'est ce qui aurait attrapé le commentaire XML invalide de `cli/pom.xml`. → *priorité immédiate.*
2. **#52 — PR + merge `feature/mvp-console-correction` → `main`.** Une fois le CI minimal vert sur la PR, merger l'acquis MVP console pour que `main` reflète la réalité. Les itérations suivantes branchent depuis `main`.
3. **#43–#51 — Target MVP (vrais Checkers + bundle + docs).** Depuis une branche issue de `main`. Inclut le **StyleChecker en mode advisory** (non bloquant) pour la beta — voir plan target-MVP, Tasks 5–6.
4. **#11b — Job `valider-solutions` dans le CI.** Débloqué par les vrais Checkers : faire tourner la moulinette sur la `solution/` de référence de chaque exo. Si la solution ne passe pas ses propres tests, l'exo est cassé *avant* le stagiaire. Filet pédagogique le plus précieux.
5. **#15 + #16 — Module 1 complet** (6 chapitres restants + 9 exercices restants). Boucle vertueuse : chaque exo écrit est immédiatement validé par la moulinette + le CI `valider-solutions`. **Fait partie du MVP.**
6. **#14 — `CONTRIBUTING.md` + `LICENSE`.** Cadrage institutionnel/militaire : comment ajouter exos/chapitres, droits d'usage. Peu coûteux.

Hygiène (hors chemin critique, à caser quand pratique) : **#54** (committer `maven-wrapper.jar` pour un `./mvnw` offline), **#53** (durcir le style advisory → bloquant après la beta), **#55** (migrer `ReportGenerator` vers Jackson quand le format JSON se stabilise).

---

## Phase 1 — Outillage fondateur (priorité HAUTE)

> Avant de produire du contenu pédagogique en masse, on cristallise les outils qui le valideront. Sans ça, on accumulerait de la dette.

### #9 — Squelette Maven multi-module de la moulinette
**Statut** : Faite — commit `feat(moulinette): squelette Maven multi-module Java 25` (voir historique git)
**Priorité** : Haute
**Pré-requis** : aucun
**Pourquoi** : la moulinette est le différenciateur du projet (correction explicative). Avoir le squelette tôt permet de tester l'intégration avec les exercices.

**Livrable** : `moulinette/` à la racine, projet Maven multi-module Java 25.

**Sous-modules cibles** (validés en mémoire le 2026-05-22) :
- `moulinette/framework/` — API des `Checker` (compile, run, lint, style, plagiat) et du modèle de rapport.
- `moulinette/runner/` — exécution sandboxée (au début : process forké sans Docker ; Docker viendra plus tard).
- `moulinette/reports/` — génération du rapport pédagogique (Markdown + JSON).
- `moulinette/cli/` — point d'entrée CLI `mvn -pl cli exec:java -- run --exo 1.1.1 --rendu <path>`.

**Critères d'acceptation** :
- [x] POM parent Java 25, dependency management JUnit 5 / AssertJ / SLF4J.
- [x] Chaque module a son `pom.xml`, son `src/main/java`, un test smoke.
- [ ] `mvn -f moulinette/pom.xml verify` passe — **en attente** : Java 25 pas encore installé en local (tâche #12). Sera validé par le CI (tâche #11).
- [x] README dans `moulinette/` qui explique la structure et comment lancer en local.

**Décisions prises** :
- Groupe ID : `etnc.piscine.moulinette` ✓
- Logging : **SLF4J 2.0.16 + Logback 1.5.12** (API dans les modules bibliothèques, runtime dans cli)
- Format du rapport : **concaténation simple** — Mustache à introduire quand le format sera figé.

---

### #10 — Maven Wrapper (mvnw) mutualisé pour les exercices
**Statut** : Faite — commit `feat(tooling): Maven Wrapper 3.3.2 + docs/setup-dev.md` (voir historique git)
**Priorité** : Haute
**Pré-requis** : aucun
**Pourquoi** : éviter aux stagiaires (et au CI) d'avoir à installer Maven globalement. `./mvnw` suffit.

**Livrable** : un Maven Wrapper installé à la racine du repo, utilisable depuis n'importe quel exercice via un alias / un lien.

**Critères d'acceptation** :
- [x] `./mvnw -v` fonctionne depuis la racine (télécharge Maven 3.9.9 à la première exécution).
- [x] Documentation dans `docs/setup-dev.md` créée : installation Java 25 portable + exemples `./mvnw`.
- [x] Format d'exercice mis à jour pour mentionner `./mvnw` plutôt que `mvn`.

**Décision prise** :
- Un seul `mvnw` racine partagé ✓ — évite la duplication (commande `./mvnw -f <chemin>/pom.xml`).

---

### #11 — GitHub Actions CI (scindée en #11a et #11b)
**Statut** : **#11a Faite** (commit `8e3399b`, [PR #1](https://github.com/Benjamin-Curlier/Piscine-ETNC/pull/1)) — **#11b Faite** (job `valider-solutions` via `scripts/valider-solutions.sh` : `mvn test` sur chaque `solution/`)
**Priorité** : **Haute — point d'entrée de la roadmap MVP.**
**Pré-requis** : #9 (squelette moulinette), #10 (mvnw)
**Pourquoi** : casser tout PR qui régresse un exercice, le site, ou la moulinette. *Le plus rentable* : c'est l'absence de CI qui avait laissé passer le commentaire XML invalide de `cli/pom.xml`.

**Livrable** : `.github/workflows/ci.yml`. Le job `valider-solutions` dépend des vrais Checkers (#43–#51), donc la tâche est scindée en deux jalons exécutés à des moments différents de la roadmap.

#### #11a — CI minimal (AVANT les Checkers, filet anti-régression immédiat)
**Jobs** :
1. **build-moulinette** — `mvn -f moulinette/pom.xml verify` (reactor + unitaires) puis les suites lourdes taguées : `git`, `tools`, `e2e` (lancées explicitement via `-Dgroups`). Casse à la moindre régression de build/test.
2. **lint-exercices** — script Bash/Python qui vérifie pour chaque dossier `exercises/M.S.E-slug/` la présence des fichiers obligatoires (`sujet.md`, `metadata.yml`, `starter/pom.xml`, etc.) et la conformité YAML.
3. **build-docusaurus** — `cd courses && npm ci && npm run build`. Le `onBrokenLinks: 'throw'` casse le job si une réf est cassée.

**Critères d'acceptation #11a** :
- [ ] Les 3 jobs passent sur la PR `feature/mvp-console-correction → main` (#52).
- [ ] Un PR qui supprime `sujet.md` d'un exercice casse `lint-exercices`.
- [ ] Un PR qui introduit un lien cassé dans un chapitre casse `build-docusaurus`.
- [ ] Tourne sur `ubuntu-latest` avec `setup-java@v4` (Temurin 25) et `setup-node@v4` (Node 22).
- [ ] Gotcha réseau Maven : prévoir le cache `~/.m2` (`actions/cache` ou `setup-java` avec `cache: maven`) ; sur les runners GitHub la résolution Maven Central fonctionne (contrairement à la machine du formateur).

#### #11b — Job `valider-solutions` (APRÈS les Checkers #43–#51)
**Job** : pour chaque exercice, faire tourner la moulinette sur la `solution/` de référence (compile + tests publics + tests privés). La solution doit passer **tous** ses propres tests. Filet pédagogique le plus précieux : un exo cassé est détecté avant le stagiaire.

**Critères d'acceptation #11b** :
- [ ] Un PR qui altère la solution de l'exo 1.1.1 (ou ses tests) casse `valider-solutions`.
- [ ] Le job s'exécute sur tous les exos présents sous `exercises/` (boucle, pas en dur).
- [ ] Réutilise l'uber-jar console (`run --exo … --solution`) ou le module `cli` headless — pas de logique de validation dupliquée dans le YAML.

---

### #12 — Documentation install Java 25 + script `setup-dev`
**Statut** : Faite — branche `feature/setup-dev-scripts` (PR à merger). Doc `setup-dev.md` enrichie (Node 22, wrapper offline #54, §0 automatisée) + scripts `setup-dev.ps1`/`setup-dev.sh` (install Temurin 25 portable via API Adoptium). _⚠️ chemin de téléchargement non exécuté en session (pas de Java/Maven local) — à valider par un run formateur, comme #40._
**Priorité** : Haute (bloque la validation locale par tout contributeur)
**Pré-requis** : aucun
**Pourquoi** : actuellement la machine du formateur n'a que Java 8 et pas de Maven. Personne ne peut valider en local. Pareil pour les futurs stagiaires.

**Livrable** :
- `docs/setup-dev.md` : install Java 25 (Windows/Linux/macOS) + Node 22 + variables d'env.
- Optionnel : `scripts/setup-dev.ps1` (Windows) et `scripts/setup-dev.sh` (Unix) qui installent Java 25 en mode portable / utilisateur (la machine du formateur n'a pas les droits admin).

**Critères d'acceptation** :
- [x] Sur la machine du formateur (Windows, no admin), suivre `setup-dev.md` permet d'arriver à `java --version` → 25 et `mvn -v` (ou `./mvnw -v`) qui fonctionne. _(procédure manuelle §1-4 + script §0 ; run de validation formateur recommandé.)_
- [x] La doc renvoie vers la version `.zip` portable de Temurin pour le cas no-admin.
- [x] Le `README.md` racine ajoute un lien vers `setup-dev.md` (section « Démarrage rapide »).

**Décisions à prendre** :
- Faut-il versionner un JDK Temurin portable dans le repo ? **Non**, taille trop grosse et ça date. Pointer vers Adoptium.

---

### #13 — `docs/grille-evaluation.md`
**Statut** : Faite — branche `feature/grille-evaluation` (PR à merger). Grille livrée + ré-ID des critères `formateur` des `evaluation.yml` du module 1 (1.1.1/1.1.2 → `respect-consignes`, 1.2.3 → `lisibilite`).
**Priorité** : Moyenne
**Pré-requis** : aucun
**Pourquoi** : `docs/format-exercice.md` mentionne ce fichier mais il n'existe pas encore. Cadre la part `type: formateur` des `evaluation.yml`.

**Livrable** : grille de notation détaillée pour les critères humains (lisibilité, idiomatisme, respect des consignes, démarche).

**Critères d'acceptation** :
- [x] Une grille par "type de critère humain" récurrent (au moins : `demarche`, `lisibilite`, `idiomatisme`, `respect-consignes`).
- [x] Pour chaque grille : 4 niveaux (excellent / bon / passable / insuffisant) avec descripteurs concrets et exemples observables.
- [x] Un exemple appliqué à l'exo 1.1.1 hello-world.

---

### #14 — `CONTRIBUTING.md` + `LICENSE` à la racine
**Statut** : Faite — branche `feature/contributing-license` (PR à merger). LICENSE = en-tête provisoire « Tous droits réservés — usage interne ETNC » (arbitrage définitif hiérarchie/RSSI à venir).
**Priorité** : Haute (étape 6 de la [roadmap MVP](#-roadmap-jusquau-mvp-ordre-dexécution-validé-le-2026-06-01))
**Pré-requis** : #13 (la grille est référencée)
**Pourquoi** : projet institutionnel/militaire partagé entre formateurs — cadrer l'ajout d'exos/chapitres et clarifier les droits d'usage avant d'élargir la base de contributeurs.

**Livrable** : `CONTRIBUTING.md` racine + un fichier `LICENSE` à la racine.

**Critères d'acceptation** :
- [x] Workflow Git : `feature/<slug>` → PR → revue par 1 autre formateur → merge (le CI #11 doit être vert).
- [x] Checklist d'ajout d'un exercice (renvoie vers `docs/format-exercice.md`).
- [x] Checklist d'ajout d'un chapitre (renvoie vers `docs/charte-redaction.md`).
- [x] Critères de bloc à la revue (lisibilité, niveau de langue, tests qui passent, etc.).
- [x] `LICENSE` : en-tête provisoire « Tous droits réservés — usage interne ETNC » posé en attendant l'arbitrage hiérarchie/RSSI.

---

## Phase 1bis — MVP Piscine locale (priorité HAUTE)

> Première itération **bout en bout utilisable** : un stagiaire clone le repo, lance un script de bootstrap, et fait des exos en local avec une console REPL qui déclenche la moulinette sur la séquence git `add` + `commit` + `push` (branche `rendu/<sous-groupe>`).
> Spec validée : [`docs/superpowers/specs/2026-05-28-mvp-console-correction-design.md`](superpowers/specs/2026-05-28-mvp-console-correction-design.md).
> Tous ces items partagent le pré-requis #9 (squelette moulinette).
> **Itération réalisée** sur la branche `feature/mvp-console-correction` — plan d'implémentation : [`docs/superpowers/plans/2026-05-28-mvp-console-correction.md`](superpowers/plans/2026-05-28-mvp-console-correction.md).
> Build vérifié : `mvn -f moulinette/pom.xml verify` (unitaires) + suites `@Tag("git")` et `@Tag("e2e")` vertes.

### #34 — Module Maven `moulinette/console` (squelette)
**Statut** : Faite — commit `feat(console): squelette Maven module console`
**Priorité** : Haute — **Pré-requis** : #9
**Livrable** : nouveau sous-module Maven `moulinette/console`, dépend de `framework`+`runner`+`reports`, enum `Mode { LOCAL, NOMINAL }`, smoke test.
**Critères** :
- [x] `mvn -f moulinette/pom.xml -pl console verify` passe.
- [x] Pas de dépendance vers `cli`.
- [x] Smoke test charge le module (`ConsoleSmokeTest`).
- _Note : a aussi corrigé un commentaire XML invalide dans `cli/pom.xml` qui bloquait le reactor._

### #35 — `console.git.GitClient` + `ProcessGitClient`
**Statut** : Faite — commit `feat(console): GitClient + ProcessGitClient + FakeGitClient`
**Priorité** : Haute — **Pré-requis** : #34
**Livrable** : interface `GitClient` (run, currentBranch, lastPushRefs) + impl sous-process + tests intégration `@Tag("git")`.
**Critères** :
- [x] `FakeGitClient` disponible pour les tests des autres packages.
- [x] Tests int : init / commit / push vers bare local / lecture des refs (`ProcessGitClientIT`).
- [x] Tagués `@Tag("git")` (skippables via `surefire.excludedGroups`).

### #36 — `console.workspace` (catalogue + initializer)
**Statut** : Faite — commits `feat(console): ExerciseCatalog` + `feat(console): LocalWorkspaceInitializer`
**Priorité** : Haute — **Pré-requis** : #34, #35
**Livrable** : `ExerciseCatalog` (scan `exercises/`, regroupement par `sous_groupe`, tri par `position`), `LocalWorkspaceInitializer` (création repo stagiaire + bare remote + copie starters + commit initial).
**Critères** :
- [x] `init` produit un repo git valide avec remote `origin = file://…/.piscine/remote.git`.
- [x] Catalogue résilient à un `metadata.yml` invalide (warn, skip).
- [x] Tests unitaires + intégration avec `@TempDir`.
- _Note : `LocalSubmissionBackend` non nécessaire au MVP ; l'init local suffit. Le découplage `local`/`nominal` est porté par l'enum `Mode` (#39)._

### #37 — `console.commands` + `console.repl`
**Statut** : Faite — commits `feat(console): commandes git de base` + `feat(console): PushCommand, SubmitStart, CommandRegistry, Repl`
**Priorité** : Haute — **Pré-requis** : #35
**Livrable** : interface `Command`, classes `AddCommand`, `CommitCommand`, `PushCommand`, `StatusCommand`, `LogCommand`, `DiffCommand`, `SubmitStartCommand`, `HelpCommand`, `ExitCommand`. Boucle `Repl` avec prompt `piscine[<branche>]>` et tokenizer respectant les guillemets.
**Critères** :
- [x] Commande inconnue → message « non supportée dans le MVP » + suggestion.
- [x] Commande malformée → message pédagogique (ex: commit sans `-m`).
- [x] Tests unitaires sur `console.commands` (Add, Commit, Push, SubmitStart, Registry).

### #38 — `SubmissionTrigger` + `MoulinetteRunner`
**Statut** : Faite — commit `feat(console): SubmissionTrigger + MoulinetteRunner`
**Priorité** : Haute — **Pré-requis** : #36, #37
**Livrable** : détection du push sur `rendu/<sous-groupe>`, lancement de la moulinette sur les exos du sous-groupe en ordre de difficulté croissante, **arrêt au premier exo qui échoue**, génération du rapport `.piscine/reports/<groupe>-<ts>.md`.
**Critères** :
- [x] Push sur `main` ne déclenche pas.
- [x] Séquence stoppe au 1er `Checker` KO avec message explicite (détection via `CheckResult.Status.OK`).
- [x] Le rapport contient une section par exo exécuté.

### #39 — Sous-commandes `init` / `repl` dans `console.Main`
**Statut** : Faite — commit `feat(console): Main init/repl + shade jar exécutable`
**Priorité** : Haute — **Pré-requis** : #36, #37
**Livrable** : `Main` parse `init` et `repl`, accepte `--mode local` (par défaut). `--mode nominal` retourne une erreur explicite « non implémenté dans le MVP ». Fat-jar via maven-shade.
**Critères** :
- [x] `java -jar moulinette-console.jar init --nom X --dest <path>` fonctionne bout en bout (vérifié manuellement).
- [x] `java -jar moulinette-console.jar repl --repo <path>` lance la boucle.
- [x] `--help` documente les deux sous-commandes.

### #40 — Script `scripts/piscine-bootstrap.{sh,ps1}`
**Statut** : Faite — commit `feat(scripts): bootstrap autonome piscine-bootstrap`
**Priorité** : Haute — **Pré-requis** : #39
**Livrable** : deux scripts (Bash + PowerShell) à la racine du repo. Vérifient les prérequis, builent la console, créent le workspace à côté du repo (`../piscine-<nom>/`), lancent `init`, affichent la commande pour démarrer le REPL.
**Critères** :
- [x] Idempotent (relance détecte le workspace, propose `--force`).
- [x] Message clair si Java 25 ou git absent + renvoi vers `docs/setup-dev.md`.
- [x] Testé sur la machine du formateur (Windows, no admin) — résolution `JAVA_HOME` + préférence `mvn` système.

### #41 — `docs/piscine-stagiaire.md` (guide autonome)
**Statut** : Faite — commit `docs: guide stagiaire autonome + lien depuis README`
**Priorité** : Haute — **Pré-requis** : #40
**Livrable** : guide unique pour le stagiaire — du `git clone` au premier rendu validé.
**Critères** :
- [x] Section « Démarrage » : `clone` → `bootstrap` → premier `repl`.
- [x] Section « Faire un rendu » : `submit-start` → édition → `add` + `commit` + `push` → lecture du rapport.
- [x] Section « Bloqué ? » : comment lire un rapport d'échec, comment re-pousser.
- [x] Lien depuis le `README.md` racine en haut.

### #42 — E2E smoke « happy path » sur exo 1.1.1
**Statut** : Faite — commit `test(console): E2E happy path sur exo 1.1.1`
**Priorité** : Haute — **Pré-requis** : #38, #39, #40
**Livrable** : test JUnit (`HappyPathE2EIT`) qui exécute init + un REPL scripté (`submit-start` → add/commit/push) sur un repo factice et vérifie qu'un rapport ✓ est généré.
**Critères** :
- [x] Test passe en local (`@Tag("e2e")`) et sera repris par le CI #11.
- [x] Aucun fichier hors `@TempDir`.
- _Note : exercice factice utilisé (la solution de référence réelle de 1.1.1 sera branchée quand de vrais `Checker` existeront — voir limites ci-dessous)._

### Limites connues du MVP (à traiter ultérieurement)
- **Pas de vrais `Checker`** : `MoulinetteRunner.Default` est câblé mais reçoit une liste de `Checker` vide ; aucun n'existe encore dans `framework`. Tant qu'il n'y en a pas, tout sous-groupe est considéré « OK ». Prochaine étape : implémenter `CompileChecker`/`TestChecker` et les injecter dans `Main.runRepl`.
- **`mvnw` ne télécharge pas Maven** dans certains environnements réseau restreints ; les scripts préfèrent donc un `mvn` système quand il existe.
- **Mode `nominal`** : rejeté explicitement (non implémenté).

---

## Phase 1quater — Target MVP : Checkers réels + déploiement (#43–#51) — **Faite**

> Itération réalisée sur `feature/target-mvp-checkers` (issue de `main`). La moulinette est désormais **réellement évaluante**.
> Spec : [`docs/superpowers/specs/2026-05-29-target-mvp-checkers-deploiement-design.md`](superpowers/specs/2026-05-29-target-mvp-checkers-deploiement-design.md) — Plan : [`docs/superpowers/plans/2026-05-29-target-mvp-checkers-deploiement.md`](superpowers/plans/2026-05-29-target-mvp-checkers-deploiement.md).
> Vérifié : les 4 suites vertes (`verify` 28 unit, `git` 2, `tools` 7, `e2e` 2) + uber-jar exécutable (`java -jar … --help`).

| # | Tâche | Statut |
|---|---|---|
| #43 | `CheckerContext` porte `exerciseRefPath` | Faite |
| #44 | `JavaToolkit` + `FqcnExtractor` + deps outillage runtime | Faite |
| #45 | `CompileChecker` | Faite |
| #46 | `PublicTestChecker` + `PrivateTestChecker` | Faite |
| #47 | `StyleChecker` **advisory** + `isBlocking()` + config Checkstyle | Faite |
| #48 | Câblage `Main`/`MoulinetteRunner` + rapport `ReportGenerator` + E2E | Faite |
| #49 | Script `build-bundle` (ZIP portable) | Faite (scripts livrés ; exécution ZIP non vérifiée localement — PowerShell interdit + `zip` absent) |
| #50 | `docs/deploiement-instructeur.md` | Faite |
| #51 | `docs/architecture-moulinette.md` | Faite |

**Ruse centrale** : l'uber-jar shadé embarque JUnit/AssertJ/Checkstyle (scope `runtime`) et **sert lui-même de classpath** pour compiler/exécuter le code stagiaire — zéro Maven, zéro réseau à l'exécution. Fix shade : exclusion des `META-INF/*.SF|DSA|RSA` (deps signées).

**Débloque #11b** (`valider-solutions`) : la moulinette peut maintenant tourner sur les `solution/` de référence en CI.

**Limites** : style advisory (#53 pour durcir), PMD différé, note /20 future, JSON fait main (#55 Jackson), anti-triche (#29), Docker (#30).

**À vérifier côté instructeur** : un `\.\scripts\build-bundle.ps1` complet (génération du ZIP + lancement hors repo) sur une machine où PowerShell est autorisé.

---

## Phase 2 — Module 1 pilote complet (priorité HAUTE — **inclus dans le MVP**)

> On finit **entièrement** le module 1 avant de toucher aux autres : 7 chapitres + 10 exercices + rétro. Sert de gabarit pour les modules 2 à 6.
> **Décision 2026-06-01** : le module 1 complet fait partie du périmètre MVP (étape 5 de la [roadmap](#-roadmap-jusquau-mvp-ordre-dexécution-validé-le-2026-06-01)). L'outillage a dépassé le contenu (1 exo, 2 chapitres) ; or la moulinette + le CI `valider-solutions` (#11b) permettent désormais une **boucle vertueuse** : on écrit l'exo, la moulinette valide immédiatement sa solution de référence. C'est le bon moment pour produire le contenu en masse sans accumuler de dette.

### #15 — Chapitres restants du module 1
**Statut** : Faite — branche `feature/m1-chapitres` (PR à merger). Spec [`2026-06-01-m1-chapitres-design.md`](superpowers/specs/2026-06-01-m1-chapitres-design.md), plan [`2026-06-01-m1-chapitres.md`](superpowers/plans/2026-06-01-m1-chapitres.md).
**Priorité** : Haute
**Pré-requis** : aucun (le chapitre 1.1 sert de modèle)

**Livrable** : 6 chapitres dans `courses/docs/module-1-fondamentaux/`, format défini en section 6 de `docs/charte-redaction.md`.

- [x] `1-2-variables-types-primitifs.md`
- [x] `1-3-operateurs.md`
- [x] `1-4-entrees-clavier.md`
- [x] `1-5-conditions.md`
- [x] `1-6-boucles.md`
- [x] `1-7-bonnes-pratiques-lisibilite.md`

**Critères d'acceptation** :
- [x] Chaque chapitre respecte la structure imposée (section 6 charte rédaction).
- [x] Build Docusaurus passe (`npm run build`).
- [x] Chaque chapitre a son "exercice guidé" et ses "vérifications d'acquis".
- [x] Aucune notion utilisée n'a été introduite plus tard que son chapitre d'origine (cohérence avec [`referentiel.md`](referentiel.md)).

---

### #16 — Exercices restants du module 1
**Statut** : Faite — livrée en 3 PRs (un sous-groupe par PR). Spec [`2026-06-01-m1-exercices-design.md`](superpowers/specs/2026-06-01-m1-exercices-design.md), plan [`2026-06-01-m1-exercices.md`](superpowers/plans/2026-06-01-m1-exercices.md). 1.1 (PR #6) et 1.2 (PR #7) mergées ; 1.3 en PR finale.
**Priorité** : Haute (étape 5 de la roadmap MVP)
**Pré-requis** : #10 (mvnw), #15 (les notions des chapitres déterminent ce qu'on peut demander), **#43–#51 (vrais Checkers)** + **#11b (`valider-solutions`)** pour la boucle vertueuse de validation

**Livrable** : 9 exercices dans `exercises/module-1-fondamentaux/`, format défini dans `docs/format-exercice.md`.

Sous-groupes ([referentiel.md §4 module 1](referentiel.md#sous-groupes-dexercices-3-sous-groupes-10-exercices)) :
- [x] **1.1 Premiers pas** : `1.1.2-affichage-formate`, `1.1.3-lecture-saisie`.
- [x] **1.2 Variables et opérateurs** : `1.2.1-conversion-unites`, `1.2.2-calculs-geometriques`, `1.2.3-manipulation-booleenne`.
- [x] **1.3 Contrôle de flux** : `1.3.1-fizzbuzz`, `1.3.2-fibonacci-iteratif`, `1.3.3-table-multiplication`, `1.3.4-devine-le-nombre`.

**Critères d'acceptation** :
- [x] Chaque exercice complet (11 fichiers ; exception documentée : `1.1.2` sans `tests-prives/`, autorisée par `format-exercice.md` §2).
- [x] `mvn test` sur chaque `solution/` passe les tests publics ET privés (job CI `valider-solutions`).
- [x] Le CI #11 passe.

**Décisions** : tout dans `main` + E/S standard (antériorité) ; util de test `CaptureEntree` ; 1.3.4 secret via `Random(1789)` fourni dans le starter ; entrée clavier sans séparateur décimal (insensible à la locale).

---

### #17 — Rétrospective module 1 pilote
**Statut** : Faite — branche `feature/retro-module-1` (PR à merger). `docs/retro-module-1.md`. _Reste : le formateur doit renseigner les temps réels de production (§4) — donnée non instrumentée._
**Priorité** : Moyenne
**Pré-requis** : #15, #16

**Livrable** : `docs/retro-module-1.md` documentant ce qui a été appris en construisant le module pilote, ajustements à propager aux modules 2-6.

**Critères d'acceptation** :
- [x] Liste des ajustements à apporter aux formats (`format-exercice.md`, `charte-redaction.md`).
- [x] Estimation du temps réel passé par chapitre / par exercice → ré-estimation des modules 2-6. _(tableau fourni ; temps de production formateur à remplir.)_
- [x] Décision claire : on continue identique / on adapte / on simplifie. _(on continue à l'identique, ajustements intégrés.)_

---

## Phase 3 — Modules 2 à 6 + projets binôme (priorité MOYENNE)

> 5 modules × (chapitres + exercices) + 3 projets binôme. Volume gros, à étaler.

### #18 à #22 — Modules 2 à 6 (1 tâche par module)
Pour chaque module, créer une tâche dédiée listant ses chapitres et exercices. Ne pas tout démarrer en parallèle — pédagogie d'abord, donc finir Mod2 avant d'attaquer Mod3, etc.

- **#18 — Module 2** (5 chapitres, 12 exercices, 4 sous-groupes) — **FAITE** (12/12 exercices + 5 chapitres livrés). Brief : [`docs/superpowers/2026-06-01-m2-kickoff.md`](superpowers/2026-06-01-m2-kickoff.md). Validation **locale** (Java 25 + Maven) + CI : 22/22 solutions, lint 0 erreur.
  - ✅ **Volet chapitres livré** (PR #16 mergée) : `_category_.json` + 5 chapitres (2.1 Tableaux 1D, 2.2 Tableaux 2D, 2.3 Chaînes, 2.4 Méthodes, 2.5 Récursivité) + liaison module 1→2. Build Docusaurus vert. Spec [`specs/2026-06-01-m2-chapitres-design.md`](superpowers/specs/2026-06-01-m2-chapitres-design.md), plan [`plans/2026-06-01-m2-chapitres.md`](superpowers/plans/2026-06-01-m2-chapitres.md).
  - ✅ **Volet exercices livré** (cycles séparés, 1 sous-groupe = 1 PR) : 2.1 Tableaux, 2.2 Chaînes, 2.3 Méthodes, 2.4 Récursivité. **Brief de démarrage prêt** : [`superpowers/2026-06-01-m2-exos-kickoff.md`](superpowers/2026-06-01-m2-exos-kickoff.md). Décisions transverses figées (spec chapitres §10) : tout dans `main` pour 2.1/2.2, méthodes `static` testées par retour pour 2.3/2.4, récursivité imposée par consigne, refactor 2.3.3 basé sur 1.2.2.
    - ✅ **2.1 Tableaux** (PR #18) : `min-max-moyenne`, `inversion`, `recherche-lineaire`, `rotation-matrice`. Spec [`specs/2026-06-02-m2-exos-2-1-design.md`](superpowers/specs/2026-06-02-m2-exos-2-1-design.md), plan [`plans/2026-06-02-m2-exos-2-1.md`](superpowers/plans/2026-06-02-m2-exos-2-1.md).
    - ✅ **2.2 Chaînes** (PR #19) : `palindrome`, `comptage-occurrences`, `ascii-art`. Saisie clavier + test sur sortie ; solutions validées en local (`valider-solutions` + lint 0 erreur). Spec [`specs/2026-06-02-m2-exos-2-2-design.md`](superpowers/specs/2026-06-02-m2-exos-2-2-design.md).
    - ✅ **2.3 Méthodes** (PR #20) : `biblio-maths`, `surcharge`, `refactor-calculs-geometriques`. Méthodes `static` à signature imposée, **testées par valeur de retour** (AssertJ direct). Spec [`specs/2026-06-02-m2-exos-2-3-design.md`](superpowers/specs/2026-06-02-m2-exos-2-3-design.md).
    - ✅ **2.4 Récursivité** : `factorielle-puissance`, `parcours-matrice-recursif`. Récursivité imposée (critère formateur `respect-consignes`), testée par valeur de retour. Spec [`specs/2026-06-02-m2-exos-2-4-design.md`](superpowers/specs/2026-06-02-m2-exos-2-4-design.md).
- **#56 — Mode standalone (ZIP autonome stagiaire)** — **FAITE** (insérée avant le module 3). ZIP hors-ligne complet : site de cours servi en local **in-process** (`CourseSiteServer` via `SimpleFileServer`, option `--site` du REPL), **MinGit** épinglé + SHA-256 vérifié, JDK portable, moulinette pré-compilée, launcher Windows unique. Spec [`specs/2026-06-02-standalone-design.md`](superpowers/specs/2026-06-02-standalone-design.md), plan [`plans/2026-06-02-standalone-bundle.md`](superpowers/plans/2026-06-02-standalone-bundle.md), brief [`superpowers/2026-06-02-standalone-kickoff.md`](superpowers/2026-06-02-standalone-kickoff.md). Décisions figées : Maven/`.m2` hors scope (la moulinette grade via javac + JUnit ConsoleLauncher) ; Windows d'abord. Doc des **deux modes** : nouvelle page `courses/docs/lancer-la-piscine.md` + `README.md` + `docs/piscine-stagiaire.md` + `CONTRIBUTING.md`. Reste : **validation sur machine Windows propre** (run formateur, §2.D du brief).
- **#19 — Module 3 (POO)** (10 chapitres, 14 exercices, 4 sous-groupes) → débloque le **Projet binôme #1**. ✅ **TERMINÉ** (chapitres + exercices livrés ; reste à rédiger la fiche du Projet binôme #1 dans un cycle dédié). Brief : [`superpowers/2026-06-03-m3-kickoff.md`](superpowers/2026-06-03-m3-kickoff.md) ; conception : spec [`specs/2026-06-03-m3-chapitres-design.md`](superpowers/specs/2026-06-03-m3-chapitres-design.md) + plan [`plans/2026-06-03-m3-chapitres.md`](superpowers/plans/2026-06-03-m3-chapitres.md).
    - **Volet chapitres** ✅ **LIVRÉ** (2 PR de 5) :
        - ✅ **PR-A — 3-1 à 3-5** (bases & héritage) : `classes-et-objets`, `encapsulation`, `constructeurs`, `this-et-static`, `heritage` + `_category_.json` + liaison module 2→3. Mergée.
        - ✅ **PR-B — 3-6 à 3-10** (abstraction & types modernes) : `polymorphisme`, `classes-abstraites`, `interfaces`, `enums`, `records-et-sealed`. Build Docusaurus vert ; exemple 3-10 (record/sealed/pattern matching) compilé en Java 25.
    - **Volet exercices** ✅ **TERMINÉ** (4 sous-groupes, cycles séparés) : ✅ 3.1 ✅ 3.2 ✅ 3.3 ✅ 3.4. Décisions transverses figées en annexe §10 de la spec.
        - ✅ **3.1 Classes et objets** : `3.1.1-point2d`, `3.1.2-compte`, `3.1.3-soldat`. Tests par API/AssertJ (on teste des objets), starter à signatures complètes à corps vide, frontière « 3.1 douce, 3.2 stricte » (pas de validation d'invariants). Solutions validées en local (`valider-solutions` + lint 0 erreur). PR #29 mergée. Spec [`specs/2026-06-03-m3-exos-3-1-design.md`](superpowers/specs/2026-06-03-m3-exos-3-1-design.md), plan [`plans/2026-06-03-m3-exos-3-1.md`](superpowers/plans/2026-06-03-m3-exos-3-1.md).
        - ✅ **3.2 Encapsulation et constructeurs** : `3.2.1-rectangle` (invariants + surcharge/`this(...)`), `3.2.2-compte-protege` (prolonge le Compte de 3.1 avec gardes), `3.2.3-date` (bornage jour/mois). Validation **sans exception** (M5) : constructeur corrige, setter ignore, méthode métier refuse. Solutions validées en local (`valider-solutions` 28/28 + lint 0 erreur). PR #30 mergée. Spec [`specs/2026-06-03-m3-exos-3-2-design.md`](superpowers/specs/2026-06-03-m3-exos-3-2-design.md), plan [`plans/2026-06-03-m3-exos-3-2.md`](superpowers/plans/2026-06-03-m3-exos-3-2.md).
        - ✅ **3.3 Héritage et polymorphisme** (4 exos) : `3.3.1-forme` (classe abstraite + Cercle/Rectangle), `3.3.2-vehicule` (héritage concret + super + @Override), `3.3.3-personnel` (hiérarchie abstraite Officier/SousOfficier/MilitaireDuRang), `3.3.4-parc` (consommer le polymorphisme sur Forme[] + instanceof). Starter léger pré-câblé (base fournie, sous-classes à compléter) ; polymorphisme testé via le type de base. Solutions validées en local (`valider-solutions` 32/32 + lint 0 erreur). PR #31 mergée. Spec [`specs/2026-06-03-m3-exos-3-3-design.md`](superpowers/specs/2026-06-03-m3-exos-3-3-design.md), plan [`plans/2026-06-03-m3-exos-3-3.md`](superpowers/plans/2026-06-03-m3-exos-3-3.md).
        - ✅ **3.4 Interfaces, enums, records** (4 exos) : `3.4.1-coordonnees` (record immuable + equals généré), `3.4.2-grade` (enum à attribut + switch exhaustif), `3.4.3-comparable` (interface de comparaison maison non générique + Dossier/Classement), `3.4.4-evenement` (hiérarchie sealed de records + switch exhaustif sans default). En-têtes structurels fournis, logique à écrire ; vérification par comportement (pas de réflexion). Solutions validées en local (`valider-solutions` 36/36 + lint 0 erreur). Spec [`specs/2026-06-03-m3-exos-3-4-design.md`](superpowers/specs/2026-06-03-m3-exos-3-4-design.md), plan [`plans/2026-06-03-m3-exos-3-4.md`](superpowers/plans/2026-06-03-m3-exos-3-4.md).
- **#20 — Module 4** (8 chapitres, 12 exercices, 3 sous-groupes) — **FAITE** (8 chapitres + 12 exercices livrés ; **PR #37→#40 mergées dans `main` le 2026-06-04** ; CI 4 jobs verts dont « valider-solutions » ; lint 48 exos / 0 erreur). Produit en **ultracode** (workflows multi-agents : brainstorm → rédaction → revue adversariale → gate Maven → lint). → débloque (avec le module 5) le **Projet binôme #2**. Brief : [`superpowers/2026-06-03-m4-kickoff.md`](superpowers/2026-06-03-m4-kickoff.md) ; conception : spec [`specs/2026-06-04-m4-chapitres-design.md`](superpowers/specs/2026-06-04-m4-chapitres-design.md) + plan [`plans/2026-06-04-m4-chapitres.md`](superpowers/plans/2026-06-04-m4-chapitres.md).
    - **Volet chapitres** ✅ **LIVRÉ** (1 PR de 8 chapitres `4-1`→`4-8` sous `courses/docs/module-4-collections-generiques-lambdas/`, `_category_.json` `collapsed:false`, liaison 3-10→4-1, build Docusaurus vert). Décisions transverses des exos figées en annexe §10 de la spec (modèle de domaine `enum Grade`+`record Soldat(nom,grade,anciennete)` ; `equals`/`hashCode` à la main pratiqués en 4.1.2 via `Utilisateur` ; généricité prouvée par le compilateur ; streams imposés par le design d'API quand possible ; cas limites sans exception). Revue adversariale 3 agents (structure/antériorité/anti-spoil/compilation Java 25) passée, corrections appliquées.
    - **Volet exercices** ✅ **LIVRÉ** (3 sous-groupes, 1 PR chacun ; specs `…-m4-exos-4-{1,2,3}-design.md`) : **4.1 Collections** (PR #38 : `annuaire-militaire`, `utilisateurs-uniques` [equals/hashCode à la main], `file-de-mission`, `groupement-par-grade`) ; **4.2 Génériques et tri** (PR #39 : `pile-generique`, `paire-generique`, `tri-soldats` [record `Comparable<Soldat>`], `comparator-chaine`) ; **4.3 Lambdas et streams** (PR #40 : `filtrage-projection`, `agregation`, `groupement-stream`, `recherche-optional` [Optional]). Chaque solution validée publics+privés (CI « valider-solutions » + local), chaque starter compile-mais-échoue (TDD).
- **#21 — Module 5** (7 chapitres, 10 exercices, 3 sous-groupes) → débloque le **Projet binôme #2**. Produit en **ultracode** (rédaction 6 agents Sonnet → revue adversariale 3 agents Opus → gates build Docusaurus + compilation JDK 25). Brief : [`superpowers/2026-06-04-m5-kickoff.md`](superpowers/2026-06-04-m5-kickoff.md) (PR #41) ; conception : spec [`specs/2026-06-04-m5-chapitres-design.md`](superpowers/specs/2026-06-04-m5-chapitres-design.md) + plan [`plans/2026-06-04-m5-chapitres.md`](superpowers/plans/2026-06-04-m5-chapitres.md).
    - **Volet chapitres** ✅ **PRODUIT** (branche `feature/m5-chapitres`, **PR à ouvrir/merger**) : 7 chapitres `5-1`→`5-7` sous `courses/docs/module-5-exceptions-io/` (`5-1` hiérarchie des exceptions, `5-2` try/catch/finally, `5-3` try-with-resources, `5-4` exceptions personnalisées, `5-5` I/O classique, `5-6` NIO.2, `5-7` formats texte CSV/properties), `_category_.json` `collapsed:false`, liaison `4-8`→`5-1`, build Docusaurus vert + extraits compilés au JDK 25. Décisions transverses des exos figées en annexe §10 de la spec : exceptions custom **unchecked** (`extends RuntimeException`) ; **NIO.2 imposé** dans les exos ; CSV/`.properties` **à la main**, pas d'exo JSON ; refactor 5.1.3 = recopie locale `etnc.m5` de 2.3.3 calculs-geometriques ; pattern de test `@TempDir`+UTF-8+`\n` ; `assertThatThrownBy(...)` pour tester les exceptions. Revue adversariale (structure/antériorité/anti-spoil/compilation Java 25) passée, corrections appliquées (IOException gérée dans les fragments 5-6, checked non-I/O en 5-1, `### Exemple` ajouté 5-5/5-7, gloss « parsing », coquilles).
    - **Volet exercices** ✅ **TERMINÉ** (3 sous-groupes, 1 PR chacun) : ✅ 5.1 ✅ 5.2 ✅ 5.3 — **module 5 exos COMPLET (10 exos)**.
        - ✅ **5.1 Exceptions** (3 exos, **PR #44 mergée**) : `5.1.1-saisie-defensive` (`validerEntier` : `NumberFormatException` illisible/null vs `IllegalArgumentException` hors-plage, `trim`), `5.1.2-effectif-invalide` (exception custom `EffectifInvalideException extends RuntimeException` **écrite par le stagiaire** + chaînage `cause` via `affecter(String)`), `5.1.3-calculs-geometriques-exceptions` (refactor `etnc.m5` de 2.3.3, gardes `IllegalArgumentException` sur dimension négative + helper `exigerPositif`). Premier sous-groupe où le **verrou « pas d'exceptions » saute** (tests par comportement : `assertThatThrownBy`/`getCause`/`assertThatCode`, antériorité ch.1-4, **zéro I/O**). Produit en **ultracode** (3 agents : Sonnet ×2, Opus pour 5.1.2). Solutions validées en local (publics+privés : 17/16/17 tests) ; starters compile-mais-échouent (TDD) ; lint 51 exos / 0 erreur. Spec [`specs/2026-06-04-m5-exos-5-1-design.md`](superpowers/specs/2026-06-04-m5-exos-5-1-design.md), plan [`plans/2026-06-04-m5-exos-5-1.md`](superpowers/plans/2026-06-04-m5-exos-5-1.md).
        - ✅ **5.2 Lecture/écriture de fichiers** (4 exos, **PR #45 mergée**) : `5.2.1-compteur-lignes` (`Files.lines`+try-with-resources, total/non-vides), `5.2.2-copie-fichier` (`Files.copy`+`REPLACE_EXISTING`, octet-exact), `5.2.3-journal-evenements` (append-only `StandardOpenOption.CREATE`+`APPEND`, `lire` vide si absent), `5.2.4-lecture-config` (parse `clé=valeur` **à la main**, sans `java.util.Properties` — réservé ch.7). Premier sous-groupe **I/O** : NIO.2 imposé, tests par **`@TempDir`** (UTF-8 + `\n` explicites, assertion sur le contenu), méthodes `throws IOException`. Produit en **ultracode** (4 agents : Sonnet ×3, Opus pour 5.2.4). Solutions vertes 16 tests chacune ; starters compilent ; lint 55 exos / 0 erreur. Spec [`specs/2026-06-04-m5-exos-5-2-design.md`](superpowers/specs/2026-06-04-m5-exos-5-2-design.md), plan [`plans/2026-06-04-m5-exos-5-2.md`](superpowers/plans/2026-06-04-m5-exos-5-2.md).
        - ✅ **5.3 Parsing et formats** (3 exos, branche `feature/m5-exos-5-3`, **PR à ouvrir/merger**) : `5.3.1-import-csv-personnel` (CSV → `List<Personnel>` ; `enum Grade`+`record Personnel` fournis ; lève `IllegalArgumentException` **cause chaînée** sur ligne/grade/ancienneté invalide — pont formats↔exceptions), `5.3.2-export-csv-trie` (tri `Comparator` ancienneté↓/nom↑ + `String.join` + `Files.write`, copie défensive), `5.3.3-parseur-commandes` (texte pur : `split`+`switch` dispatch, lève sur commande inconnue/arguments invalides). Synthèse du module (CSV à la main, NIO.2, `Comparator`, exceptions). Produit en **ultracode** (3 agents : Opus pour 5.3.1, Sonnet ×2). Solutions vertes 16/16/17 tests ; starters compilent ; lint 58 exos / 0 erreur. Spec [`specs/2026-06-04-m5-exos-5-3-design.md`](superpowers/specs/2026-06-04-m5-exos-5-3-design.md), plan [`plans/2026-06-04-m5-exos-5-3.md`](superpowers/plans/2026-06-04-m5-exos-5-3.md).
- **#22 — Module 6 (Tests et Git)** (8 chapitres, 7 exercices, 2 sous-groupes) → débloque le **Projet binôme #3** (final). Brief : [`superpowers/2026-06-08-m6-kickoff.md`](superpowers/2026-06-08-m6-kickoff.md) ; conception : spec [`specs/2026-06-08-m6-chapitres-design.md`](superpowers/specs/2026-06-08-m6-chapitres-design.md) + plan [`plans/2026-06-08-m6-chapitres.md`](superpowers/plans/2026-06-08-m6-chapitres.md). **Difficulté propre au module : la méta-circularité** (on enseigne JUnit/Git que la moulinette utilise déjà) — levier pédagogique assumé aux ch.6-1/6-6, sans spoiler l'implémentation. **3 décisions formateur figées (spec §10)** : grading 6.1 « mutation maison » (les tests stagiaire doivent tuer des mutants cachés) ; grading 6.2 par assertions sur l'état d'un repo-bac-à-sable isolé ; chapitres en **1 PR**.
    - **Volet chapitres** ✅ **PRODUIT ET MERGÉ** ([PR #49](https://github.com/Benjamin-Curlier/Piscine-ETNC/pull/49), CI 4/4) : 8 chapitres `6-1`→`6-8` sous `courses/docs/module-6-tests-git/` (`6-1` pourquoi tester, `6-2` JUnit 5 bases, `6-3` JUnit 5 avancé, `6-4` AssertJ, `6-5` TDD, `6-6` Git fondamentaux, `6-7` branches/conflits, `6-8` workflow PR), `_category_.json` `collapsed:false`, liaison `5-7`→`6-1`, **`6-8` clôt le parcours** (renvoi accueil, pas de « prochain chapitre »). Build Docusaurus vert ; extraits de test (ch.6-2→6-5) compilés au JDK 25 + JUnit 5.11.4/AssertJ 3.26.3 ; commandes git (ch.6-6→6-8) vérifiées sur dépôt-jouet (conflit reproduit, marqueurs conformes). Produit **inline**.
    - **Volet exercices** (2 sous-groupes, 1 PR chacun) : ✅ **6.1 Tests JUnit 5 PRODUIT** (branche `feature/m6-exos-6-1`, **PR à ouvrir/merger**) — 4 exos « écriture de tests » gradés par mutation (#57) : `6.1.1-tests-classe-existante` (Temperature), `6.1.2-tests-parametres` (`@CsvSource` imposé, Classement), `6.1.3-calculatrice-tdd` (4 ops + division/0 qui lève), `6.1.4-refactor-sous-tests` (Rectangle à invariant). Spec [`specs/2026-06-08-m6-exos-6-1-design.md`](superpowers/specs/2026-06-08-m6-exos-6-1-design.md). Chaque suite modèle vérifiée : passe sur le correct + tue ses 3 mutants ; lint adapté (layout écriture-de-tests) 62 exos / 0 erreur. ✅ **6.2 Git en pratique PRODUIT** (branche `feature/m6-exos-6-2`, **PR à ouvrir/merger**) — 3 exos « le stagiaire écrit du Java qui pilote git » (helper `GitCommandes` fourni), gradés par **assertions sur l'état d'un repo-jouet `@TempDir`** (checkers normaux, zéro infra nouvelle) : `6.2.1-commit-propre` (2 commits atomiques), `6.2.2-resolution-conflit` (retirer les marqueurs + conclure le merge, commit à 2 parents), `6.2.3-pull-request-fictive` (branche→commit→merge dans main + `PULL_REQUEST.md`). Spec [`specs/2026-06-08-m6-exos-6-2-design.md`](superpowers/specs/2026-06-08-m6-exos-6-2-design.md). Chaque solution vérifiée 4-5/5 (publics+privés) vs vrai git ; lint 65 exos / 0 erreur. **→ Module 6 entièrement produit (8 chapitres + 7 exos).** _Reste : extension `valider-solutions.sh` (boucle sur `mutants/`) en tâche dédiée._

### #57 — MutationChecker : grader les tests écrits par le stagiaire (extension moulinette)
**Statut** : **PRODUIT** (branche `feature/mutation-checker`, **PR à ouvrir/merger**). Implémenté en **TDD** : kickoff + spec [`specs/2026-06-08-mutation-checker-design.md`](superpowers/specs/2026-06-08-mutation-checker-design.md) + plan. Contrat `Checker.appliesTo` + filtrage `MoulinetteRunner.Default` + `MutationChecker` (tests stagiaire × impl correcte = PASS, × mutants = FAIL) + gating des checkers normaux + wiring `Main` + doc `format-exercice.md` §11bis. **Verdict binaire + feedback proportionnel** (le modèle `CheckResult` n'a pas de note fractionnaire — spec §3). Vérifié : `mvn verify` 33/33 + suites `tools`+`e2e` 15/15 (MutationCheckerIT 5, non-régression TestCheckersIT/HappyPathE2EIT). _Reste : extension `valider-solutions.sh` (boucle sur `mutants/`) au cycle 6.1._
**Priorité** : Haute — **bloque le sous-groupe d'exos 6.1**, lui-même sur le chemin du Projet binôme #3.
**Pré-requis** : #46 (Public/PrivateTestChecker)
**Pourquoi** : en 6.1 le livrable du stagiaire est **le test**, pas le code. La moulinette grade aujourd'hui l'inverse (tests de référence × code stagiaire). Il faut exécuter les tests **du stagiaire** contre une impl correcte (doit passer) et des **mutants** cachés (doivent échouer = test « tué »). Décision formateur 2026-06-08 : construire le mécanisme proprement.
**Livrable** : contrat `Checker.appliesTo(ctx)` (défaut `true`) + filtrage du pipeline dans `MoulinetteRunner.Default` + nouveau `MutationChecker` (console/checkers) + layout d'exo « écriture de tests » (`mutants/` + impl correcte fournie) + extension `format-exercice.md`. **Implémenté en TDD** (`@Tag("tools")`), CI vert. Détails : voir kickoff §2-§4.

### #23 — Projet binôme #1 — Mini-domaine OO (Caserne)
**Pré-requis** : #19
Voir `referentiel.md` §5. ✅ **Fiche livrée** dans `exercises/projets-binome/projet-1-mini-domaine/` (sujet, consignes, grille, métadonnées). Conception : spec [`specs/2026-06-03-projet-binome-1-design.md`](superpowers/specs/2026-06-03-projet-binome-1-design.md) + plan [`plans/2026-06-03-projet-binome-1.md`](superpowers/plans/2026-06-03-projet-binome-1.md).

### #24 — Projet binôme #2 — App + persistance fichier (TaskManager CLI)
**Pré-requis** : #21
Voir `referentiel.md` §5. ✅ **Fiche livrée** dans `exercises/projets-binome/projet-2-persistance/` (sujet, consignes, grille, métadonnées). Conception : spec [`specs/2026-06-05-projet-binome-2-design.md`](superpowers/specs/2026-06-05-projet-binome-2-design.md) + plan [`plans/2026-06-05-projet-binome-2.md`](superpowers/plans/2026-06-05-projet-binome-2.md).

### #25 — Projet binôme #3 — Mini-moulinette testée (final)
**Pré-requis** : #22
**Statut** : ✅ **Fiche livrée** (branche `feature/projet-binome-3`, **PR à ouvrir/merger**) dans `exercises/projets-binome/projet-3-mini-moulinette/` (sujet, consignes, grille, métadonnées). **Mini-moulinette pédagogique** : pipeline compiler→exécuter→rapport sur un `Operation.java` à contrat fixe (`appliquer(int):int`), cas lus depuis `cas.csv` (à la main), **tests JUnit ≥70 % couverture** + **workflow Git branches/PR** exigés (premier projet). Synthèse OO/persistance/tests/Git. Conception : spec [`specs/2026-06-08-projet-binome-3-design.md`](superpowers/specs/2026-06-08-projet-binome-3-design.md). **→ Phase 3 TERMINÉE** (modules 2-6 + 3 projets binôme).

---

## Phase 4 — Production & déploiement militaire (priorité BASSE pour l'instant)

> Travaux d'infrastructure pour la mise en service réelle. À ne pas démarrer avant que le contenu pédagogique soit substantiellement avancé (objectif : module 1 et 2 complets minimum).

### #26 — Migration GitLab CE on-premise
**Pourquoi** : production cible. GitHub n'est utilisé que pour la phase de dev.
**Inclut** : config GitLab CI/CD miroir du GitHub Actions, runners on-premise, configuration projet.

### #27 — Auth LDAP / AD militaire
**Pré-requis** : interlocuteur infra militaire identifié.

### #28 — Mirror Maven interne (Nexus / Artifactory)
**Pourquoi** : pas d'accès direct à Maven Central depuis l'environnement militaire.

### #29 — Anti-triche (JPlag entre soumissions)
**Pré-requis** : volume suffisant d'exercices (≥ module 1 + 2 complets).
**Livrable** : intégration JPlag dans la pipeline post-rendu, journalisation des similarités > seuil.

### #30 — Durcissement runner Docker
**Pré-requis** : #9 (squelette moulinette).
**Inclut** : `eclipse-temurin:25-jdk` durci, `--network=none`, FS read-only, limites CPU/RAM, seccomp.

### #31 — Conformité Diffusion Restreinte (DR)
**Pré-requis** : confirmation RSSI ETNC.
**Inclut** : revue de toutes les dépendances tierces, journalisation, chiffrement au repos.

---

## Tâches transverses / continues

### #32 — Maintenance et évolution
- Mise à jour Java LTS (prochaine en sept. 2027 — Java 29).
- Mise à jour Docusaurus (release mineure ~tous les 2-3 mois).
- Veille pédagogique : retours stagiaires → ajustements de chapitres / exercices.

### #33 — Branding ETNC
**Statut** : À faire
**Priorité** : Basse
Remplacer les assets Docusaurus par défaut (favicon, logo, social card) par les visuels ETNC.

---

## Phase 1ter — Resynchronisation & hygiène (ajoutées le 2026-06-01)

> Issues de la remontée d'action. La #52 est sur le chemin critique du MVP (étape 2) ; les autres sont de l'hygiène à caser quand pratique.

### #52 — PR + merge `feature/mvp-console-correction` → `main`
**Statut** : **Faite** — [PR #1](https://github.com/Benjamin-Curlier/Piscine-ETNC/pull/1) mergée (merge commit `c86e098`), branche supprimée. La target-MVP est exécutée depuis `feature/target-mvp-checkers` (issue de `main`).
**Priorité** : **Haute — étape 2 de la roadmap MVP.**
**Pré-requis** : #11a (CI minimal en place pour valider la PR)
**Pourquoi** : tout le MVP vit sur une branche de feature longue durée ; `main` est resté au squelette. L'écart grandit à chaque itération. Resynchroniser pour que `main` reflète la réalité et que les prochaines itérations branchent proprement.

**Critères d'acceptation** :
- [ ] Une PR `feature/mvp-console-correction → main` est ouverte, le CI #11a est vert dessus.
- [ ] La PR est mergée (merge ou squash selon la convention #14 à venir).
- [ ] La target-MVP (#43–#51) est exécutée depuis une **nouvelle branche issue de `main`** (mettre à jour la section « Démarrage » du plan target-MVP en conséquence).

### #53 — Durcir le style (advisory → bloquant) après la beta
**Statut** : À faire
**Priorité** : Basse
**Pré-requis** : #47 (StyleChecker), retours beta stagiaires
**Pourquoi** : le `StyleChecker` est livré en mode **advisory** (non bloquant) pour la beta afin de ne pas frustrer les stagiaires (« mes tests passent mais c'est rouge à cause d'une accolade »). Une fois la beta stabilisée, repasser le style en bloquant (ou introduire un seuil).

**Critères d'acceptation** :
- [ ] Le caractère bloquant/advisory du style est configurable (pas de re-code en dur).
- [ ] Décision documentée après retours stagiaires.

### #54 — Committer le `maven-wrapper.jar` (le dé-ignorer)
**Statut** : Faite — branche `chore/commit-maven-wrapper` (PR à merger). Négation `!.mvn/wrapper/maven-wrapper.jar` dans `.gitignore` (le `*.jar` global l'attrapait) + jar versionné.
**Priorité** : Basse
**Pré-requis** : #10
**Pourquoi** : versionner `.mvn/wrapper/maven-wrapper.jar` rend `./mvnw` fonctionnel **offline**, sans dépendre du réseau (pratique standard pour des builds reproductibles, pertinent vu le réseau restreint côté formateur et la cible militaire).

**Critères d'acceptation** :
- [x] `.mvn/wrapper/maven-wrapper.jar` retiré du `.gitignore` et committé.
- [x] `./mvnw -v` fonctionne sur une machine sans accès réseau Maven (le jar versionné évite le téléchargement initial).

### #55 — Migrer `ReportGenerator` JSON vers Jackson
**Statut** : À faire
**Priorité** : Basse
**Pré-requis** : #48 (rapport câblé), format JSON stabilisé
**Pourquoi** : le JSON de `ReportGenerator` est fabriqué à la main (déjà noté dans le code). Migrer vers Jackson quand le format se stabilise — plus robuste (échappement, structures imbriquées). Pas urgent.

**Critères d'acceptation** :
- [ ] Jackson ajouté en dépendance (dependency management) et utilisé dans `reports`.
- [ ] Sérialisation identique (tests de non-régression sur le JSON produit).

---

## Légende des priorités

- **Haute** : bloque ou prépare immédiatement la suite (outillage de phase 1, contenu pilote du module 1).
- **Moyenne** : à programmer dans les sessions suivantes une fois la phase 1 stable.
- **Basse** : à valeur produite faible tant que le contenu pédagogique n'est pas substantiel, ou dépendant d'interlocuteurs externes.

---

*Backlog maintenu par l'équipe formateurs. Une session peut ajouter une nouvelle tâche en fin de section appropriée avec un ID continu et un statut `À faire`.*
