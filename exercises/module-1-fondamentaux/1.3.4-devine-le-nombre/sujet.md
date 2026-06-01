# Exercice 1.3.4 — Devine le nombre

## Contexte

Un petit jeu pour mettre en pratique les boucles : l'ordinateur a « choisi » un nombre entre 1 et 100, et le joueur doit le deviner. À chaque essai, le programme indique si la cible est plus grande ou plus petite.

Le nombre secret est **déjà tiré pour vous** dans le squelette (avec une graine fixe, pour que la correction soit reproductible). Vous n'avez donc pas à vous occuper du tirage : concentrez-vous sur la **boucle de jeu**.

## Énoncé

Complétez la méthode `main` pour, **tant que le joueur n'a pas trouvé** :

1. Lire un essai au clavier (un entier).
2. Afficher :
   - `C'est plus grand.` si l'essai est **inférieur** au secret ;
   - `C'est plus petit.` si l'essai est **supérieur** au secret ;
   - `Bravo, vous avez trouvé en N essai(s) !` quand l'essai **vaut** le secret (`N` = nombre total d'essais), puis arrêter.

## Exemple

**Exécution attendue** (le joueur tape `50`, puis `75`, puis le bon nombre) :

```text
Devinez le nombre (entre 1 et 100) :
C'est plus petit.
C'est plus grand.
Bravo, vous avez trouvé en 3 essai(s) !
```

## Contraintes

- La classe doit s'appeler `DevineLeNombre` et rester dans le package `etnc.m1`.
- **Ne modifiez ni la graine, ni la ligne du secret** déjà fournies.
- Comptez le nombre d'essais et affichez-le dans le message final.
- Respectez exactement les trois phrases de sortie.

## Ce qui sera vérifié

- Votre programme **compile** sans erreur.
- Les messages « plus grand » / « plus petit » sont corrects selon l'essai.
- Le nombre d'essais affiché à la victoire est exact.
- La boucle s'arrête bien quand le nombre est trouvé.

## Pour aller plus loin (optionnel — non noté)

- Que se passe-t-il si le joueur ne trouve jamais ? Comment limiteriez-vous le nombre d'essais ?
- Comment réagiriez-vous à une saisie hors de l'intervalle 1–100 ?
