# Exercice 4.3.4 — Recherche avec Optional

## Contexte

Plutôt que de renvoyer `null` quand un membre est introuvable, Java offre
`Optional<T>` : un conteneur explicitement vide ou plein. Bien utilisé, il
s'enchaîne avec `map` et `orElse` sans jamais appeler `get()` directement.

## Énoncé

Le domaine (fourni, ne pas modifier) :

```java
public enum Niveau { JUNIOR, CONFIRME, SENIOR, LEAD, PRINCIPAL }
public record Membre(String nom, Niveau niveau, int anciennete) { }
```

Complétez la classe `Recherche` (package `piscine.m4`) :

```java
static Optional<Membre> premier(List<Membre> membres, Predicate<Membre> critere)
static Optional<Membre> plusHautNiveau(List<Membre> membres)
static String nomOuParDefaut(List<Membre> membres, Predicate<Membre> critere)
```

**`premier`** : renvoie le premier membre de la liste qui satisfait `critere`,
ou `Optional.empty()` si aucun ne le satisfait.

**`plusHautNiveau`** : renvoie le membre de niveau le plus élevé (ordre de
déclaration de l'enum), ou `Optional.empty()` si la liste est vide.

**`nomOuParDefaut`** : renvoie le **nom** du premier membre qui satisfait
`critere`, ou `"Aucun"` s'il n'y en a pas.

## Indices

- `membres.stream().filter(critere).findFirst()` — retourne directement un `Optional`.
- `membres.stream().max(Comparator.comparing(Membre::niveau))` — `max` renvoie aussi un `Optional`.
- `premier(membres, critere).map(Membre::nom).orElse("Aucun")` — enchaîner sans `get()`.

## Exemple

```text
List<Membre> equipe = List.of(
    new Membre("Martin",  Niveau.CONFIRME,    3),
    new Membre("Lebrun",  Niveau.SENIOR,    7),
    new Membre("Dupont",  Niveau.PRINCIPAL, 12)
);

premier(equipe, s -> s.anciennete() > 5)   // Optional[Lebrun/SENIOR/7]
plusHautNiveau(equipe)                       // Optional[Dupont/PRINCIPAL/12]
nomOuParDefaut(equipe, s -> s.anciennete() > 20) // "Aucun"
```

## Contraintes

- Package `piscine.m4`. **Ne modifiez pas** `Niveau.java` ni `Membre.java`.
- **N'appelez jamais `get()`** sur un `Optional` : utilisez `map`, `orElse`,
  `isPresent`, `isEmpty`, `ifPresent`, etc.
- Liste vide → `Optional.empty()` (pas d'exception).

## Ce qui sera vérifié

- `premier` présent quand un élément satisfait ; vide sinon.
- `plusHautNiveau` contient le membre de niveau maximum.
- `nomOuParDefaut` renvoie le nom ou `"Aucun"`.
- Comportement sur liste vide et sur liste sans match.

## Pour aller plus loin (optionnel — non noté)

- Quelle différence entre `findFirst()` et `findAny()` dans un stream parallèle ?
- Comment chaîner `flatMap` si `premier` renvoyait lui-même un `Optional` ?
