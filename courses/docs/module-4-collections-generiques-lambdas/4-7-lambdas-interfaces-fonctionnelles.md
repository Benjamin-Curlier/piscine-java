---
id: 4-7-lambdas-interfaces-fonctionnelles
sidebar_position: 7
title: "Lambdas et interfaces fonctionnelles"
description: "Écrire et utiliser des lambdas Java : syntaxe, interfaces fonctionnelles standard (Predicate, Function, Consumer, Supplier) et références de méthode."
---

# Lambdas et interfaces fonctionnelles

## Pourquoi ce chapitre

Au chapitre 4-4, vous avez vu des expressions de la forme `element -> action` sans explication formelle — c'était un premier contact concret. Ce chapitre pose enfin la théorie : qu'est-ce qu'une lambda, pourquoi Java l'accepte, et comment s'en servir de façon maîtrisée.

Comprendre les lambdas est indispensable pour le chapitre 4-8 (Streams), où presque chaque opération en reçoit une. Ce chapitre est donc le socle qui débloque la programmation fonctionnelle en Java.

## Ce que vous saurez faire à la fin

- **Définir** une interface fonctionnelle et expliquer son rôle.
- **Écrire** une lambda dans ses différentes formes syntaxiques.
- **Utiliser** les quatre interfaces fonctionnelles standard : `Predicate<T>`, `Function<T,R>`, `Consumer<T>`, `Supplier<T>`.
- **Reconnaître** et **écrire** une référence de méthode (`Classe::methode`).
- **Identifier** la règle de capture des variables (effectively final).

## 1. L'interface fonctionnelle

### Définition

Une **interface fonctionnelle** (functional interface) est une interface qui contient **exactement une méthode abstraite**. C'est cette contrainte d'une seule méthode qui permet à Java d'accepter une lambda à la place d'une instance de cette interface.

L'annotation `@FunctionalInterface` n'est pas obligatoire, mais elle est recommandée : elle demande au compilateur de vérifier que l'interface n'a bien qu'une seule méthode abstraite.

### Exemple

```java
// Voici une interface fonctionnelle maison (pour comprendre le principe) :
@FunctionalInterface
interface Testeur {
    boolean tester(int valeur); // une seule méthode abstraite
}

public class InterfaceFonctionnelleDemo {
    public static void main(String[] args) {
        // Sans lambda, on devrait créer une classe entière :
        Testeur estPositif = new Testeur() {
            @Override
            public boolean tester(int valeur) {
                return valeur > 0;
            }
        };

        // Avec une lambda, c'est beaucoup plus court :
        Testeur estPositifLambda = valeur -> valeur > 0;

        System.out.println(estPositif.tester(5));        // true
        System.out.println(estPositifLambda.tester(5));  // true
        System.out.println(estPositifLambda.tester(-3)); // false
    }
}
```

Java reconnaît que `valeur -> valeur > 0` a la bonne forme (un paramètre entier, retourne un booléen) et l'accepte partout où un `Testeur` est attendu.

### À retenir

> - Une interface fonctionnelle possède **exactement une méthode abstraite**.
> - `@FunctionalInterface` est un garde-fou de compilation, pas une obligation.
> - Une lambda est une implémentation compacte de cette unique méthode.

## 2. Syntaxe des lambdas

### Les formes possibles

Une lambda s'écrit : `(paramètres) -> corps`. Le corps peut être une expression ou un bloc.

```java
import java.util.function.BinaryOperator;
import java.util.function.IntUnaryOperator;
import java.util.function.Supplier;

public class FormesLambda {
    public static void main(String[] args) {
        // Forme 1 : un paramètre, expression directe (parenthèses et return facultatifs)
        IntUnaryOperator doubler = valeur -> valeur * 2;

        // Forme 2 : plusieurs paramètres (parenthèses obligatoires)
        BinaryOperator<Integer> addition = (a, b) -> a + b;

        // Forme 3 : aucun paramètre (parenthèses vides obligatoires)
        Supplier<String> salutation = () -> "Bonjour";

        // Forme 4 : corps en bloc (accolades + return explicite)
        IntUnaryOperator doublerBloc = n -> {
            int resultat = n * 2;
            return resultat;
        };

        System.out.println(doubler.applyAsInt(5));   // 10
        System.out.println(addition.apply(3, 4));     // 7
        System.out.println(salutation.get());         // Bonjour
        System.out.println(doublerBloc.applyAsInt(6)); // 12
    }
}
```

### Exemple

```java
import java.util.ArrayList;
import java.util.List;

public class LambdaForEachDemo {
    public static void main(String[] args) {
        List<String> prenoms = new ArrayList<>();
        prenoms.add("Alice");
        prenoms.add("Bob");
        prenoms.add("Clara");

        // Lambda avec un paramètre — afficher chaque prénom en majuscules
        prenoms.forEach(p -> System.out.println(p.toUpperCase()));
        // ALICE
        // BOB
        // CLARA

        // Lambda avec corps en bloc — afficher uniquement les prénoms longs
        prenoms.forEach(p -> {
            if (p.length() > 3) {
                System.out.println(p + " (long)");
            }
        });
        // Alice (long)
        // Clara (long)
    }
}
```

### À retenir

> - Un paramètre seul : pas de parenthèses nécessaires.
> - Plusieurs paramètres ou aucun : parenthèses obligatoires.
> - Corps d'une expression simple : pas d'accolades ni de `return`.
> - Corps complexe (plusieurs instructions) : accolades + `return` explicite.

## 3. Les quatre interfaces fonctionnelles standard

Le JDK (Java Development Kit) fournit dans le package `java.util.function` des interfaces fonctionnelles prêtes à l'emploi pour les cas les plus courants. En voici les quatre essentielles :

### `Predicate<T>` — tester une condition

`Predicate<T>` représente un test booléen sur un objet de type `T`. Sa méthode abstraite est `boolean test(T t)`.

```java
import java.util.function.Predicate;

public class PredicateDemo {
    public static void main(String[] args) {
        // Un prédicat qui teste si une chaîne est courte (< 5 caractères)
        Predicate<String> estCourt = s -> s.length() < 5;

        System.out.println(estCourt.test("Ax"));        // true
        System.out.println(estCourt.test("Alexandre")); // false

        // Un prédicat qui teste si une chaîne commence par une majuscule
        Predicate<String> commenceParMajuscule = s -> Character.isUpperCase(s.charAt(0));

        System.out.println(commenceParMajuscule.test("Bonjour")); // true
        System.out.println(commenceParMajuscule.test("java"));    // false
    }
}
```

### `Function<T,R>` — transformer une valeur

`Function<T,R>` transforme un objet de type `T` en un objet de type `R`. Sa méthode abstraite est `R apply(T t)`.

```java
import java.util.function.Function;

public class FunctionDemo {
    public static void main(String[] args) {
        // Transformer une chaîne en sa longueur (String → Integer)
        Function<String, Integer> longueur = s -> s.length();
        System.out.println(longueur.apply("Bonjour")); // 7

        // Transformer une chaîne en majuscules (String → String)
        Function<String, String> enMajuscules = s -> s.toUpperCase();
        System.out.println(enMajuscules.apply("module")); // MODULE
    }
}
```

### `Consumer<T>` — consommer sans retour

`Consumer<T>` effectue une action sur un objet de type `T` et ne renvoie rien. Sa méthode abstraite est `void accept(T t)`. C'est ce que `forEach` utilise en interne.

```java
import java.util.function.Consumer;

public class ConsumerDemo {
    public static void main(String[] args) {
        // Afficher une chaîne avec un préfixe
        Consumer<String> afficher = s -> System.out.println(">> " + s);

        afficher.accept("Première ligne"); // >> Première ligne
        afficher.accept("Deuxième ligne"); // >> Deuxième ligne
    }
}
```

### `Supplier<T>` — produire sans entrée

`Supplier<T>` produit une valeur de type `T` sans recevoir de paramètre. Sa méthode abstraite est `T get()`.

```java
import java.util.function.Supplier;

public class SupplierDemo {
    public static void main(String[] args) {
        // Produire un message par défaut (utile pour les cas "aucun résultat")
        Supplier<String> messageParDefaut = () -> "Aucun résultat trouvé.";

        System.out.println(messageParDefaut.get()); // Aucun résultat trouvé.
    }
}
```

### Tableau récapitulatif

| Interface | Méthode | Paramètre | Retour | Usage typique |
|---|---|---|---|---|
| `Predicate<T>` | `test(T)` | `T` | `boolean` | Filtrer, tester |
| `Function<T,R>` | `apply(T)` | `T` | `R` | Transformer |
| `Consumer<T>` | `accept(T)` | `T` | rien | Afficher, enregistrer |
| `Supplier<T>` | `get()` | rien | `T` | Produire, créer |

### À retenir

> - `Predicate` teste → résultat `boolean`.
> - `Function` transforme → résultat d'un autre type.
> - `Consumer` consomme → pas de retour.
> - `Supplier` fournit → pas de paramètre.

## 4. Références de méthode

### Principe

Quand une lambda ne fait qu'appeler une méthode existante, Java propose une écriture encore plus courte : la **référence de méthode** (en anglais *method reference*), avec la syntaxe `Classe::methode` ou `objet::methode`.

```java
import java.util.function.Consumer;
import java.util.function.Function;

public class ReferenceMethodeDemo {
    public static void main(String[] args) {
        // Ces deux lignes sont équivalentes :
        Consumer<String> afficher1 = s -> System.out.println(s);
        Consumer<String> afficher2 = System.out::println;  // référence de méthode

        // Ces deux lignes sont équivalentes :
        Function<String, Integer> longueur1 = s -> s.length();
        Function<String, Integer> longueur2 = String::length;  // référence de méthode
    }
}
```

### Exemple

```java
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ReferenceMethodeExemple {
    public static void main(String[] args) {
        List<String> mots = new ArrayList<>();
        mots.add("bonjour");
        mots.add("monde");
        mots.add("java");

        // Afficher avec une référence de méthode (sur une instance : System.out::println)
        mots.forEach(System.out::println);
        // bonjour
        // monde
        // java

        // Transformer avec une référence de méthode (sur le type : String::toUpperCase)
        Function<String, String> majuscule = String::toUpperCase;
        System.out.println(majuscule.apply("bonjour")); // BONJOUR
    }
}
```

### À retenir

> - La syntaxe `Classe::methode` est une référence de méthode.
> - Utilisez-la quand la lambda ne fait qu'appeler une méthode existante — c'est plus lisible.
> - `System.out::println` est la référence la plus fréquente : elle remplace `s -> System.out.println(s)`.

## 5. Capture de variables (effectively final)

### La règle

Une lambda peut lire des variables du contexte qui l'entoure (variables locales de la méthode enclosante). Mais ces variables doivent être **effectively final** (effectivement finales) : leur valeur ne doit jamais changer après leur initialisation.

```java
import java.util.function.Predicate;

public class CaptureDemo {
    public static void main(String[] args) {
        int seuil = 10; // effectivement finale : elle n'est pas réassignée

        Predicate<Integer> estAuDessus = n -> n > seuil; // OK : seuil est capturée

        System.out.println(estAuDessus.test(15)); // true
        System.out.println(estAuDessus.test(5));  // false

        // Si l'on essayait de réassigner seuil après, le compilateur refuserait :
        // seuil = 20; // décommentez pour voir l'erreur de compilation
    }
}
```

### Pourquoi cette règle ?

Une lambda peut être exécutée après que la méthode qui l'a créée soit terminée. Pour éviter des états imprévisibles, Java exige que les variables capturées ne changent plus. Si votre variable doit évoluer, stockez-la dans un champ d'objet (un attribut) plutôt que dans une variable locale.

### Exemple

```java
import java.util.function.Function;

public class CapturePrefixeDemo {
    public static void main(String[] args) {
        String prefixe = "Bonjour, "; // effectivement finale

        Function<String, String> saluer = nom -> prefixe + nom;

        System.out.println(saluer.apply("Alice")); // Bonjour, Alice
        System.out.println(saluer.apply("Bob"));   // Bonjour, Bob
    }
}
```

### À retenir

> - Une lambda peut capturer une variable locale, à condition qu'elle soit **effectivement finale** (jamais réassignée).
> - Le compilateur signale l'erreur si vous tentez de modifier une variable capturée.
> - Pour une valeur qui doit changer, utilisez un attribut d'objet, pas une variable locale.

## Erreurs fréquentes

- **Capturer une variable réassignée** : écrire `int compteur = 0;` puis `compteur++;` dans la méthode, et utiliser `compteur` dans une lambda lève une erreur de compilation (`Variable used in lambda expression should be effectively final`). Cause : la variable n'est pas effectivement finale. Correction : ne pas réassigner la variable, ou utiliser un tableau d'un élément (`int[] compteur = {0};`) si vous devez vraiment la muter.

- **Écrire une boucle imbriquée dans une lambda** : mettre une boucle `for` complexe à l'intérieur d'une lambda rend le code illisible. Cause : mauvais découpage. Correction : extraire la logique dans une méthode nommée, puis l'utiliser comme référence de méthode.

- **Confondre `Predicate` et `Function`** : utiliser un `Predicate<String>` là où un `Function<String, String>` est attendu. Cause : les deux prennent un paramètre, mais `Predicate` renvoie `boolean` et `Function` renvoie un autre type. Correction : vérifier le type de retour de la méthode attendue.

- **Oublier les parenthèses pour zéro ou plusieurs paramètres** : écrire `-> "valeur"` au lieu de `() -> "valeur"` pour un `Supplier`, ou `a, b -> a + b` au lieu de `(a, b) -> a + b`. Cause : confusion de syntaxe. Correction : les parenthèses sont obligatoires dès que les paramètres ne sont pas exactement un.

## Exercice guidé

**Contexte** : vous travaillez sur une liste de nombres entiers et une liste de chaînes de caractères. Vous devez filtrer les nombres selon différents critères (`Predicate`), puis transformer les chaînes (`Function`), en passant les lambdas comme paramètres de méthodes utilitaires.

**Pas à pas :**

1. Créez une méthode statique `filtrer(List<Integer> liste, Predicate<Integer> critere)` qui renvoie une nouvelle `List<Integer>` ne contenant que les éléments pour lesquels `critere.test(element)` est `true`.

2. Créez une méthode statique `transformer(List<String> liste, Function<String, String> transformation)` qui applique la transformation à chaque élément et renvoie une nouvelle `List<String>`.

3. Dans `main` :
   - Créez une liste de nombres : `[3, 7, -2, 15, 0, -8, 42]`.
   - Appelez `filtrer` avec un `Predicate` qui garde uniquement les nombres positifs stricts (`> 0`).
   - Appelez `filtrer` à nouveau avec un `Predicate` qui garde uniquement les nombres supérieurs à 10.
   - Créez une liste de mots : `["bonjour", "monde", "java", "lambda"]`.
   - Appelez `transformer` avec une `Function` qui met chaque mot en majuscules.
   - Affichez chaque résultat.

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

```java
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class ExerciceLambda {

    // Étape 1 : méthode de filtrage générique
    static List<Integer> filtrer(List<Integer> liste, Predicate<Integer> critere) {
        List<Integer> resultat = new ArrayList<>();
        for (int valeur : liste) {
            if (critere.test(valeur)) { // on délègue le test au Predicate
                resultat.add(valeur);
            }
        }
        return resultat;
    }

    // Étape 2 : méthode de transformation générique
    static List<String> transformer(List<String> liste, Function<String, String> transformation) {
        List<String> resultat = new ArrayList<>();
        for (String element : liste) {
            resultat.add(transformation.apply(element)); // on délègue la transformation
        }
        return resultat;
    }

    public static void main(String[] args) {

        // Étape 3a : liste de nombres
        List<Integer> nombres = new ArrayList<>();
        nombres.add(3);
        nombres.add(7);
        nombres.add(-2);
        nombres.add(15);
        nombres.add(0);
        nombres.add(-8);
        nombres.add(42);

        // Filtrer avec un Predicate : nombres positifs stricts
        List<Integer> positifs = filtrer(nombres, n -> n > 0);
        System.out.println("Positifs : " + positifs); // [3, 7, 15, 42]

        // Filtrer avec un autre Predicate : nombres > 10
        List<Integer> grands = filtrer(nombres, n -> n > 10);
        System.out.println("Supérieurs à 10 : " + grands); // [15, 42]

        // Étape 3b : liste de mots
        List<String> mots = new ArrayList<>();
        mots.add("bonjour");
        mots.add("monde");
        mots.add("java");
        mots.add("lambda");

        // Transformer avec une Function : mise en majuscules
        List<String> enMajuscules = transformer(mots, String::toUpperCase); // référence de méthode
        System.out.println("Majuscules : " + enMajuscules); // [BONJOUR, MONDE, JAVA, LAMBDA]

        // Transformer avec une autre Function : longueur sous forme de chaîne
        Function<String, String> descriptionLongueur = m -> m + " (" + m.length() + " lettres)";
        List<String> descriptions = transformer(mots, descriptionLongueur);
        descriptions.forEach(System.out::println);
        // bonjour (7 lettres)
        // monde (5 lettres)
        // java (4 lettres)
        // lambda (6 lettres)
    }
}
```

**Points clés** :
- `filtrer` et `transformer` prennent une interface fonctionnelle en paramètre : l'appelant choisit le comportement via une lambda.
- `String::toUpperCase` est une référence de méthode qui remplace `m -> m.toUpperCase()`.
- `System.out::println` est utilisé dans `forEach` en référence de méthode.

</details>

## Vérifiez vos acquis

- Qu'est-ce qui différencie une interface fonctionnelle d'une interface ordinaire ?
- Quelle interface fonctionnelle standard choisir pour tester si un nombre est pair ? Pour convertir un entier en chaîne ? Pour afficher une valeur ? Pour produire un identifiant unique ?
- Que signifie « effectivement finale » pour une variable capturée par une lambda ?
- Dans quelle situation préférez-vous écrire `String::toUpperCase` plutôt que `s -> s.toUpperCase()` ?

## Pour aller plus loin

- [Lambda Expressions](https://dev.java/learn/lambdas/) (dev.java) — tutoriel officiel Oracle sur les lambdas et les interfaces fonctionnelles.
- [Java 8 Functional Interfaces](https://www.baeldung.com/java-8-functional-interfaces) (Baeldung) — tour complet de `Predicate`, `Function`, `Consumer`, `Supplier` et leurs variantes.
- [Package java.util.function](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/function/package-summary.html) (Javadoc OpenJDK 25) — liste complète des interfaces fonctionnelles du JDK.

## Prochain chapitre

→ **[Chapitre 4-8 — Streams et Optional](4-8-streams-optional)**
