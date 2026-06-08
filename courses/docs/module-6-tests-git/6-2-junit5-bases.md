---
id: 6-2-junit5-bases
sidebar_position: 2
title: "JUnit 5 — les bases"
description: "Écrire ses premiers tests avec JUnit 5 : @Test, les assertions, et le schéma Arrange-Act-Assert."
---

# JUnit 5 — les bases

## Pourquoi ce chapitre

Le chapitre précédent vous a convaincu de l'intérêt des tests ; celui-ci vous apprend à en écrire. **JUnit 5** est la bibliothèque de test standard de l'écosystème Java — c'est elle que la moulinette utilise pour vous noter. Vous allez découvrir où se rangent les tests, comment marquer une méthode comme test, comment affirmer qu'un résultat est correct, et un schéma simple pour structurer chaque test de façon lisible.

## Ce que vous saurez faire à la fin

- **Placer** vos classes de test au bon endroit dans un projet Maven.
- **Écrire** une méthode de test avec l'annotation `@Test`.
- **Vérifier** un résultat avec les assertions de JUnit (`assertEquals`, `assertTrue`, `assertThrows`).
- **Structurer** un test selon le schéma Arrange-Act-Assert.
- **Nommer** un test par le comportement qu'il vérifie.

## 1. Où vivent les tests

Dans un projet Maven (celui que la Piscine utilise pour chaque exercice), le code de production et le code de test sont séparés en deux arborescences parallèles :

```text
src/
├── main/java/        ← votre code de production (la classe Calcul…)
└── test/java/        ← vos tests (la classe CalculTest…)
```

La convention est simple : pour une classe `Calcul`, on crée une classe de test `CalculTest`, dans le **même package**, mais sous `src/test/java`. Cette séparation permet de livrer le code sans embarquer les tests, tout en gardant les deux côte à côte.

### À retenir

> - Le code de production vit sous `src/main/java`, les tests sous `src/test/java`.
> - Pour une classe `Xxx`, la classe de test s'appelle `XxxTest`, dans le même package.

## 2. Une classe de test, une méthode @Test

Un test est une méthode publique (ou de visibilité package) sans valeur de retour, annotée avec **`@Test`**. L'annotation indique à JUnit : « cette méthode est un test, exécute-la ». Une classe de test peut contenir autant de méthodes `@Test` que de comportements à vérifier.

### Exemple

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculTest {

    @Test
    void additionneDeuxEntiers() {
        Calcul calcul = new Calcul();
        int resultat = calcul.ajouter(2, 3);
        assertEquals(5, resultat);
    }
}
```

JUnit exécute `additionneDeuxEntiers`, et le test **passe** si l'assertion est satisfaite, **échoue** sinon. On lance les tests depuis l'IDE (un clic sur la classe), en ligne de commande (`mvn test`), ou… via la moulinette, qui ne fait pas autre chose.

### À retenir

> - Une méthode annotée `@Test` est un test exécutable par JUnit.
> - Une classe de test regroupe plusieurs `@Test`, un par comportement vérifié.
> - Sans `@Test`, la méthode n'est jamais exécutée comme test.

## 3. Les assertions de JUnit

Une **assertion** est une affirmation que le test impose. Si elle est fausse, le test échoue. JUnit fournit des méthodes statiques dans la classe `Assertions` :

| Assertion | Vérifie que… |
|---|---|
| `assertEquals(attendu, obtenu)` | les deux valeurs sont égales |
| `assertTrue(condition)` | la condition est vraie |
| `assertFalse(condition)` | la condition est fausse |
| `assertNull(objet)` / `assertNotNull(objet)` | l'objet est `null` / non `null` |
| `assertThrows(Type.class, () -> …)` | le bloc lève bien l'exception attendue |

**Attention à l'ordre des arguments** de `assertEquals` : la valeur **attendue** vient en premier, la valeur **obtenue** ensuite. Inverser les deux ne change pas le verdict du test, mais rend le message d'échec trompeur (« expected 5 but was 3 » deviendrait faux).

### Exemple

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculTest {

    @Test
    void diviseDeuxEntiers() {
        Calcul calcul = new Calcul();
        assertEquals(4, calcul.diviser(12, 3));
    }

    @Test
    void unNombrePairEstDetecte() {
        Calcul calcul = new Calcul();
        assertTrue(calcul.estPair(10));
    }

    @Test
    void diviserParZeroLeveUneException() {
        Calcul calcul = new Calcul();
        assertThrows(ArithmeticException.class, () -> calcul.diviser(12, 0));
    }
}
```

### À retenir

> - Une assertion affirme un fait ; si le fait est faux, le test échoue.
> - `assertEquals(attendu, obtenu)` — l'**attendu** d'abord, toujours.
> - `assertThrows` vérifie qu'un bloc lève bien l'exception prévue (acquis du module 5).

## 4. Le schéma Arrange-Act-Assert

Un bon test se lit en trois temps, dans cet ordre :

1. **Arrange** (préparer) — créer les objets, préparer les données d'entrée.
2. **Act** (agir) — appeler la méthode que l'on veut tester.
3. **Assert** (vérifier) — affirmer que le résultat est celui attendu.

Ce découpage rend chaque test lisible d'un coup d'œil : on voit la situation, l'action, le verdict. Gardez **une seule action vérifiée par test** : si une méthode a plusieurs comportements, écrivez plusieurs tests plutôt qu'un seul test fourre-tout.

### Exemple

```java
@Test
void laMoyenneDeDeuxNotesEstLeurDemiSomme() {
    Calcul calcul = new Calcul();                 // Arrange
    double moyenne = calcul.moyenne(12, 16);      // Act
    assertEquals(14.0, moyenne);                  // Assert
}
```

### À retenir

> - **Arrange-Act-Assert** : préparer, agir, vérifier — dans cet ordre.
> - Un test vérifie **un seul** comportement ; plusieurs comportements ⇒ plusieurs tests.
> - Nommez le test par ce qu'il vérifie, au présent (`additionneDeuxEntiers`).

## Erreurs fréquentes

- **Inverser les arguments de `assertEquals`** : écrire `assertEquals(resultat, 5)` au lieu de `assertEquals(5, resultat)`. Cause : on lit « le résultat égale 5 » et on tape dans cet ordre. Correction : l'attendu vient toujours en premier — sinon le message d'échec annonce la mauvaise valeur comme « attendue ».

- **Mettre plusieurs comportements dans un seul `@Test`** : tester l'addition, la division et la parité dans la même méthode. Cause : ça paraît économe. Correction : un test par comportement — sinon, dès qu'une assertion échoue, les suivantes ne s'exécutent pas et vous perdez de l'information.

- **Oublier l'annotation `@Test`** : écrire une méthode de test sans la marquer. Cause : on se concentre sur le corps et on oublie l'annotation. Correction : JUnit n'exécute que les méthodes `@Test` — sans elle, votre test est ignoré silencieusement (faux sentiment de sécurité, « tout est vert »).

- **Confondre `import org.junit.jupiter.api` (JUnit 5) et `org.junit` (JUnit 4)** : copier un vieil exemple. Cause : les deux versions coexistent sur le web. Correction : en JUnit 5, les imports commencent par `org.junit.jupiter`.

## Exercice guidé

**Objectif** : écrire vos premiers tests sur une méthode déjà fournie, sans en modifier le code.

On vous donne une classe `Nombres` contenant une méthode `estPair(int n)` qui renvoie `true` si `n` est pair, `false` sinon. Écrivez une classe de test qui vérifie les deux cas.

**Pas à pas :**

**Étape 1** — Créez `NombresTest` dans `src/test/java`, dans le même package que `Nombres`.

**Étape 2** — Écrivez un premier test : un nombre pair est bien détecté.

**Étape 3** — Écrivez un second test : un nombre impair n'est pas détecté comme pair.

```java
// Code fourni (ne pas modifier)
public class Nombres {
    public boolean estPair(int n) {
        return n % 2 == 0;
    }
}
```

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class NombresTest {

    @Test
    void quatreEstPair() {
        Nombres nombres = new Nombres();   // Arrange
        boolean resultat = nombres.estPair(4); // Act
        assertTrue(resultat);              // Assert
    }

    @Test
    void septNEstPasPair() {
        Nombres nombres = new Nombres();
        assertFalse(nombres.estPair(7));
    }
}
```

Points à noter :
- Deux comportements (« pair détecté », « impair non détecté ») ⇒ deux tests distincts.
- Chaque test suit Arrange-Act-Assert (ici compact, mais l'ordre est respecté).
- Le nom dit ce qui est vérifié : pas besoin de commentaire pour comprendre l'intention.
- On pourrait aller plus loin et tester `0` (pair), un nombre négatif pair, etc. — chaque cas limite mérite son test.

</details>

## Vérifiez vos acquis

- Dans quel dossier rangez-vous une classe de test, et comment la nommez-vous pour une classe `Facture` ?
- À quoi sert l'annotation `@Test` ? Que se passe-t-il si vous l'oubliez ?
- Dans `assertEquals(a, b)`, lequel est l'attendu et lequel est l'obtenu ?
- Quelles sont les trois étapes du schéma Arrange-Act-Assert ?
- Comment vérifier qu'une méthode lève bien une exception donnée ?

## Pour aller plus loin

- [JUnit 5 User Guide — Writing Tests](https://junit.org/junit5/docs/current/user-guide/#writing-tests) (documentation officielle) — la référence complète sur `@Test` et les assertions.
- [Guide to JUnit 5](https://www.baeldung.com/junit-5) (Baeldung) — prise en main pas à pas avec des exemples annotés.

## Prochain chapitre

→ **[Chapitre 6-3 — JUnit 5, avancé](6-3-junit5-avance)**
