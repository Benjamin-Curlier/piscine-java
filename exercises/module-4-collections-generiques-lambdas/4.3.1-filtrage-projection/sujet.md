# Exercice 4.3.1 — Filtrage et projection (streams)

## Contexte

Le commandant veut extraire des informations précises d'une liste de soldats :
noms des militaires d'un grade suffisant, ou tout sous-ensemble répondant à
un critère quelconque. Les **streams Java** permettent d'exprimer cela en un
pipeline `filter` / `map` / `collect` lisible et idiomatique.

## Modèle fourni (ne pas modifier)

```java
public enum Grade { SOLDAT, CAPORAL, SERGENT, ADJUDANT, LIEUTENANT }
// ordre décroissant de grade = ordre de déclaration (enum Comparable)

public record Soldat(String nom, Grade grade, int anciennete) { }
// anciennete = années de service (>= 0)
```

## Énoncé

Complétez la classe `Effectifs` :

```java
// Renvoie les noms des soldats dont le grade est >= min, dans l'ordre source.
static List<String> nomsDesGradesAuMoins(List<Soldat> soldats, Grade min)

// Renvoie la sous-liste des soldats satisfaisant le critère (Predicate).
static List<Soldat> filtrer(List<Soldat> soldats, Predicate<Soldat> critere)
```

## Exemple

```text
List<Soldat> troupe = List.of(
    new Soldat("Dupont", Grade.SOLDAT,    2),
    new Soldat("Martin", Grade.SERGENT,   8),
    new Soldat("Leroy",  Grade.LIEUTENANT, 15)
);

nomsDesGradesAuMoins(troupe, Grade.SERGENT)
    // -> ["Martin", "Leroy"]

filtrer(troupe, s -> s.anciennete() > 5)
    // -> [Martin, Leroy]
```

## Contraintes

- Package `etnc.m4`. **Ne modifiez pas** `Grade`, `Soldat` ni les signatures.
- Utilisez un pipeline `stream()` / `filter` / `map` / `collect` idiomatique.
- `filtrer` accepte n'importe quel `Predicate<Soldat>` — ne codez pas le critère en dur.
- Si aucun élément ne satisfait le critère, renvoyez une **liste vide** (jamais `null`).
- La liste source ne doit **pas être modifiée**.

## Ce qui sera vérifié

- `nomsDesGradesAuMoins` renvoie les noms des soldats de grade au moins égal à `min`, dans l'ordre source.
- `filtrer` avec un lambda passé par le test : ancienneté, grade, etc.
- Cas limites : aucun élément ne satisfait → liste vide ; tous satisfont → tous renvoyés.

## Pour aller plus loin (optionnel — non noté)

- Quelle différence entre `Collectors.toList()` et `Stream.toList()` (Java 16+) ?
- Comment chaîner plusieurs `filter` successifs ?
