# v1 — Journal de la boucle

Branche `v1`. Démarré le 2026-06-16. Budget ~6 h, auto-rythmé. Un item par itération, vérifié, commité si vert.

## ⚠️ Protocole git (un worktree concurrent partage ce clone)

Un worktree `../wt-coherence` (branche `fix/doc-coherence`) partage ce dépôt ; une bascule de
branche concurrente peut faire atterrir un commit hors de `v1`. **origin/v1 est la source de
vérité.** Chaque itération :
1. **Début** : `git fetch origin v1 -q && git checkout -B v1 origin/v1` (resynchronise sur origin).
2. **Fin** : après `git add -A && git commit …`, pousser **`git push origin HEAD:refs/heads/v1`**
   (pousse le commit courant vers v1 quel que soit l'état local de HEAD), puis **vérifier**
   `git log --oneline -1 origin/v1` == ton commit. Sinon re-pousser `git push origin <hash>:refs/heads/v1`.

## Checklist (cocher quand **vert & commité**)

- [x] **A1** moulinette : rapport attendu/obtenu + indice — `AbstractTestChecker.extraitEchecs`
- [x] **A2** chapitre debug / stack trace — `module-1-fondamentaux/1-8-lire-une-erreur.md`
- [x] **B1** commande `submit` (git masqué) — `SubmitCommand` (checkout+add+commit+push en une commande)
- [x] **B2** Module 0 / Quickstart — `courses/docs/demarrage-rapide.md` + `lancer-la-piscine.md` modernisé
- [x] **B3** noter tout le sous-groupe (rapport complet ; progression séquentielle préservée)
- [x] **C1** gamification — moteur (`console/gamification/`) + commande REPL **`profil`** (XP/niveau/barre/badges visibles, testés). [carte tableau de bord web = polish optionnel]
- [x] **C2** jeu capstone Snake (cœur noté vert + rendu jouable Swing) — `exercises/projets-jeux/jeu-1-snake/`
- [ ] **C3** (stretch) 2e jeu
- [x] **D1** chapitre Maven/Gradle — `module-1-fondamentaux/1-9-comprendre-le-pom-maven.md`
- [x] **D2** points fins — `java.time` ajouté (`module-5/5-8`) ; enums (`3-9`) & Optional (`4-8`) **déjà couverts** (ma critique était inexacte sur ce point → à refléter en D5)
- [ ] **D3** (stretch) Module 7 Concurrence
- [ ] **D4** (stretch) Module 8 Persistance
- [x] **D5** périmètre honnête README + « la suite » — section « Périmètre & la suite » (fondamentaux ≠ bootcamp ; pointeur Spring/SQL/REST)

## Journal

### Itération 1 — 2026-06-16 ✓
- Setup branche `v1`, `ROADMAP.md`, `PROGRESS.md`.
- **C2 Snake livré** : `exercises/projets-jeux/jeu-1-snake/` — cœur `JeuSnake` (déplacement,
  collisions mur + corps avec cas « suivre sa queue », manger/score, demi-tour interdit) noté
  **vert** (publics + privés), rendu **jouable** `SnakeSwing` (flèches, Swing), sujet/correction/
  metadata/evaluation conformes. `lint-exercices` toujours 65/0.
- Suivi (itération ultérieure) : intégrer le capstone au **catalogue/tableau de bord** (la GUI
  groupe par module 1-6 ; les `projets-jeux/` ne sont pas encore listés) — à coupler avec **C1**.
### Itération 2 — 2026-06-16 ✓
- **A1 livré** : `extraitEchecs` parse la section « Failures » du ConsoleLauncher et produit,
  pour chaque test, `✗ intitulé` + `attendu :` / `obtenu :` (ou `a échoué : <exception>`),
  sans le bruit (MethodSource, stack frames). Indice du checker amélioré. Repli sur l'ancien
  filtre si format inconnu. Nouveau test unitaire `AbstractTestCheckerTest` (4 cas) vert ;
  suite unitaire `:console:test` verte (pas de régression, MutationChecker inclus).
- **Avant** : `Failures (1): MethodSource [className=..., methodName=...]`.
  **Après** : `✗ affiche la fiche… / attendu : "=== Fiche membre ===…" / obtenu : ""`.
### Itération 3 — 2026-06-16 ✓
- **B3 livré** : `MoulinetteRunner` ne s'arrête plus au 1er échec — **tous** les exos du
  sous-groupe sont notés et rapportés (✓/✗ pour chacun). La **progression reste séquentielle**
  (seul le préfixe d'exos réussis sans interruption est validé dans `progress.json`).
  `GroupReport.stoppedEarly` → `tousReussis` ; messages REPL/rapport mis à jour (« Corrige les
  exos en ✗ » / « 🎉 sous-groupe complet »). Nouveau test 2-exos `tous_les_exos_du_groupe_sont_notes`
  + `SubmissionTriggerTest` ajusté ; `:console:test` **et** `:console:testE2e` verts.
### Itération 4 — 2026-06-16 ✓
- **B1 livré** : nouvelle commande REPL **`submit <sous-groupe>`** = `checkout -B rendu/<g>`
  + `add -A` + `commit` + `push` (déclenche la moulinette), en une seule commande. Tolère
  « rien à valider », infère le groupe depuis la branche `rendu/` courante si l'argument est
  omis. Enregistrée en tête du `CommandRegistry` ; l'aide « commande non supportée » pour
  `checkout`/`branch` pointe désormais vers `submit`. 4 tests `SubmitCommandTest` + `CommandRegistryTest`
  ajusté ; `:console:test` vert.
- **Prochaine itération : B2** (Module 0 / Quickstart — documente `submit` et « comment lancer son code »).

### Interlude — récupération + durcissement git (worktree concurrent)
- Un worktree concurrent `../wt-coherence` a fait atterrir le commit B1 hors de `v1` ; commit
  récupéré et fast-forward sur `origin/v1`. **Protocole git durci** (voir section en tête) :
  resync sur `origin/v1` au début, `git push origin HEAD:refs/heads/v1` + vérif à la fin.

### Itération 5 — 2026-06-16 ✓
- **B2 livré** : nouvelle page **`courses/docs/demarrage-rapide.md`** (« Démarrage rapide, 5 min »,
  position 1.5) qui enseigne la boucle au débutant **avant** le Java : `submit <groupe>` (Git masqué),
  lecture d'un échec (attendu/obtenu), et « voir son code prendre vie » (Snake jouable). `lancer-la-piscine.md`
  modernisé : `submit` mis en avant, exemple de rapport mis à jour (grade-all + attendu/obtenu),
  fin de « s'arrête au premier échec ». Site **Docusaurus** *builde* (0 lien cassé).
- **Prochaine itération : A2** (chapitre « lire une stack trace & déboguer » — complète l'épic A).

### Itération 6 — 2026-06-16 ✓
- **A2 livré** : chapitre **`module-1-fondamentaux/1-8-lire-une-erreur.md`** (« Lire une erreur et
  déboguer ») — erreurs de compilation (javac : fichier/ligne/cause), stack traces d'exécution
  (type, message, « votre » ligne, `Caused by`), lecture du rapport moulinette (attendu/obtenu, lien
  avec A1), et méthode de débogage (`println`, réduction, point d'arrêt). Site Docusaurus *builde*.
  **Épic A complet** (A1 + A2).
- **Prochaine itération : C1** (moteur de gamification : XP/niveaux/streaks/badges — le « wow »).

### Itération 7 — 2026-06-16 ✓ (C1 — partie 1 : moteur)
- **Moteur de gamification livré** (`console/gamification/`) : `Gamification.evaluer(validés, exos)` →
  `Profil(xp, niveau, titre, xpProchainNiveau, progression %, badges)`. XP par difficulté
  (`Difficulte` : tres-facile/debutant=10 … difficile=40), 6 niveaux (Débutant→Maître), badges
  (Premier sang, Assidu·e, Maître du module N, Mi-parcours, Diplômé·e). Pur, sans effet de bord.
  6 tests `GamificationTest` verts (`:console:test`).
- **Prochaine itération : C1 partie 2** — câbler : commande REPL `profil` (lit `progress.json` +
  catalogue → affiche XP/niveau/badges) et exposer le profil au tableau de bord GUI.

### Itération 9 — 2026-06-16 ✓
- **C1 complété** : commande REPL **`profil`** (`ProfilCommand`) + `AdaptateurCatalogue` (catalogue
  + difficulté lue du metadata → `List<Exo>`). `profil` lit `progress.json`, calcule le `Profil`
  (XP, niveau+titre, barre 20 segments, badges) et l'affiche. Enregistrée dans `CommandRegistry`
  (2e, après `submit`). 2 tests `ProfilCommandTest` (profil garni + vierge) ; `:console:test` vert.
  **Épic C cœur fait** (C1 gamification + C2 Snake). Carte web du tableau de bord = polish optionnel.
- **Prochaine itération : D1** (chapitre Maven/Gradle — outils de build, déjà imposés au jour 1).

### Itération 10 — 2026-06-16 ✓
- **D1 livré** : chapitre **`module-1-fondamentaux/1-9-comprendre-le-pom-maven.md`** — à quoi sert un
  outil de build, anatomie du `pom.xml` (coordonnées + dépendances JUnit/AssertJ, Maven Central),
  `mvn test` (= ce que fait la moulinette), et situe Gradle. Répond à la critique « outils de build
  imposés mais jamais enseignés ». Site Docusaurus *builde*.
- **Prochaine itération : D2** (chapitres points fins : enums, Optional, java.time).

### Itération 11 — 2026-06-16 ✓
- **D2 livré** : ajout du chapitre **`module-5-exceptions-io/5-8-dates-et-heures.md`** (java.time :
  LocalDate/LocalDateTime/Instant, calculs Period/ChronoUnit, immuabilité, parse/format). Constat
  d'**honnêteté** : enums (`3-9-enums.md`, 187 l.) et Optional (`4-8-streams-optional.md`) étaient
  **déjà enseignés** — la critique « thin spots non enseignés » était inexacte sur ces deux points.
  À refléter dans le README de périmètre (D5). Site Docusaurus *builde*.
- **Prochaine itération : D5** (README honnête sur le périmètre + « la suite » Spring/BDD/REST), puis PR v1.

### Itération 12 — 2026-06-16 ✓
- **D5 livré** : section **« Périmètre & la suite »** dans le README — assume que c'est une piscine
  de **fondamentaux du langage** (pas un bootcamp job-ready), liste ce qui n'est pas couvert
  (Spring, SQL/JDBC/JPA, REST, concurrence avancée) et trace **la suite** vers un poste back-end.
  Corrige l'« oversell » de l'ancien README.
- **DoD cœur atteint** (A,B,C-cœur,D1,D2,D5) → **ouverture de la PR v1 → main**.
- **Restant (stretch, optionnel) :** C3 2e jeu, D3 module Concurrence, D4 module Persistance.
