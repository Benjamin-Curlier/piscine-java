---
id: 2-5-recursivite
sidebar_position: 5
title: "Récursivité"
description: "Comprendre une méthode qui s'appelle elle-même : cas de base, cas récursif, pile d'exécution, et savoir quand préférer la récursivité à une boucle."
---

# Récursivité

## Pourquoi ce chapitre

Vous savez maintenant écrire des méthodes et les appeler. Voici une idée qui surprend au premier abord : une méthode peut **s'appeler elle-même**. C'est la **récursivité**.

Bien utilisée, elle exprime certains problèmes plus clairement qu'une boucle — surtout ceux qui se définissent « en fonction d'eux-mêmes ». Ce chapitre s'appuie directement sur les méthodes du [chapitre 2.4](2-4-methodes). Vous y verrez les deux ingrédients indispensables d'une récursion correcte, comment elle se déroule en mémoire, et quand la préférer à une boucle.

## Ce que vous saurez faire à la fin

- **Définir** ce qu'est une méthode récursive.
- **Identifier** le cas de base et le cas récursif.
- **Décrire** le déroulement d'une récursion sur la pile d'exécution.
- **Choisir** entre récursivité et boucle selon le problème.

## 1. Une méthode qui s'appelle elle-même

Une méthode **récursive** est une méthode dont le corps contient un appel à elle-même. Cela paraît tourner en rond, mais ce n'est pas le cas : à chaque appel, on traite un problème **plus petit**, jusqu'à un cas simple que l'on sait résoudre directement.

Prenons la factorielle : `n!` vaut `n × (n-1) × (n-2) × … × 1`. On remarque que `n!` = `n × (n-1)!`. La factorielle se définit donc à partir d'elle-même : c'est un cas naturel de récursivité.

### À retenir

> - Une méthode **récursive** s'appelle elle-même.
> - Chaque appel traite un problème **plus petit** que le précédent.

## 2. Cas de base et cas récursif

Toute méthode récursive a besoin de **deux** parties :

- Le **cas de base** : la situation simple, résolue **sans** nouvel appel. Il **arrête** la récursion.
- Le **cas récursif** : la méthode s'appelle elle-même sur un problème réduit, en se rapprochant du cas de base.

Sans cas de base, la méthode s'appellerait sans fin. Le cas de base est donc **obligatoire**.

### Exemple

```java
static int factorielle(int n) {
    if (n == 0) {
        return 1;                    // cas de base : 0! vaut 1, sans nouvel appel
    }
    return n * factorielle(n - 1);   // cas récursif : on réduit n vers 0
}
```

À chaque appel, `n` diminue de 1. On finit toujours par atteindre `n == 0`, qui stoppe la descente.

### À retenir

> - **Cas de base** : résolu sans appel, il arrête la récursion.
> - **Cas récursif** : appel sur un problème réduit, vers le cas de base.
> - Sans cas de base atteignable, la récursion est infinie.

## 3. La pile d'exécution

Comment Java exécute-t-il ces appels emboîtés ? Chaque appel de méthode est **empilé** : on suspend l'appel courant, on traite le nouvel appel, et on ne reprend l'appel suspendu qu'une fois le sous-appel terminé. Cette structure s'appelle la **pile d'exécution** (en anglais *call stack*).

Voici le déroulé de `factorielle(3)`. On descend en empilant, puis on remonte en dépilant :

```text
factorielle(3)                       <- appel initial
└─ 3 * factorielle(2)                <- empile, attend
   └─ 2 * factorielle(1)             <- empile, attend
      └─ 1 * factorielle(0)          <- empile, attend
         └─ factorielle(0) = 1       <- cas de base : on remonte
      └─ 1 * 1 = 1                   <- dépile
   └─ 2 * 1 = 2                      <- dépile
└─ 3 * 2 = 6                         <- dépile : résultat final
```

On descend jusqu'au cas de base, puis chaque appel en attente reçoit le résultat de son sous-appel et calcule le sien.

### À retenir

> - Chaque appel est **empilé** ; il attend la fin de son sous-appel.
> - On descend jusqu'au cas de base, puis on **remonte** en dépilant.

## 4. Récursif ou itératif ?

Tout ce qui se fait par récursivité peut aussi se faire avec une boucle (de façon **itérative**), et inversement. La factorielle, par exemple, s'écrit très bien avec une boucle `for`.

La récursivité brille quand le problème est **naturellement récursif** : sa définition se réfère à elle-même, ou les données sont elles-mêmes emboîtées. Dans ces cas, la version récursive est souvent plus courte et plus proche de l'énoncé.

À l'inverse, pour un simple comptage ou une somme linéaire, une boucle est généralement plus lisible et plus économe : chaque appel récursif consomme une place sur la pile, ce qu'une boucle évite.

### À retenir

> - Récursif et itératif peuvent résoudre les mêmes problèmes.
> - Récursivité : claire quand le problème est **naturellement récursif**.
> - Boucle : souvent préférable pour un parcours simple (plus économe).

## Erreurs fréquentes

- **Cas de base manquant ou jamais atteint** : la récursion ne s'arrête plus, la pile déborde et le programme s'arrête sur une `StackOverflowError`. Vérifiez qu'il existe un cas de base **et** qu'on s'en rapproche à chaque appel.
- **Argument qui ne décroît pas** : appeler `factorielle(n)` au lieu de `factorielle(n - 1)` ne réduit jamais le problème → récursion infinie.
- **Utiliser la récursivité là où une boucle suffit** : pour une somme ou un comptage simple, une boucle est plus lisible et plus sûre.

## Exercice guidé

**Objectif** : calculer la somme des entiers de 1 à N, de façon récursive.

Écrivez une méthode `static int somme(int n)` qui renvoie `1 + 2 + … + n` **par récursivité**. Identifiez d'abord le cas de base, puis le cas récursif (`somme(n)` = `n + somme(n - 1)`).

Indices :
- Cas de base : `somme(0)` vaut `0` (rien à additionner).
- Cas récursif : `n + somme(n - 1)`.

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

```java
public class SommeRecursive {

    static int somme(int n) {
        if (n == 0) {
            return 0;                 // cas de base : la somme jusqu'à 0 vaut 0
        }
        return n + somme(n - 1);      // cas récursif : on réduit n vers 0
    }

    public static void main(String[] args) {
        System.out.println(somme(5));   // 1 + 2 + 3 + 4 + 5 = 15
    }
}
```

</details>

## Vérifiez vos acquis

- Qu'est-ce qu'un cas de base, et pourquoi est-il indispensable ?
- Que se passe-t-il si le cas de base n'est jamais atteint ?
- Comment les appels récursifs s'organisent-ils en mémoire ?
- La récursivité est-elle toujours préférable à une boucle ?

## Pour aller plus loin

- [Recursion in Java](https://www.baeldung.com/java-recursion) (Baeldung) — exemples et explication de la pile.
- [Methods](https://dev.java/learn/language-basics/methods/) (dev.java) — rappel sur l'appel de méthode, fondement de la récursivité.

## Prochain chapitre

→ **Module 3 — Programmation orientée objet** *(à venir)*
