# Correction — Exercice 2.4.1 Factorielle et puissance

## Démarche attendue

- **factorielle** : cas de base `n <= 1` qui renvoie `1` ; cas récursif
  `n * factorielle(n - 1)`. À chaque appel, `n` diminue et finit par atteindre
  le cas de base.
- **puissance** : cas de base `exposant == 0` qui renvoie `1` ; cas récursif
  `base * puissance(base, exposant - 1)`. L'exposant décroît vers 0.

## Points clés

- **Cas de base obligatoire** : sans lui, la récursion ne s'arrête jamais
  (débordement de pile).
- **Réduction vers le cas de base** : chaque appel doit traiter un problème
  strictement plus petit (`n - 1`, `exposant - 1`).
- **Type `long`** : `13!` vaut 6 227 020 800, au-delà de la capacité d'un `int`
  (~2,1 milliards). Renvoyer un `long` évite le débordement silencieux.

## Erreurs fréquentes observées

- Oublier le cas de base → `StackOverflowError`.
- Cas de base `n == 0` seulement : correct, mais `n <= 1` est plus robuste.
- Déclarer le retour en `int` → résultat faux (négatif) dès `factorielle(13)`.
- Écrire une boucle : le résultat serait juste, mais la consigne « récursif »
  n'est pas respectée (critère formateur).

## Variantes possibles

- `puissance` par exponentiation rapide (récursivité sur `exposant / 2`) : plus
  efficace, hors périmètre ici.
- `factorielle` itérative : correcte mathématiquement mais hors consigne.

## Pour approfondir

- La pile d'exécution (call stack) et la limite de profondeur de récursion.
