---
id: 3-1-classes-et-objets
sidebar_position: 1
title: "Classes et objets"
description: "Définir une classe, créer des instances avec new, comprendre attributs, méthodes, référence d'objet et null."
---

# Classes et objets

## Pourquoi ce chapitre

Jusqu'ici, vos programmes manipulaient des données séparées : un `int` par-ci, un `String` par-là, parfois un tableau. Mais un livre, ce n'est pas un titre **et** un nombre de pages flottant chacun dans son coin : c'est un tout cohérent. La **programmation orientée objet** (POO) permet justement de regrouper des données et les opérations qui vont avec dans une même unité.

Ce chapitre pose la première pierre : la différence entre une **classe** (un plan) et un **objet** (une chose construite à partir du plan). Tout le module 3 en découle.

## Ce que vous saurez faire à la fin

- **Distinguer** une classe d'une instance (un objet).
- **Définir** une classe avec ses attributs et ses méthodes.
- **Créer** des objets avec `new` et accéder à leurs membres.
- **Expliquer** ce qu'est une référence et ce que vaut `null`.

## 1. Une classe est un plan, un objet en est une réalisation

Une **classe** décrit un type de chose : quelles informations elle contient et ce qu'elle sait faire. C'est un **plan**, comme le plan d'architecte d'une maison. Le plan n'est pas une maison : on ne peut pas habiter dedans.

Un **objet** (on dit aussi une **instance**) est une chose concrète construite à partir du plan. À partir d'un même plan, on construit autant de maisons que l'on veut, chacune indépendante.

### À retenir

> - Une **classe** est un plan : un type que vous définissez.
> - Un **objet** (ou **instance**) est une chose créée à partir de ce plan.
> - Une classe, plusieurs objets : chacun est indépendant.

## 2. Attributs et méthodes

Une classe regroupe deux choses :

- des **attributs** (ou champs) : les données, l'**état** de l'objet ;
- des **méthodes** : les opérations, le **comportement** de l'objet.

### Exemple

```java
public class Livre {
    // Attributs : l'état d'un livre.
    String titre;
    int pages;

    // Méthode : un comportement.
    void decrire() {
        System.out.println(titre + " (" + pages + " pages)");
    }
}
```

Cette classe `Livre` dit : « un livre a un titre, un nombre de pages, et sait se décrire ». Elle ne crée encore aucun livre — c'est un plan.

### À retenir

> - Les **attributs** portent l'état (les données) de l'objet.
> - Les **méthodes** portent le comportement (les opérations).

## 3. Créer un objet avec `new`

Pour construire un objet à partir de la classe, on utilise le mot-clé `new`. Chaque `new` crée un objet **distinct**. On accède ensuite à ses attributs et méthodes avec le point (`.`).

### Exemple

```java
public class Demo {
    public static void main(String[] args) {
        Livre premier = new Livre();   // un premier objet Livre
        premier.titre = "Le Cid";
        premier.pages = 120;

        Livre second = new Livre();    // un second objet, indépendant
        second.titre = "Candide";
        second.pages = 96;

        premier.decrire();   // Le Cid (120 pages)
        second.decrire();    // Candide (96 pages)
    }
}
```

`premier` et `second` sont deux objets séparés : modifier l'un ne touche pas l'autre.

### À retenir

> - `new NomClasse()` construit un nouvel objet.
> - On accède aux membres avec le point : `objet.attribut`, `objet.methode()`.

## 4. Une variable objet est une référence

Une variable de type objet ne contient pas l'objet lui-même : elle contient une **référence**, c'est-à-dire une **poignée** qui désigne l'objet. C'est une différence majeure avec les types primitifs (`int`, `double`…).

Conséquence importante : affecter une variable objet à une autre **ne copie pas l'objet**. Les deux variables désignent alors le **même** objet (on parle d'**alias**).

### Exemple

```java
Livre a = new Livre();
a.titre = "Le Cid";

Livre b = a;            // b et a désignent le MÊME objet
b.titre = "Candide";    // on modifie l'objet via b...

System.out.println(a.titre);   // ...donc a.titre vaut aussi "Candide"
```

Pour obtenir un second livre indépendant, il faut un nouveau `new Livre()`, pas une simple affectation.

### À retenir

> - Une variable objet contient une **référence** (une poignée), pas l'objet.
> - `b = a` ne copie pas l'objet : `a` et `b` désignent le même objet (alias).

## 5. `null` et l'état par défaut

Une référence qui ne désigne aucun objet vaut `null`. Tenter d'utiliser un membre sur une référence `null` provoque une **`NullPointerException`** à l'exécution.

À l'inverse, un objet fraîchement créé avec `new` a ses attributs initialisés à des **valeurs par défaut** : `0` pour les nombres, `false` pour un `boolean`, et `null` pour les références (dont les `String`).

### Exemple

```java
Livre livre;                       // déclaré mais non initialisé : vaut null
// livre.decrire();                // NullPointerException : aucun objet derrière

livre = new Livre();               // maintenant livre désigne un objet
System.out.println(livre.pages);   // 0 : valeur par défaut d'un int
System.out.println(livre.titre);   // null : valeur par défaut d'une String
```

### À retenir

> - Une référence sans objet vaut `null` ; l'utiliser lève une `NullPointerException`.
> - Après `new`, les attributs valent leurs valeurs par défaut (`0`, `false`, `null`).

## Erreurs fréquentes

- **Confondre la classe et l'objet** : la classe `Livre` est le plan ; `new Livre()` crée un objet. On ne « décrit » pas la classe, on décrit un objet précis.
- **Oublier `new`** : `Livre l;` déclare une référence mais ne crée aucun objet. Tant qu'on n'a pas écrit `new`, `l` vaut `null`.
- **`NullPointerException`** : appeler une méthode ou lire un attribut sur une référence `null`. Symptôme : le programme s'arrête sur cette ligne. Cause : aucun objet derrière la variable. Correction : créer l'objet avec `new` avant de l'utiliser.
- **Croire que `b = a` copie l'objet** : c'est un alias. Les deux variables pointent vers le même objet ; modifier l'un se voit via l'autre.

## Exercice guidé

**Objectif** : définir une classe simple et en créer deux instances.

Écrivez une classe `Livre` avec deux attributs (`titre`, `pages`) et une méthode `decrire()` qui affiche le titre suivi du nombre de pages. Dans un `main`, créez **deux** livres différents et appelez `decrire()` sur chacun.

Indices :
- Les attributs se déclarent directement dans la classe, hors de toute méthode.
- Chaque objet a besoin de son propre `new Livre()`.

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

```java
public class Livre {
    String titre;
    int pages;

    void decrire() {
        System.out.println(titre + " (" + pages + " pages)");
    }

    public static void main(String[] args) {
        Livre premier = new Livre();
        premier.titre = "Le Cid";
        premier.pages = 120;

        Livre second = new Livre();
        second.titre = "Candide";
        second.pages = 96;

        premier.decrire();   // Le Cid (120 pages)
        second.decrire();    // Candide (96 pages)
    }
}
```

</details>

## Vérifiez vos acquis

- Quelle est la différence entre une classe et un objet ?
- Que contient réellement une variable de type objet ?
- Que vaut une référence non initialisée, et que se passe-t-il si on l'utilise ?
- Après `b = a`, modifier `b` change-t-il `a` ? Pourquoi ?

## Pour aller plus loin

- [Classes](https://dev.java/learn/classes-objects/) (dev.java) — définir des classes, attributs et méthodes.
- [Objects in Java](https://www.baeldung.com/java-classes-objects) (Baeldung) — classes, objets et références.

## Prochain chapitre

→ **[Chapitre 3.2 — Encapsulation](3-2-encapsulation)**
