# Exercice 4.3.1 — Filtrage et projection (streams)

## Contexte

Le responsable veut extraire des informations précises d'une liste de membres :
noms des membres d'un niveau suffisant, ou tout sous-ensemble répondant à
un critère quelconque. Les **streams Java** permettent d'exprimer cela en un
pipeline `filter` / `map` / `collect` lisible et idiomatique.

## Modèle fourni (ne pas modifier)

```java
public enum Niveau { JUNIOR, CONFIRME, SENIOR, LEAD, PRINCIPAL }
// ordre décroissant de niveau = ordre de déclaration (enum Comparable)

public record Membre(String nom, Niveau niveau, int anciennete) { }
// anciennete = années de service (>= 0)
```

## Énoncé

Complétez la classe `Effectifs` :

```java
// Renvoie les noms des membres dont le niveau est >= min, dans l'ordre source.
static List<String> nomsDesNiveauxAuMoins(List<Membre> membres, Niveau min)

// Renvoie la sous-liste des membres satisfaisant le critère (Predicate).
static List<Membre> filtrer(List<Membre> membres, Predicate<Membre> critere)
```

## Exemple

```text
List<Membre> equipe = List.of(
    new Membre("Dupont", Niveau.JUNIOR,    2),
    new Membre("Martin", Niveau.SENIOR,   8),
    new Membre("Leroy",  Niveau.PRINCIPAL, 15)
);

nomsDesNiveauxAuMoins(equipe, Niveau.SENIOR)
    // -> ["Martin", "Leroy"]

filtrer(equipe, s -> s.anciennete() > 5)
    // -> [Martin, Leroy]
```

## Contraintes

- Package `piscine.m4`. **Ne modifiez pas** `Niveau`, `Membre` ni les signatures.
- Utilisez un pipeline `stream()` / `filter` / `map` / `collect` idiomatique.
- `filtrer` accepte n'importe quel `Predicate<Membre>` — ne codez pas le critère en dur.
- Si aucun élément ne satisfait le critère, renvoyez une **liste vide** (jamais `null`).
- La liste source ne doit **pas être modifiée**.

## Ce qui sera vérifié

- `nomsDesNiveauxAuMoins` renvoie les noms des membres de niveau au moins égal à `min`, dans l'ordre source.
- `filtrer` avec un lambda passé par le test : ancienneté, niveau, etc.
- Cas limites : aucun élément ne satisfait → liste vide ; tous satisfont → tous renvoyés.

## Pour aller plus loin (optionnel — non noté)

- Quelle différence entre `Collectors.toList()` et `Stream.toList()` (Java 16+) ?
- Comment chaîner plusieurs `filter` successifs ?
