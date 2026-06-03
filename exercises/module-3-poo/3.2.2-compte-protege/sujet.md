# Exercice 3.2.2 — Compte protégé

## Contexte

La trésorerie veut un compte plus robuste que celui du sous-groupe précédent :
impossible de descendre sous zéro, et les opérations absurdes sont refusées.

## Énoncé

Complétez la classe `CompteProtege` fournie dans le `starter/`. Elle garantit
l'invariant **« le solde n'est jamais négatif »** et doit proposer :

- un constructeur `CompteProtege(String titulaire, double soldeInitial)` : un
  solde initial strictement négatif est **corrigé à `0.0`** ;
- les accesseurs `getTitulaire()` et `getSolde()` ;
- `deposer(double montant)` : un montant inférieur ou égal à 0 est **ignoré** ;
- `retirer(double montant)` : refusé si le montant est inférieur ou égal à 0
  **ou** supérieur au solde (l'objet reste inchangé) ;
- une redéfinition de `toString()` au format `Compte de <titulaire> : <solde> €`.

## Exemple

```text
CompteProtege c = new CompteProtege("Dupont", 100.0);
c.deposer(50.0);   // solde = 150.0
c.retirer(200.0);  // refusé : solde reste 150.0
c.retirer(50.0);   // solde = 100.0
c.toString();      // "Compte de Dupont : 100.00 €"
```

## Contraintes

- La classe doit s'appeler `CompteProtege` et rester dans le package `etnc.m3`.
- **Ne modifiez pas les signatures publiques** : les tests s'appuient dessus.
- Les attributs doivent être **privés**.
- Aucune exception : une opération invalide est **corrigée** (constructeur) ou
  **refusée** (l'objet reste inchangé). Le solde ne devient **jamais négatif**.
- `toString()` formate le solde à deux décimales (`java.util.Locale.ROOT`).

## Ce qui sera vérifié

- La construction, les accesseurs et `toString` sont corrects.
- Un solde initial négatif est corrigé à `0.0`.
- `deposer` ignore un montant `<= 0`.
- `retirer` refuse un montant `<= 0` ou supérieur au solde ; un retrait égal au
  solde amène le solde à `0.0`.

## Pour aller plus loin (optionnel — non noté)

- Comment signaleriez-vous un refus plutôt que de l'ignorer en silence ?
  (Les exceptions sont vues au module 5.)
- Ajoutez un plafond de dépôt journalier.
