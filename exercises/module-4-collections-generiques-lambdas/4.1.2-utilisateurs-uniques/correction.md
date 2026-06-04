# Correction — Exercice 4.1.2 Utilisateurs uniques (equals / hashCode)

## Démarche attendue

1. `Utilisateur` : champs `private final String nom;` et
   `private final int matricule;`, mémorisés au constructeur ; accesseurs simples.
2. `equals(Object o)` :
   - `if (this == o) return true;` (court-circuit pour le même objet) ;
   - `if (!(o instanceof Utilisateur u)) return false;` (gère `null` **et** un
     autre type, et fournit la variable `u` typée) ;
   - `return matricule == u.matricule && Objects.equals(nom, u.nom);`.
3. `hashCode()` : `return Objects.hash(nom, matricule);` — combine les **mêmes
   champs** que `equals`.
4. `RegistreUtilisateurs` : un `private final Set<Utilisateur> utilisateurs =
   new HashSet<>();` et des délégations directes (`add`, `contains`, `size`),
   `tous()` renvoyant une **copie** `new HashSet<>(utilisateurs)`.

## Points clés

- **Contrat equals/hashCode** : si `a.equals(b)`, alors
  `a.hashCode() == b.hashCode()`. C'est ce qui permet au `HashSet` de retrouver
  puis dédupliquer un doublon logique.
- **`instanceof` avec pattern** (`o instanceof Utilisateur u`) : il renvoie
  `false` si `o` est `null` ou d'un autre type, et déballe directement la
  variable typée `u`. Plus sûr et plus court qu'un `getClass()` + cast.
- **`Objects.hash` / `Objects.equals`** : idiomatiques, et `Objects.equals`
  gère un `nom` éventuellement `null` sans `NullPointerException`.
- **Copie défensive** : `tous()` renvoie un nouvel ensemble pour qu'un appelant
  ne puisse pas modifier l'état interne du registre.

## Erreurs fréquentes observées

- Écrire `equals` mais **oublier** `hashCode` (ou l'inverse) : le `HashSet` ne
  déduplique alors plus correctement.
- `hashCode` basé sur d'autres champs que `equals` (incohérence).
- Oublier la vérification de type → `ClassCastException` sur `equals("texte")`.
- Comparer les `String` avec `==` au lieu de `Objects.equals`.

## Pour approfondir

- Les **records** Java génèrent `equals`/`hashCode`/`toString` automatiquement à
  partir des composants.
- `java.util.Objects` (méthodes `hash`, `equals`, `requireNonNull`).
