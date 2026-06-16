# Correction — Exercice 4.3.3 Groupement par collecteurs (streams)

## Démarche attendue

1. `parNiveau` : `membres.stream().collect(Collectors.groupingBy(Membre::niveau))`.
   La référence de méthode `Membre::niveau` extrait la clé de groupement.
   La map résultante ne contient que les niveaux effectivement présents.
2. `selonAnciennete` : `membres.stream().collect(Collectors.partitioningBy(s -> s.anciennete() >= seuil))`.
   `partitioningBy` garantit **toujours** les deux clés `true` et `false`, même
   si l'un des groupes est vide — c'est sa différence fondamentale avec
   `groupingBy`.
3. `effectifsParNiveau` : `membres.stream().collect(Collectors.groupingBy(Membre::niveau, Collectors.counting()))`.
   Le collecteur downstream `counting()` remplace la liste par défaut par un
   compteur `Long`.

## Points clés

- **`groupingBy` vs `partitioningBy`** : `groupingBy` ne crée une entrée que
  si au moins un élément y appartient ; `partitioningBy` garantit toujours les
  deux clés `true`/`false`.
- **Collecteur downstream** : `groupingBy(classifier, downstream)` permet de
  transformer chaque groupe — ici `counting()`, mais on pourrait écrire
  `mapping(...)`, `joining(...)`, etc.
- **Référence de méthode** : `Membre::niveau` est équivalente à la lambda
  `s -> s.niveau()` ; la forme compacte est idiomatique.

## Erreurs fréquentes observées

- Utiliser `groupingBy` pour `selonAnciennete` : la clé `false` peut alors
  être absente si tous les membres satisfont le critère.
- Oublier le collecteur downstream `counting()` et obtenir une `Map<Niveau, List<Membre>>`
  au lieu de `Map<Niveau, Long>`.
- Retourner `null` pour une liste vide au lieu d'une map vide (comportement
  naturel de `groupingBy`).

## Pour approfondir

- `Collectors.toUnmodifiableMap` pour des résultats immuables.
- `Collectors.groupingByConcurrent` pour des traitements parallèles.
