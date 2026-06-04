---
id: 3-10-records-et-sealed
sidebar_position: 10
title: "Records et sealed"
description: "Découvrir les types modernes de Java : records immuables, hiérarchies scellées et pattern matching exhaustif."
---

# Records et sealed

## Pourquoi ce chapitre

Écrire une classe qui ne fait que **transporter des données** (un point, une coordonnée) demande beaucoup de code répétitif : attributs, constructeur, accesseurs… Et fermer proprement une hiérarchie (« une figure est un cercle **ou** un carré, rien d'autre ») n'était pas exprimable simplement.

Java moderne apporte deux outils élégants : le **record**, pour les porteurs de données immuables, et les classes **`sealed`**, pour les hiérarchies fermées. Ce chapitre clôt le module en réunissant tout ce que vous avez appris.

## Ce que vous saurez faire à la fin

- **Définir** un `record` immuable et utiliser ce qu'il génère.
- **Fermer** une hiérarchie avec `sealed` / `permits`.
- **Écrire** un `switch` exhaustif par pattern matching.

## 1. Le record, porteur de données immuable

Un **record** déclare une classe dont le seul rôle est de regrouper des données. À partir de la liste de ses composants, Java **génère automatiquement** : le constructeur, les accesseurs (`x()`, `y()`), ainsi que `equals()`, `hashCode()` et `toString()`.

### Exemple

```java
public record Point(int x, int y) {}
```

```java
Point p = new Point(3, 4);
System.out.println(p.x());      // 3 : accesseur généré
System.out.println(p);          // Point[x=3, y=4] : toString généré
```

Un record est **immuable** : ses composants sont fixés à la construction, il n'y a pas de setter. Pour « modifier » un point, on en crée un nouveau.

### À retenir

> - Un `record` regroupe des données ; Java génère constructeur, accesseurs, `equals`, `hashCode`, `toString`.
> - Un record est **immuable** : aucun setter, on crée un nouvel objet pour changer une valeur.

## 2. Profiter de l'`equals` généré

Parmi les méthodes générées, `equals()` compare les records **par leurs valeurs** : deux points de mêmes composants sont égaux. C'est exactement le comportement attendu d'un porteur de données.

### Exemple

```java
Point a = new Point(3, 4);
Point b = new Point(3, 4);
System.out.println(a.equals(b));   // true : mêmes composants
System.out.println(a == b);        // false : deux objets distincts
```

Vous **utilisez** cet `equals` sans avoir à l'écrire. Écrire soi-même `equals` et `hashCode` (pour une classe ordinaire) est une notion à part entière, abordée au module 4 — ici, le record s'en charge pour vous.

### À retenir

> - L'`equals` généré compare un record **par ses valeurs**.
> - On s'appuie dessus ; écrire `equals`/`hashCode` à la main viendra plus tard.

## 3. Fermer une hiérarchie avec `sealed`

Une interface (ou classe) **`sealed`** restreint la liste de ses sous-types : seuls les types listés après `permits` peuvent l'implémenter. La hiérarchie est ainsi **fermée** et entièrement connue.

### Exemple

```java
public sealed interface Figure permits Cercle, Carre {}

public record Cercle(double rayon) implements Figure {}
public record Carre(double cote) implements Figure {}
```

Chaque sous-type d'un type `sealed` doit préciser son propre degré d'ouverture : `final` (ici, un record l'est implicitement), `sealed`, ou `non-sealed`. Aucune autre figure que `Cercle` ou `Carre` ne peut exister.

### À retenir

> - `sealed ... permits ...` fixe la liste **exhaustive** des sous-types autorisés.
> - Chaque sous-type est `final`, `sealed` ou `non-sealed` (un record est `final`).

## 4. Pattern matching exhaustif

L'intérêt d'une hiérarchie `sealed` apparaît avec le `switch` par **pattern matching** : comme le compilateur connaît **tous** les cas possibles, il vérifie que le `switch` les couvre tous. Inutile (et même déconseillé) d'ajouter un `default`.

### Exemple

```java
public static double aire(Figure figure) {
    return switch (figure) {
        case Cercle c -> Math.PI * c.rayon() * c.rayon();
        case Carre carre -> carre.cote() * carre.cote();
    };   // pas de default : tous les cas sont couverts, le compilateur le vérifie
}
```

Si demain on ajoute un type `Triangle` à `permits`, le compilateur signalera que ce `switch` n'est plus exhaustif : on est forcé de traiter le nouveau cas. Un `default` aurait masqué cette vérification précieuse.

### À retenir

> - Sur une hiérarchie `sealed`, le `switch` peut être **exhaustif sans `default`**.
> - Le compilateur garantit que tous les cas sont traités — un `default` annulerait ce garde-fou.

## Erreurs fréquentes

- **Tenter de muter un record** : un record est immuable ; il n'a pas de setter et ses composants ne changent pas. Pour une valeur différente, créez un nouveau record.
- **Oublier un type dans `permits`** : un sous-type non listé (ou un fichier mal placé) provoque une erreur de compilation. La liste `permits` doit être complète et cohérente.
- **Ajouter un `default` inutile sur un `switch` `sealed`** : il masque la vérification d'exhaustivité. Si un type est ajouté plus tard, le compilateur ne préviendra plus. Laissez le `switch` exhaustif sans `default`.
- **Réécrire `equals` d'un record à la main** : inutile, il est déjà généré et correct. On l'utilise tel quel.

## Exercice guidé

**Objectif** : combiner record, `sealed` et pattern matching.

Définissez un `record Point(int x, int y)`. Puis une `sealed interface Figure permits Cercle, Carre`, avec `Cercle` et `Carre` déclarés comme records implémentant `Figure`. Écrivez une méthode `aire(Figure f)` qui calcule l'aire par un `switch` **sans `default`**. Testez avec un cercle et un carré.

Indices :
- Un record `Cercle(double rayon)` et un record `Carre(double cote)`.
- Le `switch` a un `case` par type ; pas de `default` puisque la hiérarchie est fermée.

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

```java
public record Point(int x, int y) {}

public sealed interface Figure permits Cercle, Carre {}

public record Cercle(double rayon) implements Figure {}
public record Carre(double cote) implements Figure {}

public class Demo {
    static double aire(Figure figure) {
        return switch (figure) {
            case Cercle c -> Math.PI * c.rayon() * c.rayon();
            case Carre carre -> carre.cote() * carre.cote();
        };
    }

    public static void main(String[] args) {
        Point origine = new Point(0, 0);
        System.out.println(origine);                 // Point[x=0, y=0]

        System.out.println(aire(new Cercle(2.0)));   // ~12.566...
        System.out.println(aire(new Carre(3.0)));    // 9.0
    }
}
```

</details>

## Vérifiez vos acquis

- Que génère automatiquement un `record` ?
- Pourquoi dit-on qu'un record est immuable ?
- Que garantit le mot-clé `sealed` sur une hiérarchie ?
- Pourquoi éviter un `default` dans un `switch` sur une hiérarchie `sealed` ?

## Pour aller plus loin

- [Record Classes](https://docs.oracle.com/en/java/javase/25/language/records.html) (Javadoc 25) — déclaration et membres générés.
- [Sealed Classes and Interfaces](https://docs.oracle.com/en/java/javase/25/language/sealed-classes-and-interfaces.html) (Javadoc 25) — `sealed`, `permits`, exhaustivité.

## Prochain chapitre

→ **[Module 4 — Collections, génériques, lambdas](../module-4-collections-generiques-lambdas/4-1-list-arraylist)**
