# Correction — Exercice 4.2.4 Tri par chaînage de Comparator

## Démarche attendue

1. **`parNom`** : `Comparator.comparing(Membre::nom)` — référence de méthode sur
   l'accesseur du record ; `String` implémente `Comparable`, donc pas besoin de
   `Comparator` supplémentaire.

2. **`parNiveauPuisNom`** : `Comparator.comparing(Membre::niveau).thenComparing(Membre::nom)` —
   les `enum` implémentent `Comparable` selon leur ordre de déclaration ; le
   chaînage avec `thenComparing` ajoute un second critère déclenché uniquement
   en cas d'égalité du premier.

3. **`parNiveauDecroissant`** : `Comparator.comparing(Membre::niveau).reversed()` —
   `.reversed()` inverse l'ordre sans réécrire la logique.

4. **Immutabilité de la source** : chaque méthode commence par
   `List<Membre> copie = new ArrayList<>(membres);`, trie `copie`, et renvoie
   `copie`. La liste d'entrée reste dans son ordre original.

## Points clés

- **Référence de méthode** : `Membre::nom` est l'équivalent de `s -> s.nom()` ;
  sur un record, les accesseurs s'appellent sans préfixe `get`.
- **`enum` naturellement `Comparable`** : l'ordre de déclaration
  `JUNIOR < CONFIRME < SENIOR < LEAD < PRINCIPAL` est utilisé directement
  par `Comparator.comparing`.
- **Ne pas muter** : `new ArrayList<>(source)` est le patron standard. Utiliser
  `source.sort(...)` directement serait une faute idiomatique.
- **`reversed()` sans cast** : retourne un `Comparator<Membre>` ; pas besoin
  de `(Comparator<Membre>)`.

## Erreurs fréquentes observées

- Trier la liste source directement (mutation silencieuse).
- Utiliser un lambda `(a, b) -> ...` là où une référence de méthode suffit.
- Oublier que `reversed()` s'enchaîne après `comparing(...)`, pas avant.
- Confondre `thenComparing` (chaînage) avec un second appel à `sort`.

## Pour approfondir

- `Comparator.thenComparingInt(Membre::anciennete)` pour un troisième critère
  sur un `int` primitif.
- `Comparator.naturalOrder()` fonctionne si la classe implémente `Comparable` ;
  ici `Membre` ne l'implémente pas, donc `Comparator.comparing(Membre::niveau)`.
