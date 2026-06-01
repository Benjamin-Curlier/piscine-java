#!/usr/bin/env bash
# Vérifie la conformité structurelle des exercices individuels (docs/format-exercice.md).
# Utilisé par le CI (#11a) ; lançable en local : ./scripts/lint-exercices.sh
#
# Vérifie pour chaque exercices/module-*/M.S.E-slug/ :
#   - présence des fichiers obligatoires (tests-prives/ est optionnel, cf. format §2)
#   - validité YAML de metadata.yml et evaluation.yml (si python3 disponible)
#   - metadata.yml : champs obligatoires présents
#   - evaluation.yml : somme des poids == total
set -uo pipefail

REPO_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
EX_ROOT="$REPO_ROOT/exercises"
errors=0
checked=0

# Fichiers obligatoires (tests-prives/ exclu volontairement : optionnel pour les exos simples).
REQUIRED_FILES=(sujet.md metadata.yml evaluation.yml correction.md starter/pom.xml solution/pom.xml)

# Détection robuste d'un Python *fonctionnel avec PyYAML* (évite le stub Microsoft
# Store de Windows, qui répond à `command -v` mais ne s'exécute pas).
PYTHON=""
for cand in python3 python; do
  if command -v "$cand" >/dev/null 2>&1 && "$cand" -c "import yaml" >/dev/null 2>&1; then
    PYTHON="$cand"
    break
  fi
done
have_python=0
[[ -n "$PYTHON" ]] && have_python=1

fail() { echo "  ✗ $1"; errors=$((errors + 1)); }

is_exo_dir() { [[ "$1" =~ ^[1-6]\.[0-9]+\.[0-9]+-[a-z0-9-]+$ ]]; }

if [[ ! -d "$EX_ROOT" ]]; then
  echo "Répertoire introuvable : $EX_ROOT" >&2
  exit 1
fi

for module_dir in "$EX_ROOT"/module-*/; do
  [[ -d "$module_dir" ]] || continue
  for exo_dir in "$module_dir"*/; do
    [[ -d "$exo_dir" ]] || continue
    name="$(basename "$exo_dir")"
    if ! is_exo_dir "$name"; then
      echo "⚠  ignoré (nom non conforme M.S.E-slug) : $name"
      continue
    fi
    checked=$((checked + 1))
    echo "→ $name"

    for f in "${REQUIRED_FILES[@]}"; do
      [[ -f "$exo_dir$f" ]] || fail "fichier manquant : $f"
    done

    # tests/ : dossier présent avec au moins un .java
    if [[ -d "${exo_dir}tests" ]]; then
      if [[ -z "$(find "${exo_dir}tests" -name '*.java' -print -quit)" ]]; then
        fail "tests/ ne contient aucun .java"
      fi
    else
      fail "dossier manquant : tests/"
    fi

    if [[ $have_python -eq 1 ]]; then
      for y in metadata.yml evaluation.yml; do
        [[ -f "$exo_dir$y" ]] || continue
        if ! "$PYTHON" -c "import sys,yaml; yaml.safe_load(open(sys.argv[1],encoding='utf-8'))" "$exo_dir$y" 2>/dev/null; then
          fail "YAML invalide : $y"
        fi
      done

      if [[ -f "${exo_dir}metadata.yml" ]]; then
        "$PYTHON" - "${exo_dir}metadata.yml" <<'PY' || fail "metadata.yml : champs obligatoires manquants"
import sys, yaml
req = ["slug", "titre", "module", "sous_groupe", "position", "difficulte",
       "duree_estimee_min", "objectifs_pedagogiques", "auteur", "version", "date_creation"]
d = yaml.safe_load(open(sys.argv[1], encoding="utf-8")) or {}
sys.exit(1 if [k for k in req if k not in d] else 0)
PY
      fi

      if [[ -f "${exo_dir}evaluation.yml" ]]; then
        "$PYTHON" - "${exo_dir}evaluation.yml" <<'PY' || fail "evaluation.yml : somme des poids != total (ou structure invalide)"
import sys, yaml
d = yaml.safe_load(open(sys.argv[1], encoding="utf-8")) or {}
total = d.get("total")
crit = d.get("criteres") or []
s = sum(c.get("poids", 0) for c in crit if isinstance(c, dict))
sys.exit(0 if (total is not None and s == total) else 1)
PY
      fi
    fi
  done
done

if [[ $have_python -eq 0 ]]; then
  echo "ℹ  python3 absent : validation YAML sémantique ignorée (présence des fichiers vérifiée)."
fi

echo ""
echo "Exercices vérifiés : $checked — erreurs : $errors"
[[ $errors -eq 0 ]]
