# Correction — Exercice 5.2.2 Copie de fichier (Files.copy octet-exact)

## Démarche attendue

```java
public static void copier(Path source, Path destination) throws IOException {
    // Files.copy transfère les octets sans les interpréter comme du texte.
    // REPLACE_EXISTING écrase silencieusement la destination si elle existe.
    Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
}
```

## Points clés

- **Copie octet-exacte** : `Files.copy(Path, Path, CopyOption...)` ne passe pas
  par un décodage/ré-encodage textuel. Les fins de ligne (`\n`, `\r\n`) et
  l'encodage original sont conservés tels quels.

- **`REPLACE_EXISTING`** : sans cette option, `Files.copy` lève
  `FileAlreadyExistsException` si la destination existe. L'option désactive
  ce comportement et écrase silencieusement.

- **`throws IOException` propagé** : la méthode ne gère pas l'exception ; elle
  la déclare pour que l'appelant décide quoi faire (afficher un message,
  réessayer, alerter…). C'est le contrat habituel des méthodes NIO.2.

- **Pas de `readAllLines` + `write`** : lire le fichier ligne par ligne puis
  réécrire pourrait normaliser les fins de ligne et ajouter (ou supprimer)
  un `\n` final selon la plateforme. Pour des fichiers sensibles, c'est
  une corruption silencieuse.

## Erreurs fréquentes observées

- **Utiliser `readAllLines` + `write`** : compile, fonctionne souvent, mais
  altère les fins de ligne sur Windows (normalisées en `\n` ou `\r\n` selon
  l'API utilisée). Les tests octet-exact le détectent.

- **Oublier `REPLACE_EXISTING`** : le test d'écrasement lève alors
  `FileAlreadyExistsException` au lieu de réussir.

- **Attraper `IOException` à l'intérieur** : l'énoncé demande de propager
  (`throws IOException`), pas de gérer. Un `try/catch` qui avalelerreur
  silencieusement est une erreur de conception.

- **Copier source dans source** : `Files.copy(f, f, REPLACE_EXISTING)` vide
  le fichier sur certaines JVM. Ce cas n'est pas demandé ici mais vaut la peine
  d'être connu.

## Pour approfondir

- `Files.move` vs `Files.copy` : `move` peut être atomique (même partition) ;
  `copy` crée toujours une deuxième entrée indépendante.

- `Files.copy(InputStream, Path)` : copier le contenu d'un `InputStream` vers
  un fichier (utile pour télécharger une ressource réseau).

- `Files.walk` + `Files.copy` : pour copier un répertoire entier récursivement,
  on parcourt l'arbre avec `Files.walk` et on applique `Files.copy` sur chaque
  élément en reconstituant le chemin relatif.
