# Référentiel pédagogique — Piscine Java

> Document de référence décrivant la progression complète des cours, exercices et projets de la Piscine Java.
> **Statut** : v0.1 — squelette initial. Les intitulés peuvent évoluer après revue pédagogique.

## 1. Objectifs

Former des **développeurs débutants en programmation** aux **fondamentaux du développement logiciel** en utilisant **Java 25 LTS** comme langage support.

À l'issue de la Piscine, un stagiaire doit être capable de :

- Lire, écrire et déboguer un programme Java structuré.
- Concevoir un petit domaine orienté objet (classes, héritage, polymorphisme, interfaces).
- Manipuler les structures de données usuelles (tableaux, listes, ensembles, dictionnaires) et la programmation fonctionnelle de base (lambdas, streams).
- Gérer les exceptions et les entrées/sorties (fichiers, formats texte simples).
- Écrire des tests automatisés avec JUnit 5 et pratiquer un workflow Git collaboratif.

## 2. Principes pédagogiques

| Principe | Mise en œuvre |
|---|---|
| **Pas de pression temporelle** | Chaque stagiaire avance à son rythme. Pas de "fail rapide" comme en piscine 42. |
| **Moulinette explicative** | Pour chaque erreur détectée : message clair → cause probable → correction-type commentée. |
| **Apprentissage progressif** | Chaque exercice s'appuie uniquement sur des notions vues dans les chapitres antérieurs. |
| **Rendu Git dès le jour 1** | Familiarisation Git par la pratique, pas par un cours théorique préalable. |
| **Ressources externes assumées** | Pas de vidéo interne — liens vers contenus reconnus (Baeldung, Java Almanac, OpenJDK docs, etc.). |
| **Niveau de langue adapté** | Vocabulaire accessible, analogies métier (quand pertinent), zéro jargon non expliqué. |

## 3. Vue d'ensemble

| # | Module | Chapitres | Sous-groupes | Exercices | Projet binôme à l'issue |
|---|---|---|---|---|---|
| 1 | Fondamentaux | 7 | 3 | 10 | — |
| 2 | Tableaux, chaînes, méthodes | 5 | 4 | 12 | — |
| 3 | Programmation Orientée Objet | 10 | 4 | 14 | **Projet 1** |
| 4 | Collections, génériques, lambdas | 8 | 3 | 12 | — |
| 5 | Exceptions et I/O | 7 | 3 | 10 | **Projet 2** |
| 6 | Tests et Git | 8 | 2 | 7 | **Projet 3** (final) |
| **Total** | | **45** | **19** | **65** | **3** |

## 4. Modules détaillés

### Module 1 — Fondamentaux

**Objectif** : installer Java 25, écrire, compiler et exécuter ses premiers programmes ; maîtriser les types primitifs, les opérateurs, les conditions et les boucles.

#### Chapitres
1. **Installer Java 25 et son premier programme** — JDK Temurin, `javac`, `java`, structure d'un `.java`, `main`, `System.out.println`.
2. **Variables et types primitifs** — `int`, `long`, `double`, `boolean`, `char`, `String`, conversions, `var`.
3. **Opérateurs** — arithmétiques, comparaison, logiques, affectation composée, priorité.
4. **Entrées clavier** — `Scanner`, lecture sécurisée, parsing simple.
5. **Conditions** — `if` / `else if` / `else`, `switch` expression (Java 21+), opérateur ternaire.
6. **Boucles** — `for`, `while`, `do-while`, `break`, `continue`, choix de la bonne boucle.
7. **Bonnes pratiques de lisibilité** — indentation, nommage, commentaires utiles.

#### Sous-groupes d'exercices (3 sous-groupes, 10 exercices)
- **1.1 Premiers pas** (3 ex) — Hello world, affichage formaté, lecture/affichage d'une saisie utilisateur.
- **1.2 Variables et opérateurs** (3 ex) — Conversion d'unités, calculs géométriques simples, manipulation booléenne.
- **1.3 Contrôle de flux** (4 ex) — FizzBuzz, suite de Fibonacci itérative, table de multiplication, mini-jeu "devine le nombre".

---

### Module 2 — Tableaux, chaînes, méthodes

**Objectif** : structurer du code en méthodes réutilisables ; manipuler tableaux 1D/2D et chaînes de caractères ; aborder la récursivité.

#### Chapitres
1. **Tableaux 1D** — déclaration, initialisation, parcours, `length`, `Arrays.toString`.
2. **Tableaux 2D** — matrices, parcours imbriqué, applications (grilles, images N&B).
3. **Chaînes de caractères** — `String` immuable, méthodes courantes, `StringBuilder`.
4. **Méthodes** — signature, paramètres, retour, surcharge, portée des variables, `static`.
5. **Récursivité** — cas de base, cas récursif, pile d'exécution, quand l'utiliser.

#### Sous-groupes d'exercices (4 sous-groupes, 12 exercices)
- **2.1 Tableaux** (4 ex) — min/max/moyenne, inversion, recherche linéaire, rotation de matrice.
- **2.2 Chaînes de caractères** (3 ex) — palindrome, comptage d'occurrences, formatage type "ASCII art".
- **2.3 Méthodes** (3 ex) — bibliothèque de méthodes mathématiques, surcharge, refactor d'un exercice du module 1 en méthodes.
- **2.4 Récursivité** (2 ex) — factorielle et puissance, parcours de matrice récursif.

---

### Module 3 — Programmation Orientée Objet

**Objectif** : concevoir et utiliser des classes, comprendre encapsulation, héritage, polymorphisme, interfaces ; découvrir les nouveautés Java modernes (records, sealed).

#### Chapitres
1. **Classes et objets** — distinction classe/instance, attributs, méthodes, `new`.
2. **Encapsulation** — `private` / `public`, getters/setters, invariants.
3. **Constructeurs** — par défaut, paramétrés, surcharge, chaînage avec `this(...)`.
4. **Mots-clés `this` et `static`** — instance vs classe, variables et méthodes statiques.
5. **Héritage** — `extends`, `super`, redéfinition (`@Override`), `final`.
6. **Polymorphisme** — typage statique vs dynamique, downcast, `instanceof` pattern (Java 21+).
7. **Classes abstraites** — `abstract class`, méthodes abstraites vs concrètes.
8. **Interfaces** — contrats, implémentation multiple, méthodes `default` et `static`.
9. **Enums** — types énumérés, méthodes sur enum, `switch` sur enum.
10. **Records et `sealed`** — types immuables (records), hiérarchies fermées (`sealed` / `permits`), pattern matching.

#### Sous-groupes d'exercices (4 sous-groupes, 14 exercices)
- **3.1 Classes et objets** (3 ex) — `Point2D`, `Compte` bancaire simple, `Membre` (attributs + méthodes).
- **3.2 Encapsulation et constructeurs** (3 ex) — invariants d'un `Rectangle`, validation dans le constructeur, classe `Date` simple.
- **3.3 Héritage et polymorphisme** (4 ex) — hiérarchie `Vehicule`, `Forme` géométrique, `Personnel` (encadrant/intermédiaire/exécutant), méthode polymorphe `decrire()`.
- **3.4 Interfaces, enums, records** (4 ex) — interface `Comparable` custom, enum `Grade` avec méthodes, `record Coordonnees`, hiérarchie `sealed` d'événements.

#### → **Projet binôme #1 — Conception d'un mini-domaine OO**
Voir [section 5](#5-projets-binôme).

---

### Module 4 — Collections, génériques, lambdas

**Objectif** : utiliser les structures de données du JDK, comprendre les génériques, manipuler données avec lambdas et streams.

#### Chapitres
1. **`List` et `ArrayList`** — ajouts, suppressions, parcours, `LinkedList` vs `ArrayList`.
2. **`Set` et `HashSet`** — unicité, `equals` / `hashCode`, `TreeSet`.
3. **`Map` et `HashMap`** — clé/valeur, parcours, `getOrDefault`, `computeIfAbsent`.
4. **Itération moderne** — `Iterator`, `forEach`, vues `keySet` / `values` / `entrySet`.
5. **Génériques** — classes et méthodes génériques, bornes (`extends`, `super`), wildcards.
6. **`Comparable` et `Comparator`** — tri naturel vs custom, chaînage de comparators.
7. **Lambdas et interfaces fonctionnelles** — `Function`, `Predicate`, `Consumer`, `Supplier`, capture.
8. **Streams et `Optional`** — pipeline `map`/`filter`/`reduce`/`collect`, terminaux courants, `Optional` correctement utilisé.

#### Sous-groupes d'exercices (3 sous-groupes, 12 exercices)
- **4.1 Collections** (4 ex) — annuaire d'équipe (`Map`), set d'utilisateurs uniques, file de traitement (`Deque`), groupement par niveau.
- **4.2 Génériques et tri** (4 ex) — `Stack<T>` custom, paire générique, tri d'une liste de `Membre` (par niveau puis nom), `Comparator` chaîné.
- **4.3 Lambdas et Streams** (4 ex) — filtrage/projection d'une liste, agrégation (somme, moyenne, max), `groupingBy` / `partitioningBy`, recherche avec `Optional`.

---

### Module 5 — Exceptions et I/O

**Objectif** : gérer correctement les erreurs, lire/écrire des fichiers, manipuler quelques formats texte courants.

#### Chapitres
1. **Hiérarchie des exceptions** — `Throwable`, `Error`, `Exception`, `RuntimeException`, checked vs unchecked.
2. **`try` / `catch` / `finally`** — propagation, multi-catch, `throw` vs `throws`.
3. **`try-with-resources`** — interface `AutoCloseable`, fermeture déterministe.
4. **Exceptions personnalisées** — quand et comment, chaînage (`cause`).
5. **I/O classique** — `File`, `FileReader` / `FileWriter`, `BufferedReader` / `PrintWriter`.
6. **NIO.2** — `Path`, `Files`, lecture/écriture moderne (`readAllLines`, `lines`, `writeString`).
7. **Formats texte** — CSV simple, propriétés (`.properties`), introduction à JSON via une lib (sans détailler).

#### Sous-groupes d'exercices (3 sous-groupes, 10 exercices)
- **5.1 Exceptions** (3 ex) — gestion défensive d'une saisie, exception custom `EffectifInvalideException`, refactor d'un exercice du module 2 avec exceptions.
- **5.2 Lecture/écriture de fichiers** (4 ex) — compteur de lignes, copie de fichier, journal d'événements append-only, lecture d'un fichier de config `.properties`.
- **5.3 Parsing et formats** (3 ex) — import CSV de personnel, export CSV trié, mini-parseur de commandes texte.

#### → **Projet binôme #2 — Application avec persistance fichier**
Voir [section 5](#5-projets-binôme).

---

### Module 6 — Tests et Git

**Objectif** : adopter une démarche de test automatisé et un workflow Git collaboratif.

#### Chapitres
1. **Pourquoi tester** — types de tests, ROI, peur du changement.
2. **JUnit 5 — les bases** — `@Test`, `Assertions.*`, structure d'un projet de test.
3. **JUnit 5 — avancé** — `@BeforeEach`, `@AfterEach`, `@ParameterizedTest`, `@Nested`.
4. **AssertJ** — assertions fluides et lisibles, comparaisons d'objets.
5. **TDD en pratique** — cycle Red / Green / Refactor sur un exemple guidé.
6. **Git — fondamentaux** — `init`, `add`, `commit`, `log`, `diff`, `status`, `.gitignore`.
7. **Git — branches et collaboration** — `branch`, `checkout` / `switch`, `merge`, `rebase`, résolution de conflits.
8. **Git — workflow PR** — fork ou branche, push, pull request, revue, merge.

#### Sous-groupes d'exercices (2 sous-groupes, 7 exercices)
- **6.1 Tests avec JUnit 5** (4 ex) — écrire des tests sur une classe existante, paramétrer des tests, TDD d'une calculatrice, refactor d'un exercice du module 3 sous tests.
- **6.2 Git en pratique** (3 ex) — workflow `commit` / `push` / `pull` propre, résolution d'un conflit guidé, ouverture et revue d'une PR fictive.

#### → **Projet binôme #3 — Application complète testée**
Voir [section 5](#5-projets-binôme).

---

## 5. Projets binôme

Trois projets en binôme jalonnent la Piscine. Chaque projet est volontairement modeste en périmètre mais exigeant sur la qualité (lisibilité, tests, Git). Les binômes sont **imposés par les formateurs** (rotation pour faire travailler chacun avec des profils différents).

| # | Quand | Thème pédagogique | Exemple de sujet |
|---|---|---|---|
| **Projet 1** | Après module 3 | Conception orientée objet d'un mini-domaine | **Gestion d'un bureau** — membres, niveaux, équipes, opérations simples (affecter, promouvoir, lister). |
| **Projet 2** | Après module 5 | Application OO + persistance fichier | **Gestionnaire de tâches CLI** — ajouter, lister, marquer fait, persistance CSV (à la main), gestion d'erreurs propre. |
| **Projet 3** | Après module 6 (final) | Application complète : OO + persistance + tests + Git collaboratif | **Mini-moulinette pédagogique** — checker simple qui compile un fichier Java, l'exécute sur des cas, génère un rapport explicatif. Le binôme doit produire ≥ 70 % de couverture JUnit et utiliser des branches + PR. |

Chaque projet a son propre dossier dans [`exercises/`](../exercises/) avec sujet, critères d'évaluation, livrables attendus et grille de notation pédagogique. **Les trois projets sont disponibles** : [`projet-1-mini-domaine/`](../exercises/projets-binome/projet-1-mini-domaine/), [`projet-2-persistance/`](../exercises/projets-binome/projet-2-persistance/) et [`projet-3-mini-moulinette/`](../exercises/projets-binome/projet-3-mini-moulinette/).

## 6. Progression et prérequis

```
Module 1 (Fondamentaux)
   ↓
Module 2 (Tableaux, chaînes, méthodes)
   ↓
Module 3 (POO) ────→ Projet 1
   ↓
Module 4 (Collections, lambdas)
   ↓
Module 5 (Exceptions, I/O) ────→ Projet 2
   ↓
Module 6 (Tests, Git) ────→ Projet 3 (final)
```

Aucun module ne peut être abordé sans avoir validé le précédent. À l'intérieur d'un module, les chapitres sont eux aussi linéaires ; les exercices d'un sous-groupe sont à faire dans l'ordre indiqué.

## 7. Conventions

- **Langue** : tout le contenu pédagogique (cours, sujets, corrections, messages de la moulinette) est rédigé en **français**. Le code Java suit les conventions standard (identifiants en anglais, sauf vocabulaire métier où le français est plus clair pour des débutants).
- **Format d'un exercice** : voir [`docs/format-exercice.md`](format-exercice.md) (à venir).
- **Charte de rédaction** : voir [`docs/charte-redaction.md`](charte-redaction.md) (à venir).
- **Stack technique** : voir [README.md](../README.md).

---

*Document maintenu par l'équipe de formateurs Piscine Java. Toute modification structurelle (ajout/suppression d'un module, refonte d'un sous-groupe) passe par une PR avec revue par au moins un autre formateur.*
