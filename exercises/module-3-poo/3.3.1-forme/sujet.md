# Exercice 3.3.1 — Formes géométriques

## Contexte

Un logiciel de cartographie manipule des zones de formes variées. Plutôt que de
multiplier les cas particuliers, vous modélisez une **forme** générale dont
chaque type concret sait calculer son aire et son périmètre.

## Énoncé

La classe abstraite `Forme` vous est **fournie** (ne la modifiez pas) :

```java
public abstract class Forme {
    public abstract double aire();
    public abstract double perimetre();
    public String decrire() { /* déjà écrit : utilise aire() et perimetre() */ }
}
```

Complétez les deux sous-classes du `starter/` :

- `Cercle` : défini par un `rayon` (constructeur `Cercle(double rayon)`,
  accesseur `getRayon()`). `aire()` = π·r², `perimetre()` = 2·π·r.
- `Rectangle` : défini par `largeur` et `hauteur` (constructeur
  `Rectangle(double largeur, double hauteur)`, accesseurs). `aire()` =
  largeur·hauteur, `perimetre()` = 2·(largeur + hauteur).

## Exemple

```text
Forme f = new Cercle(1.0);
f.aire();                       // π ≈ 3.14159...
new Rectangle(3.0, 4.0).decrire();  // "aire = 12.00, perimetre = 14.00"
```

## Contraintes

- Classes dans le package `etnc.m3`. **Ne modifiez pas les signatures
  publiques** ni la classe `Forme`.
- Les champs des sous-classes doivent être **privés**.
- Utilisez `Math.PI` pour le cercle.

## Ce qui sera vérifié

- `aire()` et `perimetre()` corrects pour `Cercle` et `Rectangle`.
- L'appel **via le type de base** `Forme` exécute la bonne version (polymorphisme).
- `decrire()` produit le format attendu (deux décimales, point décimal).

## Pour aller plus loin (optionnel — non noté)

- Ajoutez une sous-classe `Triangle` (base et hauteur).
- Pourquoi `Forme` ne peut-elle pas être instanciée directement avec `new` ?
