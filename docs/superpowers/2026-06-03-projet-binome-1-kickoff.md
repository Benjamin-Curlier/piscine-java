# #23 — Projet binôme #1 (« Caserne », mini-domaine OO) : brief de démarrage

> **But de ce document** : permettre à une **nouvelle session** de rédiger la fiche du **Projet binôme #1** sans re-découvrir le contexte. Ce n'est PAS une spec ni un plan : c'est le point de départ du cycle habituel **brainstorm → spec → plan → exécution**, en **un seul cycle = une PR**, exécution **inline** (pas de sous-agents — contrainte projet, voir mémoire).
>
> Lire d'abord : [`docs/format-exercice.md`](../format-exercice.md) **§11 (projets binôme)**, [`docs/referentiel.md`](../referentiel.md) **§5 (projets binôme)** + §module 3, [`docs/charte-redaction.md`](../charte-redaction.md), [`docs/grille-evaluation.md`](../grille-evaluation.md), et le kickoff/spec des exos M3 ([`2026-06-03-m3-exos-kickoff.md`](2026-06-03-m3-exos-kickoff.md)) pour les conventions de nommage et l'antériorité.

## 0. Où on en est (au 2026-06-03)

- **Module 3 (POO) ENTIÈREMENT LIVRÉ** : 10 chapitres (`courses/docs/module-3-poo/`) + 14 exercices (`exercises/module-3-poo/`, sous-groupes 3.1→3.4, PR #29/#30/#31/#32 mergées). Le **vocabulaire OO** est donc posé et stabilisé : `Soldat`, `Personnel` (officier/sous-off/MdR), enum `Grade`, records, `sealed`, interfaces, héritage/polymorphisme. C'est le matériau du projet.
- **#19 clos** → **débloque le Projet binôme #1** (#23). C'est le **PROCHAIN CHANTIER**.
- **Le format du projet binôme est DÉJÀ cadré** par [`format-exercice.md`](../format-exercice.md) §11 (voir §3 ci-dessous) : structure de fichiers fixée, **évaluation manuelle**, **pas de moulinette**, **pas de starter/solution/tests figés**.
- Brancher depuis `main` à jour. Conventions Git / revue : [`CONTRIBUTING.md`](../../CONTRIBUTING.md).

## 1. 🔑 Le point structurant : ce n'est PAS un exercice

Tout le volet exercices reposait sur : un `starter/`, une `solution/` de référence, des tests publics/privés JUnit, une **moulinette qui exécute et note automatiquement**. **Le projet binôme est l'inverse** (décision figée §11) :

- **Le binôme conçoit le projet de A à Z** : pas de `starter/` ni d'API imposée fichier par fichier.
- **La moulinette n'exécute pas** : l'évaluation est **manuelle**, guidée par `evaluation.yml` (grille pédagogique, dont des critères de **collaboration**).
- **Notre livrable de formateur n'est donc PAS du code** mais une **fiche de projet** : énoncé, consignes de livraison, grille d'évaluation, métadonnées (+ emplacement pour des rendus exemplaires *post-promo*).

> **Conséquence pour la conception** : le travail de cette session est essentiellement **rédactionnel et pédagogique** (cadrer un sujet motivant, réaliste, faisable avec M1–M3 seulement, et une grille juste), pas du code Java. C'est LE sujet du brainstorm.

## 2. Périmètre — le domaine « Caserne » (referentiel §5)

Sujet de référence (referentiel §5, Projet 1) : **Gestion d'une caserne** — soldats, grades, unités, opérations simples (**affecter, promouvoir, lister**). Thème : **conception orientée objet d'un mini-domaine**.

**Antériorité = contrainte FORTE (critère bloquant)** : le projet arrive après le module 3, donc il ne peut mobiliser que **M1 à M3**. À garder constamment en tête lors de la rédaction du sujet :

- ✅ Autorisé : classes, encapsulation, constructeurs, héritage, polymorphisme, classes abstraites, interfaces, enums, records, `sealed` ; **tableaux** (M2) ; méthodes/`static` (M2) ; E/S **console** (`Scanner`/`System.out`, M1) ; validation **par correction/refus sans exception** (idiome 3.2).
- ❌ Interdit (vu plus tard) : **collections & génériques** (`List`, `Map`, `ArrayList`…) et **lambdas/streams** = **M4** ; **exceptions** (`try`/`catch`/`throw`) et **I/O fichier** = **M5** ; **JUnit écrit par le binôme** & **Git avancé/PR** comme objectif noté = **M6**.

> ⚠️ **Point de conception majeur à brainstormer** : « lister » et « gérer un ensemble de soldats » **sans collections** → il faut s'appuyer sur des **tableaux** (capacité fixe, ou tableau redimensionné à la main). Le sujet doit rendre ce choix naturel (ex. effectif maximal d'une unité). De même, « gérer les erreurs » se fait **par correction/refus** (pas d'exception), comme en 3.2.

## 3. Contraintes héritées (format §11 — déjà figé)

Cible : `exercises/projets-binome/projet-1-mini-domaine/`. Structure **imposée** :

```
projets-binome/projet-1-mini-domaine/
├── sujet.md                  # énoncé du projet (contexte caserne, objectifs, périmètre, livrables)
├── metadata.yml              # + champ binome: true et duree_estimee_h (pas duree_estimee_min)
├── consignes-livraison.md    # branches Git, commits, README attendu du binôme
├── exemples-rendus/          # 1–2 rendus exemplaires anonymisés — AJOUTÉS post-promo (vide au départ)
└── evaluation.yml            # rubrique avec critères de collaboration en plus
```

- **Pas de `starter/`, `solution/`, `tests/`** : le binôme part d'une page blanche.
- **Évaluation manuelle** guidée par `evaluation.yml` (la moulinette n'exécute pas).
- **Charte** : vouvoiement, phrases courtes, pas d'humour, touche militaire bienvenue ([`charte-redaction.md`](../charte-redaction.md)).
- **Grille** : s'inspirer de [`grille-evaluation.md`](../grille-evaluation.md) mais **adapter** au projet (critères de conception OO + collaboration ; le barème `total`/`seuil_reussite` est à trancher).
- ⚠️ **Lint** : `scripts/lint-exercices.sh` valide aujourd'hui le format **11 fichiers des exercices individuels** (il a vérifié 36 exos M1–M3). Vérifier s'il **ignore** `projets-binome/` ou s'il faut l'**adapter** (sinon la CI pourrait casser). À traiter dans la spec/plan.

## 4. Ce que cette session doit PRODUIRE (livrables formateur)

Une **fiche de projet** complète (4 fichiers + 1 dossier) :

1. **`sujet.md`** — le cœur : contexte « caserne », objectifs pédagogiques (conception OO), **périmètre fonctionnel** (entités + opérations affecter/promouvoir/lister), contraintes techniques (M1–M3 only, tableaux, console, sans exception), livrables attendus du binôme, et comment **démontrer** que le programme tourne (un `main` de démonstration ? un scénario imposé ?).
2. **`consignes-livraison.md`** — modalités : dépôt Git (un repo/branche), rythme de commits, **README** attendu (comment compiler/lancer, répartition du binôme), format de rendu.
3. **`evaluation.yml`** — grille adaptée : conception OO (encapsulation, héritage/polymorphisme/abstraction à bon escient), lisibilité/idiomatisme, respect du périmètre, **collaboration** (équilibre du binôme, historique Git). Barème à trancher.
4. **`metadata.yml`** — `binome: true`, `duree_estimee_h`, module de rattachement (3), prerequis, objectifs, notions.
5. **`exemples-rendus/`** — dossier présent mais **vide au départ** (rendus exemplaires ajoutés *post-promo*) ; prévoir un `.gitkeep` + une note.

> Pas de code Java livré. **Option à trancher au brainstorm** : produit-on, en interne (non livré au binôme), une **solution de référence** ou des **tests d'acceptation** pour que le formateur valide la faisabilité du sujet ? §11 dit « évaluation manuelle » et « pas de solution » dans l'arbo livrée — mais un prototype hors-repo peut sécuriser le périmètre.

## 5. Questions ouvertes à trancher au brainstorm

- **Périmètre fonctionnel exact** : quelles entités (`Soldat`, `Grade` (enum), `Unite`, `Caserne` ?), quelles opérations minimales vs optionnelles (affecter à une unité, promouvoir = changer de grade, lister l'effectif, …) ? Jusqu'où imposer une API vs laisser la conception libre (un projet binôme = **plus de liberté de conception** qu'un exercice) ?
- **Sans collections** : effectif géré par **tableau** (capacité max imposée ? redimensionnement manuel ?). Le sujet doit cadrer ce choix sans le rendre pénible.
- **Sans exceptions** : opérations invalides (promouvoir au-delà du grade max, affecter à une unité pleine) gérées par **refus/correction** (idiome 3.2) — à expliciter.
- **Démonstration d'exécution** : `main` interactif (menu console) ? scénario scripté ? Comment le formateur « voit » que ça marche, puisque la moulinette n'exécute pas.
- **Auto-grading partiel ?** Garde-t-on **strictement manuel** (conforme §11) ou ajoute-t-on des **tests d'acceptation** optionnels pour aider le formateur ? (cf. §4, option).
- **Volet collaboration / Git** : Git formel est au M6 → quel niveau exiger ici (commits réguliers + README + répartition) sans en faire un objectif M6 prématuré ? Comment le noter dans `evaluation.yml` ?
- **Barème** : `total`, `seuil_reussite`, poids des critères (conception / lisibilité / périmètre / collaboration). Aligner ou non sur le `20` des exercices.
- **Durée** (`duree_estimee_h`) et **modalités binôme** (les binômes sont imposés par les formateurs — info à refléter dans `consignes-livraison.md`).
- **Lint/CI** : adapter `lint-exercices.sh` (ou l'exclure de `projets-binome/`) pour ne pas casser la CI.

## 6. Premiers pas de la nouvelle session

- [ ] Lire les docs en tête + ce brief + `format-exercice.md` §11 + `referentiel.md` §5.
- [ ] `git checkout main && git pull` ; brancher `feature/projet-binome-1` (ou `docs/...` selon la nature — c'est de la doc pédagogique).
- [ ] **Brainstorm** (skill `superpowers:brainstorming`) focalisé sur §1 (ce n'est pas un exercice), §2 (antériorité M1–M3) et §5 (questions ouvertes) — surtout : périmètre fonctionnel, gestion sans collections/exceptions, mode de démonstration, grille + collaboration.
- [ ] Spec : `docs/superpowers/specs/<date>-projet-binome-1-design.md`, puis plan `plans/<date>-projet-binome-1.md`.
- [ ] Exécuter **inline** : rédiger les 4 fichiers + `exemples-rendus/.gitkeep`, vérifier la CI (lint adapté), PR unique.

## 7. Cadence recommandée

1. **Un seul cycle = une PR** (`feature/projet-binome-1`), branchée depuis `main` à jour.
2. Travail surtout **rédactionnel** : pas de `valider-solutions` à faire passer (pas de solution), mais **CI verte** (lint adapté + build Docusaurus si le sujet est lié au site) avant merge.
3. MAJ [`docs/backlog.md`](../backlog.md) (#23) et `referentiel.md` §5 (le dossier n'est plus « à venir »).

---

*Brief préparé le 2026-06-03 en clôture du module 3 (chapitres + 14 exercices livrés, PR #25→#32). Référence backlog : #23 (Projet binôme #1). Format figé : [`format-exercice.md`](../format-exercice.md) §11. Sujet de référence : [`referentiel.md`](../referentiel.md) §5 (« Gestion d'une caserne »).*
