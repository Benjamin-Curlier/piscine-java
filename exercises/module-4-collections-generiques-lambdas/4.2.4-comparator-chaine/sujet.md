# Exercice 4.2.4 — Tri par chaînage de Comparator

## Contexte

L'état-major veut classer les soldats selon plusieurs critères : d'abord par
ordre alphabétique, ensuite par grade puis par nom, ou encore par grade en ordre
décroissant. La classe `Comparator` du JDK permet de construire ces règles de
tri par **chaînage**, sans modifier les objets.

## Fourni (ne pas modifier)

```java
public enum Grade { SOLDAT, CAPORAL, SERGENT, ADJUDANT, LIEUTENANT }
// L'ordre de déclaration définit la hiérarchie croissante.

public record Soldat(String nom, Grade grade, int anciennete) { }
// Record complet — Soldat n'implémente pas Comparable.
```

## Énoncé

Complétez la classe `TriSoldats` avec trois méthodes **statiques** :

```java
// Trie par nom (ordre alphabétique).
static List<Soldat> parNom(List<Soldat> soldats)

// Trie d'abord par grade croissant, puis par nom en cas d'égalité de grade.
static List<Soldat> parGradePuisNom(List<Soldat> soldats)

// Trie par grade décroissant (LIEUTENANT en premier).
static List<Soldat> parGradeDecroissant(List<Soldat> soldats)
```

Chaque méthode doit :

1. **Créer une copie** de la liste source (`new ArrayList<>(soldats)`).
2. Trier la copie avec `copie.sort(comparator)`.
3. **Renvoyer la copie** — la liste passée en argument ne doit pas être modifiée.

## Exemple

```text
List<Soldat> soldats = List.of(
    new Soldat("Martin",  Grade.SERGENT,    5),
    new Soldat("Dubois",  Grade.CAPORAL,    2),
    new Soldat("Bernard", Grade.LIEUTENANT, 8)
);

parNom(soldats)
// → [Bernard, Dubois, Martin]   (alpha)

parGradePuisNom(soldats)
// → [Dubois(CAPORAL), Martin(SERGENT), Bernard(LIEUTENANT)]

parGradeDecroissant(soldats)
// → [Bernard(LIEUTENANT), Martin(SERGENT), Dubois(CAPORAL)]
```

## Contraintes

- Package `etnc.m4`. **Ne modifiez pas** `Grade`, `Soldat` ni les signatures.
- Utilisez `Comparator.comparing` (et `thenComparing`, `reversed` selon le cas).
- Les méthodes de `TriSoldats` sont **statiques**.
- Cas limite : si `soldats` est `null`, renvoyer `null`.

## Ce qui sera vérifié

- `parNom` : ordre alphabétique strict.
- `parGradePuisNom` : grade croissant, départage par nom à grade égal.
- `parGradeDecroissant` : grade décroissant.
- La liste source n'est **pas mutée** par les appels.
- Liste à un seul élément renvoyée inchangée.

## Pour aller plus loin (optionnel — non noté)

- Comment ajouter un troisième critère (ancienneté) sans toucher aux deux
  premiers ?
- Quelle différence entre `Comparator.comparing(Soldat::grade)` et
  `Comparator.naturalOrder()` ici ?
