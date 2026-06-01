# #18 — Module 2, volet EXERCICES : brief de démarrage

> **But de ce document** : permettre à une **nouvelle session** d'attaquer les exercices du module 2 sans re-découvrir le contexte. Ce n'est PAS une spec : c'est le point de départ du cycle habituel **brainstorm → spec → plan → exécution**, ici **un cycle par sous-groupe** (exécution **inline**, pas de sous-agents — contrainte projet, voir mémoire).
>
> Lire d'abord : [`docs/format-exercice.md`](../format-exercice.md), [`docs/charte-redaction.md`](../charte-redaction.md), [`docs/grille-evaluation.md`](../grille-evaluation.md), [`docs/referentiel.md`](../referentiel.md) §4 module 2, et la **spec chapitres** [`specs/2026-06-01-m2-chapitres-design.md`](specs/2026-06-01-m2-chapitres-design.md) (son §10 fige les décisions transverses ci-dessous).

## 0. Où on en est (au 2026-06-01)

- **Module 1 complet** (7 chapitres + 10 exercices). **Module 2 — chapitres faits et mergés** (PR #16, branche `main` à jour) : 5 chapitres `courses/docs/module-2-tableaux-chaines-methodes/`.
- **Reste pour #18** : les **12 exercices** en **4 sous-groupes**. Aucun fichier sous `exercises/module-2-tableaux-chaines-methodes/` n'existe encore.
- Brancher chaque sous-groupe depuis `main` à jour. Convention Git + revue : [`CONTRIBUTING.md`](../../CONTRIBUTING.md).

## 1. 🔒 Décisions DÉJÀ tranchées (ne pas re-brainstormer)

Issues du brainstorm du 2026-06-01 (chapitres). Consignées dans la spec chapitres §10. **À appliquer telles quelles** :

1. **Stratégie de test / antériorité « méthodes »** :
   - **2.1 (tableaux) et 2.2 (chaînes)** = **tout dans `main`**, E/S console, **testés sur la sortie standard** (modèle module 1, via l'util `CaptureSortie` / `CaptureEntree`). Le stagiaire n'écrit PAS ses propres méthodes : il lit des données et affiche un résultat.
   - **2.3 (méthodes) et 2.4 (récursivité)** = le stagiaire **écrit des méthodes `static`** dont la **signature est imposée** par le starter, **testées par valeur de retour** (AssertJ direct sur le résultat — plus simple et plus robuste que la sortie console).
2. **Récursivité (2.4)** : **imposée par la consigne** ; les tests ne vérifient que le **résultat** ; le respect de la forme récursive relève du critère formateur **`respect-consignes`** (pas de vérification statique de l'auto-appel).
3. **Refactor 2.3.3** : base = **`1.2.2-calculs-geometriques`** du module 1 → extraction en méthodes `static`, surcharge naturelle `aire`/`perimetre`.

## 2. Périmètre : 12 exercices en 4 sous-groupes

Cible : `exercises/module-2-tableaux-chaines-methodes/`. Nommage `2.S.E-slug/` (cf. format §1).

### 2.1 Tableaux (4) — *tout dans `main`, test sur sortie*
- **2.1.1 min-max-moyenne** — lire/parcourir un tableau, sortir min, max, moyenne.
- **2.1.2 inversion** — inverser l'ordre des éléments et afficher.
- **2.1.3 recherche-lineaire** — chercher une valeur, sortir son indice (ou « absent »).
- **2.1.4 rotation-matrice** — faire pivoter une matrice carrée (sens à préciser au brainstorm).

### 2.2 Chaînes (3) — *tout dans `main`, test sur sortie*
- **2.2.1 palindrome** — déterminer si une chaîne est un palindrome.
- **2.2.2 comptage-occurrences** — compter les occurrences d'un caractère/mot.
- **2.2.3 ascii-art** — formatage type « ASCII art » (forme exacte à cadrer au brainstorm).

### 2.3 Méthodes (3) — *méthodes `static` imposées, test par valeur de retour*
- **2.3.1 biblio-maths** — bibliothèque de méthodes mathématiques (signatures à définir : `pgcd`, `estPremier`, `puissance`… ?).
- **2.3.2 surcharge** — exercice centré sur la surcharge (même nom, paramètres différents).
- **2.3.3 refactor-calculs-geometriques** — refactor de `1.2.2` en méthodes `static` + surcharge `aire`/`perimetre`.

### 2.4 Récursivité (2) — *méthodes `static` imposées, récursivité imposée par consigne, test par valeur de retour*
- **2.4.1 factorielle-puissance** — `factorielle(n)` et `puissance(base, exposant)` en récursif.
- **2.4.2 parcours-matrice-recursif** — parcours/somme d'une matrice en récursif.

## 3. Contraintes de format (format-exercice.md + rétro #17)

- **11 fichiers** par exo (`sujet.md`, `metadata.yml`, `starter/`, `tests/`, `tests-prives/`, `solution/`, `correction.md`, `evaluation.yml`). `tests-prives/` **omis seulement si justifié** (cf. 1.1.2).
- **Package `etnc.m2`**, `groupId` `etnc.piscine.m2`. `pom.xml` du `starter` calqué sur module 1 ; `solution/pom.xml` injecte **tests + tests-prives** via `build-helper`.
- **`evaluation.yml`** : critère `type: formateur` avec `id` ∈ {`demarche`, `lisibilite`, `idiomatisme`, `respect-consignes`} (`grille-evaluation.md`). Poids cumulés = `total` (lint le vérifie). **Appliquer dès la conception**, pas en rattrapage. Pour **2.4**, prévoir `respect-consignes` (vérifie la forme récursive).
- **Réutiliser** : `CaptureSortie` / `CaptureEntree` (sous `etnc.util` dans `tests/`) pour 2.1/2.2 ; graine fixe pour tout aléatoire ; entrée clavier insensible à la locale.
- **Antériorité** : les exos n'utilisent que des notions vues (chapitres 2.1→2.5 + module 1). Cohérent avec le découpage test (2.1/2.2 sans méthodes maison).
- **Anti-spoil** déjà respecté côté chapitres : les exercices guidés ne donnent pas ces solutions (mapping spec §5.3).

## 4. Validation EN LOCAL (acquis depuis #18 chapitres)

Java 25 + Maven installés. Commandes vérifiées (exit 0) le 2026-06-01 :

```bash
# Valider UNE solution de référence en cours d'écriture
JAVA_HOME="E:/java/jdk-25.0.3+9" "E:/java/apache-maven-3.9.9/bin/mvn" \
  -f exercises/module-2-tableaux-chaines-methodes/2.1.1-<slug>/solution/pom.xml test

# Valider TOUTES les solutions (= job CI valider-solutions, en local)
JAVA_HOME="E:/java/jdk-25.0.3+9" MVN="E:/java/apache-maven-3.9.9/bin/mvn" bash scripts/valider-solutions.sh
```

`.m2` chaud (JUnit 5.11 / AssertJ 3.26) → fonctionne **offline**. **Valider chaque solution avant de pousser.** (Détails machine : mémoire `reference-env-setup`.)

## 5. Cadence recommandée (rétro #17)

- **1 sous-groupe = 1 PR**, branché depuis `main` après merge du précédent. Branches : `feature/m2-exos-2-1`, `-2-2`, `-2-3`, `-2-4`. Modèle : cycle #16 (exos module 1).
- **Ordre conseillé** : 2.1 → 2.2 → 2.3 → 2.4 (suit l'ordre des chapitres et l'antériorité : 2.3 a besoin du chapitre Méthodes, déjà en ligne).
- Chaque PR : **solution de référence validée en local** (§4) **+ CI vert** (4 jobs, dont `valider-solutions` et `lint-exercices`) avant push. MAJ `docs/backlog.md` (#18) à chaque sous-groupe.
- **La solution de référence est le test du format** (filet le plus rentable, rétro #17 §2).

## 6. Questions ouvertes à trancher au brainstorm (par sous-groupe)

À cadrer **au début du cycle de chaque sous-groupe** (pas tout d'un coup) :

- **2.1** : format d'entrée des tableaux (saisie clavier ligne d'entiers ? tableau en dur dans le starter ?). Sens de rotation de 2.1.4 (horaire/anti-horaire), matrice carrée seulement ?
- **2.2** : définition exacte de « palindrome » (ignorer casse/espaces ?). Périmètre de l'« ASCII art » de 2.2.3 (quel motif : rectangle, triangle, bannière ?).
- **2.3** : **liste précise et signatures** des méthodes de `biblio-maths` ; scénario de l'exo `surcharge` ; périmètre exact du refactor 2.3.3 (quelles méthodes extraire, quelles surcharges).
- **2.4** : signatures imposées (`long factorielle(int n)` ? `double puissance(double base, int exposant)` ?) ; ce que « parcours récursif » de 2.4.2 doit calculer (somme ? recherche ?). Comment formuler la consigne « doit être récursif ».
- **Transverse** : difficulté + `duree_estimee_min` de chaque exo (pour `metadata.yml`).
- **Action formateur en attente** : remplir les temps réels de production dans [`docs/retro-module-1.md`](../retro-module-1.md) §4 pour dimensionner la charge.

## 7. Premiers pas de la nouvelle session

- [ ] Lire les docs en tête + ce brief + la spec chapitres §10.
- [ ] `git checkout main && git pull` ; sanity-check `valider-solutions` local (§4).
- [ ] Choisir le sous-groupe (commencer par **2.1**).
- [ ] **Brainstorm** ciblé sur les questions §6 du sous-groupe (skill `superpowers:brainstorming`).
- [ ] Spec : `docs/superpowers/specs/<date>-m2-exos-2-1-design.md`. Plan : `docs/superpowers/plans/<date>-m2-exos-2-1.md`.
- [ ] Exécuter inline (les 4 exos du sous-groupe), valider en local + CI, PR unique, MAJ backlog.

---

*Brief préparé le 2026-06-01 après merge du volet chapitres (#18, PR #16). Référence backlog : #18.*
