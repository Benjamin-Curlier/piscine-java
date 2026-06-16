# Exercice 1.1.2 — Affichage formaté

## Contexte

Au bureau de votre équipe, on édite des fiches récapitulatives pour chaque membre. Vous allez écrire un petit programme qui affiche une fiche proprement présentée, à partir de données déjà fournies dans le code.

## Énoncé

Le squelette de code (`starter/`) déclare déjà trois variables : un nom, un niveau et un âge. Vous devez **compléter la méthode `main`** pour afficher la fiche **exactement** au format ci-dessous.

Vous ne lisez rien au clavier : les valeurs sont fixées dans le code.

## Exemple

**Exécution attendue :**

```text
=== Fiche membre ===
Nom    : Martin
Niveau : Confirmé
Age    : 29 ans
```

## Contraintes

- La classe doit s'appeler `AffichageFormate` et rester dans le package `piscine.m1`.
- La sortie doit être **exactement** celle de l'exemple (espaces, deux-points, retours à la ligne compris).
- Vous ne modifiez **que** le corps de la méthode `main`, sans changer les valeurs des variables fournies.
- Une instruction d'affichage par ligne à produire (utilisez `System.out.println`).

## Ce qui sera vérifié

- Votre programme **compile** sans erreur.
- La sortie correspond **caractère pour caractère** à la fiche attendue.
- Le programme se termine sans lever d'exception.

## Pour aller plus loin (optionnel — non noté)

- Comment afficheriez-vous une ligne supplémentaire « Équipe : … » en gardant l'alignement des deux-points ?
- À votre avis, que se passe-t-il si vous remplacez un `println` par un `print` ? Essayez et observez.
