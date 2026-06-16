# Exercice 2.2.3 — ASCII art

## Contexte

Les premiers terminaux ne savaient afficher que du texte. On dessinait alors des
formes avec des caractères : c'est l'« ASCII art ». Vous allez dessiner un
triangle d'étoiles.

## Énoncé

Écrivez un programme qui lit un entier `n` (la hauteur) et affiche un triangle
d'étoiles : la première ligne contient une étoile, la deuxième en contient deux,
et ainsi de suite jusqu'à la ligne `n`.

La saisie se fait au clavier : une seule ligne contenant l'entier `n` (`n ≥ 1`).

## Exemple

**Exécution attendue** (l'utilisateur saisit `3`) :

```text
*
**
***
```

## Contraintes

- La classe doit s'appeler `AsciiArt` et rester dans le package `piscine.m2`.
- Lisez l'entrée avec un `Scanner`. Tout le code peut tenir dans `main`.
- Chaque ligne ne contient que des étoiles (`*`), suivies d'un retour à la ligne.

## Ce qui sera vérifié

- Le triangle a bien `n` lignes.
- La ligne `i` contient exactement `i` étoiles.
- Les cas `n = 1` et des hauteurs plus grandes fonctionnent.

## Pour aller plus loin (optionnel — non noté)

- Sauriez-vous dessiner le triangle **inversé** (la première ligne la plus
  longue) ?
- Et une **pyramide centrée** ? (indice : ajouter des espaces devant les étoiles)
