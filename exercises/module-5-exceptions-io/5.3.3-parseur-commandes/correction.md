# Correction — Exercice 5.3.3 Parseur de commandes texte (split + switch + exceptions)

## Démarche attendue

```java
public static int executer(String ligne) {
    // Garde 1 : ligne vide ou null → exception explicite avant tout traitement
    if (ligne == null || ligne.isBlank()) {
        throw new IllegalArgumentException("commande vide");
    }

    // Découpage : trim() absorbe les espaces en tête/queue,
    // split("\\s+") gère les espaces multiples entre tokens.
    String[] tokens = ligne.trim().split("\\s+");
    String cmd = tokens[0];

    // Dispatch sur la commande — switch lisible, un cas par commande
    switch (cmd) {
        case "somme" -> {
            // Valider le nombre d'arguments AVANT Integer.parseInt
            if (tokens.length != 3) {
                throw new IllegalArgumentException("arguments invalides pour " + cmd);
            }
            int a = Integer.parseInt(tokens[1]);
            int b = Integer.parseInt(tokens[2]);
            return a + b;
        }
        case "diff" -> {
            if (tokens.length != 3) {
                throw new IllegalArgumentException("arguments invalides pour " + cmd);
            }
            int a = Integer.parseInt(tokens[1]);
            int b = Integer.parseInt(tokens[2]);
            return a - b;
        }
        case "produit" -> {
            if (tokens.length != 3) {
                throw new IllegalArgumentException("arguments invalides pour " + cmd);
            }
            int a = Integer.parseInt(tokens[1]);
            int b = Integer.parseInt(tokens[2]);
            return a * b;
        }
        case "oppose" -> {
            if (tokens.length != 2) {
                throw new IllegalArgumentException("arguments invalides pour " + cmd);
            }
            return -Integer.parseInt(tokens[1]);
        }
        default -> throw new IllegalArgumentException("commande inconnue : " + cmd);
    }
}
```

## Points clés

- **Garde `isBlank` en premier** : si on appelait `split` sur `null` ou une
  chaîne blanche, on obtiendrait un `NullPointerException` ou un tableau
  d'un seul token vide — difficile à diagnostiquer. La garde explicite produit
  un message clair : `"commande vide"`.

- **`trim()` avant `split("\\s+")`** : `split` sur `"  oppose 5 "` sans
  `trim()` préalable produirait `["", "oppose", "5"]` (token vide en tête) —
  la commande serait `""`, non reconnue. Le `trim()` nettoie les bords.

- **Validation du nombre d'arguments AVANT `Integer.parseInt`** : si l'on
  appelait `parseInt` d'abord, un `ArrayIndexOutOfBoundsException` (argument
  manquant) ou un résultat silencieusement faux (argument en trop ignoré)
  pourraient survenir. La vérification explicite donne un message utile :
  `"arguments invalides pour somme"`.

- **`NumberFormatException` propagée** : `Integer.parseInt("a")` lève une
  `NumberFormatException`. On ne l'attrape pas — elle remonte naturellement à
  l'appelant avec le message généré par la JVM (`For input string: "a"`).
  C'est un choix délibéré : la conversion échouée est une erreur de l'appelant,
  pas une erreur interne du parseur.

- **Casse sensible** : `"SOMME 1 2"` tombe dans le `default` et lève
  `IllegalArgumentException("commande inconnue : SOMME")`. On ne normalise pas
  la casse car le protocole de commande est défini en minuscules.

## Erreurs fréquentes observées

- **Omettre le `trim()` avant `split`** : les espaces en tête de chaîne
  génèrent un token vide `""` qui ne correspond à aucune commande — toutes les
  lignes avec espaces en tête échouent comme « inconnues ».

- **Vérifier `tokens.length == 0`** : impossible après `split("\\s+")` sur une
  chaîne non blanche — il y a toujours au moins un token. La garde `isBlank`
  est suffisante ; tester `tokens.length == 0` est un faux sentiment de sécurité.

- **Attraper `NumberFormatException` pour la re-lever** :
  `catch (NumberFormatException e) { throw e; }` est du code inutile.
  Laisser simplement remonter suffit.

- **Utiliser `if/else if` au lieu de `switch`** : fonctionne, mais moins
  lisible et ne satisfait pas le critère formateur `idiomatisme`.

## Pour approfondir

- Les **switch expressions** de Java 14+ (`switch (cmd) { case "somme" -> …;
  default -> …; }`) permettent de retourner une valeur directement et
  éliminent les `break`. On peut aussi écrire le dispatch de façon encore plus
  concise en extrayant les arguments dans des variables locales communes.

- Pour un protocole plus riche, on pourrait enregistrer les commandes dans une
  `Map<String, Function<int[], Integer>>` — mais pour 4 commandes fixes, le
  `switch` explicite est plus lisible et ne nécessite pas de lambdas.

- `Integer.parseInt` accepte les nombres négatifs (`"-3"`) car le signe `−`
  fait partie de la syntaxe entière Java. `split("\\s+")` ne décompose pas
  `"-3"` — le tiret n'est pas un espace.
