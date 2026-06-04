# Spec — #20 Chapitres du module 4 (Collections, génériques, lambdas)

> Design validé le 2026-06-04 (brainstorming ultracode — 9 analystes + synthèse). Tâche backlog **#20**.
> Cycle isolé : ce cycle ne couvre que les **8 chapitres**. Les 3 sous-groupes d'exercices (4.1 → 4.3) suivront dans leurs propres cycles spec / plan / PR (cadence : chapitres d'abord — les notions bornent ce que les exercices peuvent demander).
> Point de départ : [`docs/superpowers/2026-06-03-m4-kickoff.md`](../2026-06-03-m4-kickoff.md).
> Références cadre : [`charte-redaction.md`](../../charte-redaction.md) (§6 structure, §2/§3 ton/langue, §7 code, §10 relecture), [`referentiel.md`](../../referentiel.md) (§module 4), modèles existants [`module-3-poo/`](../../../courses/docs/module-3-poo/), spec sœur [`2026-06-03-m3-chapitres-design.md`](2026-06-03-m3-chapitres-design.md).

## 1. Objectif et périmètre

Produire les **8 chapitres** du module 4 dans `courses/docs/module-4-collections-generiques-lambdas/`, conformes à la charte §6, cohérents entre eux et avec le module 3 (qui se termine sur le chapitre 3-10 « records et `sealed` »).

**Objectif pédagogique** (référentiel §module 4) : utiliser les structures de données du JDK, comprendre les génériques, manipuler les données avec lambdas et streams.

**Nouveauté structurante** (kickoff §4) : le module 4 **lève les verrous** des modules 1-3. Sont désormais autorisés et enseignés : les **collections** (`List`/`Set`/`Map`/`Deque`) qui remplacent les tableaux à capacité fixe ; l'écriture **manuelle de `equals`/`hashCode`** et son contrat ; le vrai `java.lang.Comparable<T>` **générique** + `Comparator` (là où 3.4.3 se limitait à une interface maison non générique) ; la **programmation fonctionnelle** (lambdas, interfaces fonctionnelles, streams, `Optional`).

**Hors périmètre** : tout fichier sous `exercises/` (les 12 exos = cycles ultérieurs), la moulinette, la fiche du **Projet binôme #2** (débloqué par le module 5, pas le 4). Aucun exercice noté n'est touché. Les **décisions transverses** prises au brainstorm (squelette `starter/`, test du contrat `equals`/`hashCode`, généricité prouvée par le compilateur, streams libres mais cadrés, modèle de domaine `Grade`/`Soldat`, roster des 12 exos) sont consignées en §10 pour que le futur volet exercices en hérite — elles ne produisent aucun livrable dans ce cycle.

## 2. Livrables

8 fichiers Markdown + 1 fichier de config (à créer) + 1 retouche de liaison :

| Fichier | `id` | `sidebar_position` | Notions (réf. §module 4) |
|---|---|---|---|
| `4-1-list-arraylist.md` | `4-1-list-arraylist` | 1 | interface `List` vs `ArrayList`, `add`/`get`/`set`/`remove`/`size`, parcours, `contains`/`indexOf`, `ArrayList` vs `LinkedList`, la `List` remplace le tableau du module 2 |
| `4-2-set-hashset-equals-hashcode.md` | `4-2-set-hashset-equals-hashcode` | 2 | `Set` (unicité), `HashSet` vs `TreeSet`, `add` qui renvoie `false`, égalité logique (`equals`) vs référence (`==`), **écrire `equals`/`hashCode` à la main + leur contrat** |
| `4-3-map-hashmap.md` | `4-3-map-hashmap` | 3 | association clé→valeur, `put`/`get`/`containsKey`/`remove`, clés uniques (s'appuie sur `equals`/`hashCode` ch.2), `getOrDefault`, `computeIfAbsent`, `HashMap` vs `TreeMap` |
| `4-4-iteration-moderne.md` | `4-4-iteration-moderne` | 4 | `Iterator` (`hasNext`/`next`), le for-each sous le capot, `iterator.remove()` et `ConcurrentModificationException`, `forEach` + 1re lambda « action », vues `keySet`/`values`/`entrySet`, `Map.Entry`, `removeIf` |
| `4-5-generiques.md` | `4-5-generiques` | 5 | pourquoi les génériques (sûreté de type, plus de cast), classe générique `Boite<T>`, méthode générique, bornes `<T extends …>`, wildcards `? extends`/`? super` (PECS en douceur), effacement de type évoqué |
| `4-6-comparable-comparator.md` | `4-6-comparable-comparator` | 6 | le vrai `java.lang.Comparable<T>` (**pont** avec l'interface maison non générique de 3.4.3), tri naturel (`List.sort`), `Comparator<T>`, `comparing`/`thenComparing`, `reversed`/`naturalOrder`, contrat de `compareTo` |
| `4-7-lambdas-interfaces-fonctionnelles.md` | `4-7-lambdas-interfaces-fonctionnelles` | 7 | interface fonctionnelle (`@FunctionalInterface`), syntaxe lambda, `Predicate`/`Function`/`Consumer`/`Supplier`, références de méthode, capture (`effectively final`) |
| `4-8-streams-optional.md` | `4-8-streams-optional` | 8 | `stream()`, intermédiaires (`filter`/`map`/`sorted`/`distinct`/`limit`), terminales (`collect`/`count`/`reduce`/`forEach`), collecteurs (`toList`/`joining`/intro `groupingBy`/`partitioningBy`), `Optional` (présence/absence, `orElse`, `map`), `findFirst`/`max`/`min` |

**Config** : `_category_.json` **à créer** dans `courses/docs/module-4-collections-generiques-lambdas/` (`label` « Module 4 — Collections, génériques, lambdas », `position: 5` — à valider vs sidebar : M3 = position 4, `collapsible: true`, `collapsed: false` pour l'homogénéité, `link.type: generated-index`).

**Retouche de liaison** : le bloc « Prochain chapitre » de `3-10-records-et-sealed.md` (module 3) pointe actuellement vers « Module 4 — Collections, génériques, lambdas *(à venir)* » → le faire pointer vers `4-1-list-arraylist`. Le chapitre 4-8 pointe vers « Module 5 — Exceptions et I/O *(à venir)* ».

## 3. Structure imposée (chaque chapitre)

Ordre strict de la charte §6 (identique aux modules 1 à 3) :

1. Frontmatter : `id`, `sidebar_position`, `title`, `description`.
2. `# Titre`
3. `## Pourquoi ce chapitre` (2–3 phrases).
4. `## Ce que vous saurez faire à la fin` (objectifs en verbes d'action).
5. Sections numérotées `## 1. …`, `## 2. …` : notion + `### Exemple` (code annoté) + `### À retenir` (encadré `>`).
6. `## Erreurs fréquentes` (puces **symptôme → cause → correction**).
7. `## Exercice guidé` (pas-à-pas, solution repliée dans `<details>`).
8. `## Vérifiez vos acquis` (questions ouvertes).
9. `## Pour aller plus loin` (liens externes annotés).
10. `## Prochain chapitre` (→ lien vers le suivant).

**Longueur cible** : 800–2000 mots par chapitre (les modules 1-3 tournaient à ~1 200). Le chapitre 4-8 (streams + `Optional`) est le plus dense — viser le haut de la fourchette, quitte à reporter une notion avancée vers « Pour aller plus loin ».

## 4. Conventions transverses (décisions validées)

- **Exemples indépendants par chapitre** : chaque chapitre prend l'exemple le plus clair pour SA notion, sans fil conducteur imposé. Domaines des exemples guidés choisis **neutres et distincts des exos notés** (anti-spoil, cf. §5.4). Touche militaire seulement quand elle clarifie (charte §4), jamais forcée.
- **Ton / langue** : vouvoiement, phrases courtes, voix active (charte §2). Règle des trois fois pour le vocabulaire technique (charte §3). Acronymes développés à la première occurrence (JDK = Java Development Kit ; API = interface de programmation ; FIFO/LIFO explicités).
- **Code** (charte §7) : identifiants Java en anglais, métier en français quand plus clair ; commentaires français expliquant le *pourquoi* ; lisibilité avant astuce ; **imports explicites** (jamais `import java.util.*` — d'autant plus important au module 4 où chaque collection a son import) ; pas de variable d'une lettre hors `i/j/k` et `e`, plus les paramètres de lambda courts (`s`, `n`) quand le contexte est limpide.
- **Liens « Pour aller plus loin »** standardisés sur les sources du référentiel §2 : **dev.java**, **Baeldung**, **Javadoc OpenJDK 25** (`docs.oracle.com/en/java/javase/25/...`). 1 à 3 liens annotés par chapitre.
- **Markdown** (charte §9) : un seul `#`, pas de saut de niveau de titre, blocs de code toujours typés (` ```java `, ` ```text `).

## 5. Cohérence pédagogique (garde-fous)

### 5.1 Règle d'antériorité — externe (modules précédents)
Tout l'acquis des modules 1-3 est disponible (variables, contrôle de flux, tableaux, chaînes, méthodes `static`, récursivité, POO complète : classes, encapsulation, héritage, polymorphisme, interfaces, enums, records, `sealed`). Aucune notion d'un module **ultérieur** n'apparaît : pas d'**exceptions** ni d'**IO** (module 5), pas de **tests JUnit écrits par le stagiaire** ni de **Git avancé** (module 6). En particulier, **aucune levée d'exception** dans les exemples (les cas limites se gèrent par valeur sentinelle / `Optional` — cf. §10.5), conformément au report des exceptions au module 5.

### 5.2 Règle d'antériorité — **interne au module 4** (garde-fou central)
L'ordre des chapitres suit l'ordre des notions ; chaque chapitre n'emploie que ce qui précède :

- **4-1 `List`** pose la première collection (et le rôle interface/implémentation) sans rien présupposer d'autre.
- **4-2 `Set` + `equals`/`hashCode`** introduit l'unicité **et** l'écriture manuelle de `equals`/`hashCode` (la base contractuelle dont **4-3 `Map`** a besoin pour ses clés).
- **4-3 `Map`** s'appuie sur `equals`/`hashCode` (4-2) pour justifier l'unicité des clés.
- **4-4 itération moderne** parcourt `List`/`Set`/`Map` (4-1 à 4-3) et **introduit la lambda comme simple "action"** (`forEach`) sans encore la théoriser — amorce douce avant 4-7. *(La lambda est en réalité rencontrée dès 4-3 sous forme d'idiome concret — `computeIfAbsent(k, key -> new ArrayList<>())` — présenté comme une « petite recette » ; elle n'est **formalisée** qu'au 4-7. Conséquence pour les exos : 4.1.4 peut utiliser `computeIfAbsent` sans rompre l'antériorité, puisque l'idiome est enseigné au 4-3.)*
- **4-5 génériques** généralise ce que les collections (4-1 à 4-4) utilisaient déjà implicitement (`List<T>`) ; prérequis du `Comparable<T>` de 4-6.
- **4-6 `Comparable`/`Comparator`** s'appuie sur les génériques (4-5) et trie des `List` (4-1).
- **4-7 lambdas / interfaces fonctionnelles** théorise la lambda amorcée en 4-4 ; prérequis des streams.
- **4-8 streams / `Optional`** vient **strictement en dernier** : les pipelines consomment des lambdas (4-7), trient via `Comparator` (4-6), et produisent des collections (4-1 à 4-3).

### 5.3 Mapping antériorité exos → chapitres (borne les futurs exos)
Les chapitres précèdent les exos qui mobilisent leurs notions. Un exo n'utilise jamais une notion vue **plus tard** :

| Sous-groupe d'exos | Chapitres requis (préalables) |
|---|---|
| 4.1 Collections (`List`/`Set`/`Map`/`Deque`) | ch. 1-4 |
| 4.2 Génériques et tri (`Comparable`/`Comparator`) | ch. 5-6 |
| 4.3 Lambdas et streams (`Optional`) | ch. 7-8 |

⇒ Les chapitres d'abord (respecté par ce cycle). Vérification d'antériorité du roster en §10.6 : **aucun risque bloquant**. Point de vigilance retenu : 4.1.4 (groupement par grade) construit sa `Map` **à la main** (`computeIfAbsent`) — **pas** de `Collectors.groupingBy` (ch.8), reporté à 4.3.3.

### 5.4 Exercices guidés distincts des exos secs (cycles ultérieurs)
Comme aux modules 1-3 : **l'exercice guidé d'un chapitre ne donne jamais la solution d'un exo noté** des futurs sous-groupes. Il traite une variante proche servant d'échauffement, sur un **domaine neutre**. Mapping des exos à NE PAS spoiler, par chapitre :

| Chapitre | Exos adossés (à NE PAS spoiler) | Domaine neutre retenu pour le guidé |
|---|---|---|
| 4-1 `List` | 4.1.1 annuaire, 4.1.3 file | Liste de courses (ajouter/retirer/parcourir des articles) — ni annuaire, ni file de mission. |
| 4-2 `Set`/`equals` | 4.1.2 `Utilisateur` unique | Ensemble de mots-clés (tags) d'un article, puis une petite classe `Couleur(r,v,b)` pour `equals`/`hashCode` à la main — pas d'`Utilisateur`. |
| 4-3 `Map` | 4.1.1 annuaire, 4.1.4 groupement | Comptage d'occurrences de mots d'une phrase (`getOrDefault`/`computeIfAbsent`) — pas d'annuaire, pas de groupement par grade. |
| 4-4 itération | 4.1.4 groupement, 4.3.3 groupement-stream | Inventaire matériel (`Map` article→quantité) parcouru/élagué — domaine matériel neutre. |
| 4-5 génériques | 4.2.1 `Pile<T>`, 4.2.2 `Paire<A,B>` | `Boite<T>` (un élément) + méthode générique `premier(List<T>)` — ni pile, ni paire. |
| 4-6 `Comparable`/`Comparator` | 4.2.3 `Soldat` `Comparable`, 4.2.4 comparator chaîné | Tri de `Livre` (année puis titre) — pas de `Soldat`, pas de tri par grade. |
| 4-7 lambdas | 4.3.1 filtrage, 4.3.4 recherche | Filtrer des nombres (`Predicate`), transformer du texte (`Function`) — pas d'agrégation métier. |
| 4-8 streams/`Optional` | 4.3.1 à 4.3.4 | Pipeline sur une liste de mots (filtrer/majuscules/collecter) + 1er mot d'une longueur donnée (`Optional`) — pas de somme/moyenne/`groupingBy` par grade. |

## 6. Plan de contenu par chapitre (cible de rédaction)

Indicatif — le rédacteur ajuste tant que la charte §6 et les garde-fous §5 sont respectés.

- **4-1 `List` et `ArrayList`** — l'interface `List` (le contrat) vs l'implémentation `ArrayList` (`List<String> l = new ArrayList<>();` et pourquoi typer par l'interface) ; `add`/`get`/`set`/`remove`/`size`/`isEmpty` ; parcours `for` indexé et for-each ; `contains`/`indexOf` ; `ArrayList` (accès indexé rapide) vs `LinkedList` (insertions en tête/queue) — quand l'un, quand l'autre ; **la `List` remplace le tableau à capacité fixe du module 2** (pont explicite). Erreurs fréquentes : confondre le type déclaré `List` et l'implémentation, `IndexOutOfBoundsException` sur `get(size())`, modifier la taille pendant un for-each indexé. Domaine guidé : liste de courses.
- **4-2 `Set`, `HashSet`, `equals` et `hashCode`** — `Set` = collection **sans doublon** ; `add` renvoie `false` si déjà présent ; `HashSet` (non ordonné) vs `TreeSet` (trié) ; **égalité logique** (`equals`) vs **égalité de référence** (`==`) ; **écrire `equals` à la main** (champ par champ, `null`-safe, vérification de type) et **`hashCode` à la main** (`Objects.hash(...)`) ; le **contrat** (réflexivité, symétrie, transitivité, cohérence `equals`/`hashCode`) présenté **simplement** ; conséquence concrète : un objet sans `equals`/`hashCode` correct **se duplique** dans un `HashSet`. Erreurs fréquentes : redéfinir `equals` sans `hashCode` (doublons dans le `HashSet`), comparer avec `==` deux objets « égaux », `equals` qui caste sans vérifier le type (`ClassCastException`). Domaine guidé : tags d'un article puis `Couleur(r,v,b)`.
- **4-3 `Map` et `HashMap`** — association **clé → valeur** ; `put`/`get`/`containsKey`/`remove`/`size` ; `get` sur clé absente renvoie `null` → `getOrDefault` ; `computeIfAbsent(k, key -> new ArrayList<>())` pour initialiser une valeur-collection à la volée (la lambda est ici une « petite recette de valeur par défaut » — idiome concret, formalisé au 4-7) ; clés **uniques** (s'appuie sur `equals`/`hashCode` de la clé, ch.2) ; `HashMap` (non ordonnée) vs `TreeMap` (triée par clé). Erreurs fréquentes : `NullPointerException` en chaînant sur un `get` qui renvoie `null`, utiliser comme clé un objet sans `equals`/`hashCode` correct, `get`+`put` là où `computeIfAbsent` est plus clair. Domaine guidé : comptage d'occurrences de mots.
- **4-4 Itération moderne** — l'`Iterator` (`hasNext`/`next`) et **le for-each sous le capot** ; supprimer pendant l'itération : `iterator.remove()` et le piège `ConcurrentModificationException` ; **`forEach(...)` et la première lambda** présentée comme une simple « action à appliquer » (sans théoriser — la théorie est au 4-7) ; parcourir une `Map` via les vues `keySet` / `values` / `entrySet` ; `Map.Entry` (`getKey`/`getValue`) ; `removeIf` comme alternative sûre. Erreurs fréquentes : modifier une collection dans un for-each (`ConcurrentModificationException`), oublier que `keySet` ne donne pas les valeurs, croire que `forEach` peut modifier la collection. Domaine guidé : inventaire matériel.
- **4-5 Génériques** — **pourquoi** : sûreté de type à la compilation, plus de cast (rappel : la `List<String>` du ch.1 était déjà générique) ; classe générique `class Boite<T>` ; **méthode générique** `<T> T premier(List<T> l)` ; **bornes** `<T extends Comparable<T>>` ; **wildcards** `? extends` / `? super` (PECS introduit **en douceur**, exemple à l'appui, sans formalisme) ; limite : **effacement de type** (type erasure) évoqué simplement (pas de `new T[]`). Erreurs fréquentes : utiliser `Object` + cast au lieu d'un type paramétré, confondre `<T>` de classe et `<T>` de méthode, sur-utiliser les wildcards. Domaine guidé : `Boite<T>`.
- **4-6 `Comparable` et `Comparator`** — **le pont (kickoff §4)** : ouvrir par un rappel explicite « en 3.4.3 vous aviez fabriqué une interface maison `Ordonnable` avec `int comparerA(Ordonnable autre)` et un downcast manuel ; voici le vrai `java.lang.Comparable<T>` du JDK » — montrer les deux signatures côte à côte pour rendre visible que les génériques (ch.5) **suppriment le downcast** ; tri naturel `List.sort(null)` / `Collections.sort` sur des éléments `Comparable` ; `Comparator<T>` pour un tri externe ; `Comparator.comparing(...)` et `thenComparing(...)` (tri à plusieurs critères) ; `reversed()` / `naturalOrder()` ; contrat de `compareTo` (cohérence, antisymétrie) et cohérence recommandée avec `equals`. **Ne pas réembarquer** `Dossier`/`Ordonnable` de M3 dans le code (le pont est un objet de cours). Erreurs fréquentes : `compareTo` incohérent (renvoie un booléen-isme au lieu de négatif/zéro/positif), oublier que `List.sort` mute la liste, confondre tri naturel et `Comparator`. Domaine guidé : tri de `Livre`.
- **4-7 Lambdas et interfaces fonctionnelles** — une **interface fonctionnelle** = une seule méthode abstraite (`@FunctionalInterface`) ; **syntaxe lambda** `(params) -> corps` comme implémentation concise ; les 4 interfaces fonctionnelles standard : `Predicate<T>` (test booléen), `Function<T,R>` (transformation), `Consumer<T>` (action sans retour), `Supplier<T>` (production sans entrée) ; **références de méthode** (`String::length`, `System.out::println`) ; **capture** de variables (`effectively final`) et portée. Erreurs fréquentes : capturer une variable réassignée (« doit être `effectively final` »), écrire une boucle imbriquée dans une lambda illisible, confondre `Predicate` et `Function`. Domaine guidé : filtrer des nombres / transformer du texte.
- **4-8 Streams et `Optional`** — créer un stream depuis une collection (`stream()`) ; **opérations intermédiaires** `filter`/`map`/`sorted`/`distinct`/`limit` (paresseuses) ; **opérations terminales** `collect(toList())`/`count`/`reduce`/`forEach` ; **collecteurs** courants `Collectors.toList`/`joining`, **introduction** à `groupingBy`/`partitioningBy` ; **`Optional`** : présence/absence, `isPresent`, `orElse`, `map` — **éviter le `null`** et le `get()` nu ; `findFirst`/`max`/`min` renvoient un `Optional` ; **stream vs boucle** : lisibilité, et « le test n'observe que le résultat » (annonce du parti pris des exos, cf. §10.1). Erreurs fréquentes : réutiliser un stream déjà consommé, `optional.get()` sans vérifier la présence, croire qu'un stream modifie la collection source. Domaine guidé : pipeline sur une liste de mots + recherche `Optional`.

## 7. Vérification

- **Bloquant (CI #11a, job `build-docusaurus`)** : `cd courses && npm run build` doit passer. `onBrokenLinks: 'throw'` casse au moindre lien interne mort → les liens « Prochain chapitre », la retouche du 3-10 et les ancres doivent être exacts.
- **Validation locale** : build Docusaurus lancé en local avant push (`cd courses && npm run build`).
- **Compilation des extraits (recommandée pour M4)** : les chapitres ne passent pas par `valider-solutions`, mais Java 25 est installé localement et le module 4 contient beaucoup de **code exécutable et non trivial** (génériques, `Comparator` chaînés, pipelines de streams). Compiler à la main les extraits les plus denses (ch. 5, 6, 8) lève les doutes de syntaxe avant relecture. Précédent : l'exemple `record`/`sealed` du 3-10 avait été compilé en Java 25.
- **Relecture humaine** (charte §10) : structure respectée, niveau de langue adapté débutant, jargon expliqué (les anglicismes `stream`/`collect`/`mapper` annotés à la 1re occurrence), cohérence avec le référentiel.

## 8. Workflow d'exécution

- **1 seule PR** (découpage validé au brainstorm — choix formateur) : « #20 — chapitres module 4 (List/Set/Map/itération + génériques/Comparator/lambdas/streams) », branche `feature/m4-chapitres` issue de `main`, + retouche liaison `3-10-records-et-sealed` → `4-1` + création `_category_.json`.
- **Cadence** : rédaction des 8 chapitres, **commits par chapitre** pour garder l'historique lisible ; build Docusaurus à chaque étape ; **relecture globale** par le formateur en fin de PR (8 chapitres ~1200 mots = relecture conséquente, anticipée par le choix d'une PR unique).
- PR relue par un autre formateur → merge une fois le CI vert (+ build local).
- Clôture : mettre à jour [`backlog.md`](../../backlog.md) (#20, avancement du volet chapitres) avec lien commit.

## 9. Critères d'acceptation

- [ ] Les 8 chapitres existent et respectent la structure charte §6.
- [ ] `_category_.json` créé (position 5, cohérent avec la sidebar) ; liaison module 3 → module 4 corrigée dans `3-10-records-et-sealed` ; le 4-8 pointe vers « Module 5 *(à venir)* ».
- [ ] `npm run build` passe (Docusaurus, liens internes valides).
- [ ] Chaque chapitre a son « exercice guidé » (solution dans `<details>`) et ses « vérifiez vos acquis ».
- [ ] Garde-fou interne respecté : ordre des notions tenu (`equals`/`hashCode` au 4-2 avant les clés de `Map` au 4-3 ; génériques au 4-5 avant `Comparable<T>` au 4-6 ; lambdas au 4-7 avant streams au 4-8) ; aucune notion de module ultérieur (exceptions, IO, JUnit), aucune levée d'exception dans les exemples.
- [ ] Le **pont 3.4.3 → `Comparable<T>`** est présent au chapitre 4-6 (rappel intégré, pas de chapitre dédié).
- [ ] Aucun exercice guidé ne donne la solution d'un exo noté des futurs sous-groupes (mapping §5.4).

## 10. Annexe — décisions transverses module 4 (héritées par le volet exercices)

Tranchées au brainstorming du 2026-06-04 (et validées par le formateur), consignées ici pour les cycles d'exercices à venir (aucun livrable dans ce cycle). Les points marqués **[formateur]** ont été explicitement arbitrés ; les points marqués **[raffinement]** sont des ajustements apportés à la conception après le brainstorm pour la cohérence (à confirmer à la relecture de cette spec).

### 10.1 Streams imposés « par le design d'API quand possible » **[formateur]**
La note **automatique** n'observe **que la valeur de retour** (AssertJ), jamais la forme (pas de réflexion / parsing pour détecter `.stream()`). L'attendu « programmation fonctionnelle » est porté de trois façons : (a) **design d'API** — quand c'est naturel, la signature **prend une interface fonctionnelle en paramètre** (`Predicate<Soldat>`, `Function<…>`), ce qui rend la lambda **structurellement nécessaire** et **prouvé par le compilateur**, pas par réflexion (appliqué à 4.3.1 et 4.3.4) ; (b) le **critère formateur `idiomatisme`** (poids 2/20) sur les exos 4.3 ; (c) l'**annonce explicite** dans `sujet.md` (« la solution attendue passe par streams/lambdas ; une boucle correcte reste fonctionnelle mais moins idiomatique ») et la version stream **en référence** dans `correction.md` (boucle en variante). Pour l'**agrégation scalaire** (4.3.2, moyenne/max), aucun paramètre fonctionnel naturel → **libre choix** boucle/stream, idiomatisme noté à la main. Précédent : 3.4.4 (switch exhaustif noté via `idiomatisme`, pas par réflexion).

### 10.2 Test du contrat `equals`/`hashCode` — périmètre gradué **[formateur]**
Test par **comportement observable** (AssertJ, zéro réflexion), en deux paliers :
- **Public** (notions du ch.2) : égalité simple (`isEqualTo`/`isNotEqualTo`) ; cohérence `a.equals(b)` ⇒ même `hashCode` ; **comportement métier** — doublon écarté d'un `HashSet` (la taille reste 1), valeur retrouvée via une clé **égale mais non identique** d'une `HashMap`.
- **Privé** (robustesse) : réflexivité ; **symétrie** (les deux sens) ; `equals(null)` ⇒ `false` ; `equals(objet d'un autre type)` ⇒ `false` ; robustesse `HashSet` (2 égaux + 1 différent ⇒ taille 2, `contains(copieEgale)`).
- **Explicitement EXCLU** (à écrire noir sur blanc dans le sujet et la spec d'exo, garde-fou « pas de coins juridiques pour débutants ») : **transitivité** ; débat **`getClass()` vs `instanceof`** ; **héritage + `equals`** ; stabilité/distribution de `hashCode` ; l'**implication inverse** (deux `hashCode` égaux **n'impliquent pas** `equals` — collision légitime, ne jamais asserter « `hashCode` différents ⇒ non égaux »).

### 10.3 Type générique custom — squelette et test **[validé]**
- **Starter** : fournir **l'en-tête générique complet** (classe paramétrée + signatures publiques génériques), corps vides `return null/0/true; // TODO` (compile mais échoue — TDD), comme l'en-tête fourni de `Dossier`/`Ordonnable` en 3.4.3. Le **stockage interne est au choix** du stagiaire (recommandé : `private final List<T> …` — collections enfin autorisées) et **non observable** par les tests.
- **Test** : par l'API publique uniquement. **Généricité prouvée par le compilateur** : instancier le **même type avec deux paramètres** dans des tests séparés (`Pile<String>` et `Pile<Integer>`) **et** affecter le retour à une variable typée **sans cast** (`Integer sommet = pile.sommet();`) — ne compile que si `<T>` est correctement propagé. **Jamais** de réflexion (`getTypeParameters`) ni d'inspection du champ interne.
- **Cas limite** : `depiler()`/`sommet()` sur pile vide ⇒ **`null`** (pas d'exception — antériorité M5), cohérent avec `plusPrioritaire([])` ⇒ `null` de 3.4.3.

### 10.4 Modèle de domaine local au module 4 — `Grade` + `Soldat` **[raffinement]**
**Pas de réutilisation/import des classes M3** (package `etnc.m3` ≠ `etnc.m4`, aucun mécanisme de dépendance inter-modules, le ZIP standalone copie exo par exo). Le fil rouge militaire reste **narratif**. Modèle **fourni complet** (« FOURNI — ne pas modifier ») dans les starters des exos concernés, package `etnc.m4` :

```java
// Fourni dans 4.1.4, 4.2.*, 4.3.*
public enum Grade { SOLDAT, CAPORAL, SERGENT, ADJUDANT, LIEUTENANT }
// ordre de déclaration = hiérarchie croissante ; un enum est naturellement Comparable
// (ordre naturel = ordre de déclaration) — utile pour le tri et le groupement.

// Fourni dans 4.2.* et 4.3.*
public record Soldat(String nom, Grade grade, int anciennete) { }
// grade  -> tri / groupement (4.2.3, 4.2.4, 4.3.1, 4.3.3, 4.3.4)
// anciennete (années de service, >= 0) -> agrégation numérique (4.3.2)
```

**Raffinement vs brainstorm** : le roster initial proposait `Soldat(String nom, int rangGrade)`. On retient `enum Grade` (réemploi du concept de 3.4.2, plus idiomatique, `Comparable` gratuit pour le tri/groupement) **et** un champ numérique `anciennete` distinct, qui donne à l'agrégation 4.3.2 un support numérique naturel (moyenne/max d'ancienneté) sans dépendre d'`ordinal()`. En 4.2.3, la variante fournie est `record Soldat(String nom, Grade grade, int anciennete) implements Comparable<Soldat>` avec `compareTo` **en TODO** (l'objet de l'exo).

### 10.5 Cas limites sans exception (antériorité M5) **[validé]**
Cohérence stricte : `Pile`/`FileMission` (vide) ⇒ `null` ; `Annuaire.rechercher(absent)` ⇒ `"Inconnu"` (`getOrDefault`) ; `supprimer(absent)` ⇒ `false` ; `membres(grade jamais affecté)` ⇒ liste vide (pas `null`) ; `Statistiques.rangMoyen([])` ⇒ `0.0` (sentinelle, validé formateur), `anciennete max([])` ⇒ `0` ; tout `Optional` ⇒ `Optional.empty()`.

### 10.6 Roster des 12 exercices (3 sous-groupes) et vérification d'antériorité
Critère formateur par exo : `idiomatisme` par défaut ; `demarche` pour 4.1.3, 4.1.4, 4.3.2. Barème `evaluation.yml` calqué M3 : tests-publics 8 + tests-prives 8 + `style` 2 (automatique Checkstyle/PMD, `id: style`) + 1 critère formateur poids 2 → total 20, seuil 12.

**4.1 Collections** (→ ch.1-4) — *gabarit du sous-groupe : 4.1.1.*
- **4.1.1 `annuaire-militaire`** — `Map<String,String>` (`HashMap`) indicatif→nom. `Annuaire { void enregistrer(String,String); String rechercher(String) /* "Inconnu" via getOrDefault */; boolean supprimer(String); int taille(); List<String> indicatifsTries() }`. Test : valeurs de retour ; tri alphabétique via `containsExactly`. Critère `idiomatisme`.
- **4.1.2 `utilisateurs-uniques`** **[raffinement]** — exo **`equals`/`hashCode` à la main**. `class Utilisateur` (champs `String nom, int matricule` ; `equals`/`hashCode` **en TODO**) + `RegistreUtilisateurs { boolean ajouter(Utilisateur) /* false si présent */; boolean contient(Utilisateur); int nombreDistincts(); Set<Utilisateur> tous() }` (dédoublonnage par `HashSet`). Test : contrat gradué §10.2. Critère `idiomatisme`. *Raffinement vs brainstorm : la version initiale dédoublonnait des `String` (donc `equals` JDK, sans rien écrire) ; on passe à une classe `Utilisateur` custom pour réellement faire écrire `equals`/`hashCode` — ce que le ch.2 et le kickoff §4 promettent.*
- **4.1.3 `file-de-mission`** — `Deque<String>` (`ArrayDeque`) en FIFO. `FileMission { void ajouter(String); String traiterProchaine() /* pollFirst, null si vide */; String prochaine() /* peekFirst */; boolean estVide(); int taille() }`. Test : ordre FIFO. Critère `demarche`.
- **4.1.4 `groupement-par-grade`** — `Map<Grade,List<String>>` **à la main** (`computeIfAbsent`), pas de `groupingBy`. Starter fournit `enum Grade`. `GroupesParGrade { void affecter(Grade,String); List<String> membres(Grade) /* getOrDefault liste vide */; Set<Grade> grades(); int effectif(Grade) }`. Critère `demarche`.

**4.2 Génériques et tri** (→ ch.5-6) — *gabarit : 4.2.1.*
- **4.2.1 `pile-generique`** — `class Pile<T> { void empiler(T); T depiler() /* LIFO, null si vide */; T sommet(); boolean estVide(); int taille() }`. Test multi-types (`Pile<String>` + `Pile<Integer>`) + affectation sans cast (§10.3). Critère `idiomatisme`.
- **4.2.2 `paire-generique`** — `final class Paire<A,B> { Paire(A,B); A premier(); B second(); Paire<B,A> inverser(); static <X,Y> Paire<X,Y> de(X,Y) }`. Test multi-types + immuabilité. Critère `idiomatisme`.
- **4.2.3 `tri-soldats`** — **le pont** : `record Soldat(...) implements Comparable<Soldat>` avec `compareTo` en TODO (par `grade` puis `nom`) ; util `static void trier(List<Soldat>)` → `sort(null)`. Test : ordre après tri + signe de `compareTo`. Critère `idiomatisme`.
- **4.2.4 `comparator-chaine`** — `TriSoldats { static List<Soldat> parNom(...); static List<Soldat> parGradePuisNom(...) /* comparing(Soldat::grade).thenComparing(Soldat::nom) */; static List<Soldat> parGradeDecroissant(...) /* .reversed() */ }`, renvoient une **nouvelle** liste (source non mutée). Critère `idiomatisme`.

**4.3 Lambdas et streams** (→ ch.7-8) — *gabarit : 4.3.1.*
- **4.3.1 `filtrage-projection`** — `Effectifs { static List<String> nomsDesGradesAuMoins(List<Soldat>, Grade min); static List<Soldat> filtrer(List<Soldat>, Predicate<Soldat> critere) /* lambda en paramètre = streams imposés par design */ }`. Test : liste résultat. Critère `idiomatisme`.
- **4.3.2 `agregation`** — `Statistiques { static int total(List<Soldat>); static double ancienneteMoyenne(List<Soldat>) /* average().orElse(0.0) */; static int ancienneteMax(List<Soldat>) /* 0 si vide */ }`. Libre boucle/stream. Test : tolérance flottante sur la moyenne ; liste vide ⇒ `0.0`/`0`. Critère `demarche`.
- **4.3.3 `groupement-stream`** — `Regroupement { static Map<Grade,List<Soldat>> parGrade(...) /* groupingBy */; static Map<Boolean,List<Soldat>> selonAnciennete(List<Soldat>, int seuil) /* partitioningBy */; static Map<Grade,Long> effectifsParGrade(...) /* groupingBy + counting */ }`. Miroir stream de 4.1.4. Test : `partitioningBy` garantit **toujours** les deux clés. Critère `idiomatisme`.
- **4.3.4 `recherche-optional`** — `Recherche { static Optional<Soldat> premier(List<Soldat>, Predicate<Soldat>); static Optional<Soldat> plusHautGrade(List<Soldat>) /* max(comparing(Soldat::grade)) */; static String nomOuParDefaut(List<Soldat>, Predicate<Soldat>) /* premier(...).map(Soldat::nom).orElse("Aucun") */ }`. Test : `OptionalAssert` (`isPresent`/`contains`/`isEmpty`), jamais la façon de l'obtenir. Critère `idiomatisme`.

**Vérification d'antériorité** : mapping 4.1→ch.1-4, 4.2→ch.5-6, 4.3→ch.7-8 respecté exo par exo. Points sécurisés : 4.1.4 construit sa `Map` à la main (ni `groupingBy` ni `Soldat` — grade en clé `enum`, noms en `String`) ; les clés de `Map`/`Set` en 4.1 sont des types à `equals`/`hashCode` connus au ch.2 (`String`, `enum`, ou `Utilisateur` dont c'est justement l'objet) ; `Comparable<Soldat>` (4.2.3) exige le ch.6 (OK) ; `Optional` n'apparaît qu'en 4.3 (ch.8, OK). **Aucun risque bloquant.**

### 10.7 Projet binôme #2
**Non débloqué par le module 4** : il requiert le **module 5** (exceptions + persistance fichier). Sa fiche sera rédigée dans un cycle dédié après le module 5. #20 ne le touche pas.
