# Exercice 1.3.2 — Suite de Fibonacci (itérative)

## Contexte

La suite de Fibonacci apparaît dans de nombreux domaines. Chaque terme est la somme des deux précédents : 0, 1, 1, 2, 3, 5, 8, 13, 21… Vous allez la calculer **sans récursivité**, simplement avec une boucle.

## Énoncé

Complétez la méthode `main` pour :

1. Afficher `Quel rang de la suite de Fibonacci ?` (déjà fait dans le squelette).
2. Lire un rang `n` (un entier).
3. Calculer **de manière itérative** (avec une boucle) le n-ième terme `F(n)`, sachant que `F(0) = 0` et `F(1) = 1`.
4. Afficher `F(<n>) = <valeur>`.

Conseil : gardez deux variables de type `long` (le terme précédent et le terme courant) et faites-les « glisser » à chaque tour.

## Exemple

**Exécution attendue** (l'utilisateur saisit `10`) :

```text
Quel rang de la suite de Fibonacci ?
F(10) = 55
```

## Contraintes

- La classe doit s'appeler `FibonacciIteratif` et rester dans le package `piscine.m1`.
- Calcul **itératif** uniquement (pas de récursivité, non vue à ce stade).
- Utilisez `long` pour la valeur calculée (les termes grandissent vite).
- `F(0) = 0`, `F(1) = 1`.

## Ce qui sera vérifié

- Votre programme **compile** sans erreur.
- `F(n)` est correct, **y compris les cas de base** `F(0)` et `F(1)`.
- La sortie respecte le format attendu.

## Pour aller plus loin (optionnel — non noté)

- Jusqu'à quel rang `n` un `int` suffirait-il avant de déborder ? Et un `long` ?
- Afficheriez-vous toute la suite jusqu'à `F(n)` plutôt que seulement le dernier terme ?
