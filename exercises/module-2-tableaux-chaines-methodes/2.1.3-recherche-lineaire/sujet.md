# Exercice 2.1.3 — Recherche linéaire

## Contexte

Dans un fichier de fréquences, vous cherchez à quelle position apparaît une
valeur précise. La méthode la plus simple consiste à parcourir la liste du début
à la fin jusqu'à la trouver.

## Énoncé

Écrivez un programme qui recherche une valeur dans une série d'entiers et indique
sa position.

La saisie se fait au clavier :

1. une première ligne contient `n`, le nombre de valeurs (au moins 1) ;
2. une deuxième ligne contient les `n` entiers, séparés par des espaces ;
3. une troisième ligne contient la valeur recherchée.

Si la valeur est présente, affichez `Indice : <i>` où `i` est sa position, en
**commençant à compter à 0**. Si elle apparaît plusieurs fois, affichez l'indice
de la **première** occurrence. Si elle est absente, affichez `Absent`.

## Exemple

**Exécution attendue** (saisie `3`, puis `10 20 30`, puis `20`) :

```text
Indice : 1
```

## Contraintes

- La classe doit s'appeler `RechercheLineaire` et rester dans le package `etnc.m2`.
- Lisez l'entrée avec un `Scanner`. Tout le code peut tenir dans `main`.
- Les indices commencent à 0 (la première case du tableau est l'indice 0).

## Ce qui sera vérifié

- Le programme trouve la valeur quand elle est présente (début, milieu, fin).
- En cas de doublons, il renvoie bien la première occurrence.
- Il affiche `Absent` quand la valeur n'est pas dans le tableau.

## Pour aller plus loin (optionnel — non noté)

- Si le tableau était **trié**, connaissez-vous une méthode de recherche plus
  rapide ? (recherche dichotomique)
