# Exercice 5.3.3 — Parseur de commandes texte (split + switch + exceptions)

## Contexte

Un système embarqué reçoit des ordres de calcul sous forme de chaînes de
texte brut : `"somme 3 4"`, `"oppose 8"`, etc. Votre rôle est d'écrire le
**parseur** qui interprète ces lignes et renvoie le résultat entier, ou lève
une exception explicite si la commande est mal formée.

Pas de fichier ici — tout se passe en mémoire, sur des `String`.

## Énoncé

Complétez la méthode statique suivante dans la classe `ParseurCommandes` :

```java
public static int executer(String ligne)
```

La méthode découpe `ligne` sur les espaces, identifie la commande (premier
token) et les arguments (tokens suivants), puis exécute le calcul :

| Commande  | Arguments | Résultat  |
|-----------|-----------|-----------|
| `somme`   | `a b`     | `a + b`   |
| `diff`    | `a b`     | `a - b`   |
| `produit` | `a b`     | `a * b`   |
| `oppose`  | `a`       | `-a`      |

## Règles de validation (levées avant tout calcul)

1. **Ligne vide ou blanche** (`null`, `""`, `"   "`) →
   `throw new IllegalArgumentException("commande vide")`

2. **Commande inconnue** (ni `somme`, ni `diff`, ni `produit`, ni `oppose`) →
   `throw new IllegalArgumentException("commande inconnue : <cmd>")`

3. **Mauvais nombre d'arguments** (trop peu ou trop) →
   `throw new IllegalArgumentException("arguments invalides pour <cmd>")`

4. **Argument non numérique** (`"somme a b"`) →
   `NumberFormatException` levée par `Integer.parseInt` — **ne pas attraper**,
   laisser se propager.

## Exemples

```text
executer("somme 3 4")    →  7
executer("diff 10 3")    →  7
executer("produit 2 5")  → 10
executer("oppose 8")     → -8
executer("  oppose 5 ") → -5   (espaces en tête/queue ignorés)
executer("somme   3    4") → 7 (espaces multiples tolérés)
executer("somme -3 4")  →  1   (nombres négatifs acceptés)
```

## Contraintes

- Package `piscine.m5`. **Ne modifiez pas** la signature de `executer`.
- La méthode ne déclare **pas** `throws` — toutes les exceptions levées sont
  unchecked (`IllegalArgumentException`, `NumberFormatException`).
- Utilisez `split("\\s+")` après `trim()` pour découper les tokens.
- Validez le nombre d'arguments **avant** d'appeler `Integer.parseInt`.
- La casse est **sensible** : `"SOMME 1 2"` est une commande inconnue.

## Ce qui sera vérifié

- `"somme 3 4"` → 7 ; `"diff 10 3"` → 7 ; `"produit 2 5"` → 10 ;
  `"oppose 8"` → -8.
- Commande inconnue (`"racine 9"`) → `IllegalArgumentException`.
- Argument manquant (`"somme 3"`) → `IllegalArgumentException`.
- Argument en trop (`"oppose 1 2"`) → `IllegalArgumentException`.
- Argument non numérique (`"somme a b"`) → `NumberFormatException`.
- Ligne vide / blanche → `IllegalArgumentException`.
- Espaces multiples entre tokens (`"somme   3    4"`) → résultat correct.
- Nombres négatifs en argument (`"somme -3 4"` → 1).

## Pour aller plus loin (optionnel — non noté)

- Pourquoi valider le nombre d'arguments **avant** la conversion ? Quel
  message d'erreur l'utilisateur obtiendrait-il sinon ?
- Comment étendriez-vous ce parseur pour supporter une nouvelle commande
  `maximum a b` sans toucher à la logique existante ?
- Que se passe-t-il si l'un des entiers dépasse `Integer.MAX_VALUE` ?
