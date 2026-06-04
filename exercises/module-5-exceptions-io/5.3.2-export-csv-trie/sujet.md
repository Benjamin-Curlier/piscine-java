# Exercice 5.3.2 — Export CSV trié (Comparator + String.join + Files.write)

## Contexte

Le service des ressources humaines militaires doit générer des fichiers CSV à
partir de listes de personnels, **triés par ancienneté décroissante** (les plus
anciens en premier) puis par **nom alphabétique** pour égalité. Ces fichiers
servent à produire des tableaux d'affichage et doivent rester lisibles dans
n'importe quel tableur.

Vous avez à disposition le modèle fourni :

```java
public enum Grade { SOLDAT, CAPORAL, SERGENT, ADJUDANT, LIEUTENANT }
public record Personnel(String nom, Grade grade, int anciennete) { }
```

Ces deux classes sont **fournies complètes** dans le starter — ne les modifiez pas.

## Énoncé

Complétez la méthode statique de la classe `ExportCsv` :

```java
public static void exporter(List<Personnel> personnels, Path csv) throws IOException
```

Cette méthode doit :

1. **Trier une copie** de la liste par ancienneté décroissante, puis par nom
   alphabétique croissant pour les égalités d'ancienneté. La liste source
   **ne doit pas être modifiée**.

2. **Construire la liste des lignes** CSV :
   - La première ligne est toujours l'en-tête : `nom,grade,anciennete`
   - Puis une ligne par personnel :
     `String.join(",", p.nom(), p.grade().name(), String.valueOf(p.anciennete()))`

3. **Écrire le fichier** avec `Files.write(csv, lignes, StandardCharsets.UTF_8)`.
   Si le fichier existe déjà, il est écrasé (comportement par défaut).

## Exemples

```text
Entrée :
  Personnel("Dupont", SERGENT, 10)
  Personnel("Martin", SOLDAT, 2)
  Personnel("Leroy",  CAPORAL, 5)

Fichier CSV produit :
  nom,grade,anciennete
  Dupont,SERGENT,10
  Leroy,CAPORAL,5
  Martin,SOLDAT,2
```

```text
Entrée : liste vide

Fichier CSV produit :
  nom,grade,anciennete
```

```text
Anciennetés égales :
  Personnel("Robert", SERGENT, 7)
  Personnel("Andre",  CAPORAL, 7)

Fichier CSV produit :
  nom,grade,anciennete
  Andre,CAPORAL,7
  Robert,SERGENT,7
```

## Contraintes

- Package `etnc.m5`. **Ne modifiez pas** les signatures ni les classes fournies.
- Trier une **copie** (`new ArrayList<>(personnels)`) — ne jamais muter la source.
- Tri avec `Comparator.comparingInt(Personnel::anciennete).reversed().thenComparing(Personnel::nom)`.
- Construire chaque ligne avec `String.join(",", ...)` — pas de concaténation `+`.
- `Files.write` avec `StandardCharsets.UTF_8` explicite.
- La méthode propage `throws IOException` (ne pas absorber l'exception).

## Ce qui sera vérifié

- 3 personnels exportés → relecture : en-tête en 1re ligne + 3 lignes triées.
- Format de ligne exact : `nom,GRADE,anciennete` (grade en majuscules).
- Égalité d'ancienneté → départagée par nom alphabétique.
- Liste vide → fichier avec seulement l'en-tête.
- La liste source **n'est pas mutée** après l'appel.
- Noms avec accents (UTF-8) lus et écrits correctement.
- Fichier existant → écrasé (pas d'ajout en fin de fichier).
- Ancienneté négative → triée correctement comme entier signé.

## Pour aller plus loin (optionnel — non noté)

- Que se passe-t-il si un champ contient une virgule ? (Limite du CSV simple sans
  guillemets — cf. chapitre 5-7 pour la discussion sur les limites de `split(",")`)
- Comment écrire avec `StandardOpenOption.APPEND` pour ajouter plutôt qu'écraser ?
- Pourquoi `grade().name()` est-il préférable à `grade().toString()` ici ?
