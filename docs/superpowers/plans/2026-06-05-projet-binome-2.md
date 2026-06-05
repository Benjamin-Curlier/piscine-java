# Projet binôme #2 « Gestionnaire de tâches CLI » — Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Rédiger la fiche de projet (livrable formateur, 100 % rédactionnel) du Projet binôme #2 « Gestionnaire de tâches CLI » dans `exercises/projets-binome/projet-2-persistance/`, conforme au format §11 et à la charte.

**Architecture:** Aucun code Java. On crée 5 fichiers (4 fichiers de fiche + un `.gitkeep`) dans un nouveau dossier `exercises/projets-binome/projet-2-persistance/`, puis on met à jour deux docs existantes (`backlog.md`, `referentiel.md`). L'évaluation est manuelle ; la CI (lint) ignore nativement `projets-binome/`. Une seule PR `feature/projet-binome-2`. Contenu autoritatif = spec [`2026-06-05-projet-binome-2-design.md`](../specs/2026-06-05-projet-binome-2-design.md) §5.

**Tech Stack:** Markdown + YAML. Charte : vouvoiement, phrases courtes, pas d'humour, touche militaire. Référence format : `docs/format-exercice.md` §11. Modèle direct : la fiche du binôme #1 (`exercises/projets-binome/projet-1-mini-domaine/`).

---

## Contexte hérité (à NE PAS redécouvrir)

- **Décisions de design** : voir spec §2. Résumé : `record Tache` immuable (`id`/`titre`/`statut`) + remplacement ; `enum Statut{A_FAIRE, FAITE}` ; conteneur = collection ; persistance **CSV à la main** UTF-8 ; charger au démarrage / sauver sur `sauver` + à `quitter` ; ligne corrompue → avertir + ignorer + continuer ; **≥1 exception custom unchecked imposée** ; barème /20 seuil 12 avec `persistance-robustesse` ; durée 14 h ; slug `projet-2-persistance` ; pas de prototype.
- **Antériorité M1–M5.** Les trois verrous du #1 sautent : ✅ collections/génériques, lambdas/streams (M4) ; exceptions, I/O fichier NIO.2, CSV à la main (M5). ❌ JUnit noté & Git PR noté (M6) ; JSON via bibliothèque (offline → CSV à la main).
- **Inversion d'idiome vs #1** : la « gestion d'erreurs propre » se fait désormais **avec** exceptions (≠ refus/correction sans `throw`).
- **Vocabulaire disponible** (exos M3–M5 déjà livrés, à référencer sans imposer fichier par fichier) : `record`/`enum` (3.4), collections/`Comparator` (4.1/4.2), streams/`Optional` (4.3), exceptions custom + chaînage (5.1), I/O NIO.2 + CSV à la main (5.2/5.3.1/5.3.2).
- **Lint CI** : `scripts/lint-exercices.sh` ne parcourt que `exercises/module-*/` → `projets-binome/` ignoré, **aucun changement de script** (prouvé par le #1).

## File Structure

| Fichier | Responsabilité |
|---|---|
| `exercises/projets-binome/projet-2-persistance/sujet.md` | Cahier des charges fonctionnel (contexte, entité Tache/Statut, opérations, persistance CSV, robustesse/exceptions, menu, démo, bonus) |
| `exercises/projets-binome/projet-2-persistance/consignes-livraison.md` | Modalités Git léger, README attendu (dont **où est `taches.csv`**), répartition binôme |
| `exercises/projets-binome/projet-2-persistance/evaluation.yml` | Grille manuelle /20, seuil 12 (avec `persistance-robustesse`) |
| `exercises/projets-binome/projet-2-persistance/metadata.yml` | Métadonnées binôme (module 5, durée 14 h) |
| `exercises/projets-binome/projet-2-persistance/exemples-rendus/.gitkeep` | Dossier vide + note post-promo |
| `docs/backlog.md` (modif) | Marquer #24 livré |
| `docs/referentiel.md` (modif) | §5 : projet 2 disponible |

---

### Task 1: `sujet.md` — cahier des charges fonctionnel

**Files:** Create `exercises/projets-binome/projet-2-persistance/sujet.md`

- [ ] **Step 1** : rédiger selon spec §5.1 (9 sections : Contexte, Objectifs, Entité imposée `Tache`/`Statut`/gestionnaire, Opérations, Persistance CSV, Robustesse & exceptions, Antériorité, Démonstration, Bonus). Modèle de ton/structure = `projet-1-mini-domaine/sujet.md`.
- [ ] **Step 2** : relire charte (vouvoiement, phrases courtes, pas d'humour). Vérifier : rien d'interdit demandé (pas de JUnit noté, pas de JSON/lib, pas de Git PR noté) ; exceptions présentées comme l'idiome attendu ; CSV simple sans virgule documenté.
- [ ] **Step 3** : commit `docs(#24): sujet.md du Projet binôme #2 (cahier des charges TaskManager CLI)`.

### Task 2: `consignes-livraison.md`

**Files:** Create `exercises/projets-binome/projet-2-persistance/consignes-livraison.md`

- [ ] **Step 1** : rédiger selon spec §5.2. Calqué sur le #1 + ajout README « où se trouve `taches.csv` (chemin/encodage) ».
- [ ] **Step 2** : relire charte ; Git léger sans branches/PR imposées ; binômes imposés par les formateurs.
- [ ] **Step 3** : commit `docs(#24): consignes de livraison du Projet binôme #2`.

### Task 3: `evaluation.yml` — grille manuelle /20

**Files:** Create `exercises/projets-binome/projet-2-persistance/evaluation.yml`

- [ ] **Step 1** : créer avec ce contenu exact :

```yaml
total: 20
seuil_reussite: 12
criteres:
  - id: conception-oo
    description: "Conception OO : record Tache immuable, enum Statut, encapsulation du gestionnaire, collection à bon escient, immuabilité bien exploitée"
    poids: 5
    type: formateur
  - id: perimetre
    description: "Périmètre fonctionnel : opérations ajouter/lister/marquer-fait et menu console conformes"
    poids: 4
    type: formateur
  - id: persistance-robustesse
    description: "Persistance et robustesse : charger/sauvegarder en CSV (UTF-8) corrects, fichier absent géré, ligne corrompue signalée puis ignorée, erreurs gérées par exceptions propres (custom unchecked + IOException)"
    poids: 5
    type: formateur
  - id: lisibilite
    description: "Lisibilité et idiomatisme : noms clairs, méthodes courtes, code propre"
    poids: 3
    type: formateur
  - id: collaboration
    description: "Collaboration : contributions équilibrées (historique Git) et README décrivant la répartition"
    poids: 3
    type: formateur
```

- [ ] **Step 2** : vérifier 5 + 4 + 5 + 3 + 3 = 20 (= `total`), tous `type: formateur`, structure identique aux `evaluation.yml` des exos.
- [ ] **Step 3** : commit `docs(#24): grille d'évaluation manuelle du Projet binôme #2`.

### Task 4: `metadata.yml`

**Files:** Create `exercises/projets-binome/projet-2-persistance/metadata.yml`

- [ ] **Step 1** : créer avec ce contenu exact :

```yaml
slug: projet-2-persistance
titre: "Projet binôme #2 — Gestionnaire de tâches CLI"
binome: true
module: 5
duree_estimee_h: 14
prerequis:
  - "4.1"
  - "5.1"
  - "5.2"
  - "5.3"
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

- [ ] **Step 2** : vérifier `binome: true`, `module: 5`, `duree_estimee_h: 14` (pas `_min`), aucun champ d'exo individuel (`sous_groupe`/`position`/`difficulte`). Champs identiques au `metadata.yml` du #1.
- [ ] **Step 3** : commit `docs(#24): metadata.yml du Projet binôme #2`.

### Task 5: `exemples-rendus/.gitkeep`

**Files:** Create `exercises/projets-binome/projet-2-persistance/exemples-rendus/.gitkeep`

- [ ] **Step 1** : contenu exact :

```text
# Ce dossier accueillera 1 ou 2 rendus exemplaires anonymisés.
# Il reste vide jusqu'à la première promotion (rendus ajoutés post-promo).
```

- [ ] **Step 2** : commit `docs(#24): dossier exemples-rendus (vide, rendus post-promo)`.

### Task 6: Mettre à jour `docs/backlog.md`

**Files:** Modify le bloc `### #24 — Projet binôme #2`.

- [ ] **Step 1** : remplacer la ligne `**Pré-requis** : #21` (+ ajouter une ligne) pour marquer la fiche livrée, sur le modèle du #23 :

```markdown
### #24 — Projet binôme #2 — App + persistance fichier (TaskManager CLI)
**Pré-requis** : #21
✅ **Fiche livrée** dans `exercises/projets-binome/projet-2-persistance/` (sujet, consignes, grille, métadonnées). Conception : spec [`specs/2026-06-05-projet-binome-2-design.md`](superpowers/specs/2026-06-05-projet-binome-2-design.md) + plan [`plans/2026-06-05-projet-binome-2.md`](superpowers/plans/2026-06-05-projet-binome-2.md).
```

- [ ] **Step 2** : aussi mettre à jour la ligne `#24` de la section roadmap Phase 3 (`- **#24 …**`) si présente, pour cohérence. Commit `docs(backlog): #24 fiche Projet binôme #2 livrée`.

### Task 7: Mettre à jour `docs/referentiel.md` §5

**Files:** Modify `docs/referentiel.md` ligne ~186.

- [ ] **Step 1** : texte actuel se termine par « … Le **Projet 1** est disponible : […]. Les projets 2 et 3 sont à venir. » Remplacer par :

```markdown
Chaque projet a son propre dossier dans [`exercises/`](../exercises/) avec sujet, critères d'évaluation, livrables attendus et grille de notation pédagogique. Les **Projets 1 et 2** sont disponibles : [`projet-1-mini-domaine/`](../exercises/projets-binome/projet-1-mini-domaine/) et [`projet-2-persistance/`](../exercises/projets-binome/projet-2-persistance/). Le projet 3 est à venir.
```

- [ ] **Step 2** : commit `docs(referentiel): §5 Projet binôme #2 disponible`.

### Task 8: Revue adversariale (ultracode) + vérification finale + PR

- [ ] **Step 1** : workflow de revue 3 agents (lentilles : conformité format §11 + charte ; antériorité M1–M5 + faisabilité ; cohérence interne barème/menu/metadata/liens). Appliquer les corrections confirmées.
- [ ] **Step 2** : `ls -R exercises/projets-binome/projet-2-persistance` → 5 fichiers attendus. `bash scripts/lint-exercices.sh` → PASS (ignore `projets-binome/`).
- [ ] **Step 3** : `git push -u origin feature/projet-binome-2` + `gh pr create` (base main). CI verte → merge.

---

## Self-Review (effectuée à la rédaction du plan)

**Spec coverage :** chaque livrable de la spec §5 a une tâche — sujet.md (T1), consignes (T2), evaluation.yml (T3), metadata.yml (T4), exemples-rendus (T5) ; MAJ doc §6 (T6, T7) ; revue/CI/PR §7 (T8). ✅
**Placeholders :** YAML exacts fournis (T3, T4) ; markdown renvoyé à la spec §5.1/§5.2 (autoritative, complète). ✅
**Cohérence :** ids/poids (`conception-oo` 5 / `perimetre` 4 / `persistance-robustesse` 5 / `lisibilite` 3 / `collaboration` 3 = 20) identiques entre spec et `evaluation.yml` ; `duree_estimee_h: 14` cohérent metadata/spec ; slug `projet-2-persistance` partout. ✅
**Adaptation ultracode :** production inline (5 fichiers interdépendants → cohérence préservée) + revue adversariale multi-agents (T8), pattern M4/M5. Justifié.
