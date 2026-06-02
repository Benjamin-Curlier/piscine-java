# Correction — Exercice 2.2.2 Comptage d'occurrences

## Démarche attendue

1. Lire le texte avec `clavier.nextLine()` (il peut contenir des espaces).
2. Lire la ligne du caractère, puis en extraire le premier caractère avec
   `charAt(0)`.
3. Parcourir le texte de l'indice 0 à `texte.length() - 1`, et incrémenter un
   compteur chaque fois que `texte.charAt(i)` est égal au caractère cherché.
4. Afficher `Occurrences : <compteur>`.

## Points clés

- **`length()` puis `charAt(i)`** : la façon idiomatique de parcourir une chaîne.
- **Comparaison de `char`** : on compare deux `char` avec `==` (correct ici).
- **Casse** : aucune normalisation, donc `M` ≠ `m`.
- **Lecture du caractère** : passer par une ligne entière puis `charAt(0)` évite
  les pièges de `Scanner` autour des espaces et des retours à la ligne.

## Erreurs fréquentes observées

- Utiliser `next()` au lieu de `nextLine()` pour le texte → s'arrête au premier
  espace.
- Comparer `texte.charAt(i)` (un `char`) avec une `String` → ne compile pas, ou
  comparaison incorrecte.
- Normaliser la casse alors que l'énoncé demande de la respecter.

## Variantes possibles

- Compter sans tenir compte de la casse : passer texte et caractère en minuscules
  avant de comparer.
- Compter un **mot** : utiliser `indexOf(mot, depuis)` en boucle.

## Pour approfondir

- Documentation de `String.indexOf` (recherche de sous-chaîne).
