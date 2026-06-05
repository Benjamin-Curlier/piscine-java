# Spec — Projet binôme #2 « Gestionnaire de tâches CLI » (app + persistance fichier)

> **Statut** : design validé (brainstorm du 2026-06-05), prêt pour le plan d'implémentation.
> **Issue** : #24 (Projet binôme #2). **Format figé** : [`format-exercice.md`](../../format-exercice.md) §11. **Sujet de référence** : [`referentiel.md`](../../referentiel.md) §5.
> **Kickoff** : [`2026-06-04-projet-binome-2-kickoff.md`](../2026-06-04-projet-binome-2-kickoff.md). **Modèle direct** : la fiche du binôme #1 ([`exercises/projets-binome/projet-1-mini-domaine/`](../../../exercises/projets-binome/projet-1-mini-domaine/)) et sa spec [`2026-06-03-projet-binome-1-design.md`](2026-06-03-projet-binome-1-design.md).

## 1. Objectif

Produire la **fiche de projet** (livrable formateur, 100 % rédactionnel — pas de code, pas de prototype) du deuxième projet binôme de la Piscine ETNC, sur le thème « Gestionnaire de tâches CLI ». Le binôme conçoit l'application de A à Z ; l'évaluation est **manuelle**, guidée par une grille. C'est le premier projet binôme qui **charge/sauvegarde un fichier** et **lève/rattrape des exceptions** : le cœur pédagogique nouveau = **persistance + robustesse**.

## 2. Décisions validées (brainstorm)

| Sujet | Décision |
|---|---|
| Liberté de conception | **Cahier des charges fonctionnel** : on impose le *quoi* (entité + opérations + persistance + robustesse), le binôme décide du *comment* (signatures, découpage). |
| Modèle de la tâche | **`record Tache` immuable** (au minimum `id`, `titre`, `statut`). « Marquer fait » **reconstruit** la tâche (`new Tache(id, titre, FAITE)`) et la **remplace** dans la collection. |
| Statut | **`enum Statut { A_FAIRE, FAITE }`** (idiomatique, réutilise l'acquis enum M3 ; extensible). |
| Conteneur | **Une collection** imposée (`List<Tache>` ou `Map<Integer, Tache>`) — plus de tableaux à capacité fixe (≠ #1). |
| Persistance — format | **CSV à la main**, UTF-8 explicite, ligne `id,titre,statut`. **JSON/bibliothèque exclus** (offline, décision M5 §10.4). CSV simple **sans échappement** → contrainte documentée : le titre ne contient pas de virgule (idiome ch. 5-7). |
| Persistance — mécanique | **Charger au démarrage** (fichier absent → liste vide). **Sauvegarder** sur commande `sauver` **et** à `quitter`. Compteur d'`id` repris au-dessus du max chargé. |
| Robustesse au chargement | **Ligne CSV corrompue → avertissement + on ignore la ligne et on continue** (le programme démarre toujours). |
| Gestion d'erreurs | **≥ 1 exception custom unchecked imposée** (ex. `TacheIntrouvableException extends RuntimeException`) sur les cas métier, **rattrapée par le menu** ; `IOException` géré proprement. |
| Mode de démonstration | **Menu console interactif** (`Scanner`) + **preuve de persistance** (quitter, relancer, les tâches sont là). Jeu de commandes minimal imposé. |
| Collaboration / Git | **Git léger + répartition tracée** : commits réguliers des deux membres, README décrivant qui a fait quoi. Pas de branches/PR imposées (réservé M6). |
| Barème | **Total 20, seuil 12**, avec un nouveau critère **`persistance-robustesse`**. |
| Durée | **`duree_estimee_h: 14`** (vs 12 h au #1 ; +2 h pour la persistance/robustesse). |
| Slug du dossier | **`projet-2-persistance`** — déjà inscrit dans l'arbo de [`format-exercice.md`](../../format-exercice.md) (cohérent avec `projet-1-mini-domaine`). Le thème vit dans le **titre**. |
| Prototype interne | **Non** : faisabilité prouvée par l'exo **5.3.1 import-csv-personnel** (CSV → `List<record>` + exception chaînée). |

## 3. Contraintes structurantes

- **Antériorité (critère bloquant)** : le projet arrive après le module 5, il mobilise **M1 à M5**. La grande différence avec le #1 : les **trois verrous** (collections, exceptions, I/O fichier) **sautent**.
  - ✅ Autorisé : tout M1–M3 (POO, encapsulation, héritage, polymorphisme, interfaces, **enums**, **records**, `sealed`) ; **collections & génériques** (`List`/`Map`/`ArrayList`/`HashMap`, `Comparator`, lambdas/streams — M4) ; **exceptions** (`try`/`catch`/`throw`, **custom unchecked** `extends RuntimeException`, chaînage `cause` — M5) ; **I/O fichier NIO.2** (`Path`/`Files`, `try-with-resources`, **UTF-8 explicite**) ; **CSV à la main** (`split`/`String.join`) ; E/S console (`Scanner`).
  - ❌ Interdit (vu plus tard / hors périmètre) : **JUnit écrit par le binôme comme livrable noté** et **Git avancé (branches/PR) comme objectif noté** = module 6 (#22) ; **JSON via bibliothèque** = la Piscine est offline / sans dépendance (décision M5 §10.4) → **la persistance se fait en CSV à la main**.
- **« Gestion d'erreurs propre » = avec exceptions** (≠ refus/correction sans `throw` du #1, qui était imposé par l'antériorité M3). C'est l'inversion d'idiome qui matérialise l'acquis M5.
- **Charte de rédaction** : vouvoiement, phrases courtes, pas d'humour, touche militaire bienvenue ([`charte-redaction.md`](../../charte-redaction.md)).
- **Lint / CI** : `scripts/lint-exercices.sh` ne parcourt que `exercises/module-*/`. `projets-binome/` est **ignoré nativement** (prouvé par le #1) → aucun changement de script, pas de `valider-solutions` (pas de solution). Vérifier tout de même la CI verte avant merge.

## 4. Vocabulaire disponible (issu de M4–M5, déjà éprouvé)

Matériau que le sujet recombine (référence, non imposé fichier par fichier) :

- **Records & enums** (M3, exos 3.4.1 `record Coordonnees`, 3.4.2 `enum Grade`) : modèle direct de `record Tache` + `enum Statut`.
- **Collections & `Comparator`** (M4, exos 4.1 annuaire/utilisateurs-uniques, 4.2 tri-soldats) : `List`/`Map`, tri par `Comparator` (bonus tri/filtre).
- **Streams & `Optional`** (M4, exos 4.3 filtrage/agregation/recherche-optional) : disponibles pour filtrer/agréger (bonus).
- **Exceptions custom + chaînage** (M5, exos 5.1.2 `EffectifInvalideException`, 5.1.1 saisie défensive) : modèle de `TacheIntrouvableException` et de la validation de saisie.
- **I/O NIO.2 + CSV à la main** (M5, exos 5.2 `Files.lines`/`Files.write`/`@TempDir`, 5.3.1 import-CSV-personnel, 5.3.2 export-CSV-trié) : **modèle direct** du chargement/sauvegarde CSV UTF-8 et du parsing ligne à ligne avec ligne corrompue gérée.

## 5. Livrables (la fiche de projet)

Cible : `exercises/projets-binome/projet-2-persistance/`. Structure imposée par §11 (identique au #1) :

```
projet-2-persistance/
├── sujet.md                  # cahier des charges fonctionnel
├── consignes-livraison.md    # Git léger, README, répartition
├── evaluation.yml            # grille /20, seuil 12 (avec persistance-robustesse)
├── metadata.yml              # binome: true, module: 5, duree_estimee_h: 14
└── exemples-rendus/
    └── .gitkeep              # note « rendus exemplaires ajoutés post-promo »
```

**Pas de `starter/`, `solution/`, `tests/`.** Page blanche pour le binôme ; évaluation manuelle.

### 5.1 `sujet.md`

Sections (charte : vouvoiement, phrases courtes, touche militaire) :

1. **Contexte** — une unité tient sa liste de tâches/consignes ; le binôme livre un outil CLI pour les saisir, suivre leur état, et **les retrouver au prochain lancement**.
2. **Objectifs pédagogiques** — application OO + **collections** (M4) + **persistance fichier CSV** (M5) + **gestion d'erreurs par exceptions** (M5).
3. **Entité imposée** (le *quoi* ; signatures et découpage libres) :
   - `Tache` — un **`record` immuable** avec au minimum : `id` (entier, **attribué par l'application**, sert à cibler une tâche), `titre` (non vide), `statut`.
   - `Statut` — un **`enum`** `{ A_FAIRE, FAITE }`.
   - Un **gestionnaire** (classe au choix, ex. `GestionnaireTaches` / `DepotTaches`) qui détient la **collection** (`List<Tache>` ou `Map<Integer, Tache>`), attribue les `id`, et porte charger/sauvegarder.
4. **Opérations obligatoires** :
   - **Ajouter** une tâche (titre → nouvel `id`, statut `A_FAIRE`).
   - **Lister** les tâches (avec `id` et statut visibles).
   - **Marquer fait** une tâche par son `id` (reconstruction du `record` + remplacement).
   - **Persistance** : **charger** au démarrage, **sauvegarder** l'état.
5. **Persistance (CSV à la main)** :
   - Fichier `taches.csv` (chemin par défaut fixe, **documenté dans le README**), **UTF-8 explicite**.
   - Format : une tâche par ligne, `id,titre,statut`. **CSV simple sans échappement** → le **titre ne contient pas de virgule** (contrainte explicite, idiome ch. 5-7).
   - **Charger** au démarrage : fichier **absent → liste vide** (premier lancement normal). Reprendre le compteur d'`id` **au-dessus du max chargé**.
   - **Sauvegarder** : sur commande `sauver` **et** automatiquement à `quitter`.
6. **Robustesse & gestion d'erreurs (avec exceptions)** :
   - **Ligne CSV corrompue** au chargement (colonnes manquantes, `id` non numérique, statut inconnu) → **message d'avertissement + on ignore la ligne et on continue** ; le programme démarre toujours.
   - **`fait <id>` sur un `id` inexistant** → **exception custom unchecked imposée** (ex. `TacheIntrouvableException extends RuntimeException`), **rattrapée par la boucle de menu** → message clair, le programme continue.
   - **Titre vide** à l'ajout → refus / exception rattrapée + message.
   - **Accès fichier** (`IOException` : droits, chemin) → message propre, pas de pile d'appels brute à l'écran.
7. **Contrainte d'antériorité** explicite (cf. §3) : rappel ✅ M1–M5 / ❌ JUnit & Git PR notés (M6), JSON/lib (→ CSV à la main).
8. **Démonstration attendue** : **menu console interactif** (`Scanner`) exposant au minimum `ajouter`, `lister`, `fait <id>`, `sauver`, `quitter` ; **preuve de persistance** : ajouter des tâches, `quitter`, **relancer** → les tâches sont rechargées.
9. **Bonus (non bloquants, valorisés)** : `supprimer <id>` ; priorité (`enum`) ou échéance ; **tri/filtre** via `Comparator`/streams (M4) ; statut intermédiaire `EN_COURS`.

### 5.2 `consignes-livraison.md`

- **Rendu via dépôt Git** ; commits réguliers **des deux membres** (l'historique sert de preuve d'équilibre). Pas de branches/PR imposées (réservé M6).
- **README attendu** : comment compiler/lancer (`javac`/`java`), **commandes du menu**, **où se trouve `taches.csv`** (chemin/encodage), **répartition du binôme** (qui a fait quoi).
- Binômes **imposés par les formateurs** (refléter `referentiel.md` §5).
- Préciser : arborescence de sources attendue (package `etnc`…), aucun `.class`/dossier de build versionné, échéance fixée par le formateur.

### 5.3 `evaluation.yml`

Grille **/20, seuil 12**, **sans critères automatiques** (`type: formateur` partout — la moulinette n'exécute pas) :

| id | description | poids | (#1) |
|---|---|---|---|
| `conception-oo` | `record`/`enum`, encapsulation du gestionnaire, immuabilité bien exploitée, collection à bon escient | **5** | 8 |
| `perimetre` | Opérations ajouter/lister/fait + menu conformes | **4** | 5 |
| `persistance-robustesse` | Charger/sauver CSV corrects (UTF-8), fichier absent géré, **ligne corrompue signalée + ignorée**, exceptions propres (**custom** + `IOException`) | **5** | — |
| `lisibilite` | Style, idiomatisme, méthodes courtes | **3** | 4 |
| `collaboration` | Équilibre des contributions (historique Git + README) | **3** | 3 |

Total : 5 + 4 + 5 + 3 + 3 = **20**, seuil **12**.

### 5.4 `metadata.yml`

```yaml
slug: projet-2-persistance
titre: "Projet binôme #2 — Gestionnaire de tâches CLI"
binome: true
module: 5
duree_estimee_h: 14
prerequis: ["4.1", "5.1", "5.2", "5.3"]
objectifs_pedagogiques:
  - "Concevoir une petite application OO avec collections (record, enum, gestionnaire)"
  - "Persister l'état dans un fichier CSV à la main (NIO.2, UTF-8)"
  - "Gérer les erreurs proprement avec des exceptions (custom unchecked + IOException)"
  - "Être robuste aux données corrompues (ligne CSV invalide ignorée)"
  - "Collaborer en binôme avec un historique Git équilibré"
notions:
  - classe
  - record
  - enum
  - collection
  - exception
  - fichier
  - csv
  - persistance
  - collaboration
auteur: "ETNC"
version: 1
date_creation: 2026-06-05
```

> ⚠️ À la rédaction : reprendre **exactement** les champs du `metadata.yml` du binôme #1 (`slug`, `titre`, `binome`, `module`, `duree_estimee_h`, `prerequis`, `objectifs_pedagogiques`, `notions`, `auteur`, `version`, `date_creation`). On **omet** les champs propres aux exercices individuels (`sous_groupe`/`position`/`difficulte`/`duree_estimee_min`).

### 5.5 `exemples-rendus/`

Dossier présent mais **vide au départ** : un `.gitkeep` portant une note « rendus exemplaires anonymisés ajoutés *post-promo* » (identique au #1).

## 6. Mise à jour de la documentation existante

- `docs/backlog.md` : marquer **#24** comme livré (statut + lien commit de clôture), comme l'a été le #1 sous #23.
- `docs/referentiel.md` §5 : le dossier `projet-2-persistance` n'est plus « à venir » (la phrase « Les projets 2 et 3 sont à venir » devient « Le projet 3 est à venir »).
- `docs/format-exercice.md` : l'arbo nomme **déjà** `projet-2-persistance` → **rien à changer**.

## 7. Cadence / livraison

- **Un seul cycle = une PR** `feature/projet-binome-2`, branchée depuis `main` à jour.
- Exécution **inline** (travail rédactionnel ; pas de workflow ultracode — ce n'est pas du code produit en masse).
- **CI verte** avant merge (lint inchangé ; build Docusaurus si le contenu touche le site — ici non). Pas de `valider-solutions` (pas de solution).

## 8. Hors périmètre (YAGNI)

- Aucun code Java livré ni prototype interne.
- Aucun `starter/` / `solution/` / `tests/`.
- Pas d'auto-grading ni de tests d'acceptation (évaluation strictement manuelle, conforme §11).
- Pas de JSON ni de bibliothèque de sérialisation (CSV à la main, offline).
- Pas de branches/PR imposées au binôme, ni de JUnit noté (réservé M6).
