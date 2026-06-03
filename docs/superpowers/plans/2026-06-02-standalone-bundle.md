# Bundle standalone (#56) — Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task (exécution **inline** — contrainte projet : pas de sous-agents). Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Produire un ZIP autonome hors-ligne (site de cours servi en local + rendu git + moulinette) lançable sur une machine Windows vierge, et documenter les deux modes pour le stagiaire.

**Architecture:** La console Java sert le site Docusaurus statique en in-process via `SimpleFileServer` (module `jdk.httpserver`), piloté par une nouvelle option `--site` de la commande `repl`. Le script `build-bundle` est étendu pour régénérer le site, télécharger un MinGit épinglé (checksum vérifié) et générer un launcher Windows unique qui câble `PATH` (JDK + git) puis lance le REPL avec le site.

**Tech Stack:** Java 25 (`com.sun.net.httpserver.SimpleFileServer`, `java.net.http.HttpClient` pour les tests), JUnit 5 + AssertJ, Maven, PowerShell + Bash (build-bundle), Docusaurus (`npm run build`).

**Référence spec :** [`docs/superpowers/specs/2026-06-02-standalone-design.md`](../specs/2026-06-02-standalone-design.md).

---

## Task 1: `CourseSiteServer` — service statique in-process

**Files:**
- Create: `moulinette/console/src/main/java/etnc/piscine/moulinette/console/repl/CourseSiteServer.java`
- Test: `moulinette/console/src/test/java/etnc/piscine/moulinette/console/repl/CourseSiteServerTest.java`

- [ ] **Step 1: Write the failing test**

```java
package etnc.piscine.moulinette.console.repl;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class CourseSiteServerTest {

    @Test
    void sert_index_html_sur_le_port_prefere() throws Exception {
        Path site = Files.createTempDirectory("site");
        Files.writeString(site.resolve("index.html"), "<h1>Piscine</h1>");

        try (CourseSiteServer server = CourseSiteServer.start(site, 8830, 8840)) {
            assertThat(server.url().toString()).isEqualTo("http://127.0.0.1:8830/");
            HttpResponse<String> resp = HttpClient.newHttpClient().send(
                HttpRequest.newBuilder(URI.create(server.url() + "index.html")).build(),
                HttpResponse.BodyHandlers.ofString());
            assertThat(resp.statusCode()).isEqualTo(200);
            assertThat(resp.body()).contains("Piscine");
        }
    }

    @Test
    void bascule_sur_le_port_suivant_si_le_prefere_est_pris() throws Exception {
        Path site = Files.createTempDirectory("site");
        Files.writeString(site.resolve("index.html"), "ok");

        try (CourseSiteServer first = CourseSiteServer.start(site, 8841, 8850);
             CourseSiteServer second = CourseSiteServer.start(site, 8841, 8850)) {
            assertThat(first.url().toString()).isEqualTo("http://127.0.0.1:8841/");
            assertThat(second.url().toString()).isEqualTo("http://127.0.0.1:8842/");
        }
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `mvn -f moulinette/pom.xml -pl console -q test -Dtest=CourseSiteServerTest`
Expected: FAIL (compilation : `CourseSiteServer` n'existe pas).

- [ ] **Step 3: Write the implementation**

```java
package etnc.piscine.moulinette.console.repl;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.SimpleFileServer;

import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.file.Path;

/** Sert un dossier statique (le site Docusaurus) sur 127.0.0.1, en in-process. */
public final class CourseSiteServer implements AutoCloseable {

    private final HttpServer server;
    private final URI url;

    private CourseSiteServer(HttpServer server, URI url) {
        this.server = server;
        this.url = url;
    }

    /**
     * Démarre le serveur en essayant les ports de {@code preferredPort} à {@code maxPort} inclus.
     * @throws IllegalStateException si aucun port n'est libre dans l'intervalle.
     */
    public static CourseSiteServer start(Path siteDir, int preferredPort, int maxPort) {
        Path root = siteDir.toAbsolutePath().normalize();
        UncheckedIOException last = null;
        for (int port = preferredPort; port <= maxPort; port++) {
            try {
                InetSocketAddress addr =
                    new InetSocketAddress(InetAddress.getLoopbackAddress(), port);
                HttpServer s = SimpleFileServer.createFileServer(
                    addr, root, SimpleFileServer.OutputLevel.NONE);
                s.start();
                return new CourseSiteServer(s, URI.create("http://127.0.0.1:" + port + "/"));
            } catch (UncheckedIOException e) {
                last = e; // port probablement occupé : on tente le suivant
            }
        }
        throw new IllegalStateException(
            "Aucun port libre entre " + preferredPort + " et " + maxPort, last);
    }

    public URI url() {
        return url;
    }

    public void stop() {
        server.stop(0);
    }

    @Override
    public void close() {
        stop();
    }
}
```

- [ ] **Step 4: Run test to verify it passes**

Run: `mvn -f moulinette/pom.xml -pl console -q test -Dtest=CourseSiteServerTest`
Expected: PASS (2 tests verts).

- [ ] **Step 5: Commit**

```bash
git add moulinette/console/src/main/java/etnc/piscine/moulinette/console/repl/CourseSiteServer.java moulinette/console/src/test/java/etnc/piscine/moulinette/console/repl/CourseSiteServerTest.java
git commit -m "feat(#56): CourseSiteServer — service statique in-process du site de cours"
```

---

## Task 2: Brancher `--site` dans `Main.runRepl` (ouverture navigateur + arrêt propre)

**Files:**
- Modify: `moulinette/console/src/main/java/etnc/piscine/moulinette/console/Main.java`

Note : pas de test unitaire ici (effets de bord : démarrage serveur, ouverture navigateur, `System.exit`). La logique testable vit dans `CourseSiteServer` (Task 1). On valide en bout de chaîne (Task 6).

- [ ] **Step 1: Modifier `runRepl` pour démarrer/arrêter le site**

Remplacer le corps de `runRepl` (à partir de la ligne `var ctx = ...`) pour envelopper l'exécution du REPL :

```java
        var ctx = new ReplContext(repo, git, catalog, Mode.LOCAL);
        var repl = new Repl(ctx, CommandRegistry.defaults(trigger), ReplIo.stdio());

        String siteArg = optional(args, "--site", null);
        CourseSiteServer site = startSiteIfRequested(siteArg);
        try {
            repl.run();
        } finally {
            if (site != null) {
                site.stop();
            }
        }
        return 0;
```

Ajouter l'import en tête de fichier (avec les autres imports `repl`) :

```java
import etnc.piscine.moulinette.console.repl.CourseSiteServer;
```

Et ajouter ces deux méthodes privées dans la classe `Main` :

```java
    private static CourseSiteServer startSiteIfRequested(String siteArg) {
        if (siteArg == null) {
            return null;
        }
        Path siteDir = Paths.get(siteArg);
        if (!java.nio.file.Files.isDirectory(siteDir)) {
            System.out.println("[console] Site de cours introuvable (" + siteDir
                + ") — démarrage sans site.");
            return null;
        }
        CourseSiteServer server = CourseSiteServer.start(siteDir, 8800, 8810);
        System.out.println("[console] Site de cours : " + server.url());
        tryOpenBrowser(server.url().toString());
        return server;
    }

    private static void tryOpenBrowser(String url) {
        String os = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
        List<String> cmd;
        if (os.contains("win")) {
            cmd = List.of("cmd", "/c", "start", "", url);
        } else if (os.contains("mac")) {
            cmd = List.of("open", url);
        } else {
            cmd = List.of("xdg-open", url);
        }
        try {
            new ProcessBuilder(cmd).inheritIO().start();
        } catch (Exception e) {
            System.out.println("[console] Ouvre le site manuellement : " + url);
        }
    }
```

- [ ] **Step 2: Mettre à jour le `printUsage()`**

Dans le texte de `printUsage()`, remplacer la ligne `repl` par :

```
              moulinette-console repl --repo <dossier> [--piscine-repo <chemin>] [--site <dossier-site>] [--mode local]
```

- [ ] **Step 3: Compiler + lancer toute la suite console**

Run: `mvn -f moulinette/pom.xml -pl console -am -q test`
Expected: PASS (suite existante + `CourseSiteServerTest` verts, aucune régression).

- [ ] **Step 4: Vérifier manuellement le service du site**

```bash
mvn -f moulinette/pom.xml -pl console -am -q -DskipTests package
# Sert exercises/ comme faux "site" juste pour vérifier le démarrage + l'URL :
echo "" | java -jar moulinette/console/target/moulinette-console.jar repl --repo . --piscine-repo . --site courses/build 2>&1 | head -5
```
Expected : une ligne `[console] Site de cours : http://127.0.0.1:8800/` s'affiche (le REPL se ferme sur EOF). Si `courses/build` n'existe pas encore, le message « introuvable » s'affiche — c'est attendu, il sera généré en Task 5.

- [ ] **Step 5: Commit**

```bash
git add moulinette/console/src/main/java/etnc/piscine/moulinette/console/Main.java
git commit -m "feat(#56): option --site du REPL (serveur de cours + ouverture navigateur)"
```

---

## Task 3: Étendre `build-bundle.ps1` (site + MinGit + nouvelle structure + launcher)

**Files:**
- Modify: `scripts/build-bundle.ps1`

- [ ] **Step 1: Ajouter les constantes MinGit + le flag de rebuild du site**

En tête, après le bloc `param(...)`, étendre les paramètres et ajouter les constantes :

```powershell
param(
    [string] $JdkPath = $env:JAVA_HOME,
    [string] $OutDir = "",
    [switch] $RebuildSite
)
$ErrorActionPreference = "Stop"

# MinGit épinglé (git-for-windows). Pour (re)trouver le SHA-256 officiel :
#   Invoke-WebRequest $MinGitUrl -OutFile mingit.zip ; (Get-FileHash mingit.zip -Algorithm SHA256).Hash
$MinGitVersion = "2.47.1"
$MinGitUrl = "https://github.com/git-for-windows/git/releases/download/v$MinGitVersion.windows.1/MinGit-$MinGitVersion-64-bit.zip"
$MinGitSha256 = "RENSEIGNER_LE_SHA256_OFFICIEL"
```

> À l'exécution de cette task : lancer la commande du commentaire une fois, copier le hash réel dans `$MinGitSha256`. C'est une **valeur de configuration**, pas un placeholder de logique.

- [ ] **Step 2: Adapter le staging à la nouvelle structure**

Remplacer la création des dossiers (`New-Item ... "$Stage\app",...`) par :

```powershell
if (Test-Path $Stage) { Remove-Item -Recurse -Force $Stage }
New-Item -ItemType Directory -Force -Path "$Stage\app","$Stage\piscine\docs","$Stage\workspace" | Out-Null
```
(inchangé — `site` et `git` sont créés par leurs étapes via Expand/Copy).

- [ ] **Step 3: Générer + copier le site Docusaurus**

Juste après la copie du JDK (`Copy-Item -Recurse $JdkPath "$Stage\jdk"`), insérer :

```powershell
$SiteBuild = Join-Path $RepoRoot "courses\build"
if ($RebuildSite -or -not (Test-Path "$SiteBuild\index.html")) {
    Write-Host "[bundle] Génération du site de cours (npm) ..."
    if (-not (Get-Command npm -ErrorAction SilentlyContinue)) {
        throw "npm introuvable : installe Node.js pour construire le site de cours."
    }
    Push-Location "$RepoRoot\courses"
    try {
        & npm ci; if ($LASTEXITCODE -ne 0) { throw "npm ci échoué." }
        & npm run build; if ($LASTEXITCODE -ne 0) { throw "npm run build échoué." }
    } finally { Pop-Location }
}
Write-Host "[bundle] Copie du site de cours ..."
Copy-Item -Recurse $SiteBuild "$Stage\site"
```

- [ ] **Step 4: Télécharger + vérifier + extraire MinGit**

Juste après l'étape site, insérer :

```powershell
Write-Host "[bundle] Téléchargement de MinGit $MinGitVersion ..."
$MinGitZip = Join-Path $env:TEMP "mingit-$MinGitVersion.zip"
if (-not (Test-Path $MinGitZip)) {
    Invoke-WebRequest -Uri $MinGitUrl -OutFile $MinGitZip
}
$actualSha = (Get-FileHash $MinGitZip -Algorithm SHA256).Hash.ToLower()
if ($actualSha -ne $MinGitSha256.ToLower()) {
    throw "Checksum MinGit invalide : attendu $MinGitSha256, obtenu $actualSha"
}
Write-Host "[bundle] Extraction de MinGit ..."
Expand-Archive -Path $MinGitZip -DestinationPath "$Stage\git" -Force
```

- [ ] **Step 5: Réécrire le launcher `piscine.bat`**

Remplacer le bloc here-string qui génère `piscine.bat` par :

```powershell
@'
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
'@ | Set-Content -Encoding ascii "$Stage\piscine.bat"
```

- [ ] **Step 6: Réécrire le launcher `piscine.sh` (symétrique)**

Remplacer le bloc here-string `piscine.sh` par :

```powershell
@'
#!/usr/bin/env bash
HERE="$(cd "$(dirname "$0")" && pwd)"
export JAVA_HOME="$HERE/jdk"
export PATH="$HERE/jdk/bin:$HERE/git/cmd:$PATH"
JAVA="$HERE/jdk/bin/java"
JAR="$HERE/app/moulinette-console.jar"
WS="$HERE/workspace"
if [ ! -d "$WS/.git" ]; then
  "$JAVA" -jar "$JAR" init --nom stagiaire --dest "$WS" --piscine-repo "$HERE/piscine"
fi
"$JAVA" -jar "$JAR" repl --repo "$WS" --piscine-repo "$HERE/piscine" --site "$HERE/site"
'@ | Set-Content -Encoding ascii "$Stage\piscine.sh"
```

- [ ] **Step 7: Mettre à jour `LISEZMOI.txt`**

Remplacer le here-string `LISEZMOI.txt` par :

```powershell
@'
Piscine ETNC — version autonome (standalone)

Double-cliquez sur piscine.bat (Windows) pour demarrer.
Au premier lancement, votre espace de travail est cree automatiquement.
Le site de cours s'ouvre dans votre navigateur (sinon, ouvrez l'URL affichee dans la console).
Tapez `help` dans la console pour la liste des commandes, `exit` pour quitter.
Vos rapports de correction : workspace\.piscine\reports\
Guide complet : piscine\docs\piscine-stagiaire.md

Aucune installation ni connexion reseau requise : tout est dans ce dossier.
'@ | Set-Content -Encoding utf8 "$Stage\LISEZMOI.txt"
```

- [ ] **Step 8: Commit**

```bash
git add scripts/build-bundle.ps1
git commit -m "feat(#56): build-bundle.ps1 — site de cours, MinGit, launcher unique"
```

---

## Task 4: Étendre `build-bundle.sh` (symétrique)

**Files:**
- Modify: `scripts/build-bundle.sh`

- [ ] **Step 1: Ajouter le flag `--rebuild-site` + constantes MinGit**

Dans la boucle de parsing d'arguments, ajouter un cas, et déclarer les constantes après le `while` :

```bash
REBUILD_SITE=0
while [[ $# -gt 0 ]]; do
  case "$1" in
    --jdk) JDK_PATH="$2"; shift 2;;
    --out) OUT_DIR="$2"; shift 2;;
    --rebuild-site) REBUILD_SITE=1; shift;;
    *) echo "Argument inconnu : $1"; exit 2;;
  esac
done

# MinGit épinglé (git-for-windows). SHA-256 officiel : sha256sum mingit.zip
MINGIT_VERSION="2.47.1"
MINGIT_URL="https://github.com/git-for-windows/git/releases/download/v${MINGIT_VERSION}.windows.1/MinGit-${MINGIT_VERSION}-64-bit.zip"
MINGIT_SHA256="RENSEIGNER_LE_SHA256_OFFICIEL"
```

> Même valeur SHA-256 qu'en Task 3 (asset identique). La renseigner ici aussi.

- [ ] **Step 2: Générer + copier le site, après la copie du JDK**

Après `cp -r "$JDK_PATH" "$STAGE/jdk"`, insérer :

```bash
SITE_BUILD="$REPO_ROOT/courses/build"
if [[ "$REBUILD_SITE" -eq 1 || ! -f "$SITE_BUILD/index.html" ]]; then
  command -v npm >/dev/null 2>&1 || { echo "npm introuvable : installe Node.js."; exit 1; }
  echo "[bundle] Génération du site de cours (npm) ..."
  ( cd "$REPO_ROOT/courses" && npm ci && npm run build )
fi
echo "[bundle] Copie du site de cours ..."
cp -r "$SITE_BUILD" "$STAGE/site"
```

- [ ] **Step 3: Télécharger + vérifier + extraire MinGit**

Juste après l'étape site, insérer :

```bash
echo "[bundle] Téléchargement de MinGit $MINGIT_VERSION ..."
MINGIT_ZIP="$(mktemp -u).zip"
curl -fSL "$MINGIT_URL" -o "$MINGIT_ZIP"
echo "${MINGIT_SHA256}  ${MINGIT_ZIP}" | sha256sum -c - || { echo "Checksum MinGit invalide."; exit 1; }
echo "[bundle] Extraction de MinGit ..."
mkdir -p "$STAGE/git"
unzip -q "$MINGIT_ZIP" -d "$STAGE/git"
```

- [ ] **Step 4: Mettre à jour le launcher `piscine.sh` + LISEZMOI**

Remplacer le here-doc `piscine.sh` par :

```bash
cat > "$STAGE/piscine.sh" <<'EOF'
#!/usr/bin/env bash
HERE="$(cd "$(dirname "$0")" && pwd)"
export JAVA_HOME="$HERE/jdk"
export PATH="$HERE/jdk/bin:$HERE/git/cmd:$PATH"
JAVA="$HERE/jdk/bin/java"
JAR="$HERE/app/moulinette-console.jar"
WS="$HERE/workspace"
if [ ! -d "$WS/.git" ]; then
  "$JAVA" -jar "$JAR" init --nom stagiaire --dest "$WS" --piscine-repo "$HERE/piscine"
fi
"$JAVA" -jar "$JAR" repl --repo "$WS" --piscine-repo "$HERE/piscine" --site "$HERE/site"
EOF
chmod +x "$STAGE/piscine.sh"

cat > "$STAGE/LISEZMOI.txt" <<'EOF'
Piscine ETNC — version autonome (standalone)
Lancez ./piscine.sh (ou piscine.bat sous Windows) pour demarrer.
Le site de cours s'ouvre dans le navigateur (sinon URL affichee dans la console).
Votre espace est cree au premier lancement. `help` pour les commandes, `exit` pour quitter.
Rapports : workspace/.piscine/reports/  — Guide : piscine/docs/piscine-stagiaire.md
Aucune installation ni reseau requis : tout est dans ce dossier.
EOF
```

- [ ] **Step 5: Commit**

```bash
git add scripts/build-bundle.sh
git commit -m "feat(#56): build-bundle.sh — site de cours, MinGit, launcher (symétrie .ps1)"
```

---

## Task 5: Régénérer le site + build complet + validation locale

**Files:** aucun fichier source (étape de validation). Produit `dist/piscine-etnc-stagiaire-*.zip`.

- [ ] **Step 1: Générer le site de cours**

Run:
```bash
cd courses && npm ci && npm run build && cd ..
```
Expected: `courses/build/index.html` existe.

- [ ] **Step 2: Construire le bundle complet (PowerShell)**

Run (PowerShell, JDK 25 local) :
```powershell
.\scripts\build-bundle.ps1 -JdkPath "E:\java\jdk-25.0.3+9"
```
Expected: se termine par `[bundle] OK : ...\dist\piscine-etnc-stagiaire-<date>.zip`.

- [ ] **Step 3: Vérifier la structure du ZIP**

Run:
```powershell
$d = "dist\piscine-etnc-stagiaire"
Test-Path "$d\app\moulinette-console.jar","$d\jdk\bin\java.exe","$d\git\cmd\git.exe","$d\site\index.html","$d\piscine\exercises","$d\piscine.bat"
```
Expected: six `True`. (Si `git\cmd\git.exe` n'est pas le bon chemin, ajuster le `PATH` du launcher au sous-dossier réel de MinGit — vérifier avec `Get-ChildItem "$d\git" -Recurse -Filter git.exe`.)

- [ ] **Step 4: Lancer le bundle depuis le dossier stagé (smoke local)**

Run:
```powershell
cd dist\piscine-etnc-stagiaire
echo "exit" | .\piscine.bat
cd ..\..
```
Expected: init du workspace au 1er lancement, ligne `[console] Site de cours : http://127.0.0.1:8800/`, puis sortie propre du REPL. Vérifier `dist\piscine-etnc-stagiaire\workspace\.git` créé.

- [ ] **Step 5: Vérifier un rendu d'exercice évalué (boucle moulinette)**

Run (toujours dans `dist\piscine-etnc-stagiaire`) :
```
echo submit-start 1.1
```
puis suivre le guide stagiaire (add/commit/push d'un exo) et confirmer qu'un rapport apparaît dans `workspace\.piscine\reports\`. Critère §7 de la spec.

> **Validation « machine propre » (manuelle, formateur) :** copier le ZIP sur une machine Windows sans Java/git/Node, dézipper, lancer `piscine.bat` hors-ligne. Cocher quand fait (cf. brief #56 §2.D / tâches #40/#54).

- [ ] **Step 6: Pas de commit** (artefacts `dist/` ignorés — vérifier que `dist/` est bien dans `.gitignore`, sinon l'ajouter et committer ce seul changement).

---

## Task 6: Documentation — les deux modes + commandes ZIP

**Files:**
- Create: `courses/docs/lancer-la-piscine.md`
- Modify: `courses/docs/intro.md`
- Modify: `docs/piscine-stagiaire.md`
- Modify: `README.md`
- Modify: `CONTRIBUTING.md`
- Modify: `docs/setup-dev.md`
- Modify: `docs/backlog.md`

- [ ] **Step 1: Créer la page « Comment lancer la Piscine »**

Créer `courses/docs/lancer-la-piscine.md` :

```markdown
---
id: lancer-la-piscine
sidebar_position: 2
title: "Comment lancer la Piscine"
slug: /lancer-la-piscine
---

# Comment lancer la Piscine

Il existe **deux façons** d'utiliser la Piscine. Tu n'en utilises **qu'une seule** — celle qui correspond à ce que ton formateur t'a remis.

| Tu as reçu… | Ton mode | Ce que tu fais |
|-------------|----------|----------------|
| un **fichier ZIP** | **Standalone** | Tu décompresses et tu lances. **Rien à installer, aucune connexion.** |
| un **accès au dépôt Git** | **Dépôt** | Tu installes Java 25 + git, tu clones, tu lances le bootstrap. |

> Dans les deux cas, **la façon de travailler est identique** : tu édites un exercice, tu le rends via Git, et la moulinette te répond avec un rapport. Seule la mise en route change.

## Mode standalone (ZIP)

1. Décompresse le ZIP `piscine-etnc-stagiaire-*.zip` où tu veux.
2. Double-clique sur **`piscine.bat`** (Windows).
3. Au premier lancement, ton espace de travail est créé automatiquement, **le site de cours s'ouvre dans ton navigateur** et la **console de correction** démarre.
4. Si le navigateur ne s'ouvre pas tout seul, ouvre l'adresse affichée dans la console (par défaut `http://127.0.0.1:8800/`).

Tout est embarqué (Java, git, cours, moulinette) : **aucune installation, aucun réseau**.

## Mode dépôt (clone du projet)

1. Installe **Java 25** et **git** (voir le guide d'installation du dépôt).
2. Clone le dépôt, puis lance le script de bootstrap qui prépare ton espace de travail.
3. Lance la console de correction (REPL) avec la commande affichée par le bootstrap.

Le détail pas-à-pas des deux modes est dans le **[guide stagiaire complet](https://github.com/Benjamin-Curlier/Piscine-ETNC/blob/main/docs/piscine-stagiaire.md)**.

## Ensuite : la boucle de travail

Quel que soit ton mode :

1. Lis le chapitre de cours.
2. Fais l'exercice.
3. Rends via Git (`submit-start`, puis `add` / `commit` / `push`).
4. Lis le **rapport** de la moulinette.
5. Passe au chapitre suivant.
```

- [ ] **Step 2: Ajuster `courses/docs/intro.md`**

Dans `courses/docs/intro.md`, remplacer la puce « Un ordinateur… installer **Java 25** » de la section « Ce dont vous aurez besoin » par :

```markdown
- Un ordinateur (Windows, Linux ou macOS). **Selon ce que ton formateur te remet, tu n'auras peut-être rien à installer** : voir **[Comment lancer la Piscine](lancer-la-piscine.md)**.
```

Et ajouter, juste sous le titre `# Bienvenue à la Piscine ETNC`, une ligne :

```markdown
> 👉 Pressé de démarrer ? Va directement à **[Comment lancer la Piscine](lancer-la-piscine.md)**.
```

- [ ] **Step 3: Finaliser la section standalone du guide stagiaire**

Dans `docs/piscine-stagiaire.md`, remplacer le bloc d'avertissement 🚧 (la citation `> 🚧 **En cours de finalisation**...`) par un pas-à-pas définitif :

```markdown
Le ZIP `piscine-etnc-stagiaire-*.zip` embarque **tout** : un JDK, git, la moulinette pré-compilée et le **site de cours servi en local**. Aucune installation, aucun réseau.
```

Et remplacer la liste « Principe (cible) » par :

```markdown
1. Décompresse le ZIP `piscine-etnc-stagiaire-*.zip` où tu veux.
2. Lance le launcher (Windows : double-clic sur `piscine.bat` ; Unix : `./piscine.sh`).
3. Au premier lancement, ton espace de travail est créé, **le site de cours s'ouvre dans ton navigateur** (sinon, ouvre l'URL affichée, par défaut `http://127.0.0.1:8800/`) et la **console** démarre.
4. Tu travailles exactement comme aux sections **4 à 8** ci-dessous (`submit-start`, rendu Git, rapport).
```

- [ ] **Step 4: Retirer la mention « en cours » du README**

Dans `README.md`, supprimer entièrement la ligne de citation `> **Le mode standalone est en cours de finalisation** ...` (lignes ~22-23) et la remplacer par :

```markdown
> Le mode standalone est produit par [`scripts/build-bundle.ps1`](scripts/build-bundle.ps1) / [`.sh`](scripts/build-bundle.sh) (voir [`CONTRIBUTING.md`](CONTRIBUTING.md#produire-le-bundle-standalone)).
```

- [ ] **Step 5: Documenter la production du bundle dans CONTRIBUTING.md**

Ajouter à la fin de `CONTRIBUTING.md` une nouvelle section :

```markdown
## 7. Produire le bundle standalone

Le **mode standalone** est un ZIP autonome (JDK + git + site de cours + moulinette) que le stagiaire lance sans rien installer. Il est produit par `scripts/build-bundle`.

**Prérequis (côté formateur, machine de build) :**
- un **JDK 25** local (chemin passé en argument) ;
- **Node.js** (pour générer le site de cours via `npm run build`) ;
- un **accès réseau** au premier build (téléchargement de MinGit, version épinglée + SHA-256 vérifié).

**Commandes :**

```powershell
# Windows (PowerShell)
.\scripts\build-bundle.ps1 -JdkPath "C:\chemin\jdk-25" [-OutDir dist] [-RebuildSite]
```

```bash
# Linux / macOS
./scripts/build-bundle.sh --jdk /chemin/jdk-25 [--out dist] [--rebuild-site]
```

- `-JdkPath` / `--jdk` : JDK 25 portable à embarquer (obligatoire ou via `JAVA_HOME`).
- `-OutDir` / `--out` : dossier de sortie (défaut : `dist/`).
- `-RebuildSite` / `--rebuild-site` : force la régénération du site de cours même si `courses/build/` existe.

Le ZIP est écrit dans `dist/piscine-etnc-stagiaire-<AAAAMMJJ>.zip`.

**Tester le ZIP :** décompresser sur une machine Windows **sans Java/git/Node**, lancer `piscine.bat`, vérifier que le site s'ouvre, que le REPL démarre et qu'un rendu d'exercice est évalué — **hors-ligne**.
```

- [ ] **Step 6: Préciser `setup-dev.md`**

Ajouter en tête de `docs/setup-dev.md`, juste après le titre, une note :

```markdown
> Ce guide concerne le **mode dépôt** (développement / contribution). Si tu utilises la Piscine via le **ZIP standalone**, tu n'as **rien à installer** : voir [`piscine-stagiaire.md`](piscine-stagiaire.md#mode-standalone-zip).
```

- [ ] **Step 7: Marquer #56 fait dans le backlog**

Dans `docs/backlog.md`, passer l'entrée #56 à l'état « fait » (suivre le format des entrées déjà cochées du fichier — vérifier le format exact en lisant la ligne #56 et une entrée déjà faite, ex. #18).

- [ ] **Step 8: Régénérer le site (pour intégrer les nouvelles pages au build versionné si applicable) et vérifier**

Run:
```bash
cd courses && npm run build && cd ..
```
Expected: build OK, `courses/build/lancer-la-piscine/index.html` généré.

- [ ] **Step 9: Commit**

```bash
git add courses/docs/lancer-la-piscine.md courses/docs/intro.md docs/piscine-stagiaire.md README.md CONTRIBUTING.md docs/setup-dev.md docs/backlog.md
git commit -m "docs(#56): les deux modes côté stagiaire + commandes du bundle standalone"
```

---

## Task 7: Vérification finale + PR

- [ ] **Step 1: Suite complète + lint**

Run: `mvn -f moulinette/pom.xml -q verify`
Expected: BUILD SUCCESS (tous modules, IT compris).

- [ ] **Step 2: Vérifier le CI localement (build Docusaurus)**

Run: `cd courses && npm run build && cd ..`
Expected: succès (le job CI `build-docusaurus` ne cassera pas).

- [ ] **Step 3: Pousser la branche + ouvrir la PR**

```bash
git push -u origin feature/standalone-bundle
gh pr create --title "feat(#56): bundle standalone (site local + MinGit + launcher) & doc des deux modes" --body "$(cat <<'EOF'
Finalise le mode standalone (#56) : ZIP autonome hors-ligne (JDK + git portable + site de cours servi en local + moulinette) lançable sur Windows vierge, et documentation des deux modes côté stagiaire.

## Contenu
- `CourseSiteServer` : service in-process du site Docusaurus (SimpleFileServer), option `--site` du REPL, ouverture navigateur + fallback de port.
- `build-bundle.ps1`/`.sh` : régénération du site, MinGit épinglé (SHA-256 vérifié), launcher Windows unique, nouvelle structure de ZIP.
- Docs : nouvelle page « Comment lancer la Piscine », guide stagiaire finalisé, README/CONTRIBUTING/setup-dev mis à jour, backlog #56 fait.

## Validation
- Suite moulinette verte (`mvn verify`), build Docusaurus OK.
- Bundle construit et lancé en local (site + REPL + rendu évalué).
- Validation machine Windows propre : à confirmer par le formateur (§2.D).

🤖 Generated with [Claude Code](https://claude.com/claude-code)
EOF
)"
```

---

## Self-review (vérifié)

- **Couverture spec** : §4 → Task 1+2 ; §5 → Task 3+4 ; §6 launcher → Task 3.5/3.6/4.4 ; §7 validation → Task 5 ; §8 erreurs → couvertes (port fallback Task 1, checksum/npm Task 3-4, site absent Task 2, navigateur Task 2) ; §9 tests → Task 1 + Task 7.1 ; §10 docs → Task 6 ; §11 hors-scope respecté (pas de Maven embarqué, pas de sous-commande `site`).
- **Placeholders** : `RENSEIGNER_LE_SHA256_OFFICIEL` est une **valeur de configuration** à renseigner via la commande fournie (Task 3.1) — pas un trou de logique.
- **Cohérence des types** : `CourseSiteServer.start(Path, int, int)` / `url()` / `stop()` / `close()` identiques entre Task 1 (def) et Task 2 (usage). Option `--site` lue via `optional(args, "--site", null)` cohérente avec le parsing existant de `Main`.
