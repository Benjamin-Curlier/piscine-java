---
id: 3-2-encapsulation
sidebar_position: 2
title: "Encapsulation"
description: "Protéger l'état d'un objet avec private, exposer getters/setters et garantir des invariants."
---

# Encapsulation

## Pourquoi ce chapitre

Au [chapitre 3.1](3-1-classes-et-objets), n'importe qui pouvait écrire `livre.pages = -50`. Un livre de −50 pages n'a aucun sens, mais rien ne l'empêchait. C'est le problème d'un objet « ouvert » : son état peut devenir incohérent.

L'**encapsulation** consiste à fermer cette boîte : cacher les attributs et n'autoriser leur modification que par des opérations contrôlées. L'objet devient alors maître de son propre état.

## Ce que vous saurez faire à la fin

- **Rendre** un attribut `private` et expliquer pourquoi.
- **Écrire** des getters et setters pour un accès contrôlé.
- **Définir** un invariant et le faire respecter.

## 1. Cacher l'état avec `private`

Un attribut déclaré `private` n'est visible **que** dans sa propre classe. Le code extérieur ne peut plus le lire ni le modifier directement. On « ferme la boîte » qu'on avait laissée ouverte au chapitre précédent.

```java
public class CompteBancaire {
    private double solde;   // inaccessible directement de l'extérieur
}
```

Désormais, `compte.solde = -100;` depuis une autre classe provoque une **erreur de compilation**. C'est exactement ce que l'on veut : interdire les manipulations non contrôlées.

### À retenir

> - `private` limite la visibilité d'un attribut à sa propre classe.
> - L'état n'est plus modifiable directement de l'extérieur.

## 2. `private` et `public` : cacher les données, exposer les opérations

Une classe bien conçue **cache ses données** (`private`) et **expose des opérations** (`public`). Les méthodes `public` forment l'**interface d'usage** : la seule façon, pour le reste du programme, d'interagir avec l'objet.

### Exemple

```java
public class CompteBancaire {
    private double solde;             // donnée cachée

    public void deposer(double montant) {   // opération exposée
        solde = solde + montant;
    }
}
```

L'extérieur ne touche jamais `solde` directement ; il passe par `deposer`. La classe garde le contrôle.

### À retenir

> - On cache les données (`private`) et on expose les opérations (`public`).
> - Les méthodes `public` sont la seule porte d'entrée vers l'objet.

## 3. Getters et setters

Pour lire ou modifier un attribut de façon contrôlée, on écrit des méthodes dédiées : un **getter** (lecture) et un **setter** (écriture). La convention de nommage est `getX` / `setX`, et `isX` pour un booléen.

### Exemple

```java
public class Personne {
    private String nom;

    public String getNom() {       // getter : lecture
        return nom;
    }

    public void setNom(String nouveauNom) {   // setter : écriture
        nom = nouveauNom;
    }
}
```

Un getter/setter n'est pas obligatoire pour chaque attribut : on n'expose que ce qui doit l'être. Un attribut sans setter devient ainsi accessible en lecture seule.

### À retenir

> - Un **getter** (`getX`) lit, un **setter** (`setX`) modifie.
> - On n'expose que les accès nécessaires : pas de setter = lecture seule.

## 4. Garantir un invariant

L'intérêt d'un setter n'est pas de recopier bêtement une valeur : c'est de la **valider**. Un **invariant** est une propriété qui doit **toujours** rester vraie pour un objet valide — par exemple « un solde n'est jamais négatif » ou « une température ne descend pas sous le zéro absolu ». Le setter (ou une méthode métier) fait respecter cet invariant en refusant les valeurs interdites.

### Exemple

```java
public class CompteBancaire {
    private double solde;

    public void retirer(double montant) {
        if (montant <= 0 || montant > solde) {
            // Valeur refusée : on laisse le solde inchangé.
            System.out.println("Retrait impossible.");
            return;
        }
        solde = solde - montant;
    }

    public double getSolde() {
        return solde;
    }
}
```

Ici l'invariant « solde ≥ 0 » est protégé : un retrait trop grand est simplement refusé, et l'objet reste cohérent.

### À retenir

> - Un **invariant** est une propriété toujours vraie d'un objet valide.
> - Le setter (ou une méthode métier) le protège en refusant les valeurs invalides.
> - Une valeur refusée laisse l'objet **inchangé** (on corrige ou on ignore).

## Erreurs fréquentes

- **Tout laisser `public`** : si les attributs sont `public`, l'encapsulation est cassée — n'importe qui peut mettre l'objet dans un état incohérent. Symptôme : un objet « impossible » (solde négatif, âge négatif). Correction : passer les attributs en `private` et exposer des opérations.
- **Setter sans validation** : un setter qui se contente d'`attribut = valeur` n'apporte rien de plus qu'un attribut `public`. Mettez-y la vérification de l'invariant.
- **Getter qui expose une référence interne modifiable** : renvoyer directement un tableau interne permet à l'extérieur de le modifier dans le dos de l'objet. À signaler ; on y reviendra avec les collections.
- **Confondre `private` et la portée locale** : `private` concerne la visibilité d'un **attribut** entre classes ; la portée locale (vue au [chapitre 2.4](../module-2-tableaux-chaines-methodes/2-4-methodes)) concerne une variable dans une méthode. Ce ne sont pas les mêmes notions.

## Exercice guidé

**Objectif** : encapsuler un attribut et protéger un invariant.

Écrivez une classe `Temperature` avec un attribut `private double celsius`. L'invariant : la température ne peut pas descendre sous le zéro absolu (−273.15 °C). Fournissez un setter qui **refuse** une valeur trop basse (il laisse la valeur inchangée et affiche un message) et un getter de lecture.

Indices :
- Le setter compare la valeur reçue à `-273.15` avant d'accepter.
- En cas de refus, ne modifiez pas l'attribut et sortez avec `return`.

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

```java
public class Temperature {
    private double celsius;

    public void setCelsius(double valeur) {
        if (valeur < -273.15) {
            // Sous le zéro absolu : valeur refusée, objet inchangé.
            System.out.println("Température impossible : " + valeur);
            return;
        }
        celsius = valeur;
    }

    public double getCelsius() {
        return celsius;
    }

    public static void main(String[] args) {
        Temperature t = new Temperature();
        t.setCelsius(20);
        System.out.println(t.getCelsius());   // 20.0
        t.setCelsius(-300);                    // refusé : message affiché
        System.out.println(t.getCelsius());   // toujours 20.0
    }
}
```

</details>

## Vérifiez vos acquis

- Pourquoi rendre un attribut `private` plutôt que `public` ?
- À quoi sert un setter par rapport à un attribut directement modifiable ?
- Qu'est-ce qu'un invariant, et où le fait-on respecter ?

## Pour aller plus loin

- [Java Encapsulation](https://www.baeldung.com/java-encapsulation) (Baeldung) — cacher l'état et exposer des opérations.
- [Classes](https://dev.java/learn/classes-objects/) (dev.java) — modificateurs d'accès et membres.

## Prochain chapitre

→ **[Chapitre 3.3 — Constructeurs](3-3-constructeurs)**
