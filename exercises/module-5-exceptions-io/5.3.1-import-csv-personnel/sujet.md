# Exercice 5.3.1 — Import CSV de personnel (split + records + exceptions chaînées)

## Contexte

Une unité tient à jour la liste de son personnel dans un fichier **CSV** : une
ligne d'en-tête, puis une ligne par militaire, avec trois informations séparées
par des virgules. Vous devez écrire l'**import** de ce fichier vers des objets
Java exploitables — et, surtout, **détecter proprement les lignes invalides**.

C'est le point de jonction du module : on **lit un fichier** (NIO.2), on
**structure du texte** en CSV, et on **lève des exceptions** dès qu'une donnée
n'a pas le format attendu. La difficulté n'est pas le découpage en lui-même,
mais la **rigueur de la validation** et le **chaînage de la cause** d'origine.

## Modèle de domaine fourni

Deux fichiers vous sont **fournis — ne les modifiez pas** :

```java
public enum Grade { SOLDAT, CAPORAL, SERGENT, ADJUDANT, LIEUTENANT }

public record Personnel(String nom, Grade grade, int anciennete) { }
```

`Grade` est l'ensemble fermé des grades reconnus (sensible à la casse).
`Personnel` est un `record` : son `equals` est généré, donc deux `Personnel`
ayant les mêmes champs sont égaux (pratique pour les tests).

## Format du fichier CSV

```text
nom,grade,anciennete
Durand,SERGENT,5
Martin,CAPORAL,2
Petit,LIEUTENANT,12
```

- La **1re ligne** est l'en-tête `nom,grade,anciennete` : elle est **sautée**.
- Chaque ligne de données contient **exactement 3 champs** séparés par des
  virgules. CSV **simple** : aucun champ ne contient de virgule.
- Les **lignes blanches** (vides ou faites d'espaces) sont **ignorées**.

## Énoncé

Complétez la classe `ImportCsv` avec la méthode statique suivante :

```java
public static List<Personnel> importer(Path csv) throws IOException
```

Elle lit le fichier avec `Files.readAllLines(csv, StandardCharsets.UTF_8)`, saute
l'en-tête, ignore les lignes blanches, puis pour chaque ligne de données :

1. découpe la ligne avec `ligne.split(",")` ;
2. si le nombre de champs n'est **pas 3** → lève
   `IllegalArgumentException("ligne CSV invalide (3 champs attendus) : " + ligne)` ;
3. convertit le grade avec `Grade.valueOf(champs[1].trim())` ; si la constante
   n'existe pas (échec → `IllegalArgumentException`), **rattrape** et relève
   `IllegalArgumentException("grade inconnu : ... (ligne : ...)", e)` en
   **chaînant** la cause `e` ;
4. convertit l'ancienneté avec `Integer.parseInt(champs[2].trim())` ; si la
   valeur n'est pas un entier (échec → `NumberFormatException`), **rattrape** et
   relève `IllegalArgumentException("anciennete invalide : ... (ligne : ...)", e)`
   en **chaînant** la cause `e` ;
5. ajoute `new Personnel(champs[0].trim(), grade, anciennete)` à la liste.

Renvoie la liste, **dans l'ordre du fichier**.

## Exemples

```text
nom,grade,anciennete
Durand,SERGENT,5
Martin,CAPORAL,2

importer(...) → [Personnel[Durand, SERGENT, 5], Personnel[Martin, CAPORAL, 2]]
```

```text
nom,grade,anciennete
Dupont,GENERAL,5          → IllegalArgumentException (grade inconnu : GENERAL)

nom,grade,anciennete
Dupont,SERGENT,abc        → IllegalArgumentException (anciennete invalide : abc)

nom,grade,anciennete
Dupont,SERGENT            → IllegalArgumentException (ligne CSV invalide)
```

## Contraintes

- Package `etnc.m5`. **Ne modifiez pas** la signature de `importer`, ni les
  fichiers `Grade.java` / `Personnel.java`.
- Lecture en **NIO.2** : `Files.readAllLines` avec `StandardCharsets.UTF_8`
  explicite.
- Découpage **à la main** : `split(",")` (aucune bibliothèque CSV).
- **Toutes** les lignes de données invalides lèvent `IllegalArgumentException` ;
  pour le grade et l'ancienneté, la **cause d'origine doit être chaînée**
  (2e argument du constructeur `IllegalArgumentException(message, cause)`).
- Nettoyez chaque champ avec `trim()`.
- Imports **explicites** (jamais `import ...*`).

## Ce qui sera vérifié

- CSV valide de 3 lignes → les 3 `Personnel` attendus, dans l'ordre.
- En-tête sauté ; lignes blanches ignorées ; fichier en-tête seul → liste vide.
- Grade inconnu → `IllegalArgumentException` ; cause = celle de `Grade.valueOf`.
- Ancienneté non numérique → `IllegalArgumentException` ; cause = `NumberFormatException`.
- Nombre de champs ≠ 3 (2 ou 4) → `IllegalArgumentException`.
- Le message contient l'information fautive (grade, valeur, ou ligne complète).
- Champs entourés d'espaces nettoyés ; accents UTF-8 préservés ; ancienneté
  négative acceptée.

## Pour aller plus loin (optionnel — non noté)

- Pourquoi **chaîner** la cause plutôt que la perdre ? (À la lecture du
  `stack trace`, on garde l'origine exacte : `NumberFormat: For input string`.)
- Pourquoi valider le **nombre de champs avant** de convertir ? (On évite un
  `ArrayIndexOutOfBoundsException` en accédant à `champs[2]`.)
- Comment gérer un champ contenant une virgule (« Durand, Jr ») ? C'est la
  limite du CSV « simple » — les vrais parseurs CSV gèrent les guillemets.
