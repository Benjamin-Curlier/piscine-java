# Exercice 4.2.3 — Tri de soldats (Comparable<T>)

## Contexte

Un peloton compte des soldats de grades et de noms variés. Pour les afficher
dans l'ordre hiérarchique, on veut que la classe `Soldat` définisse son
**ordre naturel** en implémentant `Comparable<Soldat>`.

## Ce qui est fourni

`Grade.java` vous est **fourni — ne pas modifier** :

```java
public enum Grade { SOLDAT, CAPORAL, SERGENT, ADJUDANT, LIEUTENANT }
```

L'ordre de déclaration des constantes est la hiérarchie croissante :
`SOLDAT` est le grade le plus bas, `LIEUTENANT` le plus élevé. Comme toute
enum Java, `Grade` implémente déjà `Comparable<Grade>` dans cet ordre.

## Énoncé

Complétez la méthode `compareTo` de `Soldat` :

```java
public record Soldat(String nom, Grade grade, int anciennete)
        implements Comparable<Soldat> {
    @Override
    public int compareTo(Soldat autre) {
        return 0; // TODO
    }
}
```

**Règle de tri** :

1. D'abord par **grade croissant** (`SOLDAT` < ... < `LIEUTENANT`).
2. En cas d'égalité de grade, par **nom alphabétique** (`String.compareTo`).

Une fois `compareTo` implémenté, `Collections.sort(liste)` et `liste.sort(null)`
trient automatiquement une `List<Soldat>` sans comparateur supplémentaire.

## Exemple

```text
Soldat alice = new Soldat("Alice", Grade.SERGENT, 3);
Soldat bob   = new Soldat("Bob",   Grade.SOLDAT,  1);
Soldat clara = new Soldat("Clara", Grade.SERGENT, 5);

// Avant tri : [alice, bob, clara]
List<Soldat> liste = new ArrayList<>(List.of(alice, bob, clara));
Collections.sort(liste);
// Après tri : [bob, alice, clara]
//   bob   → SOLDAT (le plus bas)
//   alice → SERGENT, puis clara → SERGENT (même grade, "Alice" < "Clara")
```

## Contraintes

- Package `etnc.m4`. **Ne modifiez pas** `Grade` ni les signatures de `Soldat`.
- `compareTo` doit être cohérent : si `a.compareTo(b) > 0` alors `b.compareTo(a) < 0`.
- Deux soldats de même grade **et** de même nom → `compareTo` renvoie **zéro**.
- Aucune exception levée, aucune réflexion.

## Ce qui sera vérifié

- Tri d'une liste mélangée : ordre grade croissant, puis nom alphabétique à grade égal.
- Signe de `compareTo` entre deux grades différents (négatif/positif).
- Départage alphabétique à grade égal.
- Égalité stricte (même grade, même nom → zéro).
- Antisymétrie : `a.compareTo(b)` et `b.compareTo(a)` ont des signes opposés.

## Pour aller plus loin (optionnel — non noté)

- Ajoutez `anciennete` comme troisième critère de tri.
- Explorez `Comparator.comparing(Soldat::grade).thenComparing(Soldat::nom)` :
  comment se compare-t-il à votre `compareTo` ? (cf. exercice 4.2.4)
