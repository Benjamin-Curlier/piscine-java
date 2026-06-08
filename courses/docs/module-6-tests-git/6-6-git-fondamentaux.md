---
id: 6-6-git-fondamentaux
sidebar_position: 6
title: "Git — les fondamentaux"
description: "Versionner son code : init, les trois zones, add/commit/status/log/diff, .gitignore et l'anatomie d'un bon message de commit."
---

# Git — les fondamentaux

## Pourquoi ce chapitre

Comme pour les tests, vous avez déjà utilisé Git sans le savoir : à chaque rendu, la Piscine enregistrait votre travail avec `git add`, `git commit` et `git push`, puis déclenchait la moulinette. Ce chapitre lève le rideau sur cet outil. **Git** est un système de gestion de versions (en anglais *VCS*, *Version Control System*) : il garde l'historique complet de votre code, vous permet de revenir en arrière, et rend la collaboration possible. Vous allez apprendre à versionner un projet vous-même, commande par commande.

## Ce que vous saurez faire à la fin

- **Expliquer** ce qu'apporte un système de gestion de versions.
- **Initialiser** un dépôt avec `git init`.
- **Décrire** les trois zones (répertoire de travail, index, dépôt) et le rôle de `git add`.
- **Enchaîner** le cycle `add` / `commit` / `status` / `log` / `diff`.
- **Écrire** un `.gitignore` et un message de commit clair.

## 1. Pourquoi versionner

Sans gestion de versions, on sauvegarde des copies (`projet`, `projet-v2`, `projet-final`, `projet-final-vraiment`…) et on perd vite le fil. Git résout cela : il enregistre des **instantanés** successifs de votre projet, chacun avec un auteur, une date et un message. Vous pouvez consulter l'historique, comparer deux versions, revenir à n'importe quel état antérieur, et — on le verra aux chapitres suivants — travailler à plusieurs sans s'écraser mutuellement.

### À retenir

> - Git enregistre l'**historique** de votre code sous forme d'instantanés datés et signés.
> - Il remplace les copies manuelles (`projet-final-v3`) par un historique propre et navigable.

## 2. Les trois zones

C'est le concept clé de Git. Votre code passe par trois espaces :

```text
Répertoire de travail   →   Index (staging)   →   Dépôt (historique)
   (vos fichiers)            git add               git commit
```

- **Répertoire de travail** : les fichiers que vous éditez.
- **Index** (ou *staging area*, zone de préparation) : la liste des modifications que vous avez **sélectionnées** pour le prochain instantané, via `git add`.
- **Dépôt** : l'historique des instantanés validés, via `git commit`.

Cette zone intermédiaire (l'index) est ce qui déroute au début : `git add` ne sauvegarde rien dans l'historique, il **prépare** ce qui sera inclus au prochain `commit`. Elle permet de composer un commit cohérent, même si vous avez modifié plein de choses.

### À retenir

> - Trois zones : répertoire de travail → index (*staging*) → dépôt.
> - `git add` déplace une modification du travail vers l'index (elle est *préparée*, pas encore enregistrée).
> - `git commit` enregistre dans l'historique tout ce qui est dans l'index.

## 3. Le cycle de base

Voici la séquence que vous répéterez en permanence. On travaille sur un **dépôt-jouet** (jamais sur le dépôt de la Piscine).

### Exemple

```bash
mkdir mon-projet && cd mon-projet
git init                              # crée un dépôt Git vide
echo "Bonjour" > notes.txt            # on crée un fichier
git status                            # Git voit un fichier "non suivi"
git add notes.txt                     # on prépare le fichier (index)
git commit -m "Ajoute une note de bienvenue"
git log --oneline                     # on consulte l'historique
```

Sortie typique de `git status` juste après la création du fichier :

```text
On branch main
No commits yet

Untracked files:
  (use "git add <file>..." to include in what will be committed)
        notes.txt
```

Et de `git log --oneline` après le commit :

```text
a1b2c3d Ajoute une note de bienvenue
```

Les commandes à connaître :

| Commande | Rôle |
|---|---|
| `git status` | où en est-on ? (fichiers modifiés, préparés, non suivis) |
| `git add <fichier>` | préparer un fichier pour le prochain commit |
| `git commit -m "…"` | enregistrer un instantané avec un message |
| `git log` | afficher l'historique des commits |
| `git diff` | voir les modifications non encore préparées |

### À retenir

> - Le cycle de base : modifier → `git add` → `git commit -m "…"`.
> - `git status` est votre boussole : lancez-le souvent pour savoir où vous en êtes.
> - `git diff` montre ce qui a changé ; `git log` montre l'historique validé.

## 4. .gitignore et messages de commit

### Ignorer des fichiers

Certains fichiers ne doivent **jamais** entrer dans l'historique : résultats de compilation (`target/`, `*.class`), fichiers temporaires, secrets. On les liste dans un fichier nommé `.gitignore` à la racine du dépôt. Git ignore alors ces chemins.

```text
# .gitignore
target/
*.class
*.log
```

### Un bon message de commit

Le message décrit **ce que fait** le commit, à l'impératif présent, en une ligne concise. Il explique l'intention, pas le détail technique (que le `diff` montre déjà).

- ✅ `Corrige le calcul de moyenne sur un tableau vide`
- ❌ `fix`, ❌ `wip`, ❌ `modif du code`

### À retenir

> - `.gitignore` empêche de versionner les fichiers générés ou sensibles (`target/`, secrets).
> - Un bon message de commit est à l'impératif, concis, et dit **pourquoi** plus que comment.

## Erreurs fréquentes

- **`git commit` sans `git add`** : modifier un fichier puis lancer `commit` directement, et constater que rien n'est enregistré. Cause : le commit ne prend que ce qui est dans l'index. Correction : faites `git add` d'abord (ou `git commit -a` pour préparer-et-valider les fichiers déjà suivis), et vérifiez avec `git status`.

- **Versionner `target/` ou des secrets** : commiter tout le dossier sans `.gitignore`. Cause : `git add .` ratisse tout. Correction : créez un `.gitignore` **avant** le premier commit, listant `target/`, les logs et les fichiers sensibles.

- **Messages de commit illisibles** : `fix`, `wip`, `truc`. Cause : on commite vite, sans réfléchir au message. Correction : une ligne impérative qui décrit l'intention — votre futur vous (et vos collègues) vous remercieront en relisant l'historique.

## Exercice guidé

**Objectif** : créer un petit dépôt et enregistrer deux instantanés propres.

Vous allez versionner une « liste de courses » dans un dépôt-jouet, en deux commits.

**Pas à pas :**

**Étape 1** — Créez un dossier `courses-demo`, entrez dedans, initialisez le dépôt.

**Étape 2** — Créez un fichier `liste.txt` contenant `pain`, ajoutez-le et commitez (« Ajoute le pain »).

**Étape 3** — Ajoutez `lait` au fichier, puis faites un second commit (« Ajoute le lait »).

**Étape 4** — Consultez l'historique avec `git log --oneline` : vous devez voir deux commits.

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

```bash
mkdir courses-demo && cd courses-demo
git init

echo "pain" > liste.txt
git add liste.txt
git commit -m "Ajoute le pain à la liste"

echo "lait" >> liste.txt          # >> ajoute une ligne sans écraser
git add liste.txt
git commit -m "Ajoute le lait à la liste"

git log --oneline
```

Sortie attendue de `git log --oneline` (les identifiants varieront) :

```text
f4e5d6c Ajoute le lait à la liste
a1b2c3d Ajoute le pain à la liste
```

Points à noter :
- Chaque modification suit le cycle `add` → `commit` : c'est un instantané cohérent à chaque fois.
- `>>` ajoute au fichier, `>` l'écrase — une confusion classique à éviter.
- Le commit le plus récent apparaît **en haut** de `git log`.
- Deux commits courts et bien nommés valent mieux qu'un seul commit fourre-tout : l'historique raconte une histoire lisible.

</details>

## Vérifiez vos acquis

- Que gagne-t-on à utiliser Git plutôt que des copies manuelles de dossiers ?
- Citez les trois zones de Git et la commande qui fait passer de la première à la deuxième.
- Pourquoi `git commit` n'enregistre-t-il rien si vous n'avez pas fait `git add` ?
- À quoi sert un fichier `.gitignore` ? Donnez deux exemples de ce qu'on y met.
- Qu'est-ce qui caractérise un bon message de commit ?

## Pour aller plus loin

- [Pro Git — Les bases de Git](https://git-scm.com/book/fr/v2/Les-bases-de-Git-Enregistrer-des-modifications-dans-le-d%C3%A9p%C3%B4t) (version française) — le chapitre de référence sur `add`, `commit`, `status`, `log`.
- [Git Cheat Sheet](https://education.github.com/git-cheat-sheet-education.pdf) (GitHub) — l'aide-mémoire des commandes courantes, à garder sous la main.

## Prochain chapitre

→ **[Chapitre 6-7 — Git : branches et collaboration](6-7-git-branches-collaboration)**
