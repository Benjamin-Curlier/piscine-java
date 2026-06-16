# Exercice 1.1.3 — Lecture d'une saisie

## Contexte

Un programme devient vraiment utile quand il réagit à ce que l'utilisateur saisit. Vous allez écrire un programme qui demande son prénom et son âge à la personne, puis lui renvoie un message personnalisé.

## Énoncé

Complétez la méthode `main` pour :

1. Afficher `Quel est votre prénom ?` puis lire le prénom saisi.
2. Afficher `Quel est votre âge ?` puis lire l'âge saisi (un entier).
3. Afficher un message de la forme `Bonjour <prénom>, vous avez <âge> ans.`

Le squelette (`starter/`) crée déjà le `Scanner` et affiche les deux questions. À vous de lire les réponses et de composer le message final.

## Exemple

**Exécution attendue** (l'utilisateur saisit `Dupont` puis `42`) :

```text
Quel est votre prénom ?
Quel est votre âge ?
Bonjour Dupont, vous avez 42 ans.
```

## Contraintes

- La classe doit s'appeler `LectureSaisie` et rester dans le package `piscine.m1`.
- Le prénom peut contenir des espaces (« Jean Le Goff ») : lisez **toute la ligne**.
- L'âge est un entier.
- Le message final doit être **exactement** au format de l'exemple (ponctuation et espaces compris).

## Ce qui sera vérifié

- Votre programme **compile** sans erreur.
- Il lit correctement un prénom (même composé) et un âge.
- Le message affiché correspond exactement au format attendu.

## Pour aller plus loin (optionnel — non noté)

- Que se passe-t-il si vous lisez l'âge **avant** le prénom ? Renseignez-vous sur le « piège du `nextLine` après `nextInt` » vu au chapitre 1.4.
- Comment réagiriez-vous si l'utilisateur tapait du texte là où un nombre est attendu ? (Indice : les exceptions, au module 5.)
