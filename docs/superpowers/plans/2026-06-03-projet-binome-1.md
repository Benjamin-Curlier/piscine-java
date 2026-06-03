# Projet binôme #1 « Caserne » — Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Rédiger la fiche de projet (livrable formateur, 100 % rédactionnel) du Projet binôme #1 « Caserne » dans `exercises/projets-binome/projet-1-mini-domaine/`, conforme au format §11 et à la charte.

**Architecture:** Aucun code Java. On crée 5 fichiers (4 fichiers de fiche + un `.gitkeep`) dans un nouveau dossier `exercises/projets-binome/projet-1-mini-domaine/`, puis on met à jour deux docs existantes (`backlog.md`, `referentiel.md`). L'évaluation est manuelle ; la CI (lint) ignore nativement `projets-binome/`. Une seule PR `feature/projet-binome-1`.

**Tech Stack:** Markdown + YAML. Charte : vouvoiement, phrases courtes, pas d'humour, touche militaire. Référence format : `docs/format-exercice.md` §11.

---

## Contexte hérité (à NE PAS redécouvrir)

- **Décisions de design** : voir spec [`docs/superpowers/specs/2026-06-03-projet-binome-1-design.md`](../specs/2026-06-03-projet-binome-1-design.md). Résumé : cahier des charges fonctionnel ; menu console interactif ; socle Caserne/Unite/Soldat + 3 opérations ; Git léger + répartition tracée ; barème /20 seuil 12 ; durée 12 h ; pas de prototype interne.
- **Antériorité M1–M3 uniquement.** ❌ collections/génériques, lambdas/streams (M4) ; exceptions, I/O fichier (M5) ; branches/PR notées (M6). Ensembles → **tableaux à capacité fixe**. Cas invalides → **refus/correction sans exception**.
- **Vocabulaire OO disponible** (exos M3 déjà livrés, à référencer sans imposer fichier par fichier) : enum `Grade` (SOLDAT→LIEUTENANT, ordre = promotion), `Soldat`, agrégation/tableaux, hiérarchie `Personnel` (bonus).
- **Lint CI** : `scripts/lint-exercices.sh` ne parcourt que `exercises/module-*/` → `projets-binome/` ignoré, **aucun changement de script**.

## File Structure

| Fichier | Responsabilité |
|---|---|
| `exercises/projets-binome/projet-1-mini-domaine/sujet.md` | Cahier des charges fonctionnel (contexte, entités, opérations, règles, démo, bonus) |
| `exercises/projets-binome/projet-1-mini-domaine/consignes-livraison.md` | Modalités Git léger, README attendu, répartition binôme |
| `exercises/projets-binome/projet-1-mini-domaine/evaluation.yml` | Grille manuelle /20, seuil 12 |
| `exercises/projets-binome/projet-1-mini-domaine/metadata.yml` | Métadonnées binôme |
| `exercises/projets-binome/projet-1-mini-domaine/exemples-rendus/.gitkeep` | Dossier vide + note post-promo |
| `docs/backlog.md` (modif) | Marquer #23 en cours/livré |
| `docs/referentiel.md` (modif) | §5 : dossier plus « à venir » |

---

### Task 1: `sujet.md` — cahier des charges fonctionnel

**Files:**
- Create: `exercises/projets-binome/projet-1-mini-domaine/sujet.md`

- [ ] **Step 1: Créer le fichier avec ce contenu exact**

```markdown
# Projet binôme #1 — Gestion d'une caserne

## Contexte

Une caserne héberge plusieurs unités. Chaque unité accueille des soldats, dans
la limite de sa capacité. Chaque soldat porte un grade. L'état-major a besoin
d'un petit logiciel pour affecter les soldats aux unités, gérer leurs
promotions et consulter les effectifs.

Vous concevez ce logiciel **en binôme**, de A à Z. Le sujet impose **ce que** le
programme doit faire ; vous restez libres du **comment** (noms de classes,
signatures, découpage).

## Objectifs pédagogiques

- Concevoir un mini-domaine orienté objet : entités, encapsulation, agrégation.
- Réutiliser un `enum` pour modéliser un ensemble fini (les grades).
- Gérer des ensembles d'objets avec des **tableaux à capacité fixe**.
- Traiter les cas invalides par **refus ou correction**, sans exception.
- Collaborer en binôme avec un historique Git équilibré.

## Entités imposées

Vous devez modéliser au minimum les entités suivantes. Les attributs listés sont
un minimum ; les signatures précises et le découpage interne sont à votre
initiative.

- **Grade** — un `enum` des grades militaires, du plus bas au plus haut. Vous
  pouvez reprendre la progression vue en module 3 : `SOLDAT`, `CAPORAL`,
  `SERGENT`, `ADJUDANT`, `LIEUTENANT`. L'ordre des constantes définit le
  « grade suivant » utilisé pour les promotions.
- **Soldat** — possède au minimum un nom et un grade.
- **Unite** — possède un nom et un ensemble de soldats borné par une
  **capacité maximale fixe**. L'ensemble est géré par un **tableau** (pas de
  `List` ni de `ArrayList`).
- **Caserne** — possède un ensemble d'unités, géré lui aussi par un **tableau**.

## Opérations obligatoires

1. **Affecter** un soldat à une unité.
2. **Promouvoir** un soldat : il passe au grade immédiatement supérieur.
3. **Lister** l'effectif : par unité et/ou pour l'ensemble de la caserne.

## Règles métier (sans exception)

Les cas invalides ne doivent **jamais** lever d'exception (`throw` interdit). Vous
les traitez par **refus ou correction**, avec un message en console et une valeur
de retour explicite (par exemple un `boolean` indiquant le succès) :

- Affecter un soldat à une **unité pleine** : l'affectation est refusée.
- Promouvoir un soldat déjà au **grade maximal** : la promotion est refusée.

## Contrainte d'antériorité (importante)

Ce projet n'utilise que les notions des **modules 1 à 3**.

- Autorisé : classes, encapsulation, constructeurs, héritage, polymorphisme,
  classes abstraites, interfaces, `enum`, `record`, `sealed` ; tableaux ;
  méthodes et membres `static` ; entrées/sorties **console**
  (`Scanner`, `System.out`).
- Interdit : collections et génériques (`List`, `Map`, `ArrayList`…) et
  lambdas/streams (module 4) ; exceptions (`try`/`catch`/`throw`) et
  entrées/sorties fichier (module 5).

Pour gérer les ensembles (soldats d'une unité, unités d'une caserne), vous
utilisez des **tableaux à capacité fixe**.

## Démonstration attendue

Le programme expose un **menu console interactif** (à l'aide de `Scanner`). Le
formateur lance le programme et saisit des commandes. Votre menu doit au minimum
proposer :

- `affecter` — affecter un soldat à une unité ;
- `promouvoir` — promouvoir un soldat ;
- `lister` — afficher l'effectif ;
- `quitter` — terminer le programme.

Ces commandes minimales sont **imposées** : elles permettent au formateur
d'évaluer chaque rendu sur la même base.

## Bonus (facultatifs, valorisés)

Ces extensions ne sont pas obligatoires mais sont valorisées dans la note de
conception :

- Une hiérarchie de personnels : une classe abstraite `Personnel` et ses
  sous-classes `Officier`, `SousOfficier`, `MilitaireDuRang`, avec une méthode
  `solde()` redéfinie (héritage et polymorphisme).
- Le calcul de la solde totale d'une unité.
- Le tri de l'effectif par grade.
```

- [ ] **Step 2: Vérifier la conformité charte (relecture)**

Vérifier : vouvoiement partout, phrases courtes, pas d'humour, aucune notion M4+ mentionnée comme autorisée, `throw` bien présenté comme interdit. Aucune commande à lancer (pas de moulinette sur ce dossier).

- [ ] **Step 3: Commit**

```bash
git add exercises/projets-binome/projet-1-mini-domaine/sujet.md
git commit -m "docs(#23): sujet.md du Projet binôme #1 (cahier des charges Caserne)

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

---

### Task 2: `consignes-livraison.md`

**Files:**
- Create: `exercises/projets-binome/projet-1-mini-domaine/consignes-livraison.md`

- [ ] **Step 1: Créer le fichier avec ce contenu exact**

```markdown
# Consignes de livraison — Projet binôme #1

## Modalités

- Ce projet se réalise **en binôme**. Les binômes sont **imposés par les
  formateurs**.
- Durée indicative : environ 12 heures, réparties sur plusieurs séances.

## Dépôt et historique Git

- Le rendu se fait dans un **dépôt Git**.
- **Les deux membres** du binôme commitent régulièrement. L'historique doit
  refléter une **contribution équilibrée** : il sert de preuve du travail de
  chacun.
- Les branches et les pull requests ne sont **pas exigées** à ce stade : un
  travail direct sur la branche principale est accepté. Le workflow de branches
  sera vu plus tard.

## README attendu

Le dépôt contient un fichier `README.md` qui précise :

- **Comment compiler** le projet (par exemple `javac`).
- **Comment lancer** le programme (par exemple `java`), et comment utiliser le
  menu console (liste des commandes).
- La **répartition du binôme** : qui a réalisé quoi.

## Arborescence et format

- Les sources Java sont organisées dans un package (par exemple `etnc`).
- Aucun fichier compilé (`.class`) ni dossier de build n'est versionné.
- Le rendu est remis à la date fixée par le formateur.
```

- [ ] **Step 2: Vérifier la conformité charte (relecture)**

Vérifier : vouvoiement, phrases courtes, Git léger sans branches/PR imposées, README couvre compiler/lancer/répartition.

- [ ] **Step 3: Commit**

```bash
git add exercises/projets-binome/projet-1-mini-domaine/consignes-livraison.md
git commit -m "docs(#23): consignes de livraison du Projet binôme #1

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

---

### Task 3: `evaluation.yml` — grille manuelle /20

**Files:**
- Create: `exercises/projets-binome/projet-1-mini-domaine/evaluation.yml`

- [ ] **Step 1: Créer le fichier avec ce contenu exact**

```yaml
total: 20
seuil_reussite: 12
criteres:
  - id: conception-oo
    description: "Conception OO : encapsulation, enum, agrégation, héritage/polymorphisme à bon escient (le polymorphisme est valorisé quand il est pertinent, non pénalisé sinon)"
    poids: 8
    type: formateur
  - id: perimetre
    description: "Périmètre fonctionnel : opérations affecter/promouvoir/lister conformes, refus propre des cas invalides sans exception"
    poids: 5
    type: formateur
  - id: lisibilite
    description: "Lisibilité et idiomatisme POO : noms clairs, méthodes courtes, code propre"
    poids: 4
    type: formateur
  - id: collaboration
    description: "Collaboration : contributions équilibrées (historique Git) et README décrivant la répartition"
    poids: 3
    type: formateur
```

- [ ] **Step 2: Vérifier la somme des poids et le format**

Vérifier à l'œil : 8 + 5 + 4 + 3 = 20 (= `total`), tous `type: formateur` (pas de critère automatique), structure identique aux `evaluation.yml` des exos (`total`/`seuil_reussite`/`criteres` avec `id`/`description`/`poids`/`type`).

- [ ] **Step 3: Commit**

```bash
git add exercises/projets-binome/projet-1-mini-domaine/evaluation.yml
git commit -m "docs(#23): grille d'évaluation manuelle du Projet binôme #1

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

---

### Task 4: `metadata.yml`

**Files:**
- Create: `exercises/projets-binome/projet-1-mini-domaine/metadata.yml`

- [ ] **Step 1: Créer le fichier avec ce contenu exact**

```yaml
slug: projet-1-mini-domaine
titre: "Projet binôme #1 — Caserne"
binome: true
module: 3
duree_estimee_h: 12
prerequis:
  - "3.1"
  - "3.2"
  - "3.3"
  - "3.4"
objectifs_pedagogiques:
  - "Concevoir un mini-domaine OO (entités, encapsulation, agrégation)"
  - "Gérer des ensembles avec des tableaux à capacité fixe (sans collections)"
  - "Traiter les cas invalides par refus/correction, sans exception"
  - "Collaborer en binôme avec un historique Git équilibré"
notions:
  - classe
  - objet
  - encapsulation
  - enum
  - heritage
  - polymorphisme
  - tableau
  - agregation
auteur: "ETNC"
version: 1
date_creation: 2026-06-03
```

- [ ] **Step 2: Vérifier les champs**

Vérifier : `binome: true` présent, `duree_estimee_h` (et **non** `duree_estimee_min`), pas de champs propres aux exos individuels (`sous_groupe`, `position`, `difficulte`). Conforme à `format-exercice.md` §11.

- [ ] **Step 3: Commit**

```bash
git add exercises/projets-binome/projet-1-mini-domaine/metadata.yml
git commit -m "docs(#23): metadata.yml du Projet binôme #1

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

---

### Task 5: `exemples-rendus/.gitkeep`

**Files:**
- Create: `exercises/projets-binome/projet-1-mini-domaine/exemples-rendus/.gitkeep`

- [ ] **Step 1: Créer le fichier avec ce contenu exact**

```text
# Ce dossier accueillera 1 ou 2 rendus exemplaires anonymisés.
# Il reste vide jusqu'à la première promotion (rendus ajoutés post-promo).
```

- [ ] **Step 2: Commit**

```bash
git add exercises/projets-binome/projet-1-mini-domaine/exemples-rendus/.gitkeep
git commit -m "docs(#23): dossier exemples-rendus (vide, rendus post-promo)

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

---

### Task 6: Mettre à jour `docs/backlog.md`

**Files:**
- Modify: `docs/backlog.md:389-391`

- [ ] **Step 1: Lire le bloc actuel pour confirmer le texte exact**

Run: lire les lignes 389–391 de `docs/backlog.md`. Texte actuel :

```markdown
### #23 — Projet binôme #1 — Mini-domaine OO (Caserne)
**Pré-requis** : #19
Voir `referentiel.md` §5. Format à définir dans `exercises/projets-binome/projet-1-mini-domaine/`.
```

- [ ] **Step 2: Remplacer la 3e ligne pour marquer la fiche livrée**

Nouveau texte du bloc :

```markdown
### #23 — Projet binôme #1 — Mini-domaine OO (Caserne)
**Pré-requis** : #19
Voir `referentiel.md` §5. ✅ **Fiche livrée** dans `exercises/projets-binome/projet-1-mini-domaine/` (sujet, consignes, grille, métadonnées). Conception : spec [`specs/2026-06-03-projet-binome-1-design.md`](superpowers/specs/2026-06-03-projet-binome-1-design.md) + plan [`plans/2026-06-03-projet-binome-1.md`](superpowers/plans/2026-06-03-projet-binome-1.md).
```

- [ ] **Step 3: Commit**

```bash
git add docs/backlog.md
git commit -m "docs(backlog): #23 fiche Projet binôme #1 livrée

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

---

### Task 7: Mettre à jour `docs/referentiel.md` §5

**Files:**
- Modify: `docs/referentiel.md:186`

- [ ] **Step 1: Lire la ligne 186 pour confirmer le texte exact**

Run: lire la ligne 186 de `docs/referentiel.md`. Texte actuel :

```markdown
Chaque projet a son propre dossier dans [`exercises/`](../exercises/) (à venir) avec sujet, critères d'évaluation, livrables attendus et grille de notation pédagogique.
```

- [ ] **Step 2: Remplacer pour signaler que le Projet 1 n'est plus « à venir »**

Nouveau texte :

```markdown
Chaque projet a son propre dossier dans [`exercises/`](../exercises/) avec sujet, critères d'évaluation, livrables attendus et grille de notation pédagogique. Le **Projet 1** est disponible : [`exercises/projets-binome/projet-1-mini-domaine/`](../exercises/projets-binome/projet-1-mini-domaine/). Les projets 2 et 3 sont à venir.
```

- [ ] **Step 3: Commit**

```bash
git add docs/referentiel.md
git commit -m "docs(referentiel): §5 Projet binôme #1 disponible

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

---

### Task 8: Vérification finale + PR

- [ ] **Step 1: Vérifier l'arborescence créée**

Run: `ls -R exercises/projets-binome/projet-1-mini-domaine`
Expected : `sujet.md`, `consignes-livraison.md`, `evaluation.yml`, `metadata.yml`, `exemples-rendus/.gitkeep`.

- [ ] **Step 2: Lancer le lint pour confirmer qu'il ignore `projets-binome/`**

Run: `bash scripts/lint-exercices.sh`
Expected : PASS, parcourt uniquement les `module-*` (36 exos M1–M3), ne signale rien sur `projets-binome/`.

- [ ] **Step 3: Pousser la branche et ouvrir la PR**

```bash
git push -u origin feature/projet-binome-1
gh pr create --base main --title "docs(#23): fiche du Projet binôme #1 (Caserne)" --body "Fiche complète du Projet binôme #1 (mini-domaine OO « Caserne »).

## Contenu
- sujet.md — cahier des charges fonctionnel (entités, opérations affecter/promouvoir/lister, règles sans exception, menu console, bonus Personnel)
- consignes-livraison.md — Git léger + répartition tracée, README attendu
- evaluation.yml — grille manuelle /20, seuil 12 (conception-oo 8, perimetre 5, lisibilite 4, collaboration 3)
- metadata.yml — binome: true, duree_estimee_h: 12
- exemples-rendus/.gitkeep — dossier vide (rendus post-promo)
- MAJ backlog.md (#23) et referentiel.md §5

## Conception
- Spec : docs/superpowers/specs/2026-06-03-projet-binome-1-design.md
- Plan : docs/superpowers/plans/2026-06-03-projet-binome-1.md

## Notes
- Antériorité M1–M3 respectée (tableaux, sans collections ni exceptions).
- Lint inchangé : scripts/lint-exercices.sh ne parcourt que exercises/module-*/.

🤖 Generated with [Claude Code](https://claude.com/claude-code)"
```

---

## Self-Review (effectuée à la rédaction du plan)

**Spec coverage :** chaque section du livrable de la spec §5 a une tâche — sujet.md (T1), consignes (T2), evaluation.yml (T3), metadata.yml (T4), exemples-rendus (T5) ; MAJ doc §6 (T6, T7) ; cadence/CI §7 (T8). ✅
**Placeholders :** contenu complet fourni pour chaque fichier, aucun « TBD ». ✅
**Cohérence :** ids de critères (`conception-oo`/`perimetre`/`lisibilite`/`collaboration`) et poids (8/5/4/3=20) identiques entre spec et evaluation.yml ; `duree_estimee_h: 12` cohérent metadata/sujet. ✅
**Adaptation TDD :** livrable rédactionnel sans code → pas de cycle test/impl ; la « vérification » de chaque tâche est la relecture charte/format + le lint final (T8). Justifié.
