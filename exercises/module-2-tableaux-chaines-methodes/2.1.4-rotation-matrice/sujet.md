# Exercice 2.1.4 — Rotation d'une matrice

## Contexte

Sur une carte numérique, faire pivoter une grille d'un quart de tour est une
opération courante. Vous allez faire tourner une matrice carrée de 90° dans le
sens des aiguilles d'une montre.

## Énoncé

Écrivez un programme qui lit une matrice carrée d'entiers et affiche cette
matrice tournée de **90° dans le sens horaire**.

La saisie se fait au clavier :

1. une première ligne contient `n`, le côté de la matrice (au moins 1) ;
2. les `n` lignes suivantes contiennent chacune `n` entiers, séparés par des
   espaces.

Affichez la matrice résultat : `n` lignes de `n` entiers, séparés par un espace.

## Exemple

**Exécution attendue** (saisie `2`, puis `1 2`, puis `3 4`) :

```text
3 1
4 2
```

La première colonne de départ (`1` en haut, `3` en bas) devient la première
ligne d'arrivée, lue de bas en haut : `3 1`.

## Contraintes

- La classe doit s'appeler `RotationMatrice` et rester dans le package `piscine.m2`.
- Lisez l'entrée avec un `Scanner`. Tout le code peut tenir dans `main`.
- La matrice est toujours **carrée** (`n` lignes de `n` colonnes).
- La rotation est de 90° dans le sens **horaire**.

## Ce qui sera vérifié

- Votre programme lit correctement `n` puis les `n × n` valeurs.
- La rotation horaire est correcte (testée sur 2×2 et 3×3).
- Les cas limites fonctionnent : matrice 1×1, valeurs négatives.

## Pour aller plus loin (optionnel — non noté)

- Que donne la rotation appliquée **quatre fois** de suite ? Vérifiez-le.
- Sauriez-vous faire la rotation **en place**, sans seconde matrice ? (plus
  difficile : penser aux échanges « en couches »)
