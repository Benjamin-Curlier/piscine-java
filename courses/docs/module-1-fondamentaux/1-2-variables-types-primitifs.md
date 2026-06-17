---
id: 1-2-variables-types-primitifs
sidebar_position: 2
title: "Variables et types primitifs"
description: "Déclarer des variables, comprendre les types primitifs (int, long, double, boolean, char), distinguer String, gérer les conversions et découvrir var."
---

# Variables et types primitifs

## Pourquoi ce chapitre

Un programme manipule des données : un âge, une distance, un nom, une réponse par oui ou par non. Pour garder ces données en mémoire et les réutiliser, on les range dans des **variables**. Encore faut-il dire à Java *quel genre* de donnée chaque variable contient — c'est le rôle des **types**.

Ce chapitre vous apprend à déclarer des variables, à choisir le bon type, et à éviter les pièges classiques (la division qui « oublie » les décimales, les nombres trop grands, la comparaison de texte).

## Ce que vous saurez faire à la fin

- **Déclarer** une variable et lui affecter une valeur.
- **Choisir** le type primitif adapté à une donnée (`int`, `long`, `double`, `boolean`, `char`).
- **Distinguer** un type primitif d'un `String`.
- **Convertir** une valeur d'un type vers un autre, et anticiper la perte de précision.
- **Décider** quand utiliser `var`.

## 1. Qu'est-ce qu'une variable

Une **variable** est un emplacement nommé dans la mémoire, qui contient une valeur. Pour l'utiliser, vous la **déclarez** : vous annoncez son type et son nom, puis vous lui donnez une valeur.

### Exemple

```java
int age = 30;        // déclaration + affectation en une ligne
age = 31;            // réaffectation : la même variable change de valeur

int compteur;        // déclaration seule
compteur = 0;        // affectation plus tard
```

La forme générale est `type nom = valeur;`. Le `type` (ici `int`) fixe une fois pour toutes le genre de valeur que la variable peut contenir : une variable `int` ne contiendra jamais de texte.

### À retenir

> - On déclare une variable avec `type nom = valeur;`.
> - Le type est fixé à la déclaration et ne change plus.
> - Le nom doit être parlant : `age`, `vitesse`, pas `x` ou `truc`.

## 2. Les types entiers : `int` et `long`

Pour les nombres **sans virgule**, le type courant est `int`. Il couvre les valeurs d'environ −2,1 milliards à +2,1 milliards — largement suffisant la plupart du temps.

Quand un nombre risque de dépasser cette plage (un grand identifiant, un nombre de millisecondes), on utilise `long`, qui va beaucoup plus loin. On signale une valeur `long` en lui ajoutant le suffixe `L`.

### Exemple

```java
int effectif = 250;              // un effectif tient largement dans un int
long population = 8_100_000_000L; // un long pour un très grand nombre

// Le caractère _ est juste là pour la lisibilité : Java l'ignore.
```

Attention : un `int` a une **limite haute** (`Integer.MAX_VALUE`, environ 2,1 milliards). Si un calcul la dépasse, le résultat **ne plante pas** : il « repasse » brutalement dans les négatifs (on parle de *débordement*, ou *overflow*).

```java
int max = Integer.MAX_VALUE;     // 2147483647
System.out.println(max + 1);     // affiche -2147483648, pas 2147483648 !
```

Si vos valeurs risquent d'être très grandes, utilisez un `long` dès le départ.

### À retenir

> - `int` pour les entiers usuels.
> - `long` pour les très grands entiers, avec le suffixe `L`.
> - On peut écrire `1_000_000` pour mieux lire un grand nombre.
> - Un `int` **déborde** silencieusement au-delà de `Integer.MAX_VALUE` (il repasse dans les négatifs). Passez à `long` pour les grandes valeurs.

## 3. Les nombres à virgule : `double`

Pour les valeurs **avec virgule** (une taille, une moyenne, un prix), on utilise `double`. En Java, le séparateur décimal est le **point**, jamais la virgule.

Il existe aussi `float`, plus petit et moins précis, qu'on signale avec le suffixe `f`. Dans cette formation, **préférez toujours `double`** : il est plus précis et c'est le choix par défaut.

### Exemple

```java
double taille = 1.78;        // un point décimal, pas une virgule
double moyenne = 12.5;
float ratio = 0.75f;         // float : moins courant, suffixe f obligatoire
```

Un `double` est **approximatif**. La machine stocke les nombres à virgule en binaire, et certaines valeurs simples en décimal ne tombent pas juste. Conséquence surprenante : `0.1 + 0.2` ne vaut **pas** exactement `0.3`.

```java
double somme = 0.1 + 0.2;
System.out.println(somme);   // affiche 0.30000000000000004, pas 0.3 !
```

Pour cette raison, **ne testez jamais l'égalité exacte entre deux `double`** : un calcul peut tomber « presque » sur la bonne valeur sans l'atteindre pile.

### À retenir

> - `double` pour les nombres à virgule.
> - Le séparateur décimal est le **point** (`1.78`, pas `1,78`).
> - Par défaut, on utilise `double` et pas `float`.
> - Un `double` est **approximatif** : `0.1 + 0.2 != 0.3`. Ne comparez pas deux `double` avec une égalité exacte.

## 4. `boolean` et `char`

Un `boolean` ne peut prendre que **deux** valeurs : `true` (vrai) ou `false` (faux). C'est le type des réponses par oui ou par non.

Un `char` contient **un seul caractère**, écrit entre **apostrophes simples** : `'A'`, `'7'`, `'?'`.

### Exemple

```java
boolean estActif = true;
boolean aReussi = false;

char initiale = 'D';     // un seul caractère, entre apostrophes simples
char lettre = 'z';
```

Attention : `'A'` (apostrophes simples) est un `char`. `"A"` (guillemets doubles) est un texte — c'est un `String`, le type de la section suivante.

### À retenir

> - `boolean` : `true` ou `false`, rien d'autre.
> - `char` : un seul caractère, entre apostrophes simples (`'A'`).

## 5. `String` : du texte (et ce n'est pas un type primitif)

Pour du **texte** — un mot, une phrase, un nom — on utilise `String`. Un **`String`** est une suite de caractères, écrite entre **guillemets doubles**.

Contrairement à `int`, `double` ou `boolean`, **`String` n'est pas un type primitif** : c'est un *type objet*, une notion que vous approfondirez au module 3. Pour l'instant, retenez seulement qu'il s'utilise comme les autres pour stocker du texte, et qu'il commence par une majuscule (`String`, pas `string`).

On peut **concaténer** (mettre bout à bout) des `String` avec l'opérateur `+`.

### Exemple

```java
String nom = "Dupont";
String message = "Bonjour " + nom + " !";   // concaténation avec +

System.out.println(message);                 // affiche : Bonjour Dupont !
```

### À retenir

> - `String` sert à stocker du texte, entre guillemets doubles.
> - `String` n'est **pas** un type primitif (c'est un type objet, vu plus tard).
> - L'opérateur `+` met deux `String` bout à bout (concaténation).

## 6. Les conversions entre types

On a souvent besoin de passer une valeur d'un type à un autre. Deux cas se présentent.

**L'élargissement est automatique** : un `int` rentre sans problème dans un `double`, car aucun chiffre n'est perdu.

```java
int entier = 5;
double reel = entier;    // automatique : reel vaut 5.0
```

**Le rétrécissement demande un cast explicite** : pour forcer un `double` dans un `int`, on écrit le type cible entre parenthèses. Attention, **la partie décimale est perdue** (pas arrondie, tronquée).

```java
double prix = 9.99;
int prixArrondi = (int) prix;   // cast explicite : prixArrondi vaut 9 (pas 10 !)
```

### Le piège de la division entière

Quand on divise deux `int`, le résultat est un `int` : **la partie décimale disparaît**.

```java
int resultat = 5 / 2;          // vaut 2, et non 2.5 !

double exact = 5.0 / 2;        // vaut 2.5 : dès qu'un opérande est double, le calcul est en double
```

### À retenir

> - `int` → `double` : automatique, sans perte.
> - `double` → `int` : cast explicite `(int) ...`, **partie décimale tronquée**.
> - `5 / 2` vaut `2` (division entière). Pour obtenir `2.5`, un des nombres doit être un `double` (`5.0 / 2`).

## 7. `var` : laisser Java deviner le type

Depuis Java 10, on peut écrire `var` au lieu du type quand celui-ci est **évident à la lecture de la valeur**. Java déduit alors le type tout seul. Le type reste fixé : `var` n'est pas « sans type », il est juste *inféré*.

### Exemple

```java
var age = 30;            // Java en déduit int
var taille = 1.78;       // Java en déduit double
var nom = "Dupont";      // Java en déduit String

// var compteur;         // INTERDIT : sans valeur, Java ne peut rien deviner
```

Utilisez `var` quand il rend la ligne plus lisible et que le type saute aux yeux. Évitez-le quand il **cache** une information utile : `var resultat = calculer();` ne dit pas du tout ce qu'on récupère. Dans le doute, écrivez le type.

### À retenir

> - `var` laisse Java déduire le type à partir de la valeur.
> - Le type reste fixé : `var` n'autorise pas à changer de type ensuite.
> - À utiliser seulement quand le type est évident à la lecture.

## Erreurs fréquentes

- **`int resultat = 5 / 2;` donne `2` alors qu'on attendait `2.5`** : c'est une division entière. Mettez au moins un `double` dans le calcul (`5.0 / 2`) et stockez le résultat dans un `double`.
- **`double r = (int) 9.99;` donne `9`** : le cast vers `int` tronque la décimale, il n'arrondit pas. Si vous voulez arrondir, ce n'est pas un simple cast.
- **`long n = 8000000000;` refuse de compiler** : la valeur dépasse la plage de `int` et Java l'interprète d'abord comme un `int`. Ajoutez le suffixe : `8000000000L`.
- **`char c = "A";` ne compile pas** : `"A"` (guillemets doubles) est un `String`. Pour un `char`, utilisez les apostrophes simples : `'A'`.
- **`0.1 + 0.2 == 0.3` est `false`** : un `double` est approximatif. Ne testez pas l'égalité exacte entre deux nombres à virgule.
- **`Integer.MAX_VALUE + 1` donne un nombre négatif** : c'est un débordement d'`int`. Si vos valeurs peuvent être très grandes, utilisez `long`.
- **Comparer deux `String` avec `==`** : cela ne compare pas le texte comme on s'y attend. La bonne façon sera vue au [chapitre 1.5](1-5-conditions). Retenez juste le piège pour l'instant.

## Exercice guidé

**Objectif** : déclarer les variables décrivant un profil, puis les afficher.

Écrivez un programme qui déclare :
- un `int age` (par exemple `28`),
- un `double taille` en mètres (par exemple `1.75`),
- un `char initiale` (par exemple `'M'`),
- un `boolean actif` (par exemple `true`),

puis affiche chaque valeur sur sa propre ligne avec un libellé clair.

Indices :
- Une instruction `System.out.println(...)` par ligne à afficher.
- Vous pouvez concaténer un libellé et une variable avec `+` : `"Age : " + age`.

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

```java
public class Profil {
    public static void main(String[] args) {
        int age = 28;
        double taille = 1.75;
        char initiale = 'M';
        boolean actif = true;

        System.out.println("Age : " + age);
        System.out.println("Taille : " + taille + " m");
        System.out.println("Initiale : " + initiale);
        System.out.println("Actif : " + actif);
    }
}
```

</details>

## Vérifiez vos acquis

- Pourquoi `5 / 2` vaut-il `2` et non `2.5` ? Comment obtenir `2.5` ?
- Quand choisiriez-vous `long` plutôt que `int` ?
- `String` est-il un type primitif ? Comment l'écrit-on (majuscule ? quels guillemets) ?
- Que perd-on en écrivant `(int) 3.99` ?

## Pour aller plus loin

- [Primitive Types and Values](https://dev.java/learn/language-basics/primitive-types/) (dev.java) — la liste complète des types primitifs et leurs plages.
- [La classe `String` — Javadoc OpenJDK 25](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/lang/String.html) — toutes les méthodes que vous découvrirez plus tard.

## Prochain chapitre

→ **[Chapitre 1.3 — Opérateurs](1-3-operateurs)**
