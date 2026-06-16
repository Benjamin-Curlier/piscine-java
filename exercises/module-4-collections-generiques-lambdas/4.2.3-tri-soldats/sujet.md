# Exercice 4.2.3 — Tri de membres (Comparable<T>)

## Contexte

Un équipe compte des membres de niveaux et de noms variés. Pour les afficher
dans l'ordre hiérarchique, on veut que la classe `Membre` définisse son
**ordre naturel** en implémentant `Comparable<Membre>`.

## Ce qui est fourni

`Niveau.java` vous est **fourni — ne pas modifier** :

```java
public enum Niveau { JUNIOR, CONFIRME, SENIOR, LEAD, PRINCIPAL }
```

L'ordre de déclaration des constantes est la hiérarchie croissante :
`JUNIOR` est le niveau le plus bas, `PRINCIPAL` le plus élevé. Comme toute
enum Java, `Niveau` implémente déjà `Comparable<Niveau>` dans cet ordre.

## Énoncé

Complétez la méthode `compareTo` de `Membre` :

```java
public record Membre(String nom, Niveau niveau, int anciennete)
        implements Comparable<Membre> {
    @Override
    public int compareTo(Membre autre) {
        return 0; // TODO
    }
}
```

**Règle de tri** :

1. D'abord par **niveau croissant** (`JUNIOR` < ... < `PRINCIPAL`).
2. En cas d'égalité de niveau, par **nom alphabétique** (`String.compareTo`).

Une fois `compareTo` implémenté, `Collections.sort(liste)` et `liste.sort(null)`
trient automatiquement une `List<Membre>` sans comparateur supplémentaire.

## Exemple

```text
Membre alice = new Membre("Alice", Niveau.SENIOR, 3);
Membre bob   = new Membre("Bob",   Niveau.JUNIOR,  1);
Membre clara = new Membre("Clara", Niveau.SENIOR, 5);

// Avant tri : [alice, bob, clara]
List<Membre> liste = new ArrayList<>(List.of(alice, bob, clara));
Collections.sort(liste);
// Après tri : [bob, alice, clara]
//   bob   → JUNIOR (le plus bas)
//   alice → SENIOR, puis clara → SENIOR (même niveau, "Alice" < "Clara")
```

## Contraintes

- Package `piscine.m4`. **Ne modifiez pas** `Niveau` ni les signatures de `Membre`.
- `compareTo` doit être cohérent : si `a.compareTo(b) > 0` alors `b.compareTo(a) < 0`.
- Deux membres de même niveau **et** de même nom → `compareTo` renvoie **zéro**.
- Aucune exception levée, aucune réflexion.

## Ce qui sera vérifié

- Tri d'une liste mélangée : ordre niveau croissant, puis nom alphabétique à niveau égal.
- Signe de `compareTo` entre deux niveaux différents (négatif/positif).
- Départage alphabétique à niveau égal.
- Égalité stricte (même niveau, même nom → zéro).
- Antisymétrie : `a.compareTo(b)` et `b.compareTo(a)` ont des signes opposés.

## Pour aller plus loin (optionnel — non noté)

- Ajoutez `anciennete` comme troisième critère de tri.
- Explorez `Comparator.comparing(Membre::niveau).thenComparing(Membre::nom)` :
  comment se compare-t-il à votre `compareTo` ? (cf. exercice 4.2.4)
