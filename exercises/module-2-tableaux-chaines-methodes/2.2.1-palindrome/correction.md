# Correction — Exercice 2.2.1 Palindrome

## Démarche attendue

1. Lire le mot avec `clavier.nextLine()`.
2. Normaliser la casse avec `toLowerCase()` (et, par sécurité, `strip()` pour
   retirer d'éventuels espaces en bord de ligne).
3. Comparer les caractères deux à deux depuis les extrémités : `charAt(i)` avec
   `charAt(longueur - 1 - i)`, pour `i` allant de 0 jusqu'au **milieu**.
4. Dès qu'une paire diffère, ce n'est pas un palindrome : afficher `non`.
   Sinon, afficher `oui`.

## Points clés

- **Boucle jusqu'au milieu** : inutile d'aller au-delà de `longueur / 2`, on
  comparerait deux fois les mêmes paires.
- **Casse** : sans `toLowerCase`, `Radar` serait refusé (`R` ≠ `r`).
- **`charAt`** : accède au caractère à une position donnée (indice base 0).

## Erreurs fréquentes observées

- Boucler sur toute la longueur → travail inutile (mais résultat correct).
- Oublier `toLowerCase` → échec sur les mots à majuscule initiale.
- Comparer avec `==` deux `String` au lieu de comparer des `char` : ici on
  compare bien des `char` (`charAt`), donc `==` est correct.

## Variantes possibles

- **Inverser la chaîne** : `new StringBuilder(mot).reverse().toString()` puis
  comparer avec `equals`. Plus court, mais cache la mécanique de parcours.

## Pour approfondir

- Différence entre comparaison de `char` (`==`) et comparaison de `String`
  (`equals`).
