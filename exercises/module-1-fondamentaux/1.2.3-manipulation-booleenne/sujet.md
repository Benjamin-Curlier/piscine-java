# Exercice 1.2.3 — Manipulation booléenne (année bissextile)

## Contexte

Calculer une date, planifier un calendrier d'opérations : il faut parfois savoir si une année est **bissextile** (366 jours au lieu de 365). La règle paraît simple, mais comporte un piège que vous allez traduire en expression booléenne.

## Énoncé

Complétez la méthode `main` pour :

1. Afficher `Quelle année ?` (déjà fait dans le squelette).
2. Lire une année (un entier).
3. Déterminer si elle est bissextile, **dans une variable `boolean`**.
4. Afficher `<année> est bissextile.` ou `<année> n'est pas bissextile.` selon le cas.

**La règle :** une année est bissextile si elle est divisible par 4, **sauf** les années divisibles par 100 qui ne le sont pas par 400. En expression :

```text
(année % 4 == 0 && année % 100 != 0) || année % 400 == 0
```

## Exemple

**Exécution attendue** (l'utilisateur saisit `2024`) :

```text
Quelle année ?
2024 est bissextile.
```

## Contraintes

- La classe doit s'appeler `AnneeBissextile` et rester dans le package `etnc.m1`.
- Calculez le résultat dans une variable `boolean` avant de l'afficher.
- Utilisez le modulo `%` et les opérateurs logiques `&&`, `||`.
- Respectez exactement les deux phrases de sortie.

## Ce qui sera vérifié

- Votre programme **compile** sans erreur.
- La règle est correcte, **y compris pour les années séculaires** (1900, 2000…).
- La sortie respecte le format attendu.

## Pour aller plus loin (optionnel — non noté)

- Pourquoi 1900 n'est-il **pas** bissextile alors que 2000 l'est ? Testez les deux.
- Sauriez-vous écrire la même condition avec l'opérateur ternaire pour produire directement le texte ?
