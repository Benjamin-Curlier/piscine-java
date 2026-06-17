# Piscine Java — Objectif v1

> But : transformer la plateforme **v0.7.0** (cours de fondamentaux + moulinette) en une
> expérience d'onboarding **engageante** et **auto-explicative** qu'une nouvelle recrue peut
> démarrer en 5 minutes, suivre sans décrocher, et terminer en sachant ce qu'il lui reste à apprendre.

Chaque épic ci-dessous répond à un point précis de la critique UX, par ordre de **levier décroissant**.
Le « wow » (gamification + jeu capstone) est traité tôt car c'est la priorité motivation.

---

## EPIC A — Un feedback qui *enseigne* (corrige : erreurs sèches, compétences de debug)

- **A1.** La moulinette explique chaque échec : **attendu vs obtenu** (diff), le message d'assertion
  en clair, et **un indice** par checker. Fini le `MethodSource[...]` brut.  *(levier #1)*
- **A2.** Chapitre « Lire une stack trace & utiliser un débogueur ».

**DoD** : un exo échoué (ex. 1.1.2 avec un `main` vide) produit un rapport lisible par un débutant
(diff attendu/obtenu + indice), vérifié de bout en bout.

## EPIC B — Un démarrage sans friction (corrige : git-avant-feedback, double-contexte, arrêt au 1er échec)

- **B1.** Commande **`submit`** unique qui masque `branch`/`add`/`commit`/`push` (git utilisé de façon
  invisible d'abord, *enseigné* au module 6).
- **B2.** **Module 0 / Quickstart** : comment on rend et on exécute son code, **avant** le Java.
- **B3.** Noter **tous** les exos d'un sous-groupe et rapporter chacun (la progression reste séquentielle),
  au lieu de s'arrêter au premier échec.

**DoD** : un débutant rend 1.1.1 avec une seule commande `submit` ; le rapport d'un sous-groupe liste tous les exos.

## EPIC C — Wow & motivation (corrige : aspect terne / pas de wow)  ← priorité utilisateur

- **C1.** Moteur de **gamification** : XP par exo (selon difficulté), **niveaux**, **séries (streaks)**,
  **badges** (First Blood, Série x5, Maître de module, Tueur de mutants, Game Over = finir un jeu).
  Persisté dans `progress.json`, affiché sur le **tableau de bord**.
- **C2.** Jeu capstone **Snake** : cœur de logique **notable** (Plateau/Direction/Serpent/Pomme/collision/score)
  + rendu **jouable** (Swing). On apprend en construisant un *vrai jeu*.
- **C3.** *(stretch)* 2e jeu : Bomberman (grille + bombes) ou R-Type (cœur logique d'un shoot'em up).

**DoD** : terminer des exos octroie XP/badges visibles au tableau de bord ; le cœur de Snake **note vert** et est jouable.

## EPIC D — Conscient du métier (corrige : outils de build, manques de cursus, points fins, honnêteté du périmètre)

- **D1.** Chapitre court **Maven/Gradle** (les outils de build sont déjà imposés dès le jour 1).
- **D2.** Chapitres « points fins » : **enums**, **Optional**, **date/heure moderne** (`java.time`).
- **D3.** *(stretch)* **Module 7 — Concurrence** (threads, executors, immuabilité).
- **D4.** *(stretch)* **Module 8 — Persistance** (JDBC + SQLite, fichier).
- **D5.** **Périmètre honnête** dans le README + pointeur « la suite » (Spring/REST/BDD).

**DoD** : le README énonce le périmètre + la suite ; les nouveaux chapitres *buildent* sous Docusaurus ;
les exos des nouveaux modules **notent vert**.

---

## Definition of Done — v1

1. EPIC A, B, C (cœur) faits + D1, D2, D5 ; D3/D4/C3 si le temps le permet.
2. **Tout vérifié** : tous les exos notent vert (`valider-solutions`), moulinette `build` + E2E verts,
   site Docusaurus *builde*, `lint-exercices` 0 erreur.
3. Livré en **PR** sur `main` pour revue (jamais auto-mergé).

## Protocole de boucle (auto-rythmée, ~6 h)

- Tout sur la branche **`v1`**. Un item à la fois, **vérifié**, **commité seulement si vert**.
- `docs/v1/PROGRESS.md` tient le journal d'itérations + la checklist.
- Arrêt quand la DoD est atteinte **ou** le budget ~6 h écoulé.
