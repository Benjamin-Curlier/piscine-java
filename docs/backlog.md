# Backlog — Piscine ETNC

> Liste des tâches à venir, organisée en phases. Tenue à jour à chaque session.
> Une session reprend ce backlog, choisit une tâche **non bloquée**, suit le [workflow par validation](#convention) et coche les critères d'acceptation avant de marquer la tâche `Faite`.

## Convention

- **Statut** : `À faire` / `En cours` / `Faite` / `Bloquée` / `Abandonnée`.
- **Workflow par session** :
  1. Lire la tâche complète et ses pré-requis.
  2. **Proposer** un plan d'exécution détaillé.
  3. **Attendre la validation** du formateur avant d'écrire / modifier des fichiers.
  4. Exécuter, cocher les critères d'acceptation.
  5. Commit + push.
  6. Mettre à jour ce fichier (statut + lien vers le commit qui clôt la tâche).
- **ID** : continu, jamais réutilisé. Les tâches abandonnées gardent leur ID.
- **Numérotation** : les tâches #1 à #8 sont déjà faites — voir l'historique git (commits `a74d3be` → `4a92d36`).

---

## Phase 1 — Outillage fondateur (priorité HAUTE)

> Avant de produire du contenu pédagogique en masse, on cristallise les outils qui le valideront. Sans ça, on accumulerait de la dette.

### #9 — Squelette Maven multi-module de la moulinette
**Statut** : À faire
**Priorité** : Haute
**Pré-requis** : aucun
**Pourquoi** : la moulinette est le différenciateur du projet (correction explicative). Avoir le squelette tôt permet de tester l'intégration avec les exercices.

**Livrable** : `moulinette/` à la racine, projet Maven multi-module Java 25.

**Sous-modules cibles** (validés en mémoire le 2026-05-22) :
- `moulinette/framework/` — API des `Checker` (compile, run, lint, style, plagiat) et du modèle de rapport.
- `moulinette/runner/` — exécution sandboxée (au début : process forké sans Docker ; Docker viendra plus tard).
- `moulinette/reports/` — génération du rapport pédagogique (Markdown + JSON).
- `moulinette/cli/` — point d'entrée CLI `mvn -pl cli exec:java -- run --exo 1.1.1 --rendu <path>`.

**Critères d'acceptation** :
- [ ] POM parent Java 25, dependency management JUnit 5 / AssertJ / SLF4J.
- [ ] Chaque module a son `pom.xml`, son `src/main/java`, un test smoke qui passe.
- [ ] `mvn -f moulinette/pom.xml verify` passe (sur une machine avec Java 25 + Maven 3.9+).
- [ ] README dans `moulinette/` qui explique la structure et comment lancer en local.

**Décisions à prendre** :
- Groupe ID : `etnc.piscine.moulinette` (proposé).
- Logging : SLF4J + Logback ? Ou java.util.logging pour zéro dépendance ?
- Format du rapport : Markdown généré + JSON parallèle pour la moulinette future. Templates Mustache ou concaténation simple ?

---

### #10 — Maven Wrapper (mvnw) mutualisé pour les exercices
**Statut** : À faire
**Priorité** : Haute
**Pré-requis** : aucun
**Pourquoi** : éviter aux stagiaires (et au CI) d'avoir à installer Maven globalement. `./mvnw` suffit.

**Livrable** : un Maven Wrapper installé à la racine du repo, utilisable depuis n'importe quel exercice via un alias / un lien.

**Critères d'acceptation** :
- [ ] `./mvnw -v` fonctionne depuis la racine (télécharge Maven à la première exécution).
- [ ] Documentation dans `docs/setup-dev.md` (à créer en #12) qui explique comment lancer les tests d'un exercice via `./mvnw -f exercises/.../starter/pom.xml test`.
- [ ] Format d'exercice mis à jour pour mentionner `./mvnw` plutôt que `mvn`.

**Décisions à prendre** :
- Un seul `mvnw` racine partagé, ou un par exercice ? **Recommandation** : un seul racine pour ne pas dupliquer.

---

### #11 — GitHub Actions CI
**Statut** : À faire
**Priorité** : Haute
**Pré-requis** : #9 (squelette moulinette), #10 (mvnw)
**Pourquoi** : casser tout PR qui régresse un exercice, le site, ou la moulinette.

**Livrable** : `.github/workflows/ci.yml` avec trois jobs en parallèle.

**Jobs** :
1. **lint-exercices** — script Bash/Python qui vérifie pour chaque dossier `exercises/M.S.E-slug/` la présence des fichiers obligatoires (`sujet.md`, `metadata.yml`, `starter/pom.xml`, etc.) et la conformité YAML.
2. **build-docusaurus** — `cd courses && npm ci && npm run build`. Le `onBrokenLinks: 'throw'` casse le job si une réf est cassée.
3. **valider-solutions** — pour chaque exercice : `cd exercises/.../solution && ../../../../mvnw test`. La solution de référence doit passer **tous** les tests (publics ET privés).

**Critères d'acceptation** :
- [ ] Les 3 jobs passent sur la branche `main` actuelle.
- [ ] Un PR qui supprime `sujet.md` d'un exercice casse `lint-exercices`.
- [ ] Un PR qui introduit un lien cassé dans un chapitre casse `build-docusaurus`.
- [ ] Un PR qui altère la solution de l'exo 1.1.1 casse `valider-solutions`.
- [ ] Le job tourne sur `ubuntu-latest` avec `setup-java@v4` (Temurin 25) et `setup-node@v4` (Node 20).

---

### #12 — Documentation install Java 25 + script `setup-dev`
**Statut** : À faire
**Priorité** : Haute (bloque la validation locale par tout contributeur)
**Pré-requis** : aucun
**Pourquoi** : actuellement la machine du formateur n'a que Java 8 et pas de Maven. Personne ne peut valider en local. Pareil pour les futurs stagiaires.

**Livrable** :
- `docs/setup-dev.md` : install Java 25 (Windows/Linux/macOS) + Node 20 + variables d'env.
- Optionnel : `scripts/setup-dev.ps1` (Windows) et `scripts/setup-dev.sh` (Unix) qui installent Java 25 en mode portable / utilisateur (la machine du formateur n'a pas les droits admin).

**Critères d'acceptation** :
- [ ] Sur la machine du formateur (Windows, no admin), suivre `setup-dev.md` permet d'arriver à `java --version` → 25 et `mvn -v` (ou `./mvnw -v`) qui fonctionne.
- [ ] La doc renvoie vers la version `.zip` portable de Temurin pour le cas no-admin.
- [ ] Le `README.md` racine ajoute un lien vers `setup-dev.md`.

**Décisions à prendre** :
- Faut-il versionner un JDK Temurin portable dans le repo ? **Non**, taille trop grosse et ça date. Pointer vers Adoptium.

---

### #13 — `docs/grille-evaluation.md`
**Statut** : À faire
**Priorité** : Moyenne
**Pré-requis** : aucun
**Pourquoi** : `docs/format-exercice.md` mentionne ce fichier mais il n'existe pas encore. Cadre la part `type: formateur` des `evaluation.yml`.

**Livrable** : grille de notation détaillée pour les critères humains (lisibilité, idiomatisme, respect des consignes, démarche).

**Critères d'acceptation** :
- [ ] Une grille par "type de critère humain" récurrent (au moins : `demarche`, `lisibilite`, `idiomatisme`, `respect-consignes`).
- [ ] Pour chaque grille : 4 niveaux (excellent / bon / passable / insuffisant) avec descripteurs concrets et exemples observables.
- [ ] Un exemple appliqué à l'exo 1.1.1 hello-world.

---

### #14 — `CONTRIBUTING.md` à la racine
**Statut** : À faire
**Priorité** : Moyenne
**Pré-requis** : #13 (la grille est référencée)
**Pourquoi** : cadrer comment d'autres formateurs (ou plus tard, contributeurs externes) ajoutent du contenu.

**Livrable** : `CONTRIBUTING.md` racine.

**Critères d'acceptation** :
- [ ] Workflow Git : `feature/<slug>` → PR → revue par 1 autre formateur → merge.
- [ ] Checklist d'ajout d'un exercice (renvoie vers `docs/format-exercice.md`).
- [ ] Checklist d'ajout d'un chapitre (renvoie vers `docs/charte-redaction.md`).
- [ ] Critères de bloc à la revue (lisibilité, niveau de langue, tests qui passent, etc.).

---

## Phase 2 — Module 1 pilote complet (priorité HAUTE)

> On finit **entièrement** le module 1 avant de toucher aux autres : 7 chapitres + 10 exercices + rétro. Sert de gabarit pour les modules 2 à 6.

### #15 — Chapitres restants du module 1
**Statut** : À faire
**Priorité** : Haute
**Pré-requis** : aucun (le chapitre 1.1 sert de modèle)

**Livrable** : 6 chapitres dans `courses/docs/module-1-fondamentaux/`, format défini en section 6 de `docs/charte-redaction.md`.

- [ ] `1-2-variables-types-primitifs.md`
- [ ] `1-3-operateurs.md`
- [ ] `1-4-entrees-clavier.md`
- [ ] `1-5-conditions.md`
- [ ] `1-6-boucles.md`
- [ ] `1-7-bonnes-pratiques-lisibilite.md`

**Critères d'acceptation** :
- [ ] Chaque chapitre respecte la structure imposée (section 6 charte rédaction).
- [ ] Build Docusaurus passe (`npm run build`).
- [ ] Chaque chapitre a son "exercice guidé" et ses "vérifications d'acquis".
- [ ] Aucune notion utilisée n'a été introduite plus tard que son chapitre d'origine (cohérence avec [`referentiel.md`](referentiel.md)).

---

### #16 — Exercices restants du module 1
**Statut** : À faire
**Priorité** : Haute
**Pré-requis** : #10 (mvnw pour valider localement), #15 (les notions des chapitres déterminent ce qu'on peut demander)

**Livrable** : 9 exercices dans `exercises/module-1-fondamentaux/`, format défini dans `docs/format-exercice.md`.

Sous-groupes ([referentiel.md §4 module 1](referentiel.md#sous-groupes-dexercices-3-sous-groupes-10-exercices)) :
- [ ] **1.1 Premiers pas** : `1.1.2-affichage-formate`, `1.1.3-lecture-saisie`.
- [ ] **1.2 Variables et opérateurs** : `1.2.1-conversion-unites`, `1.2.2-calculs-geometriques`, `1.2.3-manipulation-booleenne`.
- [ ] **1.3 Contrôle de flux** : `1.3.1-fizzbuzz`, `1.3.2-fibonacci-iteratif`, `1.3.3-table-multiplication`, `1.3.4-devine-le-nombre`.

**Critères d'acceptation** :
- [ ] Chaque exercice complet (11 fichiers minimum, voir `format-exercice.md`).
- [ ] `mvn test` sur chaque `solution/` passe les tests publics ET privés.
- [ ] Le CI #11 passe.

---

### #17 — Rétrospective module 1 pilote
**Statut** : À faire
**Priorité** : Moyenne
**Pré-requis** : #15, #16

**Livrable** : `docs/retro-module-1.md` documentant ce qui a été appris en construisant le module pilote, ajustements à propager aux modules 2-6.

**Critères d'acceptation** :
- [ ] Liste des ajustements à apporter aux formats (`format-exercice.md`, `charte-redaction.md`).
- [ ] Estimation du temps réel passé par chapitre / par exercice → ré-estimation des modules 2-6.
- [ ] Décision claire : on continue identique / on adapte / on simplifie.

---

## Phase 3 — Modules 2 à 6 + projets binôme (priorité MOYENNE)

> 5 modules × (chapitres + exercices) + 3 projets binôme. Volume gros, à étaler.

### #18 à #22 — Modules 2 à 6 (1 tâche par module)
Pour chaque module, créer une tâche dédiée listant ses chapitres et exercices. Ne pas tout démarrer en parallèle — pédagogie d'abord, donc finir Mod2 avant d'attaquer Mod3, etc.

- **#18 — Module 2** (5 chapitres, 12 exercices, 4 sous-groupes)
- **#19 — Module 3** (10 chapitres, 14 exercices, 4 sous-groupes) → débloque le **Projet binôme #1**
- **#20 — Module 4** (8 chapitres, 12 exercices, 3 sous-groupes)
- **#21 — Module 5** (7 chapitres, 10 exercices, 3 sous-groupes) → débloque le **Projet binôme #2**
- **#22 — Module 6** (8 chapitres, 7 exercices, 2 sous-groupes) → débloque le **Projet binôme #3** (final)

### #23 — Projet binôme #1 — Mini-domaine OO (Caserne)
**Pré-requis** : #19
Voir `referentiel.md` §5. Format à définir dans `exercises/projets-binome/projet-1-mini-domaine/`.

### #24 — Projet binôme #2 — App + persistance fichier (TaskManager CLI)
**Pré-requis** : #21

### #25 — Projet binôme #3 — Mini-moulinette testée (final)
**Pré-requis** : #22

---

## Phase 4 — Production & déploiement militaire (priorité BASSE pour l'instant)

> Travaux d'infrastructure pour la mise en service réelle. À ne pas démarrer avant que le contenu pédagogique soit substantiellement avancé (objectif : module 1 et 2 complets minimum).

### #26 — Migration GitLab CE on-premise
**Pourquoi** : production cible. GitHub n'est utilisé que pour la phase de dev.
**Inclut** : config GitLab CI/CD miroir du GitHub Actions, runners on-premise, configuration projet.

### #27 — Auth LDAP / AD militaire
**Pré-requis** : interlocuteur infra militaire identifié.

### #28 — Mirror Maven interne (Nexus / Artifactory)
**Pourquoi** : pas d'accès direct à Maven Central depuis l'environnement militaire.

### #29 — Anti-triche (JPlag entre soumissions)
**Pré-requis** : volume suffisant d'exercices (≥ module 1 + 2 complets).
**Livrable** : intégration JPlag dans la pipeline post-rendu, journalisation des similarités > seuil.

### #30 — Durcissement runner Docker
**Pré-requis** : #9 (squelette moulinette).
**Inclut** : `eclipse-temurin:25-jdk` durci, `--network=none`, FS read-only, limites CPU/RAM, seccomp.

### #31 — Conformité Diffusion Restreinte (DR)
**Pré-requis** : confirmation RSSI ETNC.
**Inclut** : revue de toutes les dépendances tierces, journalisation, chiffrement au repos.

---

## Tâches transverses / continues

### #32 — Maintenance et évolution
- Mise à jour Java LTS (prochaine en sept. 2027 — Java 29).
- Mise à jour Docusaurus (release mineure ~tous les 2-3 mois).
- Veille pédagogique : retours stagiaires → ajustements de chapitres / exercices.

### #33 — Branding ETNC
**Statut** : À faire
**Priorité** : Basse
Remplacer les assets Docusaurus par défaut (favicon, logo, social card) par les visuels ETNC.

---

## Légende des priorités

- **Haute** : bloque ou prépare immédiatement la suite (outillage de phase 1, contenu pilote du module 1).
- **Moyenne** : à programmer dans les sessions suivantes une fois la phase 1 stable.
- **Basse** : à valeur produite faible tant que le contenu pédagogique n'est pas substantiel, ou dépendant d'interlocuteurs externes.

---

*Backlog maintenu par l'équipe formateurs. Une session peut ajouter une nouvelle tâche en fin de section appropriée avec un ID continu et un statut `À faire`.*
