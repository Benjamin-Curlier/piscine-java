---
id: 5-3-try-with-resources
sidebar_position: 3
title: "try-with-resources"
description: "Fermer automatiquement les ressources avec AutoCloseable et le bloc try-with-resources."
---

# try-with-resources

## Pourquoi ce chapitre

Au chapitre 5-2, vous avez utilisé `finally` pour libérer des ressources — fermer un fichier, couper une connexion, arrêter un compteur. Ce code fonctionne, mais il est verbeux et fragile : un oubli suffit pour laisser une ressource ouverte. Le bloc `try-with-resources` résout ce problème de façon élégante et déterministe.

Ce mécanisme est le prérequis direct des chapitres sur les fichiers (chapitres 5-5 et 5-6). Tout flux de données — lecture, écriture, flux de lignes — doit être fermé après usage, et `try-with-resources` est le seul moyen fiable de le garantir.

## Ce que vous saurez faire à la fin

- **Expliquer** le problème posé par la fermeture manuelle dans un `finally`.
- **Implémenter** l'interface `AutoCloseable` dans une classe maison.
- **Écrire** un bloc `try-with-resources` avec une ou plusieurs ressources.
- **Décrire** l'ordre de fermeture quand plusieurs ressources sont déclarées.
- **Identifier** la notion de *suppressed exception* (exception supprimée).

## 1. Le problème du finally manuel

Revenez à ce que vous avez vu au chapitre 5-2. Pour fermer proprement une ressource, on écrivait un `finally` :

```java
Connexion connexion = new Connexion(); // ressource à fermer
try {
    connexion.utiliser();
} catch (Exception e) {
    System.out.println("Erreur : " + e.getMessage());
} finally {
    connexion.fermer(); // fermeture manuelle, dans tous les cas
}
```

Ce code a trois défauts.

**Verbosité.** Pour chaque ressource, il faut un `try`, un `catch`, un `finally`, et une déclaration de variable hors du `try`. Avec deux ressources, la structure s'imbrique et devient difficile à relire.

**Risque d'oubli.** Si vous oubliez le `finally` — ou si vous sortez de la méthode avec un `return` sans passer par `finally` — la ressource reste ouverte. En pratique, cela provoque des **fuites** (fuite de mémoire, fichier verrouillé, connexion non rendue au pool).

**Exception masquée.** Si le corps du `try` lève une exception *et* que `connexion.fermer()` dans le `finally` en lève une autre, la première est perdue. C'est le bug le plus sournois.

### À retenir

> - Un `finally` manuel oublie facilement de fermer la ressource.
> - Si `fermer()` lève une exception, l'exception d'origine peut être silencieusement écrasée.
> - Le code devient difficile à lire dès qu'il y a plusieurs ressources.

## 2. AutoCloseable et la syntaxe try-with-resources

### L'interface AutoCloseable

Java résout le problème avec l'interface `AutoCloseable` (automatiquement-fermable). Elle déclare une seule méthode :

```java
public interface AutoCloseable {
    void close() throws Exception;
}
```

Toute classe qui implémente `AutoCloseable` peut être utilisée dans un bloc `try-with-resources`. Le *runtime* (environnement d'exécution Java) appelle alors `close()` **automatiquement** à la sortie du bloc, que ce soit par un chemin normal ou par une exception.

L'interface `Closeable` (du package `java.io`) est une variante plus restrictive : elle restreint le type de l'exception lancée par `close()` à `IOException`. Vous la rencontrerez dans les chapitres I/O.

### La syntaxe try-with-resources

La ressource est déclarée **à l'intérieur** des parenthèses du `try` :

```java
try (TypeDeLaRessource nom = new TypeDeLaRessource()) {
    // utilisation de la ressource
} // close() est appelé automatiquement ici, même en cas d'exception
```

La ressource est **fermée dès la sortie du bloc**, dans tous les cas. Vous n'avez rien d'autre à écrire.

### Exemple

```java
class Chronometre implements AutoCloseable {
    Chronometre() { System.out.println("Démarrage"); }

    @Override
    public void close() { System.out.println("Arrêt"); }
}

public class Demo {
    public static void main(String[] args) {
        try (Chronometre c = new Chronometre()) {
            System.out.println("Mesure en cours");
        } // close() appelé automatiquement ici, même en cas d'exception
    }
}
```

Ce code affiche :

```text
Démarrage
Mesure en cours
Arrêt
```

La méthode `close()` est appelée à la sortie du bloc `try`, avant même que l'exécution ne continue après l'accolade fermante. Comparez avec le `finally` : le résultat est identique, mais le code est plus court et la fermeture ne peut pas être oubliée.

### À retenir

> - Déclarez la ressource **entre les parenthèses du `try`** pour bénéficier de la fermeture automatique.
> - La classe doit implémenter `AutoCloseable` (ou `Closeable`).
> - `close()` est appelé à la fin du bloc, même si une exception est levée dans le corps.

## 3. Plusieurs ressources

Vous pouvez déclarer plusieurs ressources dans le même `try`. Séparez-les par un point-virgule `;` :

```java
try (PremiereTache tache1 = new PremiereTache();
     DeuxiemeTache tache2 = new DeuxiemeTache()) {
    tache1.executer();
    tache2.executer();
}
```

L'ordre de **fermeture est l'inverse de l'ordre d'ouverture** : `tache2.close()` est appelé en premier, puis `tache1.close()`. C'est comme une pile : dernier ouvert, premier fermé. Cette règle garantit que les dépendances entre ressources sont respectées (si `tache2` dépend de `tache1`, elle est fermée avant `tache1`).

### Exemple

```java
class TacheA implements AutoCloseable {
    TacheA() { System.out.println("Ouverture A"); }

    @Override
    public void close() { System.out.println("Fermeture A"); }
}

class TacheB implements AutoCloseable {
    TacheB() { System.out.println("Ouverture B"); }

    @Override
    public void close() { System.out.println("Fermeture B"); }
}

public class DemoDouble {
    public static void main(String[] args) {
        try (TacheA a = new TacheA();
             TacheB b = new TacheB()) {
            System.out.println("Travail en cours");
        }
    }
}
```

Ce code affiche :

```text
Ouverture A
Ouverture B
Travail en cours
Fermeture B
Fermeture A
```

### À retenir

> - Déclarez plusieurs ressources séparées par `;` dans les parenthèses du `try`.
> - Les ressources sont **fermées dans l'ordre inverse** de leur déclaration.
> - Cette règle vaut aussi en cas d'exception.

## 4. Notion de suppressed exception

Que se passe-t-il si le corps du `try` lève une exception *et* que `close()` en lève une autre au même moment ?

Avec un `finally` classique, la deuxième exception écrase la première — l'exception d'origine est silencieusement perdue. Le `try-with-resources` corrige ce comportement : si `close()` lève une exception pendant la propagation d'une première exception, la deuxième est **supprimée** (en anglais *suppressed*) et attachée à la première. Elle ne disparaît pas : vous pouvez la retrouver avec `exception.getSuppressed()`.

Vous n'avez rien à coder pour que ce mécanisme fonctionne : il est entièrement géré par le *runtime*. Ce détail est mentionné ici pour que vous compreniez pourquoi `try-with-resources` est supérieur au `finally` manuel — pas pour que vous l'utilisiez activement à ce stade.

### À retenir

> - Avec `try-with-resources`, si `close()` lève une exception pendant qu'une autre se propage, la deuxième n'est pas perdue : elle est **attachée** à la première comme *suppressed exception*.
> - Avec un `finally` manuel, la deuxième exception écrase la première — perte silencieuse.
> - Vous n'avez rien à faire : le *runtime* gère cela automatiquement.

## Erreurs fréquentes

- **Déclarer la ressource hors du `try`** : écrire `Chronometre c = new Chronometre();` avant le `try`, puis `try { c.utiliser(); }`. Cause : `c` est hors de la portée du `try-with-resources`, le compilateur ne peut pas appeler `close()` automatiquement. Correction : déplacez la déclaration dans les parenthèses du `try`.

- **Oublier d'implémenter `AutoCloseable`** : utiliser une classe maison dans un `try-with-resources` sans qu'elle implémente `AutoCloseable`. Cause : le compilateur refuse (`error: ... cannot be converted to AutoCloseable`). Correction : ajoutez `implements AutoCloseable` à la classe et implémentez `close()`.

- **Croire que `try-with-resources` rattrape l'exception** : penser que le bloc ferme la ressource *et* empêche l'exception de se propager. Cause : confusion entre fermeture automatique et gestion d'erreur. Correction : ajoutez un `catch` si vous voulez traiter l'exception ; la fermeture de la ressource et le traitement de l'erreur sont deux choses distinctes.

- **Oublier le point-virgule entre deux ressources** : écrire `try (TacheA a = new TacheA() TacheB b = new TacheB())`. Cause : oubli de syntaxe. Correction : séparez chaque ressource par un `;`.

## Exercice guidé

**Contexte** : vous souhaitez mesurer le temps d'exécution d'une opération. Vous allez écrire une classe `Minuteur` qui trace l'ouverture et la fermeture, puis l'utiliser dans un `try-with-resources`.

**Pas à pas :**

1. Créez une classe `Minuteur` qui implémente `AutoCloseable`. Le constructeur affiche `"[Minuteur] Démarré"`. La méthode `close()` affiche `"[Minuteur] Arrêté"`.

2. Ajoutez une méthode `mesurer()` à `Minuteur` qui affiche `"[Minuteur] Mesure en cours..."`.

3. Dans un `main`, utilisez `Minuteur` dans un `try-with-resources`. Appelez `mesurer()` dans le corps du `try`.

4. Vérifiez que `"[Minuteur] Arrêté"` s'affiche **après** `"[Minuteur] Mesure en cours..."`, même si vous ajoutez une exception dans le corps du `try` (par exemple `throw new RuntimeException("test")`). Ajoutez un `catch` pour observer que la fermeture a bien eu lieu avant que l'exception ne remonte.

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

```java
public class Minuteur implements AutoCloseable {

    public Minuteur() {
        System.out.println("[Minuteur] Démarré");
    }

    public void mesurer() {
        System.out.println("[Minuteur] Mesure en cours...");
    }

    @Override
    public void close() {
        // close() est appelé automatiquement à la fin du try-with-resources
        System.out.println("[Minuteur] Arrêté");
    }
}
```

```java
public class DemoMinuteur {

    public static void main(String[] args) {

        // Cas normal : pas d'exception
        System.out.println("--- Cas sans exception ---");
        try (Minuteur m = new Minuteur()) {
            m.mesurer();
        }
        // Affiche :
        // [Minuteur] Démarré
        // [Minuteur] Mesure en cours...
        // [Minuteur] Arrêté

        System.out.println();

        // Cas avec exception : close() est quand même appelé avant que l'exception remonte
        System.out.println("--- Cas avec exception ---");
        try (Minuteur m = new Minuteur()) {
            m.mesurer();
            throw new RuntimeException("Interruption de la mesure"); // exception simulée
        } catch (RuntimeException e) {
            // close() a déjà été appelé avant d'arriver ici
            System.out.println("Exception rattrapée : " + e.getMessage());
        }
        // Affiche :
        // [Minuteur] Démarré
        // [Minuteur] Mesure en cours...
        // [Minuteur] Arrêté
        // Exception rattrapée : Interruption de la mesure
    }
}
```

**Points clés** :
- `Minuteur` implémente `AutoCloseable` : une seule méthode `close()` à fournir.
- Dans les deux cas (avec ou sans exception), `"[Minuteur] Arrêté"` apparaît **avant** le `catch` — la fermeture est garantie.
- Le `catch` est facultatif : sans lui, l'exception se propage normalement, mais la ressource est quand même fermée.

</details>

## Vérifiez vos acquis

- Quelle interface une classe doit-elle implémenter pour être utilisable dans un `try-with-resources` ?
- Si vous déclarez deux ressources `A` et `B` dans un même `try-with-resources` (dans cet ordre), dans quel ordre leurs méthodes `close()` sont-elles appelées ?
- `try-with-resources` rattrape-t-il l'exception levée dans le corps du `try` ? Justifiez votre réponse.
- Qu'est-ce qu'une *suppressed exception* et pourquoi est-ce une amélioration par rapport au `finally` manuel ?

## Pour aller plus loin

- [The try-with-resources Statement](https://dev.java/learn/exceptions/try-with-resources/) (dev.java) — explication officielle Oracle avec des exemples détaillés sur la fermeture déterministe et les suppressed exceptions.
- [Java Try-With-Resources](https://www.baeldung.com/java-try-with-resources) (Baeldung) — guide pratique avec de nombreux exemples, notamment sur les ressources multiples et les suppressions d'exceptions.
- [AutoCloseable (Javadoc OpenJDK 25)](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/lang/AutoCloseable.html) — documentation de référence de l'interface `AutoCloseable`.

## Prochain chapitre

→ **[Chapitre 5-4 — Exceptions personnalisées](5-4-exceptions-personnalisees)**
