# Site de cours — Piscine Java

Site [Docusaurus 3](https://docusaurus.io/) hébergeant l'ensemble des cours de la Piscine Java.

## Prérequis

- **Node.js 18+** (testé en 24).
- **npm 9+**.

## Démarrage local

```bash
cd courses
npm install
npm start
```

Le site s'ouvre sur `http://localhost:3000/` avec rechargement à chaud.

## Build de production

```bash
npm run build
```

Les fichiers statiques sont générés dans `courses/build/` (non versionné).

Pour prévisualiser le build :

```bash
npm run serve
```

## Structure

```
courses/
├── docs/                              # contenu pédagogique (versionné)
│   ├── intro.md                       # page d'accueil
│   ├── module-1-fondamentaux/
│   │   ├── _category_.json            # libellé et ordre du module dans la sidebar
│   │   ├── 1-1-installer-java.md      # chapitre
│   │   └── ...
│   ├── module-2-tableaux-chaines-methodes/
│   ├── module-3-poo/
│   ├── module-4-collections-generiques-lambdas/
│   ├── module-5-exceptions-io/
│   └── module-6-tests-git/
├── src/                               # composants React custom (homepage, etc.)
├── static/                            # ressources statiques (images, etc.)
├── docusaurus.config.ts               # config principale
└── sidebars.ts                        # config sidebar (auto-générée depuis docs/)
```

## Conventions de rédaction

Tout chapitre doit suivre la structure et le ton définis dans la
[**charte de rédaction**](../docs/charte-redaction.md) (section 6 pour les chapitres de cours).

## Déploiement

- **Phase de développement** : prévu sur GitHub Pages depuis le repo `Benjamin-Curlier/piscine-java` (à configurer).
- **Production** : GitLab CE on-premise (à configurer côté infrastructure interne).
