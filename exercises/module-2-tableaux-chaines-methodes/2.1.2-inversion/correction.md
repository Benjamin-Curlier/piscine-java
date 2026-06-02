# Correction — Exercice 2.1.2 Inversion d'un tableau

## Démarche attendue

1. Lire `n`, créer et remplir le tableau (comme en 2.1.1).
2. Parcourir le tableau **de la dernière case (`n-1`) vers la première (`0`)**.
3. Construire la ligne de sortie au fur et à mesure, en n'ajoutant un espace
   qu'entre deux valeurs (pas après la dernière).

## Points clés

- **Sens de parcours** : la boucle décrémente l'indice (`for (int i = n-1; i >= 0; i--)`).
- **Séparateur** : ajouter l'espace seulement si `i > 0` évite l'espace final.
  (Les tests tolèrent un espace final, mais une sortie propre est préférable.)

## Erreurs fréquentes observées

- Boucler de `0` à `n-1` puis afficher → on n'inverse rien.
- Oublier le retour à la ligne final.
- Décaler les indices (`n` au lieu de `n-1`) → `ArrayIndexOutOfBoundsException`.

## Variantes possibles

- **Inversion en place** : échanger `valeurs[i]` et `valeurs[n-1-i]` pour `i`
  allant de `0` à `n/2`, puis afficher dans l'ordre normal.
- Utiliser `String.join` après avoir converti les entiers en chaînes.

## Pour approfondir

- Documentation de `StringBuilder` (construction efficace de chaînes).
