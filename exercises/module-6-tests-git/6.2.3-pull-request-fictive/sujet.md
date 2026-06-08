# Exercice 6.2.3 — Pull Request fictive

## Contexte

Une **Pull Request** (PR) propose de fusionner une branche dans `main`, en ouvrant une
discussion et une revue. Sans forge (GitHub/GitLab) à disposition, on en **simule le cycle en
local** : une branche, un commit, une fusion dans `main`, et un fichier `PULL_REQUEST.md` qui
joue le rôle de la description de PR (titre, description, checklist de revue).

Vous pilotez Git depuis Java via `GitCommandes`. Le dépôt-jouet a déjà un commit de départ
sur `main`.

## Énoncé

Complétez `preparerPullRequest(GitCommandes git)` :

1. Créer et basculer sur une branche **`feature/ajoute-licence`** (`git switch -c ...`).
2. Créer un fichier `LICENSE` (contenu libre), puis `add` + `commit` avec le message
   **« Ajoute le fichier LICENSE »**.
3. Revenir sur `main` (`git switch main`) et **fusionner** la branche (`git merge feature/ajoute-licence`).
4. Écrire un fichier **`PULL_REQUEST.md`** contenant au minimum :
   - un titre (`# ...`),
   - une section **`## Description`**,
   - une section **`## Checklist de revue`** avec quelques cases `- [ ]`.

## Exemple de `PULL_REQUEST.md`

```markdown
# Ajoute le fichier LICENSE

## Description
Ajoute un fichier LICENSE à la racine pour clarifier les droits d'usage.

## Checklist de revue
- [ ] Le fichier LICENSE est présent
- [ ] Le titre décrit le changement
```

## Contraintes

- La branche est créée, reçoit le commit, puis est **fusionnée dans `main`**.
- À la fin, vous êtes revenu sur `main`, et `LICENSE` y est présent.
- `PULL_REQUEST.md` contient les sections `## Description` et `## Checklist de revue`.

## Ce qui sera vérifié

- Vous êtes sur `main` à la fin, et `LICENSE` y est présent (branche fusionnée).
- L'historique de `main` contient le commit « Ajoute le fichier LICENSE ».
- `PULL_REQUEST.md` existe et contient les sections attendues.

## Pour aller plus loin (optionnel)

- Sur une vraie forge, ouvrez une PR à partir d'une branche poussée pour comparer.
