---
id: 1-9-comprendre-le-pom-maven
sidebar_position: 9
title: "Comprendre le pom.xml (Maven)"
description: "À quoi sert un outil de build, comment lire le pom.xml présent dans chaque exercice, d'où viennent JUnit et AssertJ, et comment lancer les tests soi-même."
---

# Comprendre le pom.xml (Maven)

## Pourquoi ce chapitre

Vous l'avez peut-être remarqué : **chaque exercice contient un fichier `pom.xml`**. Ce n'est pas du
décor. C'est le fichier de configuration de **Maven**, l'**outil de build** qui compile votre code,
récupère les bibliothèques dont il a besoin, et lance les tests. Comprendre ce fichier, c'est
démystifier ce qui se passe quand vous rendez un exercice — et c'est un réflexe que vous garderez
toute votre carrière (tout vrai projet Java a un outil de build).

> Bonne nouvelle : pour réussir la Piscine, **vous n'avez rien à modifier** dans le `pom.xml`.
> `submit` s'occupe de tout. Ce chapitre est là pour que ça ne reste pas une boîte noire.

## Ce que vous saurez faire à la fin

- Expliquer à quoi sert un **outil de build**.
- Lire les **coordonnées** et les **dépendances** d'un `pom.xml`.
- Savoir d'où viennent **JUnit** et **AssertJ**.
- Lancer les tests **vous-même** avec `mvn test` (optionnel).
- Situer **Gradle** par rapport à Maven.

## À quoi sert un outil de build ?

Compiler à la main avec `javac`, puis aller télécharger les bonnes versions de chaque bibliothèque,
puis lancer les tests avec la bonne ligne de commande… c'est faisable pour un fichier, **intenable**
pour un vrai projet. Un **outil de build** automatise tout ça :

- **compiler** votre code (`javac` pour vous) ;
- **gérer les dépendances** : télécharger les bibliothèques (JUnit, AssertJ…) dans les bonnes versions ;
- **lancer les tests** ;
- **packager** le résultat (un `.jar`).

Pour Java, les deux outils dominants sont **Maven** et **Gradle**. Vos exercices utilisent **Maven**.

## Anatomie du `pom.xml` d'un exercice

```xml
<project ...>
  <modelVersion>4.0.0</modelVersion>

  <!-- 1. Les « coordonnées » : l'identité du projet -->
  <groupId>piscine.m1</groupId>
  <artifactId>hello-world</artifactId>
  <version>1.0.0</version>

  <!-- 2. Les dépendances : les bibliothèques utilisées (ici, pour les tests) -->
  <dependencies>
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter</artifactId>
      <version>5.11.0</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.assertj</groupId>
      <artifactId>assertj-core</artifactId>
      <version>3.26.3</version>
      <scope>test</scope>
    </dependency>
  </dependencies>
</project>
```

Deux blocs à retenir :

1. **Les coordonnées** (`groupId` / `artifactId` / `version`) — l'« adresse » unique du projet. C'est
   ainsi que Maven identifie *votre* projet **et** chaque bibliothèque.
2. **Les dépendances** — la liste des bibliothèques nécessaires. Ici :
   - **JUnit** (`junit-jupiter`) : le moteur de tests ;
   - **AssertJ** (`assertj-core`) : les assertions lisibles (`assertThat(x).isEqualTo(y)`).
   - `<scope>test</scope>` signifie « utile seulement pour les tests, pas dans le programme final ».

Maven télécharge ces bibliothèques **une fois** depuis un grand dépôt en ligne, le **Maven Central**,
et les met en cache sur votre machine. Vous n'installez rien à la main.

## Lancer les tests vous-même (optionnel)

Quand vous rendez avec `submit`, la moulinette compile et teste pour vous. Mais vous **pouvez** le
faire à la main, depuis le dossier de l'exercice :

```bash
mvn test
```

Maven enchaîne alors le **cycle** : `compile` (votre code) → `test-compile` (les tests) → `test`
(les exécute). S'il manque un `;`, l'étape `compile` échoue et s'arrête là — exactement comme la
moulinette.

## Et Gradle ?

Vous croiserez le mot **Gradle** : c'est l'autre grand outil de build Java. La **plateforme
Piscine elle-même** (la moulinette) est construite avec Gradle ; **vos exercices**, eux, utilisent
Maven. Même rôle (compiler, dépendances, tests), syntaxe différente (`build.gradle` au lieu de
`pom.xml`). Savoir lire l'un aide à lire l'autre.

## En résumé

- Le `pom.xml` décrit **comment construire** l'exercice : son identité et ses **dépendances**.
- **JUnit + AssertJ** sont déclarés comme dépendances de test, téléchargées depuis **Maven Central**.
- `mvn test` reproduit ce que fait la moulinette ; mais `submit` suffit.
- **Maven** (vos exercices) et **Gradle** (la plateforme) sont deux outils de build au rôle identique.

## Pour aller plus loin (optionnel)

- Lancez `mvn -version` puis `mvn test` sur un exercice déjà réussi et observez les étapes affichées.
- Ouvrez le `pom.xml` d'un exercice plus avancé : repérez le plugin qui ajoute les tests « privés ».
- Renseignez-vous sur le **cycle de vie Maven** complet (`validate`, `compile`, `test`, `package`, `install`).
