---
id: 4-8-streams-optional
sidebar_position: 8
title: "Streams et Optional"
description: "Apprendre à construire des pipelines de traitement de données avec les streams Java et à gérer l'absence de valeur avec Optional."
---

# Streams et Optional

## Pourquoi ce chapitre

Les collections (chapitres 4-1 à 4-3) stockent des données ; les lambdas (chapitre 4-7) décrivent des traitements. Les **streams** — ou flux de données — combinent les deux : ils permettent de filtrer, transformer et collecter des éléments en une suite d'opérations lisibles et concises. En parallèle, **`Optional`** offre un moyen propre d'exprimer qu'une valeur peut être absente, sans recourir au `null`.

Ce chapitre clôt le module 4 en assemblant tout ce que vous avez appris.

## Ce que vous saurez faire à la fin

- **Créer** un stream depuis une collection avec `stream()`.
- **Appliquer** des opérations intermédiaires : `filter`, `map`, `sorted`, `distinct`, `limit`.
- **Déclencher** le pipeline avec des opérations terminales : `collect`, `count`, `reduce`, `forEach`.
- **Utiliser** les collecteurs courants (`toList`, `joining`) et découvrir `groupingBy` et `partitioningBy`.
- **Manipuler** un `Optional` : tester la présence, fournir une valeur par défaut, transformer avec `map`.
- **Interpréter** le résultat de `findFirst`, `max` et `min` qui renvoient un `Optional`.
- **Choisir** entre stream et boucle selon le contexte.

## 1. Créer un stream

Un **stream** (flux) est une séquence d'éléments à traiter une seule fois. Il **ne modifie pas** la collection source : il produit un résultat à part.

On crée un stream depuis une `List` (ou n'importe quelle `Collection`) avec la méthode `stream()`.

### Exemple

```java
import java.util.List;
import java.util.stream.Stream;

public class CreationStream {
    public static void main(String[] args) {
        List<String> mots = List.of("radio", "mission", "code", "terrain", "alerte");

        // stream() ouvre le flux — aucun traitement n'a lieu ici
        Stream<String> flux = mots.stream();
    }
}
```

Le stream ne fait rien tant qu'une **opération terminale** ne le déclenche pas. Ce mécanisme s'appelle l'**évaluation paresseuse** (lazy evaluation) : les opérations intermédiaires sont empilées, puis exécutées d'un coup.

### À retenir

> - `collection.stream()` ouvre un flux sans modifier la collection.
> - Un stream est **consommé une seule fois** : on ne peut pas le relancer.
> - Les opérations intermédiaires sont **paresseuses** : elles n'agissent que quand une terminale est appelée.

## 2. Opérations intermédiaires

Les opérations intermédiaires transforment ou filtrent le flux. Elles renvoient toutes un nouveau stream, ce qui permet de les **chaîner**.

| Opération | Rôle |
|---|---|
| `filter(predicate)` | Garde les éléments qui satisfont le prédicat |
| `map(function)` | Transforme chaque élément |
| `sorted()` | Trie selon l'ordre naturel |
| `sorted(comparator)` | Trie selon un `Comparator` |
| `distinct()` | Supprime les doublons (`equals`) |
| `limit(n)` | Garde au plus `n` éléments |

### Exemple

```java
import java.util.List;

public class PipelineDemo {
    public static void main(String[] args) {
        List<String> mots = List.of("radio", "mission", "code", "terrain", "alerte", "code");

        List<String> resultat = mots.stream()
            .filter(m -> m.length() > 4)       // garde les mots de plus de 4 lettres
            .map(String::toUpperCase)           // met en majuscules
            .distinct()                         // supprime les doublons éventuels
            .sorted()                           // trie alphabétiquement
            .limit(3)                           // au plus 3 éléments
            .toList();                          // déclenche le pipeline et collecte

        System.out.println(resultat);           // [ALERTE, MISSION, RADIO]
    }
}
```

Notez que `"code"` (4 lettres, pas plus de 4) est écarté dès `filter`. Les opérations se lisent de haut en bas, dans l'ordre du traitement.

### À retenir

> - Les opérations intermédiaires se **chaînent** : chacune reçoit le flux de la précédente.
> - Elles sont **paresseuses** : rien ne s'exécute avant l'opération terminale.
> - `map` change le type des éléments si la `Function` le demande (ex. : `String` → `Integer` avec `String::length`).

## 3. Opérations terminales

Une opération terminale **déclenche** le pipeline et produit un résultat. Après elle, le stream est **consommé** et ne peut plus être utilisé.

### Exemple

```java
import java.util.List;

public class TerminalesDemo {
    public static void main(String[] args) {
        List<String> mots = List.of("alpha", "bravo", "charlie", "delta", "echo");

        // toList : rassemble dans une List
        List<String> longs = mots.stream()
            .filter(m -> m.length() >= 5)
            .toList();
        System.out.println(longs);   // [alpha, bravo, charlie, delta]

        // count : compte les éléments qui passent le filtre
        long nombre = mots.stream()
            .filter(m -> m.length() > 4)
            .count();
        System.out.println(nombre);  // 4

        // forEach : applique une action sur chaque élément (ne renvoie rien)
        mots.stream()
            .filter(m -> m.startsWith("b"))
            .forEach(System.out::println);   // bravo

        // reduce : agrège les éléments en une seule valeur
        int totalLettres = mots.stream()
            .map(String::length)
            .reduce(0, Integer::sum);        // accumulateur : somme des longueurs
        System.out.println(totalLettres);    // 26 (5+5+7+5+4)
    }
}
```

`reduce(identite, accumulateur)` prend une valeur de départ (`0`) et une fonction qui combine l'accumulateur avec chaque élément.

### À retenir

> - `collect`, `count`, `reduce`, `forEach` sont des terminales : elles **déclenchent** le pipeline.
> - Après une terminale, le stream est **fermé** — toute nouvelle opération dessus lève une erreur.
> - `forEach` ne renvoie rien (`void`) : utilisez `collect` quand vous avez besoin du résultat.

## 4. Collecteurs courants

`Collectors` est la classe (du package `java.util.stream`) qui fournit les **collecteurs** prêts à l'emploi pour `collect(...)`.

### 4.1 `toList` et `joining`

```java
import java.util.List;
import java.util.stream.Collectors;

public class CollecteursDemo {
    public static void main(String[] args) {
        List<String> mots = List.of("foxtrot", "golf", "hotel", "india");

        // toList() : collecte dans une List (méthode directe du Stream)
        List<String> resultat = mots.stream()
            .filter(m -> m.length() > 4)
            .toList();
        System.out.println(resultat);   // [foxtrot, hotel, india]

        // joining(delimiter) : concatène les éléments en une String
        String phrase = mots.stream()
            .collect(Collectors.joining(", "));
        System.out.println(phrase);   // foxtrot, golf, hotel, india

        // joining(delimiter, prefix, suffix) : avec préfixe et suffixe
        String liste = mots.stream()
            .collect(Collectors.joining(", ", "[", "]"));
        System.out.println(liste);    // [foxtrot, golf, hotel, india]
    }
}
```

### 4.2 Introduction à `groupingBy` et `partitioningBy`

`groupingBy` regroupe les éléments dans une `Map` selon une **clé** calculée à la volée. `partitioningBy` est un cas particulier qui sépare en exactement **deux** groupes (`true` / `false`).

```java
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupesDemo {
    public static void main(String[] args) {
        List<String> mots = List.of("as", "bras", "code", "delta", "echo", "foxtrot");

        // groupingBy : regroupe par longueur du mot
        Map<Integer, List<String>> parLongueur = mots.stream()
            .collect(Collectors.groupingBy(String::length));
        // Résultat (ordre des clés non garanti) : {2=[as], 4=[bras, code, echo], 5=[delta], 7=[foxtrot]}
        System.out.println(parLongueur);

        // partitioningBy : sépare en longs (>= 5) et courts (< 5)
        Map<Boolean, List<String>> partition = mots.stream()
            .collect(Collectors.partitioningBy(m -> m.length() >= 5));
        // Résultat : {false=[as, bras, code, echo], true=[delta, foxtrot]}
        System.out.println(partition.get(true));   // [delta, foxtrot]
        System.out.println(partition.get(false));  // [as, bras, code, echo]
    }
}
```

`partitioningBy` garantit toujours les deux clés `true` et `false` dans la `Map`, même si l'un des groupes est vide.

### À retenir

> - `stream.toList()` : stream → `List` (méthode directe, à préférer à `collect(Collectors.toList())`).
> - `Collectors.joining(sep)` : stream de `String` → `String` concaténée.
> - `Collectors.groupingBy(f)` : stream → `Map<K, List<V>>` selon la clé `f`.
> - `Collectors.partitioningBy(p)` : stream → `Map<Boolean, List<V>>` en deux groupes.

## 5. Optional : gérer l'absence de valeur

`Optional<T>` est un **conteneur** qui contient soit un objet de type `T`, soit rien. Il remplace le `null` comme signal d'absence de valeur, et force le code appelant à **traiter les deux cas**.

### 5.1 Créer et interroger un Optional

```java
import java.util.Optional;

public class OptionalDemo {
    public static void main(String[] args) {
        // Optional avec une valeur
        Optional<String> present = Optional.of("bonjour");

        // Optional vide (absence de valeur)
        Optional<String> vide = Optional.empty();

        // isPresent() : true si une valeur est là
        System.out.println(present.isPresent());   // true
        System.out.println(vide.isPresent());      // false

        // orElse(defaut) : renvoie la valeur ou la valeur par défaut
        String valeur = vide.orElse("valeur par défaut");
        System.out.println(valeur);   // valeur par défaut

        // map(function) : transforme la valeur si elle est présente
        Optional<Integer> longueur = present.map(String::length);
        System.out.println(longueur.orElse(0));   // 7
    }
}
```

**Règle d'or** : n'appelez jamais `optional.get()` sans avoir vérifié `isPresent()` au préalable. Si l'`Optional` est vide, `get()` lève une erreur — préférez toujours `orElse(...)` ou `map(...)`.

### 5.2 `findFirst`, `max` et `min`

Plusieurs opérations terminales de stream renvoient un `Optional` parce que la collection source peut être vide.

```java
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class OptionalStreamDemo {
    public static void main(String[] args) {
        List<String> mots = List.of("kilo", "lima", "mike", "november");

        // findFirst : premier élément qui passe le filtre
        Optional<String> premier = mots.stream()
            .filter(m -> m.startsWith("m"))
            .findFirst();
        System.out.println(premier.orElse("aucun"));   // mike

        // max : élément maximum selon un Comparator
        Optional<String> plusLong = mots.stream()
            .max(Comparator.comparingInt(String::length));
        System.out.println(plusLong.orElse(""));   // november

        // min : élément minimum
        Optional<String> plusCourt = mots.stream()
            .min(Comparator.comparingInt(String::length));
        System.out.println(plusCourt.orElse(""));  // kilo (ou lima ou mike, ex aequo)
    }
}
```

Si le stream est vide (ou que le filtre écarte tout), `findFirst`, `max` et `min` renvoient `Optional.empty()` — et `orElse` fournit une valeur de repli propre, sans `null`.

### À retenir

> - `Optional` exprime l'**absence possible** d'une valeur ; évitez `null`.
> - `isPresent()` / `orElse(defaut)` / `map(f)` : les trois opérations à connaître.
> - **Ne jamais appeler `get()` sans `isPresent()`** — utilisez `orElse`.
> - `findFirst`, `max`, `min` renvoient un `Optional` : ils gèrent naturellement le stream vide.

## 6. Stream vs boucle

Les streams et les boucles `for` produisent souvent le même résultat. Quand préférer l'un ou l'autre ?

| Situation | Préférer |
|---|---|
| Pipeline linéaire (filtrer → transformer → collecter) | **stream** : une ligne lisible |
| Logique complexe avec plusieurs variables accumulatrices | **boucle** : plus lisible |
| Traitement avec indice (`i`) | **boucle** : `for (int i = 0; …)` |

> ⚠️ Vous croiserez `parallelStream()`, qui répartit le travail sur plusieurs cœurs. **N'y touchez pas pour l'instant** : c'est un outil avancé, piégeux (ordre, état partagé, opérations qui doivent être associatives) et qui ralentit souvent plus qu'il n'accélère sur des collections normales. Un `stream()` séquentiel est le bon choix par défaut.

```java
import java.util.ArrayList;
import java.util.List;

public class StreamVsBoucle {
    public static void main(String[] args) {
        List<String> mots = List.of("oscar", "papa", "quebec", "romeo");

        // Avec une boucle
        List<String> boucle = new ArrayList<>();
        for (String m : mots) {
            if (m.length() > 4) {
                boucle.add(m.toUpperCase());
            }
        }

        // Avec un stream — même résultat, plus concis
        List<String> stream = mots.stream()
            .filter(m -> m.length() > 4)
            .map(String::toUpperCase)
            .toList();

        // Les deux produisent : [OSCAR, QUEBEC, ROMEO]
    }
}
```

Pour les exercices du sous-groupe 4.3, les tests observent **uniquement le résultat** (la valeur retournée), jamais la façon de l'obtenir. Une boucle correcte reste fonctionnelle ; un stream idiomatique est cependant attendu quand la signature du problème prend une interface fonctionnelle en paramètre.

### À retenir

> - Stream et boucle sont **équivalents** en résultat ; le stream gagne en lisibilité pour les pipelines simples.
> - Les tests du module 4 observent **le résultat**, pas la forme du code.
> - Un stream **ne modifie jamais** la collection source.

## Erreurs fréquentes

- **Réutiliser un stream déjà consommé** : après une opération terminale, le stream est fermé. Créez un nouveau stream avec `collection.stream()`.
- **Appeler `optional.get()` sans vérifier** : si l'`Optional` est vide, `get()` produit une erreur d'exécution. Utilisez toujours `orElse(...)` ou vérifiez avec `isPresent()` avant.
- **Croire qu'un stream modifie la source** : `filter`, `map`, etc. produisent un nouveau flux ; la collection d'origine reste inchangée.
- **Oublier l'opération terminale** : un stream sans terminale n'exécute rien. Si rien ne se passe, vérifiez que le pipeline se termine par `collect`, `count`, `forEach`, etc.
- **Chaîner après `forEach`** : `forEach` est une terminale qui renvoie `void` — on ne peut rien chaîner après. Remplacez par `map` + `collect` si vous avez besoin du résultat.
- **`Collectors` non importé** : `Collectors` est dans `java.util.stream.Collectors` ; `Optional` est dans `java.util.Optional`. Importez-les explicitement.

## Exercice guidé

**Objectif** : construire deux pipelines sur une liste de mots.

Vous disposez de la liste suivante :

```java
import java.util.List;

List<String> mots = List.of(
    "kiwi", "melon", "banane", "cerise", "prune",
    "abricot", "fraise", "datte", "poire", "fig"
);
```

**Partie A** : filtrez les mots dont la longueur est strictement supérieure à 4 lettres, mettez-les en majuscules, triez-les alphabétiquement et collectez le résultat dans une `List<String>`.

**Partie B** : trouvez le premier mot de la liste dont la longueur est exactement 5 lettres. Affichez-le, ou affichez `"Aucun"` si aucun mot de cette longueur n'existe.

Indications :
- Partie A : chaînez `filter` → `map` → `sorted` → `toList()`.
- Partie B : utilisez `filter` → `findFirst()` qui renvoie un `Optional<String>`, puis `orElse("Aucun")`.

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

```java
import java.util.List;
import java.util.Optional;

public class ExerciceMots {

    public static void main(String[] args) {
        List<String> mots = List.of(
            "kiwi", "melon", "banane", "cerise", "prune",
            "abricot", "fraise", "datte", "poire", "fig"
        );

        // Partie A : filtrer (> 4 lettres), majuscules, trier, collecter
        List<String> resultat = mots.stream()
            .filter(m -> m.length() > 4)       // melon, banane, cerise, prune, abricot, fraise, datte, poire
            .map(String::toUpperCase)           // mise en majuscules
            .sorted()                           // ordre alphabétique
            .toList();                          // déclenche et collecte

        System.out.println(resultat);
        // [ABRICOT, BANANE, CERISE, DATTE, FRAISE, MELON, POIRE, PRUNE]

        // Partie B : premier mot de longueur exactement 5
        Optional<String> premierDeCinq = mots.stream()
            .filter(m -> m.length() == 5)      // kiwi (4 — non), puis melon (5 — premier !)
            .findFirst();                       // renvoie un Optional<String>

        String affichage = premierDeCinq.orElse("Aucun");
        System.out.println(affichage);   // melon
    }
}
```

</details>

## Vérifiez vos acquis

- Quelle est la différence entre une opération **intermédiaire** et une opération **terminale** dans un pipeline de stream ?
- Pourquoi `findFirst()` et `max()` renvoient-ils un `Optional` plutôt qu'une valeur directe ?
- Que se passe-t-il si vous appelez une deuxième opération terminale sur le même stream ?
- Dans quel cas préférez-vous `Collectors.partitioningBy` à `Collectors.groupingBy` ?

## Pour aller plus loin

- [The Stream API](https://dev.java/learn/api/streams/) (dev.java) — tutoriel officiel Oracle sur les streams, opérations intermédiaires et terminales, collecteurs.
- [Guide to Java 8 Optional](https://www.baeldung.com/java-optional) (Baeldung) — utilisation correcte d'`Optional`, pièges courants, patterns idiomatiques.
- [Interface Stream\<T\>](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/stream/Stream.html) (Javadoc OpenJDK 25) — référence complète de toutes les opérations disponibles.

## Prochain chapitre

→ **[Module 5 · Chapitre 5-1 — Hiérarchie des exceptions](../module-5-exceptions-io/5-1-hierarchie-exceptions)**
