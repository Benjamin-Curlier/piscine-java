# Exercice 2.2.2 — Comptage d'occurrences

## Contexte

En analysant un message, on a souvent besoin de savoir combien de fois un
caractère précis apparaît. Vous allez écrire un petit compteur.

## Énoncé

Écrivez un programme qui compte le nombre d'occurrences d'un caractère dans un
texte.

La saisie se fait au clavier :

1. une première ligne contient le texte (il peut contenir des espaces) ;
2. une seconde ligne contient le caractère recherché.

Le comptage **respecte la casse** : `A` et `a` sont considérés comme différents.

Affichez `Occurrences : <n>` où `n` est le nombre de fois où le caractère
apparaît.

## Exemple

**Exécution attendue** (saisie `mississippi`, puis `s`) :

```text
Occurrences : 4
```

## Contraintes

- La classe doit s'appeler `ComptageOccurrences` et rester dans le package `piscine.m2`.
- Lisez l'entrée avec un `Scanner` (`nextLine` pour le texte et pour le caractère).
- Tout le code peut tenir dans `main`.
- Le comptage respecte la casse.

## Ce qui sera vérifié

- Le comptage est correct sur des cas nominaux (`mississippi`).
- Un caractère absent donne `Occurrences : 0`.
- La casse est bien respectée (`M` et `m` comptés séparément).
- Le texte peut contenir des espaces.

## Pour aller plus loin (optionnel — non noté)

- Comment compteriez-vous les occurrences sans tenir compte de la casse ?
- Sauriez-vous compter un **mot** entier plutôt qu'un caractère (piste :
  `indexOf` en boucle) ?
