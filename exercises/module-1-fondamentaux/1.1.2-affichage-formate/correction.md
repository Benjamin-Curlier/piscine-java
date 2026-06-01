# Correction — Exercice 1.1.2 Affichage formaté

## Démarche attendue

Quatre instructions `System.out.println(...)`, une par ligne de la fiche :

```java
System.out.println("=== Fiche militaire ===");
System.out.println("Nom    : " + nom);
System.out.println("Grade  : " + grade);
System.out.println("Age    : " + age + " ans");
```

Étapes intellectuelles :

1. **Repérer le format exact** demandé dans le sujet : titre encadré, puis trois lignes `libellé : valeur`.
2. **Aligner les libellés** : ici chaque libellé occupe 7 caractères avant les deux-points (`Nom    `, `Grade  `, `Age    `). C'est ce qui rend la fiche lisible.
3. **Concaténer** texte fixe et variables avec `+`. Pour l'âge, on ajoute aussi le suffixe `" ans"`.

## Points clés

- **Sortie exacte** : la moulinette compare caractère pour caractère. Une espace en trop ou un deux-points mal placé fait échouer le test.
- **`println` ajoute le retour à la ligne** adapté au système : pas besoin de l'écrire soi-même.
- **Concaténation** : `"Age    : " + age + " ans"` mélange sans problème texte et `int` ; Java convertit le nombre en texte automatiquement.

## Erreurs fréquentes observées

- **Alignement non respecté** (`Nom : ` au lieu de `Nom    : `) → la sortie ne correspond plus à l'attendu.
- **Oubli du suffixe `" ans"`** sur la ligne de l'âge.
- **Utilisation de `print` au lieu de `println`** → les lignes se collent, plus aucun retour à la ligne.
- **Modification des valeurs fournies** → la fiche affichée ne correspond plus à l'exemple du sujet.

## Variantes possibles

On pourrait construire toute la fiche dans une seule chaîne avec des `\n`, mais c'est moins lisible pour un débutant :

```java
System.out.println("=== Fiche militaire ===\nNom    : " + nom + "\n...");
```

Quatre `println` séparés sont plus clairs et plus faciles à modifier.

## Pour approfondir

- [`System.out` et la classe `PrintStream` — Javadoc](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/io/PrintStream.html)
- [La concaténation de chaînes — Baeldung](https://www.baeldung.com/java-string-concatenation)
