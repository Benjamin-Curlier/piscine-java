# Charte de rédaction — Piscine Java

> Règles communes pour rédiger **cours, sujets d'exercices, corrections et messages de la moulinette**.
> Toute contribution écrite au projet doit respecter cette charte.

## 1. Public cible

**Développeurs débutants francophones en cours d'onboarding.** Profils variés (reconversion, jeunes diplômés, autodidactes), tous sont volontaires pour se former au développement. Hypothèses de travail :

- **Bagage technique** : à peu près aucun. Bureautique courante OK. Ligne de commande : non.
- **Bagage scolaire** : hétérogène (Bac à Bac+5). Pas tous à l'aise avec les maths.
- **Motivation** : forte (formation choisie). Mais frustration possible face à l'abstrait.
- **Contexte** : ils utiliseront Java en entreprise (logiciels métier, outils internes, parfois embarqué).

**Conséquence directe** : on écrit pour quelqu'un qui ne sait rien, mais qui n'est ni bête ni paresseux. Pas de condescendance, pas de raccourcis non explicités.

## 2. Ton

| À faire | À éviter |
|---|---|
| Vouvoyer ("vous écrivez", "vous remarquez"). | Tutoyer, tutoyer-vouvoyer en alternance. |
| Phrases courtes et directes. | Phrases longues avec subordonnées en cascade. |
| Voix active. | Voix passive systématique ("il est nécessaire que…"). |
| Encourager les essais ("essayez, puis comparez"). | Culpabiliser ("vous auriez dû savoir"). |
| Expliciter les pièges courants. | Faire semblant qu'ils n'existent pas. |
| Reconnaître la difficulté ("ce concept demande un peu de pratique"). | Minimiser ("c'est trivial", "facile"). |

**Bannir** : humour, argot, références culturelles (cinéma, jeux vidéo, mèmes), anglicismes non techniques, expressions idiomatiques régionales.

## 3. Niveau de langue

### Vocabulaire technique
**Règle des trois fois** : un terme technique est défini la première fois, rappelé brièvement la deuxième fois, utilisé librement la troisième.

> Exemple — première occurrence :
> > Un **type primitif** est un type de donnée de base, fourni directement par le langage Java (par opposition aux types objets que vous créerez plus loin).

> Deuxième occurrence (chapitre suivant) :
> > Les types primitifs (les types de base : `int`, `double`, `boolean`, etc.) ne peuvent pas être nuls.

> Troisième occurrence et au-delà :
> > Les types primitifs ne peuvent pas être nuls.

### Termes en anglais
Le code Java est en anglais — c'est non négociable. Mais le **texte autour** est en français.

| Acceptable en français | À garder en anglais |
|---|---|
| "Une boucle", "une condition", "une méthode" | `for`, `while`, `if`, `class`, `static`, `final` (mots-clés) |
| "Un tableau", "une chaîne de caractères" | `String`, `int[]` (noms de types) |
| "Une exception", "une interface" | `Exception`, `Comparable` (noms de classes/interfaces) |
| "Un test unitaire" | `@Test`, `assertEquals` (API) |

### Acronymes
Toujours développés à la première occurrence du document.

> **JVM** (Java Virtual Machine) — la machine virtuelle qui exécute le code Java compilé.

## 4. Vocabulaire et analogies métier

Un contexte métier concret est un **levier pédagogique** quand il rend une notion plus tangible. Pas un gimmick à plaquer partout.

### Faire
- Utiliser un vocabulaire métier neutre dans les **exemples** : `Membre`, `Niveau`, `Equipe`, `Projet`, `Tache`, `Materiel`, `Bureau`.
- Filer une **analogie** quand elle clarifie : "une interface est un peu comme une fiche de poste — elle dit *quoi* faire sans dire *comment*".
- Choisir des situations crédibles : gestion d'un effectif, journal d'activité, envoi de notifications, inventaire de matériel.

### Ne pas faire
- Forcer des analogies tirées par les cheveux ("le polymorphisme c'est comme une équipe pluridisciplinaire").
- Utiliser du jargon métier pointu non expliqué (acronymes internes, codes propres à une organisation, etc.).
- Présupposer un secteur d'activité particulier ou une expérience professionnelle commune.

**Test simple** : si un débutant qui découvre le secteur peut suivre, c'est bon.

## 5. Inclusivité

- Pas de masculin générique systématique : alterner "le stagiaire / la stagiaire" ou utiliser "vous", "on", "la personne", "l'utilisateur·rice" selon le contexte.
- Pas de stéréotype de genre dans les exemples (la `Membre Dupont` aussi bien que le `Membre Martin`).
- Pas de référence aux origines, religions, opinions politiques dans les exemples.

## 6. Structure d'un chapitre de cours

Tout chapitre suit cette structure :

```markdown
---
id: 1-1-installer-java
sidebar_position: 1
title: "Installer Java 25 et son premier programme"
---

# Installer Java 25 et son premier programme

## Pourquoi ce chapitre
[2–3 phrases : à quoi sert ce chapitre dans votre parcours,
quelles questions concrètes il résout.]

## Ce que vous saurez faire à la fin
- [Objectif 1, formulé en verbe d'action ("compiler", "exécuter", "expliquer")]
- [Objectif 2]
- [Objectif 3]

## 1. [Première notion]
[Texte d'explication.]

### Exemple
```java
// Code annoté.
```

### À retenir
> [Encadré "à retenir" : 1 à 3 points clés.]

## 2. [Deuxième notion]
[…]

## Erreurs fréquentes
- **[Symptôme]** : [cause] → [comment corriger].

## Exercice guidé
[Petit exercice fait pas-à-pas avec le stagiaire, dans le chapitre.
Différent des exercices "secs" du dossier exercises/.]

## Vérifiez vos acquis
- [Question 1, ouverte]
- [Question 2, ouverte]

## Pour aller plus loin
- [Lien externe annoté]
- [Lien externe annoté]

## Prochain chapitre
→ [Lien vers le chapitre suivant]
```

**Longueur cible** : 800 à 2 000 mots. Au-delà, scinder le chapitre.

## 7. Conventions de code dans les exemples

### Lisibilité avant astuce
On préfère **5 lignes claires** à **1 ligne ingénieuse**. Les stagiaires lisent du code Java pour la première fois — la concision prématurée est l'ennemi.

### Nommage
- **Identifiants Java** en anglais : `firstName`, `getCount()`, `MAX_RETRIES`.
- **Identifiants métier** en français quand ils rendent l'intention plus claire pour des francophones débutants : `Membre`, `Niveau`, `affecterAUneEquipe()`. À garder cohérent dans un même exemple.
- Pas de variable d'une lettre sauf `i`, `j`, `k` dans une boucle ou `e` dans un `catch`.

### Commentaires
- Commentaires en **français**.
- Expliquent le **pourquoi**, pas le **quoi**.

```java
// MAUVAIS — commentaire qui paraphrase le code
i = i + 1;  // on ajoute 1 a i

// BON — commentaire qui explique l'intention
i = i + 1;  // passe au membre suivant de la liste
```

### Imports
- Toujours explicites (pas d'`import java.util.*`).
- Groupés et triés (laisser l'IDE faire).

## 8. Messages de la moulinette

C'est le **différenciateur clé du projet**. Chaque message d'erreur de la moulinette suit ce schéma :

```
[Erreur] Court résumé en une phrase claire.

[Cause probable] Explication accessible de la cause la plus fréquente.

[Correction-type]
   ↓
   ```java
   // exemple de code corrigé, commenté
   ```

[Pour comprendre] Lien vers la section concernée du cours.
```

**Exemple** :

```
[Erreur] La méthode `main` est introuvable dans votre classe.

[Cause probable] Soit elle n'est pas déclarée `public static void main(String[] args)`
exactement, soit la classe ne s'appelle pas comme le fichier.

[Correction-type]
   public class HelloWorld {              // doit correspondre au nom du fichier
       public static void main(String[] args) {  // signature exacte requise
           System.out.println("Hello, world!");
       }
   }

[Pour comprendre] Voir le chapitre 1.1, section "Anatomie d'un programme Java".
```

**Jamais** :
- "Erreur : NullPointerException ligne 42" sans plus de contexte.
- Un stack trace brut sans explication.
- "Vous avez mal fait X." (formulation accusatoire).

## 9. Mise en forme Markdown

- **Titres** : `#` une seule fois (titre du chapitre), puis `##`, `###`. Pas de saut de niveau (jamais `#` → `###`).
- **Listes** : tiret `-` pour les listes non ordonnées, chiffres pour les ordonnées.
- **Code inline** : backticks pour les noms de variables, classes, mots-clés (`int`, `HelloWorld`, `for`).
- **Blocs de code** : toujours avec le langage indiqué (` ```java `, ` ```bash `, ` ```text `).
- **Tableaux** : oui pour comparer des concepts, non pour faire joli.
- **Citations** : `>` pour mettre en avant une notion à retenir ou une définition.

## 10. Process de relecture

Aucun contenu pédagogique n'est mergé sans **relecture par au moins un autre formateur** (PR + revue).

**Critères de validation** :
- [ ] Respect de la structure imposée (section 6 pour les cours, [`format-exercice.md`](format-exercice.md) pour les exos).
- [ ] Niveau de langue adapté (test : "un cousin de 18 ans qui ne fait pas d'info comprendrait ?").
- [ ] Pas de jargon métier non expliqué.
- [ ] Code compilable et passant les tests.
- [ ] Pas de fautes d'orthographe / de grammaire (vérifier avec un correcteur).
- [ ] Cohérent avec le [référentiel](referentiel.md) (numérotation, objectifs).

---

*Version 1 — 2026-05-26. Cette charte évolue avec les retours des stagiaires. Toute modification passe par une PR.*
