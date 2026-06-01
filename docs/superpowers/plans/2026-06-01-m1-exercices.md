# #16 — Exercices module 1 : plan d'implémentation

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:executing-plans (exécution **inline**, pas de sous-agents — contrainte projet). Cases à cocher (`- [ ]`) pour le suivi.

**Goal:** Produire les 9 exercices restants du module 1 (`exercises/module-1-fondamentaux/`), chacun avec une solution de référence qui passe ses tests publics et privés en CI.

**Architecture:** Chaque exercice est un dossier autonome au format `format-exercice.md` (Maven mono-module, package `etnc.m1`). Le stagiaire ne complète que `main` ; les tests pilotent `System.in`/`System.out`. Validation = CI (`lint-exercices` + `valider-solutions`), pas de compilation locale (Java 8 sur la machine du formateur).

**Tech Stack:** Java 25, Maven, JUnit 5.11.0, AssertJ 3.26.3, build-helper-maven-plugin.

**Spec:** [`docs/superpowers/specs/2026-06-01-m1-exercices-design.md`](../specs/2026-06-01-m1-exercices-design.md)

**Livraison :** 3 PRs, une par sous-groupe. Phase 1 = sous-groupe 1.1 (branche `feature/m1-exos-1-1`, déjà créée, spec commitée). Phases 2 et 3 sur de nouvelles branches issues de `main` après merge de la précédente.

---

## Artefacts partagés (référence)

### A. `starter/pom.xml` (gabarit)
Copie du starter de `1.1.1-hello-world`, en remplaçant `artifactId` par le slug et le `<name>`. Câble **uniquement** `../tests` :

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                             https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>etnc.piscine.m1</groupId>
    <artifactId>SLUG</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>
    <name>Exercice M.S.E — TITRE (starter)</name>
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.release>25</maven.compiler.release>
        <junit.version>5.11.0</junit.version>
        <assertj.version>3.26.3</assertj.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>${junit.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.assertj</groupId>
            <artifactId>assertj-core</artifactId>
            <version>${assertj.version}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.13.0</version>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.5.0</version>
            </plugin>
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>build-helper-maven-plugin</artifactId>
                <version>3.6.0</version>
                <executions>
                    <execution>
                        <id>add-public-test-sources</id>
                        <phase>generate-test-sources</phase>
                        <goals><goal>add-test-source</goal></goals>
                        <configuration>
                            <sources>
                                <source>${project.basedir}/../tests/src/test/java</source>
                            </sources>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
```

### B. `solution/pom.xml` (gabarit)
Identique à A mais `artifactId` = `SLUG-solution`, `<name>` « (solution de reference) », et **deux** `<source>` quand des tests-privés existent :

```xml
                            <sources>
                                <source>${project.basedir}/../tests/src/test/java</source>
                                <source>${project.basedir}/../tests-prives/src/test/java</source>
                            </sources>
```

Pour **1.1.2** (sans tests-privés), le `solution/pom.xml` ne câble que `../tests` (comme A mais `artifactId` = `affichage-formate-solution`).

### C. `CaptureSortie.java`
Copier **à l'identique** depuis `exercises/module-1-fondamentaux/1.1.1-hello-world/tests/src/test/java/etnc/util/CaptureSortie.java` dans le `tests/.../etnc/util/` de chaque exo.

### D. `CaptureEntree.java` (nouveau, pour les exos à saisie)
À placer dans `tests/.../etnc/util/` de chaque exo qui lit au clavier (tous sauf 1.1.2 et 1.3.1) :

```java
package etnc.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Utilitaire de test : alimente {@code System.in} avec un contenu fixe le temps
 * d'exécuter une action (typiquement un appel à main qui lit au clavier).
 *
 * <p>L'état de {@code System.in} est restauré automatiquement, même en cas
 * d'exception levée par l'action.</p>
 */
public final class CaptureEntree {

    private CaptureEntree() {
        // classe utilitaire — pas d'instance
    }

    /**
     * Exécute {@code action} en faisant croire au programme que l'utilisateur
     * a saisi {@code entree} au clavier.
     *
     * @param entree le texte saisi (lignes séparées par '\n')
     * @param action code à exécuter (typiquement un appel à main)
     */
    public static void avecEntree(String entree, Runnable action) {
        InputStream original = System.in;
        try {
            System.setIn(new ByteArrayInputStream(entree.getBytes(StandardCharsets.UTF_8)));
            action.run();
        } finally {
            System.setIn(original);
        }
    }
}
```

Combinaison entrée + sortie dans un test :

```java
String[] sortie = new String[1];
CaptureEntree.avecEntree("Dupont\n42\n",
    () -> sortie[0] = CaptureSortie.capturer(() -> LectureSaisie.main(new String[]{})));
assertThat(sortie[0]).contains("Dupont").contains("42");
```

### E. `metadata.yml` / `evaluation.yml` (gabarits)
`metadata.yml` : tous les champs requis (`slug, titre, module, sous_groupe, position, difficulte, duree_estimee_min, prerequis, objectifs_pedagogiques, notions, auteur, version, date_creation`), `auteur: "ETNC"`, `version: 1`, `date_creation: 2026-06-01`.

`evaluation.yml` standard (avec tests-privés) :

```yaml
total: 20
seuil_reussite: 12
criteres:
  - id: tests-publics
    description: "Tests publics passants"
    poids: 8
    type: automatique
  - id: tests-prives
    description: "Tests privés passants"
    poids: 8
    type: automatique
  - id: style
    description: "Style et lisibilité (Checkstyle + PMD)"
    poids: 2
    type: automatique
  - id: demarche
    description: "Pertinence de la démarche (lisibilité, idiomatisme)"
    poids: 2
    type: formateur
```

Variante **sans** tests-privés (1.1.2 uniquement) : `tests-publics` poids `16`, pas de critère `tests-prives` (total reste 20).

---

# PHASE 1 — Sous-groupe 1.1 (branche `feature/m1-exos-1-1`)

## Task 1 : Exercice 1.1.2 — affichage-formate

**Files (Create) :** sous `exercises/module-1-fondamentaux/1.1.2-affichage-formate/`
- `sujet.md`, `metadata.yml`, `evaluation.yml`, `correction.md`
- `starter/pom.xml`, `starter/src/main/java/etnc/m1/AffichageFormate.java`
- `solution/pom.xml`, `solution/src/main/java/etnc/m1/AffichageFormate.java`
- `tests/src/test/java/etnc/m1/AffichageFormateTest.java`
- `tests/src/test/java/etnc/util/CaptureSortie.java`

**Contrat I/O :** pas d'entrée. À partir de variables fixées dans `main` (ex. `nom = "Martin"`, `grade = "Sergent"`, `age = 29`), afficher une fiche multi-lignes **exacte**. Format retenu (verrouillé pour les tests) :

```text
=== Fiche militaire ===
Nom    : Martin
Grade  : Sergent
Age    : 29 ans
```

- [ ] **Step 1 :** créer le dossier + `starter/pom.xml` (artefact A, `artifactId` `affichage-formate`) et `solution/pom.xml` (variante **sans** privés : `artifactId` `affichage-formate-solution`, un seul `<source>`).
- [ ] **Step 2 :** `starter/.../AffichageFormate.java` — classe `etnc.m1.AffichageFormate`, `main` avec les variables fixées et `// TODO : afficher la fiche au format demandé`.
- [ ] **Step 3 :** `solution/.../AffichageFormate.java` — implémentation : 4 `System.out.println(...)` produisant exactement le format ci-dessus, commentaire expliquant le *pourquoi* d'un libellé aligné.
- [ ] **Step 4 :** copier `CaptureSortie.java` (artefact C) dans `tests/.../etnc/util/`.
- [ ] **Step 5 :** `AffichageFormateTest.java` — un test capture la sortie de `main(new String[]{})` et `isEqualTo` la fiche complète (avec `System.lineSeparator()` entre lignes) ; un test `assertThatNoException`.
- [ ] **Step 6 :** `sujet.md` (format §3 : contexte, énoncé, exemple d'exécution = la fiche, contraintes « ne modifier que main », « ce qui sera vérifié »), `metadata.yml` (slug `affichage-formate`, sous_groupe `"1.1"`, position `2`, difficulte `tres-facile`, durée `10`, prerequis `[hello-world]`), `evaluation.yml` (variante sans privés), `correction.md` (format §9).
- [ ] **Step 7 : Commit**

```bash
git add exercises/module-1-fondamentaux/1.1.2-affichage-formate
git commit -m "feat(exo): 1.1.2 affichage-formate"
```

## Task 2 : Exercice 1.1.3 — lecture-saisie

**Files (Create) :** sous `1.1.3-lecture-saisie/` — même liste que Task 1 **plus** `tests-prives/src/test/java/etnc/m1/LectureSaisiePriveTest.java` et `tests/.../etnc/util/CaptureEntree.java`. Classe : `etnc.m1.LectureSaisie`.

**Contrat I/O :** lit un prénom (`nextLine`) puis un entier (`nextInt`). Entrée test `"Dupont\n42\n"`. Sortie exacte :

```text
Quel est votre prénom ?
Quel est votre âge ?
Bonjour Dupont, vous avez 42 ans.
```

(Les deux premières lignes sont les invites affichées avant chaque lecture ; la 3ᵉ est le résultat.)

- [ ] **Step 1 :** dossier + `starter/pom.xml` (artefact A, slug `lecture-saisie`) + `solution/pom.xml` (artefact B **avec** privés).
- [ ] **Step 2 :** `starter/.../LectureSaisie.java` — `import java.util.Scanner;`, crée le `Scanner`, affiche les invites, `// TODO : lire le prénom puis l'âge, puis afficher le message`. Indiquer en commentaire le piège `nextInt`/`nextLine`.
- [ ] **Step 3 :** `solution/.../LectureSaisie.java` — lit prénom via `nextLine`, âge via `nextInt`, affiche le message ; commentaire sur l'ordre de lecture.
- [ ] **Step 4 :** copier `CaptureSortie.java` (C) + créer `CaptureEntree.java` (D) dans `tests/.../etnc/util/`.
- [ ] **Step 5 :** `LectureSaisieTest.java` — test nominal (entrée `"Dupont\n42\n"`, sortie `isEqualTo` exacte) via le pattern combiné C+D.
- [ ] **Step 6 :** `tests-prives/.../LectureSaisiePriveTest.java` — entrée avec prénom composé (`"Jean Le Goff\n30\n"`) → vérifie que le prénom complet est repris (utilité de `nextLine` vs `next`).
- [ ] **Step 7 :** `sujet.md`, `metadata.yml` (position `3`, difficulte `facile`, durée `15`, prerequis `[affichage-formate]`, notions `scanner`/`saisie`), `evaluation.yml` (standard), `correction.md` (insister sur le piège `nextLine`).
- [ ] **Step 8 : Commit**

```bash
git add exercises/module-1-fondamentaux/1.1.3-lecture-saisie
git commit -m "feat(exo): 1.1.3 lecture-saisie"
```

## Task 3 : Lint + PR phase 1

- [ ] **Step 1 :** `bash scripts/lint-exercices.sh`
  Expected : `erreurs : 0` et les exos `1.1.2`, `1.1.3` (et `1.1.1`) listés `→`.
- [ ] **Step 2 :** ajouter le plan au dépôt et committer :

```bash
git add docs/superpowers/plans/2026-06-01-m1-exercices.md
git commit -m "docs(plan): implémentation #16 exercices module 1"
```

- [ ] **Step 3 : Push + PR**

```bash
git push -u origin feature/m1-exos-1-1
gh pr create --base main --title "#16 — Exercices module 1 : sous-groupe 1.1" --body "<spec + plan inclus ; exos 1.1.2 et 1.1.3 ; CI valider-solutions doit être vert>"
```

- [ ] **Step 4 :** attendre CI vert (`gh run watch`), surtout `valider-solutions`. Si rouge → corriger (cf. §10 spec), re-push. Puis relecture humaine + merge + `git checkout main && git pull`.

---

# PHASE 2 — Sous-groupe 1.2 (branche `feature/m1-exos-1-2` issue de `main`)

> Pré-requis : phase 1 mergée. `git checkout main && git pull && git checkout -b feature/m1-exos-1-2`.
> Chaque exo lit ses données au clavier (Scanner), util `CaptureSortie` + `CaptureEntree`, tests-privés présents, `solution/pom.xml` artefact B avec privés.

## Task 4 : Exercice 1.2.1 — conversion-unites

**Files :** dossier `1.2.1-conversion-unites/` complet (11 fichiers, dont `tests-prives/` et `CaptureEntree`). Classe `etnc.m1.ConversionUnites`.

**Contrat I/O :** lit une température °C (`nextDouble`), affiche la conversion °F. Formule : `f = celsius * 9 / 5 + 32` (en `double`). Entrée `"100\n"` → sortie :

```text
Température en °C ?
100.0 °C = 212.0 °F
```

- [ ] **Step 1 :** dossier + poms (A starter / B solution avec privés), slug `conversion-unites`.
- [ ] **Step 2 :** starter `ConversionUnites.java` — Scanner, invite, `// TODO : lire la température et afficher sa conversion en °F`.
- [ ] **Step 3 :** solution — lecture `nextDouble`, calcul en `double` (attention : `9.0 / 5` ou `* 9 / 5` sur un `double` — la valeur est déjà flottante), affichage ; commentaire sur la division flottante.
- [ ] **Step 4 :** util C + D.
- [ ] **Step 5 :** `ConversionUnitesTest.java` — entrée `"100\n"` → sortie exacte ci-dessus.
- [ ] **Step 6 :** privés — `"0\n"` → `32.0 °F` ; `"-40\n"` → `-40.0 °F` (point fixe) ; `"37\n"` → `98.6 °F`.
- [ ] **Step 7 :** `sujet.md`, `metadata.yml` (sous_groupe `"1.2"`, position `1`, difficulte `facile`, durée `15`), `evaluation.yml`, `correction.md`.
- [ ] **Step 8 :** `git add … && git commit -m "feat(exo): 1.2.1 conversion-unites"`

## Task 5 : Exercice 1.2.2 — calculs-geometriques

**Files :** dossier `1.2.2-calculs-geometriques/` complet. Classe `etnc.m1.CalculsGeometriques`.

**Contrat I/O :** lit le rayon d'un cercle (`nextDouble`), affiche aire et périmètre. `aire = Math.PI * rayon * rayon`, `perimetre = 2 * Math.PI * rayon`. Entrée `"2\n"` → sortie :

```text
Rayon du cercle ?
Aire : 12.566370614359172
Périmètre : 12.566370614359172
```

(Pour rayon 2 : aire = π·4 ≈ 12.566…, périmètre = 4π ≈ 12.566… — identiques par coïncidence ; les privés lèvent l'ambiguïté.)

- [ ] **Step 1 :** dossier + poms, slug `calculs-geometriques`.
- [ ] **Step 2 :** starter — Scanner, invite, `// TODO : lire le rayon, calculer et afficher aire et périmètre`.
- [ ] **Step 3 :** solution — `Math.PI`, calculs, affichage ; commentaire.
- [ ] **Step 4 :** util C + D.
- [ ] **Step 5 :** test public — entrée `"2\n"` → sortie exacte (vérifier les deux valeurs telles que produites par `Math.PI`).
- [ ] **Step 6 :** privés — `"1\n"` → aire `3.141592653589793`, périmètre `6.283185307179586` (lève l'ambiguïté du cas rayon 2) ; `"3\n"`.
- [ ] **Step 7 :** docs (position `2`, difficulte `facile`, durée `15`).
- [ ] **Step 8 :** `git commit -m "feat(exo): 1.2.2 calculs-geometriques"`

> Note : si le format exact des `double` est fastidieux à prévoir à la main, **construire la chaîne attendue dans le test** avec la même expression (`"Aire : " + (Math.PI * r * r)`) plutôt qu'un littéral — garantit l'égalité sans deviner les décimales.

## Task 6 : Exercice 1.2.3 — manipulation-booleenne (année bissextile)

**Files :** dossier `1.2.3-manipulation-booleenne/` complet. Classe `etnc.m1.AnneeBissextile`.

**Contrat I/O :** lit une année (`nextInt`), affiche si elle est bissextile. Règle : `(annee % 4 == 0 && annee % 100 != 0) || annee % 400 == 0`. Entrée `"2024\n"` → sortie :

```text
Quelle année ?
2024 est bissextile.
```

Année non bissextile → `<annee> n'est pas bissextile.`

- [ ] **Step 1 :** dossier + poms, slug `manipulation-booleenne`.
- [ ] **Step 2 :** starter — Scanner, invite, `// TODO : déterminer et afficher si l'année est bissextile`. Indice en commentaire sur la combinaison `%`, `&&`, `||`.
- [ ] **Step 3 :** solution — calcul du `boolean estBissextile`, affichage conditionnel (un `if`/`else` est permis : chap. 1.5) ; commentaire sur la règle.
- [ ] **Step 4 :** util C + D.
- [ ] **Step 5 :** test public — `"2024\n"` → bissextile.
- [ ] **Step 6 :** privés — `"1900\n"` (non, `%100` sans `%400`), `"2000\n"` (oui, `%400`), `"2023\n"` (non), `"2020\n"` (oui).
- [ ] **Step 7 :** docs (position `3`, difficulte `moyen`, durée `20`, notions `booleen`/`operateurs-logiques`).
- [ ] **Step 8 :** `git commit -m "feat(exo): 1.2.3 manipulation-booleenne"`

## Task 7 : Lint + PR phase 2

- [ ] **Step 1 :** `bash scripts/lint-exercices.sh` → `erreurs : 0`.
- [ ] **Step 2 :** push + PR « #16 — Exercices module 1 : sous-groupe 1.2 », attendre CI vert (`valider-solutions`), corriger si besoin, merge, resync `main`.

---

# PHASE 3 — Sous-groupe 1.3 (branche `feature/m1-exos-1-3` issue de `main`)

> Pré-requis : phase 2 mergée. Util : `CaptureSortie` partout ; `CaptureEntree` pour 1.3.2/1.3.3/1.3.4 (1.3.1 n'a pas d'entrée).

## Task 8 : Exercice 1.3.1 — fizzbuzz

**Files :** dossier `1.3.1-fizzbuzz/` complet (avec privés ; **pas** de `CaptureEntree`, aucune entrée). Classe `etnc.m1.FizzBuzz`.

**Contrat I/O :** pas d'entrée. Affiche les lignes 1 à 100 : multiple de 15 → `FizzBuzz`, de 3 → `Fizz`, de 5 → `Buzz`, sinon le nombre. Une valeur par ligne.

- [ ] **Step 1 :** dossier + poms (B avec privés), slug `fizzbuzz`.
- [ ] **Step 2 :** starter — `// TODO : parcourir 1 à 100 et afficher Fizz/Buzz/FizzBuzz ou le nombre`. Indice : tester le multiple de 15 **avant** 3 et 5.
- [ ] **Step 3 :** solution — boucle `for` 1..100, cascade `if/else if` avec `% 15` d'abord ; commentaire sur l'ordre des tests.
- [ ] **Step 4 :** util C.
- [ ] **Step 5 :** test public — capture la sortie ; vérifie des lignes clés via `contains` ligne à ligne : `"1"`, `"Fizz"` (pour 3), `"Buzz"` (pour 5), `"FizzBuzz"` (pour 15). Construire l'attendu des 1ères lignes (`1,2,Fizz,4,Buzz,Fizz,7,8,Fizz,Buzz,11,Fizz,13,14,FizzBuzz`) et `startsWith`.
- [ ] **Step 6 :** privés — reconstruire la sortie **complète** des 100 lignes par une boucle équivalente dans le test et `isEqualTo`.
- [ ] **Step 7 :** docs (position `1`, difficulte `facile`, durée `20`, notions `boucle`/`modulo`/`conditions`).
- [ ] **Step 8 :** `git commit -m "feat(exo): 1.3.1 fizzbuzz"`

## Task 9 : Exercice 1.3.2 — fibonacci-iteratif

**Files :** dossier `1.3.2-fibonacci-iteratif/` complet (avec privés + `CaptureEntree`). Classe `etnc.m1.FibonacciIteratif`.

**Contrat I/O :** lit `n` (`nextInt`), affiche le n-ième terme (F(0)=0, F(1)=1), calcul **itératif** (deux variables glissantes, type `long`). Entrée `"10\n"` → sortie :

```text
Quel rang de la suite de Fibonacci ?
F(10) = 55
```

- [ ] **Step 1 :** dossier + poms, slug `fibonacci-iteratif`.
- [ ] **Step 2 :** starter — Scanner, invite, `// TODO : calculer F(n) de façon itérative et l'afficher`. Indice : pas de récursivité (vue plus tard), deux variables `long`.
- [ ] **Step 3 :** solution — boucle itérative, `long precedent`/`courant`, gère n=0 et n=1 ; commentaire.
- [ ] **Step 4 :** util C + D.
- [ ] **Step 5 :** test public — `"10\n"` → `F(10) = 55`.
- [ ] **Step 6 :** privés — `"0\n"` → `0`, `"1\n"` → `1`, `"2\n"` → `1`, `"20\n"` → `6765`.
- [ ] **Step 7 :** docs (position `2`, difficulte `moyen`, durée `25`, notions `boucle`/`suite`).
- [ ] **Step 8 :** `git commit -m "feat(exo): 1.3.2 fibonacci-iteratif"`

## Task 10 : Exercice 1.3.3 — table-multiplication

**Files :** dossier `1.3.3-table-multiplication/` complet (avec privés + `CaptureEntree`). Classe `etnc.m1.TableMultiplication`.

**Contrat I/O :** lit un entier `n` (`nextInt`), affiche sa table de 1 à 10. Entrée `"7\n"` → sortie :

```text
Quelle table ?
7 x 1 = 7
7 x 2 = 14
...
7 x 10 = 70
```

- [ ] **Step 1 :** dossier + poms, slug `table-multiplication`.
- [ ] **Step 2 :** starter — Scanner, invite, `// TODO : afficher la table de n de 1 à 10`.
- [ ] **Step 3 :** solution — boucle `for` `i` de 1 à 10, `n + " x " + i + " = " + (n * i)` ; commentaire.
- [ ] **Step 4 :** util C + D.
- [ ] **Step 5 :** test public — `"7\n"` ; construire l'attendu via une boucle équivalente dans le test et `isEqualTo`.
- [ ] **Step 6 :** privés — `"0\n"` (toutes lignes = 0), `"-3\n"` (produits négatifs).
- [ ] **Step 7 :** docs (position `3`, difficulte `facile`, durée `15`, notions `boucle`).
- [ ] **Step 8 :** `git commit -m "feat(exo): 1.3.3 table-multiplication"`

## Task 11 : Exercice 1.3.4 — devine-le-nombre

**Files :** dossier `1.3.4-devine-le-nombre/` complet (avec privés + `CaptureEntree`). Classe `etnc.m1.DevineLeNombre`.

**Contrat I/O :** le **starter fournit** le tirage du secret (graine fixe) ; le stagiaire écrit la boucle. Constante `GRAINE = 1789`. Secret = `new Random(1789).nextInt(100) + 1`. À chaque essai lu (`nextInt`) : `C'est plus grand.` (essai < secret), `C'est plus petit.` (essai > secret), `Bravo, vous avez trouvé en N essai(s) !` quand trouvé, puis fin.

- [ ] **Step 1 :** dossier + poms, slug `devine-le-nombre`.
- [ ] **Step 2 :** starter `DevineLeNombre.java` :

```java
package etnc.m1;

import java.util.Random;
import java.util.Scanner;

public class DevineLeNombre {
    public static void main(String[] args) {
        final int GRAINE = 1789;                          // NE PAS modifier
        int secret = new Random(GRAINE).nextInt(100) + 1; // nombre à deviner, entre 1 et 100

        Scanner clavier = new Scanner(System.in);
        System.out.println("Devinez le nombre (entre 1 et 100) :");

        // TODO : tant que le nombre n'est pas trouvé, lire un essai (clavier.nextInt())
        //        et afficher "C'est plus grand." / "C'est plus petit." ;
        //        quand l'essai vaut le secret, afficher
        //        "Bravo, vous avez trouvé en N essai(s) !" puis arrêter.
    }
}
```

- [ ] **Step 3 :** solution — boucle `while` avec compteur d'essais, comparaisons, messages exacts ; commentaire sur la condition d'arrêt.
- [ ] **Step 4 :** util C + D.
- [ ] **Step 5 :** test public — dans le test, recalculer `int secret = new Random(1789).nextInt(100) + 1;` puis construire une entrée qui converge (ex. fournir directement `secret` au 1er essai : `secret + "\n"`) → vérifier `contains("Bravo, vous avez trouvé en 1 essai")`.
- [ ] **Step 6 :** privés — entrée à plusieurs essais menant au secret (ex. `"50\n" + ajustements + secret`), vérifier la présence de `"C'est plus grand."` ou `"C'est plus petit."` selon le secret, et le message final avec le bon compte d'essais. Calculer la séquence à partir du `secret` recalculé.
- [ ] **Step 7 :** docs (position `4`, difficulte `moyen`, durée `30`, notions `boucle-while`/`conditions`/`scanner`). `correction.md` : expliquer la condition d'arrêt et le compteur ; mentionner que le secret est fixé pour la reproductibilité pédagogique.
- [ ] **Step 8 :** `git commit -m "feat(exo): 1.3.4 devine-le-nombre"`

## Task 12 : Lint + PR phase 3 + clôture backlog

- [ ] **Step 1 :** `bash scripts/lint-exercices.sh` → `erreurs : 0`, 9 nouveaux exos listés.
- [ ] **Step 2 :** cocher #16 dans `docs/backlog.md` (les 3 sous-groupes + 3 critères d'acceptation `[x]`, statut `Faite`).

```bash
git add docs/backlog.md
git commit -m "docs(backlog): #16 exercices module 1 faite"
```

- [ ] **Step 3 :** push + PR « #16 — Exercices module 1 : sous-groupe 1.3 », CI vert (`valider-solutions` sur les 4 nouveaux exos), merge, resync `main`.

---

## Self-review (rempli à la rédaction du plan)

- **Couverture spec** : §1/§2 périmètre+3 PRs → Phases 1/2/3 ; §3 gabarit → artefacts A–E + chaque task ; §4 interface/test → artefacts C/D + contrats I/O ; §4.4 secret → Task 11 Step 2 (code starter inliné) ; §5 contenu → Tasks 1,2,4,5,6,8,9,10,11 (1 par exo) ; §6 tests-privés (sauf 1.1.2) → Task 1 sans privés, autres avec ; §7 vérif → Steps lint + PR de chaque phase ; §8 workflow → en-têtes de phase ; §9 critères → couverts. ✅ Pas de gap.
- **Placeholders** : les `// TODO` sont le contenu **attendu** des starters (pas des trous du plan) ; les `...` dans les sorties illustratives sont explicités par le contrat. ✅
- **Cohérence des noms** : classes `AffichageFormate`, `LectureSaisie`, `ConversionUnites`, `CalculsGeometriques`, `AnneeBissextile`, `FizzBuzz`, `FibonacciIteratif`, `TableMultiplication`, `DevineLeNombre` ; util `CaptureSortie`/`CaptureEntree.avecEntree` ; graine `1789` cohérente starter/solution/tests. ✅
- **Piège des `double`** (Tasks 4/5) : recommandation de construire l'attendu via la même expression dans le test plutôt qu'un littéral décimal. ✅
```
