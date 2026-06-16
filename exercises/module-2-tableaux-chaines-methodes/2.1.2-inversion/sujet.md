# Exercice 2.1.2 — Inversion d'un tableau

## Contexte

Un message reçu doit parfois être relu à l'envers pour vérification. Vous allez
écrire un petit utilitaire qui inverse l'ordre d'une série de nombres.

## Énoncé

Écrivez un programme qui lit une série d'entiers et les réaffiche dans l'ordre
inverse.

La saisie se fait au clavier :

1. une première ligne contient `n`, le nombre de valeurs (au moins 1) ;
2. une seconde ligne contient les `n` entiers, séparés par des espaces.

Affichez les valeurs **dans l'ordre inverse**, sur une seule ligne, séparées par
un espace.

## Exemple

**Exécution attendue** (l'utilisateur saisit `4` puis `1 2 3 4`) :

```text
4 3 2 1
```

## Contraintes

- La classe doit s'appeler `Inversion` et rester dans le package `piscine.m2`.
- Lisez l'entrée avec un `Scanner`. Tout le code peut tenir dans `main`.
- Les valeurs sont séparées par un seul espace.

## Ce qui sera vérifié

- Votre programme compile et lit correctement la taille puis les valeurs.
- Les valeurs sont affichées dans l'ordre exactement inverse.
- Le programme fonctionne avec une seule valeur, deux valeurs, des doublons et
  des valeurs négatives.

## Pour aller plus loin (optionnel — non noté)

- Sauriez-vous inverser le tableau **en place** (en échangeant les cases), sans
  construire de nouvelle structure ? Combien d'échanges faut-il ?
