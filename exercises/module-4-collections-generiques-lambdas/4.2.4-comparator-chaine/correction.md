# Correction — Exercice 4.2.4 Tri par chaînage de Comparator

## Démarche attendue

1. **`parNom`** : `Comparator.comparing(Soldat::nom)` — référence de méthode sur
   l'accesseur du record ; `String` implémente `Comparable`, donc pas besoin de
   `Comparator` supplémentaire.

2. **`parGradePuisNom`** : `Comparator.comparing(Soldat::grade).thenComparing(Soldat::nom)` —
   les `enum` implémentent `Comparable` selon leur ordre de déclaration ; le
   chaînage avec `thenComparing` ajoute un second critère déclenché uniquement
   en cas d'égalité du premier.

3. **`parGradeDecroissant`** : `Comparator.comparing(Soldat::grade).reversed()` —
   `.reversed()` inverse l'ordre sans réécrire la logique.

4. **Immutabilité de la source** : chaque méthode commence par
   `List<Soldat> copie = new ArrayList<>(soldats);`, trie `copie`, et renvoie
   `copie`. La liste d'entrée reste dans son ordre original.

## Points clés

- **Référence de méthode** : `Soldat::nom` est l'équivalent de `s -> s.nom()` ;
  sur un record, les accesseurs s'appellent sans préfixe `get`.
- **`enum` naturellement `Comparable`** : l'ordre de déclaration
  `SOLDAT < CAPORAL < SERGENT < ADJUDANT < LIEUTENANT` est utilisé directement
  par `Comparator.comparing`.
- **Ne pas muter** : `new ArrayList<>(source)` est le patron standard. Utiliser
  `source.sort(...)` directement serait une faute idiomatique.
- **`reversed()` sans cast** : retourne un `Comparator<Soldat>` ; pas besoin
  de `(Comparator<Soldat>)`.

## Erreurs fréquentes observées

- Trier la liste source directement (mutation silencieuse).
- Utiliser un lambda `(a, b) -> ...` là où une référence de méthode suffit.
- Oublier que `reversed()` s'enchaîne après `comparing(...)`, pas avant.
- Confondre `thenComparing` (chaînage) avec un second appel à `sort`.

## Pour approfondir

- `Comparator.thenComparingInt(Soldat::anciennete)` pour un troisième critère
  sur un `int` primitif.
- `Comparator.naturalOrder()` fonctionne si la classe implémente `Comparable` ;
  ici `Soldat` ne l'implémente pas, donc `Comparator.comparing(Soldat::grade)`.
