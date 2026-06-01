# Contribuer à la Piscine ETNC

> Guide destiné aux **formateurs** de l'École des Transmissions, du Numérique et du Cyber qui ajoutent ou modifient du contenu (exercices, chapitres, outillage).
> Les stagiaires n'ont pas à lire ce document : ils suivent [`docs/piscine-stagiaire.md`](docs/piscine-stagiaire.md).

## 1. Principes

- **Pédagogie d'abord.** Un ajout n'est utile que s'il est clair pour un stagiaire débutant. Le format et le ton ne sont pas négociables (voir les documents de cadrage en §4).
- **Rien dans `main` sans revue.** Tout passe par une *pull request* relue par un autre formateur et un CI vert.
- **Antériorité des notions.** On n'utilise jamais une notion avant le chapitre/exercice qui l'introduit (cohérence avec [`docs/referentiel.md`](docs/referentiel.md)).
- **La moulinette valide la solution de référence.** Un exercice dont la `solution/` ne passe pas ses propres tests est cassé *avant* d'arriver au stagiaire.

## 2. Workflow Git

1. **Brancher depuis `main`** à jour :
   ```bash
   git checkout main && git pull --ff-only
   git checkout -b feature/<slug>
   ```
   Convention de nommage : `feature/<slug>` (contenu), `fix/<slug>` (correctif), `chore/<slug>` (outillage/CI). `<slug>` en `kebab-case`, court et explicite.
2. **Commits** au format *Conventional Commits* français, comme l'historique du dépôt :
   - `feat(exo): 1.3.1 fizzbuzz`
   - `docs(backlog): #16 exercices module 1 faite`
   - `ci: monter Node 20 -> 22`
3. **Pousser** et **ouvrir une PR** vers `main`.
4. **Faire relire par un autre formateur.** Au moins **une** approbation est requise.
5. **Attendre le CI vert** (voir §3) puis **merger** (merge commit ; la branche est supprimée après).
6. **Mettre à jour** [`docs/backlog.md`](docs/backlog.md) : statut de la tâche + référence à la PR/au commit qui la clôt.

## 3. Le CI doit être vert

La PR ne se merge pas tant que les jobs de [`.github/workflows/ci.yml`](.github/workflows/ci.yml) ne passent pas :

- **Moulinette (build + tests)** — `mvn verify` du reactor + suites taguées (`git`, `tools`, `e2e`).
- **Lint exercices** — présence des fichiers obligatoires + cohérence YAML (`scripts/lint-exercices.sh`).
- **Build site Docusaurus** — `npm run build` ; un lien cassé casse le build (`onBrokenLinks: throw`).
- **Valider les solutions de référence** — chaque `solution/` doit passer **tous** ses tests (publics + privés).

## 4. Checklist — ajouter un **exercice**

Référence complète : [`docs/format-exercice.md`](docs/format-exercice.md).

- [ ] Dossier `exercises/module-N-.../M.S.E-slug/` au nommage conforme.
- [ ] Les fichiers obligatoires sont présents : `sujet.md`, `metadata.yml`, `starter/`, `tests/`, `solution/`, `correction.md`, `evaluation.yml` (`tests-prives/` facultatif pour les exercices les plus simples du module 1).
- [ ] `sujet.md` suit la structure imposée (contexte, énoncé, exemple, contraintes, ce qui sera vérifié) et respecte la [charte de rédaction](docs/charte-redaction.md).
- [ ] `metadata.yml` complet et valide (champs obligatoires renseignés).
- [ ] `evaluation.yml` : somme des `poids` = `total` ; tout critère `type: formateur` porte un `id` couvert par [`docs/grille-evaluation.md`](docs/grille-evaluation.md) (`demarche`, `lisibilite`, `idiomatisme`, `respect-consignes`).
- [ ] La `solution/` passe **tous** ses tests (publics + privés) : `./mvnw -f solution/pom.xml test`.
- [ ] Aucune notion utilisée n'est introduite plus tard que son chapitre/exercice d'origine.
- [ ] L'exercice est référencé dans [`docs/referentiel.md`](docs/referentiel.md).

## 5. Checklist — ajouter un **chapitre**

Référence complète : [`docs/charte-redaction.md`](docs/charte-redaction.md) (§6 pour la structure imposée).

- [ ] Fichier dans `courses/docs/module-N-.../`, nommé `N-S-titre.md`.
- [ ] Structure de chapitre respectée : *Pourquoi ce chapitre*, *Ce que vous saurez faire*, notions numérotées, *Erreurs fréquentes*, *Exercice guidé*, *Vérifiez vos acquis*, *Pour aller plus loin*, *Prochain chapitre*.
- [ ] Ton et niveau de langue conformes : vouvoiement, phrases courtes, jargon expliqué à la première occurrence, pas d'argot ni d'humour.
- [ ] Conventions de code des exemples respectées (§7 charte : lisibilité avant astuce, nommage, commentaires utiles).
- [ ] `cd courses && npm run build` passe (aucun lien cassé).
- [ ] Antériorité des notions respectée vis-à-vis du [`référentiel`](docs/referentiel.md).

## 6. Critères de blocage à la revue

Un relecteur **bloque** la PR tant que l'un de ces points n'est pas levé :

- **Tests rouges** ou CI non vert.
- **Solution de référence** qui ne passe pas ses propres tests.
- **Niveau de langue** non conforme (tutoiement, argot, humour, allusions culturelles).
- **Notion en avance** sur sa place dans le référentiel.
- **Format non respecté** (structure de `sujet.md` / de chapitre, fichiers manquants).
- **Code d'exemple non idiomatique ou illisible** (voir [`docs/grille-evaluation.md`](docs/grille-evaluation.md)).
- **Absence de mise à jour** du backlog ou du référentiel quand elle est attendue.

Les remarques de moindre importance (formulation, détail de style) sont des *suggestions* et ne bloquent pas le merge.

---

*Projet institutionnel ETNC. En cas de doute sur le périmètre ou la diffusion d'une contribution, se référer à la hiérarchie / au RSSI avant de pousser.*
