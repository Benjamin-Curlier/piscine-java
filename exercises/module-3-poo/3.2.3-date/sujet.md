# Exercice 3.2.3 — Date simple

## Contexte

Un journal de bord enregistre des dates. Pour rester exploitable, chaque date
doit avoir un jour et un mois dans des plages valides.

## Énoncé

Complétez la classe `Date` fournie dans le `starter/`. Elle représente une date
(jour, mois, année) et doit proposer :

- un constructeur `Date(int jour, int mois, int annee)` : le jour est **borné à
  l'intervalle [1..31]** et le mois à **[1..12]** (une valeur hors plage est
  ramenée à la borne la plus proche) ; l'année n'est pas contrainte ;
- les accesseurs `getJour()`, `getMois()`, `getAnnee()` ;
- `setJour(int)` : modifie le jour seulement s'il est dans [1..31] (sinon
  inchangé) ;
- `setMois(int)` : modifie le mois seulement s'il est dans [1..12] (sinon
  inchangé) ;
- une redéfinition de `toString()` au format `JJ/MM/AAAA` (avec zéro-padding).

## Exemple

```text
Date d = new Date(7, 3, 2026);
d.toString();    // "07/03/2026"
d.setMois(13);   // ignoré : mois reste 3
new Date(40, 1, 2026).getJour();  // 31 (borné)
```

## Contraintes

- La classe doit s'appeler `Date` et rester dans le package `etnc.m3`.
- **Ne modifiez pas les signatures publiques** : les tests s'appuient dessus.
- Les attributs doivent être **privés**.
- Aucune exception : une valeur hors plage est **bornée** (constructeur) ou
  **ignorée** (setter).
- `toString()` produit `JJ/MM/AAAA` avec zéro-padding (astuce :
  `String.format("%02d/%02d/%04d", jour, mois, annee)`).

## Ce qui sera vérifié

- La construction, les accesseurs et `toString` sont corrects.
- Un jour hors [1..31] est borné ; un mois hors [1..12] est borné.
- Un setter avec une valeur hors plage laisse l'objet inchangé.
- Le zéro-padding est respecté (jour/mois à un chiffre affichés sur deux).

## Pour aller plus loin (optionnel — non noté)

- Comment limiteriez-vous le jour selon le mois (28/30/31) et l'année
  bissextile ?
- Ajoutez une méthode `estAvant(Date autre)` qui compare deux dates.
