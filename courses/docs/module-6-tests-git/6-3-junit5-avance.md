---
id: 6-3-junit5-avance
sidebar_position: 3
title: "JUnit 5 — avancé"
description: "Cycle de vie (@BeforeEach), tests paramétrés (@ParameterizedTest, @CsvSource), organisation (@Nested, @DisplayName)."
---

# JUnit 5 — avancé

## Pourquoi ce chapitre

Vos premiers tests fonctionnent, mais vous remarquez vite deux gênes : on recrée les mêmes objets au début de chaque test, et on duplique un test pour chaque jeu de valeurs. JUnit 5 répond à ces deux besoins avec le **cycle de vie** (`@BeforeEach`) et les **tests paramétrés** (`@ParameterizedTest`). Vous apprendrez aussi à organiser et nommer vos tests pour qu'une suite reste lisible quand elle grossit.

## Ce que vous saurez faire à la fin

- **Factoriser** la préparation commune avec `@BeforeEach` et `@AfterEach`.
- **Écrire** un test paramétré avec `@ParameterizedTest`, `@ValueSource` et `@CsvSource`.
- **Organiser** des tests apparentés avec `@Nested`.
- **Donner** un libellé lisible à un test avec `@DisplayName`.
- **Distinguer** `@BeforeEach` (avant chaque test) de `@BeforeAll` (une seule fois).
- **Comprendre** ce qu'est un *test double* (stub, fake, mock) et écrire un *fake* à la main.

## 1. Le cycle de vie : @BeforeEach et @AfterEach

Quand tous vos tests commencent par les mêmes lignes de préparation, on les déplace dans une méthode annotée **`@BeforeEach`** : JUnit l'exécute **avant chaque** test de la classe. Symétriquement, **`@AfterEach`** s'exécute après chaque test (utile pour nettoyer une ressource).

Point important : JUnit crée **une nouvelle instance de la classe de test pour chaque méthode `@Test`**. Vos tests sont donc isolés les uns des autres — l'état préparé dans `@BeforeEach` repart à neuf à chaque fois.

### Exemple

```java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PanierTest {

    private Panier panier;

    @BeforeEach
    void preparerUnPanierVide() {
        panier = new Panier(); // recréé avant CHAQUE test
    }

    @Test
    void unPanierNeufEstVide() {
        assertEquals(0, panier.nombreArticles());
    }

    @Test
    void ajouterUnArticleIncrementeLeCompte() {
        panier.ajouter("pain");
        assertEquals(1, panier.nombreArticles());
    }
}
```

### À retenir

> - `@BeforeEach` s'exécute **avant chaque** test ; `@AfterEach`, après chaque test.
> - JUnit crée une instance neuve par test : les tests sont isolés.
> - Déplacez la préparation commune dans `@BeforeEach` plutôt que de la dupliquer.

## 2. Les tests paramétrés

Tester la même logique sur plusieurs jeux de valeurs avec un test par valeur est fastidieux. Un **test paramétré** exécute la **même** méthode plusieurs fois, avec des entrées différentes. On remplace `@Test` par **`@ParameterizedTest`** et on fournit les valeurs avec une source.

- **`@ValueSource`** : une liste de valeurs simples (entiers, chaînes…), un paramètre.
- **`@CsvSource`** : des lignes « entrée, attendu » séparées par des virgules — le plus lisible pour associer une entrée à son résultat.

### Exemple

```java
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class NombresTest {

    @ParameterizedTest
    @ValueSource(ints = {2, 4, 100, -8})
    void cesNombresSontPairs(int n) {
        assertTrue(new Nombres().estPair(n));
    }

    @ParameterizedTest
    @CsvSource({
        "0, 32",
        "100, 212",
        "-40, -40"
    })
    void celsiusVersFahrenheit(int celsius, int attendu) {
        assertEquals(attendu, new Convertisseur().versFahrenheit(celsius));
    }
}
```

Le premier test s'exécute quatre fois (une par valeur), le second trois fois (une par ligne). Chaque ligne de `@CsvSource` fournit deux paramètres : l'entrée et le résultat attendu.

### À retenir

> - `@ParameterizedTest` rejoue la même méthode avec plusieurs jeux de données.
> - `@ValueSource` : des valeurs simples, un paramètre.
> - `@CsvSource` : des couples « entrée, attendu » — idéal pour la lisibilité.

## 3. Organiser : @Nested et @DisplayName

Quand une classe de test grossit, on regroupe les tests apparentés dans une classe interne annotée **`@Nested`** : par exemple, tous les tests « panier vide » d'un côté, « panier rempli » de l'autre. Le rapport de test affiche alors une arborescence.

L'annotation **`@DisplayName`** remplace le nom technique de la méthode par un libellé lisible dans le rapport — pratique pour les phrases qui ne tiennent pas dans un nom de méthode.

### Exemple

```java
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Le panier d'achat")
class PanierTest {

    @Nested
    @DisplayName("quand il est vide")
    class PanierVide {
        @Test
        @DisplayName("contient zéro article")
        void compteEstZero() {
            assertEquals(0, new Panier().nombreArticles());
        }
    }
}
```

### À retenir

> - `@Nested` regroupe des tests apparentés en sous-classes ⇒ rapport en arbre.
> - `@DisplayName` donne un libellé lisible (avec espaces et accents) au test ou à la classe.

## 4. @BeforeAll et @AfterAll (à connaître)

À côté de `@BeforeEach`, il existe **`@BeforeAll`** : exécutée **une seule fois**, avant tous les tests de la classe (et `@AfterAll`, une fois après). Comme elle ne dépend d'aucune instance, elle doit être **`static`**. On la réserve à une préparation coûteuse et partageable sans risque (par exemple ouvrir une connexion en lecture seule). Pour la plupart des tests, `@BeforeEach` est le bon choix, car il garantit l'isolement.

### À retenir

> - `@BeforeAll` / `@AfterAll` : une seule fois pour toute la classe, et **`static`**.
> - Par défaut, préférez `@BeforeEach` — il préserve l'isolement entre tests.

## 5. Isoler le code testé : les *test doubles*

Un bon test unitaire vérifie **une seule** classe à la fois. Mais cette classe dépend souvent d'autres objets : une horloge, une base de données, un service réseau… Ces **collaborateurs** sont parfois lents, indisponibles, ou imprévisibles (l'heure courante change à chaque exécution). Les faire intervenir pour de vrai rendrait le test lent et fragile.

La solution : remplacer le vrai collaborateur par une doublure de test (*test double*) — un objet bidon, sous votre contrôle, qui joue le même rôle le temps du test. On distingue trois variantes selon ce qu'on attend de la doublure :

- **Stub** : il renvoie des réponses **fixées d'avance**. « Quand on te demande l'heure, réponds toujours 10 h. » On l'utilise pour **fournir une entrée** au code testé.
- **Fake** : une implémentation **simplifiée mais réelle**, suffisante pour le test. Typiquement, un dépôt de données stocké dans une simple `Map` en mémoire au lieu d'une vraie base.
- **Mock** : un objet qui **enregistre comment on l'a appelé**, pour qu'on puisse ensuite **vérifier les interactions** (« la méthode `envoyer` a-t-elle bien été appelée une fois ? »).

Pas besoin de bibliothèque (comme Mockito) pour débuter : il suffit d'**implémenter l'interface du collaborateur** avec une petite classe écrite à la main. Pour cela, le code testé doit dépendre d'une **interface**, pas d'une classe concrète — on lui passe alors le vrai objet en production, et la doublure dans le test.

### Exemple

On veut tester un `Bonjour` qui salue différemment selon l'heure. Il dépend d'une `Horloge` — qu'on ne veut surtout pas faire dépendre de l'heure réelle.

```java
// L'interface dont dépend le code testé
interface Horloge {
    int heure(); // 0 à 23
}

// Le code testé : il reçoit une Horloge, sans savoir laquelle
class Bonjour {
    private final Horloge horloge;

    Bonjour(Horloge horloge) {
        this.horloge = horloge;
    }

    String saluer() {
        return horloge.heure() < 18 ? "Bonjour" : "Bonsoir";
    }
}
```

Dans le test, on écrit un **fake** d'`Horloge` qui renvoie une heure choisie :

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BonjourTest {

    // Un fake : implémentation minimale et contrôlée d'Horloge
    static class HorlogeFigee implements Horloge {
        private final int heure;
        HorlogeFigee(int heure) { this.heure = heure; }
        @Override public int heure() { return heure; }
    }

    @Test
    void diseBonjourLeMatin() {
        var bonjour = new Bonjour(new HorlogeFigee(9));
        assertEquals("Bonjour", bonjour.saluer());
    }

    @Test
    void diseBonsoirLeSoir() {
        var bonjour = new Bonjour(new HorlogeFigee(20));
        assertEquals("Bonsoir", bonjour.saluer());
    }
}
```

Le test est désormais **rapide, déterministe et indépendant** de l'heure réelle : on a maîtrisé le collaborateur.

### À retenir

> - Un *test double* remplace un collaborateur réel (lent, externe, imprévisible) le temps du test.
> - **Stub** = réponses fixées d'avance ; **fake** = implémentation simplifiée réelle ; **mock** = enregistre les appels pour vérifier les interactions.
> - On peut écrire un *fake* **à la main** en implémentant une interface — aucune bibliothèque n'est nécessaire pour débuter.
> - Pour pouvoir substituer une doublure, faites dépendre votre code d'une **interface**, pas d'une classe concrète.

## Erreurs fréquentes

- **Partager un état mutable entre tests** : déclarer un objet en champ, le remplir dans un test, et compter dessus dans le suivant. Cause : on croit l'état conservé d'un test à l'autre. Correction : JUnit recrée l'instance à chaque test ; remettez l'état à neuf dans `@BeforeEach`, ne dépendez jamais de l'ordre d'exécution.

- **Mauvais séparateur ou mauvais types dans `@CsvSource`** : écrire `"0; 32"` (point-virgule) ou attendre un `int` là où la valeur est décimale. Cause : la virgule est le séparateur par défaut, et les types doivent correspondre aux paramètres. Correction : séparez par des virgules et alignez les types des paramètres de la méthode.

- **Croire que `@BeforeAll` s'exécute avant chaque test** : y mettre une préparation qu'on veut neuve à chaque fois. Cause : confusion avec `@BeforeEach`. Correction : `@BeforeAll` ne tourne qu'une fois (et doit être `static`) ; pour une préparation par test, c'est `@BeforeEach`.

## Exercice guidé

**Objectif** : remplacer plusieurs tests répétitifs par un seul test paramétré avec `@CsvSource`.

On veut tester une méthode `versFahrenheit(int celsius)` d'une classe `Convertisseur` (formule : `°F = °C × 9 / 5 + 32`). Au lieu d'écrire un test par température, écrivez un seul test paramétré couvrant plusieurs couples connus.

**Pas à pas :**

**Étape 1** — Préparez le `Convertisseur` dans un `@BeforeEach`.

**Étape 2** — Écrivez un `@ParameterizedTest` avec `@CsvSource` couvrant au moins : 0 °C → 32 °F, 100 °C → 212 °F, −40 °C → −40 °F.

```java
// Code fourni (ne pas modifier)
public class Convertisseur {
    public int versFahrenheit(int celsius) {
        return celsius * 9 / 5 + 32;
    }
}
```

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

```java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ConvertisseurTest {

    private Convertisseur convertisseur;

    @BeforeEach
    void initialiser() {
        convertisseur = new Convertisseur();
    }

    @ParameterizedTest
    @CsvSource({
        "0, 32",
        "100, 212",
        "-40, -40",
        "37, 98"
    })
    void celsiusVersFahrenheit(int celsius, int attendu) {
        assertEquals(attendu, convertisseur.versFahrenheit(celsius));
    }
}
```

Points à noter :
- Un seul test couvre quatre cas : ajouter une ligne à `@CsvSource` suffit pour en couvrir un cinquième.
- `@BeforeEach` évite de recréer le `Convertisseur` à la main dans chaque cas.
- Le couple `-40 → -40` est un cas intéressant : la seule température identique dans les deux échelles. Bien choisir ses valeurs de test, c'est couvrir les cas qui révèlent les bugs.

</details>

## Vérifiez vos acquis

- Quelle est la différence entre `@BeforeEach` et `@BeforeAll` ? Laquelle doit être `static` ?
- Pourquoi JUnit crée-t-il une nouvelle instance de la classe de test pour chaque méthode `@Test` ?
- Quand préférez-vous `@CsvSource` à `@ValueSource` ?
- À quoi servent `@Nested` et `@DisplayName` dans une grande suite de tests ?
- Un test paramétré avec trois lignes de `@CsvSource` : combien de fois s'exécute-t-il ?
- Quelle est la différence entre un *stub*, un *fake* et un *mock* ? Pourquoi votre code doit-il dépendre d'une interface pour qu'on puisse y glisser une doublure ?

## Pour aller plus loin

- [JUnit 5 — Parameterized Tests](https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests) (documentation officielle) — toutes les sources de paramètres, dont `@MethodSource`.
- [Guide to JUnit 5 Parameterized Tests](https://www.baeldung.com/parameterized-tests-junit-5) (Baeldung) — exemples détaillés de `@ValueSource`, `@CsvSource` et au-delà.

## Prochain chapitre

→ **[Chapitre 6-4 — AssertJ](6-4-assertj)**
