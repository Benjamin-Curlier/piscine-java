# Spec — #57 MutationChecker (grader les tests écrits par le stagiaire)

> Design validé le 2026-06-08 (brainstorming formateur : option « construire le MutationChecker » + 3 forks tranchés « go avec recommandations »). Tâche backlog **#57**.
> Point de départ : [`docs/superpowers/2026-06-08-mutation-checker-kickoff.md`](../2026-06-08-mutation-checker-kickoff.md).
> Code de référence (lu) : `moulinette/console/src/main/java/etnc/piscine/moulinette/console/checkers/{AbstractTestChecker,PublicTestChecker,PrivateTestChecker,CompileChecker,JavaToolkit,FqcnExtractor}.java`, `framework/{Checker,CheckResult,CheckerContext}.java`, `console/trigger/MoulinetteRunner.java`.
> **Implémentation en TDD** (skill `superpowers:test-driven-development`).

## 1. Objectif et périmètre

Permettre à la moulinette de **grader des exercices où le livrable du stagiaire est le test** (sous-groupe 6.1). Le mécanisme : exécuter les tests **du stagiaire** contre une implémentation **correcte** de référence (doivent **passer**) et contre plusieurs implémentations **mutées** (doivent **échouer** — le test « tue » le mutant). Un test qui passe partout, mutants compris, ne capture rien.

**Hors périmètre** : les exos 6.1 eux-mêmes (cycle suivant), tout contenu pédagogique, le futur moteur de note /20 (n'existe pas encore — cf. §3).

## 2. Constat du code existant (ce sur quoi on s'appuie)

- `AbstractTestChecker.check(ctx)` : compile `renduPath/starter/src/main/java` (**code stagiaire**) → exécute les tests **de référence** (`exerciseRefPath/tests` ou `tests-prives`). Le couple est (code = stagiaire, tests = référence).
- Les 4 checkers (`compile`, `tests-publics`, `tests-prives`, `style`) sont une **liste figée** dans `Main.runRepl` (`List.of(...)`), exécutée pour **tout** exo par `MoulinetteRunner.Default` (boucle `for (Checker c : checkers)`).
- `JavaToolkit` fournit : `compile(List<Path> srcDirs, Path outDir, String cp)`, `runJUnit(Path workDir, String cp, List<String> selectClasses)` (via ConsoleLauncher, `--fail-if-no-tests`), `toolingClasspath()` (JUnit+AssertJ embarqués). `FqcnExtractor.fqcnsUnder(dir)` liste les FQCN d'un dossier.
- `CheckResult` = record `(Status {OK,FAIL,ERROR}, messages, suggestions)`. **Pas de score fractionnaire.**
- `CheckerContext` = record `(exerciseId, renduPath, exerciseRefPath)`. **Ne porte pas la metadata.**

## 3. 🔑 Contrainte découverte : verdict binaire, pas de note fractionnaire **[à acter]**

Le runtime **n'agrège pas** de note /20 : `MoulinetteRunner.Default` exécute chaque checker, stocke un `CheckResult` OK/FAIL/ERROR par `id`, et écrit un rapport **✓/✗ par checker**. Les poids d'`evaluation.yml` **ne sont pas consommés** par la moulinette (métadonnée pour un futur moteur de note ; le CI `valider-solutions` se contente de `mvn test`).

**Conséquence sur le fork #2 (« score proportionnel », validé)** : un score fractionnaire `mutants tués / total` **n'est pas représentable** dans le modèle actuel (`CheckResult` est binaire). Résolution fidèle retenue :
- Le `MutationChecker` rend **`OK` si et seulement si** : les tests stagiaire **passent sur l'impl correcte** **ET** **tuent TOUS les mutants** ; sinon **`FAIL`**.
- Le **caractère proportionnel vit dans le message** pédagogique : « 3/4 mutants détectés — le mutant `borne-inferieure` survit : ton test ne couvre pas la valeur limite. » C'est exactement le pattern de tous les checkers existants (verdict binaire + feedback riche).
- `evaluation.yml` de 6.1 peut déclarer un critère `mutants-tues` (poids 8) pour le **futur** moteur de note ; aujourd'hui son ✓/✗ reflète le verdict du checker.
- **Un vrai score fractionnaire** (note partielle) exigerait d'étendre `CheckResult` (champ `score` 0..1) **et** d'écrire le moteur de note — **hors périmètre #57**, candidat à une tâche future si le besoin se confirme à l'usage. *(Signalé au formateur ; implémentation fidèle = binaire + message proportionnel.)*

## 4. Design (additif, rétro-compatible)

### 4.1 Contrat `Checker.appliesTo` **[fork #1 résolu]**
Ajouter à l'interface `Checker` :
```java
/** Vrai si ce checker s'applique à l'exo décrit par ctx. Défaut : true (rétro-compatible). */
default boolean appliesTo(CheckerContext ctx) { return true; }
```
`MoulinetteRunner.Default` **filtre** la liste : `if (!c.appliesTo(ctx)) continue;` avant `c.check(...)`. Aucun impact sur les modules 1-5 (tous les checkers gardent le défaut `true`).

**Marqueur d'exo « écriture de tests »** : présence du dossier **`exerciseRefPath/mutants/`** (signal runtime, explicite et non ambigu — un dossier dédié ne se crée pas par accident ; **aucune extension de `CheckerContext`**). En complément documentaire/lint : `metadata.yml` porte `mode: ecriture-tests` (intention lisible + cohérence vérifiable par le lint). *(Raffinement vs « metadata explicite » : le gate runtime clé sur le dossier pour éviter de plomber `CheckerContext` ; un gate strictement metadata-driven serait une extension de `CheckerContext`, notée mais non retenue.)*

Helper partagé (ex. `ExerciseLayout.estEcritureDeTests(ctx)` = `Files.isDirectory(ctx.exerciseRefPath().resolve("mutants"))`), utilisé par les trois checkers concernés.

### 4.2 `MutationChecker` (nouveau, `console/checkers`)
`id() = "mutation"`. `appliesTo(ctx)` = `estEcritureDeTests(ctx)`.

`check(ctx)` :
1. **Compiler le test du stagiaire** : `renduPath/starter/src/test/java` → `classes-test`. (Classpath de compilation = `toolingClasspath()` + `classes-correct`, voir étape 2 ; il faut une impl pour compiler le test.) Si échec compile → `FAIL` « ton test ne compile pas ».
2. **PASS sur l'impl correcte** : compiler `exerciseRefPath/solution/src/main/java` → `classes-correct` ; exécuter les FQCN du test stagiaire (via `runJUnit`) avec classpath `tooling + classes-correct + classes-test`. **Exit 0 attendu.** Sinon → `FAIL` « tes tests ne passent pas sur une implémentation correcte (ils sont faux ou trop stricts) ».
3. **FAIL sur chaque mutant** : pour chaque sous-dossier `exerciseRefPath/mutants/<id>/` , compiler son impl → `classes-mut-<id>` ; ré-exécuter le **même** test stagiaire avec classpath `tooling + classes-mut-<id> + classes-test`. **Exit ≠ 0 attendu** (mutant tué). Un mutant qui sort **0** = **survivant** (non détecté).
4. **Verdict** : `OK` si étape 2 = pass **et** tous les mutants tués ; sinon `FAIL` avec message « N/M mutants détectés » + liste des survivants (+ indice). `ERROR` si la **référence** (solution ou un mutant) ne compile pas (problème côté exo, pas côté stagiaire).

Réutilise `JavaToolkit` (compile/runJUnit) et `FqcnExtractor`. La mécanique « même FQCN, classes-main différentes » est celle d'`AbstractTestChecker` : on garde `classes-test` (stagiaire) constant et on swappe `classes-main` (correct → mutant).

### 4.3 Désactiver les checkers normaux sur ces exos **[fork validé]**
- `PublicTestChecker.appliesTo` / `PrivateTestChecker.appliesTo` → `return !estEcritureDeTests(ctx);` (sinon ils gradderaient l'impl correcte fournie, trivialement verte et trompeuse).
- `CompileChecker` : reste actif, mais doit compiler **le test du stagiaire** sur ces exos (le livrable), pas seulement `starter/main`. **[raffinement]** : soit `CompileChecker` détecte le mode et compile `starter/src/test/java` (+ l'impl fournie pour le classpath), soit on laisse le `MutationChecker` porter le diagnostic de compilation (étape 1) et `CompileChecker.appliesTo` → false en mode écriture-tests. **Retenu** : `CompileChecker.appliesTo` → `false` en écriture-tests, le `MutationChecker` portant le message « ne compile pas » (étape 1) — un seul checker responsable, moins de redondance. À confirmer à l'implémentation.
- `StyleChecker` : reste actif (style du code de **test** du stagiaire) — advisory de toute façon.

### 4.4 Wiring `Main.runRepl`
Ajouter `new MutationChecker(toolkit)` à la liste `List.of(...)`. Grâce à `appliesTo`, il ne s'active que sur les exos à `mutants/`. Ordre : après `compile`, avant `style`.

## 5. Layout d'un exo « écriture de tests » (extension `format-exercice.md`)

```
6.1.1-<slug>/
├── metadata.yml            # + mode: ecriture-tests
├── sujet.md                # « écris la suite de tests de la classe fournie »
├── correction.md
├── evaluation.yml          # tests-valides 8 + mutants-tues 8 + style 2 + formateur 2
├── starter/
│   ├── pom.xml
│   └── src/
│       ├── main/java/etnc/m6/Xxx.java       # FOURNI correct (le stagiaire le TESTE, ne le modifie pas)
│       └── test/java/etnc/m6/XxxTest.java   # squelette à compléter par le stagiaire (≥ 1 @Test à écrire)
├── solution/
│   └── src/main/java/etnc/m6/Xxx.java       # impl correcte de référence (identique au starter/main)
│   └── src/test/java/etnc/m6/XxxTest.java   # suite de tests MODÈLE (référence — valide l'exo en CI)
└── mutants/
    ├── <id-1>/etnc/m6/Xxx.java              # variante buggée 1 (même FQCN)
    ├── <id-2>/etnc/m6/Xxx.java              # variante buggée 2
    └── …                                    # 3 à 5 mutants (un par règle/cas limite) [fork #3]
```
- **CI `valider-solutions`** (#11b adapté, au cycle 6.1) : la suite **modèle** (`solution/.../test`) doit **passer sur le correct** et **tuer tous les mutants** → garantit que l'exo est gradable avant le stagiaire. (L'extension du script `valider-solutions.sh` pour boucler sur `mutants/` est traitée **au cycle 6.1**, pas ici — #57 livre le checker + le contrat de layout.)

## 6. Stratégie de test (TDD — obligatoire)

Tests dans `moulinette/console/src/test/java/.../checkers/`, `@Tag("tools")` (comme les autres checkers ; lourds car forkent javac/java). Fixtures montées en `@TempDir` reproduisant le layout §5 avec une classe-jouet triviale (ex. `Addition.somme(int,int)`).

Cas à couvrir (tests d'abord) :
1. **`appliesTo`** : exo avec `mutants/` → `true` ; exo sans → `false`. Idem `Public/PrivateTestChecker.appliesTo` inversés (non-régression : un exo classique garde Public/Private actifs).
2. **Nominal OK** : test stagiaire correct (passe sur correct, tue 2/2 mutants) → `Status.OK`.
3. **Test faux sur le correct** : test stagiaire qui échoue sur l'impl correcte → `FAIL`, message « ne passe pas sur une implémentation correcte ».
4. **Mutant survivant** : test stagiaire qui passe sur correct mais laisse survivre 1 mutant → `FAIL`, message « 1/2 mutants détectés », nomme le survivant.
5. **Test vide / trivial** : `starter/test` sans `@Test` (ou `--fail-if-no-tests`) → `FAIL` « aucun test trouvé » (anti-triche léger).
6. **Référence cassée** : un mutant qui ne compile pas → `ERROR` (problème côté exo).
7. **Filtrage runner** : `MoulinetteRunner.Default` saute un checker dont `appliesTo` = false (test unitaire avec un `FakeChecker`).

Gate : `mvn -f moulinette/pom.xml verify` (unitaires) + suite `@Tag("tools")` verte ; pas de régression des suites `git`/`e2e`.

## 7. Workflow d'exécution

- Branche `feature/mutation-checker` (déjà créée, kickoff committé). Issue de `main`.
- **TDD** : pour chaque cas §6, test rouge → implémentation → vert. Ordre des tâches : (1) `Checker.appliesTo` + filtrage runner ; (2) helper `estEcritureDeTests` ; (3) `Public/Private/CompileChecker.appliesTo` ; (4) `MutationChecker` (cœur) ; (5) wiring `Main` ; (6) extension `format-exercice.md`.
- Commits par tâche. Gate Maven + lint à la fin. PR `#57 — MutationChecker`, CI 4 jobs verts → merge.
- Clôture : `backlog.md` (#57 fait), puis **cycle exos 6.1** (`feature/m6-exos-6-1`).

## 8. Critères d'acceptation

- [ ] `Checker.appliesTo(ctx)` ajouté (défaut `true`), `MoulinetteRunner.Default` filtre dessus ; **modules 1-5 inchangés** (non-régression : suites `tools`/`e2e`/`git` vertes).
- [ ] `MutationChecker` : OK ssi tests stagiaire passent sur le correct **et** tuent tous les mutants ; FAIL sinon avec message « N/M mutants », survivants nommés ; ERROR si référence cassée.
- [ ] `Public/Private/CompileChecker` inactifs en mode écriture-tests ; `StyleChecker` actif.
- [ ] Marqueur = présence `mutants/` (runtime) + `mode: ecriture-tests` (metadata, doc/lint).
- [ ] Tests `@Tag("tools")` couvrant les 7 cas §6, verts ; `mvn -f moulinette/pom.xml verify` + suites taguées vertes.
- [ ] `format-exercice.md` documente le layout « écriture de tests ».
- [ ] Verdict binaire + message proportionnel acté (§3) ; pas de fausse promesse de note fractionnaire.

## 9. Décisions consignées (forks formateur 2026-06-08)
- **#1 marqueur** : validé « metadata explicite » → **raffiné** en « `mutants/` runtime + `mode:` metadata doc/lint » (évite d'étendre `CheckerContext`). À confirmer à l'implémentation.
- **#2 score** : validé « proportionnel » → **adapté** en « verdict binaire + feedback proportionnel dans le message » (le modèle `CheckResult` n'a pas de note fractionnaire ; vrai score = tâche future). **Signalé.**
- **#3 mutants** : **3 à 5 par exo**, un par règle/cas limite. Figé au cycle 6.1, exo par exo.
