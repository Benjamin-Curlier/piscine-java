# Correction — Exercice 4.2.3 Tri de membres (Comparable<T>)

## Démarche attendue

1. **Niveau** : `Niveau` est une enum qui implémente `Comparable<Niveau>` dans
   l'ordre de déclaration. `this.niveau().compareTo(autre.niveau())` renvoie donc
   un signe correct sans aucun code supplémentaire.

2. **Composition de critères** : si le résultat de la comparaison des niveaux
   est non nul, on le retourne directement. Si les niveaux sont égaux (`c == 0`),
   on départage par le nom avec `this.nom().compareTo(autre.nom())`.

3. **Code final** :

```java
@Override
public int compareTo(Membre autre) {
    int c = this.niveau().compareTo(autre.niveau());
    return c != 0 ? c : this.nom().compareTo(autre.nom());
}
```

## Points clés

- **`Comparable<T>` générique** : contrairement à l'interface `Ordonnable`
  de l'exercice 3.4.3 (qui acceptait n'importe quel `Ordonnable`), `Comparable<Membre>`
  garantit à la compilation que l'on compare deux `Membre` — pas de downcast nécessaire.
- **Composition de critères** : le pattern `int c = critere1(); return c != 0 ? c : critere2();`
  est l'idiome standard pour enchaîner des critères de tri.
- **Record et `compareTo`** : un record Java peut implémenter des interfaces ;
  les accesseurs (`nom()`, `niveau()`, `anciennete()`) sont générés automatiquement.
- **Enum naturellement comparable** : l'ordre de déclaration des constantes
  enum est leur ordre naturel — pas besoin de convertir en entier.

## Erreurs fréquentes observées

- Comparer `this.niveau().ordinal()` avec `autre.niveau().ordinal()` : correct
  mais inutilement verbeux ; `niveau().compareTo(autre.niveau())` suffit.
- Oublier le second critère (nom) et renvoyer `c` directement : deux membres
  de même niveau mais de noms différents seraient considérés égaux.
- Retourner `this.niveau().ordinal() - autre.niveau().ordinal()` : risqué pour
  la soustraction (débordement possible sur de grands entiers).

## Pour approfondir

- L'exercice 4.2.4 montre `Comparator.comparing(Membre::niveau).thenComparing(Membre::nom)` :
  équivalent fonctionnel de ce `compareTo`, mais sans modifier la classe.
