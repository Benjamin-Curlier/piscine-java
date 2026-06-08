# Exercice 6.1.4 — Filet de tests avant refactor

## Contexte

Avant de **refactorer** une classe (la réorganiser sans changer son comportement), on
écrit d'abord une **suite de tests** qui capture son comportement actuel. Ce filet de
sécurité garantit qu'on n'a rien cassé : si un test passe au rouge après le refactor,
c'est qu'on a changé un comportement par accident.

Ici, vous écrivez ce filet pour une classe `Rectangle` issue du module 3 (orientée objet,
avec des **invariants**). Comme dans tout ce sous-groupe, votre livrable est la **suite de
tests**, gradée par mutation.

## Énoncé

La classe `Rectangle` (fournie, **à ne pas modifier**) :

```java
public Rectangle(int largeur, int hauteur) // une dimension <= 0 est CORRIGÉE à 1 (invariant)
public int getLargeur()
public int getHauteur()
public int aire()        // largeur × hauteur
public int perimetre()   // 2 × (largeur + hauteur)
```

L'**invariant** est la règle clé : une largeur ou une hauteur invalide (`<= 0`) est
silencieusement **ramenée à 1** par le constructeur (convention du module 3 : on corrige
plutôt que de lever). Votre suite de tests doit couvrir :

1. `aire()` sur un rectangle valide (par exemple `Rectangle(3, 4).aire() == 12`).
2. `perimetre()` sur un rectangle valide (`Rectangle(3, 4).perimetre() == 14`).
3. L'**invariant** : `new Rectangle(-5, 4)` doit avoir une largeur de `1` (et donc une aire
   de `4`), pas `-5`.

## Exemple

```text
new Rectangle(3, 4).aire()        // → 12
new Rectangle(3, 4).perimetre()   // → 14
new Rectangle(-5, 4).getLargeur() // → 1   (dimension invalide corrigée)
new Rectangle(0, 7).getLargeur()  // → 1
```

## Contraintes

- Couvrez `aire`, `perimetre` **et** l'invariant (dimension invalide corrigée).
- Une assertion par comportement ; assertions AssertJ.
- Ne modifiez pas la classe `Rectangle`.

## Ce qui sera vérifié

- Vos tests **passent** sur l'implémentation correcte.
- Vos tests **détectent** chaque version buggée : une qui calcule l'aire comme une somme,
  une qui oublie le facteur 2 du périmètre, et une qui **supprime l'invariant** (accepte une
  dimension négative).
- La qualité du filet de tests (critère formateur : démarche, couverture des bordures).

## Pour aller plus loin (optionnel)

- Un rectangle « carré » (`Rectangle(5, 5)`) est-il bien géré ? Un test de plus le documente.
