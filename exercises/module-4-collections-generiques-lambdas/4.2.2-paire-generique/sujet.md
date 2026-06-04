# Exercice 4.2.2 — Paire générique (deux paramètres de type)

## Contexte

On a souvent besoin d'associer **deux valeurs** de types différents : un nom et un
âge, un identifiant et une coordonnée, une clé et une valeur… Plutôt que d'écrire
une classe figée pour chaque cas, on écrit **une seule** classe paramétrée par
deux types `A` et `B`.

## Énoncé

Complétez la classe **`Paire<A, B>`** (déclarée `final`) :

- deux champs **privés et `final`** : `A premier` et `B second`, mémorisés dans le
  constructeur `Paire(A premier, B second)` ;
- les accesseurs `A premier()` et `B second()` ;
- `Paire<B, A> inverser()` : renvoie une **nouvelle** paire dont les composants
  sont échangés (le `second` devient le premier, le `premier` devient le second) ;
- une **méthode générique de fabrique**
  `static <X, Y> Paire<X, Y> de(X x, Y y)` : renvoie `new Paire<>(x, y)`.

## Exemple

```text
Paire<String, Integer> p = new Paire<>("Mission", 5);
p.premier();          // "Mission"   (type String)
p.second();           // 5           (type Integer)

Paire<Integer, String> q = p.inverser();
q.premier();          // 5
q.second();           // "Mission"

Paire<String, Integer> r = Paire.de("x", 1);   // types inférés
```

## Contraintes

- Package `etnc.m4`. **Ne changez pas** les signatures publiques.
- La classe est **`final`** et **immuable** : champs `private final`, **aucun
  mutateur**. `inverser()` ne modifie pas l'objet d'origine, il en crée un nouveau.
- Conservez l'en-tête générique fourni (`Paire<A, B>`, `<B, A>`, `<X, Y>`) :
  **aucun `Object`, aucun cast**. La généricité doit être propagée par les types.
- Aucune exception levée.

## Ce qui sera vérifié

- `premier()` / `second()` renvoient les bonnes valeurs **avec les bons types**
  (affectation sans cast : `String s = p.premier();`, `Integer i = p.second();`).
- `inverser()` renvoie une `Paire<B, A>` aux composants échangés.
- `de(...)` infère correctement les types et renvoie une paire conforme.

## Pour aller plus loin (optionnel — non noté)

- Pourquoi `de` est-elle une **méthode** générique (`static <X, Y>`) et pas
  seulement une méthode de la classe `Paire<A, B>` ?
- Que se passerait-il si vous ajoutiez un mutateur `setPremier(...)` ?
