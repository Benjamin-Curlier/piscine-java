# Exercice 2.1.1 — Min, max et moyenne

## Contexte

Au poste de transmission, vous recevez une série de mesures (par exemple des
niveaux de signal). Avant de transmettre un compte rendu, vous devez en extraire
les valeurs caractéristiques : la plus basse, la plus haute et la moyenne.

## Énoncé

Écrivez un programme qui lit une série d'entiers, puis affiche leur minimum,
leur maximum et leur moyenne.

La saisie se fait au clavier, dans cet ordre :

1. une première ligne contient `n`, le nombre de valeurs (au moins 1) ;
2. une seconde ligne contient les `n` entiers, séparés par des espaces.

Affichez ensuite, une information par ligne :

```
Min : <minimum>
Max : <maximum>
Moyenne : <moyenne>
```

La moyenne est affichée avec **deux décimales** et un **point** comme séparateur
(par exemple `5.00` ou `6.50`).

## Exemple

**Exécution attendue** (l'utilisateur saisit `5` puis `3 9 1 7 5`) :

```text
Min : 1
Max : 9
Moyenne : 5.00
```

## Contraintes

- La classe doit s'appeler `MinMaxMoyenne` et rester dans le package `etnc.m2`.
- Lisez l'entrée avec un `Scanner`. Tout le code peut tenir dans `main`.
- La moyenne doit être calculée en **nombres à virgule** (`double`), sans
  division entière, et affichée à deux décimales avec un point
  (astuce : `String.format(java.util.Locale.ROOT, "%.2f", moyenne)`).
- Le format de sortie doit être exactement celui de l'exemple.

## Ce qui sera vérifié

- Votre programme compile et lit correctement la taille puis les valeurs.
- Le minimum et le maximum sont corrects, y compris avec des valeurs négatives.
- La moyenne est correcte et formatée à deux décimales.
- Le format des trois lignes de sortie est respecté.

## Pour aller plus loin (optionnel — non noté)

- Que se passe-t-il si la somme dépasse la capacité d'un `int` ? Renseignez-vous
  sur le type `long` et la notion de débordement (overflow).
- La classe `java.util.Arrays` propose des outils de tri. Pourriez-vous obtenir
  le min et le max autrement ? Quel serait le coût ?
