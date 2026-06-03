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
