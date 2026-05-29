# Target MVP — Checkers réels + déploiement stagiaire

**Date** : 2026-05-29
**Auteur** : itération « target MVP » Piscine ETNC
**Statut** : spec validée, prête pour planification
**Itération précédente** : [`2026-05-28-mvp-console-correction-design.md`](2026-05-28-mvp-console-correction-design.md) (console REPL + trigger, `MoulinetteRunner` câblé mais sans `Checker`).

---

## 1. Objectif de l'itération

Transformer le MVP « squelette » en **MVP livrable à des stagiaires pour test** :

1. **La moulinette évalue réellement** : compile le code, lance les tests publics puis privés, vérifie le style — au lieu de toujours répondre OK.
2. **L'app se déploie sur un PC stagiaire isolé** : un bundle ZIP auto-suffisant (JDK portable + uber-jar + exos), sans installation ni accès internet.
3. **Deux documentations** : une pour l'instructeur qui déploie, une pour les futurs développeurs.

Périmètre validé en brainstorming :

| Décision | Valeur |
|---|---|
| Checkers | `compile` + `tests-publics` + `tests-prives` + `style` (Checkstyle) |
| Notation | **pass/fail par Checker** (pas de note /20 cette itération) |
| Runtime stagiaire | **javac + JUnit bundlés**, sans Maven ni internet |
| Embarquement des outils | **A1 : l'uber-jar EST le classpath** (JUnit/AssertJ/Checkstyle shadés dedans) |
| Structure Checkers | **B1 : un Checker par responsabilité** |
| PMD | **différé** (StyleChecker conçu pour l'accueillir ; Checkstyle seul cette itération) |
| Déploiement | **bundle ZIP portable auto-suffisant**, Windows-first (variante Linux) |

---

## 2. Architecture — vue d'ensemble

On ne crée pas de nouveau module : on enrichit `moulinette/console` (Checkers + câblage) et `moulinette/framework` (un champ de contexte). On ajoute des scripts de packaging et deux docs.

```
moulinette/
├── framework/   (modifié : CheckerContext gagne exerciseRefPath)
├── runner/      (inchangé : ProcessRunner)
├── reports/     (inchangé : ReportGenerator, réutilisé par le runner)
├── cli/         (inchangé)
└── console/     (enrichi : package console.checkers + câblage Main/MoulinetteRunner)
scripts/
├── build-bundle.ps1   (NOUVEAU)
└── build-bundle.sh    (NOUVEAU)
docs/
├── deploiement-instructeur.md  (NOUVEAU)
└── architecture-moulinette.md  (NOUVEAU)
```

---

## 3. Couche Checkers

### 3.1 Changement framework : `CheckerContext`

```java
public record CheckerContext(String exerciseId, Path renduPath, Path exerciseRefPath) {
    // exerciseId      : "1.1.1"
    // renduPath       : <workspace>/exercises/<dir>  (dossier contenant starter/ — code stagiaire)
    // exerciseRefPath : <piscine>/exercises/.../<dir> (référence : tests/, tests-prives/, evaluation.yml, solution/)
}
```
Champs non nuls (`exerciseRefPath` peut pointer vers un dossier inexistant → géré en `error(...)`). Appelants à mettre à jour : `MoulinetteRunner.Default`, `FrameworkSmokeTest`, et tout test construisant un `CheckerContext`.

### 3.2 `JavaToolkit` (package `console.checkers`)

Façade au-dessus de `runner.ProcessRunner`. Toutes les méthodes renvoient un `ProcessResult`.

```java
public final class JavaToolkit {
    public JavaToolkit(ProcessRunner runner) { ... }

    // javac -d outDir -cp classpath <toutes les *.java sous srcDirs>
    ProcessResult compile(List<Path> srcDirs, Path outDir, String classpath) throws IOException;

    // java -cp classpath org.junit.platform.console.ConsoleLauncher
    //      --disable-banner --details=summary (--select-class <fqcn>)+
    ProcessResult runJUnit(String classpath, List<String> selectClasses) throws IOException;

    // java -cp <toolingClasspath> com.puppycrawl.tools.checkstyle.Main -c <config> <fichiers>
    ProcessResult checkstyle(Path config, List<Path> srcDirs) throws IOException;

    // Chemin du classpath outillage : l'uber-jar si on tourne depuis le jar,
    // sinon System.getProperty("java.class.path") (dev/tests).
    String toolingClasspath();

    // Chemin de l'exécutable java courant (System.getProperty("java.home")/bin/java).
    String javaExe();
}
```

`toolingClasspath()` : `JavaToolkit.class.getProtectionDomain().getCodeSource().getLocation()` → si `.jar`, ce jar ; sinon `java.class.path`. C'est ce qui porte JUnit/AssertJ/Checkstyle.

### 3.3 Découverte des classes de test : `FqcnExtractor`

Lit un fichier `.java`, extrait le `package x.y;` (ou défaut) et le nom de la classe publique (nom de fichier sans `.java`) → FQCN. Sert à passer `--select-class` à JUnit. Les classes sans `@Test` (ex. `etnc.util.CaptureSortie`) sélectionnées sont ignorées par JUnit sans erreur.

### 3.4 Les quatre Checkers (package `console.checkers`)

Exécutés dans l'ordre par `MoulinetteRunner.Default`, **arrêt au premier `Checker` non-OK** pour un exo donné. Chacun compile dans `<workspace>/.piscine/build/<exoId>/` (nettoyé en début de run).

| Classe | `id()` | Comportement | OK / FAIL / ERROR |
|---|---|---|---|
| `CompileChecker` | `compile` | `javac` du code stagiaire seul (`renduPath/starter/src/main/java`) → `build/<exo>/classes-main`. | OK si exit 0 ; FAIL si erreurs de compilation (stderr tronqué ~30 lignes + suggestion) ; ERROR si javac introuvable. |
| `PublicTestChecker` | `tests-publics` | compile (main déjà fait ou recompilé + sources `exerciseRefPath/tests`) → `classes-test` ; `runJUnit` en sélectionnant les FQCN trouvés sous `tests/`. | OK si tous verts ; FAIL avec la liste des tests rouges ; ERROR si compilation des tests impossible (problème côté réf). |
| `PrivateTestChecker` | `tests-prives` | idem en sélectionnant les FQCN sous `tests-prives/`. Compile aussi `tests/` pour les utilitaires partagés (ex. `CaptureSortie`) mais ne sélectionne que les classes privées. | idem. |
| `StyleChecker` | `style` | `checkstyle` sur `renduPath/starter/src/main/java` avec la config bundlée. | OK si aucune violation ; FAIL avec `fichier:ligne:règle` ; ERROR si Checkstyle plante. |

**Compilation partagée** : pour éviter de recompiler le main 3×, `MoulinetteRunner.Default` compile une fois le main par exo et passe le dossier `classes-main` aux Checkers via un champ de contexte interne au runner (les Checkers de test recompilent uniquement les sources de test contre `classes-main`). *Décision d'implémentation* : si le partage complique le contrat `Checker`, chaque Checker de test fait sa propre passe `javac main+tests` (exos minuscules, coût négligeable). Le plan tranchera ; le comportement observable est identique.

**Config Checkstyle** : fichier `console/src/main/resources/checkstyle-etnc.xml` (jeu de règles volontairement léger pour une beta : indentation, accolades, imports inutilisés, nommage de base). Extrait dans un temp au premier usage.

---

## 4. Câblage & flux

### 4.1 `Main.runRepl`

```java
var toolkit = new JavaToolkit(new ProcessRunner());
Path styleConfig = StyleChecker.extractBundledConfig(); // resources → temp
List<Checker> checkers = List.of(
    new CompileChecker(toolkit),
    new PublicTestChecker(toolkit),
    new PrivateTestChecker(toolkit),
    new StyleChecker(toolkit, styleConfig));
var runner = new MoulinetteRunner.Default(catalog, checkers, repo.resolve(".piscine/reports"));
```

### 4.2 `MoulinetteRunner.Default`

- Fournit `CheckerContext(e.id(), renduPath, e.exerciseDir())` (le catalogue connaît déjà `exerciseDir`).
- Nettoie/crée `<workspace>/.piscine/build/<exoId>/` avant les Checkers de l'exo.
- Conserve la sémantique : arrêt au premier exo échoué ; à l'intérieur, arrêt au premier Checker non-OK.

### 4.3 Rapport : réutilisation de `reports.ReportGenerator`

Pour chaque exo, le runner agrège les `CheckResult` en un `EvaluationReport(exoId, resultsParCheckerId)` et appelle `ReportGenerator.toMarkdown` + `toJson`. Le fichier `.piscine/reports/<groupe>-<ts>.md` concatène les sections par exo (et un `.json` jumeau). Le récap console (`▶ Exo … ✓/✗`) reste produit par `SubmissionTrigger` (inchangé).

### 4.4 Flux complet

```
git push origin rendu/1.1
  → PushCommand → SubmissionTrigger.onPushSucceeded
      → runner.runGroup("1.1", workspaceRoot)
          pour chaque exo (difficulté croissante) :
            renduPath       = workspace/exercises/1.1.1-hello-world
            exerciseRefPath = piscine/exercises/module-1-fondamentaux/1.1.1-hello-world
            compile → tests-publics → tests-prives → style   (stop au 1er non-OK)
            → ExoOutcome + EvaluationReport
            si non-OK → stop (exo bloquant)
          → écrit .piscine/reports/1.1-<ts>.{md,json}
      → récap console + chemin du rapport
```

---

## 5. Bundle ZIP portable

### 5.1 `scripts/build-bundle.{ps1,sh}` (lancé par l'instructeur)

1. `mvn -pl console -am package` (préfère `mvn` système, fallback `./mvnw`) → uber-jar.
2. Stage `dist/piscine-etnc-stagiaire/` :

```
piscine-etnc-stagiaire/
├── jdk/                         JDK 25 portable (copié depuis -JdkPath / $JAVA_HOME)
├── app/moulinette-console.jar   uber-jar (JUnit + AssertJ + Checkstyle inclus)
├── piscine/
│   ├── exercises/…              exos + tests + tests-prives (copie élaguée)
│   └── docs/piscine-stagiaire.md
├── workspace/                   vide (créé au 1er lancement)
├── piscine.bat                  lanceur Windows
├── piscine.sh                   lanceur Linux (variante)
└── LISEZMOI.txt                 démarrage rapide stagiaire
```

3. Zippe → `dist/piscine-etnc-stagiaire-<date>.zip`.

### 5.2 Lanceur `piscine.bat`

```bat
@echo off
set "JAVA=%~dp0jdk\bin\java.exe"
set "JAR=%~dp0app\moulinette-console.jar"
set "WS=%~dp0workspace"
if not exist "%WS%\.git" (
    "%JAVA%" -jar "%JAR%" init --nom stagiaire --dest "%WS%" --piscine-repo "%~dp0piscine"
)
"%JAVA%" -jar "%JAR%" repl --repo "%WS%" --piscine-repo "%~dp0piscine"
```

(`piscine.sh` : équivalent bash, `JAVA="$(dirname "$0")/jdk/bin/java"`.)

### 5.3 Choix & options

- **Chemins relatifs au bundle** (`%~dp0`) → copiable partout (USB inclus), sans droits admin.
- **JDK bundlé** → aucune install Java requise ; `JAVA_HOME` du stagiaire ignoré.
- **`-JdkPath` configurable** (défaut `$JAVA_HOME`).
- **Copie élaguée** : `exercises/` + guide stagiaire uniquement (pas `courses/`, pas les sources `moulinette/`).
- **Limite assumée** : `tests-prives/` et `solution/` présents dans le bundle (lisibles par un stagiaire curieux). Acceptable en phase de test ; anti-triche = tâche #29.

---

## 6. Documentation

### 6.1 `docs/deploiement-instructeur.md` (pour qui déploie)

- Prérequis **sur la machine instructeur** (JDK 25, repo cloné) — rien à pré-installer sur le PC stagiaire.
- Construire le bundle : `./scripts/build-bundle.ps1 [-JdkPath <chemin>]`, emplacement du ZIP.
- Déployer : copier le ZIP sur le PC stagiaire (USB/partage), décompresser n'importe où.
- Lancer : double-clic `piscine.bat` (ou `./piscine.sh`).
- Premier lancement attendu (init auto → REPL).
- **Dépannage** : Java introuvable (bundle incomplet), antivirus bloquant le `.bat`, chemin avec espaces/accents, emplacement des rapports (`workspace/.piscine/reports/`), réinitialisation (supprimer `workspace/`).
- Renvoi vers `piscine-stagiaire.md` pour l'usage.

### 6.2 `docs/architecture-moulinette.md` (pour les futurs devs)

- Les 6 modules Maven, rôles, schéma de dépendances.
- Deux points d'entrée : `cli` (headless/CI) vs `console` (REPL stagiaire).
- Flux d'évaluation `git push` → rapport (schéma §4.4).
- **Astuce « l'uber-jar EST le classpath »** + fallback dev (`java.class.path`).
- **Ajouter un Checker** : implémenter `framework.Checker`, l'enregistrer dans `Main.runRepl`.
- **Ajouter un exercice** : structure `starter/tests/tests-prives/solution/evaluation.yml` (renvoi `format-exercice.md`).
- **Tests** : tags `git` / `e2e` / `tools`, commandes par suite, `mvn` vs `mvnw`.
- **Limites & dette** : PMD différé, anti-triche (#29), Docker (#30), mode `nominal`, note /20 (future).

---

## 7. Gestion d'erreurs (Checkers)

| Situation | Traitement |
|---|---|
| Erreur de compilation stagiaire | `CompileChecker` → FAIL, stderr javac tronqué (~30 lignes) + « corrige avant de continuer ». Stoppe la chaîne. |
| Tests rouges | `Public/PrivateTestChecker` → FAIL + liste des tests échoués (parsing sortie ConsoleLauncher). |
| Boucle infinie | `ProcessRunner` coupe à 30 s → FAIL « exécution trop longue (boucle infinie ?) ». |
| Violations de style | `StyleChecker` → FAIL, `fichier:ligne:règle`. |
| Outil absent / exit anormal de javac | `CheckResult.error(...)` (≠ FAIL) : erreur technique, log détaillé, n'accuse pas le stagiaire. |
| tests/`evaluation.yml` manquants côté réf | `error(...)` explicite pour l'instructeur. |

Aucune stacktrace en sortie nominale (réservée à `--debug` / logs).

---

## 8. Stratégie de tests

- **Unitaires** : `FqcnExtractor` (package simple, défaut, multi-lignes) ; `JavaToolkit.toolingClasspath()` (jar vs `java.class.path`) ; parsing sortie JUnit (compte + échecs).
- **Intégration `@Tag("tools")`** (compile/exécute réellement, `@TempDir`) :
  - `CompileChecker` : code OK → OK ; erreur de syntaxe → FAIL.
  - `PublicTestChecker` : solution de réf 1.1.1 → OK ; `main` vide → FAIL avec le test échoué.
  - `StyleChecker` : code propre → OK ; code mal indenté → FAIL.
- **E2E `@Tag("e2e")` mis à jour** : vrais Checkers. Solution de réf 1.1.1 → rapport ✓ ; `main` vide → rapport ✗ bloquant sur `tests-publics`.
- **Bundle** : pas de test auto ; procédure de vérification manuelle dans `deploiement-instructeur.md`.
- **Surefire** : tag `tools` ajouté à `surefire.excludedGroups` par défaut (`git,e2e,tools`) ; lançable via `-Dsurefire.excludedGroups=… -Dgroups=tools`.

---

## 9. Critères d'acceptation de l'itération

- [ ] `CheckerContext` enrichi, tous les appelants compilent.
- [ ] `mvn -f moulinette/pom.xml verify` passe (unitaires).
- [ ] Suite `@Tag("tools")` verte : compile, tests publics/privés, style sur 1.1.1.
- [ ] E2E : solution de réf 1.1.1 → rapport ✓ ; `main` vide → rapport ✗ bloquant.
- [ ] Le rapport `.piscine/reports/<groupe>-<ts>.md` (+ `.json`) est produit via `ReportGenerator`.
- [ ] `build-bundle.ps1` produit un ZIP lançable contenant jdk/app/piscine/lanceur.
- [ ] Sur une arbo propre (hors repo), décompresser + `piscine.bat` → init auto + REPL ; un `push` d'une bonne solution donne ✓.
- [ ] `docs/deploiement-instructeur.md` et `docs/architecture-moulinette.md` rédigés.

---

## 10. Hors périmètre (explicite)

| Sujet | Renvoi |
|---|---|
| PMD | fast-follow (StyleChecker prévu pour) |
| Note pondérée /20 + seuil | itération ultérieure |
| Anti-triche / tests-prives cachés | tâche #29 |
| Isolation Docker du runner | tâche #30 |
| Mode `nominal` (plateforme) | tâches #26-#28 |
| Installateur natif (jpackage) | écarté au profit du bundle ZIP |
| Bundle Linux/macOS de production | variante `.sh` fournie, non durcie cette itération |

---

## 11. Découpage en tâches (proposition backlog, IDs #43+)

1. **#43** — `framework.CheckerContext` enrichi (`exerciseRefPath`) + mise à jour des appelants.
2. **#44** — `JavaToolkit` + `FqcnExtractor` (+ tests unitaires).
3. **#45** — `CompileChecker` (+ tests `@Tag("tools")`).
4. **#46** — `PublicTestChecker` + `PrivateTestChecker` (+ tests `@Tag("tools")`).
5. **#47** — `StyleChecker` + config Checkstyle bundlée (+ tests `@Tag("tools")`).
6. **#48** — Câblage `Main`/`MoulinetteRunner` + rapport via `ReportGenerator` + E2E mis à jour.
7. **#49** — `scripts/build-bundle.{ps1,sh}` + lanceurs + LISEZMOI.
8. **#50** — `docs/deploiement-instructeur.md`.
9. **#51** — `docs/architecture-moulinette.md`.
