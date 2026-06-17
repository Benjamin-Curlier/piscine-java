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
| un **installeur** (`.exe` Windows, `.deb`/portable Linux) | **Installeur** | Tu installes (aucun droit admin) et tu lances. **Rien d'autre à installer, aucune connexion.** |
| un **accès au dépôt Git** | **Dépôt** | Tu installes Java 25 + git, tu clones, tu lances le bootstrap. |

> Dans les deux cas, **la façon de travailler est identique** : tu édites un exercice, tu le rends via Git, et la moulinette te répond avec un rapport. Seule la mise en route change.

## Mode installeur (recommandé)

L'installeur embarque **tout** : Java, git, la moulinette, les exercices et le site de cours. Aucune autre installation, aucun réseau.

1. **Windows** : double-clique sur `Piscine Java-<version>.exe` (installation par utilisateur, aucun droit admin). **Linux** : installe le `.deb` ou extrais l'archive portable.
2. Lance « **Piscine Java** » depuis le menu Démarrer (ou ton lanceur d'applications).
3. Au premier lancement, ton espace de travail est créé dans `~/PiscineJava` et **l'application s'ouvre dans ton navigateur** : tableau de bord (ta progression, tes badges, tes rapports), cours et terminal git intégré.
4. Si le navigateur ne s'ouvre pas tout seul, ouvre l'adresse affichée par l'application.

Tout est embarqué (Java, git, cours, moulinette) : **aucune installation supplémentaire, aucun réseau**.

## Mode dépôt (clone du projet)

1. Installe **Java 25** et **git** (voir le guide d'installation du dépôt).
2. Clone le dépôt, puis lance le script de bootstrap qui prépare ton espace de travail.
3. Lance la console de correction (REPL) avec la commande affichée par le bootstrap.

Le détail pas-à-pas des deux modes est dans le **[guide stagiaire complet](https://github.com/Benjamin-Curlier/piscine-java/blob/main/docs/piscine-stagiaire.md)**.

## La console reste ouverte

⚠️ **Garde la fenêtre de la console ouverte pendant tout ton travail.** C'est elle qui sert le site de cours **et** qui lance la moulinette quand tu rends un exercice. Si tu la fermes, le site s'arrête et plus rien n'est corrigé. Pour quitter (en fin de session), tape `exit`.

Tu tapes tes commandes Git **dans cette console** (l'invite `piscine[...]>`), pas dans un autre terminal : la console est un Git « encadré » qui ne connaît que les commandes utiles à la Piscine.

## Rendre un exercice

C'est **le geste central** de la Piscine. Le plus simple : **une seule commande**, `submit`.

```text
piscine[main]> submit 1.1
```

`submit 1.1` enregistre ton travail, le rend, et lance la moulinette sur le sous-groupe `1.1` — **sans que tu aies besoin de connaître Git**. Tu peux t'arrêter là pour démarrer ; la suite explique **ce que `submit` fait pour toi** (utile à savoir, et au programme du **module 6**).

### En détail : ce que `submit` fait à ta place (Git encadré)

Tu rends un **sous-groupe** d'exercices (ex. `1.1`) sur une branche dédiée `rendu/<sous-groupe>`. **C'est le `push` de cette branche qui déclenche la moulinette.** Étape par étape, `submit 1.1` revient à :

```text
piscine[main]> submit-start 1.1        # crée/rebascule sur la branche rendu/1.1
piscine[rendu/1.1]> git add .          # indexe tes modifications
piscine[rendu/1.1]> git commit -m "…"  # enregistre une version
piscine[rendu/1.1]> git push origin rendu/1.1   # rend → déclenche la moulinette
```

Tu peux taper ces commandes une à une (c'est l'occasion d'apprendre Git), ou laisser `submit` tout enchaîner.

### Ce qui se passe quand tu pousses

```text
[console] Push détecté sur rendu/1.1 → lancement moulinette sur sous-groupe 1.1
[console] ▶ Exo 1.1.1  ✓ OK
[console] ▶ Exo 1.1.2  ✗ ÉCHEC
    ✗ affiche la fiche au format attendu
       attendu : "=== Fiche membre ===\n..."
       obtenu  : ""
[console] Rapport : workspace/.piscine/reports/1.1-….md
[console] Corrige les exos en ✗ ci-dessus puis re-pousse (la progression se débloque dans l'ordre).
```

- La console détecte que tu as poussé une branche **`rendu/<sous-groupe>`**.
- Elle lance la **moulinette** sur **tous** les exercices de ce sous-groupe, **du plus simple au plus difficile**, et t'affiche le résultat de chacun. La **progression reste séquentielle** : un exo n'est validé que si **tous les précédents** passent — mais tu vois d'un coup d'œil où tu en es.
- En cas d'échec, elle te montre **ce qui était attendu vs. obtenu** (pas une trace Java brute).
- Elle écrit un **rapport pédagogique** dans `workspace/.piscine/reports/`.

### Lire le rapport et recommencer

Ouvre le fichier `.md` indiqué : pour chaque exo tu as le **statut**, les **messages d'erreur** et une **correction-type commentée**. Corrige l'exo qui a échoué, puis **refais `git add` / `git commit` / `git push origin rendu/1.1`**. La moulinette repart et avance tant que les exos passent.

> 💡 **Pourquoi Git ?** Dans la vraie vie, on rend et on partage du code via Git. La Piscine t'y entraîne dès le premier exercice : `add` (je prépare), `commit` (j'enregistre une version), `push` (je publie — ici, je rends). Le détail complet est dans le **module 6 « Tests et Git »**.

## En résumé, la boucle de travail

1. Lis le chapitre de cours.
2. Fais l'exercice.
3. Rends-le : **`submit <sous-groupe>`** (ex. `submit 1.1`).
4. Lis le **rapport** de la moulinette, corrige, re-`submit`.
5. Passe au chapitre suivant.

> Tout débutant·e peut s'en tenir à `submit`. Voir aussi le **[Démarrage rapide](demarrage-rapide.md)**.
