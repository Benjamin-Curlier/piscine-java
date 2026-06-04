---
id: 5-2-try-catch-finally
sidebar_position: 2
title: "try, catch, finally"
description: "Rattraper et propager des exceptions avec try/catch/finally, le multi-catch, et la différence entre throw et throws."
---

# try, catch, finally

## Pourquoi ce chapitre

Au chapitre 5-1, vous avez observé des exceptions levées par le JDK et appris à les lire dans la hiérarchie. Mais jusqu'ici, votre programme s'arrêtait brutalement dès qu'une exception apparaissait. Ce chapitre vous apprend à **réagir** : rattraper l'exception, exécuter un traitement de nettoyage dans tous les cas, et décider si vous propagez l'anomalie à l'appelant ou si vous la gérez sur place.

La maîtrise de `try`/`catch`/`finally` est le socle de tout le module 5 : elle conditionne la lecture de fichiers (chapitres 5-5 et 5-6), la création d'exceptions métier (chapitre 5-4) et, plus généralement, tout code robuste en Java.

## Ce que vous saurez faire à la fin

- **Écrire** un bloc `try`/`catch` pour rattraper une exception précise.
- **Respecter** l'ordre du plus précis au plus général dans les clauses `catch`.
- **Utiliser** le multi-catch (`catch (A | B e)`) quand deux exceptions appellent le même traitement.
- **Expliquer** pourquoi `finally` s'exécute toujours, même en cas de `return`.
- **Distinguer** `throw` (lever une exception) de `throws` (la déclarer dans une signature).
- **Décrire** comment une exception non rattrapée remonte la pile d'appels.

## 1. Rattraper avec try / catch

### La structure de base

Un bloc `try` délimite le code susceptible de lever une exception. Chaque clause `catch` indique le **type** d'exception à intercepter et ce qu'il faut faire dans ce cas.

```java
try {
    // code susceptible de lever une exception
} catch (TypeException e) {
    // traitement si l'exception TypeException (ou une de ses sous-classes) est levée
}
```

Si aucune exception n'est levée à l'intérieur du `try`, la clause `catch` est ignorée. Si une exception est levée, Java cherche la première clause `catch` dont le type correspond et exécute son corps.

### L'ordre des catch : du plus précis au plus général

Java examine les clauses `catch` dans l'ordre. Il faut donc placer le type **le plus précis** en premier, et le type **le plus général** en dernier. Si vous faites l'inverse, le compilateur signale une erreur : le `catch` précis serait inaccessible (code mort).

### Exemple

```java
// Exemple : parse d'un entier puis division — deux exceptions possibles
String saisie = "0"; // imaginez une valeur lue depuis un fichier ou une interface

try {
    int valeur = Integer.parseInt(saisie); // lève NumberFormatException si saisie invalide
    System.out.println(100 / valeur);      // lève ArithmeticException si valeur vaut 0
} catch (NumberFormatException e) {
    // on attrape d'abord le type le plus précis
    System.out.println("Pas un nombre : " + e.getMessage());
} catch (ArithmeticException e) {
    // puis un autre type précis
    System.out.println("Division par zéro");
}
```

`e.getMessage()` renvoie le message d'erreur fourni par le JDK. Utiliser ce message dans votre retour aide au diagnostic.

### À retenir

> - Le bloc `try` contient le code risqué.
> - Chaque `catch` intercepte un type d'exception précis.
> - Ordonnez toujours les `catch` du **plus précis** au **plus général** — sinon le compilateur refuse.

## 2. Le multi-catch

### Quand deux exceptions appellent le même traitement

Depuis Java 7, vous pouvez fusionner plusieurs types dans une seule clause `catch` avec le symbole `|`. Cela évite la duplication quand les deux cas produisent exactement le même comportement.

### Exemple

```java
import java.io.IOException;

// On suppose : static void traiterFichier(String f) throws IOException
// Deux exceptions possibles, même traitement : afficher le problème et arrêter
try {
    traiterFichier("rapport.txt");
} catch (NumberFormatException | IOException e) {
    // le multi-catch regroupe deux types distincts dans une seule variable e
    System.out.println("Impossible de traiter le fichier : " + e.getMessage());
}
```

**Contrainte importante** : les types regroupés dans un multi-catch ne doivent pas être dans une relation d'héritage (l'un ne doit pas étendre l'autre). Si `B` étend `A`, écrire `catch (A | B e)` est inutile — `A` suffit. Le compilateur le refuse.

### À retenir

> - Le multi-catch `catch (A | B e)` regroupe deux exceptions sans lien d'héritage.
> - Il évite la duplication quand les deux cas produisent le même code de traitement.
> - La variable `e` est **effectivement finale** dans un multi-catch (vous ne pouvez pas la réassigner).

## 3. finally : le bloc qui s'exécute toujours

### Le principe

Un bloc `finally` se place après toutes les clauses `catch`. Son code s'exécute **dans tous les cas** :
- si le `try` se termine normalement,
- si un `catch` est déclenché,
- si le `try` ou le `catch` contient un `return`,
- si une exception non rattrapée traverse le bloc.

Le `finally` est fait pour libérer des ressources ou nettoyer un état, quelle que soit l'issue.

### Exemple

```java
try {
    int valeur = Integer.parseInt(saisie);
    System.out.println(100 / valeur);
} catch (NumberFormatException e) {
    System.out.println("Pas un nombre : " + e.getMessage());
} catch (ArithmeticException e) {
    System.out.println("Division par zéro");
} finally {
    System.out.println("Traitement terminé"); // toujours exécuté
}
```

Quelle que soit la valeur de `saisie`, le message `"Traitement terminé"` s'affiche. C'est le comportement garanti de `finally`.

### finally vs catch : ce n'est pas la même chose

`catch` intercepte une exception. `finally` s'exécute toujours, qu'il y ait exception ou non. Vous pouvez écrire un `try` avec un `finally` mais sans aucun `catch` : le bloc de nettoyage s'exécute même si l'exception remonte à l'appelant.

### À retenir

> - `finally` s'exécute **toujours**, même sur un `return` ou une exception non rattrapée.
> - Il sert à libérer une ressource, fermer une connexion, ou garantir un nettoyage.
> - Au chapitre 5-3, vous verrez `try-with-resources`, qui automatise cette fermeture sans `finally` manuel.

## 4. throw vs throws : lever et déclarer

### throw — lever une instance

Le mot-clé `throw` (sans `s`) lève une exception à un endroit précis du code. Il prend en opérande **une instance** d'exception, créée avec `new`.

```java
// Lever une exception si l'argument est invalide
static int diviser(int dividende, int diviseur) {
    if (diviseur == 0) {
        throw new ArithmeticException("Le diviseur ne peut pas être zéro");
    }
    return dividende / diviseur;
}
```

Après un `throw`, l'exécution de la méthode s'arrête immédiatement. Java remonte la pile d'appels à la recherche d'un `catch` correspondant.

### throws — déclarer dans la signature

Le mot-clé `throws` (avec `s`) s'écrit dans la **signature** d'une méthode pour indiquer qu'elle peut lever une exception **checked** (vérifiée par le compilateur) sans la rattraper elle-même. Il prévient l'appelant que la gestion lui incombe.

```java
import java.io.IOException;

// Cette méthode peut lever IOException — elle le déclare avec throws
static void lireFichier(String chemin) throws IOException {
    // ... code de lecture
    // si la lecture échoue, une IOException remonte automatiquement à l'appelant
}
```

Pour les exceptions **unchecked** (comme `RuntimeException` et ses sous-classes), `throws` est facultatif — vous pouvez l'omettre, même si vous levez l'exception.

### La propagation le long de la pile d'appels

Quand une exception est levée et n'est pas rattrapée dans la méthode courante, Java la **propage** à l'appelant. Si l'appelant ne la rattrape pas non plus, elle remonte encore, jusqu'à atteindre la méthode `main`. Si `main` ne la rattrape pas, la JVM (Java Virtual Machine) affiche la stack trace et le programme s'arrête.

```text
main()
  └── calculer()          ← lève ArithmeticException
        └── pas de catch  → remonte à main()
              └── pas de catch dans main → JVM affiche la stack trace et s'arrête
```

Cette remontée s'appelle la **propagation** de l'exception.

### À retenir

> - `throw` lève une instance d'exception : `throw new MonException("message")`.
> - `throws` déclare dans la signature qu'une méthode peut lever une exception **checked**.
> - Une exception non rattrapée remonte la pile d'appels jusqu'au `main`, puis la JVM l'affiche.

## Erreurs fréquentes

- **`catch (Exception e)` placé avant un `catch` plus précis** : le compilateur refuse avec « Exception has already been caught ». Cause : `Exception` étant la mère de toutes les exceptions vérifiées, elle capture tout ce qui suit — le `catch` précis devient inaccessible (code mort). Correction : replacer le `catch (Exception e)` en dernier.

- **Avaler l'exception** (`catch` vide) : écrire `catch (Exception e) { }` sans rien dans le corps. Cause : tentation de faire taire le compilateur. Résultat : le programme continue silencieusement sur un état incohérent, et le diagnostic devient impossible. Correction : au minimum, afficher `e.getMessage()` ou relancer l'exception.

- **Confondre `throw` et `throws`** : `throw` lève une instance dans le corps d'une méthode ; `throws` déclare une exception possible dans la signature. On peut avoir les deux dans la même méthode, mais ils ont des rôles distincts.

- **Placer du code entre un `catch` et le `finally`** : il n'est pas possible d'intercaler d'autres instructions entre les clauses `catch` et le `finally`. La structure légale est `try` / `catch*` / `finally?`.

## Exercice guidé

**Contexte** : vous voulez calculer la division entière d'un dividende par un diviseur, avec une protection contre la division par zéro. Vous souhaitez aussi garantir qu'un message de fin s'affiche dans tous les cas, même si une exception survient — pour simuler la libération d'une ressource.

**Pas à pas :**

1. Créez une méthode statique `diviserAvecProtection(int dividende, int diviseur)` qui renvoie le résultat entier de la division.

2. Dans cette méthode, entourez le calcul d'un bloc `try`/`catch` : rattrapez `ArithmeticException` si `diviseur` vaut 0, affichez un message clair, et renvoyez 0 comme valeur par défaut.

3. Ajoutez un bloc `finally` qui affiche `"Calcul terminé."` pour simuler la libération d'une ressource.

4. Dans `main`, appelez `diviserAvecProtection(10, 2)`, puis `diviserAvecProtection(10, 0)`, et affichez les résultats.

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

```java
public class DivisionProtegee {

    // Méthode qui protège la division entière contre le zéro
    static int diviserAvecProtection(int dividende, int diviseur) {
        int resultat = 0;
        try {
            resultat = dividende / diviseur;
        } catch (ArithmeticException e) {
            // le diviseur est zéro — on signale le problème et on renvoie 0
            System.out.println("Erreur : " + e.getMessage());
        } finally {
            // ce message s'affiche dans tous les cas (résultat OK ou exception)
            System.out.println("Calcul terminé.");
        }
        return resultat;
    }

    public static void main(String[] args) {
        int r1 = diviserAvecProtection(10, 2);
        System.out.println("10 / 2 = " + r1);
        // Calcul terminé.
        // 10 / 2 = 5

        int r2 = diviserAvecProtection(10, 0);
        System.out.println("10 / 0 = " + r2);
        // Erreur : / by zero
        // Calcul terminé.
        // 10 / 0 = 0
    }
}
```

**Points clés :**
- La variable `resultat` est déclarée **avant** le `try` pour rester accessible après.
- `catch (ArithmeticException e)` cible le type exact : on ne rattrape pas plus que nécessaire.
- `finally` s'exécute dans les deux appels, même quand `catch` est déclenché.

</details>

## Vérifiez vos acquis

- Que se passe-t-il si vous placez `catch (Exception e)` avant `catch (NumberFormatException e)` ? Pourquoi le compilateur refuse-t-il ?
- Dans quelle situation le bloc `finally` ne s'exécute-t-il pas (indice : pensez à un arrêt brutal de la JVM) ?
- Quelle est la différence entre `throw new ArithmeticException("msg")` et la déclaration `throws ArithmeticException` dans une signature ?
- Une méthode qui lève une `RuntimeException` est-elle obligée de la déclarer avec `throws` ? Pourquoi ?
- Décrivez le chemin d'une exception non rattrapée dans un programme à trois niveaux d'appel (`main` → `A` → `B`).

## Pour aller plus loin

- [Handling Exceptions](https://dev.java/learn/exceptions/handling-exceptions/) (dev.java) — tutoriel officiel Oracle sur la gestion des exceptions en Java.
- [Exception Handling in Java](https://www.baeldung.com/java-exceptions) (Baeldung) — guide complet : structure `try`/`catch`/`finally`, multi-catch, bonnes pratiques.

## Prochain chapitre

→ **[Chapitre 5-3 — try-with-resources](5-3-try-with-resources)**
