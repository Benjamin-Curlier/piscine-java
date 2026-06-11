#!/usr/bin/env bash
# Prépare l'environnement de développement Piscine ETNC sous Linux/macOS, sans droits admin.
# Vérifie git et Node, installe un JDK Temurin 25 portable si Java 25+ est absent,
# configure JAVA_HOME / PATH (session + ~/.profile), puis vérifie le Maven Wrapper.
# Usage : ./scripts/setup-dev.sh [--jdk-dir <dossier>] [--force]
#   --jdk-dir : où installer le JDK portable (défaut : $HOME/tools/jdk-25)
#   --force   : réinstalle le JDK même si un Java 25+ est déjà détecté

set -euo pipefail

JDK_DIR="$HOME/tools/jdk-25"
FORCE=0

while [[ $# -gt 0 ]]; do
    case "$1" in
        --jdk-dir) JDK_DIR="$2"; shift 2;;
        --force)   FORCE=1; shift;;
        -h|--help) sed -n '2,8p' "$0"; exit 0;;
        *) echo "Argument inconnu : $1" >&2; exit 2;;
    esac
done

REPO_ROOT="$(cd "$(dirname "$0")/.." && pwd)"

java_major() {
    local exe="$1"
    "$exe" -version 2>&1 | head -1 | sed -n 's/.*version "\([0-9]*\).*/\1/p'
}

echo "== Setup dev Piscine ETNC =="

# 1. git
if ! command -v git >/dev/null 2>&1; then
    echo "Erreur : git introuvable. Installe git puis relance. Voir docs/setup-dev.md." >&2
    exit 1
fi
echo "OK git    : $(git --version)"

# 2. Node (avertissement seulement — requis uniquement pour le site Docusaurus)
if command -v node >/dev/null 2>&1; then
    echo "OK node   : $(node --version) (requis : Node 22+ pour courses/)"
else
    echo "ATTENTION node absent : nécessaire uniquement pour le site Docusaurus (Node 22+). Voir docs/setup-dev.md §5." >&2
fi

# 3. Java 25
JAVA_EXE=""
if [[ -n "${JAVA_HOME:-}" && -x "$JAVA_HOME/bin/java" ]]; then
    JAVA_EXE="$JAVA_HOME/bin/java"
elif command -v java >/dev/null 2>&1; then
    JAVA_EXE="$(command -v java)"
fi
MAJOR=0
[[ -n "$JAVA_EXE" ]] && MAJOR="$(java_major "$JAVA_EXE" || echo 0)"

if [[ "$MAJOR" -ge 25 && "$FORCE" -eq 0 ]]; then
    echo "OK java   : version $MAJOR détectée ($JAVA_EXE)"
else
    if [[ "$MAJOR" -gt 0 ]]; then
        echo "Java $MAJOR détecté mais 25+ requis — installation d'un JDK portable."
    else
        echo "Aucun Java 25+ détecté — installation d'un JDK Temurin portable."
    fi

    # OS / arch pour l'API Adoptium
    case "$(uname -s)" in
        Linux)  OS="linux";;
        Darwin) OS="mac";;
        *) echo "OS non géré par ce script : $(uname -s). Voir docs/setup-dev.md §2." >&2; exit 1;;
    esac
    case "$(uname -m)" in
        x86_64|amd64) ARCH="x64";;
        arm64|aarch64) ARCH="aarch64";;
        *) echo "Architecture non gérée : $(uname -m)." >&2; exit 1;;
    esac

    URL="https://api.adoptium.net/v3/binary/latest/25/ga/${OS}/${ARCH}/jdk/hotspot/normal/eclipse?project=jdk"
    TARBALL="$(mktemp -t temurin25.XXXXXX.tar.gz)"
    echo "Téléchargement depuis Adoptium (${OS}/${ARCH})..."
    curl -fsSL "$URL" -o "$TARBALL"

    rm -rf "$JDK_DIR"
    mkdir -p "$JDK_DIR"
    # --strip-components=1 retire le dossier racine jdk-25+xx de l'archive
    tar -xzf "$TARBALL" -C "$JDK_DIR" --strip-components=1
    rm -f "$TARBALL"

    # macOS empaquette le JDK sous Contents/Home
    if [[ "$OS" == "mac" && -d "$JDK_DIR/Contents/Home" ]]; then
        JAVA_HOME_PATH="$JDK_DIR/Contents/Home"
    else
        JAVA_HOME_PATH="$JDK_DIR"
    fi

    export JAVA_HOME="$JAVA_HOME_PATH"
    export PATH="$JAVA_HOME/bin:$PATH"

    MAJOR="$(java_major "$JAVA_HOME/bin/java" || echo 0)"
    [[ "$MAJOR" -ge 25 ]] || { echo "Installation du JDK échouée (version : $MAJOR)." >&2; exit 1; }

    # Persistance dans ~/.profile (idempotent)
    MARK="# Piscine ETNC — JDK 25"
    if ! grep -qF "$MARK" "$HOME/.profile" 2>/dev/null; then
        {
            echo ""
            echo "$MARK"
            echo "export JAVA_HOME=\"$JAVA_HOME_PATH\""
            echo "export PATH=\"\$JAVA_HOME/bin:\$PATH\""
        } >> "$HOME/.profile"
        echo "   (JAVA_HOME ajouté à ~/.profile — relance ton shell ou 'source ~/.profile')"
    fi
    echo "OK java   : version $MAJOR installée dans $JAVA_HOME_PATH"
fi

# 4. Wrappers de build (jars versionnés → fonctionnent offline, cf. #54)
#    Gradle : build de la moulinette ; Maven : projets des exercices.
echo "Vérification du Gradle Wrapper (moulinette)..."
chmod +x "$REPO_ROOT/moulinette/gradlew"
"$REPO_ROOT/moulinette/gradlew" -p "$REPO_ROOT/moulinette" -v
echo "Vérification du Maven Wrapper (exercices)..."
chmod +x "$REPO_ROOT/mvnw"
"$REPO_ROOT/mvnw" -v
echo "== Environnement prêt =="
