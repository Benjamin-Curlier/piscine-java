# Correction — Exercice 2.2.3 ASCII art

## Démarche attendue

1. Lire la hauteur `n` avec `clavier.nextInt()`.
2. Boucle externe : `i` de 1 à `n` (le numéro de ligne = le nombre d'étoiles).
3. Boucle interne : ajouter `i` étoiles à la ligne courante.
4. Passer à la ligne après chaque ligne d'étoiles.

## Points clés

- **Double boucle** : la boucle interne dépend du compteur de la boucle externe
  (`j < i`). C'est le cœur de l'exercice.
- **Construction de la sortie** : un `StringBuilder` évite de multiplier les
  appels à `System.out` et garde le code lisible. Un `System.out.print` ligne par
  ligne fonctionne aussi.

## Erreurs fréquentes observées

- Faire varier `i` de 0 à `n` → une ligne de trop (ou une de moins).
- Oublier le retour à la ligne entre deux lignes d'étoiles.
- Mélanger les bornes des deux boucles → triangle décalé.

## Variantes possibles

- **Triangle inversé** : faire décroître le nombre d'étoiles (`n` puis `n-1`…).
- **Pyramide centrée** : préfixer chaque ligne d'espaces pour centrer les étoiles.

## Pour approfondir

- `"*".repeat(i)` (Java 11+) produit directement `i` étoiles : une alternative
  plus concise à la boucle interne.
