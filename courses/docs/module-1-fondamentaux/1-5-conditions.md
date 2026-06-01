---
id: 1-5-conditions
sidebar_position: 5
title: "Conditions"
description: "Prendre des décisions avec if / else if / else, le switch expression (Java 21+) et l'opérateur ternaire."
---

# Conditions

## Pourquoi ce chapitre

Un programme utile ne fait pas toujours la même chose : il **prend des décisions**. Si l'utilisateur est majeur, on l'autorise ; sinon, on le refuse. Si la note dépasse un seuil, on affiche une mention ; sinon, une autre.

Ce chapitre vous apprend à exprimer ces décisions en Java, avec `if` / `else`, le `switch` moderne, et un raccourci pratique, l'opérateur ternaire. Vous vous appuierez sur les opérateurs de comparaison et logiques vus au [chapitre 1.3](1-3-operateurs).

## Ce que vous saurez faire à la fin

- **Exécuter** du code conditionnellement avec `if` / `else if` / `else`.
- **Choisir** parmi plusieurs cas avec un `switch` expression.
- **Écrire** une décision simple avec l'opérateur ternaire.
- **Comparer** correctement deux `String`.

## 1. La structure `if` / `else`

Un `if` exécute un bloc de code **seulement si** une condition (un `boolean`) est vraie. Le `else` couvre le cas contraire.

### Exemple

```java
int age = 16;

if (age >= 18) {
    System.out.println("Vous êtes majeur.");
} else {
    System.out.println("Vous êtes mineur.");
}
```

La condition entre parenthèses doit produire un `boolean` (cf. [chapitre 1.3](1-3-operateurs)). **Mettez toujours les accolades `{ }`**, même pour une seule instruction : c'est plus sûr et plus lisible.

### À retenir

> - `if (condition) { ... }` exécute le bloc si la condition est vraie.
> - `else { ... }` couvre le cas contraire.
> - Mettez **toujours** les accolades, même pour une ligne.

## 2. Plusieurs cas avec `else if`

Quand il y a plus de deux possibilités, on enchaîne des `else if`. Java teste les conditions **dans l'ordre** et s'arrête à la première qui est vraie.

### Exemple

```java
int note = 15;

if (note >= 16) {
    System.out.println("Très bien");
} else if (note >= 14) {
    System.out.println("Bien");
} else if (note >= 12) {
    System.out.println("Assez bien");
} else {
    System.out.println("Sans mention");
}
```

L'ordre compte : ici, on teste d'abord les seuils les plus hauts. Une note de `15` déclenche le « Bien » et les tests suivants sont ignorés.

### À retenir

> - `else if` enchaîne plusieurs cas.
> - Java s'arrête au **premier** test vrai : l'ordre des conditions est important.

## 3. Le `switch` expression (Java 21+)

Quand on compare une même variable à plusieurs **valeurs précises**, un `switch` est souvent plus lisible qu'une longue cascade de `else if`. Java moderne propose le `switch` sous forme d'**expression**, avec une flèche `->`.

### Exemple

```java
int jour = 3;

String nom = switch (jour) {
    case 1 -> "Lundi";
    case 2 -> "Mardi";
    case 3 -> "Mercredi";
    case 4, 5 -> "Presque le week-end";   // plusieurs valeurs pour un même cas
    default -> "Week-end";                // tous les autres cas
};

System.out.println(nom);   // affiche : Mercredi
```

Le `switch` expression **renvoie une valeur** qu'on range ici dans `nom`. Le `default` couvre tous les cas non listés ; il rend le `switch` **exhaustif** (aucun cas oublié). Si un cas demande plusieurs instructions, on utilise un bloc et le mot-clé `yield` pour renvoyer la valeur :

```java
String niveau = switch (jour) {
    case 6, 7 -> "Repos";
    default -> {
        System.out.println("Jour travaillé");
        yield "Travail";   // yield renvoie la valeur depuis un bloc
    }
};
```

### À retenir

> - Le `switch` expression compare une variable à des valeurs avec `case ... ->`.
> - Il **renvoie** une valeur ; `default` couvre le reste.
> - Pour un cas en plusieurs instructions : un bloc `{ ... }` et `yield`.

## 4. L'opérateur ternaire

Pour une décision **très simple** entre deux valeurs, l'opérateur ternaire condense un `if` / `else` en une ligne : `condition ? valeurSiVrai : valeurSiFaux`.

### Exemple

```java
int age = 20;

String statut = age >= 18 ? "majeur" : "mineur";
System.out.println(statut);   // majeur
```

C'est pratique, mais à utiliser avec mesure : dès que la logique se complique, un `if` / `else` classique reste plus lisible.

### À retenir

> - `condition ? a : b` vaut `a` si la condition est vraie, `b` sinon.
> - À réserver aux choix simples ; sinon, préférez `if` / `else`.

## 5. Comparer deux `String`

Un piège important : pour comparer le **contenu** de deux `String`, on utilise la méthode `equals(...)`, **pas** `==`. L'opérateur `==` ne donne pas le résultat attendu sur du texte (la raison tient à la nature objet de `String`, vue au module 3).

### Exemple

```java
String reponse = "oui";

if (reponse.equals("oui")) {           // CORRECT : compare le contenu
    System.out.println("Confirmé.");
}

// if (reponse == "oui") { ... }       // À ÉVITER : ne compare pas le texte de façon fiable
```

### À retenir

> - Pour comparer deux `String`, utilisez `texte.equals(autre)`.
> - **Jamais** `==` pour comparer le contenu de deux `String`.

## Erreurs fréquentes

- **Écrire `if (age = 18)`** : c'est une affectation, pas une comparaison. Il faut `==` : `if (age == 18)`.
- **Oublier les accolades** : `if (x > 0) System.out.println("positif");` fonctionne, mais une instruction ajoutée dessous **ne sera pas** dans le `if`. Mettez toujours `{ }`.
- **Comparer des `String` avec `==`** : utilisez `equals(...)` (section 5). C'est une erreur très fréquente chez les débutants.
- **`switch` non exhaustif** : un `switch` expression doit couvrir tous les cas. Ajoutez un `default`.
- **Mauvais ordre des `else if`** : tester `note >= 12` avant `note >= 16` attribuerait « Assez bien » à toutes les bonnes notes. Allez du plus restrictif au plus large.

## Exercice guidé

**Objectif** : attribuer une mention à une note, d'abord avec `if` / `else if`, puis avec un `switch`.

1. Déclarez `int note = 13;`.
2. Affichez une mention selon les seuils : `>= 16` → « Très bien », `>= 14` → « Bien », `>= 12` → « Assez bien », sinon « Sans mention », **avec une cascade de `if` / `else if`**.
3. Bonus : recalculez une « tranche » (`note / 2`, qui donne un entier de 0 à 10) et utilisez un `switch` expression sur cette tranche pour afficher un commentaire.

Indices :
- Allez du seuil le plus haut au plus bas.
- Pour le bonus, `note / 2` est une division entière (cf. [chapitre 1.2](1-2-variables-types-primitifs)).

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

```java
public class Mention {
    public static void main(String[] args) {
        int note = 13;

        // 1) Mention par cascade de conditions, du seuil le plus haut au plus bas.
        if (note >= 16) {
            System.out.println("Très bien");
        } else if (note >= 14) {
            System.out.println("Bien");
        } else if (note >= 12) {
            System.out.println("Assez bien");
        } else {
            System.out.println("Sans mention");
        }

        // 2) Bonus : un switch expression sur une tranche entière.
        int tranche = note / 2;   // 13 / 2 vaut 6 (division entière)
        String commentaire = switch (tranche) {
            case 8, 9, 10 -> "Excellent niveau";
            case 6, 7 -> "Bon niveau";
            default -> "À consolider";
        };
        System.out.println(commentaire);
    }
}
```

</details>

## Vérifiez vos acquis

- Pourquoi faut-il toujours mettre les accolades après un `if` ?
- Dans quel cas un `switch` est-il plus lisible qu'une cascade de `else if` ?
- Comment compare-t-on correctement le contenu de deux `String` ?
- Que renvoie l'expression `age >= 18 ? "majeur" : "mineur"` quand `age` vaut `15` ?

## Pour aller plus loin

- [Branching Statements](https://dev.java/learn/language-basics/branching-statements/) (dev.java) — `if`, `switch` et leurs usages.
- [Switch Expressions](https://docs.oracle.com/en/java/javase/25/language/switch-expressions.html) (Oracle) — la référence sur le `switch` moderne.

## Prochain chapitre

→ **[Chapitre 1.6 — Boucles](1-6-boucles)**
