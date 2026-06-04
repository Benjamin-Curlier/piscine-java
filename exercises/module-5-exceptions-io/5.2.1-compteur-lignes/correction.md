# Correction — Exercice 5.2.1 Compteur de lignes (Files.lines + try-with-resources)

## Démarche attendue

```java
public static long compterLignes(Path fichier) throws IOException {
    // Files.lines ouvre un flux de lignes ; il est AutoCloseable → try-with-resources
    // obligatoire pour garantir la fermeture même en cas d'IOException durant count().
    try (Stream<String> flux = Files.lines(fichier, StandardCharsets.UTF_8)) {
        return flux.count();
    }
}

public static long compterLignesNonVides(Path fichier) throws IOException {
    // String.isBlank() renvoie true pour "", " ", "\t\t", etc.
    // On garde uniquement les lignes pour lesquelles isBlank() est false.
    try (Stream<String> flux = Files.lines(fichier, StandardCharsets.UTF_8)) {
        return flux.filter(ligne -> !ligne.isBlank()).count();
    }
}
```

## Points clés

- **`try-with-resources` sur `Stream<String>`** : `Files.lines` maintient un
  descripteur de fichier ouvert jusqu'à la fermeture du flux. Sans
  `try-with-resources`, si `count()` ou `filter()` levait une exception, le
  descripteur resterait ouvert (fuite de ressource). L'idiome `try (Stream<String>
  flux = …) { … }` appelle `flux.close()` à la sortie du bloc, qu'il y ait
  exception ou non.

- **`StandardCharsets.UTF_8` explicite** : sans charset précisé, `Files.lines`
  utilise l'encodage par défaut de la JVM, qui peut différer entre Windows
  (souvent CP-1252) et Linux (UTF-8). Passer `StandardCharsets.UTF_8` garantit
  le même comportement partout — indispensable pour les fichiers contenant des
  accents.

- **`String.isBlank()` vs `String.isEmpty()`** : `isEmpty()` ne renvoie `true`
  que si la chaîne est vide (`""`). `isBlank()` renvoie `true` aussi pour les
  chaînes ne contenant que des espaces (`" "`, `"\t"`, `"  \t  "`). L'énoncé
  demande d'ignorer les lignes **blanches**, donc `isBlank()` est le bon choix.

- **`throws IOException` propagée** : la méthode ne rattrape pas l'exception —
  elle la propage à l'appelant avec `throws IOException`. Ce n'est pas de la
  paresse : signaler l'erreur à l'appelant (le test, l'application) permet une
  meilleure prise en charge que de l'absorber silencieusement.

## Erreurs fréquentes observées

- **Oublier le `try-with-resources`** : le code compile et passe les tests, mais
  le critère formateur `idiomatisme` est raté, et en production le fichier reste
  ouvert.

- **Passer `Charset` incorrect ou oublier l'UTF-8** : les tests avec accents
  échouent sur Windows si l'encodage par défaut n'est pas UTF-8.

- **Utiliser `isEmpty()` au lieu de `isBlank()`** : une ligne `"   "` n'est pas
  vide (pas `isEmpty()`), mais elle est bien blanche (`isBlank()`). Les tests
  avec des lignes d'espaces échouent.

- **Charger toutes les lignes en mémoire** (`Files.readAllLines`) : fonctionne
  mais n'est pas idiomatique pour un comptage — l'énoncé demande explicitement
  `Files.lines` (flux évalué à la demande).

## Pour approfondir

- `Files.lines` évalue les lignes **paresseusement** (lazy) : elles ne sont pas
  toutes chargées en mémoire simultanément, contrairement à `Files.readAllLines`
  qui retourne une `List<String>`. Pour un fichier de plusieurs gigaoctets,
  `Files.lines` peut traiter le flux ligne par ligne avec une empreinte mémoire
  constante.

- `Stream<String>` implémente `AutoCloseable` précisément pour permettre le
  `try-with-resources`. C'est un pattern général en Java pour les ressources
  d'E/S : connexion base de données, socket réseau, curseur de résultat SQL —
  tous sont `AutoCloseable`.

- Pour compter les lignes d'un très grand fichier sans les charger : on pourrait
  aussi utiliser `Files.newBufferedReader` + une boucle, mais c'est moins concis
  et moins idiomatique que le pipeline `Files.lines().count()`.
