# Exercice 3.4.2 — Niveau (enum)

## Contexte

Les niveaux de séniorité forment un ensemble **fini et fixe**. Chaque niveau a
un salaire de base et appartient à une catégorie. Un `enum` est l'outil idéal.

## Énoncé

L'enum `Niveau` vous est **fourni** avec ses constantes et leur salaire de base :
`JUNIOR(1600)`, `CONFIRME(1800)`, `SENIOR(2200)`, `LEAD(2600)`,
`PRINCIPAL(3000)`. L'attribut `soldeBase`, le constructeur et `getSoldeBase()`
sont déjà écrits.

Complétez la méthode `categorie()` qui renvoie, **par un `switch` exhaustif sans
`default`** :

- `"Débutant"` pour `JUNIOR` et `CONFIRME` ;
- `"Intermédiaire"` pour `SENIOR` et `LEAD` ;
- `"Expert"` pour `PRINCIPAL`.

## Exemple

```text
Niveau.JUNIOR.getSoldeBase();   // 1600.0
Niveau.SENIOR.categorie();      // "Intermédiaire"
Niveau.values().length;         // 5
```

## Contraintes

- Enum `Niveau` dans le package `piscine.m3`. **Ne changez pas les signatures.**
- `categorie()` utilise un `switch` **exhaustif sans `default`** (toutes les
  constantes sont couvertes ; le compilateur le vérifie).

## Ce qui sera vérifié

- `getSoldeBase()` pour différentes constantes.
- `categorie()` pour chaque catégorie.
- `Niveau.values()` contient les 5 constantes ; `valueOf` retrouve une constante.

## Pour aller plus loin (optionnel — non noté)

- Pourquoi compare-t-on deux valeurs d'enum avec `==` ?
- Ajoutez une méthode `estExpert()`.
