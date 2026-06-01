# Spec — #15 Chapitres restants du module 1

> Design validé le 2026-06-01 (brainstorming). Tâche backlog **#15**.
> Cycle isolé : #15 a sa propre spec / plan / PR. #16, #13, #14 suivront dans leurs cycles.
> Références cadre : [`charte-redaction.md`](../../charte-redaction.md) (§6 structure, §2/§3 ton/langue, §7 code, §10 relecture), [`referentiel.md`](../../referentiel.md) (§4 module 1), modèle existant [`1-1-installer-java.md`](../../../courses/docs/module-1-fondamentaux/1-1-installer-java.md).

## 1. Objectif et périmètre

Compléter le **module 1** côté cours : produire les **6 chapitres** manquants (1.2 → 1.7) dans `courses/docs/module-1-fondamentaux/`, conformes à la charte §6, pédagogiquement cohérents entre eux et avec le chapitre 1.1 déjà en ligne.

**Hors périmètre** : les exercices secs (#16), la grille d'évaluation (#13), la gouvernance (#14). Aucun fichier sous `exercises/` n'est touché.

## 2. Livrables

6 fichiers Markdown + 1 retouche de liaison :

| Fichier | `id` | `sidebar_position` | Notions (réf. §4 module 1) |
|---|---|---|---|
| `1-2-variables-types-primitifs.md` | `1-2-variables-types-primitifs` | 2 | `int`, `long`, `double`, `boolean`, `char`, `String`, conversions, `var` |
| `1-3-operateurs.md` | `1-3-operateurs` | 3 | arithmétiques, comparaison, logiques, affectation composée, priorité |
| `1-4-entrees-clavier.md` | `1-4-entrees-clavier` | 4 | `Scanner`, lecture sécurisée, parsing simple |
| `1-5-conditions.md` | `1-5-conditions` | 5 | `if` / `else if` / `else`, `switch` expression (Java 21+), ternaire |
| `1-6-boucles.md` | `1-6-boucles` | 6 | `for`, `while`, `do-while`, `break`, `continue`, choix de la boucle |
| `1-7-bonnes-pratiques-lisibilite.md` | `1-7-bonnes-pratiques-lisibilite` | 7 | indentation, nommage, commentaires utiles |

**Retouche** : le bloc « Prochain chapitre » de `1-1-installer-java.md` (actuellement « Chapitre 1.2 — *(à venir)* ») pointe vers `1-2`. `_category_.json` inchangé. Le chapitre 1.7 pointe vers « Module 2 — *(à venir)* ».

## 3. Structure imposée (chaque chapitre)

Ordre strict de la charte §6 :

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

**Longueur cible** : 800–2000 mots par chapitre. Au-delà, scinder une section.

## 4. Conventions transverses (décisions validées)

- **Exemples indépendants par chapitre** : chaque chapitre prend l'exemple le plus clair pour SA notion, sans fil conducteur imposé. Touche militaire seulement quand elle clarifie (charte §4), jamais forcée.
- **Ton / langue** : vouvoiement, phrases courtes, voix active (charte §2). Règle des trois fois pour le vocabulaire technique (charte §3), tenue d'un chapitre à l'autre. Acronymes développés à la première occurrence du document.
- **Code** (charte §7) : identifiants Java en anglais, métier en français quand plus clair ; commentaires français expliquant le *pourquoi* ; lisibilité avant astuce ; imports explicites ; pas de variable d'une lettre hors `i/j/k` et `e`.
- **Liens « Pour aller plus loin »** standardisés sur les sources du référentiel §2 : **dev.java**, **Baeldung**, **Javadoc OpenJDK 25** (`docs.oracle.com/en/java/javase/25/...`). 1 à 3 liens annotés par chapitre.
- **Markdown** (charte §9) : un seul `#`, pas de saut de niveau de titre, blocs de code toujours typés (` ```java `, ` ```text `, ` ```bash `).

## 5. Cohérence pédagogique (garde-fous)

### 5.1 Règle d'antériorité
Aucune notion n'est utilisée avant son chapitre d'origine. Conséquences concrètes :
- Pas de `Scanner` ni de lecture clavier avant **1.4**.
- Pas de `if`/`switch` avant **1.5**.
- Pas de boucle avant **1.6**.
- Le chapitre 1.2 n'utilise donc ni condition ni boucle dans ses exemples ; 1.3 reste sur des expressions ; etc. La progression est strictement linéaire (référentiel §6).

### 5.2 Exercices guidés distincts des exos secs #16
Décision validée : **l'exercice guidé d'un chapitre ne donne jamais la solution d'un exo noté de #16**. Il traite une variante proche servant d'échauffement. Mapping des exos #16 à éviter, par chapitre :

| Chapitre | Exos #16 adossés (à NE PAS spoiler) | Concept retenu pour le guidé |
|---|---|---|
| 1.2 | 1.2.1 conversion-unites | Déclarer/afficher quelques variables d'un profil (`int`, `double`, `char`, `boolean`) — pas de conversion d'unité. |
| 1.3 | 1.2.2 calculs-geometriques, 1.2.3 manipulation-booleenne | Moyenne pondérée de deux notes (priorité + affectation composée) — pas de géométrie ni de table de vérité complète. |
| 1.4 | 1.1.3 lecture-saisie, 1.3.4 devine-le-nombre | Lire un entier au clavier et afficher son double — pas de boucle de jeu. |
| 1.5 | 1.3.1 fizzbuzz, 1.3.4 devine-le-nombre | Attribuer une mention à une note (`if`/`else if` puis `switch`) — pas de FizzBuzz. |
| 1.6 | 1.3.1 fizzbuzz, 1.3.2 fibonacci, 1.3.3 table-multiplication, 1.3.4 devine | Somme cumulée de 1 à N (ou compte à rebours `while`) — ni FizzBuzz, ni Fibonacci, ni table. |
| 1.7 | (aucun exo dédié) | Refactor d'un extrait volontairement illisible vers une version propre. |

## 6. Plan de contenu par chapitre (cible de rédaction)

Indicatif — le rédacteur ajuste tant que la charte §6 et les garde-fous §5 sont respectés.

- **1.2 Variables et types primitifs** — notion de variable (déclaration/affectation) ; types primitifs entiers (`int`, `long`) et flottants (`double`) ; `boolean` ; `char` ; `String` (objet, pas primitif, distinction signalée) ; conversions (élargissement implicite, *cast* explicite, perte de précision) ; `var` (inférence, quand l'utiliser/l'éviter). Erreurs fréquentes : division entière, dépassement de capacité, `==` sur `String` (annoncé, traité plus tard), oubli du `f`/`L`.
- **1.3 Opérateurs** — arithmétiques (`+ - * / %`, division entière vs flottante) ; comparaison (`== != < > <= >=`) ; logiques (`&& || !`, court-circuit) ; affectation composée (`+= -= *= /= %=`) ; priorité et parenthésage. Erreurs fréquentes : précédence mal anticipée, `%` sur flottants, confusion `=`/`==`.
- **1.4 Entrées clavier** — `Scanner` sur `System.in` ; `nextInt`/`nextDouble`/`nextLine` ; piège du `nextLine` après `nextInt` ; lecture « sécurisée » (vérifier le type attendu, message clair) ; parsing simple (`Integer.parseInt`). Erreurs fréquentes : `InputMismatchException`, tampon résiduel, fermeture de `Scanner(System.in)`.
- **1.5 Conditions** — `if` / `else if` / `else` ; blocs et accolades (toujours les mettre) ; `switch` *expression* (Java 21+, `->`, `yield`) ; opérateur ternaire (usage mesuré). Erreurs fréquentes : `=` au lieu de `==`, oubli d'accolades (« dangling »), chaînes comparées avec `==`, cas `switch` non couverts.
- **1.6 Boucles** — `while` ; `do-while` ; `for` classique ; `break` / `continue` ; choisir la bonne boucle selon le cas. Erreurs fréquentes : boucle infinie, erreur d'indice (`off-by-one`), condition jamais vraie/fausse.
- **1.7 Bonnes pratiques de lisibilité** — indentation cohérente ; nommage parlant (rappel anglais/métier de la charte §7) ; commentaires utiles (*pourquoi* pas *quoi*) ; petites fonctions/blocs courts ; constantes nommées plutôt que « nombres magiques ». Sert de charnière vers le module 2. Exemple central : avant/après d'un même extrait.

## 7. Vérification

- **Bloquant (CI #11a, job `build-docusaurus`)** : `cd courses && npm run build` doit passer. `onBrokenLinks: 'throw'` casse au moindre lien interne mort → les liens « Prochain chapitre » et ancres doivent être exacts.
- **Non couvert automatiquement** : les extraits de code des chapitres ne sont **ni compilés ni testés** (les chapitres ne passent pas par `valider-solutions`, qui ne cible que les `solution/` d'exercices). Vu l'absence de Java 25 sur la machine du formateur (cf. #12), la correction du code repose sur une **relecture manuelle attentive**, calée sur l'idiome déjà validé du chapitre 1.1. Assumé explicitement.
- **Relecture humaine** (charte §10) : critères de la grille de validation — structure respectée, niveau de langue, pas de jargon militaire non expliqué, cohérence avec le référentiel.

## 8. Workflow d'exécution

- Branche `feature/m1-chapitres` issue de `main`.
- **Cadence validée** : rédaction des **6 chapitres en une fois**, build Docusaurus, puis **relecture globale** par le formateur. (Un écart de format se corrige alors en une passe sur les 6.) Commits par chapitre pour garder l'historique lisible.
- Une **PR unique** « #15 — chapitres module 1 » → relue par un autre formateur → merge une fois le CI vert.
- Clôture : cocher les critères #15 dans [`backlog.md`](../../backlog.md), statut `Faite` + lien commit.

## 9. Critères d'acceptation (rappel backlog #15)

- [ ] Les 6 chapitres existent et respectent la structure charte §6.
- [ ] `npm run build` passe (Docusaurus, liens internes valides).
- [ ] Chaque chapitre a son « exercice guidé » et ses « vérifiez vos acquis ».
- [ ] Aucune notion utilisée avant son chapitre d'origine (cohérence référentiel).
- [ ] Aucun exercice guidé ne donne la solution d'un exo noté de #16.

## 10. Annexe — décision de roadmap connexe (hors #15)

Tranché au même brainstorming, pour mémoire (à appliquer dans les cycles suivants, pas ici) :
- **Découpage** : un cycle (spec/plan/PR) **par tâche** — #15, puis #16, puis #13, puis #14.
- **Séquence #13/#14** : **#13 (`docs/grille-evaluation.md`) avant #14 (`CONTRIBUTING.md` + `LICENSE`)**, pour que CONTRIBUTING renvoie à une grille existante. La séquence MVP finale devient donc : **#15 → #16 → #13 → #14**. Le step 6 de la roadmap du backlog sera mis à jour en conséquence lorsqu'on entamera ces cycles.
