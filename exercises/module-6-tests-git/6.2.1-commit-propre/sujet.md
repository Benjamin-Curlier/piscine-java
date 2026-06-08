# Exercice 6.2.1 — Commit propre

## Contexte

On ne « rend » pas un exercice Git en affichant quelque chose : on **transforme l'état d'un
dépôt**. Ici, vous pilotez Git depuis Java grâce à un utilitaire fourni, `GitCommandes`, qui
exécute les commandes exactement comme au terminal :

```java
git.run("add", "notes.txt");
git.run("commit", "-m", "Ajoute les notes");
```

Vos opérations sont évaluées sur l'**état final du dépôt** (nombre de commits, contenu,
messages) — dans un dépôt-jouet isolé, jamais le dépôt de la Piscine.

## Énoncé

Complétez la méthode `creerHistorique(GitCommandes git)` de la classe `CommitPropre`. Le
dépôt est déjà initialisé (un `git init` et une identité git sont configurés). Vous devez
produire **deux commits atomiques** (un fichier par commit) :

1. Créer `notes.txt` (contenu « première note »), puis `add` + `commit` avec le message
   **« Ajoute les notes »**.
2. Créer `liste.txt` (contenu « première tâche »), puis `add` + `commit` avec le message
   **« Ajoute la liste »**.

Pour créer un fichier dans le dépôt, utilisez `Files.writeString(git.repo().resolve("..."), ...)`.

## Exemple

Après votre méthode, `git log --oneline` doit montrer deux commits :

```text
b2c3d4e Ajoute la liste
a1b2c3d Ajoute les notes
```

## Contraintes

- **Deux commits**, un par fichier (commits atomiques) — pas un seul commit groupé.
- Messages à l'impératif, non vides (pas de « wip » ni « modif »).
- N'utilisez que `git.run(...)` et `Files.writeString(...)` ; ne modifiez pas `GitCommandes`.

## Ce qui sera vérifié

- Le dépôt contient exactement **2 commits**, les deux fichiers existent.
- Chaque commit est **atomique** : `notes.txt` dans le premier, `liste.txt` dans le second.
- Les messages de commit attendus sont présents ; l'arbre de travail est propre à la fin.

## Pour aller plus loin (optionnel)

- Lancez `git log` au terminal sur un vrai dépôt pour comparer avec ce que produit votre code.
