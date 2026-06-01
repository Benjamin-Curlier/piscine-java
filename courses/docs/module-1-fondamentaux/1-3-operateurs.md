---
id: 1-3-operateurs
sidebar_position: 3
title: "Opérateurs"
description: "Opérateurs arithmétiques, de comparaison, logiques, affectation composée et règles de priorité en Java."
---

# Opérateurs

## Pourquoi ce chapitre

Stocker des données dans des variables, c'est bien ; les **combiner** pour calculer un résultat, c'est mieux. Additionner, comparer, vérifier deux conditions à la fois : tout cela passe par des **opérateurs**.

Ce chapitre fait le tour des opérateurs dont vous vous servirez tout le temps, et insiste sur deux sources d'erreurs classiques : la confusion entre `=` et `==`, et les règles de **priorité** (quel calcul est fait en premier).

## Ce que vous saurez faire à la fin

- **Calculer** avec les opérateurs arithmétiques, y compris le modulo `%`.
- **Comparer** deux valeurs et obtenir un `boolean`.
- **Combiner** des conditions avec les opérateurs logiques `&&`, `||`, `!`.
- **Raccourcir** une affectation avec `+=`, `-=`, `++`, etc.
- **Anticiper** l'ordre dans lequel un calcul est effectué.

## 1. Les opérateurs arithmétiques

Les cinq opérateurs de base : `+` (addition), `-` (soustraction), `*` (multiplication), `/` (division) et `%` (**modulo**, le reste d'une division entière).

### Exemple

```java
int a = 17;
int b = 5;

System.out.println(a + b);   // 22
System.out.println(a - b);   // 12
System.out.println(a * b);   // 85
System.out.println(a / b);   // 3  -> division entière (cf. chapitre 1.2)
System.out.println(a % b);   // 2  -> reste de 17 / 5
```

Le modulo `%` est très utile : `nombre % 2` vaut `0` si le nombre est pair, `1` s'il est impair.

### À retenir

> - `/` entre deux `int` est une **division entière** (le reste est ignoré).
> - `%` donne le **reste** de la division.
> - `n % 2 == 0` teste si `n` est pair.

## 2. Les opérateurs de comparaison

Ils comparent deux valeurs et renvoient un `boolean` (`true` ou `false`) : `==` (égal), `!=` (différent), `<`, `>`, `<=`, `>=`.

### Exemple

```java
int age = 20;

boolean estMajeur = age >= 18;     // true
boolean estExact = age == 20;      // true
boolean estDifferent = age != 20;  // false
```

Attention à ne pas confondre `=` (qui **affecte** une valeur) et `==` (qui **compare**). C'est l'une des erreurs les plus fréquentes.

### À retenir

> - Une comparaison produit toujours un `boolean`.
> - `==` compare, `=` affecte. Ce sont deux choses différentes.

## 3. Les opérateurs logiques

Pour combiner plusieurs conditions : `&&` (ET), `||` (OU), `!` (NON).

- `a && b` vaut `true` seulement si **les deux** sont `true`.
- `a || b` vaut `true` si **au moins une** est `true`.
- `!a` inverse : `true` devient `false`.

### Exemple

```java
int age = 25;
boolean aLicence = true;

boolean peutConduire = age >= 18 && aLicence;   // true : les deux conditions tiennent
boolean estJeune = !(age >= 30);                // true : l'inverse de "age >= 30"
```

Java évalue ces opérateurs **en court-circuit** : dans `a && b`, si `a` est déjà `false`, `b` n'est même pas évalué (le résultat est forcément `false`). De même, dans `a || b`, si `a` est `true`, `b` est ignoré. C'est pratique et parfois indispensable.

### À retenir

> - `&&` : vrai si les deux côtés sont vrais. `||` : vrai si au moins un l'est.
> - `!` inverse un `boolean`.
> - Évaluation **en court-circuit** : le second opérande peut ne pas être évalué.

## 4. L'affectation composée et l'incrément

Modifier une variable à partir de sa propre valeur est si courant qu'il existe des raccourcis.

### Exemple

```java
int score = 10;

score += 5;   // équivaut à : score = score + 5;  -> 15
score -= 3;   // score = score - 3;               -> 12
score *= 2;   // score = score * 2;               -> 24
score /= 4;   // score = score / 4;               -> 6

int compteur = 0;
compteur++;   // équivaut à : compteur = compteur + 1;  -> 1
compteur--;   // compteur = compteur - 1;               -> 0
```

`++` et `--` (incrément et décrément) vous serviront énormément dans les boucles, au [chapitre 1.6](1-6-boucles).

### À retenir

> - `+=`, `-=`, `*=`, `/=`, `%=` modifient une variable à partir d'elle-même.
> - `i++` ajoute 1, `i--` retire 1.

## 5. La priorité des opérateurs

Comme en mathématiques, certains opérateurs passent **avant** d'autres. La multiplication et la division passent avant l'addition et la soustraction.

### Exemple

```java
int r1 = 2 + 3 * 4;       // 14, et non 20 : 3 * 4 est calculé d'abord
int r2 = (2 + 3) * 4;     // 20 : les parenthèses changent l'ordre
```

Vous n'avez pas besoin de mémoriser toute la table de priorité. **Dans le doute, mettez des parenthèses** : elles rendent l'intention claire pour la personne qui relira votre code (souvent vous-même).

### À retenir

> - `*` et `/` passent avant `+` et `-`.
> - Les parenthèses forcent l'ordre **et** améliorent la lisibilité.

## Erreurs fréquentes

- **Écrire `=` au lieu de `==` dans une comparaison** : `age = 18` affecte `18` à `age` au lieu de tester l'égalité. Le compilateur vous arrête souvent, mais pas toujours — soyez vigilant·e.
- **Oublier la priorité** : `moyenne = note1 + note2 / 2` ne calcule pas la moyenne ! La division passe avant l'addition. Écrivez `(note1 + note2) / 2`.
- **Division entière dans un calcul à virgule** : `(note1 + note2) / 2` avec des `int` donne une division entière. Pour une moyenne précise, utilisez des `double` (rappel [chapitre 1.2](1-2-variables-types-primitifs)).
- **Croire que `%` marche bien sur les `double`** : le modulo a un sens clair sur les entiers. Réservez-le aux `int`.

## Exercice guidé

**Objectif** : calculer la moyenne pondérée de deux notes.

Un examen a deux épreuves : un **écrit** (coefficient 2) et un **oral** (coefficient 1). La moyenne pondérée vaut `(ecrit * 2 + oral * 1) / 3`.

Écrivez un programme qui déclare `double ecrit = 14.0;` et `double oral = 11.0;`, calcule la moyenne pondérée dans une variable `double moyenne`, et l'affiche. Utilisez au moins une fois l'affectation composée.

Indices :
- Pensez aux parenthèses : la somme pondérée doit être calculée **avant** la division.
- Avec des `double`, la division n'est pas entière.

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

```java
public class MoyennePonderee {
    public static void main(String[] args) {
        double ecrit = 14.0;
        double oral = 11.0;

        // Somme pondérée des deux notes (coef 2 pour l'écrit, coef 1 pour l'oral).
        double sommePonderee = ecrit * 2 + oral * 1;
        sommePonderee /= 3;   // affectation composée : on divise par le total des coefficients

        System.out.println("Moyenne pondérée : " + sommePonderee);
    }
}
```

</details>

## Vérifiez vos acquis

- Quelle est la différence entre `=` et `==` ?
- Que vaut `7 % 3` ? Et `8 % 2` ? À quoi sert le modulo ?
- Pourquoi `2 + 3 * 4` vaut-il `14` et non `20` ?
- Que signifie l'évaluation « en court-circuit » de `&&` ?

## Pour aller plus loin

- [Operators](https://dev.java/learn/language-basics/operators/) (dev.java) — la liste complète des opérateurs.
- [Java Operators](https://www.baeldung.com/java-operators) (Baeldung) — exemples détaillés et table de priorité.

## Prochain chapitre

→ **[Chapitre 1.4 — Entrées clavier](1-4-entrees-clavier)**
