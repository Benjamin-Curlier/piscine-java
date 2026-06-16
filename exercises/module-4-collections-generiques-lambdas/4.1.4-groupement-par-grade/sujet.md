# Exercice 4.1.4 — Groupement de personnel par niveau

## Contexte

Le service des effectifs doit organiser les membres par niveau : chaque niveau
dispose de sa propre liste de membres, que l'on construit au fil des affectations.

## Enonce

L'enum `Niveau` vous est **fournie** (ne pas modifier) :

```java
public enum Niveau { JUNIOR, CONFIRME, SENIOR, LEAD, PRINCIPAL }
```

Completez la classe `GroupesParNiveau` :

```java
// Champ interne (deja present) :
private final Map<Niveau, List<String>> parNiveau = new HashMap<>();

void affecter(Niveau niveau, String nom)   // ajoute nom a la liste du niveau
List<String> membres(Niveau niveau)        // liste des membres du niveau (vide si niveau jamais affecte)
Set<Niveau> niveaux()                      // ensemble des niveaux ayant au moins un membre
int effectif(Niveau niveau)                // nombre de membres du niveau
```

**Indice** : utilisez `computeIfAbsent` pour creer la liste d'un niveau la premiere
fois qu'un membre y est affecte, et `getOrDefault` pour lire sans creer d'entree.

## Exemple

```text
GroupesParNiveau g = new GroupesParNiveau();
g.affecter(Niveau.SENIOR, "Dupont");
g.affecter(Niveau.SENIOR, "Martin");
g.affecter(Niveau.CONFIRME, "Bernard");
g.membres(Niveau.SENIOR);   // ["Dupont", "Martin"]
g.niveaux();                 // {SENIOR, CONFIRME}
g.effectif(Niveau.CONFIRME);  // 1
g.membres(Niveau.PRINCIPAL); // [] (liste vide, pas null)
```

## Contraintes

- Package `piscine.m4`. **Ne modifiez pas** `Niveau` ni les signatures.
- `membres` d'un niveau jamais affecte renvoie une **liste vide** (jamais `null`).
- `effectif` d'un niveau jamais affecte renvoie **0**.
- **Pas** de `Collectors.groupingBy` (reserve au chapitre 4.8).
- `computeIfAbsent` est l'idiome attendu pour `affecter`.

## Ce qui sera verifie

- `affecter` plusieurs noms au meme niveau les accumule dans l'ordre d'insertion.
- `niveaux()` renvoie exactement les niveaux ayant au moins un membre.
- `effectif` correspond bien a la taille de la liste du niveau.
- Un meme nom affecte deux fois au meme niveau apparait deux fois (c'est une `List`).
- `membres` d'un niveau jamais affecte renvoie une liste vide (pas `null`).

## Pour aller plus loin (optionnel - non note)

- Comment trier les membres de chaque niveau par ordre alphabetique ?
- `Collectors.groupingBy` (chapitre 4.8) permet de construire ce regroupement
  en une seule ligne a partir d'un stream. Comparez avec votre demarche manuelle.
