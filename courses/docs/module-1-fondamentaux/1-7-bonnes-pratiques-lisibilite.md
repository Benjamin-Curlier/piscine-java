---
id: 1-7-bonnes-pratiques-lisibilite
sidebar_position: 7
title: "Bonnes pratiques de lisibilité"
description: "Indentation, nommage parlant, commentaires utiles et constantes nommées : écrire du code que les autres (et vous) reliront sans peine."
---

# Bonnes pratiques de lisibilité

## Pourquoi ce chapitre

Un programme est écrit une fois, mais relu des dizaines de fois — par vos camarades, par vos formateurs, et surtout par **vous-même** quelques semaines plus tard. Du code qui « marche » mais qu'on ne comprend pas est une dette : on n'ose plus le modifier, on y réintroduit des bugs.

Ce chapitre rassemble les habitudes simples qui rendent un code lisible. Elles ne changent rien à ce que fait le programme, mais tout à la facilité de le relire et de le corriger. C'est aussi la porte d'entrée vers le module 2, où vous découperez votre code en méthodes.

## Ce que vous saurez faire à la fin

- **Indenter** votre code de façon cohérente.
- **Nommer** vos variables pour qu'elles se passent de commentaire.
- **Écrire** des commentaires qui expliquent le *pourquoi*, pas le *quoi*.
- **Remplacer** les « nombres magiques » par des constantes nommées.

## 1. Indenter pour montrer la structure

L'**indentation** est le décalage vers la droite du code à l'intérieur d'un bloc (`{ ... }`). Elle n'a aucun effet sur l'exécution, mais elle rend visible d'un coup d'œil ce qui est à l'intérieur de quoi.

### Exemple

```java
// MAUVAIS — tout est aligné à gauche, la structure est illisible
public class Demo {
public static void main(String[] args) {
for (int i = 0; i < 3; i++) {
System.out.println(i);
}
}
}

// BON — chaque niveau de bloc est décalé
public class Demo {
    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            System.out.println(i);
        }
    }
}
```

La plupart des éditeurs indentent automatiquement (souvent 4 espaces par niveau). Laissez-les faire, et restez cohérent·e dans tout le fichier.

### À retenir

> - L'indentation rend la structure des blocs visible.
> - Une instruction par ligne, un niveau de décalage par bloc.
> - Laissez l'éditeur indenter, et restez cohérent·e.

## 2. Nommer pour être compris

Un bon nom de variable dit **ce qu'elle contient**, sans qu'on ait à deviner. Par convention, les identifiants Java s'écrivent en anglais ; le vocabulaire métier peut rester en français quand il est plus clair pour vous.

### Exemple

```java
// MAUVAIS — noms vides de sens
int x = 250;
double y = x * 0.2;

// BON — les noms expliquent l'intention
int effectif = 250;
double tauxAbsence = effectif * 0.2;
```

Évitez les variables d'une seule lettre, sauf `i`, `j`, `k` comme compteurs de boucle. Un nom un peu long mais clair vaut toujours mieux qu'un nom court et énigmatique.

### À retenir

> - Un nom doit décrire le contenu : `effectif`, pas `x`.
> - Variables d'une lettre tolérées seulement pour les compteurs de boucle (`i`, `j`, `k`).

## 3. Des commentaires qui expliquent le *pourquoi*

Un commentaire utile ne redit pas ce que le code montre déjà. Il explique une **intention** ou une **raison** que le code seul ne révèle pas.

### Exemple

```java
// MAUVAIS — le commentaire paraphrase le code
compteur = compteur + 1;   // on ajoute 1 à compteur

// BON — le commentaire explique pourquoi
compteur = compteur + 1;   // passe au membre suivant de la liste
```

Si vous avez besoin d'un commentaire pour expliquer *ce que fait* une ligne, c'est souvent le signe qu'un meilleur nom de variable rendrait le commentaire inutile.

### À retenir

> - Un bon commentaire explique le **pourquoi**, pas le **quoi**.
> - Si le code est clair, il se passe de commentaire.

## 4. Bannir les « nombres magiques »

Un **nombre magique** est une valeur écrite en dur dans le code, sans explication de ce qu'elle représente. On la remplace par une **constante nommée**, déclarée avec `final` (qui interdit toute modification ultérieure).

### Exemple

```java
// MAUVAIS — d'où sortent 20 et 0.2 ?
double prime = salaire * 0.2;
if (anciennete > 20) { ... }

// BON — les constantes nomment l'intention
final double TAUX_PRIME = 0.2;
final int ANCIENNETE_SENIOR = 20;

double prime = salaire * TAUX_PRIME;
if (anciennete > ANCIENNETE_SENIOR) { ... }
```

Par convention, les constantes se nomment en majuscules avec des underscores (`TAUX_PRIME`). Outre la clarté, une constante se modifie en **un seul endroit** si la règle change.

### À retenir

> - Un nombre magique est une valeur en dur sans explication.
> - Remplacez-le par une constante `final` au nom parlant.
> - Convention : constantes en `MAJUSCULES_AVEC_UNDERSCORES`.

## 5. Garder les blocs courts

Plus un bloc est long, plus il est difficile à suivre. Quand une suite d'instructions forme une étape identifiable (« calculer la moyenne », « afficher le menu »), c'est un signal : au module 2, vous apprendrez à l'extraire dans une **méthode** portant un nom clair. Pour l'instant, retenez le réflexe : un bloc qui fait trop de choses gagne à être découpé.

### À retenir

> - Un bloc court et focalisé se relit plus facilement.
> - Une étape identifiable deviendra une méthode au module 2.

## Erreurs fréquentes

- **Indentation incohérente** : mélanger les décalages rend la structure illisible. Laissez l'éditeur réindenter le fichier entier.
- **Noms vagues** (`x`, `tmp`, `data`, `truc`) : on perd un temps fou à deviner leur rôle. Renommez dès que le sens n'est pas évident.
- **Commentaires qui paraphrasent** : `i++; // incrémente i` n'apporte rien. Commentez le pourquoi, ou pas du tout.
- **Nombres magiques disséminés** : la même valeur `0.2` répétée à cinq endroits devient un piège le jour où elle change. Une constante, un seul endroit à modifier.

## Exercice guidé

**Objectif** : réécrire un extrait illisible en code propre, sans changer son comportement.

Voici un extrait volontairement mal écrit. Il calcule une prime (20 % du salaire) et affiche un message si l'ancienneté dépasse 20 ans. **Réécrivez-le** : indentation correcte, noms parlants, constantes nommées pour `0.2` et `20`.

```java
public class P{
public static void main(String[] a){
double s=2000;int an=25;
double p=s*0.2;
if(an>20){System.out.println("senior, prime = "+p);}
}
}
```

Indices :
- Renommez `s`, `an`, `p` en noms explicites.
- Sortez `0.2` et `20` dans des constantes `final`.
- Réindentez chaque bloc.

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

```java
public class Prime {

    public static void main(String[] args) {
        final double TAUX_PRIME = 0.2;          // 20 % du salaire
        final int ANCIENNETE_SENIOR = 20;       // seuil en années

        double salaire = 2000;
        int anciennete = 25;

        double prime = salaire * TAUX_PRIME;

        if (anciennete > ANCIENNETE_SENIOR) {
            System.out.println("senior, prime = " + prime);
        }
    }
}
```

Même comportement, mais on comprend immédiatement ce que fait le code et où changer une règle.

</details>

## Vérifiez vos acquis

- À quoi sert l'indentation, alors qu'elle ne change pas l'exécution ?
- Qu'est-ce qu'un « nombre magique » et pourquoi l'éviter ?
- À quelle question un bon commentaire doit-il répondre ?
- Pourquoi `final double TAUX_PRIME = 0.2;` est-il préférable à écrire `0.2` partout ?

## Pour aller plus loin

- [Java Code Conventions](https://www.oracle.com/java/technologies/javase/codeconventions-contents.html) (Oracle) — les conventions historiques de style Java.
- [Clean Code Principles in Java](https://www.baeldung.com/java-clean-code) (Baeldung) — les bonnes habitudes, illustrées.

## Prochain chapitre

→ **[Module 2 — Tableaux 1D](../module-2-tableaux-chaines-methodes/2-1-tableaux-1d)**
