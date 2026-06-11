# Cours + tableau de bord (v1 chantier 4) — Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** La GUI devient le portail unique : tableau de bord (progression par module/sous-groupe/exo, historique des rapports, boutons explorateur/terminal) + accès aux cours.

**Architecture:**
- **Progression persistée** : `MoulinetteRunner.Default` écrit/merge `<ws>/.piscine/progress.json` (`{"exoId": true}` pour chaque exo validé, jamais rétrogradé) après chaque run. Source de vérité simple, pas de parsing de rapports markdown.
- **GuiServer** : nouveaux endpoints `GET /api/progress` (catalogue + statut par exo : `done`/`current`/`locked`, dérivé séquentiellement), `GET /api/reports` (liste `<ws>/.piscine/reports/*.md`, récents d'abord), `GET /api/report?name=` (markdown brut, nom validé), `POST /api/open` (`{"target":"explorer"|"terminal"}` — ouvre le workspace, fire-and-forget). `ConsoleSession` expose `catalog()`.
- **Cours** : la GUI sert le **site Docusaurus déjà construit** (arg `--site <dir>`, monté tel quel) — c'est l'infra existante du bundle ; PAS de re-rendu marked.js des chapitres (déviation spec assumée : le site est plus riche et déjà livré). Les rapports moulinette, eux, sont rendus avec **marked.js vendoré**.
- **Frontend** : barre latérale 3 entrées (Tableau de bord / Cours / Terminal). Dashboard = cartes modules avec compteurs, exos avec statut, liste des rapports cliquables (rendu markdown), boutons « Ouvrir dans l'explorateur » / « Ouvrir un terminal ici ». Cours = lien (nouvel onglet) vers le site servi, masqué si pas de `--site`.

**Déviations spec (assumées) :** chapitres non verrouillés par module (site statique entier — gating à réévaluer plus tard) ; cours via Docusaurus, pas marked.js.

**Tech Stack:** Java 25, marked.js vendoré, Gradle.

---

### Task 1: Progression persistée (console, TDD)
- Test `MoulinetteRunnerProgressTest` : runner avec checker stub OK → `progress.json` contient `"1.1.1": true` ; un 2e run en échec ne rétrograde pas.
- Impl dans `MoulinetteRunner.Default.runGroup` : après la boucle, merge des `outcomes` ok dans `<repoRoot>/.piscine/progress.json` (lecture naïve du JSON plat existant + réécriture).
- `ConsoleSession` : accesseur `catalog()`.
- Commit `feat(console): progression persistee (.piscine/progress.json)`.

### Task 2: Endpoints dashboard (gui, TDD)
- Tests `GuiServerTest` étendus (catalogue construit dans un dossier temp avec 2 `metadata.yml`) : `/api/progress` rend modules/exos avec statuts ; `/api/reports` liste ; `/api/report?name=` rend le md ; nom hors liste blanche → 404 ; `/api/open` cible inconnue → 400.
- Impl dans `GuiServer` (+ champ `siteDir` optionnel servi sous `/cours/`).
- `Main` gui : arg `--site` (comme la console).
- Commit `feat(gui): API progress/reports/open + montage du site de cours`.

### Task 3: Frontend dashboard
- `index.html` : sidebar 3 entrées, vues `#view-dashboard` / `#view-terminal` ; vendored `marked.min.js`.
- `dashboard.js` : fetch `/api/progress` → cartes modules (n validés / total), sous-groupes dépliés du module courant, statuts ✓/●/🔒 ; fetch `/api/reports` → liste, clic → `/api/report` rendu via marked dans un panneau ; boutons explorateur/terminal → `POST /api/open`. Rafraîchi après chaque commande terminal qui déclenche une évaluation (simple : refresh à chaque retour de `/api/terminal`).
- Lien « Cours » si `/api/state` indique un site monté.
- Vérification manuelle navigateur (preview) + suites complètes vertes.
- Commit `feat(gui): tableau de bord (progression, rapports, raccourcis)` + docs/CHANGELOG.
