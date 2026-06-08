# Correction — 6.1.4 Filet de tests avant refactor

## Démarche attendue

Avant de refactorer, on capture le comportement existant par des tests. Pour une classe à
**invariant**, le filet doit couvrir deux choses : le **comportement nominal** (aire,
périmètre sur des dimensions valides) **et** l'**invariant** lui-même (une dimension `<= 0`
est corrigée à 1). C'est l'invariant qu'on oublie le plus souvent — et c'est précisément ce
qu'un refactor maladroit risque de casser.

## Suite de tests modèle

```java
@Test void aireDUnRectangleValide() {
    assertThat(new Rectangle(3, 4).aire()).isEqualTo(12);
}
@Test void perimetreDUnRectangleValide() {
    assertThat(new Rectangle(3, 4).perimetre()).isEqualTo(14);
}
@Test void largeurNegativeEstCorrigeeAUn() {
    assertThat(new Rectangle(-5, 4).getLargeur()).isEqualTo(1);
}
@Test void largeurNulleEstCorrigeeAUn() {
    assertThat(new Rectangle(0, 7).getLargeur()).isEqualTo(1);
}
```

## Quel test tue quel mutant

| Mutant | Bug injecté | Test qui le détecte |
|---|---|---|
| `aire-en-somme` | `aire` renvoie `largeur + hauteur` | `aireDUnRectangleValide` (attend 12, le mutant donne 7) |
| `perimetre-sans-double` | `perimetre` oublie le `2 ×` | `perimetreDUnRectangleValide` (attend 14, le mutant donne 7) |
| `invariant-absent` | le constructeur ne corrige plus | `largeurNegativeEstCorrigeeAUn` (attend 1, le mutant donne -5) |

> Sans test sur l'invariant, le mutant `invariant-absent` survit : tous les calculs sur des
> dimensions valides restent corrects. C'est exactement le genre de régression silencieuse
> qu'un refactor peut introduire — et qu'un bon filet de tests attrape.

## Points clés

- Un filet de tests sécurise un refactor : on refactore, on relance, tout reste vert.
- Tester l'**invariant** (dimension corrigée) autant que le comportement nominal.
- Une assertion par comportement, des noms qui décrivent l'attendu.

## Erreurs fréquentes observées

- Ne tester que `aire`/`perimetre` sur des valeurs valides → le mutant `invariant-absent` survit.
- Croire que `new Rectangle(-5, 4)` doit lever une exception : la convention du module 3 est
  de **corriger** (ramener à 1), pas de lever — testez le comportement réel de la classe.
- Modifier la classe `Rectangle` (interdit : seuls vos tests sont évalués).
