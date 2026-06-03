# Correction — Exercice 3.1.3 Soldat

## Démarche attendue

1. Déclarer `private final String nom;`, `private final String grade;` et
   `private int pointsDeVie;`.
2. Le constructeur affecte les trois champs.
3. Les accesseurs renvoient les champs correspondants.
4. `subirDegats` retranche les dégâts puis **plafonne** :
   `pointsDeVie -= degats; if (pointsDeVie < 0) pointsDeVie = 0;`.
5. `soigner` ajoute simplement : `pointsDeVie += soin;`.
6. `estVivant` renvoie `pointsDeVie > 0`.
7. `toString` concatène : `nom + " (" + grade + ") - " + pointsDeVie + " PV"`.

## Points clés

- **Plancher à 0** : c'est une règle métier (les PV ne sont jamais négatifs),
  pas une validation d'entrée. Le `if` après la soustraction suffit.
- **État dérivé** : `estVivant` ne stocke rien ; il **calcule** une réponse à
  partir de `pointsDeVie`. On évite un champ booléen redondant à maintenir.
- **`final`** : `nom` et `grade` ne changent pas, ils peuvent être `final`.

## Erreurs fréquentes observées

- Oublier le plancher : `subirDegats(80)` sur 50 PV donne `-30` au lieu de `0`.
- Stocker un booléen `vivant` mis à jour à la main → source d'incohérences ;
  préférer le calcul `pointsDeVie > 0`.
- `estVivant` avec `>=` au lieu de `>` → un soldat à 0 PV serait « vivant ».

## Pour approfondir

- La différence entre **état stocké** et **état dérivé** (propriété calculée).
- L'intérêt des champs `final` pour l'immuabilité partielle d'un objet.
