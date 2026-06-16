# Exercice 3.4.4 — Journal d'événements (sealed)

## Contexte

Un système de supervision enregistre des événements de natures connues et
**fermées** : connexion, déconnexion, erreur. Une hiérarchie `sealed` garantit
qu'aucun autre type n'existe.

## Énoncé

La hiérarchie vous est **fournie** :

```java
public sealed interface Evenement permits Connexion, Deconnexion, Erreur {}
public record Connexion(String utilisateur) implements Evenement {}
public record Deconnexion(String utilisateur) implements Evenement {}
public record Erreur(int code, String message) implements Evenement {}
```

Complétez `Journal.resumer(Evenement e)` avec un **`switch` exhaustif sans
`default`** :

- `Connexion` → `"Connexion de " + utilisateur`
- `Deconnexion` → `"Deconnexion de " + utilisateur`
- `Erreur` → `"Erreur " + code + " : " + message`

## Exemple

```text
Journal.resumer(new Connexion("alice"));        // "Connexion de alice"
Journal.resumer(new Erreur(404, "Not Found"));  // "Erreur 404 : Not Found"
```

## Contraintes

- Package `piscine.m3`. **Ne modifiez pas** la hiérarchie `Evenement`.
- Le `switch` est **exhaustif sans `default`** : la hiérarchie sealed garantit
  que tous les cas sont couverts (le compilateur le vérifie).
- Accédez aux composants des records par leurs accesseurs (`c.utilisateur()`,
  `err.code()`, `err.message()`).

## Ce qui sera vérifié

- `resumer` pour chaque type d'événement.
- Le traitement d'un tableau hétérogène `Evenement[]`.
- Les accesseurs et l'`equals` générés des records.

## Pour aller plus loin (optionnel — non noté)

- Que se passe-t-il si l'on ajoute un type à `permits` sans traiter son cas ?
- Pourquoi un `default` serait-il déconseillé ici ?
