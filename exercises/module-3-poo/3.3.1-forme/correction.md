# Correction — Exercice 3.3.1 Formes géométriques

## Démarche attendue

1. `Cercle` : un champ `private double rayon;`, mémorisé dans le constructeur.
   `aire()` renvoie `Math.PI * rayon * rayon` ; `perimetre()` renvoie
   `2 * Math.PI * rayon`.
2. `Rectangle` : deux champs privés `largeur`/`hauteur`. `aire()` =
   `largeur * hauteur` ; `perimetre()` = `2 * (largeur + hauteur)`.
3. Chaque méthode redéfinie porte `@Override` : Java vérifie qu'elle correspond
   bien à une méthode abstraite de `Forme`.

## Points clés

- **Méthode abstraite** : `Forme` impose `aire()`/`perimetre()` sans dire
  comment. Chaque sous-classe fournit sa formule.
- **Polymorphisme** : `decrire()` (écrite une fois dans `Forme`) appelle
  `aire()` ; la version exécutée dépend du **type réel** de l'objet.
- **`@Override`** : facultatif pour compiler, mais protège des fautes de frappe
  dans la signature.

## Erreurs fréquentes observées

- Oublier `Math.PI` ou écrire `rayon * 2 * 2` au lieu de `rayon * rayon`.
- Rendre les champs `public`.
- Modifier `Forme` au lieu de compléter les sous-classes.

## Pour approfondir

- Le patron de conception « méthode modèle » (template method).
