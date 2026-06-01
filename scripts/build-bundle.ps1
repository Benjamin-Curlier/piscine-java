# Construit le bundle ZIP portable auto-suffisant de la Piscine ETNC.
# Usage : .\scripts\build-bundle.ps1 [-JdkPath <chemin JDK 25>] [-OutDir <dossier>]
param(
    [string] $JdkPath = $env:JAVA_HOME,
    [string] $OutDir = ""
)
$ErrorActionPreference = "Stop"
$RepoRoot = (Resolve-Path "$PSScriptRoot\..").Path
if (-not $OutDir) { $OutDir = Join-Path $RepoRoot "dist" }
$Stage = Join-Path $OutDir "piscine-etnc-stagiaire"

if (-not (Test-Path "$JdkPath\bin\java.exe")) {
    Write-Error "JDK introuvable à '$JdkPath'. Passe -JdkPath <chemin d'un JDK 25>."
}

# 1. Build de l'uber-jar (préfère mvn système)
$MvnCmd = if (Get-Command mvn -ErrorAction SilentlyContinue) { "mvn" } else { "$RepoRoot\mvnw.cmd" }
Write-Host "[bundle] Build de l'uber-jar (via $MvnCmd) ..."
& $MvnCmd -f "$RepoRoot\moulinette\pom.xml" -pl console -am -q -DskipTests package
if ($LASTEXITCODE -ne 0) { throw "Build Maven échoué." }
$Jar = "$RepoRoot\moulinette\console\target\moulinette-console.jar"
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

# 3. Lanceurs + LISEZMOI
@'
@echo off
setlocal
set "JAVA=%~dp0jdk\bin\java.exe"
set "JAR=%~dp0app\moulinette-console.jar"
set "WS=%~dp0workspace"
if not exist "%WS%\.git" (
    "%JAVA%" -jar "%JAR%" init --nom stagiaire --dest "%WS%" --piscine-repo "%~dp0piscine"
)
"%JAVA%" -jar "%JAR%" repl --repo "%WS%" --piscine-repo "%~dp0piscine"
endlocal
'@ | Set-Content -Encoding ascii "$Stage\piscine.bat"

@'
#!/usr/bin/env bash
HERE="$(cd "$(dirname "$0")" && pwd)"
JAVA="$HERE/jdk/bin/java"
JAR="$HERE/app/moulinette-console.jar"
WS="$HERE/workspace"
if [ ! -d "$WS/.git" ]; then
  "$JAVA" -jar "$JAR" init --nom stagiaire --dest "$WS" --piscine-repo "$HERE/piscine"
fi
"$JAVA" -jar "$JAR" repl --repo "$WS" --piscine-repo "$HERE/piscine"
'@ | Set-Content -Encoding ascii "$Stage\piscine.sh"

@'
Piscine ETNC — console locale

Double-cliquez sur piscine.bat (Windows) pour demarrer.
Au premier lancement, votre espace de travail est cree automatiquement.
Tapez `help` dans la console pour la liste des commandes, `exit` pour quitter.
Vos rapports de correction : workspace\.piscine\reports\
Guide complet : piscine\docs\piscine-stagiaire.md
'@ | Set-Content -Encoding utf8 "$Stage\LISEZMOI.txt"

# 4. Zip
$Stamp = Get-Date -Format "yyyyMMdd"
$Zip = Join-Path $OutDir "piscine-etnc-stagiaire-$Stamp.zip"
if (Test-Path $Zip) { Remove-Item -Force $Zip }
Write-Host "[bundle] Compression -> $Zip"
Compress-Archive -Path $Stage -DestinationPath $Zip
Write-Host "[bundle] OK : $Zip"
