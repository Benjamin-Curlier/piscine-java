# Correction — Exercice 3.2.3 Date simple

## Démarche attendue

1. Déclarer `private int jour;`, `private int mois;`, `private int annee;`.
2. Écrire un petit utilitaire de bornage (méthode `private static` réutilisable) :
   `borner(valeur, min, max)` renvoie `min`/`max` si la valeur sort des bornes.
3. Constructeur : `this.jour = borner(jour, 1, 31); this.mois = borner(mois, 1, 12);`
   et `this.annee = annee;`.
4. Setters : ne modifier que si la valeur est dans la plage
   (`if (jour >= 1 && jour <= 31) ...`).
5. `toString` : `String.format("%02d/%02d/%04d", jour, mois, annee)`.

## Points clés

- **Invariant de plage** : le jour et le mois restent dans des bornes valides,
  garanti à la création (bornage) et préservé par les setters (refus).
- **Méthode `static` privée** : `borner` ne dépend pas de l'état de l'objet ;
  la déclarer `static` est idiomatique (vu au chapitre 3-4).
- **Zéro-padding** : `%02d` affiche au moins deux chiffres (`7` → `07`).

## Erreurs fréquentes observées

- Dupliquer la logique de bornage au lieu d'une méthode utilitaire.
- Oublier le zéro-padding (`7/3/2026` au lieu de `07/03/2026`).
- Setter sans garde : un mois à 13 passe alors tel quel.

## Pour approfondir

- Le format `%0Nd` de `String.format` (largeur minimale + remplissage par des 0).
- La vraie classe `java.time.LocalDate` (au-delà de ce module).
