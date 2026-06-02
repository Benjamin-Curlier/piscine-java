# Correction — Exercice 2.3.3 Calculs géométriques (refactor)

## Démarche attendue

1. Reprendre les formules du cercle de l'exercice 1.2.2 et les placer dans deux
   méthodes `static` : `aire(double rayon)` et `perimetre(double rayon)`.
2. Ajouter les versions rectangle, à **deux** paramètres : `aire(double, double)`
   et `perimetre(double, double)`.
3. Utiliser `Math.PI` pour le cercle.

## Points clés

- **Refactor** : extraire un calcul de `main` vers une méthode le rend
  réutilisable et testable indépendamment.
- **Surcharge par nombre de paramètres** : `aire(double)` et `aire(double, double)`
  coexistent car elles n'ont pas le même nombre de paramètres.
- **Pas de carré séparé** : `aire(double cote)` aurait la même signature que
  `aire(double rayon)` — Java refuserait. Le carré se traite comme un rectangle
  `aire(cote, cote)`.

## Erreurs fréquentes observées

- Écrire `Math.PI * rayon` au lieu de `Math.PI * rayon * rayon` pour l'aire.
- Tenter de créer `aire(double cote)` pour le carré → erreur de compilation
  (signature en double).
- Approximer π à `3.14` au lieu d'utiliser `Math.PI`.

## Variantes possibles

- Faire appeler `aire(cote, cote)` par une méthode dédiée au carré nommée
  différemment (`aireCarre`).
- Ajouter le triangle via `aire(double base, double hauteur)` — mais attention,
  cette signature entre en collision avec le rectangle : il faudrait un nom
  distinct.

## Pour approfondir

- La différence entre **surcharge** (même nom, signatures différentes) et
  **redéfinition** (override), qui viendra avec l'héritage au module suivant.
