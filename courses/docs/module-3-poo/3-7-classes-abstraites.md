---
id: 3-7-classes-abstraites
sidebar_position: 7
title: "Classes abstraites"
description: "Forcer la spécialisation avec abstract : classe non instanciable, méthodes abstraites et concrètes."
---

# Classes abstraites

## Pourquoi ce chapitre

Reprenons l'héritage. Une classe `Paiement` générale a-t-elle un sens en tant qu'objet concret ? Non : on ne paie jamais « en général », mais toujours **par un moyen précis** (carte, espèces…). Pourtant, `Paiement` reste utile comme base commune et comme type pour le polymorphisme.

La **classe abstraite** répond exactement à ce besoin : une classe qui sert de socle commun, qu'on ne peut pas instancier directement, et qui **force** les sous-classes à fournir certaines opérations.

## Ce que vous saurez faire à la fin

- **Déclarer** une classe `abstract` non instanciable.
- **Définir** des méthodes abstraites que les sous-classes doivent implémenter.
- **Combiner** méthodes abstraites et concrètes dans une même classe.

## 1. Une classe qu'on ne peut pas instancier

Une classe déclarée `abstract` ne peut pas être créée avec `new` : elle n'existe que pour être **héritée**. Elle représente un concept général dont seules les versions spécialisées sont concrètes.

```java
public abstract class Paiement {
    // ...
}

// Paiement p = new Paiement();   // INTERDIT : classe abstraite
```

### À retenir

> - Une classe `abstract` ne s'instancie pas avec `new`.
> - Elle sert de **base commune** à des sous-classes concrètes.

## 2. La méthode abstraite

Une **méthode abstraite** est déclarée sans corps : seule sa signature est donnée, suivie d'un point-virgule. Elle dit « toute sous-classe concrète **doit** fournir cette opération », sans imposer comment.

### Exemple

```java
public abstract class Paiement {
    // Pas de corps : chaque moyen de paiement calcule son montant à sa façon.
    public abstract double montant();
}
```

Une classe qui contient (ou hérite) une méthode abstraite non implémentée doit elle-même être déclarée `abstract`.

### À retenir

> - Une méthode `abstract` n'a pas de corps : la sous-classe l'implémente.
> - Une classe avec une méthode abstraite non implémentée est elle-même abstraite.

## 3. Mélanger abstrait et concret

Tout l'intérêt : une classe abstraite peut contenir à la fois des méthodes **abstraites** (à compléter) et des méthodes **concrètes** (comportement commun, déjà écrit), ainsi que des attributs. Le code commun est mutualisé ; seules les parties variables sont laissées aux enfants.

### Exemple

```java
public abstract class Paiement {
    protected String libelle;

    public Paiement(String libelle) {
        this.libelle = libelle;
    }

    public abstract double montant();         // varie selon le moyen

    public String recu() {                    // commun à tous : déjà écrit
        return libelle + " : " + montant() + " €";
    }
}

public class PaiementEspeces extends Paiement {
    private double somme;

    public PaiementEspeces(double somme) {
        super("Espèces");
        this.somme = somme;
    }

    @Override
    public double montant() {
        return somme;
    }
}
```

`recu()` est écrit une seule fois dans la classe abstraite et appelle `montant()`, dont la version dépend du type réel (polymorphisme). Chaque sous-classe ne fournit que ce qui lui est propre.

### À retenir

> - Une classe abstraite peut mêler méthodes abstraites, méthodes concrètes et attributs.
> - On factorise le commun, on force la spécialisation du reste.

## 4. Quand choisir une classe abstraite

On choisit une classe abstraite, plutôt qu'une classe ordinaire, quand un concept de base **n'a pas de sens à l'état pur** mais regroupe un comportement commun à ses variantes. Elle combine réutilisation (le code concret partagé) et contrainte (les méthodes abstraites à implémenter).

### À retenir

> - Classe abstraite = base commune qui factorise **et** force la spécialisation.
> - Pertinente quand l'objet « générique » n'existe pas concrètement.

## Erreurs fréquentes

- **Instancier une classe abstraite** : `new Paiement(...)` ne compile pas. Symptôme : erreur « Paiement is abstract; cannot be instantiated ». Correction : instancier une sous-classe concrète.
- **Oublier d'implémenter une méthode abstraite** : une sous-classe qui n'implémente pas toutes les méthodes abstraites héritées doit être déclarée `abstract` à son tour, sinon le code ne compile pas.
- **Tout rendre abstrait** : si tout est abstrait et qu'il n'y a aucun comportement commun concret, une **interface** (chapitre suivant) est sans doute plus adaptée.

## Exercice guidé

**Objectif** : écrire une classe abstraite avec une méthode abstraite et une concrète.

Écrivez `abstract class Paiement` avec un attribut `libelle`, une méthode **abstraite** `montant()` et une méthode **concrète** `recu()` qui renvoie une description utilisant `montant()`. Créez deux sous-classes, `PaiementCarte` et `PaiementEspeces`, qui implémentent `montant()`. Testez `recu()` sur chacune.

Indices :
- `recu()` appelle `montant()` : grâce au polymorphisme, c'est la bonne version qui s'exécute.
- Chaque sous-classe a son propre attribut (somme, plafond…) et son constructeur appelant `super(...)`.

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

```java
public abstract class Paiement {
    protected String libelle;

    public Paiement(String libelle) {
        this.libelle = libelle;
    }

    public abstract double montant();

    public String recu() {
        return libelle + " : " + montant() + " €";
    }
}

public class PaiementCarte extends Paiement {
    private double somme;

    public PaiementCarte(double somme) {
        super("Carte");
        this.somme = somme;
    }

    @Override
    public double montant() {
        return somme;
    }
}

public class PaiementEspeces extends Paiement {
    private double somme;

    public PaiementEspeces(double somme) {
        super("Espèces");
        this.somme = somme;
    }

    @Override
    public double montant() {
        return somme;
    }
}

public class Demo {
    public static void main(String[] args) {
        Paiement p1 = new PaiementCarte(49.90);
        Paiement p2 = new PaiementEspeces(20.0);
        System.out.println(p1.recu());   // Carte : 49.9 €
        System.out.println(p2.recu());   // Espèces : 20.0 €
    }
}
```

</details>

## Vérifiez vos acquis

- Pourquoi une classe abstraite ne peut-elle pas être instanciée ?
- Quelle différence entre une méthode abstraite et une méthode concrète ?
- Que doit faire une sous-classe concrète d'une classe abstraite ?

## Pour aller plus loin

- [Abstract Classes in Java](https://www.baeldung.com/java-abstract-class) (Baeldung) — classes et méthodes abstraites.
- [Abstract Methods and Classes](https://dev.java/learn/inheritance/abstract-classes/) (dev.java) — quand et comment les utiliser.

## Prochain chapitre

→ **[Chapitre 3.8 — Interfaces](3-8-interfaces)**
