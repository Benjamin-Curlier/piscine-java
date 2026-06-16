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

1. Décompresse le ZIP `piscine-java-stagiaire-*.zip` où tu veux.
2. Double-clique sur **`piscine.bat`** (Windows).
3. Au premier lancement, ton espace de travail est créé automatiquement, **le site de cours s'ouvre dans ton navigateur** et la **console de correction** démarre.
4. Si le navigateur ne s'ouvre pas tout seul, ouvre l'adresse affichée dans la console (par défaut `http://127.0.0.1:8800/`).

Tout est embarqué (Java, git, cours, moulinette) : **aucune installation, aucun réseau**.

## Mode dépôt (clone du projet)

1. Installe **Java 25** et **git** (voir le guide d'installation du dépôt).
2. Clone le dépôt, puis lance le script de bootstrap qui prépare ton espace de travail.
3. Lance la console de correction (REPL) avec la commande affichée par le bootstrap.

Le détail pas-à-pas des deux modes est dans le **[guide stagiaire complet](https://github.com/Benjamin-Curlier/piscine-java/blob/main/docs/piscine-stagiaire.md)**.

## La console reste ouverte

⚠️ **Garde la fenêtre de la console ouverte pendant tout ton travail.** C'est elle qui sert le site de cours **et** qui lance la moulinette quand tu rends un exercice. Si tu la fermes, le site s'arrête et plus rien n'est corrigé. Pour quitter (en fin de session), tape `exit`.

Tu tapes tes commandes Git **dans cette console** (l'invite `piscine[...]>`), pas dans un autre terminal : la console est un Git « encadré » qui ne connaît que les commandes utiles à la Piscine.

## Rendre un exercice avec Git

C'est **le geste central** de la Piscine. Tu rends un **sous-groupe** d'exercices (ex. `1.1`) sur une branche dédiée `rendu/<sous-groupe>`. **C'est le `push` de cette branche qui déclenche la moulinette.**

```text
piscine[main]> submit-start 1.1
Bascule sur la branche rendu/1.1.
```

1. **`submit-start 1.1`** crée (ou rebascule sur) la branche **`rendu/1.1`**.
2. **Édite ton code** dans `workspace/exercises/1.1.1-.../starter/` avec ton éditeur habituel.
3. **Indexe et enregistre** tes modifications :
   ```text
   piscine[rendu/1.1]> git add exercises/1.1.1-hello-world
   piscine[rendu/1.1]> git commit -m "rendu 1.1.1"
   ```
4. **Pousse la branche de rendu** — c'est ce qui lance la correction :
   ```text
   piscine[rendu/1.1]> git push origin rendu/1.1
   ```

### Ce qui se passe quand tu pousses

```text
[console] Push détecté sur rendu/1.1 → lancement moulinette sur sous-groupe 1.1
[console] ▶ Exo 1.1.1  ✓ OK
[console] ▶ Exo 1.1.2  ✗ ÉCHEC
[console] Rapport : workspace/.piscine/reports/1.1-….md
[console] On s'arrête ici (un exo doit passer avant de continuer). Corrige et re-push.
```

- La console détecte que tu as poussé une branche **`rendu/<sous-groupe>`**.
- Elle lance la **moulinette** sur les exercices de ce sous-groupe, **du plus simple au plus difficile**, et **s'arrête au premier échec**.
- Elle écrit un **rapport pédagogique** dans `workspace/.piscine/reports/`.

### Lire le rapport et recommencer

Ouvre le fichier `.md` indiqué : pour chaque exo tu as le **statut**, les **messages d'erreur** et une **correction-type commentée**. Corrige l'exo qui a échoué, puis **refais `git add` / `git commit` / `git push origin rendu/1.1`**. La moulinette repart et avance tant que les exos passent.

> 💡 **Pourquoi Git ?** Dans la vraie vie, on rend et on partage du code via Git. La Piscine t'y entraîne dès le premier exercice : `add` (je prépare), `commit` (j'enregistre une version), `push` (je publie — ici, je rends). Le détail complet est dans le **module 6 « Tests et Git »**.

## En résumé, la boucle de travail

1. Lis le chapitre de cours.
2. Fais l'exercice.
3. Rends-le : `submit-start`, puis `git add` / `git commit` / `git push origin rendu/<sous-groupe>`.
4. Lis le **rapport** de la moulinette, corrige, re-pousse.
5. Passe au chapitre suivant.
