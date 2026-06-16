# Exercice 2.4.2 — Parcours récursif de matrice

## Contexte

On peut parcourir une structure de données sans aucune boucle, uniquement par
récursivité : on traite un élément, puis on s'appelle soi-même sur le reste.
Vous allez sommer tous les éléments d'une matrice de cette façon.

## Énoncé

Complétez la méthode `static` de la classe `ParcoursMatrice`. **Ne modifiez pas
sa signature.**

- `public static int sommeMatrice(int[][] matrice)` — renvoie la somme de tous
  les éléments de la matrice. La matrice a au moins une ligne et une colonne, et
  peut être **rectangulaire**.

**Votre solution doit être récursive** : un **cas de base** et au moins un
**appel récursif**, sans boucle `for`/`while`. Vous pouvez (et il est conseillé
d')ajouter vos propres méthodes `private` récursives auxiliaires (par exemple
pour sommer une ligne).

## Exemple

```java
int[][] m = {{1, 2}, {3, 4}};
ParcoursMatrice.sommeMatrice(m)   // 10
```

## Contraintes

- La classe doit s'appeler `ParcoursMatrice` et rester dans le package `piscine.m2`.
- **La signature de `sommeMatrice` est imposée.**
- **La solution doit être récursive** (aucune boucle ; cas de base + appel
  récursif). Les méthodes auxiliaires `private` sont autorisées.
- La méthode renvoie son résultat ; elle n'affiche rien.

## Ce qui sera vérifié

- La somme est correcte sur des matrices carrées (2×2, 3×3), une matrice 1×1,
  des valeurs négatives, une matrice rectangulaire et une matrice à une ligne.
- La forme **récursive** est évaluée par le formateur (cas de base + appel
  récursif, pas de boucle).

## Pour aller plus loin (optionnel — non noté)

- Combien d'appels récursifs au total pour une matrice `L × C` ?
- Sauriez-vous écrire une version qui ne sépare pas « ligne » et « matrice »,
  mais parcourt récursivement une position `(i, j)` ?
