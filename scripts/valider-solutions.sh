#!/usr/bin/env bash
# Valide la solution de référence de chaque exercice (#11b).
# Chaque solution/pom.xml wire les tests publics ET privés (build-helper,
# cf. docs/format-exercice.md §8) : `mvn test` dessus doit donc tout passer.
# Un exo dont la solution échoue est cassé AVANT que le stagiaire le voie.
#
# Exos « écriture de tests » (sous-groupe 6.1, dossier mutants/, cf. format-exercice §11bis) :
# en plus de « la suite modèle passe sur le correct » (ci-dessus), on vérifie qu'elle
# **tue chaque mutant** : on substitue le mutant à l'impl correcte de la solution, on relance
# `mvn test`, et il DOIT échouer. Un mutant survivant = exo mal calibré (la suite modèle ne le
# détecte pas), donc un stagiaire pourrait réussir avec une suite trop faible.
#
# Utilisé par le CI ; lançable en local : ./scripts/valider-solutions.sh
# Surcharger Maven : MVN=./mvnw ./scripts/valider-solutions.sh
set -uo pipefail

REPO_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
EX_ROOT="$REPO_ROOT/exercises"
MVN="${MVN:-mvn}"
fails=0
checked=0
mutants_checked=0

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

  # Exo « écriture de tests » : vérifier que la suite modèle tue chaque mutant.
  exo_dir="$(dirname "$(dirname "$pom")")"
  mutants_dir="$exo_dir/mutants"
  sol_main="$exo_dir/solution/src/main/java"
  if [[ -d "$mutants_dir" ]]; then
    for mut_id_dir in "$mutants_dir"/*/; do
      [[ -d "$mut_id_dir" ]] || continue
      mid="$(basename "$mut_id_dir")"
      mutants_checked=$((mutants_checked + 1))
      # Substituer tous les fichiers du mutant à leur homologue dans solution/main.
      declare -a backups=() targets=()
      ok_swap=1
      while IFS= read -r mut_file; do
        rel="${mut_file#"$mut_id_dir"}"          # ex. piscine/m6/Operation.java
        target="$sol_main/$rel"
        if [[ ! -f "$target" ]]; then
          echo "  ! $exo/$mid : cible introuvable pour $rel"
          ok_swap=0
          break
        fi
        backup="$(mktemp)"
        cp "$target" "$backup"
        backups+=("$backup")
        targets+=("$target")
        cp "$mut_file" "$target"
      done < <(find "$mut_id_dir" -name '*.java')

      if [[ $ok_swap -eq 1 ]]; then
        # La suite modèle DOIT échouer sur le mutant (le mutant est « tué »).
        if "$MVN" -B -q -f "$pom" test >/dev/null 2>&1; then
          echo "  ✗ $exo : mutant SURVIVANT « $mid » (la suite modèle ne le détecte pas)"
          fails=$((fails + 1))
        else
          echo "  ✓ $exo : mutant tué « $mid »"
        fi
      else
        fails=$((fails + 1))
      fi

      # Restaurer l'impl correcte (toujours, même si le swap a partiellement échoué).
      for i in "${!backups[@]}"; do
        cp "${backups[$i]}" "${targets[$i]}"
        rm -f "${backups[$i]}"
      done
      unset backups targets
    done
  fi
done

echo ""
echo "Solutions vérifiées : $checked — mutants vérifiés : $mutants_checked — échecs : $fails"
[[ $fails -eq 0 ]]
