---
id: 4-5-generiques
sidebar_position: 5
title: "Génériques"
description: "Écrire des classes et des méthodes paramétrées par un type pour garantir la sûreté de type à la compilation et supprimer les cast."
---

# Génériques

## Pourquoi ce chapitre

Depuis le chapitre 4-1, vous utilisez `List<String>` ou `List<Integer>` sans vous arrêter sur le `<String>` entre chevrons. Ce symbole est un **type paramétré** (générique) : il indique que la liste travaille avec un type précis, et le compilateur vérifie que vous ne glissez pas le mauvais. Ce mécanisme s'applique à vos propres classes et méthodes, pas seulement aux collections du JDK.

## Ce que vous saurez faire à la fin

- **Lire** la notation `<T>` et comprendre ce qu'elle signifie.
- **Écrire** une classe générique avec un paramètre de type.
- **Écrire** une méthode générique indépendante de la classe.
- **Borner** un type générique avec `extends` pour accéder à ses méthodes.
- **Lire** les wildcards `? extends` et `? super` dans des signatures du JDK.
- **Expliquer** en une phrase pourquoi `new T[]` est impossible.

## 1. Le problème : `Object` + cast

Avant les génériques (Java 1 et 2), on stockait tout dans des collections d'`Object`. Pour récupérer un élément, il fallait le **transtyper** (cast) manuellement. Si l'on se trompait de type, l'erreur n'apparaissait qu'à l'**exécution**.

### Exemple

```java
import java.util.ArrayList;
import java.util.List;

// Ancienne approche (Java 1 / Java 2) — à NE PAS reproduire.
List listeSansType = new ArrayList();    // Collection brute, sans <T>
listeSansType.add("bonjour");
listeSansType.add(42);                   // Le compilateur ne proteste pas…

// Le cast s'impose à la lecture.
String mot = (String) listeSansType.get(0);  // OK ici
String raté = (String) listeSansType.get(1); // Erreur à l'EXÉCUTION : ClassCastException
```

Avec un type paramétré, l'erreur est **signalée à la compilation** — le compilateur refuse d'insérer un entier dans une `List<String>` :

```java
import java.util.ArrayList;
import java.util.List;

List<String> mots = new ArrayList<>();  // <String> = paramètre de type
mots.add("bonjour");
// mots.add(42);   // Erreur de compilation — impossible d'ajouter un int à une List<String>

String mot = mots.get(0);   // Aucun cast : le compilateur sait déjà que c'est un String
```

### À retenir

> - Sans générique : cast obligatoire, erreurs à l'exécution.
> - Avec générique : le compilateur vérifie les types, plus de cast.

## 2. Classe générique

Une **classe générique** déclare un **paramètre de type** entre chevrons après son nom : `class MaClasse<T>`. À l'intérieur, `T` sert de type comme un autre — pour les champs, les paramètres de méthode, les types de retour.

La lettre `T` est une convention (Type) ; `E` est souvent utilisé pour un élément de collection, `K`/`V` pour clé/valeur, `R` pour un résultat. N'importe quelle lettre majuscule fonctionne.

### Exemple

```java
// Une boîte qui contient exactement un élément de type T.
public class Boite<T> {

    private T contenu;   // Le champ a le type T

    public Boite(T contenu) {
        this.contenu = contenu;
    }

    // Méthode de lecture : renvoie T
    public T ouvrir() {
        return contenu;
    }

    // Méthode d'écriture : accepte T
    public void ranger(T nouvelElement) {
        this.contenu = nouvelElement;
    }

    @Override
    public String toString() {
        return "Boite[" + contenu + "]";
    }
}
```

À l'utilisation, on instancie en précisant le type réel entre chevrons :

```java
Boite<String> boiteTexte = new Boite<>("message secret");
String texte = boiteTexte.ouvrir();   // Aucun cast : le compilateur sait que c'est un String
System.out.println(texte);            // message secret

Boite<Integer> boiteNombre = new Boite<>(42);
int valeur = boiteNombre.ouvrir();    // Auto-unboxing inclus
System.out.println(valeur);           // 42
```

Le compilateur produit du bytecode Java identique pour les deux boîtes — c'est l'**effacement de type** (§5), mais votre code source reste sûr et lisible.

### À retenir

> - `class MaClasse<T>` déclare un paramètre de type `T`.
> - `T` s'utilise comme un type ordinaire dans la classe.
> - On instancie avec `new MaClasse<TypeConcret>()`.

## 3. Méthode générique

Une méthode peut avoir **son propre** paramètre de type, indépendant de la classe. On le déclare **avant** le type de retour : `<T> TypeDeRetour nomMethode(...)`.

C'est utile pour des méthodes utilitaires statiques qui opèrent sur un type quelconque.

### Exemple

```java
import java.util.List;

public class Utilitaires {

    // <T> devant le type de retour : T est propre à cette méthode.
    // Renvoie le premier élément d'une liste, ou null si la liste est vide.
    public static <T> T premier(List<T> liste) {
        if (liste.isEmpty()) {
            return null;   // Valeur sentinelle — les exceptions arrivent au module 5
        }
        return liste.get(0);
    }
}
```

Appel sans préciser `T` (le compilateur l'infère) :

```java
import java.util.List;

List<String> noms = List.of("Alice", "Bob");
String premierNom = Utilitaires.premier(noms);   // T inféré : String — aucun cast
System.out.println(premierNom);                  // Alice

List<Integer> scores = List.of(100, 75, 88);
int premierScore = Utilitaires.premier(scores);  // T inféré : Integer — aucun cast
System.out.println(premierScore);                // 100
```

### À retenir

> - `<T> TypeDeRetour methode(...)` : T est propre à la méthode.
> - Le compilateur **infère** T à l'appel — inutile de l'écrire explicitement.
> - `<T>` de la méthode et `<T>` de la classe sont **indépendants**.

## 4. Bornes : `<T extends ...>`

Par défaut, `T` peut être n'importe quelle classe — mais on ne peut alors appeler sur `T` que les méthodes héritées d'`Object` (`toString`, `equals`…). Pour accéder à des méthodes plus précises, on **borne** `T`.

La borne `<T extends Interface>` (ou `<T extends ClasseAbstraite>`) garantit que `T` respecte ce contrat : on peut alors appeler les méthodes de l'interface sur un objet de type `T`.

### Exemple

```java
import java.util.List;

public class Extremes {

    // T doit implémenter Comparable<T> : on peut appeler compareTo.
    // Renvoie le maximum d'une liste non vide, ou null si la liste est vide.
    public static <T extends Comparable<T>> T maximum(List<T> liste) {
        if (liste.isEmpty()) {
            return null;    // Valeur sentinelle
        }
        T max = liste.get(0);
        for (int i = 1; i < liste.size(); i++) {
            if (liste.get(i).compareTo(max) > 0) {   // compareTo disponible grâce à la borne
                max = liste.get(i);
            }
        }
        return max;
    }
}
```

```java
import java.util.ArrayList;
import java.util.List;

List<Integer> notes = new ArrayList<>();
notes.add(12);
notes.add(18);
notes.add(7);

Integer meilleureNote = Extremes.maximum(notes);
System.out.println(meilleureNote);   // 18
```

Retenez pour l'instant que la borne est la porte d'entrée vers les méthodes d'une interface ; vous verrez l'utilisation concrète de `Comparable` dans le prochain chapitre.

### À retenir

> - `<T extends X>` : T doit être une sous-classe de X (ou implémenter X si X est une interface).
> - La borne donne accès aux méthodes de X sur les objets de type T.
> - On peut enchaîner plusieurs bornes : `<T extends A & B>` (une classe puis des interfaces).

## 5. Wildcards : `? extends` et `? super`

Les wildcards (caractère joker `?`) apparaissent dans les **types déclarés**, pas dans les définitions de classes ou méthodes génériques. On les rencontre fréquemment dans les signatures du JDK.

**Règle mémo-technique PECS** (Producer Extends, Consumer Super) — formulée en douceur :

- `List<? extends T>` : la liste **produit** (fournit) des éléments qu'on lit comme des `T`. On ne peut pas y ajouter (le compilateur ne sait pas le type exact).
- `List<? super T>` : la liste **consomme** (reçoit) des éléments de type `T`. On peut y ajouter des `T`, mais on lit seulement des `Object`.

### Exemple

```java
import java.util.ArrayList;
import java.util.List;

// Additionne tous les entiers d'une liste, y compris List<Integer> ou List<Number>.
// ? extends Number = on lit des Number depuis la liste.
public static double somme(List<? extends Number> nombres) {
    double total = 0;
    for (Number n : nombres) {
        total += n.doubleValue();   // doubleValue() disponible sur Number
    }
    return total;
}
```

```java
import java.util.List;

System.out.println(somme(List.of(3, 7)));       // 10.0 — List<Integer> acceptée
System.out.println(somme(List.of(1.5, 2.5)));   // 4.0  — List<Double> acceptée
```

Voyez `? extends T` dans une signature JDK → la méthode lit depuis la collection sans y écrire. Voyez `? super T` → la méthode y écrit des `T`.

### À retenir

> - `?` est un wildcard : un type inconnu.
> - `? extends T` : on lit des `T` (Producer Extends).
> - `? super T` : on écrit des `T` (Consumer Super).
> - Ces notations servent surtout à lire les API du JDK ; évitez-les dans vos propres signatures tant que c'est possible.

## 6. Effacement de type

Java implémente les génériques par **effacement de type** (type erasure) : à la compilation, les paramètres de type sont remplacés par leur borne (ou `Object` si pas de borne), et aucune information de type générique n'est conservée dans le bytecode.

Conséquences pratiques :

- On **ne peut pas** écrire `new T()` ni `new T[]` : le compilateur ne connaît plus `T` à l'exécution.
- On **ne peut pas** écrire `if (objet instanceof T)` : même raison.
- Deux variables `List<String>` et `List<Integer>` partagent la **même classe** à l'exécution (`java.util.ArrayList`).

```java
// INTERDIT — ne compile pas.
// T est effacé : à l'exécution, la JVM ne sait pas quel tableau créer.
// T[] tableau = new T[10];

// Solution habituelle : utiliser une List<T> à la place du tableau.
import java.util.ArrayList;
import java.util.List;

List<T> elements = new ArrayList<>();   // Correct : ArrayList gère l'effacement en interne
```

### À retenir

> - À l'exécution, `T` est effacé : `new T[]` et `instanceof T` sont impossibles.
> - Utilisez `List<T>` plutôt qu'un tableau `T[]` dans vos classes génériques.

## Erreurs fréquentes

- **Utiliser `Object` + cast au lieu d'un type paramétré** : le cast supprime la vérification du compilateur et ramène les erreurs à l'exécution. Préférez `<T>`.
- **Confondre le `<T>` de la classe et le `<T>` d'une méthode** : une méthode `public <T> T foo(...)` dans une classe `Boite<T>` déclare un `T` **différent** de celui de la classe. Nommez-le autrement (`<R>` par exemple) pour éviter la confusion.
- **Écrire `new T[n]`** : interdit à cause de l'effacement de type — utilisez `new ArrayList<T>()` ou passez un tableau pré-créé en paramètre.
- **Sur-utiliser les wildcards** : `? extends` et `? super` sont utiles dans les bibliothèques génériques ; dans votre propre code, un simple `<T>` est souvent suffisant et plus lisible.
- **Oublier la borne et ne pas pouvoir appeler les méthodes voulues** : si le compilateur signale que `T` ne possède pas telle méthode, c'est que `T` n'est pas borné sur l'interface correspondante.

## Exercice guidé

**Objectif** : écrire une classe générique `Boite<T>` et une méthode générique `premier`.

**Étape 1** — Écrivez la classe `Boite<T>` :
- Un constructeur `Boite(T contenu)`.
- Une méthode `T ouvrir()` qui renvoie le contenu.
- Une méthode `void ranger(T nouvelElement)` qui remplace le contenu.

**Étape 2** — Dans une classe `Utilitaires`, écrivez la méthode statique générique :
```java
public static <T> T premier(List<T> liste)
```
Elle renvoie le premier élément, ou `null` si la liste est vide.

**Étape 3** — Dans un `main`, vérifiez que :
- `new Boite<String>("mission")` et `new Boite<Integer>(7)` compilent sans cast.
- `premier(List.of("alpha", "beta"))` renvoie `"alpha"` sans cast.
- `premier(List.of())` renvoie `null`.

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

```java
// Boite.java
public class Boite<T> {

    private T contenu;

    public Boite(T contenu) {
        this.contenu = contenu;
    }

    public T ouvrir() {
        return contenu;
    }

    public void ranger(T nouvelElement) {
        this.contenu = nouvelElement;
    }

    @Override
    public String toString() {
        return "Boite[" + contenu + "]";
    }
}
```

```java
// Utilitaires.java
import java.util.List;

public class Utilitaires {

    // Renvoie le premier élément, ou null si la liste est vide.
    public static <T> T premier(List<T> liste) {
        if (liste.isEmpty()) {
            return null;
        }
        return liste.get(0);
    }
}
```

```java
// Demo.java
import java.util.List;

public class Demo {

    public static void main(String[] args) {
        // Classe générique — deux instanciations distinctes, aucun cast
        Boite<String> boiteTexte = new Boite<>("mission");
        String contenuTexte = boiteTexte.ouvrir();   // String — pas de cast
        System.out.println(contenuTexte);             // mission

        Boite<Integer> boiteNombre = new Boite<>(7);
        int contenuNombre = boiteNombre.ouvrir();     // int — pas de cast
        System.out.println(contenuNombre);            // 7

        // Méthode générique — T inféré par le compilateur
        String premierMot = Utilitaires.premier(List.of("alpha", "beta"));
        System.out.println(premierMot);               // alpha

        String absent = Utilitaires.premier(List.of());
        System.out.println(absent);                   // null
    }
}
```

</details>

## Vérifiez vos acquis

- Quelle différence entre un `<T>` déclaré sur la classe et un `<T>` déclaré sur une méthode ?
- Pourquoi ne peut-on pas écrire `new T[10]` dans une classe générique ?
- Quand utilise-t-on `<T extends X>` plutôt que juste `<T>` ?
- Dans la signature `void copier(List<? extends T> source, List<? super T> cible)`, quel wildcard reçoit des éléments et lequel en fournit ?

## Pour aller plus loin

- [Generics](https://dev.java/learn/generics/) (dev.java) — cours officiel complet sur les génériques Java.
- [Java Generics — Wildcards](https://www.baeldung.com/java-generics) (Baeldung) — exemples pratiques des wildcards et de PECS.
- [Javadoc `java.util.List<E>`](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/List.html) (OpenJDK 25) — voir les signatures génériques de la collection que vous utilisez depuis le chapitre 4-1.

## Prochain chapitre

→ **[Chapitre 4.6 — Comparable et Comparator](4-6-comparable-comparator)**
