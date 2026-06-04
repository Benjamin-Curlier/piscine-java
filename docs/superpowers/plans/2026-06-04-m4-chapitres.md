# Module 4 — Chapitres (Collections, génériques, lambdas) — Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Produire les 8 chapitres du module 4 dans `courses/docs/module-4-collections-generiques-lambdas/`, conformes à la charte §6, build Docusaurus vert, en 1 PR.

**Architecture:** 8 fichiers Markdown (1 par chapitre) + `_category_.json` + retouche de liaison du 3-10. Contenu cadré par la spec [`2026-06-04-m4-chapitres-design.md`](../specs/2026-06-04-m4-chapitres-design.md) §6 (plan de contenu) et §5 (anti-spoil + antériorité interne). Exécution ultracode : 6 agents Sonnet rédacteurs (groupés), puis assemblage/vérif en boucle principale.

**Tech Stack:** Docusaurus (Markdown + frontmatter), Node 22 (`npm run build`, `onBrokenLinks: 'throw'`), Java 25 local (compilation ponctuelle des extraits denses).

---

## Découpage en unités de rédaction (mapping vers les 6 agents)

| Agent | Chapitres | Densité |
|---|---|---|
| A | `4-1` List, `4-3` Map | concret, moyen |
| B | `4-2` Set + `equals`/`hashCode` | dense (contrat) |
| C | `4-4` itération, `4-7` lambdas | moyen ×2 |
| D | `4-5` génériques | dense |
| E | `4-6` Comparable/Comparator (+ pont 3.4.3) | dense |
| F | `4-8` streams + `Optional` | le plus dense |

Chaque agent reçoit : la spec §6 (contenu de SON/SES chapitre(s)), la structure charte §6 (§3 de la spec), les conventions §4, l'anti-spoil §5.4 (domaine neutre imposé), un chapitre M3 existant comme modèle de style. Il **écrit le(s) fichier(s)** et ne touche à rien d'autre.

---

### Task 1 (Agents A–F en parallèle) : rédiger les 8 chapitres

**Files (Create):**
- `courses/docs/module-4-collections-generiques-lambdas/4-1-list-arraylist.md`
- `courses/docs/module-4-collections-generiques-lambdas/4-2-set-hashset-equals-hashcode.md`
- `courses/docs/module-4-collections-generiques-lambdas/4-3-map-hashmap.md`
- `courses/docs/module-4-collections-generiques-lambdas/4-4-iteration-moderne.md`
- `courses/docs/module-4-collections-generiques-lambdas/4-5-generiques.md`
- `courses/docs/module-4-collections-generiques-lambdas/4-6-comparable-comparator.md`
- `courses/docs/module-4-collections-generiques-lambdas/4-7-lambdas-interfaces-fonctionnelles.md`
- `courses/docs/module-4-collections-generiques-lambdas/4-8-streams-optional.md`

- [ ] **Step 1 : Chaque fichier respecte l'ordre charte §6**

Frontmatter `id`/`sidebar_position`/`title`/`description`, puis `# Titre`, `## Pourquoi ce chapitre`, `## Ce que vous saurez faire à la fin`, sections `## 1.`/`## 2.` (notion + `### Exemple` + `### À retenir`), `## Erreurs fréquentes` (symptôme→cause→correction), `## Exercice guidé` (`<details>`), `## Vérifiez vos acquis`, `## Pour aller plus loin` (1-3 liens dev.java / Baeldung / Javadoc 25), `## Prochain chapitre`.

- [ ] **Step 2 : Contenu = spec §6 du chapitre concerné** (notions-clés, erreurs fréquentes listées, domaine de l'exemple guidé **imposé** par §5.4).

- [ ] **Step 3 : Garde-fous**
  - Antériorité interne (§5.2) : un chapitre n'emploie que les notions des chapitres précédents. `equals`/`hashCode` au 4-2 ; lambda formalisée au 4-7 (au 4-3/4-4 = idiome concret seulement) ; streams en dernier (4-8).
  - Anti-spoil (§5.4) : l'exercice guidé n'utilise PAS le domaine d'un exo noté (cf. table §5.4).
  - Aucune **levée d'exception** dans les exemples (antériorité M5) ; imports explicites (jamais `java.util.*`).
  - Liens internes : `## Prochain chapitre` pointe vers le slug suivant (4-1→4-2→…→4-7→4-8) ; 4-8 → « Module 5 *(à venir)* » (pas de lien mort).

- [ ] **Step 4 : Le chapitre 4-6 contient le pont** (rappel `Ordonnable` 3.4.3 → `java.lang.Comparable<T>`, deux signatures côte à côte) — sans réembarquer le code M3.

- [ ] **Step 5 : Longueur** 800–2000 mots ; 4-8 vers le haut de la fourchette.

### Task 2 (boucle principale) : config + liaison

**Files:**
- Create: `courses/docs/module-4-collections-generiques-lambdas/_category_.json`
- Modify: `courses/docs/module-3-poo/3-10-records-et-sealed.md` (bloc « Prochain chapitre »)

- [ ] **Step 1 : `_category_.json`**

```json
{
  "label": "Module 4 — Collections, génériques, lambdas",
  "position": 5,
  "collapsible": true,
  "collapsed": false,
  "link": { "type": "generated-index" }
}
```

- [ ] **Step 2 : Vérifier la `position`** vs la sidebar réelle (M3 = 4). Lister les `_category_.json` existants et leurs `position` ; ajuster si 5 entre en collision.

- [ ] **Step 3 : Retoucher le 3-10** : son « Prochain chapitre » doit pointer vers `4-1-list-arraylist` (et non « Module 4 *(à venir)* »). Vérifier le texte exact actuel avant édition.

### Task 3 (boucle principale) : vérification

- [ ] **Step 1 : Build Docusaurus**

Run: `cd courses && npm run build`
Expected: build OK, **0 broken link** (`onBrokenLinks: 'throw'`).

- [ ] **Step 2 : Corriger tout lien interne mort** (slugs « Prochain chapitre », ancres, liaison 3-10) jusqu'au build vert.

- [ ] **Step 3 : Compilation ponctuelle des extraits denses** (recommandé, §7) — extraire les exemples des ch. 4-5 (génériques), 4-6 (`Comparator` chaîné), 4-8 (streams/`Optional`) dans un fichier scratch et `javac` en Java 25 local pour lever les doutes de syntaxe. Hors scratch, ne rien committer.

- [ ] **Step 4 : Relecture humaine** (charte §10) : structure §6, niveau débutant, jargon expliqué (anglicismes `stream`/`collect`/`mapper` annotés), cohérence référentiel, anti-spoil §5.4 respecté.

### Task 4 (boucle principale) : commits + clôture

- [ ] **Step 1 : Commits par chapitre** (historique lisible) — la spec + le plan sont committés en tête de PR.

```bash
git add docs/superpowers/specs/2026-06-04-m4-chapitres-design.md docs/superpowers/plans/2026-06-04-m4-chapitres.md
git commit -m "docs(#20): spec + plan des chapitres du module 4"
git add courses/docs/module-4-collections-generiques-lambdas/ courses/docs/module-3-poo/3-10-records-et-sealed.md
git commit -m "docs(#20): 8 chapitres du module 4 (collections, génériques, lambdas)"
```

- [ ] **Step 2 : MAJ backlog** : `docs/backlog.md` #20 — avancement du volet chapitres (lien commit).

- [ ] **Step 3 : Push + PR** (quand le formateur valide) : branche `feature/m4-chapitres`, PR « #20 — chapitres module 4 », attendre CI 4 jobs verts + relecture, puis merge.

---

## Critères d'acceptation (rappel spec §9)

- [ ] 8 chapitres conformes charte §6, exercice guidé + vérifiez-vos-acquis dans chacun.
- [ ] `_category_.json` créé (position cohérente) ; liaison 3-10 → 4-1 ; 4-8 → Module 5 *(à venir)*.
- [ ] `npm run build` vert (0 lien mort).
- [ ] Antériorité interne tenue ; aucune notion de module ultérieur ; aucune exception levée.
- [ ] Pont 3.4.3 → `Comparable<T>` présent au 4-6.
- [ ] Aucun exercice guidé ne spoile un exo noté (§5.4).

## Self-review (couverture spec)

- Spec §2 (8 fichiers + config + liaison) → Tasks 1-2. ✓
- Spec §3 (structure) + §6 (contenu) → Task 1. ✓
- Spec §5 (antériorité + anti-spoil) → Task 1 Step 3-4. ✓
- Spec §7 (vérification : build + compilation extraits) → Task 3. ✓
- Spec §8 (1 PR, commits par chapitre, backlog) → Task 4. ✓
- Spec §10 (décisions transverses exos) → hors périmètre de ce plan (cycles exos). ✓
