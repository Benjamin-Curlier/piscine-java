# Subagents — Piscine ETNC

Subagents Claude Code spécifiques au projet.

## À venir

- **`exercise-author`** — Rédige un nouvel exercice au format standard (sujet en français, métadonnées, tests JUnit, solution de référence commentée).
- **`moulinette-builder`** — Implémente un nouveau checker pour la moulinette (analyse statique, exécution sandboxée, génération du rapport pédagogique).
- **`course-reviewer`** — Relit un chapitre de cours pour cohérence pédagogique, niveau de langue, exemples adaptés aux débutants.

## Format

Chaque subagent est un fichier `<nom>.md` avec un frontmatter YAML (`name`, `description`, `tools`, `model`) et un prompt système en corps de fichier.

Voir la [doc officielle](https://docs.anthropic.com/en/docs/claude-code/sub-agents).
