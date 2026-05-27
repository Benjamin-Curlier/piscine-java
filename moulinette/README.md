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
| Maven | **3.9+** | Ou utiliser `./mvnw` à la racine du repo (tâche #10) |

> **Pas encore de Java 25 ?**
> Consultez la tâche **#12** (`docs/setup-dev.md` à venir) pour l'installation portable
> sur Windows sans droits administrateur.
> Le CI GitHub Actions (tâche **#11**) valide automatiquement ce module sur chaque push.

---

## Lancer les tests

```bash
# Depuis la racine du repo — via Maven Wrapper (après tâche #10)
./mvnw -f moulinette/pom.xml verify

# Ou avec Maven installé globalement
mvn -f moulinette/pom.xml verify

# Un seul module
mvn -f moulinette/pom.xml -pl framework test
```

---

## Lancer la CLI (développement)

```bash
mvn -f moulinette/pom.xml -pl cli exec:java \
    -Dexec.mainClass="etnc.piscine.moulinette.cli.Main" \
    -Dexec.args="run --exo 1.1.1 --rendu /chemin/vers/rendu"
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
| Maven Compiler Plugin | 3.13.0 |
| Maven Surefire Plugin | 3.5.2 |
| Exec Maven Plugin | 3.4.1 |

---

## Feuille de route

- **Squelette** (tâche #9) ← *vous êtes ici*
- Maven Wrapper mutualisé (tâche #10)
- CI GitHub Actions (tâche #11)
- Isolation Docker du runner (tâche #30)
