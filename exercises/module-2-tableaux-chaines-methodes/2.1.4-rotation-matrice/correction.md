# Correction — Exercice 2.1.4 Rotation d'une matrice

## Démarche attendue

1. Lire `n`, puis remplir un tableau à deux dimensions `int[n][n]` avec une
   double boucle (`i` les lignes, `j` les colonnes).
2. Pour la rotation horaire, la case `resultat[i][j]` vaut `source[n-1-j][i]` :
   la ligne `i` de la sortie est la colonne `i` de la source, lue de bas en haut.
3. Afficher ligne par ligne, valeurs séparées par un espace.

## Points clés

- **Formule de rotation** : `resultat[i][j] = source[n-1-j][i]` (horaire).
  La retenir, c'est comprendre que les colonnes deviennent des lignes.
- **Double boucle** : la boucle externe parcourt les lignes de sortie, l'interne
  les colonnes.
- On peut écrire directement la sortie sans stocker `resultat` dans un second
  tableau (c'est ce que fait la solution).

## Erreurs fréquentes observées

- Confondre horaire et anti-horaire (`source[j][n-1-i]` donne l'anti-horaire).
- Inverser le rôle de `i` et `j` dans la lecture ou l'affichage.
- `ArrayIndexOutOfBoundsException` en oubliant le `n-1`.

## Variantes possibles

- **Transposer puis inverser les lignes** : une autre façon d'obtenir la
  rotation horaire, en deux étapes plus simples à visualiser.
- Stocker explicitement la matrice résultat avant l'affichage (plus lisible pour
  un débutant, à peine plus coûteux).

## Pour approfondir

- Les rotations en place « par couches » utilisées dans les exercices d'entretien.
