# Spec — #21 Chapitres du module 5 (Exceptions et I/O)

> Design validé le 2026-06-04 (brainstorming avec le formateur — 4 décisions structurantes tranchées + 3 recommandations acceptées). Tâche backlog **#21**.
> Cycle isolé : ce cycle ne couvre que les **7 chapitres**. Les 3 sous-groupes d'exercices (5.1 → 5.3) suivront dans leurs propres cycles spec / plan / PR (cadence : chapitres d'abord — les notions bornent ce que les exercices peuvent demander).
> Point de départ : [`docs/superpowers/2026-06-04-m5-kickoff.md`](../2026-06-04-m5-kickoff.md).
> Références cadre : [`charte-redaction.md`](../../charte-redaction.md) (§6 structure, §2/§3 ton/langue, §7 code, §10 relecture), [`referentiel.md`](../../referentiel.md) (§module 5), modèles existants [`module-4-collections-generiques-lambdas/`](../../../courses/docs/module-4-collections-generiques-lambdas/), spec sœur [`2026-06-04-m4-chapitres-design.md`](2026-06-04-m4-chapitres-design.md).

## 1. Objectif et périmètre

Produire les **7 chapitres** du module 5 dans `courses/docs/module-5-exceptions-io/`, conformes à la charte §6, cohérents entre eux et avec le module 4 (qui se termine sur le chapitre 4-8 « Streams et `Optional` »).

**Objectif pédagogique** (référentiel §module 5) : gérer correctement les erreurs, lire/écrire des fichiers, manipuler quelques formats texte courants.

**Nouveauté structurante** (kickoff §4) : le module 5 **lève le verrou central des modules 1-4** — l'interdiction de lever des exceptions. Jusqu'ici les cas limites se géraient par **valeur sentinelle / `null` / `Optional`**. Le module 5 fait de la gestion d'erreurs **son sujet** : on enseigne et on exige désormais de **lever** (`throw`), **propager** (`throws`), **rattraper** (`try`/`catch`) et **tester** une exception (custom comprise). Deuxième nouveauté : **l'I/O fichier** (lecture/écriture, ressources fermées proprement, chemins `Path`/`Files`) et **quelques formats texte** (CSV, `.properties`).

**Hors périmètre** : tout fichier sous `exercises/` (les 10 exos = cycles ultérieurs), la moulinette, la fiche du **Projet binôme #2** (débloqué *après* le module 5, cycle dédié). Aucun exercice noté n'est touché. Les **décisions transverses** prises au brainstorm (exceptions custom unchecked, NIO.2 imposé dans les exos, formats parsés à la main sans lib, pattern `@TempDir`, modèle de domaine local `etnc.m5`, roster des 10 exos) sont consignées en §10 pour que le futur volet exercices en hérite — elles ne produisent aucun livrable dans ce cycle.

## 2. Livrables

7 fichiers Markdown + 1 retouche de config + 1 retouche de liaison :

| Fichier | `id` | `sidebar_position` | Notions (réf. §module 5) |
|---|---|---|---|
| `5-1-hierarchie-exceptions.md` | `5-1-hierarchie-exceptions` | 1 | `Throwable` → `Error` / `Exception` → `RuntimeException` ; checked vs unchecked (qui force `throws`, qui ne le force pas) ; ce qu'est une exception « levée » ; lire une **stack trace** ; distinguer erreur de compilation et exception à l'exécution |
| `5-2-try-catch-finally.md` | `5-2-try-catch-finally` | 2 | `try`/`catch`/`finally`, **multi-catch** (`A \| B`), `finally` toujours exécuté, `throw` (lever) vs `throws` (déclarer), **propagation** le long de la pile d'appels, capter le bon type (du plus précis au plus général) |
| `5-3-try-with-resources.md` | `5-3-try-with-resources` | 3 | interface `AutoCloseable` / `Closeable`, **fermeture déterministe** (le `close()` automatique remplace le `finally` manuel), ressources multiples, ordre de fermeture inverse, intro aux *suppressed exceptions* (mention) |
| `5-4-exceptions-personnalisees.md` | `5-4-exceptions-personnalisees` | 4 | **quand** créer une exception métier, **comment** (`extends RuntimeException` — choix module, cf. §10.1), constructeurs (`message`, `message + cause`), **chaînage** (`cause`, `getCause()`), conventions de nommage (`…Exception`) |
| `5-5-io-classique.md` | `5-5-io-classique` | 5 | `File`, `FileReader`/`FileWriter`, `BufferedReader` (`readLine`) / `PrintWriter` (`println`), **`IOException` (checked !)** rattrapée ou déclarée, fermeture via `try-with-resources` (ch.3), encodage explicite |
| `5-6-nio2.md` | `5-6-nio2` | 6 | `Path` / `Paths.get`, `Files` : `readAllLines`, `lines` (Stream + `try-with-resources`), `writeString`, `readString`, `write`, `exists`/`copy`, **UTF-8 explicite** (`StandardCharsets.UTF_8`) ; NIO.2 comme API moderne à privilégier |
| `5-7-formats-texte.md` | `5-7-formats-texte` | 7 | **CSV simple** (séparateur, `String.split`/`String.join`, limites du split naïf), fichiers **`.properties`** (`java.util.Properties`, `load`/`getProperty`), **introduction à JSON via une lib** (mention, schéma de principe, **sans exo ni dépendance**) |

**Config** : `_category_.json` **existe déjà** dans `courses/docs/module-5-exceptions-io/` (label « Module 5 — Exceptions et I/O », `position: 6`, `link.type: generated-index`). **Unique retouche** : passer `collapsed: true` → `false` pour l'homogénéité avec les modules 1-4.

**Retouche de liaison** : le bloc « Prochain chapitre » de `4-8-streams-optional.md` (module 4) affiche actuellement `→ **Module 5 — Exceptions et I/O** *(à venir)*` → le faire pointer vers `5-1-hierarchie-exceptions` (lien relatif inter-modules : `../module-5-exceptions-io/5-1-hierarchie-exceptions`). Le chapitre `5-7` pointe vers « Module 6 — Tests et Git *(à venir)* » (le module 6 n'existe pas encore).

## 3. Structure imposée (chaque chapitre)

Ordre strict de la charte §6 (identique aux modules 1 à 4) :

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

**Longueur cible** : 800–2000 mots par chapitre (les modules 1-4 tournaient à ~1 200). Les chapitres `5-6` (NIO.2, beaucoup d'API) et `5-7` (formats) sont les plus denses — viser le haut de la fourchette, quitte à reporter une API avancée vers « Pour aller plus loin ».

## 4. Conventions transverses (décisions validées)

- **Exemples indépendants par chapitre** : chaque chapitre prend l'exemple le plus clair pour SA notion, sans fil conducteur imposé. Domaines des exercices guidés choisis **neutres et distincts des exos notés** (anti-spoil, cf. §5.4). Touche militaire seulement quand elle clarifie (charte §4), jamais forcée.
- **Ton / langue** : vouvoiement, phrases courtes, voix active (charte §2). Règle des trois fois pour le vocabulaire technique (charte §3). Acronymes développés à la première occurrence (I/O = entrées/sorties ; NIO = *New I/O* ; CSV = *Comma-Separated Values* ; UTF-8 explicité).
- **Code** (charte §7) : identifiants Java en anglais, métier en français quand plus clair ; commentaires français expliquant le *pourquoi* ; lisibilité avant astuce ; **imports explicites** (jamais `import java.util.*` ni `java.io.*` / `java.nio.file.*` en étoile) ; **encodage UTF-8 explicite** partout où une API le permet (`StandardCharsets.UTF_8`) ; pas de variable d'une lettre hors `i/j/k` et `e` (utilisé conventionnellement pour la variable d'exception dans un `catch`).
- **Sandbox des exemples I/O** : dans les chapitres, les exemples qui écrivent un fichier le font dans un chemin **clairement temporaire / illustratif** et **ne dépendent pas** d'un fichier présent sur le disque du lecteur (le code est lisible, pas forcément exécuté tel quel). Les vrais tests isolés (`@TempDir`) sont l'affaire des exos (cf. §10.3).
- **Liens « Pour aller plus loin »** standardisés sur les sources du référentiel §2 : **dev.java**, **Baeldung**, **Javadoc OpenJDK 25** (`docs.oracle.com/en/java/javase/25/...`). 1 à 3 liens annotés par chapitre.
- **Markdown** (charte §9) : un seul `#`, pas de saut de niveau de titre, blocs de code toujours typés (` ```java `, ` ```text `, ` ```properties `).

## 5. Cohérence pédagogique (garde-fous)

### 5.1 Règle d'antériorité — externe (modules précédents)
Tout l'acquis des modules 1-4 est disponible : variables, contrôle de flux, tableaux, chaînes (`split`, `join`, `String.format`), méthodes `static`, récursivité, POO complète (classes, encapsulation, héritage, polymorphisme, interfaces, enums, records, `sealed`), collections (`List`/`Set`/`Map`/`Deque`), génériques, `Comparable`/`Comparator`, lambdas, interfaces fonctionnelles, streams, `Optional`. Aucune notion d'un module **ultérieur** n'apparaît : pas de **tests JUnit écrits par le stagiaire** ni de **Git avancé** (module 6).

**Renversement assumé du verrou « pas d'exceptions »** : contrairement aux modules 1-4 (où la moindre levée d'exception était proscrite), les exceptions sont **le sujet** de ce module et apparaissent partout dès le chapitre 1. C'est cohérent et voulu.

### 5.2 Règle d'antériorité — **interne au module 5** (garde-fou central)
L'ordre des chapitres suit l'ordre des notions ; chaque chapitre n'emploie que ce qui précède :

- **5-1 hiérarchie** pose le vocabulaire (`Throwable`/`Error`/`Exception`/`RuntimeException`, checked vs unchecked) sans encore demander d'écrire un `try`/`catch` complet — on **observe** des exceptions levées par le JDK (parse, division, indice hors bornes) pour cartographier la hiérarchie.
- **5-2 `try`/`catch`/`finally`** apprend à **rattraper et relancer** (multi-catch, `throw`/`throws`, propagation) — s'appuie sur la hiérarchie (5-1) pour capter du plus précis au plus général.
- **5-3 `try-with-resources`** présente la **fermeture déterministe** comme une amélioration du `finally` (5-2) — prérequis des chapitres I/O (un flux / un `Stream` `Files.lines` doit être fermé).
- **5-4 exceptions personnalisées** combine `throw` (5-2) et la hiérarchie (5-1) pour créer un type métier (`extends RuntimeException`), avec chaînage de `cause`.
- **5-5 I/O classique** est le **premier contact fichier** : `BufferedReader`/`PrintWriter` fermés via `try-with-resources` (5-3), `IOException` rattrapée/déclarée (5-2) — première exception **checked** vraiment manipulée.
- **5-6 NIO.2** modernise le 5-5 (`Path`/`Files`), réutilise `try-with-resources` pour `Files.lines` et mobilise les streams (M4) sur le contenu d'un fichier.
- **5-7 formats texte** vient **en dernier** : il lit/écrit des fichiers (5-5/5-6), découpe des chaînes (M2), trie via `Comparator` (M4) et lève des exceptions sur format invalide (5-2/5-4).

### 5.3 Mapping antériorité exos → chapitres (borne les futurs exos)
Les chapitres précèdent les exos qui mobilisent leurs notions. Un exo n'utilise jamais une notion vue **plus tard** :

| Sous-groupe d'exos | Chapitres requis (préalables) |
|---|---|
| 5.1 Exceptions | ch. 1-4 |
| 5.2 Lecture/écriture de fichiers | ch. 3, 5-6 |
| 5.3 Parsing et formats | ch. 6-7 |

⇒ Les chapitres d'abord (respecté par ce cycle).

**⚠️ Subtilité repérée — l'exo `.properties` (5.2.4) straddle ch.6/ch.7.** Le brief range « lecture d'un fichier de config `.properties` » dans le sous-groupe **5.2** (mappé ch.5-6), mais l'API `java.util.Properties` est enseignée au **ch.7** (formats). Résolution retenue (à figer au cycle 5.2) : **5.2.4 lit un fichier de configuration `clé=valeur` à la main** (NIO.2 `readAllLines` + `split('=')`, du ressort du ch.6), et `java.util.Properties` *proprement dit* reste un sujet **ch.7 / sous-groupe 5.3**. Aucune notion future n'est ainsi mobilisée par 5.2.4. *(Rappel : la cadence « chapitres d'abord » publie de toute façon les 7 chapitres avant le premier exo ; le mapping ci-dessus est le garde-fou de **pacing pédagogique**, pas une contrainte de disponibilité.)*

### 5.4 Exercices guidés distincts des exos secs (cycles ultérieurs)
Comme aux modules 1-4 : **l'exercice guidé d'un chapitre ne donne jamais la solution d'un exo noté** des futurs sous-groupes. Il traite une variante proche servant d'échauffement, sur un **domaine neutre**. Mapping des exos à NE PAS spoiler, par chapitre :

| Chapitre | Exos adossés (à NE PAS spoiler) | Domaine neutre retenu pour le guidé |
|---|---|---|
| 5-1 hiérarchie | 5.1.1 saisie défensive | **Cartographier la hiérarchie** : provoquer puis identifier 3 exceptions JDK distinctes (`NumberFormatException`, `ArithmeticException`, `ArrayIndexOutOfBoundsException`) — observation, pas de gestion défensive métier. |
| 5-2 `try`/`catch`/`finally` | 5.1.1 saisie défensive, 5.1.3 calculs géométriques | Division entière protégée contre le `/0` + `finally` qui « libère » — arithmétique neutre, ni saisie validée, ni géométrie. |
| 5-3 `try-with-resources` | tous les exos I/O (5.2/5.3) | Une **ressource maison** `Chronometre implements AutoCloseable` qui trace ouverture/fermeture — **pas encore de fichier**. |
| 5-4 exceptions perso | 5.1.2 `EffectifInvalideException`, 5.1.3 géométrie | `SoldeInsuffisantException` sur un retrait bancaire — ni effectif d'unité, ni dimension géométrique. |
| 5-5 I/O classique | 5.2.1 compteur, 5.2.2 copie, 5.2.3 journal, 5.2.4 config | Écrire puis relire une courte note multi-lignes (liste de capitales) — ni comptage, ni copie, ni append, ni config. |
| 5-6 NIO.2 | 5.2.* (compteur/copie/journal/config), 5.3.* | Écrire une liste de fruits (`writeString`) puis la **transformer en majuscules** via `Files.lines` (`try-with-resources`) — transformation, pas comptage/copie/append. |
| 5-7 formats | 5.3.1 import CSV personnel, 5.3.2 export trié, 5.3.3 parseur, 5.2.4 config | Lire un petit CSV de **livres** (`titre,annee`) + un `app.properties` (`langue=fr`) — domaine « bibliothèque », ni personnel, ni commandes. |

## 6. Plan de contenu par chapitre (cible de rédaction)

Indicatif — le rédacteur ajuste tant que la charte §6 et les garde-fous §5 sont respectés.

- **5-1 Hiérarchie des exceptions** — pourquoi les exceptions existent (signaler une anomalie sans valeur de retour magique — **le pont avec les sentinelles/`Optional` des modules 1-4**) ; l'arbre `Throwable` → `Error` (ne pas rattraper) / `Exception` → `RuntimeException` ; **checked vs unchecked** : la checked **force** `throws`/`catch` (vérifiée par le compilateur), l'unchecked non ; **lire une stack trace** (type, message, ligne, pile) ; distinguer erreur de **compilation** et exception à **l'exécution**. Erreurs fréquentes : confondre `Error` (problème système, on ne rattrape pas) et `Exception` ; croire qu'une `RuntimeException` doit être déclarée ; ignorer le message de la stack trace. Domaine guidé : provoquer/identifier 3 exceptions JDK.
- **5-2 `try` / `catch` / `finally`** — structure `try`/`catch` ; capter **du plus précis au plus général** (ordre des `catch`) ; **multi-catch** (`catch (A \| B e)`) ; `finally` **toujours** exécuté (même sur `return`) ; `throw` (lever une instance) vs `throws` (déclarer dans la signature) ; **propagation** : une exception non rattrapée remonte la pile d'appels jusqu'au `main`. Erreurs fréquentes : `catch (Exception e)` avant `catch (IOException e)` (code mort, ne compile pas) ; avaler l'exception (`catch` vide) ; confondre `throw` et `throws`. Domaine guidé : division protégée + `finally`.
- **5-3 `try-with-resources`** — le problème du `finally` manuel (verbeux, oubli de `close()`, exception masquée) ; l'interface `AutoCloseable` (`close()`) et `Closeable` ; syntaxe `try (Ressource r = …) { … }` → `close()` **automatique et déterministe** ; **plusieurs ressources** (fermées dans l'ordre inverse d'ouverture) ; mention des *suppressed exceptions* (sans détailler). Erreurs fréquentes : déclarer la ressource hors du `try` (pas de fermeture auto) ; oublier qu'une classe doit implémenter `AutoCloseable` pour en bénéficier ; croire que `try-with-resources` rattrape l'exception (il ferme, il ne capte pas). Domaine guidé : `Chronometre implements AutoCloseable`.
- **5-4 Exceptions personnalisées** — **quand** : exprimer une règle métier violée plus clairement qu'une exception générique ; **comment** : `class EffectifInvalideException extends RuntimeException` (choix module §10.1 — unchecked pour ne pas alourdir les signatures débutant ; mentionner qu'`extends Exception` la rendrait checked) ; constructeurs standard (`(String message)`, `(String message, Throwable cause)`) ; **chaînage** (`cause`) : encapsuler une exception de bas niveau dans une exception métier sans perdre l'origine (`getCause()`). Erreurs fréquentes : créer une exception là où un `IllegalArgumentException` du JDK suffirait ; perdre la `cause` (relancer sans la passer) ; nommer sans suffixe `Exception`. Domaine guidé : `SoldeInsuffisantException`.
- **5-5 I/O classique** — modèle de **flux** (ouvrir → lire/écrire → fermer) ; `File` (un chemin) ; lecture ligne à ligne avec `BufferedReader.readLine()` (boucle jusqu'à `null`) ; écriture avec `PrintWriter.println` ; **`IOException` est checked** → la rattraper ou la déclarer (pont avec 5-2) ; **fermeture via `try-with-resources`** (pont avec 5-3) ; encodage explicite (`new InputStreamReader(…, StandardCharsets.UTF_8)` ou équivalent) — annoncer que NIO.2 (ch.6) simplifie tout ça. Erreurs fréquentes : oublier de fermer le flux (fuite — d'où `try-with-resources`) ; lire avec le mauvais encodage (accents cassés) ; boucle de lecture qui ne teste pas `null`. Domaine guidé : écrire/relire une note de capitales.
- **5-6 NIO.2** — `Path` / `Paths.get(...)` (vs l'ancien `File`) ; `Files` : **`readAllLines`** (petit fichier → `List<String>`), **`lines`** (gros fichier → `Stream<String>` **à fermer**, `try-with-resources`), **`writeString`** / **`readString`** / **`write`** (lignes), **`exists`** / **`copy`** ; **UTF-8 explicite** (`StandardCharsets.UTF_8`) ; combiner avec les streams M4 (`Files.lines(...).filter(...).count()` reste un exemple de cours, pas l'exo). Annoncer : **les exos I/O imposent NIO.2** (§10.2). Erreurs fréquentes : `Files.lines` sans `try-with-resources` (`Stream` non fermé) ; charger un fichier énorme avec `readAllLines` (tout en mémoire) ; oublier l'encodage (défaut plateforme ≠ UTF-8 partout). Domaine guidé : liste de fruits → majuscules via `Files.lines`.
- **5-7 Formats texte** — **CSV** : structure (lignes, séparateur), écrire avec `String.join(",", …)`, lire avec `String.split(",")` ; **limites du split naïf** (virgule dans un champ, guillemets) annoncées honnêtement (on reste sur du CSV simple sans champ échappé) ; **`.properties`** : `java.util.Properties` (`load(Reader)`, `getProperty(clé, défaut)`) ; **JSON** : **mention seulement** — à quoi ça ressemble, qu'on utilise une **lib** (Jackson/Gson) en vrai, **pas d'exo, pas de dépendance** dans la Piscine (cohérent offline). Erreurs fréquentes : `split(",")` sur une ligne contenant une virgule dans un champ ; oublier l'en-tête CSV ; lire un `.properties` avec le mauvais encodage. Domaine guidé : CSV de livres + `app.properties`.

## 7. Vérification

- **Bloquant (CI #11a, job `build-docusaurus`)** : `cd courses && npm run build` doit passer. `onBrokenLinks: 'throw'` casse au moindre lien interne mort → les liens « Prochain chapitre », la retouche de `4-8-streams-optional` (→ `5-1`) et les ancres doivent être exacts.
- **Validation locale** : build Docusaurus lancé en local avant push (`cd courses && npm run build`).
- **Compilation des extraits (recommandée — a attrapé de vrais bugs en M4)** : les chapitres ne passent pas par `valider-solutions`, mais Java 25 est installé localement et le module 5 contient du **code exécutable non trivial** (multi-catch, `try-with-resources` à ressources multiples, `Files.lines` + stream, parsing CSV/properties). Compiler à la main les extraits les plus denses (ch. 3, 6, 7) lève les doutes de syntaxe avant relecture.
- **Relecture humaine** (charte §10) : structure respectée, niveau de langue adapté débutant, jargon expliqué (anglicismes `stream`/`buffer`/`parser` annotés à la 1re occurrence), cohérence avec le référentiel, renversement du verrou « exceptions » assumé et expliqué.

## 8. Workflow d'exécution

- **1 seule PR** (découpage validé — choix formateur) : « #21 — chapitres module 5 (hiérarchie/`try-catch`/`try-with-resources`/exceptions perso + I/O classique/NIO.2/formats) », branche `feature/m5-chapitres` issue de `main`, + retouche liaison `4-8-streams-optional` → `5-1` + bascule `_category_.json` (`collapsed: false`).
- **Cadence** : rédaction des 7 chapitres, **commits par chapitre** pour garder l'historique lisible ; build Docusaurus à chaque étape ; **relecture globale** par le formateur en fin de PR. Exécution **inline ou en workflow ultracode** selon la demande du formateur (rédaction ≤6 agents Sonnet → revue adversariale structure/antériorité/anti-spoil + compilation des extraits → build Docusaurus).
- PR relue par un autre formateur → merge une fois le CI vert (+ build local).
- Clôture : mettre à jour [`backlog.md`](../../backlog.md) (#21, avancement du volet chapitres) avec lien commit.

## 9. Critères d'acceptation

- [ ] Les 7 chapitres existent et respectent la structure charte §6.
- [ ] `_category_.json` basculé en `collapsed: false` ; liaison module 4 → module 5 corrigée dans `4-8-streams-optional` (→ `5-1`) ; le `5-7` pointe vers « Module 6 *(à venir)* ».
- [ ] `npm run build` passe (Docusaurus, liens internes valides).
- [ ] Chaque chapitre a son « exercice guidé » (solution dans `<details>`) et ses « vérifiez vos acquis ».
- [ ] Garde-fou interne respecté : ordre des notions tenu (hiérarchie 5-1 avant `catch` 5-2 ; `finally` 5-2 avant `try-with-resources` 5-3 ; `try-with-resources` 5-3 avant les I/O 5-5/5-6 ; I/O 5-5/5-6 avant formats 5-7) ; aucune notion de module ultérieur (JUnit écrit par le stagiaire, Git avancé).
- [ ] Le **renversement du verrou « pas d'exceptions »** est présent et explicité (pont avec les sentinelles/`Optional` des modules 1-4, ch. 5-1).
- [ ] Aucun exercice guidé ne donne la solution d'un exo noté des futurs sous-groupes (mapping §5.4).
- [ ] La **subtilité `.properties` (5.2.4 ↔ ch.7)** est tranchée et consignée (§5.3 / §10.4).

## 10. Annexe — décisions transverses module 5 (héritées par le volet exercices)

Tranchées au brainstorming du 2026-06-04 (et validées par le formateur), consignées ici pour les cycles d'exercices à venir (aucun livrable dans ce cycle). Les points marqués **[formateur]** ont été explicitement arbitrés ; les points marqués **[raffinement]** sont des ajustements de conception (à confirmer à la relecture de cette spec ou au cycle d'exo concerné).

### 10.1 Exceptions custom = unchecked (`extends RuntimeException`) **[formateur]**
`EffectifInvalideException` (5.1.2) et l'exception du refactor géométrique (5.1.3) **héritent de `RuntimeException`** : pas de `throws` imposé, signatures légères, plus doux pour des débutants. Les **deux familles** (checked / unchecked) restent **enseignées au ch. 5-1** ; le checked se rencontre concrètement via **`IOException`** dès le ch. 5-5 (et dans les exos 5.2/5.3). Pour une garde simple sans type métier dédié, `IllegalArgumentException` (du JDK, unchecked) reste recommandé (5.1.1, et option de 5.1.3 si pas de type custom).

### 10.2 I/O = NIO.2 imposé dans les exos **[formateur]**
Les exos 5.2/5.3 utilisent **`Path` / `Files`** (`readAllLines`, `lines`, `writeString`, `write`, `copy`). L'I/O classique (`BufferedReader`/`PrintWriter`) est **montrée en cours** (ch. 5-5) mais **non exigée** dans les exos. Les tests asserteront le **contenu** du fichier (cf. §10.3), **indépendamment** de l'API choisie par le stagiaire — la consigne « NIO.2 » est portée par le `sujet.md` et le critère formateur `idiomatisme`, pas par de la réflexion.

### 10.3 Pattern de test I/O — `@TempDir` **[validé]**
Test par **comportement observable**, **déterministe et isolé** :
- Chaque test écrit/lit ses fichiers dans le **`@TempDir`** de JUnit Jupiter (dossier temporaire fourni au test, nettoyé automatiquement — **aucun fichier hors sandbox**). **Déjà disponible, aucune nouvelle dépendance.**
- **Encodage UTF-8 explicite** (`StandardCharsets.UTF_8`) côté test comme côté solution.
- **Fins de ligne `\n` explicites** dans les fixtures et les assertions (portabilité Windows/Linux — ne pas dépendre de `System.lineSeparator()`).
- Asserter le **contenu** (`Files.readString` / `readAllLines` → AssertJ `contains` / `containsExactly` / `isEqualTo`), jamais l'API employée pour le produire.

### 10.4 Formats = CSV + `.properties` à la main, pas d'exo JSON **[formateur]**
Parsing **maison** : CSV via `String.split` / `String.join` (CSV **simple**, sans champ échappé) ; `.properties` via `java.util.Properties` (au ch. 7 / sous-groupe 5.3) — **zéro lib tierce** (cohérent offline / souveraineté). **JSON** : **mention en cours uniquement** (ch. 5-7), **aucun exo, aucune dépendance**. **Subtilité `.properties` (cf. §5.3)** : l'exo 5.2.4 lit un config `clé=valeur` **à la main** (NIO.2 + `split('=')`, ressort du ch. 6) ; `java.util.Properties` est réservé au ch. 7 / 5.3 — à figer au cycle 5.2.

### 10.5 Test des exceptions — par le comportement **[validé]**
Tester une exception = **`assertThatThrownBy(() -> …).isInstanceOf(XxxException.class).hasMessageContaining("…")`** (AssertJ, **déjà disponible**), jamais de réflexion ni d'inspection de signature. Périmètre gradué (calqué sur le pattern M4 §10.2) :
- **Public** (notions enseignées) : l'exception attendue est bien levée sur entrée invalide ; le **type** est correct ; le **message** contient l'information clé ; le cas valide **ne lève pas**.
- **Privé** (robustesse) : bornes exactes (limite valide vs première valeur invalide) ; pour le chaînage, `getCause()` non nul quand pertinent ; multi-cas d'entrées invalides.
- **Exclu** (garde-fou débutant) : hiérarchies d'exceptions custom complexes, *suppressed exceptions*, dépendance au texte exact d'un message JDK (on teste *notre* message, `hasMessageContaining` et non `isEqualTo`).

### 10.6 Modèle de domaine local au module 5 — `etnc.m5` **[raffinement]**
**Pas de réutilisation/import des classes M2/M3/M4** (packages distincts, aucun mécanisme de dépendance inter-modules, le ZIP standalone copie exo par exo). Modèles **fournis complets** (« FOURNI — ne pas modifier ») dans les starters des exos concernés, package `etnc.m5` :
- **5.1.2** : un type métier minimal portant la règle d'effectif (ex. `Unite` avec un `effectif` ∈ `[0, capaciteMax]`) + `EffectifInvalideException extends RuntimeException`.
- **5.1.3** : recopie locale `etnc.m5` de `CalculsGeometriques` (le code de 2.3.3), signatures `aire`/`perimetre` inchangées, **corps à compléter** pour lever sur dimension négative.
- **5.3.1 / 5.3.2** (CSV personnel) : recommandation `enum Grade` (réemploi du concept M3/M4, local) + `record Personnel(String nom, Grade grade, int anciennete)`. L'import CSV qui rencontre un grade inconnu **lève une exception** (joli pont formats ↔ exceptions). Modèle exact à confirmer au cycle 5.3. Le tri de l'export (5.3.2) réutilise `Comparator` (M4).

### 10.7 Barème `evaluation.yml` **[validé]**
Calqué M3/M4 : **tests-publics 8 + tests-prives 8 + `style` 2** (automatique Checkstyle/PMD, `id: style`) **+ 1 critère formateur poids 2** (`demarche` / `lisibilite` / `idiomatisme` / `respect-consignes` selon l'exo) → **total 20, seuil 12**. Format exercice : **9–11 fichiers** (`docs/format-exercice.md`), package **`etnc.m5`**, groupId **`etnc.piscine.m5`**, dossier module **`module-5-exceptions-io`** (slug exact à vérifier en créant le premier exo).

### 10.8 Roster des 10 exercices (3 sous-groupes) — esquisse
Détaillé (signatures, fixtures, critère par exo) **au cycle de chaque sous-groupe**. Vérification d'antériorité ci-dessous.

**5.1 Exceptions** (→ ch. 1-4) :
- **5.1.1 `saisie-defensive`** — convertir/valider une donnée (ex. un entier dans une plage) ; lever `IllegalArgumentException` (ou rattraper `NumberFormatException`) sur entrée invalide ; tester via `assertThatThrownBy`. Critère : `respect-consignes` ou `idiomatisme`.
- **5.1.2 `effectif-invalide`** — `EffectifInvalideException extends RuntimeException` ; type métier (`Unite`) qui lève sur effectif hors bornes. Critère : `demarche`.
- **5.1.3 `calculs-geometriques-exceptions`** — refactor de 2.3.3 (recopie `etnc.m5`) : lever sur dimension négative (rayon / largeur / hauteur < 0). Critère : `idiomatisme`.

**5.2 Lecture/écriture de fichiers** (→ ch. 3, 5-6 ; NIO.2 imposé) :
- **5.2.1 `compteur-lignes`** — compter les lignes (non vides ?) d'un fichier (`Files.lines` + `try-with-resources`, ou `readAllLines`). Critère : `idiomatisme`.
- **5.2.2 `copie-fichier`** — copier un fichier (lire puis écrire, ou `Files.copy`) en préservant le contenu/encodage. Critère : `respect-consignes`.
- **5.2.3 `journal-evenements`** — journal **append-only** (`Files.write(..., StandardOpenOption.APPEND/CREATE)`), chaque appel ajoute une ligne horodatée-fictive. Critère : `demarche`.
- **5.2.4 `lecture-config`** — lire un config `clé=valeur` **à la main** (NIO.2 + `split('=')`, cf. §5.3 / §10.4) ; valeur par défaut si clé absente. Critère : `idiomatisme`.

**5.3 Parsing et formats** (→ ch. 6-7) :
- **5.3.1 `import-csv-personnel`** — parser un CSV en `List<Personnel>` ; lever une exception sur ligne/grade invalide (pont formats ↔ exceptions). Critère : `demarche`.
- **5.3.2 `export-csv-trie`** — trier (`Comparator`, M4) puis écrire un CSV (`String.join` + `Files.write`/`writeString`). Critère : `idiomatisme`.
- **5.3.3 `parseur-commandes`** — mini-parseur de commandes texte (`split`, dispatch) ; lève sur commande inconnue / arguments manquants. Critère : `idiomatisme`.

**Vérification d'antériorité** : 5.1 → ch.1-4 (exceptions seules, aucune I/O) ✓ ; 5.2 → ch.3 (`try-with-resources`) + 5-6 (NIO.2), **5.2.4 sans `java.util.Properties`** (parse manuel) pour rester ch.6 ✓ ; 5.3 → ch.6-7 (CSV/properties + I/O), tri via `Comparator` (M4, dispo) ✓. **Aucun risque bloquant** une fois la subtilité §5.3 tranchée.

### 10.9 Projet binôme #2
**Débloqué par le module 5** (exceptions + persistance fichier) : sa fiche (`exercises/projets-binome/projet-2-…`) sera rédigée dans un **cycle dédié après le module 5**, comme le binôme #1 l'a été après le module 3. #21 ne le touche pas.
