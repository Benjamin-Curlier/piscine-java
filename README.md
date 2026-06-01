# Piscine ETNC

> **Stagiaire ?** Va directement à [`docs/piscine-stagiaire.md`](docs/piscine-stagiaire.md) — tout est expliqué pour démarrer en autonomie.

Plateforme pédagogique d'apprentissage du langage **Java** pour les stagiaires de l'**École des Transmissions, du Numérique et du Cyber** (ETNC).

Inspirée des piscines C/C++ de 42 et Epitech, cette piscine se distingue par :

- **Pas de contrainte de temps** — chaque stagiaire avance à son rythme.
- **Une moulinette pédagogique** qui n'évalue pas seulement, mais **explique** chaque erreur et propose une **correction-type commentée**.
- **Pas de vidéos intégrées** — les cours s'appuient sur du texte, du code commenté, et renvoient vers des ressources externes (articles, vidéos) sélectionnées.
- **Rendu via Git** — pour familiariser les stagiaires avec l'outil dès le premier exercice.

## Organisation du dépôt

| Dossier | Contenu |
|---------|---------|
| `docs/` | Documentation du projet : référentiel pédagogique, format d'exercice, charte de rédaction, procédures de déploiement. |
| `courses/` | Site Docusaurus regroupant les 6 modules de cours, organisés en chapitres progressifs. |
| `exercises/` | 65 exercices individuels + 3 projets en binôme, au format standardisé (sujet, métadonnées, tests, solution de référence). |
| `moulinette/` | Code Java de la moulinette de correction automatique (multi-module Maven). |
| `.claude/` | Configuration Claude Code du projet (skills, hooks, subagents). |

## Modules

1. **Fondamentaux** — Variables, types, opérateurs, conditions, boucles.
2. **Tableaux, chaînes, méthodes** — Manipulation de données, structuration du code.
3. **Programmation Orientée Objet** — Classes, héritage, polymorphisme, interfaces.
4. **Collections, génériques, lambdas** — List/Set/Map, génériques, streams.
5. **Exceptions et I/O** — Gestion d'erreurs, lecture/écriture de fichiers.
6. **Tests et Git** — JUnit, TDD, workflow Git collaboratif.

## Architecture technique

- **Java 25 LTS** (OpenJDK Temurin)
- **Maven** multi-module pour la moulinette
- **JUnit 5** + **AssertJ** + **JaCoCo** pour les tests
- **Checkstyle / PMD / SpotBugs** pour l'analyse statique
- **Docker** pour l'isolation du runner
- **Docusaurus** pour le site de cours
- **GitHub** (dev) → **GitLab CE on-premise** (prod)

## Démarrage rapide

Voir **[`docs/setup-dev.md`](docs/setup-dev.md)** pour installer Java 25 (sans droits admin) et lancer les tests localement.

```bash
# Unix — rendre le wrapper exécutable (une seule fois)
chmod +x mvnw

# Vérifier la version de Maven téléchargée
./mvnw -v
```

## Statut

Projet en cours de construction. Voir [`docs/backlog.md`](docs/backlog.md) pour les tâches en cours et [`docs/referentiel.md`](docs/referentiel.md) pour le plan pédagogique complet.

---

*Projet à vocation pédagogique au profit des forces militaires françaises.*
