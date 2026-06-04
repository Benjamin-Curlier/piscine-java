# Correction — Exercice 5.1.1 Saisie défensive (validerEntier)

## Démarche attendue

```java
public static int validerEntier(String texte, int min, int max) {
    // 1. Garde null explicite — AVANT texte.trim()
    if (texte == null) {
        throw new NumberFormatException("Le texte ne peut pas être null");
    }
    // 2. Conversion — la NFE de parseInt remonte telle quelle
    int valeur = Integer.parseInt(texte.trim());
    // 3. Vérification de plage — IAE levée en propre (pas une NFE)
    if (valeur < min || valeur > max) {
        throw new IllegalArgumentException(
            "La valeur " + valeur + " est hors de la plage autorisée [" + min + ", " + max + "]"
        );
    }
    // 4. Valide — renvoyer la valeur
    return valeur;
}
```

## Points clés

- **Garde `null` en tête** : `texte.trim()` sur `null` lèverait une
  `NullPointerException` sans message utile. Lever une `NumberFormatException`
  à la place regroupe `null` avec les autres entrées illisibles et donne un
  message exploitable à l'appelant.

- **Laisser remonter la `NumberFormatException` de `parseInt`** : on ne la
  rattrape pas avec un `try/catch`. L'énoncé distingue deux familles ; si on
  la rattrapait pour la re-lancer en `IllegalArgumentException`, on perdrait
  le type précis que les tests vérifient.

- **Lever sa propre `IllegalArgumentException` pour le hors-plage** : après
  le `parseInt`, la valeur est connue. Si elle sort de `[min, max]`, on lève
  notre exception avec un message qui nomme `min`, `max` et la valeur fautive —
  trois informations que l'appelant ne peut pas deviner sans ce message.

- **`trim()` avant `parseInt`** : permet d'accepter les saisies avec espaces
  (copier-coller, formulaire web) sans erreur de format inutile.

## Erreurs fréquentes observées

- **Oublier la garde `null`** : résultat = `NullPointerException` au lieu de
  `NumberFormatException`, ce que les tests privés détectent.

- **Attraper la `NumberFormatException` de `parseInt` et la re-lancer en
  `IllegalArgumentException`** : les tests vérifient le type précis
  `NumberFormatException.class` pour les entrées illisibles ; cette confusion
  les fait échouer.

- **Ne pas inclure la valeur fautive dans le message de l'IAE** : les tests
  privés vérifient que le message `hasMessageContaining("42")` (par exemple).

- **Vérifier la plage avant la conversion** : impossible, on ne connaît pas
  encore `valeur` avant `parseInt`.

- **Condition de plage inversée** : écrire `valeur >= min && valeur <= max`
  dans le `if` et lever l'exception (`if (dans_la_plage) throw`) au lieu de
  `if (hors_plage) throw`. Les deux sont logiquement équivalents mais la forme
  positive (`if (hors_plage)`) est plus lisible.

## Pour approfondir

- `NumberFormatException extends IllegalArgumentException` : cette hiérarchie
  signifie qu'un `catch (IllegalArgumentException e)` attraperait aussi les
  `NumberFormatException`. Pour distinguer les deux familles à l'appel, il faut
  écrire deux blocs `catch` dans l'ordre du plus spécifique au plus général.

- `Integer.parseInt` vs `Integer.valueOf` : `parseInt` renvoie le primitif
  `int` (pas de boxing inutile) et lève `NumberFormatException` à la même
  façon. Pour les grands entiers on utiliserait `Long.parseLong`.

- Pattern alternatif avec `Optional` : certains préfèrent une méthode renvoyant
  `Optional<Integer>` pour les entrées optionnelles, réservant les exceptions
  aux erreurs « vraiment exceptionnelles ». Ici l'énoncé impose la levée
  d'exception car c'est le but pédagogique.
