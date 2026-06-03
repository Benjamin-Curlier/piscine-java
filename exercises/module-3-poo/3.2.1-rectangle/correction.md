# Correction — Exercice 3.2.1 Rectangle

## Démarche attendue

1. Déclarer `private double largeur;` et `private double hauteur;`.
2. Dans `Rectangle(double, double)`, corriger chaque dimension :
   `this.largeur = largeur > 0 ? largeur : 1.0;` (idem hauteur).
3. Dans `Rectangle(double cote)`, **chaîner** : `this(cote, cote);` (doit être la
   première instruction). La correction est ainsi mutualisée.
4. Les setters ne modifient que si la valeur est `> 0` (`if (largeur > 0) ...`).
5. `aire` renvoie `largeur * hauteur` ; `perimetre` renvoie `2 * (largeur + hauteur)`.
6. `toString` : `String.format(Locale.ROOT, "Rectangle %s x %s", largeur, hauteur)`.

## Points clés

- **Invariant** : « dimensions strictement positives », garanti à la création
  (correction) et préservé par les setters (refus).
- **Chaînage `this(...)`** : évite de dupliquer la logique de correction entre
  les deux constructeurs. L'appel doit être la **première instruction**.
- **Refus silencieux** : sans exceptions (module 5), un setter invalide ne fait
  rien — l'objet reste cohérent.

## Erreurs fréquentes observées

- Dupliquer la correction dans le constructeur carré au lieu de `this(cote, cote)`.
- Oublier le `?:` ou la condition : une dimension négative passe alors telle quelle.
- Setter sans garde (`this.largeur = largeur;`) : l'invariant n'est plus protégé.

## Pour approfondir

- L'opérateur ternaire `condition ? a : b`.
- La règle « `this(...)` en première instruction » d'un constructeur.
