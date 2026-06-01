# Correction — Exercice 1.3.2 Suite de Fibonacci (itérative)

## Démarche attendue

```java
int n = clavier.nextInt();
long a = 0;   // F(i)
long b = 1;   // F(i+1)
for (int i = 0; i < n; i++) {
    long suivant = a + b;
    a = b;
    b = suivant;
}
System.out.println("F(" + n + ") = " + a);
```

Étapes intellectuelles :

1. **Deux variables qui glissent** : `a` représente `F(i)`, `b` représente `F(i+1)`. À chaque tour, on avance d'un cran.
2. **Le nombre de tours** : après `n` tours, `a` contient `F(n)`. Cela gère naturellement `F(0) = 0` (zéro tour) et `F(1) = 1` (un tour).
3. **Type `long`** : les termes grandissent vite ; `long` repousse le dépassement de capacité.

## Points clés

- **Itératif, pas récursif** : une simple boucle suffit et reste efficace. La récursivité (vue plus tard) serait ici plus lente et plus risquée pour la pile.
- **Variable temporaire `suivant`** : indispensable pour ne pas écraser `a` avant de l'avoir copié dans `b`.
- **Cas de base gratuits** : avec `a = 0`, `b = 1` et `n` tours, `F(0)` et `F(1)` sortent sans condition spéciale.

## Erreurs fréquentes observées

- **Oublier la variable temporaire** : faire `a = b; b = a + b;` utilise le nouveau `a`, le calcul est faux.
- **Mauvais nombre de tours** (`i <= n`) → on calcule `F(n+1)`.
- **Utiliser `int`** → débordement silencieux pour les grands rangs.

## Variantes possibles

On peut traiter explicitement les cas de base avant la boucle, mais ce n'est pas nécessaire avec l'initialisation `a = 0, b = 1`.

## Pour approfondir

- [Dépassement de capacité des entiers — Baeldung](https://www.baeldung.com/java-integer-overflow)
