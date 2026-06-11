# Moulinette ETNC

Correcteur pédagogique automatique pour la **Piscine ETNC**.

La moulinette ne se contente pas de noter : pour chaque erreur détectée, elle affiche
un **message clair**, la **cause probable** et une **correction-type commentée**.

---

## Architecture multi-module

| Module | Rôle |
|--------|------|
| `framework` | API des `Checker`, modèles `CheckResult` / `EvaluationReport` / `CheckerContext` |
| `runner` | Exécution sandboxée (`ProcessRunner` → isolation Docker prévue en tâche #30) |
| `reports` | Génération du rapport pédagogique (Markdown + JSON) |
| `cli` | Point d'entrée `Main`, parsing des arguments |

### Dépendances inter-modules

```
framework
   ↑        ↑
runner    reports
   ↑        ↑
      cli
```

---

## Prérequis

| Outil | Version minimale | Notes |
|-------|-----------------|-------|
| Java (Temurin) | **25** LTS | Voir `docs/setup-dev.md` pour l'installation sans droits admin |
| Gradle | — | Fourni par le wrapper versionné `moulinette/gradlew` (rien à installer) |

> **Pas encore de Java 25 ?**
> Consultez la tâche **#12** (`docs/setup-dev.md` à venir) pour l'installation portable
> sur Windows sans droits administrateur.
> Le CI GitHub Actions (tâche **#11**) valide automatiquement ce module sur chaque push.

---

## Lancer les tests

```bash
# Depuis la racine du repo — via Gradle Wrapper (versionné, offline)
moulinette/gradlew -p moulinette build

# Uber-jar de la console
moulinette/gradlew -p moulinette :console:shadowJar

# Un seul module
moulinette/gradlew -p moulinette :framework:test

# Suites lourdes à la demande
moulinette/gradlew -p moulinette :console:testGit     # ou :console:testTools / :console:testE2e
```

---

## Lancer la CLI (développement)

```bash
moulinette/gradlew -p moulinette :cli:run --args="run --exo 1.1.1 --rendu /chemin/vers/rendu"
```

---

## Versions clés

| Bibliothèque | Version |
|---|---|
| Java | 25 LTS |
| JUnit Jupiter | 5.11.4 |
| AssertJ | 3.26.3 |
| SLF4J | 2.0.16 |
| Logback | 1.5.12 |
| Gradle (wrapper) | 9.5.1 |
| Plugin Shadow (uber-jar) | 9.4.2 |

---

## Feuille de route

- **Squelette** (tâche #9) ← *vous êtes ici*
- Maven Wrapper mutualisé (tâche #10)
- CI GitHub Actions (tâche #11)
- Isolation Docker du runner (tâche #30)
