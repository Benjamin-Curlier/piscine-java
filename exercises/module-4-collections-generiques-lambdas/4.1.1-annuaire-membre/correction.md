# Correction — Exercice 4.1.1 Annuaire de l'équipe (Map)

## Démarche attendue

1. **`enregistrer`** : `annuaire.put(indicatif, nom)` — `put` écrase silencieusement
   toute valeur existante pour la même clé.
2. **`rechercher`** : `annuaire.getOrDefault(indicatif, "Inconnu")` — retourne la
   valeur associée ou la valeur par défaut si la clé est absente, en un seul appel.
3. **`supprimer`** : `annuaire.remove(indicatif) != null` — `remove` renvoie
   l'ancienne valeur ou `null` si absent ; comparer à `null` donne le booléen attendu.
4. **`taille`** : `annuaire.size()` — taille courante de la Map.
5. **`indicatifsTries`** : créer `new ArrayList<>(annuaire.keySet())`, appeler
   `Collections.sort(liste)` puis retourner la liste.

## Points clés

- **`getOrDefault`** est l'idiome JDK pour éviter un `if (containsKey(...))` manuel ;
  plus lisible et moins sujet aux erreurs de logique.
- **`remove` retourne `null` sur clé absente** (pas d'exception) : c'est le
  comportement garanti par `Map`, d'où la robustesse de la comparaison `!= null`.
- **Copier `keySet()` dans une `ArrayList`** avant de trier est indispensable :
  `keySet()` est une vue non-modifiable chez `HashMap`.
- **`Collections.sort`** trie en place la liste par ordre naturel (`String` est
  `Comparable<String>` selon l'ordre lexicographique, donc alphabétique en ASCII).

## Erreurs fréquentes observées

- Utiliser `containsKey` puis `get` au lieu de `getOrDefault` (deux appels inutiles).
- Appeler `Collections.sort(annuaire.keySet())` directement →
  `UnsupportedOperationException` sur la vue.
- Retourner `new ArrayList<>(annuaire.keySet())` sans trier : l'ordre d'une
  `HashMap` est non déterministe.
- Renvoyer la référence interne `keySet()` comme `List` (incompatible de type et
  exposerait l'état interne).

## Pour approfondir

- `java.util.TreeMap` maintient les clés triées automatiquement (compromis :
  insertion O(log n) au lieu de O(1) amorti pour `HashMap`).
- `Map.getOrDefault` vs `Map.computeIfAbsent` : le second insère la valeur par
  défaut dans la Map, ce qu'on ne veut pas ici.
