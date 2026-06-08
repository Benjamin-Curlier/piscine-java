# Exercice 6.1.3 — Calculatrice en TDD

## Contexte

Vous testez une `Calculatrice` à quatre opérations. L'une d'elles, la division, doit
**lever une exception** quand on divise par zéro. C'est l'occasion de tester à la fois des
**valeurs de retour** et un **comportement d'erreur** (`assertThatThrownBy`, vu au module 5
et au chapitre 6-4).

La démarche conseillée est **TDD** : pour chaque comportement, écrivez d'abord le test,
voyez-le rouge, puis vérifiez qu'il passe sur la classe fournie. Ici, l'implémentation est
correcte — votre livrable et votre note portent sur la **suite de tests**.

## Énoncé

La classe `Calculatrice` (fournie, **à ne pas modifier**) expose :

```java
public int ajouter(int a, int b)
public int soustraire(int a, int b)
public int multiplier(int a, int b)
public int diviser(int a, int b)   // lève ArithmeticException si b == 0
```

Complétez `CalculatriceTest` pour couvrir :

1. L'addition, la soustraction, la multiplication sur des valeurs connues.
2. La division nominale (par exemple `diviser(6, 2) == 3`).
3. La division par zéro : `diviser(x, 0)` doit **lever** `ArithmeticException`
   (utilisez `assertThatThrownBy(() -> ...).isInstanceOf(ArithmeticException.class)`).

## Exemple

```text
calc.ajouter(2, 3)      // → 5
calc.soustraire(7, 4)   // → 3
calc.multiplier(6, 7)   // → 42
calc.diviser(6, 2)      // → 3
calc.diviser(6, 0)      // → lève ArithmeticException
```

## Contraintes

- Une assertion par comportement ; assertions AssertJ (`assertThat`, `assertThatThrownBy`).
- Couvrez **les quatre opérations** et **le cas division par zéro**.
- Ne modifiez pas la classe `Calculatrice`.

## Ce qui sera vérifié

- Vos tests **passent** sur l'implémentation correcte.
- Vos tests **détectent** chaque version buggée : une addition transformée en soustraction,
  une multiplication transformée en addition, et une division qui **n'avertit pas** du zéro
  (renvoie une valeur au lieu de lever).
- La démarche de test (critère formateur : exhaustivité, clarté, TDD).

## Pour aller plus loin (optionnel)

- Vérifiez que `diviser(0, 5)` vaut bien `0` — un cas limite qui ne doit pas lever.
