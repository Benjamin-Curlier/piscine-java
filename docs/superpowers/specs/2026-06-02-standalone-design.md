# #56 — Bundle standalone (ZIP autonome stagiaire) : design

> Spec de l'itération #56. Origine : brief [`docs/superpowers/2026-06-02-standalone-kickoff.md`](../2026-06-02-standalone-kickoff.md).
> Cycle : ce document (design) → plan d'implémentation → exécution **inline** (pas de sous-agents, contrainte projet) → **1 PR** sur `feature/standalone-bundle`.

## 1. Objectif

Livrer un **ZIP autonome et hors-ligne** qu'un stagiaire décompresse et lance sur une machine **Windows vierge** (sans Java, git, Node, ni droits admin). Le ZIP doit permettre, **sans réseau** :

1. de **lire le site de cours** (Docusaurus) dans le navigateur, servi en local ;
2. de **rendre des exercices via git** et d'obtenir le **rapport de la moulinette**.

À la livraison, le mode standalone est documenté comme **finalisé** (suppression des mentions « en cours » dans `README.md` et le guide stagiaire).

## 2. Décisions actées (brainstorm 2026-06-02)

| Sujet | Décision |
|-------|----------|
| Site servi | **In-process** dans la console Java via `com.sun.net.httpserver.SimpleFileServer` (module `jdk.httpserver`). Pas de process `jwebserver` séparé. |
| git portable | **MinGit Windows**, **téléchargé au build** depuis git-for-windows, **version épinglée + SHA-256 vérifié**. |
| Site dans le ZIP | **Régénéré au build** (`npm ci && npm run build` dans `courses/`). Pas d'artefact versionné. |
| Port / navigateur | Port **8800** par défaut, **fallback** sur le port libre suivant si pris ; ouverture auto du navigateur **et** URL toujours affichée. |
| Résolution de `git` | Via `PATH` (le launcher préfixe `git\cmd`). **Aucune modification Java** : `ProcessGitClient` fait déjà `cmd.add("git")` et hérite de l'environnement. |
| OS | **Windows d'abord** (launcher `.bat` + JDK/MinGit Windows). `piscine.sh` conservé en best-effort pour le formateur/Unix. |
| Maven / `.m2` | **Hors scope** : la boucle de feedback (javac + JUnit `ConsoleLauncher` via l'uber-jar shadé) est déjà offline. |

## 3. Structure du ZIP

```
piscine-etnc-stagiaire/
├─ app/moulinette-console.jar      uber-jar (commandes init / repl), inchangé
├─ jdk/                            JDK 25 portable (flag --jdk / -JdkPath)
├─ git/                            MinGit Windows (git.exe sous git/cmd/)
├─ site/                           = courses/build/ (site Docusaurus statique)
├─ piscine/
│   ├─ exercises/                  les exercices de référence
│   └─ docs/piscine-stagiaire.md   guide stagiaire
├─ workspace/                      espace de rendu, créé au 1er lancement
├─ piscine.bat                     launcher Windows (principal)
├─ piscine.sh                      launcher Unix (best-effort formateur)
└─ LISEZMOI.txt
```

## 4. Composant A — Service du site (Java, in-process)

### 4.1 Classe `CourseSiteServer`
Nouvelle classe dans la console (`etnc.piscine.moulinette.console`), responsabilité unique : servir un dossier statique en local et gérer son cycle de vie.

- **API** : `CourseSiteServer.start(Path siteDir, int preferredPort) → CourseSiteServer` ; méthodes `URI url()` et `void stop()`. `AutoCloseable`.
- **Implémentation** : `SimpleFileServer.createFileServer(new InetSocketAddress(InetAddress.getLoopbackAddress(), port), siteDir, OutputLevel.NONE)`. Lié à **127.0.0.1** uniquement.
- **Fallback de port** : tenter `preferredPort` (8800) ; sur `BindException`, réessayer `port+1`, jusqu'à un plafond (ex. 8810) puis échec explicite. Le port retenu est exposé par `url()`.
- **Routing Docusaurus** : `SimpleFileServer` redirige une requête de répertoire (`/docs/intro`) vers `/docs/intro/` puis sert `index.html`. `baseUrl` du site = `/` (déjà le cas). À vérifier en validation que la navigation profonde fonctionne.

### 4.2 Intégration au REPL
- La commande `repl` gagne une option `--site <dir>` (optionnelle), via le parsing manuel existant (`optional(args, "--site", null)` dans `Main`). Pas de picocli dans ce module.
- L'orchestration vit dans `Main.runRepl` (et non dans la classe `Repl`, laissée inchangée) : si `--site` est fourni et que le dossier existe, démarrer `CourseSiteServer.start(...)`, puis exécuter `repl.run()` dans un `try`/`finally` qui appelle `stop()`. Au démarrage :
  - **ouverture navigateur** cross-OS : Windows `cmd /c start "" <url>`, macOS `open <url>`, Linux `xdg-open <url>` ; échec silencieux (best-effort).
  - **affichage console** systématique : `Site de cours : http://127.0.0.1:<port>/` (fallback manuel).
- **Arrêt propre** : `stop()` appelé à la sortie du REPL (bloc `finally` / hook), pour libérer le port.
- Si `--site` absent ou dossier manquant : comportement actuel inchangé (mode dépôt non impacté).

## 5. Composant B — Extension de `build-bundle` (`.ps1` + `.sh`)

Étendre les deux scripts existants en conservant l'ordre : uber-jar → staging → **site** → **git** → JDK → launchers → ZIP.

### 5.1 MinGit (téléchargement épinglé)
- Constantes en tête de script : `MINGIT_VERSION`, `MINGIT_URL` (asset `MinGit-<ver>-64-bit.zip` des releases git-for-windows), `MINGIT_SHA256`.
  - Version épinglée : dernière MinGit 64-bit stable de git-for-windows ; la valeur SHA-256 est celle publiée par le projet pour cet asset (renseignée dans le script lors de l'implémentation à partir du fichier de checksums officiel).
- Télécharger l'archive (`Invoke-WebRequest` / `curl`), **vérifier le SHA-256** (abort si mismatch), extraire dans `stage/git`.
- Échec réseau ou checksum → message clair + arrêt du build.

### 5.2 Site (régénération)
- Si `courses/build/` absent ou plus ancien que les sources `courses/docs/**` : exécuter `npm ci` puis `npm run build` dans `courses/`.
- `npm`/`node` absent → message explicite (« installez Node pour construire le bundle ») + arrêt.
- Copier `courses/build/` → `stage/site`.

### 5.3 Reste
- Conserver : uber-jar (`mvn -pl console -am package`), `exercises/`, `docs/piscine-stagiaire.md`, JDK portable.
- Générer `piscine.bat`, `piscine.sh`, `LISEZMOI.txt` (cf. §6), puis compresser en `piscine-etnc-stagiaire-<AAAAMMJJ>.zip`.

## 6. Composant C — Launchers

### 6.1 `piscine.bat` (Windows, principal)
```bat
@echo off
setlocal
set "HERE=%~dp0"
set "JAVA_HOME=%HERE%jdk"
set "PATH=%HERE%jdk\bin;%HERE%git\cmd;%PATH%"
set "JAVA=%HERE%jdk\bin\java.exe"
set "JAR=%HERE%app\moulinette-console.jar"
if not exist "%HERE%workspace\.git" (
    "%JAVA%" -jar "%JAR%" init --nom stagiaire --dest "%HERE%workspace" --piscine-repo "%HERE%piscine"
)
"%JAVA%" -jar "%JAR%" repl --repo "%HERE%workspace" --piscine-repo "%HERE%piscine" --site "%HERE%site"
endlocal
```
Le préfixe `git\cmd` dans `PATH` rend `git` résoluble par `ProcessGitClient` (hérité via `ProcessBuilder`).

### 6.2 `piscine.sh` (Unix, best-effort)
Symétrique : `JAVA_HOME`, `PATH` (jdk/bin + git si présent), `init` conditionnel, `repl … --site "$HERE/site"`.

### 6.3 `LISEZMOI.txt`
Mis à jour : double-clic `piscine.bat`, le site s'ouvre dans le navigateur, `help`/`exit`, emplacement des rapports, renvoi au guide.

## 7. Composant D — Validation (machine cible)

- Build local : `./scripts/build-bundle.ps1 -JdkPath <jdk25>` (et `.sh`). Vérifier que le ZIP contient `site/`, `git/`, `jdk/`, `app/`, `piscine/`.
- Décompresser sur une **machine Windows propre** (sans Java/git/Node), lancer `piscine.bat`. Critères :
  1. le site s'ouvre dans le navigateur (ou URL affichée) et la navigation Docusaurus fonctionne ;
  2. le REPL démarre ;
  3. un rendu d'exercice (`submit-start` → add/commit/push) est **évalué** et produit un rapport ;
  4. le tout **hors-ligne**.
- À la fermeture du REPL, le port 8800 est libéré.

## 8. Gestion d'erreurs (récap)

| Cas | Comportement |
|-----|--------------|
| JDK absent au build | erreur (déjà géré) |
| Download MinGit / checksum KO | message + arrêt du build |
| `npm`/`node` absent au build | message + arrêt du build |
| Port 8800 occupé | fallback port libre (jusqu'à 8810) |
| Ouverture navigateur échoue | URL affichée dans la console |
| `--site` pointant un dossier absent | REPL normal sans site (log d'avertissement) |

## 9. Tests

- **Unitaire** `CourseSiteServerTest` : démarre sur un dossier temporaire, sert un fichier (HTTP 200 + contenu), `url()` reflète le port retenu, `stop()` libère le port. Test du **fallback** : un serveur occupe 8800, le second prend 8801.
- **Non-régression** : la suite moulinette existante reste verte ; le mode dépôt (`repl` sans `--site`) inchangé.
- **CI** : ne pas casser les 4 jobs (dont `build-docusaurus`).

## 10. Documentation (les deux modes + commandes ZIP)

- **`courses/docs/lancer-la-piscine.md`** *(nouveau, sidebar_position 2)* : explique au stagiaire les **deux modes** dès le lancement (« Tu as reçu un ZIP ? → standalone / Tu as accès au dépôt ? → dépôt »), pas-à-pas de chaque mode, message « la boucle de travail est identique ». Standalone présenté comme **finalisé**.
- **`courses/docs/intro.md`** : renvoi vers la nouvelle page ; ajuster « Ce dont vous aurez besoin » pour ne plus sous-entendre que l'installation de Java est obligatoire.
- **`docs/piscine-stagiaire.md`** : finaliser la section « Mode standalone » avec le pas-à-pas réel ; retirer le bandeau 🚧.
- **`README.md`** : retirer « Le mode standalone est en cours de finalisation ».
- **`CONTRIBUTING.md`** : nouvelle section **« Produire le bundle standalone »** — commandes `build-bundle.ps1` / `.sh`, options (`-JdkPath`/`--jdk`, `-OutDir`/`--out`), prérequis build (Node, réseau pour MinGit), et comment tester le ZIP.
- **`docs/setup-dev.md`** : préciser que le mode standalone n'exige aucune installation.
- **`docs/backlog.md`** : marquer #56 fait.

## 11. Hors scope (YAGNI)

- Maven / dépôt `.m2` dans le ZIP.
- Launcher Linux/macOS de premier rang (best-effort uniquement).
- Sous-commande moulinette `site` dédiée (le service est porté par l'option `--site` du REPL).
- HTTPS / multi-utilisateur / configuration du port par le stagiaire.
