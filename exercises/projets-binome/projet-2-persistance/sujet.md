# Projet binôme #2 — Gestionnaire de tâches en ligne de commande

## Contexte

Une équipe doit suivre ses tâches courantes : consignes à exécuter, points à
traiter, actions en attente. L'équipe veut un petit outil en ligne de
commande pour saisir ces tâches, suivre leur avancement et les **retrouver au
prochain lancement**. Les tâches sont donc enregistrées dans un fichier.

Vous concevez ce logiciel **en binôme**, de A à Z. Le sujet impose **ce que** le
programme doit faire ; vous restez libres du **comment** (noms de classes,
signatures, découpage).

## Objectifs pédagogiques

- Concevoir une petite application orientée objet appuyée sur une **collection**.
- Modéliser une donnée immuable avec un `record` et un état fini avec un `enum`.
- **Persister** l'état de l'application dans un **fichier CSV**, à la main.
- Gérer les erreurs proprement avec des **exceptions** (dont une exception que
  vous écrivez vous-mêmes).
- Être **robuste** face à un fichier absent ou à une donnée corrompue.
- Collaborer en binôme avec un historique Git équilibré.

## Entités imposées

Vous devez modéliser au minimum les entités suivantes. Les attributs listés sont
un minimum ; les signatures précises et le découpage interne sont à votre
initiative.

- **Tache** — un `record` **immuable** qui porte au minimum :
  - un **identifiant** (`id`, un entier) **attribué par l'application**, qui sert
    à désigner une tâche dans le menu ;
  - un **titre** (texte non vide) ;
  - un **statut**.

  Comme une `Tache` est immuable, « marquer une tâche faite » consiste à
  **reconstruire** la tâche avec le nouveau statut, puis à **remplacer**
  l'ancienne dans la collection.
- **Statut** — un `enum` à deux valeurs au minimum : `A_FAIRE` et `FAITE`.
- **Un gestionnaire** — une classe (au nom de votre choix) qui détient
  l'ensemble des tâches dans une **collection** (`List` ou `Map`), attribue les
  identifiants, et porte le chargement et la sauvegarde.

## Opérations obligatoires

1. **Ajouter** une tâche à partir d'un titre. L'application attribue un nouvel
   identifiant ; la tâche démarre au statut `A_FAIRE`.
2. **Lister** les tâches, avec leur identifiant et leur statut visibles.
3. **Marquer faite** une tâche désignée par son identifiant.
4. **Charger** les tâches au démarrage et **sauvegarder** l'état (voir
   « Persistance »).

## Persistance (fichier CSV, à la main)

L'état est enregistré dans un fichier texte au format **CSV**, que vous lisez et
écrivez **vous-mêmes** (pas de bibliothèque externe).

- Le fichier s'appelle `taches.csv` (son emplacement est fixé par vous et
  **documenté dans le README**). Il est lu et écrit en **UTF-8**.
- Une tâche par ligne, au format `id,titre,statut`.
- Le CSV reste **simple** : pas de gestion des virgules à l'intérieur d'un champ.
  Vous imposez donc qu'un **titre ne contienne pas de virgule**.
- **Au démarrage**, le programme **charge** le fichier. S'il est **absent**
  (premier lancement), la liste démarre **vide** : ce n'est pas une erreur.
- Au chargement, l'application reprend l'attribution des identifiants **au-dessus
  du plus grand identifiant déjà présent** dans le fichier. Si aucune tâche
  valide n'est chargée, vous démarrez à l'identifiant de votre choix (par
  exemple `1`).
- La **sauvegarde** s'effectue sur une commande `sauver` **et** automatiquement à
  la commande `quitter`.

## Robustesse et gestion des erreurs (avec exceptions)

Dans ce projet, la gestion des cas invalides se fait **avec des exceptions**
(acquis du module 5).

- **Ligne corrompue au chargement** (nombre de champs incorrect, identifiant non
  numérique, statut inconnu) : le programme **signale** la ligne fautive, **passe
  à la suivante** et continue. Une seule ligne abîmée ne doit pas empêcher le
  programme de démarrer.
- **Identifiant inexistant** lors d'un « marquer faite » : votre code **lève une
  exception que vous écrivez vous-mêmes** (par exemple
  `TacheIntrouvableException` qui hérite de `RuntimeException`). La boucle du menu
  la **rattrape** et affiche un message clair ; le programme continue.
- **Titre vide** à l'ajout : la saisie est refusée par une exception rattrapée,
  avec un message.
- **Accès au fichier** (droits, chemin invalide) : l'`IOException` est traitée
  proprement, avec un message lisible — jamais une trace d'erreur brute jetée à
  l'écran.

## Contrainte d'antériorité (importante)

Ce projet utilise les notions des **modules 1 à 5**.

- Autorisé : toute la POO (classes, encapsulation, héritage, polymorphisme,
  interfaces, `enum`, `record`, `sealed`) ; les **collections et génériques**
  (`List`, `Map`, `ArrayList`, `HashMap`, `Comparator`) et les
  **lambdas/streams** (module 4) ; les **exceptions** (`try`/`catch`/`throw`,
  exceptions personnalisées) et les **entrées/sorties fichier** (`Path`, `Files`,
  `try`-with-resources, module 5) ; les entrées/sorties **console** (`Scanner`).
- À ne pas utiliser : une **bibliothèque de sérialisation JSON**. La persistance
  se fait en **CSV à la main**.
- Ne constituent **pas** des objectifs notés à ce stade : **JUnit** comme
  livrable et un **workflow Git à base de branches et de pull requests**. Ces
  points seront abordés au module 6 et au projet final.

## Démonstration attendue

Le programme expose un **menu console interactif** (à l'aide de `Scanner`). Le
formateur lance le programme et saisit des commandes. Votre menu doit au minimum
proposer :

- `ajouter` — saisir le titre d'une nouvelle tâche ;
- `lister` — afficher les tâches (identifiant, titre, statut) ;
- `fait <id>` — marquer faite la tâche d'identifiant `<id>` ;
- `sauver` — enregistrer l'état dans le fichier ;
- `quitter` — sauvegarder puis terminer le programme.

Ces commandes minimales sont **imposées** : elles permettent au formateur
d'évaluer chaque rendu sur la même base.

La **persistance doit être prouvée** : on ajoute des tâches, on `quitter`, on
**relance** le programme, et les tâches sont toujours là.

## Bonus (facultatifs, valorisés)

Ces extensions ne sont pas obligatoires mais sont valorisées dans la note de
conception :

- `supprimer <id>` — retirer une tâche.
- Une **priorité** (par un `enum`) ou une **échéance** sur chaque tâche.
- Un **tri** ou un **filtre** de l'affichage (par statut, par priorité) à l'aide
  d'un `Comparator` ou de streams.
- Un statut intermédiaire `EN_COURS`.
