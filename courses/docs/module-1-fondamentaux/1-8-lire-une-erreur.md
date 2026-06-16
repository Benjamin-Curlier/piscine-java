---
id: 1-8-lire-une-erreur
sidebar_position: 8
title: "Lire une erreur et déboguer"
description: "Décoder un message du compilateur, lire une stack trace d'exécution, comprendre le rapport de la moulinette, et trouver un bug avec méthode (println, point d'arrêt)."
---

# Lire une erreur et déboguer

## Pourquoi ce chapitre

Vous allez passer une bonne partie de votre temps de développeur·se à **lire des erreurs**. C'est
normal, et c'est une compétence qui s'apprend. Un message d'erreur n'est pas une punition : c'est
l'ordinateur qui vous dit **où** et **pourquoi** ça coince. Apprendre à le lire, c'est diviser par dix
le temps passé bloqué·e.

## Ce que vous saurez faire à la fin

- Distinguer une **erreur de compilation** d'une **erreur à l'exécution**.
- Lire un message de `javac` : trouver le **fichier**, la **ligne** et la **cause**.
- Lire une **stack trace** : repérer le type d'exception, le message, et **la ligne de _votre_ code**.
- Décoder le rapport de la **moulinette** (attendu vs. obtenu).
- Trouver un bug avec méthode : `println` de débogage, réduction, point d'arrêt.

## Deux familles d'erreurs

| | Quand | Qui parle | Exemple |
|--|-------|-----------|---------|
| **Compilation** | avant l'exécution, quand `javac` traduit votre code | le **compilateur** | `cannot find symbol`, `';' expected` |
| **Exécution** | pendant que le programme tourne | la **JVM** (une _exception_ est levée) | `NullPointerException` |

> 🔑 **Règle d'or : lisez toujours la PREMIÈRE erreur en premier.** Une erreur en cascade en provoque
> souvent d'autres ; corrigez celle du haut, recompilez, et les suivantes disparaissent souvent d'elles-mêmes.

## Lire une erreur de compilation

```text
HelloWorld.java:14: error: cannot find symbol
        System.out.printline("Hello, world!");
                  ^
  symbol:   method printline(String)
  location: class java.io.PrintStream
```

Décodage :

- **`HelloWorld.java:14`** → le fichier et la **ligne** (14).
- **`cannot find symbol … method printline`** → Java ne connaît pas `printline`. Faute de frappe :
  la méthode s'appelle `println`.
- Le **`^`** pointe la position exacte sur la ligne.

Quelques classiques que vous rencontrerez vite :

| Message | Traduction | Cause fréquente |
|---------|-----------|-----------------|
| `';' expected` | il manque un `;` | point-virgule oublié en fin d'instruction |
| `cannot find symbol` | nom inconnu | faute de frappe, variable non déclarée, mauvais import |
| `incompatible types: String cannot be converted to int` | mauvais type | une chaîne là où un entier est attendu |
| `reached end of file while parsing` | accolade manquante | une `}` non fermée |

## Lire une stack trace (erreur à l'exécution)

Quand le programme **plante**, la JVM affiche une **stack trace** : la pile des appels au moment du crash.

```text
Exception in thread "main" java.lang.NullPointerException: Cannot invoke "String.length()" because "nom" is null
    at piscine.m1.Fiche.afficher(Fiche.java:12)
    at piscine.m1.Fiche.main(Fiche.java:5)
```

Lecture, de haut en bas :

1. **`java.lang.NullPointerException`** → le **type** de l'erreur (ici, on a utilisé une référence `null`).
2. **`… because "nom" is null`** → le **message** : la variable `nom` valait `null`.
3. **`at piscine.m1.Fiche.afficher(Fiche.java:12)`** → la **ligne 12** de _votre_ fichier, là où ça a
   planté. **C'est presque toujours la première ligne `at …` qui mentionne VOTRE classe** qui compte.

> 💡 Dans une longue trace, **cherchez le nom de vos fichiers** (`Fiche.java:12`). Les lignes
> `java.base/...` viennent de la bibliothèque standard — rarement votre faute.

### « Caused by »

Une trace peut contenir un bloc **`Caused by:`** : c'est la **cause d'origine**, souvent la plus utile.
Lisez-la avant tout.

## Le rapport de la moulinette

La moulinette ne vous jette pas la trace brute. Pour un test qui échoue, elle montre **ce qui était
attendu** et **ce que votre programme a produit** :

```text
✗ affiche la fiche au format attendu
   attendu : "=== Fiche membre ===\nNom    : Martin"
   obtenu  : ""
```

La **différence** entre `attendu` et `obtenu` est exactement ce qu'il faut corriger. Ici, votre
programme n'a **rien affiché** (`obtenu : ""`) : vous avez sans doute oublié le `System.out.println`.
Le `\n` représente un retour à la ligne.

## Trouver un bug avec méthode

Quand le code compile, tourne, mais donne un **mauvais résultat** (pas de crash, mais pas le bon
affichage) :

1. **Relisez l'attendu vs. l'obtenu.** Où diffèrent-ils _exactement_ ? (un espace ? une majuscule ?
   une ligne en trop ?)
2. **`println` de débogage.** Affichez les valeurs intermédiaires pour voir ce que fait _vraiment_
   votre code :
   ```java
   System.out.println("DEBUG nom = [" + nom + "]");
   ```
   Les crochets `[ ]` rendent visibles les espaces et le vide. **Retirez ces lignes** une fois le bug trouvé.
3. **Réduisez.** Isolez le plus petit cas qui reproduit le problème.
4. **Le débogueur de l'IDE** (IntelliJ, VS Code…) : posez un **point d'arrêt** sur la ligne suspecte,
   lancez en mode _Debug_, et avancez **pas à pas** en observant les variables. C'est l'outil le plus
   puissant — on s'en sert toute sa carrière.

## En résumé

- Lisez **la première erreur**, **de haut en bas**.
- Compilation → `fichier:ligne` + cause. Exécution → type + message + **votre** ligne dans la trace.
- Moulinette → comparez **attendu** et **obtenu**.
- Bloqué·e sans crash → `println` de débogage, puis le **débogueur**.

## Pour aller plus loin (optionnel)

- Provoquez volontairement chaque erreur du tableau (oubliez un `;`, appelez `printline`) pour
  reconnaître les messages instantanément.
- Essayez le **débogueur** de votre IDE sur un exercice déjà réussi : posez un point d'arrêt,
  observez les variables changer.
- Les exceptions seront étudiées en profondeur au **module 5** (les _attraper_ et en _créer_).
