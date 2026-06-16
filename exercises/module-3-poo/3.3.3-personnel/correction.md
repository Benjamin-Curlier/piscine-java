# Correction — Exercice 3.3.3 Hiérarchie de personnels

## Démarche attendue

1. Chaque sous-classe `extends Personnel` et appelle `super(nom)` dans son
   constructeur (première instruction).
2. `Senior` : champ `private int echelon;`. `niveau()` renvoie `"Senior"` ;
   `solde()` renvoie `3000 + 200 * echelon`.
3. `Confirme` : champ `private int anciennete;`. `niveau()` = `"Confirmé"`
   ; `solde()` = `2200 + 100 * anciennete`.
4. `Junior` : pas de champ supplémentaire. `niveau()` =
   `"Junior"` ; `solde()` = `1600`.

## Points clés

- **Classe abstraite** : `Personnel` factorise `nom`, `getNom()` et `fiche()`,
  et impose `niveau()`/`solde()` aux enfants.
- **Polymorphisme** : `fiche()` est écrite une seule fois mais affiche le bon
  niveau et le bon salaire selon le type réel.
- **`super(nom)`** : initialise la partie héritée de l'objet.

## Erreurs fréquentes observées

- Oublier `super(nom)` ou le placer après une autre instruction.
- Texte de niveau inexact (`"senior"` minuscule, accents).
- Mauvaise formule de salaire (coefficients inversés).

## Pour approfondir

- La différence entre méthode abstraite (imposée) et méthode concrète héritée.
