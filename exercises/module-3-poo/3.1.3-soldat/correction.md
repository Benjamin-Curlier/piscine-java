# Correction — Exercice 3.1.3 Membre

## Démarche attendue

1. Déclarer `private final String nom;`, `private final String niveau;` et
   `private int pointsDeVie;`.
2. Le constructeur affecte les trois champs.
3. Les accesseurs renvoient les champs correspondants.
4. `subirDegats` retranche les dégâts puis **plafonne** :
   `pointsDeVie -= degats; if (pointsDeVie < 0) pointsDeVie = 0;`.
5. `soigner` ajoute simplement : `pointsDeVie += soin;`.
6. `estActif` renvoie `pointsDeVie > 0`.
7. `toString` concatène : `nom + " (" + niveau + ") - " + pointsDeVie + " PV"`.

## Points clés

- **Plancher à 0** : c'est une règle métier (les PV ne sont jamais négatifs),
  pas une validation d'entrée. Le `if` après la soustraction suffit.
- **État dérivé** : `estActif` ne stocke rien ; il **calcule** une réponse à
  partir de `pointsDeVie`. On évite un champ booléen redondant à maintenir.
- **`final`** : `nom` et `niveau` ne changent pas, ils peuvent être `final`.

## Erreurs fréquentes observées

- Oublier le plancher : `subirDegats(80)` sur 50 PV donne `-30` au lieu de `0`.
- Stocker un booléen `actif` mis à jour à la main → source d'incohérences ;
  préférer le calcul `pointsDeVie > 0`.
- `estActif` avec `>=` au lieu de `>` → un membre à 0 PV serait « actif ».

## Pour approfondir

- La différence entre **état stocké** et **état dérivé** (propriété calculée).
- L'intérêt des champs `final` pour l'immuabilité partielle d'un objet.
