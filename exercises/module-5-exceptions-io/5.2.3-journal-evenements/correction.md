# Correction — Exercice 5.2.3 Journal d'événements (append-only)

## Démarche attendue

```java
public static void ajouter(Path journal, String evenement) throws IOException {
    // CREATE ouvre ou crée le fichier ; APPEND positionne en fin de fichier.
    // Sans ces options, writeString écraserait le contenu existant à chaque appel.
    Files.writeString(journal, evenement + "\n", StandardCharsets.UTF_8,
            StandardOpenOption.CREATE, StandardOpenOption.APPEND);
}

public static List<String> lire(Path journal) throws IOException {
    // Si le journal n'a jamais été alimenté, le fichier n'existe pas encore.
    // readAllLines lèverait NoSuchFileException — on protège avec exists().
    if (!Files.exists(journal)) {
        return List.of();
    }
    return Files.readAllLines(journal, StandardCharsets.UTF_8);
}
```

## Points clés

- **`CREATE` + `APPEND` sont indissociables** pour un journal :
  - `CREATE` : ouvre le fichier s'il existe, le crée s'il est absent.
  - `APPEND` : déplace le curseur d'écriture en fin de fichier avant chaque écriture.
  Sans `APPEND`, le curseur reste au début et le contenu est écrasé.
  Sans `CREATE`, l'écriture échoue si le fichier n'existe pas encore.

- **`Files.exists` avant `readAllLines`** : `readAllLines` lève
  `NoSuchFileException` (sous-classe d'`IOException`) si le fichier est absent.
  Le test d'existence rend l'API plus conviviale : un journal vierge renvoie
  une liste vide plutôt qu'une exception.

- **`evenement + "\n"` et `StandardCharsets.UTF_8`** : on choisit `\n` comme
  séparateur de ligne plutôt que `System.lineSeparator()` pour garantir la
  portabilité entre Windows (`\r\n`) et Linux (`\n`). L'UTF-8 explicite assure
  que les accents et caractères spéciaux ne sont pas altérés.

- **`List.of()`** : renvoie une liste immuable vide — juste et idiomatique
  pour signaler « aucun événement enregistré ».

## Erreurs fréquentes observées

- **Omettre `StandardOpenOption.APPEND`** : `Files.writeString` avec `CREATE`
  seul écrase le fichier à chaque appel ; les tests d'accumulation échouent.

- **Omettre `StandardOpenOption.CREATE`** : le premier appel à `ajouter`
  lève `NoSuchFileException` car le fichier n'existe pas encore.

- **Ne pas tester `Files.exists`** dans `lire` : un journal vierge provoque
  une `NoSuchFileException` non gérée alors que l'énoncé attend une liste vide.

- **Utiliser `System.lineSeparator()`** : produit `\r\n` sous Windows,
  ce qui décale les fins de ligne et fait échouer les assertions `\n` explicites
  des tests.

- **Oublier `StandardCharsets.UTF_8`** : les accents peuvent être mal encodés
  selon la plateforme (Windows utilise souvent CP1252 par défaut).

## Pour approfondir

- **`StandardOpenOption.WRITE` vs `APPEND`** : `WRITE` seul positionne le
  curseur au début et écrase ; `APPEND` le positionne en fin à chaque appel,
  même si plusieurs processus écrivent en même temps (atomicité partielle
  garantie par le système d'exploitation sur la plupart des FS locaux).

- **Rotation de journal** : les journaux industriels (Log4j, Logback) ajoutent
  la gestion de la taille maximale (rotation), de la compression des archives
  et de la purge automatique — couches ajoutées au-dessus du même mécanisme
  d'append-only.

- **Journalisation structurée** : ajouter un horodatage (`LocalDateTime.now()`)
  et un niveau (`INFO`, `WARN`, `ERROR`) transforme le journal en log exploitable
  par un outil d'analyse (grep, ELK, Splunk…).
