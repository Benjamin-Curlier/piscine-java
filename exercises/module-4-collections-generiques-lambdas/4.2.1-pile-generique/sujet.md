# Exercice 4.2.1 — Pile générique (LIFO)

## Contexte

Une **pile** (*stack*) est une structure « dernier entré, premier sorti »
(LIFO : *Last In, First Out*). On veut une pile capable de contenir **n'importe
quel type** d'élément, sans réécrire le code pour chaque type.

## Énoncé

L'en-tête générique de la classe vous est **fourni** :

```java
public class Pile<T> { ... }
```

Le `<T>` est un **paramètre de type** : il sera remplacé par le type réel à
l'usage (`Pile<String>`, `Pile<Integer>`…). Le but est de **propager** ce `<T>`
dans toutes les méthodes : aucune n'utilise `Object` ni de cast.

Complétez `Pile<T>` (champ interne `private final List<T> elements` déjà fourni) :

- `void empiler(T element)` : ajoute l'élément au sommet.
- `T depiler()` : retire et renvoie l'élément du sommet (LIFO) ; pile vide →
  `null`.
- `T sommet()` : renvoie l'élément du sommet **sans le retirer** ; pile vide →
  `null`.
- `boolean estVide()` : `true` si la pile ne contient rien.
- `int taille()` : nombre d'éléments.

## Exemple

```text
Pile<String> pile = new Pile<>();
pile.empiler("a");
pile.empiler("b");
String x = pile.depiler();   // "b" (dernier empilé, premier dépilé)
String y = pile.depiler();   // "a"
pile.estVide();              // true
```

## Contraintes

- Package `etnc.m4`. **Ne modifiez pas** l'en-tête `public class Pile<T>` ni les
  signatures fournies.
- **Aucun `Object`, aucun cast** : le `<T>` doit être propagé partout (c'est tout
  l'intérêt d'une classe générique).
- Le champ interne reste `private` (la pile ne s'inspecte pas de l'extérieur).
- Pile vide → `null` (on **ne lève pas d'exception** ; les exceptions sont au
  module 5).

## Ce qui sera vérifié

- L'ordre **LIFO** sur `depiler` (le dernier empilé sort en premier).
- La **généricité par compilation** : `String x = pile.depiler();` et
  `Integer n = pile.depiler();` doivent compiler **sans cast** (preuve que `<T>`
  est bien propagé).
- `sommet` ne modifie pas la taille.
- `estVide` / `taille` cohérents, y compris pile vide → `null`.

## Pour aller plus loin (optionnel — non noté)

- Quelle est la différence entre `Pile<T>` et une `Pile` qui stockerait des
  `Object` ?
- Ajoutez une méthode `void vider()`.
