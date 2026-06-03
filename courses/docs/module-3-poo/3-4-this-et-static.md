---
id: 3-4-this-et-static
sidebar_position: 4
title: "this et static"
description: "Distinguer membre d'instance et membre de classe : this, variables et méthodes static, constantes static final."
---

# this et static

## Pourquoi ce chapitre

Vous avez croisé `this` dans les constructeurs et `static` depuis le module 2 (toutes vos méthodes l'étaient), sans qu'on les explique vraiment. Il est temps de clarifier ces deux mots-clés, car ils répondent à une même question de fond : une donnée ou une opération appartient-elle à **chaque objet**, ou à **la classe** dans son ensemble ?

Bien comprendre cette distinction évite des erreurs de conception fréquentes et explique enfin pourquoi `main` est `static`.

## Ce que vous saurez faire à la fin

- **Utiliser** `this` pour désigner l'objet courant.
- **Distinguer** un membre d'instance d'un membre de classe (`static`).
- **Déclarer** des variables, méthodes et constantes `static`.

## 1. `this`, la référence à l'objet courant

À l'intérieur d'une méthode ou d'un constructeur d'instance, `this` désigne **l'objet sur lequel on travaille**. Son usage le plus courant : lever l'ambiguïté quand un paramètre porte le même nom qu'un attribut.

### Exemple

```java
public class Livre {
    private String titre;

    public void setTitre(String titre) {
        this.titre = titre;   // this.titre = l'attribut ; titre = le paramètre
    }
}
```

Sans `this`, écrire `titre = titre` n'affecterait que le paramètre à lui-même : l'attribut ne changerait pas. `this` peut aussi servir à passer l'objet courant en argument à une autre méthode.

### À retenir

> - `this` désigne l'objet courant à l'intérieur d'une méthode d'instance.
> - `this.attribut` distingue l'attribut d'un paramètre de même nom.

## 2. Membre d'instance ou membre de classe

Reprenons la question de fond. Un **membre d'instance** existe **une fois par objet** : deux livres ont chacun leur propre `titre`. À l'inverse, un **membre de classe**, déclaré `static`, appartient à **la classe** : il existe en un seul exemplaire, partagé par tous les objets — et même accessible sans aucun objet.

### À retenir

> - Membre **d'instance** : un exemplaire **par objet**.
> - Membre **`static`** (de classe) : un seul exemplaire, **partagé** par tous.

## 3. Variables et méthodes `static`

Une **variable `static`** est commune à toutes les instances : modifiée via un objet, elle change pour tous. Une **méthode `static`** s'appelle sur la classe, sans créer d'objet — comme `Math.max(2, 3)`. Conséquence importante : **une méthode `static` n'a pas de `this`**, puisqu'elle n'est rattachée à aucun objet.

### Exemple

```java
public class Compteur {
    private static int total = 0;   // partagé par toutes les instances

    public Compteur() {
        total = total + 1;          // chaque création incrémente le total commun
    }

    public static int getTotal() {  // méthode de classe : pas de this
        return total;
    }
}
```

```java
new Compteur();
new Compteur();
System.out.println(Compteur.getTotal());   // 2 : appel sur la classe
```

### À retenir

> - Une variable `static` est partagée par toutes les instances.
> - Une méthode `static` s'appelle sur la classe et n'a **pas** de `this`.

## 4. Les constantes `static final`

Une valeur fixe et partagée se déclare `static final` : `static` car commune à la classe, `final` car non modifiable. Par convention, son nom est en `MAJUSCULES_AVEC_UNDERSCORES`.

### Exemple

```java
public class Cercle {
    public static final double PI = 3.14159;   // constante de classe

    private double rayon;

    public Cercle(double rayon) {
        this.rayon = rayon;
    }

    public double aire() {
        return PI * rayon * rayon;
    }
}
```

### À retenir

> - `static final` = constante partagée et non modifiable.
> - Convention de nommage : `EN_MAJUSCULES`.

## 5. Quand `static` est-il justifié ?

`static` se justifie dans trois cas typiques : une **constante** partagée (`PI`), une **méthode utilitaire** sans état (comme celles de `Math`), ou une **donnée commune** à toutes les instances (un compteur global).

Cela éclaire enfin le module 2 : `main` et vos méthodes utilitaires étaient `static` parce qu'**il n'y avait pas encore d'objet** à ce stade — on appelait sans instance. Maintenant que vous créez des objets, la plupart de vos méthodes seront, au contraire, des méthodes d'instance.

### À retenir

> - `static` convient aux constantes, utilitaires sans état et données communes.
> - `main` est `static` car appelé sans objet ; vos méthodes d'objet, elles, ne le sont pas.

## Erreurs fréquentes

- **Accéder à un membre d'instance depuis un contexte `static`** : message « non-static variable/method cannot be referenced from a static context ». Cause : une méthode `static` n'a pas d'objet courant, donc pas d'attribut d'instance sous la main. Correction : passer l'objet en paramètre, ou rendre la méthode non `static`.
- **Abuser de `static`** : transformer des données qui devraient être propres à chaque objet en variables `static` crée un état global partagé, source de bugs difficiles. Réservez `static` aux cas vraiment communs.
- **Oublier `final` sur une constante** : une valeur censée être fixe mais déclarée seulement `static` peut être modifiée par erreur.
- **Croire que `static` signifie « constant »** : `static` parle d'**appartenance à la classe**, pas d'immuabilité. C'est `final` qui empêche la modification.

## Exercice guidé

**Objectif** : utiliser une variable `static` partagée.

Écrivez une classe `Ticket` qui attribue à chaque instance un identifiant **unique** et croissant. Utilisez une variable `static int dernierId` partagée : à chaque construction, incrémentez-la et donnez sa valeur à l'attribut d'instance `id`. Créez trois tickets dans `main` et affichez leurs identifiants.

Indices :
- `dernierId` est `static` (commun à tous) ; `id` est un attribut d'instance (propre à chaque ticket).
- Dans le constructeur : `dernierId = dernierId + 1; this.id = dernierId;`.

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

```java
public class Ticket {
    private static int dernierId = 0;   // partagé par tous les tickets
    private int id;                     // propre à chaque ticket

    public Ticket() {
        dernierId = dernierId + 1;
        this.id = dernierId;
    }

    public int getId() {
        return id;
    }

    public static void main(String[] args) {
        Ticket a = new Ticket();
        Ticket b = new Ticket();
        Ticket c = new Ticket();
        System.out.println(a.getId());   // 1
        System.out.println(b.getId());   // 2
        System.out.println(c.getId());   // 3
    }
}
```

</details>

## Vérifiez vos acquis

- Que désigne `this` dans une méthode d'instance ?
- Quelle différence entre un attribut d'instance et un attribut `static` ?
- Pourquoi une méthode `static` n'a-t-elle pas accès à `this` ?
- À quoi sert `static final` ?

## Pour aller plus loin

- [The static Keyword in Java](https://www.baeldung.com/java-static) (Baeldung) — variables, méthodes et blocs `static`.
- [Classes](https://dev.java/learn/classes-objects/) (dev.java) — membres de classe et d'instance.

## Prochain chapitre

→ **[Chapitre 3.5 — Héritage](3-5-heritage)**
