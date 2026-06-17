---
id: 2-2-tableaux-2d
sidebar_position: 2
title: "Tableaux 2D"
description: "Manipuler des matrices avec int[][], parcourir en boucles imbriquées, et représenter des grilles ou des images noir et blanc."
---

# Tableaux 2D

## Pourquoi ce chapitre

Un tableau à une dimension range des valeurs en ligne. Mais beaucoup de données s'organisent en **grille** : un damier, une image, un plan quadrillé, un tableur. Pour cela, Java propose le **tableau à deux dimensions**, ou **matrice**.

Ce chapitre prolonge le [chapitre 2.1](2-1-tableaux-1d) : une matrice n'est rien d'autre qu'un tableau dont chaque case est elle-même un tableau. Vous apprendrez à la créer, à accéder à une case par sa ligne et sa colonne, et à la parcourir avec deux boucles imbriquées.

## Ce que vous saurez faire à la fin

- **Déclarer** et **créer** une matrice avec `int[][]`.
- **Accéder** à une case par sa ligne et sa colonne.
- **Distinguer** le nombre de lignes du nombre de colonnes.
- **Parcourir** une matrice avec deux boucles imbriquées.
- **Afficher** une matrice d'un coup avec `Arrays.deepToString`.

## 1. Du tableau 1D à la matrice

Un **tableau à deux dimensions** (type `int[][]`) est un **tableau de tableaux**. On le visualise comme une grille organisée en **lignes** et en **colonnes**.

Par exemple, une grille de 3 lignes et 4 colonnes contient 3 × 4 = 12 cases. Chaque case est repérée par **deux** indices : d'abord la ligne, puis la colonne — tous deux comptés à partir de `0`.

### À retenir

> - Un `int[][]` est un tableau de tableaux, vu comme une grille.
> - Une case se repère par **deux** indices : `[ligne][colonne]`.
> - Lignes et colonnes commencent à `0`.

## 2. Déclarer et créer une matrice

Comme pour les tableaux 1D, deux écritures coexistent.

**Avec `new`** : on indique le nombre de lignes puis de colonnes. Java remplit de `0`.

```java
int[][] grille = new int[3][4];   // 3 lignes, 4 colonnes, tout à 0
```

**Avec un littéral imbriqué** : chaque ligne est un tableau entre accolades, le tout entre accolades.

```java
int[][] image = {
    {1, 0, 1},
    {0, 1, 0},
    {1, 1, 1}
};   // 3 lignes de 3 colonnes
```

### À retenir

> - `new int[3][4]` crée 3 lignes de 4 colonnes, remplies de `0`.
> - Le littéral imbriqué `{{...}, {...}}` crée et remplit la matrice.

## 3. Accéder à une case

On accède à une case avec deux paires de crochets : `m[ligne][colonne]`.

### Exemple

```java
int[][] image = {
    {1, 0, 1},
    {0, 1, 0},
    {1, 1, 1}
};

System.out.println(image[0][0]);   // 1  (ligne 0, colonne 0 : coin haut-gauche)
System.out.println(image[1][2]);   // 0  (ligne 1, colonne 2)

image[0][1] = 1;                   // on allume le pixel ligne 0, colonne 1
System.out.println(image[0][1]);   // 1
```

L'ordre est toujours **ligne d'abord, colonne ensuite**. Inverser les deux est l'erreur la plus fréquente.

### À retenir

> - `m[i][j]` désigne la case ligne `i`, colonne `j`.
> - L'ordre est **ligne, puis colonne** — toujours.

## 4. Les deux dimensions

Une matrice a deux tailles à connaître :

- `m.length` donne le **nombre de lignes**.
- `m[i].length` donne le **nombre de colonnes de la ligne `i`**.

```java
int[][] grille = new int[3][4];

System.out.println(grille.length);      // 3 (nombre de lignes)
System.out.println(grille[0].length);   // 4 (nombre de colonnes de la ligne 0)
```

Cette distinction est logique : puisqu'une matrice est un tableau de tableaux, `m.length` compte les tableaux-lignes, et `m[i].length` compte les éléments d'une ligne.

### À retenir

> - `m.length` : nombre de **lignes**.
> - `m[i].length` : nombre de **colonnes** de la ligne `i`.

## 5. Parcourir une matrice

Pour visiter toutes les cases, on imbrique deux boucles : une boucle **externe** sur les lignes, une boucle **interne** sur les colonnes.

### Exemple

```java
int[][] image = {
    {1, 0, 1},
    {0, 1, 0},
    {1, 1, 1}
};

// Affiche l'image ligne par ligne : 1 = pixel noir, 0 = pixel blanc.
for (int ligne = 0; ligne < image.length; ligne++) {
    for (int colonne = 0; colonne < image[ligne].length; colonne++) {
        System.out.print(image[ligne][colonne] + " ");
    }
    System.out.println();   // saut de ligne après chaque rangée
}
```

La boucle externe avance d'une ligne à la fois ; pour chaque ligne, la boucle interne parcourt toutes ses colonnes. Le `System.out.println()` vide, placé après la boucle interne, passe à la rangée suivante à l'écran.

### À retenir

> - Deux boucles imbriquées : externe sur les lignes, interne sur les colonnes.
> - Un `println()` vide après la boucle interne passe à la rangée suivante.

## 6. Afficher une matrice d'un coup

Comme pour les tableaux 1D (chapitre 2.1), afficher directement une matrice ne donne rien d'utile. Pire : `Arrays.toString` ne suffit pas non plus, car chaque ligne est elle-même un tableau et s'afficherait comme une adresse illisible.

Pour une matrice, Java fournit `Arrays.deepToString`, qui descend **en profondeur** dans les tableaux imbriqués et affiche tout le contenu.

### Exemple

```java
import java.util.Arrays;

public class AffichageMatrice {
    public static void main(String[] args) {
        int[][] image = {
            {1, 0, 1},
            {0, 1, 0},
            {1, 1, 1}
        };
        System.out.println(Arrays.deepToString(image));   // [[1, 0, 1], [0, 1, 0], [1, 1, 1]]
    }
}
```

### À retenir

> - Pour une matrice, `Arrays.toString` ne suffit pas (lignes illisibles).
> - `Arrays.deepToString(m)` descend dans les tableaux imbriqués et affiche tout.

## 7. Une application : la grille noir et blanc

Une image en noir et blanc se représente naturellement par une matrice de `0` (blanc) et de `1` (noir). Un damier, un plan de tir quadrillé, une carte de zones : tous ces cas se ramènent à une grille de nombres que l'on parcourt et que l'on affiche. La matrice est l'outil de base pour ces données en deux dimensions.

### À retenir

> - Beaucoup de données concrètes (image, damier, plan) sont des grilles.
> - Une matrice de `0`/`1` suffit à représenter une image noir et blanc.

## Erreurs fréquentes

- **Inverser ligne et colonne** : écrire `m[colonne][ligne]` au lieu de `m[ligne][colonne]`. Fixez-vous une règle : **ligne d'abord, toujours**.
- **Confondre `m.length` et `m[i].length`** : le premier compte les lignes, le second les colonnes d'une ligne.
- **Supposer que toutes les lignes ont la même longueur** : c'est vrai pour `new int[3][4]`, mais pas garanti en général. Parcourez chaque ligne avec `m[i].length`, pas avec une valeur fixe.
- **Double `ArrayIndexOutOfBoundsException`** : une borne de boucle trop grande, sur les lignes ou sur les colonnes. Vérifiez `< m.length` et `< m[i].length`.

## Exercice guidé

**Objectif** : afficher une matrice ligne par ligne, puis calculer la somme d'une de ses lignes.

On vous donne une grille `int[][] grille = {{3, 5, 2}, {7, 1, 8}, {4, 6, 0}};`. Affichez-la rangée par rangée, puis calculez et affichez la somme de la **deuxième ligne** (indice `1`).

Indices :
- Pour l'affichage, deux boucles imbriquées + un `println()` vide en fin de rangée.
- Pour la somme d'une ligne, une seule boucle suffit : vous parcourez `grille[1]`, qui est un tableau 1D ordinaire.

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

```java
public class SommeLigne {
    public static void main(String[] args) {
        int[][] grille = {
            {3, 5, 2},
            {7, 1, 8},
            {4, 6, 0}
        };

        // Affichage rangée par rangée.
        for (int ligne = 0; ligne < grille.length; ligne++) {
            for (int colonne = 0; colonne < grille[ligne].length; colonne++) {
                System.out.print(grille[ligne][colonne] + " ");
            }
            System.out.println();
        }

        // Somme de la deuxième ligne (indice 1) : c'est un tableau 1D.
        int somme = 0;
        for (int colonne = 0; colonne < grille[1].length; colonne++) {
            somme += grille[1][colonne];
        }
        System.out.println("Somme de la ligne 1 : " + somme);   // 7 + 1 + 8 = 16
    }
}
```

</details>

## Vérifiez vos acquis

- Que représente `grille[1][2]` ?
- Comment obtient-on le nombre de colonnes d'une matrice ?
- Pourquoi parcourt-on une matrice avec deux boucles imbriquées ?
- Quelle est l'erreur la plus fréquente quand on accède à une case ?

## Pour aller plus loin

- [Multi-Dimensional Arrays in Java](https://www.baeldung.com/java-multi-dimensional-arrays) (Baeldung) — matrices et tableaux de tableaux.
- [Arrays](https://dev.java/learn/language-basics/arrays/) (dev.java) — section sur les tableaux à plusieurs dimensions.

## Prochain chapitre

→ **[Chapitre 2.3 — Chaînes de caractères](2-3-chaines-de-caracteres)**
