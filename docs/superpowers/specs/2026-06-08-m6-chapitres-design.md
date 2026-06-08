# Spec — #22 Chapitres du module 6 (Tests et Git)

> Design validé le 2026-06-08 (brainstorming avec le formateur — 3 décisions structurantes tranchées via AskUserQuestion : grading 6.1 « mutation maison », grading 6.2 « assertions sur l'état du repo », chapitres en **2 PR** alignées aux sous-groupes). Tâche backlog **#22**.
> Cycle isolé : ce cycle ne couvre que les **8 chapitres**. Les 2 sous-groupes d'exercices (6.1 → 6.2) suivront dans leurs propres cycles spec / plan / PR (cadence : chapitres d'abord — les notions bornent ce que les exercices peuvent demander).
> Point de départ : [`docs/superpowers/2026-06-08-m6-kickoff.md`](../2026-06-08-m6-kickoff.md).
> Références cadre : [`charte-redaction.md`](../../charte-redaction.md) (§6 structure, §2/§3 ton/langue, §7 code, §10 relecture), [`referentiel.md`](../../referentiel.md) (§module 6), modèles existants [`module-5-exceptions-io/`](../../../courses/docs/module-5-exceptions-io/), spec sœur [`2026-06-04-m5-chapitres-design.md`](2026-06-04-m5-chapitres-design.md).

## 1. Objectif et périmètre

Produire les **8 chapitres** du module 6 dans `courses/docs/module-6-tests-git/`, conformes à la charte §6, cohérents entre eux et avec le module 5 (qui se termine sur le chapitre 5-7 « Formats texte »).

**Objectif pédagogique** (référentiel §module 6) : adopter une démarche de **test automatisé** (JUnit 5, AssertJ, TDD) et un **workflow Git collaboratif** (branches, conflits, PR).

**Particularité structurante** (kickoff §4) : le module 6 est **le dernier module de contenu** et le plus **méta** — il enseigne JUnit, AssertJ et Git, **les outils mêmes par lesquels la moulinette valide les rendus depuis le module 1**. Le stagiaire a donc *subi* ces outils sans les comprendre ; ce module **lève le rideau** : il explique enfin ce qui se passait « de l'autre côté » à chaque rendu (les tests qui passaient/échouaient, le `git add`/`commit`/`push` de la séquence de rendu). Cette familiarité est un **levier pédagogique** (« vous l'avez déjà fait, voici pourquoi »), pas un tabou.

**Hors périmètre** : tout fichier sous `exercises/` (les 7 exos = cycles ultérieurs), la moulinette, la fiche du **Projet binôme #3** (#25, débloqué *après* le module 6, cycle dédié et **final**). Aucun exercice noté n'est touché. Les **décisions transverses** prises au brainstorm (grading « mutation maison » des tests stagiaire, grading des exos Git par assertions sur l'état d'un repo-bac-à-sable, modèle de domaine local `etnc.m6`, roster des 7 exos, barème) sont consignées en §10 pour que le futur volet exercices en hérite — elles ne produisent aucun livrable dans ce cycle.

## 2. Livrables

8 fichiers Markdown + 1 retouche de config + 1 retouche de liaison :

| Fichier | `id` | `sidebar_position` | Notions (réf. §module 6) |
|---|---|---|---|
| `6-1-pourquoi-tester.md` | `6-1-pourquoi-tester` | 1 | À quoi sert un test automatisé ; types de tests (unitaire / intégration / E2E, survol) ; **ROI** du test (le filet qui permet de changer le code sans peur) ; régression ; **la moulinette est un harnais de tests** (révélation) ; test manuel vs automatisé |
| `6-2-junit5-bases.md` | `6-2-junit5-bases` | 2 | Structure d'un projet de test (`src/test/java`) ; `@Test` ; `Assertions.*` (`assertEquals`, `assertTrue`, `assertThrows`) ; nommage d'un test ; AAA (Arrange-Act-Assert) ; exécuter une classe de test |
| `6-3-junit5-avance.md` | `6-3-junit5-avance` | 3 | `@BeforeEach` / `@AfterEach` (cycle de vie) ; `@ParameterizedTest` + `@ValueSource` / `@CsvSource` ; `@Nested` (organisation) ; `@DisplayName` ; `@BeforeAll`/`@AfterAll` (mention) |
| `6-4-assertj.md` | `6-4-assertj` | 4 | `assertThat(...)` ; assertions fluides lisibles ; `isEqualTo` / `contains` / `containsExactly` / `hasSize` ; `assertThatThrownBy` (rappel M5) ; comparaison d'objets ; **pourquoi AssertJ par-dessus `Assertions.*`** (messages d'erreur lisibles) |
| `6-5-tdd-en-pratique.md` | `6-5-tdd-en-pratique` | 5 | Cycle **Red → Green → Refactor** ; écrire le test d'abord ; « le test échoue pour la bonne raison » ; petits pas ; le test comme spécification exécutable ; quand (ne pas) faire du TDD |
| `6-6-git-fondamentaux.md` | `6-6-git-fondamentaux` | 6 | Pourquoi un VCS ; `init` ; zone de staging ; `add` / `commit` / `status` / `log` / `diff` ; `.gitignore` ; anatomie d'un bon **message de commit** ; **ce que la séquence de rendu faisait pour vous** |
| `6-7-git-branches-collaboration.md` | `6-7-git-branches-collaboration` | 7 | `branch` / `switch` (et `checkout` mentionné) ; pourquoi des branches ; `merge` (fast-forward vs commit de merge) ; `rebase` (survol, prudence) ; **résolution d'un conflit** (lire les marqueurs `<<<<<<< ======= >>>>>>>`, choisir, valider) |
| `6-8-git-workflow-pr.md` | `6-8-git-workflow-pr` | 8 | `push` vers un dépôt distant ; la **Pull Request** (proposer, discuter, reviewer, merger) ; fork vs branche ; la **revue de code** (donner/recevoir un retour) ; boucler le cycle collaboratif ; **clôture du parcours Piscine** |

**Config** : `_category_.json` **existe déjà** dans `courses/docs/module-6-tests-git/` (label « Module 6 — Tests et Git », `position: 7`, `link.type: generated-index`). **Unique retouche** : passer `collapsed: true` → `false` pour l'homogénéité avec les modules 1-5.

**Retouche de liaison** : le bloc « Prochain chapitre » de `5-7-formats-texte.md` (module 5) affiche actuellement un renvoi vers « Module 6 — Tests et Git *(à venir)* » → le faire pointer vers `6-1-pourquoi-tester` (lien relatif inter-modules : `../module-6-tests-git/6-1-pourquoi-tester`). **À vérifier en ouvrant le fichier** (le wording exact peut différer). Le chapitre **`6-8` est le dernier du dernier module** : son bloc final ne pointe pas vers un « prochain chapitre » mais **clôt le parcours** — renvoi vers les **Projets binôme** (`../../intro` ou la page des projets) et un mot de conclusion (cf. §6, ch. 6-8).

## 3. Structure imposée (chaque chapitre)

Ordre strict de la charte §6 (identique aux modules 1 à 5) :

1. Frontmatter : `id`, `sidebar_position`, `title`, `description`.
2. `# Titre`
3. `## Pourquoi ce chapitre` (2–3 phrases).
4. `## Ce que vous saurez faire à la fin` (objectifs en verbes d'action).
5. Sections numérotées `## 1. …`, `## 2. …` : notion + `### Exemple` (code/commandes annoté) + `### À retenir` (encadré `>`).
6. `## Erreurs fréquentes` (puces **symptôme → cause → correction**).
7. `## Exercice guidé` (pas-à-pas, solution repliée dans `<details>`).
8. `## Vérifiez vos acquis` (questions ouvertes).
9. `## Pour aller plus loin` (liens externes annotés).
10. `## Prochain chapitre` (→ lien vers le suivant ; **sauf 6-8**, cf. §2).

**Longueur cible** : 800–2000 mots par chapitre (les modules 1-5 tournaient à ~1 200). Les chapitres `6-3` (JUnit avancé, beaucoup d'annotations) et `6-7` (branches + conflits) sont les plus denses — viser le haut de la fourchette, quitte à reporter un détail vers « Pour aller plus loin ».

## 4. Conventions transverses (décisions validées)

- **Deux types d'« Exemple » selon le chapitre** : ch. 6-1 à 6-5 → **code Java** (classes de test JUnit/AssertJ) ; ch. 6-6 à 6-8 → **commandes shell git** + extraits de sortie. Blocs typés en conséquence (` ```java `, ` ```bash `, ` ```text ` pour les sorties git/console, ` ```diff ` pour les conflits/diffs).
- **Exemples indépendants par chapitre** : chaque chapitre prend l'exemple le plus clair pour SA notion, sans fil conducteur imposé. Domaines des exercices guidés choisis **neutres et distincts des exos notés** (anti-spoil, cf. §5.4). Touche militaire seulement quand elle clarifie (charte §4), jamais forcée.
- **Ton / langue** : vouvoiement, phrases courtes, voix active (charte §2). Règle des trois fois pour le vocabulaire technique (charte §3). Acronymes développés à la première occurrence (TDD = *Test-Driven Development* / développement piloté par les tests ; VCS = *Version Control System* / système de gestion de versions ; PR = *Pull Request* ; CI = *Continuous Integration*).
- **Code Java** (charte §7) : identifiants en anglais, métier en français quand plus clair ; commentaires français expliquant le *pourquoi* ; **imports explicites** (`org.junit.jupiter.api.Test`, `org.assertj.core.api.Assertions.assertThat`, jamais en étoile) ; classes de test nommées `…Test`, méthodes de test au présent décrivant le comportement (`additionneDeuxEntiers`, `leveSurDivisionParZero`).
- **Commandes git** : toujours montrées avec leur **sortie attendue** (bloc ` ```text `) quand elle éclaire ; un seul concept git par section ; jamais de commande destructive sans avertissement (`reset --hard`, `push --force` → « Pour aller plus loin » avec garde-fou). Les exemples git **n'opèrent jamais sur le repo de la Piscine** : ils créent un **dépôt-jouet** (`/tmp/demo`, `mon-projet/`) pour ne pas heurter la séquence de rendu.
- **Lien avec la moulinette (levier méta, dosé)** : 6-1 et 6-6 peuvent expliciter « ce que vous avez déjà vécu » (les tests verts/rouges du rapport ; le `add`/`commit`/`push` de la séquence de rendu). À **mentionner** comme ancrage, **sans** dévoiler l'implémentation interne de la moulinette ni anticiper les exos (anti-spoil).
- **Liens « Pour aller plus loin »** : **dev.java**, **Baeldung**, **Javadoc OpenJDK 25**, **doc officielle JUnit 5** (`junit.org/junit5/docs`), **doc officielle AssertJ** (`assertj.github.io`), **Pro Git** (`git-scm.com/book/fr` — version française disponible). 1 à 3 liens annotés par chapitre.
- **Markdown** (charte §9) : un seul `#`, pas de saut de niveau de titre, blocs de code toujours typés.

## 5. Cohérence pédagogique (garde-fous)

### 5.1 Règle d'antériorité — externe (modules précédents)
Tout l'acquis des modules 1-5 est disponible : variables, contrôle de flux, tableaux/chaînes, méthodes, récursivité, POO complète (classes, héritage, polymorphisme, interfaces, enums, records, `sealed`), collections, génériques, `Comparable`/`Comparator`, lambdas, streams, `Optional`, **exceptions** (lever/propager/rattraper/custom), **I/O** (NIO.2, `@TempDir` côté test). Le module 6 est le **dernier** : il n'y a **aucun module ultérieur** dont il faudrait se garder. En particulier, la **division par zéro de la calculatrice TDD (6.1.3) peut lever une exception** (acquis M5).

**Point méta assumé** : JUnit et Git ne sont pas « nouveaux » au sens strict — le stagiaire les a *utilisés passivement* depuis le module 1 (la moulinette teste, la séquence de rendu commit/push). Le module les rend **explicites et actifs** (le stagiaire écrit lui-même des tests, manipule lui-même des branches). Cohérent et voulu.

### 5.2 Règle d'antériorité — **interne au module 6** (garde-fou central)
L'ordre des chapitres suit l'ordre des notions ; chaque chapitre n'emploie que ce qui précède :

- **6-1 pourquoi tester** pose le *pourquoi* (valeur, ROI, régression) sans encore une seule annotation JUnit — motivation, vocabulaire, lien avec le vécu moulinette.
- **6-2 JUnit bases** introduit la mécanique minimale (`@Test`, `Assertions.*`, AAA) — premier test écrit à la main.
- **6-3 JUnit avancé** enrichit 6-2 (`@BeforeEach`, `@ParameterizedTest`, `@Nested`) — suppose qu'un `@Test` simple est acquis.
- **6-4 AssertJ** remplace les `Assertions.*` (6-2/6-3) par des assertions fluides — **`assertThatThrownBy` rappelé** (déjà croisé en M5, ici resitué dans son outil).
- **6-5 TDD** orchestre tout le bloc test (6-2 à 6-4) en une **démarche** (Red/Green/Refactor) — suppose qu'écrire un test et l'exécuter est acquis.
- **6-6 Git fondamentaux** ouvre le bloc Git : cycle local `add`/`commit` — indépendant du bloc test, placé après car les exos 6.1 (tests) précèdent 6.2 (git).
- **6-7 branches & conflits** suppose le cycle local (6-6) acquis ; ajoute `branch`/`switch`/`merge`/conflits.
- **6-8 workflow PR** suppose branches (6-7) acquises ; ajoute `push` distant, PR, revue — **clôt le parcours**.

### 5.3 Mapping antériorité exos → chapitres (borne les futurs exos)
Les chapitres précèdent les exos qui mobilisent leurs notions. Un exo n'utilise jamais une notion vue **plus tard** :

| Sous-groupe d'exos | Chapitres requis (préalables) |
|---|---|
| 6.1 Tests avec JUnit 5 | ch. 1-5 (pourquoi, JUnit bases/avancé, AssertJ, TDD) |
| 6.2 Git en pratique | ch. 6-8 (fondamentaux, branches/conflits, PR) |

⇒ Les chapitres d'abord (respecté par ce cycle). **Découpage en 2 PR aligné sur cette frontière** (PR-A = 6-1→6-5, PR-B = 6-6→6-8 ; cf. §8) — chaque PR débloque pédagogiquement son sous-groupe d'exos.

### 5.4 Exercices guidés distincts des exos secs (cycles ultérieurs)
Comme aux modules 1-5 : **l'exercice guidé d'un chapitre ne donne jamais la solution d'un exo noté** des futurs sous-groupes. Il traite une variante proche servant d'échauffement, sur un **domaine neutre**. Mapping des exos à NE PAS spoiler, par chapitre :

| Chapitre | Exos adossés (à NE PAS spoiler) | Domaine neutre retenu pour le guidé |
|---|---|---|
| 6-1 pourquoi tester | 6.1.* (tous) | Réflexion : lister 3 bugs qu'un test aurait attrapés sur du code déjà écrit en M1-M5 (pas d'écriture de test encore). |
| 6-2 JUnit bases | 6.1.1 tests sur classe existante | Tester une méthode `estPair(int)` **fournie** (1 cas vrai, 1 cas faux) — ni la classe ni le domaine de 6.1.1. |
| 6-3 JUnit avancé | 6.1.2 `@ParameterizedTest` | `@CsvSource` sur une conversion Celsius→Fahrenheit — domaine météo, pas celui de 6.1.2. |
| 6-4 AssertJ | 6.1.1 / 6.1.4 (assertions d'objets) | `assertThat(liste).containsExactly(...)` sur une liste de couleurs — domaine neutre. |
| 6-5 TDD | 6.1.3 calculatrice TDD | TDD d'une fonction `pgcd(a,b)` (3 pas Red/Green) — arithmétique, **pas** la calculatrice à 4 opérations de 6.1.3. |
| 6-6 git fondamentaux | 6.2.1 commit/push propre | Initialiser un dépôt-jouet « liste de courses », 2 commits — pas le scénario de 6.2.1. |
| 6-7 branches & conflits | 6.2.2 résolution de conflit | Conflit fabriqué sur un fichier `README` (deux branches éditent la même ligne) — pas le fichier/scénario de 6.2.2. |
| 6-8 workflow PR | 6.2.3 PR fictive | Décrire (sans l'exécuter complètement) le cycle PR sur le dépôt-jouet — pas le `PULL_REQUEST.md` exact de 6.2.3. |

## 6. Plan de contenu par chapitre (cible de rédaction)

Indicatif — le rédacteur ajuste tant que la charte §6 et les garde-fous §5 sont respectés.

- **6-1 Pourquoi tester** — le coût d'un bug trouvé tard ; le test comme **filet de sécurité** qui autorise le changement (refactorer sans peur) ; **types de tests** (unitaire / intégration / bout-en-bout — survol, le module fait de l'unitaire) ; **régression** (un test qui garde un bug corrigé) ; le **ROI** (écrire un test coûte, en re-vérifier mille fois gratuitement rapporte) ; **révélation** : « la moulinette qui vous notait depuis le module 1 est un harnais de tests JUnit — ce module vous apprend à écrire les vôtres ». Erreurs fréquentes : croire que « ça marche chez moi » suffit ; tester seulement le cas passant ; confondre test et `System.out.println` de débogage. Domaine guidé : recenser des bugs qu'un test aurait attrapés (réflexion, sans code).
- **6-2 JUnit 5 — les bases** — où vivent les tests (`src/test/java`, miroir de `src/main/java`) ; une classe `XxxTest`, une méthode `@Test` ; **`Assertions.*`** : `assertEquals(attendu, obtenu)`, `assertTrue`, `assertFalse`, `assertThrows` ; le triptyque **Arrange-Act-Assert** ; nommer un test par le comportement vérifié ; lancer les tests (IDE / `mvn test` / la moulinette). Erreurs fréquentes : inverser `assertEquals(obtenu, attendu)` (message d'erreur trompeur) ; plusieurs comportements dans un seul `@Test` ; oublier `@Test` (méthode jamais exécutée). Domaine guidé : tester `estPair(int)`.
- **6-3 JUnit 5 — avancé** — **`@BeforeEach`/`@AfterEach`** (préparer/nettoyer avant chaque test, éviter la duplication) ; cycle de vie (une instance par test) ; **`@ParameterizedTest`** + **`@ValueSource`** (valeurs simples) et **`@CsvSource`** (couples entrée→attendu, le plus lisible) ; **`@Nested`** pour grouper des tests apparentés ; **`@DisplayName`** pour un libellé lisible ; `@BeforeAll`/`@AfterAll` (`static`, mention). Erreurs fréquentes : partager un état mutable entre tests (d'où `@BeforeEach`) ; mésusage de `@CsvSource` (séparateur, types) ; croire que `@BeforeAll` est par test. Domaine guidé : `@CsvSource` sur Celsius→Fahrenheit.
- **6-4 AssertJ** — **`assertThat(obtenu)`** suivi d'une assertion lisible ; pourquoi AssertJ par-dessus `Assertions.*` (**messages d'échec explicites**, enchaînement fluide, autocomplétion) ; panel utile : `isEqualTo`, `isNotNull`, `contains`/`containsExactly`/`containsExactlyInAnyOrder`, `hasSize`, `isEmpty`, `startsWith` ; **`assertThatThrownBy(() -> …).isInstanceOf(...).hasMessageContaining(...)`** (rappel M5, resitué dans AssertJ) ; comparer des objets (records → `isEqualTo` ; collections → `containsExactly`). Erreurs fréquentes : mélanger import JUnit `assertThat` (déprécié) et AssertJ ; `containsExactly` vs `containsExactlyInAnyOrder` (ordre) ; comparer des objets sans `equals` pertinent (records OK, classe nue NON). Domaine guidé : `assertThat(couleurs).containsExactly(...)`.
- **6-5 TDD en pratique** — le cycle **Red (écrire un test qui échoue) → Green (le code minimal qui passe) → Refactor (nettoyer à filet vert)** ; « le test doit échouer **pour la bonne raison** » (sinon il ne teste rien — pont direct avec le grading « mutation » des exos, §10.1, mais **sans le nommer**) ; **petits pas** ; le test comme **spécification exécutable** ; quand le TDD aide / quand il gêne (exploration). Erreurs fréquentes : écrire le code avant le test (ce n'est plus du TDD) ; un premier test qui passe immédiatement (n'a jamais été rouge) ; sauter le refactor (dette). Domaine guidé : TDD de `pgcd(a,b)` en 3 pas.
- **6-6 Git — fondamentaux** — pourquoi versionner (historique, revenir en arrière, collaborer) ; `git init` ; les **trois zones** (répertoire de travail → *staging*/index → dépôt) ; `git add` / `git commit -m` / `git status` / `git log` / `git diff` ; **`.gitignore`** (ne pas versionner `target/`, fichiers générés) ; anatomie d'un **bon message de commit** (impératif, concis, le *pourquoi*) ; **révélation** : « le `add` + `commit` + `push` que la séquence de rendu faisait pour vous, le voici ». Erreurs fréquentes : `commit` sans `add` (rien de *staged*) ; versionner `target/`/secrets (d'où `.gitignore`) ; message « fix » / « wip » illisible. Domaine guidé : dépôt-jouet « liste de courses », 2 commits.
- **6-7 Git — branches et collaboration** — pourquoi des **branches** (travailler en parallèle sans casser `main`) ; `git branch` / `git switch -c` (et `checkout` mentionné comme l'ancienne forme) ; **`merge`** : *fast-forward* vs commit de merge ; **`rebase`** (survol : rejouer ses commits, à manier avec prudence, **ne pas rebaser du partagé**) ; **résolution d'un conflit** : provoquer un conflit, **lire les marqueurs** `<<<<<<<` / `=======` / `>>>>>>>`, choisir/fusionner à la main, `add` + `commit` pour conclure. Erreurs fréquentes : travailler directement sur `main` ; paniquer devant un conflit (c'est normal et réparable) ; `rebase` d'une branche déjà poussée/partagée. Domaine guidé : conflit fabriqué sur un `README`.
- **6-8 Git — workflow PR** — pousser vers un **dépôt distant** (`git push -u origin <branche>`) ; la **Pull Request** : proposer une branche au merge, ouvrir la discussion ; **fork vs branche** (quand l'un, quand l'autre) ; la **revue de code** : lire le diff, commenter avec bienveillance, demander des changements, approuver ; **boucler** (corriger, re-pousser, merger) ; **clôture du parcours** : ce module ferme la Piscine ; place aux **projets binôme** (dont le projet final #3 met en œuvre tests + Git collaboratif). Erreurs fréquentes : une PR énorme (illisible — préférer petit et fréquent) ; pousser sur la branche de quelqu'un d'autre sans accord ; revue « LGTM » sans avoir lu. Domaine guidé : décrire le cycle PR sur le dépôt-jouet. **Pas de `## Prochain chapitre`** — un bloc de **clôture** renvoyant vers les projets binôme et félicitant le stagiaire.

## 7. Vérification

- **Bloquant (CI #11a, job `build-docusaurus`)** : `cd courses && npm run build` doit passer. `onBrokenLinks: 'throw'` casse au moindre lien interne mort → les liens « Prochain chapitre », la retouche de `5-7-formats-texte` (→ `6-1`), le renvoi de clôture de `6-8` (→ projets/intro) et les ancres doivent être exacts.
- **Validation locale** : build Docusaurus lancé en local avant push (`cd courses && npm run build`).
- **Compilation des extraits Java (recommandée — a attrapé de vrais bugs en M4/M5)** : s'applique aux chapitres **6-2 à 6-5** (classes de test JUnit/AssertJ). Compiler à la main les extraits non triviaux (`@ParameterizedTest` + `@CsvSource`, `assertThatThrownBy`, classe sous test + classe de test) au JDK 25 avec JUnit/AssertJ au classpath (l'uber-jar moulinette les embarque, ou le `.m2` chaud). **Ne s'applique PAS aux ch. 6-6/6-7/6-8** (commandes shell) → pour ceux-là, **vérifier la justesse des commandes git et de leurs sorties** (les exécuter sur un dépôt-jouet jetable).
- **Relecture humaine** (charte §10) : structure respectée, niveau de langue débutant, jargon expliqué (`stub`, `mock` **évités** au niveau débutant ; `staging`, `merge`, `rebase`, `diff` annotés à la 1re occurrence), cohérence avec le référentiel, **dosage du levier méta** (assez pour ancrer, pas au point de spoiler la moulinette ou les exos).

## 8. Workflow d'exécution

- **2 PR** (découpage validé — choix formateur, aligné sur les 2 sous-groupes d'exos) :
  - **PR-A — `feature/m6-chapitres-tests`** : 6-1 → 6-5 (Pourquoi tester, JUnit bases, JUnit avancé, AssertJ, TDD) + retouche liaison `5-7-formats-texte` → `6-1` + bascule `_category_.json` (`collapsed: false`). Le `6-5` pointe (temporairement) vers `6-6` *(à venir)* tant que PR-B n'est pas mergée — **ou** PR-B suit assez vite pour que le lien soit valide ; **résolution retenue** : PR-A pose un lien `6-5 → 6-6` qui sera valide dès PR-B ; tant que PR-B n'est pas là, **garder PR-A non mergée** ou faire pointer `6-5` vers une cible existante. **Le plus simple** : enchaîner PR-A puis PR-B dans la foulée, PR-A mergée seulement quand PR-B est prête (ou les deux dans une branche commune `feature/m6-chapitres` à 2 commits-groupes). → **à confirmer au plan** ; défaut proposé : **une branche `feature/m6-chapitres`, deux PR successives**, la liaison `6-5→6-6` n'étant publiée que lorsque `6-6` existe.
  - **PR-B — (suite)** : 6-6 → 6-8 (Git fondamentaux, branches/conflits, workflow PR) + bloc de clôture de `6-8`.
- **Cadence** : rédaction des chapitres, **commits par chapitre** pour un historique lisible ; build Docusaurus à chaque étape ; **relecture globale** par le formateur en fin de chaque PR. Exécution **inline ou en workflow ultracode** selon la demande du formateur (rédaction ≤6 agents Sonnet → revue adversariale structure/antériorité/anti-spoil + compilation des extraits Java (ch.2-5) / vérification des commandes git (ch.6-8) → build Docusaurus).
- PR relue par un autre formateur → merge une fois le CI vert (+ build local).
- Clôture : mettre à jour [`backlog.md`](../../backlog.md) (#22, avancement du volet chapitres) avec lien commit.

## 9. Critères d'acceptation

- [ ] Les 8 chapitres existent et respectent la structure charte §6.
- [ ] `_category_.json` basculé en `collapsed: false` ; liaison module 5 → module 6 corrigée dans `5-7-formats-texte` (→ `6-1`) ; `6-8` clôt le parcours (renvoi projets binôme, pas de « prochain chapitre »).
- [ ] `npm run build` passe (Docusaurus, liens internes valides).
- [ ] Chaque chapitre a son « exercice guidé » (solution dans `<details>`) et ses « vérifiez vos acquis ».
- [ ] Garde-fou interne respecté : ordre des notions tenu (pourquoi 6-1 avant JUnit 6-2 ; bases 6-2 avant avancé 6-3 ; `Assertions.*` 6-2/6-3 avant AssertJ 6-4 ; tout le bloc test 6-2→6-4 avant TDD 6-5 ; cycle local 6-6 avant branches 6-7 avant PR 6-8) ; aucune notion non encore introduite.
- [ ] Le **levier méta** (la moulinette = harnais de tests ; la séquence de rendu = `add`/`commit`/`push`) est présent aux ch. 6-1 et 6-6, **sans** spoiler l'implémentation de la moulinette ni les exos.
- [ ] Les extraits Java (ch. 6-2→6-5) compilent au JDK 25 (JUnit/AssertJ au classpath) ; les commandes git (ch. 6-6→6-8) sont exactes (vérifiées sur dépôt-jouet).
- [ ] Aucun exercice guidé ne donne la solution d'un exo noté des futurs sous-groupes (mapping §5.4).

## 10. Annexe — décisions transverses module 6 (héritées par le volet exercices)

Tranchées au brainstorming du 2026-06-08 (validées par le formateur via AskUserQuestion), consignées ici pour les cycles d'exercices à venir (aucun livrable dans ce cycle). Les points **[formateur]** ont été explicitement arbitrés ; les points **[raffinement]** sont à confirmer au cycle d'exo concerné.

### 10.1 Grading des tests écrits par le stagiaire (6.1) = « mutation maison » **[formateur]**
Inversion du contrat habituel : en 6.1, **le livrable est le test**, pas le code. Pour prouver qu'un test stagiaire **capture vraiment** un bug (et n'est pas un `assertTrue(true)` complaisant), la moulinette exécute la **suite de tests du stagiaire** contre **plusieurs implémentations cachées** de la classe sous test :
- **1 implémentation correcte** (fournie côté solution/privé) : les tests du stagiaire **doivent passer** (preuve qu'ils n'over-spécifient pas / ne sont pas faux).
- **N implémentations mutées/buggées** (cachées) : les tests du stagiaire **doivent échouer** sur **chacune** (preuve qu'ils détectent le défaut injecté). Une mutation = une altération minime et réaliste (`+`→`-`, `<`→`<=`, oubli d'un cas, retour constant).
- **Score** = proportion de mutants « tués » + passage sur l'impl correcte. **Sans lib** (pas de PIT) : mutants écrits **à la main**, mécanisme à base de **classpath alterné** (la même FQCN compilée depuis des sources différentes), dans l'esprit de ce que `PrivateTestChecker` fait déjà (swap de sources côté `framework`/`runner`).
- **[raffinement] à figer au cycle 6.1** : (a) **nombre de mutants** par exo (proposé : 3–5, un par règle métier) ; (b) **réutiliser ou créer un Checker** — étudier si l'existant (`PublicTestChecker`/`PrivateTestChecker`) suffit en jouant sur les sources, ou s'il faut un `StudentTestChecker`/`MutationChecker` dédié dans `framework` ; (c) **forme du livrable stagiaire** : un fichier de test à compléter (`XxxTest.java` avec des `@Test` à écrire) ; (d) **anti-triche léger** : refuser une suite vide ou triviale (≥ k tests, ≥ 1 assertion par test).
- **Pont pédagogique** : c'est exactement le « le test doit échouer pour la bonne raison » du ch. 6-5 (TDD). Le chapitre **prépare** la notion ; l'exo la **mesure** — sans que le chapitre nomme le mécanisme de mutation (anti-spoil).

### 10.2 Grading des exos Git (6.2) = assertions sur l'état d'un repo-bac-à-sable **[formateur]**
Un workflow git ne « retourne » rien : il **transforme l'état d'un dépôt**. Grading par **inspection de l'état d'un dépôt-jouet isolé** :
- Le starter (ou le test) crée un **dépôt-jouet dédié dans un `@TempDir`** (jamais le repo de la Piscine — pas de collision avec la séquence de rendu `rendu/<sous-groupe>`).
- Le stagiaire exécute la manip demandée (via un petit programme Java qui pilote git, **ou** via des commandes que le test rejoue — **forme à figer au cycle 6.2**).
- **Nos tests** ouvrent ensuite le repo et assertent : nombre de commits (`git log`), existence/position d'une branche, **merge sans marqueur de conflit résiduel** (`<<<<<<<` absent des fichiers), arbre/contenu attendu, message de commit conforme. **Réutiliser `console.git.GitClient` / `ProcessGitClient`** (déjà écrits pour le MVP : `currentBranch`, refs, log) plutôt que réinventer un wrapper git.
- **PR fictive (6.2.3)** : pas de GitHub en local/standalone → la « PR » est simulée par un **merge de branche local** + un fichier **`PULL_REQUEST.md`** que le stagiaire rédige (titre, description, checklist de revue). Grading : auto sur l'état git (branche mergée proprement) + présence/structure du `PULL_REQUEST.md` ; la **qualité** de la description relève du critère `formateur`.
- **[raffinement] à figer au cycle 6.2** : (a) **comment le stagiaire « rend » une manip git** — programme Java pilotant `GitClient`, ou script de commandes exécuté par le harnais ? (b) part **auto vs formateur** par exo (proposé : 6.2.1/6.2.2 majoritairement auto sur l'état du repo, 6.2.3 PR fictive plus formateur sur la description).

### 10.3 Modèle de domaine / supports locaux au module 6 — `etnc.m6` **[raffinement]**
Pas de réutilisation/import des classes M2-M5 (packages distincts, ZIP standalone copie exo par exo). Supports **fournis complets** (« FOURNI — ne pas modifier ») dans les starters, package `etnc.m6` :
- **6.1.1 (tester une classe existante)** : une classe **fournie et correcte** (ex. un petit validateur ou un convertisseur), dont le stagiaire **écrit la suite de tests** ; les mutants cachés (§10.1) en dérivent.
- **6.1.3 (calculatrice TDD)** : énoncé impose d'écrire **tests d'abord** ; 4 opérations + **division par zéro qui lève** (acquis exceptions M5) ; le stagiaire livre **tests + implémentation** ; double grading (nos tests sur son code **et** ses tests contre des mutants).
- **6.1.4 (refactor d'un exo M3 sous tests)** : **recopie locale `etnc.m6`** d'une classe OO du module 3 (candidats : `3.1.2-compte`, `3.2.1-rectangle`, ou une classe à invariants) — le stagiaire **écrit d'abord la suite de tests** (filet), puis applique un **petit refactor sûr** (extraire une méthode, renommer) en gardant les tests verts. Classe exacte **à confirmer au cycle 6.1**.
- **6.2.* (Git)** : dépôts-jouets neutres (fichiers texte simples : `notes.txt`, `README.md`), aucun lien avec le repo Piscine.

### 10.4 Barème `evaluation.yml` — adaptation au module **[raffinement, à figer par sous-groupe]**
Le gabarit M1-M5 (tests-publics 8 + tests-prives 8 + `style` 2 + 1 formateur 2 = 20, seuil 12) **ne se mappe pas mécaniquement** sur 6.1 (le livrable est le test) ni sur 6.2 (l'état d'un repo). Adaptations proposées (à confirmer) :
- **6.1** : « tests-publics » = les tests **du stagiaire passent sur l'impl correcte** (≈ 8) ; « tests-prives » = les tests **du stagiaire tuent les mutants cachés** (≈ 8, proportion de mutants tués) ; `style` 2 (Checkstyle sur le code de test du stagiaire) ; 1 formateur 2 (`demarche` pour la qualité/lisibilité des tests). Total 20, seuil 12.
- **6.2** : « publics » = assertions sur l'état du repo correspondant aux étapes **visibles** de la consigne ; « prives » = vérifications fines (message de commit, absence de marqueur de conflit, branche exacte) ; 1 formateur 2 (`respect-consignes` ou, pour 6.2.3, qualité du `PULL_REQUEST.md`). `style` peu pertinent en 6.2 (peu de code) → poids éventuellement reversé sur le critère formateur, **à trancher au cycle 6.2**.
- Format exercice : **9–11 fichiers** (`docs/format-exercice.md`), package **`etnc.m6`**, groupId **`etnc.piscine.m6`**, dossier module **`module-6-tests-git`** (slug exact à vérifier en créant le premier exo).

### 10.5 Roster des 7 exercices (2 sous-groupes) — esquisse
Détaillé (signatures, fixtures, mutants, critère par exo) **au cycle de chaque sous-groupe**. Vérification d'antériorité ci-dessous.

**6.1 Tests avec JUnit 5** (→ ch. 1-5) :
- **6.1.1 `tests-classe-existante`** — écrire la suite de tests JUnit/AssertJ d'une classe **fournie correcte** ; grading « mutation » (les tests tuent des mutants cachés). Critère : `demarche`.
- **6.1.2 `tests-parametres`** — couvrir une méthode avec un **`@ParameterizedTest`** (`@CsvSource`) ; grading mutation + exigence d'usage du paramétrage (critère `respect-consignes`/`idiomatisme`).
- **6.1.3 `calculatrice-tdd`** — TDD d'une calculatrice (4 opés + division/0 qui **lève**) ; livre tests **et** code ; double grading (nos tests sur son code + ses tests contre mutants). Critère : `demarche`.
- **6.1.4 `refactor-sous-tests`** — recopie `etnc.m6` d'une classe M3 ; écrire d'abord les tests (filet), puis un petit refactor sûr à filet vert. Critère : `demarche`.

**6.2 Git en pratique** (→ ch. 6-8 ; repo-bac-à-sable isolé) :
- **6.2.1 `commit-propre`** — cycle `init`/`add`/`commit` propre sur un dépôt-jouet : commits atomiques, messages conformes ; grading sur l'état du repo (`GitClient`). Critère : `respect-consignes`.
- **6.2.2 `resolution-conflit`** — résoudre un **conflit fabriqué** (deux branches éditent la même ligne) : merger, supprimer les marqueurs, conclure par un commit ; grading : merge propre, aucun `<<<<<<<` résiduel, contenu attendu. Critère : `demarche`.
- **6.2.3 `pull-request-fictive`** — ouvrir une « PR » simulée (branche + merge local) + rédiger un **`PULL_REQUEST.md`** (titre, description, checklist de revue) ; grading : état git + présence/structure du fichier, qualité au critère `formateur`. Critère : `respect-consignes` (+ part formateur sur la description).

**Vérification d'antériorité** : 6.1 → ch.1-5 (tests/AssertJ/TDD, aucune notion git) ✓ ; 6.2 → ch.6-8 (git local→branches→PR), aucune notion de test JUnit non vue ✓. **Aucun risque bloquant** ; les deux subtilités de conception (mécanisme de mutation §10.1, forme de rendu git §10.2) sont **internes au cycle exos**, pas au cycle chapitres.

### 10.6 Projet binôme #3 (#25) — final
**Débloqué par le module 6** (tests + Git collaboratif) : sa fiche (`exercises/projets-binome/projet-3-mini-moulinette/`) sera rédigée dans un **cycle dédié après le module 6**, comme les binômes #1/#2. C'est le **projet final** de la Piscine : une **mini-moulinette pédagogique** (compile un fichier Java, l'exécute sur des cas, génère un rapport) avec **≥ 70 % de couverture JUnit** et **branches + PR** imposés — synthèse de tout le parcours (OO M3, persistance M5, tests + Git M6). #22 ne le touche pas.
