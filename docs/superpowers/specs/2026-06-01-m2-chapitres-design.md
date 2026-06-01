# Spec — #18 Chapitres du module 2 (Tableaux, chaînes, méthodes)

> Design validé le 2026-06-01 (brainstorming). Tâche backlog **#18**.
> Cycle isolé : ce cycle ne couvre que les **5 chapitres**. Les 4 sous-groupes d'exercices (2.1 → 2.4) suivront dans leurs propres cycles spec / plan / PR (cadence rétro #17 : chapitres d'abord).
> Point de départ : [`docs/superpowers/2026-06-01-m2-kickoff.md`](../2026-06-01-m2-kickoff.md).
> Références cadre : [`charte-redaction.md`](../../charte-redaction.md) (§6 structure, §2/§3 ton/langue, §7 code, §10 relecture), [`referentiel.md`](../../referentiel.md) (§4 module 2), modèle existant [`module-1-fondamentaux/`](../../../courses/docs/module-1-fondamentaux/) et spec sœur [`2026-06-01-m1-chapitres-design.md`](2026-06-01-m1-chapitres-design.md).

## 1. Objectif et périmètre

Produire les **5 chapitres** du module 2 dans `courses/docs/module-2-tableaux-chaines-methodes/`, conformes à la charte §6, cohérents entre eux et avec le module 1 (qui se termine sur le chapitre charnière 1.7 « bonnes pratiques »).

**Objectif pédagogique** (référentiel §4) : structurer du code en méthodes réutilisables ; manipuler tableaux 1D/2D et chaînes ; aborder la récursivité.

**Hors périmètre** : tout fichier sous `exercises/` (les 12 exos = cycles ultérieurs), la moulinette, la grille d'évaluation. Aucun exercice noté n'est touché. Les **décisions transverses** prises au brainstorm (test, récursivité, base du refactor) sont consignées en §10 pour que le futur volet exercices en hérite — elles ne produisent aucun livrable dans ce cycle.

## 2. Livrables

5 fichiers Markdown + 1 fichier de config + 1 retouche de liaison :

| Fichier | `id` | `sidebar_position` | Notions (réf. §4 module 2) |
|---|---|---|---|
| `2-1-tableaux-1d.md` | `2-1-tableaux-1d` | 1 | `int[]`, `new`, littéral `{…}`, accès indexé, `length`, parcours `for` / `for-each`, `Arrays.toString` |
| `2-2-tableaux-2d.md` | `2-2-tableaux-2d` | 2 | `int[][]`, matrice, parcours imbriqué, longueur par ligne, applications grille / image N&B |
| `2-3-chaines-de-caracteres.md` | `2-3-chaines-de-caracteres` | 3 | `String` immuable, `length()`, `charAt`, `substring`, `indexOf`, `equals` vs `==`, `toUpperCase`/`toLowerCase`, `split`, `StringBuilder` |
| `2-4-methodes.md` | `2-4-methodes` | 4 | signature, paramètres, type de retour, `void`/`return`, surcharge, portée des variables, `static` |
| `2-5-recursivite.md` | `2-5-recursivite` | 5 | cas de base, cas récursif, pile d'exécution, factorielle, `StackOverflowError`, récursif vs itératif |

**Config** : `_category_.json` calqué sur le module 1 — `label` « Module 2 — Tableaux, chaînes, méthodes », `position: 3`, `collapsible: true`, `collapsed: false`, `link.type: generated-index` avec description = objectif référentiel §4.

**Retouche de liaison** : le bloc « Prochain chapitre » de `1-7-bonnes-pratiques-lisibilite.md` (module 1) pointe actuellement vers « Module 2 — *(à venir)* » → le faire pointer vers `2-1-tableaux-1d`. Le chapitre 2.5 pointe vers « Module 3 — POO *(à venir)* ».

## 3. Structure imposée (chaque chapitre)

Ordre strict de la charte §6 (identique au module 1) :

1. Frontmatter : `id`, `sidebar_position`, `title`, `description`.
2. `# Titre`
3. `## Pourquoi ce chapitre` (2–3 phrases).
4. `## Ce que vous saurez faire à la fin` (objectifs en verbes d'action).
5. Sections numérotées `## 1. …`, `## 2. …` : notion + `### Exemple` (code annoté) + `### À retenir` (encadré `>`).
6. `## Erreurs fréquentes` (puces **symptôme → cause → correction**).
7. `## Exercice guidé` (pas-à-pas, solution repliée dans `<details>`).
8. `## Vérifiez vos acquis` (questions ouvertes).
9. `## Pour aller plus loin` (liens externes annotés).
10. `## Prochain chapitre` (→ lien vers le suivant).

**Longueur cible** : 800–2000 mots par chapitre (le module 1 tournait à ~1 200).

## 4. Conventions transverses (décisions validées)

- **Exemples indépendants par chapitre** : chaque chapitre prend l'exemple le plus clair pour SA notion, sans fil conducteur imposé. Touche militaire seulement quand elle clarifie (charte §4), jamais forcée.
- **Ton / langue** : vouvoiement, phrases courtes, voix active (charte §2). Règle des trois fois pour le vocabulaire technique (charte §3), tenue d'un chapitre à l'autre. Acronymes développés à la première occurrence.
- **Code** (charte §7) : identifiants Java en anglais, métier en français quand plus clair ; commentaires français expliquant le *pourquoi* ; lisibilité avant astuce ; imports explicites (pas de `import java.util.*`) ; pas de variable d'une lettre hors `i/j/k` et `e`.
- **Liens « Pour aller plus loin »** standardisés sur les sources du référentiel §2 : **dev.java**, **Baeldung**, **Javadoc OpenJDK 25** (`docs.oracle.com/en/java/javase/25/...`). 1 à 3 liens annotés par chapitre.
- **Markdown** (charte §9) : un seul `#`, pas de saut de niveau de titre, blocs de code toujours typés (` ```java `, ` ```text `, ` ```bash `).

## 5. Cohérence pédagogique (garde-fous)

### 5.1 Règle d'antériorité — externe (modules précédents)
Tout l'acquis du module 1 est disponible (variables, opérateurs, `Scanner`, conditions, boucles). Aucune notion d'un module **ultérieur** (objets/classes du module 3, exceptions, fichiers) n'apparaît.

### 5.2 Règle d'antériorité — **interne au module 2** (garde-fou central)
La décision « tout dans `main` » pour les exercices 2.1/2.2 (cf. §10) impose une discipline dans les **chapitres qui précèdent le chapitre Méthodes (2-4)** :

- Les chapitres **2-1, 2-2, 2-3** ne **définissent aucune méthode `static` maison** dans leurs exemples ni dans leur « exercice guidé » : tout le code illustratif tient dans `main` (ou dans des extraits sans signature de méthode).
- **Appeler** une méthode de bibliothèque (`Arrays.toString(t)`, `s.length()`, `sb.append(...)`) est permis dès 2-1 : on consomme une méthode existante sans savoir encore en écrire une.
- **Écrire** sa propre méthode n'est introduit qu'au chapitre **2-4**. Les chapitres 2-4 et 2-5 peuvent donc structurer leurs exemples en méthodes.
- **2-5 Récursivité dépend de 2-4** (une fonction récursive est une méthode qui s'appelle elle-même) → 2-5 reste strictement en dernier.

### 5.3 Exercices guidés distincts des exos secs (cycles ultérieurs)
Comme au module 1 : **l'exercice guidé d'un chapitre ne donne jamais la solution d'un exo noté** des futurs sous-groupes. Il traite une variante proche servant d'échauffement. Mapping des exos à NE PAS spoiler, par chapitre :

| Chapitre | Exos adossés (à NE PAS spoiler) | Concept retenu pour le guidé |
|---|---|---|
| 2-1 Tableaux 1D | 2.1.1 min/max/moyenne, 2.1.2 inversion, 2.1.3 recherche linéaire | Somme des éléments d'un tableau + affichage via `Arrays.toString` — ni min/max, ni inversion, ni recherche. |
| 2-2 Tableaux 2D | 2.1.4 rotation de matrice, 2.4.2 parcours récursif | Afficher une matrice ligne par ligne et calculer la somme d'une ligne — pas de rotation. |
| 2-3 Chaînes | 2.2.1 palindrome, 2.2.2 comptage d'occurrences, 2.2.3 ASCII art | Mettre une phrase en majuscules et compter sa longueur — ni palindrome, ni comptage de caractère. |
| 2-4 Méthodes | 2.3.1 biblio maths, 2.3.2 surcharge, 2.3.3 refactor de 1.2.2 | Extraire une méthode `static int max(int a, int b)` et la surcharger pour 3 entiers — pas la bibliothèque maths complète, pas le refactor de calculs-geometriques. |
| 2-5 Récursivité | 2.4.1 factorielle/puissance, 2.4.2 parcours récursif | Somme de 1 à N en récursif (cas de base + cas récursif) — ni factorielle, ni puissance. |

## 6. Plan de contenu par chapitre (cible de rédaction)

Indicatif — le rédacteur ajuste tant que la charte §6 et les garde-fous §5 sont respectés.

- **2-1 Tableaux 1D** — qu'est-ce qu'un tableau (collection de taille fixe, indexée à partir de 0) ; déclaration `int[] t`, allocation `new int[n]`, littéral `{…}` ; accès `t[i]` ; `length` (attribut, pas méthode — contraste avec `String.length()` annoncé pour 2-3) ; parcours `for` indexé et `for-each` (quand préférer lequel) ; afficher avec `Arrays.toString` (premier `import` explicite `java.util.Arrays`). Erreurs fréquentes : `ArrayIndexOutOfBoundsException` (off-by-one sur `length`), confusion `length`/`length()`, tableau non initialisé (valeurs par défaut `0`). Exemple métier : relevé de notes de tir d'une section.
- **2-2 Tableaux 2D** — tableau de tableaux `int[][]`, vu comme **matrice** (lignes × colonnes) ; allocation `new int[lignes][colonnes]` et littéral imbriqué ; accès `m[i][j]` ; `m.length` (nb de lignes) vs `m[i].length` (nb de colonnes) ; parcours imbriqué (deux `for`) ; applications grille / image noir & blanc (matrice de `0`/`1`). Erreurs fréquentes : inversion ligne/colonne, supposer toutes les lignes de même longueur, double `ArrayIndexOutOfBounds`. Exemple métier : grille de tir, damier.
- **2-3 Chaînes de caractères** — `String` est un **objet immuable** (toute « modification » crée une nouvelle chaîne) ; `length()` (méthode, vs `length` des tableaux) ; `charAt(i)` ; `substring` ; `indexOf` ; comparaison `equals` vs `==` (rappel : piège annoncé au module 1, **résolu ici**) ; `toUpperCase`/`toLowerCase` ; `split` ; `StringBuilder` pour concaténer dans une boucle (coût de la concaténation par `+`). Erreurs fréquentes : `==` sur des chaînes, `StringIndexOutOfBounds`, oublier que `substring` ne modifie pas l'original. Exemple métier : message de transmission à normaliser.
- **2-4 Méthodes** — pourquoi factoriser du code en méthodes (lisibilité, réutilisation, non-répétition) ; anatomie d'une signature (modificateurs, type de retour, nom, paramètres) ; `void` vs valeur de retour, instruction `return` ; passage de paramètres (par valeur — primitifs copiés, référence de tableau partagée, à signaler) ; **surcharge** (même nom, signatures différentes) ; portée des variables (locale à la méthode/au bloc) ; pourquoi `static` ici (pas encore d'objets — on appelle sans instance ; sera nuancé au module 3). Fait le pont vers le refactor 2.3.3. Erreurs fréquentes : oublier `return`, type de retour incohérent, croire qu'on modifie un primitif passé en paramètre, masquage de variable. Exemple métier : méthodes utilitaires sur un effectif.
- **2-5 Récursivité** — définition (une méthode qui s'appelle elle-même) ; **cas de base** (condition d'arrêt) et **cas récursif** ; déroulé sur la **pile d'exécution** (schéma `text` des appels empilés/dépilés) ; exemple factorielle ; `StackOverflowError` quand le cas de base est absent ou jamais atteint ; quand préférer la récursivité à l'itération (et inversement). Dépend du chapitre 2-4. Erreurs fréquentes : cas de base manquant, argument qui ne décroît pas vers le cas de base, récursion là où une boucle serait plus simple. Exemple : compte à rebours / factorielle.

## 7. Vérification

- **Bloquant (CI #11a, job `build-docusaurus`)** : `cd courses && npm run build` doit passer. `onBrokenLinks: 'throw'` casse au moindre lien interne mort → les liens « Prochain chapitre », la retouche du 1-7 et les ancres doivent être exacts.
- **Validation locale (nouveau, cf. kickoff §1)** : le build Docusaurus peut être lancé en local avant push (`cd courses && npm run build`) — boucle plus rapide que le CI.
- **Non couvert automatiquement** : les extraits de code des chapitres ne sont **ni compilés ni testés** (les chapitres ne passent pas par `valider-solutions`, qui ne cible que les `solution/` d'exercices). La correction du code des exemples repose sur une **relecture manuelle attentive**, calée sur l'idiome déjà validé du module 1. Assumé explicitement. *(Note : Java 25 est désormais installé localement — un extrait douteux peut être vérifié à la main si besoin, mais ce n'est pas systématisé pour les chapitres.)*
- **Relecture humaine** (charte §10) : structure respectée, niveau de langue adapté, pas de jargon militaire non expliqué, cohérence avec le référentiel.

## 8. Workflow d'exécution

- Branche `feature/m2-chapitres` issue de `main` (déjà créée).
- **Cadence validée** (modèle cycle #15) : rédaction des **5 chapitres en une fois**, build Docusaurus, puis **relecture globale** par le formateur. Commits par chapitre pour garder l'historique lisible.
- Une **PR unique** « #18 — chapitres module 2 » → relue par un autre formateur → merge une fois le CI vert (+ build local).
- Clôture : mettre à jour [`backlog.md`](../../backlog.md) (#18, avancement du volet chapitres) avec lien commit.

## 9. Critères d'acceptation

- [ ] Les 5 chapitres existent et respectent la structure charte §6.
- [ ] `_category_.json` créé (position 3) et liaison module 1 → module 2 corrigée dans `1-7`.
- [ ] `npm run build` passe (Docusaurus, liens internes valides).
- [ ] Chaque chapitre a son « exercice guidé » (solution dans `<details>`) et ses « vérifiez vos acquis ».
- [ ] Garde-fou interne respecté : aucune méthode `static` maison dans les exemples des chapitres 2-1 à 2-3 ; récursivité (2-5) seulement après méthodes (2-4).
- [ ] Aucun exercice guidé ne donne la solution d'un exo noté des futurs sous-groupes (mapping §5.3).

## 10. Annexe — décisions transverses module 2 (héritées par le volet exercices)

Tranchées au même brainstorming, consignées ici pour les cycles d'exercices à venir (aucun livrable dans ce cycle) :

1. **Stratégie de test / antériorité « méthodes »** : « tout dans `main` » pour les sous-groupes **2.1 (tableaux) et 2.2 (chaînes)** — E/S console, testés sur la sortie standard (modèle module 1, util `CaptureEntree`). Les méthodes `static` écrites par le stagiaire, **testées par valeur de retour** (AssertJ direct), n'apparaissent qu'**à partir de 2.3** (après le chapitre 2-4 Méthodes). Pas de réordonnancement des chapitres.
2. **Récursivité (2.4)** : la récursivité est **imposée par la consigne** ; les tests ne vérifient que le **résultat** (valeur de retour correcte) ; le respect de la forme récursive relève du critère formateur **`respect-consignes`** (pas de vérification statique de l'auto-appel).
3. **Refactor 2.3.3** : base = **`1.2.2 calculs-geometriques`** du module 1 (extraction en méthodes `static`, surcharge naturelle `aire`/`perimetre`).
4. **Action formateur en attente** (kickoff §5) : remplir les temps réels de production dans `docs/retro-module-1.md` §4 pour dimensionner la charge des exercices #18. Indépendant de ce cycle.
