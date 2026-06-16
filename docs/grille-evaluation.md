# Grille d'évaluation — critères `formateur`

> Référentiel de notation des critères **humains** d'un exercice, c'est-à-dire ceux
> marqués `type: formateur` dans `evaluation.yml` (voir [`format-exercice.md`](format-exercice.md) §10).
> La moulinette **ne** note **pas** ces critères : elle les laisse à l'instructeur,
> qui s'appuie sur les grilles ci-dessous pour rendre une note **justifiable et reproductible**.

## 1. À quoi sert cette grille

Un `evaluation.yml` mélange deux familles de critères :

- `type: automatique` — calculés par la moulinette (tests publics, tests privés, style Checkstyle/PMD). Pas d'humain dans la boucle.
- `type: formateur` — jugement humain sur la **qualité** du rendu, au-delà du « ça compile et ça passe ».

`format-exercice.md` §10 impose : *« Tout critère `formateur` doit être justifiable par une grille »*. Ce document **est** cette grille. Sans elle, deux instructeurs noteraient différemment le même rendu — ce qui est inacceptable dans un cadre institutionnel où la note doit pouvoir être défendue devant le stagiaire et la hiérarchie.

## 2. Les quatre types de critères humains

Chaque critère `formateur` d'un `evaluation.yml` porte un `id` qui le rattache à **une** des grilles ci-dessous :

| `id` | Question à laquelle le critère répond |
|------|----------------------------------------|
| `demarche` | Le raisonnement est-il pertinent ? Le bon outil pour le bon problème ? |
| `lisibilite` | Un autre humain comprend-il le code sans effort ? |
| `idiomatisme` | Est-ce du Java écrit « comme un développeur Java », pas du C/Python transposé ? |
| `respect-consignes` | Le stagiaire a-t-il fait **ce qui était demandé**, ni plus ni moins ? |

Un exercice n'utilise généralement **qu'un seul** de ces critères (poids typique : 2/20). On choisit celui qui porte l'enjeu pédagogique de l'exercice. Rien n'interdit d'en cumuler plusieurs si le total des poids reste cohérent.

## 3. Principe de notation : 4 niveaux

Chaque grille décline **quatre niveaux**. Pour convertir un niveau en points, on applique une fraction du poids du critère :

| Niveau | Fraction du poids | Exemple sur un critère à **2 pts** | Exemple sur **4 pts** |
|--------|-------------------|-----------------------------------|------------------------|
| **Excellent** | 100 % | 2 | 4 |
| **Bon** | 75 % | 1,5 | 3 |
| **Passable** | 50 % | 1 | 2 |
| **Insuffisant** | 0 % | 0 | 0 |

Règles :

- On arrondit au demi-point le plus proche.
- En cas d'hésitation entre deux niveaux, on retient le **plus bas** et on justifie par un commentaire concret (« la méthode `f` mélange lecture clavier et calcul → lisibilité Passable »).
- La note d'un critère humain s'accompagne **toujours** d'un commentaire d'une phrase rattaché à un fait observable. Pas de note nue.

## 4. Les grilles

### 4.1 `demarche` — pertinence de la démarche

Évalue le **choix de la solution** : structure de contrôle adaptée, type de donnée adapté, absence de complexité inutile ou de détours. On ne note pas la beauté du code (→ `lisibilite`) ni les tournures Java (→ `idiomatisme`), mais le **raisonnement**.

| Niveau | Descripteur | Indices observables |
|--------|-------------|---------------------|
| **Excellent** | Démarche directe et adaptée ; le bon outil pour le problème. | Boucle/condition appropriée ; type numérique correct (`long` quand un `int` déborderait) ; pas de cas particulier traité « en dur ». |
| **Bon** | Démarche correcte avec une maladresse mineure sans impact fonctionnel. | Une variable intermédiaire superflue ; un `if/else` là où un `else` suffisait ; ordre des conditions non optimal mais correct. |
| **Passable** | Démarche fonctionnelle mais détournée ou fragile. | Recopie manuelle au lieu d'une boucle ; conversion de type évitable ; logique correcte par accident (dépend de l'ordre des tests). |
| **Insuffisant** | Démarche inadaptée, même si les tests passent. | Solution « en dur » qui ne marche que pour les cas testés ; structure de contrôle contredisant l'énoncé (récursif imposé → itératif, ou l'inverse). |

### 4.2 `lisibilite` — lisibilité du code

Évalue si un **autre humain** comprend le code rapidement. Indépendant de la justesse : un code juste peut être illisible.

| Niveau | Descripteur | Indices observables |
|--------|-------------|---------------------|
| **Excellent** | Se lit comme une explication. Noms parlants, structure claire. | Variables nommées par leur rôle (`rayon`, `nbEssais`) ; une responsabilité par bloc ; pas de commentaire nécessaire pour suivre. |
| **Bon** | Lisible, avec une zone qui demande un léger effort. | Un nom abrégé (`r`, `tmp`) isolé ; une ligne un peu dense mais décodable ; indentation correcte. |
| **Passable** | Compréhensible au prix d'un effort réel. | Plusieurs noms opaques (`a`, `b`, `x1`) ; expression composée non décomposée ; indentation irrégulière. |
| **Insuffisant** | Demande de reconstituer mentalement l'intention. | Noms trompeurs ou tous d'une lettre ; tout dans une expression géante ; code mort ; mise en forme anarchique. |

### 4.3 `idiomatisme` — idiomatisme Java

Évalue si le code est écrit **« comme du Java »** et non comme du C, du Python ou du pseudo-code transposé.

| Niveau | Descripteur | Indices observables |
|--------|-------------|---------------------|
| **Excellent** | Tournures naturelles à un développeur Java. | `for (int i = 0; ...)` standard ou boucle adaptée ; `String.format`/concaténation appropriée ; `equals` pour les chaînes ; pas de réinvention de la roue. |
| **Bon** | Idiomatique avec un écart mineur. | Un `==` sur `String` qui marche par chance sur des littéraux ; un `Integer` boîté là où un `int` suffit ; verbosité évitable. |
| **Passable** | Java « accentué » d'un autre langage. | Drapeau booléen géré à la main au lieu d'un `break` ; concaténation pénible évitable par `printf`/`format` ; conventions de nommage non Java (`ma_variable`). |
| **Insuffisant** | Translittération d'un autre langage. | `snake_case` partout ; gestion mémoire/pointeurs simulée ; n'utilise aucune construction propre à Java là où elle s'impose. |

### 4.4 `respect-consignes` — respect des consignes

Évalue si le stagiaire a fait **exactement** ce qui était demandé : périmètre, contraintes explicites, format de sortie, fichiers à ne pas toucher.

| Niveau | Descripteur | Indices observables |
|--------|-------------|---------------------|
| **Excellent** | Toutes les consignes respectées à la lettre. | N'a modifié que la zone prévue (ex. corps du `main`) ; respecte les contraintes (pas d'import interdit, méthode imposée présente) ; format de sortie exact. |
| **Bon** | Consignes respectées, un détail de forme à côté. | Un espace en trop dans un message ; un import inutile mais non interdit ; un commentaire `// TODO` du starter laissé en place. |
| **Passable** | Une consigne explicite contournée sans casser les tests. | A modifié la signature alors qu'elle était imposée ; a ajouté une classe non demandée ; a ignoré une contrainte non testée automatiquement. |
| **Insuffisant** | Consignes ignorées. | A réécrit/supprimé du code fourni qu'il fallait conserver ; a utilisé un mécanisme explicitement interdit ; hors-sujet partiel. |

## 5. Articulation avec `evaluation.yml`

L'`id` du critère `formateur` dans `evaluation.yml` **doit** correspondre à l'une des quatre grilles (`demarche`, `lisibilite`, `idiomatisme`, `respect-consignes`). La `description` précise l'angle propre à l'exercice ; la grille fournit l'échelle.

Exemple (extrait d'un `evaluation.yml`) :

```yaml
  - id: respect-consignes
    description: "N'a modifié que le corps du main, sortie au format exact"
    poids: 2
    type: formateur
```

→ L'instructeur ouvre la grille **4.4**, situe le rendu sur les 4 niveaux, applique le barème de la **section 3** (2 / 1,5 / 1 / 0) et justifie d'une phrase.

## 6. Exemple appliqué — exercice 1.1.1 `hello-world`

`evaluation.yml` de 1.1.1 :

```yaml
  - id: respect-consignes
    description: "N'a modifié que le corps du main (classe et signature inchangées)"
    poids: 2
    type: formateur
```

Le starter fournit la classe `HelloWorld` et la signature `public static void main(String[] args)` ; la consigne est d'afficher `Hello, world!` **dans le corps du `main`**, sans rien changer d'autre.

Cas concrets de notation (grille **4.4**, poids 2) :

| Rendu observé | Niveau | Points | Commentaire type |
|---------------|--------|--------|------------------|
| A complété le corps du `main` avec `System.out.println("Hello, world!");`, rien d'autre touché. | **Excellent** | 2 | « Périmètre respecté à la lettre. » |
| Idem mais a laissé le commentaire `// TODO` du starter au-dessus de sa ligne. | **Bon** | 1,5 | « Consigne respectée ; reste un `// TODO` du starter à nettoyer. » |
| A renommé la classe `HelloWorld` en `Main` (les tests passent quand même via réflexion). | **Passable** | 1 | « A modifié le nom de classe imposé par le starter. » |
| A supprimé la méthode `main` fournie pour écrire son propre point d'entrée non conforme. | **Insuffisant** | 0 | « Structure imposée non respectée. » |

> Note : le critère `respect-consignes` reste **distinct** des tests automatiques. Un rendu peut passer 100 % des tests et n'être que **Passable** ici (ex. nom de classe changé) — c'est précisément ce que la part humaine attrape.

---

*Version 1 — 2026-06-01. Toute évolution de ces grilles passe par une PR ; l'impact sur les `evaluation.yml` existants doit être évalué.*
