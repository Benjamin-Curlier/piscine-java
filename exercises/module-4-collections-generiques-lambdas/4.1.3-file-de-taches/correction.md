# Correction — Exercice 4.1.3 File de tâches (Deque FIFO)

## Démarche attendue

1. `ajouter` : `file.addLast(tache)` insère en queue — c'est le côté « entrée »
   d'une file FIFO.
2. `traiterProchaine` : `file.pollFirst()` retire et renvoie la tête de file.
   `pollFirst` (contrairement à `removeFirst`) renvoie `null` si la file est vide,
   ce qui évite toute exception.
3. `prochaine` : `file.peekFirst()` consulte la tête sans la retirer.
   Même comportement défensif : renvoie `null` si vide.
4. `estVide` : délégation directe à `file.isEmpty()`.
5. `taille` : délégation directe à `file.size()`.

## Points clés

- **FIFO avec `Deque`** : `ArrayDeque` est la file standard Java. On pousse en
  queue (`addLast`) et on retire en tête (`pollFirst`). Nommer les méthodes
  côté tête/queue évite la confusion avec Stack/Queue.
- **`poll` vs `remove`** : les méthodes préfixées `poll`/`peek` renvoient `null`
  sur collection vide ; celles préfixées `remove`/`element` lèvent une exception.
  Pour un usage sans exception, toujours préférer `poll`/`peek`.
- **`null` plutôt qu'exception** : les exceptions ne sont introduites qu'au
  module 5 ; ici la valeur sentinelle `null` suffit.

## Erreurs fréquentes observées

- Utiliser `addFirst` à la place de `addLast` (file inversée : le dernier ajouté
  serait traité en premier, comportement LIFO et non FIFO).
- Utiliser `removeFirst` ou `element` au lieu de `pollFirst`/`peekFirst` (lève
  `NoSuchElementException` sur file vide).
- Oublier le cas vide et renvoyer une valeur arbitraire au lieu de `null`.

## Pour approfondir

- `java.util.Queue` : interface plus restreinte que `Deque`, suffisante pour un
  usage FIFO pur.
- `java.util.PriorityQueue` : file par priorité, vue au module 4 avec
  `Comparable`.
