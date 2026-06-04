# Correction — Exercice 5.2.4 Lecture de configuration (cle=valeur)

## Démarche attendue

```java
public static Map<String, String> lireConfig(Path config) throws IOException {
    // UTF-8 explicite : un fichier de config peut contenir des accents.
    List<String> lignes = Files.readAllLines(config, StandardCharsets.UTF_8);

    // LinkedHashMap : conserve l'ordre d'apparition (lisibilité au débogage).
    // put écrase une clé déjà présente → « la dernière occurrence gagne ».
    Map<String, String> configuration = new LinkedHashMap<>();

    for (String ligne : lignes) {
        if (ligne.isBlank()) {
            continue;                          // ligne vide ignorée
        }
        if (ligne.trim().startsWith("#")) {
            continue;                          // commentaire ignoré
        }
        int positionEgal = ligne.indexOf('=');
        if (positionEgal < 0) {
            continue;                          // pas une paire → ignorée
        }
        String cle = ligne.substring(0, positionEgal).trim();
        String valeur = ligne.substring(positionEgal + 1).trim();
        configuration.put(cle, valeur);
    }
    return configuration;
}

public static String lireValeur(Path config, String cle, String valeurParDefaut) throws IOException {
    // Délègue : zéro duplication de la logique de parsing.
    return lireConfig(config).getOrDefault(cle, valeurParDefaut);
}
```

Variante équivalente pour la coupure, avec `split` :

```java
String[] parts = ligne.split("=", 2);   // limite 2 = couper au PREMIER '='
if (parts.length < 2) {
    continue;                            // aucun '=' → pas une paire
}
String cle = parts[0].trim();
String valeur = parts[1].trim();
```

## Points clés

- **Couper au PREMIER `=`.** C'est LE piège de l'exercice. `ligne.split("=")`
  **sans limite** sur `url=http://x?a=b` produit trois morceaux et la valeur est
  tronquée. Avec `split("=", 2)` (ou `indexOf` + `substring`), la valeur garde
  **tout** ce qui suit le premier `=`. La limite `2` n'est pas optionnelle.

- **`trim` la clé ET la valeur.** Un fichier édité à la main contient souvent des
  espaces de présentation (`langue = fr`). On les retire pour que la clé soit
  exploitable et que la valeur ne traîne pas d'espace parasite.

- **`isBlank()` pour les lignes vides, `trim().startsWith("#")` pour les
  commentaires.** `isBlank()` (Java 11+) couvre les lignes faites uniquement
  d'espaces ou de tabulations. Le `trim()` avant `startsWith("#")` permet de
  reconnaître un commentaire **indenté** (`  # ...`).

- **`Map.put` réalise « la dernière gagne » gratuitement.** Pas besoin de tester
  l'existence préalable : la seconde affectation écrase la première.

- **`getOrDefault` pour le cas « clé absente ».** Plus lisible qu'un
  `containsKey` suivi d'un `get`, et atomique en intention.

- **UTF-8 explicite** à la lecture : on ne se fie pas à l'encodage par défaut de
  la plateforme (qui varie entre Windows et Linux).

- **`lireValeur` délègue à `lireConfig`** : une seule définition du format, donc
  un seul endroit à corriger. C'est l'esprit « don't repeat yourself ».

## Erreurs fréquentes observées

- **`ligne.split("=")` sans la limite `2`** : casse les valeurs contenant un
  `=` (URL, base64, expression). Le test privé `url=http://x?a=b` le détecte.

- **Utiliser `java.util.Properties`** : interdit ici (objectif = parser à la
  main). Le critère formateur le sanctionne.

- **Oublier de `trim` la clé** : `langue = fr` produit alors une clé `"langue "`
  (avec espace) introuvable par l'appelant.

- **Tester `startsWith("#")` sans `trim` au préalable** : un commentaire indenté
  `  # ...` n'est plus reconnu et pollue la table.

- **Ne pas ignorer les lignes sans `=`** : `indexOf('=') == -1` doit faire
  `continue` ; sinon un `substring` négatif lèverait une exception.

- **Croire qu'une valeur vide est une erreur** : `cle=` est valide et doit
  donner `""`. Avec `split("=", 2)`, attention : `"cle=".split("=", 2)` renvoie
  bien `["cle", ""]` (la limite `2` garde le tableau de taille 2) ; la version
  `indexOf` + `substring(positionEgal + 1)` donne aussi `""`.

## Pour approfondir

- **`split("=", 2)` vs `split("=")`** : la version sans limite supprime de plus
  les morceaux vides **en fin** de tableau. `"cle=".split("=")` renvoie un
  tableau de taille **1** (`["cle"]`), ce qui ferait perdre la valeur vide —
  une raison de plus de toujours fixer la limite.

- **Sensibilité à la casse** : ici les clés sont sensibles à la casse
  (`Langue` ≠ `langue`), comportement par défaut d'une `Map<String,String>`.
  Pour l'ignorer, on normaliserait la clé (`toLowerCase`) à l'insertion ET à la
  lecture, ou on utiliserait une `TreeMap` avec `String.CASE_INSENSITIVE_ORDER`.

- **Ordre des clés** : `HashMap` n'ordonne pas, `LinkedHashMap` conserve l'ordre
  d'insertion, `TreeMap` trie. Pour une config relue par un humain, l'ordre
  d'apparition (`LinkedHashMap`) est souvent le plus agréable.

- **`Files.readAllLines` vs `Files.lines`** : `readAllLines` charge tout en
  mémoire (simple, suffisant pour une config de quelques dizaines de lignes) ;
  `Files.lines` (flux paresseux, à fermer en `try-with-resources`) serait
  préférable pour un très gros fichier.
