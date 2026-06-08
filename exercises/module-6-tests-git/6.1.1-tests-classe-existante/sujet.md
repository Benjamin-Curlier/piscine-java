# Exercice 6.1.1 — Tester une classe existante

## Contexte

Dans cet exercice, **vous n'écrivez pas de code de production** : la classe à tester
vous est **fournie et correcte**. Votre travail est d'écrire la **suite de tests**
qui la valide — exactement ce que fait la moulinette quand elle vous note.

C'est aussi ainsi que cet exercice est évalué : vos tests sont exécutés contre
l'implémentation correcte (ils doivent **passer**) et contre plusieurs
implémentations **volontairement buggées** cachées (ils doivent **échouer**, c'est-à-dire
détecter le bug). Un test qui passe partout, même sur du code faux, ne prouve rien.

## Énoncé

La classe `Temperature` (fournie dans `starter/src/main/java/etnc/m6/Temperature.java`,
**à ne pas modifier**) expose deux méthodes :

```java
public int celsiusVersFahrenheit(int celsius) // convertit °C en °F (°F = °C × 9 / 5 + 32)
public boolean estPositive(int celsius)        // vrai si la température est strictement positive (> 0)
```

Complétez la classe de test `TemperatureTest` (dans
`starter/src/test/java/etnc/m6/TemperatureTest.java`) avec des tests qui couvrent :

1. La conversion sur **0 °C** (résultat attendu : 32 °F).
2. La conversion sur une autre valeur connue (par exemple **100 °C → 212 °F**).
3. `estPositive` sur **0** (qui n'est **pas** strictement positif → `false`).
4. `estPositive` sur une valeur positive (→ `true`).
5. `estPositive` sur une valeur négative (→ `false`).

## Exemple

```text
new Temperature().celsiusVersFahrenheit(0)    // → 32
new Temperature().celsiusVersFahrenheit(100)  // → 212
new Temperature().estPositive(0)              // → false
new Temperature().estPositive(5)              // → true
```

## Contraintes

- Une classe `TemperatureTest`, des méthodes annotées `@Test`, des assertions **AssertJ**
  (`assertThat(...).isEqualTo(...)`, `assertThat(...).isTrue()` / `.isFalse()`).
- Un comportement vérifié par méthode de test (schéma Arrange-Act-Assert).
- Ne modifiez **pas** la classe `Temperature` : seul votre fichier de test est évalué.

## Ce qui sera vérifié

- Vos tests **passent** sur l'implémentation correcte de `Temperature`.
- Vos tests **détectent** (font échouer) chaque implémentation buggée cachée — notamment
  une qui oublie le `+ 32`, une qui inverse le rapport `× 9 / 5`, et une qui se trompe
  sur la frontière `0` de `estPositive`.
- La clarté et la pertinence de vos tests (critère formateur : démarche).

## Pour aller plus loin (optionnel)

- Que se passe-t-il pour une température négative très grande en valeur absolue ?
  Un test de plus ne coûte rien et documente le comportement.
