---
id: 3-9-enums
sidebar_position: 9
title: "Enums"
description: "Définir un type énuméré, lui donner attributs et méthodes, et l'utiliser dans un switch."
---

# Enums

## Pourquoi ce chapitre

Comment représenter un jour de la semaine ? Avec un `int` (0 = lundi, 1 = mardi…) ? Rien n'empêche alors d'écrire `7`, qui ne correspond à aucun jour. Avec un `String` (« lundi ») ? Une faute de frappe (« lndi ») passe inaperçue jusqu'au bug.

Le type **énuméré** (`enum`) résout ce problème : il définit un ensemble **fini et fixe** de valeurs nommées, connues du compilateur. Impossible d'en inventer une autre par erreur.

## Ce que vous saurez faire à la fin

- **Définir** un `enum` listant des constantes nommées.
- **Enrichir** un `enum` avec des attributs et des méthodes.
- **Utiliser** un `enum` dans un `switch`.

## 1. Un ensemble fini de constantes nommées

Un `enum` déclare la liste exhaustive de ses valeurs possibles. Chacune est une constante du type.

### Exemple

```java
public enum JourSemaine {
    LUNDI, MARDI, MERCREDI, JEUDI, VENDREDI, SAMEDI, DIMANCHE
}
```

```java
JourSemaine jour = JourSemaine.LUNDI;
System.out.println(jour);   // LUNDI
```

Le compilateur connaît toutes les valeurs : `JourSemaine.LUNDU` (faute de frappe) ne compile pas. On gagne en sûreté par rapport à un `int` ou un `String`.

### À retenir

> - Un `enum` définit un ensemble **fini et fixe** de constantes nommées.
> - Le compilateur connaît toutes les valeurs : pas de valeur invalide possible.

## 2. Un enum peut avoir des méthodes

Un `enum` est plus qu'une liste : c'est un type à part entière, qui peut porter des **méthodes**. On peut ainsi attacher un comportement à l'ensemble des valeurs.

### Exemple

```java
public enum JourSemaine {
    LUNDI, MARDI, MERCREDI, JEUDI, VENDREDI, SAMEDI, DIMANCHE;

    public boolean estWeekend() {
        return this == SAMEDI || this == DIMANCHE;
    }
}
```

```java
System.out.println(JourSemaine.SAMEDI.estWeekend());   // true
System.out.println(JourSemaine.LUNDI.estWeekend());    // false
```

Noter la comparaison avec `==` : sur un `enum`, c'est l'usage idiomatique (sûr et lisible).

### À retenir

> - Un `enum` peut définir des méthodes qui s'appliquent à ses constantes.
> - On compare deux valeurs d'enum avec `==`.

## 3. Attributs et constructeur d'enum

Chaque constante peut porter des **données** associées, via un attribut et un constructeur (toujours `private`). On donne alors une valeur à chaque constante entre parenthèses.

### Exemple

```java
public enum Planete {
    MERCURE(3.30e23), TERRE(5.97e24), JUPITER(1.90e27);

    private final double masse;

    Planete(double masse) {     // constructeur (implicitement private)
        this.masse = masse;
    }

    public double getMasse() {
        return masse;
    }
}
```

```java
System.out.println(Planete.TERRE.getMasse());   // 5.97E24
```

### À retenir

> - Une constante d'enum peut porter des données (attribut + constructeur `private`).
> - On donne la valeur de chaque constante entre parenthèses, à la déclaration.

## 4. `switch` sur un enum

Le `switch` se marie très bien avec les enums : on branche selon la constante. La méthode `values()` permet de parcourir toutes les valeurs.

### Exemple

```java
public static String activite(JourSemaine jour) {
    return switch (jour) {
        case SAMEDI, DIMANCHE -> "Repos";
        default -> "Travail";
    };
}

// Parcourir toutes les valeurs :
for (JourSemaine jour : JourSemaine.values()) {
    System.out.println(jour + " : " + activite(jour));
}
```

Dans un `switch` sur enum, on écrit directement `SAMEDI` (pas `JourSemaine.SAMEDI`).

### À retenir

> - Un `switch` branche commodément selon une constante d'enum.
> - `values()` parcourt toutes les valeurs ; dans le `switch`, on omet le nom du type.

## Erreurs fréquentes

- **Réinventer un enum avec des `int` ou des `String`** : utiliser `0`, `1`, `2` ou `"lundi"` pour des valeurs fixes perd la sûreté du compilateur. Dès qu'un ensemble de valeurs est fini et connu, un `enum` s'impose.
- **`switch` non exhaustif** : oublier une valeur sans `default` peut laisser des cas non traités. Couvrir tous les cas, ou prévoir un `default`.
- **Comparer avec `equals` au lieu de `==`** : les deux fonctionnent, mais `==` est l'usage idiomatique sur les enums — plus lisible et insensible au `null`.
- **Tenter `new` sur un enum** : impossible. Les constantes sont les **seules** instances ; le constructeur est privé et appelé automatiquement.

## Exercice guidé

**Objectif** : créer un enum avec une méthode et l'utiliser dans un `switch`.

Écrivez un `enum JourSemaine` (les sept jours) avec une méthode `estWeekend()`. Dans un `main`, parcourez `values()` et affichez, pour chaque jour, s'il s'agit d'un jour de repos ou de travail (via un `switch` ou via `estWeekend()`).

Indices :
- `estWeekend()` compare `this` à `SAMEDI` et `DIMANCHE` avec `==`.
- `JourSemaine.values()` donne le tableau de toutes les constantes.

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

```java
public enum JourSemaine {
    LUNDI, MARDI, MERCREDI, JEUDI, VENDREDI, SAMEDI, DIMANCHE;

    public boolean estWeekend() {
        return this == SAMEDI || this == DIMANCHE;
    }
}

public class Demo {
    public static void main(String[] args) {
        for (JourSemaine jour : JourSemaine.values()) {
            String activite = jour.estWeekend() ? "Repos" : "Travail";
            System.out.println(jour + " : " + activite);
        }
    }
}
```

</details>

## Vérifiez vos acquis

- Pourquoi un `enum` est-il plus sûr que des constantes `int` ou `String` ?
- Un `enum` peut-il avoir des méthodes et des attributs ?
- Pourquoi compare-t-on les valeurs d'enum avec `==` ?
- Que renvoie la méthode `values()` ?

## Pour aller plus loin

- [A Guide to Java Enums](https://www.baeldung.com/a-guide-to-java-enums) (Baeldung) — enums simples et enrichis.
- [Enums](https://dev.java/learn/classes-objects/enums/) (dev.java) — déclaration et usages.

## Prochain chapitre

→ **[Chapitre 3.10 — Records et sealed](3-10-records-et-sealed)**
