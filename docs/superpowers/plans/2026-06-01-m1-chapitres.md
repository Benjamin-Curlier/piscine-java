# #15 — Chapitres module 1 : plan d'implémentation

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:executing-plans pour exécuter ce plan tâche par tâche (exécution **inline** — pas de sous-agents, contrainte projet). Les étapes utilisent des cases à cocher (`- [ ]`).

**Goal:** Produire les 6 chapitres manquants du module 1 (1.2 → 1.7) dans `courses/docs/module-1-fondamentaux/`, conformes à la charte §6, et vérifiés par le build Docusaurus.

**Architecture:** Contenu Markdown statique consommé par Docusaurus (sidebar auto-générée). Chaque chapitre est un fichier autonome suivant la structure imposée. La « vérification » d'un chapitre n'est pas un test unitaire mais le **build Docusaurus** (`npm run build`, `onBrokenLinks: 'throw'`) plus une relecture de conformité charte. Les extraits de code ne sont pas compilés (relecture manuelle).

**Tech Stack:** Markdown / MDX, Docusaurus (Node 24, npm 11 disponibles localement ; `courses/node_modules` présent).

**Spec:** [`docs/superpowers/specs/2026-06-01-m1-chapitres-design.md`](../specs/2026-06-01-m1-chapitres-design.md)

**Branche:** `feature/m1-chapitres` (déjà créée, spec commitée `5e3ee78`).

---

## Cadre commun à toutes les tâches de rédaction

Avant d'écrire un chapitre, le rédacteur garde sous les yeux :
- **Charte §6** (structure), **§2/§3** (ton/langue, vouvoiement, règle des trois fois), **§7** (code : anglais pour Java, commentaires français du *pourquoi*, lisibilité avant astuce, imports explicites), **§9** (markdown).
- **Modèle vivant** : [`courses/docs/module-1-fondamentaux/1-1-installer-java.md`](../../../courses/docs/module-1-fondamentaux/1-1-installer-java.md). Copier ses conventions exactes (encadrés `> **À retenir**`, tableaux, `<details>` pour la solution du guidé, ton).
- **Garde-fous spec §5** : règle d'antériorité (aucune notion avant son chapitre) + exercice guidé qui **ne spoile aucun exo #16**.
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

## Task 1 : Chapitre 1.2 — Variables et types primitifs

**Files:**
- Create: `courses/docs/module-1-fondamentaux/1-2-variables-types-primitifs.md`

- [ ] **Step 1 : Frontmatter (verbatim)**

```markdown
---
id: 1-2-variables-types-primitifs
sidebar_position: 2
title: "Variables et types primitifs"
description: "Déclarer des variables, comprendre les types primitifs (int, long, double, boolean, char), distinguer String, gérer les conversions et découvrir var."
---
```

- [ ] **Step 2 : Rédiger le corps** selon le squelette commun. Plan de sections :
  1. **Qu'est-ce qu'une variable** — déclaration `type nom = valeur;`, affectation, réaffectation.
  2. **Les types entiers** — `int`, `long` (suffixe `L`), plage de valeurs, dépassement de capacité.
  3. **Les types à virgule** — `double` (et mention `float` + suffixe `f`), précision.
  4. **`boolean` et `char`** — `true`/`false` ; `char` entre `'simples quotes'`.
  5. **`String` : pas un primitif** — c'est un objet ; distinction signalée (règle des trois fois : 1ʳᵉ occurrence définie ici) ; concaténation avec `+`.
  6. **Conversions** — élargissement implicite (`int` → `double`), *cast* explicite (`(int)`), perte de précision ; **division entière** (`5/2 == 2`).
  7. **`var`** — inférence de type local, quand l'utiliser, quand l'éviter (lisibilité).
  - **Antériorité** : aucun `if`, aucune boucle, aucun `Scanner` dans les exemples.
- [ ] **Step 3 : Erreurs fréquentes** (au moins) : division entière inattendue (`int` / `int`), dépassement `int`, oubli du suffixe `L`/`f`, `==` sur `String` (signalé comme piège, traité au chap. 1.5), perte de précision au *cast*.
- [ ] **Step 4 : Exercice guidé** — déclarer puis afficher les variables d'un profil simple : un `int age`, un `double taille`, un `char initiale`, un `boolean actif` ; afficher chaque valeur. **Ne pas** faire de conversion d'unité (réservé à l'exo #16 1.2.1). Solution repliée dans `<details>`.
- [ ] **Step 5 : Vérifiez vos acquis** — 3–4 questions ouvertes (ex. : pourquoi `5/2` vaut `2` ? quand préférer `double` à `int` ? `String` est-il un type primitif ?).
- [ ] **Step 6 : Pour aller plus loin** — liens annotés :
  - `https://dev.java/learn/language-basics/primitive-types/` (dev.java — types primitifs)
  - `https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/lang/String.html` (Javadoc 25 — `String`)
- [ ] **Step 7 : Prochain chapitre** → `## Prochain chapitre` : « → **Chapitre 1.3 — Opérateurs** » avec lien `[1-3-operateurs](1-3-operateurs)`.
- [ ] **Step 8 : Commit**

```bash
git add courses/docs/module-1-fondamentaux/1-2-variables-types-primitifs.md
git commit -m "docs(m1): chapitre 1.2 variables et types primitifs"
```

---

## Task 2 : Chapitre 1.3 — Opérateurs

**Files:**
- Create: `courses/docs/module-1-fondamentaux/1-3-operateurs.md`

- [ ] **Step 1 : Frontmatter (verbatim)**

```markdown
---
id: 1-3-operateurs
sidebar_position: 3
title: "Opérateurs"
description: "Opérateurs arithmétiques, de comparaison, logiques, affectation composée et règles de priorité en Java."
---
```

- [ ] **Step 2 : Corps** — plan de sections :
  1. **Arithmétiques** — `+ - * / %` ; rappel division entière vs flottante (lien chap. 1.2) ; `%` (modulo) sur entiers.
  2. **Comparaison** — `== != < > <= >=` ; résultat de type `boolean`.
  3. **Logiques** — `&&`, `||`, `!` ; évaluation en court-circuit (expliquée).
  4. **Affectation composée** — `+= -= *= /= %=` ; `i++` / `i--` (incrément).
  5. **Priorité et parenthésage** — table de priorité simplifiée ; conseil : parenthéser pour la lisibilité.
  - **Antériorité** : pas de `if`/boucle ; on reste sur des expressions et des `System.out.println(...)`.
- [ ] **Step 3 : Erreurs fréquentes** : confusion `=` (affectation) / `==` (comparaison) ; priorité mal anticipée (`2 + 3 * 4`) ; `%` mal compris ; division entière qui « avale » la décimale.
- [ ] **Step 4 : Exercice guidé** — calculer la **moyenne pondérée de deux notes** (ex. note écrit coef 2, note oral coef 1) avec opérateurs et affectation composée, afficher le résultat en `double`. **Pas** de géométrie (exo #16 1.2.2) ni de table de vérité exhaustive (exo #16 1.2.3). Solution dans `<details>`.
- [ ] **Step 5 : Vérifiez vos acquis** — ex. : différence `=` / `==` ? que vaut `7 % 3` ? pourquoi parenthéser ?
- [ ] **Step 6 : Pour aller plus loin** :
  - `https://dev.java/learn/language-basics/operators/` (dev.java — opérateurs)
  - `https://www.baeldung.com/java-operators` (Baeldung — operators)
- [ ] **Step 7 : Prochain chapitre** → `[1-4-entrees-clavier](1-4-entrees-clavier)` « Entrées clavier ».
- [ ] **Step 8 : Commit**

```bash
git add courses/docs/module-1-fondamentaux/1-3-operateurs.md
git commit -m "docs(m1): chapitre 1.3 operateurs"
```

---

## Task 3 : Chapitre 1.4 — Entrées clavier

**Files:**
- Create: `courses/docs/module-1-fondamentaux/1-4-entrees-clavier.md`

- [ ] **Step 1 : Frontmatter (verbatim)**

```markdown
---
id: 1-4-entrees-clavier
sidebar_position: 4
title: "Entrées clavier"
description: "Lire des données saisies au clavier avec Scanner, gérer le piège nextLine, et faire un parsing simple."
---
```

- [ ] **Step 2 : Corps** — plan de sections :
  1. **Lire au clavier avec `Scanner`** — `import java.util.Scanner;`, `new Scanner(System.in)`, `System.in` = entrée standard.
  2. **Lire selon le type** — `nextInt()`, `nextDouble()`, `nextLine()`, `next()`.
  3. **Le piège du `nextLine` après `nextInt`** — explication du retour-chariot résiduel + solution (`nextLine()` de purge).
  4. **Lecture sécurisée / parsing simple** — `Integer.parseInt(String)`, idée de vérifier ce qu'on lit (sans `try/catch` — exceptions = module 5, juste signalé).
  - **Antériorité** : `if` pas encore vu (1.5), boucle pas encore vue (1.6) → les exemples lisent une ou deux valeurs, sans boucle de validation.
- [ ] **Step 3 : Erreurs fréquentes** : `InputMismatchException` quand l'utilisateur tape du texte au lieu d'un nombre (expliqué, renvoi module 5) ; le `nextLine` « sauté » ; oublier `import java.util.Scanner`.
- [ ] **Step 4 : Exercice guidé** — lire un entier au clavier et afficher son double. **Pas** de boucle de jeu (exo #16 1.3.4 devine-le-nombre), pas d'écho stylisé (exo #16 1.1.3). Solution dans `<details>`.
- [ ] **Step 5 : Vérifiez vos acquis** — ex. : à quoi sert `System.in` ? pourquoi un `nextLine()` vide après un `nextInt()` ?
- [ ] **Step 6 : Pour aller plus loin** :
  - `https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/Scanner.html` (Javadoc 25 — `Scanner`)
  - `https://www.baeldung.com/java-scanner` (Baeldung — Scanner)
- [ ] **Step 7 : Prochain chapitre** → `[1-5-conditions](1-5-conditions)` « Conditions ».
- [ ] **Step 8 : Commit**

```bash
git add courses/docs/module-1-fondamentaux/1-4-entrees-clavier.md
git commit -m "docs(m1): chapitre 1.4 entrees clavier"
```

---

## Task 4 : Chapitre 1.5 — Conditions

**Files:**
- Create: `courses/docs/module-1-fondamentaux/1-5-conditions.md`

- [ ] **Step 1 : Frontmatter (verbatim)**

```markdown
---
id: 1-5-conditions
sidebar_position: 5
title: "Conditions"
description: "Prendre des décisions avec if / else if / else, le switch expression (Java 21+) et l'opérateur ternaire."
---
```

- [ ] **Step 2 : Corps** — plan de sections :
  1. **`if` / `else`** — condition `boolean` (lien chap. 1.3), bloc, accolades **toujours** présentes.
  2. **`else if` en cascade** — choisir parmi plusieurs cas.
  3. **Le `switch` expression (Java 21+)** — syntaxe `->`, plusieurs étiquettes, `yield`, exhaustivité ; contraste avec `if/else if`.
  4. **L'opérateur ternaire** — `condition ? a : b` ; usage mesuré (lisibilité).
  - **Comparer des `String`** : signaler `equals(...)` et le piège `==` (rappel chap. 1.2, règle des trois fois).
  - **Antériorité** : pas de boucle (1.6).
- [ ] **Step 3 : Erreurs fréquentes** : `=` au lieu de `==` dans la condition ; accolades oubliées (instruction « pendante ») ; comparer des `String` avec `==` ; `switch` non exhaustif.
- [ ] **Step 4 : Exercice guidé** — attribuer une **mention** à une note (`>= 16` Très bien, `>= 14` Bien, etc.) d'abord en `if/else if`, puis montrer la même logique en `switch` sur une tranche. **Pas** FizzBuzz (exo #16 1.3.1), pas devine-le-nombre (1.3.4). Solution dans `<details>`.
- [ ] **Step 5 : Vérifiez vos acquis** — ex. : pourquoi toujours mettre des accolades ? quand un `switch` est-il plus lisible qu'une cascade de `if` ? comment compare-t-on deux `String` ?
- [ ] **Step 6 : Pour aller plus loin** :
  - `https://dev.java/learn/language-basics/branching-statements/` (dev.java — branching)
  - `https://docs.oracle.com/en/java/javase/25/language/switch-expressions.html` (Oracle — switch expressions)
- [ ] **Step 7 : Prochain chapitre** → `[1-6-boucles](1-6-boucles)` « Boucles ».
- [ ] **Step 8 : Commit**

```bash
git add courses/docs/module-1-fondamentaux/1-5-conditions.md
git commit -m "docs(m1): chapitre 1.5 conditions"
```

---

## Task 5 : Chapitre 1.6 — Boucles

**Files:**
- Create: `courses/docs/module-1-fondamentaux/1-6-boucles.md`

- [ ] **Step 1 : Frontmatter (verbatim)**

```markdown
---
id: 1-6-boucles
sidebar_position: 6
title: "Boucles"
description: "Répéter des instructions avec while, do-while et for, maîtriser break et continue, et choisir la bonne boucle."
---
```

- [ ] **Step 2 : Corps** — plan de sections :
  1. **`while`** — répéter tant qu'une condition est vraie (lien chap. 1.5).
  2. **`do-while`** — au moins une exécution ; quand l'utiliser.
  3. **La boucle `for`** — initialisation / condition / incrément ; cas du compteur connu.
  4. **`break` et `continue`** — sortir tôt, sauter une itération.
  5. **Choisir la bonne boucle** — tableau récapitulatif (nb d'itérations connu → `for`, sinon `while`, au moins une fois → `do-while`).
- [ ] **Step 3 : Erreurs fréquentes** : boucle infinie (condition jamais fausse, oubli d'incrément) ; erreur d'indice `off-by-one` (`<` vs `<=`) ; modifier le compteur dans le corps par mégarde.
- [ ] **Step 4 : Exercice guidé** — calculer la **somme cumulée de 1 à N** avec une boucle `for` (et montrer la variante `while`). **Pas** FizzBuzz (1.3.1), Fibonacci (1.3.2), table de multiplication (1.3.3) ni devine-le-nombre (1.3.4). Solution dans `<details>`.
- [ ] **Step 5 : Vérifiez vos acquis** — ex. : différence `while` / `do-while` ? quand choisir `for` ? qu'est-ce qu'une boucle infinie et comment l'éviter ?
- [ ] **Step 6 : Pour aller plus loin** :
  - `https://dev.java/learn/language-basics/controlling-flow/` (dev.java — control flow)
  - `https://www.baeldung.com/java-loops` (Baeldung — loops)
- [ ] **Step 7 : Prochain chapitre** → `[1-7-bonnes-pratiques-lisibilite](1-7-bonnes-pratiques-lisibilite)` « Bonnes pratiques de lisibilité ».
- [ ] **Step 8 : Commit**

```bash
git add courses/docs/module-1-fondamentaux/1-6-boucles.md
git commit -m "docs(m1): chapitre 1.6 boucles"
```

---

## Task 6 : Chapitre 1.7 — Bonnes pratiques de lisibilité

**Files:**
- Create: `courses/docs/module-1-fondamentaux/1-7-bonnes-pratiques-lisibilite.md`

- [ ] **Step 1 : Frontmatter (verbatim)**

```markdown
---
id: 1-7-bonnes-pratiques-lisibilite
sidebar_position: 7
title: "Bonnes pratiques de lisibilité"
description: "Indentation, nommage parlant, commentaires utiles et constantes nommées : écrire du code que les autres (et vous) reliront sans peine."
---
```

- [ ] **Step 2 : Corps** — plan de sections :
  1. **Indentation et structure** — alignement des blocs, une instruction par ligne.
  2. **Nommer pour être compris** — rappel charte §7 (identifiants Java en anglais, métier en français quand plus clair) ; bannir les noms d'une lettre hors `i/j/k`.
  3. **Commentaires utiles** — expliquer le *pourquoi*, pas le *quoi* ; exemple MAUVAIS vs BON (reprendre l'esprit de la charte §7).
  4. **Constantes nommées** — `final` + nom parlant plutôt que « nombre magique ».
  5. **Garder les blocs courts** — charnière vers le module 2 (méthodes).
  - **Section centrale** : un même extrait **avant / après** refactor lisibilité.
- [ ] **Step 3 : Erreurs fréquentes** : indentation incohérente ; noms vagues (`x`, `tmp`, `data`) ; commentaires qui paraphrasent le code ; nombres magiques disséminés.
- [ ] **Step 4 : Exercice guidé** — on fournit un extrait **volontairement illisible** (noms vagues, pas d'indentation, nombre magique) ; le stagiaire le réécrit proprement. Aucun exo #16 n'est adossé à ce chapitre → pas de contrainte de spoil. Solution propre dans `<details>`.
- [ ] **Step 5 : Vérifiez vos acquis** — ex. : qu'est-ce qu'un « nombre magique » et pourquoi l'éviter ? un bon commentaire répond à quelle question ?
- [ ] **Step 6 : Pour aller plus loin** :
  - `https://www.oracle.com/java/technologies/javase/codeconventions-contents.html` (Oracle — Code Conventions)
  - `https://www.baeldung.com/java-clean-code` (Baeldung — clean code)
- [ ] **Step 7 : Prochain chapitre** → mention « → **Module 2 — Tableaux, chaînes, méthodes** *(à venir)* » (pas de lien actif, le module 2 n'existe pas encore).
- [ ] **Step 8 : Commit**

```bash
git add courses/docs/module-1-fondamentaux/1-7-bonnes-pratiques-lisibilite.md
git commit -m "docs(m1): chapitre 1.7 bonnes pratiques de lisibilite"
```

---

## Task 7 : Câbler le lien « Prochain chapitre » du chapitre 1.1

**Files:**
- Modify: `courses/docs/module-1-fondamentaux/1-1-installer-java.md` (dernière section)

- [ ] **Step 1 : Remplacer** le bloc final actuel :

```markdown
## Prochain chapitre

→ **Chapitre 1.2 — Variables et types primitifs** *(à venir)*
```

par :

```markdown
## Prochain chapitre

→ **[Chapitre 1.2 — Variables et types primitifs](1-2-variables-types-primitifs)**
```

- [ ] **Step 2 : Commit**

```bash
git add courses/docs/module-1-fondamentaux/1-1-installer-java.md
git commit -m "docs(m1): relier chapitre 1.1 au chapitre 1.2"
```

---

## Task 8 : Vérification — build Docusaurus + relecture conformité

**Files:** aucun (vérification)

- [ ] **Step 1 : Build Docusaurus**

Run : `cd courses; npm run build`
Expected : build qui se termine sans erreur. En particulier, **aucun** `Error: Docusaurus found broken links` (le `onBrokenLinks: 'throw'` casserait sur un lien interne mort — vérifie les `[..](1-x-...)` et les ancres).

- [ ] **Step 2 : Relecture de conformité** (checklist charte §10, sur les 6 chapitres) :
  - [ ] Structure §6 respectée (les 10 blocs dans l'ordre).
  - [ ] Vouvoiement, phrases courtes, pas de jargon militaire non expliqué, pas d'humour/argot.
  - [ ] Code : anglais pour Java, commentaires français du *pourquoi*, imports explicites, pas de variable d'une lettre hors `i/j/k`/`e`.
  - [ ] **Antériorité** : relire chaque chapitre et confirmer qu'aucune notion d'un chapitre ultérieur n'y est utilisée.
  - [ ] **Anti-spoil** : confirmer qu'aucun exercice guidé ne résout un exo #16 (cf. tableau spec §5.2).
  - [ ] Longueur 800–2000 mots par chapitre.
- [ ] **Step 3 :** corriger inline tout écart relevé, puis re-`npm run build` si un fichier a été modifié. Commit des corrections éventuelles :

```bash
git add courses/docs/module-1-fondamentaux/
git commit -m "docs(m1): corrections relecture conformite charte"
```

---

## Task 9 : Pull request + clôture backlog

**Files:**
- Modify: `docs/backlog.md` (cocher #15)

- [ ] **Step 1 : Cocher les critères #15** dans `docs/backlog.md` (section « #15 — Chapitres restants du module 1 ») : les 6 chapitres `[x]`, les 4 critères d'acceptation `[x]`, statut → `Faite` (le lien commit sera ajouté après merge).
- [ ] **Step 2 : Commit**

```bash
git add docs/backlog.md
git commit -m "docs(backlog): #15 chapitres module 1 faite"
```

- [ ] **Step 3 : Push + PR**

```bash
git push -u origin feature/m1-chapitres
gh pr create --base main --title "#15 — Chapitres module 1 (1.2 → 1.7)" --body "<résumé : 6 chapitres charte §6, build Docusaurus vert, garde-fous antériorité + anti-spoil #16. Spec: docs/superpowers/specs/2026-06-01-m1-chapitres-design.md>"
```

- [ ] **Step 4 :** relecture par un autre formateur (charte §10) + CI #11a vert → merge. (Hors session : action humaine.)

---

## Self-review (rempli à la rédaction du plan)

- **Couverture spec** : les 6 livrables §2 → Tasks 1–6 ; retouche lien 1.1 §2 → Task 7 ; vérification §7 (build + relecture) → Task 8 ; workflow/PR/backlog §8 → Task 9. Garde-fous §5 (antériorité, anti-spoil) → intégrés dans chaque task de rédaction + checklist Task 8. Conventions §4 → « Cadre commun ». ✅ Pas de gap.
- **Placeholders** : les seuls « *(à venir)* » sont volontaires (module 2 inexistant, Task 6) ; les cases `[ ]` sont le format de suivi attendu, pas des placeholders de contenu. ✅
- **Cohérence des liens internes** : chaque « Prochain chapitre » pointe vers l'`id` du fichier suivant (`1-2-…` → `1-3-…` → … → `1-7-…`) ; 1.1 mis à jour en Task 7 ; 1.7 ne pointe vers aucun lien actif. ✅
```

