# Architecture de la moulinette ETNC (pour les développeurs)

## Vue d'ensemble

Projet Gradle multi-module sous `moulinette/` (wrapper versionné `moulinette/gradlew` ; les exercices restent des projets Maven) :

| Module | Rôle |
|---|---|
| `framework` | Contrats : `Checker`, `CheckerContext`, `CheckResult`, `EvaluationReport`. |
| `runner` | `ProcessRunner` : exécution de commandes système en sous-processus (timeout 30 s). |
| `reports` | `ReportGenerator` : rapport Markdown + JSON depuis un `EvaluationReport`. |
| `cli` | Point d'entrée headless (CI interne uniquement, non livré au stagiaire) : `run --exo X --rendu Y`. |
| `console` | Bibliothèque `ConsoleSession` (git sandbox + évaluation locale) + REPL terminal legacy. |
| `gui` | Point d'entrée stagiaire v1 : serveur HTTP local (`127.0.0.1`) + terminal web xterm.js, client de `ConsoleSession`. |

Dépendances : `console`/`cli` → `framework` + `runner` + `reports`. Pas de cycle.

## Deux points d'entrée

- **`cli`** : usage machine/CI, sans interaction.
- **`console`** : REPL git sandbox pour le stagiaire (commandes `add/commit/push/...`, `submit-start`). Un `git push origin rendu/<sous-groupe>` déclenche l'évaluation.

La logique console est portée par la façade **`ConsoleSession`** (bibliothèque) :
`ConsoleSession.open(repo, piscineRepo)` fait tout le câblage (git, catalogue, checkers,
runner, trigger) et `execute(ligne) → CommandResult` tokenise puis dispatch. Le REPL
terminal (`Repl`) n'est qu'un client ; la GUI v1 en sera un autre.

## Flux d'évaluation (`console`)

```
ConsoleSession.execute("git push origin rendu/1.1")
  → PushCommand → SubmissionTrigger.onPushSucceeded
      → MoulinetteRunner.Default.runGroup("1.1", workspace)
          pour chaque exo (difficulté croissante) :
            checkers filtrés par Checker.appliesTo(ctx) (défaut true), puis :
            CompileChecker → PublicTestChecker → PrivateTestChecker → MutationChecker → StyleChecker
            (arrêt au 1er Checker BLOQUANT non-OK ; arrêt au 1er exo non-OK)
            (le StyleChecker est advisory en beta : isBlocking()==false → rapporté mais ne bloque pas)
            → EvaluationReport → ReportGenerator (md + json) dans .piscine/reports/
```

### Exercices « écriture de tests » (sous-groupe 6.1)

Pour ces exos, le livrable du stagiaire est **le test**, pas le code (cf. `format-exercice.md` §11bis). La sélection des checkers est **dépendante de l'exo** via `Checker.appliesTo(ctx)` :

- Détection : présence d'un dossier `mutants/` dans l'exo de référence (`MutationChecker.estEcritureDeTests`).
- Sur ces exos : seul le **`MutationChecker`** s'applique ; `CompileChecker`/`PublicTestChecker`/`PrivateTestChecker` renvoient `appliesTo == false`.
- Le `MutationChecker` compile le **test du stagiaire** et l'exécute contre l'impl **correcte** de référence (doit passer) puis contre chaque impl **mutée** (`mutants/<id>/`, doit échouer = mutant « tué »). Verdict **binaire** (OK ssi tous les mutants tués) ; le détail proportionnel (`N/total`) vit dans le message.

```
      → récap console + chemin du rapport
```

## L'astuce « l'uber-jar EST le classpath »

La console est packagée en uber-jar (plugin Shadow, tâche `:console:shadowJar`) qui embarque JUnit, AssertJ et Checkstyle (déclarés en `runtimeOnly`). À l'exécution, les Checkers compilent et lancent le code stagiaire avec ce **même jar** comme classpath : aucune dépendance réseau, aucun Maven requis sur le PC stagiaire.

- `JavaToolkit.toolingClasspath()` renvoie le chemin de l'uber-jar quand on tourne depuis un `.jar` ; sinon (dev/tests Gradle) il retombe sur `System.getProperty("java.class.path")`, qui contient déjà ces dépendances (`runtimeOnly` ⇒ présent au classpath de test).
- Les outils sont invoqués par nom de classe : `org.junit.platform.console.ConsoleLauncher`, `com.puppycrawl.tools.checkstyle.Main`.
- Le shade exclut les fichiers de signature des dépendances (`META-INF/*.SF|DSA|RSA`) : Checkstyle et ses transitives sont signées, et sans ce filtre le manifeste fusionné lève `Invalid signature file digest`.

## Ajouter un Checker

1. Implémenter `framework.Checker` (méthode `id()` + `check(CheckerContext)`).
2. Utiliser `JavaToolkit` pour compiler/exécuter au besoin ; renvoyer `CheckResult.ok()/fail(...)/error(...)`.
3. L'enregistrer dans `Main.runRepl` (liste `checkers`, dans l'ordre voulu).
4. Pour un Checker **non bloquant** (advisory), redéfinir `default boolean isBlocking()` à `false` : son échec est rapporté mais n'arrête pas la chaîne ni ne fait échouer l'exo (cas du `StyleChecker` en beta).
5. Pour un Checker **spécifique à un type d'exo**, redéfinir `default boolean appliesTo(CheckerContext)` : `MoulinetteRunner.Default` filtre la liste dessus avant exécution (cas du `MutationChecker`, actif seulement sur les exos « écriture de tests »).

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

| Suite | Tag | Lancer (depuis la racine) |
|---|---|---|
| Unitaires | (aucun) | `moulinette/gradlew -p moulinette :console:test` |
| Intégration git | `git` | `moulinette/gradlew -p moulinette :console:testGit` |
| Outillage (compile/test/style réels) | `tools` | `moulinette/gradlew -p moulinette :console:testTools` |
| Bout en bout | `e2e` | `moulinette/gradlew -p moulinette :console:testE2e` |

La tâche `test` par défaut exclut les tags `git`, `e2e`, `tools` (les suites lourdes ne tournent qu'à la demande, via leurs tâches dédiées). Le CI GitHub Actions (#11a) exécute les quatre suites.

## Limites connues & dette

- **Style advisory en beta** : le `StyleChecker` est non bloquant (`isBlocking()==false`) ; durcissement prévu tâche #53.
- **PMD** différé (le `StyleChecker` est prévu pour l'accueillir ; Checkstyle seul aujourd'hui).
- **Note pondérée /20 + seuil** : pas encore calculée (pass/fail par Checker).
- **`ReportGenerator` JSON fait main** : migration vers Jackson prévue (tâche #55) quand le format se stabilise.
- **Anti-triche** (tests-prives visibles dans le bundle) : tâche #29.
- **Isolation Docker** du runner : tâche #30.
- **Mode `nominal`** (plateforme serveur) : **abandonné en v1** (piscine 100 % locale, auto-rythmée) ; l'enum `Mode` ne garde que `LOCAL`.
