# #57 — MutationChecker : grader les tests écrits par le stagiaire (cycle moulinette)

> **But de ce document** : cadrer le cycle **moulinette** qui débloque le sous-groupe d'exercices **6.1** (où le livrable du stagiaire est **le test**, pas le code). Ce n'est PAS une spec ni un plan : c'est le point de départ du cycle **brainstorm → spec → plan → implémentation (TDD)**.
>
> Lire d'abord : [`docs/architecture-moulinette.md`](../architecture-moulinette.md), [`docs/format-exercice.md`](../format-exercice.md), la spec chapitres M6 [`specs/2026-06-08-m6-chapitres-design.md`](specs/2026-06-08-m6-chapitres-design.md) §10.1 (décision « mutation maison ») et §10.4 (barème). Code de référence : `moulinette/console/src/main/java/etnc/piscine/moulinette/console/checkers/`.

## 0. Pourquoi ce cycle existe (au 2026-06-08)

Les chapitres du module 6 sont mergés (PR #49). Le sous-groupe d'exos **6.1 (Tests avec JUnit 5)** demande au stagiaire d'**écrire des tests**. Or la moulinette, telle qu'elle est, grade l'**inverse** : elle exécute des tests **de référence** contre le **code du stagiaire**. Pour 6.1, il faut exécuter les tests **du stagiaire** contre des implémentations de référence (une correcte + des mutants), et vérifier que ses tests **passent sur le correct** et **échouent sur chaque mutant** (preuve qu'ils détectent un vrai bug).

**Décision formateur (2026-06-08)** : construire ce mécanisme proprement dans la moulinette (option « MutationChecker, cycle dédié »), plutôt qu'un grading intérimaire. Ce cycle livre l'outil ; le cycle 6.1 livrera les 4 exos qui s'appuient dessus.

## 1. Ce que fait la moulinette aujourd'hui (constat, lecture du code)

`AbstractTestChecker.check(ctx)` (dans `console/checkers`) :
1. compile **`renduPath/starter/src/main/java`** (le **code du stagiaire**) → `classes-main` ;
2. compile le dossier de tests **de référence** (`exerciseRefPath/tests/...` ou `tests-prives/...`) → `classes-test` ;
3. exécute via JUnit ConsoleLauncher les classes du dossier sélectionné ; **exit 0 = OK**.

`PublicTestChecker` sélectionne `tests/`, `PrivateTestChecker` sélectionne `tests-prives/`. Les 4 checkers (`compile`, `tests-publics`, `tests-prives`, `style`) sont une **liste figée** dans `Main.runRepl` (`List.of(...)`, ligne ~74) appliquée à **tout** exo. Le mapping note se fait par `evaluation.yml` (critères `tests-publics` poids 8, `tests-prives` poids 8, `style` 2, 1 formateur 2 → /20, seuil 12).

**Conclusion** : pour 6.1, le couple (code, tests) est inversé, et la sélection des checkers doit devenir **dépendante de l'exo**.

## 2. Design proposé (additif, faible perturbation)

### 2.a — Rendre le pipeline sélectif par exo
- Ajouter au contrat `Checker` : `default boolean appliesTo(CheckerContext ctx) { return true; }`.
- Dans `MoulinetteRunner.Default`, **filtrer** la liste par `appliesTo(ctx)` avant exécution (aujourd'hui elle exécute tout). Changement minimal, rétro-compatible (défaut `true` ⇒ comportement inchangé pour les modules 1-5).
- Critère « cet exo est-il à écriture de tests ? » : présence d'un dossier **`mutants/`** sous l'exo (option A, auto-détection) **ou** un champ `metadata.yml` (option B, explicite, ex. `mode: ecriture-tests`). **Recommandation : option B** (explicite > implicite ; le lint peut vérifier la cohérence). À trancher en spec.

### 2.b — Nouveau `MutationChecker`
Pour un exo à écriture de tests :
1. compile le **test du stagiaire** (`renduPath/starter/src/test/java`) ;
2. **PASS attendu** : exécute ce test contre la **solution correcte** (`exerciseRefPath/solution/src/main/java`) → doit sortir **exit 0**. Sinon : les tests du stagiaire sont faux ou over-spécifient → échec « tes tests ne passent même pas sur une implémentation correcte ».
3. **FAIL attendu, par mutant** : pour chaque `exerciseRefPath/mutants/<id>/...` , exécute le même test contre le mutant → doit sortir **exit ≠ 0** (le test « tue » le mutant). Un mutant **survivant** (test vert) = non détecté.
4. **Score** : `mutants tués / mutants total`, plus l'exigence PASS sur le correct. Émet un (ou deux) `CheckResult` ; messages pédagogiques : « 3/4 mutants détectés — le mutant `borne-inferieure` survit, ton test ne couvre pas la valeur limite ».

Réutilise `JavaToolkit.compile` / `runJUnit` (déjà là). La mécanique « même FQCN, sources différentes » est exactement celle de `AbstractTestChecker` (qui swappe déjà classes-main vs classes-test) — on swappe ici les **classes-main** (correct → mutant) en gardant **classes-test** (celles du stagiaire).

### 2.c — Désactiver les checkers normaux sur ces exos
`PublicTestChecker.appliesTo` / `PrivateTestChecker.appliesTo` → **false** quand l'exo est en mode écriture-tests (sinon ils grraderaient le code fourni correct, trivialement vert et trompeur). `CompileChecker` reste actif (le test doit compiler) ; `StyleChecker` reste actif (style du **test** stagiaire). À confirmer : faut-il un `CompileChecker` qui compile *aussi* le test ? (aujourd'hui il compile `starter/main` ; pour 6.1, compiler `starter/test` a du sens). À détailler en spec.

### 2.d — Layout d'un exo « écriture de tests » (format-exercice étendu)
```
6.1.1-<slug>/
├── metadata.yml            # mode: ecriture-tests  (ou marqueur équivalent)
├── sujet.md                # « écris les tests de la classe fournie »
├── correction.md
├── evaluation.yml          # tests-valides 8 + mutants-tues 8 + style 2 + formateur 2
├── starter/
│   ├── pom.xml
│   └── src/
│       ├── main/java/etnc/m6/Xxx.java     # FOURNI correct (le stagiaire le TESTE, ne le modifie pas)
│       └── test/java/etnc/m6/XxxTest.java # squelette à compléter par le stagiaire
├── solution/
│   └── src/main/java/etnc/m6/Xxx.java     # impl correcte de référence (= celle du starter)
│   └── src/test/java/etnc/m6/XxxTest.java # suite de tests « modèle » (référence, pour valider l'exo en CI)
└── mutants/
    ├── borne-inferieure/etnc/m6/Xxx.java  # variante buggée 1
    ├── operateur-inverse/etnc/m6/Xxx.java # variante buggée 2
    └── …                                  # 3 à 5 mutants/exo (cf. §10.1)
```
- **CI `valider-solutions`** : la suite de tests **modèle** (`solution/.../test`) doit passer sur le correct **et** tuer tous les mutants → garantit que l'exo est « gradable » avant le stagiaire (filet #11b adapté).
- Détails (où vit exactement le test stagiaire, comment `valider-solutions.sh` boucle sur `mutants/`) à figer en spec.

## 3. Stratégie de test (TDD — c'est du code, pas du contenu)

Skill `superpowers:test-driven-development` **obligatoire**. Tests unitaires/intégration du `MutationChecker` avec des fixtures `@TempDir` :
- un exo-fixture minimal (classe `Addition` correcte + 2 mutants + un test stagiaire bon) → checker **OK**, score 2/2 ;
- test stagiaire **faux sur le correct** → **FAIL** « ne passe pas sur l'implémentation correcte » ;
- test stagiaire **qui laisse survivre un mutant** → score 1/2, message nommant le mutant survivant ;
- `appliesTo` : un exo classique (sans `mutants/`) → MutationChecker **inactif**, Public/Private **actifs** (non-régression du pipeline) ;
- tag `@Tag("tools")` comme les autres checkers ; intégrés au CI (`mvn -f moulinette/pom.xml verify` + suites taguées).

## 4. Questions ouvertes à trancher au brainstorm (formateur)

- **Marqueur d'exo** : auto-détection `mutants/` (A) vs champ `metadata.yml` explicite (B, recommandé). 
- **Nombre de mutants par exo** : 3–5 (recommandé : un par règle/cas limite de la classe testée).
- **Granularité du score** : binaire (tous les mutants tués ou échec) vs **proportionnel** (recommandé : `mutants-tués/total`, plus pédagogique). Impact sur `evaluation.yml` (un critère `mutants-tues` pondéré) et sur le calcul de note (vérifier que le moteur de score gère un score fractionnaire, pas seulement OK/FAIL).
- **`CompileChecker` sur les exos écriture-tests** : compiler `starter/test` (le livrable) en plus/à la place de `starter/main`.
- **Nommage** : `MutationChecker` (mécanisme) vs `StudentTestChecker` (rôle). 
- **`evaluation.yml` 6.1** : `tests-valides` (8) + `mutants-tues` (8) + `style` (2) + `formateur` (2) — confirmer (cf. spec chapitres §10.4).

## 5. Cadence recommandée

1. **Spec** `specs/2026-06-08-mutation-checker-design.md` (contrat `appliesTo`, `MutationChecker`, layout exo, mapping `evaluation.yml`, fixtures de test) → **validation formateur**.
2. **Plan** `plans/2026-06-08-mutation-checker.md` (tâches TDD : contrat → checker → filtrage runner → wiring Main → CI/lint).
3. **Implémentation TDD** sur `feature/mutation-checker` (issue de `main`) : tests d'abord, puis code, gate `mvn -f moulinette/pom.xml verify` + suites `tools`/`e2e`, PR.
4. **PUIS** cycle exos **6.1** (`feature/m6-exos-6-1`) : 4 exos s'appuyant sur le checker, 1 PR. Puis 6.2 (Git), puis fiche Projet binôme #3.

## 6. Premiers pas de la session d'implémentation

- [ ] Lire ce brief + `architecture-moulinette.md` + `AbstractTestChecker`/`PublicTestChecker`/`PrivateTestChecker` + `MoulinetteRunner.Default` + `JavaToolkit`.
- [ ] Brainstorm §4 avec le formateur.
- [ ] Spec + plan, validation.
- [ ] Implémenter en TDD, CI vert, PR.
- [ ] Mettre à jour `backlog.md` (#57 fait) et `format-exercice.md` (layout « écriture de tests »).

---

*Brief préparé le 2026-06-08 après merge des chapitres M6 (#49) et exploration du code de la moulinette. Référence backlog : #57 (nouveau). Débloque le sous-groupe d'exos 6.1, lui-même sur le chemin du Projet binôme #3 (final).*
