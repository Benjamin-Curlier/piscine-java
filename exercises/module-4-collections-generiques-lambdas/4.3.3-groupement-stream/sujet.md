# Exercice 4.3.3 — Groupement par collecteurs (streams)

## Contexte

La section des ressources humaines doit produire des tableaux de bord sur la
répartition des soldats : combien par grade, quels soldats ont suffisamment
d'ancienneté, etc. Les **collecteurs** `groupingBy` et `partitioningBy` permettent
de construire ces vues en une seule passe sur la liste.

## Modèle fourni (ne pas modifier)

```java
public enum Grade { SOLDAT, CAPORAL, SERGENT, ADJUDANT, LIEUTENANT }
public record Soldat(String nom, Grade grade, int anciennete) { }
```

## Énoncé

Complétez la classe `Regroupement` :

```java
// Regroupe les soldats par grade (une clé par grade présent dans la liste)
static Map<Grade, List<Soldat>> parGrade(List<Soldat> soldats)

// Partitionne les soldats selon que leur ancienneté >= seuil (true) ou non (false)
// Les deux clés true ET false sont toujours présentes dans le résultat
static Map<Boolean, List<Soldat>> selonAnciennete(List<Soldat> soldats, int seuil)

// Renvoie le nombre de soldats par grade
static Map<Grade, Long> effectifsParGrade(List<Soldat> soldats)
```

Utilisez `stream()` avec les collecteurs `Collectors.groupingBy`,
`Collectors.partitioningBy` et `Collectors.counting()`.

## Exemple

```text
List<Soldat> liste = List.of(
    new Soldat("Martin", Grade.SERGENT, 8),
    new Soldat("Durand", Grade.CAPORAL, 3),
    new Soldat("Petit",  Grade.SERGENT, 12)
);

parGrade(liste).get(Grade.SERGENT)
    // → [Soldat("Martin", SERGENT, 8), Soldat("Petit", SERGENT, 12)] (ordre quelconque)

selonAnciennete(liste, 5).get(true)
    // → [Soldat("Martin", SERGENT, 8), Soldat("Petit", SERGENT, 12)]
selonAnciennete(liste, 5).get(false)
    // → [Soldat("Durand", CAPORAL, 3)]

effectifsParGrade(liste).get(Grade.SERGENT)  // → 2L
effectifsParGrade(liste).get(Grade.CAPORAL)  // → 1L
```

## Contraintes

- Package `etnc.m4`. **Ne modifiez pas** `Grade`, `Soldat`, ni les signatures.
- Liste vide : `parGrade` renvoie une map **vide** (pas `null`).
- `selonAnciennete` renvoie **toujours** les deux clés `true` et `false`
  (comportement garanti par `partitioningBy`, contrairement à `groupingBy`).
- Aucune exception ne doit être levée.

## Ce qui sera vérifié

- `parGrade` : les soldats sont correctement répartis par grade.
- `selonAnciennete` : les deux partitions sont correctes ; les deux clés
  existent même si un groupe est vide.
- `effectifsParGrade` : les comptes par grade sont exacts.
- Cas limite : liste vide → map vide pour `parGrade`.

## Pour aller plus loin (optionnel — non noté)

- Quelle différence de comportement observe-t-on entre `groupingBy` et
  `partitioningBy` quand tous les éléments sont dans le même groupe ?
- Comment produire une `Map<Grade, Set<Soldat>>` à la place d'une `List` ?
