# Exercice 5.1.2 — Effectif invalide (exception personnalisée + chaînage)

## Contexte

On gère l'**effectif** des équipes d'un groupement. Chaque équipe a un nom et une
**capacité maximale** d'accueil. Affecter un effectif négatif, supérieur à la
capacité, ou issu d'une saisie illisible doit être **signalé par une exception**
plutôt que silencieusement ignoré.

Vous allez écrire **votre propre exception**, `EffectifInvalideException`, puis
l'utiliser dans la classe `Equipe`. C'est l'occasion de pratiquer une exception
**personnalisée** *unchecked* et le **chaînage** d'une cause d'origine.

## Énoncé

### 1. Écrire l'exception `EffectifInvalideException`

Elle doit hériter de `RuntimeException` (donc *unchecked* : aucun `throws` requis,
aucun rattrapage obligatoire) et fournir **deux constructeurs** :

```java
public EffectifInvalideException(String message)                 // message seul
public EffectifInvalideException(String message, Throwable cause) // message + cause (chaînage)
```

Le second constructeur permet de **conserver la cause d'origine** : on l'utilisera
quand l'effectif vient d'un texte illisible, pour ne pas masquer la
`NumberFormatException` réelle. Le stub fourni ne contient que le premier
constructeur ; **à vous d'ajouter celui de chaînage**.

### 2. Compléter la classe `Equipe`

Champs, constructeur et getters sont **fournis**. Complétez les deux `affecter` :

```java
Equipe(String nom, int capaciteMax)   // effectif initial = 0
String getNom()   int getCapaciteMax()   int getEffectif()

void affecter(int effectif)
//  effectif < 0 || effectif > capaciteMax
//      -> throw new EffectifInvalideException(message nommant la valeur fautive ET le nom de l'équipe)
//  sinon -> met à jour l'effectif

void affecter(String effectifTexte)
//  texte illisible (Integer.parseInt lève NumberFormatException)
//      -> throw new EffectifInvalideException("Effectif illisible : " + effectifTexte, e)  // CHAÎNAGE
//  sinon -> délègue à affecter(int)  (la garde de plage s'applique alors, SANS cause)
```

- La plage valide est `[0, capaciteMax]` (les **deux bornes incluses**).
- En cas d'effectif invalide, l'**effectif courant ne change pas** : on rejette
  **avant** toute mise à jour.
- Dans `affecter(String)`, seul le texte **illisible** produit une exception avec
  **cause** (`getCause()` = la `NumberFormatException`). Un texte parseable mais
  hors plage (ex. `"-5"`) lève **sans cause** (`getCause() == null`), car c'est la
  garde de plage qui se déclenche, pas une erreur de lecture.

## Exemple

```text
Equipe alpha = new Equipe("Alpha", 100);
alpha.affecter(50);          // OK   -> getEffectif() == 50
alpha.affecter("30");        // OK   -> getEffectif() == 30
alpha.affecter(150);         // EffectifInvalideException (message contient "150" et "Alpha")
alpha.affecter(-1);          // EffectifInvalideException (hors plage)
alpha.affecter("abc");       // EffectifInvalideException, getCause() = NumberFormatException
alpha.affecter("-5");        // EffectifInvalideException, getCause() == null (hors plage)
alpha.getEffectif();         // 30  (inchangé : les échecs n'ont pas corrompu l'état)
```

## Contraintes

- Package `piscine.m5`. **Ne modifiez pas** les signatures fournies.
- `EffectifInvalideException` **hérite de `RuntimeException`** (unchecked).
- Le message de l'exception de plage **nomme la valeur fautive ET le nom de l'équipe**.
- Le chaînage de `affecter(String)` **préserve la `NumberFormatException`** comme cause.
- Une affectation qui échoue **laisse l'effectif inchangé**.
- Imports **explicites** (jamais `import ...*`), encodage UTF-8.

## Ce qui sera vérifié

- `affecter(0)`, une valeur au milieu et `capaciteMax` sont valides ; `getEffectif`
  reflète la valeur.
- `affecter(-1)` et `affecter(capaciteMax + 1)` lèvent une `EffectifInvalideException`.
- Le **type** levé est bien `EffectifInvalideException`, et son message **contient
  la valeur fautive** et le **nom de l'équipe**.
- `affecter("30")` (texte valide) met l'effectif à 30 ; `affecter("abc")` lève.
- L'exception est **unchecked** (`isInstanceOf(RuntimeException.class)`).
- `affecter("abc")` chaîne une `NumberFormatException` (`getCause()`) ; `affecter("-5")`
  lève **sans** cause.
- `capaciteMax == 0` : seul `0` est valide.
- L'état reste **inchangé après un échec**.

## Pour aller plus loin (optionnel — non noté)

- Pourquoi une exception **unchecked** ici plutôt que *checked* ? (Erreur de
  validation d'argument vs erreur récupérable attendue.)
- Que perd-on si, dans `affecter(String)`, on relance l'exception **sans** passer
  la cause ? (Indice : `getCause()` et la pile d'origine.)
