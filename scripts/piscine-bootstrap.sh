#!/usr/bin/env bash
# Bootstrap autonome de la Piscine Java en mode local.
# Usage : ./scripts/piscine-bootstrap.sh --nom <slug> [--dest <dossier>] [--force]

set -euo pipefail

NOM=""
DEST=""
FORCE=0

while [[ $# -gt 0 ]]; do
    case "$1" in
        --nom)   NOM="$2"; shift 2;;
        --dest)  DEST="$2"; shift 2;;
        --force) FORCE=1; shift;;
        -h|--help)
            sed -n '2,4p' "$0"; exit 0;;
        *) echo "Argument inconnu : $1"; exit 2;;
    esac
done

if [[ -z "$NOM" ]]; then
    echo "Erreur : --nom est obligatoire." >&2
    exit 2
fi

REPO_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
DEST="${DEST:-$REPO_ROOT/../piscine-$NOM}"

# Résolution de java : préfère $JAVA_HOME/bin/java (utile si le java du PATH est trop ancien)
JAVA_BIN="java"
if [[ -n "${JAVA_HOME:-}" && -x "$JAVA_HOME/bin/java" ]]; then
    JAVA_BIN="$JAVA_HOME/bin/java"
fi

# Prérequis
command -v git >/dev/null || { echo "git introuvable. Voir docs/setup-dev.md."; exit 1; }
command -v "$JAVA_BIN" >/dev/null 2>&1 || { echo "java introuvable. Voir docs/setup-dev.md."; exit 1; }

JAVA_MAJOR="$("$JAVA_BIN" -version 2>&1 | head -1 | sed -n 's/.*version "\([0-9]*\).*/\1/p')"
if [[ -z "$JAVA_MAJOR" || "$JAVA_MAJOR" -lt 25 ]]; then
    echo "Java 25+ requis (détecté : ${JAVA_MAJOR:-inconnu}). Voir docs/setup-dev.md." >&2
    echo "Astuce : exporte JAVA_HOME vers un JDK 25." >&2
    exit 1
fi

JAR="$REPO_ROOT/moulinette/console/build/libs/moulinette-console.jar"

if [[ -d "$DEST" && "$FORCE" -eq 0 ]]; then
    echo "Workspace déjà présent : $DEST"
    echo "Relance avec --force pour réinitialiser, ou démarre le REPL :"
    echo "  \"$JAVA_BIN\" -jar \"$JAR\" repl --repo \"$DEST\" --piscine-repo \"$REPO_ROOT\""
    exit 0
fi

if [[ -d "$DEST" && "$FORCE" -eq 1 ]]; then
    echo "[bootstrap] Suppression du workspace existant : $DEST"
    rm -rf "$DEST"
fi

echo "[bootstrap] Build moulinette-console (via gradlew) ..."
"$REPO_ROOT/moulinette/gradlew" -p "$REPO_ROOT/moulinette" -q :console:shadowJar

[[ -f "$JAR" ]] || { echo "Build du jar échoué : $JAR introuvable."; exit 1; }

echo "[bootstrap] Initialisation du workspace stagiaire ..."
"$JAVA_BIN" -jar "$JAR" init --nom "$NOM" --dest "$DEST" --piscine-repo "$REPO_ROOT"

echo ""
echo "[bootstrap] ✓ Prêt. Lance le REPL :"
echo "  \"$JAVA_BIN\" -jar \"$JAR\" repl --repo \"$DEST\" --piscine-repo \"$REPO_ROOT\""
