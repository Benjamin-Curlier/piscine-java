# Correction — Exercice 3.4.2 Grade (enum)

## Démarche attendue

`categorie()` renvoie une chaîne selon `this`, par un `switch` expression :

```java
return switch (this) {
    case SOLDAT, CAPORAL -> "Militaire du rang";
    case SERGENT, ADJUDANT -> "Sous-officier";
    case LIEUTENANT -> "Officier";
};
```

## Points clés

- **Enum = ensemble fini** : le compilateur connaît toutes les constantes, donc
  le `switch` peut être **exhaustif sans `default`**.
- **Pas de `default`** : si l'on ajoutait un grade, le compilateur signalerait le
  `switch` incomplet — un garde-fou précieux qu'un `default` masquerait.
- **Attribut d'enum** : chaque constante porte sa `soldeBase`, donnée entre
  parenthèses et mémorisée par le constructeur (implicitement `private`).

## Erreurs fréquentes observées

- Ajouter un `default` inutile (masque la vérification d'exhaustivité).
- Oublier une constante dans le `switch` (ne compile pas, c'est voulu).
- Texte de catégorie inexact.

## Pour approfondir

- `values()` et `valueOf(String)`, générées pour tout enum.
