---
id: 4-6-comparable-comparator
sidebar_position: 6
title: "Comparable et Comparator"
description: "Trier des objets en Java : implementer l'ordre naturel avec Comparable<T> et definir un tri externe avec Comparator<T>."
---

# Comparable et Comparator

## Pourquoi ce chapitre

Trier une liste de nombres ou de chaînes fonctionne immédiatement en Java : ces types savent déjà se comparer. Mais dès que vous créez vos propres classes, Java ne sait pas dans quel ordre les ranger — vous devez le lui expliquer.

Ce chapitre vous donne deux outils complémentaires : `Comparable<T>` pour décrire l'ordre **naturel** d'une classe, et `Comparator<T>` pour définir des tris **alternatifs** sans toucher à la classe d'origine.

## Ce que vous saurez faire à la fin

- **Implémenter** `Comparable<T>` pour donner un ordre naturel à une classe.
- **Trier** une liste avec `List.sort(null)` et `Collections.sort`.
- **Créer** un `Comparator<T>` externe pour un tri différent de l'ordre naturel.
- **Chaîner** des critères de tri avec `Comparator.comparing(...).thenComparing(...)`.
- **Inverser** un tri ou revenir à l'ordre naturel avec `reversed()` et `naturalOrder()`.
- **Respecter** le contrat de `compareTo` (négatif, zéro, positif ; cohérence avec `equals`).

## 1. Le pont avec le module 3 : de l'interface maison à `Comparable<T>`

En 3.4.3, vous avez fabriqué votre propre interface `Ordonnable` pour comparer des dossiers :

```java
// Ce que vous avez écrit en 3.4.3 — interface MAISON, sans générique
public interface Ordonnable {
    int comparerA(Ordonnable autre); // le paramètre est de type Ordonnable
}

public class Dossier implements Ordonnable {
    private int priorite;

    @Override
    public int comparerA(Ordonnable autre) {
        Dossier autreDossier = (Dossier) autre; // downcast OBLIGATOIRE et risqué
        return this.priorite - autreDossier.priorite;
    }
}
```

Le problème : le paramètre de type `Ordonnable` oblige à un **downcast manuel** (`(Dossier) autre`). Si on passe accidentellement un autre type, le programme plante.

Le JDK fournit `java.lang.Comparable<T>`, qui résout exactement ce problème grâce aux génériques (chapitre 4-5) :

```java
// L'interface du JDK — version GENERIQUE
public interface Comparable<T> {
    int compareTo(T autre); // T remplace Object : plus de downcast
}
```

Voyez les deux signatures côte à côte :

```text
// Interface MAISON (3.4.3) :
int comparerA(Ordonnable autre)   --> downcast (Dossier) autre requis

// Interface JDK (4-6) :
int compareTo(T autre)            --> T est déjà le bon type, zéro downcast
```

Le paramètre de type `T` garantit au compilateur que vous comparez bien un `Livre` à un `Livre`, un `Membre` à un `Membre`. Le downcast disparaît — c'est exactement la valeur ajoutée des génériques.

### À retenir

> - `Comparable<T>` est l'interface **du JDK** ; `compareTo(T autre)` remplace l'ancien `comparerA(Ordonnable autre)`.
> - Le paramètre générique `T` **supprime le downcast** : le compilateur vérifie les types à votre place.

## 2. Implémenter l'ordre naturel avec `Comparable<T>`

L'ordre naturel d'une classe, c'est son ordre « par défaut » — celui qui s'applique lorsque l'on trie sans préciser d'autre règle. Pour une classe `Livre`, l'ordre naturel pourrait être l'année de parution.

`compareTo` renvoie :
- un **entier négatif** si `this` vient **avant** l'autre,
- **zéro** s'ils sont égaux selon ce critère,
- un **entier positif** si `this` vient **après** l'autre.

### Exemple

```java
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Livre implements Comparable<Livre> {

    private final String titre;
    private final int anneeParution;

    public Livre(String titre, int anneeParution) {
        this.titre = titre;
        this.anneeParution = anneeParution;
    }

    public String getTitre() { return titre; }
    public int getAnneeParution() { return anneeParution; }

    @Override
    public int compareTo(Livre autre) {
        // Ordre naturel : par année de parution croissante
        return Integer.compare(this.anneeParution, autre.anneeParution);
        // Integer.compare évite les débordements arithmétiques (préférable à soustraction)
    }

    @Override
    public String toString() {
        return titre + " (" + anneeParution + ")";
    }
}
```

```java
List<Livre> bibliotheque = new ArrayList<>();
bibliotheque.add(new Livre("Les Misérables", 1862));
bibliotheque.add(new Livre("Germinal", 1885));
bibliotheque.add(new Livre("Le Rouge et le Noir", 1830));

Collections.sort(bibliotheque);   // utilise compareTo : tri par anneeParution
// ou : bibliotheque.sort(null);  // null = « utilise l'ordre naturel »

for (Livre l : bibliotheque) {
    System.out.println(l);
}
// Le Rouge et le Noir (1830)
// Les Misérables (1862)
// Germinal (1885)
```

`List.sort(null)` et `Collections.sort` sont **équivalents** : `null` signifie « utilise l'ordre naturel défini par `compareTo` ».

### À retenir

> - `Comparable<T>` exprime l'ordre **naturel** de la classe ; `compareTo` est la méthode à implémenter.
> - `Integer.compare(a, b)` est la façon sûre de comparer deux entiers dans `compareTo` (évite les débordements).
> - `List.sort(null)` et `Collections.sort(liste)` trient tous les deux par l'ordre naturel.

## 3. Comparator : un tri externe sans modifier la classe

Parfois, l'ordre naturel ne suffit pas. Vous voulez trier vos livres par titre au lieu de l'année, ou faire les deux à la fois. `Comparator<T>` est une interface **séparée** : elle décrit un critère de tri sans toucher à la classe `Livre`.

### Exemple — tri par titre seul

```java
import java.util.Comparator;

// Comparator créé avec la méthode usine Comparator.comparing
Comparator<Livre> parTitre = Comparator.comparing(Livre::getTitre);
// Livre::getTitre est une "recette" : pour chaque livre, extrais son titre et compare
// (les références de méthode sont théorisées au chapitre 4-7 ; ici, c'est un idiome concret)

bibliotheque.sort(parTitre); // List.sort accepte n'importe quel Comparator

for (Livre l : bibliotheque) {
    System.out.println(l);
}
// Germinal (1885)
// Le Rouge et le Noir (1830)
// Les Misérables (1862)
```

`Comparator.comparing(Livre::getTitre)` construit un comparateur qui trie les livres par leur titre — ordre alphabétique par défaut.

### À retenir

> - `Comparator<T>` est **externe** à la classe : il n'est pas nécessaire de modifier `Livre`.
> - `Comparator.comparing(Classe::getterOuMethode)` construit un comparateur à partir d'un critère.
> - On passe le `Comparator` à `List.sort(...)` comme argument.

## 4. Chaîner les critères : thenComparing et reversed

Pour trier d'abord par année, puis par titre en cas d'égalité d'année, on **chaîne** des comparateurs.

### Exemple — tri par année, puis par titre

```java
Comparator<Livre> parAnneePuisTitre =
    Comparator.comparing(Livre::getAnneeParution)  // critère principal : année
              .thenComparing(Livre::getTitre);      // critère secondaire : titre (si même année)

bibliotheque.add(new Livre("Nana", 1880));
bibliotheque.add(new Livre("L'Assommoir", 1877));
bibliotheque.add(new Livre("Pot-Bouille", 1882));
bibliotheque.add(new Livre("Au Bonheur des Dames", 1883));

bibliotheque.sort(parAnneePuisTitre);

for (Livre l : bibliotheque) {
    System.out.println(l);
}
// Le Rouge et le Noir (1830)
// Les Misérables (1862)
// L'Assommoir (1877)
// Nana (1880)
// Pot-Bouille (1882)
// Au Bonheur des Dames (1883)
// Germinal (1885)
```

Pour inverser un tri, `.reversed()` suffit :

```java
Comparator<Livre> plusRecentEnPremier =
    Comparator.comparing(Livre::getAnneeParution).reversed();
// Germinal (1885) apparaîtra en tête
```

Pour revenir explicitement à l'ordre naturel depuis un `Comparator` chaîné :

```java
Comparator<Livre> ordreNaturelPuisTitre =
    Comparator.<Livre>naturalOrder()  // utilise compareTo de Livre
              .thenComparing(Livre::getTitre);
```

### À retenir

> - `.thenComparing(...)` ajoute un critère **secondaire** utilisé quand le critère principal donne zéro.
> - `.reversed()` **inverse** l'ordre du comparateur sur lequel il est appelé.
> - `.naturalOrder()` crée un comparateur qui délègue à `compareTo`.

## 5. Le contrat de compareTo

Pour que le tri reste cohérent, `compareTo` doit respecter trois règles :

1. **Antisymétrie** : `a.compareTo(b)` et `b.compareTo(a)` ont des signes opposés (ou sont tous deux zéro).
2. **Transitivité** : si `a < b` et `b < c`, alors `a < c`.
3. **Cohérence avec `equals`** (recommandée, non obligatoire) : `a.compareTo(b) == 0` devrait correspondre à `a.equals(b)`.

En pratique, utiliser `Integer.compare`, `String.compareTo` ou `Double.compare` respecte automatiquement ces règles.

### À retenir

> - `compareTo` renvoie un entier : **négatif** (this avant autre), **zéro** (égaux), **positif** (this après autre).
> - Utiliser les méthodes utilitaires du JDK (`Integer.compare`, etc.) respecte le contrat sans effort.
> - Si `compareTo` renvoie zéro alors que `equals` renvoie `false`, des surprises peuvent survenir dans un `TreeSet` ou `TreeMap`.

## Erreurs fréquentes

- **`compareTo` renvoie 0 ou 1 comme un test booléen** — symptôme : l'ordre de tri est fantaisiste. Cause : on a écrit `return this.annee > autre.annee ? 1 : 0` en oubliant le cas « inférieur ». Correction : utiliser `Integer.compare(this.annee, autre.annee)` qui couvre les trois cas.

- **La soustraction au lieu de `Integer.compare`** — symptôme : tri incorrect sur de grands nombres négatifs. Cause : `return this.annee - autre.annee` peut déborder si les valeurs sont très grandes ou très négatives. Correction : remplacer par `Integer.compare(this.annee, autre.annee)`.

- **`List.sort` mute la liste de départ** — symptôme : la liste originale est réordonnée alors qu'on voulait la garder intacte. Cause : `sort` réordonne la liste **en place**. Correction : copier la liste avant de la trier (`new ArrayList<>(original)`).

- **Confondre tri naturel et `Comparator`** — symptôme : on passe `null` à `List.sort` sur une classe qui n'implémente pas `Comparable` — `ClassCastException` au moment du tri. Correction : soit implémenter `Comparable<T>`, soit toujours fournir un `Comparator` explicite.

## Exercice guidé

**Objectif** : trier une liste de livres d'abord par année de parution, puis par titre en cas d'égalité.

**Données** :
- *Notre-Dame de Paris* (1831)
- *Le Père Goriot* (1835)
- *Indiana* (1832)
- *Eugénie Grandet* (1833)
- *La Peau de chagrin* (1831)

**Pas à pas** :

1. Créez la classe `Livre` avec les champs `titre` (String) et `anneeParution` (int), et des accesseurs `getTitre()` et `getAnneeParution()`.
2. Faites implémenter `Comparable<Livre>` à `Livre` : l'ordre naturel est l'année de parution (`Integer.compare`).
3. Vérifiez que `Collections.sort(bibliotheque)` trie bien par année.
4. Construisez un `Comparator<Livre>` qui trie par année **puis** par titre avec `thenComparing`.
5. Appliquez-le avec `bibliotheque.sort(...)` et affichez la liste.

Résultat attendu (ordre : 1831a, 1831b, 1832, 1833, 1835) :
```text
La Peau de chagrin (1831)
Notre-Dame de Paris (1831)
Indiana (1832)
Eugénie Grandet (1833)
Le Père Goriot (1835)
```

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

```java compile
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Livre implements Comparable<Livre> {

    private final String titre;
    private final int anneeParution;

    public Livre(String titre, int anneeParution) {
        this.titre = titre;
        this.anneeParution = anneeParution;
    }

    public String getTitre() { return titre; }
    public int getAnneeParution() { return anneeParution; }

    @Override
    public int compareTo(Livre autre) {
        // Ordre naturel : année de parution croissante
        return Integer.compare(this.anneeParution, autre.anneeParution);
    }

    @Override
    public String toString() {
        return titre + " (" + anneeParution + ")";
    }
}

// Demo n'est PAS public : Java n'autorise qu'une seule classe publique par fichier.
// Compilez ce fichier sous le nom Livre.java, puis lancez : java Demo
class Demo {
    public static void main(String[] args) {
        List<Livre> bibliotheque = new ArrayList<>();
        bibliotheque.add(new Livre("Notre-Dame de Paris", 1831));
        bibliotheque.add(new Livre("Le Père Goriot", 1835));
        bibliotheque.add(new Livre("Indiana", 1832));
        bibliotheque.add(new Livre("Eugénie Grandet", 1833));
        bibliotheque.add(new Livre("La Peau de chagrin", 1831));

        // Étape 3 : tri naturel (par année seulement)
        Collections.sort(bibliotheque);
        System.out.println("-- Tri naturel (année) --");
        for (Livre l : bibliotheque) {
            System.out.println(l);
        }

        // Étape 4-5 : Comparator chaîné (année, puis titre)
        Comparator<Livre> parAnneePuisTitre =
            Comparator.comparing(Livre::getAnneeParution)
                      .thenComparing(Livre::getTitre);

        bibliotheque.sort(parAnneePuisTitre);
        System.out.println("-- Tri : année puis titre --");
        for (Livre l : bibliotheque) {
            System.out.println(l);
        }
        // La Peau de chagrin (1831)
        // Notre-Dame de Paris (1831)
        // Indiana (1832)
        // Eugénie Grandet (1833)
        // Le Père Goriot (1835)
    }
}
```

</details>

## Vérifiez vos acquis

- Quelle différence y a-t-il entre l'interface maison `Ordonnable` (module 3) et `Comparable<T>` du JDK, et pourquoi les génériques suppriment-ils le downcast ?
- Que renvoie `compareTo` pour signifier « cet objet vient avant l'autre » ?
- Quand préférez-vous `Comparator` à `Comparable` pour trier une liste ?
- Comment trier une liste par un critère principal et un critère secondaire en cas d'égalité ?

## Pour aller plus loin

- [Comparable (Javadoc OpenJDK 25)](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/lang/Comparable.html) — contrat complet, toutes les méthodes.
- [Comparator (Javadoc OpenJDK 25)](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/Comparator.html) — méthodes `comparing`, `thenComparing`, `reversed`, `naturalOrder`.
- [Guide to Java Comparators](https://www.baeldung.com/java-comparator-comparable) (Baeldung) — exemples supplémentaires et cas d'usage courants.

## Prochain chapitre

→ [**4-7 — Lambdas et interfaces fonctionnelles**](./4-7-lambdas-interfaces-fonctionnelles.md)
