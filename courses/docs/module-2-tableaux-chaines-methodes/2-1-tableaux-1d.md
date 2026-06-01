---
id: 2-1-tableaux-1d
sidebar_position: 1
title: "Tableaux 1D"
description: "Déclarer, initialiser et parcourir un tableau à une dimension, utiliser length et afficher avec Arrays.toString."
---

# Tableaux 1D

## Pourquoi ce chapitre

Jusqu'ici, une variable contenait **une** valeur : un âge, une note, un nom. Mais comment stocker les notes de tir d'une section entière sans déclarer `note1`, `note2`, `note3`… jusqu'à `note30` ?

La réponse est le **tableau** (en anglais *array*) : une seule variable qui range plusieurs valeurs du même type, accessibles par leur position. Ce chapitre vous apprend à créer un tableau à une dimension, à lire et modifier ses éléments, à le parcourir avec une boucle, et à l'afficher proprement.

## Ce que vous saurez faire à la fin

- **Déclarer** et **créer** un tableau à une dimension.
- **Accéder** à un élément par son indice et le modifier.
- **Parcourir** un tableau avec une boucle `for` ou `for-each`.
- **Afficher** le contenu d'un tableau avec `Arrays.toString`.

## 1. Qu'est-ce qu'un tableau

Un **tableau** est une collection de **taille fixe** : il contient un nombre d'éléments décidé à sa création, qui ne change plus ensuite. Tous les éléments sont du **même type** (que des `int`, que des `double`, etc.).

Chaque élément a une **position**, appelée **indice** (en anglais *index*). Point crucial : les indices commencent à **0**. Le premier élément est à l'indice `0`, le deuxième à l'indice `1`, et ainsi de suite.

### À retenir

> - Un tableau range plusieurs valeurs du **même type**.
> - Sa taille est **fixe** : décidée à la création, elle ne change plus.
> - Les indices commencent à **0**.

## 2. Déclarer et créer un tableau

On déclare un tableau en ajoutant des crochets `[]` au type. On le crée de deux façons.

**Avec `new`** : on réserve une taille, et Java remplit le tableau de valeurs par défaut (`0` pour les nombres entiers).

```java
int[] notes = new int[5];   // 5 cases, toutes à 0 au départ
```

**Avec un littéral** : on fournit directement les valeurs entre accolades. Java compte les éléments pour fixer la taille.

```java
int[] notes = {12, 8, 15, 17, 9};   // un tableau de 5 entiers, déjà remplis
```

### Exemple

```java
// Notes de tir d'un groupe de 4 stagiaires, sur 20.
int[] notes = {14, 11, 18, 9};

// On peut aussi créer un tableau vide puis le remplir case par case.
double[] temperatures = new double[3];
temperatures[0] = 21.5;
temperatures[1] = 19.0;
temperatures[2] = 23.2;
```

### À retenir

> - `int[] t = new int[5];` crée 5 cases remplies de la valeur par défaut (`0`).
> - `int[] t = {12, 8, 15};` crée et remplit le tableau d'un coup.

## 3. Accéder aux éléments

On lit ou on modifie un élément en indiquant son indice entre crochets.

### Exemple

```java
int[] notes = {14, 11, 18, 9};

System.out.println(notes[0]);   // affiche 14 (premier élément)
System.out.println(notes[2]);   // affiche 18 (troisième élément)

notes[3] = 12;                  // on remplace 9 par 12
System.out.println(notes[3]);   // affiche 12
```

L'indice `notes[0]` désigne la première case. Une erreur classique est de croire qu'elle est à l'indice `1`.

### À retenir

> - `t[i]` lit l'élément à l'indice `i`.
> - `t[i] = valeur;` modifie cet élément.
> - Le premier élément est `t[0]`, pas `t[1]`.

## 4. La taille avec `length`

Pour connaître le nombre d'éléments, on utilise `length`. Attention : pour un tableau, `length` est un **attribut**, écrit **sans parenthèses**.

```java
int[] notes = {14, 11, 18, 9};
System.out.println(notes.length);   // affiche 4
```

Retenez bien cette écriture sans parenthèses. Plus tard, pour une chaîne de caractères, on écrira `chaine.length()` **avec** parenthèses : ce sont deux choses différentes. Nous y reviendrons au chapitre sur les chaînes.

Le dernier élément d'un tableau est toujours à l'indice `length - 1` (ici, `notes[3]`).

### À retenir

> - `t.length` donne le nombre d'éléments (**sans** parenthèses).
> - Le dernier élément valide est `t[t.length - 1]`.

## 5. Parcourir un tableau

Parcourir, c'est passer sur chaque élément. Java propose deux boucles pour cela.

**La boucle `for` indexée** : on fait varier un indice `i` de `0` à `length - 1`. À privilégier quand on a besoin de la **position** (pour modifier, ou pour afficher le numéro).

```java
int[] notes = {14, 11, 18, 9};

for (int i = 0; i < notes.length; i++) {
    System.out.println("Note n°" + (i + 1) + " : " + notes[i]);
}
```

Notez la condition `i < notes.length` (et non `<=`) : comme le dernier indice valide est `length - 1`, s'arrêter à `length` provoquerait une erreur.

**La boucle `for-each`** : on parcourt directement les valeurs, sans gérer d'indice. Plus simple à lire quand la position n'a pas d'importance.

```java
int[] notes = {14, 11, 18, 9};
int somme = 0;

for (int note : notes) {   // « pour chaque note dans notes »
    somme += note;
}
System.out.println("Total : " + somme);   // 52
```

### À retenir

> - `for (int i = 0; i < t.length; i++)` : quand vous avez besoin de l'**indice**.
> - `for (int e : t)` : quand seule la **valeur** compte. Plus lisible.

## 6. Afficher un tableau

Afficher directement un tableau ne donne pas son contenu, mais une suite de caractères illisible (une adresse interne).

```java
int[] notes = {14, 11, 18, 9};
System.out.println(notes);   // affiche par exemple [I@1b6d3586 : inutilisable
```

Pour voir le contenu, Java fournit l'outil `Arrays.toString`. Il faut d'abord l'importer.

### Exemple

```java
import java.util.Arrays;   // en haut du fichier

public class AffichageTableau {
    public static void main(String[] args) {
        int[] notes = {14, 11, 18, 9};
        System.out.println(Arrays.toString(notes));   // affiche [14, 11, 18, 9]
    }
}
```

### À retenir

> - Afficher un tableau directement donne une valeur illisible.
> - `Arrays.toString(t)` affiche son contenu : `[14, 11, 18, 9]`.
> - Pensez à `import java.util.Arrays;` en haut du fichier.

## Erreurs fréquentes

- **`ArrayIndexOutOfBoundsException`** : vous accédez à un indice qui n'existe pas, souvent `t[t.length]`. Le dernier indice valide est `t.length - 1` → corrigez la borne de votre boucle (`i < t.length`).
- **Confondre `length` et `length()`** : un tableau utilise `t.length` (sans parenthèses). Les parenthèses sont réservées aux chaînes (`chaine.length()`).
- **Afficher le tableau au lieu de son contenu** : `System.out.println(t)` ne montre rien d'utile → utilisez `Arrays.toString(t)`.
- **Croire que la taille peut changer** : un tableau créé avec 5 cases en aura toujours 5. Pour une taille variable, il faudra d'autres outils, vus plus tard.

## Exercice guidé

**Objectif** : calculer la somme des éléments d'un tableau, puis afficher le tableau.

On vous donne `int[] effectifs = {12, 8, 15, 9, 11};` (le nombre de personnes dans cinq sections). Calculez le total, affichez-le, puis affichez le tableau complet avec `Arrays.toString`.

Indices :
- Déclarez `int total = 0;` **avant** la boucle.
- Une boucle `for-each` suffit pour la somme, puisque la position n'importe pas.
- N'oubliez pas l'import pour `Arrays.toString`.

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

```java
import java.util.Arrays;

public class SommeEffectifs {
    public static void main(String[] args) {
        int[] effectifs = {12, 8, 15, 9, 11};

        // for-each : on additionne chaque valeur, sans avoir besoin de l'indice.
        int total = 0;
        for (int effectif : effectifs) {
            total += effectif;
        }

        System.out.println("Total : " + total);                  // 55
        System.out.println("Détail : " + Arrays.toString(effectifs));  // [12, 8, 15, 9, 11]
    }
}
```

</details>

## Vérifiez vos acquis

- À partir de quel indice commence un tableau ?
- Quelle est la différence d'écriture entre la taille d'un tableau et la longueur d'une chaîne ?
- Que se passe-t-il si vous accédez à `t[t.length]` ?
- Quand préférer une boucle `for-each` à une boucle `for` indexée ?

## Pour aller plus loin

- [Arrays](https://dev.java/learn/language-basics/arrays/) (dev.java) — déclaration et usage des tableaux.
- [Class Arrays](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/Arrays.html) (Javadoc 25) — toutes les méthodes utilitaires, dont `toString`.

## Prochain chapitre

→ **[Chapitre 2.2 — Tableaux 2D](2-2-tableaux-2d)**
