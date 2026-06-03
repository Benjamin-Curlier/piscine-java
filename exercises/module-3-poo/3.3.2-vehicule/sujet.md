# Exercice 3.3.2 — Hiérarchie de véhicules

## Contexte

Le parc automobile de l'unité comporte des voitures et des motos. Toutes
partagent une marque et une vitesse maximale, mais se décrivent différemment.

## Énoncé

La classe de base `Vehicule` vous est **fournie** (ne la modifiez pas) : elle a
une `marque`, une `vitesseMax` (toutes deux `protected`), leurs accesseurs et une
méthode `decrire()` renvoyant `Vehicule <marque> (<vitesseMax> km/h)`.

Complétez les deux sous-classes du `starter/` :

- `Voiture` : ajoute un nombre de portes. Constructeur
  `Voiture(String marque, int vitesseMax, int nbPortes)` qui appelle
  `super(...)`. Accesseur `getNbPortes()`. `decrire()` redéfini :
  `Voiture <marque> (<vitesseMax> km/h, <nbPortes> portes)`.
- `Moto` : constructeur `Moto(String marque, int vitesseMax)` qui appelle
  `super(...)`. `decrire()` redéfini : `Moto <marque> (<vitesseMax> km/h)`.

## Exemple

```text
new Voiture("Peugeot", 200, 5).decrire();  // "Voiture Peugeot (200 km/h, 5 portes)"
new Moto("Yamaha", 220).decrire();         // "Moto Yamaha (220 km/h)"
```

## Contraintes

- Package `etnc.m3`. **Ne modifiez pas les signatures publiques** ni `Vehicule`.
- Le constructeur de chaque sous-classe doit appeler `super(...)`.
- Le champ `nbPortes` de `Voiture` doit être **privé**.
- Chaque `decrire()` redéfini porte `@Override`.

## Ce qui sera vérifié

- `decrire()` correct pour `Vehicule`, `Voiture` et `Moto`.
- Les accesseurs hérités (`getMarque`, `getVitesseMax`) fonctionnent sur les
  sous-classes.
- L'appel **via le type de base** `Vehicule` exécute la version redéfinie.

## Pour aller plus loin (optionnel — non noté)

- Faites en sorte que `Voiture.decrire()` réutilise `super.decrire()`.
- Ajoutez une sous-classe `Camion` avec une charge utile.
