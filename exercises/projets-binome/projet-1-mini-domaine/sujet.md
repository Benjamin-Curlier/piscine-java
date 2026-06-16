# Projet binôme #1 — Gestion d'une entreprise

## Contexte

Une entreprise héberge plusieurs équipes. Chaque équipe accueille des membres,
dans la limite de sa capacité. Chaque membre a un niveau de séniorité. La
direction a besoin d'un petit logiciel pour affecter les membres aux équipes,
gérer leurs promotions et consulter les effectifs.

Vous concevez ce logiciel **en binôme**, de A à Z. Le sujet impose **ce que** le
programme doit faire ; vous restez libres du **comment** (noms de classes,
signatures, découpage).

## Objectifs pédagogiques

- Concevoir un mini-domaine orienté objet : entités, encapsulation, agrégation.
- Réutiliser un `enum` pour modéliser un ensemble fini (les niveaux).
- Gérer des ensembles d'objets avec des **tableaux à capacité fixe**.
- Traiter les cas invalides par **refus ou correction**, sans exception.
- Collaborer en binôme avec un historique Git équilibré.

## Entités imposées

Vous devez modéliser au minimum les entités suivantes. Les attributs listés sont
un minimum ; les signatures précises et le découpage interne sont à votre
initiative.

- **Niveau** — un `enum` des niveaux de séniorité, du plus bas au plus haut. Vous
  pouvez reprendre la progression vue en module 3 : `STAGIAIRE`, `JUNIOR`,
  `CONFIRME`, `SENIOR`, `LEAD`. L'ordre des constantes définit le
  « niveau suivant » utilisé pour les promotions.
- **Membre** — possède au minimum un nom et un niveau.
- **Equipe** — possède un nom et un ensemble de membres borné par une
  **capacité maximale fixe**. L'ensemble est géré par un **tableau** (pas de
  `List` ni de `ArrayList`).
- **Entreprise** — possède un ensemble d'équipes, géré lui aussi par un **tableau**.

## Opérations obligatoires

1. **Affecter** un membre à une équipe.
2. **Promouvoir** un membre : il passe au niveau immédiatement supérieur.
3. **Lister** l'effectif : par équipe et/ou pour l'ensemble de l'entreprise.

## Règles métier (sans exception)

Les cas invalides ne doivent **jamais** lever d'exception (`throw` interdit). Vous
les traitez par **refus ou correction**, avec un message en console et une valeur
de retour explicite (par exemple un `boolean` indiquant le succès) :

- Affecter un membre à une **équipe pleine** : l'affectation est refusée.
- Promouvoir un membre déjà au **niveau maximal** : la promotion est refusée.

## Contrainte d'antériorité (importante)

Ce projet n'utilise que les notions des **modules 1 à 3**.

- Autorisé : classes, encapsulation, constructeurs, héritage, polymorphisme,
  classes abstraites, interfaces, `enum`, `record`, `sealed` ; tableaux ;
  méthodes et membres `static` ; entrées/sorties **console**
  (`Scanner`, `System.out`).
- Interdit : collections et génériques (`List`, `Map`, `ArrayList`…) et
  lambdas/streams (module 4) ; exceptions (`try`/`catch`/`throw`) et
  entrées/sorties fichier (module 5).

Pour gérer les ensembles (membres d'une équipe, équipes d'une entreprise), vous
utilisez des **tableaux à capacité fixe**.

## Démonstration attendue

Le programme expose un **menu console interactif** (à l'aide de `Scanner`). Le
formateur lance le programme et saisit des commandes. Votre menu doit au minimum
proposer :

- `affecter` — affecter un membre à une équipe ;
- `promouvoir` — promouvoir un membre ;
- `lister` — afficher l'effectif ;
- `quitter` — terminer le programme.

Ces commandes minimales sont **imposées** : elles permettent au formateur
d'évaluer chaque rendu sur la même base.

## Bonus (facultatifs, valorisés)

Ces extensions ne sont pas obligatoires mais sont valorisées dans la note de
conception :

- Une hiérarchie de collaborateurs : une classe abstraite `Collaborateur` et ses
  sous-classes `Manager`, `Senior`, `Developpeur`, avec une méthode
  `salaire()` redéfinie (héritage et polymorphisme).
- Le calcul du salaire total d'une équipe.
- Le tri de l'effectif par niveau.
