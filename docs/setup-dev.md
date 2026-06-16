# Guide d'installation — environnement de développement Piscine Java

> Ce guide s'adresse aux **formateurs et contributeurs** qui souhaitent travailler
> sur le projet en local (rédiger des exercices, valider des solutions, lancer la moulinette).
>
> Pour les **stagiaires**, consultez les instructions dans `exercises/README.md` (à venir).
>
> Ce guide concerne le **mode dépôt** (développement / contribution). Si tu utilises la Piscine via l'**installeur autonome**, tu n'as **rien à installer** : voir [`piscine-stagiaire.md`](piscine-stagiaire.md#mode-installeur-recommandé).

---

## Prérequis

| Outil | Version minimale | Rôle |
|-------|-----------------|------|
| **Java 25 (Temurin)** | 25 LTS | Compiler et exécuter les exercices et la moulinette |
| **Git** | 2.x | Cloner le dépôt, soumettre des rendus |
| **Node.js** | 22 LTS | Construire le site Docusaurus (cours) |

Aucun outil de build **n'est à installer** : deux wrappers sont versionnés dans le dépôt
(jars inclus, cf. #54), donc ils fonctionnent même **sans accès réseau** au premier appel :
- **Gradle Wrapper** `moulinette/gradlew` — build de la **moulinette** ;
- **Maven Wrapper** `./mvnw` (racine) — projets des **exercices** (`starter/`, `solution/`).

---

## 0. Installation automatisée (recommandé, sans droits admin)

Des scripts préparent l'environnement en une commande : vérification de git/Node,
installation d'un JDK **Temurin 25 portable** si Java 25+ est absent (téléchargé depuis
Adoptium, extrait dans votre dossier utilisateur), configuration de `JAVA_HOME`/`PATH`
au niveau utilisateur, puis vérification des wrappers Gradle (moulinette) et Maven (exercices).

```powershell
# Windows (PowerShell) — depuis la racine du repo
.\scripts\setup-dev.ps1            # ou : .\scripts\setup-dev.ps1 -Force pour réinstaller le JDK
```

```bash
# Linux / macOS — depuis la racine du repo
./scripts/setup-dev.sh             # ou : ./scripts/setup-dev.sh --force
```

Après exécution, **rouvrez votre terminal** pour que `JAVA_HOME` soit pris en compte, puis
vérifiez : `java --version` (→ 25), `moulinette/gradlew -p moulinette -v` et `./mvnw -v`.

Les sections 1 à 6 ci-dessous décrivent la **procédure manuelle** équivalente, utile si vous
préférez maîtriser chaque étape ou si un script échoue (proxy, OS non géré, etc.).

---

## 1. Installer Java 25 (Temurin) sans droits administrateur — Windows

> Cette procédure utilise l'archive `.zip` portable d'Eclipse Temurin,
> qui ne nécessite pas de droits administrateur.

### 1.1 Télécharger l'archive

Rendez-vous sur **[adoptium.net/temurin/releases](https://adoptium.net/temurin/releases/)**,
sélectionnez :
- **Version** : 25
- **OS** : Windows
- **Architecture** : x64
- **Package type** : `.zip`

Téléchargez le fichier (ex. `OpenJDK25U-jdk_x64_windows_hotspot_25_xx.zip`).

### 1.2 Extraire et choisir un emplacement

Extrayez l'archive dans un dossier **sans espace** dans le chemin, par exemple :

```
C:\tools\jdk-25\
```

### 1.3 Définir les variables d'environnement (session courante)

Dans un terminal PowerShell :

```powershell
$env:JAVA_HOME = "C:\tools\jdk-25"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
```

Pour rendre ces variables permanentes **sans droits admin** (utilisateur courant) :

```powershell
[Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\tools\jdk-25", "User")
[Environment]::SetEnvironmentVariable(
    "PATH",
    "C:\tools\jdk-25\bin;" + [Environment]::GetEnvironmentVariable("PATH", "User"),
    "User"
)
```

### 1.4 Vérifier l'installation

```powershell
java --version
# Résultat attendu : openjdk 25 ...
```

---

## 2. Installer Java 25 — Linux / macOS

### Via SDKMAN (recommandé)

```bash
# Installer SDKMAN si absent
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"

# Installer Temurin 25
sdk install java 25-tem

# Vérifier
java --version
```

### Via archive portable

Téléchargez l'archive `.tar.gz` sur [adoptium.net](https://adoptium.net/temurin/releases/),
extrayez-la et ajoutez `bin/` au `PATH` :

```bash
export JAVA_HOME="$HOME/tools/jdk-25"
export PATH="$JAVA_HOME/bin:$PATH"
# Ajoutez ces lignes à ~/.bashrc ou ~/.zshrc pour les rendre permanentes
```

---

## 3. Cloner le dépôt

```bash
git clone https://github.com/Benjamin-Curlier/piscine-java.git
cd piscine-java
```

---

## 4. Utiliser les wrappers de build

### Moulinette → Gradle Wrapper

Le build de la moulinette utilise Gradle via `moulinette/gradlew` (Unix) et
`moulinette/gradlew.bat` (Windows), avec le `gradle-wrapper.jar` versionné (même
politique offline que #54).

```bash
# Unix — rendre le script exécutable (une seule fois après le clone)
chmod +x moulinette/gradlew

# Vérifier la version de Gradle
moulinette/gradlew -p moulinette -v       # Unix
moulinette\gradlew.bat -p moulinette -v   # Windows PowerShell

# Tous les modules (build + tests unitaires + uber-jar)
moulinette/gradlew -p moulinette build :console:shadowJar

# Un seul module
moulinette/gradlew -p moulinette :framework:test

# Suites lourdes à la demande
moulinette/gradlew -p moulinette :console:testGit    # ou :console:testTools / :console:testE2e
```

### Exercices → Maven Wrapper

Les projets des exercices (`starter/`, `solution/`) restent des projets Maven : le repo
contient `mvnw` (Unix) et `mvnw.cmd` (Windows) à sa racine, avec le `maven-wrapper.jar`
versionné (#54). Maven 3.9.9 est téléchargé dans `~/.m2/wrapper/dists/` au premier appel.

```bash
# Unix — rendre le script exécutable (une seule fois après le clone)
chmod +x mvnw

# Tests publics du starter (depuis la racine du repo)
./mvnw -f exercises/module-1-fondamentaux/1.1.1-hello-world/starter/pom.xml test

# Tests complets de la solution de référence (publics + privés)
./mvnw -f exercises/module-1-fondamentaux/1.1.1-hello-world/solution/pom.xml test
```

---

## 5. Construire le site de cours (Docusaurus)

```bash
cd courses

# Installer les dépendances Node (une seule fois)
npm ci

# Lancer en mode développement (hot-reload)
npm start

# Construire la version statique (vérification des liens cassés)
npm run build
```

---

## 6. Résolution des problèmes courants

| Symptôme | Cause probable | Solution |
|---|---|---|
| `java: command not found` | `JAVA_HOME` non défini | Relire §1.3 / §2 |
| `./mvnw: Permission denied` | Script non exécutable | `chmod +x mvnw` |
| `mvnw` télécharge mais échoue | Proxy réseau | Configurer `~/.m2/settings.xml` avec les paramètres proxy |
| Tests échouent sur `utf-8` | Encodage terminal | Ajouter `-Dfile.encoding=UTF-8` à `MAVEN_OPTS` |
| Port 3000 occupé (Docusaurus) | Autre processus | `npm start -- --port 3001` |

---

*Dernière mise à jour : 2026-06-01 — tâche #12 (scripts `setup-dev`, Node 22, wrapper offline).*
