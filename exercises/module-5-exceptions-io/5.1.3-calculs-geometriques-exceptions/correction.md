# Correction — Exercice 5.1.3 Calculs géométriques avec gardes (refactor 2.3.3)

## Démarche attendue

1. **Écrire le helper privé** `exigerPositif(double valeur, String nom)` :
   - Condition : `if (valeur < 0)` — la comparaison stricte traite `-0.0` comme
     autorisé (`-0.0 < 0` est `false` en Java, comportement voulu).
   - Lève `new IllegalArgumentException("La dimension '" + nom + "' doit être >= 0, ...")`.

2. **Appeler `exigerPositif` en tête de chaque méthode** avant le calcul :
   - `aire(double rayon)` → `exigerPositif(rayon, "rayon")`.
   - `aire(double largeur, double hauteur)` → appel sur `largeur` puis sur `hauteur`
     (la première rencontrée déclenche l'exception si négative).
   - Idem pour `perimetre`.

3. **Retourner le calcul inchangé** (formules identiques à 2.3.3) :
   `Math.PI * rayon * rayon`, `largeur * hauteur`,
   `2 * Math.PI * rayon`, `2 * (largeur + hauteur)`.

## Points clés

- **`IllegalArgumentException` est l'exception JDK idiomatique** pour signaler
  qu'un argument ne satisfait pas le contrat d'une méthode. On ne crée pas de
  type custom ici (réservé à des domaines avec sémantique propre comme 5.1.2).
- **Le helper privé est l'essentiel du critère `idiomatisme`** : sans lui, la
  garde est dupliquée 4 à 6 fois. Avec lui, une seule règle, un seul message,
  zéro répétition — principe DRY appliqué à la validation.
- **Le zéro est autorisé** : un cercle de rayon 0 ou un rectangle de largeur 0
  sont des cas dégénérés mais géométriquement valides. La condition est `< 0`,
  pas `<= 0`.
- **`-0.0 < 0` vaut `false`** en Java (IEEE 754 : zéro négatif est égal à zéro
  positif). Pas besoin de cas particulier, la condition `< 0` le couvre naturellement.
- **Message nominatif** : le message contient le nom du paramètre (`"rayon"`,
  `"largeur"`, `"hauteur"`). Les tests privés le vérifient via
  `hasMessageContaining("rayon")` — on n'impose pas le libellé exact, juste la
  présence du mot-clé.

## Erreurs fréquentes observées

- Utiliser `<= 0` comme condition : rejette le zéro à tort (0.0 est autorisé).
- Dupliquer la garde dans chaque méthode sans factoriser : code répétitif, risque
  d'incohérence si la règle change.
- Créer une exception custom (`DimensionInvalideException`) alors que
  `IllegalArgumentException` du JDK suffit et est plus reconnaissable.
- Oublier de nommer le paramètre dans le message : le stagiaire ne sait pas
  laquelle des deux dimensions est en cause pour `aire(-1.0, 4.0)`.
- Tester `valeur <= 0` plutôt que `valeur < 0` dans le helper, puis constater
  que `aire(0.0)` lève une exception → échec des tests sur le zéro autorisé.

## Pour approfondir

- **Préconditions Guava / `Objects.requireNonNull`** : les librairies populaires
  (Guava, Spring) offrent des méthodes utilitaires du style
  `Preconditions.checkArgument(rayon >= 0, "rayon négatif : %s", rayon)`.
  Notre `exigerPositif` est l'équivalent artisanal.
- **`assert`** : Java propose le mot-clé `assert valeur >= 0 : nom;` mais les
  assertions sont désactivées en production par défaut (`-ea` requis). Pour des
  gardes visibles en prod, `IllegalArgumentException` est préférable.
- **`Math.PI` et la précision flottante** : les tests utilisent `isCloseTo(...,
  within(1e-9))` et non `isEqualTo` pour les calculs impliquant `Math.PI`, car
  les multiplications flottantes introduisent des erreurs d'arrondi infimes.
  Pour les rectangles (entiers), `isEqualTo` est acceptable.
