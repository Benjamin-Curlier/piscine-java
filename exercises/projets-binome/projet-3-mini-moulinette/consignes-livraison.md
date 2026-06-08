# Consignes de livraison — Projet binôme #3 (final)

## Modalités

- Ce projet se réalise **en binôme**. Les binômes sont **imposés par les formateurs**.
- Durée indicative : environ 16 heures, réparties sur plusieurs séances.
- C'est le **projet final** de la Piscine : il mobilise tous les modules (1 à 6).

## Dépôt et workflow Git (exigé)

Contrairement aux projets #1 et #2, un **vrai workflow collaboratif** est attendu ici :

- Le travail se fait sur des **branches de fonctionnalité** (`feature/...`), pas seulement
  sur `main`.
- **Au moins une Pull Request** est ouverte et **relue** par l'autre membre du binôme (ou
  auto-revue documentée dans la PR si votre forge ne permet pas la revue croisée) avant
  d'être fusionnée dans `main`.
- **Les deux membres** commitent régulièrement. L'historique doit refléter une
  **contribution équilibrée** : il sert de preuve du travail de chacun.

## README attendu

Le dépôt contient un fichier `README.md` qui précise :

- **Comment compiler et lancer** la mini-moulinette, et comment lui fournir un fichier
  `Operation.java` et un fichier `cas.csv`.
- Les **choix techniques** : compilation (`javac` ou `JavaCompiler`) et exécution
  (réflexion ou sous-processus `java`).
- L'**emplacement** des fichiers d'entrée (`Operation.java`, `cas.csv`) et de sortie
  (`rapport.md`), et leur encodage (UTF-8).
- Le **taux de couverture JUnit obtenu** (≥ 70 %) et l'outil utilisé pour le mesurer.
- La **répartition du binôme** : qui a réalisé quoi (et le lien vers la/les Pull Request).

## Arborescence et format

- Les sources Java sont organisées dans un package (par exemple `etnc`).
- La suite de **tests JUnit** est présente dans le dépôt (par exemple sous `src/test/java`).
- Aucun fichier compilé (`.class`) ni dossier de build n'est versionné (ajoutez-les au
  `.gitignore`).
- Les fichiers produits à l'exécution (`rapport.md`, classes compilées du code testé) **ne
  sont pas** des livrables : ne les versionnez pas. En revanche, **un exemple** de
  `Operation.java` et de `cas.csv` peut être fourni pour la démonstration.
- Le rendu est remis à la date fixée par le formateur.
