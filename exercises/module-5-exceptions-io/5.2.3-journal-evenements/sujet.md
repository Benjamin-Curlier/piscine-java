# Exercice 5.2.3 — Journal d'événements (append-only)

## Contexte

Un journal d'événements est un fichier qui **accumule** des lignes au fil du
temps : chaque nouvel événement vient s'ajouter après les précédents, sans
jamais écraser ce qui a déjà été enregistré. C'est un besoin courant dans
les systèmes de supervision et de traçabilité (logs d'opérations, journaux
d'audit, traces tactiques…).

En Java, l'écriture dans un fichier **écrase par défaut** le contenu existant.
Pour construire un journal append-only, il faut explicitement demander à NIO.2
d'**ouvrir ou créer** le fichier et d'**écrire en fin de fichier** — deux
options de `StandardOpenOption`.

## Énoncé

Complétez la classe `Journal` avec les deux méthodes statiques :

```java
public static void ajouter(Path journal, String evenement) throws IOException

public static List<String> lire(Path journal) throws IOException
```

**`ajouter`** doit ajouter `evenement + "\n"` **en fin de fichier** sans
écraser le contenu existant. Si le fichier n'existe pas encore, il doit être
créé automatiquement. Utilisez `Files.writeString` avec les options
`StandardOpenOption.CREATE` et `StandardOpenOption.APPEND`.

**`lire`** doit retourner toutes les lignes du journal sous forme de liste.
Si le fichier n'existe pas encore (journal jamais alimenté), elle doit retourner
une **liste vide** sans lever d'exception. Utilisez `Files.exists` pour tester
l'existence avant de lire, puis `Files.readAllLines` avec `StandardCharsets.UTF_8`.

## Exemple

```text
// Journal vide au départ
Journal.lire(chemin)    // → []

Journal.ajouter(chemin, "Démarrage du système")
Journal.lire(chemin)    // → ["Démarrage du système"]

Journal.ajouter(chemin, "Connexion opérateur")
Journal.ajouter(chemin, "Mission lancée")
Journal.lire(chemin)    // → ["Démarrage du système", "Connexion opérateur", "Mission lancée"]
```

## Contraintes

- Package `etnc.m5`. **Ne modifiez pas** les signatures.
- `ajouter` : **`Files.writeString`** avec `StandardCharsets.UTF_8`,
  `StandardOpenOption.CREATE` et `StandardOpenOption.APPEND` — dans cet ordre.
- `lire` : tester `Files.exists(journal)` avant tout appel de lecture ;
  retourner `List.of()` si le fichier est absent ; sinon `Files.readAllLines`
  avec `StandardCharsets.UTF_8`.
- Les deux méthodes propagent `throws IOException` (exception checked).
- Les fins de ligne dans le fichier sont `\n` (explicite, pas `System.lineSeparator()`).
- Aucune dépendance extérieure n'est autorisée.

## Ce qui sera vérifié

- Un seul `ajouter` → `lire` renvoie une liste à un élément.
- Deux `ajouter` successifs → `lire` renvoie les deux lignes dans l'ordre.
- Après le premier `ajouter`, le fichier existe bien (`Files.exists`).
- `lire` sur un journal jamais créé renvoie une liste vide (pas d'exception).
- Chaque `ajouter` ajoute exactement une ligne (pas de doublons, pas d'effacement).
- Les accents et caractères spéciaux sont préservés (UTF-8).
- Un `ajouter` sur un fichier déjà pré-rempli ajoute bien **après** le contenu.
- Dix `ajouter` successifs → dix lignes, dans l'ordre.
- Un événement vide `""` ajoute une ligne vide (taille totale + 1).

## Pour aller plus loin (optionnel — non noté)

- Que se passerait-il si on omettait `StandardOpenOption.APPEND` en gardant
  `CREATE` seul ? Le fichier serait recréé (et donc vidé) à chaque appel.
- Pourquoi utiliser `StandardOpenOption.CREATE` plutôt que
  `StandardOpenOption.CREATE_NEW` ? `CREATE_NEW` lève une exception si le
  fichier existe déjà — incompatible avec un journal qui grossit à chaque appel.
- Comment protéger le journal contre les écritures concurrentes (plusieurs
  threads) ? Indice : `StandardOpenOption.SYNC` ou un verrou explicite (`FileLock`).
