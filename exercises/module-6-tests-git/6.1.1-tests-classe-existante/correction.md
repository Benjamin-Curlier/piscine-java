# Correction — 6.1.1 Tester une classe existante

## Démarche attendue

L'exercice inverse le contrat habituel : la classe est correcte, c'est la **suite de
tests** qui est le livrable. La clé est de choisir des valeurs qui **distinguent** le
code correct d'un code buggé — autrement dit, de couvrir les **cas limites**, pas
seulement un cas « facile ».

## Suite de tests modèle

```java
class TemperatureTest {
    private final Temperature temperature = new Temperature();

    @Test void zeroCelsiusVaut32Fahrenheit() {
        assertThat(temperature.celsiusVersFahrenheit(0)).isEqualTo(32);
    }
    @Test void centCelsiusVaut212Fahrenheit() {
        assertThat(temperature.celsiusVersFahrenheit(100)).isEqualTo(212);
    }
    @Test void zeroNEstPasStrictementPositif() {
        assertThat(temperature.estPositive(0)).isFalse();
    }
    @Test void uneTemperaturePositiveEstDetectee() {
        assertThat(temperature.estPositive(5)).isTrue();
    }
    @Test void uneTemperatureNegativeNEstPasPositive() {
        assertThat(temperature.estPositive(-3)).isFalse();
    }
}
```

## Quel test tue quel mutant

| Mutant caché | Bug injecté | Test qui le détecte |
|---|---|---|
| `offset-absent` | `× 9 / 5` sans `+ 32` | `zeroCelsiusVaut32Fahrenheit` (attend 32, le mutant donne 0) |
| `operateur-inverse` | `× 5 / 9` au lieu de `× 9 / 5` | `centCelsiusVaut212Fahrenheit` (attend 212, le mutant donne 87) |
| `borne-zero` | `>= 0` au lieu de `> 0` | `zeroNEstPasStrictementPositif` (attend false, le mutant donne true) |

> Remarque : un test sur 0 °C **ne suffit pas** à tuer `operateur-inverse` (le mutant
> donne aussi 32 à 0 °C). C'est pourquoi il faut **une deuxième valeur** de conversion.
> De même, sans test sur `estPositive(0)`, le mutant `borne-zero` survit. Chaque cas
> limite mérite son test.

## Points clés

- **Cas limite = frontière** : `0 °C` pour la conversion, `0` pour `estPositive`.
- Une assertion AssertJ par comportement, nom de test au présent décrivant l'attendu.
- Tester aussi le cas négatif consolide la confiance même si aucun mutant ne le cible ici.

## Erreurs fréquentes observées

- N'écrire que le test fourni → seul `offset-absent` est tué, les deux autres survivent → échec.
- Tester deux conversions mais oublier la frontière de `estPositive` → `borne-zero` survit.
- Modifier la classe `Temperature` (interdit : elle n'est pas évaluée, seuls vos tests le sont).
