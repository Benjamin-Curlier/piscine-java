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
  autre thread. (C'est pour ça que `String` est si pratique en concurrence.) ⚠️ Un `record` n'est
  immuable **que si tous ses champs le sont aussi** : `record Panier(List<String> articles)` reste
  modifiable via la liste partagée — ce n'est *pas* thread-safe. Visez des champs eux-mêmes immuables
  (types primitifs, `String`, autres records immuables, ou copies défensives).

## Les *virtual threads* : un aperçu

Les threads classiques (dits *plateforme*) sont **chers** : chacun correspond à un thread du système d'exploitation et consomme de la mémoire. On ne peut en lancer que quelques milliers — d'où les **pools** qui les recyclent.

Java propose maintenant les **threads virtuels** (*virtual threads*, JEP 444, stables depuis Java 21 et présents dans Java 25). Ce sont des threads **ultra-légers**, gérés par la JVM et non par le système. On peut en lancer **des millions** sans saturer la machine. L'idée : écrire du code **bloquant simple** (« lis ce fichier, attends cette réponse réseau ») tout en gardant une excellente capacité de traitement en parallèle.

Les lancer est direct :

```java
// Un thread virtuel ponctuel
Thread.ofVirtual().start(() -> System.out.println("Salut depuis un thread virtuel"));

// Ou un exécuteur qui crée UN thread virtuel par tâche soumise
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    for (int i = 0; i < 10_000; i++) {
        executor.submit(() -> traiter(requete));
    }
} // le try-with-resources attend la fin de toutes les tâches
```

Quand brillent-ils ? Quand vos tâches passent leur temps à **attendre** (entrées/sorties : réseau, fichiers, base de données) plutôt qu'à calculer. Pendant qu'un thread virtuel attend une réponse, la JVM le « met de côté » et fait avancer les autres — d'où la possibilité d'en avoir une multitude. Pour du calcul pur qui sature le processeur, ils n'apportent rien de plus que les threads classiques.

:::note À retenir
Les threads virtuels ne suppriment **pas** le piège de l'état partagé : une *race condition* reste une *race condition*. Ils changent le **coût** d'un thread, pas les règles de la concurrence.
:::

## En résumé

- Un **thread** exécute du code en parallèle ; on les gère via un **`ExecutorService`**.
- Le piège n°1 est l'**état mutable partagé** → **race conditions** (bugs intermittents).
- On s'en protège avec des **verrous** (`synchronized`), des types **atomiques**, les outils de
  `java.util.concurrent`, et surtout en **limitant l'état partagé**.
- Les **threads virtuels** (Java 21+) sont ultra-légers : idéaux pour beaucoup de tâches qui
  **attendent** des entrées/sorties — mais ils ne dispensent pas de gérer l'état partagé.

## Pour aller plus loin

- Reproduisez la race condition du compteur, puis corrigez-la avec `AtomicInteger`.
- Explorez `ExecutorService` et `CompletableFuture`.
- Lectures de référence : *Java Concurrency in Practice* (Goetz). C'est le sujet d'une suite avancée
  à cette Piscine — voir la section **« Périmètre & la suite »** du README.
