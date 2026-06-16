# Exercice 4.3.3 — Groupement par collecteurs (streams)

## Contexte

La section des ressources humaines doit produire des tableaux de bord sur la
répartition des membres : combien par niveau, quels membres ont suffisamment
d'ancienneté, etc. Les **collecteurs** `groupingBy` et `partitioningBy` permettent
de construire ces vues en une seule passe sur la liste.

## Modèle fourni (ne pas modifier)

```java
public enum Niveau { JUNIOR, CONFIRME, SENIOR, LEAD, PRINCIPAL }
public record Membre(String nom, Niveau niveau, int anciennete) { }
```

## Énoncé

Complétez la classe `Regroupement` :

```java
// Regroupe les membres par niveau (une clé par niveau présent dans la liste)
static Map<Niveau, List<Membre>> parNiveau(List<Membre> membres)

// Partitionne les membres selon que leur ancienneté >= seuil (true) ou non (false)
// Les deux clés true ET false sont toujours présentes dans le résultat
static Map<Boolean, List<Membre>> selonAnciennete(List<Membre> membres, int seuil)

// Renvoie le nombre de membres par niveau
static Map<Niveau, Long> effectifsParNiveau(List<Membre> membres)
```

Utilisez `stream()` avec les collecteurs `Collectors.groupingBy`,
`Collectors.partitioningBy` et `Collectors.counting()`.

## Exemple

```text
List<Membre> liste = List.of(
    new Membre("Martin", Niveau.SENIOR, 8),
    new Membre("Durand", Niveau.CONFIRME, 3),
    new Membre("Petit",  Niveau.SENIOR, 12)
);

parNiveau(liste).get(Niveau.SENIOR)
    // → [Membre("Martin", SENIOR, 8), Membre("Petit", SENIOR, 12)] (ordre quelconque)

selonAnciennete(liste, 5).get(true)
    // → [Membre("Martin", SENIOR, 8), Membre("Petit", SENIOR, 12)]
selonAnciennete(liste, 5).get(false)
    // → [Membre("Durand", CONFIRME, 3)]

effectifsParNiveau(liste).get(Niveau.SENIOR)  // → 2L
effectifsParNiveau(liste).get(Niveau.CONFIRME)  // → 1L
```

## Contraintes

- Package `piscine.m4`. **Ne modifiez pas** `Niveau`, `Membre`, ni les signatures.
- Liste vide : `parNiveau` renvoie une map **vide** (pas `null`).
- `selonAnciennete` renvoie **toujours** les deux clés `true` et `false`
  (comportement garanti par `partitioningBy`, contrairement à `groupingBy`).
- Aucune exception ne doit être levée.

## Ce qui sera vérifié

- `parNiveau` : les membres sont correctement répartis par niveau.
- `selonAnciennete` : les deux partitions sont correctes ; les deux clés
  existent même si un groupe est vide.
- `effectifsParNiveau` : les comptes par niveau sont exacts.
- Cas limite : liste vide → map vide pour `parNiveau`.

## Pour aller plus loin (optionnel — non noté)

- Quelle différence de comportement observe-t-on entre `groupingBy` et
  `partitioningBy` quand tous les éléments sont dans le même groupe ?
- Comment produire une `Map<Niveau, Set<Membre>>` à la place d'une `List` ?
