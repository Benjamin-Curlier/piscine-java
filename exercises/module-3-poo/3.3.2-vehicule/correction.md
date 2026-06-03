# Correction — Exercice 3.3.2 Hiérarchie de véhicules

## Démarche attendue

1. `Voiture extends Vehicule` : champ `private int nbPortes;`. Le constructeur
   appelle `super(marque, vitesseMax);` **en première instruction**, puis
   `this.nbPortes = nbPortes;`.
2. `Voiture.decrire()` renvoie
   `"Voiture " + marque + " (" + vitesseMax + " km/h, " + nbPortes + " portes)"`.
   (`marque`/`vitesseMax` sont accessibles car `protected`.)
3. `Moto extends Vehicule` : constructeur `super(marque, vitesseMax);`.
   `decrire()` renvoie `"Moto " + marque + " (" + vitesseMax + " km/h)"`.

## Points clés

- **`super(...)`** : appelle le constructeur parent ; obligatoire en première
  instruction car `Vehicule` n'a pas de constructeur sans argument.
- **`@Override`** : redéfinit la méthode du parent ; la version exécutée dépend
  du type réel (polymorphisme), même via une référence `Vehicule`.
- **`protected`** : les champs du parent sont visibles dans les sous-classes.

## Erreurs fréquentes observées

- Oublier `super(...)` (la classe ne compile pas).
- Redéclarer `marque`/`vitesseMax` dans la sous-classe au lieu de réutiliser
  ceux du parent.
- Faute de frappe dans le texte de `decrire()` (espaces, virgule).

## Pour approfondir

- `super.methode()` pour réutiliser le comportement du parent.
