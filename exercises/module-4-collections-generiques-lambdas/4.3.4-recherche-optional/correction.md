# Correction — Exercice 4.3.4 Recherche avec Optional

## Démarche attendue

1. **`premier`** : `membres.stream().filter(critere).findFirst()` — le stream
   renvoie directement un `Optional<Membre>` ; aucune logique conditionnelle
   n'est nécessaire.
2. **`plusHautNiveau`** : `membres.stream().max(Comparator.comparing(Membre::niveau))`.
   L'enum `Niveau` est `Comparable` par ordre de déclaration (JUNIOR < ... < PRINCIPAL),
   donc `Comparator.comparing` sur `Membre::niveau` suffit. `max` renvoie un
   `Optional<Membre>` déjà vide si la liste est vide.
3. **`nomOuParDefaut`** : `premier(membres, critere).map(Membre::nom).orElse("Aucun")`.
   On réutilise `premier` et on enchaîne sans jamais appeler `get()`.

## Points clés

- **`Optional` sans `get()`** : appeler `get()` sur un Optional vide lève une
  `NoSuchElementException`. Les méthodes `map`/`orElse`/`ifPresent` sont sûres.
- **Réutilisation** : `nomOuParDefaut` délègue à `premier`, évitant toute
  duplication de la logique de filtrage.
- **Cas liste vide géré gratuitement** : `stream().filter(...).findFirst()`
  renvoie `Optional.empty()` naturellement ; pas besoin de garde `if (membres.isEmpty())`.

## Erreurs fréquentes observées

- Appeler `opt.get()` sans vérifier `isPresent()` → risque de `NoSuchElementException`.
- Utiliser `orElse(null)` et rouvrir la porte aux `NullPointerException`.
- Réécrire la logique de filtrage dans `nomOuParDefaut` au lieu de réutiliser `premier`.
- Confondre `Comparator.comparing(Membre::niveau)` (comparaison par ordre naturel
  de l'enum) et `Comparator.comparingInt(...)` (réservé aux `int` primitifs).

## Pour approfondir

- `Optional.flatMap` : utile quand la transformation elle-même renvoie un `Optional`.
- `Stream.findAny()` vs `findFirst()` : `findAny` autorise plus d'optimisations
  en parallèle mais ne garantit pas l'ordre.
