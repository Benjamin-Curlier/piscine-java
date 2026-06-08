# Correction — 6.2.2 Résolution de conflit

## Démarche attendue

Résoudre un conflit, c'est trois gestes : **lire** le fichier conflictuel, **choisir** quoi
garder en **supprimant les marqueurs**, puis **conclure** la fusion (`add` + `commit`). Ici,
on conserve les deux contributions : il suffit de retirer les trois lignes de marqueurs.

## Solution de référence

```java
public void resoudreConflit(GitCommandes git) throws IOException {
    Path readme = git.repo().resolve("README.md");
    List<String> resolu = new ArrayList<>();
    for (String ligne : Files.readAllLines(readme, StandardCharsets.UTF_8)) {
        if (ligne.startsWith("<<<<<<<") || ligne.startsWith("=======") || ligne.startsWith(">>>>>>>")) {
            continue;
        }
        resolu.add(ligne);
    }
    Files.write(readme, resolu, StandardCharsets.UTF_8);
    git.run("add", "README.md");
    git.run("commit", "-m", "Résout le conflit de fusion");
}
```

## Points clés

- Les trois marqueurs : `<<<<<<<` (début, version courante), `=======` (séparateur),
  `>>>>>>>` (fin, version entrante). On les retire **tous les trois**.
- Conserver les deux contributions = garder les lignes `- import` (côté HEAD) et `- export`
  (côté feature).
- Le `git commit` après résolution crée un **commit de merge** à deux parents (la fusion en
  cours, signalée par `MERGE_HEAD`, est alors conclue).

## Erreurs fréquentes observées

- Oublier une ligne de marqueur (souvent le `=======`) → le fichier reste invalide.
- Ne garder qu'une seule contribution → l'autre branche est perdue.
- Résoudre le fichier mais oublier `git add` + `git commit` → la fusion n'est pas conclue
  (le dépôt reste en état de conflit).
