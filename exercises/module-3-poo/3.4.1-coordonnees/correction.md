# Correction — Exercice 3.4.1 Coordonnées (record)

## Démarche attendue

1. `normeCarree()` renvoie `x * x + y * y` (on accède aux composants directement
   par leur nom dans le record).
2. `translater(dx, dy)` renvoie `new Coordonnees(x + dx, y + dy)` — un **nouvel**
   objet, sans toucher l'actuel.

## Points clés

- **Record immuable** : pas de setter ; pour « changer » une valeur, on crée un
  nouvel objet. `translater` illustre exactement ce principe.
- **`equals` généré** : un record compare ses composants par valeur ; inutile (et
  déconseillé) de le réécrire.
- **Accesseurs** : `x()` et `y()` (pas `getX()`), générés par le record.

## Erreurs fréquentes observées

- Vouloir « modifier » `x`/`y` (impossible : immuable).
- Réécrire `equals` à la main.
- Renvoyer `this` au lieu d'un nouveau record dans `translater`.

## Pour approfondir

- Le constructeur compact d'un record (validation à la construction).
