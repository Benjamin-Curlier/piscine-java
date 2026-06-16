# Exercice 2.3.1 — Bibliothèque mathématique

## Contexte

Plutôt que de réécrire les mêmes calculs partout, on les regroupe dans des
méthodes réutilisables. Vous allez constituer une petite bibliothèque de trois
fonctions mathématiques.

## Énoncé

Complétez les trois méthodes `static` de la classe `BiblioMaths`. **Ne modifiez
pas leur signature.** Chaque méthode **renvoie** un résultat (avec `return`) ;
elle n'affiche rien.

1. `public static int pgcd(int a, int b)` — renvoie le plus grand commun
   diviseur de `a` et `b` (entiers `≥ 0`). Par exemple `pgcd(12, 18)` vaut `6`.
2. `public static boolean estPremier(int n)` — renvoie `true` si `n` est un
   nombre premier, `false` sinon. Par convention, tout `n` inférieur à 2 n'est
   pas premier.
3. `public static int sommeChiffres(int n)` — renvoie la somme des chiffres de
   `n` (`n ≥ 0`). Par exemple `sommeChiffres(123)` vaut `6`.

## Exemple

Pour ces appels :

```java
BiblioMaths.pgcd(12, 18)      // 6
BiblioMaths.estPremier(7)     // true
BiblioMaths.sommeChiffres(123) // 6
```

## Contraintes

- La classe doit s'appeler `BiblioMaths` et rester dans le package `piscine.m2`.
- **Les signatures des trois méthodes sont imposées** : ne les changez pas.
- Les méthodes renvoient leur résultat ; elles n'affichent rien et ne lisent rien.

## Ce qui sera vérifié

- `pgcd` est correct, y compris pour des nombres premiers entre eux, des
  multiples, et `pgcd(0, n)`.
- `estPremier` traite correctement `0`, `1`, `2`, un grand premier et un composé.
- `sommeChiffres` fonctionne sur `0` et sur des nombres à plusieurs chiffres.

## Pour aller plus loin (optionnel — non noté)

- Pour `estPremier`, pourquoi suffit-il de tester les diviseurs jusqu'à la
  **racine carrée** de `n` ?
- Renseignez-vous sur l'**algorithme d'Euclide** : pourquoi `pgcd(a, b)` est-il
  égal à `pgcd(b, a % b)` ?
