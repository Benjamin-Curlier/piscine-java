# Rétrospective — Module 1 pilote

> Bilan de la construction du **module 1 (Fondamentaux)**, premier module complet de la Piscine : **7 chapitres + 10 exercices**. Sert de gabarit aux modules 2 à 6.
> Objectif : figer ce qui marche, corriger les formats avant de produire en masse, et ré-estimer la charge des modules suivants.

## 1. Ce qui a été produit

| Volet | Livrables | Référence |
|-------|-----------|-----------|
| Chapitres | 7 (`1-1` à `1-7`) | `courses/docs/module-1-fondamentaux/` |
| Exercices | 10 (sous-groupes 1.1, 1.2, 1.3) | `exercises/module-1-fondamentaux/` |
| Outillage validant | Checkers réels + `valider-solutions` en CI | #43–#51, #11b |

Toutes les solutions de référence passent leurs tests (publics + privés) via le job CI `valider-solutions`. La **boucle vertueuse** visée — écrire l'exo, la moulinette valide immédiatement sa solution — a fonctionné.

## 2. Ce qui a bien marché (à reconduire)

- **Un sous-groupe = une PR.** Le module 1 a été livré en cycles courts (1.1, 1.2, 1.3 séparés). Revue plus facile, CI ciblé, moins de conflits.
- **La solution de référence comme test du format.** Brancher chaque `solution/` dans `valider-solutions` a attrapé les exos cassés *avant* le stagiaire. C'est le filet le plus rentable.
- **E/S standard + tout dans `main`.** Cohérent avec l'antériorité des notions (pas de méthodes/objets avant leur chapitre). À garder pour le module 1 ; à relâcher dès que le module concerné introduit les méthodes/classes.
- **Utilitaire de test partagé `CaptureEntree`** (capture de `System.in`/`System.out`) sous `etnc.util` : réutilisable, a évité de dupliquer la plomberie de test dans chaque exo à saisie clavier.
- **Graine fixe pour l'aléatoire** (`Random(1789)` fourni dans le starter de 1.3.4) : rend l'exercice testable de façon déterministe sans retirer l'aspect « jeu ».
- **Entrée clavier insensible à la locale** (pas de séparateur décimal) : a supprimé une classe entière de faux échecs liés au `,` vs `.`.

## 3. Ajustements de format à propager

### `docs/format-exercice.md`
- **`tests-prives/` confirmé facultatif** pour les exercices les plus simples : 1.1.2 (`affichage-formate`) en est dépourvu, ce qui est légitime (toute la valeur est dans la sortie exacte, déjà couverte par les tests publics à poids 16). Garder cette exception explicite — ne pas forcer des tests privés artificiels.
- **`id` des critères `type: formateur` normalisés** (via #13) : ils doivent désormais correspondre à une grille de [`grille-evaluation.md`](grille-evaluation.md) (`demarche`, `lisibilite`, `idiomatisme`, `respect-consignes`). Le module 1 a servi de banc d'essai : la plupart des exos utilisent `demarche`, deux `respect-consignes` (1.1.1/1.1.2), un `lisibilite` (1.2.3). **À appliquer dès la conception** pour les modules suivants, pas en rattrapage.
- **Pondération du critère humain stable à 2/20.** Suffisant pour le module 1 (l'essentiel se joue sur les tests). À réévaluer quand les exercices deviendront plus ouverts (modules 4+).

### `docs/charte-redaction.md`
- La structure de chapitre (§6) a tenu sur les 7 chapitres sans accroc — **aucun changement nécessaire**.
- Confirmer dans la charte la règle « **une notion n'apparaît jamais avant son chapitre** » : elle a structuré tout le module (E/S avant méthodes, etc.) et mérite d'être un critère de revue explicite (déjà repris dans `CONTRIBUTING.md` §6).

### Outillage
- `StyleChecker` reste **advisory** (non bloquant) pour la beta (#47/#53). Le module 1 n'a pas permis de trancher sur le passage en bloquant : il faut des retours stagiaires réels. Décision reportée à #53.

## 4. Temps passé et ré-estimation des modules 2-6

> ⚠️ Les colonnes « durée stagiaire » viennent des `metadata.yml`. Le **temps de production formateur** ci-dessous est une **estimation à valider/corriger par le formateur** (donnée non instrumentée pendant le build).

### Exercices — durée stagiaire estimée (metadata)

| Exercice | Difficulté | Durée stagiaire |
|----------|-----------|-----------------|
| 1.1.1 hello-world | très-facile | 10 min |
| 1.1.2 affichage-formate | très-facile | 10 min |
| 1.1.3 lecture-saisie | facile | 15 min |
| 1.2.1 conversion-unites | facile | 15 min |
| 1.2.2 calculs-geometriques | facile | 15 min |
| 1.2.3 manipulation-booleenne | moyen | 20 min |
| 1.3.1 fizzbuzz | facile | 20 min |
| 1.3.2 fibonacci-iteratif | moyen | 25 min |
| 1.3.3 table-multiplication | facile | 15 min |
| 1.3.4 devine-le-nombre | moyen | 30 min |
| **Total module 1** | — | **~175 min** |

### Production formateur — estimation à valider

| Type de livrable | Temps de production estimé (à ajuster) |
|------------------|----------------------------------------|
| Chapitre de cours | ___ (proposition : 1 h 30 – 2 h 30 selon densité) |
| Exercice simple (très-facile/facile) | ___ (proposition : 1 h – 1 h 30) |
| Exercice moyen | ___ (proposition : 2 h – 3 h, surtout pour les tests privés) |

**Projection modules 2-6** (à recalculer une fois les temps réels remplis) :

| Module | Chapitres | Exercices |
|--------|-----------|-----------|
| 2 | 5 | 12 |
| 3 | 10 | 14 |
| 4 | 8 | 12 |
| 5 | 7 | 10 |
| 6 | 8 | 7 |
| **Total** | **38** | **55** |

À multiplier par les temps unitaires une fois confirmés, pour donner une charge réaliste à la hiérarchie.

## 5. Décision

**On continue à l'identique**, avec les ajustements ci-dessus déjà intégrés au processus (pas de refonte de format) :

1. **Process reconduit** : un sous-groupe = une PR ; solution de référence validée en CI ; antériorité des notions comme critère de revue.
2. **À appliquer dès la conception** (et non en rattrapage) : IDs de critères `formateur` alignés sur les grilles ; `tests-prives/` omis seulement quand justifié.
3. **À trancher plus tard** : passage du style en bloquant (#53, après retours beta) ; pondération du critère humain pour les exercices ouverts (modules 4+).
4. **Action formateur** : remplir les temps réels de production (§4) pour fiabiliser l'estimation des modules 2-6 avant de lancer #18.

---

*Version 1 — 2026-06-01. Rétrospective du module pilote. À relire au démarrage de #18 (module 2).*
