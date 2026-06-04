# #24 — Projet binôme #2 (« TaskManager CLI », app + persistance fichier) : brief de démarrage

> **But de ce document** : permettre à une **nouvelle session** de rédiger la fiche du **Projet binôme #2** sans re-découvrir le contexte. Ce n'est PAS une spec ni un plan : c'est le point de départ du cycle habituel **brainstorm → spec → plan → exécution**, en **un seul cycle = une PR**, exécution **inline** (travail rédactionnel ; pas de workflow ultracode nécessaire — c'est de la doc pédagogique, pas du code à produire en masse).
>
> Lire d'abord : [`docs/format-exercice.md`](../format-exercice.md) **§11 (projets binôme)**, [`docs/referentiel.md`](../referentiel.md) **§5 (projets binôme)** + §module 5, [`docs/charte-redaction.md`](../charte-redaction.md), [`docs/grille-evaluation.md`](../grille-evaluation.md), et surtout le **jumeau** [`2026-06-03-projet-binome-1-kickoff.md`](2026-06-03-projet-binome-1-kickoff.md) + la fiche livrée du binôme #1 ([`exercises/projets-binome/projet-1-mini-domaine/`](../../exercises/projets-binome/projet-1-mini-domaine/)) — **modèle direct** de ce qu'il faut produire.

## 0. Où on en est (au 2026-06-04)

- **Module 5 (Exceptions et I/O) ENTIÈREMENT LIVRÉ** : 7 chapitres (`courses/docs/module-5-exceptions-io/`) + **10 exercices** (`exercises/module-5-exceptions-io/`, sous-groupes 5.1→5.3, **PR #43→#46 mergées dans `main`**). Tout l'outillage du projet est donc posé : **exceptions** (custom unchecked, chaînage), **I/O fichier NIO.2** (`Path`/`Files`, `try-with-resources`, UTF-8), **CSV à la main** (`split`/`String.join`), **collections/streams/`Comparator`** (M4).
- **#21 clos** → **débloque le Projet binôme #2** (#24). C'est le **PROCHAIN CHANTIER** (avant le module 6, #22).
- **Le format du projet binôme est DÉJÀ cadré** par [`format-exercice.md`](../format-exercice.md) §11 et **prouvé** par le binôme #1 : structure de fichiers fixée, **évaluation manuelle**, **pas de moulinette**, **pas de starter/solution/tests figés**. Le binôme #1 est passé en CI (lint + jobs) → **le chemin `projets-binome/` est déjà géré** (pas de nouvelle bascule lint/CI à craindre ; vérifier tout de même la CI verte avant merge).
- Brancher depuis `main` à jour. Conventions Git / revue : [`CONTRIBUTING.md`](../../CONTRIBUTING.md).

## 1. 🔑 Le point structurant : ce n'est PAS un exercice (identique au binôme #1)

Comme le binôme #1, et **à l'inverse des exercices** : pas de `starter/`, pas de `solution/` de référence, pas de tests publics/privés, **la moulinette n'exécute pas**. Le binôme conçoit l'app **de A à Z** ; l'évaluation est **manuelle**, guidée par `evaluation.yml` (grille pédagogique avec critères de **collaboration**).

> **Notre livrable de formateur n'est PAS du code** mais une **fiche de projet** (4 fichiers + 1 dossier). Le travail de cette session est **rédactionnel et pédagogique** : cadrer un sujet motivant, réaliste, faisable avec **M1–M5**, et une grille juste. C'est LE sujet du brainstorm.

## 2. Périmètre — « Gestionnaire de tâches CLI » (referentiel §5)

Sujet de référence (referentiel §5, Projet 2) : **Gestionnaire de tâches CLI** — *ajouter, lister, marquer fait, persistance CSV ou JSON, gestion d'erreurs propre*. Thème : **application OO + persistance fichier**.

### 🔼 LA grande différence avec le binôme #1 : l'antériorité monte à **M1–M5**

Le binôme #1 (après M3) **interdisait** collections, exceptions et I/O fichier. **Le binôme #2 lève ces trois verrous** — c'est ce qui en fait une vraie petite application :

- ✅ **Désormais autorisé** (acquis M4–M5, à exploiter) :
  - **Collections & génériques** : `List`/`Map`/`ArrayList`/`HashMap`, `Comparator`, lambdas/streams (M4) → fini les tableaux à capacité fixe du #1.
  - **Exceptions** : `try`/`catch`/`throw`, exceptions **custom unchecked** (`extends RuntimeException`), chaînage `cause` (M5) → la « gestion d'erreurs propre » du referentiel se fait **avec** exceptions (≠ refus/correction du #1).
  - **I/O fichier NIO.2** : `Path`/`Files` (`readAllLines`, `writeString`, `write`+`APPEND`), `try-with-resources`, **UTF-8 explicite** ; **persistance CSV à la main** (`split`/`String.join`) (M5).
  - Tout M1–M3 (POO, enums, records, console `Scanner`).
- ❌ **Toujours interdit** (vu plus tard, ou hors périmètre Piscine) :
  - **JUnit écrit par le binôme** comme livrable **noté**, et **Git avancé / branches / PR** comme **objectif noté** = **module 6** (#22). Le binôme *utilise* git pour collaborer, mais n'est pas évalué sur un workflow PR.
  - **JSON via bibliothèque** : la Piscine est **offline / sans dépendance** (décision M5 §10.4 : JSON = survol en cours, **aucun exo, aucune lib**). ⇒ **la persistance se fait en CSV à la main** (le « ou JSON » du referentiel est tranché en faveur du CSV, cohérent avec le module 5). À confirmer au brainstorm.

> **Conséquence conception** : c'est le premier projet binôme qui **charge/sauvegarde un fichier** et **lève/rattrape des exceptions**. Le cœur pédagogique nouveau = **persistance + robustesse** (charger au démarrage, sauvegarder, gérer un fichier absent/corrompu, une saisie invalide).

## 3. Contraintes héritées (format §11 — déjà figé, prouvé par le binôme #1)

Cible : `exercises/projets-binome/projet-2-<slug>/` (slug à choisir, ex. `projet-2-taskmanager-cli`). Structure **imposée** (identique au #1) :

```
projets-binome/projet-2-<slug>/
├── sujet.md                  # énoncé (contexte, objectifs, périmètre fonctionnel, persistance, livrables)
├── metadata.yml              # binome: true, duree_estimee_h, module: 5, prerequis, objectifs, notions
├── consignes-livraison.md    # dépôt Git, commits équilibrés, README attendu du binôme
├── exemples-rendus/          # 1–2 rendus exemplaires anonymisés — AJOUTÉS post-promo (vide au départ + .gitkeep)
└── evaluation.yml            # rubrique formateur (critères conception + persistance/robustesse + collaboration)
```

- **Pas de `starter/`, `solution/`, `tests/`** : page blanche pour le binôme.
- **Évaluation manuelle** guidée par `evaluation.yml` (la moulinette n'exécute pas).
- **Charte** : vouvoiement, phrases courtes, pas d'humour, touche militaire bienvenue.
- **Grille** : partir de celle du binôme #1 (`conception-oo` 8 / `perimetre` 5 / `lisibilite` 4 / `collaboration` 3 = 20, seuil 12) mais **l'ADAPTER** : ajouter/peser un critère **persistance & robustesse** (chargement/sauvegarde corrects, fichier absent/corrompu géré, exceptions propres). Re-trancher les poids.
- **Lint/CI** : le binôme #1 prouve que `projets-binome/` passe la CI. Refaire le même check avant merge (build/lint verts). Pas de `valider-solutions` (pas de solution).

## 4. Ce que cette session doit PRODUIRE (livrables formateur)

Une **fiche de projet** complète (calquée sur le #1) :

1. **`sujet.md`** — le cœur : contexte (gestionnaire de tâches en ligne de commande), objectifs pédagogiques (OO + collections + **persistance fichier** + **gestion d'erreurs par exceptions**), **entités** (au moins une `Tache`), **opérations imposées** (ajouter, lister, marquer fait, + persistance charger/sauvegarder), **format de persistance** (CSV à la main, encodage UTF-8), **règles de robustesse** (fichier absent au 1er lancement → liste vide ; ligne corrompue → erreur gérée proprement ; saisie invalide → exception rattrapée + message), **démonstration** (menu console `Scanner`, relancer prouve la persistance), bonus facultatifs.
2. **`consignes-livraison.md`** — dépôt Git, commits équilibrés (preuve de contribution), **README** (compiler/lancer, commandes du menu, **où est le fichier de données**, répartition du binôme). Branches/PR **non exigées** (M6) — à confirmer.
3. **`evaluation.yml`** — grille adaptée : `conception-oo`, `perimetre` (opérations conformes), **`persistance-robustesse`** (nouveau, poids à trancher), `lisibilite`, `collaboration`. Total/seuil à trancher (aligner sur 20/12 ?).
4. **`metadata.yml`** — `binome: true`, `duree_estimee_h` (le #1 = 12 h ; #2 plus riche avec la persistance → ~14–16 h, à trancher), `module: 5`, `prerequis` (sous-groupes 5.1–5.3, voire 4.x), `objectifs_pedagogiques`, `notions` (classe, collection, exception, fichier, csv, persistance, collaboration).
5. **`exemples-rendus/`** — dossier présent mais **vide au départ** (`.gitkeep` + note ; rendus exemplaires ajoutés *post-promo*).

> Pas de code Java livré. **Option à trancher (comme au #1)** : produit-on, **en interne (non livré)**, un **prototype de référence** (un petit TaskManager CSV qui tourne) pour valider que le périmètre est faisable et la persistance CSV réaliste ? Recommandé ici plus qu'au #1, car la persistance + les exceptions ont des pièges (format CSV, fichier absent, encodage) — un prototype hors-`exercises/` sécurise le sujet. À décider au brainstorm.

## 5. Questions ouvertes à trancher au brainstorm

- **Entité `Tache` — champs** : `titre` (obligatoire) ; `fait`/statut (booléen ou enum `Statut{A_FAIRE, FAITE}`) ; un **identifiant** pour cibler une tâche au menu (numéro d'ordre ? id incrémental ?) ; champs optionnels (priorité via `enum`, échéance, description) ? Minimal imposé vs bonus.
- **Conteneur** : `List<Tache>` (M4) — imposer `List`/`Map` ou laisser libre ? Un `record Tache(...)` (immuable) + remplacement, ou une classe mutable (`marquerFait()`) ? (impacte le « marquer fait »).
- **Persistance — format & mécanique** : **CSV à la main** confirmé (pas de JSON/lib) ? Nom/chemin du fichier (`taches.csv` fixe ? configurable ?). **Quand charger** (au démarrage ; absent → vide) et **quand sauvegarder** (à chaque modif ? à `quitter` ? commande `sauver` explicite ?). En-tête CSV ? Échappement (CSV simple sans virgule dans un champ, comme 5-7) → contrainte sur les titres ?
- **Gestion d'erreurs (avec exceptions, M5)** : quels cas lèvent/rattrapent — id de tâche inexistant, titre vide, **ligne CSV corrompue au chargement** (exception + message, on ignore la ligne ou on stoppe ?), `IOException` (droits/chemin). Exception(s) **custom** (`TacheIntrouvableException` ? `FichierTachesInvalideException` ?) **unchecked** + chaînage ? Comment la grille note « gestion d'erreurs propre ».
- **Menu CLI imposé** : socle minimal commun pour évaluer tous les rendus pareil (ex. `ajouter`, `lister`, `fait <id>`, `supprimer <id>` ?, `sauver` ?, `quitter`). Lesquelles **imposées** vs **bonus** ?
- **Démonstration** : menu console interactif (`Scanner`) + **preuve de persistance** (quitter, relancer, les tâches sont là). Scénario imposé pour le formateur ?
- **Collaboration / Git** : rester au niveau #1 (commits équilibrés + README, **pas** de PR exigée — Git formel = M6) ou exiger un cran de plus ? Comment le noter.
- **Barème** : `total`/`seuil_reussite` (aligner sur 20/12 ?), poids des critères en intégrant **persistance-robustesse** (probablement au détriment du poids `perimetre`/`conception` du #1).
- **Durée** (`duree_estimee_h`) et rappel **binômes imposés par les formateurs** (à refléter dans `consignes-livraison.md`).
- **Slug du dossier** (`projet-2-taskmanager-cli` ? `projet-2-gestionnaire-taches` ?) et **module de rattachement** (5).

## 6. Premiers pas de la nouvelle session

- [ ] Lire les docs en tête + ce brief + `format-exercice.md` §11 + `referentiel.md` §5 + la **fiche binôme #1** (modèle).
- [ ] `git checkout main && git pull` ; brancher `feature/projet-binome-2` (doc pédagogique).
- [ ] **Brainstorm** (skill `superpowers:brainstorming`) focalisé sur §1 (ce n'est pas un exercice), §2 (antériorité **M1–M5**, les 3 verrous levés, JSON exclu → CSV) et §5 (questions ouvertes) — surtout : périmètre fonctionnel, **modèle de persistance CSV**, **gestion d'erreurs par exceptions**, menu imposé, grille + critère persistance/robustesse.
- [ ] (Recommandé) **prototype interne** non livré pour valider la faisabilité de la persistance CSV + des exceptions.
- [ ] Spec : `docs/superpowers/specs/<date>-projet-binome-2-design.md`, puis plan `plans/<date>-projet-binome-2.md`.
- [ ] Exécuter **inline** : rédiger les 4 fichiers + `exemples-rendus/.gitkeep`, CI verte (lint déjà OK pour `projets-binome/`), **PR unique**.

## 7. Cadence recommandée

1. **Un seul cycle = une PR** (`feature/projet-binome-2`), branchée depuis `main` à jour.
2. Travail surtout **rédactionnel** : pas de `valider-solutions` (pas de solution), mais **CI verte** (lint + build Docusaurus le cas échéant) avant merge.
3. MAJ [`docs/backlog.md`](../backlog.md) (#24) et `referentiel.md` §5 (le dossier projet 2 n'est plus « à venir »).

---

*Brief préparé le 2026-06-04 en clôture du Module 5 (#21 : 7 chapitres + 10 exercices livrés, PR #43→#46 mergées). Référence backlog : #24 (Projet binôme #2). Format figé : [`format-exercice.md`](../format-exercice.md) §11. Modèle direct : le binôme #1 ([kickoff](2026-06-03-projet-binome-1-kickoff.md) + [fiche](../../exercises/projets-binome/projet-1-mini-domaine/)). Sujet de référence : [`referentiel.md`](../referentiel.md) §5 (« Gestionnaire de tâches CLI »). Pré-requis : #21 (clos).*
