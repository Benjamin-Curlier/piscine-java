---
id: 2-3-chaines-de-caracteres
sidebar_position: 3
title: "Chaînes de caractères"
description: "Comprendre que String est immuable, utiliser ses méthodes courantes (length, charAt, substring, indexOf, split), comparer avec equals, et concaténer efficacement avec StringBuilder."
---

# Chaînes de caractères

## Pourquoi ce chapitre

Vous manipulez des chaînes de caractères (le type `String`) depuis le premier programme : `"Hello, world!"` en est une. Mais une chaîne est bien plus qu'un texte à afficher. On peut mesurer sa longueur, en extraire un morceau, y chercher un mot, la mettre en majuscules, la découper.

Ce chapitre présente le fonctionnement des chaînes en Java, leurs méthodes les plus utiles, le piège classique de leur comparaison, et l'outil `StringBuilder` pour les assembler efficacement.

## Ce que vous saurez faire à la fin

- **Expliquer** pourquoi une `String` est immuable.
- **Utiliser** les méthodes courantes : `length()`, `charAt`, `substring`, `indexOf`, `split`.
- **Comparer** deux chaînes correctement avec `equals`.
- **Assembler** des chaînes efficacement avec `StringBuilder`.

## 1. `String`, un objet immuable

Une `String` est **immuable** : une fois créée, elle ne change jamais. Les méthodes qui semblent la « modifier » (mettre en majuscules, remplacer un caractère…) ne la touchent pas : elles renvoient une **nouvelle** chaîne.

```java
String message = "bonjour";
message.toUpperCase();                 // ne change RIEN à message
System.out.println(message);           // affiche toujours "bonjour"

String crie = message.toUpperCase();   // il faut récupérer la valeur renvoyée
System.out.println(crie);              // affiche "BONJOUR"
```

Retenez ce réflexe : pour profiter d'une transformation, **récupérez la valeur renvoyée** dans une variable.

### À retenir

> - Une `String` ne se modifie jamais sur place : elle est **immuable**.
> - Les méthodes de transformation renvoient une **nouvelle** chaîne.
> - Pensez à affecter le résultat : `String r = s.toUpperCase();`.

## 2. Longueur et accès aux caractères

`length()` donne le nombre de caractères. Attention : pour une chaîne, c'est une **méthode**, écrite **avec** parenthèses — contrairement au `length` des tableaux, vu au [chapitre 2.1](2-1-tableaux-1d), qui n'en a pas.

`charAt(i)` renvoie le caractère (type `char`) à la position `i`. Comme pour les tableaux, les positions commencent à `0` et vont jusqu'à `length() - 1`.

### Exemple

```java
String niveau = "Manager";

System.out.println(niveau.length());   // 7
System.out.println(niveau.charAt(0));  // 'M' (premier caractère)
System.out.println(niveau.charAt(6));  // 'r' (dernier : length() - 1)
```

### À retenir

> - `s.length()` : nombre de caractères (**avec** parenthèses — différent des tableaux).
> - `s.charAt(i)` : le caractère à la position `i`, de `0` à `length() - 1`.

## 3. Extraire et chercher

`substring(debut, fin)` renvoie le morceau de chaîne de l'indice `debut` (inclus) à l'indice `fin` (**exclu**). `indexOf("texte")` renvoie la position de la première occurrence, ou `-1` si elle est absente.

### Exemple

```java
String phrase = "ligne de commande";

System.out.println(phrase.substring(0, 5));    // "ligne" (indices 0 à 4)
System.out.println(phrase.indexOf("commande")); // 9
System.out.println(phrase.indexOf("erreur"));   // -1 (absent)
```

La borne de fin **exclue** dans `substring` surprend souvent : `substring(0, 5)` prend 5 caractères, des indices `0` à `4`.

### À retenir

> - `s.substring(debut, fin)` : de `debut` (inclus) à `fin` (**exclu**).
> - `s.indexOf("x")` : position de `"x"`, ou `-1` si absent.

## 4. Comparer des chaînes

Pour comparer le **contenu** de deux chaînes, on utilise `equals(...)`, **jamais** `==`.

L'opérateur `==` compare des références (« est-ce le même objet en mémoire ? »), pas le texte. Sur des chaînes, il donne des résultats trompeurs. C'est le piège annoncé au module 1 ; le voici résolu.

### Exemple

```java
String attendu = "oui";

// "saisie" est construite à l'exécution (concaténation) : c'est un AUTRE objet en mémoire,
// même si son contenu est identique. C'est typiquement le cas d'une vraie saisie utilisateur.
String debut = "ou";
String saisie = debut + "i";

System.out.println(saisie.equals(attendu));   // true  : même contenu
System.out.println(saisie == attendu);         // false : objets différents → le piège !

// equalsIgnoreCase ignore la casse (majuscules/minuscules).
System.out.println("OUI".equalsIgnoreCase("oui"));   // true
```

> ⚠️ Si vous testez `"oui" == "oui"` avec deux **littéraux** écrits en dur, Java les partage en mémoire et `==` renvoie `true` — ce qui masque le problème. Dès que la chaîne vient d'une saisie ou d'un calcul, `==` casse. D'où la règle simple : **toujours `equals`**.

### À retenir

> - Comparez le contenu avec `s.equals(autre)`, **jamais** avec `==`.
> - `==` compare les références, pas le texte → résultats trompeurs.
> - `equalsIgnoreCase` ignore la différence majuscules/minuscules.

## 5. Transformer et découper

Plusieurs méthodes renvoient une nouvelle chaîne transformée :

- `toUpperCase()` / `toLowerCase()` : changent la casse.
- `trim()` : enlève les espaces en début et en fin.
- `split(" ")` : découpe la chaîne en un **tableau** de morceaux, selon un séparateur. Le résultat est un `String[]`, parcourable comme tout tableau (chapitre 2.1).

### Exemple

```java
String ligne = "  Dupont Martin Bernard  ";

String propre = ligne.trim();              // "Dupont Martin Bernard"
String[] noms = propre.split(" ");         // {"Dupont", "Martin", "Bernard"}

System.out.println(noms.length);           // 3
System.out.println(noms[0]);               // "Dupont"
```

### À retenir

> - `toUpperCase`, `toLowerCase`, `trim` renvoient une chaîne transformée.
> - `split(sep)` découpe en un `String[]`, parcourable comme un tableau.

## 6. Concaténer efficacement avec `StringBuilder`

Assembler des chaînes avec `+` est pratique pour quelques morceaux. Mais comme une `String` est immuable, **chaque** `+` crée une nouvelle chaîne. Dans une grande boucle, cela multiplie les chaînes intermédiaires inutiles.

Pour assembler beaucoup de morceaux, on utilise `StringBuilder` : un assembleur que l'on remplit avec `append`, puis dont on récupère le texte final avec `toString`.

### Exemple

```java
StringBuilder builder = new StringBuilder();

for (int i = 1; i <= 5; i++) {
    builder.append("Ligne ").append(i).append("\n");
}

String resultat = builder.toString();
System.out.print(resultat);
```

Pour quelques concaténations ponctuelles, `+` reste parfait. `StringBuilder` se justifie dès qu'on assemble dans une boucle.

### À retenir

> - Chaque `+` entre chaînes crée une nouvelle `String` (car immuable).
> - Dans une boucle, préférez `StringBuilder` : `append(...)` puis `toString()`.

## Erreurs fréquentes

- **Comparer avec `==`** : `"oui" == saisie` teste les références, pas le contenu. Utilisez `equals`.
- **`StringIndexOutOfBoundsException`** : un indice hors limites dans `charAt` ou `substring`. Rappel : indices valides de `0` à `length() - 1`, et borne de fin **exclue** dans `substring`.
- **Oublier de récupérer le résultat** : `message.toUpperCase();` seul ne fait rien d'utile. Affectez : `message = message.toUpperCase();`.
- **Concaténer avec `+` dans une grande boucle** : cela fonctionne mais gaspille ; `StringBuilder` est fait pour ça.

## Exercice guidé

**Objectif** : mettre une phrase en majuscules et afficher sa longueur.

On vous donne `String message = "bonne formation";`. Affichez sa version en majuscules, puis le nombre de caractères de la phrase d'origine.

Indices :
- `toUpperCase()` renvoie une nouvelle chaîne : récupérez-la dans une variable.
- `length()` (avec parenthèses) donne le nombre de caractères, espace compris.

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

```java
public class MiseEnForme {
    public static void main(String[] args) {
        String message = "bonne formation";

        // toUpperCase renvoie une NOUVELLE chaîne : on la récupère.
        String majuscules = message.toUpperCase();

        System.out.println(majuscules);            // "BONNE FORMATION"
        System.out.println(message.length());      // 15 (l'espace compte)
    }
}
```

</details>

## Vérifiez vos acquis

- Pourquoi dit-on qu'une `String` est immuable ?
- Comment compare-t-on deux chaînes par leur contenu ?
- Que renvoie `indexOf` quand le texte cherché est absent ?
- Dans quel cas vaut-il mieux utiliser `StringBuilder` que l'opérateur `+` ?

## Pour aller plus loin

- [Class String](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/lang/String.html) (Javadoc 25) — la liste complète des méthodes de `String`.
- [Java String Operations](https://www.baeldung.com/java-string-operations) (Baeldung) — les opérations courantes, avec exemples.

## Prochain chapitre

→ **[Chapitre 2.4 — Méthodes](2-4-methodes)**
