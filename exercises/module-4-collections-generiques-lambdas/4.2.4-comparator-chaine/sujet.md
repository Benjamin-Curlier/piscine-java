# Exercice 4.2.4 — Tri par chaînage de Comparator

## Contexte

L'état-major veut classer les membres selon plusieurs critères : d'abord par
ordre alphabétique, ensuite par niveau puis par nom, ou encore par niveau en ordre
décroissant. La classe `Comparator` du JDK permet de construire ces règles de
tri par **chaînage**, sans modifier les objets.

## Fourni (ne pas modifier)

```java
public enum Niveau { JUNIOR, CONFIRME, SENIOR, LEAD, PRINCIPAL }
// L'ordre de déclaration définit la hiérarchie croissante.

public record Membre(String nom, Niveau niveau, int anciennete) { }
// Record complet — Membre n'implémente pas Comparable.
```

## Énoncé

Complétez la classe `TriMembres` avec trois méthodes **statiques** :

```java
// Trie par nom (ordre alphabétique).
static List<Membre> parNom(List<Membre> membres)

// Trie d'abord par niveau croissant, puis par nom en cas d'égalité de niveau.
static List<Membre> parNiveauPuisNom(List<Membre> membres)

// Trie par niveau décroissant (PRINCIPAL en premier).
static List<Membre> parNiveauDecroissant(List<Membre> membres)
```

Chaque méthode doit :

1. **Créer une copie** de la liste source (`new ArrayList<>(membres)`).
2. Trier la copie avec `copie.sort(comparator)`.
3. **Renvoyer la copie** — la liste passée en argument ne doit pas être modifiée.

## Exemple

```text
List<Membre> membres = List.of(
    new Membre("Martin",  Niveau.SENIOR,    5),
    new Membre("Dubois",  Niveau.CONFIRME,    2),
    new Membre("Bernard", Niveau.PRINCIPAL, 8)
);

parNom(membres)
// → [Bernard, Dubois, Martin]   (alpha)

parNiveauPuisNom(membres)
// → [Dubois(CONFIRME), Martin(SENIOR), Bernard(PRINCIPAL)]

parNiveauDecroissant(membres)
// → [Bernard(PRINCIPAL), Martin(SENIOR), Dubois(CONFIRME)]
```

## Contraintes

- Package `piscine.m4`. **Ne modifiez pas** `Niveau`, `Membre` ni les signatures.
- Utilisez `Comparator.comparing` (et `thenComparing`, `reversed` selon le cas).
- Les méthodes de `TriMembres` sont **statiques**.
- Cas limite : si `membres` est `null`, renvoyer `null`.

## Ce qui sera vérifié

- `parNom` : ordre alphabétique strict.
- `parNiveauPuisNom` : niveau croissant, départage par nom à niveau égal.
- `parNiveauDecroissant` : niveau décroissant.
- La liste source n'est **pas mutée** par les appels.
- Liste à un seul élément renvoyée inchangée.

## Pour aller plus loin (optionnel — non noté)

- Comment ajouter un troisième critère (ancienneté) sans toucher aux deux
  premiers ?
- Quelle différence entre `Comparator.comparing(Membre::niveau)` et
  `Comparator.naturalOrder()` ici ?
