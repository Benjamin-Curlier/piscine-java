# Correction — Exercice 4.2.1 Pile générique (LIFO)

## Démarche attendue

1. Le champ `private final List<T> elements = new ArrayList<>();` est fourni :
   il porte déjà le `<T>`. Tout le travail consiste à **déléguer** à cette liste
   en gardant le type `T`.
2. `empiler(T element)` : `elements.add(element)` (ajoute en fin de liste = au
   sommet).
3. `depiler()` : si `estVide()`, renvoyer `null` ; sinon
   `elements.remove(elements.size() - 1)` (retire **et** renvoie le dernier →
   LIFO).
4. `sommet()` : si `estVide()`, renvoyer `null` ; sinon
   `elements.get(elements.size() - 1)` (lit le dernier **sans** le retirer).
5. `estVide()` : `elements.isEmpty()`. `taille()` : `elements.size()`.

## Points clés

- **Propagation de `<T>`** : `depiler()` et `sommet()` renvoient `T`, pas
  `Object`. C'est ce qui permet à l'appelant d'écrire `String x = pile.depiler();`
  sans cast. Si on stockait des `Object`, il faudrait caster — et perdre la
  sécurité de type.
- **LIFO via le dernier indice** : empiler/dépiler en fin de liste est en
  temps constant et donne naturellement l'ordre LIFO.
- **`null` plutôt qu'exception** : choix volontaire (les exceptions arrivent au
  module 5). Une vraie pile lèverait plutôt une exception sur pile vide.

## Erreurs fréquentes observées

- Déclarer `List<Object>` ou renvoyer `Object` → casse la généricité (les tests
  multi-types ne compilent plus sans cast).
- Dépiler/lire au **mauvais bout** (indice `0` au lieu de `size()-1`) → ordre
  FIFO au lieu de LIFO.
- Oublier le garde-fou `estVide()` → `IndexOutOfBoundsException` sur pile vide
  au lieu de `null`.

## Pour approfondir

- `java.util.Deque` (`ArrayDeque`) : la pile/file idiomatique du JDK
  (`push`/`pop`/`peek`).
