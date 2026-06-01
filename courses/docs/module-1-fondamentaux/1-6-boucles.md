---
id: 1-6-boucles
sidebar_position: 6
title: "Boucles"
description: "Répéter des instructions avec while, do-while et for, maîtriser break et continue, et choisir la bonne boucle."
---

# Boucles

## Pourquoi ce chapitre

Recopier dix fois la même instruction serait absurde. Quand une action doit se répéter — afficher les nombres de 1 à 100, additionner une série de valeurs, redemander une saisie tant qu'elle est invalide — on utilise une **boucle**.

Ce chapitre présente les trois boucles de Java (`while`, `do-while`, `for`), les mots-clés `break` et `continue`, et surtout comment **choisir** la bonne boucle selon la situation. Les conditions du [chapitre 1.5](1-5-conditions) en sont le cœur : une boucle tourne tant qu'une condition reste vraie.

## Ce que vous saurez faire à la fin

- **Répéter** des instructions avec `while`, `do-while` et `for`.
- **Interrompre** ou **sauter** une itération avec `break` et `continue`.
- **Choisir** la boucle adaptée à un problème donné.
- **Reconnaître** et éviter une boucle infinie.

## 1. La boucle `while`

Une boucle `while` répète un bloc **tant que** sa condition est vraie. La condition est testée **avant** chaque tour : si elle est fausse dès le départ, le bloc n'est jamais exécuté.

### Exemple

```java
int compteur = 1;

while (compteur <= 5) {
    System.out.println("Tour numéro " + compteur);
    compteur++;   // ESSENTIEL : sans cette ligne, la condition reste vraie pour toujours
}
```

Remarquez la ligne `compteur++`. Sans elle, `compteur` resterait à `1`, la condition resterait vraie, et la boucle tournerait **sans fin**. Toute boucle `while` doit faire évoluer sa condition vers la sortie.

### À retenir

> - `while (condition) { ... }` répète tant que la condition est vraie.
> - La condition est testée **avant** chaque tour.
> - Le bloc doit faire évoluer la condition, sinon la boucle est infinie.

## 2. La boucle `do-while`

La boucle `do-while` ressemble à `while`, mais la condition est testée **après** le bloc. Conséquence : le bloc est exécuté **au moins une fois**, même si la condition est fausse d'emblée.

### Exemple

```java
int valeur = 10;

do {
    System.out.println("Exécuté au moins une fois, valeur = " + valeur);
    valeur--;
} while (valeur > 12);   // faux dès le départ, mais le bloc a tourné une fois
```

On l'utilise quand l'action doit avoir lieu avant le premier test — par exemple afficher un menu puis demander un choix.

### À retenir

> - `do { ... } while (condition);` teste la condition **après** le bloc.
> - Le bloc s'exécute donc **au moins une fois**.

## 3. La boucle `for`

La boucle `for` regroupe sur une seule ligne les trois éléments d'une répétition à compteur : l'**initialisation**, la **condition** et l'**incrément**. Elle est idéale quand on connaît le nombre de tours.

### Exemple

```java
// initialisation ; condition ; incrément
for (int i = 1; i <= 5; i++) {
    System.out.println("Ligne " + i);
}
```

À lire ainsi : on part de `i = 1`, on continue tant que `i <= 5`, et après chaque tour on fait `i++`. Tout est visible d'un coup d'œil, ce qui réduit le risque d'oublier l'incrément.

### À retenir

> - `for (init ; condition ; incrément) { ... }` réunit les trois éléments du compteur.
> - À privilégier quand le **nombre de tours est connu**.

## 4. `break` et `continue`

Deux mots-clés ajustent le déroulement d'une boucle :

- `break` **sort** immédiatement de la boucle.
- `continue` **saute** le reste du tour courant et passe directement au tour suivant.

### Exemple

```java
for (int i = 1; i <= 10; i++) {
    if (i == 7) {
        break;        // on quitte la boucle dès que i vaut 7
    }
    if (i % 2 == 0) {
        continue;     // on saute les nombres pairs
    }
    System.out.println(i);   // affiche 1, 3, 5
}
```

### À retenir

> - `break` quitte la boucle entièrement.
> - `continue` passe au tour suivant sans finir le tour courant.

## 5. Choisir la bonne boucle

Les trois boucles peuvent souvent résoudre le même problème, mais l'une est généralement plus naturelle que les autres.

| Situation | Boucle conseillée |
|---|---|
| Nombre de tours connu d'avance (de 1 à N, parcourir un compteur) | `for` |
| Répéter tant qu'une condition tient, sans nombre de tours fixe | `while` |
| Comme `while`, mais l'action doit avoir lieu au moins une fois | `do-while` |

Dans le doute, demandez-vous : « est-ce que je connais le nombre de répétitions ? » Si oui, `for`. Sinon, `while`.

### À retenir

> - Nombre de tours connu → `for`.
> - Condition sans compteur fixe → `while`.
> - Au moins une exécution garantie → `do-while`.

## Erreurs fréquentes

- **Boucle infinie** : la condition ne devient jamais fausse. Vérifiez que le bloc fait bien évoluer la variable testée (`compteur++`, décrément, etc.).
- **Erreur d'un cran (`off-by-one`)** : `for (int i = 1; i < 5; i++)` s'arrête à `4`, pas `5`. Choisissez `<` ou `<=` selon ce que vous voulez vraiment.
- **Modifier le compteur dans le corps de la boucle** : changer `i` à l'intérieur d'un `for` rend le code difficile à suivre. Laissez l'incrément faire son travail.
- **Oublier l'incrément dans un `while`** : c'est la cause la plus courante de boucle infinie.

## Exercice guidé

**Objectif** : calculer la somme cumulée des entiers de 1 à N.

Écrivez un programme qui, pour `int n = 10;`, calcule `1 + 2 + 3 + ... + 10` et affiche le total (ici, `55`). Faites-le avec une boucle `for`, puis, en bonus, refaites-le avec un `while` pour comparer.

Indices :
- Déclarez une variable `int somme = 0;` **avant** la boucle, et ajoutez-y chaque valeur (`somme += i;`).
- Avec `for`, faites varier `i` de `1` à `n` inclus (`i <= n`).

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

```java
public class SommeCumulee {
    public static void main(String[] args) {
        int n = 10;

        // Version for : le nombre de tours est connu (de 1 à n).
        int somme = 0;
        for (int i = 1; i <= n; i++) {
            somme += i;   // on accumule chaque entier dans la somme
        }
        System.out.println("Somme (for) : " + somme);   // 55

        // Version while : même logique, compteur géré à la main.
        int sommeBis = 0;
        int i = 1;
        while (i <= n) {
            sommeBis += i;
            i++;          // sans cette ligne, la boucle serait infinie
        }
        System.out.println("Somme (while) : " + sommeBis);   // 55
    }
}
```

</details>

## Vérifiez vos acquis

- Quelle est la différence entre `while` et `do-while` ?
- Dans quel cas une boucle `for` est-elle le meilleur choix ?
- Qu'est-ce qu'une boucle infinie, et comment l'éviter ?
- Que fait `continue`, par rapport à `break` ?

## Pour aller plus loin

- [Controlling the Flow of Your Code](https://dev.java/learn/language-basics/controlling-flow/) (dev.java) — boucles et branchements.
- [Java Loops](https://www.baeldung.com/java-loops) (Baeldung) — `for`, `while`, `do-while` en détail.

## Prochain chapitre

→ **[Chapitre 1.7 — Bonnes pratiques de lisibilité](1-7-bonnes-pratiques-lisibilite)**
