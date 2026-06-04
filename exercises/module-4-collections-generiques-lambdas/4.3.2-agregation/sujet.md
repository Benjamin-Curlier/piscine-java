# Exercice 4.3.2 — Réductions numériques (streams)

## Contexte

L'état-major veut des statistiques rapides sur un groupe de soldats : combien
sont-ils, quelle est leur ancienneté moyenne, quel est le maximum d'ancienneté ?
Ces calculs doivent fonctionner **même sur une liste vide** — on attend alors une
**valeur-sentinelle** (0 ou 0.0) plutôt qu'une exception.

## Domaine fourni

```java
public enum Grade { SOLDAT, CAPORAL, SERGENT, ADJUDANT, LIEUTENANT }

public record Soldat(String nom, Grade grade, int anciennete) { }
// anciennete = années de service (>= 0)
```

Ces deux classes sont **fournies** dans le starter. Ne les modifiez pas.

## Énoncé

Complétez la classe `Statistiques` :

```java
/** Renvoie le nombre de soldats dans la liste. */
static int total(List<Soldat> soldats)

/** Renvoie l'ancienneté moyenne ; 0.0 si la liste est vide. */
static double ancienneteMoyenne(List<Soldat> soldats)

/** Renvoie l'ancienneté maximale ; 0 si la liste est vide. */
static int ancienneteMax(List<Soldat> soldats)
```

## Démarche attendue

Utilisez les streams Java pour ces réductions :

- `total` : `soldats.size()` (ou `.stream().count()`)
- `ancienneteMoyenne` : `soldats.stream().mapToInt(Soldat::anciennete).average().orElse(0.0)`
- `ancienneteMax` : `soldats.stream().mapToInt(Soldat::anciennete).max().orElse(0)`

Le mot-clé `orElse` gère le cas de la liste vide sans lever d'exception.

## Exemple

```text
List<Soldat> troupe = List.of(
    new Soldat("Dupont", Grade.CAPORAL, 3),
    new Soldat("Martin", Grade.SERGENT, 7),
    new Soldat("Legrand", Grade.SOLDAT, 1)
);

Statistiques.total(troupe)             // 3
Statistiques.ancienneteMoyenne(troupe) // 3.666...
Statistiques.ancienneteMax(troupe)     // 7

Statistiques.total(List.of())             // 0
Statistiques.ancienneteMoyenne(List.of()) // 0.0
Statistiques.ancienneteMax(List.of())     // 0
```

## Contraintes

- Package `etnc.m4`. **Ne modifiez pas** `Grade`, `Soldat`, ni les signatures.
- Aucune exception ne doit être levée, y compris sur liste vide.
- Les méthodes sont `static` ; la classe `Statistiques` n'a pas d'état.

## Ce qui sera vérifié

- `total` renvoie bien la taille de la liste.
- `ancienneteMoyenne` est correcte à 0.001 près (y compris liste vide → 0.0).
- `ancienneteMax` renvoie le maximum (y compris liste vide → 0).
- Aucune `NullPointerException` ni `NoSuchElementException` sur liste vide.

## Pour aller plus loin (optionnel — non noté)

- Ajoutez `ancienneteMin` avec la même logique.
- Que se passe-t-il si vous appelez `.average().getAsDouble()` sans `orElse` sur
  une liste vide ? Essayez pour observer l'exception.
