# Exercice 1.2.1 — Conversion d'unités

## Contexte

Les documents techniques que vous croiserez mélangent parfois les unités. Savoir convertir rapidement est utile. Vous allez écrire un convertisseur de température : des degrés Celsius vers des degrés Fahrenheit.

## Énoncé

Complétez la méthode `main` pour :

1. Afficher `Température en °C ?` (déjà fait dans le squelette).
2. Lire une température en degrés Celsius (un nombre à virgule).
3. La convertir en Fahrenheit avec la formule : `fahrenheit = celsius × 9 / 5 + 32`.
4. Afficher le résultat sous la forme `<celsius> °C = <fahrenheit> °F`.

## Exemple

**Exécution attendue** (l'utilisateur saisit `100`) :

```text
Température en °C ?
100.0 °C = 212.0 °F
```

## Contraintes

- La classe doit s'appeler `ConversionUnites` et rester dans le package `piscine.m1`.
- Le calcul doit se faire en **nombres à virgule** (`double`), sans division entière.
- Le format de sortie doit être exactement celui de l'exemple.

## Ce qui sera vérifié

- Votre programme **compile** sans erreur.
- La conversion est **mathématiquement correcte** (testée sur plusieurs valeurs).
- La sortie respecte le format attendu.

## Pour aller plus loin (optionnel — non noté)

- Pourquoi `-40 °C` et `-40 °F` désignent-ils la même température ? Vérifiez-le avec votre programme.
- Que se passe-t-il si vous écrivez la formule `celsius * (9 / 5) + 32` ? Essayez et expliquez la différence (indice : division entière, chapitre 1.2).
