# Spec — #19 Chapitres du module 3 (Programmation orientée objet)

> Design validé le 2026-06-03 (brainstorming). Tâche backlog **#19**.
> Cycle isolé : ce cycle ne couvre que les **10 chapitres**. Les 4 sous-groupes d'exercices (3.1 → 3.4) suivront dans leurs propres cycles spec / plan / PR (cadence : chapitres d'abord — les notions bornent ce que les exercices peuvent demander).
> Point de départ : [`docs/superpowers/2026-06-03-m3-kickoff.md`](../2026-06-03-m3-kickoff.md).
> Références cadre : [`charte-redaction.md`](../../charte-redaction.md) (§6 structure, §2/§3 ton/langue, §7 code, §10 relecture), [`referentiel.md`](../../referentiel.md) (§module 3), modèles existants [`module-1-fondamentaux/`](../../../courses/docs/module-1-fondamentaux/) et [`module-2-tableaux-chaines-methodes/`](../../../courses/docs/module-2-tableaux-chaines-methodes/), spec sœur [`2026-06-01-m2-chapitres-design.md`](2026-06-01-m2-chapitres-design.md).

## 1. Objectif et périmètre

Produire les **10 chapitres** du module 3 dans `courses/docs/module-3-poo/`, conformes à la charte §6, cohérents entre eux et avec le module 2 (qui se termine sur le chapitre 2-5 « récursivité »).

**Objectif pédagogique** (référentiel §module 3) : concevoir et utiliser des classes ; comprendre encapsulation, héritage, polymorphisme, interfaces ; découvrir enums, records & `sealed` (Java moderne).

**Hors périmètre** : tout fichier sous `exercises/` (les 14 exos = cycles ultérieurs), la moulinette, la grille d'évaluation, la fiche du **Projet binôme #1** (cycle dédié post-module 3). Aucun exercice noté n'est touché. Les **décisions transverses** prises au brainstorm (squelette `starter/`, vérification des notions POO, `equals`/`hashCode`, fil rouge) sont consignées en §10 pour que le futur volet exercices en hérite — elles ne produisent aucun livrable dans ce cycle.

## 2. Livrables

10 fichiers Markdown + 1 fichier de config (déjà présent, à vérifier) + 1 retouche de liaison :

| Fichier | `id` | `sidebar_position` | PR | Notions (réf. §module 3) |
|---|---|---|---|---|
| `3-1-classes-et-objets.md` | `3-1-classes-et-objets` | 1 | **A** | classe vs instance, attributs, méthodes, `new`, référence d'objet, `null` |
| `3-2-encapsulation.md` | `3-2-encapsulation` | 2 | A | `private`/`public`, getters/setters, invariants, pourquoi cacher l'état |
| `3-3-constructeurs.md` | `3-3-constructeurs` | 3 | A | constructeur par défaut, paramétré, surcharge, chaînage `this(...)` |
| `3-4-this-et-static.md` | `3-4-this-et-static` | 4 | A | `this`, membre d'instance vs de classe, variables/méthodes `static`, constantes `static final` |
| `3-5-heritage.md` | `3-5-heritage` | 5 | A | `extends`, `super`, `@Override`, `final`, `protected`, `Object` |
| `3-6-polymorphisme.md` | `3-6-polymorphisme` | 6 | **B** | typage statique vs dynamique, liaison dynamique, downcast, `instanceof` pattern (Java 21+) |
| `3-7-classes-abstraites.md` | `3-7-classes-abstraites` | 7 | B | `abstract`, méthodes abstraites vs concrètes, classe non instanciable |
| `3-8-interfaces.md` | `3-8-interfaces` | 8 | B | contrat, `implements`, implémentation multiple, méthodes `default`/`static` |
| `3-9-enums.md` | `3-9-enums` | 9 | B | type énuméré, constantes, méthodes sur enum, `switch` sur enum |
| `3-10-records-et-sealed.md` | `3-10-records-et-sealed` | 10 | B | record immuable, `sealed`/`permits`/`non-sealed`, pattern matching exhaustif |

**Config** : `_category_.json` **existe déjà** (`label` « Module 3 — Programmation Orientée Objet », `position: 4`, `collapsible: true`, `link.type: generated-index`). Vérifier qu'il reste cohérent ; aligner `collapsed` sur le module 2 (`false`) pour l'homogénéité de la sidebar. Aucune création nécessaire.

**Retouche de liaison** : le bloc « Prochain chapitre » de `2-5-recursivite.md` (module 2) pointe actuellement vers « Module 3 — Programmation orientée objet *(à venir)* » → le faire pointer vers `3-1-classes-et-objets`. Le chapitre 3-10 pointe vers « Module 4 — Collections, génériques, lambdas *(à venir)* ».

## 3. Structure imposée (chaque chapitre)

Ordre strict de la charte §6 (identique aux modules 1 et 2) :

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

**Longueur cible** : 800–2000 mots par chapitre (les modules 1-2 tournaient à ~1 200).

## 4. Conventions transverses (décisions validées)

- **Exemples indépendants par chapitre** : chaque chapitre prend l'exemple le plus clair pour SA notion, sans fil conducteur imposé. Touche militaire seulement quand elle clarifie (charte §4), jamais forcée. (Pas de fil rouge — cf. §10.4.)
- **Ton / langue** : vouvoiement, phrases courtes, voix active (charte §2). Règle des trois fois pour le vocabulaire technique (charte §3), tenue d'un chapitre à l'autre. Acronymes développés à la première occurrence (POO = programmation orientée objet).
- **Code** (charte §7) : identifiants Java en anglais, métier en français quand plus clair ; commentaires français expliquant le *pourquoi* ; lisibilité avant astuce ; imports explicites (pas de `import java.util.*`) ; pas de variable d'une lettre hors `i/j/k` et `e`.
- **Liens « Pour aller plus loin »** standardisés sur les sources du référentiel §2 : **dev.java**, **Baeldung**, **Javadoc OpenJDK 25** (`docs.oracle.com/en/java/javase/25/...`). 1 à 3 liens annotés par chapitre.
- **Markdown** (charte §9) : un seul `#`, pas de saut de niveau de titre, blocs de code toujours typés (` ```java `, ` ```text `, ` ```bash `).

## 5. Cohérence pédagogique (garde-fous)

### 5.1 Règle d'antériorité — externe (modules précédents)
Tout l'acquis des modules 1-2 est disponible (variables, opérateurs, conditions, boucles, tableaux, chaînes, méthodes `static`, récursivité). Aucune notion d'un module **ultérieur** n'apparaît : pas de collections génériques (`List`, `Map`, `ArrayList`), pas de lambdas/streams (module 4), pas d'exceptions ni d'IO (module 5). En particulier, l'écriture manuelle de `equals`/`hashCode` est **réservée au module 4** (cf. §10.3).

### 5.2 Règle d'antériorité — **interne au module 3** (garde-fou central)
L'ordre des chapitres suit l'ordre des notions ; chaque chapitre n'emploie que ce qui précède :

- **3-1 à 3-4** posent la classe (état, encapsulation, construction, `this`/`static`) sans héritage ni polymorphisme.
- **3-5 héritage** introduit `extends`/`super`/`@Override` ; **3-6 polymorphisme** en dépend strictement (liaison dynamique, `instanceof` pattern).
- **3-7 classes abstraites** s'appuie sur l'héritage (3-5) et le polymorphisme (3-6).
- **3-8 interfaces** vient après les classes abstraites (contraste contrat vs classe partielle).
- **3-9 enums** peut s'appuyer sur méthodes/constructeurs (3-3/3-4) et, pour les enums « riches », sur l'idée d'interface (3-8).
- **3-10 records et `sealed`** reste **strictement en dernier** : le pattern matching exhaustif s'appuie sur `instanceof` pattern (3-6) et les `sealed` ferment une hiérarchie (héritage 3-5, interfaces 3-8).

### 5.3 Mapping antériorité exos → chapitres (borne les futurs exos)
Les chapitres précèdent les exos qui mobilisent leurs notions. Un exo n'utilise jamais une notion vue **plus tard** :

| Sous-groupe d'exos | Chapitres requis (préalables) |
|---|---|
| 3.1 Classes et objets | ch. 1-4 |
| 3.2 Encapsulation et constructeurs | ch. 2-4 |
| 3.3 Héritage et polymorphisme | ch. 5-7 |
| 3.4 Interfaces, enums, records | ch. 8-10 |

⇒ Les chapitres d'abord (respecté par ce cycle). Cette colonne n'est pas un livrable ici : elle cadre les cycles d'exercices.

### 5.4 Exercices guidés distincts des exos secs (cycles ultérieurs)
Comme aux modules 1-2 : **l'exercice guidé d'un chapitre ne donne jamais la solution d'un exo noté** des futurs sous-groupes. Il traite une variante proche servant d'échauffement. Mapping des exos à NE PAS spoiler, par chapitre :

| Chapitre | Exos adossés (à NE PAS spoiler) | Concept retenu pour le guidé |
|---|---|---|
| 3-1 Classes et objets | 3.1.1 `Point2D`, 3.1.2 `Compte`, 3.1.3 `Soldat` | Une classe `Livre` (titre, pages) avec une méthode d'affichage — ni point, ni compte, ni soldat. |
| 3-2 Encapsulation | 3.2.x invariants `Rectangle`/validation/`Date` | Une `Temperature` encapsulée avec borne minimale (zéro absolu) — pas de rectangle ni de date. |
| 3-3 Constructeurs | 3.2.x (validation au constructeur) | Surcharge de constructeurs sur une classe `Couleur` (RGB / niveau de gris) + chaînage `this(...)`. |
| 3-4 `this`/`static` | 3.1.x, 3.2.x | Compteur d'instances `static` sur une classe `Ticket` (id auto-incrémenté) — domaine neutre. |
| 3-5 Héritage | 3.3.1 `Vehicule`, 3.3.3 `Personnel` | Hiérarchie `Animal` → `Chien`/`Chat` avec `@Override` — ni véhicule, ni personnel militaire. |
| 3-6 Polymorphisme | 3.3.2 `Forme`, 3.3.4 `decrire()` polymorphe | Tableau polymorphe d'`Animal` et appel uniforme de `crier()` — pas de `Forme` géométrique ni `decrire()`. |
| 3-7 Classes abstraites | 3.3.2 `Forme` (souvent abstraite) | Classe abstraite `Paiement` avec `montant()` abstrait — éviter la `Forme` géométrique des exos. |
| 3-8 Interfaces | 3.4.1 `Comparable` custom | Interface `Sonore` implémentée par deux classes sans lien d'héritage — pas de `Comparable`. |
| 3-9 Enums | 3.4.2 enum `Grade` | Enum `JourSemaine` avec une méthode (`estWeekend()`) et `switch` — pas de `Grade` militaire. |
| 3-10 Records / `sealed` | 3.4.3 `record Coordonnees`, 3.4.4 `sealed` événements | `record Point(int x, int y)` + hiérarchie `sealed Figure permits Cercle, Carre` avec pattern matching — éviter `Coordonnees` et la hiérarchie d'événements des exos. |

## 6. Plan de contenu par chapitre (cible de rédaction)

Indicatif — le rédacteur ajuste tant que la charte §6 et les garde-fous §5 sont respectés.

- **3-1 Classes et objets** — qu'est-ce qu'une classe (un type qu'on définit : un plan), une instance (un objet créé à partir du plan) ; attributs (état) et méthodes (comportement) ; `new` et la **référence** d'objet (variable = poignée vers l'objet, pas l'objet lui-même) ; `null` et l'`NullPointerException` annoncée ; un objet par défaut a ses attributs initialisés (`0`/`false`/`null`). Pas encore d'encapsulation (attributs `public` tolérés ici, corrigés au 3-2). Erreurs fréquentes : confondre classe et instance, `NullPointerException` sur référence non initialisée, croire que `b = a` copie l'objet (alias). Exemple : un `Livre` ou un `Point` simple.
- **3-2 Encapsulation** — pourquoi cacher l'état (`private`) ; getters/setters ; **invariant** (propriété toujours vraie d'un objet valide) protégé par les setters ; contraste avec les attributs `public` du 3-1 (« on ferme la boîte »). Erreurs fréquentes : tout laisser `public`, setter sans validation, getter qui expose une référence interne modifiable. Exemple : `CompteBancaire` dont le solde ne se modifie que par `deposer`/`retirer`, ou `Temperature` bornée.
- **3-3 Constructeurs** — rôle (garantir un objet valide dès la création) ; constructeur par défaut implicite et sa disparition dès qu'on en écrit un ; constructeur paramétré ; **surcharge** de constructeurs ; chaînage `this(...)` pour factoriser. Erreurs fréquentes : oublier que le défaut disparaît, dupliquer la logique entre constructeurs au lieu de `this(...)`, confondre `this(...)` (constructeur) et `this.champ` (attribut). Exemple : `Couleur` (RGB / niveau de gris), `Rectangle`.
- **3-4 `this` et `static`** — `this` (la référence à l'objet courant : lever l'ambiguïté paramètre/attribut, se passer en argument) ; membre d'**instance** (un par objet) vs membre de **classe** (`static`, partagé) ; variable et méthode `static` ; `static final` pour les constantes ; quand `static` est justifié (utilitaire sans état, compteur partagé) et son piège (pas d'accès à `this`). Pont avec les méthodes `static` du module 2 (« maintenant on sait pourquoi `main` est `static` »). Erreurs fréquentes : appeler une méthode d'instance depuis un contexte `static`, abuser de `static` (état global), constante non `final`. Exemple : compteur d'instances `Ticket`, constante `Math.PI`-like.
- **3-5 Héritage** — `extends` (réutiliser et spécialiser) ; `super(...)` (appel du constructeur parent) et `super.methode()` ; `@Override` (et pourquoi l'annoter) ; `protected` ; `final` (classe/méthode non dérivable/redéfinissable) ; `Object` comme racine (toString). Préférer composition vs héritage évoqué brièvement. Erreurs fréquentes : oublier `super(...)` requis, redéfinir sans `@Override` (faute de frappe → surcharge accidentelle), héritage là où une composition conviendrait. Exemple : `Animal` → `Chien`/`Chat`, ou `Forme` → spécialisations.
- **3-6 Polymorphisme** — type **déclaré** (statique) vs type **réel** (dynamique) ; **liaison dynamique** (la méthode appelée dépend du type réel) ; traiter uniformément des objets via le type parent (tableau de `Animal`) ; `instanceof` **pattern** (Java 21+ : `if (a instanceof Chien c) c.aboyer();`) et downcast sûr. Dépend de 3-5. Erreurs fréquentes : downcast non vérifié (`ClassCastException`), croire que le type déclaré décide de la méthode, chaînes de `instanceof` là où le polymorphisme suffit. Exemple : tableau hétérogène d'`Animal` et appel uniforme `crier()`.
- **3-7 Classes abstraites** — `abstract` sur la classe (non instanciable) et sur des méthodes (sans corps, à implémenter) ; mélange méthodes abstraites / concrètes ; pourquoi une classe abstraite plutôt qu'une concrète (factoriser un comportement commun tout en forçant la spécialisation). Dépend de 3-5/3-6. Erreurs fréquentes : tenter `new` sur une abstraite, oublier d'implémenter une méthode abstraite dans la sous-classe, tout rendre abstrait. Exemple : `Paiement` abstrait avec `montant()` abstrait et `recu()` concret.
- **3-8 Interfaces** — un **contrat** sans implémentation ; `implements` (et implémentation **multiple**, contrairement à `extends`) ; méthodes `default` et `static` d'interface ; contraste classe abstraite (état + partiel) vs interface (contrat, multiple). Erreurs fréquentes : confondre `implements` et `extends`, oublier d'implémenter une méthode du contrat, mettre de l'état mutable dans une interface. Exemple : `Sonore`/`Affichable` implémentés par des classes sans lien.
- **3-9 Enums** — type énuméré (ensemble fini de constantes nommées) ; enum « riche » : attributs, constructeur, méthodes (`switch` ou méthode par constante) ; `switch` sur enum (et exhaustivité). Erreurs fréquentes : comparer des enums avec `equals` au lieu de `==` (les deux marchent, `==` idiomatique et null-safe), `switch` non exhaustif, réinventer un enum avec des `int`/`String`. Exemple : `JourSemaine` (`estWeekend()`), `Direction`.
- **3-10 Records et `sealed`** — **record** : porteur de données immuable (`record Point(int x, int y)`), `equals`/`hashCode`/`toString` **générés** (cf. §10.3 : on s'en sert, on ne les réécrit pas) ; **`sealed`** : fermer une hiérarchie (`sealed … permits …`, `non-sealed`/`final` sur les sous-types) ; **pattern matching** exhaustif d'un `switch` sur hiérarchie `sealed` (le compilateur vérifie l'exhaustivité, pas de `default` nécessaire). Dépend de 3-5/3-6/3-8. Erreurs fréquentes : tenter de muter un champ de record, oublier un type dans `permits`, ajouter un `default` qui masque la vérification d'exhaustivité. Exemple : `record Point` + `sealed Figure permits Cercle, Carre` avec aire calculée par pattern matching.

## 7. Vérification

- **Bloquant (CI #11a, job `build-docusaurus`)** : `cd courses && npm run build` doit passer. `onBrokenLinks: 'throw'` casse au moindre lien interne mort → les liens « Prochain chapitre », la retouche du 2-5 et les ancres doivent être exacts.
- **Validation locale** : le build Docusaurus est lancé en local avant push (`cd courses && npm run build`) — boucle plus rapide que le CI.
- **Non couvert automatiquement** : les extraits de code des chapitres ne sont **ni compilés ni testés** (les chapitres ne passent pas par `valider-solutions`, qui ne cible que les `solution/` d'exercices). La correction du code des exemples repose sur une **relecture manuelle attentive**, calée sur l'idiome déjà validé des modules 1-2. Java 25 étant installé localement, un extrait douteux (record, `sealed`, pattern matching — syntaxe récente) **peut** être compilé à la main pour lever un doute, sans que ce soit systématisé.
- **Relecture humaine** (charte §10) : structure respectée, niveau de langue adapté, pas de jargon militaire non expliqué, cohérence avec le référentiel.

## 8. Workflow d'exécution

- **2 PR** (découpage validé au brainstorm) :
  - **PR-A** « #19 — chapitres module 3 (1/2 : bases & héritage) » : ch. 3-1 à 3-5, branche `feature/m3-chapitres-1` issue de `main`, + retouche liaison `2-5-recursivite` → `3-1`.
  - **PR-B** « #19 — chapitres module 3 (2/2 : abstraction & types modernes) » : ch. 3-6 à 3-10, branche `feature/m3-chapitres-2` issue de `main` **après merge de PR-A**.
- **Cadence par PR** : rédaction des 5 chapitres, build Docusaurus, puis **relecture globale** par le formateur. Commits par chapitre pour garder l'historique lisible.
- Chaque PR relue par un autre formateur → merge une fois le CI vert (+ build local).
- Clôture : mettre à jour [`backlog.md`](../../backlog.md) (#19, avancement du volet chapitres) avec lien commit, à chaque PR.

## 9. Critères d'acceptation

- [ ] Les 10 chapitres existent et respectent la structure charte §6.
- [ ] `_category_.json` cohérent (position 4) ; liaison module 2 → module 3 corrigée dans `2-5-recursivite` ; le 3-10 pointe vers « Module 4 *(à venir)* ».
- [ ] `npm run build` passe (Docusaurus, liens internes valides).
- [ ] Chaque chapitre a son « exercice guidé » (solution dans `<details>`) et ses « vérifiez vos acquis ».
- [ ] Garde-fou interne respecté : ordre des notions tenu (héritage avant polymorphisme avant abstraites ; records/`sealed` en dernier) ; aucune notion de module ultérieur (collections, lambdas, exceptions, IO ; `equals`/`hashCode` manuel).
- [ ] Aucun exercice guidé ne donne la solution d'un exo noté des futurs sous-groupes (mapping §5.4).

## 10. Annexe — décisions transverses module 3 (héritées par le volet exercices)

Tranchées au même brainstorming, consignées ici pour les cycles d'exercices à venir (aucun livrable dans ce cycle) :

1. **Squelette `starter/` (signature imposée)** — **au cas par cas** : pour **3.1 et 3.2**, le `starter/` fournit les **signatures publiques complètes à corps vide** (classes, constructeurs, méthodes avec `return` par défaut / `TODO` + Javadoc des contrats) → on évalue la logique, pas le nommage, et les tests par API sont stables. Pour **3.3 et 3.4**, **squelette plus léger** (noms de classes + signatures-clés, le reste cadré par la consigne) car la **conception** (choisir la hiérarchie, l'interface, le record) fait partie de l'exercice.
2. **Vérifier les notions POO structurelles** — **compilation/API d'abord, réflexion en dernier recours**. Privilégier des tests qui dépendent de la propriété à la compilation (usage polymorphe via le type interface ; `switch` exhaustif **sans `default`** sur une hiérarchie `sealed`) ou par comportement (API). N'utiliser la **réflexion** (`Class.isRecord()`, `isSealed()`, `getPermittedSubclasses()`, `getInterfaces()`) que quand la propriété n'est **pas observable autrement**. Éviter les tests fragiles.
3. **`equals`/`hashCode`** — formellement traités au **module 4**. Pour le module 3 : les **records** génèrent `equals`/`hashCode` automatiquement → un test **peut** s'appuyer dessus (`assertThat(coord).isEqualTo(new Coordonnees(...))`), mais **aucun exo n'exige d'écrire `equals`/`hashCode` à la main** avant le module 4. La `Comparable` custom (3.4) porte sur **`compareTo` seul**.
4. **Fil rouge militaire** — **pas de fil rouge imposé**. Les exos sont **indépendants** (comme au module 2) ; chaque exo prend l'exemple le plus clair pour sa notion (touche militaire si elle aide). `Soldat`/`Personnel`/`Grade` restent des exemples ponctuels, pas une histoire couplée d'un exo à l'autre.
5. **Projet binôme #1** — sa fiche est rédigée **plus tard**, dans un **cycle dédié** une fois le module 3 terminé (quand le mini-domaine OO produit par les exos est stabilisé). #19 le débloque mais ne le rédige pas.
