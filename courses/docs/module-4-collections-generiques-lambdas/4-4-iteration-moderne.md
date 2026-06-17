---
id: 4-4-iteration-moderne
sidebar_position: 4
title: "Itération moderne"
description: "Parcourir et modifier des collections sans risque : Iterator, for-each sous le capot, forEach, vues de Map et removeIf."
---

# Itération moderne

## Pourquoi ce chapitre

Vous savez déjà parcourir une `List` avec un for-each ou un for indexé. Mais parcourir une `Map`, ou **supprimer des éléments pendant le parcours**, demande des outils différents. Ce chapitre dévoile ce qui se passe « sous le capot » du for-each, et vous donne les techniques sûres pour modifier une collection pendant qu'on l'explore.

Vous verrez aussi la méthode `forEach(...)`, qui accepte une simple « action à appliquer » : c'est votre première rencontre concrète avec cette syntaxe — elle sera formalisée au chapitre 4-7.

## Ce que vous saurez faire à la fin

- **Expliquer** ce que fait le for-each sous le capot (interface `Iterable` et `Iterator`).
- **Utiliser** un `Iterator` manuellement pour supprimer des éléments sans erreur.
- **Identifier** l'erreur `ConcurrentModificationException` et l'éviter.
- **Parcourir** une `Map` via ses vues `keySet`, `values` et `entrySet`.
- **Lire** une entrée de `Map` avec `Map.Entry` (`getKey` / `getValue`).
- **Supprimer** des éléments selon un critère avec `removeIf`.

## 1. L'Iterator : le for-each sous le capot

### La mécanique cachée

Quand vous écrivez un for-each sur une collection, Java le traduit automatiquement en appels à un **Iterator** (itérateur). Un `Iterator<E>` est un objet qui sait avancer dans une collection, case après case, grâce à deux méthodes :

| Méthode | Rôle |
|---|---|
| `boolean hasNext()` | Renvoie `true` s'il reste des éléments. |
| `E next()` | Renvoie l'élément suivant et avance d'un pas. |

### Exemple

```java
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IteratorDemo {
    public static void main(String[] args) {
        List<String> couleurs = new ArrayList<>();
        couleurs.add("rouge");
        couleurs.add("vert");
        couleurs.add("bleu");

        // Ce que le for-each fait derrière la scène :
        Iterator<String> it = couleurs.iterator(); // on demande un itérateur
        while (it.hasNext()) {                      // tant qu'il reste des éléments
            String c = it.next();                   // on prend le suivant
            System.out.println(c);
        }
        // rouge
        // vert
        // bleu

        // Le for-each ci-dessous est STRICTEMENT équivalent :
        for (String c : couleurs) {
            System.out.println(c);
        }
    }
}
```

Les deux boucles font exactement la même chose. Le for-each est simplement une écriture plus courte que Java traduit pour vous.

### À retenir

> - Toute collection Java implémente `Iterable`, ce qui lui permet de fournir un `Iterator`.
> - Un `Iterator` avance en avant uniquement : `hasNext()` teste, `next()` consomme.
> - Le for-each est un raccourci syntaxique : il cache l'`Iterator`, il ne le supprime pas.

## 2. Supprimer pendant l'itération : `iterator.remove()`

### Le piège ConcurrentModificationException

Supprimer un élément d'une collection **directement dans un for-each** provoque une erreur à l'exécution :

```java
import java.util.ArrayList;
import java.util.List;

public class SuppressionDangereuse {
    public static void main(String[] args) {
        List<String> mots = new ArrayList<>();
        mots.add("alpha");
        mots.add("bêta");
        mots.add("gamma");

        // DANGER : ne jamais faire ceci
        for (String m : mots) {
            if (m.startsWith("b")) {
                mots.remove(m); // ConcurrentModificationException !
            }
        }
    }
}
```

L'erreur s'appelle `ConcurrentModificationException` (modification concurrente). Elle survient parce que la collection et l'itérateur interne du for-each se « marchent dessus » : l'un supprime pendant que l'autre compte les cases.

### La solution : `iterator.remove()`

L'`Iterator` propose une troisième méthode, `remove()`, qui supprime l'élément **que `next()` vient de rendre**, de façon coordonnée et sûre :

```java
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SuppressionSure {
    public static void main(String[] args) {
        List<String> mots = new ArrayList<>();
        mots.add("alpha");
        mots.add("bêta");
        mots.add("gamma");

        Iterator<String> it = mots.iterator();
        while (it.hasNext()) {
            String m = it.next();        // on lit l'élément
            if (m.startsWith("b")) {
                it.remove();             // suppression sûre via l'itérateur
            }
        }

        System.out.println(mots);        // [alpha, gamma]
    }
}
```

La règle est simple : **toujours appeler `next()` avant `it.remove()`**. L'itérateur sait alors précisément quel élément supprimer.

### À retenir

> - Modifier une collection pendant un for-each ordinaire lève une `ConcurrentModificationException`.
> - `iterator.remove()` est la seule façon sûre de supprimer pendant un parcours avec `Iterator`.
> - L'ordre est strict : `next()` d'abord, `remove()` ensuite.

## 3. `forEach(...)` : une action à appliquer

Les collections offrent aussi la méthode `forEach(...)`, qui applique une **action** à chaque élément. L'action s'écrit sous la forme `element -> ce qu'on fait`, que vous verrez appelée une **lambda** :

```java
import java.util.ArrayList;
import java.util.List;

public class ForEachDemo {
    public static void main(String[] args) {
        List<String> villes = new ArrayList<>();
        villes.add("Paris");
        villes.add("Lyon");
        villes.add("Marseille");

        // Afficher chaque ville avec forEach
        villes.forEach(ville -> System.out.println("Ville : " + ville));
        // Ville : Paris
        // Ville : Lyon
        // Ville : Marseille
    }
}
```

Lisez `ville -> System.out.println(...)` comme : « pour chaque ville, fais `System.out.println(...)` ». C'est une recette d'action, courte et directe.

**Point de vigilance** : `forEach` n'est pas conçu pour modifier la collection (ajouter ou supprimer). Pour cela, préférez `iterator.remove()` ou `removeIf` (section 5).

### À retenir

> - `forEach(element -> action)` applique une action à chaque élément, dans l'ordre.
> - Cette syntaxe `->` est un avant-goût des **lambdas**, formalisées au chapitre 4-7.
> - `forEach` parcourt ; il ne supprime pas — utilisez `removeIf` pour filtrer.

## 4. Parcourir une Map : `keySet`, `values`, `entrySet`

Une `Map` n'est pas une collection d'éléments simples : chaque entrée est un couple **clé → valeur**. Elle propose trois vues pour l'explorer :

| Vue | Type | Ce qu'elle contient |
|---|---|---|
| `keySet()` | `Set<K>` | Toutes les clés. |
| `values()` | `Collection<V>` | Toutes les valeurs (avec doublons possibles). |
| `entrySet()` | `Set<Map.Entry<K,V>>` | Tous les couples clé-valeur. |

### Exemple

```java
import java.util.HashMap;
import java.util.Map;

public class VuesMapDemo {
    public static void main(String[] args) {
        Map<String, Integer> scores = new HashMap<>();
        scores.put("Alice", 85);
        scores.put("Bob", 92);
        scores.put("Clara", 78);

        // Parcourir uniquement les clés
        for (String nom : scores.keySet()) {
            System.out.println("Stagiaire : " + nom);
        }

        // Parcourir uniquement les valeurs
        for (int note : scores.values()) {
            System.out.println("Note : " + note);
        }
    }
}
```

### À retenir

> - `keySet()` donne un `Set` des clés — itérable avec un for-each.
> - `values()` donne une `Collection` des valeurs — peut contenir des doublons.
> - `entrySet()` donne les couples complets — c'est la vue la plus complète (section 5).

## 5. `Map.Entry` : lire clé et valeur ensemble

Quand on a besoin de la clé **et** de la valeur en même temps, `entrySet()` est la bonne vue. Chaque élément est un `Map.Entry<K,V>` qui expose deux méthodes :

- `getKey()` — renvoie la clé.
- `getValue()` — renvoie la valeur.

### Exemple

```java
import java.util.HashMap;
import java.util.Map;

public class EntrySetDemo {
    public static void main(String[] args) {
        Map<String, Integer> scores = new HashMap<>();
        scores.put("Alice", 85);
        scores.put("Bob", 92);
        scores.put("Clara", 78);

        // Parcourir clé et valeur ensemble via entrySet
        for (Map.Entry<String, Integer> entree : scores.entrySet()) {
            String nom  = entree.getKey();    // la clé
            int    note = entree.getValue();  // la valeur
            System.out.println(nom + " → " + note);
        }
        // Alice → 85  (l'ordre dépend de HashMap, non garanti)
        // Bob → 92
        // Clara → 78
    }
}
```

C'est la façon la plus directe de parcourir une `Map` quand on a besoin des deux informations. Elle est aussi plus efficace que d'appeler `get(clé)` dans une boucle sur `keySet()`.

### À retenir

> - `entrySet()` est la vue à privilégier quand on a besoin de la clé **et** de la valeur.
> - `Map.Entry<K,V>` donne accès à `getKey()` et `getValue()`.
> - Évitez de parcourir `keySet()` puis d'appeler `get(clé)` : `entrySet()` est plus lisible et plus efficace.

## 6. `removeIf` : supprimer selon un critère

Pour supprimer des éléments d'une `List` ou d'un `Set` selon un critère, `removeIf(...)` est l'alternative la plus concise et la plus sûre à `iterator.remove()` :

```java
import java.util.ArrayList;
import java.util.List;

public class RemoveIfDemo {
    public static void main(String[] args) {
        List<String> mots = new ArrayList<>();
        mots.add("alpha");
        mots.add("bêta");
        mots.add("gamma");
        mots.add("delta");

        // Supprimer tous les mots de moins de 5 caractères
        mots.removeIf(m -> m.length() < 5);

        System.out.println(mots); // [alpha, gamma, delta]
    }
}
```

Lisez `m -> m.length() < 5` comme : « pour chaque mot `m`, le supprimer si sa longueur est inférieure à 5 ». La méthode gère seule l'itération interne — pas de risque de `ConcurrentModificationException`.

`removeIf` fonctionne aussi sur les `Set`. En revanche, pour supprimer des entrées d'une `Map` selon un critère, utilisez `entrySet().removeIf(...)` ou `values().removeIf(...)`.

### À retenir

> - `removeIf(critere)` supprime tous les éléments pour lesquels le critère est `true`.
> - Il gère l'itération en interne : aucun risque de `ConcurrentModificationException`.
> - C'est l'alternative moderne à la boucle avec `iterator.remove()`.

## Erreurs fréquentes

- **Supprimer dans un for-each** : appeler `collection.remove(element)` à l'intérieur d'un for-each ordinaire lève `ConcurrentModificationException`. Cause : la collection est modifiée pendant que l'itérateur interne compte les cases. Correction : utiliser `iterator.remove()` ou `removeIf(...)`.

- **Appeler `iterator.remove()` avant `next()`** : `remove()` ne sait pas quoi supprimer si `next()` n'a pas encore été appelé. Cause : ordre inversé. Correction : toujours appeler `next()` puis `remove()`.

- **Parcourir `keySet()` quand on veut clé et valeur** : on se retrouve à appeler `map.get(clé)` dans la boucle, ce qui est redondant. Cause : mauvais choix de vue. Correction : utiliser `entrySet()` et `Map.Entry`.

- **Croire que `forEach` peut modifier la collection** : `forEach` n'est pas conçu pour supprimer des éléments pendant le parcours. Cause : confusion avec `removeIf`. Correction : utiliser `removeIf` pour supprimer, `forEach` pour lire ou afficher.

## Exercice guidé

**Contexte** : vous gérez un inventaire matériel, représenté par une `Map<String, Integer>` qui associe le nom d'un article à sa quantité en stock. Vous devez afficher l'inventaire complet, puis élager en supprimant tous les articles dont le stock est tombé à zéro.

**Pas à pas :**

1. Créez une `Map<String, Integer>` nommée `inventaire` et remplissez-la :
   - `"Gourde"` → `3`
   - `"Lampe torche"` → `0`
   - `"Ration"` → `5`
   - `"Couverture"` → `0`
   - `"Boussole"` → `2`

2. Affichez chaque article et sa quantité en utilisant `entrySet()` et `Map.Entry`.

3. Supprimez toutes les entrées dont la valeur est `0`. Utilisez `entrySet().removeIf(...)`.

4. Affichez à nouveau l'inventaire pour confirmer que les articles épuisés ont disparu.

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

```java
import java.util.HashMap;
import java.util.Map;

public class GestionInventaire {

    public static void main(String[] args) {

        // Étape 1 : construction de l'inventaire
        Map<String, Integer> inventaire = new HashMap<>();
        inventaire.put("Gourde",        3);
        inventaire.put("Lampe torche",  0);
        inventaire.put("Ration",        5);
        inventaire.put("Couverture",    0);
        inventaire.put("Boussole",      2);

        // Étape 2 : affichage avec entrySet / Map.Entry
        System.out.println("=== Inventaire complet ===");
        for (Map.Entry<String, Integer> entree : inventaire.entrySet()) {
            System.out.println(entree.getKey() + " : " + entree.getValue());
        }

        // Étape 3 : suppression des articles épuisés avec removeIf
        inventaire.entrySet().removeIf(entree -> entree.getValue() == 0);

        // Étape 4 : affichage après élagage
        System.out.println("\n=== Après élagage (stock > 0) ===");
        inventaire.forEach((article, quantite) ->
            System.out.println(article + " : " + quantite)
        );
        // Gourde : 3
        // Ration : 5
        // Boussole : 2
    }
}
```

**Points clés** :
- `entrySet()` donne accès simultané à la clé et à la valeur.
- `entrySet().removeIf(...)` supprime les entrées de la `Map` de façon sûre.
- Le deuxième affichage utilise `forEach` avec deux paramètres `(article, quantite)` — une variante de la lambda adaptée aux `Map`.

</details>

## Vérifiez vos acquis

- Quelle est la différence entre `iterator.remove()` et `collection.remove(element)` à l'intérieur d'une boucle ?
- Pourquoi `entrySet()` est-il préférable à `keySet()` quand on a besoin des clés et des valeurs ?
- Dans quelle situation `removeIf` est-il plus adapté qu'une boucle avec `Iterator` ?
- Que se passe-t-il si vous appelez `iterator.remove()` sans avoir appelé `next()` au préalable ?

## Pour aller plus loin

- [The Collection Interface — Iterators](https://dev.java/learn/api/collections-framework/the-collection-interface/) (dev.java) — explication officielle de l'`Iterator` et du for-each.
- [Guide to the Java Iterator](https://www.baeldung.com/java-iterator) (Baeldung) — exemples complets d'utilisation et de `ConcurrentModificationException`.
- [Interface Iterator](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/Iterator.html) (Javadoc OpenJDK 25) — référence complète des méthodes `hasNext`, `next`, `remove`.

## Prochain chapitre

→ **[Chapitre 4-5 — Génériques](4-5-generiques)**
