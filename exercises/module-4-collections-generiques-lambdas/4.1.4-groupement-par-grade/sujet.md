# Exercice 4.1.4 — Groupement de personnel par grade

## Contexte

Le service des effectifs doit organiser les soldats par grade : chaque grade
dispose de sa propre liste de membres, que l'on construit au fil des affectations.

## Enonce

L'enum `Grade` vous est **fournie** (ne pas modifier) :

```java
public enum Grade { SOLDAT, CAPORAL, SERGENT, ADJUDANT, LIEUTENANT }
```

Completez la classe `GroupesParGrade` :

```java
// Champ interne (deja present) :
private final Map<Grade, List<String>> parGrade = new HashMap<>();

void affecter(Grade grade, String nom)   // ajoute nom a la liste du grade
List<String> membres(Grade grade)        // liste des membres du grade (vide si grade jamais affecte)
Set<Grade> grades()                      // ensemble des grades ayant au moins un membre
int effectif(Grade grade)                // nombre de membres du grade
```

**Indice** : utilisez `computeIfAbsent` pour creer la liste d'un grade la premiere
fois qu'un membre y est affecte, et `getOrDefault` pour lire sans creer d'entree.

## Exemple

```text
GroupesParGrade g = new GroupesParGrade();
g.affecter(Grade.SERGENT, "Dupont");
g.affecter(Grade.SERGENT, "Martin");
g.affecter(Grade.CAPORAL, "Bernard");
g.membres(Grade.SERGENT);   // ["Dupont", "Martin"]
g.grades();                 // {SERGENT, CAPORAL}
g.effectif(Grade.CAPORAL);  // 1
g.membres(Grade.LIEUTENANT); // [] (liste vide, pas null)
```

## Contraintes

- Package `etnc.m4`. **Ne modifiez pas** `Grade` ni les signatures.
- `membres` d'un grade jamais affecte renvoie une **liste vide** (jamais `null`).
- `effectif` d'un grade jamais affecte renvoie **0**.
- **Pas** de `Collectors.groupingBy` (reserve au chapitre 4.8).
- `computeIfAbsent` est l'idiome attendu pour `affecter`.

## Ce qui sera verifie

- `affecter` plusieurs noms au meme grade les accumule dans l'ordre d'insertion.
- `grades()` renvoie exactement les grades ayant au moins un membre.
- `effectif` correspond bien a la taille de la liste du grade.
- Un meme nom affecte deux fois au meme grade apparait deux fois (c'est une `List`).
- `membres` d'un grade jamais affecte renvoie une liste vide (pas `null`).

## Pour aller plus loin (optionnel - non note)

- Comment trier les membres de chaque grade par ordre alphabetique ?
- `Collectors.groupingBy` (chapitre 4.8) permet de construire ce regroupement
  en une seule ligne a partir d'un stream. Comparez avec votre demarche manuelle.
