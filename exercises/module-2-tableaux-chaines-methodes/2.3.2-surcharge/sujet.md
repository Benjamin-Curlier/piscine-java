# Exercice 2.3.2 — Surcharge

## Contexte

Il est pratique d'avoir une seule fonction `maximum` qui s'adapte au nombre et
au type de ses arguments. En Java, cela s'appelle la **surcharge** : plusieurs
méthodes portent le même nom mais ont des paramètres différents.

## Énoncé

Complétez les trois méthodes `static` de la classe `Surcharge`. **Ne modifiez
pas leurs signatures.** Toutes s'appellent `maximum` et renvoient le plus grand
de leurs arguments.

1. `public static int maximum(int a, int b)`
2. `public static int maximum(int a, int b, int c)`
3. `public static double maximum(double a, double b)`

## Exemple

```java
Surcharge.maximum(3, 7)       // 7
Surcharge.maximum(3, 7, 5)    // 7
Surcharge.maximum(2.5, 1.5)   // 2.5
```

## Contraintes

- La classe doit s'appeler `Surcharge` et rester dans le package `etnc.m2`.
- **Les trois signatures sont imposées** : c'est la surcharge qui est évaluée.
- Les méthodes renvoient leur résultat ; elles n'affichent rien.

## Ce qui sera vérifié

- Les trois versions de `maximum` renvoient le bon résultat.
- Les cas particuliers fonctionnent : valeurs négatives, valeurs égales, et un
  maximum situé à différentes positions parmi trois entiers.

## Pour aller plus loin (optionnel — non noté)

- Java choisit la bonne méthode selon les **types** des arguments à la
  compilation. Que se passe-t-il si vous appelez `maximum(2, 3.0)` ? Essayez et
  observez quelle version est choisie.
- La méthode `Math.max` existe déjà : pourriez-vous l'utiliser dans vos
  implémentations ?
