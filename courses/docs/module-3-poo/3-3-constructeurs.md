---
id: 3-3-constructeurs
sidebar_position: 3
title: "Constructeurs"
description: "Initialiser un objet valide dès sa création : constructeur par défaut, paramétré, surcharge et chaînage this(...)."
---

# Constructeurs

## Pourquoi ce chapitre

Aux chapitres précédents, créer un objet se faisait en deux temps : `new Livre()`, puis on remplissait les attributs un par un. Entre les deux, l'objet existait dans un état incomplet (un livre sans titre, avec `0` page). Un objet à moitié construit est une source de bugs.

Le **constructeur** règle ce problème : il garantit qu'un objet naît directement **valide et complet**. C'est lui qui s'exécute quand vous écrivez `new`.

## Ce que vous saurez faire à la fin

- **Écrire** un constructeur paramétré qui initialise un objet.
- **Expliquer** quand le constructeur par défaut existe et quand il disparaît.
- **Surcharger** des constructeurs et les **chaîner** avec `this(...)`.

## 1. À quoi sert un constructeur

Un **constructeur** est un bloc de code appelé automatiquement par `new`, au moment de la création de l'objet. Son rôle : mettre l'objet dans un état valide dès sa naissance. Il porte le **même nom que la classe** et n'a **pas de type de retour** (pas même `void`).

### À retenir

> - Le constructeur s'exécute à chaque `new`.
> - Il porte le nom de la classe et n'a pas de type de retour.
> - Son rôle : garantir un objet valide dès sa création.

## 2. Le constructeur par défaut

Si vous n'écrivez **aucun** constructeur, Java en fournit un implicitement : le **constructeur par défaut**, sans argument, qui ne fait rien de particulier. C'est lui qui permettait `new Livre()` jusqu'ici.

Point crucial : dès que vous écrivez **votre propre** constructeur, ce constructeur par défaut **disparaît**. Si vous voulez encore pouvoir écrire `new Livre()`, il faudra le déclarer explicitement.

### À retenir

> - Sans constructeur écrit, Java fournit un constructeur par défaut sans argument.
> - Dès qu'on écrit un constructeur, le constructeur par défaut **disparaît**.

## 3. Le constructeur paramétré

Le plus utile est le constructeur **paramétré** : il reçoit les valeurs initiales en arguments. On y utilise souvent `this.attribut` pour distinguer l'attribut du paramètre de même nom (la référence `this` sera détaillée au [chapitre 3.4](3-4-this-et-static)).

### Exemple

```java
public class Livre {
    private String titre;
    private int pages;

    // Constructeur paramétré : un livre naît complet.
    public Livre(String titre, int pages) {
        this.titre = titre;   // this.titre = attribut ; titre = paramètre
        this.pages = pages;
    }
}
```

L'appel devient direct et sûr : `new Livre("Le Cid", 120)` crée un livre déjà complet, sans étape intermédiaire. On peut aussi y valider les arguments, comme pour l'encapsulation du chapitre précédent.

### À retenir

> - Un constructeur paramétré reçoit les valeurs initiales en arguments.
> - `this.attribut` désigne l'attribut, le distinguant d'un paramètre de même nom.

## 4. Surcharger des constructeurs

Comme pour les méthodes (vu au [chapitre 2.4](../module-2-tableaux-chaines-methodes/2-4-methodes)), on peut écrire **plusieurs constructeurs** de signatures différentes : c'est la **surcharge**. Java choisit le bon selon les arguments fournis. Cela offre plusieurs façons de créer l'objet.

### Exemple

```java
public class Couleur {
    private int rouge;
    private int vert;
    private int bleu;

    public Couleur(int rouge, int vert, int bleu) {
        this.rouge = rouge;
        this.vert = vert;
        this.bleu = bleu;
    }

    // Surcharge : une seule valeur pour un niveau de gris.
    public Couleur(int gris) {
        this.rouge = gris;
        this.vert = gris;
        this.bleu = gris;
    }
}
```

### À retenir

> - Plusieurs constructeurs de **paramètres différents** = surcharge.
> - Chaque surcharge offre une autre façon de créer l'objet.

## 5. Chaîner les constructeurs avec `this(...)`

Dans l'exemple précédent, les deux constructeurs répètent la même logique d'affectation. Pour éviter cette duplication, un constructeur peut **en appeler un autre** avec `this(...)`. Cet appel doit être la **première instruction** du constructeur.

### Exemple

```java
public class Couleur {
    private int rouge;
    private int vert;
    private int bleu;

    public Couleur(int rouge, int vert, int bleu) {
        this.rouge = rouge;
        this.vert = vert;
        this.bleu = bleu;
    }

    // Niveau de gris : on délègue au constructeur complet.
    public Couleur(int gris) {
        this(gris, gris, gris);   // appelle Couleur(int, int, int)
    }
}
```

La logique d'affectation n'existe plus qu'à **un seul endroit**. Si elle change, une seule correction suffit.

### À retenir

> - `this(...)` appelle un autre constructeur de la même classe.
> - Il doit être la **première** instruction.
> - Il évite de dupliquer la logique d'initialisation.

## Erreurs fréquentes

- **Croire que le constructeur par défaut subsiste** : après avoir écrit `Livre(String, int)`, l'appel `new Livre()` ne compile plus. Cause : le constructeur par défaut a disparu. Correction : ajouter explicitement un constructeur sans argument si besoin.
- **Dupliquer la logique entre constructeurs** : recopier les affectations dans chaque surcharge. Utilisez `this(...)` pour déléguer.
- **`this(...)` mal placé** : il doit être la toute première instruction du constructeur, sinon erreur de compilation.
- **Confondre `this(...)` et `this.champ`** : `this(...)` appelle un **autre constructeur** ; `this.champ` accède à un **attribut**. Même mot-clé, usages distincts.
- **Donner un type de retour au constructeur** : écrire `public void Livre(...)` en fait une **méthode** nommée `Livre`, pas un constructeur. Un constructeur n'a aucun type de retour.

## Exercice guidé

**Objectif** : surcharger deux constructeurs et les chaîner.

Écrivez une classe `Couleur` avec trois attributs `private` (`rouge`, `vert`, `bleu`). Fournissez un constructeur `Couleur(int rouge, int vert, int bleu)` et un constructeur surchargé `Couleur(int gris)` qui produit un niveau de gris **en chaînant** vers le premier via `this(gris, gris, gris)`.

Indices :
- Le constructeur à un argument ne fait qu'appeler `this(...)`.
- Ajoutez une petite méthode d'affichage pour vérifier.

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

```java
public class Couleur {
    private int rouge;
    private int vert;
    private int bleu;

    public Couleur(int rouge, int vert, int bleu) {
        this.rouge = rouge;
        this.vert = vert;
        this.bleu = bleu;
    }

    public Couleur(int gris) {
        this(gris, gris, gris);   // chaînage vers le constructeur complet
    }

    public void afficher() {
        System.out.println("rgb(" + rouge + ", " + vert + ", " + bleu + ")");
    }

    public static void main(String[] args) {
        Couleur orange = new Couleur(255, 165, 0);
        Couleur gris = new Couleur(128);
        orange.afficher();   // rgb(255, 165, 0)
        gris.afficher();     // rgb(128, 128, 128)
    }
}
```

</details>

## Vérifiez vos acquis

- Quand le constructeur par défaut disparaît-il ?
- À quoi sert `this(...)` dans un constructeur, et où doit-il se trouver ?
- Deux constructeurs peuvent-ils avoir exactement la même signature ?

## Pour aller plus loin

- [A Guide to Constructors in Java](https://www.baeldung.com/java-constructors) (Baeldung) — constructeurs, surcharge et chaînage.
- [Classes](https://dev.java/learn/classes-objects/) (dev.java) — déclarer des constructeurs.

## Prochain chapitre

→ **[Chapitre 3.4 — this et static](3-4-this-et-static)**
