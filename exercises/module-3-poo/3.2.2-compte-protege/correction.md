# Correction — Exercice 3.2.2 Compte protégé

## Démarche attendue

1. Déclarer `private final String titulaire;` et `private double solde;`.
2. Constructeur : corriger le solde initial négatif —
   `this.solde = soldeInitial < 0 ? 0.0 : soldeInitial;`.
3. `deposer` : `if (montant > 0) this.solde += montant;`.
4. `retirer` : `if (montant > 0 && montant <= solde) this.solde -= montant;`.
5. `toString` : `String.format(Locale.ROOT, "Compte de %s : %.2f €", titulaire, solde)`.

## Points clés

- **Invariant métier** : « solde ≥ 0 ». La garde de `retirer`
  (`montant <= solde`) l'assure à chaque opération.
- **Refus silencieux** : sans exceptions (module 5), une opération invalide ne
  fait rien — l'objet reste cohérent. C'est la suite directe du compte « ouvert »
  du sous-groupe 3.1, auquel on ajoute les gardes.
- **Double condition** dans `retirer` : montant positif ET couvert par le solde.

## Erreurs fréquentes observées

- Oublier la condition `montant <= solde` : le solde peut devenir négatif.
- Tester `montant < solde` (strict) au lieu de `<=` : un retrait du solde entier
  serait refusé à tort.
- Autoriser les montants négatifs (`deposer(-x)` qui débite).

## Pour approfondir

- La notion d'invariant de classe maintenu par toutes les méthodes mutatrices.
