# Correction — Exercice 1.2.3 Manipulation booléenne

## Démarche attendue

```java
int annee = clavier.nextInt();
boolean estBissextile = (annee % 4 == 0 && annee % 100 != 0) || annee % 400 == 0;
if (estBissextile) {
    System.out.println(annee + " est bissextile.");
} else {
    System.out.println(annee + " n'est pas bissextile.");
}
```

Étapes intellectuelles :

1. **Traduire la règle** en une expression booléenne. Le « sauf » se traduit par `&& annee % 100 != 0`, et l'exception à l'exception (les multiples de 400) par `|| annee % 400 == 0`.
2. **Stocker dans un `boolean` nommé** : la condition d'affichage devient triviale et lisible.
3. **Afficher** la bonne phrase selon le `boolean`.

## Points clés

- **Priorité des opérateurs** : `&&` est évalué avant `||`. Les parenthèses autour de `(annee % 4 == 0 && annee % 100 != 0)` clarifient l'intention même si elles ne sont pas strictement nécessaires.
- **Le piège des années séculaires** : 1900 est divisible par 4 et par 100 mais **pas** par 400 → non bissextile. 2000 est divisible par 400 → bissextile. C'est tout l'enjeu de la règle.
- **Variable booléenne nommée** : préférable à mettre toute l'expression dans le `if`, pour la lisibilité.

## Erreurs fréquentes observées

- **Tester seulement `annee % 4 == 0`** → 1900 serait déclaré bissextile à tort.
- **Oublier le cas `% 400`** → 2000 serait déclaré non bissextile à tort.
- **Mauvaise phrase de sortie** (`n'est pas` mal orthographié, point final manquant).

## Variantes possibles

Avec l'opérateur ternaire, on peut produire directement le texte :

```java
String reponse = estBissextile ? " est bissextile." : " n'est pas bissextile.";
System.out.println(annee + reponse);
```

Lisible ici car le `boolean` reste calculé à part.

## Pour approfondir

- [Operators — dev.java](https://dev.java/learn/language-basics/operators/)
- [Année bissextile — règle de calcul](https://fr.wikipedia.org/wiki/Ann%C3%A9e_bissextile)
