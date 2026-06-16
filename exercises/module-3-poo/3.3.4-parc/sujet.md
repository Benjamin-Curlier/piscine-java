# Exercice 3.3.4 — Parc de formes (polymorphisme)

## Contexte

Un service cartographie un terrain comme un ensemble de formes. On veut des
outils qui travaillent sur **n'importe quelles** formes, sans se soucier de leur
type précis.

## Énoncé

Les classes `Forme` (abstraite), `Cercle` et `Rectangle` vous sont **fournies**
(identiques à l'exercice 3.3.1). Vous n'écrivez que la classe utilitaire `Parc`,
avec trois méthodes `static` :

- `surfaceTotale(Forme[] formes)` : somme des `aire()` de toutes les formes ;
- `plusGrande(Forme[] formes)` : la forme d'aire maximale, ou `null` si le
  tableau est vide ;
- `compterCercles(Forme[] formes)` : le nombre d'éléments qui sont des `Cercle`
  (indice : `instanceof`).

## Exemple

```text
Forme[] formes = { new Cercle(1.0), new Rectangle(2.0, 3.0) };
Parc.surfaceTotale(formes);   // π + 6 ≈ 9.14
Parc.compterCercles(formes);  // 1
```

## Contraintes

- Package `piscine.m3`. **Ne modifiez pas** `Forme`, `Cercle`, `Rectangle`.
- Les trois méthodes de `Parc` sont `static` ; ne changez pas leurs signatures.
- N'utilisez ni collection ni stream : parcourez le **tableau** avec une boucle.
- `plusGrande` d'un tableau vide renvoie `null`.

## Ce qui sera vérifié

- `surfaceTotale` (y compris tableau vide → `0.0`).
- `plusGrande` renvoie la bonne forme (et `null` si vide).
- `compterCercles` compte correctement (y compris `0`), via `instanceof`.

## Pour aller plus loin (optionnel — non noté)

- Ajoutez `compter(Forme[] formes, ...)` générique au type recherché.
- Après `instanceof Cercle`, comment récupérer son rayon ? (downcast)
