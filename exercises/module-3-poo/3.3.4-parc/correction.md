# Correction — Exercice 3.3.4 Parc de formes (polymorphisme)

## Démarche attendue

1. `surfaceTotale` : une boucle `for (Forme f : formes)` qui accumule `f.aire()`.
   La bonne version d'`aire()` est appelée selon le type réel de `f`.
2. `plusGrande` : garder une référence `max` (initialisée à `null`) ; pour
   chaque forme, si `max == null` **ou** `f.aire() > max.aire()`, mettre à jour.
   Renvoyer `max` (donc `null` pour un tableau vide).
3. `compterCercles` : une boucle qui incrémente un compteur quand
   `f instanceof Cercle`.

## Points clés

- **Polymorphisme** : on manipule des `Forme` sans connaître le type concret ;
  `aire()` s'adapte automatiquement.
- **`instanceof`** : teste le type réel d'un objet à l'exécution.
- **Downcast** : après `instanceof Cercle`, `(Cercle) f` permet d'accéder à
  `getRayon()`.
- **`null` plutôt qu'exception** : choix volontaire ici (les exceptions sont au
  module 5).

## Erreurs fréquentes observées

- Initialiser `max` à `formes[0]` sans gérer le tableau vide (plantage).
- Comparer les formes au lieu de comparer leurs aires.
- Utiliser une collection alors qu'un tableau suffit.

## Pour approfondir

- Le « pattern matching for instanceof » (`if (f instanceof Cercle c)`) — sucre
  syntaxique récent qui combine test et downcast.
