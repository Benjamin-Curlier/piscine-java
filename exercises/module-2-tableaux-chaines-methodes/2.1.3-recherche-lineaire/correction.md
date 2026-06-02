# Correction — Exercice 2.1.3 Recherche linéaire

## Démarche attendue

1. Lire `n`, le tableau, puis la valeur cherchée.
2. Utiliser une variable `indice` initialisée à `-1` (« pas encore trouvé »).
3. Parcourir le tableau ; dès qu'une case vaut la cible, mémoriser l'indice et
   sortir de la boucle (`break`).
4. Après la boucle : si `indice >= 0`, afficher `Indice : <indice>` ; sinon
   `Absent`.

## Points clés

- **`break` à la première occurrence** : garantit qu'on renvoie la première
  position en cas de doublons, et évite de parcourir inutilement la suite.
- **Indice 0** : la première case est l'indice 0, comme partout en Java.
- **Sentinelle -1** : une valeur impossible comme indice sert à distinguer
  « trouvé » de « absent ».

## Erreurs fréquentes observées

- Continuer la boucle après avoir trouvé → renvoie la dernière occurrence.
- Afficher l'indice + 1 (réflexe « humain » de compter à partir de 1).
- Oublier le cas `Absent`.

## Variantes possibles

- Sans `break`, en parcourant à l'envers : renverrait la première occurrence
  aussi, mais c'est moins lisible.
- Extraire la recherche dans une méthode `static int indiceDe(int[], int)` —
  ce sera l'objet du sous-groupe 2.3 (méthodes).

## Pour approfondir

- Comparer recherche linéaire (O(n)) et recherche dichotomique (O(log n)).
