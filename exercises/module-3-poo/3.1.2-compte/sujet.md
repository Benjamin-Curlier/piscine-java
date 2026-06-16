# Exercice 3.1.2 — Compte bancaire simple

## Contexte

La trésorerie de l'entreprise tient un compte par service. On veut un objet simple
capable d'enregistrer des dépôts et des retraits et de connaître son solde.

## Énoncé

Complétez la classe `Compte` fournie dans le `starter/`. Elle représente un
compte avec un titulaire et un solde, et doit proposer :

- un constructeur `Compte(String titulaire, double soldeInitial)` ;
- deux accesseurs `getTitulaire()` et `getSolde()` ;
- une méthode `deposer(double montant)` qui **crédite** le compte ;
- une méthode `retirer(double montant)` qui **débite** le compte ;
- une redéfinition de `toString()` au format
  `Compte de <titulaire> : <solde> €` (solde à deux décimales).

> À ce stade, `retirer` n'effectue **aucun contrôle** : le solde peut devenir
> négatif. La gestion du découvert sera étudiée au sous-groupe suivant.

## Exemple

```text
Compte c = new Compte("Dupont", 100.0);
c.deposer(50.0);   // solde = 150.0
c.retirer(30.0);   // solde = 120.0
c.toString();      // "Compte de Dupont : 120.00 €"
```

## Contraintes

- La classe doit s'appeler `Compte` et rester dans le package `piscine.m3`.
- **Ne modifiez pas les signatures publiques** : les tests s'appuient dessus.
- Les attributs doivent être **privés**. Le solde est un `double`.
- `toString()` formate le solde à deux décimales avec un point décimal
  (`java.util.Locale.ROOT`), suivi d'un espace et du symbole `€`.

## Ce qui sera vérifié

- La construction et les accesseurs renvoient le bon titulaire et le bon solde.
- `deposer` et `retirer` mettent correctement à jour le solde, y compris en
  enchaînant plusieurs opérations.
- Un retrait supérieur au solde rend bien un solde négatif (pas de garde ici).
- `toString` respecte le format et les deux décimales.

## Pour aller plus loin (optionnel — non noté)

- Comment empêcheriez-vous un retrait supérieur au solde ? (C'est l'objet du
  prochain sous-groupe.)
- Pourquoi le titulaire peut-il être déclaré `final` alors que le solde, non ?
