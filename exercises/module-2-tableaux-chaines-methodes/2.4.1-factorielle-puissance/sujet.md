# Exercice 2.4.1 — Factorielle et puissance

## Contexte

Certains calculs se définissent naturellement « en fonction d'eux-mêmes » :
`n! = n × (n−1)!`. La récursivité permet d'écrire ces calculs presque mot pour
mot comme leur définition mathématique.

## Énoncé

Complétez les deux méthodes `static` de la classe `FactoriellePuissance`. **Ne
modifiez pas leurs signatures.** Chaque méthode renvoie son résultat.

1. `public static long factorielle(int n)` — renvoie `n!` (`n ≥ 0`). Par
   définition, `0! = 1`.
2. `public static long puissance(int base, int exposant)` — renvoie
   `base^exposant` (`exposant ≥ 0`). Par définition, `base⁰ = 1`.

**Votre solution doit être récursive** : chaque méthode comporte un **cas de
base** (résolu sans nouvel appel) et au moins un **appel à elle-même** sur un
problème plus petit. Une solution avec une boucle ne respecte pas la consigne.

## Exemple

```java
FactoriellePuissance.factorielle(5)   // 120
FactoriellePuissance.puissance(2, 10) // 1024
```

## Contraintes

- La classe doit s'appeler `FactoriellePuissance` et rester dans le package `etnc.m2`.
- **Les deux signatures sont imposées.**
- **Les deux méthodes doivent être récursives** (cas de base + appel récursif).
- Les méthodes renvoient leur résultat ; elles n'affichent rien.

## Ce qui sera vérifié

- `factorielle` est correct, y compris `factorielle(0)` et de grandes valeurs
  comme `factorielle(13)` (d'où le type `long`).
- `puissance` est correct, y compris `exposant = 0`.
- La forme **récursive** est évaluée par le formateur (un cas de base et un
  appel récursif, pas de boucle).

## Pour aller plus loin (optionnel — non noté)

- Combien d'appels récursifs `factorielle(5)` déclenche-t-il ?
- La récursivité est-elle ici plus lisible qu'une boucle ? Et pour de très
  grandes valeurs, quel est le risque (pile d'exécution) ?
