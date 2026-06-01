---
id: 1-4-entrees-clavier
sidebar_position: 4
title: "Entrées clavier"
description: "Lire des données saisies au clavier avec Scanner, gérer le piège nextLine, et faire un parsing simple."
---

# Entrées clavier

## Pourquoi ce chapitre

Jusqu'ici, vos programmes affichaient des valeurs que vous aviez écrites vous-même dans le code. Un vrai programme est plus intéressant quand il **réagit à ce que l'utilisateur tape** : un nom, un nombre, une réponse.

Ce chapitre vous montre comment lire ce qui est saisi au clavier avec l'outil standard de Java pour ça : la classe `Scanner`. Vous verrez aussi un piège célèbre — le `nextLine` qui semble « sauté » — et comment l'éviter.

## Ce que vous saurez faire à la fin

- **Créer** un `Scanner` pour lire l'entrée clavier.
- **Lire** un entier, un nombre à virgule ou une ligne de texte.
- **Éviter** le piège du `nextLine` après un `nextInt`.
- **Convertir** une chaîne lue en nombre avec `Integer.parseInt`.

## 1. Lire au clavier avec `Scanner`

Pour lire ce que l'utilisateur tape, Java fournit la classe **`Scanner`**. Une `Scanner` est un objet capable de découper une source de texte en valeurs. La source qui nous intéresse ici est `System.in`, l'**entrée standard** (le clavier), pendant que `System.out` était la sortie standard (l'écran).

Avant de l'utiliser, il faut l'importer en haut du fichier.

### Exemple

```java
import java.util.Scanner;   // toujours en haut, avant la classe

public class Saisie {
    public static void main(String[] args) {
        Scanner clavier = new Scanner(System.in);

        System.out.println("Quel est votre nom ?");
        String nom = clavier.nextLine();   // lit toute la ligne saisie

        System.out.println("Bonjour " + nom + " !");
    }
}
```

### À retenir

> - On lit le clavier via `new Scanner(System.in)`.
> - Il faut `import java.util.Scanner;` en haut du fichier.
> - `System.in` est l'entrée standard, le pendant de `System.out`.

## 2. Lire selon le type attendu

La `Scanner` propose une méthode par type de donnée :

- `nextInt()` lit un entier (`int`),
- `nextDouble()` lit un nombre à virgule (`double`),
- `next()` lit un seul mot (jusqu'au prochain espace),
- `nextLine()` lit toute la ligne, espaces compris.

### Exemple

```java
import java.util.Scanner;

public class Age {
    public static void main(String[] args) {
        Scanner clavier = new Scanner(System.in);

        System.out.println("Quel est votre âge ?");
        int age = clavier.nextInt();

        System.out.println("Dans 10 ans, vous aurez " + (age + 10) + " ans.");
    }
}
```

### À retenir

> - `nextInt()` pour un entier, `nextDouble()` pour un nombre à virgule.
> - `next()` lit un mot ; `nextLine()` lit toute la ligne.

## 3. Le piège du `nextLine` après un `nextInt`

Voici l'erreur que tout le monde rencontre une fois. Quand vous tapez un nombre puis appuyez sur Entrée, `nextInt()` lit le nombre **mais laisse le retour à la ligne** dans le tampon. Un `nextLine()` qui suit lit alors ce retour à la ligne restant… et semble « sauté ».

### Exemple

```java
import java.util.Scanner;

public class Pieges {
    public static void main(String[] args) {
        Scanner clavier = new Scanner(System.in);

        System.out.println("Votre âge ?");
        int age = clavier.nextInt();

        clavier.nextLine();   // <-- purge le retour à la ligne laissé par nextInt()

        System.out.println("Votre ville ?");
        String ville = clavier.nextLine();   // maintenant, lit bien la ville

        System.out.println(age + " ans, à " + ville);
    }
}
```

La parade est simple : après un `nextInt()` (ou `nextDouble()`) suivi d'un `nextLine()`, ajoutez un `clavier.nextLine();` « de purge ».

### À retenir

> - `nextInt()` laisse le retour à la ligne dans le tampon.
> - Avant un `nextLine()` qui suit, ajoutez un `nextLine()` de purge.

## 4. Convertir une chaîne en nombre

Parfois, on lit une ligne entière (un `String`) mais on a besoin d'un nombre. La conversion se fait avec `Integer.parseInt` (pour un `int`) ou `Double.parseDouble` (pour un `double`).

### Exemple

```java
String saisie = "42";
int nombre = Integer.parseInt(saisie);   // le texte "42" devient le nombre 42

System.out.println(nombre + 1);          // 43
```

Si l'utilisateur tape autre chose qu'un nombre (par exemple `bonjour`), `nextInt()` comme `Integer.parseInt` provoquent une **erreur** à l'exécution. Gérer proprement ces cas demande les *exceptions*, une notion du module 5. Pour l'instant, supposez une saisie correcte et sachez simplement que le risque existe.

### À retenir

> - `Integer.parseInt("42")` transforme le texte `"42"` en l'entier `42`.
> - Une saisie invalide provoque une erreur — la gestion fine viendra au module 5.

## Erreurs fréquentes

- **Le `nextLine()` semble ignoré après un `nextInt()`** : c'est le piège du tampon. Ajoutez un `clavier.nextLine()` de purge entre les deux (voir section 3).
- **`error: cannot find symbol — class Scanner`** : vous avez oublié `import java.util.Scanner;` en haut du fichier.
- **L'utilisateur tape du texte là où un nombre est attendu** : `nextInt()` lève une `InputMismatchException` et le programme s'arrête. C'est normal pour l'instant ; la gestion propre est au programme du module 5.
- **Confondre `next()` et `nextLine()`** : `next()` s'arrête au premier espace, `nextLine()` lit toute la ligne. Pour « Jean Dupont », `next()` ne lirait que « Jean ».

## Exercice guidé

**Objectif** : lire un entier au clavier et afficher son double.

Écrivez un programme qui demande un nombre entier à l'utilisateur, le lit avec `Scanner`, puis affiche ce nombre multiplié par 2.

Indices :
- N'oubliez pas l'`import` en haut du fichier.
- Lisez avec `nextInt()`.
- Vous n'avez pas besoin de `nextLine()` ici (une seule valeur à lire), donc pas de piège de purge.

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

```java
import java.util.Scanner;

public class Double2 {
    public static void main(String[] args) {
        Scanner clavier = new Scanner(System.in);

        System.out.println("Entrez un nombre entier :");
        int nombre = clavier.nextInt();

        // On multiplie par 2 et on affiche le résultat.
        System.out.println("Le double est : " + (nombre * 2));
    }
}
```

</details>

## Vérifiez vos acquis

- À quoi sert `System.in` ? En quoi diffère-t-il de `System.out` ?
- Pourquoi faut-il parfois ajouter un `nextLine()` « vide » après un `nextInt()` ?
- Quelle est la différence entre `next()` et `nextLine()` ?
- Comment transformer le texte `"123"` en l'entier `123` ?

## Pour aller plus loin

- [La classe `Scanner` — Javadoc OpenJDK 25](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/Scanner.html) — toutes les méthodes de lecture.
- [Java Scanner](https://www.baeldung.com/java-scanner) (Baeldung) — exemples variés de lecture clavier.

## Prochain chapitre

→ **[Chapitre 1.5 — Conditions](1-5-conditions)**
