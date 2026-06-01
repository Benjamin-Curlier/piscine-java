# Correction — Exercice 1.3.3 Table de multiplication

## Démarche attendue

```java
int n = clavier.nextInt();
for (int i = 1; i <= 10; i++) {
    System.out.println(n + " x " + i + " = " + (n * i));
}
```

Étapes intellectuelles :

1. **Lire `n`** au clavier.
2. **Boucler de 1 à 10** : nombre de tours connu → la boucle `for` est le choix naturel.
3. **Composer chaque ligne** par concaténation, en pensant aux **parenthèses** autour de `n * i`.

## Points clés

- **Les parenthèses autour de `n * i`** sont importantes : sans elles, `"= " + n * i` reste correct (la multiplication passe avant la concaténation), mais les ajouter rend l'intention claire et évite les surprises.
- **`for` plutôt que `while`** : on sait qu'on fait exactement 10 tours.
- **Format exact** : espaces autour du `x` et du `=`.

## Erreurs fréquentes observées

- **Oublier les parenthèses dans un contexte ambigu** : `n + " x " + i + " = " + n * i` fonctionne ici, mais `"" + n + i` collerait les chiffres. En cas de doute, parenthésez le calcul.
- **Boucler de 0 à 10 ou de 1 à 9** → ligne en trop ou manquante.
- **Format approximatif** (pas d'espace autour du `=`).

## Variantes possibles

On pourrait formater avec un alignement des colonnes, mais cela dépasse le périmètre demandé et complique la comparaison de sortie.

## Pour approfondir

- [Controlling the Flow of Your Code — dev.java](https://dev.java/learn/language-basics/controlling-flow/)
