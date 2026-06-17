# Correction — Exercice 3.1.4 Égalité de valeur (equals / hashCode)

## Démarche attendue

1. **`equals(Object autre)`** suit le squelette canonique :
   - `if (this == autre) return true;` — court-circuit (réflexivité, perf) ;
   - `if (autre == null || getClass() != autre.getClass()) return false;` —
     rejet de `null` et d'un type différent ;
   - downcast `(Carte) autre`, puis comparaison champ par champ :
     `valeur == carte.valeur && Objects.equals(couleur, carte.couleur)`.
2. **`hashCode()`** : `return Objects.hash(valeur, couleur);` — il utilise
   **exactement** les champs comparés par `equals`, ce qui garantit la cohérence.
3. **`toString()`** : `return valeur + " de " + couleur;`.

```java
@Override
public boolean equals(Object autre) {
    if (this == autre) {
        return true;
    }
    if (autre == null || getClass() != autre.getClass()) {
        return false;
    }
    Carte carte = (Carte) autre;
    return valeur == carte.valeur && Objects.equals(couleur, carte.couleur);
}

@Override
public int hashCode() {
    return Objects.hash(valeur, couleur);
}
```

## Points clés

- **Le contrat equals/hashCode** : si `a.equals(b)` alors
  `a.hashCode() == b.hashCode()`. Les collections de hachage (`HashSet`,
  `HashMap`) s'appuient d'abord sur `hashCode` pour choisir un « seau », puis sur
  `equals` à l'intérieur. Une incohérence rend un élément **introuvable**.
- **Mêmes champs des deux côtés** : `equals` et `hashCode` doivent reposer sur le
  **même** ensemble de champs (ici `valeur` et `couleur`).
- **`getClass()` vs `instanceof`** : `getClass()` garantit la symétrie même en
  présence de sous-classes ; on l'utilise ici car `Carte` n'est pas conçue pour
  être étendue.
- **`Objects.equals`** gère le cas `null` sans `NullPointerException`.

## Erreurs fréquentes observées

- Redéfinir `equals` **sans** redéfinir `hashCode` (ou l'inverse) : le `HashSet`
  ne dédoublonne plus et `contains` échoue sur une carte pourtant « égale ».
- Signature erronée `boolean equals(Carte autre)` : ce n'est **pas** une
  redéfinition de `Object.equals(Object)` (c'est une surcharge), donc les
  collections ne l'appellent jamais. Le `@Override` aurait levé une erreur.
- Oublier le test `autre == null` ou `getClass()` : `equals(null)` plante ou
  renvoie `true` à tort.
- `hashCode` qui renvoie une constante (`return 0;`) : « correct » mais ruine les
  performances (toutes les cartes dans le même seau).
- Comparer les couleurs avec `==` au lieu de `equals` / `Objects.equals`.

## Pour approfondir

- `record` (module 3.4) : génère `equals`, `hashCode` et `toString` corrects
  automatiquement.
- `Objects.hash(...)` vs un calcul manuel `31 * result + champ` (ce que génère
  l'IDE).
