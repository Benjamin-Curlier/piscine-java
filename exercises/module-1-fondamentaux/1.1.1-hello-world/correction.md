# Correction — Exercice 1.1.1 Hello World

## Démarche attendue

Une seule instruction suffit dans le corps de `main` :

```java
System.out.println("Hello, world!");
```

Étapes intellectuelles :

1. **Lire l'énoncé jusqu'au bout** avant de coder. La contrainte "exactement `Hello, world!`" implique de faire attention à la casse, à la virgule, au point d'exclamation.
2. **Repérer le squelette fourni** : tout est déjà en place sauf le corps de `main`. Il n'y a rien à toucher d'autre.
3. **Choisir entre `print` et `println`** : le sujet demande un retour à la ligne → c'est `println`.

## Points clés

- **`println` versus `print`** : `println` ajoute un saut de ligne adapté au système (`\n` sous Linux/macOS, `\r\n` sous Windows). `print` n'ajoute rien.
- **Le `;`** termine l'instruction. Sans lui, le compilateur refuse de compiler avec un message du type `';' expected`.
- **Le nom de fichier** (`HelloWorld.java`) **doit correspondre exactement** au nom de la classe (`HelloWorld`). Casse comprise.

## Erreurs fréquentes observées

- **`Hello world!` (sans virgule)** ou **`Hello, World!` (majuscule sur W)** → tests qui échouent. Le sujet est strict sur la chaîne exacte.
- **`System.out.print(...)`** au lieu de `println` → manque le retour à la ligne, le test public échoue avec un message clair.
- **Touche aux imports / au package** → le code compile mais l'exécution échoue parce que la classe n'est plus trouvée dans `piscine.m1`. Toujours respecter les contraintes du sujet sur ce qui peut être modifié.
- **Ajout d'une instruction supplémentaire** ("Bienvenue !" sur une seconde ligne, par exemple) → la sortie ne correspond plus à l'attendu.

## Variantes possibles

Quelques solutions également valides, mais à éviter pour cet exercice (elles sortent du périmètre demandé) :

```java
// Variante 1 — explicite mais lourde
System.out.print("Hello, world!");
System.out.println();
```

```java
// Variante 2 — concatène le séparateur de ligne explicitement
System.out.print("Hello, world!" + System.lineSeparator());
```

Pour `println`, c'est la JVM qui choisit le séparateur de ligne adapté — vous n'avez pas à vous en soucier, c'est l'idiome standard.

## Pour approfondir

- [`System.out` et la classe `PrintStream` — Javadoc](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/io/PrintStream.html)
- [Pourquoi `println` plutôt que `\n` ? — discussion sur Baeldung](https://www.baeldung.com/java-system-lineseparator)
- [Convention de nommage Java — Oracle Code Conventions](https://www.oracle.com/java/technologies/javase/codeconventions-namingconventions.html)
