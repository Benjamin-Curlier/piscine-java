# Exercice 3.1.1 — Point2D

## Contexte

Sur une carte d'état-major, chaque position se repère par deux coordonnées. Vous
allez modéliser un point du plan sous la forme d'une classe Java réutilisable.

## Énoncé

Complétez la classe `Point2D` fournie dans le `starter/`. Elle représente un
point de coordonnées réelles `(x, y)` et doit proposer :

- un constructeur `Point2D(double x, double y)` ;
- deux accesseurs `getX()` et `getY()` ;
- une méthode `deplacer(double dx, double dy)` qui **translate** le point
  (ajoute `dx` à l'abscisse et `dy` à l'ordonnée) ;
- une méthode `distance(Point2D autre)` qui renvoie la **distance euclidienne**
  entre ce point et `autre` ;
- une redéfinition de `toString()` au format `(x, y)`.

## Exemple

```text
Point2D origine = new Point2D(0.0, 0.0);
Point2D cible   = new Point2D(3.0, 4.0);

origine.distance(cible);   // 5.0
cible.deplacer(1.0, -1.0); // cible devient (4.0, 3.0)
cible.toString();          // "(4.0, 3.0)"
```

## Contraintes

- La classe doit s'appeler `Point2D` et rester dans le package `etnc.m3`.
- **Ne modifiez pas les signatures publiques** : les tests s'appuient dessus.
- Les coordonnées sont des `double` ; les attributs doivent être **privés**.
- `toString()` doit produire exactement `(x, y)` (ex. `(1.0, 2.0)`). Utilisez
  `java.util.Locale.ROOT` pour un formatage indépendant de la machine.

## Ce qui sera vérifié

- La construction et les accesseurs renvoient les bonnes coordonnées.
- `deplacer` met correctement à jour l'état du point (y compris en cumulé).
- `distance` calcule la distance euclidienne (et vaut 0 d'un point à lui-même).
- `toString` respecte le format attendu.

## Pour aller plus loin (optionnel — non noté)

- Que renverrait une méthode `distance` si vous oubliiez `Math.sqrt` ? À quoi
  correspond cette valeur ?
- Comment écririez-vous une méthode qui renvoie un **nouveau** point déplacé,
  sans modifier le point courant (objet immuable) ?
