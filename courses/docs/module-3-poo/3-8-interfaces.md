---
id: 3-8-interfaces
sidebar_position: 8
title: "Interfaces"
description: "Définir un contrat avec interface, l'implémenter (y compris multiple) et utiliser les méthodes default et static."
---

# Interfaces

## Pourquoi ce chapitre

Une cloche et un klaxon n'ont aucun lien de parenté : l'un n'« est pas un » l'autre, et les faire hériter d'une classe commune n'aurait pas de sens. Pourtant, tous deux **savent émettre un son**. Comment exprimer cette capacité partagée sans héritage forcé ?

L'**interface** répond à ce besoin : elle décrit un **contrat** — un ensemble d'opérations à fournir — que des classes sans lien peuvent s'engager à respecter. C'est le dernier grand outil d'abstraction de ce module.

## Ce que vous saurez faire à la fin

- **Définir** une interface comme un contrat de méthodes.
- **Implémenter** une ou plusieurs interfaces dans une classe.
- **Utiliser** les méthodes `default` et `static` d'une interface.
- **Choisir** entre interface et classe abstraite.

## 1. Une interface est un contrat

Une **interface** déclare un ensemble de méthodes **sans** les implémenter : c'est une liste d'engagements. Une classe qui « signe » ce contrat devra fournir toutes les méthodes annoncées.

```java
public interface Sonore {
    String son();   // contrat : « toute classe Sonore sait produire un son »
}
```

Une interface ne contient pas d'état mutable : ses champs sont implicitement `public static final` (des constantes).

### À retenir

> - Une interface déclare des méthodes **sans** implémentation : un contrat.
> - Elle décrit ce qu'une classe sait faire, pas comment.

## 2. Implémenter une interface

Une classe **implémente** une interface avec le mot-clé `implements`, et doit alors fournir le corps de chaque méthode du contrat.

### Exemple

```java
public class Cloche implements Sonore {
    @Override
    public String son() {
        return "Ding dong";
    }
}
```

On peut ensuite manipuler une `Cloche` à travers le type `Sonore` — comme avec l'héritage, c'est du polymorphisme :

```java
Sonore s = new Cloche();
System.out.println(s.son());   // Ding dong
```

### À retenir

> - `class X implements Contrat` engage `X` à fournir toutes les méthodes du contrat.
> - On peut manipuler l'objet via le type de l'interface (polymorphisme).

## 3. L'implémentation multiple

Contrairement à `extends` (une seule super-classe), une classe peut **implémenter plusieurs interfaces** : elle cumule alors plusieurs contrats. C'est une différence majeure avec l'héritage de classe.

### Exemple

```java
public interface Affichable {
    String afficher();
}

// Klaxon respecte deux contrats à la fois.
public class Klaxon implements Sonore, Affichable {
    @Override
    public String son() {
        return "Tut tut";
    }

    @Override
    public String afficher() {
        return "[Klaxon]";
    }
}
```

### À retenir

> - Une classe peut implémenter **plusieurs** interfaces (héritage unique pour les classes).
> - Elle s'engage alors à respecter tous les contrats cumulés.

## 4. Méthodes `default` et `static`

Une interface peut aussi fournir des méthodes **déjà écrites** :

- une méthode `default` propose une implémentation par défaut : les classes qui implémentent l'interface en héritent sans la réécrire (elles peuvent la redéfinir si besoin) ;
- une méthode `static` regroupe un utilitaire lié au contrat, appelé sur l'interface elle-même.

### Exemple

```java
public interface Sonore {
    String son();

    default String sonFort() {              // implémentation par défaut
        return son().toUpperCase() + " !";
    }

    static String description() {           // utilitaire appelé sur l'interface
        return "Objet capable d'émettre un son.";
    }
}
```

`sonFort()` est disponible sur toute classe `Sonore` sans qu'elle l'écrive ; `Sonore.description()` s'appelle directement sur l'interface, sans objet.

### À retenir

> - `default` : une implémentation par défaut héritée par les classes.
> - `static` (sur l'interface) : un utilitaire appelé sur l'interface.

## 5. Interface ou classe abstraite ?

Les deux organisent l'abstraction, mais répondent à des besoins différents :

- une **interface** décrit un **contrat** (« sait faire ») ; elle s'implémente en **multiple** et ne porte pas d'état mutable ;
- une **classe abstraite** ([chapitre 3.7](3-7-classes-abstraites)) est une **base partielle** (« est un ») avec attributs et méthodes concrètes ; l'héritage en est **unique**.

En pratique : pour une capacité partagée par des classes sans parenté, choisissez une interface ; pour un socle commun avec du code et de l'état partagés, une classe abstraite.

### À retenir

> - Interface = contrat, multiple, sans état mutable.
> - Classe abstraite = base partielle (état + code concret), héritage unique.

## Erreurs fréquentes

- **Confondre `implements` et `extends`** : on **implémente** une interface (`implements`), on **hérite** d'une classe (`extends`). Les mélanger ne compile pas.
- **Oublier une méthode du contrat** : une classe qui implémente une interface doit fournir **toutes** ses méthodes (sauf les `default`), sinon elle doit être abstraite.
- **Vouloir un attribut mutable dans une interface** : impossible, les champs d'une interface sont des constantes (`public static final`). L'état appartient aux classes.
- **Croire qu'on n'implémente qu'une interface** : une classe peut en cumuler plusieurs ; c'est même un de leurs grands intérêts.

## Exercice guidé

**Objectif** : définir un contrat et l'implémenter dans deux classes sans lien.

Écrivez une interface `Sonore` avec une méthode `String son()`. Implémentez-la dans deux classes **indépendantes** (sans héritage commun), par exemple `Cloche` et `Klaxon`. Dans un `main`, placez-les dans un tableau `Sonore[]` et affichez le son de chacune.

Indices :
- Les deux classes n'ont aucun `extends` commun : seule l'interface les relie.
- Le tableau est de type `Sonore[]` ; la boucle appelle `son()` polymorphiquement.

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

```java
public interface Sonore {
    String son();
}

public class Cloche implements Sonore {
    @Override
    public String son() {
        return "Ding dong";
    }
}

public class Klaxon implements Sonore {
    @Override
    public String son() {
        return "Tut tut";
    }
}

public class Demo {
    public static void main(String[] args) {
        Sonore[] sources = { new Cloche(), new Klaxon() };
        for (Sonore source : sources) {
            System.out.println(source.son());
        }
        // Ding dong
        // Tut tut
    }
}
```

</details>

## Vérifiez vos acquis

- Quelle différence entre une interface et une classe abstraite ?
- Combien d'interfaces une classe peut-elle implémenter ?
- À quoi sert une méthode `default` dans une interface ?
- Pourquoi une interface ne porte-t-elle pas d'état mutable ?

## Pour aller plus loin

- [Interfaces](https://dev.java/learn/interfaces/) (dev.java) — contrats, implémentation, méthodes `default`.
- [Java Interfaces](https://www.baeldung.com/java-interfaces) (Baeldung) — interface vs classe abstraite.

## Prochain chapitre

→ **[Chapitre 3.9 — Enums](3-9-enums)**
