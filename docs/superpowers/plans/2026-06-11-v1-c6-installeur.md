# Installeur jpackage (v1 chantier 6) — Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: superpowers:executing-plans (inline). Steps en checkboxes.

**Goal:** Un installeur par-utilisateur (sans admin) « Piscine ETNC » : double-clic → la GUI s'ouvre dans le navigateur, workspace auto dans `~/PiscineETNC`, JRE + MinGit + contenus embarqués. Windows `.exe` + Linux `.deb`/app-image (CI).

**Architecture:**
1. **Résolution git explicite** (`ProcessGitClient`) : propriété système `piscine.git` (chemin de l'exe) → env `PISCINE_GIT_HOME` (`<home>/cmd/git.exe|bin/git`) → `git` du PATH. La GUI positionne `piscine.git` si un MinGit embarqué est présent.
2. **Mode zéro-argument de la GUI** (`Main`) : sans `--repo`, mode « installé » — localise le dossier app (CodeSource du jar), contenu piscine sous `<app>/piscine` (exercises + site), MinGit sous `<app>/git`, workspace par défaut `%USERPROFILE%/PiscineETNC/workspace` (surchargeable `PISCINE_HOME`), **init automatique au premier lancement** (`LocalWorkspaceInitializer`), puis serveur + navigateur. Les données restent hors du répertoire d'installation.
3. **Tâche Gradle `jpackageStaging` + `jpackageApp`** (module gui) : staging = uber-jar + `piscine/exercises` + `piscine/site` (si `courses/build` existe) + `git/` (MinGit téléchargé/caché, Windows) ; puis `jpackage` (`--type exe --win-per-user-install --win-dir-chooser --win-menu --win-shortcut`, icône différée). Linux : `--type deb` + app-image tar.gz. WiX requis sur Windows (portable, sans admin).
4. **CI release** : workflow `release.yml` (déclenché sur tag `v*`) — jobs windows-latest (exe) et ubuntu-latest (deb + tar.gz), artefacts uploadés. Le CI PR existant ne change pas.
5. **Nettoyage** : `build-bundle.sh/.ps1` et le flux ZIP retirés ; `docs/deploiement-instructeur.md` réécrit autour de l'installeur ; CHANGELOG.

**Critère :** sur la machine de dev, `gradlew :gui:jpackageApp` produit un installeur/app-image dont l'exe lancé SANS argument ouvre la GUI sur un workspace auto-initialisé, git embarqué fonctionnel (`submit-start`/`push` OK).
