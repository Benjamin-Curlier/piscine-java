# Correction — Exercice 1.2.1 Conversion d'unités

## Démarche attendue

```java
double celsius = clavier.nextDouble();
double fahrenheit = celsius * 9 / 5 + 32;
System.out.println(celsius + " °C = " + fahrenheit + " °F");
```

Étapes intellectuelles :

1. **Lire un nombre à virgule** avec `nextDouble()`.
2. **Appliquer la formule**. Comme `celsius` est un `double`, l'expression `celsius * 9 / 5 + 32` est entièrement évaluée à virgule : pas de division entière.
3. **Afficher** au format demandé.

## Points clés

- **Pas de division entière** : si vous écriviez `9 / 5` isolément (deux entiers), Java calculerait `1`. Ici `celsius * 9` est déjà un `double`, donc `/ 5` reste une division à virgule. C'est l'ordre des opérations qui sauve le calcul.
- **Affichage des `double`** : Java affiche `100.0`, `212.0`, `98.6`… avec un point décimal. Le format de la moulinette suit cette représentation.

## Erreurs fréquentes observées

- **`celsius * (9 / 5) + 32`** → `9 / 5` vaut `1` (division entière), le résultat est faux.
- **Tout calculer en `int`** → perte des décimales.
- **Format de sortie approximatif** (oubli des unités `°C`/`°F`, espaces) → sortie non conforme.

## Variantes possibles

Écrire la fraction en flottant lève toute ambiguïté :

```java
double fahrenheit = celsius * 9.0 / 5.0 + 32;
```

C'est équivalent ici, mais plus explicite sur l'intention « calcul à virgule ».

## Pour approfondir

- [Les types primitifs — dev.java](https://dev.java/learn/language-basics/primitive-types/)
- [Java operators — Baeldung](https://www.baeldung.com/java-operators)
