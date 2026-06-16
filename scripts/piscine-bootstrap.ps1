# Bootstrap autonome de la Piscine Java en mode local (Windows).
# Usage : .\scripts\piscine-bootstrap.ps1 -Nom <slug> [-Dest <dossier>] [-Force]

param(
    [Parameter(Mandatory=$true)] [string] $Nom,
    [string] $Dest = "",
    [switch] $Force
)

$ErrorActionPreference = "Stop"
$RepoRoot = (Resolve-Path "$PSScriptRoot\..").Path
if (-not $Dest) { $Dest = Join-Path (Split-Path $RepoRoot -Parent) "piscine-$Nom" }

# Résolution de java : préfère $env:JAVA_HOME\bin\java (utile si le java du PATH est trop ancien)
$JavaBin = "java"
if ($env:JAVA_HOME -and (Test-Path "$env:JAVA_HOME\bin\java.exe")) {
    $JavaBin = "$env:JAVA_HOME\bin\java.exe"
}

if (-not (Get-Command git -ErrorAction SilentlyContinue)) {
    Write-Error "git introuvable. Voir docs/setup-dev.md."
}

$javaVer = (& $JavaBin -version 2>&1)[0]
if ($javaVer -notmatch 'version "(\d+)') {
    Write-Error "Impossible de parser la version de Java ($JavaBin)."
}
if ([int]$Matches[1] -lt 25) {
    Write-Error "Java 25+ requis (détecté : $($Matches[1])). Voir docs/setup-dev.md. Astuce : définis JAVA_HOME vers un JDK 25."
}

$Jar = "$RepoRoot\moulinette\console\build\libs\moulinette-console.jar"

if ((Test-Path $Dest) -and -not $Force) {
    Write-Host "Workspace déjà présent : $Dest"
    Write-Host "Relance avec -Force pour réinitialiser, ou démarre le REPL :"
    Write-Host "  & `"$JavaBin`" -jar `"$Jar`" repl --repo `"$Dest`" --piscine-repo `"$RepoRoot`""
    return
}

if ((Test-Path $Dest) -and $Force) {
    Write-Host "[bootstrap] Suppression du workspace existant : $Dest"
    Remove-Item -Recurse -Force $Dest
}

Write-Host "[bootstrap] Build moulinette-console (via gradlew) ..."
& "$RepoRoot\moulinette\gradlew.bat" -p "$RepoRoot\moulinette" -q :console:shadowJar
if ($LASTEXITCODE -ne 0) { throw "Build Gradle échoué." }

if (-not (Test-Path $Jar)) { throw "Build du jar échoué : $Jar introuvable." }

Write-Host "[bootstrap] Initialisation du workspace stagiaire ..."
& $JavaBin -jar $Jar init --nom $Nom --dest $Dest --piscine-repo $RepoRoot

Write-Host ""
Write-Host "[bootstrap] OK. Lance le REPL :"
Write-Host "  & `"$JavaBin`" -jar `"$Jar`" repl --repo `"$Dest`" --piscine-repo `"$RepoRoot`""
