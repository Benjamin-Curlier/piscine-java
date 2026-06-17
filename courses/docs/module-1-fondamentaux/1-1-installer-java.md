---
id: 1-1-installer-java
sidebar_position: 1
title: "Installer Java 25 et son premier programme"
description: "Installer le JDK Temurin 25, vérifier l'installation, écrire et exécuter un premier programme Java."
---

# Installer Java 25 et son premier programme

## Pourquoi ce chapitre

Avant d'écrire la moindre ligne de Java, il faut **installer le langage** sur votre machine et **vérifier que tout fonctionne**. Vous allez ensuite écrire votre tout premier programme — un classique : afficher un message à l'écran.

À la fin du chapitre, vous aurez compilé et exécuté votre premier code Java, et vous saurez à quoi sert chaque étape.

## Ce que vous saurez faire à la fin

- **Installer** le JDK Temurin 25 sur votre système.
- **Vérifier** votre installation en ligne de commande.
- **Écrire** un programme Java minimal et l'enregistrer correctement.
- **Compiler** votre programme avec `javac`.
- **Exécuter** votre programme compilé avec `java`.

## 1. Java, le JDK, la JVM : de quoi parle-t-on ?

Trois mots reviennent sans cesse quand on parle de Java. Démêlons-les tout de suite.

- **Java** est un **langage de programmation**. Quand vous écrivez du Java, vous écrivez du texte dans un fichier qui suit certaines règles de syntaxe.
- La **JVM** (*Java Virtual Machine*, la "machine virtuelle Java") est le **programme qui exécute** votre code Java. Elle prend votre code compilé et le fait tourner sur votre système — Windows, Linux ou macOS, peu importe.
- Le **JDK** (*Java Development Kit*, le "kit de développement Java") est l'**ensemble d'outils** dont vous avez besoin pour écrire du Java : il contient la JVM, le compilateur (`javac`), le lanceur (`java`), et une grosse bibliothèque standard.

> **À retenir** : en pratique, **vous installez un JDK**. Le JDK contient tout ce qu'il vous faut, y compris la JVM.

Il existe plusieurs distributions du JDK (toutes compatibles entre elles pour notre usage). Nous utiliserons **Eclipse Temurin**, une distribution gratuite, open source et largement adoptée en entreprise.

## 2. Installer le JDK Temurin 25

### Sous Windows

1. Rendez-vous sur la page de téléchargement de Temurin : [adoptium.net/temurin/releases](https://adoptium.net/temurin/releases/).
2. Sélectionnez :
   - **Operating System** : `Windows`
   - **Architecture** : `x64`
   - **Package Type** : `JDK`
   - **Version** : `25 - LTS`
3. Téléchargez le fichier `.msi`.
4. Lancez l'installeur. **Cochez** les options :
   - `Add to PATH`
   - `Set JAVA_HOME variable`

   Ces deux options vous éviteront pas mal de tracas plus tard.
5. Terminez l'installation.

> **Pas de droits administrateur ?** Téléchargez le **fichier `.zip`** plutôt que le `.msi`, dézippez-le quelque part dans votre dossier utilisateur, et ajoutez le chemin `…\jdk-25\bin` à votre `PATH` utilisateur (panneau de configuration → variables d'environnement).

### Sous Linux (Debian / Ubuntu)

```bash
# Ajout du dépôt Eclipse Adoptium
wget -qO- https://packages.adoptium.net/artifactory/api/gpg/key/public | sudo tee /etc/apt/keyrings/adoptium.asc
echo "deb [signed-by=/etc/apt/keyrings/adoptium.asc] https://packages.adoptium.net/artifactory/deb $(lsb_release -cs) main" | sudo tee /etc/apt/sources.list.d/adoptium.list
sudo apt update

# Installation du JDK 25
sudo apt install temurin-25-jdk
```

### Sous macOS

```bash
brew install --cask temurin@25
```

## 3. Vérifier l'installation

Ouvrez un **nouveau terminal** (Invite de commandes, PowerShell, Terminal Linux/macOS) et tapez :

```bash
java --version
```

Vous devez voir quelque chose comme :

```text
openjdk 25 2025-09-16
OpenJDK Runtime Environment Temurin-25 (build 25+...)
OpenJDK 64-Bit Server VM Temurin-25 (build 25+..., mixed mode)
```

Tapez également :

```bash
javac --version
```

Vous devez voir :

```text
javac 25
```

> **Si l'une des deux commandes n'est pas reconnue**, c'est que le `PATH` n'est pas correctement configuré. Redémarrez votre terminal d'abord. Si le problème persiste, voir la section [Erreurs fréquentes](#erreurs-fréquentes) plus bas.

## 4. Anatomie d'un programme Java

Tout programme Java est constitué d'au moins **une classe** qui contient au moins **une méthode appelée `main`**. C'est cette méthode `main` que la JVM exécute quand vous lancez le programme.

Voici la structure minimale :

```java
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello, world!");
    }
}
```

Décortiquons :

| Élément | Rôle |
|---|---|
| `public class HelloWorld` | Déclare une **classe** publique nommée `HelloWorld`. Le nom doit correspondre exactement au nom du fichier (`HelloWorld.java`). |
| `public static void main(String[] args)` | La **méthode `main`** : point d'entrée du programme. C'est la forme classique, celle que vous rencontrerez dans la plupart du code et tout au long de ce cours. Java 25 en propose aussi une forme plus courte, présentée juste après. |
| `System.out.println("Hello, world!")` | Affiche le texte `Hello, world!` suivi d'un retour à la ligne, sur la **sortie standard** (la console). |
| `;` | Termine chaque instruction. Oublier le `;` est l'erreur de débutant numéro 1 — vous y aurez droit. |
| `{` et `}` | Délimitent les **blocs** : ici, le contenu de la classe, et le contenu de la méthode. |

> **À retenir** :
> - Une classe par fichier, **même nom** que le fichier.
> - La méthode `main` est le point d'entrée.
> - Toute instruction se termine par `;`.

### La forme moderne : `void main()` (Java 25)

La signature classique `public static void main(String[] args)` fait beaucoup de cérémonie pour un premier programme. Java 25 introduit une **forme raccourcie** (JEP 512) pensée pour les débutants : la méthode `main` peut désormais s'écrire **sans `public`, sans `static` et sans le paramètre `String[] args`**. C'est une méthode d'instance sans argument, mais vous n'avez pas besoin de comprendre ces mots tout de suite — retenez juste qu'elle est plus courte.

En prime, Java 25 met à disposition `IO.println(...)` (et `IO.readln(...)` pour lire au clavier) **sans aucun `import`** : ce sont des méthodes de la classe `java.lang.IO`, toujours disponible.

Voici le même programme, avant et après :

```java
// Forme classique (celle utilisée dans ce cours)
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello, world!");
    }
}
```

```java
// Forme moderne (Java 25, JEP 512) — plus courte
void main() {
    IO.println("Hello, world!");
}
```

Les deux programmes font exactement la même chose. La forme moderne abaisse la barrière d'entrée : moins de mots-clés à retenir le premier jour.

> **À retenir** :
> - La signature de `main` n'est **pas figée** : depuis Java 25, `void main()` (sans `public`, sans `static`, sans `args`) est aussi un point d'entrée valide.
> - `IO.println(...)` et `IO.readln(...)` s'utilisent sans `import` (classe `java.lang.IO`).
> - **Dans ce cours, nous gardons la forme classique** `public static void main(String[] args)` + `System.out.println`, car c'est ce que vous verrez dans la grande majorité du code existant. La forme moderne est bonne à connaître.

## 5. Écrire, compiler, exécuter

### Étape 1 — Écrire le code

Créez un dossier pour vos premiers essais, par exemple `~/piscine/01-hello/`. Dedans, créez un fichier nommé **exactement** `HelloWorld.java` avec votre éditeur favori (Notepad++, VS Code, IntelliJ Community, ce que vous voulez).

Copiez-y le code de la section précédente.

### Étape 2 — Compiler

Ouvrez un terminal **dans ce dossier**, puis :

```bash
javac HelloWorld.java
```

Si tout va bien, la commande **ne dit rien** — pas de message, c'est bon signe en informatique. Un nouveau fichier est apparu : `HelloWorld.class`. C'est votre code **compilé** en *bytecode*, le format que la JVM sait exécuter.

### Étape 3 — Exécuter

```bash
java HelloWorld
```

Notez bien : **sans** le `.class` à la fin. Vous devriez voir :

```text
Hello, world!
```

Félicitations, vous avez écrit, compilé et exécuté votre premier programme Java.

## 6. Pourquoi cette étape "compiler" ?

Vous avez peut-être déjà fait du Python ou du JavaScript : on tape `python mon_script.py` et ça marche, pas d'étape intermédiaire. Pourquoi Java fait-il différemment ?

Java est un langage **compilé** : avant d'exécuter, le compilateur (`javac`) transforme votre code source en **bytecode** (fichier `.class`). La JVM exécute ensuite ce bytecode.

L'avantage : le compilateur attrape **beaucoup d'erreurs avant l'exécution** (fautes de frappe, mauvais types, méthodes inexistantes…). Vous bénéficiez de filets de sécurité que les langages dits "interprétés" n'ont pas.

## Erreurs fréquentes

- **`'javac' n'est pas reconnu comme une commande interne ou externe`** (Windows) ou **`command not found`** (Linux/macOS)
  → Le `PATH` n'inclut pas le dossier `bin` du JDK. Redémarrez votre terminal. Si l'erreur persiste, vérifiez vos variables d'environnement.

- **`Error: Could not find or load main class HelloWorld`** quand vous lancez `java HelloWorld`
  → Vous avez probablement tapé `java HelloWorld.class` (incorrect) ou vous n'êtes pas dans le bon dossier. Vérifiez avec `ls` (Linux/macOS) ou `dir` (Windows) que `HelloWorld.class` est bien là.

- **`error: class HelloWorld is public, should be declared in a file named HelloWorld.java`**
  → Le nom du fichier ne correspond pas au nom de la classe. Renommez le fichier ou la classe pour qu'ils soient identiques (attention à la casse : `helloworld.java` ≠ `HelloWorld.java`).

- **`';' expected`** lors de la compilation
  → Vous avez oublié un point-virgule. Le numéro de ligne indiqué par le compilateur vous dit où regarder.

## Exercice guidé

**Objectif** : modifier votre `HelloWorld.java` pour qu'il affiche **deux** lignes :

```text
Bonjour, l'équipe !
Bienvenue à la Piscine Java.
```

Indices :
- Vous aurez besoin de **deux** instructions `System.out.println(...)`.
- N'oubliez pas le `;` à la fin de chaque instruction.
- Recompilez avec `javac` avant de relancer `java`.

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

```java
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Bonjour, l'équipe !");
        System.out.println("Bienvenue à la Piscine Java.");
    }
}
```

</details>

## Vérifiez vos acquis

- À quoi sert exactement le compilateur `javac` ?
- Pourquoi le fichier doit-il s'appeler comme la classe ?
- Que se passe-t-il si vous oubliez le `;` à la fin d'une instruction ?
- Différence entre `java` et `javac` ?

## Pour aller plus loin

- [Site officiel Eclipse Adoptium](https://adoptium.net/) — distributions du JDK Temurin.
- [La documentation officielle de la classe `System`](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/lang/System.html) (Oracle) — vous y reviendrez souvent.
- [Java tutorial — Getting Started](https://dev.java/learn/getting-started/) (dev.java) — un parcours plus détaillé sur les bases d'un programme Java.

## Prochain chapitre

→ **[Chapitre 1.2 — Variables et types primitifs](1-2-variables-types-primitifs)**
