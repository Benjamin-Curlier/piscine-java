# Exercice 3.4.1 — Coordonnées (record)

## Contexte

Une position sur une carte se note par deux entiers. On veut un type **immuable**
et léger : un `record` est parfait pour cela.

## Énoncé

Le record `Coordonnees(int x, int y)` vous est **fourni**. Ses accesseurs `x()`
et `y()` et son `equals` (comparaison par valeurs) sont **générés
automatiquement**. Complétez ses deux méthodes :

- `normeCarree()` : renvoie `x*x + y*y` ;
- `translater(int dx, int dy)` : renvoie un **nouveau** `Coordonnees` décalé de
  `(dx, dy)` — l'objet courant n'est **pas** modifié (un record est immuable).

## Exemple

```text
Coordonnees c = new Coordonnees(1, 2);
c.x();                       // 1
c.normeCarree();             // 5
c.translater(3, -1);         // new Coordonnees(4, 1)  (c reste (1, 2))
new Coordonnees(1, 2).equals(new Coordonnees(1, 2));   // true (equals généré)
```

## Contraintes

- Record `Coordonnees` dans le package `piscine.m3`. **Ne changez pas l'en-tête ni
  les signatures.**
- N'écrivez **pas** `equals`/`hashCode` : ils sont générés.
- `translater` ne modifie rien : il **construit** un nouveau record.

## Ce qui sera vérifié

- Les accesseurs `x()`/`y()` et l'`equals` généré.
- `normeCarree` (y compris coordonnées négatives).
- `translater` renvoie les bonnes coordonnées **et** laisse l'original inchangé.

## Pour aller plus loin (optionnel — non noté)

- Pourquoi un record n'a-t-il pas de setter ?
- Ajoutez une méthode `oppose()` qui renvoie `(-x, -y)`.
