# Correction — Exercice 5.1.2 Effectif invalide (exception personnalisée + chaînage)

## Démarche attendue

1. **Écrire `EffectifInvalideException extends RuntimeException`** avec deux
   constructeurs qui délèguent à `super` :

   ```java
   public EffectifInvalideException(String message) {
       super(message);
   }

   public EffectifInvalideException(String message, Throwable cause) {
       super(message, cause);
   }
   ```

   Hériter de `RuntimeException` la rend *unchecked* : pas de `throws` à propager,
   pas de rattrapage imposé. Le constructeur `(message, cause)` est le **seul**
   moyen de renseigner la cause (`getCause()`) — d'où son intérêt pour le chaînage.

2. **`affecter(int effectif)`** : tester la plage **avant** toute mutation, pour
   ne pas corrompre l'état si la valeur est rejetée.

   ```java
   public void affecter(int effectif) {
       if (effectif < 0 || effectif > capaciteMax) {
           throw new EffectifInvalideException(
               "Effectif invalide " + effectif + " pour l'equipe " + nom
               + " (plage autorisee : 0.." + capaciteMax + ")");
       }
       this.effectif = effectif;
   }
   ```

   Le message embarque la **valeur fautive** et le **nom de l'équipe** : un message
   d'exception doit être exploitable seul (logs, message stagiaire).

3. **`affecter(String effectifTexte)`** : entourer le `parseInt` d'un `try/catch`
   ciblé sur `NumberFormatException`, puis **relancer** une exception métier
   **chaînée** ; sinon, déléguer à la surcharge `int`.

   ```java
   public void affecter(String effectifTexte) {
       int valeur;
       try {
           valeur = Integer.parseInt(effectifTexte);
       } catch (NumberFormatException e) {
           throw new EffectifInvalideException(
               "Effectif illisible : " + effectifTexte, e); // cause chaînée
       }
       affecter(valeur); // garde de plage, SANS cause
   }
   ```

   La délégation à `affecter(int)` évite de **dupliquer** la garde de plage : un
   texte parseable mais hors plage lève la même exception que la surcharge `int`,
   donc **sans cause** (`getCause() == null`).

## Points clés

- **Unchecked = `extends RuntimeException`** : pour une erreur de validation
  d'argument, c'est le choix idiomatique (l'appelant n'est pas censé « récupérer »
  une saisie invalide en plein vol). On vérifie `isInstanceOf(RuntimeException.class)`.
- **Chaînage = `super(message, cause)`** : la `NumberFormatException` reste
  accessible via `getCause()`. On ne la **masque** pas et on n'imprime pas sa pile
  à la main — on la transporte.
- **Cause présente OU absente selon le chemin** : illisible → cause ;
  hors plage → pas de cause. Cette distinction est le cœur de l'exercice : elle
  prouve qu'on sait *quand* chaîner.
- **État préservé** : on lève **avant** d'écrire le champ. Un objet ne doit jamais
  rester dans un état incohérent à cause d'une entrée rejetée.
- **Messages via `hasMessageContaining`** : on vérifie des **sous-chaînes** (valeur,
  nom), jamais l'égalité stricte d'un message (fragile).

## Erreurs fréquentes observées

- **Oublier le second constructeur** : sans `(String, Throwable)`, impossible de
  chaîner ; le starter compile justement parce que les corps `// TODO` ne
  l'utilisent pas encore.
- **Rattraper trop large** (`catch (Exception e)`) au lieu de `NumberFormatException`,
  ce qui avalerait aussi l'`EffectifInvalideException` de plage.
- **Re-tester la plage dans `affecter(String)`** au lieu de déléguer : duplication,
  et risque d'attacher une cause là où il ne devrait pas y en avoir.
- **Muter le champ puis lever** : l'effectif reste corrompu après l'échec.
- **Relancer sans la cause** (`new EffectifInvalideException("...")` dans le `catch`) :
  on perd `getCause()` et la pile d'origine.
- **Faire hériter de `Exception`** (checked) : oblige des `throws` partout et casse
  le test « unchecked ».

## Pour approfondir

- `Throwable.getCause()` / `initCause(...)` et l'affichage « Caused by: » dans une
  stack trace : le chaînage rend le diagnostic traçable de bout en bout.
- *Checked* vs *unchecked* : `RuntimeException` pour les erreurs de programmation /
  validation, `Exception` pour les conditions qu'un appelant raisonnable peut
  vouloir gérer (à voir avec les I/O du sous-groupe suivant).
- Exceptions personnalisées : ajouter un champ métier (ex. la valeur fautive
  exposée par un getter) plutôt que de la noyer dans le message.
