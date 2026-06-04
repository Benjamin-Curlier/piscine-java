# Exercice 4.3.4 — Recherche avec Optional

## Contexte

Plutôt que de renvoyer `null` quand un soldat est introuvable, Java offre
`Optional<T>` : un conteneur explicitement vide ou plein. Bien utilisé, il
s'enchaîne avec `map` et `orElse` sans jamais appeler `get()` directement.

## Énoncé

Le domaine (fourni, ne pas modifier) :

```java
public enum Grade { SOLDAT, CAPORAL, SERGENT, ADJUDANT, LIEUTENANT }
public record Soldat(String nom, Grade grade, int anciennete) { }
```

Complétez la classe `Recherche` (package `etnc.m4`) :

```java
static Optional<Soldat> premier(List<Soldat> soldats, Predicate<Soldat> critere)
static Optional<Soldat> plusHautGrade(List<Soldat> soldats)
static String nomOuParDefaut(List<Soldat> soldats, Predicate<Soldat> critere)
```

**`premier`** : renvoie le premier soldat de la liste qui satisfait `critere`,
ou `Optional.empty()` si aucun ne le satisfait.

**`plusHautGrade`** : renvoie le soldat de grade le plus élevé (ordre de
déclaration de l'enum), ou `Optional.empty()` si la liste est vide.

**`nomOuParDefaut`** : renvoie le **nom** du premier soldat qui satisfait
`critere`, ou `"Aucun"` s'il n'y en a pas.

## Indices

- `soldats.stream().filter(critere).findFirst()` — retourne directement un `Optional`.
- `soldats.stream().max(Comparator.comparing(Soldat::grade))` — `max` renvoie aussi un `Optional`.
- `premier(soldats, critere).map(Soldat::nom).orElse("Aucun")` — enchaîner sans `get()`.

## Exemple

```text
List<Soldat> troupe = List.of(
    new Soldat("Martin",  Grade.CAPORAL,    3),
    new Soldat("Lebrun",  Grade.SERGENT,    7),
    new Soldat("Dupont",  Grade.LIEUTENANT, 12)
);

premier(troupe, s -> s.anciennete() > 5)   // Optional[Lebrun/SERGENT/7]
plusHautGrade(troupe)                       // Optional[Dupont/LIEUTENANT/12]
nomOuParDefaut(troupe, s -> s.anciennete() > 20) // "Aucun"
```

## Contraintes

- Package `etnc.m4`. **Ne modifiez pas** `Grade.java` ni `Soldat.java`.
- **N'appelez jamais `get()`** sur un `Optional` : utilisez `map`, `orElse`,
  `isPresent`, `isEmpty`, `ifPresent`, etc.
- Liste vide → `Optional.empty()` (pas d'exception).

## Ce qui sera vérifié

- `premier` présent quand un élément satisfait ; vide sinon.
- `plusHautGrade` contient le soldat de grade maximum.
- `nomOuParDefaut` renvoie le nom ou `"Aucun"`.
- Comportement sur liste vide et sur liste sans match.

## Pour aller plus loin (optionnel — non noté)

- Quelle différence entre `findFirst()` et `findAny()` dans un stream parallèle ?
- Comment chaîner `flatMap` si `premier` renvoyait lui-même un `Optional` ?
