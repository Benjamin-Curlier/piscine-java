# Exercice 2.2.1 — Palindrome

## Contexte

Un palindrome est un mot qui se lit de la même façon dans les deux sens, comme
`radar` ou `kayak`. C'est un petit jeu de manipulation de chaînes utile pour
s'entraîner à parcourir les caractères.

## Énoncé

Écrivez un programme qui lit un mot et indique s'il s'agit d'un palindrome.

La saisie se fait au clavier : une seule ligne contenant un mot (sans espace).
La comparaison **ignore la casse** : `Radar` est considéré comme un palindrome.

Affichez `oui` si le mot est un palindrome, sinon `non`.

## Exemple

**Exécution attendue** (l'utilisateur saisit `Radar`) :

```text
oui
```

## Contraintes

- La classe doit s'appeler `Palindrome` et rester dans le package `etnc.m2`.
- Lisez l'entrée avec un `Scanner`. Tout le code peut tenir dans `main`.
- La comparaison doit ignorer la casse (`toLowerCase`).
- Affichez exactement `oui` ou `non` (en minuscules).

## Ce qui sera vérifié

- Le programme reconnaît un palindrome simple (`radar`, `kayak`).
- La casse est bien ignorée (`Radar` → `oui`).
- Les cas courts fonctionnent : un seul caractère, deux caractères.
- Un mot non palindrome donne bien `non`.

## Pour aller plus loin (optionnel — non noté)

- La classe `StringBuilder` possède une méthode `reverse()`. Comparez la chaîne
  à son envers : obtenez-vous le même résultat qu'avec votre boucle ?
- Comment traiteriez-vous une **phrase** palindrome (« Ésope reste ici et se
  repose ») en ignorant les espaces et les accents ?
