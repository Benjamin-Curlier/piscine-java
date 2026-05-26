# Slash Commands — Piscine ETNC

Commandes slash custom du projet (`/<nom>` dans Claude Code).

## À venir

- **`/new-exercise <module> <slug>`** — Échafaude un nouvel exercice (dossier + fichiers standard).
- **`/new-chapter <module> <slug>`** — Échafaude un nouveau chapitre de cours Docusaurus.
- **`/check-moulinette <exercice>`** — Lance la moulinette sur un exercice local et affiche le rapport pédagogique.
- **`/audit-pedago <fichier>`** — Audit pédagogique d'un cours ou d'un exercice (cohérence, niveau, exemples).

## Format

Chaque commande est un fichier `<nom>.md` avec un frontmatter optionnel et le prompt à exécuter en corps.

Voir la [doc officielle](https://docs.anthropic.com/en/docs/claude-code/slash-commands).
