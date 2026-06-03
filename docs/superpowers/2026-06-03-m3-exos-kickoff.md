# #19 — Module 3 (POO), volet EXERCICES : brief de démarrage

> **But de ce document** : permettre à une **nouvelle session** d'attaquer les 14 exercices du module 3 sans re-découvrir le contexte. Ce n'est PAS une spec ni un plan : c'est le point de départ du cycle habituel **brainstorm → spec → plan → exécution**, **un cycle par sous-groupe**, exécution **inline** (pas de sous-agents — contrainte projet, voir mémoire).
>
> Lire d'abord : [`docs/format-exercice.md`](../format-exercice.md), [`docs/charte-redaction.md`](../charte-redaction.md), [`docs/grille-evaluation.md`](../grille-evaluation.md), [`docs/referentiel.md`](../referentiel.md) §module 3, la **spec chapitres §10** [`specs/2026-06-03-m3-chapitres-design.md`](specs/2026-06-03-m3-chapitres-design.md) (décisions transverses figées), et les specs/plans `*-m2-exos-*` dans `specs/` et `plans/` (mêmes conventions).

## 0. Où on en est (au 2026-06-03)

- **Volet chapitres du module 3 TERMINÉ et mergé** : les **10 chapitres** (3-1 à 3-10) sont sur `main` sous `courses/docs/module-3-poo/` (PR #25 spec+plan, #26 ch.1-5, #27 ch.6-10). Les notions sont donc toutes posées → elles **bornent** ce que les exercices peuvent demander.
- **Volet exercices = PROCHAIN CHANTIER** : **14 exercices en 4 sous-groupes**, cible `exercises/module-3-poo/`. Débloque ensuite le **Projet binôme #1** (#23, « Caserne »).
- Brancher depuis `main` à jour. Conventions Git / revue : [`CONTRIBUTING.md`](../../CONTRIBUTING.md).

## 1. 🔑 Le point structurant à cadrer au brainstorm : on teste des OBJETS

Aux modules 1-2, la moulinette testait surtout la **sortie console** (`main` + E/S, util `CaptureSortie`/`CaptureEntree`). En POO, les exercices produisent des **classes** : les tests vont **instancier** (`new Soldat(...)`), **appeler des méthodes** et **asserter l'état / les valeurs de retour** avec AssertJ — beaucoup plus direct, sans capture de sortie dans la plupart des cas.

**Conséquence majeure pour la conception** : le `starter/` doit **imposer la signature publique** (noms de classes, constructeurs, méthodes) pour que les tests **compilent** et restent **stables**. C'est LE sujet du brainstorm de chaque sous-groupe.

Les **décisions transverses sont déjà figées** (spec chapitres §10), à appliquer tel quel :

1. **Squelette `starter/` au cas par cas** : pour **3.1 et 3.2**, signatures publiques **complètes à corps vide** (classes, constructeurs, méthodes avec `return` par défaut / `// TODO` + Javadoc des contrats) → on évalue la logique, pas le nommage, tests par API stables. Pour **3.3 et 3.4**, **squelette plus léger** (noms de classes + signatures-clés, le reste cadré par la consigne) car la **conception** (choisir la hiérarchie, l'interface, le record) fait partie de l'exercice.
2. **Vérifier les notions POO structurelles** : **compilation/API d'abord** (usage polymorphe via le type interface ; `switch` exhaustif **sans `default`** sur une hiérarchie `sealed` ; appel d'une méthode du contrat), **réflexion en dernier recours** (`Class.isRecord()`, `isSealed()`, `getPermittedSubclasses()`, `getInterfaces()`) seulement quand la propriété n'est **pas observable autrement**. Éviter les tests fragiles.
3. **`equals`/`hashCode`** : formellement au **module 4**. Ici, les **records** génèrent `equals`/`hashCode` automatiquement → un test **peut** s'appuyer dessus (`assertThat(coord).isEqualTo(new Coordonnees(...))`), mais **aucun exo n'exige d'écrire `equals`/`hashCode` à la main**. La `Comparable` custom (3.4) porte sur **`compareTo` seul**.
4. **Pas de fil rouge** : exos **indépendants** (comme M2). Chaque exo prend l'exemple le plus clair pour sa notion (touche militaire si elle aide). `Soldat`/`Personnel`/`Grade` restent des exemples ponctuels.

> **À brainstormer par sous-groupe** : faut-il encore des E/S console quelque part (peu probable en POO) ? jusqu'où va le squelette exact ? quand un test « comportement » suffit-il vs un test structurel ? difficulté/durée par exo.

## 2. Périmètre — les 14 exercices (referentiel §module 3)

Cible `exercises/module-3-poo/`, nommage `3.S.E-slug/` (cf. [`format-exercice.md`](../format-exercice.md) §1).

- **3.1 Classes et objets** (3) : `Point2D`, `Compte` bancaire simple, `Soldat` (attributs + méthodes). — préalables : chapitres 3-1 à 3-4.
- **3.2 Encapsulation et constructeurs** (3) : invariants d'un `Rectangle`, validation dans le constructeur, classe `Date` simple. — préalables : 3-2 à 3-4.
- **3.3 Héritage et polymorphisme** (4) : hiérarchie `Vehicule`, `Forme` géométrique, `Personnel` militaire (officier/sous-off/MdR), méthode polymorphe `decrire()`. — préalables : 3-5 à 3-7.
- **3.4 Interfaces, enums, records** (4) : interface `Comparable` custom, enum `Grade` avec méthodes, `record Coordonnees`, hiérarchie `sealed` d'événements. — préalables : 3-8 à 3-10.

**Antériorité (critère de revue bloquant)** : un exo n'utilise jamais une notion vue **plus tard** que ses préalables ci-dessus, ni une notion d'un module ultérieur (collections génériques, lambdas/streams = M4 ; exceptions/IO = M5 ; `equals`/`hashCode` manuel = M4).

## 3. Contraintes héritées (à respecter dès la conception)

- **Format exercice** : 11 fichiers ([`format-exercice.md`](../format-exercice.md) §2). Package **`etnc.m3`**, groupId **`etnc.piscine.m3`**, dossier module **`module-3-poo`**, **Java 25**.
- **`starter/pom.xml`** : `build-helper` ajoute `../tests/src/test/java`. **`solution/pom.xml`** : identique + injecte **aussi** `../tests-prives/src/test/java`. JUnit Jupiter + AssertJ.
- **Classes** en `PascalCase` dérivées du slug ; tests `<Classe>Test` (public) / `<Classe>PriveTest` (privé). `tests-prives/` présent sur tous les exos POO (cas limites : invariants, downcast, exhaustivité…).
- **`evaluation.yml`** : `total: 20`, `seuil_reussite: 12`, critères `tests-publics` (8) / `tests-prives` (8) / `style` (2) / **critère formateur** (2) avec un `id` parmi `demarche`/`lisibilite`/`idiomatisme`/`respect-consignes` ([`grille-evaluation.md`](../grille-evaluation.md)). Somme des poids = `total` (vérifié par le lint).
- **`sujet.md`** : structure [`format-exercice.md`](../format-exercice.md) §3 (Contexte touche militaire / Énoncé / Exemple / Contraintes / Ce qui sera vérifié / Pour aller plus loin). Vouvoiement, phrases courtes, pas d'humour (charte). **Anti-spoil** : ne pas livrer la solution dans l'énoncé.
- **Utilitaires de test** : `CaptureSortie`/`CaptureEntree` sous `etnc.util` **seulement si** un exo garde des E/S console (rare ici) — la plupart testeront directement les objets.

## 4. Validation EN LOCAL (rappel)

Toolchain installée (chemins en mémoire `reference-env-setup`) :

```bash
# Valider UNE solution de référence (tests publics + privés)
JAVA_HOME="E:/java/jdk-25.0.3+9" "E:/java/apache-maven-3.9.9/bin/mvn" \
  -f exercises/module-3-poo/3.1.E-<slug>/solution/pom.xml test

# Valider toutes les solutions de référence (= job CI en local)
JAVA_HOME="E:/java/jdk-25.0.3+9" MVN="E:/java/apache-maven-3.9.9/bin/mvn" bash scripts/valider-solutions.sh
```

`.m2` chaud (JUnit 5.11 / AssertJ 3.26) → **offline**. On valide chaque solution **avant** de pousser. CI = 4 jobs (dont `valider-solutions` et `lint-exercices`) à passer au vert avant merge.

## 5. Cadence recommandée (modèle module 2)

1. **1 sous-groupe = 1 cycle = 1 PR**, dans l'ordre **3.1 → 3.2 → 3.3 → 3.4** (difficulté croissante, et antériorité des chapitres). Chaque PR branchée depuis `main` après merge de la précédente (`feature/m3-exos-3-1`, `-3-2`, …).
2. Au sein d'un sous-groupe : le **premier exo sert de gabarit** (`pom.xml`, structure, éventuels utilitaires) ; les suivants le réutilisent. Ordre par difficulté croissante.
3. Chaque PR : **CI verte (4 jobs) + validation locale** (§4) avant push ; MAJ [`docs/backlog.md`](../backlog.md) (#19, volet exercices) à chaque sous-groupe.

## 6. Questions ouvertes à trancher au brainstorm (par sous-groupe)

- **Forme exacte du squelette `starter/`** : pour 3.1/3.2, quelles signatures publiques fige-t-on précisément (getters ? `toString` ? noms FR/EN) ? pour 3.3/3.4, quel minimum laisse de la liberté de conception sans casser les tests ?
- **Stratégie de test structurel** : pour chaque notion (record, `sealed`, interface, enum, héritage), trancher test **comportemental** vs **structurel** vs **réflexion** (suivre §1.2). Ex. 3.4 `sealed` événements → `switch` exhaustif ; 3.4 `record Coordonnees` → `equals` généré ; 3.4 `Comparable` custom → `compareTo` + tri/comparaison.
- **`Personnel` (3.3.3)** : hiérarchie officier/sous-off/MdR — classe abstraite ou interface ? quels champs/méthodes communs ? `decrire()` polymorphe (3.3.4) : exo séparé ou volet du même domaine ?
- **Difficulté / durée** par exo et **prerequis** dans `metadata.yml` (slugs ou `[]`, aligner sur l'usage M1/M2).
- **Cas privés** marquants par exo (invariants violés, downcast erroné, exhaustivité, valeurs limites).

## 7. Premiers pas de la nouvelle session

- [ ] Lire les docs en tête + ce brief + la spec chapitres §10.
- [ ] `git checkout main && git pull` ; sanity-check toolchain (`valider-solutions.sh` passe).
- [ ] **Brainstorm** sur le sous-groupe **3.1** (skill `superpowers:brainstorming`) — focalisé sur §1 (squelette/tests d'objets) et §6.
- [ ] Spec : `docs/superpowers/specs/<date>-m3-exos-3-1-design.md`, puis plan `plans/<date>-m3-exos-3-1.md`.
- [ ] Exécuter le sous-groupe **inline** (1 exo gabarit puis les autres), validation locale + CI, PR `feature/m3-exos-3-1`.
- [ ] Répéter pour 3.2, 3.3, 3.4.

---

*Brief préparé le 2026-06-03 en clôture du volet chapitres du module 3 (PR #25/#26/#27 mergées). Référence backlog : #19 (volet exercices). Décisions transverses figées : spec chapitres §10. Débloque ensuite le Projet binôme #1 (#23).*
