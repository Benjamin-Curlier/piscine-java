# Correction — Exercice 2.1.1 Min, max et moyenne

## Démarche attendue

1. Lire `n` avec `clavier.nextInt()`, créer un tableau `int[n]`, puis le remplir
   avec une boucle `for`.
2. Initialiser `min` et `max` avec le **premier élément** du tableau (et non avec
   0 : sinon un tableau de valeurs négatives donnerait un maximum faux).
3. Parcourir le tableau une seule fois en mettant à jour `min`, `max` et la
   `somme`.
4. Calculer la moyenne en forçant un calcul en `double` :
   `(double) somme / n`.
5. Afficher les trois lignes, en formatant la moyenne avec
   `String.format(Locale.ROOT, "%.2f", moyenne)`.

## Points clés

- **Initialisation de min/max** : partir du premier élément, jamais de 0.
- **Division entière** : `somme / n` entre deux entiers tronque la partie
  décimale. Le `(double)` sur `somme` règle le problème.
- **Locale** : `%.2f` sans `Locale.ROOT` afficherait une virgule sur une machine
  configurée en français. On force le point pour une sortie reproductible.

## Erreurs fréquentes observées

- Initialiser `min`/`max` à `0` → résultat faux sur des tableaux entièrement
  positifs ou entièrement négatifs.
- Oublier le `(double)` → moyenne tronquée (`6` au lieu de `6.50`).
- Afficher la moyenne avec une virgule → échec des tests sur le format.

## Variantes possibles

- Cumuler la somme dans un `long` (fait dans la solution) pour éviter le
  débordement sur de grands tableaux. Pédagogiquement facultatif à ce stade.
- Utiliser une boucle `for` indexée plutôt qu'un `for-each` : équivalent.

## Pour approfondir

- Documentation de `java.util.Formatter` (motifs de `String.format`).
