# Correction — Exercice 3.1.2 Compte bancaire simple

## Démarche attendue

1. Déclarer `private final String titulaire;` et `private double solde;`.
2. Le constructeur affecte les deux champs (`this.titulaire = titulaire;`,
   `this.solde = soldeInitial;`).
3. Les accesseurs renvoient `titulaire` et `solde`.
4. `deposer` ajoute au solde : `this.solde += montant;`.
5. `retirer` retranche du solde : `this.solde -= montant;` (sans contrôle).
6. `toString` renvoie
   `String.format(Locale.ROOT, "Compte de %s : %.2f €", titulaire, solde)`.

## Points clés

- **`final` sur le titulaire** : le nom ne change pas après l'ouverture, le
  solde si. Marquer l'immuabilité quand c'est vrai est un bon réflexe.
- **Pas de validation ici** : c'est volontaire. La progression du sous-groupe
  3.2 ajoutera les invariants (refus du découvert, montants négatifs).
- **Format monétaire** : `%.2f` + `Locale.ROOT` pour un point décimal stable.

## Erreurs fréquentes observées

- Inverser `+=` et `-=` entre `deposer` et `retirer`.
- Ajouter une garde « solde >= montant » : prématuré pour ce sous-groupe, et
  fait échouer le test du solde négatif.
- Formater le solde avec une virgule (machine en français) → test en échec.

## Pour approfondir

- La classe `java.math.BigDecimal` pour les calculs monétaires exacts (au-delà
  de ce module).
