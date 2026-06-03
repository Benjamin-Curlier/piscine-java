# Piscine ETNC — guide stagiaire

Ce guide te permet d'utiliser la Piscine **en autonomie sur ta machine**, sans serveur.

## Deux façons de démarrer

| Mode | Quand l'utiliser | Ce que tu fais |
|------|------------------|----------------|
| **Standalone (ZIP)** | Le formateur t'a remis un **ZIP** | Tu décompresses et tu lances — **rien à installer, pas de réseau**. Voir la section **« Mode standalone »** ci-dessous. |
| **Dépôt (clone)** | Tu as accès au dépôt Git du projet | Tu installes Java 25 + git, tu clones, et tu lances le bootstrap. Voir les sections **1 à 8** ci-dessous. |

La **boucle de travail** (éditer un exercice → rendre via Git → lire le rapport de la moulinette) est **identique** dans les deux modes ; seule la mise en route diffère.

---

## Mode standalone (ZIP)

Le ZIP `piscine-etnc-stagiaire-*.zip` embarque **tout** : un JDK, git, la moulinette pré-compilée et le **site de cours servi en local**. Aucune installation, aucun réseau.

1. Décompresse le ZIP `piscine-etnc-stagiaire-*.zip` où tu veux.
2. Lance le launcher (Windows : double-clic sur `piscine.bat` ; Unix : `./piscine.sh`).
3. Au premier lancement, ton espace de travail est créé, **le site de cours s'ouvre dans ton navigateur** (sinon, ouvre l'URL affichée, par défaut `http://127.0.0.1:8800/`) et la **console** démarre.
4. Tu travailles exactement comme aux sections **4 à 8** ci-dessous (`submit-start`, rendu Git, rapport).

---

## Mode dépôt (clone du projet)

Les sections suivantes (1 à 8) décrivent la mise en route quand tu **clones le dépôt** (Java 25 + git à installer toi-même).

## 1. Installer les prérequis

- **Java 25** et **git** : voir [`setup-dev.md`](setup-dev.md) (instructions Windows sans droits admin incluses).
- Si la commande `java` de ton terminal est trop ancienne mais que tu as un JDK 25 ailleurs, définis la variable d'environnement `JAVA_HOME` vers ce JDK : le script de bootstrap l'utilisera en priorité.

## 2. Cloner le repo

```bash
git clone <url-piscine-etnc> piscine-etnc
cd piscine-etnc
```

## 3. Démarrer ton workspace personnel

**Linux / macOS :**
```bash
./scripts/piscine-bootstrap.sh --nom <ton-slug>
```

**Windows (PowerShell) :**
```powershell
.\scripts\piscine-bootstrap.ps1 -Nom <ton-slug>
```

Le script :
- vérifie tes prérequis,
- compile la console de correction,
- crée un dossier `../piscine-<ton-slug>/` à côté du repo,
- initialise un git local avec les exercices, un remote local, et un commit initial.

À la fin, il affiche la commande exacte pour lancer le REPL.

> Le script est **idempotent** : si le workspace existe déjà, il te le dit. Ajoute `--force` (ou `-Force`) pour le réinitialiser.

## 4. Travailler sur un exercice

1. Lance le REPL (commande affichée par le bootstrap), par exemple :
   ```bash
   java -jar moulinette/console/target/moulinette-console.jar repl --repo ../piscine-<ton-slug> --piscine-repo .
   ```
2. Démarre un rendu de sous-groupe :
   ```
   piscine[main]> submit-start 1.1
   ```
3. Édite ton code dans `../piscine-<ton-slug>/exercises/1.1.1-hello-world/starter/` (avec ton IDE habituel).
4. Vérifie l'état et rends :
   ```
   piscine[rendu/1.1]> git add exercises/1.1.1-hello-world
   piscine[rendu/1.1]> git commit -m "rendu 1.1.1"
   piscine[rendu/1.1]> git push origin rendu/1.1
   ```

La moulinette se lance automatiquement et écrit son rapport dans `.piscine/reports/`.

## 5. Lire le rapport

Le chemin du rapport s'affiche dans la console (`.piscine/reports/1.1-<timestamp>.md`).
Ouvre-le dans ton éditeur. Pour chaque exo : statut, messages, suggestions.

## 6. Bloqué ?

- **Un exo échoue** : la séquence s'arrête à cet exo. Corrige-le, refais `add` / `commit` / `push`.
- **`git push` refusé (non-fast-forward)** : pour le MVP, supprime la branche locale et recommence avec `submit-start <groupe>`.
- **Commande non supportée** : seules `add`, `commit`, `push`, `status`, `log`, `diff`, `submit-start`, `help`, `exit` sont reconnues dans le MVP.

## 7. Convention de rendu

| Élément | Valeur |
|---------|--------|
| Branche de rendu | `rendu/<sous-groupe>` (ex: `rendu/1.1`) |
| Trigger moulinette | `git push origin rendu/<sous-groupe>` |
| Ordre d'évaluation | exos d'un groupe par difficulté croissante, **arrêt au premier échec** |
| Remote | bare local `file://<workspace>/.piscine/remote.git` |

## 8. Commandes du REPL (rappel)

| Commande | Effet |
|----------|-------|
| `submit-start <groupe>` | crée/bascule sur la branche `rendu/<groupe>` |
| `git add <chemin>` | indexe des fichiers |
| `git commit -m "..."` | enregistre un commit |
| `git push origin rendu/<groupe>` | pousse **et déclenche la moulinette** |
| `git status` / `git log` / `git diff` | inspection |
| `help` | liste les commandes |
| `exit` | quitte le REPL |
