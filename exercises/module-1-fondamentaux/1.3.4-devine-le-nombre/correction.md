# Correction — Exercice 1.3.4 Devine le nombre

## Démarche attendue

```java
int essais = 0;
boolean trouve = false;
while (!trouve) {
    int essai = clavier.nextInt();
    essais++;
    if (essai < secret) {
        System.out.println("C'est plus grand.");
    } else if (essai > secret) {
        System.out.println("C'est plus petit.");
    } else {
        System.out.println("Bravo, vous avez trouvé en " + essais + " essai(s) !");
        trouve = true;
    }
}
```

Étapes intellectuelles :

1. **Une boucle `while`** dont on ne connaît pas le nombre de tours à l'avance : on répète tant que le nombre n'est pas trouvé.
2. **Compter les essais** : un compteur incrémenté à chaque lecture.
3. **Trois cas** : plus grand, plus petit, ou trouvé. Le dernier cas affiche la victoire et met fin à la boucle.

## Points clés

- **Condition d'arrêt** : ici un booléen `trouve`. On aurait aussi pu écrire `while (essai != secret)`, à condition de lire un premier essai avant la boucle — la version avec `trouve` évite cette duplication.
- **`while` plutôt que `for`** : le nombre de tentatives est inconnu, c'est le cas typique du `while`.
- **Le secret est fixé** par une graine : volontaire, pour que la correction automatique soit reproductible. En usage réel, on enlèverait la graine pour un vrai tirage aléatoire.

## Erreurs fréquentes observées

- **Oublier d'incrémenter le compteur** ou le placer au mauvais endroit → mauvais nombre d'essais affiché.
- **Boucle infinie** : ne jamais passer `trouve` à `true` (ou condition d'arrêt incorrecte).
- **Inverser « plus grand » et « plus petit »** : si l'essai est **inférieur** au secret, c'est le secret qui est *plus grand*.
- **Messages mal orthographiés** → sortie non conforme.

## Variantes possibles

Avec un `do-while`, on peut lire au moins une fois avant de tester. Mais la version `while (!trouve)` reste la plus lisible et gère naturellement tous les cas.

## Pour approfondir

- [Controlling the Flow of Your Code — dev.java](https://dev.java/learn/language-basics/controlling-flow/)
- [La classe `Random` — Javadoc OpenJDK 25](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/Random.html)
