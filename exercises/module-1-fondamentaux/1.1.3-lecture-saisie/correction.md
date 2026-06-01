# Correction — Exercice 1.1.3 Lecture d'une saisie

## Démarche attendue

Le squelette crée déjà le `Scanner` et affiche les questions. Il reste à lire les réponses et composer le message :

```java
String prenom = clavier.nextLine();   // toute la ligne
int age = clavier.nextInt();
System.out.println("Bonjour " + prenom + ", vous avez " + age + " ans.");
```

Étapes intellectuelles :

1. **Choisir la bonne méthode de lecture** : pour un prénom qui peut contenir des espaces, `nextLine()` (toute la ligne) plutôt que `next()` (un seul mot).
2. **Lire le prénom avant l'âge** : lire une ligne de texte puis un entier ne pose pas de problème de tampon. C'est l'ordre inverse (`nextInt` puis `nextLine`) qui réclame une purge — voir le chapitre 1.4.
3. **Composer le message** par concaténation.

## Points clés

- **`nextLine()` vs `next()`** : `next()` s'arrête au premier espace. Pour « Jean Le Goff », il ne lirait que « Jean ». `nextLine()` lit toute la ligne.
- **Ordre de lecture** : lire le texte (`nextLine`) d'abord, puis le nombre (`nextInt`), évite le piège du retour à la ligne résiduel décrit au chapitre 1.4.
- **Sortie exacte** : la moulinette compare le message au caractère près (la virgule, l'espace, le point final).

## Erreurs fréquentes observées

- **Utiliser `next()` pour le prénom** → les prénoms composés sont tronqués, le test privé échoue.
- **Lire l'âge avant le prénom** → le `nextLine()` qui suit le `nextInt()` capte un retour à la ligne vide ; le prénom semble « sauté ». Lire dans l'ordre prénom puis âge évite le souci.
- **Message mal ponctué** (`vous avez 42 ans` sans point, ou virgule manquante) → sortie non conforme.

## Variantes possibles

On peut lire l'âge en deux temps (`nextLine()` puis `Integer.parseInt(...)`) pour tout lire ligne par ligne :

```java
String prenom = clavier.nextLine();
int age = Integer.parseInt(clavier.nextLine());
```

C'est une approche valable, parfois plus régulière, mais elle suppose une saisie bien formée.

## Pour approfondir

- [La classe `Scanner` — Javadoc OpenJDK 25](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/Scanner.html)
- [Java Scanner — Baeldung](https://www.baeldung.com/java-scanner)
