# Correction — Exercice 3.3.3 Hiérarchie de personnels

## Démarche attendue

1. Chaque sous-classe `extends Personnel` et appelle `super(nom)` dans son
   constructeur (première instruction).
2. `Officier` : champ `private int echelon;`. `grade()` renvoie `"Officier"` ;
   `solde()` renvoie `3000 + 200 * echelon`.
3. `SousOfficier` : champ `private int anciennete;`. `grade()` = `"Sous-officier"`
   ; `solde()` = `2200 + 100 * anciennete`.
4. `MilitaireDuRang` : pas de champ supplémentaire. `grade()` =
   `"Militaire du rang"` ; `solde()` = `1600`.

## Points clés

- **Classe abstraite** : `Personnel` factorise `nom`, `getNom()` et `fiche()`,
  et impose `grade()`/`solde()` aux enfants.
- **Polymorphisme** : `fiche()` est écrite une seule fois mais affiche le bon
  grade et la bonne solde selon le type réel.
- **`super(nom)`** : initialise la partie héritée de l'objet.

## Erreurs fréquentes observées

- Oublier `super(nom)` ou le placer après une autre instruction.
- Texte de grade inexact (`"sous-officier"` minuscule, accents).
- Mauvaise formule de solde (coefficients inversés).

## Pour approfondir

- La différence entre méthode abstraite (imposée) et méthode concrète héritée.
