# Exercice 3.2.1 — Rectangle

## Contexte

Sur un plan de cantonnement, chaque zone est un rectangle. Une zone n'a de sens
que si ses deux dimensions sont strictement positives : votre classe doit
garantir cet invariant.

## Énoncé

Complétez la classe `Rectangle` fournie dans le `starter/`. Elle doit garantir
que la largeur et la hauteur restent **strictement positives**, et proposer :

- un constructeur `Rectangle(double largeur, double hauteur)` : toute dimension
  inférieure ou égale à 0 est **corrigée à `1.0`** ;
- un constructeur `Rectangle(double cote)` pour un carré, qui **chaîne** vers le
  précédent via `this(cote, cote)` ;
- les accesseurs `getLargeur()` et `getHauteur()` ;
- les mutateurs `setLargeur(double)` et `setHauteur(double)` : une valeur
  inférieure ou égale à 0 est **ignorée** (l'objet reste inchangé) ;
- `aire()` et `perimetre()` ;
- une redéfinition de `toString()` au format `Rectangle <largeur> x <hauteur>`.

## Exemple

```text
Rectangle r = new Rectangle(3.0, 4.0);
r.aire();          // 12.0
r.perimetre();     // 14.0
r.setLargeur(-1);  // ignoré : largeur reste 3.0
r.toString();      // "Rectangle 3.0 x 4.0"

Rectangle carre = new Rectangle(5.0);  // 5.0 x 5.0
```

## Contraintes

- La classe doit s'appeler `Rectangle` et rester dans le package `piscine.m3`.
- **Ne modifiez pas les signatures publiques** : les tests s'appuient dessus.
- Les attributs doivent être **privés**.
- Aucune exception : une valeur invalide est **corrigée** (constructeur) ou
  **ignorée** (setter).
- Le constructeur carré doit **chaîner** avec `this(cote, cote)`.
- `toString()` utilise `java.util.Locale.ROOT`.

## Ce qui sera vérifié

- La construction, les accesseurs, `aire` et `perimetre` sont corrects.
- Une dimension `<= 0` au constructeur est corrigée à `1.0` (y compris le carré).
- Un setter avec une valeur `<= 0` laisse l'objet inchangé.
- Le constructeur carré produit `largeur == hauteur`.
- `toString` respecte le format attendu.

## Pour aller plus loin (optionnel — non noté)

- Comment signaleriez-vous proprement une valeur invalide plutôt que de la
  corriger en silence ? (Les exceptions sont vues au module 5.)
- Ajoutez une méthode `estCarre()` qui indique si le rectangle est un carré.
