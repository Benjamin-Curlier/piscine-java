---
id: 4-2-set-hashset-equals-hashcode
sidebar_position: 2
title: "Set, HashSet, equals et hashCode"
description: "Utiliser un Set pour garantir l'unicité des éléments, et écrire equals et hashCode pour définir l'égalité logique d'un objet."
---

# Set, HashSet, equals et hashCode

## Pourquoi ce chapitre

Au [chapitre 4.1](4-1-list-arraylist), vous avez vu qu'une `List` conserve chaque élément, même en double. Dans certains cas, on veut au contraire une collection qui **refuse les doublons** — comme une liste de tags : ajouter le tag « java » deux fois n'a pas de sens. C'est le rôle du `Set`.

Mais pour qu'un `Set` reconnaisse deux objets comme « identiques », il faut que la classe de ces objets définisse ce que signifie l'égalité. Ce chapitre vous apprend à écrire cette définition à la main, en redéfinissant les méthodes `equals` et `hashCode`.

## Ce que vous saurez faire à la fin

- **Déclarer** un `Set<String>` et constater qu'un doublon est ignoré.
- **Distinguer** `HashSet` (non ordonné) de `TreeSet` (trié).
- **Expliquer** la différence entre l'égalité de référence (`==`) et l'égalité logique (`equals`).
- **Écrire** une méthode `equals` correcte, champ par champ, avec vérification de type et protection contre `null`.
- **Écrire** une méthode `hashCode` cohérente avec `equals` en utilisant `Objects.hash(...)`.
- **Identifier** la conséquence concrète d'un `equals`/`hashCode` manquant dans un `HashSet`.

## 1. Le `Set` : une collection sans doublon

Un `Set` (ensemble, en anglais) est une collection qui garantit que chaque élément n'apparaît **qu'une seule fois**. La méthode `add` renvoie un `boolean` : `true` si l'élément a été ajouté, `false` s'il était déjà présent.

### Exemple

```java
import java.util.HashSet;
import java.util.Set;

public class ExempleTags {
    public static void main(String[] args) {
        // On déclare par l'interface Set, on instancie HashSet
        Set<String> tags = new HashSet<>();

        boolean ajout1 = tags.add("java");       // true : premier ajout
        boolean ajout2 = tags.add("collections"); // true : nouveau tag
        boolean ajout3 = tags.add("java");       // false : déjà présent, ignoré

        System.out.println(ajout1);   // true
        System.out.println(ajout2);   // true
        System.out.println(ajout3);   // false
        System.out.println(tags);     // [java, collections] (ordre non garanti)
        System.out.println(tags.size()); // 2, pas 3
    }
}
```

Le `Set` offre aussi `contains`, `remove` et `size`, comme la `List`. En revanche, il n'y a **pas d'accès par index** (`get(i)` n'existe pas).

### À retenir

> - Un `Set` refuse les doublons : `add` renvoie `false` si l'élément est déjà présent.
> - Il n'y a pas d'accès par position : on parcourt avec un for-each ou on teste la présence avec `contains`.

## 2. `HashSet` vs `TreeSet` : lequel choisir ?

Deux implémentations du JDK couvrent la plupart des besoins :

| Implémentation | Ordre | Condition |
|---|---|---|
| `HashSet` | **Non garanti** (dépend du code de hachage) | Aucune — c'est le choix par défaut |
| `TreeSet` | **Trié** (ordre naturel des éléments) | Les éléments doivent savoir se comparer entre eux |

`HashSet` est **plus rapide** pour `add`/`contains`/`remove` (temps constant en moyenne). `TreeSet` est utile quand vous avez besoin que les éléments soient dans l'ordre lors du parcours — mais il exige que les éléments sachent se comparer entre eux : c'est automatique pour des types comme `String` ou `Integer`, et vous apprendrez à le faire pour vos propres classes dans un prochain chapitre.

### Exemple

```java
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class ExempleOrdre {
    public static void main(String[] args) {
        Set<String> nonOrdonne = new HashSet<>();
        nonOrdonne.add("gamma");
        nonOrdonne.add("alpha");
        nonOrdonne.add("beta");
        System.out.println(nonOrdonne); // ordre imprévisible

        Set<String> ordonne = new TreeSet<>();
        ordonne.add("gamma");
        ordonne.add("alpha");
        ordonne.add("beta");
        System.out.println(ordonne); // [alpha, beta, gamma]
    }
}
```

### À retenir

> - `HashSet` : choix par défaut, rapide, ordre non garanti.
> - `TreeSet` : éléments toujours triés ; utilisable avec des types qui savent se comparer (comme `String` ou `Integer`).

## 3. Égalité de référence (`==`) vs égalité logique (`equals`)

En Java, l'opérateur `==` compare des **références** : il teste si deux variables pointent vers le **même objet en mémoire**. Deux objets distincts avec les mêmes valeurs de champs ne sont **pas** `==`.

La méthode `equals`, héritée de `Object`, compare deux objets selon leur **contenu logique**. Par défaut (si vous ne la redéfinissez pas), `equals` fait la même chose que `==` : elle compare les références. C'est rarement ce qu'on veut pour nos propres classes.

### Exemple

```java
public class ExempleEgalite {
    public static void main(String[] args) {
        String a = new String("bonjour");
        String b = new String("bonjour");

        System.out.println(a == b);       // false : deux objets distincts en mémoire
        System.out.println(a.equals(b));  // true  : même contenu logique

        // String redéfinit equals pour comparer les caractères, pas la référence.
        // Sans cette redéfinition, equals se comporterait comme ==.
    }
}
```

### À retenir

> - `==` compare des références (adresses mémoire).
> - `equals` compare le contenu logique — **à condition que la classe le redéfinisse**.
> - `String`, `Integer` et les autres types du JDK redéfinissent `equals`. Vos propres classes ne le font pas automatiquement.

## 4. Écrire `equals` à la main

Pour qu'un `HashSet` (ou une `HashMap` au prochain chapitre) reconnaisse deux objets distincts comme « le même élément », il faut redéfinir `equals` dans votre classe. Voici le modèle à suivre, avec toutes les vérifications nécessaires :

### Exemple

```java
import java.util.Objects;

public class Couleur {
    private final int rouge;
    private final int vert;
    private final int bleu;

    public Couleur(int rouge, int vert, int bleu) {
        this.rouge = rouge;
        this.vert = vert;
        this.bleu = bleu;
    }

    @Override
    public boolean equals(Object autre) {
        // 1. Même référence → forcément égaux
        if (this == autre) {
            return true;
        }
        // 2. null ou type différent → pas égaux
        if (autre == null || getClass() != autre.getClass()) {
            return false;
        }
        // 3. Conversion sûre (on sait que c'est une Couleur)
        Couleur c = (Couleur) autre;
        // 4. Comparaison champ par champ
        return rouge == c.rouge
            && vert == c.vert
            && bleu == c.bleu;
    }
}
```

Détail des quatre étapes :

1. **Même référence** : si `this == autre`, c'est forcément `true` (optimisation rapide).
2. **`null` ou mauvais type** : on renvoie `false` sans risque de `NullPointerException` ni de `ClassCastException`.
3. **Conversion explicite** : après avoir vérifié le type, le cast est sûr.
4. **Comparaison champ par champ** : pour des champs primitifs (`int`, `double`…), on utilise `==` ; pour des champs objets (`String`, etc.), on utilise `Objects.equals(champ, c.champ)` pour éviter un `NullPointerException`.

### À retenir

> - Redéfinir `equals` suit toujours le même modèle en quatre étapes.
> - Vérifiez `null` et le type **avant** de caster.
> - Pour les champs objets, préférez `Objects.equals(a, b)` à `a.equals(b)` (protection contre `null`).

## 5. Écrire `hashCode` à la main

Chaque classe qui redéfinit `equals` **doit** aussi redéfinir `hashCode`. Le code de hachage (hash code) est un entier calculé à partir des champs de l'objet. Il est utilisé par `HashSet` et `HashMap` pour ranger les objets dans des « cases » (buckets) : deux objets égaux selon `equals` doivent se retrouver dans **la même case**, c'est-à-dire avoir **le même `hashCode`**.

La façon la plus simple est d'utiliser `Objects.hash(...)` en lui passant exactement les mêmes champs que ceux comparés dans `equals` :

### Exemple

```java
import java.util.Objects;

public class Couleur {
    private final int rouge;
    private final int vert;
    private final int bleu;

    public Couleur(int rouge, int vert, int bleu) {
        this.rouge = rouge;
        this.vert = vert;
        this.bleu = bleu;
    }

    @Override
    public boolean equals(Object autre) {
        if (this == autre) return true;
        if (autre == null || getClass() != autre.getClass()) return false;
        Couleur c = (Couleur) autre;
        return rouge == c.rouge && vert == c.vert && bleu == c.bleu;
    }

    @Override
    public int hashCode() {
        // On passe exactement les mêmes champs que dans equals
        return Objects.hash(rouge, vert, bleu);
    }

    public int getRed()   { return rouge; }
    public int getGreen() { return vert; }
    public int getBlue()  { return bleu; }
}
```

### À retenir

> - `equals` et `hashCode` vont **toujours ensemble** : redéfinir l'un sans l'autre est une erreur.
> - Utilisez `Objects.hash(champ1, champ2, …)` avec les **mêmes champs** que dans `equals`.

## 6. Le contrat `equals`/`hashCode` expliqué simplement

Java impose un contrat entre ces deux méthodes. En pratique, retenez trois règles :

1. **Réflexivité** : un objet est toujours égal à lui-même (`a.equals(a)` vaut `true`).
2. **Symétrie** : si `a.equals(b)` vaut `true`, alors `b.equals(a)` doit aussi valoir `true`.
3. **Cohérence `equals`/`hashCode`** : si `a.equals(b)` vaut `true`, alors `a.hashCode() == b.hashCode()` doit être vrai.

La règle 3 est **indispensable** pour que `HashSet` fonctionne correctement. La règle 2 est facile à casser par inadvertance (par exemple, si `equals` traite différemment `A.equals(B)` et `B.equals(A)`).

> **Note** : la réciproque de la règle 3 est fausse — deux `hashCode` égaux n'impliquent pas que les objets sont égaux (c'est une collision, phénomène normal). C'est pourquoi `HashSet` vérifie aussi `equals` après avoir trouvé une case.

### À retenir

> - `equals` doit être symétrique : si `a.equals(b)`, alors `b.equals(a)`.
> - Si deux objets sont égaux (`equals`), ils **doivent** avoir le même `hashCode`.
> - La réciproque est fausse : même `hashCode` ne garantit pas l'égalité.

## 7. La conséquence concrète : doublons dans un `HashSet`

Si vous mettez une classe dans un `HashSet` sans redéfinir `equals` et `hashCode`, Java utilise la version héritée d'`Object` — qui compare les références. Deux objets distincts, même identiques champ par champ, seront considérés comme différents et le `HashSet` les conservera **tous les deux**.

### Exemple

```java
import java.util.HashSet;
import java.util.Set;

public class ExempleDoublon {
    public static void main(String[] args) {
        // Couleur AVEC equals et hashCode redéfinis (voir section 5)
        Couleur rouge1 = new Couleur(255, 0, 0);
        Couleur rouge2 = new Couleur(255, 0, 0); // même valeur, objet distinct

        Set<Couleur> palette = new HashSet<>();
        palette.add(rouge1);
        palette.add(rouge2); // add renvoie false : rouge2 reconnu comme doublon

        System.out.println(palette.size()); // 1 — correct

        // Si Couleur n'avait PAS redéfini equals/hashCode :
        // palette.size() vaudrait 2 — les deux objets sont conservés
        // car == les distingue (références différentes).
    }
}
```

### À retenir

> - Sans `equals`/`hashCode` : un `HashSet` se base sur les références, les doublons logiques s'accumulent.
> - Avec `equals`/`hashCode` : le `HashSet` reconnaît les objets logiquement identiques et les rejette.

## Erreurs fréquentes

- **Redéfinir `equals` sans `hashCode`** — symptôme : des doublons apparaissent dans le `HashSet` malgré un `equals` correct. Cause : `HashSet` cherche dans la mauvaise case (le `hashCode` hérité dépend de la référence). Correction : redéfinir toujours les deux ensemble.
- **Comparer avec `==` au lieu d'`equals`** — symptôme : `if (couleur == autreObjet)` renvoie `false` même pour deux objets identiques champ par champ. Correction : utiliser `couleur.equals(autreObjet)`.
- **Caster sans vérifier le type dans `equals`** — symptôme : une `ClassCastException` à l'exécution quand on compare une `Couleur` avec un `String`. Correction : vérifier `getClass() != autre.getClass()` avant le cast.
- **Utiliser des champs différents dans `equals` et `hashCode`** — symptôme : deux objets considérés égaux par `equals` ont des `hashCode` différents ; le `HashSet` ne les retrouve pas. Correction : passer les **mêmes champs** à `Objects.hash(...)` que ceux comparés dans `equals`.

## Exercice guidé

**Domaine** : tags d'un article, puis classe `Couleur`.

### Partie A — Tags sans doublon

Créez une méthode `static` qui reçoit un tableau de `String` et renvoie un `Set<String>` contenant uniquement les valeurs distinctes, dans n'importe quel ordre.

Pas à pas :
1. Déclarez `Set<String> tags = new HashSet<>();`.
2. Parcourez le tableau avec un for-each et appelez `tags.add(mot)`.
3. Renvoyez `tags`.

### Partie B — Classe `Couleur` avec `equals`/`hashCode`

Créez une classe `Couleur(int rouge, int vert, int bleu)` avec `equals` et `hashCode` corrects. Vérifiez que deux instances avec les mêmes valeurs ne créent qu'un seul élément dans un `HashSet`.

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

**Partie A**

```java
import java.util.HashSet;
import java.util.Set;

public class UtilitaireTags {

    // Renvoie les tags distincts (les doublons sont ignorés par HashSet)
    public static Set<String> tagsDistincts(String[] mots) {
        Set<String> tags = new HashSet<>();
        for (String mot : mots) {
            tags.add(mot); // add renvoie false si le mot est déjà présent — on l'ignore
        }
        return tags;
    }

    public static void main(String[] args) {
        String[] entree = {"java", "web", "java", "api", "web", "java"};
        Set<String> resultat = tagsDistincts(entree);
        System.out.println(resultat); // [java, web, api] (ordre non garanti)
        System.out.println(resultat.size()); // 3
    }
}
```

**Partie B**

```java
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Couleur {
    private final int rouge;
    private final int vert;
    private final int bleu;

    public Couleur(int rouge, int vert, int bleu) {
        this.rouge = rouge;
        this.vert = vert;
        this.bleu = bleu;
    }

    @Override
    public boolean equals(Object autre) {
        // Étape 1 : même référence
        if (this == autre) return true;
        // Étape 2 : null ou type différent
        if (autre == null || getClass() != autre.getClass()) return false;
        // Étape 3 : conversion sûre
        Couleur c = (Couleur) autre;
        // Étape 4 : comparaison champ par champ
        return rouge == c.rouge && vert == c.vert && bleu == c.bleu;
    }

    @Override
    public int hashCode() {
        // Mêmes champs que dans equals
        return Objects.hash(rouge, vert, bleu);
    }

    public static void main(String[] args) {
        Couleur rouge1 = new Couleur(255, 0, 0);
        Couleur rouge2 = new Couleur(255, 0, 0); // même contenu, référence distincte

        System.out.println(rouge1.equals(rouge2)); // true : égalité logique
        System.out.println(rouge1 == rouge2);       // false : références différentes

        Set<Couleur> palette = new HashSet<>();
        palette.add(rouge1);
        boolean dejaPresent = !palette.add(rouge2); // add renvoie false → rouge2 ignoré
        System.out.println(dejaPresent);    // true
        System.out.println(palette.size()); // 1
    }
}
```

</details>

## Vérifiez vos acquis

- Qu'est-ce qui différencie un `Set` d'une `List` ?
- Dans quels cas choisit-on `TreeSet` plutôt que `HashSet` ?
- Que se passe-t-il si vous redéfinissez `equals` mais oubliez `hashCode`, et que vous utilisez la classe dans un `HashSet` ?
- Dans la méthode `equals`, pourquoi vérifie-t-on `getClass() != autre.getClass()` avant de caster ?

## Pour aller plus loin

- [Guide to hashCode() in Java](https://www.baeldung.com/java-hashcode) (Baeldung) — contrat, collisions et implémentations alternatives.
- [The equals and hashCode Contracts](https://dev.java/learn/jvm/tools/core/equals-hashcode/) (dev.java) — présentation officielle du contrat avec exemples.
- [Interface Set — Javadoc OpenJDK 25](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/Set.html) — méthodes disponibles sur `Set` et leurs garanties.

## Prochain chapitre

→ **[Chapitre 4.3 — Map et HashMap](4-3-map-hashmap)**
