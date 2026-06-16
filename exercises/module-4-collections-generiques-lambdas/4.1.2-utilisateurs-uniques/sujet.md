# Exercice 4.1.2 — Utilisateurs uniques (equals / hashCode)

## Contexte

Un système d'authentification ne doit pas enregistrer **deux fois le même
utilisateur**. Deux utilisateurs sont « le même » s'ils ont le **même nom** et le
**même matricule** — peu importe qu'il s'agisse de deux objets distincts en
mémoire. C'est une **égalité logique**, que vous allez définir vous-même.

## Énoncé

Complétez deux classes (package `piscine.m4`) :

- `Utilisateur` : un `nom` (String) et un `matricule` (int), champs **privés et
  finaux**, fournis avec le constructeur et les accesseurs `getNom()` /
  `getMatricule()`. **À vous d'écrire** `equals(Object)` et `hashCode()` pour que
  deux utilisateurs de mêmes `nom` et `matricule` soient considérés égaux.
- `RegistreUtilisateurs` : conserve des utilisateurs **sans doublon** (à l'aide
  d'un `Set`).
  - `boolean ajouter(Utilisateur u)` : ajoute `u` ; renvoie `true` si réellement
    ajouté, `false` si un utilisateur **égal** est déjà présent.
  - `boolean contient(Utilisateur u)` : `true` si un utilisateur égal est présent.
  - `int nombreDistincts()` : nombre d'utilisateurs distincts enregistrés.
  - `Set<Utilisateur> tous()` : une **copie** de l'ensemble enregistré.

## Exemple

```text
Utilisateur a = new Utilisateur("Dupont", 1234);
Utilisateur b = new Utilisateur("Dupont", 1234);   // objet différent, mêmes champs
a.equals(b);                 // true  (égalité logique)
a.hashCode() == b.hashCode();   // true (cohérence exigée)

RegistreUtilisateurs r = new RegistreUtilisateurs();
r.ajouter(a);                // true
r.ajouter(b);                // false (doublon logique)
r.nombreDistincts();         // 1
```

## Contraintes

- Package `piscine.m4`. **Ne changez pas** les signatures fournies.
- Les champs de `Utilisateur` restent **privés et finaux**.
- `equals` doit vérifier le **type** de l'argument (indice :
  `o instanceof Utilisateur u`) et comparer `nom` ET `matricule`.
- `hashCode` doit être **cohérent** avec `equals` (indice : `Objects.hash(...)`).
- Aucune exception : `equals(null)` renvoie `false`, jamais d'erreur.

## Ce qui sera vérifié

- `equals` : deux utilisateurs de mêmes champs sont égaux ; champs différents →
  non égaux.
- `hashCode` : si deux utilisateurs sont égaux, leurs `hashCode` sont **égaux**.
- `RegistreUtilisateurs` : `ajouter` refuse un doublon logique ;
  `nombreDistincts` ignore les doublons ; `contient` reconnaît une copie égale.

## Pour aller plus loin (optionnel — non noté)

- Pourquoi un `record` génère-t-il automatiquement `equals`/`hashCode` ?
- Que se passerait-il si `hashCode` renvoyait toujours `0` (toujours valide mais
  inefficace) ?
