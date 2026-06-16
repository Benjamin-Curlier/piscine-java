# Exercice 5.3.2 — Export CSV trié (Comparator + String.join + Files.write)

## Contexte

Le service des ressources humaines membres doit générer des fichiers CSV à
partir de listes de personnels, **triés par ancienneté décroissante** (les plus
anciens en premier) puis par **nom alphabétique** pour égalité. Ces fichiers
servent à produire des tableaux d'affichage et doivent rester lisibles dans
n'importe quel tableur.

Vous avez à disposition le modèle fourni :

```java
public enum Niveau { JUNIOR, CONFIRME, SENIOR, LEAD, PRINCIPAL }
public record Personnel(String nom, Niveau niveau, int anciennete) { }
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
   - La première ligne est toujours l'en-tête : `nom,niveau,anciennete`
   - Puis une ligne par personnel :
     `String.join(",", p.nom(), p.niveau().name(), String.valueOf(p.anciennete()))`

3. **Écrire le fichier** avec `Files.write(csv, lignes, StandardCharsets.UTF_8)`.
   Si le fichier existe déjà, il est écrasé (comportement par défaut).

## Exemples

```text
Entrée :
  Personnel("Dupont", SENIOR, 10)
  Personnel("Martin", JUNIOR, 2)
  Personnel("Leroy",  CONFIRME, 5)

Fichier CSV produit :
  nom,niveau,anciennete
  Dupont,SENIOR,10
  Leroy,CONFIRME,5
  Martin,JUNIOR,2
```

```text
Entrée : liste vide

Fichier CSV produit :
  nom,niveau,anciennete
```

```text
Anciennetés égales :
  Personnel("Robert", SENIOR, 7)
  Personnel("Andre",  CONFIRME, 7)

Fichier CSV produit :
  nom,niveau,anciennete
  Andre,CONFIRME,7
  Robert,SENIOR,7
```

## Contraintes

- Package `piscine.m5`. **Ne modifiez pas** les signatures ni les classes fournies.
- Trier une **copie** (`new ArrayList<>(personnels)`) — ne jamais muter la source.
- Tri avec `Comparator.comparingInt(Personnel::anciennete).reversed().thenComparing(Personnel::nom)`.
- Construire chaque ligne avec `String.join(",", ...)` — pas de concaténation `+`.
- `Files.write` avec `StandardCharsets.UTF_8` explicite.
- La méthode propage `throws IOException` (ne pas absorber l'exception).

## Ce qui sera vérifié

- 3 personnels exportés → relecture : en-tête en 1re ligne + 3 lignes triées.
- Format de ligne exact : `nom,NIVEAU,anciennete` (niveau en majuscules).
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
- Pourquoi `niveau().name()` est-il préférable à `niveau().toString()` ici ?
