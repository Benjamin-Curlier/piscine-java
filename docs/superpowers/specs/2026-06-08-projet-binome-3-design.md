# Spec — #25 Fiche du Projet binôme #3 (final) — Mini-moulinette pédagogique

> Design du 2026-06-08. **Projet final** de la Piscine, débloqué par le module 6 (mergé).
> Cadre : [`referentiel.md`](../../referentiel.md) §5 (Projet 3), gabarit des fiches [`projet-1-mini-domaine/`](../../../exercises/projets-binome/projet-1-mini-domaine/) et [`projet-2-persistance/`](../../../exercises/projets-binome/projet-2-persistance/), specs sœurs `…-projet-binome-1/2-design.md`.
> Livrable : 4 fichiers dans `exercises/projets-binome/projet-3-mini-moulinette/` (`sujet.md`, `consignes-livraison.md`, `evaluation.yml`, `metadata.yml`). **Pas de code** (les binômes conçoivent de A à Z ; la moulinette n'exécute pas ces projets — évaluation formateur).

## 1. Objectif et thème

Référentiel §5 : « Application complète : OO + persistance + tests + Git collaboratif. **Mini-moulinette pédagogique** — checker simple qui compile un fichier Java, l'exécute sur des cas, génère un rapport explicatif. Le binôme doit produire **≥ 70 % de couverture JUnit** et utiliser des **branches + PR**. »

**Méta-dimension assumée (forte clôture du parcours)** : le binôme construit une **version réduite de l'outil qui l'a noté** depuis le module 1. Synthèse de tout : OO (M3), collections/streams (M4), exceptions + I/O + CSV (M5), **tests JUnit + Git collaboratif (M6)**.

## 2. Périmètre **volontairement modeste** (tenir ~14-16 h)

Pour rester tractable, on **fixe le contrat du code à vérifier** : la mini-moulinette ne gère pas du Java arbitraire, mais une **classe à signature imposée**.

- **Code sous test** : un unique fichier `Operation.java` contenant `public class Operation { public int appliquer(int n) { … } }`. La mini-moulinette compile **ce** fichier et appelle `appliquer` sur des entiers.
- **Pipeline imposé (3 étapes)** :
  1. **Compiler** le fichier source (`javac` via `ProcessBuilder`, **ou** l'API `javax.tools.JavaCompiler`). Échec de compilation → le rapport dit « ne compile pas » avec le message d'erreur ; on s'arrête proprement.
  2. **Exécuter** : charger/lancer la classe et appeler `appliquer(n)` pour chaque cas (entrée → attendu). Une exception sur un cas → ce cas est marqué « erreur », on **continue** les autres.
  3. **Rapport** : générer un fichier **Markdown** récapitulatif — une ligne/section par cas (entrée, attendu, obtenu, ✓/✗/erreur) + une synthèse (`N/total réussis`).
- **Cas de test** : lus depuis un fichier **`cas.csv`** au format `entree,attendu` (CSV **à la main**, acquis M5). Fichier absent / ligne corrompue : gérés proprement (exception rattrapée, ligne signalée puis ignorée).
- **Exigences qualité imposées** :
  - **Tests JUnit du binôme** sur leur propre moulinette (parsing CSV, comparaison, génération de rapport au minimum), **≥ 70 % de couverture** mesurée par un outil de couverture (ex. **JaCoCo**) et le **chiffre reporté dans le README**.
  - **Workflow Git** : développement sur **branches de feature** + **au moins une Pull Request** (revue par l'autre binôme, ou auto-revue documentée). Historique équilibré entre les deux membres.

## 3. Décisions de conception (imposées dans la fiche)
- **Contrat fixe `Operation.appliquer(int):int`** [raffinement] : évite d'avoir à parser/introspecter du Java arbitraire (hors de portée). Le binôme peut **étendre** en bonus (autre signature, plusieurs méthodes).
- **Compilation** : `ProcessBuilder("javac", …)` (simple, déjà vu en esprit au module 6) **ou** `javax.tools.JavaCompiler` (plus avancé) — **au choix du binôme**, documenté.
- **Exécution** : reflection (`Class.forName` + `Method.invoke`) sur le `.class` compilé, **ou** un sous-process `java`. Au choix, documenté. *(Sécurité/sandbox hors périmètre — code de confiance en contexte pédagogique.)*
- **Couverture ≥ 70 %** : mesurée par le binôme (JaCoCo recommandé), **chiffre dans le README** ; le formateur vérifie. Pas d'intégration CI de la couverture (le projet binôme n'est pas branché sur la moulinette).
- **Antériorité** : modules **1 à 6** — tout est autorisé, **y compris** JUnit comme livrable et le workflow Git par branches/PR (c'est le **premier** projet où ils sont **exigés**).
- **Bonus valorisés** : plusieurs fichiers à vérifier, signature paramétrable, rapport HTML, mesure de temps, cas en `@CsvSource`, anti-régression.

## 4. Livrables (4 fichiers, gabarit binôme #1/#2)
- **`sujet.md`** : contexte (méta : « construisez une mini-moulinette »), objectifs pédagogiques, **pipeline imposé** (compiler → exécuter → rapport), contrat `Operation`, format `cas.csv`, robustesse (compile KO, fichier absent, ligne corrompue, exception sur un cas), **exigences tests ≥70 % + Git branches/PR**, démonstration attendue (lancer la moulinette sur un `Operation.java` correct **et** un buggé, lire le rapport), bonus.
- **`consignes-livraison.md`** : binôme imposé, ~16 h ; **dépôt Git avec branches + ≥1 PR** (nouveauté vs #1/#2) ; README (compiler/lancer, emplacement `cas.csv`, **taux de couverture**, répartition) ; arborescence (pas de `.class`/build versionnés, `.gitignore`).
- **`evaluation.yml`** : /20, seuil 12, **critères formateur** (binôme = évaluation humaine). Répartition proposée : `conception-oo` 4, `pipeline-fonctionnel` 4 (compile/exécute/rapport), `robustesse` 3 (erreurs gérées), **`tests-couverture` 4** (suite JUnit pertinente + ≥70 %), **`git-collaboration` 3** (branches + PR + historique équilibré), `lisibilite` 2. = 20.
- **`metadata.yml`** : `binome: true`, `module: 6`, `duree_estimee_h: 16`, prérequis `6.1`/`6.2` (+ rappels 3.x/5.x), objectifs, notions (moulinette, compilation, reflection, junit, couverture, git, pull-request, csv), `date_creation: 2026-06-08`.

## 5. Cohérence / garde-fous
- **Scope discipline** : insister dans le sujet sur le **contrat fixe** `Operation.appliquer` pour éviter que le binôme se perde à parser du Java arbitraire. Le « modeste mais exigeant sur la qualité » du référentiel = périmètre étroit, barre haute sur tests/Git.
- **Anti-spoil** : ne pas donner le code de la moulinette ETNC ; le binôme réinvente une version simple.
- **Lint** : un projet binôme n'a pas `starter/solution/tests` figés → le lint ignore les dossiers binôme (vérifier : `is_exo_dir` filtre sur `M.S.E-slug` ; `projet-3-mini-moulinette` n'est pas au format M.S.E donc **ignoré** par le lint, comme #1/#2). Pas d'impact CI.

## 6. Workflow
- Branche `feature/projet-binome-3` (créée). 1 PR (les 4 fichiers de la fiche).
- Pas de gate technique (pas de code) ; relecture cohérence pédagogique + lint (les exos restent à 65, fiche ignorée). CI : Docusaurus/moulinette/lint inchangés, `valider-solutions` ne touche pas les binômes.
- Clôture : `backlog.md` (#25 fait → **Phase 3 terminée** : modules 2-6 + 3 projets binôme). MAJ `referentiel.md` §5 (« Projet 3 à venir » → disponible).

## 7. Critères d'acceptation
- [ ] 4 fichiers conformes au gabarit binôme (#1/#2).
- [ ] Sujet : pipeline imposé (compiler→exécuter→rapport), contrat `Operation` fixe, `cas.csv`, robustesse, **exigences tests ≥70 % + Git branches/PR**, démonstration (Operation correct + buggé).
- [ ] `evaluation.yml` /20 seuil 12, critères couvrant conception/pipeline/robustesse/**tests-couverture**/**git-collaboration**/lisibilité.
- [ ] `metadata.yml` `binome: true`, module 6, prérequis 6.x.
- [ ] Antériorité modules 1-6 ; tests JUnit + Git **exigés** (premier projet).
- [ ] `referentiel.md` §5 mis à jour (Projet 3 disponible) ; `backlog.md` #25 fait (Phase 3 terminée).
