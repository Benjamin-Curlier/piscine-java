# Correction — Exercice 2.4.2 Parcours récursif de matrice

## Démarche attendue

On décompose le problème en deux récursions :

1. **Somme d'une ligne** (récursif sur les colonnes) : `sommeLigne(ligne, j)` =
   `0` si `j` atteint la longueur de la ligne ; sinon
   `ligne[j] + sommeLigne(ligne, j + 1)`.
2. **Somme de la matrice** (récursif sur les lignes) : `sommeDepuisLigne(m, i)` =
   `0` si `i` atteint le nombre de lignes ; sinon
   `sommeLigne(m[i], 0) + sommeDepuisLigne(m, i + 1)`.

`sommeMatrice(m)` appelle simplement `sommeDepuisLigne(m, 0)`.

## Points clés

- **Deux cas de base** : « plus de colonne » et « plus de ligne ». Chacun arrête
  son niveau de récursion.
- **Méthodes auxiliaires** : la signature publique est imposée, mais on s'appuie
  sur des méthodes `private` récursives — c'est une décomposition naturelle.
- **Pas de boucle** : tout le parcours passe par les appels récursifs.

## Erreurs fréquentes observées

- Utiliser une boucle `for` : résultat correct mais consigne non respectée
  (critère formateur `respect-consignes`).
- Supposer la matrice carrée : utiliser `matrice.length` pour les lignes et
  `ligne.length` pour les colonnes gère le cas rectangulaire.
- Oublier un cas de base → `StackOverflowError` ou `ArrayIndexOutOfBoundsException`.

## Variantes possibles

- **Parcours par position `(i, j)`** : une seule méthode récursive qui avance
  `j`, puis passe à la ligne suivante quand `j` atteint le bout. Plus compact,
  plus délicat à écrire.
- Sommer via un index linéaire `0..L*C-1` en convertissant en `(i, j)`.

## Pour approfondir

- Récursivité sur des structures imbriquées (arbres) : même principe, le cas de
  base est la feuille.
