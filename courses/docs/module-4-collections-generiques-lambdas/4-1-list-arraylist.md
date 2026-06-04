---
id: 4-1-list-arraylist
sidebar_position: 1
title: "List et ArrayList"
description: "Utiliser l'interface List et son implémentation ArrayList pour stocker une suite ordonnée d'éléments à taille variable."
---

# List et ArrayList

## Pourquoi ce chapitre

Au module 2, vous avez utilisé les tableaux pour stocker plusieurs valeurs. Un tableau a un défaut rédhibitoire : sa taille est fixée à la création et ne change plus jamais. Ajouter un onzième élément dans un tableau de dix cases est impossible sans en créer un nouveau.

L'interface `List` du JDK (Java Development Kit) résout ce problème. Elle représente une **suite ordonnée d'éléments** qui grandit et rétrécit à la demande. `ArrayList` est son implémentation la plus courante ; elle est au tableau ce que le classeur extensible est au carton rigide.

## Ce que vous saurez faire à la fin

- **Distinguer** l'interface `List` de l'implémentation `ArrayList` et comprendre pourquoi on déclare avec `List`.
- **Ajouter, lire, modifier et supprimer** des éléments avec `add`, `get`, `set`, `remove`, `size`, `isEmpty`.
- **Parcourir** une liste par indice et avec un for-each.
- **Rechercher** un élément avec `contains` et `indexOf`.
- **Choisir** entre `ArrayList` et `LinkedList` selon l'usage.

## 1. Interface `List` vs implémentation `ArrayList`

En Java, une **interface** est un contrat : elle dit *ce qu'on peut faire* sans dire *comment*. `List<E>` (du package `java.util`) est ce contrat pour les suites ordonnées. `ArrayList<E>` est l'une des classes qui **réalise** ce contrat.

La bonne pratique est de **typer la variable par l'interface** :

```java
List<String> courses = new ArrayList<>();
// À gauche : le contrat (ce qu'on peut faire)
// À droite : l'implémentation (comment c'est fait en mémoire)
```

Pourquoi ? Parce que si demain vous décidez de changer d'implémentation, vous ne modifiez qu'un seul mot. Tout le reste du code qui utilise `courses` ne change pas.

### À retenir

> - `List<E>` est l'interface (le contrat). `ArrayList<E>` est l'implémentation.
> - Déclarez toujours : `List<String> liste = new ArrayList<>();`
> - L'implémentation est interchangeable ; le contrat, lui, reste stable.

## 2. Les opérations de base

Voici les méthodes incontournables, illustrées avec une liste de courses.

### Exemple

```java
import java.util.ArrayList;
import java.util.List;

public class ListeDeCoursesDemo {
    public static void main(String[] args) {
        List<String> courses = new ArrayList<>();

        // --- Ajouter des éléments ---
        courses.add("pain");          // ["pain"]
        courses.add("lait");          // ["pain", "lait"]
        courses.add("oeufs");         // ["pain", "lait", "oeufs"]
        courses.add(1, "beurre");     // insertion à l'indice 1 : ["pain", "beurre", "lait", "oeufs"]

        // --- Lire ---
        String premier = courses.get(0);  // "pain" (indice 0 = premier élément)
        int taille = courses.size();      // 4 éléments

        // --- Modifier ---
        courses.set(2, "crème");     // remplace "lait" par "crème" à l'indice 2

        // --- Supprimer ---
        courses.remove("beurre");    // supprime par valeur : ["pain", "crème", "oeufs"]
        courses.remove(0);           // supprime à l'indice 0 : ["crème", "oeufs"]

        // --- Vérifier l'état ---
        boolean vide = courses.isEmpty();  // false : la liste contient encore 2 éléments
        System.out.println(courses);       // [crème, oeufs]
    }
}
```

`remove` accepte deux formes : un `int` (indice) ou un `Object` (valeur). Pour supprimer un `Integer` par valeur, passez un objet `Integer` explicite.

### À retenir

> | Méthode | Ce qu'elle fait |
> |---|---|
> | `add(element)` | Ajoute en fin de liste |
> | `add(indice, element)` | Insère à la position donnée |
> | `get(indice)` | Lit l'élément à cet indice |
> | `set(indice, element)` | Remplace l'élément à cet indice |
> | `remove(indice)` | Supprime à cet indice |
> | `remove(objet)` | Supprime la première occurrence de cet objet |
> | `size()` | Nombre d'éléments |
> | `isEmpty()` | `true` si la liste ne contient rien |

## 3. Parcourir une liste

Il existe deux façons de parcourir une `List` : le **for indexé** (quand l'indice est utile) et le **for-each** (quand on veut juste chaque élément).

### Exemple

```java
import java.util.ArrayList;
import java.util.List;

public class ParcoursDemo {
    public static void main(String[] args) {
        List<String> courses = new ArrayList<>();
        courses.add("pain");
        courses.add("lait");
        courses.add("oeufs");

        // Parcours indexé : utile quand on a besoin du numéro de position
        for (int i = 0; i < courses.size(); i++) {
            System.out.println(i + " : " + courses.get(i));
        }
        // 0 : pain
        // 1 : lait
        // 2 : oeufs

        // For-each : plus lisible quand l'indice ne sert pas
        for (String article : courses) {
            System.out.println("- " + article);
        }
        // - pain
        // - lait
        // - oeufs
    }
}
```

### À retenir

> - **For indexé** : `for (int i = 0; i < liste.size(); i++)` — quand l'indice compte.
> - **For-each** : `for (String element : liste)` — plus court, plus lisible.
> - Ne modifiez pas la taille de la liste (ajout ou suppression) pendant un for-each : le programme échoue avec une `ConcurrentModificationException`. Utilisez le for indexé en sens inverse ou une liste séparée pour les suppressions.

## 4. Rechercher dans une liste

`contains` et `indexOf` évitent d'écrire une boucle de recherche à la main.

### Exemple

```java
import java.util.ArrayList;
import java.util.List;

public class RechercheDemo {
    public static void main(String[] args) {
        List<String> courses = new ArrayList<>();
        courses.add("pain");
        courses.add("lait");
        courses.add("oeufs");

        // Vérifier la présence
        boolean aLait = courses.contains("lait");   // true
        boolean aVin  = courses.contains("vin");    // false

        // Trouver la position
        int position = courses.indexOf("lait");     // 1
        int absent   = courses.indexOf("vin");      // -1 si absent (jamais d'exception)
    }
}
```

### À retenir

> - `contains(objet)` : renvoie `true` si l'objet est dans la liste.
> - `indexOf(objet)` : renvoie l'indice de la première occurrence, ou `-1` si absent.
> - Ces méthodes utilisent `equals` pour comparer : elles fonctionnent correctement avec des `String` et des types qui redéfinissent `equals`.

## 5. `ArrayList` vs `LinkedList` : quand choisir ?

Le JDK fournit plusieurs implémentations de `List`. Les deux plus courantes sont :

| | `ArrayList` | `LinkedList` |
|---|---|---|
| Stockage interne | Tableau redimensionnable | Chaîne de nœuds liés |
| `get(i)` (lecture par indice) | Rapide (accès direct) | Lent (parcours depuis le début) |
| `add` / `remove` en milieu | Lent (décalage des éléments) | Rapide (recâblage d'un nœud) |
| Usage typique | Lecture fréquente par indice | Insertions/suppressions nombreuses en début ou milieu |

### Exemple

```java
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ChoixImplDemo {
    public static void main(String[] args) {
        // Beaucoup de lectures par indice → ArrayList
        List<String> journal = new ArrayList<>();

        // Beaucoup d'insertions en début → LinkedList
        List<String> file = new LinkedList<>();
    }
}
```

### À retenir

> - **Par défaut, choisissez `ArrayList`** : elle est plus rapide pour les lectures, et la plupart des listes sont lues plus souvent que modifiées.
> - Utilisez `LinkedList` uniquement quand vous ajoutez ou supprimez **très fréquemment** en tête ou en milieu de liste.
> - La variable est toujours déclarée `List<…>` : changer d'implémentation ne coûte qu'un mot.

## Erreurs fréquentes

- **Déclarer `ArrayList` au lieu de `List` côté gauche** (`ArrayList<String> l = new ArrayList<>()`) : cela fonctionne, mais vous perdez la flexibilité d'interchanger l'implémentation. Préférez `List<String> l = new ArrayList<>()`.

- **`IndexOutOfBoundsException` sur `get(size())`** : les indices vont de `0` à `size() - 1`. Écrire `liste.get(liste.size())` dépasse toujours d'un cran. Symptôme : `IndexOutOfBoundsException`. Correction : utilisez `liste.get(liste.size() - 1)` pour le dernier élément, ou vérifiez que la liste n'est pas vide avec `isEmpty()`.

- **Modifier la taille pendant un for-each** : supprimer ou ajouter un élément dans la liste qu'on parcourt avec `for (String s : liste)` déclenche une `ConcurrentModificationException`. Correction : parcourez par indice à rebours (`for (int i = liste.size() - 1; i >= 0; i--)`) ou collectez les éléments à supprimer dans une seconde liste, puis appelez `liste.removeAll(aSupprimer)`.

- **Confondre `remove(int)` et `remove(Object)`** : `liste.remove(1)` supprime l'élément à l'indice 1 ; `liste.remove("lait")` supprime la valeur `"lait"`. Avec une `List<Integer>`, passez `Integer.valueOf(n)` pour supprimer par valeur et éviter toute ambiguïté.

## Exercice guidé

**Domaine** : liste de courses.

**Énoncé** : gérez une liste de courses depuis un programme principal.

1. Créez une `List<String>` vide.
2. Ajoutez quatre articles : `"pain"`, `"lait"`, `"oeufs"`, `"beurre"`.
3. Affichez tous les articles avec leur numéro de position (indice + 1, pour commencer à 1).
4. L'utilisateur a déjà du lait : supprimez `"lait"` par valeur.
5. Il faut du fromage en première position : insérez-le à l'indice 0.
6. Vérifiez si `"oeufs"` est encore dans la liste et affichez le résultat.
7. Affichez la taille finale de la liste.

**Pas-à-pas** :
- Étape 1 : `List<String> courses = new ArrayList<>();`
- Étape 3 : boucle indexée de `0` à `courses.size() - 1`, affichez `(i + 1) + ". " + courses.get(i)`.
- Étape 4 : `courses.remove("lait")` (remove par valeur).
- Étape 5 : `courses.add(0, "fromage")` (insertion à l'indice 0).
- Étape 6 : `courses.contains("oeufs")` renvoie un `boolean`.
- Étape 7 : `courses.size()`.

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

```java
import java.util.ArrayList;
import java.util.List;

public class ListeDeCourses {
    public static void main(String[] args) {
        // 1. Créer la liste
        List<String> courses = new ArrayList<>();

        // 2. Ajouter quatre articles
        courses.add("pain");
        courses.add("lait");
        courses.add("oeufs");
        courses.add("beurre");

        // 3. Afficher avec numérotation à partir de 1
        System.out.println("Ma liste de courses :");
        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i + 1) + ". " + courses.get(i));
        }

        // 4. Supprimer "lait" par valeur
        courses.remove("lait");

        // 5. Insérer "fromage" en première position
        courses.add(0, "fromage");

        // 6. Vérifier la présence de "oeufs"
        boolean aOeufs = courses.contains("oeufs");
        System.out.println("Oeufs dans la liste : " + aOeufs);  // true

        // 7. Taille finale
        System.out.println("Nombre d'articles : " + courses.size());  // 4
        System.out.println(courses);  // [fromage, pain, oeufs, beurre]
    }
}
```

</details>

## Vérifiez vos acquis

- Pourquoi déclare-t-on `List<String> liste = new ArrayList<>()` plutôt que `ArrayList<String> liste = new ArrayList<>()` ?
- Que renvoie `indexOf` lorsque l'élément recherché est absent ? Comment l'utiliseriez-vous pour éviter une erreur ?
- Quelle erreur se produit si l'on supprime des éléments pendant un for-each, et comment la contourner ?
- Dans quel cas préféreriez-vous `LinkedList` à `ArrayList` ?

## Pour aller plus loin

- [The List Interface](https://dev.java/learn/api/collections-framework/lists/) (dev.java) — présentation officielle de l'interface `List` et de ses implémentations.
- [Guide to ArrayList in Java](https://www.baeldung.com/java-arraylist) (Baeldung) — création, manipulation et cas d'usage de `ArrayList`.
- [Interface List — Javadoc OpenJDK 25](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/List.html) — référence complète de toutes les méthodes disponibles.

## Prochain chapitre

→ **[Chapitre 4.2 — Set, HashSet, equals et hashCode](4-2-set-hashset-equals-hashcode)**
