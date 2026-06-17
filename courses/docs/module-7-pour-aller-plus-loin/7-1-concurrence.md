---
id: 7-1-concurrence
sidebar_position: 1
title: "Découverte : la concurrence"
description: "Un aperçu des threads en Java : exécuter plusieurs tâches en parallèle, le danger de l'état partagé (race condition), et les outils pour faire ça proprement."
---

# Découverte : la concurrence

:::note Chapitre « découverte »
Ce chapitre est un **aperçu**, pas un cours complet — il n'y a pas d'exercice noté. La concurrence
est un domaine vaste et délicat ; le but ici est que vous **sachiez ce que c'est** et **où aller**
pour l'apprendre sérieusement. C'est typiquement un sujet d'une « Piscine Java avancée ».
:::

## Pourquoi s'y intéresser

Jusqu'ici, vos programmes faisaient **une chose à la fois**, de haut en bas. Mais un vrai logiciel
fait souvent plusieurs choses **en même temps** : télécharger un fichier *pendant* que l'interface
reste réactive, traiter mille requêtes web simultanées, utiliser les **plusieurs cœurs** de votre
processeur. C'est la **concurrence**.

## Un thread, c'est quoi ?

Un **thread** (fil d'exécution) est une suite d'instructions qui s'exécute « en parallèle » des
autres. Votre programme en a déjà un : la méthode `main` tourne sur le thread `main`. On peut en
lancer d'autres.

```java
Runnable tache = () -> System.out.println("Bonjour depuis un autre thread !");
Thread t = new Thread(tache);
t.start();            // lance la tâche EN PARALLÈLE (ne pas confondre avec run() qui l'exécute ici)
```

En pratique, on ne crée pas les threads à la main : on confie les tâches à un **`ExecutorService`**
(un « pool » de threads réutilisables) :

```java
ExecutorService pool = Executors.newFixedThreadPool(4);
pool.submit(() -> traiter(requete));
```

## Le grand piège : l'**état partagé**

Tant que les threads travaillent chacun dans leur coin, tout va bien. Le danger apparaît quand
**plusieurs threads modifient la même donnée** en même temps. Exemple : deux threads qui font
chacun `compteur++` 1000 fois.

```java
int compteur = 0;            // partagé
// thread A : 1000 fois compteur++
// thread B : 1000 fois compteur++
// résultat attendu : 2000… mais on obtient souvent MOINS !
```

Pourquoi ? `compteur++` n'est pas **atomique** : c'est *lire → ajouter 1 → réécrire*. Deux threads
peuvent lire la même valeur avant de la réécrire, et une incrémentation est « perdue ». C'est une
**race condition** (situation de compétition) — le bug le plus classique et le plus sournois de la
concurrence, car il est **intermittent**.

## Les outils pour faire ça proprement (aperçu)

- **`synchronized`** : un verrou qui garantit qu'un seul thread entre dans une section à la fois.
- **`AtomicInteger`**, `AtomicLong`… : des compteurs dont les opérations sont atomiques.
- **`java.util.concurrent`** : la boîte à outils — `ExecutorService`, `ConcurrentHashMap`,
  `BlockingQueue`, `CompletableFuture` (pour l'asynchrone)…
- **Préférer l'immuabilité** : une donnée qui ne change jamais ne peut pas être corrompue par un
  autre thread. (C'est pour ça que `String` et `record` immuables sont si pratiques.)

## En résumé

- Un **thread** exécute du code en parallèle ; on les gère via un **`ExecutorService`**.
- Le piège n°1 est l'**état mutable partagé** → **race conditions** (bugs intermittents).
- On s'en protège avec des **verrous** (`synchronized`), des types **atomiques**, les outils de
  `java.util.concurrent`, et surtout en **limitant l'état partagé**.

## Pour aller plus loin

- Reproduisez la race condition du compteur, puis corrigez-la avec `AtomicInteger`.
- Explorez `ExecutorService` et `CompletableFuture`.
- Lectures de référence : *Java Concurrency in Practice* (Goetz). C'est le sujet d'une suite avancée
  à cette Piscine — voir la section **« Périmètre & la suite »** du README.
