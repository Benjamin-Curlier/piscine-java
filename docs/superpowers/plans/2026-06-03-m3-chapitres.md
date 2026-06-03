# #19 — Chapitres module 3 (POO) : plan d'implémentation

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:executing-plans pour exécuter ce plan tâche par tâche (exécution **inline** — pas de sous-agents, contrainte projet). Les étapes utilisent des cases à cocher (`- [ ]`).

**Goal:** Produire les 10 chapitres du module 3 (3-1 → 3-10) dans `courses/docs/module-3-poo/`, conformes à la charte §6, vérifiés par le build Docusaurus, livrés en **2 PR** (PR-A : 3-1 à 3-5 ; PR-B : 3-6 à 3-10).

**Architecture:** Contenu Markdown statique consommé par Docusaurus (sidebar auto-générée via `_category_.json`). Chaque chapitre est un fichier autonome suivant la structure imposée. La « vérification » d'un chapitre n'est pas un test unitaire mais le **build Docusaurus** (`npm run build`, `onBrokenLinks: 'throw'`) plus une relecture de conformité charte. Les extraits de code ne sont pas compilés automatiquement (relecture manuelle ; Java 25 dispo localement pour vérifier ponctuellement un extrait à syntaxe récente — record, `sealed`, pattern matching).

**Tech Stack:** Markdown / MDX, Docusaurus (Node/npm disponibles localement ; `courses/node_modules` présent).

**Spec:** [`docs/superpowers/specs/2026-06-03-m3-chapitres-design.md`](../specs/2026-06-03-m3-chapitres-design.md)

**Branches:** PR-A `feature/m3-chapitres-1` (issue de `main`) ; PR-B `feature/m3-chapitres-2` (issue de `main` **après merge de PR-A**).

---

## Cadre commun à toutes les tâches de rédaction

Avant d'écrire un chapitre, le rédacteur garde sous les yeux :
- **Charte §6** (structure), **§2/§3** (ton/langue, vouvoiement, règle des trois fois), **§7** (code : anglais pour les identifiants Java, métier en français quand plus clair, commentaires français du *pourquoi*, lisibilité avant astuce, imports explicites), **§9** (markdown).
- **Modèle vivant** : les chapitres des modules 1 et 2, en particulier [`module-2-tableaux-chaines-methodes/2-4-methodes.md`](../../../courses/docs/module-2-tableaux-chaines-methodes/2-4-methodes.md). Copier ses conventions exactes (frontmatter ; encadrés `> **À retenir**` ; tableaux ; `<details><summary>Solution…</summary>` pour la solution du guidé ; liens « Pour aller plus loin » annotés).
- **Garde-fous spec §5** :
  - **§5.2 antériorité interne** : l'ordre des notions est tenu — héritage (3-5) avant polymorphisme (3-6) avant classes abstraites (3-7) ; interfaces (3-8) ; enums (3-9) ; records/`sealed` (3-10) **en dernier**. Un chapitre n'utilise que les notions des chapitres **précédents**.
  - **§5.1 antériorité externe** : acquis M1-M2 disponible (variables, boucles, conditions, tableaux, chaînes, méthodes `static`, récursivité). **Interdit** : collections génériques (`List`/`Map`/`ArrayList`), lambdas/streams (M4), exceptions/IO (M5), et **écriture manuelle de `equals`/`hashCode`** (M4 — cf. spec §10.3 : on se sert de l'`equals` *généré* des records, on ne le réécrit pas).
  - **§5.4 anti-spoil** : l'exercice guidé ne résout jamais un exo noté d'un futur sous-groupe (mapping rappelé dans chaque task).
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

# PARTIE A — PR-A : chapitres 3-1 à 3-5 (bases & héritage)

> Branche `feature/m3-chapitres-1` issue de `main`.

## Task 0 : Créer la branche PR-A

- [ ] **Step 1 : Brancher depuis `main` à jour**

```bash
git checkout main
git pull
git checkout -b feature/m3-chapitres-1
```

---

## Task 1 : Config Docusaurus du module 3 (`_category_.json`)

**Files:**
- Modify: `courses/docs/module-3-poo/_category_.json` (existe déjà)

- [ ] **Step 1 : Vérifier/aligner le fichier** — il existe déjà (`position: 4`). Aligner `collapsed` sur le module 2 (`false`) pour homogénéiser la sidebar. Contenu cible (verbatim) :

```json
{
  "label": "Module 3 — Programmation Orientée Objet",
  "position": 4,
  "collapsible": true,
  "collapsed": false,
  "link": {
    "type": "generated-index",
    "title": "Module 3 — Programmation Orientée Objet",
    "description": "Classes, encapsulation, héritage, polymorphisme, interfaces. Découverte des types modernes : enums, records, classes scellées."
  }
}
```

- [ ] **Step 2 : Commit**

```bash
git add courses/docs/module-3-poo/_category_.json
git commit -m "docs(#19): aligner la config Docusaurus du module 3 (collapsed)"
```

---

## Task 2 : Chapitre 3-1 — Classes et objets

**Files:**
- Create: `courses/docs/module-3-poo/3-1-classes-et-objets.md`

- [ ] **Step 1 : Frontmatter (verbatim)**

```markdown
---
id: 3-1-classes-et-objets
sidebar_position: 1
title: "Classes et objets"
description: "Définir une classe, créer des instances avec new, comprendre attributs, méthodes, référence d'objet et null."
---
```

- [ ] **Step 2 : Corps** — plan de sections :
  1. **Classe vs objet** — une **classe** est un plan (un type qu'on définit) ; un **objet** (ou instance) est créé à partir du plan. Analogie : le plan d'une maison vs une maison construite.
  2. **Attributs et méthodes** — l'**état** (attributs/champs) et le **comportement** (méthodes). Exemple : une classe `Livre` avec `titre`, `pages` et une méthode `decrire()`.
  3. **Créer une instance avec `new`** — `Livre l = new Livre();` ; chaque `new` crée un objet distinct ; accès aux membres avec le point `l.titre`, `l.decrire()`.
  4. **La référence d'objet** — une variable objet est une **référence** (une poignée vers l'objet, pas l'objet lui-même) ; `Livre b = a;` fait **deux variables vers le même objet** (alias), pas une copie.
  5. **`null` et l'état par défaut** — une référence non initialisée vaut `null` ; déréférencer `null` lève une `NullPointerException` ; un objet fraîchement créé a ses attributs aux valeurs par défaut (`0`/`false`/`null`).
  - **Antériorité** : pas encore d'encapsulation — attributs `public` tolérés ici, on « fermera la boîte » au 3-2 (l'annoncer, règle des trois fois 1ʳᵉ occurrence d'`private`).
- [ ] **Step 3 : Erreurs fréquentes** : confondre classe et instance ; `NullPointerException` sur une référence restée `null` ; croire que `b = a` copie l'objet (c'est un alias) ; oublier `new` (la référence reste `null`).
- [ ] **Step 4 : Exercice guidé** — écrire une classe `Livre` (attributs `titre`, `pages`) et une méthode `decrire()` qui affiche un résumé ; en créer deux instances dans `main`. **Anti-spoil §5.4** : domaine neutre `Livre` — **ne pas** faire `Point2D` (exo 3.1.1), `Compte` (3.1.2) ni `Soldat` (3.1.3). Solution dans `<details>`.
- [ ] **Step 5 : Vérifiez vos acquis** — quelle différence entre une classe et un objet ? que vaut une référence non initialisée ? que fait `b = a` quand `a` est un objet ?
- [ ] **Step 6 : Pour aller plus loin** :
  - `https://dev.java/learn/classes-objects/` (dev.java — classes and objects)
  - `https://www.baeldung.com/java-classes-objects` (Baeldung — classes et objets)
- [ ] **Step 7 : Prochain chapitre** → `[3-2-encapsulation](3-2-encapsulation)` « Encapsulation ».
- [ ] **Step 8 : Commit**

```bash
git add courses/docs/module-3-poo/3-1-classes-et-objets.md
git commit -m "docs(#19): chapitre 3.1 classes et objets"
```

---

## Task 3 : Chapitre 3-2 — Encapsulation

**Files:**
- Create: `courses/docs/module-3-poo/3-2-encapsulation.md`

- [ ] **Step 1 : Frontmatter (verbatim)**

```markdown
---
id: 3-2-encapsulation
sidebar_position: 2
title: "Encapsulation"
description: "Protéger l'état d'un objet avec private, exposer getters/setters et garantir des invariants."
---
```

- [ ] **Step 2 : Corps** — plan de sections :
  1. **Pourquoi cacher l'état** — un objet maître de son état ; on « ferme la boîte » ouverte au 3-1. `private` empêche l'accès direct de l'extérieur.
  2. **`private` vs `public`** — un attribut `private` n'est visible que dans sa classe ; les méthodes `public` forment l'interface d'usage.
  3. **Getters et setters** — lire (`getSolde()`) et modifier (`setSolde(...)`) de façon contrôlée ; convention de nommage `getX`/`setX`/`isX` (booléen).
  4. **Invariant** — une propriété **toujours vraie** d'un objet valide (ex. solde ≥ 0, température ≥ zéro absolu) ; le setter (ou une méthode métier) la fait respecter en rejetant les valeurs invalides.
  - **Antériorité** : pas d'exceptions (M5) — pour rejeter une valeur invalide, on **corrige/ignore** (clamp, message, valeur inchangée) plutôt que de lancer une exception. Conditions du M1 suffisent.
- [ ] **Step 3 : Erreurs fréquentes** : tout laisser `public` (encapsulation cassée) ; setter sans aucune validation (invariant non protégé) ; getter qui retourne une référence interne modifiable ; confondre `private` (la classe) et la portée locale d'une variable (vu en 2-4).
- [ ] **Step 4 : Exercice guidé** — classe `Temperature` avec un attribut `private double celsius` borné à ≥ −273.15 ; setter qui refuse une valeur sous le zéro absolu (la laisse inchangée + message), getter de lecture. **Anti-spoil §5.4** : `Temperature` — **ne pas** faire `Rectangle`/validation/`Date` (exos 3.2.x). Solution dans `<details>`.
- [ ] **Step 5 : Vérifiez vos acquis** — pourquoi rendre un attribut `private` ? qu'est-ce qu'un invariant ? où le fait-on respecter ?
- [ ] **Step 6 : Pour aller plus loin** :
  - `https://www.baeldung.com/java-encapsulation` (Baeldung — encapsulation)
  - `https://dev.java/learn/classes-objects/` (dev.java — accès aux membres)
- [ ] **Step 7 : Prochain chapitre** → `[3-3-constructeurs](3-3-constructeurs)` « Constructeurs ».
- [ ] **Step 8 : Commit**

```bash
git add courses/docs/module-3-poo/3-2-encapsulation.md
git commit -m "docs(#19): chapitre 3.2 encapsulation"
```

---

## Task 4 : Chapitre 3-3 — Constructeurs

**Files:**
- Create: `courses/docs/module-3-poo/3-3-constructeurs.md`

- [ ] **Step 1 : Frontmatter (verbatim)**

```markdown
---
id: 3-3-constructeurs
sidebar_position: 3
title: "Constructeurs"
description: "Initialiser un objet valide dès sa création : constructeur par défaut, paramétré, surcharge et chaînage this(...)."
---
```

- [ ] **Step 2 : Corps** — plan de sections :
  1. **Rôle d'un constructeur** — garantir un objet **valide dès la création** ; appelé par `new`.
  2. **Constructeur par défaut** — fourni implicitement **tant qu'on n'en écrit aucun** ; il disparaît dès qu'on déclare un constructeur paramétré.
  3. **Constructeur paramétré** — `public Livre(String titre, int pages) { this.titre = titre; this.pages = pages; }` ; lien avec l'encapsulation (3-2 : on peut valider ici).
  4. **Surcharge de constructeurs** — plusieurs constructeurs de signatures différentes (ex. `Couleur(int r, int g, int b)` et `Couleur(int gris)`).
  5. **Chaînage `this(...)`** — un constructeur en appelle un autre pour **éviter la duplication** ; `this(...)` doit être la **première** instruction.
  - **Antériorité** : `this.champ` est utilisé ici pour lever l'ambiguïté paramètre/attribut ; sa définition complète est au 3-4 (l'annoncer brièvement). Surcharge déjà connue des méthodes (chap. 2-4) → on la transpose aux constructeurs.
- [ ] **Step 3 : Erreurs fréquentes** : croire que le constructeur par défaut subsiste après en avoir écrit un (erreur de compilation `new Livre()`) ; dupliquer la logique entre constructeurs au lieu de `this(...)` ; mettre `this(...)` ailleurs qu'en première instruction ; confondre `this(...)` (autre constructeur) et `this.champ` (attribut).
- [ ] **Step 4 : Exercice guidé** — classe `Couleur` avec `Couleur(int r, int g, int b)` et un constructeur surchargé `Couleur(int gris)` qui **chaîne** via `this(gris, gris, gris)`. **Anti-spoil §5.4** : `Couleur` — éviter le `Rectangle`/`Date`/validation des exos 3.2.x. Solution dans `<details>`.
- [ ] **Step 5 : Vérifiez vos acquis** — quand le constructeur par défaut disparaît-il ? à quoi sert `this(...)` ? deux constructeurs peuvent-ils avoir la même signature ?
- [ ] **Step 6 : Pour aller plus loin** :
  - `https://www.baeldung.com/java-constructors` (Baeldung — constructors)
  - `https://dev.java/learn/classes-objects/` (dev.java — constructors)
- [ ] **Step 7 : Prochain chapitre** → `[3-4-this-et-static](3-4-this-et-static)` « this et static ».
- [ ] **Step 8 : Commit**

```bash
git add courses/docs/module-3-poo/3-3-constructeurs.md
git commit -m "docs(#19): chapitre 3.3 constructeurs"
```

---

## Task 5 : Chapitre 3-4 — `this` et `static`

**Files:**
- Create: `courses/docs/module-3-poo/3-4-this-et-static.md`

- [ ] **Step 1 : Frontmatter (verbatim)**

```markdown
---
id: 3-4-this-et-static
sidebar_position: 4
title: "this et static"
description: "Distinguer membre d'instance et membre de classe : this, variables et méthodes static, constantes static final."
---
```

- [ ] **Step 2 : Corps** — plan de sections :
  1. **`this`, la référence à l'objet courant** — lever l'ambiguïté paramètre/attribut (`this.titre = titre`), passer l'objet courant en argument. Reprend l'usage entrevu au 3-3.
  2. **Instance vs classe** — un membre **d'instance** existe une fois **par objet** ; un membre **`static`** appartient à la **classe** (partagé par tous).
  3. **Variable et méthode `static`** — `static int compteur;` partagé ; méthode `static` appelée sur la classe (`Math.max`) sans objet ; **une méthode `static` n'a pas de `this`**.
  4. **Constantes `static final`** — `public static final double TVA = 0.20;` ; convention `MAJUSCULES_AVEC_UNDERSCORES`.
  5. **Quand `static` est justifié** — utilitaire sans état, compteur partagé, constante ; **pont avec le module 2** : « voilà pourquoi `main` et nos méthodes utilitaires étaient `static` — pas d'objet à ce moment-là ».
  - **Antériorité** : s'appuie sur les méthodes `static` du chap. 2-4 (connues) et sur `this` du 3-3.
- [ ] **Step 3 : Erreurs fréquentes** : appeler une méthode/variable d'instance depuis un contexte `static` (« non-static … cannot be referenced from a static context ») ; abuser de `static` (état global mutable) ; oublier `final` sur une constante ; croire que `static` veut dire « constant ».
- [ ] **Step 4 : Exercice guidé** — classe `Ticket` avec un compteur `static int dernierId` incrémenté à chaque construction pour donner un `id` unique à chaque instance ; afficher les ids de trois tickets. **Anti-spoil §5.4** : domaine neutre `Ticket` — éviter les exos 3.1.x/3.2.x. Solution dans `<details>`.
- [ ] **Step 5 : Vérifiez vos acquis** — quelle différence entre un membre d'instance et un membre `static` ? pourquoi une méthode `static` n'a-t-elle pas de `this` ? à quoi sert `static final` ?
- [ ] **Step 6 : Pour aller plus loin** :
  - `https://www.baeldung.com/java-static` (Baeldung — static)
  - `https://dev.java/learn/classes-objects/` (dev.java — class members)
- [ ] **Step 7 : Prochain chapitre** → `[3-5-heritage](3-5-heritage)` « Héritage ».
- [ ] **Step 8 : Commit**

```bash
git add courses/docs/module-3-poo/3-4-this-et-static.md
git commit -m "docs(#19): chapitre 3.4 this et static"
```

---

## Task 6 : Chapitre 3-5 — Héritage

**Files:**
- Create: `courses/docs/module-3-poo/3-5-heritage.md`

- [ ] **Step 1 : Frontmatter (verbatim)**

```markdown
---
id: 3-5-heritage
sidebar_position: 5
title: "Héritage"
description: "Réutiliser et spécialiser une classe avec extends, super, @Override, protected et final."
---
```

- [ ] **Step 2 : Corps** — plan de sections :
  1. **Réutiliser et spécialiser** — `class Chien extends Animal` : `Chien` hérite des attributs/méthodes d'`Animal` et les complète. Relation « est un ».
  2. **`super`** — `super(...)` appelle le constructeur du parent (obligatoire, en première instruction si présent) ; `super.methode()` appelle la version parente.
  3. **Redéfinir avec `@Override`** — une sous-classe peut **redéfinir** une méthode héritée ; annoter `@Override` (le compilateur vérifie qu'on redéfinit bien — protège des fautes de frappe qui créeraient une surcharge accidentelle).
  4. **`protected`** — visible par la classe et ses sous-classes (intermédiaire entre `private` et `public`).
  5. **`final` et `Object`** — `final` interdit l'héritage (classe) ou la redéfinition (méthode) ; toute classe hérite implicitement d'`Object` (d'où `toString()`, qu'on peut redéfinir).
  - **Antériorité** : composition vs héritage **évoquée** (préférer la composition quand ce n'est pas un vrai « est un »), sans détailler. Pas de polymorphisme encore (3-6).
- [ ] **Step 3 : Erreurs fréquentes** : oublier `super(...)` quand le parent n'a pas de constructeur sans argument ; redéfinir sans `@Override` et se tromper de signature (surcharge accidentelle, le parent reste appelé) ; utiliser l'héritage pour du « a un » (devrait être de la composition) ; tenter d'hériter d'une classe `final`.
- [ ] **Step 4 : Exercice guidé** — `Animal` (méthode `crier()` renvoyant un cri générique) puis `Chien` et `Chat` qui **redéfinissent** `crier()` avec `@Override`. **Anti-spoil §5.4** : `Animal`/`Chien`/`Chat` — **ne pas** faire `Vehicule` (exo 3.3.1) ni `Personnel` militaire (3.3.3). Solution dans `<details>`.
- [ ] **Step 5 : Vérifiez vos acquis** — que fait `extends` ? à quoi sert `@Override` et pourquoi l'annoter ? quelle différence entre `protected` et `private` ?
- [ ] **Step 6 : Pour aller plus loin** :
  - `https://dev.java/learn/inheritance/` (dev.java — inheritance)
  - `https://www.baeldung.com/java-inheritance` (Baeldung — inheritance)
- [ ] **Step 7 : Prochain chapitre** → `[3-6-polymorphisme](3-6-polymorphisme)` « Polymorphisme ».
- [ ] **Step 8 : Commit**

```bash
git add courses/docs/module-3-poo/3-5-heritage.md
git commit -m "docs(#19): chapitre 3.5 heritage"
```

---

## Task 7 : Câbler la liaison module 2 → module 3 (chapitre 2-5)

**Files:**
- Modify: `courses/docs/module-2-tableaux-chaines-methodes/2-5-recursivite.md` (dernière section)

- [ ] **Step 1 : Remplacer** le bloc final actuel :

```markdown
## Prochain chapitre

→ **Module 3 — Programmation orientée objet** *(à venir)*
```

par :

```markdown
## Prochain chapitre

→ **[Module 3 — Classes et objets](../module-3-poo/3-1-classes-et-objets)**
```

- [ ] **Step 2 : Commit**

```bash
git add courses/docs/module-2-tableaux-chaines-methodes/2-5-recursivite.md
git commit -m "docs(#19): relier la fin du module 2 au module 3"
```

---

## Task 8 : Vérification PR-A — build Docusaurus + relecture conformité

**Files:** aucun (vérification)

- [ ] **Step 1 : Build Docusaurus**

Run : `cd courses; npm run build`
Expected : build terminé sans erreur. En particulier **aucun** `Error: Docusaurus found broken links` (vérifie les « Prochain chapitre » `3-1`→…→`3-5`→`3-6`, la liaison inter-dossier du 2-5 vers `../module-3-poo/3-1-classes-et-objets`, et les ancres).

> Note : le « Prochain chapitre » du 3-5 pointe vers `3-6-polymorphisme`, qui **n'existe pas encore** (PR-B). Si `onBrokenLinks: 'throw'` casse là-dessus au build de PR-A, mettre temporairement le 3-5 en mention non-liée « → **Polymorphisme** *(chapitre suivant)* » et rétablir le lien actif dans PR-B (Task 10). À trancher au build : tester d'abord avec le lien actif.

- [ ] **Step 2 : Relecture de conformité** (checklist charte §10, sur les 5 chapitres 3-1 à 3-5) :
  - [ ] Structure §6 respectée (les 10 blocs dans l'ordre).
  - [ ] Vouvoiement, phrases courtes, pas de jargon militaire non expliqué, pas d'humour/argot.
  - [ ] Code : anglais pour les identifiants Java, commentaires français du *pourquoi*, imports explicites, pas de variable d'une lettre hors `i/j/k`/`e`.
  - [ ] **Garde-fou §5.2** : ordre des notions tenu (encapsulation après classes ; héritage en dernier de ce lot) ; aucun chapitre n'emploie polymorphisme/abstrait/interface/enum/record (réservés à PR-B).
  - [ ] **Garde-fou §5.1** : aucune collection générique, lambda, exception/IO, ni `equals`/`hashCode` écrit à la main.
  - [ ] **Anti-spoil §5.4** : `Livre`≠Point/Compte/Soldat ; `Temperature`≠Rectangle/Date ; `Couleur`≠validation ; `Ticket` neutre ; `Animal`≠Vehicule/Personnel.
  - [ ] Longueur 800–2000 mots par chapitre.
- [ ] **Step 3 :** corriger inline tout écart, re-`npm run build` si un fichier a changé, puis commit des corrections éventuelles :

```bash
git add courses/docs/module-3-poo/
git commit -m "docs(#19): corrections relecture conformite charte (PR-A)"
```

---

## Task 9 : Pull request A + clôture backlog partielle

**Files:**
- Modify: `docs/backlog.md` (avancement #19, chapitres 3-1 à 3-5)

- [ ] **Step 1 : Mettre à jour `docs/backlog.md`** (section #19) : cocher les chapitres 3-1 à 3-5, le `_category_.json` et la liaison module 2→3 ; noter PR-B (3-6 à 3-10) et les sous-groupes d'exos comme restant à faire. Lien commit/PR ajouté après merge.
- [ ] **Step 2 : Commit**

```bash
git add docs/backlog.md
git commit -m "docs(backlog): #19 chapitres 3-1 a 3-5 (PR-A) livres"
```

- [ ] **Step 3 : Push + PR**

```bash
git push -u origin feature/m3-chapitres-1
gh pr create --base main --title "#19 — Chapitres module 3 (1/2 : bases & heritage)" --body "$(cat <<'BODY'
Chapitres 3-1 à 3-5 du module 3 (classes & objets, encapsulation, constructeurs, this/static, héritage), charte §6 + alignement `_category_.json` + liaison module 2→3 (2-5 → 3-1). Build Docusaurus vert.

Garde-fous : antériorité interne (héritage en fin de lot, rien de PR-B employé) ; antériorité externe (pas de collections/lambdas/exceptions ni equals/hashCode manuel) ; anti-spoil des futurs exos 3.1/3.2/3.3.

Spec: docs/superpowers/specs/2026-06-03-m3-chapitres-design.md
Plan: docs/superpowers/plans/2026-06-03-m3-chapitres.md

🤖 Generated with [Claude Code](https://claude.com/claude-code)
BODY
)"
```

- [ ] **Step 4 :** relecture par un autre formateur (charte §10) + CI #11a vert → merge. (Hors session : action humaine.)

---

# PARTIE B — PR-B : chapitres 3-6 à 3-10 (abstraction & types modernes)

> Branche `feature/m3-chapitres-2` issue de `main` **après merge de PR-A**.

## Task 10 : Créer la branche PR-B (après merge PR-A)

- [ ] **Step 1 : Brancher depuis `main` à jour (PR-A mergée)**

```bash
git checkout main
git pull
git checkout -b feature/m3-chapitres-2
```

- [ ] **Step 2 (si applicable) :** si la note de Task 8/Step 1 a conduit à neutraliser le lien « Prochain chapitre » du 3-5, le **rétablir** vers `3-6-polymorphisme` maintenant que le fichier va exister :

```markdown
## Prochain chapitre

→ **[3-6-polymorphisme](3-6-polymorphisme)** « Polymorphisme »
```

(commit avec le chapitre 3-6 ci-dessous.)

---

## Task 11 : Chapitre 3-6 — Polymorphisme

**Files:**
- Create: `courses/docs/module-3-poo/3-6-polymorphisme.md`

- [ ] **Step 1 : Frontmatter (verbatim)**

```markdown
---
id: 3-6-polymorphisme
sidebar_position: 6
title: "Polymorphisme"
description: "Traiter uniformément des objets via leur type parent : type statique vs dynamique, liaison dynamique, downcast et instanceof pattern."
---
```

- [ ] **Step 2 : Corps** — plan de sections :
  1. **Type déclaré vs type réel** — `Animal a = new Chien();` : type **déclaré** (statique) `Animal`, type **réel** (dynamique) `Chien`.
  2. **Liaison dynamique** — `a.crier()` appelle la version de `Chien` : la méthode exécutée dépend du type **réel**, pas du type déclaré.
  3. **Traiter uniformément** — un `Animal[]` peut contenir des `Chien` et des `Chat` ; une seule boucle appelle `crier()` sur chacun (chaque objet « sait » crier à sa façon).
  4. **Downcast et `instanceof` pattern** — pour utiliser un membre propre à `Chien`, vérifier puis caster : `if (a instanceof Chien c) { c.aboyer(); }` (Java 21+ : la variable `c` est liée si le test réussit).
  - **Antériorité** : dépend de l'héritage (3-5). `instanceof` pattern sera réutilisé au 3-10 (pattern matching sur `sealed`).
- [ ] **Step 3 : Erreurs fréquentes** : downcast sans vérifier (`(Chien) a` sur un `Chat` → `ClassCastException`) ; croire que le type déclaré décide de la méthode appelée ; multiplier les `instanceof` là où une méthode polymorphe suffirait.
- [ ] **Step 4 : Exercice guidé** — remplir un `Animal[]` avec un `Chien` et un `Chat` (du chap. 3-5), parcourir et appeler `crier()` uniformément. **Anti-spoil §5.4** : tableau d'`Animal` — **ne pas** faire `Forme` géométrique (exo 3.3.2) ni la méthode `decrire()` polymorphe (3.3.4). Solution dans `<details>`.
- [ ] **Step 5 : Vérifiez vos acquis** — quelle différence entre type déclaré et type réel ? qu'est-ce que la liaison dynamique ? comment caster sans risquer un `ClassCastException` ?
- [ ] **Step 6 : Pour aller plus loin** :
  - `https://www.baeldung.com/java-polymorphism` (Baeldung — polymorphism)
  - `https://docs.oracle.com/en/java/javase/25/language/pattern-matching-instanceof.html` (Javadoc 25 — pattern matching for instanceof)
- [ ] **Step 7 : Prochain chapitre** → `[3-7-classes-abstraites](3-7-classes-abstraites)` « Classes abstraites ».
- [ ] **Step 8 : Commit** (inclure le rétablissement éventuel du lien 3-5, Task 10/Step 2)

```bash
git add courses/docs/module-3-poo/3-6-polymorphisme.md courses/docs/module-3-poo/3-5-heritage.md
git commit -m "docs(#19): chapitre 3.6 polymorphisme"
```

---

## Task 12 : Chapitre 3-7 — Classes abstraites

**Files:**
- Create: `courses/docs/module-3-poo/3-7-classes-abstraites.md`

- [ ] **Step 1 : Frontmatter (verbatim)**

```markdown
---
id: 3-7-classes-abstraites
sidebar_position: 7
title: "Classes abstraites"
description: "Forcer la spécialisation avec abstract : classe non instanciable, méthodes abstraites et concrètes."
---
```

- [ ] **Step 2 : Corps** — plan de sections :
  1. **Une classe qu'on ne peut pas instancier** — `abstract class Paiement` : `new Paiement()` est interdit ; elle sert de base commune.
  2. **Méthode abstraite** — déclarée sans corps (`abstract double montant();`) : chaque sous-classe **doit** l'implémenter.
  3. **Mélanger abstrait et concret** — une classe abstraite peut avoir des méthodes **concrètes** (comportement commun) et des attributs ; les sous-classes complètent le reste.
  4. **Quand l'utiliser** — factoriser un comportement commun **tout en forçant** la spécialisation de certaines parties.
  - **Antériorité** : dépend de l'héritage (3-5) et du polymorphisme (3-6 : on manipule des `Paiement` sans connaître le type concret).
- [ ] **Step 3 : Erreurs fréquentes** : tenter `new` sur une classe abstraite ; oublier d'implémenter une méthode abstraite dans la sous-classe (ne compile pas, ou la sous-classe doit être abstraite à son tour) ; tout déclarer abstrait alors qu'un comportement commun pourrait être concret.
- [ ] **Step 4 : Exercice guidé** — `abstract class Paiement` avec `abstract double montant()` et une méthode concrète `recu()` ; deux sous-classes (`PaiementCarte`, `PaiementEspeces`) implémentent `montant()`. **Anti-spoil §5.4** : `Paiement` — **éviter** la `Forme` géométrique (souvent abstraite, exo 3.3.2). Solution dans `<details>`.
- [ ] **Step 5 : Vérifiez vos acquis** — pourquoi une classe abstraite ne s'instancie-t-elle pas ? quelle différence entre méthode abstraite et concrète ? que doit faire une sous-classe d'une classe abstraite ?
- [ ] **Step 6 : Pour aller plus loin** :
  - `https://www.baeldung.com/java-abstract-class` (Baeldung — abstract classes)
  - `https://dev.java/learn/abstract-methods/` (dev.java — abstract methods)
- [ ] **Step 7 : Prochain chapitre** → `[3-8-interfaces](3-8-interfaces)` « Interfaces ».
- [ ] **Step 8 : Commit**

```bash
git add courses/docs/module-3-poo/3-7-classes-abstraites.md
git commit -m "docs(#19): chapitre 3.7 classes abstraites"
```

---

## Task 13 : Chapitre 3-8 — Interfaces

**Files:**
- Create: `courses/docs/module-3-poo/3-8-interfaces.md`

- [ ] **Step 1 : Frontmatter (verbatim)**

```markdown
---
id: 3-8-interfaces
sidebar_position: 8
title: "Interfaces"
description: "Définir un contrat avec interface, l'implémenter (y compris multiple) et utiliser les méthodes default et static."
---
```

- [ ] **Step 2 : Corps** — plan de sections :
  1. **Un contrat** — `interface Sonore { String son(); }` : une liste de méthodes à fournir, **sans** implémentation imposée.
  2. **`implements`** — `class Cloche implements Sonore { ... }` doit fournir `son()` ; une classe peut **implémenter plusieurs** interfaces (contrairement à `extends`, unique).
  3. **Méthodes `default` et `static`** — `default` fournit une implémentation par défaut dans l'interface (le code appelant en hérite sans la réécrire) ; `static` regroupe un utilitaire lié au contrat.
  4. **Interface vs classe abstraite** — interface = **contrat** (multiple, pas d'état mutable) ; classe abstraite = base **partielle** (état + méthodes concrètes, héritage unique). Quand préférer l'une ou l'autre.
  - **Antériorité** : dépend des classes abstraites (3-7, pour le contraste). Pas de génériques (M4) : interfaces non paramétrées (`Sonore`, pas `Comparable<T>`).
- [ ] **Step 3 : Erreurs fréquentes** : confondre `implements` (interface) et `extends` (classe) ; oublier d'implémenter une méthode du contrat ; vouloir mettre un attribut mutable dans une interface (les champs y sont `public static final`) ; croire qu'on ne peut implémenter qu'une interface (on peut en cumuler plusieurs).
- [ ] **Step 4 : Exercice guidé** — interface `Sonore { String son(); }` implémentée par deux classes **sans lien d'héritage** (`Cloche`, `Klaxon`) ; les manipuler via le type `Sonore`. **Anti-spoil §5.4** : `Sonore` — **ne pas** faire l'interface `Comparable` custom (exo 3.4.1). Solution dans `<details>`.
- [ ] **Step 5 : Vérifiez vos acquis** — quelle différence entre une interface et une classe abstraite ? combien d'interfaces une classe peut-elle implémenter ? à quoi sert une méthode `default` ?
- [ ] **Step 6 : Pour aller plus loin** :
  - `https://dev.java/learn/interfaces/` (dev.java — interfaces)
  - `https://www.baeldung.com/java-interfaces` (Baeldung — interfaces)
- [ ] **Step 7 : Prochain chapitre** → `[3-9-enums](3-9-enums)` « Enums ».
- [ ] **Step 8 : Commit**

```bash
git add courses/docs/module-3-poo/3-8-interfaces.md
git commit -m "docs(#19): chapitre 3.8 interfaces"
```

---

## Task 14 : Chapitre 3-9 — Enums

**Files:**
- Create: `courses/docs/module-3-poo/3-9-enums.md`

- [ ] **Step 1 : Frontmatter (verbatim)**

```markdown
---
id: 3-9-enums
sidebar_position: 9
title: "Enums"
description: "Définir un type énuméré, lui donner attributs et méthodes, et l'utiliser dans un switch."
---
```

- [ ] **Step 2 : Corps** — plan de sections :
  1. **Un ensemble fini de constantes nommées** — `enum JourSemaine { LUNDI, MARDI, ... }` ; plus sûr que des `int`/`String` (le compilateur connaît toutes les valeurs).
  2. **Enum « riche »** — attributs, constructeur (toujours `private`), méthodes : ex. chaque constante porte une valeur ; méthode `estWeekend()`.
  3. **`switch` sur enum** — brancher selon la constante ; mention de l'exhaustivité ; `values()` pour itérer, `name()`/`ordinal()`.
  - **Antériorité** : s'appuie sur constructeurs (3-3) et méthodes (3-4) ; un enum **peut** implémenter une interface (3-8) — mention possible, sans en faire le cœur.
- [ ] **Step 3 : Erreurs fréquentes** : réinventer un enum avec des constantes `int`/`String` (perte de sûreté) ; `switch` non exhaustif sur les valeurs ; comparer avec `equals` au lieu de `==` (les deux marchent, `==` est idiomatique et null-safe) ; croire qu'on peut créer une instance d'enum avec `new`.
- [ ] **Step 4 : Exercice guidé** — `enum JourSemaine` avec une méthode `estWeekend()` et un `switch` (ou comparaison) qui en dépend ; afficher pour quelques jours. **Anti-spoil §5.4** : `JourSemaine` — **ne pas** faire l'enum `Grade` militaire (exo 3.4.2). Solution dans `<details>`.
- [ ] **Step 5 : Vérifiez vos acquis** — pourquoi un enum est-il plus sûr que des constantes `int` ? un enum peut-il avoir des méthodes ? pourquoi comparer les enums avec `==` ?
- [ ] **Step 6 : Pour aller plus loin** :
  - `https://www.baeldung.com/a-guide-to-java-enums` (Baeldung — enums)
  - `https://dev.java/learn/classes-objects/enums/` (dev.java — enums)
- [ ] **Step 7 : Prochain chapitre** → `[3-10-records-et-sealed](3-10-records-et-sealed)` « Records et sealed ».
- [ ] **Step 8 : Commit**

```bash
git add courses/docs/module-3-poo/3-9-enums.md
git commit -m "docs(#19): chapitre 3.9 enums"
```

---

## Task 15 : Chapitre 3-10 — Records et `sealed`

**Files:**
- Create: `courses/docs/module-3-poo/3-10-records-et-sealed.md`

- [ ] **Step 1 : Frontmatter (verbatim)**

```markdown
---
id: 3-10-records-et-sealed
sidebar_position: 10
title: "Records et sealed"
description: "Découvrir les types modernes de Java : records immuables, hiérarchies scellées et pattern matching exhaustif."
---
```

- [ ] **Step 2 : Corps** — plan de sections :
  1. **Le record, porteur de données immuable** — `record Point(int x, int y) {}` génère constructeur, accesseurs (`x()`, `y()`), `equals`/`hashCode`/`toString`. Immuable : pas de setter, les composants sont fixés à la construction.
  2. **S'appuyer sur l'`equals` généré** — deux records de mêmes composants sont **égaux** (`p1.equals(p2)`) ; on **utilise** cet `equals` (on ne le réécrit pas — l'écriture manuelle est au module 4, spec §10.3).
  3. **`sealed` : fermer une hiérarchie** — `sealed interface Figure permits Cercle, Carre {}` : seuls les types listés peuvent l'implémenter ; chaque sous-type est `final`, `sealed` ou `non-sealed`.
  4. **Pattern matching exhaustif** — un `switch` sur une hiérarchie `sealed` peut couvrir tous les cas **sans `default`** ; le compilateur vérifie l'exhaustivité (`case Cercle c -> ...`).
  - **Antériorité** : dépend de l'héritage/interfaces (3-5/3-8) et du polymorphisme `instanceof` pattern (3-6). **Dernier chapitre du module.** Java 25 dispo localement pour compiler un extrait douteux (syntaxe récente).
- [ ] **Step 3 : Erreurs fréquentes** : tenter de muter un composant de record (impossible, immuable) ; oublier un type dans `permits` (ou un fichier mal placé → erreur de compilation) ; ajouter un `default` dans un `switch` `sealed` qui **masque** la vérification d'exhaustivité (si on ajoute un type, le compilateur ne prévient plus) ; vouloir réécrire `equals` d'un record à la main (inutile).
- [ ] **Step 4 : Exercice guidé** — `record Point(int x, int y)` puis `sealed interface Figure permits Cercle, Carre` (`Cercle` et `Carre` étant des records implémentant `Figure`), avec une fonction qui calcule l'aire par **pattern matching** `switch` sans `default`. **Anti-spoil §5.4** : `Point`/`Figure` — **ne pas** faire `record Coordonnees` (exo 3.4.3) ni la hiérarchie `sealed` d'**événements** (3.4.4). Solution dans `<details>`.
- [ ] **Step 5 : Vérifiez vos acquis** — qu'est-ce qu'un record génère automatiquement ? que garantit `sealed` ? pourquoi éviter `default` dans un `switch` sur hiérarchie `sealed` ?
- [ ] **Step 6 : Pour aller plus loin** :
  - `https://docs.oracle.com/en/java/javase/25/language/records.html` (Javadoc 25 — records)
  - `https://docs.oracle.com/en/java/javase/25/language/sealed-classes-and-interfaces.html` (Javadoc 25 — sealed classes)
- [ ] **Step 7 : Prochain chapitre** → mention « → **Module 4 — Collections, génériques, lambdas** *(à venir)* » (pas de lien actif, le module 4 n'existe pas encore).
- [ ] **Step 8 : Commit**

```bash
git add courses/docs/module-3-poo/3-10-records-et-sealed.md
git commit -m "docs(#19): chapitre 3.10 records et sealed"
```

---

## Task 16 : Vérification PR-B — build Docusaurus + relecture conformité

**Files:** aucun (vérification)

- [ ] **Step 1 : Build Docusaurus**

Run : `cd courses; npm run build`
Expected : build terminé sans erreur, **aucun** `broken links` (vérifie `3-6`→…→`3-10`, le lien rétabli du 3-5 vers `3-6`, et que le 3-10 ne pointe vers aucun lien actif).

- [ ] **Step 2 : Relecture de conformité** (checklist charte §10, sur les 5 chapitres 3-6 à 3-10) :
  - [ ] Structure §6 respectée (les 10 blocs dans l'ordre).
  - [ ] Vouvoiement, phrases courtes, pas de jargon militaire non expliqué, pas d'humour/argot.
  - [ ] Code : anglais pour les identifiants Java, commentaires français du *pourquoi*, imports explicites, pas de variable d'une lettre hors `i/j/k`/`e`.
  - [ ] **Garde-fou §5.2** : polymorphisme après héritage ; classes abstraites après polymorphisme ; records/`sealed` en dernier (réutilisent `instanceof` pattern de 3-6).
  - [ ] **Garde-fou §5.1** : pas de collections génériques, lambda, exception/IO ; **`equals`/`hashCode` seulement via génération record**, jamais écrit à la main.
  - [ ] **Anti-spoil §5.4** : `Animal[]`≠Forme/decrire ; `Paiement`≠Forme ; `Sonore`≠Comparable ; `JourSemaine`≠Grade ; `Point`/`Figure`≠Coordonnees/événements.
  - [ ] Longueur 800–2000 mots par chapitre.
- [ ] **Step 3 :** corriger inline tout écart, re-`npm run build` si besoin, commit des corrections éventuelles :

```bash
git add courses/docs/module-3-poo/
git commit -m "docs(#19): corrections relecture conformite charte (PR-B)"
```

---

## Task 17 : Pull request B + clôture backlog module 3 (chapitres)

**Files:**
- Modify: `docs/backlog.md` (avancement #19, chapitres 3-6 à 3-10 → volet chapitres complet)

- [ ] **Step 1 : Mettre à jour `docs/backlog.md`** (section #19) : cocher les chapitres 3-6 à 3-10 ; noter le **volet chapitres du module 3 comme livré** (les 4 sous-groupes d'exos restent à faire dans des cycles séparés). Lien commit/PR après merge.
- [ ] **Step 2 : Commit**

```bash
git add docs/backlog.md
git commit -m "docs(backlog): #19 volet chapitres module 3 livre"
```

- [ ] **Step 3 : Push + PR**

```bash
git push -u origin feature/m3-chapitres-2
gh pr create --base main --title "#19 — Chapitres module 3 (2/2 : abstraction & types modernes)" --body "$(cat <<'BODY'
Chapitres 3-6 à 3-10 du module 3 (polymorphisme, classes abstraites, interfaces, enums, records & sealed), charte §6. Build Docusaurus vert. Clôt le volet chapitres du module 3.

Garde-fous : antériorité interne (polymorphisme après héritage ; records/sealed en dernier) ; antériorité externe (pas de collections/lambdas/exceptions ; equals/hashCode uniquement via génération record, jamais à la main) ; anti-spoil des futurs exos 3.3/3.4.

Spec: docs/superpowers/specs/2026-06-03-m3-chapitres-design.md
Plan: docs/superpowers/plans/2026-06-03-m3-chapitres.md

🤖 Generated with [Claude Code](https://claude.com/claude-code)
BODY
)"
```

- [ ] **Step 4 :** relecture par un autre formateur (charte §10) + CI #11a vert → merge. (Hors session : action humaine.)

---

## Self-review (rempli à la rédaction du plan)

- **Couverture spec** : `_category_.json` §2 → Task 1 ; chapitres 3-1..3-5 §2 → Tasks 2–6 ; chapitres 3-6..3-10 §2 → Tasks 11–15 ; retouche liaison 2-5 §2 → Task 7 ; découpage 2 PR §8 → Parties A/B (Tasks 0/9 et 10/17) ; vérification §7 (build + relecture) → Tasks 8 et 16 ; workflow/PR/backlog §8 → Tasks 9 et 17. Garde-fous §5.1/§5.2/§5.4 → « Cadre commun » + intégrés à chaque task + checklists Tasks 8/16. Conventions §4 → « Cadre commun ». Décisions transverses §10 de la spec = volet exercices, **hors périmètre** de ce cycle → volontairement non couvertes. ✅ Pas de gap.
- **Placeholders** : les seuls « *(à venir)* » sont volontaires (module 4 inexistant, Task 15) ; les cases `[ ]` sont le format de suivi attendu. La note Task 8/Step 1 (lien 3-5→3-6 au moment de PR-A) est une **décision conditionnelle au build**, pas un trou. ✅
- **Cohérence des liens internes** : « Prochain chapitre » `3-1`→`3-2`→…→`3-9`→`3-10` ; le 2-5 (module 2) pointe vers `../module-3-poo/3-1-classes-et-objets` ; le 3-10 ne pointe vers aucun lien actif ; le lien 3-5→3-6 traverse la frontière PR-A/PR-B (géré Tasks 8/10/11). ✅
- **Cohérence des notions** : `this` introduit au 3-3 (usage) puis défini au 3-4 ✅ ; héritage (3-5) précède polymorphisme (3-6) précède abstraites (3-7) ✅ ; `instanceof` pattern défini au 3-6 et réutilisé au 3-10 ✅ ; `equals`/`hashCode` jamais écrits à la main, seulement générés (records, 3-10) ✅ ; pas de génériques sur les interfaces (3-8) ni `Comparable<T>` (réservé au volet exos / M4) ✅.
- **Cohérence anti-spoil** : domaines des guidés (`Livre`, `Temperature`, `Couleur`, `Ticket`, `Animal/Chien/Chat`, `Animal[]`, `Paiement`, `Sonore`, `JourSemaine`, `Point/Figure`) tous distincts des domaines d'exos notés (`Point2D`, `Compte`, `Soldat`, `Rectangle`, `Date`, `Vehicule`, `Forme`, `Personnel`, `Comparable`, `Grade`, `Coordonnees`, événements `sealed`). ✅
