# Correction — Exercice 3.4.3 Comparaison personnalisée (interface)

## Démarche attendue

1. `Dossier` : champs `private final String titre;` et `private final int priorite;`,
   mémorisés dans le constructeur ; accesseurs simples.
2. `comparerA` : on sait que l'autre est un `Dossier`, donc downcast
   `(Dossier) autre`, puis `Integer.compare(this.priorite, d.priorite)` (renvoie
   un signe : négatif, nul ou positif).
3. `Classement.plusPrioritaire` : parcourir le tableau en gardant le `max`
   (initialisé à `null`) ; mettre à jour quand `d.comparerA(max) > 0`. Renvoyer
   `max` (donc `null` si vide).

## Points clés

- **Contrat d'interface** : `implements Ordonnable` engage `Dossier` à fournir
  `comparerA`. On peut ensuite manipuler l'objet via le type `Ordonnable`.
- **Signe, pas différence** : `Integer.compare` évite les pièges de débordement
  d'une soustraction et renvoie clairement -1/0/1.
- **`null` plutôt qu'exception** : choix volontaire (les exceptions sont au
  module 5).

## Erreurs fréquentes observées

- Renvoyer `this.priorite - d.priorite` (correct ici mais risqué en général).
- Oublier le downcast `(Dossier) autre`.
- `plusPrioritaire` qui plante sur un tableau vide (mauvaise initialisation).

## Pour approfondir

- `java.lang.Comparable<T>` (la version générique du JDK, vue au module 4).
