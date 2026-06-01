# Exercice 1.3.1 — FizzBuzz

## Contexte

FizzBuzz est un exercice classique pour s'entraîner aux boucles et aux conditions. Sous son apparence anodine, il oblige à bien ordonner les tests : un détail dans l'ordre des `if` change tout le résultat.

## Énoncé

Complétez la méthode `main` pour afficher les nombres de **1 à 100**, un par ligne, en appliquant ces règles :

- multiple de **3** → afficher `Fizz` ;
- multiple de **5** → afficher `Buzz` ;
- multiple de **3 et de 5** (donc de 15) → afficher `FizzBuzz` ;
- sinon → afficher le nombre lui-même.

## Exemple

**Début de l'exécution attendue :**

```text
1
2
Fizz
4
Buzz
Fizz
7
8
Fizz
Buzz
11
Fizz
13
14
FizzBuzz
```

(… et ainsi de suite jusqu'à 100.)

## Contraintes

- La classe doit s'appeler `FizzBuzz` et rester dans le package `etnc.m1`.
- Aucune entrée clavier : la boucle va toujours de 1 à 100.
- Une valeur par ligne.

## Ce qui sera vérifié

- Votre programme **compile** sans erreur.
- Les 100 lignes sont **exactes** (testé en intégralité).
- Le cas `FizzBuzz` (multiples de 15) est correctement traité.

## Pour aller plus loin (optionnel — non noté)

- Pourquoi faut-il tester le multiple de 15 **avant** les multiples de 3 et 5 ? Inversez l'ordre et observez.
- Sauriez-vous éviter de tester `% 15` en réutilisant les deux booléens « multiple de 3 » et « multiple de 5 » ?
