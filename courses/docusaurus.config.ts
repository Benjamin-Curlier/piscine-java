import {themes as prismThemes} from 'prism-react-renderer';
import type {Config} from '@docusaurus/types';
import type * as Preset from '@docusaurus/preset-classic';

// This runs in Node.js - Don't use client-side code here (browser APIs, JSX...)

const config: Config = {
  title: 'Piscine Java',
  tagline: 'Apprendre Java en autonomie, du premier programme aux tests',
  favicon: 'img/favicon.ico',

  future: {
    v4: true,
  },

  // URL de production à ajuster lors du déploiement
  url: 'https://piscine-java.example.com',
  baseUrl: '/',

  // Configuration GitHub Pages — utile pour la phase de dev/preview
  organizationName: 'Benjamin-Curlier',
  projectName: 'piscine-java',

  onBrokenLinks: 'throw',

  markdown: {
    hooks: {
      onBrokenMarkdownLinks: 'warn',
    },
  },

  i18n: {
    defaultLocale: 'fr',
    locales: ['fr'],
  },

  presets: [
    [
      'classic',
      {
        docs: {
          sidebarPath: './sidebars.ts',
          editUrl:
            'https://github.com/Benjamin-Curlier/piscine-java/tree/main/courses/',
          showLastUpdateAuthor: true,
          showLastUpdateTime: true,
        },
        // Blog désactivé pour le moment — réactiver pour des annonces de promo si besoin
        blog: false,
        theme: {
          customCss: './src/css/custom.css',
        },
      } satisfies Preset.Options,
    ],
  ],

  themeConfig: {
    image: 'img/docusaurus-social-card.jpg',
    colorMode: {
      respectPrefersColorScheme: true,
    },
    navbar: {
      title: 'Piscine Java',
      logo: {
        alt: 'Logo Piscine Java',
        src: 'img/logo.svg',
      },
      items: [
        {
          type: 'docSidebar',
          sidebarId: 'coursSidebar',
          position: 'left',
          label: 'Cours',
        },
        {
          href: 'https://github.com/Benjamin-Curlier/piscine-java',
          label: 'GitHub',
          position: 'right',
        },
      ],
    },
    footer: {
      style: 'dark',
      links: [
        {
          title: 'Cours',
          items: [
            {
              label: 'Introduction',
              to: '/docs/intro',
            },
          ],
        },
        {
          title: 'Projet',
          items: [
            {
              label: 'Référentiel pédagogique',
              href: 'https://github.com/Benjamin-Curlier/piscine-java/blob/main/docs/referentiel.md',
            },
            {
              label: 'Format d\'exercice',
              href: 'https://github.com/Benjamin-Curlier/piscine-java/blob/main/docs/format-exercice.md',
            },
            {
              label: 'Charte de rédaction',
              href: 'https://github.com/Benjamin-Curlier/piscine-java/blob/main/docs/charte-redaction.md',
            },
          ],
        },
        {
          title: 'Liens',
          items: [
            {
              label: 'GitHub',
              href: 'https://github.com/Benjamin-Curlier/piscine-java',
            },
          ],
        },
      ],
      copyright: `Piscine Java © ${new Date().getFullYear()} — Piscine Java. Projet à vocation pédagogique.`,
    },
    prism: {
      theme: prismThemes.github,
      darkTheme: prismThemes.dracula,
      additionalLanguages: ['java', 'bash', 'yaml', 'properties'],
    },
  } satisfies Preset.ThemeConfig,
};

export default config;
