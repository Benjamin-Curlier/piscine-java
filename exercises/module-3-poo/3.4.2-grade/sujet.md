# Exercice 3.4.2 — Grade (enum)

## Contexte

Les grades militaires forment un ensemble **fini et fixe**. Chaque grade a une
solde de base et appartient à une catégorie. Un `enum` est l'outil idéal.

## Énoncé

L'enum `Grade` vous est **fourni** avec ses constantes et leur solde de base :
`SOLDAT(1600)`, `CAPORAL(1800)`, `SERGENT(2200)`, `ADJUDANT(2600)`,
`LIEUTENANT(3000)`. L'attribut `soldeBase`, le constructeur et `getSoldeBase()`
sont déjà écrits.

Complétez la méthode `categorie()` qui renvoie, **par un `switch` exhaustif sans
`default`** :

- `"Militaire du rang"` pour `SOLDAT` et `CAPORAL` ;
- `"Sous-officier"` pour `SERGENT` et `ADJUDANT` ;
- `"Officier"` pour `LIEUTENANT`.

## Exemple

```text
Grade.SOLDAT.getSoldeBase();   // 1600.0
Grade.SERGENT.categorie();     // "Sous-officier"
Grade.values().length;         // 5
```

## Contraintes

- Enum `Grade` dans le package `etnc.m3`. **Ne changez pas les signatures.**
- `categorie()` utilise un `switch` **exhaustif sans `default`** (toutes les
  constantes sont couvertes ; le compilateur le vérifie).

## Ce qui sera vérifié

- `getSoldeBase()` pour différentes constantes.
- `categorie()` pour chaque catégorie.
- `Grade.values()` contient les 5 constantes ; `valueOf` retrouve une constante.

## Pour aller plus loin (optionnel — non noté)

- Pourquoi compare-t-on deux valeurs d'enum avec `==` ?
- Ajoutez une méthode `estOfficier()`.
