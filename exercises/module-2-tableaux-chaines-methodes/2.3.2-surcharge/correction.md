# Correction — Exercice 2.3.2 Surcharge

## Démarche attendue

- `maximum(int, int)` : renvoyer le plus grand des deux, par exemple avec
  `Math.max(a, b)` ou un `if`/ternaire.
- `maximum(int, int, int)` : le plus simple est de **réutiliser** la version à
  deux entiers : `maximum(maximum(a, b), c)`.
- `maximum(double, double)` : même logique que la première, mais sur des `double`.

## Points clés

- **Surcharge** : trois méthodes de même nom coexistent car leurs paramètres
  diffèrent (par le nombre ou par le type). Java choisit la bonne à la compilation.
- **Réutilisation** : faire appeler une surcharge par une autre évite de répéter
  la logique de comparaison.
- **Pas d'affichage** : on renvoie la valeur ; c'est l'appelant qui décide quoi
  en faire.

## Erreurs fréquentes observées

- Donner des noms différents aux méthodes (`maximum2`, `maximum3`) → ce n'est
  plus de la surcharge.
- Recopier trois fois la même comparaison au lieu de réutiliser.
- Renvoyer un `int` dans la version `double` (perte de précision).

## Variantes possibles

- Comparer sans `Math.max`, avec un opérateur ternaire : `return (a > b) ? a : b;`.
- Pour trois entiers, une cascade de `if` est correcte mais plus verbeuse.

## Pour approfondir

- La résolution de surcharge en Java et les conversions implicites (`int` vers
  `double`).
