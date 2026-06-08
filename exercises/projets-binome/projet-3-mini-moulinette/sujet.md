# Projet binôme #3 — Mini-moulinette pédagogique

## Contexte

Depuis le premier module, une **moulinette** corrige vos rendus : elle compile votre
code, l'exécute sur des cas, et produit un rapport expliquant ce qui passe et ce qui
échoue. Pour ce **projet final**, vous construisez **votre propre version réduite** de cet
outil. C'est la synthèse de tout le parcours : conception orientée objet, collections,
exceptions, lecture de fichiers, **tests automatisés** et **travail Git collaboratif**.

Vous concevez ce logiciel **en binôme**, de A à Z. Le sujet impose **ce que** la
mini-moulinette doit faire ; vous restez libres du **comment** (noms de classes,
signatures, découpage interne).

## Objectifs pédagogiques

- Concevoir une petite application orientée objet réaliste (un pipeline en étapes).
- **Compiler et exécuter** du code Java depuis votre programme, et en interpréter le résultat.
- Lire des **cas de test** depuis un fichier CSV (à la main) et générer un **rapport** lisible.
- Gérer proprement les erreurs (compilation impossible, fichier absent, cas en échec) avec
  des **exceptions**.
- **Tester votre propre code** avec JUnit et viser une **couverture ≥ 70 %**.
- Collaborer en binôme avec un **vrai workflow Git** : branches de fonctionnalité et au
  moins une **Pull Request**.

## Le code à vérifier : un contrat imposé

Pour rester d'un périmètre raisonnable, votre mini-moulinette ne traite **pas** du Java
arbitraire : elle vérifie un fichier `Operation.java` au **contrat fixe** suivant :

```java
public class Operation {
    public int appliquer(int n) {
        // une transformation d'un entier (au choix de l'auteur du fichier testé)
    }
}
```

Votre moulinette compile ce fichier, puis appelle `appliquer(n)` sur une série d'entiers.

## Pipeline imposé (trois étapes)

1. **Compiler** le fichier `Operation.java` fourni en entrée. Vous pouvez compiler avec
   `javac` (lancé via `ProcessBuilder`) **ou** avec l'API `javax.tools.JavaCompiler` — au
   choix, documenté dans le README. Si la compilation échoue, le rapport l'indique
   clairement (avec le message d'erreur) et le traitement s'arrête proprement.
2. **Exécuter** : charger la classe compilée et appeler `appliquer(n)` pour chaque cas.
   Vous pouvez utiliser la **réflexion** (`Class.forName` + `Method.invoke`) ou un
   sous-processus `java` — au choix, documenté. Si un cas provoque une **exception**, ce
   cas est marqué « erreur » et le traitement **continue** avec les cas suivants.
3. **Rapport** : produire un fichier **Markdown** (`rapport.md`) récapitulant, pour chaque
   cas, l'**entrée**, la valeur **attendue**, la valeur **obtenue** et un statut
   (`✓` réussi / `✗` échoué / `erreur`), suivi d'une **synthèse** (`N/total réussis`).

## Cas de test (fichier CSV, à la main)

Les cas sont lus depuis un fichier **`cas.csv`** au format `entree,attendu` (un cas par
ligne), lu en **UTF-8**, **sans bibliothèque** (parsing à la main, acquis du module 5).

- Exemple de `cas.csv` :
  ```text
  0,0
  5,25
  -3,9
  ```
  (ici, on testerait une `Operation` qui élève au carré : `appliquer(n) == n * n`).
- **Fichier absent** : ce n'est pas une erreur fatale — signalez-le par un message clair.
- **Ligne corrompue** (mauvais nombre de champs, entier non numérique) : signalez la ligne
  fautive, **passez à la suivante**, continuez.

## Robustesse et gestion des erreurs (avec exceptions)

La gestion des cas invalides se fait **avec des exceptions** (modules 5 et 6).

- **Compilation impossible** : le rapport l'indique, le programme ne plante pas.
- **Cas en échec ou en erreur** : compté et affiché dans le rapport, sans interrompre les autres.
- **Accès fichier** (`cas.csv`, `Operation.java`, écriture du rapport) : les `IOException`
  sont traitées proprement, avec un message lisible — jamais une trace brute jetée à l'écran.
- Vous pouvez introduire une **exception métier** que vous écrivez vous-mêmes (par exemple
  `CompilationException`) pour signaler l'échec d'une étape.

## Exigence de tests (JUnit, ≥ 70 % de couverture)

C'est **le** nouvel attendu de ce projet final : votre mini-moulinette doit être **testée**.

- Écrivez une **suite de tests JUnit 5 / AssertJ** sur vos composants : au minimum le
  **parsing de `cas.csv`**, la **comparaison** attendu/obtenu, et la **génération du rapport**.
- Mesurez votre **couverture de code** avec un outil (par exemple **JaCoCo**) et visez
  **≥ 70 %**. Le **taux obtenu est reporté dans le README**.
- Pensez « cas limites » : fichier vide, ligne corrompue, cas en erreur, 0 cas réussi.

## Exigence Git (branches + Pull Request)

C'est l'autre nouveauté : un **vrai workflow collaboratif** (module 6).

- Développez sur des **branches de fonctionnalité** (`feature/...`), pas seulement sur `main`.
- Ouvrez **au moins une Pull Request**, relue par l'autre membre du binôme (ou auto-revue
  documentée si la forge ne le permet pas), avant fusion dans `main`.
- L'historique doit refléter une **contribution équilibrée** des deux membres.

## Contrainte d'antériorité

Ce projet final utilise les notions des **modules 1 à 6** — tout est autorisé, **y compris**
ce qui était jusqu'ici interdit comme livrable : **JUnit** (vos propres tests) et le
**workflow Git par branches et Pull Requests**. C'est le premier projet où ils sont **exigés**.

## Démonstration attendue

Le formateur lancera votre mini-moulinette sur **deux** fichiers `Operation.java` :

1. une version **correcte** (tous les cas passent → rapport tout vert) ;
2. une version **buggée** (au moins un cas échoue → le rapport pointe précisément le(s) cas
   en échec).

La lecture du `rapport.md` doit suffire à comprendre ce qui ne va pas. Le formateur
inspectera aussi votre **suite de tests + couverture** et votre **historique Git (branches/PR)**.

## Bonus (facultatifs, valorisés)

- Vérifier **plusieurs** fichiers `Operation.java` d'affilée.
- Rendre la **signature** vérifiée configurable (autre type, plusieurs méthodes).
- Mesurer le **temps** d'exécution de chaque cas.
- Un rapport **HTML** en plus du Markdown.
- Un **isolement** plus poussé de l'exécution du code testé.
