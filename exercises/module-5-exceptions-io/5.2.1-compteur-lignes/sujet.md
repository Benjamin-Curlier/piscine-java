# Exercice 5.2.1 — Compteur de lignes (Files.lines + try-with-resources)

## Contexte

Un outil d'analyse de texte doit pouvoir répondre à deux questions simples sur
un fichier : combien de lignes contient-il en tout, et combien de ces lignes
sont réellement remplies (non vides, non composées uniquement d'espaces) ?

Ces deux comptages se font naturellement avec les API NIO.2 de Java : on ouvre
le fichier en **flux de lignes** (`Files.lines`), on applique les opérations de
stream nécessaires, puis on ferme le flux. La fermeture doit être **garantie**
même en cas d'erreur — c'est le rôle du `try-with-resources`.

## Énoncé

Complétez la classe `CompteurLignes` avec les deux méthodes statiques suivantes :

```java
public static long compterLignes(Path fichier) throws IOException
```

Renvoie le nombre **total** de lignes du fichier (y compris les lignes vides
et les lignes blanches composées uniquement d'espaces ou de tabulations).

```java
public static long compterLignesNonVides(Path fichier) throws IOException
```

Renvoie le nombre de lignes **non blanches** — c'est-à-dire dont
`String.isBlank()` renvoie `false`.

Les deux méthodes doivent utiliser `Files.lines(fichier, StandardCharsets.UTF_8)`
à l'intérieur d'un `try-with-resources` (le `Stream<String>` est une ressource
`AutoCloseable` et doit être fermé après usage).

## Exemples

```text
Fichier "rapport.txt" contenant exactement :
  Ligne 1 : "Section Alpha"
  Ligne 2 : ""              (ligne vide)
  Ligne 3 : "   "           (ligne blanche — espaces seulement)
  Ligne 4 : "Section Bravo"

compterLignes("rapport.txt")        → 4
compterLignesNonVides("rapport.txt") → 2
```

```text
Fichier vide :
compterLignes(fichierVide)        → 0
compterLignesNonVides(fichierVide) → 0
```

## Contraintes

- Package `piscine.m5`. **Ne modifiez pas** les signatures des méthodes.
- NIO.2 obligatoire : `Files.lines`, `java.nio.file.Path`, `java.nio.file.Files`.
- `Files.lines` doit être dans un **`try-with-resources`** (le flux doit être
  fermé même si une exception survient pendant le traitement).
- **UTF-8 explicite** : passez `StandardCharsets.UTF_8` à chaque appel de
  `Files.lines`.
- Les méthodes propagent `throws IOException` (ne pas absorber l'exception).
- Aucune boucle manuelle (`for`, `while`) — les opérations de stream suffisent.

## Ce qui sera vérifié

- Fichier de 3 lignes → `compterLignes` renvoie 3, `compterLignesNonVides` 3.
- Fichier vide → 0 et 0.
- Lignes vides intercalées → total correct, non-vides correct.
- Ligne unique sans saut de ligne final → 1.
- Lignes composées d'espaces ou de tabulations → comptées par `compterLignes`,
  ignorées par `compterLignesNonVides`.
- Fichier avec caractères accentués (UTF-8) → lecture correcte.
- Fichier de 50 lignes → 50.
- Présence ou absence du saut de ligne final n'affecte pas le résultat.

## Pour aller plus loin (optionnel — non noté)

- Pourquoi `Files.lines` renvoie-t-il un `Stream<String>` plutôt qu'une
  `List<String>` ? Quel avantage pour les très grands fichiers ?
- Que se passerait-il si vous oubliiez le `try-with-resources` et que le
  fichier restait ouvert ? (Descripteur de fichier perdu, ressource bloquée.)
- Comment compteriez-vous les lignes commençant par un caractère précis ?
