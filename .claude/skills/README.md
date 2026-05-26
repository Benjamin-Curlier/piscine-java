# Skills — Piscine ETNC

Skills custom du projet (capacités spécialisées invocables par Claude Code).

## À venir

- **`exercise-format`** — Standard du format d'un exercice Piscine ETNC : sujet pédagogique, métadonnées (module, difficulté, prérequis, durée estimée), tests JUnit 5, solution de référence commentée, rubrique d'évaluation.
- **`course-chapter-format`** — Standard d'un chapitre Docusaurus : structure (introduction, théorie, exemples progressifs, exercice guidé, exercices d'entraînement, ressources externes), conventions de rédaction, niveau de langue.
- **`moulinette-checker-format`** — Standard d'un checker de la moulinette : interface, génération du rapport pédagogique (erreur → explication → correction-type), packaging Maven.

## Format

Chaque skill est un dossier `<nom>/` contenant un `SKILL.md` avec frontmatter (`name`, `description`) et le contenu du skill.

Voir la [doc officielle](https://docs.anthropic.com/en/docs/claude-code/skills).
