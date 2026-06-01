# Correction — Exercice 1.3.1 FizzBuzz

## Démarche attendue

```java
for (int i = 1; i <= 100; i++) {
    if (i % 15 == 0) {
        System.out.println("FizzBuzz");
    } else if (i % 3 == 0) {
        System.out.println("Fizz");
    } else if (i % 5 == 0) {
        System.out.println("Buzz");
    } else {
        System.out.println(i);
    }
}
```

Étapes intellectuelles :

1. **Boucler de 1 à 100** avec un `for` (nombre de tours connu).
2. **Tester le multiple de 15 en premier**. Un multiple de 15 est aussi multiple de 3 : si on testait `% 3` avant, 15 afficherait « Fizz » et jamais « FizzBuzz ».
3. **Cascade `else if`** pour ne déclencher qu'un seul cas par nombre.

## Points clés

- **L'ordre des conditions est décisif** : du cas le plus spécifique (multiple de 3 *et* 5) au plus général.
- **Le modulo `%`** teste la divisibilité : `i % 3 == 0` signifie « i est multiple de 3 ».
- **`else if`** garantit qu'on n'affiche qu'une valeur par nombre.

## Erreurs fréquentes observées

- **Tester `% 3` avant `% 15`** → les multiples de 15 affichent « Fizz » au lieu de « FizzBuzz ».
- **Utiliser des `if` séparés** (sans `else`) → certains nombres affichent deux lignes.
- **Boucler de 0 à 99 ou de 1 à 99** → décalage, dernière ligne manquante.

## Variantes possibles

On peut construire la chaîne à afficher, ce qui évite de tester `% 15` :

```java
String sortie = "";
if (i % 3 == 0) sortie += "Fizz";
if (i % 5 == 0) sortie += "Buzz";
System.out.println(sortie.isEmpty() ? Integer.toString(i) : sortie);
```

Élégant, mais l'opérateur ternaire et `isEmpty()` le rendent un peu plus avancé.

## Pour approfondir

- [Controlling the Flow of Your Code — dev.java](https://dev.java/learn/language-basics/controlling-flow/)
