# Exercice 4.1.1 — Annuaire de l'équipe (Map)

## Contexte

Un annuaire d'équipe identifie chaque membre par un **indicatif** (ex. : `"ALPHA-7"`).
On veut pouvoir enregistrer, rechercher et supprimer rapidement un correspondant
par son indicatif. La structure adaptée est une `Map<String, String>`.

## Énoncé

Complétez la classe `Annuaire` :

```java
void enregistrer(String indicatif, String nom)
String rechercher(String indicatif)
boolean supprimer(String indicatif)
int taille()
List<String> indicatifsTries()
```

- `enregistrer` associe `nom` à `indicatif` (écrase si déjà présent).
- `rechercher` renvoie le nom associé, ou `"Inconnu"` si l'indicatif est absent.
- `supprimer` retire l'entrée et renvoie `true` ; renvoie `false` si l'indicatif
  était absent (sans lever d'exception).
- `taille` renvoie le nombre d'entrées.
- `indicatifsTries` renvoie la liste de tous les indicatifs triés par ordre
  alphabétique.

## Exemple

```text
Annuaire a = new Annuaire();
a.enregistrer("CHARLIE-3", "Dupont");
a.enregistrer("ALPHA-7",   "Martin");
a.enregistrer("BRAVO-1",   "Leclerc");
a.rechercher("ALPHA-7");      // "Martin"
a.rechercher("DELTA-9");      // "Inconnu"
a.taille();                   // 3
a.indicatifsTries();          // ["ALPHA-7", "BRAVO-1", "CHARLIE-3"]
a.supprimer("BRAVO-1");       // true
a.supprimer("DELTA-9");       // false
```

## Contraintes

- Package `piscine.m4`. **Ne modifiez pas** les signatures publiques.
- Le champ interne `annuaire` est **privé** et non accessible depuis les tests.
- `rechercher` renvoie `"Inconnu"` (jamais `null`) si l'indicatif est absent.
- `supprimer` renvoie `false` sur un indicatif absent, **sans lever d'exception**.
- `indicatifsTries` renvoie une nouvelle liste triée à chaque appel.

## Ce qui sera vérifié

- `enregistrer` puis `rechercher` renvoie bien le nom enregistré.
- `rechercher` d'un indicatif absent renvoie `"Inconnu"`.
- Réenregistrer un indicatif existant écrase l'ancien nom.
- `taille` reflète exactement le nombre d'entrées.
- `indicatifsTries` renvoie les indicatifs dans l'ordre alphabétique strict.
- `supprimer` sur un absent renvoie `false` sans exception.
- `supprimer` sur un présent renvoie `true` et l'entrée disparaît.

## Pour aller plus loin (optionnel — non noté)

- Que se passe-t-il si deux indicatifs différents désignent le même nom ?
  La `Map` le permet — est-ce un problème pour un annuaire d'équipe ?
- Ajoutez une méthode `List<String> nomsTries()` qui trie les **valeurs** (noms).
