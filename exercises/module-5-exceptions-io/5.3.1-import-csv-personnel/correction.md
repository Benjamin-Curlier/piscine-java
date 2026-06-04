# Correction — Exercice 5.3.1 Import CSV de personnel

## Démarche attendue

```java
public static List<Personnel> importer(Path csv) throws IOException {
    // Lecture complète en UTF-8 explicite (les accents seraient cassés sinon).
    List<String> lignes = Files.readAllLines(csv, StandardCharsets.UTF_8);

    List<Personnel> personnels = new ArrayList<>();
    boolean premiere = true;
    for (String ligne : lignes) {
        if (premiere) {          // 1) saut de l'en-tête
            premiere = false;
            continue;
        }
        if (ligne.isBlank()) {   // 2) on ignore les lignes blanches
            continue;
        }

        String[] champs = ligne.split(",");
        if (champs.length != 3) { // 3) validation AVANT toute conversion
            throw new IllegalArgumentException(
                    "ligne CSV invalide (3 champs attendus) : " + ligne);
        }

        Grade grade;
        try {                     // 4) conversion gardée + chaînage de la cause
            grade = Grade.valueOf(champs[1].trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "grade inconnu : " + champs[1].trim() + " (ligne : " + ligne + ")", e);
        }

        int anciennete;
        try {                     // 5) idem pour l'ancienneté
            anciennete = Integer.parseInt(champs[2].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "anciennete invalide : " + champs[2].trim() + " (ligne : " + ligne + ")", e);
        }

        personnels.add(new Personnel(champs[0].trim(), grade, anciennete));
    }
    return personnels;
}
```

## Points clés

- **Ordre des contrôles.** On valide le **nombre de champs avant** d'accéder à
  `champs[1]` / `champs[2]`. Sans cette garde, une ligne à 2 champs provoquerait
  un `ArrayIndexOutOfBoundsException` peu parlant au lieu d'un message métier
  clair. La validation défensive précède toujours l'utilisation.

- **Chaînage de la cause (`new IllegalArgumentException(message, cause)`).** Les
  deux conversions peuvent échouer : `Grade.valueOf` lève une
  `IllegalArgumentException`, `Integer.parseInt` une `NumberFormatException`. On
  les **rattrape** pour fournir un message lisible (« grade inconnu : GENERAL »)
  **sans perdre** l'exception d'origine : on la passe en 2e argument du
  constructeur. Le `stack trace` affichera alors `Caused by: ...`, ce qui permet
  de diagnostiquer la racine du problème.

- **`NumberFormatException` est une sous-classe de `IllegalArgumentException`.**
  C'est pour cela que l'on rattrape spécifiquement `NumberFormatException` pour
  l'ancienneté : être précis sur le type rattrapé évite d'avaler par erreur une
  autre `IllegalArgumentException`. Côté appelant, tout remonte comme
  `IllegalArgumentException` — un seul type d'erreur métier à gérer.

- **`trim()` sur chaque champ.** Un fichier réel contient souvent des espaces
  parasites (« SERGENT » entouré d'espaces). `trim()` les supprime avant
  `Grade.valueOf` (qui est sensible à la casse **et** aux espaces) et avant
  `Integer.parseInt`.

- **UTF-8 explicite.** `Files.readAllLines(csv, StandardCharsets.UTF_8)` : sans
  le charset, l'encodage par défaut de la JVM (CP-1252 sur Windows) corromprait
  les noms accentués.

- **`split(",")` — CSV simple.** On suppose qu'aucun champ ne contient de
  virgule. C'est la **limite assumée** de l'exercice : un vrai parseur CSV gère
  les guillemets et les virgules échappées.

## Erreurs fréquentes observées

- **Oublier de chaîner la cause** : `throw new IllegalArgumentException("grade
  inconnu")` sans le `e`. Le code « marche », mais on perd l'origine — les tests
  privés sur `getCause()` échouent, et le critère `demarche` est raté.

- **Accéder à `champs[2]` avant de vérifier `champs.length`** : provoque un
  `ArrayIndexOutOfBoundsException` sur les lignes trop courtes au lieu du message
  métier attendu.

- **Ne pas sauter l'en-tête** : le programme tente de convertir `grade` à partir
  de la chaîne `"grade"` → `IllegalArgumentException` sur la 1re ligne.

- **Oublier `trim()`** : `Grade.valueOf(" SERGENT")` échoue (espace de tête), et
  `Integer.parseInt(" 5 ")` lève une `NumberFormatException`.

- **Ne pas ignorer les lignes blanches** : une ligne vide donne `split(",")` →
  `[""]` (1 champ) → `IllegalArgumentException` non désirée.

## Pour approfondir

- **`Files.readAllLines` vs `Files.lines`** : ici on charge tout en mémoire car
  on construit une liste de toute façon. Pour un très gros fichier, `Files.lines`
  (flux paresseux dans un `try-with-resources`) serait plus économe — mais le
  parsing avec validation ligne par ligne reste identique.

- **Chaîne d'exceptions** : `IllegalArgumentException` → `Caused by:
  NumberFormatException`. C'est la même mécanique que pour relancer une exception
  métier au-dessus d'une exception technique (ex. `ServiceException` au-dessus
  d'une `SQLException`). On enrichit le message sans masquer l'origine.

- **Pourquoi `IllegalArgumentException` et pas une exception personnalisée ?**
  Pour un exercice, l'exception standard suffit et reste explicite. Dans une vraie
  application, on créerait souvent une `CsvParseException` portant le numéro de
  ligne.
