# Exercice 5.2.4 — Lecture de configuration (cle=valeur)

## Contexte

La plupart des applications lisent leurs réglages dans un **fichier de
configuration** texte : une paire `cle=valeur` par ligne, des commentaires
introduits par `#`, des lignes vides pour aérer. Le format est simple, mais le
**parsing** réclame de la rigueur : où couper la ligne ? que faire des espaces ?
d'une clé répétée ? d'un `=` présent dans la valeur (une URL, par exemple) ?

Java offre une classe toute faite pour cela (`java.util.Properties`), mais elle
est **interdite ici** : l'objectif pédagogique est de **parser le format à la
main** pour comprendre chaque décision. Vous la découvrirez plus tard.

## Énoncé

Complétez la classe `LectureConfig` (package `piscine.m5`) avec **deux** méthodes
statiques. Les chemins sont **fournis par l'appelant** ; la lecture propage
`throws IOException`.

```java
public static Map<String, String> lireConfig(Path config) throws IOException
public static String lireValeur(Path config, String cle, String valeurParDefaut) throws IOException
```

### `lireConfig(Path config)`

Lit le fichier avec `Files.readAllLines(config, StandardCharsets.UTF_8)` puis,
pour **chaque ligne**, applique les règles suivantes dans l'ordre :

1. **Ligne blanche** (`ligne.isBlank()`) → ignorée.
2. **Commentaire** : si la ligne, une fois rognée (`trim`), **commence par `#`**
   → ignorée.
3. **Coupure au PREMIER `=`** : utilisez `ligne.split("=", 2)` (la limite `2` est
   essentielle) **ou** `ligne.indexOf('=')` + `substring`. La **clé** est ce qui
   précède le premier `=`, la **valeur** est **tout le reste** (qui peut donc
   contenir d'autres `=`).
4. **Ligne sans `=`** → ignorée (ce n'est pas une paire).
5. **`trim`** la clé **et** la valeur, puis insérez-les dans une `Map<String, String>`.
6. **Clé dupliquée** : la **dernière** occurrence l'emporte (c'est le
   comportement naturel de `Map.put`).

Renvoyez la `Map` ainsi remplie.

### `lireValeur(Path config, String cle, String valeurParDefaut)`

**Délègue** à `lireConfig` : renvoyez la valeur de la clé, ou `valeurParDefaut`
si la clé est absente. Une seule ligne suffit avec `Map.getOrDefault`.

## Exemple

Fichier `app.conf` :

```text
# Configuration de l'application
langue=fr
niveau=3

url=http://x?a=b
```

Résultats :

```text
LectureConfig.lireValeur(app.conf, "langue", "??")   // → "fr"
LectureConfig.lireValeur(app.conf, "niveau", "0")    // → "3"
LectureConfig.lireValeur(app.conf, "theme",  "clair")// → "clair" (clé absente)
LectureConfig.lireConfig(app.conf)                   // → {langue=fr, niveau=3, url=http://x?a=b}
```

## Contraintes

- Package `piscine.m5`. **Ne modifiez pas** les signatures.
- **Interdit : `java.util.Properties`.** Parsez le format **à la main**.
- **NIO.2** : `Path`, `Files.readAllLines`. **UTF-8 explicite**
  (`StandardCharsets.UTF_8`) à la lecture.
- Coupez au **premier** `=` (`split("=", 2)` ou `indexOf`) — jamais un `split("=")`
  sans limite, qui perdrait les `=` de la valeur.
- `trim` la clé **et** la valeur. Une valeur peut être **vide** (`cle=` → `""`).
- Clé dupliquée : la **dernière** gagne. Les clés sont **sensibles à la casse**.
- Imports explicites (jamais `.*`). Aucune dépendance extérieure.

## Ce qui sera vérifié

- `lireValeur` renvoie la bonne valeur pour une clé présente, le défaut sinon.
- `lireConfig` renvoie la table complète des paires.
- Lignes de commentaire (`# ...`) et lignes blanches **ignorées**.
- Espaces autour de la clé/valeur **rognés** (`  a = b  ` → `a` → `b`).
- Valeur vide (`cle=`) → chaîne vide `""`.
- `=` dans la valeur (`url=http://x?a=b`) → valeur `http://x?a=b` (coupe au 1er `=`).
- Clé dupliquée → la dernière l'emporte. Ligne sans `=` → ignorée.
- Fichier vide → table vide, `lireValeur` renvoie le défaut.
- Accents (UTF-8) préservés à la lecture.

## Pour aller plus loin (optionnel — non noté)

- Comment `split("=", 2)` se comporte-t-il sur `"cle="` ? Et sur `"=valeur"` ?
  Comparez avec votre version `indexOf` : donnent-elles le même résultat ?
- Que faudrait-il changer pour rendre les **clés** insensibles à la casse ?
- Et pour **conserver** l'ordre d'apparition des clés (utile au débogage) ?
  Indice : toutes les implémentations de `Map` n'ordonnent pas pareil.
