# Correction — Exercice 4.2.3 Tri de soldats (Comparable<T>)

## Démarche attendue

1. **Grade** : `Grade` est une enum qui implémente `Comparable<Grade>` dans
   l'ordre de déclaration. `this.grade().compareTo(autre.grade())` renvoie donc
   un signe correct sans aucun code supplémentaire.

2. **Composition de critères** : si le résultat de la comparaison des grades
   est non nul, on le retourne directement. Si les grades sont égaux (`c == 0`),
   on départage par le nom avec `this.nom().compareTo(autre.nom())`.

3. **Code final** :

```java
@Override
public int compareTo(Soldat autre) {
    int c = this.grade().compareTo(autre.grade());
    return c != 0 ? c : this.nom().compareTo(autre.nom());
}
```

## Points clés

- **`Comparable<T>` générique** : contrairement à l'interface `Ordonnable`
  de l'exercice 3.4.3 (qui acceptait n'importe quel `Ordonnable`), `Comparable<Soldat>`
  garantit à la compilation que l'on compare deux `Soldat` — pas de downcast nécessaire.
- **Composition de critères** : le pattern `int c = critere1(); return c != 0 ? c : critere2();`
  est l'idiome standard pour enchaîner des critères de tri.
- **Record et `compareTo`** : un record Java peut implémenter des interfaces ;
  les accesseurs (`nom()`, `grade()`, `anciennete()`) sont générés automatiquement.
- **Enum naturellement comparable** : l'ordre de déclaration des constantes
  enum est leur ordre naturel — pas besoin de convertir en entier.

## Erreurs fréquentes observées

- Comparer `this.grade().ordinal()` avec `autre.grade().ordinal()` : correct
  mais inutilement verbeux ; `grade().compareTo(autre.grade())` suffit.
- Oublier le second critère (nom) et renvoyer `c` directement : deux soldats
  de même grade mais de noms différents seraient considérés égaux.
- Retourner `this.grade().ordinal() - autre.grade().ordinal()` : risqué pour
  la soustraction (débordement possible sur de grands entiers).

## Pour approfondir

- L'exercice 4.2.4 montre `Comparator.comparing(Soldat::grade).thenComparing(Soldat::nom)` :
  équivalent fonctionnel de ce `compareTo`, mais sans modifier la classe.
