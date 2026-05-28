# MVP — Console de correction locale via REPL git

**Date** : 2026-05-28
**Auteur** : itération MVP Piscine ETNC
**Statut** : spec validée, prête pour planification

---

## 1. Objectif de l'itération

Permettre à un stagiaire de **faire la Piscine ETNC en local**, sans plateforme serveur, en utilisant une console interactive qui :

1. génère son repo de travail (un par stagiaire),
2. accueille un REPL où il saisit des commandes git réelles,
3. déclenche **automatiquement** la moulinette sur un sous-groupe d'exercices lorsque la séquence git attendue est observée (`add` → `commit` → `push` sur la branche `rendu/<sous-groupe>`),
4. **stoppe la séquence au premier exercice qui échoue** — le stagiaire doit progresser linéairement par difficulté croissante.

Cette itération est la première rampe utilisable bout en bout. Elle ne couvre ni le déploiement, ni la triche, ni le multi-utilisateur — voir §9 (hors MVP).

---

## 2. Décisions structurantes (validées en brainstorming)

| # | Décision | Alternative écartée | Raison |
|---|----------|---------------------|--------|
| D1 | REPL interne sandbox git | Wrapper / hook git | Permet feedback pédagogique sur chaque commande |
| D2 | Trigger = `add` + `commit` + `push` sur `rendu/<groupe>` | Tag, branche seule, message commit | Enseigne la séquence git « classique » |
| D3 | Remote = bare local `file://.piscine/remote.git` | Push simulé, GitHub réel | Vrai push, 100 % local, p&eacute;dagogique honnête |
| D4 | Mode groupe = séquence par difficulté croissante, stop au 1er échec | Tout en parallèle, diff-only | Force la progression linéaire et donne un feedback ciblé |
| D5 | Layout = repo stagiaire séparé, généré par `init` | Édition dans le repo Piscine | Sépare référentiel et travail stagiaire |
| D6 | Commandes git supportées = `add`, `commit`, `push`, `status`, `log`, `diff` | Set étendu, passe-plat total | Minimum pour le MVP ; toute autre → message « non supportée » |
| D7 | Nouveau module Maven `moulinette/console` à côté de `cli` | Étendre `cli`, app séparée | `cli` reste headless (CI), `console` est interactif (stagiaire) |
| D8 | Notion de `Mode` (`local` \| `nominal`) câblée dès le MVP | Tout coder en dur `local` | Évite une refacto brutale quand le mode nominal (plateforme) arrivera |
| D9 | Bootstrap depuis le repo Piscine ETNC via `scripts/piscine-bootstrap.{sh,ps1}` | Demander au stagiaire d'enchaîner manuellement `mvn package` puis `java -jar` | Autonomie : 1 commande après `git clone` |

---

## 3. Architecture

### Modules Maven

```
moulinette/
├── framework/    (existant) — Checker API, modèles de rapport
├── runner/       (existant) — exécution sandboxée
├── reports/      (existant) — génération Markdown/JSON
├── cli/          (existant) — entry headless `run --exo X --rendu Y` (pour CI)
└── console/      (NOUVEAU) — entry interactif stagiaire (REPL)
```

`moulinette/console` dépend de `framework`, `runner`, `reports`. Pas de dépendance vers `cli` ; les deux sont des entry points pairs.

### Modes d'exécution

```java
public enum Mode { LOCAL, NOMINAL }
```

Le MVP **implémente uniquement `LOCAL`**. `NOMINAL` (déploiement plateforme avec auth, rendu serveur, etc.) est référencé partout où l'implémentation diverge — typiquement via une interface `SubmissionBackend` dont seule `LocalSubmissionBackend` (bare remote `file://`) existe au MVP. Toute commande `Main` accepte un flag `--mode local` (par défaut). Un appel avec `--mode nominal` retourne une erreur explicite « non implémenté dans le MVP, voir tâche #X ».

### Packages internes

| Package | Rôle |
|---------|------|
| `console` | `Main` — parse `init` \| `repl`, délègue |
| `console.workspace` | `WorkspaceInitializer`, `ExerciseCatalog` — création du repo stagiaire, scan du catalogue d'exos |
| `console.git` | `GitClient` (wrapper sous-process `git`) — abstraction testable |
| `console.commands` | Une classe par commande supportée, implémente `Command` |
| `console.repl` | `Repl` — boucle read/dispatch/print, `ReplContext` |
| `console.trigger` | `SubmissionTrigger` — détecte la séquence valide et appelle `MoulinetteRunner` |

### Flux de dépendances

```
Main → repl, workspace
repl → commands, git
commands → git, trigger
trigger → workspace (catalog), MoulinetteRunner (framework+runner+reports)
```

Aucun cycle. Chaque package a une seule raison de changer.

---

## 4. Contrats publics

```java
// console.workspace
public interface WorkspaceInitializer {
  Workspace init(InitRequest req);
}
public record InitRequest(String nom, Path dest, String moduleInitial);
public record Workspace(Path root, Path bareRemote, Path repoRoot);

// console.git
public interface GitClient {
  GitResult run(Path repo, List<String> args);
  String currentBranch(Path repo);
  List<RefUpdate> lastPushRefs(Path repo);
}

// console.commands
public interface Command {
  String name();
  CommandResult execute(ReplContext ctx, List<String> args);
}

// console.trigger
public interface MoulinetteRunner {
  GroupReport runGroup(String sousGroupe, Path repoRoot);
}
```

`MoulinetteRunner` est le **seul** pont entre `console` et le reste de la moulinette. Tout le couplage à `framework`/`runner`/`reports` est contenu dans son implémentation.

---

## 5. Flux d'usage

### Bootstrap depuis le repo Piscine ETNC (stagiaire en autonomie)

Le stagiaire reçoit l'URL du repo Piscine ETNC. Il fait :

```
git clone <url-piscine-etnc> piscine-etnc
cd piscine-etnc
./scripts/piscine-bootstrap.sh --nom curlier        # ou .ps1 sous Windows
```

Le script :
1. vérifie les prérequis (Java 25, git) — message clair si absent + lien vers `docs/setup-dev.md`,
2. build `moulinette-console` via `./mvnw -f moulinette/pom.xml -pl console -am package`,
3. crée un workspace stagiaire à côté du repo : `../piscine-<nom>/`,
4. y exécute `init --mode local` (commit initial, bare remote, starters du module 1),
5. affiche la commande pour lancer le REPL : `java -jar … repl --repo ../piscine-curlier`.

Idempotent : relancé, il détecte le workspace existant et propose `--force` ou `--resume`.

### Initialisation (one-shot)

```
$ java -jar moulinette-console.jar init --nom curlier --dest ~/piscine-curlier
[console] Génère le repo stagiaire dans ~/piscine-curlier ...
[console]   ✓ git init
[console]   ✓ copie des starters (module 1 → sous-groupe 1.1)
[console]   ✓ remote 'origin' = file://~/piscine-curlier/.piscine/remote.git (bare)
[console]   ✓ commit initial 'piscine: setup'
[console] Prêt. Lance la console avec : moulinette console --repo ~/piscine-curlier
```

### Session REPL

```
$ java -jar moulinette-console.jar repl --repo ~/piscine-curlier
piscine[main]> status
On branch main
Sous-groupe actuel : 1.1 — Premiers pas (0/2 rendus)

piscine[main]> submit-start 1.1
[console] Bascule sur la branche rendu/1.1.

piscine[rendu/1.1]> git add exercises/1.1.1-hello-world
piscine[rendu/1.1]> git commit -m "rendu 1.1.1"
piscine[rendu/1.1]> git push origin rendu/1.1
[console] Push détecté sur rendu/1.1 → lancement moulinette sur sous-groupe 1.1
[console] ▶ Exo 1.1.1 hello-world ...... ✓ OK
[console] ▶ Exo 1.1.2 affichage-formate . ✗ ÉCHEC
[console] Rapport : ~/piscine-curlier/.piscine/reports/1.1-2026-05-28-1437.md
[console] On s'arrête ici (l'exo 1.1.2 doit passer avant 1.1.3). Corrige et re-push.
```

`submit-start <groupe>` est une commande **du REPL** (pas git) qui crée/bascule sur `rendu/<groupe>` — évite d'enseigner `checkout -b` avant le chapitre dédié.

---

## 6. Gestion d'erreurs

| Catégorie | Exemple | Traitement |
|-----------|---------|------------|
| Commande inconnue | `git checkout` | « non supportée dans le MVP » + suggestion (`submit-start`) |
| Commande malformée | `git commit` sans `-m` | Message pédagogique : « un commit a besoin d'un message » |
| État git incohérent | `push` sans commit, branche hors convention | Explique la convention `rendu/<groupe>` |
| Échec d'un exo | Test privé KO sur 1.1.2 | Sortie nominale : ✗, stoppe la séquence, pointe le rapport |
| Erreur infra | Bare remote corrompu, droits FS | Message + chemin du log détaillé |
| Bug interne | NPE dans le dispatch | Banner « bug interne », log complet, exit ≠ 0 |

Stacktrace masquée par défaut, visible avec `--debug`.

### Cas limites traités dans le MVP

- Repush sur un groupe déjà validé : rapport ré-écrit avec nouveau timestamp.
- `add` de fichiers hors du sous-groupe courant : accepté, mais `submit-start` avertit.
- Push refusé (non fast-forward) : message « supprime la branche et recommence avec `submit-start` ».
- Repo Piscine ETNC introuvable au `init` : erreur explicite.
- `git` ou Java absents au démarrage : check préalable + message clair.

### Logs et artefacts

- `~/piscine-<nom>/.piscine/logs/console.log` — Logback (SLF4J déjà choisi).
- `~/piscine-<nom>/.piscine/reports/<groupe>-<ts>.{md,json}`.

---

## 7. Stratégie de tests

### Unitaires (JUnit 5 + AssertJ)

- `commands/*` avec `FakeGitClient` : args manquants, malformés, succès, échec, contexte mis à jour.
- `repl.Repl` avec `BufferedReader` scripté + `StringWriter` : dispatch, prompt, commande inconnue.
- `trigger.SubmissionTrigger` : `rendu/1.1` déclenche, `main` non, séquence stoppée au 1er `Checker` KO.
- `workspace.ExerciseCatalog` : scan d'une arborescence factice (`@TempDir`), tri par `position`, regroupement par `sous_groupe`, `metadata.yml` invalide.

### Intégration git (vrai binaire, `@TempDir`, `@Tag("git")`)

- `ProcessGitClient` : init / commit / push vers bare local / lecture des refs poussées.
- `WorkspaceInitializer` : `init` complet → arborescence + bare + commit initial + remote.
- Scénario « submit groupe 1.1 » : `init` + REPL scripté + `Checker` factice → rapport généré.

### E2E smoke

- `HappyPathE2ETest` — `init` + REPL scripté résolvant l'exo 1.1.1 hello-world avec la *vraie* solution + vraie moulinette + vrai git → rapport ✓. Garde-fou anti-régression.

### Conventions

- Pas de mock du `framework` au-delà du strict nécessaire.
- `@TempDir` partout, jamais d'écriture dans `target/`.
- Tests `@Tag("git")` skippables via `-Dgroups='!git'`.
- Couverture cible : ≥ 80 % sur `console.commands` et `console.trigger`, le reste guidé par la valeur.

---

## 8. Critères d'acceptation de l'itération

- [ ] `mvn -f moulinette/pom.xml -pl console verify` passe.
- [ ] `java -jar moulinette-console.jar init --nom test --dest <tmp>` produit un workspace fonctionnel.
- [ ] Dans le REPL, la séquence `submit-start 1.1` → édition → `git add` → `git commit -m …` → `git push origin rendu/1.1` lance la moulinette sur le sous-groupe.
- [ ] Si un exo échoue, la séquence s'arrête et le rapport mentionne l'exo bloquant.
- [ ] Le rapport est écrit dans `.piscine/reports/<groupe>-<ts>.md`.
- [ ] Un E2E avec la solution de référence de 1.1.1 produit un rapport ✓.
- [ ] Toute commande hors `add|commit|push|status|log|diff` retourne un message « non supportée » utile.

---

## 9. Hors MVP (explicite)

| Sujet | Renvoi |
|-------|--------|
| Triche / similarité (JPlag) | Tâche backlog #29 |
| Sandbox Docker du runner | Tâche #30 |
| Multi-stagiaires sur même machine | Itération ultérieure |
| Reprise après crash mid-moulinette | Itération ultérieure |
| Auth LDAP / déploiement GitLab on-premise | Tâches #26, #27 |
| Mirror Maven interne | Tâche #28 |
| Branches `feature/*`, `merge`, `pull`, `clone` côté stagiaire | Itération « git avancé » |

---

## 10. Tâches backlog créées par cette itération

Voir `docs/backlog.md` — section MVP, tâches #34 à #41 (cf. §11 ci-dessous pour le découpage proposé).

## 11. Découpage en tâches (proposition pour backlog)

1. **#34** — Module Maven `moulinette/console` (squelette, dépendances, `Mode` enum, smoke test).
2. **#35** — `console.git.GitClient` + `ProcessGitClient` + tests intégration.
3. **#36** — `console.workspace` : `ExerciseCatalog` + `WorkspaceInitializer` + `LocalSubmissionBackend` + tests.
4. **#37** — `console.commands` + `console.repl` (commandes minimales, dispatch, prompt).
5. **#38** — `console.trigger.SubmissionTrigger` + `MoulinetteRunner` (pont vers framework).
6. **#39** — Sous-commande `init` + sous-commande `repl` dans `console.Main` (flag `--mode local`).
7. **#40** — Script `scripts/piscine-bootstrap.{sh,ps1}` + tests sur un repo factice.
8. **#41** — `docs/piscine-stagiaire.md` (guide autonome) + mention dans le `README.md` racine.
9. **#42** — E2E smoke « happy path » sur exo 1.1.1 via le bootstrap complet.
