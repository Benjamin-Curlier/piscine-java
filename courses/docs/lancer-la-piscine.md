---
id: lancer-la-piscine
sidebar_position: 2
title: "Comment lancer la Piscine"
slug: /lancer-la-piscine
---

# Comment lancer la Piscine

Il existe **deux façons** d'utiliser la Piscine. Tu n'en utilises **qu'une seule** — celle qui correspond à ce que ton formateur t'a remis.

| Tu as reçu… | Ton mode | Ce que tu fais |
|-------------|----------|----------------|
| un **fichier ZIP** | **Standalone** | Tu décompresses et tu lances. **Rien à installer, aucune connexion.** |
| un **accès au dépôt Git** | **Dépôt** | Tu installes Java 25 + git, tu clones, tu lances le bootstrap. |

> Dans les deux cas, **la façon de travailler est identique** : tu édites un exercice, tu le rends via Git, et la moulinette te répond avec un rapport. Seule la mise en route change.

## Mode standalone (ZIP)

1. Décompresse le ZIP `piscine-etnc-stagiaire-*.zip` où tu veux.
2. Double-clique sur **`piscine.bat`** (Windows).
3. Au premier lancement, ton espace de travail est créé automatiquement, **le site de cours s'ouvre dans ton navigateur** et la **console de correction** démarre.
4. Si le navigateur ne s'ouvre pas tout seul, ouvre l'adresse affichée dans la console (par défaut `http://127.0.0.1:8800/`).

Tout est embarqué (Java, git, cours, moulinette) : **aucune installation, aucun réseau**.

## Mode dépôt (clone du projet)

1. Installe **Java 25** et **git** (voir le guide d'installation du dépôt).
2. Clone le dépôt, puis lance le script de bootstrap qui prépare ton espace de travail.
3. Lance la console de correction (REPL) avec la commande affichée par le bootstrap.

Le détail pas-à-pas des deux modes est dans le **[guide stagiaire complet](https://github.com/Benjamin-Curlier/Piscine-ETNC/blob/main/docs/piscine-stagiaire.md)**.

## Ensuite : la boucle de travail

Quel que soit ton mode :

1. Lis le chapitre de cours.
2. Fais l'exercice.
3. Rends via Git (`submit-start`, puis `add` / `commit` / `push`).
4. Lis le **rapport** de la moulinette.
5. Passe au chapitre suivant.
