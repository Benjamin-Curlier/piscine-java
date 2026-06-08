---
id: 6-1-pourquoi-tester
sidebar_position: 1
title: "Pourquoi tester"
description: "Comprendre la valeur d'un test automatisé, les types de tests, le ROI et la non-régression — et découvrir que la moulinette est un harnais de tests."
---

# Pourquoi tester

## Pourquoi ce chapitre

Depuis le tout premier module, une moulinette corrige vos rendus : elle compile votre code, l'exécute sur des cas, et affiche un rapport vert ou rouge. Vous avez subi cet outil sans savoir comment il fonctionne — c'est un **harnais de tests automatisés** écrit avec les mêmes bibliothèques que celles de ce module. Ce chapitre vous explique enfin ce qui se passait « de l'autre côté », et surtout *pourquoi* tester son code est une compétence aussi importante que de l'écrire. À partir d'ici, ce n'est plus la moulinette qui teste votre code : c'est vous qui apprenez à écrire vos propres tests.

## Ce que vous saurez faire à la fin

- **Expliquer** ce qu'est un test automatisé et en quoi il diffère d'un test manuel.
- **Distinguer** les grands types de tests (unitaire, intégration, bout-en-bout).
- **Justifier** le retour sur investissement d'un test : ce qu'il coûte une fois, ce qu'il rapporte mille fois.
- **Définir** une régression et expliquer comment un test la prévient.
- **Reconnaître** dans la moulinette de la Piscine un harnais de tests automatisés.

## 1. Le coût d'un bug trouvé tard

Un bug ne coûte pas la même chose selon le moment où on le découvre. Repéré pendant que vous écrivez la méthode, il se corrige en quelques secondes. Repéré une semaine plus tard, alors que dix autres morceaux de code s'appuient dessus, il faut d'abord comprendre *pourquoi* ça casse, retrouver le coupable, puis vérifier que la correction ne casse rien d'autre. Découvert par un utilisateur en production, il coûte en plus de la confiance.

Le test automatisé déplace la découverte du bug **le plus tôt possible** : au moment où vous écrivez le code, voire avant (vous verrez cela avec le TDD au chapitre 6-5).

### Exemple

Imaginez une méthode de calcul de moyenne. Sans test, vous la lancez « à la main » une fois, le résultat semble correct, vous passez à autre chose. Trois semaines plus tard, un collègue modifie la méthode pour ignorer les valeurs négatives — et casse silencieusement le calcul sur les tableaux vides. Personne ne s'en aperçoit avant que le rapport mensuel affiche `NaN`.

Un seul test automatisé sur le cas « tableau vide » aurait signalé la casse **à la seconde où le collègue a sauvegardé son code**.

### À retenir

> - Plus un bug est découvert tard, plus il coûte cher à corriger.
> - Un test automatisé rapproche la découverte du bug du moment où il est introduit.
> - Tester « une fois à la main » ne protège pas contre les régressions futures.

## 2. Le test comme filet de sécurité

La vraie valeur d'une suite de tests n'est pas de prouver que le code marche aujourd'hui — c'est de vous **autoriser à le changer demain sans peur**. Quand chaque comportement important est couvert par un test, vous pouvez réorganiser, simplifier, optimiser : si vous cassez quelque chose, un test rouge vous le dit immédiatement.

Sans filet, on n'ose plus toucher au code qui « marche ». Le projet se fige, la dette s'accumule. Avec un filet de tests, le code reste vivant.

### À retenir

> - Les tests servent autant à **protéger les changements futurs** qu'à valider le présent.
> - Un bon filet de tests rend le code modifiable sans crainte.
> - Du code sans tests devient du code qu'on n'ose plus modifier.

## 2bis. Test manuel ou test automatisé

Tester « à la main », c'est lancer le programme, taper des valeurs, regarder l'écran. C'est utile pour explorer, mais cela a trois défauts : c'est lent, c'est non reproductible (vous oubliez un cas sur deux), et personne ne le refait à chaque modification. Un test **automatisé** est du code qui vérifie du code : on l'écrit une fois, et il se rejoue en une fraction de seconde, à l'identique, autant de fois qu'on veut.

### À retenir

> - Un test automatisé est **du code qui vérifie du code**.
> - On l'écrit une fois, il se rejoue à volonté, sans effort et sans oubli.

## 3. Les grands types de tests

On classe souvent les tests selon ce qu'ils couvrent :

| Type | Ce qu'il vérifie | Portée |
|---|---|---|
| **Unitaire** | Une méthode / une classe isolée | Petite, rapide, nombreuse |
| **Intégration** | Plusieurs composants ensemble (ex. code + fichier) | Moyenne |
| **Bout-en-bout (E2E)** | Le système complet, comme un utilisateur | Large, lente, rare |

Ce module se concentre sur le **test unitaire** : la brique de base, la plus rapide à écrire et à exécuter, celle que vous écrirez le plus souvent. Les deux autres niveaux sont mentionnés ici pour la culture ; vous les rencontrerez dans des projets plus grands.

### À retenir

> - **Unitaire** : une unité de code isolée — le cœur de ce module.
> - **Intégration** : plusieurs composants ensemble.
> - **Bout-en-bout** : le système entier, du point de vue de l'utilisateur.

## 4. Retour sur investissement et non-régression

Écrire un test a un coût : quelques minutes. Mais ce test sera rejoué des centaines de fois — à chaque modification, à chaque build, à chaque rendu. Le coût est payé **une fois**, le bénéfice se récolte **en continu**. C'est ce qu'on appelle le retour sur investissement (en anglais *ROI*, *Return On Investment*) du test.

Un cas particulièrement précieux est le test de **non-régression** : quand vous corrigez un bug, vous écrivez d'abord un test qui le reproduit. Une fois le bug corrigé, ce test passe au vert — et il **reste** dans la suite. Si quelqu'un réintroduit le même bug plus tard, le test redevient rouge. Le bug ne peut plus revenir en silence.

### La révélation : la moulinette est un harnais de tests

Vous l'avez compris : la moulinette qui vous note depuis le module 1 ne fait rien de magique. Elle compile votre rendu, puis exécute contre lui une **suite de tests** écrite avec les bibliothèques de ce module (JUnit et AssertJ). Chaque ligne verte de votre rapport est un test qui passe ; chaque ligne rouge, un test qui échoue. À partir de maintenant, vous apprenez à écrire ces tests vous-même.

### À retenir

> - Le test se paie **une fois** et rapporte **à chaque exécution** : c'est son retour sur investissement.
> - Un test de **non-régression** empêche un bug corrigé de revenir en silence.
> - La moulinette de la Piscine est un harnais de tests JUnit/AssertJ — ce module vous apprend à écrire les vôtres.

## Erreurs fréquentes

- **Se contenter de « ça marche chez moi »** : lancer le programme une fois, voir un résultat plausible, conclure que tout va bien. Cause : un essai manuel ne couvre qu'un seul cas, celui auquel on a pensé. Correction : écrire des tests automatisés qui couvrent aussi les cas limites (vide, zéro, négatif, très grand).

- **Ne tester que le cas qui passe** : vérifier que `diviser(10, 2)` donne `5` et s'arrêter là. Cause : on teste ce qu'on espère, pas ce qui peut casser. Correction : tester aussi les cas qui doivent échouer ou lever une exception (`diviser(10, 0)`).

- **Confondre test et débogage par affichage** : parsemer le code de `System.out.println` pour « voir si ça marche ». Cause : l'affichage est éphémère et demande une lecture humaine à chaque fois. Correction : un test automatisé vérifie tout seul, garde sa valeur dans le temps, et ne pollue pas le code de production.

## Exercice guidé

**Objectif** : développer le réflexe « quel test aurait attrapé ce bug ? » — sans écrire encore une seule ligne de test.

Reprenez mentalement (ou rouvrez) trois exercices que vous avez déjà rendus dans les modules 1 à 5. Pour chacun, imaginez **un bug plausible** qu'un développeur distrait aurait pu introduire, et formulez **en français** le test qui l'aurait détecté.

**Pas à pas :**

**Étape 1** — Prenez un calcul numérique (par exemple la moyenne d'un tableau, module 2). Quel bug ? Quel test l'attrape ?

**Étape 2** — Prenez une méthode qui valide une entrée (par exemple une saisie défensive, module 5). Quel bug ? Quel test l'attrape ?

**Étape 3** — Prenez une classe avec un invariant (par exemple un compte qui ne doit pas passer sous zéro, module 3). Quel bug ? Quel test l'attrape ?

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

Il n'y a pas de réponse unique — voici un exemple par étape pour calibrer votre réflexion.

**Étape 1 — Moyenne d'un tableau.**
- Bug plausible : oublier le cas du tableau vide → division par zéro, ou somme initialisée à `1` au lieu de `0`.
- Test qui l'attrape : « la moyenne de `{}` doit renvoyer `0` (ou lever une exception explicite), pas planter » ; « la moyenne de `{4, 6}` doit valoir exactement `5` ».

**Étape 2 — Saisie défensive.**
- Bug plausible : accepter une valeur hors de la plage autorisée, ou rejeter une valeur valide à la borne.
- Test qui l'attrape : « la valeur minimale autorisée est acceptée » ; « la valeur juste en dessous du minimum est rejetée (exception levée) ».

**Étape 3 — Compte avec invariant.**
- Bug plausible : un retrait supérieur au solde fait passer le compte en négatif au lieu d'être refusé.
- Test qui l'attrape : « retirer plus que le solde laisse le solde inchangé (ou lève une exception) » ; « le solde n'est jamais négatif après une suite d'opérations ».

Le point commun : chaque test décrit **un comportement attendu précis**, pour une entrée précise. C'est exactement ce que vous écrirez en JUnit dès le prochain chapitre.

</details>

## Vérifiez vos acquis

- Quelle est la différence entre un test manuel et un test automatisé ?
- En quoi un test sert-il autant à protéger les changements futurs qu'à valider le code présent ?
- Qu'est-ce qu'un test de non-régression, et pourquoi le garde-t-on dans la suite après avoir corrigé le bug ?
- Citez les trois grands types de tests. Lequel ce module enseigne-t-il principalement ?
- En une phrase : qu'est-ce que la moulinette de la Piscine, vue de l'intérieur ?

## Pour aller plus loin

- [Software Testing — Vue d'ensemble](https://www.baeldung.com/cs/unit-testing-vs-integration-testing) (Baeldung) — comparatif clair entre test unitaire et test d'intégration.
- [Unit Testing in Java](https://dev.java/learn/) (dev.java) — le portail officiel Oracle pour approfondir les bonnes pratiques de test.

## Prochain chapitre

→ **[Chapitre 6-2 — JUnit 5, les bases](6-2-junit5-bases)**
