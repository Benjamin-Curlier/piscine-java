# Correction — 6.2.3 Pull Request fictive

## Démarche attendue

Le cycle d'une PR, reproduit en local : on isole le travail sur une **branche**, on y
**commite**, on **fusionne** dans `main`, et on **décrit** le changement (le `PULL_REQUEST.md`
tient lieu de description de PR). C'est exactement le workflow utilisé pour construire cette
Piscine.

## Solution de référence

```java
public void preparerPullRequest(GitCommandes git) throws IOException {
    git.run("switch", "-c", "feature/ajoute-licence");
    Files.writeString(git.repo().resolve("LICENSE"),
        "Licence MIT — Piscine Java\n", StandardCharsets.UTF_8);
    git.run("add", "LICENSE");
    git.run("commit", "-m", "Ajoute le fichier LICENSE");

    git.run("switch", "main");
    git.run("merge", "feature/ajoute-licence");

    Files.writeString(git.repo().resolve("PULL_REQUEST.md"), """
        # Ajoute le fichier LICENSE

        ## Description
        Ajoute un fichier `LICENSE` à la racine du dépôt pour clarifier les droits d'usage.

        ## Checklist de revue
        - [ ] Le fichier LICENSE est présent et lisible
        - [ ] Le titre de la PR décrit le changement
        """, StandardCharsets.UTF_8);
}
```

## Points clés

- `git switch -c feature/...` crée la branche et s'y place ; on y travaille sans toucher `main`.
- Le `merge` depuis `main` intègre le commit de la branche (ici en *fast-forward*, car `main`
  n'a pas bougé).
- Une bonne description de PR répond à « quoi » **et** « pourquoi », et propose une checklist
  de revue.

## Erreurs fréquentes observées

- Travailler directement sur `main` (oublier `switch -c`) → pas de branche à fusionner.
- Oublier de revenir sur `main` avant le `merge` → la fusion se fait dans la mauvaise branche.
- `PULL_REQUEST.md` sans les sections attendues (`## Description`, `## Checklist de revue`).
