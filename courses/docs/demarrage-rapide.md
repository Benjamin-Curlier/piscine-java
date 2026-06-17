---
id: demarrage-rapide
sidebar_position: 1.5
title: "Démarrage rapide (5 min)"
slug: /demarrage-rapide
---

# Démarrage rapide ⏱️

Avant d'écrire la moindre ligne de Java, voici **toute la boucle** de la Piscine en 5 minutes.
Bonne nouvelle : tu n'as **rien à connaître de Git** pour commencer. Une seule commande, **`submit`**,
s'occupe de tout.

> Tu n'as pas encore lancé l'application ? → **[Comment lancer la Piscine](lancer-la-piscine.md)** (1 minute).

## La boucle, en 5 gestes

1. 📖 **Lis** le chapitre de cours.
2. ✏️ **Édite** l'exercice — le fichier dans `exercises/<exo>/starter/…` avec ton éditeur habituel.
3. 🚀 **Rends** : tape **`submit <sous-groupe>`** dans la console (ex. `submit 1.1`).
4. 💬 **Lis le rapport** : pour chaque exo, ✓ ou ✗ — et quand ça échoue, **ce qui était attendu vs. ce que ton programme a produit**.
5. 🔁 **Corrige** et re-`submit`. Quand tout est vert ✅, passe au sous-groupe suivant.

## Ton premier rendu

Tu viens d'éditer `exercises/1.1.1-hello-world/…`. Pour le rendre, **une seule commande** :

```text
piscine[main]> submit 1.1
Rendu du sous-groupe 1.1 envoyé.
[console] ▶ Exo 1.1.1  ✓ OK
[console] ▶ Exo 1.1.2  ✗ ÉCHEC
    ✗ affiche la fiche au format attendu
       attendu : "=== Fiche membre ===\nNom    : Martin"
       obtenu  : ""
[console] Corrige les exos en ✗ ci-dessus puis re-pousse (la progression se débloque dans l'ordre).
```

`submit 1.1` fait **tout** pour toi :

- il **enregistre** ton travail et le **rend** — sans que tu tapes une seule commande Git ;
- il lance la **moulinette** sur **tous** les exercices du sous-groupe `1.1` et t'affiche le résultat de chacun.

:::tip Pas besoin de Git pour démarrer
Git est l'outil que les pros utilisent pour partager du code. Tu l'**apprendras au module 6** —
ici, `submit` le fait à ta place. Curieux·se de ce qu'il fait sous le capot ?
Voir **[Comment lancer la Piscine](lancer-la-piscine.md)**.
:::

## Lire un échec (sans paniquer)

Quand un exo échoue, la moulinette ne te jette **pas** une trace Java incompréhensible. Elle te montre,
en clair, **ce qui était attendu** et **ce que ton programme a produit** :

```text
✗ affiche la fiche au format attendu
   attendu : "=== Fiche membre ===\nNom    : Martin"
   obtenu  : ""
```

Compare les deux lignes `attendu` / `obtenu` : **la différence, c'est exactement ce qu'il faut corriger.**
(Le `\n` représente un retour à la ligne.)

## Voir ton code prendre vie 🎮

Tous les exercices ne sont pas que du texte : certains se **lancent** pour de vrai. Le projet **Snake** 🐍,
par exemple — une fois tes deux méthodes écrites, ouvre la fenêtre de jeu et joue au clavier :

```text
java piscine.jeux.SnakeSwing
```

Rien ne motive autant que de voir **ton** code bouger à l'écran. 💪

## Et ensuite ?

- Prêt·e à coder ? → **Module 1 — Fondamentaux**.
- Tu veux comprendre Git (branches, commits, push) ? → **module 6 « Tests et Git »**.
