# Exercice 5.1.1 — Saisie défensive (validerEntier)

## Contexte

Toute application qui lit des entrées utilisateur doit se protéger contre deux
familles de problèmes distincts : une saisie **illisible** (texte non numérique,
valeur trop grande pour un `int`) et une saisie **hors des bornes admises** (le
nombre est lisible, mais il n'est pas dans la plage autorisée).

Ces deux familles ne se gèrent pas de la même façon : l'une vient d'un problème
de format, l'autre d'une valeur sémantiquement incorrecte. Java fournit deux
exceptions distinctes pour les distinguer : `NumberFormatException` et
`IllegalArgumentException`.

## Énoncé

Complétez la classe `ValidationSaisie` avec la méthode statique :

```java
public static int validerEntier(String texte, int min, int max)
```

La méthode doit appliquer la logique suivante dans l'ordre exact :

1. Si `texte == null` → lever `new NumberFormatException("...")` avec un message
   explicatif. Cette garde doit être **explicite**, avant tout appel de méthode
   sur `texte` (sinon `texte.trim()` lèverait une `NullPointerException` sans
   message utile).
2. Convertir : `Integer.parseInt(texte.trim())`. Si le texte est non numérique
   ou provoque un dépassement de capacité (`int`), la `NumberFormatException`
   de `parseInt` remonte **telle quelle** (on ne la rattrape pas).
3. Si la valeur est hors de `[min, max]` → lever `new IllegalArgumentException(...)`
   avec un message **nommant `min`, `max` et la valeur fautive**.
4. Sinon → renvoyer la valeur.

## Exemple

```text
ValidationSaisie.validerEntier("5",  0, 10)  // → 5
ValidationSaisie.validerEntier("0",  0, 10)  // → 0  (borne inférieure)
ValidationSaisie.validerEntier("10", 0, 10)  // → 10 (borne supérieure)
ValidationSaisie.validerEntier("  7  ", 0, 10) // → 7 (trim)

ValidationSaisie.validerEntier("-1",  0, 10) // → IllegalArgumentException
ValidationSaisie.validerEntier("11",  0, 10) // → IllegalArgumentException
ValidationSaisie.validerEntier("abc", 0, 10) // → NumberFormatException
ValidationSaisie.validerEntier(null,  0, 10) // → NumberFormatException
```

## Contraintes

- Package `piscine.m5`. **Ne modifiez pas** la signature de `validerEntier`.
- La garde `null` doit être **la première instruction** du corps (avant `trim()`).
- Pour une entrée illisible, c'est une `NumberFormatException` qui doit remonter
  (pas une `NullPointerException`, pas une `IllegalArgumentException` générique).
- Le message de l'`IllegalArgumentException` hors-plage doit **nommer les deux
  bornes et la valeur fautive** (les tests le vérifient).
- Aucune dépendance extérieure n'est autorisée.

## Ce qui sera vérifié

- Valeur au milieu, égale au min, égale au max → renvoyée correctement.
- Valeur sous le min ou au-dessus du max → `IllegalArgumentException`.
- Texte non numérique → `NumberFormatException` (type précis).
- `null` → `NumberFormatException` (pas `NullPointerException`).
- Overflow (ex. `"99999999999"`) → `NumberFormatException`.
- Message de l'exception hors-plage contient les deux bornes et la valeur.
- Texte avec espaces (`"  7  "`) → valeur correcte grâce au `trim`.
- Signe explicite (`"+5"`) → accepté si dans la plage.

## Pour aller plus loin (optionnel — non noté)

- Pourquoi distinguer `NumberFormatException` et `IllegalArgumentException` si
  l'une hérite de l'autre ? Indice : un appelant peut vouloir traiter l'illisible
  différemment du hors-plage (`catch (NumberFormatException e)` vs `catch
  (IllegalArgumentException e)`).
- Comment rendriez-vous la méthode générique pour des `long` ou des `double` ?
- Que se passerait-il si `min > max` ? Faudrait-il aussi le vérifier ?
