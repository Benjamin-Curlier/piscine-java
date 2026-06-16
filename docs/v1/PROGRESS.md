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
- [ ] **A2** chapitre debug / stack trace
- [x] **B1** commande `submit` (git masqué) — `SubmitCommand` (checkout+add+commit+push en une commande)
- [ ] **B2** Module 0 / Quickstart
- [x] **B3** noter tout le sous-groupe (rapport complet ; progression séquentielle préservée)
- [ ] **C1** moteur gamification (XP/niveaux/streaks/badges) + tableau de bord
- [x] **C2** jeu capstone Snake (cœur noté vert + rendu jouable Swing) — `exercises/projets-jeux/jeu-1-snake/`
- [ ] **C3** (stretch) 2e jeu
- [ ] **D1** chapitre Maven/Gradle
- [ ] **D2** chapitres enums / Optional / java.time
- [ ] **D3** (stretch) Module 7 Concurrence
- [ ] **D4** (stretch) Module 8 Persistance
- [ ] **D5** périmètre honnête README + « la suite »

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
