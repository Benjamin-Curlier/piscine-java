---
id: 3-5-heritage
sidebar_position: 5
title: "Héritage"
description: "Réutiliser et spécialiser une classe avec extends, super, @Override, protected et final."
---

# Héritage

## Pourquoi ce chapitre

Imaginez deux classes `Chien` et `Chat`. Toutes deux ont un nom, un âge, savent manger et dormir. Si vous les écrivez séparément, vous recopiez le même code deux fois. Et s'il faut corriger la méthode `manger`, vous devez le faire partout.

L'**héritage** résout cela : on place le code commun dans une classe « parente » (`Animal`), et chaque classe « enfant » en hérite, puis ajoute ou adapte ce qui lui est propre. C'est l'un des piliers de la POO.

## Ce que vous saurez faire à la fin

- **Faire hériter** une classe d'une autre avec `extends`.
- **Appeler** le parent avec `super`.
- **Redéfinir** une méthode héritée et l'annoter `@Override`.
- **Utiliser** `protected` et `final` à bon escient.

## 1. Réutiliser et spécialiser avec `extends`

Une classe peut **hériter** d'une autre avec `extends`. La classe enfant (sous-classe) récupère les attributs et méthodes de la classe parente (super-classe), puis les complète. L'héritage exprime une relation **« est un »** : un chien **est un** animal.

### Exemple

```java
public class Animal {
    protected String nom;

    public void manger() {
        System.out.println(nom + " mange.");
    }
}

public class Chien extends Animal {
    public void aboyer() {
        System.out.println(nom + " aboie.");   // nom est hérité d'Animal
    }
}
```

`Chien` dispose de `manger()` sans le réécrire, et ajoute `aboyer()`. Un objet `Chien` peut donc faire les deux.

### À retenir

> - `class Enfant extends Parent` fait hériter `Enfant` du `Parent`.
> - L'héritage exprime une relation **« est un »**.
> - La sous-classe récupère les membres du parent et peut en ajouter.

## 2. Appeler le parent avec `super`

Le mot-clé `super` désigne la partie « parente » de l'objet. Deux usages :

- `super(...)` appelle un **constructeur** du parent. S'il est présent, il doit être la **première** instruction du constructeur enfant.
- `super.methode()` appelle la version **parente** d'une méthode.

### Exemple

```java
public class Animal {
    protected String nom;

    public Animal(String nom) {
        this.nom = nom;
    }
}

public class Chien extends Animal {
    private String race;

    public Chien(String nom, String race) {
        super(nom);        // initialise la partie Animal
        this.race = race;  // puis la partie propre à Chien
    }
}
```

### À retenir

> - `super(...)` appelle le constructeur du parent (en première instruction).
> - `super.methode()` appelle la version parente d'une méthode.

## 3. Redéfinir une méthode avec `@Override`

Une sous-classe peut **redéfinir** (en anglais *override*) une méthode héritée pour l'adapter. On annote alors la méthode avec `@Override` : ce n'est pas obligatoire, mais fortement recommandé, car le compilateur vérifie qu'on redéfinit bien une méthode existante.

### Exemple

```java
public class Animal {
    protected String nom;

    public Animal(String nom) {
        this.nom = nom;
    }

    public String crier() {
        return nom + " fait un bruit.";
    }
}

public class Chien extends Animal {
    public Chien(String nom) {
        super(nom);
    }

    @Override
    public String crier() {       // adapte le comportement hérité
        return nom + " aboie.";
    }
}
```

L'intérêt de `@Override` : si vous écrivez `crir()` par erreur, sans l'annotation Java créerait une **nouvelle** méthode (une surcharge accidentelle) et le parent continuerait d'être appelé. Avec `@Override`, le compilateur signale aussitôt la faute.

### À retenir

> - **Redéfinir** : la sous-classe fournit sa propre version d'une méthode héritée.
> - `@Override` fait vérifier par le compilateur qu'on redéfinit bien — annotez toujours.

## 4. `protected`, `final` et la classe `Object`

Trois compléments utiles :

- **`protected`** : un membre `protected` est visible par la classe **et ses sous-classes** (intermédiaire entre `private` et `public`). C'est pratique pour un attribut que les enfants doivent utiliser, comme `nom` ci-dessus.
- **`final`** : sur une **classe**, il interdit d'en hériter ; sur une **méthode**, il interdit de la redéfinir.
- **`Object`** : toute classe hérite implicitement de la classe `Object`, racine de toute la hiérarchie Java. C'est de là que vient, par exemple, la méthode `toString()`, que vous pouvez redéfinir pour décrire vos objets.

> **Note de conception** : l'héritage convient quand la relation est vraiment « est un ». Pour un « a un » (une voiture **a un** moteur), on préfère la **composition** : la classe contient un attribut du type voulu plutôt que d'en hériter.

### À retenir

> - `protected` : visible par la classe et ses sous-classes.
> - `final` interdit l'héritage (classe) ou la redéfinition (méthode).
> - Toute classe hérite d'`Object` (d'où `toString()`).

## Erreurs fréquentes

- **Oublier `super(...)`** : si le parent n'a pas de constructeur sans argument, le constructeur enfant **doit** appeler `super(...)` en première instruction, sinon le code ne compile pas.
- **Redéfinir sans `@Override` et se tromper de signature** : une faute de frappe (`crir` au lieu de `crier`, ou un paramètre différent) crée une surcharge accidentelle ; le parent reste appelé et le bug est silencieux. L'annotation `@Override` l'évite.
- **Utiliser l'héritage pour un « a un »** : faire hériter `Voiture` de `Moteur` est une erreur de conception. Une voiture **a un** moteur (composition), elle n'**est pas** un moteur.
- **Tenter d'hériter d'une classe `final`** : impossible, par définition. Le compilateur refuse.

## Exercice guidé

**Objectif** : créer une hiérarchie et redéfinir une méthode.

Écrivez une classe `Animal` avec un attribut `nom` et une méthode `crier()` renvoyant un cri générique. Créez ensuite `Chien` et `Chat` qui **héritent** d'`Animal` et **redéfinissent** `crier()` (avec `@Override`) pour renvoyer un cri spécifique. Testez les trois depuis un `main`.

Indices :
- Donnez à `Animal` un constructeur `Animal(String nom)` et appelez `super(nom)` dans les enfants.
- `crier()` renvoie une `String` ; affichez-la dans `main`.

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

```java
public class Animal {
    protected String nom;

    public Animal(String nom) {
        this.nom = nom;
    }

    public String crier() {
        return nom + " fait un bruit.";
    }
}

public class Chien extends Animal {
    public Chien(String nom) {
        super(nom);
    }

    @Override
    public String crier() {
        return nom + " aboie.";
    }
}

public class Chat extends Animal {
    public Chat(String nom) {
        super(nom);
    }

    @Override
    public String crier() {
        return nom + " miaule.";
    }
}

public class Demo {
    public static void main(String[] args) {
        Animal animal = new Animal("L'animal");
        Chien chien = new Chien("Rex");
        Chat chat = new Chat("Felix");

        System.out.println(animal.crier());   // L'animal fait un bruit.
        System.out.println(chien.crier());    // Rex aboie.
        System.out.println(chat.crier());     // Felix miaule.
    }
}
```

</details>

## Vérifiez vos acquis

- Que récupère une sous-classe de sa super-classe ?
- À quoi servent `super(...)` et `super.methode()` ?
- Pourquoi annoter une redéfinition avec `@Override` ?
- Quelle différence de visibilité entre `protected` et `private` ?

## Pour aller plus loin

- [Inheritance](https://dev.java/learn/inheritance/) (dev.java) — héritage, `super` et redéfinition.
- [Inheritance in Java](https://www.baeldung.com/java-inheritance) (Baeldung) — `extends`, `protected`, `final`.

## Prochain chapitre

→ **[Chapitre 3.6 — Polymorphisme](3-6-polymorphisme)**
