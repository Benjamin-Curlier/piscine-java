---
id: 5-4-exceptions-personnalisees
sidebar_position: 4
title: "Exceptions personnalisées"
description: "Créer ses propres exceptions métier (extends RuntimeException), les lever et chaîner leur cause."
---

# Exceptions personnalisées

## Pourquoi ce chapitre

Les exceptions du JDK (`IllegalArgumentException`, `NullPointerException`, etc.) sont génériques. Quand votre code gère une règle métier précise — un solde insuffisant, un effectif hors bornes, un format de données invalide — une exception au nom explicite communique l'intention bien plus clairement qu'un message perdu dans une exception standard.

Ce chapitre vous apprend à créer vos propres types d'exception, à les lever avec `throw` (vu au chapitre 5-2) et à enchaîner les causes pour ne pas perdre le contexte d'erreur d'origine.

## Ce que vous saurez faire à la fin

- **Expliquer** quand créer une exception personnalisée plutôt que d'utiliser une exception du JDK.
- **Créer** une classe d'exception en étendant `RuntimeException`.
- **Lever** votre exception avec `throw` et la **rattraper** avec `catch`.
- **Chaîner** une exception de bas niveau dans une exception métier sans perdre la cause d'origine.

## 1. Pourquoi une exception métier

### Quand la créer

Une exception générique comme `IllegalArgumentException` convient pour des gardes simples (vérifier qu'un argument n'est pas nul, qu'un entier est positif). Mais elle ne dit rien du contexte métier. Lisez ces deux messages d'erreur :

```text
IllegalArgumentException: invalid value
SoldeInsuffisantException: retrait de 200 € refusé — solde disponible : 50 €
```

Le deuxième message indique immédiatement *qui* a posé la règle et *pourquoi* elle est violée. Quand vous voyez ce nom dans une pile d'appels (une *stack trace*), vous savez exactement où chercher.

**Créez une exception personnalisée quand :**

- La règle est propre à votre domaine métier.
- Le nom de l'exception en dit plus qu'un message générique.
- D'autres parties du code doivent pouvoir attraper *ce cas précis* avec un `catch` ciblé.

**Gardez une exception du JDK quand :**

- La règle est générique (argument nul → `NullPointerException` ou `Objects.requireNonNull` ; valeur hors plage → `IllegalArgumentException`).
- Créer un nouveau type n'apporterait aucune information supplémentaire.

### À retenir

> - Une exception personnalisée s'impose quand son **nom seul** communique la règle violée.
> - Pour une garde basique, `IllegalArgumentException` du JDK suffit — pas besoin d'un nouveau type.

## 2. Créer son exception

### Structure minimale

Une exception personnalisée est une classe qui **étend** une classe d'exception existante. Dans ce module, le choix retenu est `extends RuntimeException` — une **exception non vérifiée** (*unchecked*). Cela signifie que les méthodes qui la lèvent ne sont pas obligées de la déclarer avec `throws`, ce qui allège les signatures.

Si vous héritiez de `Exception` (sans passer par `RuntimeException`), votre exception deviendrait **vérifiée** (*checked*) : le compilateur forcerait alors chaque appelant à la rattraper ou à la déclarer. Vous verrez ce comportement avec `IOException` au chapitre 5-5.

Voici la structure recommandée :

```java
public class SoldeInsuffisantException extends RuntimeException {

    // Constructeur avec un message explicatif
    public SoldeInsuffisantException(String message) {
        super(message);
    }

    // Constructeur avec message ET cause — pour le chaînage (voir §4)
    public SoldeInsuffisantException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

Le mot-clé `super(message)` transmet le message au constructeur de `RuntimeException`, qui le stocke et le rend accessible via `getMessage()`. Le paramètre `Throwable cause` sert à encapsuler une exception de plus bas niveau (voir section 4).

### Conventions de nommage

- Le nom se termine **toujours** par `Exception` : `SoldeInsuffisantException`, `FormatCsvInvalideException`, `EffectifDepasseException`.
- Il décrit la **règle violée**, pas l'action qui l'a détectée.

### À retenir

> - Étendez `RuntimeException` pour une exception **non vérifiée** (signatures légères, pas de `throws` obligatoire).
> - Étendre `Exception` à la place produirait une exception **vérifiée** — le compilateur forcerait `throws` ou `catch` partout.
> - Fournissez toujours les deux constructeurs : `(String message)` et `(String message, Throwable cause)`.
> - Le nom se termine par `Exception`.

## 3. Lever et rattraper sa propre exception

### Lever avec `throw`

On crée une instance de l'exception avec `new`, puis on la lève avec `throw` — exactement comme pour toute exception du JDK.

```java
public class CompteBancaire {

    private double solde;

    public CompteBancaire(double soldeInitial) {
        this.solde = soldeInitial;
    }

    public void retirer(double montant) {
        if (montant > solde) {
            // On lève notre exception avec un message précis
            throw new SoldeInsuffisantException(
                "Retrait de " + montant + " € refusé — solde disponible : " + solde + " €"
            );
        }
        solde -= montant;
    }

    public double getSolde() {
        return solde;
    }
}
```

### Rattraper avec `catch`

Comme toute exception, elle se rattrape avec `catch`. Parce que `SoldeInsuffisantException` est unchecked, vous pouvez aussi la laisser se propager vers l'appelant sans `catch` ni `throws`.

```java
public class DemoCompte {

    public static void main(String[] args) {
        CompteBancaire compte = new CompteBancaire(50.0);

        try {
            compte.retirer(200.0); // lève SoldeInsuffisantException
        } catch (SoldeInsuffisantException e) {
            // e.getMessage() retourne le message fourni au constructeur
            System.out.println("Opération impossible : " + e.getMessage());
        }

        System.out.println("Solde actuel : " + compte.getSolde() + " €"); // 50.0 €
    }
}
```

Sortie :
```text
Opération impossible : Retrait de 200.0 € refusé — solde disponible : 50.0 €
Solde actuel : 50.0 €
```

### À retenir

> - On lève une exception personnalisée avec `throw new VotreException("message")`.
> - On la rattrape comme n'importe quelle autre exception avec `catch (VotreException e)`.
> - Une exception *unchecked* peut se propager sans `catch` ni `throws` si l'appelant n'a pas besoin de la traiter localement.

## 4. Chaîner une exception (cause)

### Le problème du relancement sans cause

Il arrive qu'une exception technique de bas niveau (ex. une `NumberFormatException` lors d'une conversion) signale en réalité une violation d'une règle métier. Si vous levez votre exception métier sans mentionner l'exception d'origine, vous perdez l'information de débogage.

```java
// MAUVAIS : la cause (NumberFormatException) est perdue
public void chargerMontant(String texte) {
    try {
        double montant = Double.parseDouble(texte);
        retirer(montant);
    } catch (NumberFormatException e) {
        throw new SoldeInsuffisantException("Montant invalide : " + texte);
        // on ne sait plus POURQUOI le montant est invalide
    }
}
```

### La solution : passer la cause au constructeur

Le constructeur `(String message, Throwable cause)` transmet l'exception d'origine à la hiérarchie `RuntimeException`. Elle reste accessible via `getCause()` et apparaît dans la pile d'appels.

```java
public void chargerMontant(String texte) {
    try {
        double montant = Double.parseDouble(texte);
        retirer(montant);
    } catch (NumberFormatException e) {
        // On encapsule l'exception technique dans l'exception métier
        throw new SoldeInsuffisantException(
            "Le montant « " + texte + " » n'est pas un nombre valide",
            e  // e est la cause : NumberFormatException sera visible dans la stack trace
        );
    }
}
```

Lors du débogage, la pile d'appels affichera les deux niveaux :
```text
SoldeInsuffisantException: Le montant « abc » n'est pas un nombre valide
    at CompteBancaire.chargerMontant(CompteBancaire.java:22)
    ...
Caused by: java.lang.NumberFormatException: For input string: "abc"
    at java.base/java.lang.Double.parseDouble(...)
    ...
```

La ligne `Caused by:` est l'empreinte du chaînage. Elle vous mène directement à la cause première.

### À retenir

> - Passez toujours la cause (`Throwable cause`) quand vous encapsulez une exception existante dans une exception personnalisée.
> - `getCause()` retourne l'exception d'origine enchaînée.
> - La section `Caused by:` dans la pile d'appels indique l'exception de bas niveau — ne la perdez pas.

## Erreurs fréquentes

- **Créer une exception là où `IllegalArgumentException` suffit** : si la règle ne nécessite pas de `catch` ciblé et que le nom ne communique rien de plus, `throw new IllegalArgumentException("message")` est plus simple. Cause : envie de tout personnaliser. Correction : se demander si l'appelant aura besoin de distinguer cette exception par son type.

- **Perdre la cause lors d'un relancement** : écrire `throw new MonException("problème")` à l'intérieur d'un `catch (AutreException e)` sans passer `e`. Cause : oubli du deuxième constructeur. Correction : utiliser `throw new MonException("problème", e)` pour chaîner.

- **Oublier le suffixe `Exception` dans le nom** : nommer la classe `SoldeInsuffisant` au lieu de `SoldeInsuffisantException`. Cause : inattention. Correction : la convention Java exige le suffixe — les outils et les lecteurs s'y attendent.

- **Étendre `Throwable` ou `Error` directement** : ces classes sont réservées à la JVM et aux erreurs système graves. Cause : confusion dans l'arbre des types (vu au chapitre 5-1). Correction : étendre `RuntimeException` (unchecked) ou `Exception` (checked) selon votre besoin.

## Exercice guidé

**Contexte** : vous gérez un compte bancaire simplifié. La règle est la suivante : un retrait ne peut pas dépasser le solde disponible.

**Pas à pas :**

1. Créez la classe `SoldeInsuffisantException extends RuntimeException` avec les deux constructeurs (`message` seul, puis `message` + `cause`).

2. Créez la classe `CompteBancaire` avec un attribut `private double solde`, un constructeur qui l'initialise, et une méthode `retirer(double montant)` qui lève `SoldeInsuffisantException` si `montant > solde`, sinon décrémente le solde.

3. Dans `main` :
   - Créez un compte avec 100,0 €.
   - Tentez un retrait de 30,0 € : affichez le nouveau solde.
   - Tentez un retrait de 200,0 € dans un `try`/`catch` : affichez le message de l'exception.
   - Affichez le solde final (il ne doit pas avoir changé).

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

```java
// Fichier : SoldeInsuffisantException.java
public class SoldeInsuffisantException extends RuntimeException {

    public SoldeInsuffisantException(String message) {
        super(message);
    }

    public SoldeInsuffisantException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

```java
// Fichier : CompteBancaire.java
public class CompteBancaire {

    private double solde;

    public CompteBancaire(double soldeInitial) {
        this.solde = soldeInitial;
    }

    public void retirer(double montant) {
        if (montant > solde) {
            throw new SoldeInsuffisantException(
                "Retrait de " + montant + " € refusé — solde disponible : " + solde + " €"
            );
        }
        solde -= montant; // la règle est respectée : on décrémente
    }

    public double getSolde() {
        return solde;
    }
}
```

```java
// Fichier : DemoCompte.java
public class DemoCompte {

    public static void main(String[] args) {
        CompteBancaire compte = new CompteBancaire(100.0);

        // Retrait valide
        compte.retirer(30.0);
        System.out.println("Solde après retrait de 30 € : " + compte.getSolde() + " €");
        // Solde après retrait de 30 € : 70.0 €

        // Retrait invalide : on attrape l'exception pour ne pas planter le programme
        try {
            compte.retirer(200.0);
        } catch (SoldeInsuffisantException e) {
            System.out.println("Erreur : " + e.getMessage());
            // Erreur : Retrait de 200.0 € refusé — solde disponible : 70.0 €
        }

        // Le solde n'a pas changé car le retrait a été refusé
        System.out.println("Solde final : " + compte.getSolde() + " €");
        // Solde final : 70.0 €
    }
}
```

**Points clés :**
- `SoldeInsuffisantException` hérite de `RuntimeException` : pas de `throws` nécessaire dans la signature de `retirer`.
- Le message précis (`montant` + `solde`) facilite le débogage sans avoir à consulter les variables.
- Le solde reste inchangé quand l'exception est levée, car la ligne `solde -= montant` n'est jamais atteinte.

</details>

## Vérifiez vos acquis

- Quelle est la différence entre une exception qui étend `RuntimeException` et une exception qui étend `Exception` ? Dans quel cas préférez-vous l'une ou l'autre ?
- Pourquoi fournit-on systématiquement un constructeur `(String message, Throwable cause)` dans une exception personnalisée ?
- Comment reconnaître, dans une pile d'appels, qu'une exception a été chaînée ?
- Dans quel cas garderiez-vous `IllegalArgumentException` du JDK au lieu de créer votre propre exception ?

## Pour aller plus loin

- [Custom Exceptions in Java](https://www.baeldung.com/java-new-custom-exception) (Baeldung) — guide complet : quand les créer, comment les structurer, checked vs unchecked.
- [Creating Exception Classes](https://dev.java/learn/exceptions/creating-exception-classes/) (dev.java) — tutoriel officiel Oracle sur les exceptions personnalisées.
- [Class RuntimeException](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/lang/RuntimeException.html) (Javadoc OpenJDK 25) — hiérarchie complète et méthodes héritées de `Throwable`.

## Prochain chapitre

→ **[Chapitre 5-5 — I/O classique](5-5-io-classique)**
