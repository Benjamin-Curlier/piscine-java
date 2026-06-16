# Déploiement — guide instructeur

Depuis la **v1**, la Piscine se distribue comme un **installeur autonome** : le stagiaire
double-clique, l'application s'ouvre dans son navigateur, et tout est embarqué (JRE, git,
exercices, site de cours). Aucun droit administrateur, aucun réseau à l'exécution.

## 1. Obtenir les installeurs

### Via le CI (recommandé)

Le workflow **Release** (`.github/workflows/release.yml`) construit les artefacts à chaque
tag `v*` (ou manuellement via *Run workflow*) :

| Artefact | Plateforme | Contenu |
|---|---|---|
| `Piscine Java-1.0.0.exe` | Windows | Installeur **par utilisateur** (sans admin), JRE + MinGit embarqués |
| `piscine-java-windows-portable.zip` | Windows **verrouillé** (AppLocker) | App portable : extraire et lancer `Piscine Java.exe` — **aucune installation** |
| `piscine-java_1.0.0_amd64.deb` | Debian/Ubuntu | Paquet deb (git système requis) |
| `piscine-java-linux-portable.tar.gz` | Linux (toutes distros) | App-image portable, à extraire et lancer |

### En local

```bash
# Site de cours (une fois) : cd courses && npm ci && npm run build
moulinette/gradlew -p moulinette :gui:jpackageApp            # .exe sur Windows (WiX requis*)
moulinette/gradlew -p moulinette :gui:jpackageApp -PjpackageType=app-image   # dossier portable
```

\* WiX 3.x portable (sans admin) : dézipper `wix314-binaries.zip` et passer
`-PwixDir=<dossier>` (ou env `WIX_DIR`). Sortie : `moulinette/gui/build/jpackage/out/`.

## 2. Installer sur le poste stagiaire

**Windows** : copier le `.exe` (clé USB, partage), double-clic. Installation par défaut dans
`%LOCALAPPDATA%` (modifiable), raccourci « Piscine Java » dans le menu Démarrer. Aucun droit
admin demandé.

**Linux** : `sudo apt install ./piscine-java_*.deb`, ou extraire le tar.gz et lancer
`Piscine Java/bin/Piscine Java` (git doit être présent : `sudo apt install git`).

### Postes Windows verrouillés (AppLocker / SRP)

Sur les parcs durcis, les **installeurs non signés sont refusés** (exe : extraction MSI dans
`%TEMP%` bloqué ; msi : Windows Installer désactivé pour l'utilisateur), mais l'exécution
d'un exe depuis un **chemin autorisé par la politique** (lecteur de travail type `E:\`,
répertoire métier…) fonctionne. Dans ce cas :

1. Extraire `piscine-java-windows-portable.zip` dans un chemin autorisé.
2. Lancer `Piscine Java\Piscine Java.exe` — identique à la version installée
   (workspace dans `~/Piscine Java`, icône de zone de notification).

Vérifié sur un poste d'entreprise sans droits admin. Pour une distribution « propre » (installeur
double-clic), la voie durable est de faire **signer l'exe par le certificat interne** de
l'organisation ou d'obtenir une **règle AppLocker dédiée** (éditeur ou chemin) auprès de la
DSI/SSI — l'exe jpackage se signe avec `signtool sign` standard.

## 3. Premier lancement

Au premier démarrage, l'application :
1. initialise le **workspace** du stagiaire dans `~/Piscine Java/workspace`
   (surchargeable par la variable d'environnement `PISCINE_HOME`) — git local, remote
   simulé, exercices copiés ;
2. démarre le serveur local (127.0.0.1, port libre) et **ouvre le navigateur** ;
3. le stagiaire travaille : tableau de bord, cours, terminal git intégré.

Les données du stagiaire vivent **hors du répertoire d'installation** : une mise à jour de
l'application ne touche jamais à son travail.

## 4. Dépannage

| Symptôme | Cause probable | Remède |
|---|---|---|
| Le navigateur ne s'ouvre pas | Politique du poste | Ouvrir manuellement l'URL affichée dans la fenêtre console |
| « Contenu piscine introuvable » | Installation incomplète | Réinstaller ; vérifier `app/piscine/exercises` dans le dossier d'installation |
| git introuvable (Linux) | git non installé | `sudo apt install git` (le .exe Windows embarque MinGit) |
| Réinitialiser un stagiaire | — | Supprimer `~/Piscine Java` (l'app le recrée au lancement) |
