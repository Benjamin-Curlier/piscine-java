# Correction — Exercice 4.1.4 Groupement de personnel par niveau

## Demarche attendue

1. **`affecter`** : utiliser `computeIfAbsent(niveau, g -> new ArrayList<>())` pour
   obtenir la liste du niveau, en la creant si elle n'existe pas encore, puis
   appeler `.add(nom)` sur le resultat. Cette methode est l'idiome cle de l'exo :
   elle garantit qu'on ne reinitialise jamais une liste deja existante.

2. **`membres`** : utiliser `getOrDefault(niveau, List.of())` pour renvoyer la liste
   associee au niveau, ou une liste vide immuable si le niveau n'est pas encore
   enregistre. Cela evite de creer une entree dans la Map pour un niveau jamais
   affecte (contrairement a `computeIfAbsent`).

3. **`niveaux`** : renvoyer `parNiveau.keySet()`. L'ensemble des cles de la Map est
   exactement l'ensemble des niveaux ayant au moins un membre.

4. **`effectif`** : delegueer a `membres(niveau).size()`. Cette delegation evite
   la duplication de la logique `getOrDefault`.

## Points cles

- **`computeIfAbsent` vs `put`** : `computeIfAbsent` ne cree la valeur (la liste)
  que si la cle est absente, et renvoie la liste (nouvelle ou existante). Un simple
  `put` remplacerait la liste a chaque affectation.
- **`getOrDefault` pour la lecture** : lire sans modifier la Map. Si on utilisait
  `computeIfAbsent` pour `membres`, on polluerait la Map avec des listes vides.
- **Delegation dans `effectif`** : `membres(niveau).size()` plutot que
  `parNiveau.getOrDefault(...).size()` directement — une seule source de verite.

## Erreurs frequentes observees

- Utiliser `computeIfAbsent` dans `membres` : cree une entree vide pour les niveaux
  jamais affectes, ce qui fausse `niveaux()`.
- Remplacer la liste par `new ArrayList<>()` a chaque `affecter` : perte des
  membres precedemment affectes au niveau.
- Renvoyer `null` au lieu d'une liste vide pour un niveau absent.

## Pour approfondir

- `Collectors.groupingBy` (chapitre 4.8) produit une `Map<K, List<V>>` a partir
  d'un stream, en une ligne — c'est la version "haute altitude" de ce que vous
  avez construit ici a la main.
