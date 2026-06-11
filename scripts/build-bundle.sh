#!/usr/bin/env bash
# Construit le bundle portable de la Piscine ETNC.
# Usage : ./scripts/build-bundle.sh [--jdk <chemin>] [--out <dossier>]
set -euo pipefail
JDK_PATH="${JAVA_HOME:-}"
OUT_DIR=""
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
MINGIT_VERSION="2.54.0"
MINGIT_URL="https://github.com/git-for-windows/git/releases/download/v${MINGIT_VERSION}.windows.1/MinGit-${MINGIT_VERSION}-64-bit.zip"
MINGIT_SHA256="04f937e1f0918b17b9be6f2294cb2bb66e96e1d9832d1c298e2de088a1d0e668"
REPO_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
OUT_DIR="${OUT_DIR:-$REPO_ROOT/dist}"
STAGE="$OUT_DIR/piscine-etnc-stagiaire"

[[ -x "$JDK_PATH/bin/java" ]] || { echo "JDK introuvable à '$JDK_PATH' (utilise --jdk)."; exit 1; }

# Build de l'uber-jar via le wrapper Gradle (versionné, fonctionne offline)
echo "[bundle] Build de l'uber-jar ..."
"$REPO_ROOT/moulinette/gradlew" -p "$REPO_ROOT/moulinette" -q :console:shadowJar
JAR="$REPO_ROOT/moulinette/console/build/libs/moulinette-console.jar"
[[ -f "$JAR" ]] || { echo "Uber-jar introuvable : $JAR"; exit 1; }

rm -rf "$STAGE"
mkdir -p "$STAGE/app" "$STAGE/piscine/docs" "$STAGE/workspace"
cp "$JAR" "$STAGE/app/moulinette-console.jar"
cp -r "$REPO_ROOT/exercises" "$STAGE/piscine/exercises"
cp "$REPO_ROOT/docs/piscine-stagiaire.md" "$STAGE/piscine/docs/piscine-stagiaire.md"
echo "[bundle] Copie du JDK portable ..."
cp -r "$JDK_PATH" "$STAGE/jdk"

# Site de cours (Docusaurus)
SITE_BUILD="$REPO_ROOT/courses/build"
if [[ "$REBUILD_SITE" -eq 1 || ! -f "$SITE_BUILD/index.html" ]]; then
  command -v npm >/dev/null 2>&1 || { echo "npm introuvable : installe Node.js."; exit 1; }
  echo "[bundle] Génération du site de cours (npm) ..."
  ( cd "$REPO_ROOT/courses" && npm ci && npm run build )
fi
echo "[bundle] Copie du site de cours ..."
cp -r "$SITE_BUILD" "$STAGE/site"

# git portable (MinGit, téléchargé + vérifié)
echo "[bundle] Téléchargement de MinGit $MINGIT_VERSION ..."
MINGIT_ZIP="$(mktemp -u).zip"
curl -fSL "$MINGIT_URL" -o "$MINGIT_ZIP"
echo "${MINGIT_SHA256}  ${MINGIT_ZIP}" | sha256sum -c - || { echo "Checksum MinGit invalide."; exit 1; }
echo "[bundle] Extraction de MinGit ..."
mkdir -p "$STAGE/git"
unzip -q "$MINGIT_ZIP" -d "$STAGE/git"

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

# Launcher Windows (double-cliquable). Le bundle cible Windows : on l'émet aussi depuis le .sh.
cat > "$STAGE/piscine.bat" <<'EOF'
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
EOF
# .bat fiable au double-clic : fins de ligne CRLF.
sed -i 's/$/\r/' "$STAGE/piscine.bat"

cat > "$STAGE/LISEZMOI.txt" <<'EOF'
Piscine ETNC — version autonome (standalone)
Lancez ./piscine.sh (ou piscine.bat sous Windows) pour demarrer.
Le site de cours s'ouvre dans le navigateur (sinon URL affichee dans la console).
Votre espace est cree au premier lancement. `help` pour les commandes, `exit` pour quitter.
Rapports : workspace/.piscine/reports/  — Guide : piscine/docs/piscine-stagiaire.md
Aucune installation ni reseau requis : tout est dans ce dossier.
EOF

STAMP="$(date +%Y%m%d)"
ZIP="$OUT_DIR/piscine-etnc-stagiaire-$STAMP.zip"
rm -f "$ZIP"
if command -v zip >/dev/null 2>&1; then
  ( cd "$OUT_DIR" && zip -rq "$ZIP" "piscine-etnc-stagiaire" )
else
  # `zip` est souvent absent sous Git-Bash Windows. Fallback sur `jar` du JDK, qui
  # produit une archive au format ZIP compatible (vérifié : extraction + lancement OK).
  echo "[bundle] zip absent — fallback sur 'jar' du JDK ..."
  ( cd "$OUT_DIR" && "$JDK_PATH/bin/jar" -c -M -f "$ZIP" "piscine-etnc-stagiaire" )
fi
echo "[bundle] OK : $ZIP"
