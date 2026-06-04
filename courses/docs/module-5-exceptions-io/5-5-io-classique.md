---
id: 5-5-io-classique
sidebar_position: 5
title: "I/O classique"
description: "Lire et écrire des fichiers texte avec FileReader/BufferedReader et PrintWriter, en gérant IOException."
---

# I/O classique

## Pourquoi ce chapitre

Jusqu'ici, vos programmes écrivent dans la console et perdent toutes leurs données à l'arrêt. Ce chapitre vous donne le premier outil pour **persister de l'information dans un fichier** : lire et écrire du texte avec l'API classique de Java (les entrées/sorties, souvent abrégées **I/O**, de l'anglais *Input/Output*).

Vous allez retrouver ici deux acquis des chapitres précédents : `try-with-resources` (chapitre 5-3) pour fermer automatiquement le flux (en anglais *stream*, désigne ici un canal de lecture ou d'écriture), et la gestion de `IOException` (chapitre 5-2), qui est la première exception **checked** que vous manipulez vraiment. Le chapitre 5-6 vous présentera ensuite NIO.2 (New I/O, l'API moderne), qui simplifie encore tout cela.

## Ce que vous saurez faire à la fin

- **Expliquer** le modèle de flux : ouvrir, lire/écrire, fermer.
- **Écrire** un fichier texte ligne par ligne avec `PrintWriter`.
- **Lire** un fichier texte ligne par ligne avec `BufferedReader`.
- **Gérer** l'`IOException` checked : la rattraper ou la déclarer.
- **Fermer** un flux automatiquement grâce à `try-with-resources`.
- **Spécifier** l'encodage UTF-8 (Universal Character Set Transformation Format, 8 bits) explicitement pour éviter les problèmes d'accents.

## 1. Le modèle de flux

Java lit et écrit les fichiers à travers des **flux** (en anglais *streams*). Le principe est toujours le même :

1. **Ouvrir** le flux (associer un objet Java au fichier sur le disque).
2. **Lire** ou **écrire** des données.
3. **Fermer** le flux (libérer la ressource système).

Si vous oubliez l'étape 3, le fichier peut rester verrouillé ou des données peuvent ne pas être écrites sur le disque (une partie de l'écriture reste dans un tampon, en anglais *buffer*). C'est pourquoi on utilise toujours `try-with-resources` avec les flux.

La classe `File` représente un **chemin** vers un fichier ou un répertoire. Elle ne lit ni n'écrit elle-même — c'est un simple descripteur.

```java
import java.io.File;

// Un chemin vers un fichier (le fichier n'est pas encore créé ou ouvert)
File chemin = new File("rapport.txt");
System.out.println(chemin.getName()); // rapport.txt
```

### Exemple

```java
import java.io.File;

File chemin = new File("donnees.txt");
// File ne crée pas le fichier — il décrit seulement le chemin
System.out.println("Chemin absolu : " + chemin.getAbsolutePath());
```

### À retenir

> - Le modèle de flux en Java est : **ouvrir → lire/écrire → fermer**.
> - `File` décrit un chemin ; il faut un `Reader` ou `Writer` pour lire ou écrire.
> - Oublier de fermer le flux entraîne des fuites de ressources. `try-with-resources` règle ce problème automatiquement.

## 2. Écrire un fichier texte

Pour écrire du texte ligne par ligne, on combine :

- `FileOutputStream` — ouvre le fichier en écriture (au niveau des octets).
- `OutputStreamWriter` — convertit les caractères en octets selon un encodage.
- `PrintWriter` — ajoute les méthodes pratiques `print` et `println`.

L'encodage est spécifié via `StandardCharsets.UTF_8` : c'est la constante Java pour l'UTF-8. Ne pas la préciser signifie utiliser l'encodage par défaut de la machine, qui peut varier (et provoquer des accents cassés sur certains systèmes).

### Exemple

```java
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

// try-with-resources ferme PrintWriter (et les flux sous-jacents) automatiquement
try (PrintWriter ecrivain = new PrintWriter(
        new OutputStreamWriter(new FileOutputStream("rapport.txt"), StandardCharsets.UTF_8))) {

    ecrivain.println("Rapport du jour");
    ecrivain.println("Effectif : 42 personnels");
    ecrivain.println("Statut : opérationnel");

} catch (IOException e) {
    // IOException est checked : on doit la rattraper ou la déclarer
    System.out.println("Impossible d'écrire le fichier : " + e.getMessage());
}
```

Après exécution, le fichier `rapport.txt` contient trois lignes en UTF-8.

### À retenir

> - Utilisez `OutputStreamWriter` avec `StandardCharsets.UTF_8` pour forcer l'encodage.
> - `PrintWriter.println` ajoute un saut de ligne après chaque appel.
> - `IOException` est une exception **checked** : le compilateur vous oblige à la gérer (rattraper ou déclarer).

## 3. Lire un fichier texte ligne à ligne

Pour lire du texte, on combine symétriquement :

- `FileInputStream` — lit le fichier au niveau des octets.
- `InputStreamReader` — convertit les octets en caractères selon l'encodage.
- `BufferedReader` — ajoute la méthode `readLine()` qui lit une ligne entière.

`readLine()` renvoie la ligne suivante sous forme de `String`, ou **`null` quand le fichier est épuisé**. La boucle doit donc tester `null` pour savoir quand s'arrêter.

### Exemple

```java
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

// lecture d'un fichier en UTF-8, ressource fermée automatiquement
try (BufferedReader lecteur = new BufferedReader(
        new InputStreamReader(new FileInputStream("rapport.txt"), StandardCharsets.UTF_8))) {
    String ligne;
    while ((ligne = lecteur.readLine()) != null) {
        // readLine() renvoie null en fin de fichier : la boucle s'arrête
        System.out.println(ligne);
    }
} catch (IOException e) {
    System.out.println("Lecture impossible : " + e.getMessage());
}
```

Le résultat affiché correspond exactement au contenu du fichier, ligne par ligne.

### À retenir

> - `BufferedReader.readLine()` renvoie `null` en fin de fichier. **Testez toujours `null`** pour arrêter la boucle.
> - Le même encodage (`StandardCharsets.UTF_8`) doit être utilisé à la lecture et à l'écriture.
> - `BufferedReader` (en anglais *buffered* = mis en mémoire tampon) améliore les performances en lisant par blocs plutôt qu'octet par octet.

## 4. IOException : une exception checked

`IOException` est la première exception **checked** que vous rencontrez vraiment en pratique. Rappel du chapitre 5-1 : une exception checked est **vérifiée par le compilateur** — il refuse de compiler votre code si vous ne la gérez pas.

Vous avez deux options.

### Exemple

**Option A — rattraper avec `catch`** (dans la méthode courante) :

```java
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

static void afficherFichier(String nomFichier) {
    try (BufferedReader lecteur = new BufferedReader(
            new InputStreamReader(new FileInputStream(nomFichier), StandardCharsets.UTF_8))) {
        String ligne;
        while ((ligne = lecteur.readLine()) != null) {
            System.out.println(ligne);
        }
    } catch (IOException e) {
        // on rattrape ici et on traite localement
        System.out.println("Erreur de lecture : " + e.getMessage());
    }
}
```

**Option B — déclarer avec `throws`** (propager au code appelant) :

```java
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

static void afficherFichier(String nomFichier) throws IOException {
    // IOException propagée à l'appelant : lui devra la gérer
    try (BufferedReader lecteur = new BufferedReader(
            new InputStreamReader(new FileInputStream(nomFichier), StandardCharsets.UTF_8))) {
        String ligne;
        while ((ligne = lecteur.readLine()) != null) {
            System.out.println(ligne);
        }
    }
}
```

Le chapitre 5-6 vous présentera NIO.2 (`Path` / `Files`), qui offre des méthodes plus concises pour les mêmes opérations — les exos I/O utiliseront cette API moderne.

### À retenir

> - `IOException` est **checked** : le compilateur exige que vous la gériez.
> - Choisissez : rattraper localement (`catch`) ou propager (`throws`) selon qui est le mieux placé pour traiter l'erreur.
> - `try-with-resources` ferme le flux même si une `IOException` est levée pendant la lecture ou l'écriture.

## Erreurs fréquentes

- **Oublier de fermer le flux** : ouvrir un `BufferedReader` ou un `PrintWriter` sans `try-with-resources` laisse le fichier verrouillé ou les données non écrites. Cause : la méthode `close()` n'est pas appelée si une exception survient. Correction : toujours ouvrir les flux dans la déclaration du `try-with-resources`.

- **Mauvais encodage** : lire un fichier UTF-8 sans spécifier l'encodage (ou avec un encodage différent) produit des caractères parasites sur les accents (`é` → `Ã©`). Cause : l'encodage par défaut de la JVM (Java Virtual Machine) dépend du système d'exploitation. Correction : spécifier `StandardCharsets.UTF_8` à la fois à l'écriture et à la lecture.

- **Boucle de lecture qui ne teste pas `null`** : écrire `while (!lecteur.readLine().isEmpty())` ou `while (true)` sans tester `null` provoque une `NullPointerException` en fin de fichier. Cause : `readLine()` renvoie `null` (et non une chaîne vide) quand le fichier est épuisé. Correction : utiliser `while ((ligne = lecteur.readLine()) != null)`.

- **Fichier écrasé à la place d'un append** : ouvrir un `FileOutputStream` sans le drapeau `append` efface le contenu existant. Cause : le constructeur par défaut écrase. Correction : utiliser `new FileOutputStream("fichier.txt", true)` pour ajouter en fin de fichier.

## Exercice guidé

**Contexte** : vous devez écrire un programme qui mémorise une liste de capitales dans un fichier texte, puis relit ce fichier et affiche chaque capitale.

**Pas à pas :**

1. Créez une méthode `ecrireCapitales(String nomFichier)` qui écrit les capitales suivantes dans le fichier, une par ligne : `Paris`, `Berlin`, `Rome`, `Madrid`.

2. Créez une méthode `lireEtAfficher(String nomFichier)` qui lit le fichier ligne par ligne et affiche chaque ligne précédée de `→ `.

3. Dans `main`, appelez successivement `ecrireCapitales("capitales.txt")` puis `lireEtAfficher("capitales.txt")`.

La sortie attendue est :
```text
→ Paris
→ Berlin
→ Rome
→ Madrid
```

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

```java
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class NoteCapitales {

    static void ecrireCapitales(String nomFichier) throws IOException {
        // try-with-resources : PrintWriter est fermé automatiquement
        try (PrintWriter ecrivain = new PrintWriter(
                new OutputStreamWriter(
                        new FileOutputStream(nomFichier), StandardCharsets.UTF_8))) {
            ecrivain.println("Paris");
            ecrivain.println("Berlin");
            ecrivain.println("Rome");
            ecrivain.println("Madrid");
        }
    }

    static void lireEtAfficher(String nomFichier) throws IOException {
        // BufferedReader fermé automatiquement en fin de bloc
        try (BufferedReader lecteur = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(nomFichier), StandardCharsets.UTF_8))) {
            String ligne;
            while ((ligne = lecteur.readLine()) != null) {
                // null = fin du fichier : la boucle s'arrête
                System.out.println("→ " + ligne);
            }
        }
    }

    public static void main(String[] args) {
        try {
            ecrireCapitales("capitales.txt");
            lireEtAfficher("capitales.txt");
        } catch (IOException e) {
            System.out.println("Erreur d'accès au fichier : " + e.getMessage());
        }
    }
}
```

**Points clés** :
- `StandardCharsets.UTF_8` est passé à la fois à l'écriture et à la lecture : même encodage des deux côtés.
- `throws IOException` dans les deux méthodes : la gestion est centralisée dans `main`.
- Le `try-with-resources` garantit que le flux est fermé même si `println` ou `readLine` lève une exception.

</details>

## Vérifiez vos acquis

- Quel est le rôle de `BufferedReader` par rapport à `InputStreamReader` ?
- Pourquoi `readLine()` renvoie-t-il `null` plutôt qu'une exception pour signaler la fin d'un fichier ?
- Quelle est la différence entre déclarer `throws IOException` dans la signature d'une méthode et placer un `catch (IOException e)` dans son corps ?
- Que se passe-t-il si vous ouvrez en écriture un fichier existant sans le drapeau `append` ?
- Pourquoi spécifier `StandardCharsets.UTF_8` est-il important sur un serveur Linux alors que votre fichier a été créé sur Windows ?

## Pour aller plus loin

- [Java IO Tutorial](https://www.baeldung.com/java-io) (Baeldung) — tour d'horizon complet de `InputStream`, `OutputStream`, `Reader`, `Writer` et leurs dérivés.
- [BufferedReader](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/io/BufferedReader.html) (Javadoc OpenJDK 25) — documentation officielle de la classe avec tous ses constructeurs et méthodes.
- [Lesson: Basic I/O](https://dev.java/learn/java-io/) (dev.java) — guide Oracle sur les flux d'entrées/sorties classiques.

## Prochain chapitre

→ **[Chapitre 5-6 — NIO.2 : Path et Files](5-6-nio2)**
