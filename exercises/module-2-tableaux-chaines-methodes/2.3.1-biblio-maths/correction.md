# Correction — Exercice 2.3.1 Bibliothèque mathématique

## Démarche attendue

- **pgcd (algorithme d'Euclide)** : tant que `b` n'est pas nul, remplacer
  `(a, b)` par `(b, a % b)`. Quand `b` vaut 0, `a` contient le PGCD. Ce procédé
  fonctionne aussi pour `pgcd(0, n) = n`.
- **estPremier** : renvoyer `false` si `n < 2`. Sinon, tester les diviseurs de 2
  jusqu'à la racine carrée de `n` (condition `d * d <= n`). Si aucun ne divise
  `n`, il est premier.
- **sommeChiffres** : tant que `n > 0`, ajouter `n % 10` (dernier chiffre) à un
  accumulateur, puis faire `n = n / 10` (retirer ce chiffre).

## Points clés

- **`return` plutôt qu'afficher** : ces méthodes produisent une valeur, réutilisable
  par l'appelant. C'est la différence avec les exercices précédents.
- **Racine carrée pour la primalité** : un diviseur plus grand que √n irait de
  pair avec un diviseur plus petit, déjà testé. On divise donc le travail par deux.
- **`% 10` et `/ 10`** : le couple idiomatique pour parcourir les chiffres d'un
  entier de droite à gauche.

## Erreurs fréquentes observées

- Oublier le cas `n < 2` dans `estPremier` (`1` n'est pas premier).
- Boucler jusqu'à `n` au lieu de √n : correct mais inutilement lent.
- Dans `sommeChiffres`, oublier `n = n / 10` → boucle infinie.

## Variantes possibles

- **pgcd récursif** : `return b == 0 ? a : pgcd(b, a % b);` (la récursivité est
  l'objet du sous-groupe 2.4).
- **estPremier** : s'arrêter dès qu'un diviseur pair est trouvé après avoir testé 2.

## Pour approfondir

- Le crible d'Ératosthène, pour tester la primalité de nombreux nombres d'un coup.
