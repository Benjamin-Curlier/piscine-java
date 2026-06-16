# Exercice 4.3.2 — Réductions numériques (streams)

## Contexte

L'état-major veut des statistiques rapides sur un groupe de membres : combien
sont-ils, quelle est leur ancienneté moyenne, quel est le maximum d'ancienneté ?
Ces calculs doivent fonctionner **même sur une liste vide** — on attend alors une
**valeur-sentinelle** (0 ou 0.0) plutôt qu'une exception.

## Domaine fourni

```java
public enum Niveau { JUNIOR, CONFIRME, SENIOR, LEAD, PRINCIPAL }

public record Membre(String nom, Niveau niveau, int anciennete) { }
// anciennete = années de service (>= 0)
```

Ces deux classes sont **fournies** dans le starter. Ne les modifiez pas.

## Énoncé

Complétez la classe `Statistiques` :

```java
/** Renvoie le nombre de membres dans la liste. */
static int total(List<Membre> membres)

/** Renvoie l'ancienneté moyenne ; 0.0 si la liste est vide. */
static double ancienneteMoyenne(List<Membre> membres)

/** Renvoie l'ancienneté maximale ; 0 si la liste est vide. */
static int ancienneteMax(List<Membre> membres)
```

## Démarche attendue

Utilisez les streams Java pour ces réductions :

- `total` : `membres.size()` (ou `.stream().count()`)
- `ancienneteMoyenne` : `membres.stream().mapToInt(Membre::anciennete).average().orElse(0.0)`
- `ancienneteMax` : `membres.stream().mapToInt(Membre::anciennete).max().orElse(0)`

Le mot-clé `orElse` gère le cas de la liste vide sans lever d'exception.

## Exemple

```text
List<Membre> equipe = List.of(
    new Membre("Dupont", Niveau.CONFIRME, 3),
    new Membre("Martin", Niveau.SENIOR, 7),
    new Membre("Legrand", Niveau.JUNIOR, 1)
);

Statistiques.total(equipe)             // 3
Statistiques.ancienneteMoyenne(equipe) // 3.666...
Statistiques.ancienneteMax(equipe)     // 7

Statistiques.total(List.of())             // 0
Statistiques.ancienneteMoyenne(List.of()) // 0.0
Statistiques.ancienneteMax(List.of())     // 0
```

## Contraintes

- Package `piscine.m4`. **Ne modifiez pas** `Niveau`, `Membre`, ni les signatures.
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
