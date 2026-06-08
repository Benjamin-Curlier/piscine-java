# Correction — 6.1.2 Tests paramétrés

## Démarche attendue

Un test paramétré (`@ParameterizedTest` + `@CsvSource`) couvre tous les paliers en une
seule méthode. La clé est de tester **chaque frontière** : la première note d'un palier
(10, 12, 14, 16) **et** la dernière du palier précédent (9, 11, 13, 15). C'est là que les
bugs de seuil se cachent.

## Suite de tests modèle

```java
@ParameterizedTest
@CsvSource({
    "20, Très bien", "16, Très bien",
    "15, Bien",      "14, Bien",
    "13, Assez bien","12, Assez bien",
    "11, Passable",  "10, Passable",
    "9, Insuffisant","0, Insuffisant"
})
void mentionSelonLaNote(int note, String attendue) {
    assertThat(new Classement().mention(note)).isEqualTo(attendue);
}
```

## Quel test tue quel mutant

| Mutant | Bug injecté | Ligne `@CsvSource` qui le détecte |
|---|---|---|
| `borne-passable` | seuil Passable `>= 11` au lieu de `>= 10` | `"10, Passable"` (le mutant renvoie "Insuffisant") |
| `seuil-tres-bien` | seuil Très bien `>= 15` au lieu de `>= 16` | `"15, Bien"` (le mutant renvoie "Très bien") |
| `retour-constant` | renvoie toujours "Passable" | toute ligne ≠ Passable, ex. `"20, Très bien"` |

> Sans la frontière `10` **et** `15`, deux mutants survivent. Tester un seul exemple par
> palier ne suffit pas : ce sont les **bornes** qui révèlent les bugs de seuil.

## Points clés

- `@CsvSource` associe chaque entrée à son résultat attendu, une ligne par cas.
- Couvrir les frontières basses **et** les valeurs juste en dessous.
- Un seul test paramétré remplace dix `@Test` répétitifs — plus lisible, plus facile à étendre.

## Erreurs fréquentes observées

- Tester une valeur « au milieu » de chaque palier (18, 13…) sans les frontières → les
  mutants de seuil survivent.
- Écrire dix `@Test` séparés au lieu d'un test paramétré (ne respecte pas la consigne).
- Oublier d'accentuer « Très bien » exactement comme la classe le renvoie.
