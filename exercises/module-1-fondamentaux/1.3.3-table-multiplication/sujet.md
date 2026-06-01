# Exercice 1.3.3 — Table de multiplication

## Contexte

Afficher une table de multiplication est un grand classique pour s'entraîner aux boucles. C'est simple, visuel, et le résultat se vérifie d'un coup d'œil.

## Énoncé

Complétez la méthode `main` pour :

1. Afficher `Quelle table ?` (déjà fait dans le squelette).
2. Lire un entier `n`.
3. Afficher la table de multiplication de `n`, de 1 à 10, **une ligne par produit**, au format `n x i = résultat`.

## Exemple

**Exécution attendue** (l'utilisateur saisit `7`) :

```text
Quelle table ?
7 x 1 = 7
7 x 2 = 14
7 x 3 = 21
7 x 4 = 28
7 x 5 = 35
7 x 6 = 42
7 x 7 = 49
7 x 8 = 56
7 x 9 = 63
7 x 10 = 70
```

## Contraintes

- La classe doit s'appeler `TableMultiplication` et rester dans le package `etnc.m1`.
- La table va toujours de 1 à 10.
- Le format de chaque ligne doit être exactement `n x i = résultat` (avec les espaces autour du `x` et du `=`).

## Ce qui sera vérifié

- Votre programme **compile** sans erreur.
- La table est correcte et complète (1 à 10), y compris pour `0` et les nombres négatifs.
- Le format de chaque ligne est respecté.

## Pour aller plus loin (optionnel — non noté)

- Afficheriez-vous une ligne de séparation ou un titre avant la table ?
- Comment afficheriez-vous toutes les tables de 1 à 10 (indice : une boucle dans une boucle — vous verrez cela au module 2) ?
