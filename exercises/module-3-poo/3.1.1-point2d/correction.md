# Correction — Exercice 3.1.1 Point2D

## Démarche attendue

1. Déclarer deux attributs **privés** `double x` et `double y`.
2. Dans le constructeur, affecter les paramètres aux attributs avec `this.x = x`
   et `this.y = y` (le mot-clé `this` lève l'ambiguïté entre paramètre et champ).
3. Les accesseurs renvoient simplement `x` et `y`.
4. `deplacer` **modifie l'état** : `this.x += dx; this.y += dy;`.
5. `distance` calcule `Math.sqrt(dx*dx + dy*dy)` où `dx`/`dy` sont les écarts de
   coordonnées avec l'autre point.
6. `toString` renvoie `String.format(Locale.ROOT, "(%s, %s)", x, y)`.

## Points clés

- **Encapsulation** : les attributs sont privés ; on n'y accède que par méthodes.
- **`this`** : indispensable quand le paramètre porte le même nom que le champ.
- **État vs calcul** : `deplacer` change l'objet ; `distance` ne le change pas,
  elle calcule une valeur à partir de l'état.
- **Locale** : `Locale.ROOT` garantit le point décimal quelle que soit la machine.

## Erreurs fréquentes observées

- Oublier `this` dans le constructeur (le paramètre s'affecte à lui-même).
- Rendre les attributs `public` au lieu de `private`.
- Calculer le carré de la distance sans `Math.sqrt`.

## Pour approfondir

- Documentation de `java.lang.Math` (`sqrt`, `hypot`).
- La notion d'objet **immuable** (champs `final`, méthodes renvoyant un nouvel objet).
