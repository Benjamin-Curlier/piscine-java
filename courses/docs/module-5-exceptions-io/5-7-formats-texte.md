---
id: 5-7-formats-texte
sidebar_position: 7
title: "Formats texte : CSV et properties"
description: "Manipuler des fichiers CSV et .properties à la main, et découvrir JSON en survol."
---

# Formats texte : CSV et properties

## Pourquoi ce chapitre

Vous savez désormais lire et écrire des fichiers avec NIO.2 (chapitre 5-6). Mais un fichier texte brut n'est pas toujours suffisant : les données ont souvent une **structure** — des colonnes, des clés, des valeurs. Ce chapitre vous apprend à manipuler deux formats texte courants en Java sans dépendance externe : le CSV (Comma-Separated Values, valeurs séparées par des virgules) et le `.properties` (clé=valeur). Vous verrez également à quoi ressemble le JSON (JavaScript Object Notation) et pourquoi on utilise une bibliothèque pour le traiter.

## Ce que vous saurez faire à la fin

- **Écrire** un fichier CSV simple avec `String.join` et NIO.2.
- **Lire** et **découper** un fichier CSV ligne par ligne avec `String.split`.
- **Identifier** les limites du découpage naïf par virgule.
- **Charger** un fichier `.properties` avec `java.util.Properties` et lire une valeur par sa clé.
- **Reconnaître** la syntaxe JSON et expliquer pourquoi on délègue son *parsing* (analyse syntaxique — la transformation d'un texte brut en données structurées) à une bibliothèque.

## 1. Le format CSV

Un fichier CSV (Comma-Separated Values — valeurs séparées par des virgules) est un fichier texte dont chaque ligne représente un enregistrement et dont les champs sont séparés par une virgule. C'est l'un des formats d'échange les plus répandus : tableurs, exports de bases de données, jeux de données ouverts.

Voici un exemple de fichier `livres.csv` :

```text
titre,annee
Le Petit Prince,1943
Candide,1759
Germinal,1885
```

La première ligne est l'**en-tête** : elle nomme les colonnes. Les lignes suivantes sont les données.

### Exemple

Pour écrire ce fichier depuis Java, vous assemblez chaque ligne avec `String.join` puis vous écrivez l'ensemble avec `Files.writeString` (chapitre 5-6) :

```java
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class EcritureCsv {

    public static void main(String[] args) throws IOException {
        Path fichier = Path.of("livres.csv");

        // Construire le contenu du CSV : en-tête + données
        String contenu = String.join("\n",
            "titre,annee",
            "Le Petit Prince,1943",
            "Candide,1759",
            "Germinal,1885"
        ) + "\n"; // fin de ligne finale

        Files.writeString(fichier, contenu, StandardCharsets.UTF_8);
        System.out.println("Fichier écrit : " + fichier.toAbsolutePath());
    }
}
```

Pour **lire** le fichier et accéder à chaque champ, vous utilisez `String.split(",")` sur chaque ligne :

```java
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class LectureCsv {

    public static void main(String[] args) throws IOException {
        Path fichier = Path.of("livres.csv");
        List<String> lignes = Files.readAllLines(fichier, StandardCharsets.UTF_8);

        // La première ligne est l'en-tête : on la saute
        for (int i = 1; i < lignes.size(); i++) {
            String ligne = lignes.get(i);
            String[] champs = ligne.split(","); // découper sur la virgule
            String titre = champs[0];
            int annee = Integer.parseInt(champs[1].strip()); // strip() supprime les espaces en trop
            System.out.println(titre + " — " + annee);
        }
    }
}
```

Ce code produit :

```text
Le Petit Prince — 1943
Candide — 1759
Germinal — 1885
```

### À retenir

> - Un fichier CSV est un texte : une ligne = un enregistrement, les champs séparés par des virgules.
> - La première ligne est souvent l'en-tête : **la sauter** avant de traiter les données.
> - `String.join(",", champs)` construit une ligne CSV ; `String.split(",")` la décompose.
> - Toujours préciser l'encodage UTF-8 à la lecture comme à l'écriture.

## 2. Limites du split naïf

`String.split(",")` fonctionne bien tant que vos données ne contiennent pas de virgule. Dès qu'un champ contient lui-même une virgule, le découpage produit des résultats faux.

### Exemple

Imaginez un CSV où le titre contient une virgule :

```text
titre,annee
Guerre et Paix,1869
Les Misérables,1862
"Ah, les beaux jours !",1963
```

La ligne `"Ah, les beaux jours !",1963` sera découpée en **trois** champs par `split(",")` au lieu de deux :

```text
champs[0] = "\"Ah"
champs[1] = " les beaux jours !\""
champs[2] = "1963"
```

Le format CSV standard définit des règles pour les guillemets et les champs échappés, mais les implémenter à la main est fastidieux et source de bugs. **Dans la Piscine, vous ne travaillez qu'avec des CSV simples, sans virgule dans un champ.** En production, on utilise une bibliothèque dédiée (Apache Commons CSV, OpenCSV) qui gère les cas complexes.

### À retenir

> - `split(",")` suffit pour un CSV **simple** (aucun champ ne contient de virgule ni de guillemets).
> - Dès que les données peuvent contenir des virgules dans un champ, il faut une bibliothèque CSV dédiée.
> - Cette limite s'applique aussi aux autres séparateurs (`;`, `\t`) : choisissez un séparateur absent de vos données.

## 3. Les fichiers .properties

Un fichier `.properties` est un fichier texte au format `clé=valeur`, un par ligne. Il sert souvent à stocker la **configuration** d'une application : langue, niveau de journalisation, chemins, paramètres métier.

Voici un exemple de fichier `app.properties` :

```properties
langue=fr
niveau=debutant
maxTentatives=3
```

Java propose la classe `java.util.Properties` pour lire ce format directement, sans parsing manuel.

### Exemple

```java
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class LectureProperties {

    public static void main(String[] args) throws IOException {
        Properties config = new Properties();

        // Charger le fichier .properties en UTF-8
        try (InputStreamReader lecteur = new InputStreamReader(
                new FileInputStream("app.properties"), StandardCharsets.UTF_8)) {
            config.load(lecteur);
        }

        // Lire une valeur par sa clé
        String langue = config.getProperty("langue"); // "fr"
        System.out.println("Langue : " + langue);

        // Lire avec une valeur par défaut si la clé est absente
        String theme = config.getProperty("theme", "clair"); // "clair" si absente
        System.out.println("Thème : " + theme);

        // Lire un entier (les valeurs sont toujours des chaînes dans Properties)
        int maxTentatives = Integer.parseInt(config.getProperty("maxTentatives", "5"));
        System.out.println("Max tentatives : " + maxTentatives);
    }
}
```

Ce code affiche :

```text
Langue : fr
Thème : clair
Max tentatives : 3
```

Remarques importantes :
- `getProperty(clé)` renvoie `null` si la clé est absente. Préférez `getProperty(clé, valeurParDefaut)` pour éviter une `NullPointerException`.
- `Properties` ne stocke que des chaînes de caractères. Pour un entier ou un booléen, vous devez convertir avec `Integer.parseInt` ou `Boolean.parseBoolean`.
- Les lignes commençant par `#` sont des commentaires dans un fichier `.properties`.

### À retenir

> - `java.util.Properties` lit les fichiers `clé=valeur` sans écrire de parsing manuellement.
> - `load(Reader)` charge le fichier ; `getProperty(clé, défaut)` lit une valeur avec repli.
> - Toutes les valeurs sont des `String` : convertir selon le type attendu.
> - Encodage UTF-8 explicite : `new InputStreamReader(new FileInputStream("app.properties"), StandardCharsets.UTF_8)`.

## 4. Et JSON ?

Le JSON (JavaScript Object Notation — notation d'objet JavaScript) est un format texte structuré très répandu pour l'échange de données entre systèmes (API web, fichiers de configuration avancés, bases de documents).

### Exemple

Voici à quoi ressemble un petit objet JSON :

```text
{
  "titre": "Le Petit Prince",
  "annee": 1943,
  "auteur": {
    "nom": "Saint-Exupéry",
    "nationalite": "française"
  },
  "disponible": true
}
```

Le JSON supporte des **types** (chaînes, nombres, booléens, tableaux, objets imbriqués) et des **structures hiérarchiques** que ni le CSV ni le `.properties` ne gèrent nativement.

**Pourquoi ne pas le parser à la main ?** Le format paraît simple, mais les cas réels incluent des caractères d'échappement, des tableaux imbriqués, des valeurs nulles, des caractères Unicode... L'écrire correctement à la main est source d'erreurs. En production, on utilise systématiquement une bibliothèque : **Jackson** ou **Gson** sont les plus répandues en Java.

**Dans la Piscine**, le JSON reste une mention de culture générale. Vous n'avez pas à l'implémenter, et aucun exercice ne demande de parser du JSON. Cela correspond à la contrainte offline du projet : pas de dépendance externe à ajouter.

### À retenir

> - JSON est un format structuré, hiérarchique, très utilisé pour les API web.
> - En Java, on le traite **toujours** avec une bibliothèque (Jackson, Gson) — jamais à la main.
> - Dans la Piscine : CSV et `.properties` suffisent ; JSON = survol uniquement.

## Erreurs fréquentes

- **`split(",")` sur une ligne contenant une virgule dans un champ** : le champ est découpé en plusieurs morceaux, `champs[1]` ne contient plus ce qu'on attend. Cause : `split` ne comprend pas les guillemets CSV. Correction : si vos données peuvent contenir des virgules, choisissez un autre séparateur (`;` ou `|`) absent des données, ou utilisez une bibliothèque CSV.

- **Oublier l'en-tête CSV** : traiter la première ligne comme une donnée et tenter de convertir `"annee"` en entier avec `Integer.parseInt`. Cause : la boucle commence à l'indice 0 au lieu de 1. Correction : démarrer la boucle à l'indice 1 (ou utiliser un `boolean` `premiereLigne` pour la sauter).

- **Lire un `.properties` avec le mauvais encodage** : les caractères accentués (`é`, `à`, `ü`) s'affichent en symboles illisibles. Cause : `config.load(new FileReader("app.properties"))` utilise l'encodage par défaut de la plateforme (souvent `windows-1252` sur Windows). Correction : toujours passer un `InputStreamReader` avec `StandardCharsets.UTF_8`.

- **`getProperty` renvoie `null` et provoque une `NullPointerException` plus loin** : la clé est absente ou mal orthographiée. Cause : utilisation de `getProperty(clé)` sans valeur par défaut. Correction : utiliser `getProperty(clé, valeurParDefaut)` et tester le `null` si la valeur par défaut n'a pas de sens.

- **`champs[1]` manquant** : une ligne du CSV est vide ou ne contient qu'un seul champ, et `Integer.parseInt(champs[1])` lève `ArrayIndexOutOfBoundsException`. Cause : ligne mal formée ou ligne vide en fin de fichier. Correction : vérifier `champs.length` avant d'accéder aux indices, et ignorer les lignes vides.

## Exercice guidé

**Contexte** : vous gérez une petite bibliothèque numérique. Vous disposez d'un fichier `livres.csv` contenant le titre et l'année de parution de quelques livres, ainsi qu'un fichier `app.properties` qui indique la langue d'affichage de l'application.

**Objectif** : lire les deux fichiers, afficher le nombre de livres et la langue configurée.

**Pas à pas :**

1. Créez le fichier `livres.csv` avec ce contenu (vous pouvez le créer à la main ou via `Files.writeString`) :

```text
titre,annee
Le Petit Prince,1943
Candide,1759
Germinal,1885
Les Misérables,1862
```

2. Créez le fichier `app.properties` :

```properties
langue=fr
```

3. Écrivez une classe `BibliothequeApp` avec une méthode `main` qui :
   - Lit `livres.csv` avec `Files.readAllLines`, saute la première ligne (en-tête), puis compte et affiche le nombre de livres.
   - Charge `app.properties` avec `java.util.Properties` et affiche la valeur de la clé `langue` (avec la valeur par défaut `"en"` si absente).

4. Pour chaque livre, affichez `titre (annee)` en convertissant le champ `annee` en entier.

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

```java
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

public class BibliothequeApp {

    public static void main(String[] args) throws IOException {

        // --- Étape 1 : lire le CSV de livres ---
        Path fichierCsv = Path.of("livres.csv");
        List<String> lignes = Files.readAllLines(fichierCsv, StandardCharsets.UTF_8);

        // Sauter la première ligne (en-tête)
        int nombreLivres = 0;
        for (int i = 1; i < lignes.size(); i++) {
            String ligne = lignes.get(i);
            if (ligne.isBlank()) {
                continue; // ignorer les lignes vides éventuelles
            }
            String[] champs = ligne.split(",");
            String titre = champs[0];
            int annee = Integer.parseInt(champs[1].strip());
            System.out.println(titre + " (" + annee + ")");
            nombreLivres++;
        }
        System.out.println("Nombre de livres : " + nombreLivres);

        // --- Étape 2 : lire la configuration ---
        Properties config = new Properties();
        try (InputStreamReader lecteur = new InputStreamReader(
                new FileInputStream("app.properties"), StandardCharsets.UTF_8)) {
            config.load(lecteur);
        }

        // Lire la langue avec valeur par défaut "en" si la clé est absente
        String langue = config.getProperty("langue", "en");
        System.out.println("Langue d'affichage : " + langue);
    }
}
```

**Points clés** :
- La boucle commence à `i = 1` pour sauter l'en-tête.
- `.strip()` sur `champs[1]` élimine les espaces éventuels avant `Integer.parseInt`.
- `getProperty("langue", "en")` fournit une valeur par défaut si la clé est absente.
- Les deux fichiers sont lus en UTF-8 explicite : `StandardCharsets.UTF_8` dans les deux cas.
- Le `try-with-resources` sur le `InputStreamReader` ferme le flux automatiquement (chapitre 5-3).

</details>

## Vérifiez vos acquis

- Quelle est la différence entre un fichier CSV et un fichier `.properties` ? Dans quel cas choisissez-vous l'un plutôt que l'autre ?
- Pourquoi `String.split(",")` peut-il produire des résultats incorrects sur un fichier CSV ?
- Que renvoie `config.getProperty("clé")` si la clé n'existe pas dans le fichier `.properties` ? Comment éviter une `NullPointerException` à l'utilisation de cette valeur ?
- Pourquoi est-il déconseillé de parser du JSON à la main en Java ?
- Quel est le rôle de `StandardCharsets.UTF_8` lors du chargement d'un fichier `.properties` ?

## Pour aller plus loin

- [Reading CSV in Java](https://www.baeldung.com/java-csv-file-array) (Baeldung) — lecture de CSV avec et sans bibliothèque, comparatif des approches.
- [Class Properties](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/Properties.html) (Javadoc OpenJDK 25) — documentation complète de `java.util.Properties` : `load`, `store`, `getProperty`, `setProperty`.
- [Working with Files in Java](https://dev.java/learn/java-io/file-system/creating-directories/) (dev.java) — guide officiel Oracle sur la manipulation de fichiers avec NIO.2.

## Prochain chapitre

→ **Module 6 — Tests et Git** *(à venir)*
