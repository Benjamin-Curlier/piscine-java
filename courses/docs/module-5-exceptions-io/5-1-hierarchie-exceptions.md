---
id: 5-1-hierarchie-exceptions
sidebar_position: 1
title: "Hiérarchie des exceptions"
description: "Comprendre l'arbre Throwable/Error/Exception, distinguer exceptions checked et unchecked, et lire une stack trace."
---

# Hiérarchie des exceptions

## Pourquoi ce chapitre

Dans les modules 1 à 4, vous avez géré les cas limites sans lever d'exception : valeur sentinelle (`-1`, `null`), `Optional`, ou vérification préalable. Cette approche fonctionne pour des règles simples, mais elle oblige à propager une valeur magique à travers toute la pile d'appels, et le code appelant peut l'ignorer sans que le compilateur proteste. Une **exception** signale une anomalie de façon explicite, sans valeur de retour : dès qu'elle est levée, l'exécution s'interrompt immédiatement et le problème remonte jusqu'à un gestionnaire ou jusqu'au programme principal. Ce chapitre pose le vocabulaire et la structure de ce mécanisme. C'est le verrou qui saute : à partir du module 5, lever une exception est non seulement autorisé, c'est la bonne pratique.

## Ce que vous saurez faire à la fin

- **Expliquer** ce qu'est une exception et pourquoi elle interrompt le flux d'exécution.
- **Lire** l'arbre `Throwable` et placer `Error`, `Exception`, `RuntimeException` dans la bonne branche.
- **Distinguer** une exception *checked* (vérifiée par le compilateur) d'une exception *unchecked* (non vérifiée).
- **Lire** une stack trace (trace de la pile d'appels) et identifier le type, le message et la ligne en cause.
- **Différencier** une erreur de compilation et une exception à l'exécution.

## 1. Qu'est-ce qu'une exception

Un programme peut rencontrer des situations anormales : index hors bornes, division par zéro, fichier introuvable, conversion impossible. En Java, ces situations sont signalées par un **objet exception** qui est *levé* (`throw`) à l'endroit du problème. Dès que cet objet est levé, l'exécution de la méthode courante s'arrête, et la JVM (Java Virtual Machine — la machine virtuelle qui exécute le code compilé) cherche un gestionnaire en remontant la pile d'appels.

Si aucun gestionnaire n'est trouvé, le programme se termine et la JVM affiche la **stack trace** (trace de la pile d'appels — voir section 4).

### Exemple

Le code suivant provoque une exception levée par le JDK (Java Development Kit) lui-même :

```java
int[] notes = {12, 9, 15};
System.out.println(notes[5]); // ArrayIndexOutOfBoundsException levée à l'exécution
```

Le tableau `notes` n'a que trois cases (indices 0, 1, 2). Tenter d'accéder à l'indice 5 lève immédiatement une `ArrayIndexOutOfBoundsException`. La ligne `System.out.println` ne s'exécute jamais.

Voici ce que la JVM affiche dans la console :

```text
Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: Index 5 out of bounds for length 3
    at Demonstration.main(Demonstration.java:3)
```

### À retenir

> - Une exception est un **objet levé** par Java (ou par votre code) pour signaler une anomalie.
> - Dès qu'une exception est levée, **l'exécution de la méthode s'arrête immédiatement**.
> - Sans gestionnaire, le programme se termine et affiche la stack trace.

## 2. L'arbre Throwable

En Java, tout ce qui peut être levé avec `throw` est une instance de la classe `Throwable`. Cette classe a deux branches principales :

```text
Throwable
├── Error                          ← problème système grave (JVM, mémoire…)
│   ├── OutOfMemoryError
│   ├── StackOverflowError
│   └── …
└── Exception                      ← anomalie applicative à gérer
    ├── IOException                ← checked : le compilateur vous oblige à la gérer
    ├── SQLException               ← checked
    ├── …
    └── RuntimeException           ← unchecked : le compilateur ne l'exige pas
        ├── NullPointerException
        ├── ArrayIndexOutOfBoundsException
        ├── NumberFormatException
        ├── ArithmeticException
        └── …
```

**La règle principale** : on ne rattrape jamais une `Error`. Les erreurs signalent un problème de la JVM elle-même (plus de mémoire, récursion infinie…). Votre programme ne peut rien y faire. En revanche, les `Exception` sont faites pour être gérées — c'est tout le sujet des chapitres 5-2 et suivants.

### Exemple

```java
// ArithmeticException : hérite de RuntimeException → unchecked
int resultat = 10 / 0; // lève ArithmeticException : / by zero

// NumberFormatException : hérite de RuntimeException → unchecked
int valeur = Integer.parseInt("abc"); // lève NumberFormatException

// ArrayIndexOutOfBoundsException : hérite de RuntimeException → unchecked
int[] nombres = {1, 2, 3};
int x = nombres[10]; // lève ArrayIndexOutOfBoundsException
```

### À retenir

> - `Throwable` est la racine de tout ce qui peut être levé.
> - `Error` : problème système grave — **on ne rattrape jamais** une `Error`.
> - `Exception` : anomalie applicative — elle est faite pour être gérée.
> - `RuntimeException` est une sous-classe d'`Exception` avec un statut particulier (voir section 3).

## 3. Checked vs unchecked

La distinction la plus importante pour votre pratique quotidienne est celle entre exceptions **checked** (vérifiées) et **unchecked** (non vérifiées).

### Exceptions unchecked

Une exception **unchecked** hérite de `RuntimeException`. Le compilateur **ne vous oblige pas** à la déclarer ni à la rattraper. Ces exceptions signalent le plus souvent des erreurs de programmation (index hors bornes, null déréférencé, mauvais argument) qui peuvent se produire dans n'importe quelle méthode.

### Exceptions checked

Une exception **checked** hérite d'`Exception` sans passer par `RuntimeException`. Le compilateur **vous oblige** à faire l'une de deux choses :

1. **Rattraper** l'exception avec `try`/`catch` (chapitre 5-2).
2. **Déclarer** que votre méthode peut la propager avec `throws` dans la signature.

Si vous ne faites ni l'un ni l'autre, le programme **ne compile pas**.

### Tableau comparatif

| | Checked | Unchecked |
|---|---|---|
| **Hérite de** | `Exception` (hors `RuntimeException`) | `RuntimeException` |
| **Compilateur vérifie** | Oui — erreur si non gérée | Non |
| **Exemples JDK** | `IOException`, `SQLException` | `NullPointerException`, `NumberFormatException`, `ArithmeticException` |
| **Signifie souvent** | Situation externe prévisible (fichier, réseau…) | Erreur de programmation |

### Exemple

```java
// InterruptedException est checked : le compilateur exige qu'on la gère.
// Sans le "throws InterruptedException", ce code ne compile pas.
public static void faireUnePause(int millisecondes) throws InterruptedException {
    Thread.sleep(millisecondes); // Thread.sleep déclare « throws InterruptedException »
}
```

```java
// NumberFormatException est unchecked : aucune déclaration requise.
// Ce code compile, même si Integer.parseInt("abc") lève à l'exécution.
public static int convertir(String texte) {
    return Integer.parseInt(texte);
}
```

### À retenir

> - **Checked** : hérite d'`Exception` (hors `RuntimeException`) — le compilateur exige `try`/`catch` ou `throws`.
> - **Unchecked** : hérite de `RuntimeException` — le compilateur ne dit rien, mais l'exception peut quand même éclater à l'exécution.
> - `Error` n'est ni checked ni unchecked au sens Java — c'est une catégorie à part qu'on ne rattrape pas.

## 4. Lire une stack trace

Quand une exception n'est pas rattrapée, la JVM affiche une **stack trace** dans la console. Savoir la lire est une compétence essentielle.

```text
Exception in thread "main" java.lang.NumberFormatException: For input string: "abc"
    at java.base/java.lang.NumberFormatException.forInputString(NumberFormatException.java:67)
    at java.base/java.lang.Integer.parseInt(Integer.java:668)
    at java.base/java.lang.Integer.parseInt(Integer.java:786)
    at Calcul.convertir(Calcul.java:12)
    at Calcul.main(Calcul.java:6)
```

Voici comment la décoder de haut en bas :

| Élément | Ce que ça signifie |
|---|---|
| `java.lang.NumberFormatException` | Le **type** de l'exception — cherchez-le dans la hiérarchie (section 2). |
| `For input string: "abc"` | Le **message** — il décrit ce qui a mal tourné. Lisez-le toujours en premier. |
| `at Calcul.main(Calcul.java:6)` | Le **point d'entrée** dans votre code — c'est ici que l'appel problématique a commencé. |
| `at Calcul.convertir(Calcul.java:12)` | La **ligne exacte** dans votre code — ouvrez ce fichier à cette ligne. |
| Les lignes `java.base/…` | Du code interne du JDK — vous n'avez pas à les modifier. |

**Règle pratique** : ignorez les lignes `java.base/…` et cherchez la première ligne qui nomme **votre** classe. C'est là que le problème se trouve dans votre code.

### Erreur de compilation vs exception à l'exécution

Ces deux types d'erreurs n'ont pas lieu au même moment :

| | Erreur de compilation | Exception à l'exécution |
|---|---|---|
| **Quand** | Avant toute exécution (`javac`) | Pendant l'exécution du programme |
| **Signalée par** | Le compilateur | La JVM |
| **Exemple** | Variable non déclarée, types incompatibles | `NullPointerException`, division par zéro |
| **Conséquence** | Le programme ne démarre pas | Le programme démarre mais peut s'arrêter |

### Exemple

```java
// Erreur de COMPILATION : x n'existe pas — javac refuse de produire le .class
System.out.println(x); // variable x introuvable

// Exception à l'EXÉCUTION : le programme démarre, mais éclate à cette ligne
int[] notes = {12, 9, 15};
System.out.println(notes[5]); // ArrayIndexOutOfBoundsException
```

### À retenir

> - La stack trace vous donne le **type**, le **message** et la **ligne** de l'erreur.
> - Lisez toujours le message — il contient souvent la valeur problématique.
> - Cherchez **votre** classe dans la pile, pas les lignes `java.base/…`.
> - Une erreur de compilation empêche le démarrage ; une exception à l'exécution survient pendant l'exécution.

## Erreurs fréquentes

- **Confondre `Error` et `Exception`** : écrire `catch (Error e)` pour gérer une situation applicative normale. Cause : les deux héritent de `Throwable`, ce qui peut induire en erreur. Correction : les `Error` signalent des défaillances de la JVM — on ne les rattrape pas. Rattrapez uniquement des `Exception` (ou ses sous-classes).

- **Croire qu'une `RuntimeException` doit être déclarée avec `throws`** : ajouter `throws NullPointerException` à une signature par précaution. Cause : confusion avec les exceptions checked. Correction : les unchecked n'ont jamais à être déclarées dans `throws` — le compilateur ne l'exige pas et cette déclaration superflue encombre le code.

- **Ignorer le message de la stack trace** : regarder uniquement le type de l'exception et chercher dans le code au hasard. Cause : le type seul (`NullPointerException`) est souvent trop général. Correction : lisez le message complet — `Cannot invoke "String.length()" because "maVariable" is null` indique directement quelle variable est nulle.

- **Confondre erreur de compilation et exception à l'exécution** : chercher la cause d'une `NullPointerException` dans les messages de `javac`. Cause : les deux affichent des messages d'erreur, mais dans des contextes différents. Correction : si `javac` refuse de compiler, corrigez d'abord les erreurs de compilation — les exceptions à l'exécution n'apparaissent qu'ensuite.

## Exercice guidé

**Objectif** : cartographier la hiérarchie des exceptions en provoquant et identifiant trois exceptions JDK distinctes.

Vous allez observer trois exceptions levées par le JDK et, pour chacune, répondre à deux questions :
1. Dans quelle branche de l'arbre `Throwable` se trouve-t-elle ?
2. Est-elle checked ou unchecked ?

**Pas à pas :**

**Étape 1** — Provoquez une `NumberFormatException` :

```java
public class CartographieExceptions {
    public static void main(String[] args) {
        // Provoquer une NumberFormatException
        int valeur = Integer.parseInt("abc");
        System.out.println(valeur); // cette ligne ne s'exécutera pas
    }
}
```

Exécutez ce code. Observez la stack trace. Notez :
- Le type exact de l'exception.
- Le message affiché.
- La ligne de votre code qui a provoqué l'erreur.

**Étape 2** — Remplacez le contenu de `main` pour provoquer une `ArithmeticException` :

```java
int quotient = 10 / 0;
System.out.println(quotient);
```

Mêmes questions : type, message, ligne.

**Étape 3** — Remplacez pour provoquer une `ArrayIndexOutOfBoundsException` :

```java
int[] scores = {5, 8, 3};
System.out.println(scores[10]);
```

**Étape 4** — Pour chacune des trois exceptions, remplissez ce tableau (répondez sans regarder la solution) :

| Exception | Hérite de | Checked ou unchecked ? |
|---|---|---|
| `NumberFormatException` | ? | ? |
| `ArithmeticException` | ? | ? |
| `ArrayIndexOutOfBoundsException` | ? | ? |

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

**Tableau complété :**

| Exception | Hérite de | Checked ou unchecked ? |
|---|---|---|
| `NumberFormatException` | `RuntimeException` | Unchecked |
| `ArithmeticException` | `RuntimeException` | Unchecked |
| `ArrayIndexOutOfBoundsException` | `RuntimeException` | Unchecked |

Les trois sont des `RuntimeException` — donc toutes unchecked. Le compilateur ne vous oblige pas à les déclarer ni à les rattraper.

**Stack trace de la `NumberFormatException` :**

```text
Exception in thread "main" java.lang.NumberFormatException: For input string: "abc"
    at java.base/java.lang.NumberFormatException.forInputString(NumberFormatException.java:67)
    at java.base/java.lang.Integer.parseInt(Integer.java:668)
    at CartographieExceptions.main(CartographieExceptions.java:4)
```

Points à noter :
- Le message `For input string: "abc"` indique exactement quelle valeur a posé problème.
- La ligne `at CartographieExceptions.main(CartographieExceptions.java:4)` pointe sur votre code.
- Les lignes `java.base/…` viennent du JDK — vous n'intervenez pas dessus.

**Stack trace de la `ArithmeticException` :**

```text
Exception in thread "main" java.lang.ArithmeticException: / by zero
    at CartographieExceptions.main(CartographieExceptions.java:4)
```

Message direct : `/ by zero`. Cause évidente : un diviseur nul.

**Stack trace de la `ArrayIndexOutOfBoundsException` :**

```text
Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: Index 10 out of bounds for length 3
    at CartographieExceptions.main(CartographieExceptions.java:4)
```

Message précis : `Index 10 out of bounds for length 3` — la JVM indique l'indice demandé (10) et la taille réelle du tableau (3).

**Remarque importante** : aucune de ces exceptions n'est une `Error`. Si vous observez une `OutOfMemoryError` ou une `StackOverflowError`, c'est un problème de la JVM, pas une anomalie applicative à gérer avec `catch`.

</details>

## Vérifiez vos acquis

- Quelle est la différence entre `Error` et `Exception` dans la hiérarchie `Throwable` ?
- Qu'est-ce qu'une exception *checked* ? Que vous impose-t-elle concrètement ?
- `NullPointerException` hérite de `RuntimeException` : est-elle checked ou unchecked ? Devez-vous la déclarer dans `throws` ?
- Dans une stack trace, quelle information lisez-vous en premier pour trouver rapidement la source du problème ?
- Un programme plante avec un message `javac: error: cannot find symbol`. S'agit-il d'une erreur de compilation ou d'une exception à l'exécution ?

## Pour aller plus loin

- [Exceptions in Java](https://dev.java/learn/exceptions/) (dev.java) — le tutoriel officiel Oracle sur les exceptions : hiérarchie, rattrapage, propagation.
- [Javadoc `Throwable`](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/lang/Throwable.html) (OpenJDK 25) — la racine de la hiérarchie : méthodes `getMessage()`, `getCause()`, `printStackTrace()`.

## Prochain chapitre

→ **[Chapitre 5-2 — try, catch, finally](5-2-try-catch-finally)**
