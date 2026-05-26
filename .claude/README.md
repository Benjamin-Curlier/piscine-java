# Configuration Claude Code — Piscine ETNC

Ce dossier contient la configuration partagée de [Claude Code](https://claude.com/claude-code) pour le projet.

## Organisation

| Fichier / Dossier | Rôle | Versionné ? |
|---|---|---|
| `settings.json` | Permissions et configuration partagées par toute l'équipe de formateurs. | ✅ Oui |
| `settings.local.json` | Permissions et préférences personnelles (par utilisateur). | ❌ Non (`.gitignore`) |
| `agents/` | Subagents spécialisés du projet (ex. : `exercise-author`, `moulinette-builder`). | ✅ Oui |
| `commands/` | Slash commands custom (ex. : `/new-exercise`, `/check-moulinette`). | ✅ Oui |
| `skills/` | Skills custom du projet (ex. : format d'exercice standard). | ✅ Oui |

## Permissions

Le `settings.json` partagé adopte une politique conservatrice :

- **allow** : commandes de lecture git/gh sans risque.
- **ask** : commandes qui modifient l'état (commit, push, merge, rebase, création de PR) — Claude doit demander.
- **deny** : commandes destructives (`rm -rf`, `push --force`, `reset --hard`) — bloquées.

Pour autoriser une commande supplémentaire seulement chez vous, ajoutez-la dans `settings.local.json` (non versionné).

## Conventions

- **Langue** : tout le contenu pédagogique et les commentaires en **français**. Le code Java suit les conventions standard (anglais).
- **Public cible** : stagiaires militaires **débutants en programmation**. Adapter le niveau de langue en conséquence.
- **Priorité** : pédagogie > moulinette > outillage. La qualité des explications est le différenciateur du projet.
