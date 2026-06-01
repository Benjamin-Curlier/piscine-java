#!/usr/bin/env bash
# Construit le bundle portable de la Piscine ETNC.
# Usage : ./scripts/build-bundle.sh [--jdk <chemin>] [--out <dossier>]
set -euo pipefail
JDK_PATH="${JAVA_HOME:-}"
OUT_DIR=""
while [[ $# -gt 0 ]]; do
  case "$1" in
    --jdk) JDK_PATH="$2"; shift 2;;
    --out) OUT_DIR="$2"; shift 2;;
    *) echo "Argument inconnu : $1"; exit 2;;
  esac
done
REPO_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
OUT_DIR="${OUT_DIR:-$REPO_ROOT/dist}"
STAGE="$OUT_DIR/piscine-etnc-stagiaire"

[[ -x "$JDK_PATH/bin/java" ]] || { echo "JDK introuvable à '$JDK_PATH' (utilise --jdk)."; exit 1; }

if command -v mvn >/dev/null 2>&1; then MVN=(mvn); else MVN=("$REPO_ROOT/mvnw"); fi
echo "[bundle] Build de l'uber-jar ..."
"${MVN[@]}" -f "$REPO_ROOT/moulinette/pom.xml" -pl console -am -q -DskipTests package
JAR="$REPO_ROOT/moulinette/console/target/moulinette-console.jar"
[[ -f "$JAR" ]] || { echo "Uber-jar introuvable : $JAR"; exit 1; }

rm -rf "$STAGE"
mkdir -p "$STAGE/app" "$STAGE/piscine/docs" "$STAGE/workspace"
cp "$JAR" "$STAGE/app/moulinette-console.jar"
cp -r "$REPO_ROOT/exercises" "$STAGE/piscine/exercises"
cp "$REPO_ROOT/docs/piscine-stagiaire.md" "$STAGE/piscine/docs/piscine-stagiaire.md"
echo "[bundle] Copie du JDK portable ..."
cp -r "$JDK_PATH" "$STAGE/jdk"

cat > "$STAGE/piscine.sh" <<'EOF'
#!/usr/bin/env bash
HERE="$(cd "$(dirname "$0")" && pwd)"
JAVA="$HERE/jdk/bin/java"
JAR="$HERE/app/moulinette-console.jar"
WS="$HERE/workspace"
if [ ! -d "$WS/.git" ]; then
  "$JAVA" -jar "$JAR" init --nom stagiaire --dest "$WS" --piscine-repo "$HERE/piscine"
fi
"$JAVA" -jar "$JAR" repl --repo "$WS" --piscine-repo "$HERE/piscine"
EOF
chmod +x "$STAGE/piscine.sh"

cat > "$STAGE/LISEZMOI.txt" <<'EOF'
Piscine ETNC — console locale
Lancez ./piscine.sh pour demarrer. Votre espace est cree au premier lancement.
Tapez `help` dans la console, `exit` pour quitter.
Rapports : workspace/.piscine/reports/  — Guide : piscine/docs/piscine-stagiaire.md
EOF

STAMP="$(date +%Y%m%d)"
ZIP="$OUT_DIR/piscine-etnc-stagiaire-$STAMP.zip"
rm -f "$ZIP"
( cd "$OUT_DIR" && zip -rq "$ZIP" "piscine-etnc-stagiaire" )
echo "[bundle] OK : $ZIP"
