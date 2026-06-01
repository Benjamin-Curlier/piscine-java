# Architecture de la moulinette ETNC (pour les développeurs)

## Vue d'ensemble

Projet Maven multi-module sous `moulinette/` :

| Module | Rôle |
|---|---|
| `framework` | Contrats : `Checker`, `CheckerContext`, `CheckResult`, `EvaluationReport`. |
| `runner` | `ProcessRunner` : exécution de commandes système en sous-processus (timeout 30 s). |
| `reports` | `ReportGenerator` : rapport Markdown + JSON depuis un `EvaluationReport`. |
| `cli` | Point d'entrée headless (CI) : `run --exo X --rendu Y`. |
| `console` | Point d'entrée interactif stagiaire : REPL git + évaluation locale. |

Dépendances : `console`/`cli` → `framework` + `runner` + `reports`. Pas de cycle.

## Deux points d'entrée

- **`cli`** : usage machine/CI, sans interaction.
- **`console`** : REPL git sandbox pour le stagiaire (commandes `add/commit/push/...`, `submit-start`). Un `git push origin rendu/<sous-groupe>` déclenche l'évaluation.

## Flux d'évaluation (`console`)

```
git push origin rendu/1.1
  → PushCommand → SubmissionTrigger.onPushSucceeded
      → MoulinetteRunner.Default.runGroup("1.1", workspace)
          pour chaque exo (difficulté croissante) :
            CompileChecker → PublicTestChecker → PrivateTestChecker → StyleChecker
            (arrêt au 1er Checker BLOQUANT non-OK ; arrêt au 1er exo non-OK)
            (le StyleChecker est advisory en beta : isBlocking()==false → rapporté mais ne bloque pas)
            → EvaluationReport → ReportGenerator (md + json) dans .piscine/reports/
      → récap console + chemin du rapport
```

## L'astuce « l'uber-jar EST le classpath »

La console est packagée en uber-jar (maven-shade) qui embarque JUnit, AssertJ et Checkstyle (déclarés en scope `runtime`). À l'exécution, les Checkers compilent et lancent le code stagiaire avec ce **même jar** comme classpath : aucune dépendance réseau, aucun Maven requis sur le PC stagiaire.

- `JavaToolkit.toolingClasspath()` renvoie le chemin de l'uber-jar quand on tourne depuis un `.jar` ; sinon (dev/surefire) il retombe sur `System.getProperty("java.class.path")`, qui contient déjà ces dépendances (scope `runtime` ⇒ présent au classpath de test).
- Les outils sont invoqués par nom de classe : `org.junit.platform.console.ConsoleLauncher`, `com.puppycrawl.tools.checkstyle.Main`.
- Le shade exclut les fichiers de signature des dépendances (`META-INF/*.SF|DSA|RSA`) : Checkstyle et ses transitives sont signées, et sans ce filtre le manifeste fusionné lève `Invalid signature file digest`.

## Ajouter un Checker

1. Implémenter `framework.Checker` (méthode `id()` + `check(CheckerContext)`).
2. Utiliser `JavaToolkit` pour compiler/exécuter au besoin ; renvoyer `CheckResult.ok()/fail(...)/error(...)`.
3. L'enregistrer dans `Main.runRepl` (liste `checkers`, dans l'ordre voulu).
4. Pour un Checker **non bloquant** (advisory), redéfinir `default boolean isBlocking()` à `false` : son échec est rapporté mais n'arrête pas la chaîne ni ne fait échouer l'exo (cas du `StyleChecker` en beta).

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

| Suite | Tag | Lancer |
|---|---|---|
| Unitaires | (aucun) | `mvn -f moulinette/pom.xml -pl console -am test` |
| Intégration git | `git` | `... -Dsurefire.excludedGroups=e2e,tools -Dgroups=git` |
| Outillage (compile/test/style réels) | `tools` | `... -Dsurefire.excludedGroups=git,e2e -Dgroups=tools` |
| Bout en bout | `e2e` | `... -Dsurefire.excludedGroups=git,tools -Dgroups=e2e` |

Exclusions par défaut : `surefire.excludedGroups = git,e2e,tools` (les suites lourdes ne tournent qu'à la demande). Le CI GitHub Actions (#11a) exécute les quatre suites.

**Maven** : `mvnw` peut échouer à télécharger Maven dans un réseau restreint ; préférer un `mvn` système si présent (les scripts le font).

## Limites connues & dette

- **Style advisory en beta** : le `StyleChecker` est non bloquant (`isBlocking()==false`) ; durcissement prévu tâche #53.
- **PMD** différé (le `StyleChecker` est prévu pour l'accueillir ; Checkstyle seul aujourd'hui).
- **Note pondérée /20 + seuil** : pas encore calculée (pass/fail par Checker).
- **`ReportGenerator` JSON fait main** : migration vers Jackson prévue (tâche #55) quand le format se stabilise.
- **Anti-triche** (tests-prives visibles dans le bundle) : tâche #29.
- **Isolation Docker** du runner : tâche #30.
- **Mode `nominal`** (plateforme serveur) : rejeté pour l'instant (tâches #26-#28).
