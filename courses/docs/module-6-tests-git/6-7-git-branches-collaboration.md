---
id: 6-7-git-branches-collaboration
sidebar_position: 7
title: "Git — branches et collaboration"
description: "Travailler en parallèle avec des branches, fusionner (merge), survol du rebase, et résoudre un conflit."
---

# Git — branches et collaboration

## Pourquoi ce chapitre

Avec le cycle de base, vous savez enregistrer l'historique d'un projet, seul, en ligne droite. Mais le vrai travail se fait à plusieurs et en parallèle : plusieurs fonctionnalités en cours en même temps, sans que l'une casse l'autre. C'est le rôle des **branches**. Vous apprendrez à créer une branche, à la fusionner (`merge`), à situer le `rebase`, et surtout à **résoudre un conflit** — l'étape qui inquiète tout le monde au début, et qui est en réalité simple et réparable.

## Ce que vous saurez faire à la fin

- **Expliquer** pourquoi on travaille sur des branches plutôt que directement sur `main`.
- **Créer** une branche avec `git switch -c` et y basculer.
- **Fusionner** une branche dans une autre avec `git merge`.
- **Situer** le `rebase` et savoir quand l'éviter.
- **Résoudre** un conflit de fusion en lisant et en corrigeant les marqueurs.
- **Récupérer** le travail d'un collègue avec `git fetch` et `git pull`.
- **Abandonner** une fusion en conflit avec `git merge --abort`, et **annuler** des changements locaux avec `git restore` / `git reset`.

## 1. Pourquoi des branches

Une **branche** est une ligne de développement indépendante. Par convention, la branche principale s'appelle `main` : elle contient la version de référence, censée toujours fonctionner. Pour développer une fonctionnalité, on crée une branche dédiée, on y travaille tranquillement, et on ne ramène le résultat dans `main` que lorsqu'il est prêt. Pendant ce temps, `main` reste intact, et un collègue peut travailler sur sa propre branche sans vous gêner.

### À retenir

> - Une branche est une ligne de développement **indépendante**.
> - `main` est la branche de référence ; on développe sur des branches dédiées et on fusionne ensuite.
> - Les branches permettent de travailler en parallèle sans se marcher dessus.

## 2. Créer et changer de branche

`git branch` liste les branches. `git switch -c <nom>` crée une nouvelle branche **et** y bascule (le `-c` signifie *create*). Pour revenir sur une branche existante, `git switch <nom>`.

```bash
git switch -c feature/ajoute-titre   # crée la branche et s'y place
# ... on travaille, on commite sur cette branche ...
git switch main                      # revient sur main
```

> Note : vous verrez souvent `git checkout` dans d'anciens tutoriels (`git checkout -b`). C'est l'ancienne commande, encore valide, mais `git switch` est plus claire car dédiée au changement de branche.

### À retenir

> - `git switch -c <nom>` crée une branche et s'y place ; `git switch <nom>` y bascule.
> - `git branch` liste les branches existantes.
> - `git switch` remplace avantageusement l'ancien `git checkout` pour changer de branche.

## 3. Fusionner avec merge

Quand le travail d'une branche est prêt, on le ramène dans `main` avec `git merge`. On se place **sur la branche de destination** (`main`), puis on fusionne la branche source.

```bash
git switch main
git merge feature/ajoute-titre
```

Deux cas se présentent :
- **Fast-forward** : si `main` n'a pas bougé depuis la création de la branche, Git avance simplement le pointeur — pas de commit supplémentaire.
- **Commit de merge** : si `main` a avancé en parallèle, Git crée un commit de fusion qui réunit les deux histoires. Si les deux branches ont modifié les **mêmes lignes**, un **conflit** survient (section 4).

### À retenir

> - On fusionne depuis la branche de **destination** : `git switch main` puis `git merge <source>`.
> - *Fast-forward* : avance simple, sans commit de fusion. Sinon, Git crée un commit de merge.
> - Un conflit n'apparaît que si les deux branches ont touché les **mêmes lignes**.

## 4. Résoudre un conflit

Un conflit, ce n'est pas une erreur : c'est Git qui vous dit « deux versions de la même ligne existent, choisissez ». Il insère des **marqueurs** dans le fichier concerné. À vous de garder la bonne version (ou de combiner les deux), de supprimer les marqueurs, puis de conclure par `git add` + `git commit`.

### Exemple

On provoque un conflit en éditant la même ligne sur deux branches :

```bash
git switch -c feature/titre
echo "Titre B" > README.md
git commit -am "Change le titre (branche)"
git switch main
echo "Titre A" > README.md
git commit -am "Change le titre (main)"
git merge feature/titre        # CONFLIT sur README.md
```

Git modifie `README.md` ainsi :

```diff
<<<<<<< HEAD
Titre A
=======
Titre B
>>>>>>> feature/titre
```

Lecture des marqueurs :
- entre `<<<<<<< HEAD` et `=======` : la version de la branche courante (`main`, « Titre A ») ;
- entre `=======` et `>>>>>>> feature/titre` : la version de la branche que vous **fusionnez** (`feature/titre`, « Titre B »). Rien n'est encore fusionné — c'est à vous de choisir.

Pour résoudre : éditez le fichier pour ne garder que ce que vous voulez (par exemple « Titre A »), **supprimez les trois lignes de marqueurs**, puis :

```bash
git add README.md
git commit            # conclut la fusion
```

### À retenir

> - Un conflit est normal : Git vous demande de **choisir** entre deux versions d'une ligne.
> - Éditez le fichier, gardez la bonne version, **supprimez les marqueurs** `<<<<<<<` `=======` `>>>>>>>`.
> - Concluez la résolution par `git add` puis `git commit`.

## 4bis. Un mot sur le rebase

`git rebase` est une alternative au `merge` : au lieu de créer un commit de fusion, il **rejoue** vos commits par-dessus une autre branche, donnant un historique linéaire. C'est élégant, mais c'est un outil à manier avec prudence : **ne rebasez jamais une branche déjà partagée** (poussée et utilisée par d'autres), car cela réécrit l'historique commun et sème la confusion. Au stade débutant, le `merge` suffit ; gardez le `rebase` pour plus tard.

### À retenir

> - `rebase` rejoue vos commits pour un historique linéaire — puissant mais délicat.
> - **Ne rebasez jamais du code déjà partagé.** En cas de doute, utilisez `merge`.

## 5. Récupérer le travail des autres : fetch et pull

Jusqu'ici vous savez **envoyer** votre travail. Mais quand un collègue pousse le sien sur le dépôt distant, comment le **récupérer** ? Deux commandes :

- **`git fetch`** télécharge les nouveautés du distant **sans rien changer** à vos fichiers. Votre branche locale n'avance pas ; vous récupérez juste de quoi voir ce qui a changé (par exemple via `git log origin/main`). C'est l'option prudente : on regarde avant d'intégrer.
- **`git pull`** fait `fetch` **puis** `merge` dans la foulée : il télécharge les nouveautés **et** les fusionne dans votre branche courante. C'est le raccourci du quotidien quand on veut simplement se mettre à jour.

```bash
git fetch                 # télécharge, sans toucher à votre travail
git switch main
git pull                  # = fetch + merge : met main à jour avec le distant
```

Comme `git pull` enchaîne un `merge`, il peut déclencher un **conflit** exactement comme à la section 4 — vous le résolvez de la même façon. Réflexe sain : `git pull` **avant** de commencer à travailler, pour partir de la dernière version et limiter les conflits.

### À retenir

> - `git fetch` télécharge les nouveautés du distant **sans** modifier vos fichiers.
> - `git pull` = `git fetch` + `git merge` : télécharge **et** fusionne dans la branche courante.
> - Faites un `git pull` avant de vous lancer, pour partir à jour.

## 6. Faire marche arrière : abort, restore, reset

Tout est réparable en Git. Trois filets de sécurité à connaître.

**Abandonner une fusion en conflit.** Un `merge` (ou un `pull`) a déclenché un conflit et vous préférez tout annuler pour repartir à zéro ? `git merge --abort` remet la branche **exactement dans l'état d'avant la fusion**, comme si vous n'aviez rien lancé.

```bash
git merge feature/titre   # ... CONFLIT, finalement on renonce
git merge --abort         # retour à l'état d'avant le merge
```

**Annuler des modifications non commitées.** `git restore <fichier>` **jette** les changements d'un fichier dans le répertoire de travail et le ramène à sa dernière version commitée. Attention : ces changements sont **définitivement perdus** (ils n'étaient pas commités).

```bash
git restore README.md     # README.md revient à sa version commitée
git restore .             # idem pour tous les fichiers (à manier avec prudence)
```

**Désindexer un fichier (sans perdre le travail).** Vous avez fait `git add` d'un fichier que vous ne vouliez pas inclure dans le prochain commit ? `git restore --staged <fichier>` (ou `git reset <fichier>`) le **retire de la zone d'index** tout en **gardant vos modifications** dans le fichier.

```bash
git add .
git restore --staged secret.txt   # secret.txt sort de l'index, vos changements restent
```

`git reset` est une commande plus large (elle peut aussi déplacer la branche, annuler des commits). À ce stade, retenez surtout son usage le plus courant et inoffensif : **désindexer**. Réservez les variantes plus puissantes (`git reset --hard`, qui efface des changements) pour quand vous serez plus à l'aise — elles peuvent détruire du travail.

### À retenir

> - `git merge --abort` annule une fusion en conflit et restaure l'état d'avant.
> - `git restore <fichier>` jette les modifications non commitées d'un fichier (**définitif**).
> - `git restore --staged <fichier>` (ou `git reset <fichier>`) **désindexe** sans rien perdre.
> - `git reset --hard` est puissant **et** destructeur : à éviter tant qu'on débute.

## Erreurs fréquentes

- **Travailler directement sur `main`** : commiter ses expérimentations sur la branche de référence. Cause : on ne pense pas à créer une branche. Correction : créez une branche dédiée (`git switch -c feature/...`) ; `main` doit rester stable et fonctionnel.

- **Paniquer devant un conflit** : annuler tout, recopier les fichiers à la main. Cause : les marqueurs impressionnent. Correction : un conflit est local et réparable — lisez les marqueurs, choisissez, supprimez-les, `add` + `commit`. Rien n'est perdu.

- **Rebaser une branche partagée** : faire `git rebase` sur une branche que des collègues utilisent déjà. Cause : on cherche un historique « propre ». Correction : réservez le `rebase` aux branches locales non partagées ; sinon, le `merge` est sans danger.

- **Confondre `git restore` et `git restore --staged`** : croire que `git restore <fichier>` ne fait que désindexer, et perdre ainsi ses modifications. Cause : les deux se ressemblent. Correction : `git restore <fichier>` **jette** les changements du fichier (définitif) ; `git restore --staged <fichier>` se contente de **désindexer** sans rien perdre.

- **Dégainer `git reset --hard` pour « tout nettoyer »** : l'utiliser sans comprendre qu'il efface les modifications non commitées. Cause : on cherche une commande magique de remise à zéro. Correction : tant qu'on débute, préférez `git restore` (pour un fichier) ou `git merge --abort` (pour une fusion) ; gardez `--hard` pour plus tard.

## Exercice guidé

**Objectif** : provoquer un conflit volontairement, puis le résoudre proprement.

Vous allez créer un conflit sur un fichier `README.md` et le résoudre.

**Pas à pas :**

**Étape 1** — Dans un dépôt-jouet avec un premier commit, créez une branche `feature/desc` et modifiez la première ligne de `README.md`, puis commitez.

**Étape 2** — Revenez sur `main`, modifiez **la même ligne** différemment, commitez.

**Étape 3** — Fusionnez `feature/desc` dans `main`. Observez le conflit.

**Étape 4** — Résolvez : gardez la version de votre choix, supprimez les marqueurs, `add` + `commit`.

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

```bash
# Préparation : un dépôt-jouet avec un README initial
mkdir conflit-demo && cd conflit-demo
git init
echo "Description initiale" > README.md
git add README.md
git commit -m "Premier README"

# Étape 1 : une branche modifie la ligne
git switch -c feature/desc
echo "Description côté branche" > README.md
git commit -am "Réécrit la description (branche)"

# Étape 2 : main modifie la même ligne autrement
git switch main
echo "Description côté main" > README.md
git commit -am "Réécrit la description (main)"

# Étape 3 : la fusion déclenche un conflit
git merge feature/desc
```

Git affiche un message du type :

```text
Auto-merging README.md
CONFLICT (content): Merge conflict in README.md
Automatic merge failed; fix conflicts and then commit the result.
```

Le fichier `README.md` contient alors :

```diff
<<<<<<< HEAD
Description côté main
=======
Description côté branche
>>>>>>> feature/desc
```

**Étape 4 — résolution.** On édite `README.md` pour ne garder qu'une version (par exemple en combinant) et on supprime les marqueurs :

```text
Description fusionnée : côté main et côté branche réunis
```

Puis :

```bash
git add README.md
git commit -m "Fusionne les descriptions, résout le conflit"
```

Points à noter :
- Le conflit n'a touché que le fichier modifié des deux côtés sur la même ligne.
- La résolution est entièrement manuelle : **vous** décidez du contenu final.
- Oublier de supprimer une ligne de marqueur (`=======` par exemple) laisserait un fichier invalide — relisez avant de commiter.

</details>

## Vérifiez vos acquis

- Pourquoi crée-t-on une branche au lieu de travailler directement sur `main` ?
- Quelle commande crée une branche et s'y place en une fois ?
- Sur quelle branche doit-on se trouver pour lancer `git merge` ?
- Que signifient les marqueurs `<<<<<<<`, `=======` et `>>>>>>>` dans un fichier en conflit ?
- Dans quel cas faut-il absolument éviter `git rebase` ?
- Quelle est la différence entre `git fetch` et `git pull` ?
- Comment abandonner une fusion qui a déclenché un conflit ?
- Quelle commande désindexe un fichier sans perdre vos modifications ?

## Pour aller plus loin

- [Pro Git — Les branches avec Git](https://git-scm.com/book/fr/v2/Les-branches-avec-Git-Brancher-et-fusionner) (version française) — branches, fusion et résolution de conflits, en détail.
- [Merging vs. Rebasing](https://www.atlassian.com/git/tutorials/merging-vs-rebasing) (Atlassian) — comparatif visuel clair entre les deux approches.

## Prochain chapitre

→ **[Chapitre 6-8 — Git : le workflow Pull Request](6-8-git-workflow-pr)**
