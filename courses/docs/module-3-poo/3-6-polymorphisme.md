---
id: 3-6-polymorphisme
sidebar_position: 6
title: "Polymorphisme"
description: "Traiter uniformément des objets via leur type parent : type statique vs dynamique, liaison dynamique, downcast et instanceof pattern."
---

# Polymorphisme

## Pourquoi ce chapitre

Au [chapitre 3.5](3-5-heritage), `Chien` et `Chat` héritaient d'`Animal` et redéfinissaient `crier()`. Mais à quoi bon, si l'on doit traiter chaque type séparément ? Le **polymorphisme** est ce qui donne sa vraie puissance à l'héritage : manipuler des objets de types différents **à travers un type commun**, et laisser chacun réagir à sa manière.

C'est ce qui permet d'écrire une seule boucle qui fait « crier » un troupeau hétérogène, sans savoir à l'avance qui est chien et qui est chat.

## Ce que vous saurez faire à la fin

- **Distinguer** le type déclaré (statique) du type réel (dynamique).
- **Expliquer** la liaison dynamique des méthodes.
- **Traiter** uniformément des objets via leur type parent.
- **Vérifier et convertir** un type avec `instanceof` pattern.

## 1. Type déclaré et type réel

Grâce à l'héritage, une variable d'un type parent peut désigner un objet d'un type enfant :

```java
Animal a = new Chien("Rex");
```

Ici, deux types coexistent :

- le **type déclaré** (ou statique) : `Animal`, écrit à gauche. Il détermine ce que le compilateur **autorise** à appeler ;
- le **type réel** (ou dynamique) : `Chien`, créé à droite. Il détermine la version de méthode **réellement exécutée**.

### À retenir

> - **Type déclaré** (statique) : à gauche ; il fixe ce qu'on a le droit d'appeler.
> - **Type réel** (dynamique) : l'objet créé ; il décide quelle version s'exécute.

## 2. La liaison dynamique

Quand on appelle une méthode redéfinie, Java choisit la version selon le **type réel** de l'objet, pas selon le type déclaré. C'est la **liaison dynamique** (en anglais *dynamic dispatch*).

### Exemple

```java
Animal a = new Chien("Rex");
System.out.println(a.crier());   // "Rex aboie." : la version de Chien

Animal b = new Chat("Felix");
System.out.println(b.crier());   // "Felix miaule." : la version de Chat
```

Bien que `a` et `b` soient tous deux déclarés `Animal`, chacun exécute la version de `crier()` correspondant à son type réel. L'objet « sait » comment se comporter.

### À retenir

> - La méthode appelée dépend du **type réel** de l'objet (liaison dynamique).
> - Le type déclaré ne décide pas du comportement, seulement de ce qui est appelable.

## 3. Traiter des objets uniformément

L'intérêt pratique : un tableau de `Animal` peut contenir des `Chien` et des `Chat`, et une **seule** boucle les traite tous. Chacun réagit à sa façon, sans `if` sur le type.

### Exemple

```java
Animal[] troupeau = {
    new Chien("Rex"),
    new Chat("Felix"),
    new Chien("Médor")
};

for (Animal animal : troupeau) {
    System.out.println(animal.crier());   // chaque animal crie à sa manière
}
```

Ajouter une nouvelle espèce (une sous-classe d'`Animal`) ne change **rien** à cette boucle : c'est la grande force du polymorphisme.

### À retenir

> - Un type parent permet de stocker et traiter des objets enfants variés.
> - Une seule boucle suffit ; chaque objet applique son propre comportement.

## 4. `instanceof` pattern et downcast

Via le type `Animal`, on ne peut appeler que ce qu'`Animal` déclare. Pour utiliser une méthode propre à `Chien` (par exemple `aboyer()`), il faut d'abord **vérifier** le type réel, puis **convertir** (downcast). Depuis Java 16, `instanceof` fait les deux d'un coup en liant une variable du bon type :

### Exemple

```java
Animal a = new Chien("Rex");

if (a instanceof Chien c) {   // si a est un Chien, c le désigne en tant que Chien
    c.aboyer();               // méthode propre à Chien, utilisable en toute sûreté
}
```

Si le test échoue, la variable `c` n'est pas définie et le bloc est ignoré : pas de risque d'erreur. Cette forme remplace l'ancien `(Chien) a` non vérifié, source de plantages.

### À retenir

> - `instanceof Type variable` teste **et** convertit en une seule expression.
> - La variable n'est liée que si le test réussit — conversion sûre.

## Erreurs fréquentes

- **Downcast non vérifié** : écrire `Chien c = (Chien) a;` alors que `a` désigne en réalité un `Chat` lève une `ClassCastException` à l'exécution. Symptôme : plantage sur la ligne du cast. Cause : conversion vers un type incompatible. Correction : tester avec `instanceof` avant de convertir.
- **Croire que le type déclaré décide de la méthode** : c'est le type **réel** qui compte pour les méthodes redéfinies (liaison dynamique). Le type déclaré ne limite que ce qui est appelable.
- **Multiplier les `instanceof`** : enchaîner `if (a instanceof Chien) ... else if (a instanceof Chat) ...` pour adapter le comportement trahit souvent un manque de polymorphisme. Une méthode redéfinie dans chaque sous-classe est généralement préférable.

## Exercice guidé

**Objectif** : exploiter le polymorphisme sur un tableau hétérogène.

En réutilisant `Animal`, `Chien` et `Chat` du [chapitre 3.5](3-5-heritage), remplissez un tableau `Animal[]` avec un `Chien` et un `Chat`, puis parcourez-le en appelant `crier()` sur chaque élément. Constatez que chacun crie à sa façon **sans** test de type.

Indices :
- Le tableau est de type `Animal[]`, mais ses cases contiennent des objets enfants.
- Une boucle `for-each` suffit ; n'écrivez aucun `if` sur le type.

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

```java
public class Demo {
    public static void main(String[] args) {
        Animal[] animaux = {
            new Chien("Rex"),
            new Chat("Felix")
        };

        for (Animal animal : animaux) {
            System.out.println(animal.crier());
        }
        // Rex aboie.
        // Felix miaule.
    }
}
```

(`Animal`, `Chien` et `Chat` sont ceux définis au chapitre 3.5.)

</details>

## Vérifiez vos acquis

- Quelle différence entre le type déclaré et le type réel d'une variable ?
- Qu'est-ce que la liaison dynamique ?
- Pourquoi un tableau `Animal[]` peut-il contenir des `Chien` et des `Chat` ?
- Comment convertir vers un sous-type sans risquer une `ClassCastException` ?

## Pour aller plus loin

- [Polymorphism in Java](https://www.baeldung.com/java-polymorphism) (Baeldung) — liaison dynamique et redéfinition.
- [Pattern Matching for instanceof](https://docs.oracle.com/en/java/javase/25/language/pattern-matching-instanceof.html) (Javadoc 25) — la forme `instanceof Type variable`.

## Prochain chapitre

→ **[Chapitre 3.7 — Classes abstraites](3-7-classes-abstraites)**
