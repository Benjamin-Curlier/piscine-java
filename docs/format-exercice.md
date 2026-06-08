# Format d'un exercice Piscine ETNC

> Standard que **tout exercice individuel** de la Piscine doit respecter.
> Garantit la cohérence pédagogique, l'automatisation par la moulinette, et la lisibilité pour les stagiaires.

## 1. Localisation et nommage

Tous les exercices vivent sous `exercises/`.

```
exercises/
├── module-1-fondamentaux/
│   ├── 1.1.1-hello-world/
│   ├── 1.1.2-affichage-formate/
│   └── ...
├── module-2-tableaux-chaines-methodes/
│   └── 2.1.1-min-max-moyenne/
├── ...
└── projets-binome/
    ├── projet-1-mini-domaine/
    ├── projet-2-persistance/
    └── projet-3-moulinette/
```

**Convention de nommage** : `M.S.E-slug/` où :
- `M` = numéro du module (1 à 6)
- `S` = numéro du sous-groupe dans le module (voir [`referentiel.md`](referentiel.md))
- `E` = position dans le sous-groupe (1, 2, 3, …)
- `slug` = nom en `kebab-case`, court, en français sans accent (ex. `hello-world`, `min-max-moyenne`, `palindrome`)

## 2. Structure d'un exercice

Chaque dossier d'exercice contient **exactement** :

```
1.1.1-hello-world/
├── sujet.md              # Énoncé pédagogique destiné au stagiaire
├── metadata.yml          # Métadonnées structurées (pour la moulinette et le site)
├── starter/              # Code de départ remis au stagiaire (à compléter)
│   ├── pom.xml
│   └── src/main/java/etnc/m1/HelloWorld.java
├── tests/                # Tests JUnit 5 PUBLICS (le stagiaire les voit)
│   └── src/test/java/etnc/m1/HelloWorldTest.java
│   └── src/test/java/etnc/util/...       # éventuels utilitaires de test partagés
├── tests-prives/         # Tests JUnit 5 PRIVÉS (exécutés par la moulinette uniquement)
│   └── src/test/java/etnc/m1/HelloWorldPriveTest.java
├── solution/             # Solution de référence commentée pédagogiquement
│   ├── pom.xml                            # identique au starter, + injecte tests-prives
│   └── src/main/java/etnc/m1/HelloWorld.java
├── correction.md         # Explication pas-à-pas de la solution (publié APRÈS rendu)
└── evaluation.yml        # Rubrique de notation pondérée
```

**Aucun de ces fichiers n'est facultatif** sauf `tests-prives/` qui peut être absent pour les exercices les plus simples du module 1.

## 3. `sujet.md` — l'énoncé pédagogique

Public cible : **stagiaire débutant** en programmation. Le sujet doit être autonome : pas de référence à un cours non encore vu, pas de pré-requis implicite.

### Structure imposée

```markdown
# Exercice 1.1.1 — Hello World

## Contexte
[1–3 phrases qui ancrent l'exercice dans une situation concrète,
si possible avec une touche militaire — bureau, transmission, ordre, etc.]

## Énoncé
[Description claire de ce qui est demandé, à la 2ᵉ personne du pluriel ("vous").]

## Exemple
**Exécution attendue :**
```
$ java HelloWorld
Hello, world!
```

## Contraintes
- [Liste à puces des contraintes techniques (pas d'import X, méthode Y obligatoire, etc.)]
- Si aucune contrainte particulière : écrire "Aucune contrainte particulière."

## Ce qui sera vérifié
[Liste haut-niveau de ce que la moulinette vérifie, SANS révéler les tests cachés.
But : que le stagiaire sache à quoi s'attendre.]

## Pour aller plus loin (optionnel)
[Pistes de bonus, variantes, lectures externes. Pas noté.]
```

### Règles de rédaction
Voir [`charte-redaction.md`](charte-redaction.md). En résumé :
- Phrases courtes, vocabulaire simple, jargon expliqué la première fois.
- Vouvoiement.
- Pas d'humour, pas d'argot, pas d'allusions culturelles.

## 4. `metadata.yml` — métadonnées structurées

Format YAML strict, consommé par la moulinette et par le site Docusaurus.

```yaml
slug: hello-world
titre: "Hello World"
module: 1
sous_groupe: "1.1"
position: 1
difficulte: tres-facile      # tres-facile | facile | moyen | difficile | tres-difficile
duree_estimee_min: 10        # estimation en minutes pour un stagiaire moyen
prerequis: []                # liste de slugs d'exercices à avoir validés avant
objectifs_pedagogiques:
  - "Compiler un programme Java avec javac"
  - "Exécuter un programme Java avec java"
  - "Utiliser System.out.println"
notions:                     # tags libres réutilisables (servent au filtrage Docusaurus)
  - syntaxe-de-base
  - compilation
  - sortie-standard
auteur: "ETNC"
version: 1
date_creation: 2026-05-26
```

**Champs obligatoires** : tous sauf `prerequis` (peut être vide) et `notions` (peut être vide).

## 5. `starter/` — code de départ

- Projet **Maven** mono-module minimal (`pom.xml` à la racine de `starter/`).
- Java **25** (`<maven.compiler.release>25</maven.compiler.release>`).
- Dépendances test : JUnit Jupiter et AssertJ.
- Le `pom.xml` utilise **`build-helper-maven-plugin`** pour ajouter `../tests/src/test/java` comme source de tests, de sorte qu'un `./mvnw test` (ou `mvn test` si Maven est installé globalement) depuis `starter/` exécute les tests publics.
- Package : `etnc.mN` où N est le numéro de module (ex. `etnc.m1`, `etnc.m3`).
- Le code de départ contient **strictement** ce qui est nécessaire pour démarrer : signatures de méthodes vides, commentaires `// TODO`, imports nécessaires.
- **Jamais** de réponse partielle qui orienterait vers une seule solution possible.

**Exemple** :

```java
// starter/src/main/java/etnc/m1/HelloWorld.java
package etnc.m1;

public class HelloWorld {
    public static void main(String[] args) {
        // TODO : afficher "Hello, world!" suivi d'un retour à la ligne
    }
}
```

## 6. `tests/` — tests publics

Tests **JUnit 5 + AssertJ** que le stagiaire peut lancer en local pour vérifier sa progression. Ils couvrent les cas évidents et nominaux.

- Package miroir du code (`etnc.m1` → `etnc.m1`).
- Nommage : `<Classe>Test.java`.
- Une assertion claire par test, message d'échec explicite en **français** (via `.as(...)`).
- Les **utilitaires de test partagés** (ex. : capture de `System.out`) vivent sous `etnc.util` dans le même dossier `tests/src/test/java/`. Ils sont accessibles tant aux tests publics qu'aux tests privés.

```java
// tests/src/test/java/etnc/m1/HelloWorldTest.java
package etnc.m1;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class HelloWorldTest {

    @Test
    void affiche_hello_world_avec_retour_ligne() {
        var sortie = capturerSortie(() -> HelloWorld.main(new String[]{}));
        assertThat(sortie)
            .as("La sortie doit etre exactement 'Hello, world!' suivi d'un retour ligne")
            .isEqualTo("Hello, world!" + System.lineSeparator());
    }

    // ...utilitaire capturerSortie() fourni
}
```

## 7. `tests-prives/` — tests cachés

Mêmes conventions que `tests/`, mais :
- Le stagiaire ne les voit pas avant le rendu.
- Ils couvrent les cas limites, l'entrée pathologique, les invariants implicites.
- Ils servent à départager les solutions superficielles des solutions robustes.

## 8. `solution/` — solution de référence

Une implémentation **idiomatique, lisible et commentée pédagogiquement**.

- **Pas** la solution la plus astucieuse ou la plus courte : la plus **claire**.
- Commentaires en français qui expliquent **pourquoi**, pas **quoi** (le code dit déjà le quoi).
- Respecte Checkstyle/PMD/SpotBugs (zéro warning).
- `solution/pom.xml` est **identique** au `starter/pom.xml`, à une différence près : `build-helper` ajoute **à la fois** `../tests/src/test/java` **et** `../tests-prives/src/test/java`. Ainsi `./mvnw -f solution/pom.xml test` (ou `cd solution && mvn test`) valide que la solution de référence passe **toutes** les épreuves (publiques et privées) — gage de cohérence pour les formateurs.

```java
// solution/src/main/java/etnc/m1/HelloWorld.java
package etnc.m1;

/**
 * Programme minimal d'affichage. Sert d'introduction a la structure
 * d'une classe Java executable.
 */
public class HelloWorld {

    public static void main(String[] args) {
        // System.out est le flux de sortie standard.
        // println ajoute un retour a la ligne adapte au systeme.
        System.out.println("Hello, world!");
    }
}
```

## 9. `correction.md` — explication de la solution

Document **publié après le rendu** (la moulinette le débloque). Format :

```markdown
# Correction — Exercice 1.1.1 Hello World

## Démarche attendue
[Explication du raisonnement étape par étape.]

## Points clés
- [Point 1 : ce qu'il fallait absolument comprendre]
- [Point 2 : piège fréquent et comment l'éviter]

## Erreurs fréquentes observées
[Liste des erreurs typiques avec, pour chacune, l'explication et la correction.]

## Variantes possibles
[Autres solutions valides, avec leurs trade-offs pédagogiques.]

## Pour approfondir
- [Lien externe 1 vers une ressource (article, doc OpenJDK, etc.)]
```

## 10. `evaluation.yml` — rubrique de notation

```yaml
total: 20
seuil_reussite: 12          # note minimale pour valider l'exercice
criteres:
  - id: tests-publics
    description: "Tests publics passants"
    poids: 8
    type: automatique        # automatique | formateur
  - id: tests-prives
    description: "Tests privés passants"
    poids: 8
    type: automatique
  - id: style
    description: "Style et lisibilité (Checkstyle + PMD)"
    poids: 2
    type: automatique
  - id: demarche
    description: "Pertinence de la démarche (lisibilité, idiomatisme)"
    poids: 2
    type: formateur
```

**Règles** :
- `poids` cumulés = `total`.
- Tout critère `formateur` doit être justifiable par une grille : voir [`grille-evaluation.md`](grille-evaluation.md). Son `id` doit correspondre à l'une des grilles (`demarche`, `lisibilite`, `idiomatisme`, `respect-consignes`).
- Le seuil de réussite est typiquement à **60 %** (12/20).

## 11. Pour les projets binôme

Les projets binôme suivent une structure **proche** mais adaptée :

```
projets-binome/projet-1-mini-domaine/
├── sujet.md
├── metadata.yml            # avec champ supplémentaire `binome: true` et `duree_estimee_h`
├── consignes-livraison.md  # branches Git, commits, README attendu
├── exemples-rendus/        # 1 ou 2 rendus exemplaires anonymisés (post-promo)
└── evaluation.yml          # rubrique avec critères collaboration en plus
```

Pas de `starter/`, `solution/`, `tests/` figés : le binôme conçoit le projet de A à Z. La moulinette n'exécute pas — l'évaluation est manuelle, guidée par `evaluation.yml`.

## 11bis. Exercices « écriture de tests » (sous-groupe 6.1)

Certains exercices du module 6 inversent le contrat habituel : **le livrable du stagiaire est la suite de tests**, pas le code. L'implémentation est **fournie correcte** ; le stagiaire écrit les tests qui la valident. La moulinette les grade par **mutation** (voir [`architecture-moulinette.md`](architecture-moulinette.md) → `MutationChecker`) : les tests du stagiaire doivent **passer** sur l'implémentation correcte et **échouer** (« tuer ») sur chaque implémentation **mutée** cachée.

### Marqueur
- `metadata.yml` porte `mode: ecriture-tests` (intention lisible, vérifiable par le lint).
- Au runtime, la moulinette reconnaît ces exos à la **présence du dossier `mutants/`** ; le `MutationChecker` s'active et les checkers normaux (`compile`, `tests-publics`, `tests-prives`) se désactivent.

### Arborescence
```
6.1.1-<slug>/
├── metadata.yml            # + mode: ecriture-tests
├── sujet.md                # « écris la suite de tests de la classe fournie »
├── correction.md
├── evaluation.yml          # tests-valides + mutants-tues + style + formateur
├── starter/
│   └── src/
│       ├── main/java/etnc/m6/Xxx.java       # FOURNI correct — le stagiaire le TESTE, ne le modifie pas
│       └── test/java/etnc/m6/XxxTest.java   # squelette à compléter (≥ 1 @Test à écrire)
├── solution/
│   ├── src/main/java/etnc/m6/Xxx.java       # impl correcte de référence (identique au starter/main)
│   └── src/test/java/etnc/m6/XxxTest.java   # suite de tests MODÈLE (valide l'exo en CI)
└── mutants/
    ├── <id-1>/etnc/m6/Xxx.java              # variante buggée (même FQCN), un dossier par mutant
    └── <id-2>/etnc/m6/Xxx.java              # 3 à 5 mutants, un par règle / cas limite
```

### Règles
- Chaque mutant est une **altération minime et réaliste** d'une règle de la classe (`+`→`-`, `<`→`<=`, retour constant, oubli d'un cas). Nommer le dossier par le défaut injecté (`operateur-inverse`, `borne-inferieure`…) — le nom apparaît dans le rapport quand le mutant survit.
- La suite de tests **modèle** (`solution/.../test`) doit passer sur le correct **et** tuer **tous** les mutants : c'est le filet `valider-solutions` (un mutant non tué par le modèle = exo mal calibré).
- Verdict de la moulinette : **OK** seulement si les tests du stagiaire passent sur le correct **et** tuent tous les mutants ; sinon **FAIL** avec le détail « N/M mutants détectés » et les survivants nommés (le score proportionnel vit dans le message — le modèle de note est binaire par checker).

## 12. Validation d'un nouvel exercice

Avant merge dans `main`, un nouvel exercice doit :

1. Respecter cette structure (vérifié par un script `tools/lint-exercice.sh` à venir).
2. Avoir tests publics et privés **passants** sur la solution de référence.
3. Avoir été **relu par un autre formateur** (PR + revue).
4. Être référencé dans [`docs/referentiel.md`](referentiel.md).

---

*Version 1 — 2026-05-26. Toute évolution de ce format passe par une PR avec impact sur les exercices existants évalué (script de migration si nécessaire).*
