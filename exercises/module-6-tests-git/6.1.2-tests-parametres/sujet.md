# Exercice 6.1.2 — Tests paramétrés

## Contexte

Quand une méthode se comporte différemment selon des **paliers** de valeurs, la tester
avec un test par valeur devient vite répétitif. Un **test paramétré**
(`@ParameterizedTest` + `@CsvSource`) exécute la même logique de test sur plusieurs
couples « entrée → attendu », de façon lisible et compacte.

Comme pour l'exercice précédent, vous écrivez **uniquement les tests** : la classe est
fournie correcte, et vos tests sont gradés par mutation (ils doivent passer sur le
correct et détecter chaque version buggée).

## Énoncé

La classe `Classement` (fournie, **à ne pas modifier**) attribue une **mention** à une
note sur 20 :

```java
public String mention(int note)
```

Les paliers sont :

| Note | Mention |
|---|---|
| 16 à 20 | `Très bien` |
| 14 à 15 | `Bien` |
| 12 à 13 | `Assez bien` |
| 10 à 11 | `Passable` |
| 0 à 9 | `Insuffisant` |

Complétez `ClassementTest` avec **un test paramétré** (`@ParameterizedTest` + `@CsvSource`)
qui couvre **chaque palier** et surtout ses **frontières** (10, 12, 14, 16 — la première
note de chaque mention, et la dernière du palier précédent).

## Exemple

```text
new Classement().mention(20) // → "Très bien"
new Classement().mention(15) // → "Bien"
new Classement().mention(12) // → "Assez bien"
new Classement().mention(10) // → "Passable"
new Classement().mention(9)  // → "Insuffisant"
```

## Contraintes

- **Le test doit être paramétré** : `@ParameterizedTest` + `@CsvSource` (pas une série de
  `@Test` séparés). C'est l'objet de l'exercice (critère formateur : respect des consignes).
- Couvrez chaque palier **et** ses frontières basses (10, 12, 14, 16) ainsi qu'une valeur
  juste en dessous (9, 11, 13, 15).
- Ne modifiez pas la classe `Classement`.

## Ce qui sera vérifié

- Vos tests **passent** sur l'implémentation correcte.
- Vos tests **détectent** chaque version buggée : une qui décale la frontière `Passable`
  (10 mal classé), une qui se trompe de seuil `Très bien` (15 mal classé), une qui
  renvoie toujours la même mention.
- L'usage effectif de `@ParameterizedTest` + `@CsvSource` (critère formateur).

## Pour aller plus loin (optionnel)

- Que se passe-t-il aux bornes 0 et 20 ? Ajoutez-les à votre `@CsvSource`.
