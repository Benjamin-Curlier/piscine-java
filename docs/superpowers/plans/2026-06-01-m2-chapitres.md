# #18 — Chapitres module 2 : plan d'implémentation

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:executing-plans pour exécuter ce plan tâche par tâche (exécution **inline** — pas de sous-agents, contrainte projet). Les étapes utilisent des cases à cocher (`- [ ]`).

**Goal:** Produire les 5 chapitres du module 2 (2.1 → 2.5) dans `courses/docs/module-2-tableaux-chaines-methodes/`, conformes à la charte §6, et vérifiés par le build Docusaurus.

**Architecture:** Contenu Markdown statique consommé par Docusaurus (sidebar auto-générée via `_category_.json`). Chaque chapitre est un fichier autonome suivant la structure imposée. La « vérification » d'un chapitre n'est pas un test unitaire mais le **build Docusaurus** (`npm run build`, `onBrokenLinks: 'throw'`) plus une relecture de conformité charte. Les extraits de code ne sont pas compilés automatiquement (relecture manuelle ; Java 25 dispo localement si vérification ponctuelle nécessaire).

**Tech Stack:** Markdown / MDX, Docusaurus (Node/npm disponibles localement ; `courses/node_modules` présent).

**Spec:** [`docs/superpowers/specs/2026-06-01-m2-chapitres-design.md`](../specs/2026-06-01-m2-chapitres-design.md)

**Branche:** `feature/m2-chapitres` (déjà créée, spec commitée `fe141e7`).

---

## Cadre commun à toutes les tâches de rédaction

Avant d'écrire un chapitre, le rédacteur garde sous les yeux :
- **Charte §6** (structure), **§2/§3** (ton/langue, vouvoiement, règle des trois fois), **§7** (code : anglais pour Java, commentaires français du *pourquoi*, lisibilité avant astuce, imports explicites), **§9** (markdown).
- **Modèle vivant** : les chapitres du module 1, en particulier [`1-7-bonnes-pratiques-lisibilite.md`](../../../courses/docs/module-1-fondamentaux/1-7-bonnes-pratiques-lisibilite.md). Copier ses conventions exactes (frontmatter ; encadrés `> **À retenir**` ; tableaux ; `<details><summary>` pour la solution du guidé ; liens « Pour aller plus loin » avec libellé annoté).
- **Garde-fous spec §5** :
  - **§5.2 garde-fou central** : dans les chapitres **2-1, 2-2, 2-3**, **aucune méthode `static` maison** dans les exemples ni l'exercice guidé — tout dans `main` ou en extraits sans signature de méthode. *Appeler* une méthode de bibliothèque (`Arrays.toString`, `s.length()`) est permis ; *écrire* la sienne n'arrive qu'au chapitre 2-4. 2-5 vient après 2-4.
  - **§5.3 anti-spoil** : l'exercice guidé ne résout jamais un exo noté d'un futur sous-groupe (mapping ci-dessous).
- **Longueur** : 800–2000 mots.

**Squelette de section imposé (ordre strict) :**
1. Frontmatter `id` / `sidebar_position` / `title` / `description`.
2. `# Titre`
3. `## Pourquoi ce chapitre`
4. `## Ce que vous saurez faire à la fin`
5. `## 1. …`, `## 2. …` (notion + `### Exemple` + `### À retenir`)
6. `## Erreurs fréquentes`
7. `## Exercice guidé` (solution dans `<details><summary>Solution…</summary>`)
8. `## Vérifiez vos acquis`
9. `## Pour aller plus loin`
10. `## Prochain chapitre`

---

## Task 1 : Config Docusaurus du module 2 (`_category_.json`)

**Files:**
- Create: `courses/docs/module-2-tableaux-chaines-methodes/_category_.json`

- [ ] **Step 1 : Créer le dossier et le fichier `_category_.json` (verbatim)**

```json
{
  "label": "Module 2 — Tableaux, chaînes, méthodes",
  "position": 3,
  "collapsible": true,
  "collapsed": false,
  "link": {
    "type": "generated-index",
    "title": "Module 2 — Tableaux, chaînes, méthodes",
    "description": "Tableaux 1D et 2D, chaînes de caractères, méthodes réutilisables et récursivité. À l'issue de ce module, vous saurez structurer votre code en méthodes et manipuler des collections de données."
  }
}
```

- [ ] **Step 2 : Commit**

```bash
git add courses/docs/module-2-tableaux-chaines-methodes/_category_.json
git commit -m "docs(m2): config Docusaurus du module 2"
```

---

## Task 2 : Chapitre 2.1 — Tableaux 1D

**Files:**
- Create: `courses/docs/module-2-tableaux-chaines-methodes/2-1-tableaux-1d.md`

- [ ] **Step 1 : Frontmatter (verbatim)**

```markdown
---
id: 2-1-tableaux-1d
sidebar_position: 1
title: "Tableaux 1D"
description: "Déclarer, initialiser et parcourir un tableau à une dimension, utiliser length et afficher avec Arrays.toString."
---
```

- [ ] **Step 2 : Corps** — plan de sections :
  1. **Qu'est-ce qu'un tableau** — collection de taille **fixe**, éléments du même type, indexée à partir de `0`.
  2. **Déclarer et créer** — `int[] notes;`, allocation `new int[5]` (valeurs par défaut `0`), littéral `int[] notes = {12, 8, 15};`.
  3. **Accéder aux éléments** — `notes[0]`, `notes[i]` ; modification `notes[2] = 20;`.
  4. **La taille avec `length`** — `notes.length` (c'est un **attribut**, sans parenthèses — contraster avec `String.length()` annoncé pour 2-3, règle des trois fois 1ʳᵉ occurrence).
  5. **Parcourir** — boucle `for` indexée (`for (int i = 0; i < notes.length; i++)`) et boucle `for-each` (`for (int note : notes)`) ; quand préférer l'une ou l'autre.
  6. **Afficher un tableau** — `import java.util.Arrays;` puis `Arrays.toString(notes)` (afficher `notes` directement donne une adresse illisible — le signaler).
  - **Antériorité §5.2** : tout dans `main`, aucune méthode maison. Boucles/conditions du module 1 autorisées.
- [ ] **Step 3 : Erreurs fréquentes** : `ArrayIndexOutOfBoundsException` (accès à `notes[notes.length]`, off-by-one) ; confusion `length` (tableau, sans `()`) vs `length()` (String) ; afficher `notes` au lieu de `Arrays.toString(notes)` ; croire que la taille peut changer après création.
- [ ] **Step 4 : Exercice guidé** — calculer la **somme** des éléments d'un tableau d'entiers, puis afficher le tableau via `Arrays.toString`. **Ne pas** faire min/max/moyenne (exo 2.1.1), ni inversion (2.1.2), ni recherche (2.1.3). Tout dans `main`. Solution dans `<details>`.
- [ ] **Step 5 : Vérifiez vos acquis** — ex. : à partir de quel indice commence un tableau ? quelle est la différence entre `length` et `length()` ? que se passe-t-il si on accède à `t[t.length]` ?
- [ ] **Step 6 : Pour aller plus loin** :
  - `https://dev.java/learn/language-basics/arrays/` (dev.java — arrays)
  - `https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/Arrays.html` (Javadoc 25 — `Arrays`)
- [ ] **Step 7 : Prochain chapitre** → `[2-2-tableaux-2d](2-2-tableaux-2d)` « Tableaux 2D ».
- [ ] **Step 8 : Commit**

```bash
git add courses/docs/module-2-tableaux-chaines-methodes/2-1-tableaux-1d.md
git commit -m "docs(m2): chapitre 2.1 tableaux 1D"
```

---

## Task 3 : Chapitre 2.2 — Tableaux 2D

**Files:**
- Create: `courses/docs/module-2-tableaux-chaines-methodes/2-2-tableaux-2d.md`

- [ ] **Step 1 : Frontmatter (verbatim)**

```markdown
---
id: 2-2-tableaux-2d
sidebar_position: 2
title: "Tableaux 2D"
description: "Manipuler des matrices avec int[][], parcourir en boucles imbriquées, et représenter des grilles ou des images noir et blanc."
---
```

- [ ] **Step 2 : Corps** — plan de sections :
  1. **Du tableau 1D à la matrice** — un `int[][]` est un **tableau de tableaux**, vu comme une grille **lignes × colonnes**.
  2. **Déclarer et créer** — `int[][] grille = new int[3][4];` ; littéral imbriqué `{{1, 0}, {0, 1}}`.
  3. **Accéder à une case** — `grille[i][j]` (ligne `i`, colonne `j`).
  4. **Les deux dimensions** — `grille.length` (nombre de lignes) vs `grille[i].length` (nombre de colonnes de la ligne `i`).
  5. **Parcourir** — deux boucles `for` imbriquées (ligne externe, colonne interne) ; afficher proprement ligne par ligne.
  6. **Application** — image noir & blanc comme matrice de `0`/`1`, ou grille de jeu / damier.
  - **Antériorité §5.2** : tout dans `main`, aucune méthode maison.
- [ ] **Step 3 : Erreurs fréquentes** : inverser ligne et colonne (`grille[j][i]`) ; supposer que toutes les lignes ont la même longueur ; double `ArrayIndexOutOfBoundsException` ; confondre `grille.length` et `grille[i].length`.
- [ ] **Step 4 : Exercice guidé** — afficher une matrice **ligne par ligne**, puis calculer la **somme d'une ligne** donnée. **Ne pas** faire la rotation de matrice (exo 2.1.4) ni de parcours récursif (2.4.2). Tout dans `main`. Solution dans `<details>`.
- [ ] **Step 5 : Vérifiez vos acquis** — ex. : que représente `grille[1][2]` ? comment obtient-on le nombre de colonnes ? pourquoi deux boucles imbriquées ?
- [ ] **Step 6 : Pour aller plus loin** :
  - `https://www.baeldung.com/java-multi-dimensional-arrays` (Baeldung — multidimensional arrays)
  - `https://dev.java/learn/language-basics/arrays/` (dev.java — arrays, section tableaux de tableaux)
- [ ] **Step 7 : Prochain chapitre** → `[2-3-chaines-de-caracteres](2-3-chaines-de-caracteres)` « Chaînes de caractères ».
- [ ] **Step 8 : Commit**

```bash
git add courses/docs/module-2-tableaux-chaines-methodes/2-2-tableaux-2d.md
git commit -m "docs(m2): chapitre 2.2 tableaux 2D"
```

---

## Task 4 : Chapitre 2.3 — Chaînes de caractères

**Files:**
- Create: `courses/docs/module-2-tableaux-chaines-methodes/2-3-chaines-de-caracteres.md`

- [ ] **Step 1 : Frontmatter (verbatim)**

```markdown
---
id: 2-3-chaines-de-caracteres
sidebar_position: 3
title: "Chaînes de caractères"
description: "Comprendre que String est immuable, utiliser ses méthodes courantes (length, charAt, substring, indexOf, split), comparer avec equals, et concaténer efficacement avec StringBuilder."
---
```

- [ ] **Step 2 : Corps** — plan de sections :
  1. **`String`, un objet immuable** — une chaîne ne se modifie jamais sur place ; toute « transformation » crée une **nouvelle** chaîne.
  2. **Longueur et accès** — `length()` (méthode, **avec** parenthèses — contraste avec `length` des tableaux, règle des trois fois) ; `charAt(i)` (type `char`) ; les indices vont de `0` à `length() - 1`.
  3. **Extraire et chercher** — `substring(debut, fin)` (fin exclue) ; `indexOf("x")` (`-1` si absent).
  4. **Comparer des chaînes** — `equals(...)` pour le contenu ; **piège `==`** (compare les références, pas le contenu) — rappel du piège annoncé au module 1, **résolu ici**.
  5. **Transformer** — `toUpperCase()`, `toLowerCase()`, `trim()` ; découper avec `split(" ")` (retourne un `String[]` — lien chap. 2-1).
  6. **Concaténer efficacement** — `+` crée une nouvelle chaîne à chaque fois ; dans une boucle, préférer `StringBuilder` (`append`, `toString`).
  - **Antériorité §5.2** : tout dans `main`, aucune méthode maison.
- [ ] **Step 3 : Erreurs fréquentes** : comparer deux chaînes avec `==` au lieu de `equals` ; `StringIndexOutOfBoundsException` (mauvais indice de `substring`/`charAt`) ; croire qu'une méthode comme `toUpperCase()` modifie la chaîne d'origine (il faut récupérer la valeur retournée) ; concaténer dans une grande boucle avec `+`.
- [ ] **Step 4 : Exercice guidé** — mettre une phrase en **majuscules** et afficher sa **longueur** (`length()`). **Ne pas** faire palindrome (exo 2.2.1), comptage d'occurrences (2.2.2) ni ASCII art (2.2.3). Tout dans `main`. Solution dans `<details>`.
- [ ] **Step 5 : Vérifiez vos acquis** — ex. : pourquoi dit-on que `String` est immuable ? comment compare-t-on deux chaînes par leur contenu ? pourquoi `StringBuilder` dans une boucle ?
- [ ] **Step 6 : Pour aller plus loin** :
  - `https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/lang/String.html` (Javadoc 25 — `String`)
  - `https://www.baeldung.com/java-string-operations` (Baeldung — String operations)
- [ ] **Step 7 : Prochain chapitre** → `[2-4-methodes](2-4-methodes)` « Méthodes ».
- [ ] **Step 8 : Commit**

```bash
git add courses/docs/module-2-tableaux-chaines-methodes/2-3-chaines-de-caracteres.md
git commit -m "docs(m2): chapitre 2.3 chaines de caracteres"
```

---

## Task 5 : Chapitre 2.4 — Méthodes

**Files:**
- Create: `courses/docs/module-2-tableaux-chaines-methodes/2-4-methodes.md`

- [ ] **Step 1 : Frontmatter (verbatim)**

```markdown
---
id: 2-4-methodes
sidebar_position: 4
title: "Méthodes"
description: "Écrire ses propres méthodes : signature, paramètres, type de retour, surcharge, portée des variables et rôle de static."
---
```

- [ ] **Step 2 : Corps** — plan de sections :
  1. **Pourquoi des méthodes** — éviter de répéter du code, nommer une intention, réutiliser. (Charnière avec le chap. 1.7 « blocs courts ».)
  2. **Anatomie d'une méthode** — signature : modificateurs (`static`), **type de retour**, **nom**, **paramètres**. Le corps fait le travail.
  3. **Retourner ou non une valeur** — `void` (ne retourne rien) vs un type de retour + instruction `return` ; une méthode `return` arrête immédiatement.
  4. **Appeler une méthode** — passer des arguments ; récupérer la valeur retournée.
  5. **Passage de paramètres** — Java passe **par valeur** : un primitif est copié (le modifier dans la méthode ne change pas l'original) ; pour un **tableau**, c'est la référence qui est copiée (la méthode peut donc modifier le contenu du tableau) — à signaler clairement.
  6. **La surcharge** — plusieurs méthodes de **même nom** mais **paramètres différents** (`max(int, int)` et `max(int, int, int)`).
  7. **Portée des variables** — une variable déclarée dans une méthode (ou un bloc) n'existe que là.
  8. **Pourquoi `static` ici** — on appelle la méthode sans créer d'objet ; les objets viendront au module 3 (mention, pas de détail).
  - **Antériorité** : ce chapitre **introduit** l'écriture de méthodes ; il peut donc structurer ses exemples en méthodes `static`.
- [ ] **Step 3 : Erreurs fréquentes** : oublier le `return` (ou un chemin sans `return`) ; type de retour incohérent avec ce qu'on renvoie ; croire qu'on a modifié un primitif passé en paramètre ; deux méthodes qui ne diffèrent que par le type de retour (ne compile pas — la surcharge porte sur les paramètres) ; réutiliser une variable hors de sa portée.
- [ ] **Step 4 : Exercice guidé** — écrire `static int max(int a, int b)`, puis la **surcharger** en `static int max(int a, int b, int c)` (en réutilisant la première). **Ne pas** faire la bibliothèque maths complète (exo 2.3.1), ni l'exercice de surcharge noté (2.3.2), ni le refactor de `1.2.2 calculs-geometriques` (2.3.3). Solution dans `<details>`.
- [ ] **Step 5 : Vérifiez vos acquis** — ex. : que contient une signature de méthode ? que signifie « passage par valeur » pour un `int` ? deux méthodes peuvent-elles avoir le même nom ? à quoi sert `return` dans une méthode `void` ?
- [ ] **Step 6 : Pour aller plus loin** :
  - `https://dev.java/learn/language-basics/methods/` (dev.java — methods)
  - `https://www.baeldung.com/java-method-overload-override` (Baeldung — overloading)
- [ ] **Step 7 : Prochain chapitre** → `[2-5-recursivite](2-5-recursivite)` « Récursivité ».
- [ ] **Step 8 : Commit**

```bash
git add courses/docs/module-2-tableaux-chaines-methodes/2-4-methodes.md
git commit -m "docs(m2): chapitre 2.4 methodes"
```

---

## Task 6 : Chapitre 2.5 — Récursivité

**Files:**
- Create: `courses/docs/module-2-tableaux-chaines-methodes/2-5-recursivite.md`

- [ ] **Step 1 : Frontmatter (verbatim)**

```markdown
---
id: 2-5-recursivite
sidebar_position: 5
title: "Récursivité"
description: "Comprendre une méthode qui s'appelle elle-même : cas de base, cas récursif, pile d'exécution, et savoir quand préférer la récursivité à une boucle."
---
```

- [ ] **Step 2 : Corps** — plan de sections :
  1. **Qu'est-ce que la récursivité** — une méthode qui **s'appelle elle-même** (s'appuie sur le chap. 2-4).
  2. **Cas de base et cas récursif** — le **cas de base** arrête la récursion ; le **cas récursif** se rapproche du cas de base. Exemple : `factorielle(n)` = `n * factorielle(n - 1)`, cas de base `factorielle(0) = 1`.
  3. **La pile d'exécution** — chaque appel empile un contexte ; les appels se dépilent quand ils retournent. Schéma en bloc ` ```text ` montrant l'empilement/dépilement de `factorielle(3)`.
  4. **Récursif vs itératif** — la même chose est souvent faisable avec une boucle ; quand la récursivité rend le code plus clair (structures naturellement récursives) et quand l'éviter.
  - **Antériorité** : dépend du chap. 2-4 (méthodes) → ce chapitre est bien le dernier du module.
- [ ] **Step 3 : Erreurs fréquentes** : **cas de base manquant ou jamais atteint** → `StackOverflowError` ; argument qui ne décroît pas vers le cas de base ; utiliser la récursivité là où une simple boucle serait plus lisible.
- [ ] **Step 4 : Exercice guidé** — calculer la **somme de 1 à N** de façon récursive (`somme(n) = n + somme(n - 1)`, cas de base `somme(0) = 0`). **Ne pas** faire factorielle/puissance (exo 2.4.1) ni parcours de matrice récursif (2.4.2). Solution dans `<details>`.
- [ ] **Step 5 : Vérifiez vos acquis** — ex. : qu'est-ce qu'un cas de base et pourquoi est-il indispensable ? que se passe-t-il s'il manque ? la récursivité est-elle toujours préférable à une boucle ?
- [ ] **Step 6 : Pour aller plus loin** :
  - `https://www.baeldung.com/java-recursion` (Baeldung — recursion)
  - `https://dev.java/learn/language-basics/methods/` (dev.java — methods, rappel sur l'appel de méthode)
- [ ] **Step 7 : Prochain chapitre** → mention « → **Module 3 — Programmation orientée objet** *(à venir)* » (pas de lien actif, le module 3 n'existe pas encore).
- [ ] **Step 8 : Commit**

```bash
git add courses/docs/module-2-tableaux-chaines-methodes/2-5-recursivite.md
git commit -m "docs(m2): chapitre 2.5 recursivite"
```

---

## Task 7 : Câbler la liaison module 1 → module 2 (chapitre 1.7)

**Files:**
- Modify: `courses/docs/module-1-fondamentaux/1-7-bonnes-pratiques-lisibilite.md` (dernière section)

- [ ] **Step 1 : Remplacer** le bloc final actuel :

```markdown
## Prochain chapitre

→ **Module 2 — Tableaux, chaînes, méthodes** *(à venir)*
```

par :

```markdown
## Prochain chapitre

→ **[Module 2 — Tableaux 1D](../module-2-tableaux-chaines-methodes/2-1-tableaux-1d)**
```

- [ ] **Step 2 : Commit**

```bash
git add courses/docs/module-1-fondamentaux/1-7-bonnes-pratiques-lisibilite.md
git commit -m "docs(m2): relier la fin du module 1 au module 2"
```

---

## Task 8 : Vérification — build Docusaurus + relecture conformité

**Files:** aucun (vérification)

- [ ] **Step 1 : Build Docusaurus**

Run : `cd courses; npm run build`
Expected : build qui se termine sans erreur. En particulier, **aucun** `Error: Docusaurus found broken links` (le `onBrokenLinks: 'throw'` casserait sur un lien interne mort — vérifie les `[..](2-x-...)`, le lien inter-module du 1.7 et les ancres).

- [ ] **Step 2 : Relecture de conformité** (checklist charte §10, sur les 5 chapitres) :
  - [ ] Structure §6 respectée (les 10 blocs dans l'ordre).
  - [ ] Vouvoiement, phrases courtes, pas de jargon militaire non expliqué, pas d'humour/argot.
  - [ ] Code : anglais pour Java, commentaires français du *pourquoi*, imports explicites, pas de variable d'une lettre hors `i/j/k`/`e`.
  - [ ] **Garde-fou §5.2** : confirmer qu'**aucune méthode `static` maison** n'apparaît dans les exemples ni les guidés des chapitres **2-1, 2-2, 2-3** ; que 2-4 introduit bien l'écriture de méthodes ; que 2-5 ne s'appuie que sur 2-4 et antérieur.
  - [ ] **Anti-spoil §5.3** : confirmer qu'aucun exercice guidé ne résout un exo noté d'un futur sous-groupe (somme≠min/max ; affichage matrice≠rotation ; majuscules≠palindrome/comptage ; `max` surchargé≠biblio maths/refactor ; somme récursive≠factorielle).
  - [ ] Longueur 800–2000 mots par chapitre.
- [ ] **Step 3 :** corriger inline tout écart relevé, puis re-`npm run build` si un fichier a été modifié. Commit des corrections éventuelles :

```bash
git add courses/docs/module-2-tableaux-chaines-methodes/
git commit -m "docs(m2): corrections relecture conformite charte"
```

---

## Task 9 : Pull request + clôture backlog

**Files:**
- Modify: `docs/backlog.md` (avancement #18, volet chapitres)

- [ ] **Step 1 : Mettre à jour `docs/backlog.md`** (section #18) : cocher les 5 chapitres + le `_category_.json` + la liaison module 1→2, et noter le volet chapitres comme livré (les sous-groupes d'exercices restent à faire dans des cycles séparés). Le lien commit/PR sera ajouté après merge.
- [ ] **Step 2 : Commit**

```bash
git add docs/backlog.md
git commit -m "docs(backlog): #18 volet chapitres module 2 livre"
```

- [ ] **Step 3 : Push + PR**

```bash
git push -u origin feature/m2-chapitres
gh pr create --base main --title "#18 — Chapitres module 2 (tableaux, chaines, methodes, recursivite)" --body "<résumé : 5 chapitres charte §6 + _category_.json + liaison module 1→2, build Docusaurus vert. Garde-fou antériorité interne (pas de méthode maison avant le chap. Méthodes) + anti-spoil des futurs exos. Spec: docs/superpowers/specs/2026-06-01-m2-chapitres-design.md>

🤖 Generated with [Claude Code](https://claude.com/claude-code)"
```

- [ ] **Step 4 :** relecture par un autre formateur (charte §10) + CI #11a vert → merge. (Hors session : action humaine.)

---

## Self-review (rempli à la rédaction du plan)

- **Couverture spec** : `_category_.json` §2 → Task 1 ; les 5 chapitres §2 → Tasks 2–6 ; retouche liaison 1.7 §2 → Task 7 ; vérification §7 (build + relecture) → Task 8 ; workflow/PR/backlog §8 → Task 9. Garde-fous §5.2/§5.3 → intégrés dans chaque task de rédaction + checklist Task 8. Conventions §4 → « Cadre commun ». Décisions transverses §10 de la spec = hors périmètre de ce cycle (volet exercices) → volontairement non couvertes ici. ✅ Pas de gap.
- **Placeholders** : les seuls « *(à venir)* » sont volontaires (module 3 inexistant, Task 6) ; les cases `[ ]` sont le format de suivi attendu, pas des placeholders de contenu. ✅
- **Cohérence des liens internes** : chaque « Prochain chapitre » pointe vers l'`id` du fichier suivant (`2-1` → `2-2` → … → `2-4` → `2-5`) ; le 1.7 du module 1 pointe vers `../module-2-tableaux-chaines-methodes/2-1-tableaux-1d` (lien inter-dossier, à valider au build) ; le 2-5 ne pointe vers aucun lien actif. ✅
- **Cohérence des notions** : `length` (tableau, sans `()`) vs `length()` (String) traité explicitement en 2-1 puis rappelé en 2-3 ; `split` (2-3) renvoie un `String[]` (notion de 2-1, donc antérieure) ✅ ; récursivité (2-5) s'appuie sur méthodes (2-4) ✅.
