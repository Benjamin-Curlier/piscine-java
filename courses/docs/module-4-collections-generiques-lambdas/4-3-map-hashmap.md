---
id: 4-3-map-hashmap
sidebar_position: 3
title: "Map et HashMap"
description: "Associer des clés à des valeurs avec l'interface Map et son implémentation HashMap, et en maîtriser les opérations essentielles."
---

# Map et HashMap

## Pourquoi ce chapitre

Une `List` stocke des éléments un par un, accessibles par leur position. Mais parfois, vous voulez accéder à une valeur par un **nom** ou un **identifiant** — non par un numéro de ligne. Une `Map` (de l'anglais « table de correspondance ») associe des **clés** à des **valeurs** : elle répond à la question « quelle valeur correspond à cette clé ? » en temps quasi constant.

Ce chapitre enseigne les opérations courantes de `HashMap`, la gestion des clés absentes, et introduit un premier idiome de la programmation fonctionnelle sous forme concrète.

## Ce que vous saurez faire à la fin

- **Créer** une `Map` et y associer des paires clé → valeur avec `put`.
- **Lire** une valeur par sa clé avec `get`, et gérer les clés absentes avec `getOrDefault`.
- **Vérifier, modifier et supprimer** des entrées avec `containsKey`, `put` et `remove`.
- **Utiliser** l'idiome `computeIfAbsent` pour initialiser une valeur à la volée.
- **Choisir** entre `HashMap` et `TreeMap` selon les besoins.

## 1. Le principe clé → valeur

Une `Map<K, V>` associe des **clés** de type `K` à des **valeurs** de type `V`. Chaque clé est **unique** dans la map : si vous appelez `put` avec une clé déjà présente, l'ancienne valeur est remplacée.

### Exemple

```java
import java.util.HashMap;
import java.util.Map;

public class MapBasiqueDemo {
    public static void main(String[] args) {
        // Clé : un mot (String), Valeur : le nombre de fois qu'il apparaît (Integer)
        Map<String, Integer> occurrences = new HashMap<>();

        // put(clé, valeur) : ajoute ou remplace
        occurrences.put("bonjour", 3);
        occurrences.put("monde", 1);
        occurrences.put("bonjour", 5);  // remplace 3 par 5 (clé déjà présente)

        // get(clé) : renvoie la valeur, ou null si la clé est absente
        Integer n = occurrences.get("bonjour");  // 5
        Integer absent = occurrences.get("au revoir");  // null : clé inconnue

        // containsKey(clé) : vérifier la présence sans risquer null
        boolean existe = occurrences.containsKey("monde");  // true

        // remove(clé) : supprime l'entrée
        occurrences.remove("monde");

        // size() : nombre de paires clé-valeur
        int taille = occurrences.size();  // 1 (seul "bonjour" reste)
    }
}
```

### À retenir

> - `put(clé, valeur)` ajoute ou **remplace** (les clés sont uniques).
> - `get(clé)` renvoie `null` si la clé est absente — **ne jamais chaîner dessus sans vérifier**.
> - `containsKey(clé)` vérifie la présence sans risquer un `null`.
> - `remove(clé)` supprime la paire, sans erreur si la clé n'existe pas.
> - `size()` renvoie le nombre de paires.

## 2. Gérer les clés absentes : `getOrDefault`

Appeler `get` sur une clé absente renvoie `null`. Si vous utilisez ce résultat sans vérification (par exemple pour l'incrémenter), le programme échoue avec une `NullPointerException`.

`getOrDefault(clé, valeurParDéfaut)` résout cela proprement : il renvoie la valeur si la clé est présente, sinon il renvoie la valeur par défaut que vous fournissez.

### Exemple

```java
import java.util.HashMap;
import java.util.Map;

public class ComptageMots {
    public static void main(String[] args) {
        String phrase = "le chat mange le chat";
        String[] mots = phrase.split(" ");  // découpe en mots

        Map<String, Integer> occurrences = new HashMap<>();

        for (String mot : mots) {
            // getOrDefault : si le mot est inconnu, on part de 0
            int compteur = occurrences.getOrDefault(mot, 0);
            occurrences.put(mot, compteur + 1);
        }

        // Résultat attendu : {le=2, chat=2, mange=1}
        System.out.println(occurrences.get("le"));    // 2
        System.out.println(occurrences.get("chat"));  // 2
        System.out.println(occurrences.get("mange")); // 1
    }
}
```

### À retenir

> - `getOrDefault(clé, défaut)` : renvoie la valeur associée, ou `défaut` si la clé est absente. La map **n'est pas modifiée**.
> - C'est l'outil idéal pour compter des occurrences, cumuler des scores ou agréger des données.

## 3. Initialiser une valeur-collection : l'idiome `computeIfAbsent`

Quand la **valeur** est elle-même une collection (par exemple, une `List` de mots ayant la même longueur), on doit créer cette collection la première fois qu'on rencontre la clé, puis l'enrichir les fois suivantes.

`computeIfAbsent(clé, recette)` fait exactement cela : si la clé est absente, il **crée** la valeur en appliquant la « recette » fournie, l'associe à la clé, et la renvoie. Si la clé est déjà présente, il renvoie la valeur existante sans rien changer.

La « recette » `key -> new ArrayList<>()` est une **petite expression** qu'on écrit directement ici comme argument : elle dit « pour créer la valeur, prends la clé et renvoie une nouvelle liste vide ». Vous verrez au chapitre 4-7 que cette syntaxe s'appelle une **lambda** ; pour l'instant, retenez-la comme une **recette de valeur par défaut** à copier-coller.

### Exemple

```java
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupementParLongueur {
    public static void main(String[] args) {
        String phrase = "le chat mange la pomme verte";
        String[] mots = phrase.split(" ");

        // Regrouper les mots par leur longueur
        Map<Integer, List<String>> parLongueur = new HashMap<>();

        for (String mot : mots) {
            int longueur = mot.length();

            // Si cette longueur n'a pas encore de liste, en créer une
            // "key -> new ArrayList<>()" est la recette de valeur par défaut
            List<String> groupe = parLongueur.computeIfAbsent(longueur, key -> new ArrayList<>());

            // Ajouter le mot dans la liste (qui vient d'être créée ou existait déjà)
            groupe.add(mot);
        }

        System.out.println(parLongueur.get(2));   // [le, la]
        System.out.println(parLongueur.get(4));   // [chat]
        System.out.println(parLongueur.get(5));   // [mange, pomme, verte]
    }
}
```

### À retenir

> - `computeIfAbsent(clé, key -> new ArrayList<>())` : crée la valeur si la clé est absente, renvoie la valeur existante sinon.
> - C'est l'idiome standard pour **grouper** des éléments dans une `Map<K, List<V>>`.
> - La syntaxe `key -> new ArrayList<>()` est une **recette** (une lambda, formalisée au chapitre 4-7) : retenez-en la forme, vous l'expliquerez au chapitre suivant.

## 4. Pourquoi les clés doivent avoir `equals` et `hashCode` corrects

Une `HashMap` ne range pas ses entrées dans un ordre linéaire : elle calcule un **code numérique** (un *hash*) à partir de la clé, et place l'entrée dans un compartiment correspondant. Pour retrouver une valeur, elle refait le même calcul.

Ce mécanisme repose sur **deux méthodes héritées de `Object`** :
- `hashCode()` : calcule le code de compartiment.
- `equals()` : confirme l'égalité en cas de collision.

Si vous utilisez une clé d'un **type standard** (`String`, `Integer`, un `enum`) : ces méthodes sont correctement définies, tout fonctionne.

Si vous utilisez comme clé un objet **d'une classe que vous avez écrite**, vous devez redéfinir `equals` et `hashCode` — c'est précisément ce qu'enseigne le chapitre 4-2. Une classe sans ces redéfinitions se base sur l'identité de l'objet (adresse mémoire), et deux objets « égaux » au sens métier seront considérés comme des clés différentes.

### À retenir

> - Les clés d'une `HashMap` **doivent** avoir `equals` et `hashCode` corrects pour fonctionner comme prévu.
> - `String`, `Integer`, et les `enum` les définissent déjà : utilisez-les sans crainte comme clés.
> - Pour vos propres classes, consultez le chapitre 4-2 avant de les employer comme clés.

## 5. `HashMap` vs `TreeMap`

| | `HashMap` | `TreeMap` |
|---|---|---|
| Ordre des clés | Aucun ordre garanti | Triées selon leur ordre naturel |
| Performance | Très rapide en général | Légèrement plus lente (arbre rouge-noir) |
| Prérequis sur la clé | `equals` + `hashCode` | La clé doit être `Comparable` (ou fournir un `Comparator`) |
| Usage typique | Comptage, recherche rapide | Affichage trié, navigation par plage de clés |

### Exemple

```java
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class TreeMapDemo {
    public static void main(String[] args) {
        Map<String, Integer> parMot = new HashMap<>();
        parMot.put("chat", 2);
        parMot.put("âne", 1);
        parMot.put("baleine", 3);

        // HashMap : ordre d'affichage imprévisible
        System.out.println(parMot);  // {chat=2, âne=1, baleine=3} ou autre ordre

        // TreeMap : clés triées alphabétiquement
        Map<String, Integer> trie = new TreeMap<>(parMot);
        System.out.println(trie);    // {baleine=3, chat=2, âne=1}
        // (tri Unicode : les lettres accentuées arrivent après z)
    }
}
```

### À retenir

> - **Par défaut, choisissez `HashMap`** : elle est plus rapide et ne nécessite pas que la clé soit `Comparable`.
> - Utilisez `TreeMap` quand vous avez besoin d'**itérer sur les clés dans leur ordre naturel**.
> - La variable est toujours déclarée `Map<K, V>` : l'implémentation s'échange en un mot.

## Erreurs fréquentes

- **`NullPointerException` en chaînant sur `get`** : `occurrences.get("absent").toString()` échoue dès que la clé est absente, car `get` renvoie `null`. Symptôme : `NullPointerException` sur la ligne de chaînage. Correction : utilisez `getOrDefault` ou vérifiez `containsKey` avant d'utiliser la valeur.

- **Utiliser comme clé un objet sans `equals`/`hashCode`** : deux objets « identiques » au sens métier sont traités comme deux clés différentes ; la map grossit indéfiniment et `get` ne retrouve jamais rien. Symptôme : `get` renvoie `null` même après un `put` avec un objet semblable. Correction : redéfinissez `equals` et `hashCode` dans la classe de la clé (chapitre 4-2).

- **`get` + `put` là où `computeIfAbsent` est plus clair** : écrire `if (!map.containsKey(k)) { map.put(k, new ArrayList<>()); } map.get(k).add(v)` fonctionne mais est verbeux et sujet aux erreurs. Remplacez par `map.computeIfAbsent(k, key -> new ArrayList<>()).add(v)` : une ligne, sans risque d'oubli.

- **Modifier la map pendant une itération directe** : appeler `put` ou `remove` sur la map pendant qu'on parcourt son contenu avec un for-each déclenche une `ConcurrentModificationException`. Collectez d'abord les clés à modifier, puis appliquez les modifications après la boucle. (Le chapitre 4-4 traite de l'itération en détail.)

## Exercice guidé

**Domaine** : comptage d'occurrences de mots dans une phrase.

**Énoncé** : écrivez un programme qui compte combien de fois chaque mot apparaît dans une phrase, puis affiche les résultats.

**Phrase de test** : `"le soleil brille le soleil brille encore le soleil"`

**Pas-à-pas** :

1. Déclarez une `Map<String, Integer>` implémentée par `HashMap`.
2. Découpez la phrase en mots avec `phrase.split(" ")`.
3. Pour chaque mot, récupérez le compteur actuel avec `getOrDefault(mot, 0)`, incrémentez-le de 1, et rangez-le avec `put`.
4. Affichez le compteur pour `"soleil"` et pour `"encore"`.
5. **Bonus** : regroupez les mots par leur longueur dans une `Map<Integer, List<String>>` en utilisant `computeIfAbsent`.

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

```java
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComptageOccurrences {
    public static void main(String[] args) {
        String phrase = "le soleil brille le soleil brille encore le soleil";
        String[] mots = phrase.split(" ");

        // --- Étapes 1 à 4 : comptage ---
        Map<String, Integer> occurrences = new HashMap<>();

        for (String mot : mots) {
            int compteur = occurrences.getOrDefault(mot, 0);
            occurrences.put(mot, compteur + 1);
        }

        System.out.println("soleil : " + occurrences.get("soleil"));   // 3
        System.out.println("encore : " + occurrences.get("encore"));   // 1
        System.out.println(occurrences);
        // {encore=1, soleil=3, brille=2, le=3} (ordre non garanti)

        // --- Bonus : regrouper par longueur ---
        Map<Integer, List<String>> parLongueur = new HashMap<>();

        for (String mot : mots) {
            parLongueur.computeIfAbsent(mot.length(), key -> new ArrayList<>()).add(mot);
        }

        System.out.println("Mots de 6 lettres : " + parLongueur.get(6));   // [soleil, brille, soleil, brille, encore, soleil]
        System.out.println("Mots de 2 lettres : " + parLongueur.get(2));   // [le, le, le]
    }
}
```

</details>

## Vérifiez vos acquis

- Que renvoie `get` lorsque la clé est absente, et quelle méthode utilise-t-on pour éviter ce cas ?
- Pourquoi les clés d'une `HashMap` doivent-elles avoir `equals` et `hashCode` corrects ?
- Quel est le rôle de `computeIfAbsent`, et dans quel contexte est-il particulièrement utile ?
- Dans quelle situation préféreriez-vous `TreeMap` à `HashMap` ?

## Pour aller plus loin

- [The Map Interface](https://dev.java/learn/api/collections-framework/maps/) (dev.java) — présentation officielle de l'interface `Map` et de ses implémentations.
- [Guide to HashMap in Java](https://www.baeldung.com/java-hashmap) (Baeldung) — création, manipulation, cas d'usage et comportement interne de `HashMap`.
- [Interface Map — Javadoc OpenJDK 25](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/Map.html) — référence complète de toutes les méthodes disponibles.

## Prochain chapitre

→ **[Chapitre 4.4 — Itération moderne](4-4-iteration-moderne)**
