#!/usr/bin/env bash
# Valide la solution de référence de chaque exercice (#11b).
# Chaque solution/pom.xml wire les tests publics ET privés (build-helper,
# cf. docs/format-exercice.md §8) : `mvn test` dessus doit donc tout passer.
# Un exo dont la solution échoue est cassé AVANT que le stagiaire le voie.
#
# Utilisé par le CI ; lançable en local : ./scripts/valider-solutions.sh
# Surcharger Maven : MVN=./mvnw ./scripts/valider-solutions.sh
set -uo pipefail

REPO_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
EX_ROOT="$REPO_ROOT/exercises"
MVN="${MVN:-mvn}"
fails=0
checked=0

for pom in "$EX_ROOT"/module-*/*/solution/pom.xml; do
  [[ -f "$pom" ]] || continue
  exo="$(basename "$(dirname "$(dirname "$pom")")")"
  checked=$((checked + 1))
  echo "==================== $exo ===================="
  if "$MVN" -B -q -f "$pom" test; then
    echo "  ✓ $exo : solution valide"
  else
    echo "  ✗ $exo : la solution NE PASSE PAS ses tests"
    fails=$((fails + 1))
  fi
done

echo ""
echo "Solutions vérifiées : $checked — échecs : $fails"
[[ $fails -eq 0 ]]
