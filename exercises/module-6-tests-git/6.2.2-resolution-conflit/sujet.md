# Exercice 6.2.2 — Résolution de conflit

## Contexte

Un **conflit de fusion** survient quand deux branches modifient la même partie d'un fichier.
Git ne choisit pas à votre place : il insère des **marqueurs** dans le fichier et vous laisse
décider. Résoudre un conflit, c'est éditer le fichier pour ne garder que ce qu'on veut,
supprimer les marqueurs, puis valider.

Ici, le dépôt-jouet est déjà dans un état de conflit (un `git merge` a échoué sur
`README.md`). Vous pilotez Git depuis Java via `GitCommandes` ; votre résolution est évaluée
sur l'état final du dépôt.

## Énoncé

Complétez `resoudreConflit(GitCommandes git)`. Le fichier `README.md` contient un conflit :

```text
fonctionnalités:
- login
<<<<<<< HEAD
- import
=======
- export
>>>>>>> feature
```

Vous devez :

1. Lire `README.md` (`Files.readAllLines(git.repo().resolve("README.md"), ...)`).
2. **Retirer les trois lignes de marqueurs** (`<<<<<<<`, `=======`, `>>>>>>>`) en **conservant
   les deux contributions** (`- import` **et** `- export`).
3. Réécrire le fichier, puis `git add README.md` et `git commit` pour **conclure la fusion**.

## Exemple

Après votre résolution, `README.md` doit ressembler à :

```text
fonctionnalités:
- login
- import
- export
```

…sans aucun marqueur, et la fusion doit être conclue par un commit de merge.

## Contraintes

- Aucun marqueur de conflit ne doit subsister.
- Les **deux** contributions (`import` et `export`) sont conservées.
- La fusion est conclue par un `git commit` (un commit de merge, à deux parents).

## Ce qui sera vérifié

- Plus aucun marqueur dans `README.md` ; l'arbre de travail est propre.
- `import` **et** `export` présents dans le fichier final.
- La fusion est conclue par un **commit de merge** (deux parents).

## Pour aller plus loin (optionnel)

- Provoquez un conflit à la main au terminal (`git merge`) pour voir les marqueurs en vrai.
