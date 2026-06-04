# Exercice 5.1.3 — Calculs géométriques avec gardes (refactor 2.3.3)

## Contexte

Dans l'exercice 2.3.3, vous aviez extrait quatre méthodes géométriques
(`aire` et `perimetre` pour cercle et rectangle). Ces méthodes fonctionnent
correctement pour des dimensions positives, mais que se passe-t-il si on passe
un rayon de `-5` ou une hauteur de `-2` ?

En Java, une méthode bien conçue **refuse en entrée ce qu'elle ne peut pas
traiter** : c'est la programmation défensive. Vous allez **enrichir** ce code
avec des **gardes** qui lèvent une `IllegalArgumentException` dès qu'une
dimension est strictement négative.

## Énoncé

Complétez la classe `CalculsGeometriques` (package `etnc.m5`). Les **quatre
signatures sont imposées et inchangées** par rapport à 2.3.3 :

```java
public static double aire(double rayon)
public static double aire(double largeur, double hauteur)
public static double perimetre(double rayon)
public static double perimetre(double largeur, double hauteur)
```

**Règle de garde** : chaque méthode doit lever une
`IllegalArgumentException` si une dimension est **strictement négative** (`< 0`).
Le zéro est autorisé (aire et périmètre nuls, valide géométriquement).

**Le message de l'exception doit nommer le paramètre fautif** :
`"rayon"`, `"largeur"` ou `"hauteur"` selon le cas.

**Conseil** : pour éviter de répéter la même logique quatre fois, écrivez un
helper privé `exigerPositif(double valeur, String nom)` qui centralise la garde.
C'est le critère `idiomatisme` de cet exercice.

## Exemple

```text
CalculsGeometriques.aire(2.0)          // ≈ 12.566... (π × 4)
CalculsGeometriques.aire(3.0, 4.0)    // 12.0
CalculsGeometriques.perimetre(2.0)    // ≈ 12.566... (4π)
CalculsGeometriques.perimetre(3.0, 4.0) // 14.0

CalculsGeometriques.aire(-1.0)         // IllegalArgumentException : "rayon"
CalculsGeometriques.aire(-1.0, 4.0)   // IllegalArgumentException : "largeur"
CalculsGeometriques.aire(3.0, -4.0)   // IllegalArgumentException : "hauteur"
CalculsGeometriques.aire(0.0)         // 0.0  (zéro autorisé)
```

## Contraintes

- Package `etnc.m5`. **Ne modifiez pas les signatures publiques.**
- Utilisez `Math.PI` (jamais une constante approchée).
- Levez `IllegalArgumentException` (du JDK, pas de type custom).
- Le message doit contenir le nom du paramètre fautif.
- Le zéro (`0.0` et `-0.0`) est autorisé : ne levez pas d'exception pour `0`.
- Le helper privé `exigerPositif` est fortement recommandé (critère noté).

## Ce qui sera vérifié

- Valeurs positives normales : aires et périmètres corrects (cercle et rectangle).
- Dimensions négatives : `IllegalArgumentException` levée sur `aire` et sur
  `perimetre`, pour le cercle comme pour le rectangle.
- Message nominatif : le message contient `"rayon"`, `"largeur"` ou `"hauteur"`
  selon le paramètre fautif.
- Zéro autorisé : `aire(0.0) == 0.0`, `perimetre(0.0) == 0.0`, `aire(0.0, 5.0) == 0.0`.

## Pour aller plus loin (optionnel — non noté)

- `Double.isNaN(valeur)` : faudrait-il aussi rejeter les valeurs `NaN` ?
  Que retourne `Math.PI * Double.NaN * Double.NaN` ?
- Comment tester une méthode sans effet de bord autre que le retour d'une valeur
  ou la levée d'une exception ? Pourquoi les méthodes `static` pures sont-elles
  faciles à tester ?
