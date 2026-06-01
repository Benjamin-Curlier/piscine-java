---
id: 2-4-methodes
sidebar_position: 4
title: "Méthodes"
description: "Écrire ses propres méthodes : signature, paramètres, type de retour, surcharge, portée des variables et rôle de static."
---

# Méthodes

## Pourquoi ce chapitre

Vous avez déjà **appelé** des méthodes : `System.out.println(...)`, `Arrays.toString(...)`, `message.toUpperCase()`. Quelqu'un les a écrites pour vous. Il est temps d'écrire les **vôtres**.

Une **méthode** est un bloc de code nommé, que l'on peut appeler à volonté. Elle évite de répéter les mêmes instructions, donne un nom clair à une opération, et découpe un programme en morceaux compréhensibles. C'est l'aboutissement du conseil « gardez des blocs courts » vu au [chapitre 1.7](../module-1-fondamentaux/1-7-bonnes-pratiques-lisibilite). Ce chapitre vous apprend à les écrire.

## Ce que vous saurez faire à la fin

- **Écrire** une méthode avec sa signature, ses paramètres et son type de retour.
- **Distinguer** une méthode `void` d'une méthode qui renvoie une valeur.
- **Surcharger** une méthode (même nom, paramètres différents).
- **Expliquer** la portée des variables et le rôle de `static`.

## 1. Pourquoi écrire des méthodes

Imaginez un programme qui calcule trois fois la moyenne de deux notes, à des endroits différents. Sans méthode, vous recopiez le calcul trois fois : trois occasions de vous tromper, et trois corrections si la formule change.

Une méthode résout cela : on écrit le calcul **une fois**, on lui donne un nom, et on l'appelle autant que nécessaire. Le code devient plus court, plus sûr, et plus lisible.

### À retenir

> - Une méthode évite de répéter du code et nomme une intention.
> - Écrite une fois, elle s'appelle autant de fois qu'on veut.

## 2. Anatomie d'une méthode

Une méthode se compose d'une **signature** et d'un **corps**. La signature annonce comment l'appeler ; le corps fait le travail.

```java
static int doubler(int valeur) {
    return valeur * 2;
}
```

Décortiquons la signature `static int doubler(int valeur)` :

- `static` : un **modificateur** (expliqué en section 7).
- `int` : le **type de retour** — le type de la valeur renvoyée.
- `doubler` : le **nom** de la méthode.
- `(int valeur)` : la liste des **paramètres** — les données fournies à l'appel.

### À retenir

> - La **signature** décrit l'appel : modificateurs, type de retour, nom, paramètres.
> - Le **corps**, entre accolades, contient les instructions.

## 3. Renvoyer une valeur, ou non

Une méthode peut renvoyer un résultat, ou ne rien renvoyer.

Si elle renvoie une valeur, son type de retour indique le type attendu, et l'instruction `return` fournit cette valeur. `return` **arrête immédiatement** la méthode.

Si elle ne renvoie rien, son type de retour est `void` (« vide »). Elle agit (afficher, par exemple) sans produire de valeur.

### Exemple

```java
// Renvoie une valeur de type int.
static int somme(int a, int b) {
    return a + b;   // arrête la méthode et renvoie le résultat
}

// Ne renvoie rien : type de retour void.
static void saluer(String nom) {
    System.out.println("Bonjour " + nom);
}
```

### À retenir

> - Type de retour + `return valeur;` : la méthode **renvoie** un résultat.
> - `void` : la méthode **agit** sans rien renvoyer.
> - `return` arrête la méthode sur-le-champ.

## 4. Appeler une méthode

Pour appeler une méthode, on écrit son nom suivi des **arguments** entre parenthèses. Si elle renvoie une valeur, on récupère le résultat.

### Exemple

```java
public class Demo {

    static int somme(int a, int b) {
        return a + b;
    }

    public static void main(String[] args) {
        int total = somme(3, 4);          // on récupère la valeur renvoyée
        System.out.println(total);        // 7
        System.out.println(somme(10, 5)); // on peut l'utiliser directement : 15
    }
}
```

Les valeurs `3` et `4` sont les **arguments** ; à l'intérieur de `somme`, elles deviennent les paramètres `a` et `b`.

### À retenir

> - On appelle avec `nomMethode(arguments)`.
> - Si la méthode renvoie une valeur, on peut la stocker ou l'utiliser directement.

## 5. Le passage de paramètres se fait par valeur

En Java, les arguments sont passés **par valeur** : la méthode reçoit une **copie**. Modifier un paramètre de type primitif (`int`, `double`, `boolean`…) à l'intérieur de la méthode **ne change pas** la variable d'origine.

```java
static void tenterDeModifier(int x) {
    x = 999;   // modifie seulement la copie locale
}

public static void main(String[] args) {
    int n = 5;
    tenterDeModifier(n);
    System.out.println(n);   // affiche toujours 5
}
```

Un cas mérite attention : pour un **tableau**, c'est la *référence* qui est copiée. La méthode et l'appelant désignent alors le même tableau, donc la méthode **peut** en modifier le contenu.

```java
static void mettreAZero(int[] t) {
    t[0] = 0;   // modifie bien le tableau d'origine
}
```

### À retenir

> - Les arguments sont passés **par valeur** (une copie).
> - Modifier un **primitif** dans la méthode ne change pas l'original.
> - Pour un **tableau**, la méthode peut modifier le contenu (référence partagée).

## 6. La surcharge

Java autorise plusieurs méthodes de **même nom**, à condition que leurs **paramètres** diffèrent (par leur nombre ou leur type). C'est la **surcharge** (en anglais *overloading*). Java choisit la bonne version selon les arguments fournis.

### Exemple

```java
static int max(int a, int b) {
    return (a > b) ? a : b;
}

// Même nom, mais trois paramètres : c'est une surcharge.
static int max(int a, int b, int c) {
    return max(max(a, b), c);   // réutilise la version à deux paramètres
}
```

Attention : la surcharge se joue sur les **paramètres**, pas sur le type de retour. Deux méthodes qui ne diffèrent que par leur type de retour ne compilent pas.

### À retenir

> - **Surcharger** : même nom, **paramètres** différents (nombre ou type).
> - Le type de retour seul ne suffit **pas** à distinguer deux méthodes.

## 7. Portée des variables et `static`

Une variable déclarée dans une méthode (ou dans un bloc) n'existe **que** là : c'est sa **portée**. En dehors, elle est inconnue. Deux méthodes peuvent donc utiliser un paramètre nommé `a` sans interférer : ce sont des variables distinctes.

Quant au mot-clé `static`, il permet d'appeler la méthode **sans créer d'objet** au préalable. Pour l'instant, tous nos programmes tiennent dans une classe avec un `main`, et toutes les méthodes que vous écrivez sont `static`. La notion d'objet — et les méthodes non `static` — arrive au module suivant.

### À retenir

> - Une variable n'existe que dans la méthode ou le bloc où elle est déclarée.
> - `static` permet d'appeler la méthode sans créer d'objet (objets : module 3).

## Erreurs fréquentes

- **Oublier le `return`** : une méthode à type de retour non `void` doit renvoyer une valeur sur tous ses chemins, sinon le code ne compile pas.
- **Type de retour incohérent** : renvoyer un `double` dans une méthode déclarée `int` provoque une erreur. Le type renvoyé doit correspondre.
- **Croire qu'on a modifié un primitif** : modifier un paramètre `int` dans la méthode ne change pas la variable de l'appelant (passage par valeur).
- **Surcharge sur le seul type de retour** : `int f()` et `double f()` ne peuvent coexister ; la surcharge porte sur les paramètres.
- **Variable hors de sa portée** : utiliser une variable en dehors de la méthode où elle est déclarée → erreur de compilation.

## Exercice guidé

**Objectif** : écrire une méthode `max` pour deux entiers, puis la surcharger pour trois.

Écrivez `static int max(int a, int b)` qui renvoie le plus grand des deux. Puis surchargez-la en `static int max(int a, int b, int c)` qui renvoie le plus grand des trois, **en réutilisant** la première version. Testez les deux depuis `main`.

Indices :
- Pour deux valeurs, un opérateur ternaire `(a > b) ? a : b` suffit.
- Pour trois, appelez deux fois la version à deux paramètres : `max(max(a, b), c)`.

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

```java
public class Maximum {

    static int max(int a, int b) {
        return (a > b) ? a : b;
    }

    // Surcharge : même nom, trois paramètres. Réutilise la version à deux.
    static int max(int a, int b, int c) {
        return max(max(a, b), c);
    }

    public static void main(String[] args) {
        System.out.println(max(3, 7));        // 7
        System.out.println(max(4, 9, 2));     // 9
    }
}
```

</details>

## Vérifiez vos acquis

- Que contient la signature d'une méthode ?
- Que signifie « passage par valeur » pour un paramètre `int` ?
- Deux méthodes peuvent-elles porter le même nom ? À quelle condition ?
- À quoi sert le mot-clé `static` dans nos programmes actuels ?

## Pour aller plus loin

- [Methods](https://dev.java/learn/language-basics/methods/) (dev.java) — définir et appeler des méthodes.
- [Method Overloading and Overriding in Java](https://www.baeldung.com/java-method-overload-override) (Baeldung) — la surcharge en détail.

## Prochain chapitre

→ **[Chapitre 2.5 — Récursivité](2-5-recursivite)**
