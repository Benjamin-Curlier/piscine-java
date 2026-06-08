# Correction — 6.1.3 Calculatrice en TDD

## Démarche attendue

L'exercice combine deux styles d'assertion : la **valeur de retour** (`isEqualTo`) pour les
opérations, et le **comportement d'erreur** (`assertThatThrownBy`) pour la division par
zéro. Une suite complète couvre les quatre opérations **et** le cas d'erreur — sinon, un
mutant ciblant l'opération non testée survit.

## Suite de tests modèle

```java
private final Calculatrice calc = new Calculatrice();

@Test void additionneDeuxEntiers()  { assertThat(calc.ajouter(2, 3)).isEqualTo(5); }
@Test void soustraitDeuxEntiers()   { assertThat(calc.soustraire(7, 4)).isEqualTo(3); }
@Test void multiplieDeuxEntiers()   { assertThat(calc.multiplier(6, 7)).isEqualTo(42); }
@Test void diviseDeuxEntiers()      { assertThat(calc.diviser(6, 2)).isEqualTo(3); }
@Test void diviserParZeroLeveUneException() {
    assertThatThrownBy(() -> calc.diviser(6, 0)).isInstanceOf(ArithmeticException.class);
}
```

## Quel test tue quel mutant

| Mutant | Bug injecté | Test qui le détecte |
|---|---|---|
| `addition-en-soustraction` | `ajouter` renvoie `a - b` | `additionneDeuxEntiers` (attend 5, le mutant donne -1) |
| `multiplication-en-addition` | `multiplier` renvoie `a + b` | `multiplieDeuxEntiers` (attend 42, le mutant donne 13) |
| `division-avale-le-zero` | renvoie `0` au lieu de lever | `diviserParZeroLeveUneException` (le mutant ne lève pas) |

> Le mutant `division-avale-le-zero` est instructif : sans un test qui **exige** la levée
> d'exception, on ne le distingue pas du code correct sur les divisions normales. Tester le
> cas d'erreur n'est pas optionnel.

## Points clés

- `assertThatThrownBy(() -> ...).isInstanceOf(...)` teste qu'un bloc lève bien une exception.
- Un comportement par test : quatre opérations + un cas d'erreur = cinq tests.
- En TDD, on écrit chaque test avant de le voir vert — ici la classe est déjà correcte, mais
  la discipline « un test par comportement » reste la même.

## Erreurs fréquentes observées

- Tester seulement les opérations « faciles » et oublier la division par zéro → un mutant survit.
- Mettre les cinq comportements dans un seul `@Test` → dès qu'une assertion échoue, les suivantes ne s'exécutent pas.
- Attendre la mauvaise exception (`Exception` générique au lieu d'`ArithmeticException`).
