# v1 — Journal de la boucle

Branche `v1`. Démarré le 2026-06-16. Budget ~6 h, auto-rythmé. Un item par itération, vérifié, commité si vert.

## Checklist (cocher quand **vert & commité**)

- [x] **A1** moulinette : rapport attendu/obtenu + indice — `AbstractTestChecker.extraitEchecs`
- [ ] **A2** chapitre debug / stack trace
- [ ] **B1** commande `submit` (git masqué)
- [ ] **B2** Module 0 / Quickstart
- [ ] **B3** noter tout le sous-groupe
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
- **Prochaine itération : B3** (noter tout le sous-groupe au lieu de s'arrêter au 1er échec).
