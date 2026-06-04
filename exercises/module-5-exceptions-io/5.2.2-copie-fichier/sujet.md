# Exercice 5.2.2 — Copie de fichier (Files.copy octet-exact)

## Contexte

Copier un fichier peut sembler simple : lire le contenu, écrire ailleurs.
Pourtant, cette approche en deux temps — lire en texte (`readAllLines`) puis
réécrire (`write`) — peut **altérer silencieusement** le fichier d'origine :
fins de ligne normalisées, encodage recalculé, byte-order mark ignoré…

Pour une copie **fidèle** (archives, binaires, fichiers de configuration
sensibles à l'octet près), Java NIO.2 fournit `Files.copy` qui transfère les
octets sans interprétation. L'option `StandardCopyOption.REPLACE_EXISTING`
permet d'écraser la destination si elle existe déjà, sans erreur.

## Énoncé

Complétez la classe `CopieFichier` avec la méthode statique :

```java
public static void copier(Path source, Path destination) throws IOException
```

La méthode doit :

1. Copier le contenu de `source` vers `destination` **octet pour octet**.
2. Si `destination` existe déjà, l'**écraser** (ne pas lever d'exception).
3. Ne **pas** passer par `readAllLines` / `writeString` — utiliser
   `Files.copy` directement.

## Exemple

```text
// Supposons que source contient "Bonjour\nMonde\n"
CopieFichier.copier(source, destination);
// destination contient maintenant exactement "Bonjour\nMonde\n"
// source est inchangée
```

## Contraintes

- Package `etnc.m5`. **Ne modifiez pas** la signature de `copier`.
- La méthode doit utiliser `Files.copy` avec `StandardCopyOption.REPLACE_EXISTING`.
- La copie est **octet-exacte** : un fichier sans `\n` final en source
  doit produire une destination sans `\n` final.
- `throws IOException` est déclaré dans la signature : on la **propage**, on ne
  la gère pas à l'intérieur.

## Ce qui sera vérifié

- Après copie, le contenu de `destination` est identique à celui de `source`.
- Les accents et caractères UTF-8 sont préservés.
- `source` est inchangée après la copie.
- Copier un fichier vide produit une destination vide.
- Un fichier multi-lignes avec `\n` explicites est copié fidèlement.
- Si `destination` existe déjà, elle est écrasée (pas d'exception).
- Un fichier sans `\n` final en source → destination sans `\n` final (octet-exact).
- Modifier la source **après** la copie n'affecte pas la destination.

## Pour aller plus loin (optionnel — non noté)

- Que se passe-t-il si `source` et `destination` désignent le même fichier ?
  Essayez avec `StandardCopyOption.REPLACE_EXISTING`.
- Comment copier récursivement un répertoire entier avec NIO.2 ?
- Quelle est la différence entre `Files.copy(Path, Path)` et
  `Files.copy(InputStream, Path)` ?
