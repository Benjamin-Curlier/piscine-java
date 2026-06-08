# Plan d'implémentation — Chapitres du module 6 (Tests et Git)

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Produire les 8 chapitres du module 6 (`courses/docs/module-6-tests-git/`) conformes à la charte §6, avec un build Docusaurus vert, le levier méta assumé (la moulinette = harnais de tests ; la séquence de rendu = `add`/`commit`/`push`) et la clôture du parcours en 6-8.

**Architecture:** Contenu pédagogique Markdown pour Docusaurus. 8 chapitres (`6-1` … `6-8`), chacun lié au suivant par « Prochain chapitre » (**sauf 6-8** qui clôt le parcours, renvoi projets binôme) ; retouche de liaison `5-7`→`6-1` et bascule `_category_.json`. **1 seule PR** (choix formateur) sur `feature/m6-chapitres` : tous les liens forward existent dans la même branche, le build `onBrokenLinks: 'throw'` passe en une fois.

**Tech Stack:** Markdown + frontmatter Docusaurus ; Node/npm (`cd courses && npm run build`) ; JDK 25 (`E:/java/jdk-25.0.3+9`) + JUnit/AssertJ (`.m2` chaud ou uber-jar moulinette) pour compiler les extraits de test des ch.6-2→6-5 ; un dépôt-jouet jetable pour vérifier les commandes git des ch.6-6→6-8.

**Spec de référence :** [`docs/superpowers/specs/2026-06-08-m6-chapitres-design.md`](../specs/2026-06-08-m6-chapitres-design.md). Plan de contenu détaillé en §6, garde-fous antériorité/anti-spoil en §5, conventions en §4, décisions exos (hors périmètre de ce cycle) en §10.

**Particularité du module (spec §1, §4) :** méta-circularité — on enseigne JUnit/Git que la moulinette utilise déjà. Levier pédagogique aux ch.6-1/6-6 (« vous l'avez déjà vécu »), **sans** spoiler l'implémentation moulinette ni les exos. Deux familles d'« Exemple » : **Java** (test) pour 6-1→6-5, **shell git** pour 6-6→6-8.

**Convention de structure (charte §6, identique M1-M5) — chaque chapitre dans cet ordre :**
1. Frontmatter (`id`, `sidebar_position`, `title`, `description`)
2. `# Titre`
3. `## Pourquoi ce chapitre` (2–3 phrases)
4. `## Ce que vous saurez faire à la fin` (puces, verbes d'action)
5. Sections `## 1.`, `## 2.`, … : notion + `### Exemple` (code/commandes annoté) + `### À retenir` (encadré `>`)
6. `## Erreurs fréquentes` (puces **symptôme → cause → correction**)
7. `## Exercice guidé` (pas-à-pas + solution dans `<details>`)
8. `## Vérifiez vos acquis` (questions ouvertes)
9. `## Pour aller plus loin` (1–3 liens annotés : dev.java / Baeldung / Javadoc OpenJDK 25 / JUnit 5 / AssertJ / Pro Git fr)
10. `## Prochain chapitre` (→ lien relatif sans `.md`) — **sauf 6-8 : bloc de clôture**

**Conventions de code (charte §7) :** imports explicites (jamais en étoile) ; classes de test `…Test`, méthodes au présent décrivant le comportement ; blocs typés ` ```java ` / ` ```bash ` / ` ```text ` / ` ```diff ` / ` ```properties `. Les commandes git n'opèrent **jamais** sur le repo Piscine (dépôt-jouet `/tmp/demo`, `mon-projet/`).

---

## Task 0 : Sanity-check de l'environnement

**Files:** aucun (vérification).

- [ ] **Step 1 : Confirmer la branche et l'outillage**

Run :
```bash
cd "E:\claude\Piscine ETNC" && git rev-parse --abbrev-ref HEAD && node --version && (cd courses && npm run build >NUL 2>&1 && echo "BUILD_BASELINE_OK")
```
Expected : branche `feature/m6-chapitres` ; une version Node ; `BUILD_BASELINE_OK`. Si le build baseline échoue déjà, **stop** et diagnostiquer (problème pré-existant).

---

## Task 1 : Chapitre 6-1 — Pourquoi tester

**Files:** Create `courses/docs/module-6-tests-git/6-1-pourquoi-tester.md`

Frontmatter :
```markdown
---
id: 6-1-pourquoi-tester
sidebar_position: 1
title: "Pourquoi tester"
description: "Comprendre la valeur d'un test automatisé, les types de tests, le ROI et la non-régression — et découvrir que la moulinette est un harnais de tests."
---
```
`## Pourquoi ce chapitre` : ouvrir par **le levier méta** (spec §4) — depuis le module 1, une moulinette vous notait ; c'est un **harnais de tests JUnit**. Ce module vous apprend à écrire les vôtres.

Sections : `## 1. Le coût d'un bug trouvé tard` ; `## 2. Le test comme filet de sécurité` (refactorer sans peur) ; `## 3. Types de tests` (unitaire/intégration/E2E, survol ; le module fait de l'unitaire) ; `## 4. ROI et non-régression` (un test garde un bug corrigé ; révélation moulinette). **Aucune annotation JUnit ici** (c'est le ch.6-2).

`## Erreurs fréquentes` : « ça marche chez moi » suffit ; tester seulement le cas passant ; confondre test et `System.out.println` de débogage.

`## Exercice guidé` (**anti-spoil §5.4**) : **réflexion, sans code** — recenser 3 bugs qu'un test aurait attrapés sur du code déjà écrit en M1-M5. Pas d'écriture de test (réservé à 6.1.*).

`## Pour aller plus loin` : dev.java « Testing » + Baeldung « Software Testing ».

`## Prochain chapitre` : `→ **[Chapitre 6-2 — JUnit 5, les bases](6-2-junit5-bases)**`

**Garde-fous :** motivation/vocabulaire seulement ; pas d'API JUnit.

Commit : `docs(#22): chapitre 6-1 pourquoi tester`

---

## Task 2 : Chapitre 6-2 — JUnit 5, les bases

**Files:** Create `courses/docs/module-6-tests-git/6-2-junit5-bases.md`

Frontmatter :
```markdown
---
id: 6-2-junit5-bases
sidebar_position: 2
title: "JUnit 5 — les bases"
description: "Écrire ses premiers tests avec JUnit 5 : @Test, les assertions, et le schéma Arrange-Act-Assert."
---
```
Sections : `## 1. Où vivent les tests` (`src/test/java`, miroir de `src/main/java`) ; `## 2. Une classe de test, une méthode @Test` ; `## 3. Les assertions JUnit` (`assertEquals(attendu, obtenu)`, `assertTrue`, `assertFalse`, `assertThrows`) ; `## 4. Arrange-Act-Assert` (structurer un test).

Ancre de code (` ```java `, **à compiler Task 10**) :
```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculTest {
    @Test
    void additionneDeuxEntiers() {
        Calcul calcul = new Calcul();          // Arrange
        int resultat = calcul.ajouter(2, 3);   // Act
        assertEquals(5, resultat);             // Assert (attendu, obtenu)
    }
}
```
`## Erreurs fréquentes` : inverser `assertEquals(obtenu, attendu)` (message trompeur) ; plusieurs comportements dans un `@Test` ; oublier `@Test` (méthode jamais exécutée).

`## Exercice guidé` (**anti-spoil**) : tester une méthode **fournie** `estPair(int)` (1 cas vrai, 1 cas faux). Ni la classe ni le domaine de 6.1.1.

`## Pour aller plus loin` : doc officielle JUnit 5 (`junit.org/junit5/docs`) + Baeldung « JUnit 5 ».

`## Prochain chapitre` : `→ **[Chapitre 6-3 — JUnit 5, avancé](6-3-junit5-avance)**`

**Garde-fous :** `Assertions.*` (pas AssertJ, ch.6-4) ; `@Test` simple (pas de cycle de vie, ch.6-3).

Commit : `docs(#22): chapitre 6-2 JUnit 5 les bases`

---

## Task 3 : Chapitre 6-3 — JUnit 5, avancé

**Files:** Create `courses/docs/module-6-tests-git/6-3-junit5-avance.md`

Frontmatter :
```markdown
---
id: 6-3-junit5-avance
sidebar_position: 3
title: "JUnit 5 — avancé"
description: "Cycle de vie (@BeforeEach), tests paramétrés (@ParameterizedTest, @CsvSource), organisation (@Nested, @DisplayName)."
---
```
Sections : `## 1. Cycle de vie : @BeforeEach / @AfterEach` (préparer/nettoyer, éviter la duplication ; une instance par test) ; `## 2. Tests paramétrés` (`@ParameterizedTest` + `@ValueSource`, puis `@CsvSource` couples entrée→attendu, le plus lisible) ; `## 3. Organiser : @Nested et @DisplayName` ; `## 4. @BeforeAll / @AfterAll` (`static`, mention brève).

Ancre de code (` ```java `, **à compiler Task 10**) :
```java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ConvertisseurTest {
    private Convertisseur convertisseur;

    @BeforeEach
    void initialiser() {
        convertisseur = new Convertisseur(); // recréé avant chaque test
    }

    @ParameterizedTest
    @CsvSource({"0, 32", "100, 212", "-40, -40"})
    void celsiusVersFahrenheit(int celsius, int attendu) {
        assertEquals(attendu, convertisseur.versFahrenheit(celsius));
    }
}
```
`## Erreurs fréquentes` : partager un état mutable entre tests (d'où `@BeforeEach`) ; mauvais séparateur/types dans `@CsvSource` ; croire que `@BeforeAll` s'exécute par test (c'est une fois pour toutes, et `static`).

`## Exercice guidé` (**anti-spoil**) : `@CsvSource` sur une conversion **Celsius→Fahrenheit** (météo). Pas le domaine de 6.1.2.

`## Pour aller plus loin` : doc JUnit 5 « Parameterized Tests » + Baeldung.

`## Prochain chapitre` : `→ **[Chapitre 6-4 — AssertJ](6-4-assertj)**`

**Garde-fous :** suppose `@Test` acquis (ch.6-2) ; toujours `Assertions.*` (AssertJ au ch.6-4).

Commit : `docs(#22): chapitre 6-3 JUnit 5 avancé`

---

## Task 4 : Chapitre 6-4 — AssertJ

**Files:** Create `courses/docs/module-6-tests-git/6-4-assertj.md`

Frontmatter :
```markdown
---
id: 6-4-assertj
sidebar_position: 4
title: "AssertJ"
description: "Écrire des assertions fluides et lisibles avec assertThat(...), comparer objets et collections, tester une exception."
---
```
Sections : `## 1. assertThat et les assertions fluides` ; `## 2. Pourquoi AssertJ par-dessus Assertions.*` (messages d'échec explicites, enchaînement, autocomplétion) ; `## 3. Comparer objets et collections` (`isEqualTo`, `contains`/`containsExactly`/`containsExactlyInAnyOrder`, `hasSize`, `isEmpty`) ; `## 4. Tester une exception` (`assertThatThrownBy(...).isInstanceOf(...).hasMessageContaining(...)` — **rappel M5**, resitué dans AssertJ).

Ancre de code (` ```java `, **à compiler Task 10**) :
```java
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PanierTest {
    @Test
    void contientLesArticlesAjoutes() {
        List<String> articles = List.of("pain", "lait", "oeufs");
        assertThat(articles).hasSize(3).contains("lait");
    }

    @Test
    void leveSurIndexInvalide() {
        List<String> vide = List.of();
        assertThatThrownBy(() -> vide.get(0))
            .isInstanceOf(IndexOutOfBoundsException.class);
    }
}
```
`## Erreurs fréquentes` : mélanger l'`assertThat` de JUnit (déprécié) et celui d'AssertJ (import) ; `containsExactly` vs `containsExactlyInAnyOrder` (ordre) ; comparer des objets sans `equals` pertinent (records OK, classe nue NON).

`## Exercice guidé` (**anti-spoil**) : `assertThat(couleurs).containsExactly("rouge","vert","bleu")` sur une liste neutre. Pas les assertions d'objets de 6.1.1/6.1.4.

`## Pour aller plus loin` : doc AssertJ (`assertj.github.io`) + Baeldung « AssertJ ».

`## Prochain chapitre` : `→ **[Chapitre 6-5 — TDD en pratique](6-5-tdd-en-pratique)**`

**Garde-fous :** `assertThatThrownBy` = rappel M5 (exceptions déjà acquises) ; pas de TDD encore.

Commit : `docs(#22): chapitre 6-4 AssertJ`

---

## Task 5 : Chapitre 6-5 — TDD en pratique

**Files:** Create `courses/docs/module-6-tests-git/6-5-tdd-en-pratique.md`

Frontmatter :
```markdown
---
id: 6-5-tdd-en-pratique
sidebar_position: 5
title: "TDD en pratique"
description: "Le cycle Red/Green/Refactor : écrire le test d'abord, le voir échouer pour la bonne raison, puis faire passer et nettoyer."
---
```
Sections : `## 1. Le cycle Red / Green / Refactor` ; `## 2. Le test doit échouer pour la bonne raison` (sinon il ne teste rien — **pont avec le grading des exos §10.1, sans nommer la « mutation »**) ; `## 3. Petits pas` (le code minimal qui passe) ; `## 4. Le test comme spécification exécutable` (+ quand le TDD aide / gêne).

Ancre de code (` ```java ` — montrer le test rouge puis le code qui le rend vert ; **à compiler Task 10**, classe + test) :
```java
// ÉTAPE RED : on écrit le test d'abord, il échoue (Pgcd n'existe pas encore)
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class PgcdTest {
    @Test
    void pgcdDeDouzeEtHuitEstQuatre() {
        assertThat(Pgcd.de(12, 8)).isEqualTo(4);
    }
}
```
```java
// ÉTAPE GREEN : le code minimal qui fait passer le test
class Pgcd {
    static int de(int a, int b) {
        return b == 0 ? a : de(b, a % b); // Euclide (récursivité, acquis M2)
    }
}
```
`## Erreurs fréquentes` : écrire le code avant le test (ce n'est plus du TDD) ; un premier test qui passe immédiatement (jamais rouge → ne prouve rien) ; sauter le refactor (dette).

`## Exercice guidé` (**anti-spoil**) : TDD de `pgcd(a,b)` en 3 pas Red/Green/Refactor (arithmétique). **Pas** la calculatrice 4 opérations de 6.1.3.

`## Pour aller plus loin` : Baeldung « Test-Driven Development » + dev.java.

`## Prochain chapitre` : `→ **[Chapitre 6-6 — Git : les fondamentaux](6-6-git-fondamentaux)**`

**Garde-fous :** orchestre 6-2→6-4 (tests acquis) ; division/0 possible (exceptions M5) ; pas encore de Git.

Commit : `docs(#22): chapitre 6-5 TDD en pratique`

---

## Task 6 : Chapitre 6-6 — Git : les fondamentaux

**Files:** Create `courses/docs/module-6-tests-git/6-6-git-fondamentaux.md`

Frontmatter :
```markdown
---
id: 6-6-git-fondamentaux
sidebar_position: 6
title: "Git — les fondamentaux"
description: "Versionner son code : init, les trois zones, add/commit/status/log/diff, .gitignore et l'anatomie d'un bon message de commit."
---
```
Sections : `## 1. Pourquoi versionner` (historique, revenir en arrière, collaborer) ; `## 2. Les trois zones` (répertoire de travail → staging/index → dépôt) ; `## 3. Le cycle de base` (`init`, `add`, `commit -m`, `status`, `log`, `diff`) ; `## 4. .gitignore et messages de commit` (ne pas versionner `target/` ; message impératif/concis/le *pourquoi*). **Levier méta** : « le `add`/`commit`/`push` de la séquence de rendu, le voici ».

Ancre de commandes (` ```bash ` + sortie ` ```text ` ; dépôt-jouet, **vérifier Task 10b**) :
```bash
mkdir mon-projet && cd mon-projet
git init
echo "Bonjour" > notes.txt
git add notes.txt
git commit -m "Ajoute une note de bienvenue"
git log --oneline
```
`## Erreurs fréquentes` : `commit` sans `add` (rien de *staged*) ; versionner `target/`/secrets (d'où `.gitignore`) ; message « fix » / « wip » illisible.

`## Exercice guidé` (**anti-spoil**) : dépôt-jouet « liste de courses », 2 commits. Pas le scénario de 6.2.1.

`## Pour aller plus loin` : Pro Git (fr) ch.2 (`git-scm.com/book/fr`) + doc git officielle.

`## Prochain chapitre` : `→ **[Chapitre 6-7 — Git : branches et collaboration](6-7-git-branches-collaboration)**`

**Garde-fous :** cycle **local** seulement ; pas de branche (6-7), pas de distant/PR (6-8). Jamais sur le repo Piscine.

Commit : `docs(#22): chapitre 6-6 Git fondamentaux`

---

## Task 7 : Chapitre 6-7 — Git : branches et collaboration

**Files:** Create `courses/docs/module-6-tests-git/6-7-git-branches-collaboration.md`

Frontmatter :
```markdown
---
id: 6-7-git-branches-collaboration
sidebar_position: 7
title: "Git — branches et collaboration"
description: "Travailler en parallèle avec des branches, fusionner (merge), survol du rebase, et résoudre un conflit."
---
```
Sections : `## 1. Pourquoi des branches` (travailler sans casser `main`) ; `## 2. Créer et changer de branche` (`git branch`, `git switch -c`, `checkout` mentionné) ; `## 3. Fusionner` (`merge` : fast-forward vs commit de merge) ; `## 4. Résoudre un conflit` (lire les marqueurs, choisir, `add` + `commit`) + **`rebase` en survol** (rejouer ses commits, **ne pas rebaser du partagé**).

Ancre de commandes (` ```bash `) + bloc conflit (` ```diff `) :
```bash
git switch -c feature/titre
echo "Titre B" > README.md
git commit -am "Change le titre (branche)"
git switch main
echo "Titre A" > README.md
git commit -am "Change le titre (main)"
git merge feature/titre   # CONFLIT sur README.md
```
```diff
<<<<<<< HEAD
Titre A
=======
Titre B
>>>>>>> feature/titre
```
`## Erreurs fréquentes` : travailler directement sur `main` ; paniquer devant un conflit (c'est normal et réparable) ; `rebase` d'une branche déjà poussée/partagée.

`## Exercice guidé` (**anti-spoil**) : conflit **fabriqué** sur un `README` (deux branches éditent la même ligne), résolution pas-à-pas. Pas le fichier/scénario de 6.2.2.

`## Pour aller plus loin` : Pro Git (fr) ch.3 (branches) + Atlassian « Merging vs Rebasing ».

`## Prochain chapitre` : `→ **[Chapitre 6-8 — Git : le workflow Pull Request](6-8-git-workflow-pr)**`

**Garde-fous :** suppose le cycle local (6-6) ; pas de distant/PR (6-8).

Commit : `docs(#22): chapitre 6-7 Git branches et collaboration`

---

## Task 8 : Chapitre 6-8 — Git : le workflow Pull Request (clôture du parcours)

**Files:** Create `courses/docs/module-6-tests-git/6-8-git-workflow-pr.md`

Frontmatter :
```markdown
---
id: 6-8-git-workflow-pr
sidebar_position: 8
title: "Git — le workflow Pull Request"
description: "Pousser vers un dépôt distant, proposer une Pull Request, mener une revue de code et boucler le cycle collaboratif."
---
```
Sections : `## 1. Pousser vers un dépôt distant` (`git push -u origin <branche>`) ; `## 2. La Pull Request` (proposer, discuter ; fork vs branche) ; `## 3. La revue de code` (lire le diff, commenter avec bienveillance, demander des changements, approuver) ; `## 4. Boucler` (corriger, re-pousser, merger).

Ancre de commandes (` ```bash `) :
```bash
git switch -c feature/ajoute-licence
git add LICENSE
git commit -m "Ajoute le fichier LICENSE"
git push -u origin feature/ajoute-licence
# Puis, sur la forge (GitHub/GitLab) : ouvrir une Pull Request,
# discuter, corriger, et merger une fois la revue approuvée.
```
`## Erreurs fréquentes` : une PR énorme (illisible — préférer petit et fréquent) ; pousser sur la branche d'autrui sans accord ; revue « LGTM » sans avoir lu.

`## Exercice guidé` (**anti-spoil**) : décrire (sans tout exécuter) le cycle PR sur le dépôt-jouet. Pas le `PULL_REQUEST.md` exact de 6.2.3.

`## Pour aller plus loin` : Pro Git (fr) ch.6 (GitHub) + doc « About pull requests ».

**Bloc de clôture (PAS de « Prochain chapitre »)** :
```markdown
## Félicitations — vous avez terminé la Piscine

Vous savez désormais écrire du code orienté objet testé, gérer les erreurs et les fichiers, et collaborer avec Git. La suite n'est plus un chapitre, mais la mise en pratique : les **projets en binôme**, dont le **projet final** met en œuvre tout ce parcours (conception OO, persistance, tests et Git collaboratif) en construisant une mini-moulinette pédagogique.

→ **[Découvrir les projets en binôme](../../intro)**
```
*(Vérifier la cible exacte à l'ouverture : `../../intro`, ou la page listant les projets binôme si elle existe. Le lien doit être valide pour le build.)*

**Garde-fous :** suppose branches (6-7) ; clôt le module final — aucun « prochain chapitre ».

Commit : `docs(#22): chapitre 6-8 Git workflow Pull Request + clôture du parcours`

---

## Task 9 : Liaison module 5 → module 6 + bascule _category_.json

**Files:**
- Modify `courses/docs/module-5-exceptions-io/5-7-formats-texte.md` (bloc « Prochain chapitre »)
- Modify `courses/docs/module-6-tests-git/_category_.json` (`collapsed`)

- [ ] **Step 1 : Retoucher la liaison de 5-7** — remplacer :
```markdown
## Prochain chapitre

→ **Module 6 — Tests et Git** *(à venir)*
```
par :
```markdown
## Prochain chapitre

→ **[Module 6 · Chapitre 6-1 — Pourquoi tester](../module-6-tests-git/6-1-pourquoi-tester)**
```

- [ ] **Step 2 : Basculer `_category_.json`** — `"collapsed": true` → `"collapsed": false` (homogénéité M1-M5). Ne rien changer d'autre (`position: 7`, label, `link`).

Commit : `docs(#22): liaison 5-7 -> 6-1 et ouverture du module 6 dans la sidebar`

---

## Task 10 : Gate — compilation des extraits de test (JDK 25 + JUnit/AssertJ)

**Files:** fichiers temporaires hors dépôt (ne pas commiter).

- [ ] **Step 1 : Compiler les extraits Java des ch.6-2→6-5**

Pour chaque ancre de test (`CalculTest` 6-2 ; `ConvertisseurTest` `@CsvSource` 6-3 ; `PanierTest` AssertJ 6-4 ; `PgcdTest`+`Pgcd` 6-5), créer la/les classe(s) manquante(s) minimale(s) (`Calcul.ajouter`, `Convertisseur.versFahrenheit`, etc.) dans un dossier temp et compiler avec JUnit/AssertJ au classpath. L'uber-jar moulinette les embarque ; sinon pointer le `.m2` :
```bash
cd "$env:TEMP\m6check" ; & "E:/java/jdk-25.0.3+9/bin/javac" -cp "<junit+assertj jars>" -d . *.java ; echo "EXIT=$LASTEXITCODE"
```
Expected : `EXIT=0` par extrait. Corriger l'extrait **dans le chapitre** si la compilation échoue (précédents M4/M5 : vrais bugs attrapés ici).

- [ ] **Step 1b : Vérifier les commandes git des ch.6-6→6-8**

Sur un dépôt-jouet jetable (hors repo Piscine), rejouer les séquences `init`/`add`/`commit`/`log` (6-6), `switch`/`merge`/conflit (6-7), `push` simulé (6-8 — au moins jusqu'à la création de branche/commit). Vérifier que la sortie montrée (` ```text `) et les marqueurs de conflit (` ```diff `) correspondent. Corriger le chapitre si une commande ou une sortie est inexacte.

---

## Task 11 : Gate — build Docusaurus

**Files:** aucun (vérification ; corrections si build cassé).

- [ ] **Step 1 : Build complet**
```bash
cd "E:\claude\Piscine ETNC\courses" && npm run build
```
Expected : build **réussi**, **0** broken link. `onBrokenLinks: 'throw'` casse au moindre lien mort.

- [ ] **Step 2 : Corriger si nécessaire** — vérifier les `id`/chemins relatifs (sans `.md`), la cible `../module-6-tests-git/6-1-pourquoi-tester`, chaque « Prochain chapitre » `6-N`→`6-(N+1)`, et le lien de clôture de 6-8 (`../../intro`). Re-builder jusqu'au vert.

Commit (si corrections) : `docs(#22): corrige les liens internes du module 6 (build vert)`

---

## Task 12 : Relecture finale (charte §10)

**Files:** les 8 chapitres (corrections inline si besoin).

- [ ] Structure charte §6 respectée (10 blocs ; **6-8 = bloc de clôture** au lieu de « Prochain chapitre »).
- [ ] Niveau de langue débutant ; acronymes développés (TDD, VCS, PR, CI) ; `mock`/`stub` **évités** ; `staging`/`merge`/`rebase`/`diff` annotés à la 1re occurrence.
- [ ] **Antériorité interne** (spec §5.2) : pourquoi (1) → JUnit bases (2) → avancé (3) → AssertJ (4) → TDD (5) → git local (6) → branches (7) → PR (8).
- [ ] **Levier méta** présent aux 6-1 et 6-6 **sans** spoiler l'implémentation moulinette ni les exos.
- [ ] **Anti-spoil §5.4** : aucun exercice guidé ne donne la solution d'un exo noté (recensement bugs / estPair / Celsius / couleurs / pgcd / liste de courses / conflit README / description cycle PR).
- [ ] Chaque chapitre a « Exercice guidé » (`<details>`) et « Vérifiez vos acquis » ; extraits Java compilent, commandes git exactes.

Commit (si corrections) : `docs(#22): relecture finale des chapitres module 6`

---

## Task 13 : Clôture — backlog + PR

**Files:** Modify `docs/backlog.md` (#22, avancement du volet chapitres)

- [ ] **Step 1 : Mettre à jour le backlog** — sur la ligne **#22**, marquer le **volet chapitres FAIT** (8 chapitres), garder les 2 sous-groupes d'exos (6.1/6.2) à produire. Respecter le format des entrées #18→#21.

Commit : `docs(#22): backlog — volet chapitres du module 6 produit`

- [ ] **Step 2 : Pousser et ouvrir la PR** (après feu vert formateur)
```bash
cd "E:\claude\Piscine ETNC" && git push -u origin feature/m6-chapitres
gh pr create --base main --head feature/m6-chapitres --title "#22 — chapitres module 6 (Tests et Git)" --body "..."
```
Expected : CI 4 jobs verts (dont `build-docusaurus`) → merge.

---

## Vérification d'auto-revue (faite à la rédaction du plan)

- **Couverture spec :** 8 chapitres (spec §2/§6) → Tasks 1-8 ; liaison + `_category_` (spec §2) → Task 9 ; compilation extraits + commandes git (spec §7) → Task 10 ; build `onBrokenLinks` (spec §7) → Task 11 ; relecture charte §10 + garde-fous §5 → Task 12 ; backlog + PR (spec §8) → Task 13. **Annexe §10 (décisions exos)** hors périmètre — non couverte, voulu.
- **Single PR :** tous les liens forward (`6-N`→`6-(N+1)`) existent dans `feature/m6-chapitres` → build vert en une passe ; pas de problème de lien inter-PR.
- **Cohérence identifiants :** `id`/chemins `6-N-…` cohérents entre frontmatter, « Prochain chapitre », cible de liaison `../module-6-tests-git/6-1-pourquoi-tester`, et lien de clôture 6-8.
