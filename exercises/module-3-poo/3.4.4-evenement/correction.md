# Correction — Exercice 3.4.4 Journal d'événements (sealed)

## Démarche attendue

`resumer` branche sur le type réel par pattern matching :

```java
return switch (e) {
    case Connexion c -> "Connexion de " + c.utilisateur();
    case Deconnexion d -> "Deconnexion de " + d.utilisateur();
    case Erreur err -> "Erreur " + err.code() + " : " + err.message();
};
```

## Points clés

- **`sealed` + `switch`** : la hiérarchie étant fermée (`permits`), le compilateur
  connaît tous les cas et vérifie l'exhaustivité. **Pas de `default`.**
- **Pattern matching** : `case Connexion c` teste le type **et** lie la variable
  `c` (déjà castée), prête à l'emploi.
- **Records** : on lit leurs composants via les accesseurs générés
  (`utilisateur()`, `code()`, `message()`).

## Erreurs fréquentes observées

- Ajouter un `default` : il masque la vérification d'exhaustivité (si un type est
  ajouté plus tard, plus d'avertissement).
- Oublier un cas (ne compile pas — c'est le garde-fou voulu).
- Mauvais format de chaîne (espaces autour de « : »).

## Pour approfondir

- Le pattern matching avancé (déstructuration de records dans le `switch`).
