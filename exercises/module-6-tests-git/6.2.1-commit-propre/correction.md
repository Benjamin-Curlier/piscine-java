# Correction — 6.2.1 Commit propre

## Démarche attendue

Le cycle de base de Git : on modifie le répertoire de travail, on **prépare** ce qu'on veut
enregistrer (`add`), puis on **valide** un instantané (`commit`). Faire un commit **atomique**
par fichier rend l'historique lisible : chaque commit raconte un changement cohérent.

## Solution de référence

```java
public void creerHistorique(GitCommandes git) throws IOException {
    Files.writeString(git.repo().resolve("notes.txt"), "première note", StandardCharsets.UTF_8);
    git.run("add", "notes.txt");
    git.run("commit", "-m", "Ajoute les notes");

    Files.writeString(git.repo().resolve("liste.txt"), "première tâche", StandardCharsets.UTF_8);
    git.run("add", "liste.txt");
    git.run("commit", "-m", "Ajoute la liste");
}
```

## Points clés

- `git.repo()` donne le répertoire du dépôt : on y crée les fichiers avec `Files.writeString`.
- `add` puis `commit` : l'ordre compte (rien n'est enregistré sans `add` préalable).
- Deux commits, un par fichier : c'est ce que vérifient `git log --oneline` et
  `git show --name-only HEAD~1` / `HEAD`.

## Erreurs fréquentes observées

- Ajouter les deux fichiers puis faire **un seul** commit → 1 commit au lieu de 2.
- `git add .` au lieu de cibler le fichier → les deux fichiers entrent dans le premier commit.
- Messages vides ou « wip » → ne respectent pas la consigne (critère formateur).
- Oublier `add` avant `commit` → `commit` n'enregistre rien (et lèvera ici une erreur).
