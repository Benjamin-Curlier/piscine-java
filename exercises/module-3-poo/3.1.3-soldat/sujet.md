# Exercice 3.1.3 — Soldat

## Contexte

Dans une simulation d'exercice, chaque combattant est suivi par ses points de
vie. Vous modélisez un soldat capable d'encaisser des dégâts, d'être soigné et
d'indiquer s'il est encore opérationnel.

## Énoncé

Complétez la classe `Soldat` fournie dans le `starter/`. Elle représente un
soldat avec un nom, un grade et des points de vie, et doit proposer :

- un constructeur `Soldat(String nom, String grade, int pointsDeVie)` ;
- trois accesseurs `getNom()`, `getGrade()`, `getPointsDeVie()` ;
- une méthode `subirDegats(int degats)` qui retire des points de vie **sans
  jamais descendre sous 0** ;
- une méthode `soigner(int soin)` qui ajoute des points de vie ;
- une méthode `estVivant()` qui renvoie `true` tant que les points de vie sont
  strictement positifs ;
- une redéfinition de `toString()` au format `<nom> (<grade>) - <pv> PV`.

## Exemple

```text
Soldat s = new Soldat("Martin", "Sergent", 100);
s.subirDegats(30);   // 70 PV
s.soigner(10);       // 80 PV
s.estVivant();       // true
s.toString();        // "Martin (Sergent) - 80 PV"
```

## Contraintes

- La classe doit s'appeler `Soldat` et rester dans le package `etnc.m3`.
- **Ne modifiez pas les signatures publiques** : les tests s'appuient dessus.
- Les attributs doivent être **privés**.
- `subirDegats` ne doit jamais laisser les points de vie en dessous de 0.
- `toString()` doit produire exactement `<nom> (<grade>) - <pv> PV`.

## Ce qui sera vérifié

- La construction et les accesseurs renvoient les bonnes valeurs.
- `subirDegats` réduit les points de vie et les **plafonne à 0** quand les
  dégâts dépassent les points de vie restants.
- `soigner` augmente les points de vie, y compris après des dégâts.
- `estVivant` renvoie `false` à 0 point de vie.
- `toString` respecte le format attendu.

## Pour aller plus loin (optionnel — non noté)

- Comment empêcheriez-vous des dégâts ou des soins **négatifs** ? (La validation
  des entrées est l'objet du prochain sous-groupe.)
- Comment ajouteriez-vous un plafond de points de vie (le soin ne dépasse pas un
  maximum) ?
