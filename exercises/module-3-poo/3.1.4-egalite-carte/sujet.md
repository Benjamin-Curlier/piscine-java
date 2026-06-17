# Exercice 3.1.4 — Égalité de valeur (equals / hashCode)

## Contexte

Dans un jeu de cartes, deux exemplaires du « 7 de Cœur » désignent **la même
carte**, même s'il s'agit d'objets différents en mémoire. Par défaut, Java
compare les objets par **référence** (l'opérateur `==`), ce qui n'est pas ce que
l'on veut ici. Vous allez définir une **égalité de valeur**.

## Énoncé

Complétez la classe `Carte` fournie dans le `starter/`. Une carte possède une
`valeur` (un `int`) et une `couleur` (un `String`), tous deux **privés et
finaux**, initialisés par le constructeur. Les accesseurs `getValeur()` et
`getCouleur()` sont déjà en place.

Vous devez :

- redéfinir `equals(Object autre)` : deux cartes sont **égales** lorsqu'elles ont
  **la même valeur ET la même couleur** ;
- redéfinir `hashCode()` **de façon cohérente** avec `equals` ;
- redéfinir `toString()` au format `"valeur de couleur"` (ex. `"7 de Coeur"`).

## Exemple

```text
Carte a = new Carte(7, "Coeur");
Carte b = new Carte(7, "Coeur");

a == b;            // false  (deux objets distincts)
a.equals(b);       // true   (même valeur de carte)
a.hashCode() == b.hashCode();  // true

Set<Carte> jeu = new HashSet<>();
jeu.add(a);
jeu.add(b);
jeu.size();        // 1  (le doublon est ignoré)
```

## Contraintes

- La classe doit s'appeler `Carte` et rester dans le package `piscine.m3`.
- **Ne modifiez pas les signatures publiques** : les tests s'appuient dessus.
- `equals` doit respecter son **contrat** : réflexivité (`x.equals(x)`),
  symétrie, transitivité, et `x.equals(null)` doit valoir `false`.
- `equals` doit renvoyer `false` pour un objet d'un **type différent**.
- **Règle d'or** : si deux objets sont égaux selon `equals`, ils **doivent**
  avoir le même `hashCode`. `java.util.Objects.hash(...)` et
  `java.util.Objects.equals(...)` sont idiomatiques.

## Ce qui sera vérifié

- `equals` : réflexivité, symétrie, transitivité, et rejet de `null` / d'un
  autre type.
- Deux cartes égales ont le **même** `hashCode`.
- Cohérence avec `HashSet` : les doublons sont dédoublonnés, et `contains`
  retrouve une carte **égale mais non identique**.
- `toString` respecte le format attendu.

## Pour aller plus loin (optionnel — non noté)

- Que se passe-t-il dans un `HashSet` si l'on redéfinit `equals` **sans**
  redéfinir `hashCode` ? Essayez mentalement avec deux cartes égales.
- Un `record Carte(int valeur, String couleur)` génère automatiquement `equals`,
  `hashCode` et `toString`. Réécrivez la classe sous forme de record et observez
  ce qui disparaît (notion approfondie au module 3.4).
