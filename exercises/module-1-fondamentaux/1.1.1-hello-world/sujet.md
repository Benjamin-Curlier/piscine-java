# Exercice 1.1.1 — Hello World

## Contexte

Bienvenue dans la Piscine. Cet exercice est volontairement minimaliste : son but est de vous faire passer en revue le cycle complet **écrire → compiler → exécuter** avec un programme dont vous ne pourrez pas vous tromper sur la logique.

Comme tout militaire en début de cursus, vous commencez par vous présenter. Votre programme va faire la même chose : se présenter au monde.

## Énoncé

Écrivez un programme Java qui affiche exactement le texte suivant, suivi d'un retour à la ligne :

```text
Hello, world!
```

Votre classe doit s'appeler **`HelloWorld`** et appartenir au **package `etnc.m1`**. Le squelette de code vous est fourni dans le dossier `starter/` — vous devez uniquement compléter le contenu de la méthode `main`.

## Exemple

**Exécution attendue (depuis la ligne de commande) :**

```text
$ java etnc.m1.HelloWorld
Hello, world!
```

## Contraintes

- La classe doit s'appeler `HelloWorld` et être dans le package `etnc.m1`.
- Le texte affiché doit être exactement `Hello, world!` (attention aux espaces, à la majuscule, à la ponctuation).
- Aucune entrée utilisateur, aucun argument de ligne de commande à lire.
- Vous ne devez **rien modifier** en dehors du corps de la méthode `main` (n'ajoutez pas d'imports, ne renommez pas la classe, ne touchez pas au package).

## Ce qui sera vérifié

- Votre programme **compile** sans erreur.
- L'exécution affiche **exactement** la chaîne attendue suivie d'un retour à la ligne.
- Vous utilisez la méthode `main` standard (`public static void main(String[] args)`).
- Le programme se termine sans lever d'exception.

## Pour aller plus loin (optionnel — non noté)

- Que se passe-t-il si vous remplacez `println` par `print` ? Lancez et observez. Comment expliqueriez-vous la différence à un camarade ?
- Pourquoi `args` apparaît-il dans la signature du `main` même si vous ne l'utilisez pas ici ? Essayez d'afficher `args.length` après votre message pour vérifier votre hypothèse.
