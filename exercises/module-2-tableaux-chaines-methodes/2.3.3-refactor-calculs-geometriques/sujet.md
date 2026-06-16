# Exercice 2.3.3 — Calculs géométriques (refactor)

## Contexte

Dans l'exercice 1.2.2, vous calculiez l'aire et le périmètre d'un cercle
directement dans `main`. Vous allez maintenant **réorganiser** ce code en
méthodes réutilisables, et les étendre au rectangle grâce à la surcharge.

## Énoncé

Complétez les quatre méthodes `static` de la classe `CalculsGeometriques`. **Ne
modifiez pas leurs signatures.** Les méthodes à un paramètre concernent le
**cercle**, celles à deux paramètres le **rectangle**.

1. `public static double aire(double rayon)` — aire d'un cercle : `π × rayon²`.
2. `public static double aire(double largeur, double hauteur)` — aire d'un
   rectangle : `largeur × hauteur`.
3. `public static double perimetre(double rayon)` — périmètre d'un cercle :
   `2 × π × rayon`.
4. `public static double perimetre(double largeur, double hauteur)` — périmètre
   d'un rectangle : `2 × (largeur + hauteur)`.

Utilisez `Math.PI`.

## Exemple

```java
CalculsGeometriques.aire(2.0)         // π × 4  ≈ 12.566...
CalculsGeometriques.aire(3.0, 4.0)    // 12.0
CalculsGeometriques.perimetre(2.0)    // 2π × 2 ≈ 12.566...
CalculsGeometriques.perimetre(3.0, 4.0) // 14.0
```

## Contraintes

- La classe doit s'appeler `CalculsGeometriques` et rester dans le package `piscine.m2`.
- **Les quatre signatures sont imposées** : la surcharge cercle / rectangle est
  évaluée.
- Utilisez `Math.PI` (n'écrivez pas une valeur approchée de π).
- Les méthodes renvoient leur résultat ; elles n'affichent rien.

## Ce qui sera vérifié

- Les aires et périmètres du cercle sont corrects (rayon 1 et 2).
- Les aires et périmètres du rectangle sont corrects (y compris un carré vu
  comme rectangle).
- La surcharge fonctionne : Java distingue les versions cercle et rectangle.

## Pour aller plus loin (optionnel — non noté)

- Pourquoi ne peut-on pas ajouter une méthode `aire(double cote)` pour le carré ?
  (indice : elle aurait la même signature que `aire(double rayon)`)
- Comment ajouteriez-vous le **triangle** (base et hauteur) sans casser la
  surcharge existante ?
