---
id: 6-5-tdd-en-pratique
sidebar_position: 5
title: "TDD en pratique"
description: "Le cycle Red/Green/Refactor : écrire le test d'abord, le voir échouer pour la bonne raison, puis faire passer et nettoyer."
---

# TDD en pratique

## Pourquoi ce chapitre

Jusqu'ici, vous avez écrit le code, puis ses tests. Le **développement piloté par les tests** (en anglais *TDD*, *Test-Driven Development*) inverse l'ordre : on écrit **le test d'abord**, on le regarde échouer, puis on écrit le code minimal qui le fait passer. Cette discipline paraît contre-intuitive, mais elle produit du code mieux conçu, mieux couvert, et des tests qui prouvent vraiment quelque chose. C'est l'aboutissement de ce bloc « tests » : vous savez écrire des tests, vous allez apprendre à vous en servir comme d'un guide de conception.

## Ce que vous saurez faire à la fin

- **Dérouler** le cycle Red / Green / Refactor.
- **Expliquer** pourquoi un test doit d'abord échouer « pour la bonne raison ».
- **Avancer** par petits pas, en écrivant le code minimal qui fait passer le test.
- **Voir** le test comme une spécification exécutable du comportement attendu.
- **Juger** quand le TDD aide et quand il gêne.

## 1. Le cycle Red / Green / Refactor

Le TDD répète un cycle court, en trois temps :

1. **Red** (rouge) — écrire un test pour un comportement qui n'existe pas encore. Le lancer : il échoue (il est rouge), c'est normal et voulu.
2. **Green** (vert) — écrire le **code minimal** qui fait passer ce test, sans chercher l'élégance. Le test devient vert.
3. **Refactor** (remanier) — nettoyer le code (et les tests) maintenant qu'un filet protège le comportement. Relancer : tout doit rester vert.

Puis on recommence pour le comportement suivant. Chaque tour ne dure que quelques minutes.

### À retenir

> - **Red** : un test qui échoue d'abord. **Green** : le code minimal qui le fait passer. **Refactor** : on nettoie à filet vert.
> - Le cycle est **court** : on tourne en quelques minutes, pas en quelques heures.

## 2. Le test doit échouer pour la bonne raison

L'étape Red n'est pas une formalité. Un test qui ne passe **jamais** par l'échec ne prouve rien : peut-être qu'il aurait été vert même sans le code ! En le voyant échouer d'abord, vous vérifiez deux choses : que le test exerce réellement le comportement visé, et qu'il échoue avec le bon message (pas à cause d'une faute de frappe ou d'une exception inattendue).

Un test qui ne distingue pas un code correct d'un code faux est un test inutile. Le cycle Red garantit que votre test **capture** vraiment le comportement : il passe sur le bon code et échoue sur le mauvais.

### Exemple

```java
// ÉTAPE RED — on écrit le test AVANT la classe Pgcd.
// À ce stade, Pgcd n'existe pas encore : le code ne compile même pas → c'est l'échec attendu.
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class PgcdTest {

    @Test
    void pgcdDeDouzeEtHuitEstQuatre() {
        assertThat(Pgcd.de(12, 8)).isEqualTo(4);
    }
}
```

### À retenir

> - Voir le test **rouge d'abord** prouve qu'il teste bien quelque chose.
> - Un test qui ne sait pas distinguer un bon code d'un mauvais est inutile.
> - L'échec initial doit survenir **pour la bonne raison** (le comportement manquant, pas une coquille).

## 3. Avancer par petits pas

À l'étape Green, on écrit le **minimum** pour faire passer le test — quitte à ce que ce soit naïf. On résiste à la tentation d'anticiper des cas non encore testés. Si un nouveau cas doit être géré, on écrit d'abord son test (retour à Red). Cette progression par petits pas garde le code toujours couvert et toujours en état de marche.

### Exemple

```java
// ÉTAPE GREEN — le code minimal qui fait passer PgcdTest.
class Pgcd {
    static int de(int a, int b) {
        return b == 0 ? a : de(b, a % b); // algorithme d'Euclide (récursivité, module 2)
    }
}
```

Le test passe au vert. **Refactor** : ici le code est déjà clair et concis, rien à nettoyer — on note simplement qu'on aurait pu renommer ou commenter si nécessaire, puis on passe au comportement suivant (par exemple, le PGCD avec un argument nul, qui mériterait son propre test).

### À retenir

> - À l'étape Green, écrivez le **code minimal**, sans anticiper les cas non testés.
> - Un nouveau cas à gérer ⇒ un nouveau test d'abord (retour à Red).
> - Le refactor est une étape à part entière, faite **à filet vert**.

## 4. Le test comme spécification exécutable

Écrit en premier, le test devient une **spécification** : il décrit, en code exécutable, ce que la méthode doit faire, avant même qu'elle existe. La suite de tests d'une classe se lit alors comme son cahier des charges : chaque test est une exigence, vérifiable automatiquement.

Le TDD n'est pas une religion. Il brille quand le comportement attendu est clair et qu'on peut le formuler en assertions (calculs, règles métier, validations). Il aide moins quand on **explore** une solution dont on ne connaît pas encore la forme (prototypage d'interface graphique, recherche d'algorithme). Sachez l'utiliser là où il paie.

### À retenir

> - Écrit d'abord, le test **spécifie** le comportement : la suite de tests est un cahier des charges exécutable.
> - Le TDD excelle sur les comportements clairs et assertables ; il gêne dans l'exploration pure.

## Erreurs fréquentes

- **Écrire le code avant le test** : coder la méthode, puis ajouter un test qui « confirme » ce qu'on vient d'écrire. Cause : c'est l'habitude. Correction : ce n'est plus du TDD — le test écrit après épouse les bugs du code. En TDD, le test vient d'abord et guide le code.

- **Un premier test qui passe immédiatement** : écrire un test déjà vert dès le départ. Cause : le comportement existait déjà, ou le test est trop faible. Correction : un test qui n'a jamais été rouge ne prouve pas qu'il teste quelque chose — assurez-vous de l'échec initial.

- **Sauter l'étape Refactor** : enchaîner les cycles Red/Green sans jamais nettoyer. Cause : « ça marche, on continue ». Correction : la dette s'accumule ; profitez du filet vert pour remanier régulièrement — c'est tout l'intérêt d'avoir des tests.

## Exercice guidé

**Objectif** : dérouler un cycle TDD complet sur une petite fonction, en respectant l'ordre Red → Green → Refactor.

On veut une méthode `Pgcd.de(int a, int b)` qui calcule le plus grand commun diviseur. Construisez-la en TDD.

**Pas à pas :**

**Étape 1 (Red)** — Écrivez un test `pgcdDeDouzeEtHuitEstQuatre` qui attend `4`. Lancez-le : il échoue (la classe `Pgcd` n'existe pas).

**Étape 2 (Green)** — Créez `Pgcd.de` avec le code minimal pour passer ce test.

**Étape 3 (Red puis Green)** — Ajoutez un test pour un cas limite : `Pgcd.de(7, 0)` doit valoir `7`. S'il échoue, ajustez le code ; s'il passe déjà, tant mieux — vous avez vérifié un cas de plus.

**Étape 4 (Refactor)** — Relisez : le code est-il clair ? Les tests sont-ils lisibles ? Nettoyez à filet vert.

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

**Les tests (écrits en premier) :**

```java
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class PgcdTest {

    @Test
    void pgcdDeDouzeEtHuitEstQuatre() {
        assertThat(Pgcd.de(12, 8)).isEqualTo(4);
    }

    @Test
    void pgcdAvecZeroRenvoieLAutreNombre() {
        assertThat(Pgcd.de(7, 0)).isEqualTo(7);
    }
}
```

**Le code (écrit pour faire passer les tests) :**

```java
class Pgcd {
    static int de(int a, int b) {
        return b == 0 ? a : de(b, a % b); // Euclide : le cas b == 0 gère aussi pgcd(7, 0)
    }
}
```

Points à noter :
- Le premier test était rouge **avant** d'écrire `Pgcd` (la classe n'existait pas) : il a bien échoué pour la bonne raison.
- Le second test (cas limite `b == 0`) passe sans modifier le code, car l'algorithme d'Euclide le gère déjà — on l'a quand même écrit pour **figer** ce comportement (non-régression).
- L'étape Refactor n'a rien changé ici : le code est déjà minimal et clair. C'est fréquent sur de petites fonctions — l'important est d'avoir pris le réflexe de se poser la question.

</details>

## Vérifiez vos acquis

- Quelles sont les trois étapes du cycle TDD, et dans quel ordre ?
- Pourquoi est-il important de voir le test échouer **avant** d'écrire le code ?
- Que signifie « écrire le code minimal » à l'étape Green ?
- En quoi un test écrit en premier sert-il de spécification ?
- Donnez un cas où le TDD aide clairement, et un cas où il est moins adapté.

## Pour aller plus loin

- [Test-Driven Development with JUnit](https://www.baeldung.com/java-test-driven-list) (Baeldung) — un exemple complet de construction d'une classe en TDD, pas à pas.
- [The cycle of TDD](https://martinfowler.com/bliki/TestDrivenDevelopment.html) (Martin Fowler) — la référence courte et claire sur l'esprit du TDD.

## Prochain chapitre

→ **[Chapitre 6-6 — Git : les fondamentaux](6-6-git-fondamentaux)**
