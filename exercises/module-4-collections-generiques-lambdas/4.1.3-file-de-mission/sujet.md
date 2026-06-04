# Exercice 4.1.3 — File de mission (Deque FIFO)

## Contexte

Le poste de commandement reçoit des missions en continu. Elles sont traitées
dans l'ordre d'arrivée : la première mission reçue est la première traitée.
Ce comportement s'appelle **FIFO** (First In, First Out) et s'implémente
naturellement avec une **Deque**.

## Énoncé

Complétez la classe `FileMission` (champ fourni : `private final Deque<String> file = new ArrayDeque<>();`) :

```java
void ajouter(String mission)   // ajoute la mission en fin de file
String traiterProchaine()      // retire et renvoie la première mission ; null si vide
String prochaine()             // renvoie la première mission SANS la retirer ; null si vide
boolean estVide()              // true si la file ne contient aucune mission
int taille()                   // nombre de missions en attente
```

## Exemple

```text
FileMission f = new FileMission();
f.ajouter("Reconnaissance");
f.ajouter("Ravitaillement");
f.prochaine();         // "Reconnaissance"  (file inchangée, taille = 2)
f.traiterProchaine();  // "Reconnaissance"  (retirée, taille = 1)
f.traiterProchaine();  // "Ravitaillement"  (retirée, taille = 0)
f.traiterProchaine();  // null              (file vide, pas d'exception)
```

## Contraintes

- Package `etnc.m4`. **Ne modifiez pas** les signatures.
- Le champ `private final Deque<String> file` est **déjà déclaré** ; ne le supprimez pas.
- `traiterProchaine` et `prochaine` renvoient `null` si la file est vide (pas d'exception).
- Utilisez les méthodes `addLast`, `pollFirst`, `peekFirst`, `isEmpty`, `size` de `Deque`.

## Ce qui sera vérifié

- L'ordre FIFO : la première mission ajoutée est bien la première traitée.
- `prochaine` consulte sans retirer (la taille reste inchangée).
- `estVide` détecte correctement une file vide ou non vide.
- `taille` reflète le nombre exact de missions en attente.
- Les cas limites : `traiterProchaine` et `prochaine` sur une file vide renvoient `null`.

## Pour aller plus loin (optionnel — non noté)

- Quelle est la différence entre `pollFirst` et `removeFirst` de `Deque` ?
- Comment transformeriez-vous cette file en pile LIFO (Last In, First Out) ?
