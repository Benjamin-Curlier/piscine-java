# Piscine Java

> **Stagiaire ?** Va directement à [`docs/piscine-stagiaire.md`](docs/piscine-stagiaire.md) — tout est expliqué pour démarrer en autonomie.

Plateforme pédagogique d'apprentissage du langage **Java**, pensée pour l'**onboarding de nouveaux développeurs** qui montent en compétence à leur rythme.

Inspirée des piscines C/C++ de 42 et Epitech, cette piscine se distingue par :

- **Pas de contrainte de temps** — chaque stagiaire avance à son rythme.
- **Une moulinette pédagogique** qui n'évalue pas seulement, mais **explique** chaque erreur et propose une **correction-type commentée**.
- **Pas de vidéos intégrées** — les cours s'appuient sur du texte, du code commenté, et renvoient vers des ressources externes (articles, vidéos) sélectionnées.
- **Rendu via Git** — pour familiariser les stagiaires avec l'outil dès le premier exercice.

## Deux modes d'utilisation

La piscine s'utilise de **deux façons**, selon le public :

| Mode | Pour qui | Comment | Prérequis |
|------|----------|---------|-----------|
| **Dépôt** (connecté) | Formateurs, contributeurs, stagiaires « connectés » | On **clone le dépôt**, on installe Java 25 + git, et `piscine-bootstrap` compile la console et crée le workspace de rendu. | Java 25, git, accès au dépôt. Voir [`docs/setup-dev.md`](docs/setup-dev.md). |
| **Installeur** (hors-ligne) | Stagiaire autonome | On reçoit un **installeur** (`.exe` Windows per-user, `.deb`/portable Linux) : JRE, git, moulinette, exercices et site de cours sont **embarqués**. Double-clic → la GUI s'ouvre dans le navigateur. **Aucun droit admin, aucun réseau.** | Aucun (tout est dans l'installeur). |

> Les installeurs sont produits par le workflow **Release** (jpackage) — voir [`docs/deploiement-instructeur.md`](docs/deploiement-instructeur.md).

Dans les deux modes, la boucle pédagogique est identique : on édite un exercice, on rend via Git, et la **moulinette** produit un rapport explicatif. Détails côté stagiaire : [`docs/piscine-stagiaire.md`](docs/piscine-stagiaire.md).

## Organisation du dépôt

| Dossier | Contenu |
|---------|---------|
| `docs/` | Documentation du projet : référentiel pédagogique, format d'exercice, charte de rédaction, procédures de déploiement. |
| `courses/` | Site Docusaurus regroupant les 6 modules de cours (+ un module bonus « Pour aller plus loin »), organisés en chapitres progressifs. |
| `exercises/` | 65 exercices individuels + 3 projets en binôme + 2 jeux capstone (Snake, Bomberman), au format standardisé (sujet, métadonnées, tests, solution de référence). |
| `moulinette/` | Code Java de la moulinette de correction automatique (multi-module Gradle). |

## Modules

1. **Fondamentaux** — Variables, types, opérateurs, conditions, boucles.
2. **Tableaux, chaînes, méthodes** — Manipulation de données, structuration du code.
3. **Programmation Orientée Objet** — Classes, héritage, polymorphisme, interfaces.
4. **Collections, génériques, lambdas** — List/Set/Map, génériques, streams.
5. **Exceptions et I/O** — Gestion d'erreurs, lecture/écriture de fichiers.
6. **Tests et Git** — JUnit, TDD, workflow Git collaboratif.
7. **Pour aller plus loin** *(bonus)* — premiers pas en concurrence et en persistance.

> En guise de capstone, deux **jeux jouables** (Snake, Bomberman) où le stagiaire écrit la logique
> par-dessus une interface graphique fournie — voir `exercises/projets-jeux/`.

## Périmètre & la suite

La Piscine est une **piscine de fondamentaux du langage Java** — dans l'esprit des piscines C/C++
de 42. Son but : vous rendre **à l'aise pour écrire du Java**, du premier programme jusqu'à la POO,
les collections, les exceptions/I/O, et les tests. Le périmètre couvre aussi le **`pom.xml`/Maven**
(chapitre 1-9), `java.time` (5-8), les **enums** (3-9) et **`Optional`** (4-8).

Ce qu'elle **ne couvre pas** (volontairement — ce n'est pas un bootcamp « job-ready ») :

- **Frameworks** : Spring / Spring Boot ;
- **Bases de données** : SQL, JDBC, JPA/Hibernate ;
- **Web & API** : HTTP, REST ;
- **Concurrence** approfondie et **build avancé**.

### La suite, pour devenir développeur·se back-end Java

Une fois la Piscine terminée, la trajectoire habituelle vers un poste est :

1. **Spring Boot** — le framework de référence pour les applications Java côté serveur ;
2. une **base de données** relationnelle (SQL) + **JDBC** puis **JPA/Hibernate** ;
3. construire une **API REST** (endpoints, JSON, tests d'intégration) ;
4. notions de **concurrence** et d'**observabilité** (logs, métriques).

> Ces sujets pourront faire l'objet d'une **« Piscine Java avancée »** ; la présente Piscine en est
> le socle indispensable.

## Architecture technique

**En place :**
- **Java 25 LTS** (OpenJDK Temurin) — moulinette **multi-module Gradle** (les exercices restent des projets Maven côté stagiaire).
- **JUnit 5** + **AssertJ** pour les tests ; notation hors-ligne via **`javac` + JUnit ConsoleLauncher** (zéro outil de build/réseau à l'exécution).
- **Checkstyle** pour le style (en mode *advisory* / non bloquant pour la beta).
- **Docusaurus** pour le site de cours (servi en local par l'application installée).
- **Git** pour le rendu (MinGit embarqué dans l'installeur) ; dépôt de dev sur **GitHub**.

**Cible / à venir** (voir [`docs/backlog.md`](docs/backlog.md)) :
- **Docker** pour l'isolation du runner (#30).
- **PMD / SpotBugs** en complément de Checkstyle, **JaCoCo** côté moulinette.
- **Anti-triche** (JPlag, #29).

> Depuis la **0.7.0**, la piscine est **100 % locale et auto-rythmée** : pas de forge distante
> ni de notation externe — le stagiaire avance à son rythme, la moulinette locale est l'arbitre.

> JaCoCo est aujourd'hui utilisé **côté projet binôme #3** (couverture ≥ 70 %), pas dans la moulinette elle-même.

## Démarrage rapide

Voir **[`docs/setup-dev.md`](docs/setup-dev.md)** pour installer Java 25 (sans droits admin) et lancer les tests localement.

```bash
# Unix — rendre le wrapper exécutable (une seule fois)
chmod +x moulinette/gradlew

# Vérifier la version de Gradle (wrapper versionné, fonctionne offline)
moulinette/gradlew -p moulinette -v
```

## Contribuer

Formateur ? Voir **[`CONTRIBUTING.md`](CONTRIBUTING.md)** : workflow Git, checklists d'ajout d'exercice / de chapitre, critères de revue.

## Statut

**Version 0.7.0** — contenu pédagogique complet (6 modules + bonus : 65 exercices + 3 projets binôme + 2 jeux capstone), moulinette évaluante, installeur hors-ligne. Voir [`CHANGELOG.md`](CHANGELOG.md) pour le détail des versions, [`docs/backlog.md`](docs/backlog.md) pour la suite (Phase 4 — déploiement à grande échelle), et [`docs/referentiel.md`](docs/referentiel.md) pour le plan pédagogique complet.

## Licence

Sous licence **MIT** — voir [`LICENSE`](LICENSE).

---

*Projet à vocation pédagogique pour l'apprentissage du langage Java.*
