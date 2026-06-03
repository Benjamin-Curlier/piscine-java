# Exercice 3.3.3 — Hiérarchie de personnels

## Contexte

Le système de gestion du personnel distingue trois catégories de militaires :
officiers, sous-officiers et militaires du rang. Toutes partagent un nom mais
ont un grade et un calcul de solde différents.

## Énoncé

La classe abstraite `Personnel` vous est **fournie** (ne la modifiez pas) : elle
a un `nom` (`protected`), un accesseur `getNom()`, deux méthodes **abstraites**
`grade()` et `solde()`, et une méthode concrète `fiche()` au format
`<nom> — <grade> — <solde> €`.

Complétez les trois sous-classes du `starter/` :

- `Officier(String nom, int echelon)` : `grade()` = `"Officier"`,
  `solde()` = `3000 + 200 * echelon`.
- `SousOfficier(String nom, int anciennete)` : `grade()` = `"Sous-officier"`,
  `solde()` = `2200 + 100 * anciennete`.
- `MilitaireDuRang(String nom)` : `grade()` = `"Militaire du rang"`,
  `solde()` = `1600`.

Chaque constructeur appelle `super(nom)`.

## Exemple

```text
new Officier("Durand", 3).solde();   // 3600.0
new Officier("Durand", 3).fiche();   // "Durand — Officier — 3600.00 €"
```

## Contraintes

- Package `etnc.m3`. **Ne modifiez pas les signatures publiques** ni `Personnel`.
- Les champs spécifiques (`echelon`, `anciennete`) doivent être **privés**.
- Chaque constructeur appelle `super(nom)` ; chaque méthode redéfinie porte
  `@Override`.

## Ce qui sera vérifié

- `grade()` et `solde()` corrects pour les trois catégories (y compris échelon 0).
- `getNom()` hérité fonctionne sur chaque sous-classe.
- `fiche()`, appelée même **via le type de base** `Personnel`, combine les bonnes
  versions (polymorphisme) et respecte le format.

## Pour aller plus loin (optionnel — non noté)

- Ajoutez une prime d'ancienneté commune calculée dans `Personnel`.
- Quelle catégorie ajouteriez-vous, et quelle méthode resterait à spécialiser ?
