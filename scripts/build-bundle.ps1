# Construit le bundle ZIP portable auto-suffisant de la Piscine ETNC.
# Usage : .\scripts\build-bundle.ps1 [-JdkPath <chemin JDK 25>] [-OutDir <dossier>]
param(
    [string] $JdkPath = $env:JAVA_HOME,
    [string] $OutDir = "",
    [switch] $RebuildSite
)
$ErrorActionPreference = "Stop"

# MinGit épinglé (git-for-windows). Pour (re)trouver le SHA-256 officiel :
#   Invoke-WebRequest $MinGitUrl -OutFile mingit.zip ; (Get-FileHash mingit.zip -Algorithm SHA256).Hash
$MinGitVersion = "2.54.0"
$MinGitUrl = "https://github.com/git-for-windows/git/releases/download/v$MinGitVersion.windows.1/MinGit-$MinGitVersion-64-bit.zip"
$MinGitSha256 = "04f937e1f0918b17b9be6f2294cb2bb66e96e1d9832d1c298e2de088a1d0e668"
$RepoRoot = (Resolve-Path "$PSScriptRoot\..").Path
if (-not $OutDir) { $OutDir = Join-Path $RepoRoot "dist" }
$Stage = Join-Path $OutDir "piscine-etnc-stagiaire"

if (-not (Test-Path "$JdkPath\bin\java.exe")) {
    Write-Error "JDK introuvable à '$JdkPath'. Passe -JdkPath <chemin d'un JDK 25>."
}

# 1. Build de l'uber-jar via le wrapper Gradle (versionné, fonctionne offline)
Write-Host "[bundle] Build de l'uber-jar (via gradlew) ..."
& "$RepoRoot\moulinette\gradlew.bat" -p "$RepoRoot\moulinette" -q :console:shadowJar
if ($LASTEXITCODE -ne 0) { throw "Build Gradle échoué." }
$Jar = "$RepoRoot\moulinette\console\build\libs\moulinette-console.jar"
if (-not (Test-Path $Jar)) { throw "Uber-jar introuvable : $Jar" }

# 2. Staging
if (Test-Path $Stage) { Remove-Item -Recurse -Force $Stage }
New-Item -ItemType Directory -Force -Path "$Stage\app","$Stage\piscine\docs","$Stage\workspace" | Out-Null

Write-Host "[bundle] Copie de l'uber-jar ..."
Copy-Item $Jar "$Stage\app\moulinette-console.jar"

Write-Host "[bundle] Copie des exercices + guide stagiaire ..."
Copy-Item -Recurse "$RepoRoot\exercises" "$Stage\piscine\exercises"
Copy-Item "$RepoRoot\docs\piscine-stagiaire.md" "$Stage\piscine\docs\piscine-stagiaire.md"

Write-Host "[bundle] Copie du JDK portable (peut prendre un moment) ..."
Copy-Item -Recurse $JdkPath "$Stage\jdk"

# 2b. Site de cours (Docusaurus)
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

# 2c. git portable (MinGit, téléchargé + vérifié)
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

# 3. Lanceurs + LISEZMOI
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

# 4. Zip
$Stamp = Get-Date -Format "yyyyMMdd"
$Zip = Join-Path $OutDir "piscine-etnc-stagiaire-$Stamp.zip"
if (Test-Path $Zip) { Remove-Item -Force $Zip }
Write-Host "[bundle] Compression -> $Zip"
Compress-Archive -Path $Stage -DestinationPath $Zip
Write-Host "[bundle] OK : $Zip"
