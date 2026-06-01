# Spec — #16 Exercices restants du module 1

> Design validé le 2026-06-01 (brainstorming). Tâche backlog **#16**.
> Cycle suivant la séquence MVP [[project_roadmap_mvp_sequence]] : #15 ✅ → **#16** → #13 → #14.
> Références cadre : [`format-exercice.md`](../../format-exercice.md), [`charte-redaction.md`](../../charte-redaction.md), [`referentiel.md`](../../referentiel.md) §4 (sous-groupes module 1), modèle existant `exercises/module-1-fondamentaux/1.1.1-hello-world/`, linter `scripts/lint-exercices.sh`, validateur `scripts/valider-solutions.sh`.

## 1. Objectif et périmètre

Produire les **9 exercices** restants du module 1 sous `exercises/module-1-fondamentaux/`, package `etnc.m1`, au format `format-exercice.md`, chacun avec une **solution de référence qui passe ses tests publics ET privés** (vérifié par le CI `valider-solutions`).

**Sous-groupes** (referentiel §4) :
- **1.1 Premiers pas** : `1.1.2-affichage-formate`, `1.1.3-lecture-saisie`.
- **1.2 Variables et opérateurs** : `1.2.1-conversion-unites`, `1.2.2-calculs-geometriques`, `1.2.3-manipulation-booleenne`.
- **1.3 Contrôle de flux** : `1.3.1-fizzbuzz`, `1.3.2-fibonacci-iteratif`, `1.3.3-table-multiplication`, `1.3.4-devine-le-nombre`.

**Hors périmètre** : chapitres (#15 ✅), grille (#13), gouvernance (#14), modules 2+.

## 2. Découpage de livraison (décision validée)

**3 PRs, une par sous-groupe**, dans l'ordre 1.1 → 1.2 → 1.3. Chaque PR : branche issue de `main`, un commit par exercice, PR relue après CI vert. Une seule spec (ce document) ; le plan d'implémentation aura **3 phases**.

## 3. Gabarit d'un exercice (contrat structurel)

Chaque dossier `M.S.E-slug/` contient (linter `lint-exercices.sh` + format §2) :

```
M.S.E-slug/
├── sujet.md            # énoncé (format §3)
├── metadata.yml        # champs requis : slug, titre, module, sous_groupe, position,
│                       #   difficulte, duree_estimee_min, objectifs_pedagogiques,
│                       #   auteur, version, date_creation (+ prerequis, notions)
├── evaluation.yml      # somme des poids == total (gabarit 1.1.1)
├── correction.md       # format §9
├── starter/
│   ├── pom.xml         # copie du starter 1.1.1 (build-helper : ../tests)
│   └── src/main/java/etnc/m1/<Classe>.java
├── solution/
│   ├── pom.xml         # copie solution 1.1.1 (build-helper : ../tests ET ../tests-prives)
│   └── src/main/java/etnc/m1/<Classe>.java
├── tests/
│   └── src/test/java/etnc/m1/<Classe>Test.java
│   └── src/test/java/etnc/util/CaptureSortie.java   # copié du modèle
│   └── src/test/java/etnc/util/CaptureEntree.java   # nouveau, si lecture clavier
└── tests-prives/       # optionnel (cf. §6)
    └── src/test/java/etnc/m1/<Classe>PriveTest.java
```

- `artifactId` du `pom.xml` = slug (starter) / slug-solution (solution), sur le modèle 1.1.1. `<maven.compiler.release>25</maven.compiler.release>`, JUnit 5.11.0 + AssertJ 3.26.3.
- `evaluation.yml` reprend les 4 critères du modèle (`tests-publics`, `tests-prives`, `style`, `demarche`) avec poids = `total` (20). Pour un exo **sans** tests-privés (1.1.2), reventiler le poids des privés sur les publics (ex. publics 16 / style 2 / démarche 2).

## 4. Interface et stratégie de test (décisions validées)

### 4.1 Tout dans `main` + entrée/sortie standard
Le stagiaire n'écrit que le corps de `main` (parfois quelques lignes au-delà du `// TODO`). **Aucune méthode à signature** (paramètres/retour) n'est demandée : ces notions arrivent au module 2 (respect strict de l'antériorité). Les exemples des exos n'utilisent que des notions des chapitres 1.1→1.6.

### 4.2 Utilitaires de test (dans `tests/.../etnc/util/`)
- **`CaptureSortie`** : copié tel quel du modèle 1.1.1 (capture `System.out`).
- **`CaptureEntree`** : **nouvel** utilitaire, ajouté aux exos qui lisent au clavier. Il fixe `System.in` sur un contenu fourni, exécute une action, restaure `System.in` dans un `finally`. Esquisse de contrat :

```java
public final class CaptureEntree {
    private CaptureEntree() {}
    /** Exécute action() en alimentant System.in avec le texte donné, puis restaure System.in. */
    public static void avecEntree(String entree, Runnable action) {
        InputStream original = System.in;
        try {
            System.setIn(new ByteArrayInputStream(entree.getBytes(StandardCharsets.UTF_8)));
            action.run();
        } finally {
            System.setIn(original);
        }
    }
}
```

Les tests combinent les deux : `CaptureEntree.avecEntree("...", () -> sortie = CaptureSortie.capturer(() -> Classe.main(new String[]{})))`. Le texte d'entrée se termine par `\n` (ou plusieurs lignes séparées par `\n`).

### 4.3 Lecture clavier partout où c'est naturel (décision validée)
Les exos calculatoires lisent leur(s) donnée(s) au clavier via `Scanner` (réinvestit le chapitre 1.4). **FizzBuzz** (1.3.1) fait exception : il parcourt 1→100 fixe, sans entrée.

### 4.4 Cas 1.3.4 — nombre secret déterministe (décision validée)
Le **starter fournit déjà** la ligne de tirage du secret, à graine fixe :

```java
final int GRAINE = 1789;                       // graine documentée, NE PAS modifier
int secret = new Random(GRAINE).nextInt(100) + 1;   // secret entre 1 et 100, fourni
```

Le stagiaire écrit **uniquement** la boucle de jeu (lecture des essais via `Scanner`, comparaison, messages « C'est plus grand » / « C'est plus petit » / « Gagné ! »). Les tests instancient `new Random(1789).nextInt(100) + 1` pour connaître le même secret et alimentent `System.in` avec une séquence d'essais qui converge (ex. recherche dichotomique ou suite directe menant au secret), puis vérifient le message final. La graine `1789` est fixée pour tout l'exo (starter, solution, tests).

## 5. Contenu par exercice

> Antériorité : seules les notions des chapitres 1.1→1.6 sont mobilisées. Les classes sont en `etnc.m1`. Les formats de sortie ci-dessous sont **indicatifs** ; la solution et les tests sont écrits ensemble, donc cohérents — le CI valide la correction.

### Sous-groupe 1.1 — Premiers pas
- **1.1.2 — `AffichageFormate`** (très-facile, pas d'entrée). Afficher une « fiche » multi-lignes à partir de variables fixées (nom, grade, âge…) en combinant `println` et concaténation (chap. 1.2). Tests publics : sortie exacte attendue. **Pas de tests-privés.**
- **1.1.3 — `LectureSaisie`** (facile, entrée). Lire un prénom (`nextLine`) puis un nombre entier (`nextInt`), réafficher un message formaté reprenant les deux. Gérer l'ordre de lecture (le piège `nextInt`/`nextLine` du chap. 1.4). Tests : `CaptureEntree` + `CaptureSortie`. Privés : entrée avec espaces, valeurs différentes.

### Sous-groupe 1.2 — Variables et opérateurs
- **1.2.1 — `ConversionUnites`** (facile, entrée). Lire une température en °C (`nextDouble`) et afficher sa conversion en °F (`f = c * 9 / 5 + 32`). Attention à la division flottante (chap. 1.2/1.3). Privés : valeur négative, zéro, décimale.
- **1.2.2 — `CalculsGeometriques`** (facile, entrée). Lire le rayon d'un cercle (`nextDouble`), afficher aire (`Math.PI * r * r`) et périmètre (`2 * Math.PI * r`). Privés : rayon entier vs décimal, grande valeur.
- **1.2.3 — `AnneeBissextile`** (moyen, entrée). Lire une année (`nextInt`), dire si elle est bissextile : `(annee % 4 == 0 && annee % 100 != 0) || annee % 400 == 0`. Met en œuvre `%`, `&&`, `||` (chap. 1.3). Privés : 1900 (non), 2000 (oui), 2024 (oui), 2023 (non).

### Sous-groupe 1.3 — Contrôle de flux
- **1.3.1 — `FizzBuzz`** (facile, pas d'entrée). Afficher 1→100 : `Fizz` (mult. 3), `Buzz` (mult. 5), `FizzBuzz` (mult. 15), sinon le nombre. Boucle `for` + `if`/`else if` + `%`. Tests publics : lignes clés (3, 5, 15, nombres simples) ; privés : sortie complète des 100 lignes.
- **1.3.2 — `FibonacciIteratif`** (moyen, entrée). Lire `n` (`nextInt`), afficher le n-ième terme de la suite de Fibonacci calculé **itérativement** (boucle, deux variables glissantes). Privés : n=0 (0), n=1 (1), n=10 (55), n grand dans la plage `long`.
- **1.3.3 — `TableMultiplication`** (facile, entrée). Lire un entier `n` (`nextInt`), afficher sa table de 1 à 10 (`n x i = ...`). Boucle `for`. Privés : n négatif, n=0.
- **1.3.4 — `DevineLeNombre`** (moyen, entrée). Secret fourni par le starter (§4.4). Le stagiaire écrit la boucle `while` qui lit les essais et répond « C'est plus grand » / « C'est plus petit » / « Bravo, gagné ! ». Tests : séquence d'essais convergente connaissant la graine ; privés : essai trouvé du premier coup, plusieurs essais.

## 6. Tests-privés (format §7)

Présents partout **sauf 1.1.2**. Ils couvrent les cas limites listés au §5. Quand absents, le `pom.xml` de la `solution/` ne câble que `../tests` (sinon il câble aussi `../tests-prives`). Le linter accepte l'absence de `tests-prives/`.

## 7. Vérification

- **Local (sans Java 25)** : `bash scripts/lint-exercices.sh` valide structure + YAML (PyYAML requis). Je relis le code à la main (Java 8 ne peut compiler `release 25`).
- **CI (par PR)** — gate réel :
  - `lint-exercices` : structure + métadonnées de chaque exo.
  - `valider-solutions` : `mvn test` sur chaque `solution/` → publics **et** privés verts. Un exo dont la solution échoue casse le job.
  - `build-docusaurus` : inchangé (les exos ne touchent pas le site).
- **Relecture humaine** (charte §10, format §12) : sujet clair, code idiomatique et lisible, niveau de langue, cohérence référentiel.

## 8. Workflow d'exécution (par sous-groupe)

1. Branche `feature/m1-exos-<sous-groupe>` (ex. `feature/m1-exos-1-1`) issue de `main`.
2. Écrire chaque exo (gabarit §3), un commit par exo.
3. `bash scripts/lint-exercices.sh` en local (doit passer).
4. Push, PR « #16 — exercices <sous-groupe> », attendre CI vert (surtout `valider-solutions`).
5. Relecture formateur → merge → resync `main` local.
6. À la clôture du **3ᵉ** sous-groupe : cocher #16 dans `docs/backlog.md` (statut `Faite`).

## 9. Critères d'acceptation (rappel backlog #16)

- [ ] Les 9 exercices existent et respectent le format (11 fichiers, ou 8 sans tests-privés pour 1.1.2).
- [ ] `mvn test` (CI `valider-solutions`) passe sur chaque `solution/` : publics ET privés verts.
- [ ] `lint-exercices` passe sur les 9 exos.
- [ ] Le CI #11 reste vert (les 4 jobs).
- [ ] Aucune notion au-delà des chapitres 1.1→1.6 (antériorité).

## 10. Risques et parades

- **Pas de compilation locale (Java 8)** → tout repose sur le CI. Parade : code de solution simple et idiomatique, lint local systématique, un sous-groupe par PR pour isoler une régression.
- **Sorties exactes fragiles** (espaces, ponctuation, séparateur de ligne) → tests et solution écrits de pair ; usage de `System.lineSeparator()` comme dans le modèle 1.1.1.
- **Piège `nextInt`/`nextLine`** (1.1.3) → traité explicitement dans la solution et documenté en correction.
- **Stabilité de `Random(graine)`** (1.3.4) → l'algorithme de `java.util.Random` est spécifié par le JDK et stable entre versions ; la graine `1789` donne un secret reproductible.
