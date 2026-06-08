---
id: 6-4-assertj
sidebar_position: 4
title: "AssertJ"
description: "Écrire des assertions fluides et lisibles avec assertThat(...), comparer objets et collections, tester une exception."
---

# AssertJ

## Pourquoi ce chapitre

Les assertions de JUnit (`assertEquals`, `assertTrue`…) font le travail, mais elles deviennent vite peu lisibles dès qu'on vérifie une collection ou un objet, et leurs messages d'échec sont parfois pauvres. **AssertJ** est une bibliothèque d'assertions « fluides » : on écrit `assertThat(obtenu).isEqualTo(attendu)`, ce qui se lit presque comme une phrase, et ses messages d'échec sont nettement plus parlants. C'est la bibliothèque d'assertions que la moulinette privilégie — autant l'adopter.

## Ce que vous saurez faire à la fin

- **Écrire** une assertion fluide avec `assertThat(...)`.
- **Expliquer** pourquoi AssertJ améliore la lisibilité et les messages d'échec.
- **Vérifier** le contenu d'une collection (`contains`, `containsExactly`, `hasSize`).
- **Comparer** des objets avec `isEqualTo`.
- **Tester** qu'un bloc lève une exception avec `assertThatThrownBy`.

## 1. assertThat et les assertions fluides

Le principe d'AssertJ tient en une ligne : on passe la valeur **obtenue** à `assertThat(...)`, puis on enchaîne une ou plusieurs vérifications. L'enchaînement (on parle d'interface « fluide ») permet de combiner plusieurs assertions sur le même objet sans le répéter.

Toutes les assertions viennent d'un seul import statique : `org.assertj.core.api.Assertions.assertThat`.

### Exemple

```java
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class CalculTest {

    @Test
    void additionneDeuxEntiers() {
        int resultat = new Calcul().ajouter(2, 3);
        assertThat(resultat).isEqualTo(5);
    }

    @Test
    void leResultatEstPositifEtPair() {
        int resultat = new Calcul().ajouter(4, 6);
        assertThat(resultat).isPositive().isEven(); // deux vérifications enchaînées
    }
}
```

Remarquez l'ordre naturel : `assertThat(obtenu).isEqualTo(attendu)` se lit « affirme que le résultat obtenu est égal à 5 ». C'est l'inverse de l'ordre de `assertEquals`, mais beaucoup plus intuitif.

### À retenir

> - `assertThat(obtenu)` d'abord, puis la vérification — l'**obtenu** vient en premier.
> - Les assertions s'enchaînent sur le même objet (`.isPositive().isEven()`).
> - Un seul import à retenir : `org.assertj.core.api.Assertions.assertThat`.

## 2. Pourquoi AssertJ plutôt que les assertions de JUnit

Trois raisons concrètes :

1. **Lisibilité** : `assertThat(liste).contains("lait")` se lit mieux que `assertTrue(liste.contains("lait"))`.
2. **Messages d'échec riches** : quand `assertThat(liste).containsExactly("a", "b")` échoue, AssertJ affiche précisément quels éléments manquaient ou étaient en trop. `assertTrue` ne dirait que « expected true but was false ».
3. **Autocomplétion guidée** : après `assertThat(uneListe).`, votre IDE propose uniquement les assertions pertinentes pour une liste — vous découvrez les possibilités sans documentation.

### À retenir

> - AssertJ gagne sur trois plans : lisibilité, qualité des messages d'échec, découvrabilité.
> - Un message d'échec précis fait gagner du temps de diagnostic à chaque test rouge.

## 3. Comparer objets et collections

AssertJ brille surtout sur les collections. Voici les assertions les plus utiles :

| Assertion | Vérifie que… |
|---|---|
| `isEqualTo(x)` / `isNotEqualTo(x)` | l'objet est égal / différent de `x` |
| `isNull()` / `isNotNull()` | l'objet est `null` / non `null` |
| `hasSize(n)` | la collection a `n` éléments |
| `isEmpty()` / `isNotEmpty()` | la collection est vide / non vide |
| `contains(x, y)` | la collection contient ces éléments (peu importe l'ordre et le reste) |
| `containsExactly(x, y, z)` | la collection contient exactement ces éléments, **dans cet ordre** |
| `containsExactlyInAnyOrder(...)` | exactement ces éléments, **ordre indifférent** |

Pour comparer deux objets avec `isEqualTo`, AssertJ s'appuie sur leur méthode `equals`. C'est immédiat pour un `record` (qui génère `equals` automatiquement, module 4), mais une classe ordinaire sans `equals` ne serait comparée que par référence — pensez-y.

### Exemple

```java
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class InventaireTest {

    @Test
    void contientLesArticlesAjoutesDansLOrdre() {
        List<String> articles = List.of("pain", "lait", "oeufs");
        assertThat(articles)
            .hasSize(3)
            .contains("lait")
            .containsExactly("pain", "lait", "oeufs");
    }
}
```

### À retenir

> - `contains` vérifie la présence ; `containsExactly` impose le **contenu exact et l'ordre**.
> - `containsExactlyInAnyOrder` quand l'ordre n'a pas de sens.
> - `isEqualTo` compare via `equals` : parfait pour un `record`, attention aux classes nues.

## 4. Tester une exception avec assertThatThrownBy

Vous avez rencontré `assertThatThrownBy` au module 5 ; le voici dans son contexte d'origine, AssertJ. On lui passe le bloc de code qui doit échouer (sous forme de lambda), puis on vérifie le **type** de l'exception et, au besoin, son **message**.

### Exemple

```java
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AccesTest {

    @Test
    void accederHorsBornesLeveUneException() {
        List<String> vide = List.of();
        assertThatThrownBy(() -> vide.get(0))
            .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    void leCasValideNeLevePas() {
        List<String> liste = List.of("a");
        assertThat(liste.get(0)).isEqualTo("a");
    }
}
```

### À retenir

> - `assertThatThrownBy(() -> …)` vérifie qu'un bloc lève bien une exception.
> - On précise le type avec `.isInstanceOf(...)` et, si utile, `.hasMessageContaining(...)`.
> - Vérifiez aussi qu'un cas valide **ne lève pas** — les deux faces du comportement.

## Erreurs fréquentes

- **Mélanger l'`assertThat` de JUnit et celui d'AssertJ** : importer `org.junit.jupiter.api.Assertions.assertThat` (déprécié, limité) au lieu de celui d'AssertJ. Cause : les deux portent le même nom. Correction : importez `org.assertj.core.api.Assertions.assertThat` ; en cas de doute, vérifiez l'import en tête de fichier.

- **Confondre `containsExactly` et `containsExactlyInAnyOrder`** : utiliser `containsExactly` sur le résultat d'un `Set`, dont l'ordre n'est pas garanti. Cause : on oublie que l'ordre compte pour `containsExactly`. Correction : pour une collection sans ordre stable, utilisez `containsExactlyInAnyOrder`.

- **Comparer avec `isEqualTo` des objets sans `equals` pertinent** : attendre que deux instances d'une classe ordinaire « égales par leurs champs » soient `isEqualTo`. Cause : sans `equals` redéfini, la comparaison se fait par référence. Correction : utilisez un `record` (qui fournit `equals`), ou redéfinissez `equals`/`hashCode` (module 4).

## Exercice guidé

**Objectif** : remplacer des assertions JUnit par des assertions AssertJ plus lisibles sur une collection.

On dispose d'une classe `Palette` dont la méthode `couleursPrimaires()` renvoie la liste `["rouge", "vert", "bleu"]` dans cet ordre. Écrivez un test AssertJ qui vérifie la taille, la présence d'une couleur, et le contenu exact ordonné.

**Pas à pas :**

**Étape 1** — Récupérez la liste via `new Palette().couleursPrimaires()`.

**Étape 2** — Enchaînez `hasSize`, `contains` et `containsExactly` en une seule assertion fluide.

```java
// Code fourni (ne pas modifier)
import java.util.List;

public class Palette {
    public List<String> couleursPrimaires() {
        return List.of("rouge", "vert", "bleu");
    }
}
```

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

```java
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class PaletteTest {

    @Test
    void lesTroisCouleursPrimairesDansLOrdre() {
        List<String> couleurs = new Palette().couleursPrimaires();
        assertThat(couleurs)
            .hasSize(3)
            .contains("vert")
            .containsExactly("rouge", "vert", "bleu");
    }
}
```

Points à noter :
- Une seule chaîne d'assertions vérifie trois choses : la taille, une présence, le contenu exact ordonné.
- Si la `Palette` renvoyait les couleurs dans un autre ordre, `containsExactly` échouerait avec un message précis (« element at index 0 differs »).
- En JUnit pur, il aurait fallu trois `assertEquals`/`assertTrue` séparés, moins lisibles et moins informatifs en cas d'échec.

</details>

## Vérifiez vos acquis

- Dans `assertThat(a).isEqualTo(b)`, lequel de `a` et `b` est la valeur obtenue ?
- Citez deux raisons concrètes de préférer AssertJ aux assertions de JUnit.
- Quelle est la différence entre `contains` et `containsExactly` ?
- Pourquoi `isEqualTo` fonctionne-t-il directement sur un `record` mais pas toujours sur une classe ordinaire ?
- Comment vérifier qu'un bloc lève une `IllegalArgumentException` avec AssertJ ?

## Pour aller plus loin

- [AssertJ — Core Assertions Guide](https://assertj.github.io/doc/#assertj-core-assertions-guide) (documentation officielle) — le catalogue complet des assertions par type.
- [Introduction to AssertJ](https://www.baeldung.com/introduction-to-assertj) (Baeldung) — tour d'horizon des assertions les plus utiles avec exemples.

## Prochain chapitre

→ **[Chapitre 6-5 — TDD en pratique](6-5-tdd-en-pratique)**
