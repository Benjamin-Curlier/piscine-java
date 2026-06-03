# Spec — Projet binôme #1 « Caserne » (mini-domaine OO)

> **Statut** : design validé (brainstorm du 2026-06-03), prêt pour le plan d'implémentation.
> **Issue** : #23 (Projet binôme #1). **Format figé** : [`format-exercice.md`](../../format-exercice.md) §11. **Sujet de référence** : [`referentiel.md`](../../referentiel.md) §5.
> **Kickoff** : [`2026-06-03-projet-binome-1-kickoff.md`](../2026-06-03-projet-binome-1-kickoff.md).

## 1. Objectif

Produire la **fiche de projet** (livrable formateur, 100 % rédactionnel — pas de code, pas de prototype) du premier projet binôme de la Piscine ETNC, sur le thème « Gestion d'une caserne ». Le binôme conçoit l'application de A à Z ; l'évaluation est **manuelle**, guidée par une grille.

## 2. Décisions validées (brainstorm)

| Sujet | Décision |
|---|---|
| Liberté de conception | **Cahier des charges fonctionnel** : on impose le *quoi* (entités + opérations + règles), le binôme décide du *comment* (signatures, découpage). |
| Mode de démonstration | **Menu console interactif** (`Scanner`), avec un jeu de commandes minimal imposé pour rester comparable entre binômes. |
| Périmètre fonctionnel | **Socle Caserne / Unite / Soldat + 3 opérations** (affecter, promouvoir, lister). Hiérarchie `Personnel` en **bonus**. |
| Collaboration / Git | **Git léger + répartition tracée** : commits réguliers des deux membres, README décrivant qui a fait quoi. Pas de branches/PR imposées (réservé M6). |
| Barème | **Total 20, seuil 12** (aligné sur les exercices). |
| Durée | **`duree_estimee_h: 12`** (8–12 h sur plusieurs séances). |
| Prototype interne | **Non** : faisabilité garantie par le vocabulaire M3 déjà éprouvé. |

## 3. Contraintes structurantes

- **Antériorité (critère bloquant)** : le projet arrive après le module 3, il ne mobilise que **M1 à M3**.
  - ✅ Autorisé : classes, encapsulation, constructeurs, héritage, polymorphisme, classes abstraites, interfaces, enums, records, `sealed` ; **tableaux** (M2) ; méthodes/`static` (M2) ; E/S **console** (`Scanner`/`System.out`, M1) ; validation **par correction/refus sans exception** (idiome 3.2).
  - ❌ Interdit : **collections & génériques** (`List`, `Map`, `ArrayList`…) et **lambdas/streams** = M4 ; **exceptions** (`try`/`catch`/`throw`) et **I/O fichier** = M5 ; Git avancé (branches/PR) comme objectif noté = M6.
- **Sans collections** → ensembles de soldats/unités gérés par **tableaux à capacité fixe**.
- **Sans exceptions** → opérations invalides (unité pleine, grade déjà max) gérées par **refus/correction** (retour booléen ou valeur sentinelle + message console), jamais de `throw`.
- **Charte de rédaction** : vouvoiement, phrases courtes, pas d'humour, touche militaire bienvenue ([`charte-redaction.md`](../../charte-redaction.md)).
- **Lint / CI** : `scripts/lint-exercices.sh` ne parcourt que `exercises/module-*/` (boucle ligne 41). `projets-binome/` est **ignoré nativement** → aucun changement de script, pas de risque de CI cassée.

## 4. Vocabulaire OO disponible (issu du module 3, déjà éprouvé)

Matériau que le sujet recombine (référence, non imposé fichier par fichier) :

- **`Grade`** (enum, exo 3.4.2) : `SOLDAT(1600)`, `CAPORAL(1800)`, `SERGENT(2200)`, `ADJUDANT(2600)`, `LIEUTENANT(3000)` ; `getSoldeBase()`, `categorie()` par `switch` exhaustif. L'ordre des constantes donne le « grade suivant » pour `promouvoir`.
- **`Personnel`** (abstrait, exo 3.3.3) → `Officier` / `SousOfficier` / `MilitaireDuRang` : `solde()`, `fiche()` polymorphes. Disponible pour le **bonus**.
- **`Soldat`** (exo 3.1.3), encapsulation (3.1/3.2), agrégation et tableaux (3.3.4 « parc »).

## 5. Livrables (la fiche de projet)

Cible : `exercises/projets-binome/projet-1-mini-domaine/`. Structure imposée par §11 :

```
projet-1-mini-domaine/
├── sujet.md                  # cahier des charges fonctionnel
├── consignes-livraison.md    # Git léger, README, répartition
├── evaluation.yml            # grille /20, seuil 12
├── metadata.yml              # binome: true, duree_estimee_h: 12
└── exemples-rendus/
    └── .gitkeep              # note « rendus exemplaires ajoutés post-promo »
```

**Pas de `starter/`, `solution/`, `tests/`.** Évaluation manuelle.

### 5.1 `sujet.md`

Sections :

1. **Contexte** — gestion d'une caserne militaire (ton charte, touche militaire).
2. **Objectifs pédagogiques** — conception OO d'un mini-domaine : encapsulation, enum, agrégation, tableaux à capacité fixe, recombinés depuis M1–M3.
3. **Entités imposées** (le *quoi* ; signatures et découpage libres) :
   - `Grade` — enum des grades (réutilisable depuis M3), donne l'ordre de promotion.
   - `Soldat` — au minimum un nom et un grade.
   - `Unite` — un nom et un ensemble de soldats à **capacité maximale fixe** (tableau).
   - `Caserne` — un ensemble d'unités (tableau).
4. **Opérations obligatoires** :
   - **Affecter** un soldat à une unité.
   - **Promouvoir** un soldat (passage au grade suivant).
   - **Lister** l'effectif (par unité et/ou global).
5. **Règles métier sans exception** (idiome 3.2) : refus propre si unité pleine ou grade déjà au maximum → message + retour booléen/valeur sentinelle ; **jamais** de `throw`.
6. **Contrainte d'antériorité** explicite (cf. §3) : rappel ✅ M1–M3 / ❌ M4+ ; tableaux à capacité fixe imposés pour les ensembles.
7. **Démonstration attendue** : **menu console interactif** (`Scanner`) exposant au minimum `affecter` / `promouvoir` / `lister` / `quitter`. Jeu de commandes minimal imposé pour la comparabilité.
8. **Bonus (non bloquants, valorisés)** : hiérarchie `Personnel` → `Officier`/`SousOfficier`/`MilitaireDuRang` (héritage + polymorphisme, `solde()`) ; solde totale d'une unité ; tri d'effectif par grade.

### 5.2 `consignes-livraison.md`

- **Rendu via dépôt Git** ; commits réguliers **des deux membres** (l'historique sert de preuve d'équilibre des contributions). Pas de branches/PR imposées (réservé M6).
- **README attendu** : comment compiler/lancer (`javac` / `java`), description du menu et de ses commandes, **répartition du binôme** (qui a fait quoi).
- Binômes **imposés par les formateurs** (refléter `referentiel.md` §5).
- Préciser : arborescence de sources attendue (package `etnc`…), format de rendu, échéance.

### 5.3 `evaluation.yml`

Grille **/20, seuil 12**, **sans critères automatiques** (`type: formateur` partout — la moulinette n'exécute pas) :

| id | description | poids |
|---|---|---|
| `conception-oo` | Encapsulation, enum, agrégation, héritage/polymorphisme **à bon escient** | 8 |
| `perimetre` | Opérations affecter/promouvoir/lister conformes + refus propre **sans exception** | 5 |
| `lisibilite` | Style, idiomatisme POO, méthodes courtes | 4 |
| `collaboration` | Équilibre des contributions (historique Git + README) | 3 |

Note de conception : `conception-oo` **valorise** le polymorphisme quand il est pertinent (bonus `Personnel`) mais **ne pénalise pas** son absence lorsque le socle ne l'exige pas. (Poids : 8 + 5 + 4 + 3 = 20.)

### 5.4 `metadata.yml`

```yaml
slug: projet-1-mini-domaine
titre: "Projet binôme #1 — Caserne"
binome: true
module: 3
duree_estimee_h: 12
prerequis: ["3.1", "3.2", "3.3", "3.4"]
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

> ⚠️ À vérifier au moment de la rédaction : champs exacts attendus pour un `metadata.yml` binôme (le gabarit exercice utilise `sous_groupe`/`position`/`difficulte`/`duree_estimee_min` qui ne s'appliquent pas ici). On retient `duree_estimee_h` (§11) et `binome: true` ; on omet les champs propres aux exercices individuels.

### 5.5 `exemples-rendus/`

Dossier présent mais **vide au départ** : un `.gitkeep` portant une note « rendus exemplaires anonymisés ajoutés *post-promo* ».

## 6. Mise à jour de la documentation existante

- `docs/backlog.md` : marquer #23 comme livré/en cours selon convention.
- `docs/referentiel.md` §5 : le dossier `projet-1-mini-domaine` n'est plus « à venir ».

## 7. Cadence / livraison

- **Un seul cycle = une PR** `feature/projet-binome-1`, branchée depuis `main` à jour.
- Exécution **inline** (contrainte projet : pas de sous-agents).
- **CI verte** avant merge (lint inchangé ; build Docusaurus si le contenu touche le site).

## 8. Hors périmètre (YAGNI)

- Aucun code Java livré ni prototype interne.
- Aucun `starter/` / `solution/` / `tests/`.
- Pas d'auto-grading ni de tests d'acceptation (évaluation strictement manuelle, conforme §11).
- Pas de branches/PR imposées au binôme (réservé M6).
