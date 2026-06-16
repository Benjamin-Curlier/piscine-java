# Exercice 4.1.3 — File de tâches (Deque FIFO)

## Contexte

Le gestionnaire de tâches reçoit des tâches en continu. Elles sont traitées
dans l'ordre d'arrivée : la première tâche reçue est la première traitée.
Ce comportement s'appelle **FIFO** (First In, First Out) et s'implémente
naturellement avec une **Deque**.

## Énoncé

Complétez la classe `FileTaches` (champ fourni : `private final Deque<String> file = new ArrayDeque<>();`) :

```java
void ajouter(String tache)     // ajoute la tâche en fin de file
String traiterProchaine()      // retire et renvoie la première tâche ; null si vide
String prochaine()             // renvoie la première tâche SANS la retirer ; null si vide
boolean estVide()              // true si la file ne contient aucune tâche
int taille()                   // nombre de tâches en attente
```

## Exemple

```text
FileTaches f = new FileTaches();
f.ajouter("Revue de code");
f.ajouter("Déploiement");
f.prochaine();         // "Revue de code"  (file inchangée, taille = 2)
f.traiterProchaine();  // "Revue de code"  (retirée, taille = 1)
f.traiterProchaine();  // "Déploiement"    (retirée, taille = 0)
f.traiterProchaine();  // null             (file vide, pas d'exception)
```

## Contraintes

- Package `piscine.m4`. **Ne modifiez pas** les signatures.
- Le champ `private final Deque<String> file` est **déjà déclaré** ; ne le supprimez pas.
- `traiterProchaine` et `prochaine` renvoient `null` si la file est vide (pas d'exception).
- Utilisez les méthodes `addLast`, `pollFirst`, `peekFirst`, `isEmpty`, `size` de `Deque`.

## Ce qui sera vérifié

- L'ordre FIFO : la première tâche ajoutée est bien la première traitée.
- `prochaine` consulte sans retirer (la taille reste inchangée).
- `estVide` détecte correctement une file vide ou non vide.
- `taille` reflète le nombre exact de tâches en attente.
- Les cas limites : `traiterProchaine` et `prochaine` sur une file vide renvoient `null`.

## Pour aller plus loin (optionnel — non noté)

- Quelle est la différence entre `pollFirst` et `removeFirst` de `Deque` ?
- Comment transformeriez-vous cette file en pile LIFO (Last In, First Out) ?
