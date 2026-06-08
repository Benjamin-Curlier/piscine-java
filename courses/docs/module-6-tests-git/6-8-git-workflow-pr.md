---
id: 6-8-git-workflow-pr
sidebar_position: 8
title: "Git — le workflow Pull Request"
description: "Pousser vers un dépôt distant, proposer une Pull Request, mener une revue de code et boucler le cycle collaboratif."
---

# Git — le workflow Pull Request

## Pourquoi ce chapitre

Vous savez versionner et fusionner en local. Reste la dernière pièce : **collaborer à distance**. Quand plusieurs personnes travaillent sur un même projet, on ne fusionne pas directement dans `main` — on **propose** ses changements via une **Pull Request** (en français « demande de fusion »), qui ouvre une discussion et une **revue** avant intégration. C'est exactement le workflow que vous suivrez sur le projet final en binôme, et celui que les formateurs utilisent pour construire cette Piscine. Ce chapitre clôt le parcours.

## Ce que vous saurez faire à la fin

- **Pousser** une branche vers un dépôt distant avec `git push`.
- **Expliquer** ce qu'est une Pull Request et à quoi elle sert.
- **Distinguer** le travail par branche du travail par fork.
- **Mener** une revue de code utile, en donnant et en recevant des retours.
- **Boucler** le cycle : corriger, re-pousser, fusionner.

## 1. Pousser vers un dépôt distant

Jusqu'ici, tout vivait sur votre machine. Un **dépôt distant** (sur une forge comme GitHub ou GitLab) est une copie partagée, accessible à toute l'équipe. On y envoie ses commits avec `git push`. La première fois qu'on pousse une branche, on la relie à son équivalent distant avec `-u` (pour *upstream*) :

```bash
git switch -c feature/ajoute-licence
git add LICENSE
git commit -m "Ajoute le fichier LICENSE"
git push -u origin feature/ajoute-licence
```

`origin` est le nom par défaut du dépôt distant. Après ce premier `push -u`, les fois suivantes, un simple `git push` suffit pour cette branche.

### À retenir

> - Un dépôt distant (`origin`) est la copie partagée du projet sur une forge.
> - `git push -u origin <branche>` envoie la branche et la relie au distant (la première fois).
> - Ensuite, `git push` seul suffit pour cette branche.

## 2. La Pull Request

Une **Pull Request** (PR) est une demande : « voici ma branche, je propose de la fusionner dans `main` ». Elle s'ouvre sur la forge, à partir de votre branche poussée. Une PR contient un **titre**, une **description** (ce que vous changez et pourquoi), et affiche automatiquement le **diff** (l'ensemble des modifications). Elle ouvre un espace de discussion où l'équipe peut commenter, demander des ajustements, puis approuver.

L'intérêt : rien n'entre dans `main` sans avoir été **vu par quelqu'un d'autre**. C'est un filet de qualité, au même titre que les tests — d'ailleurs, sur les projets bien outillés, la moulinette (ou un CI) lance les tests automatiquement sur chaque PR.

### Branche ou fork ?

Deux façons de proposer une PR :
- **Par branche** : vous avez les droits sur le dépôt, vous poussez une branche dessus et ouvrez la PR. C'est le cas dans une équipe (votre binôme, par exemple).
- **Par fork** : vous n'avez pas les droits ; vous créez d'abord une copie personnelle du dépôt (un *fork*), travaillez dessus, et proposez la PR depuis votre fork. C'est le cas pour contribuer à un projet open source.

### À retenir

> - Une Pull Request propose de fusionner une branche et ouvre la **discussion + revue** avant intégration.
> - Rien n'entre dans `main` sans relecture par un tiers — un filet de qualité.
> - **Branche** quand on a les droits (équipe) ; **fork** quand on contribue de l'extérieur.

## 3. La revue de code

Reviewer, c'est lire le diff d'un collègue et donner un retour **utile et bienveillant**. Quelques principes :

- **Comprendre avant de commenter** : lisez tout le changement, pas seulement la première ligne.
- **Commenter le code, pas la personne** : « cette méthode pourrait gérer le cas vide » plutôt que « tu as oublié ».
- **Distinguer le bloquant du suggéré** : une vraie erreur (« ce test ne couvre pas le zéro ») n'a pas le même poids qu'une préférence de style.
- **Reconnaître ce qui est bien** : une revue n'est pas qu'une liste de reproches.

Côté auteur : recevez les retours comme une aide, pas une attaque. Répondez, corrigez, ou expliquez votre choix. L'objectif commun est un meilleur code.

### À retenir

> - Une bonne revue est précise, bienveillante, et distingue le bloquant du suggéré.
> - On commente le **code**, jamais la personne.
> - Recevoir une revue, c'est recevoir de l'aide : on corrige ou on explique, sans le prendre mal.

## 4. Boucler le cycle

Le cycle collaboratif complet :

1. Créer une branche, travailler, commiter.
2. Pousser la branche (`git push`).
3. Ouvrir une Pull Request (titre + description).
4. Recevoir la revue ; corriger en commitant de nouveau, **re-pousser** (la PR se met à jour toute seule).
5. Une fois la PR approuvée (et les tests verts), **fusionner** dans `main`.
6. Supprimer la branche, recommencer pour la fonctionnalité suivante.

### Exemple

```bash
# Après une demande de correction en revue :
git switch feature/ajoute-licence
# ... on corrige le code ...
git commit -am "Précise l'en-tête de la licence (retour de revue)"
git push                 # la PR existante se met à jour automatiquement
# Puis, sur la forge : la revue approuve → on fusionne → on supprime la branche.
```

### À retenir

> - Le cycle : brancher → pousser → ouvrir la PR → reviser → re-pousser → fusionner.
> - Re-pousser sur la branche **met à jour la PR** automatiquement.
> - On ne fusionne qu'une PR **approuvée** (et, idéalement, aux tests verts).

## Erreurs fréquentes

- **Une Pull Request énorme** : accumuler des dizaines de fichiers et de changements avant d'ouvrir la PR. Cause : on attend d'avoir « tout fini ». Correction : préférez des PR petites et fréquentes — elles se relisent vite et bien. Une PR illisible reçoit une revue superficielle.

- **Pousser sur la branche de quelqu'un d'autre sans accord** : modifier la branche d'un collègue en cours de revue. Cause : on veut « aider ». Correction : travaillez sur votre propre branche ou demandez avant ; on coordonne, on n'écrase pas.

- **Approuver sans avoir lu (« LGTM »)** : valider une PR d'un clic sans regarder le diff. Cause : pression du temps, confiance excessive. Correction : la revue n'a de valeur que si elle est réelle — lisez le changement, sinon le filet de qualité ne sert à rien.

## Exercice guidé

**Objectif** : décrire le cycle complet d'une Pull Request, du push à la fusion, sur votre dépôt-jouet.

Vous n'allez pas forcément ouvrir une vraie PR sur une forge (cela dépend de votre accès réseau), mais **dérouler et formuler** chaque étape du cycle, et préparer la branche jusqu'au push.

**Pas à pas :**

**Étape 1** — Sur un dépôt-jouet, créez une branche `feature/ajoute-auteurs`, ajoutez un fichier `AUTEURS.txt`, commitez.

**Étape 2** — Décrivez (en français) le **titre** et la **description** de la PR que vous ouvririez pour cette branche.

**Étape 3** — Listez, dans l'ordre, les étapes restantes du cycle jusqu'à la fusion.

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

```bash
mkdir pr-demo && cd pr-demo
git init
echo "# Mon projet" > README.md
git add README.md && git commit -m "Premier commit"

git switch -c feature/ajoute-auteurs
echo "Alice, Bob" > AUTEURS.txt
git add AUTEURS.txt
git commit -m "Ajoute la liste des auteurs"
# git push -u origin feature/ajoute-auteurs   # si un dépôt distant est configuré
```

**Titre de PR proposé** : `Ajoute le fichier AUTEURS`

**Description de PR proposée** :
> Ajoute un fichier `AUTEURS.txt` listant les contributeurs du projet.
> - Pourquoi : tracer qui a participé, en vue de la licence.
> - Ce qui change : un seul fichier ajouté, aucun code touché.

**Étapes restantes du cycle :**
1. Pousser la branche (`git push -u origin feature/ajoute-auteurs`).
2. Ouvrir la Pull Request sur la forge (titre + description ci-dessus).
3. Un relecteur lit le diff, commente éventuellement, approuve.
4. Si des corrections sont demandées : commiter, `git push` (la PR se met à jour), re-demander la revue.
5. PR approuvée et tests verts → fusionner dans `main`.
6. Supprimer la branche `feature/ajoute-auteurs`.

Points à noter :
- La description répond toujours à « quoi » **et** « pourquoi », pas seulement « quoi ».
- Tant que la PR n'est pas fusionnée, `main` reste intact.
- Re-pousser ne crée pas une nouvelle PR : elle met à jour l'existante.

</details>

## Vérifiez vos acquis

- À quoi sert `-u` dans `git push -u origin <branche>` ?
- Qu'est-ce qu'une Pull Request, et quel filet de qualité apporte-t-elle ?
- Quelle est la différence entre proposer une PR par branche et par fork ?
- Citez deux principes d'une revue de code utile et bienveillante.
- Après une correction demandée en revue, que faites-vous pour mettre à jour la PR ?

## Pour aller plus loin

- [Pro Git — Contribuer à un projet](https://git-scm.com/book/fr/v2/GitHub-Contribuer-%C3%A0-un-projet) (version française) — le workflow fork / branche / Pull Request en détail.
- [About pull requests](https://docs.github.com/fr/pull-requests/collaborating-with-pull-requests/proposing-changes-to-your-work-with-pull-requests/about-pull-requests) (documentation GitHub) — le fonctionnement des PR, côté forge.

## Félicitations — vous avez terminé la Piscine

Vous savez désormais écrire du code orienté objet, gérer les erreurs et les fichiers, **tester** votre travail et **collaborer avec Git**. La moulinette qui vous accompagnait n'a plus de secret : c'est un harnais de tests et une séquence Git, exactement ce que vous maîtrisez maintenant.

La suite n'est plus un chapitre, mais la mise en pratique : les **projets en binôme**. Le **projet final** réunit tout ce parcours — conception orientée objet, persistance fichier, tests automatisés et Git collaboratif — pour construire une **mini-moulinette pédagogique**. Vous serez de l'autre côté de l'outil : c'est vous qui le construirez.

→ **[Revenir à l'accueil de la Piscine](../intro)**
