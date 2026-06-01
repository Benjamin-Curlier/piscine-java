# Prépare l'environnement de développement Piscine ETNC sous Windows, sans droits admin.
# Vérifie git et Node, installe un JDK Temurin 25 portable si Java 25+ est absent,
# configure JAVA_HOME / PATH au niveau utilisateur, puis vérifie le Maven Wrapper.
# Usage : .\scripts\setup-dev.ps1 [-JdkDir <dossier>] [-Force]
#   -JdkDir : où installer le JDK portable (défaut : %USERPROFILE%\tools\jdk-25)
#   -Force  : réinstalle le JDK même si un Java 25+ est déjà détecté

param(
    [string] $JdkDir = "$env:USERPROFILE\tools\jdk-25",
    [switch] $Force
)

$ErrorActionPreference = "Stop"
$RepoRoot = (Resolve-Path "$PSScriptRoot\..").Path

function Get-JavaMajor([string] $javaExe) {
    try {
        $line = (& $javaExe -version 2>&1)[0]
        if ($line -match 'version "(\d+)') { return [int]$Matches[1] }
    } catch { }
    return 0
}

Write-Host "== Setup dev Piscine ETNC ==" -ForegroundColor Cyan

# 1. git (indispensable)
if (-not (Get-Command git -ErrorAction SilentlyContinue)) {
    Write-Error "git introuvable. Installe Git for Windows (portable possible) puis relance. Voir docs/setup-dev.md."
}
Write-Host "OK git    : $(git --version)"

# 2. Node (nécessaire seulement pour construire le site Docusaurus — avertissement, pas blocage)
$node = Get-Command node -ErrorAction SilentlyContinue
if ($node) {
    Write-Host "OK node   : $(node --version) (requis : Node 22+ pour `courses/`)"
} else {
    Write-Warning "node absent : nécessaire uniquement pour le site Docusaurus (Node 22+). Voir docs/setup-dev.md §5."
}

# 3. Java 25
$javaExe = $null
if ($env:JAVA_HOME -and (Test-Path "$env:JAVA_HOME\bin\java.exe")) {
    $javaExe = "$env:JAVA_HOME\bin\java.exe"
} elseif (Get-Command java -ErrorAction SilentlyContinue) {
    $javaExe = (Get-Command java).Source
}
$major = if ($javaExe) { Get-JavaMajor $javaExe } else { 0 }

if ($major -ge 25 -and -not $Force) {
    Write-Host "OK java   : version $major détectée ($javaExe)"
} else {
    if ($major -gt 0) {
        Write-Host "Java $major détecté mais 25+ requis$(if($Force){' (-Force)'}) — installation d'un JDK portable."
    } else {
        Write-Host "Aucun Java 25+ détecté — installation d'un JDK Temurin portable."
    }

    $zip = Join-Path $env:TEMP "temurin-25.zip"
    $url = "https://api.adoptium.net/v3/binary/latest/25/ga/windows/x64/jdk/hotspot/normal/eclipse?project=jdk"
    Write-Host "Téléchargement depuis Adoptium..."
    Invoke-WebRequest -Uri $url -OutFile $zip -UseBasicParsing

    if (Test-Path $JdkDir) { Remove-Item $JdkDir -Recurse -Force }
    $tmp = Join-Path $env:TEMP "temurin-25-extract"
    if (Test-Path $tmp) { Remove-Item $tmp -Recurse -Force }
    Expand-Archive -Path $zip -DestinationPath $tmp -Force

    # L'archive contient un dossier racine jdk-25+xx : on le déplace vers $JdkDir
    $inner = Get-ChildItem $tmp -Directory | Select-Object -First 1
    Move-Item $inner.FullName $JdkDir
    Remove-Item $zip, $tmp -Recurse -Force -ErrorAction SilentlyContinue

    # Variables d'environnement au niveau utilisateur (pas d'admin)
    [Environment]::SetEnvironmentVariable("JAVA_HOME", $JdkDir, "User")
    $userPath = [Environment]::GetEnvironmentVariable("PATH", "User")
    if ($userPath -notlike "*$JdkDir\bin*") {
        [Environment]::SetEnvironmentVariable("PATH", "$JdkDir\bin;$userPath", "User")
    }
    # Session courante
    $env:JAVA_HOME = $JdkDir
    $env:PATH = "$JdkDir\bin;$env:PATH"

    $major = Get-JavaMajor "$JdkDir\bin\java.exe"
    if ($major -lt 25) { Write-Error "Installation du JDK échouée (version détectée : $major)." }
    Write-Host "OK java   : version $major installée dans $JdkDir"
    Write-Host "   (JAVA_HOME défini pour l'utilisateur — rouvre tes terminaux pour qu'il soit pris en compte)"
}

# 4. Maven Wrapper (le jar est versionné depuis #54 → fonctionne offline)
Write-Host "Vérification du Maven Wrapper..."
& "$RepoRoot\mvnw.cmd" -v
Write-Host "== Environnement prêt ==" -ForegroundColor Green
