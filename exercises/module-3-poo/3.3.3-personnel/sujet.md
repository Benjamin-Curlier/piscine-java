# Exercice 3.3.3 — Hiérarchie de personnels

## Contexte

Le système de gestion du personnel distingue trois niveaux de séniorité :
seniors, confirmés et juniors. Tous partagent un nom mais ont un niveau et un
calcul de salaire différents.

## Énoncé

La classe abstraite `Personnel` vous est **fournie** (ne la modifiez pas) : elle
a un `nom` (`protected`), un accesseur `getNom()`, deux méthodes **abstraites**
`niveau()` et `solde()`, et une méthode concrète `fiche()` au format
`<nom> — <niveau> — <solde> €`.

Complétez les trois sous-classes du `starter/` :

- `Senior(String nom, int echelon)` : `niveau()` = `"Senior"`,
  `solde()` = `3000 + 200 * echelon`.
- `Confirme(String nom, int anciennete)` : `niveau()` = `"Confirmé"`,
  `solde()` = `2200 + 100 * anciennete`.
- `Junior(String nom)` : `niveau()` = `"Junior"`,
  `solde()` = `1600`.

Chaque constructeur appelle `super(nom)`.

## Exemple

```text
new Senior("Durand", 3).solde();   // 3600.0
new Senior("Durand", 3).fiche();   // "Durand — Senior — 3600.00 €"
```

## Contraintes

- Package `piscine.m3`. **Ne modifiez pas les signatures publiques** ni `Personnel`.
- Les champs spécifiques (`echelon`, `anciennete`) doivent être **privés**.
- Chaque constructeur appelle `super(nom)` ; chaque méthode redéfinie porte
  `@Override`.

## Ce qui sera vérifié

- `niveau()` et `solde()` corrects pour les trois catégories (y compris échelon 0).
- `getNom()` hérité fonctionne sur chaque sous-classe.
- `fiche()`, appelée même **via le type de base** `Personnel`, combine les bonnes
  versions (polymorphisme) et respecte le format.

## Pour aller plus loin (optionnel — non noté)

- Ajoutez une prime d'ancienneté commune calculée dans `Personnel`.
- Quelle catégorie ajouteriez-vous, et quelle méthode resterait à spécialiser ?
