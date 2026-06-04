# Plan d'implémentation — Chapitres du module 5 (Exceptions et I/O)

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Produire les 7 chapitres du module 5 (`courses/docs/module-5-exceptions-io/`) conformes à la charte §6, avec un build Docusaurus vert et le renversement du verrou « pas d'exceptions » assumé.

**Architecture:** Contenu pédagogique Markdown pour Docusaurus. 7 chapitres Markdown (`5-1` … `5-7`), chacun lié au suivant par « Prochain chapitre » ; retouche de liaison `4-8`→`5-1` et bascule `_category_.json`. Cadence : 1 commit par chapitre, build Docusaurus comme gate final (les liens forward exigent que toute la chaîne existe avant que `onBrokenLinks: 'throw'` passe).

**Tech Stack:** Markdown + frontmatter Docusaurus ; Node/npm pour le build (`cd courses && npm run build`) ; JDK 25 (`E:/java/jdk-25.0.3+9`) pour compiler les extraits denses.

**Spec de référence :** [`docs/superpowers/specs/2026-06-04-m5-chapitres-design.md`](../specs/2026-06-04-m5-chapitres-design.md). Le plan de contenu détaillé est en §6, les garde-fous antériorité/anti-spoil en §5, les conventions en §4.

**Adaptation contenu (vs plan de code) :** chaque tâche-chapitre fournit le brief concret (frontmatter exact, découpage des sections `## N.`, domaine guidé anti-spoil, ancres de code à inclure, garde-fous antériorité). Le rédacteur produit la prose (~800–2000 mots, charte §6) **dans ces contraintes** — la prose verbatim n'est pas pré-écrite ici. Le « test » est : structure §6 présente + build Docusaurus vert + extraits denses qui compilent.

**Pré-requis (déjà fait) :** branche `feature/m5-chapitres` créée depuis `main`, spec commitée dessus.

**Convention de structure (charte §6, identique M1-M4) — chaque chapitre dans cet ordre :**
1. Frontmatter (`id`, `sidebar_position`, `title`, `description`)
2. `# Titre`
3. `## Pourquoi ce chapitre` (2–3 phrases)
4. `## Ce que vous saurez faire à la fin` (puces, verbes d'action)
5. Sections `## 1.`, `## 2.`, … : notion + `### Exemple` (code annoté) + `### À retenir` (encadré `>`)
6. `## Erreurs fréquentes` (puces **symptôme → cause → correction**)
7. `## Exercice guidé` (pas-à-pas + solution dans `<details>`)
8. `## Vérifiez vos acquis` (questions ouvertes)
9. `## Pour aller plus loin` (1–3 liens annotés : dev.java / Baeldung / Javadoc OpenJDK 25)
10. `## Prochain chapitre` (→ lien relatif sans `.md`)

**Conventions de code (charte §7) :** imports explicites (jamais `java.io.*` / `java.nio.file.*` / `java.util.*` en étoile) ; UTF-8 explicite (`StandardCharsets.UTF_8`) ; `e` admis pour la variable de `catch` ; commentaires français sur le *pourquoi* ; blocs typés ` ```java ` / ` ```text ` / ` ```properties `.

---

## Task 0 : Sanity-check de l'environnement

**Files:** aucun (vérification).

- [ ] **Step 1 : Confirmer la branche et l'outillage**

Run :
```bash
cd "E:\claude\Piscine ETNC" && git rev-parse --abbrev-ref HEAD && node --version && (cd courses && npm run build >NUL 2>&1 && echo "BUILD_BASELINE_OK")
```
Expected : branche `feature/m5-chapitres` ; une version Node ; `BUILD_BASELINE_OK` (le build part vert **avant** ajout — sinon, régler avant de commencer).

> Note : si le build baseline échoue déjà, **stop** et diagnostiquer (problème pré-existant, pas lié au module 5).

---

## Task 1 : Chapitre 5-1 — Hiérarchie des exceptions

**Files:**
- Create: `courses/docs/module-5-exceptions-io/5-1-hierarchie-exceptions.md`

- [ ] **Step 1 : Écrire le chapitre**

Frontmatter exact :
```markdown
---
id: 5-1-hierarchie-exceptions
sidebar_position: 1
title: "Hiérarchie des exceptions"
description: "Comprendre l'arbre Throwable/Error/Exception, distinguer exceptions checked et unchecked, et lire une stack trace."
---
```
`## Pourquoi ce chapitre` : ouvrir par **le pont avec les modules 1-4** — jusqu'ici un cas limite se gérait par sentinelle / `null` / `Optional` ; une exception **signale** une anomalie sans valeur de retour magique. C'est le verrou qui saute (spec §1, §5.1).

Sections suggérées :
- `## 1. Qu'est-ce qu'une exception` — un objet levé qui interrompt le flux ; exemple d'une exception JDK qui « éclate » sans gestion.
- `## 2. L'arbre Throwable` — `Throwable` → `Error` (problème système, **on ne rattrape pas**) / `Exception` → `RuntimeException`. Diagramme en bloc ` ```text `.
- `## 3. Checked vs unchecked` — la **checked** (`Exception` hors `RuntimeException`) est **vérifiée par le compilateur** (force `throws`/`catch`) ; l'**unchecked** (`RuntimeException` et filles) non. Tableau comparatif.
- `## 4. Lire une stack trace` — type, message, ligne, pile d'appels ; distinguer **erreur de compilation** (avant exécution) et **exception à l'exécution**.

Ancre de code (à inclure, ` ```java `) — observer une exception levée par le JDK :
```java
int[] notes = {12, 9, 15};
System.out.println(notes[5]); // ArrayIndexOutOfBoundsException levée à l'exécution
```
`## Erreurs fréquentes` : confondre `Error` et `Exception` (on ne rattrape pas une `Error`) ; croire qu'une `RuntimeException` doit être déclarée avec `throws` ; ignorer le message de la stack trace.

`## Exercice guidé` (**domaine anti-spoil, spec §5.4**) : **cartographier la hiérarchie** — provoquer et identifier 3 exceptions JDK distinctes (`NumberFormatException` via `Integer.parseInt("abc")`, `ArithmeticException` via `10/0`, `ArrayIndexOutOfBoundsException`). Pour chacune : dans quelle branche de l'arbre ? checked ou unchecked ? Solution dans `<details>`. **Ne PAS** faire de gestion défensive d'une saisie validée (réservé à l'exo 5.1.1).

`## Pour aller plus loin` : dev.java « Exceptions » + Javadoc `Throwable` (OpenJDK 25).

`## Prochain chapitre` :
```markdown
## Prochain chapitre

→ **[Chapitre 5-2 — try, catch, finally](5-2-try-catch-finally)**
```

**Garde-fous antériorité (spec §5.2) :** on **observe** des exceptions du JDK ; **ne pas** encore écrire de `try`/`catch` complet (c'est le ch.2), ni d'exception custom (ch.4), ni d'I/O (ch.5+).

- [ ] **Step 2 : Vérifier la structure**

Vérifier visuellement : frontmatter (4 champs), les 10 blocs de la charte §6 présents, lien « Prochain chapitre » exact, blocs de code typés.

- [ ] **Step 3 : Commit**

```bash
cd "E:\claude\Piscine ETNC" && git add courses/docs/module-5-exceptions-io/5-1-hierarchie-exceptions.md && git commit -m "docs(#21): chapitre 5-1 hiérarchie des exceptions

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

---

## Task 2 : Chapitre 5-2 — try / catch / finally

**Files:**
- Create: `courses/docs/module-5-exceptions-io/5-2-try-catch-finally.md`

- [ ] **Step 1 : Écrire le chapitre**

Frontmatter exact :
```markdown
---
id: 5-2-try-catch-finally
sidebar_position: 2
title: "try, catch, finally"
description: "Rattraper et propager des exceptions avec try/catch/finally, le multi-catch, et la différence entre throw et throws."
---
```
Sections suggérées :
- `## 1. Rattraper avec try/catch` — structure ; capter **du plus précis au plus général** (ordre des `catch`).
- `## 2. multi-catch` — `catch (NumberFormatException | ArithmeticException e)` quand le traitement est commun.
- `## 3. finally` — **toujours** exécuté (même sur `return` / exception), pour libérer/nettoyer.
- `## 4. throw vs throws** — `throw` lève une instance ; `throws` la **déclare** dans la signature. **Propagation** : une exception non rattrapée remonte la pile d'appels jusqu'au `main`.

Ancre de code (` ```java `) :
```java
try {
    int valeur = Integer.parseInt(saisie);
    System.out.println(100 / valeur);
} catch (NumberFormatException e) {
    System.out.println("Pas un nombre : " + e.getMessage());
} catch (ArithmeticException e) {
    System.out.println("Division par zéro");
} finally {
    System.out.println("Traitement terminé"); // toujours exécuté
}
```
`## Erreurs fréquentes` : `catch (Exception e)` placé **avant** un `catch` plus précis (code mort → **ne compile pas**) ; avaler l'exception (`catch` vide) ; confondre `throw` (lever) et `throws` (déclarer).

`## Exercice guidé` (**anti-spoil**) : **division entière protégée** contre le `/0` avec un `finally` qui « libère une ressource » (simulée par un `println`). Arithmétique neutre — **ni** saisie validée métier (5.1.1), **ni** géométrie (5.1.3). Solution dans `<details>`.

`## Pour aller plus loin` : Baeldung « Java Exceptions » + dev.java handling exceptions.

`## Prochain chapitre` :
```markdown
→ **[Chapitre 5-3 — try-with-resources](5-3-try-with-resources)**
```

**Garde-fous :** pas encore de `try-with-resources` (ch.3), pas d'I/O (ch.5+), pas d'exception custom (ch.4).

- [ ] **Step 2 : Vérifier la structure** (idem Task 1 Step 2).
- [ ] **Step 3 : Commit**
```bash
cd "E:\claude\Piscine ETNC" && git add courses/docs/module-5-exceptions-io/5-2-try-catch-finally.md && git commit -m "docs(#21): chapitre 5-2 try/catch/finally

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

---

## Task 3 : Chapitre 5-3 — try-with-resources

**Files:**
- Create: `courses/docs/module-5-exceptions-io/5-3-try-with-resources.md`

- [ ] **Step 1 : Écrire le chapitre**

Frontmatter exact :
```markdown
---
id: 5-3-try-with-resources
sidebar_position: 3
title: "try-with-resources"
description: "Fermer automatiquement les ressources avec AutoCloseable et le bloc try-with-resources."
---
```
Sections suggérées :
- `## 1. Le problème du finally manuel` — verbeux, oubli de `close()`, exception masquée.
- `## 2. AutoCloseable et la syntaxe try-with-resources` — `try (Ressource r = …) { … }` → `close()` **automatique et déterministe**.
- `## 3. Plusieurs ressources` — fermées dans l'**ordre inverse** d'ouverture.
- `## 4. Notion de suppressed exception` — **mention** (sans détailler), l'exception du `close()` ne masque pas celle du corps.

Ancre de code (` ```java `, **doit compiler — extrait à tester Task 10**) :
```java
class Chronometre implements AutoCloseable {
    Chronometre() { System.out.println("Démarrage"); }
    @Override public void close() { System.out.println("Arrêt"); }
}

public class Demo {
    public static void main(String[] args) {
        try (Chronometre c = new Chronometre()) {
            System.out.println("Mesure en cours");
        } // close() appelé automatiquement ici, même en cas d'exception
    }
}
```
`## Erreurs fréquentes` : déclarer la ressource **hors** du `try` (pas de fermeture auto) ; oublier d'implémenter `AutoCloseable` ; croire que `try-with-resources` **rattrape** l'exception (il ferme, il ne capte pas).

`## Exercice guidé` (**anti-spoil**) : écrire une **ressource maison** `Chronometre`/`Connexion` qui trace ouverture/fermeture — **pas encore de fichier** (les fichiers viennent au ch.5). Solution dans `<details>`.

`## Pour aller plus loin` : dev.java « try-with-resources » + Baeldung.

`## Prochain chapitre` :
```markdown
→ **[Chapitre 5-4 — Exceptions personnalisées](5-4-exceptions-personnalisees)**
```

**Garde-fous :** ressource **maison** (pas de flux fichier ici), pas d'I/O réelle.

- [ ] **Step 2 : Vérifier la structure**.
- [ ] **Step 3 : Commit**
```bash
cd "E:\claude\Piscine ETNC" && git add courses/docs/module-5-exceptions-io/5-3-try-with-resources.md && git commit -m "docs(#21): chapitre 5-3 try-with-resources

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

---

## Task 4 : Chapitre 5-4 — Exceptions personnalisées

**Files:**
- Create: `courses/docs/module-5-exceptions-io/5-4-exceptions-personnalisees.md`

- [ ] **Step 1 : Écrire le chapitre**

Frontmatter exact :
```markdown
---
id: 5-4-exceptions-personnalisees
sidebar_position: 4
title: "Exceptions personnalisées"
description: "Créer ses propres exceptions métier (extends RuntimeException), les lever et chaîner leur cause."
---
```
Sections suggérées :
- `## 1. Pourquoi une exception métier` — exprimer une règle violée plus clairement qu'une exception générique.
- `## 2. Créer son exception` — `extends RuntimeException` (**choix du module, spec §10.1** : unchecked, signatures légères pour débutants ; **mentionner** qu'`extends Exception` la rendrait *checked*). Constructeurs `(String message)` et `(String message, Throwable cause)`.
- `## 3. Lever et rattraper la sienne** — `throw new …Exception("…")` puis `catch`.
- `## 4. Chaînage (cause)` — encapsuler une exception de bas niveau sans perdre l'origine (`getCause()`).

Ancre de code (` ```java `, **extrait à tester Task 10**) :
```java
public class SoldeInsuffisantException extends RuntimeException {
    public SoldeInsuffisantException(String message) {
        super(message);
    }
    public SoldeInsuffisantException(String message, Throwable cause) {
        super(message, cause);
    }
}
```
`## Erreurs fréquentes` : créer une exception là où `IllegalArgumentException` (JDK) suffirait ; **perdre la cause** (relancer sans la passer) ; oublier le suffixe `Exception` dans le nom.

`## Exercice guidé` (**anti-spoil**) : `SoldeInsuffisantException` sur un **retrait bancaire** (`retirer(montant)` lève si `montant > solde`). **Ni** effectif d'unité (5.1.2), **ni** géométrie (5.1.3). Solution dans `<details>`.

`## Pour aller plus loin` : Baeldung « Custom Exceptions » + dev.java.

`## Prochain chapitre` :
```markdown
→ **[Chapitre 5-5 — I/O classique](5-5-io-classique)**
```

**Garde-fous :** unchecked par défaut (cohérent §10.1) ; pas d'I/O ici.

- [ ] **Step 2 : Vérifier la structure**.
- [ ] **Step 3 : Commit**
```bash
cd "E:\claude\Piscine ETNC" && git add courses/docs/module-5-exceptions-io/5-4-exceptions-personnalisees.md && git commit -m "docs(#21): chapitre 5-4 exceptions personnalisées

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

---

## Task 5 : Chapitre 5-5 — I/O classique

**Files:**
- Create: `courses/docs/module-5-exceptions-io/5-5-io-classique.md`

- [ ] **Step 1 : Écrire le chapitre**

Frontmatter exact :
```markdown
---
id: 5-5-io-classique
sidebar_position: 5
title: "I/O classique"
description: "Lire et écrire des fichiers texte avec FileReader/BufferedReader et PrintWriter, en gérant IOException."
---
```
Sections suggérées :
- `## 1. Le modèle de flux` — ouvrir → lire/écrire → fermer ; `File` = un chemin.
- `## 2. Écrire un fichier texte` — `PrintWriter` + `println`, encodage explicite.
- `## 3. Lire ligne à ligne` — `BufferedReader.readLine()` en boucle **jusqu'à `null`**.
- `## 4. IOException, une exception checked` — pont avec ch.2 (rattraper/déclarer) et ch.3 (`try-with-resources` ferme le flux). **Annoncer que NIO.2 (ch.6) simplifie tout ça.**

Ancre de code (` ```java `, **extrait à tester Task 10** ; UTF-8 explicite + try-with-resources) :
```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

// ... lecture d'un fichier en UTF-8, ressource fermée automatiquement
try (BufferedReader lecteur = new BufferedReader(
        new InputStreamReader(new FileInputStream("notes.txt"), StandardCharsets.UTF_8))) {
    String ligne;
    while ((ligne = lecteur.readLine()) != null) {
        System.out.println(ligne);
    }
} catch (IOException e) {
    System.out.println("Lecture impossible : " + e.getMessage());
}
```
`## Erreurs fréquentes` : oublier de fermer le flux (fuite → d'où `try-with-resources`) ; mauvais encodage (accents cassés) ; boucle de lecture qui ne teste pas `null`.

`## Exercice guidé` (**anti-spoil**) : **écrire puis relire** une courte note multi-lignes (liste de **capitales**). **Ni** comptage de lignes (5.2.1), **ni** copie (5.2.2), **ni** append (5.2.3), **ni** config (5.2.4). Solution dans `<details>`.

`## Pour aller plus loin` : Baeldung « Java IO » + Javadoc `BufferedReader` (OpenJDK 25).

`## Prochain chapitre` :
```markdown
→ **[Chapitre 5-6 — NIO.2 : Path et Files](5-6-nio2)**
```

**Garde-fous :** `IOException` est la 1re exception **checked** vraiment manipulée ; réutilise `try-with-resources` (ch.3). Pas de `Path`/`Files` ici (ch.6).

- [ ] **Step 2 : Vérifier la structure**.
- [ ] **Step 3 : Commit**
```bash
cd "E:\claude\Piscine ETNC" && git add courses/docs/module-5-exceptions-io/5-5-io-classique.md && git commit -m "docs(#21): chapitre 5-5 I/O classique

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

---

## Task 6 : Chapitre 5-6 — NIO.2 (Path et Files)

**Files:**
- Create: `courses/docs/module-5-exceptions-io/5-6-nio2.md`

- [ ] **Step 1 : Écrire le chapitre**

Frontmatter exact :
```markdown
---
id: 5-6-nio2
sidebar_position: 6
title: "NIO.2 : Path et Files"
description: "Lire et écrire des fichiers avec l'API moderne java.nio.file (Path, Files, readAllLines, lines, writeString)."
---
```
Sections suggérées :
- `## 1. Path, le chemin moderne` — `Path` / `Paths.get(...)` vs l'ancien `File`.
- `## 2. Lire un fichier` — `Files.readAllLines` (petit fichier → `List<String>`), `Files.readString` ; **`Files.lines` renvoie un `Stream` à fermer** (`try-with-resources` + lien M4 streams).
- `## 3. Écrire un fichier` — `Files.writeString`, `Files.write(path, lignes)`.
- `## 4. UTF-8 et utilitaires` — `StandardCharsets.UTF_8` explicite ; `Files.exists`, `Files.copy`. **Annoncer : les exos I/O imposent NIO.2 (spec §10.2).**

Ancre de code (` ```java `, **extrait à tester Task 10** ; `Files.lines` + `try-with-resources` + stream M4) :
```java
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;

Path fichier = Path.of("fruits.txt");
Files.writeString(fichier, "pomme\npoire\nkiwi\n", StandardCharsets.UTF_8);

// lecture simple, petit fichier
List<String> lignes = Files.readAllLines(fichier, StandardCharsets.UTF_8);

// lecture en flux : le Stream DOIT être fermé
try (Stream<String> flux = Files.lines(fichier, StandardCharsets.UTF_8)) {
    flux.map(String::toUpperCase).forEach(System.out::println);
}
```
`## Erreurs fréquentes` : `Files.lines` **sans** `try-with-resources` (`Stream` non fermé) ; charger un fichier énorme avec `readAllLines` (tout en mémoire) ; oublier l'encodage (défaut plateforme ≠ UTF-8 partout).

`## Exercice guidé` (**anti-spoil**) : écrire une **liste de fruits** (`writeString`) puis la **relire et transformer en majuscules** via `Files.lines`. **Transformation**, pas comptage/copie/append. Solution dans `<details>`.

`## Pour aller plus loin` : dev.java « Reading and Writing Files » + Javadoc `Files` (OpenJDK 25).

`## Prochain chapitre` :
```markdown
→ **[Chapitre 5-7 — Formats texte : CSV et properties](5-7-formats-texte)**
```

**Garde-fous :** réutilise `try-with-resources` (ch.3) et les streams (M4) ; pas encore de format CSV/properties (ch.7).

- [ ] **Step 2 : Vérifier la structure**.
- [ ] **Step 3 : Commit**
```bash
cd "E:\claude\Piscine ETNC" && git add courses/docs/module-5-exceptions-io/5-6-nio2.md && git commit -m "docs(#21): chapitre 5-6 NIO.2 (Path et Files)

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

---

## Task 7 : Chapitre 5-7 — Formats texte (CSV et properties)

**Files:**
- Create: `courses/docs/module-5-exceptions-io/5-7-formats-texte.md`

- [ ] **Step 1 : Écrire le chapitre**

Frontmatter exact :
```markdown
---
id: 5-7-formats-texte
sidebar_position: 7
title: "Formats texte : CSV et properties"
description: "Manipuler des fichiers CSV et .properties à la main, et découvrir JSON en survol."
---
```
Sections suggérées :
- `## 1. Le format CSV` — lignes, séparateur, en-tête ; écrire avec `String.join(",", …)`, lire avec `String.split(",")`.
- `## 2. Limites du split naïf` — virgule dans un champ, guillemets : **honnêteté** — on reste sur du CSV **simple sans champ échappé** (les libs gèrent le reste).
- `## 3. Les fichiers .properties` — `java.util.Properties` : `load(Reader)`, `getProperty(clé, défaut)`. Bloc ` ```properties ` pour l'exemple de fichier.
- `## 4. Et JSON ?` — **mention seulement** : à quoi ça ressemble (` ```text ` d'un petit objet), qu'on utilise une **lib** (Jackson/Gson) en production, **pas d'exo, pas de dépendance** dans la Piscine (cohérent offline).

Ancre de code (` ```java `, **extrait à tester Task 10** ; CSV + Properties) :
```java
// CSV simple : titre,annee
String ligne = "Le Petit Prince,1943";
String[] champs = ligne.split(",");
String titre = champs[0];
int annee = Integer.parseInt(champs[1]);

String exportLigne = String.join(",", titre, String.valueOf(annee));
```
Bloc ` ```properties ` pour l'exemple `.properties` :
```properties
langue=fr
niveau=debutant
```
`## Erreurs fréquentes` : `split(",")` sur une ligne contenant une virgule dans un champ ; oublier l'en-tête CSV ; lire un `.properties` avec le mauvais encodage.

`## Exercice guidé` (**anti-spoil**) : lire un petit **CSV de livres** (`titre,annee`) en objets + lire un `app.properties` (`langue=fr`). Domaine « bibliothèque » — **ni** personnel (5.3.1), **ni** export trié (5.3.2), **ni** commandes (5.3.3), **ni** config 5.2.4. Solution dans `<details>`.

`## Pour aller plus loin` : Baeldung « Reading CSV » + Javadoc `Properties` (OpenJDK 25).

`## Prochain chapitre` (le module 6 n'existe pas encore — **texte simple, pas de lien**) :
```markdown
## Prochain chapitre

→ **Module 6 — Tests et Git** *(à venir)*
```

**Garde-fous :** lit/écrit via I/O (ch.5/6) ; CSV à la main (pas de lib) ; JSON en survol sans exo (spec §10.4).

- [ ] **Step 2 : Vérifier la structure**.
- [ ] **Step 3 : Commit**
```bash
cd "E:\claude\Piscine ETNC" && git add courses/docs/module-5-exceptions-io/5-7-formats-texte.md && git commit -m "docs(#21): chapitre 5-7 formats texte (CSV et properties)

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

---

## Task 8 : Liaison module 4 → module 5 + bascule _category_.json

**Files:**
- Modify: `courses/docs/module-4-collections-generiques-lambdas/4-8-streams-optional.md` (bloc « Prochain chapitre »)
- Modify: `courses/docs/module-5-exceptions-io/_category_.json` (`collapsed`)

- [ ] **Step 1 : Retoucher la liaison de 4-8**

Remplacer le bloc final actuel :
```markdown
## Prochain chapitre

→ **Module 5 — Exceptions et I/O** *(à venir)*
```
par :
```markdown
## Prochain chapitre

→ **[Module 5 · Chapitre 5-1 — Hiérarchie des exceptions](../module-5-exceptions-io/5-1-hierarchie-exceptions)**
```

- [ ] **Step 2 : Basculer `_category_.json`**

Dans `courses/docs/module-5-exceptions-io/_category_.json`, passer `"collapsed": true` → `"collapsed": false` (homogénéité M1-M4). Ne **rien** changer d'autre (`position: 6`, label, `link` inchangés).

- [ ] **Step 3 : Commit**
```bash
cd "E:\claude\Piscine ETNC" && git add courses/docs/module-4-collections-generiques-lambdas/4-8-streams-optional.md courses/docs/module-5-exceptions-io/_category_.json && git commit -m "docs(#21): liaison 4-8 -> 5-1 et ouverture du module 5 dans la sidebar

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

---

## Task 9 : Gate de vérification — build Docusaurus

**Files:** aucun (vérification ; corrections si build cassé).

- [ ] **Step 1 : Build complet**

Run :
```bash
cd "E:\claude\Piscine ETNC\courses" && npm run build
```
Expected : build **réussi**, **0** broken link. `onBrokenLinks: 'throw'` casse au moindre lien interne mort.

- [ ] **Step 2 : Corriger si nécessaire**

Si un lien casse : vérifier les `id`/chemins relatifs (sans `.md`), la cible `../module-5-exceptions-io/5-1-hierarchie-exceptions`, et chaque « Prochain chapitre » `5-N` → `5-(N+1)`. Re-builder jusqu'au vert. Commiter les corrections :
```bash
cd "E:\claude\Piscine ETNC" && git add courses/docs/ && git commit -m "docs(#21): corrige les liens internes du module 5 (build vert)

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```
*(Sauter le commit si aucune correction n'a été nécessaire.)*

---

## Task 10 : Compilation des extraits denses (JDK 25)

**Files:** fichiers temporaires hors dépôt (ne pas commiter).

- [ ] **Step 1 : Compiler les ancres de code des chapitres 3, 5, 6, 7**

Pour chaque extrait Java non trivial (Chronometre `AutoCloseable` du 5-3 ; lecture `BufferedReader`+UTF-8 du 5-5 ; `Files.lines`/`writeString` du 5-6 ; CSV/`Properties` du 5-7), créer un fichier `.java` minimal autour de l'extrait dans un dossier temp et compiler :
```bash
cd "$env:TEMP" ; & "E:/java/jdk-25.0.3+9/bin/javac" -d . Extrait.java ; echo "EXIT=$LASTEXITCODE"
```
Expected : `EXIT=0` pour chaque extrait (aucune erreur de compilation). Corriger l'extrait **dans le chapitre** et re-commiter si un extrait ne compile pas (précédent M4 : de vrais bugs de syntaxe attrapés ici).

> Note : les extraits utilisant un fichier (`notes.txt`, `fruits.txt`) compilent sans que le fichier existe ; ne pas les **exécuter**, juste les **compiler**.

---

## Task 11 : Relecture finale (charte §10)

**Files:** les 7 chapitres (corrections inline si besoin).

- [ ] **Step 1 : Passer la checklist de relecture**

Vérifier sur les 7 chapitres :
- [ ] Structure charte §6 respectée (10 blocs, dans l'ordre) sur chaque chapitre.
- [ ] Niveau de langue débutant : vouvoiement, phrases courtes, voix active ; anglicismes (`stream`, `buffer`, `parser`) annotés à la 1re occurrence ; acronymes développés (I/O, NIO, CSV, UTF-8).
- [ ] **Antériorité interne** tenue (spec §5.2) : hiérarchie (1) avant `catch` (2) ; `finally` (2) avant `try-with-resources` (3) ; `try-with-resources` (3) avant I/O (5/6) ; I/O (5/6) avant formats (7). Aucune notion de module ultérieur (JUnit écrit par le stagiaire, Git avancé).
- [ ] **Renversement du verrou « pas d'exceptions »** présent et explicité au 5-1 (pont avec sentinelles/`Optional` M1-M4).
- [ ] **Anti-spoil** (spec §5.4) : aucun exercice guidé ne donne la solution d'un exo noté futur (cartographie / division / Chronometre / banque / capitales / fruits→majuscules / livres+properties).
- [ ] Chaque chapitre a son « Exercice guidé » (solution dans `<details>`) et ses « Vérifiez vos acquis ».
- [ ] Imports explicites partout, UTF-8 explicite dans les extraits I/O.

- [ ] **Step 2 : Commit des corrections de relecture** (si besoin)
```bash
cd "E:\claude\Piscine ETNC" && git add courses/docs/module-5-exceptions-io/ && git commit -m "docs(#21): relecture finale des chapitres module 5

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

---

## Task 12 : Clôture — backlog + PR

**Files:**
- Modify: `docs/backlog.md` (#21, avancement du volet chapitres)

- [ ] **Step 1 : Mettre à jour le backlog**

Dans `docs/backlog.md`, sur la ligne/section **#21**, marquer le **volet chapitres FAIT** (7 chapitres mergés à venir) en gardant les 3 sous-groupes d'exos (5.1/5.2/5.3) à produire. Respecter le format des entrées existantes (cf. clôture #20). Commit :
```bash
cd "E:\claude\Piscine ETNC" && git add docs/backlog.md && git commit -m "docs(#21): backlog — volet chapitres du module 5 produit

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

- [ ] **Step 2 : Pousser la branche et ouvrir la PR** (après feu vert du formateur)
```bash
cd "E:\claude\Piscine ETNC" && git push -u origin feature/m5-chapitres
gh pr create --base main --head feature/m5-chapitres --title "#21 — chapitres module 5 (Exceptions et I/O)" --body "Les 7 chapitres du module 5 (hiérarchie / try-catch-finally / try-with-resources / exceptions perso + I/O classique / NIO.2 / formats texte). Liaison 4-8 -> 5-1, sidebar module 5 ouverte. Build Docusaurus vert, extraits denses compilés au JDK 25.

Spec : docs/superpowers/specs/2026-06-04-m5-chapitres-design.md
Plan : docs/superpowers/plans/2026-06-04-m5-chapitres.md

🤖 Generated with [Claude Code](https://claude.com/claude-code)"
```
Expected : CI 4 jobs verts (dont `build-docusaurus`) → merge par un autre formateur.

---

## Vérification d'auto-revue (faite à la rédaction du plan)

- **Couverture spec :** les 7 chapitres (spec §2/§6) → Tasks 1-7 ; retouche liaison + `_category_` (spec §2) → Task 8 ; build `onBrokenLinks` (spec §7) → Task 9 ; compilation extraits (spec §7) → Task 10 ; relecture charte §10 + garde-fous §5 (antériorité, anti-spoil, renversement du verrou) → Task 11 ; backlog + PR 1 commit/chapitre (spec §8) → Tasks 1-7 + 12. **Annexe §10 (décisions exos)** = hors périmètre de ce cycle (aucun livrable), non couverte par une tâche — c'est voulu.
- **Placeholders :** aucun ; chaque tâche a frontmatter exact, sections, ancres de code concrètes, commandes exactes.
- **Cohérence des identifiants :** `id`/chemins `5-N-…` cohérents entre frontmatter, liens « Prochain chapitre » et la cible de liaison `../module-5-exceptions-io/5-1-hierarchie-exceptions`.
