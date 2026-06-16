# Exercice 3.4.3 — Comparaison personnalisée (interface)

## Contexte

Le secrétariat traite des dossiers par ordre de priorité. On définit un
**contrat de comparaison** que tout objet « classable » peut respecter.

## Énoncé

L'interface `Ordonnable` vous est **fournie** :

```java
public interface Ordonnable {
    int comparerA(Ordonnable autre);
}
```

`comparerA` renvoie un entier **négatif / nul / positif** selon que l'objet est
avant / égal / après l'autre.

Complétez :

- `Dossier implements Ordonnable` : un `titre` et une `priorite` (champs privés),
  leurs accesseurs `getTitre()`/`getPriorite()`, et `comparerA` qui compare les
  **priorités** (indice : `(Dossier) autre`, puis `Integer.compare`).
- `Classement.plusPrioritaire(Dossier[] dossiers)` : renvoie le dossier de plus
  haute priorité (en utilisant `comparerA`), ou `null` si le tableau est vide.

## Exemple

```text
Dossier a = new Dossier("A", 5);
Dossier b = new Dossier("B", 3);
a.comparerA(b);   // positif (5 > 3)
Classement.plusPrioritaire(new Dossier[] { a, b }).getTitre();   // "A"
```

## Contraintes

- Package `piscine.m3`. **Ne modifiez pas** `Ordonnable` ni les signatures.
- Les champs de `Dossier` sont **privés**.
- `comparerA` renvoie un **signe** (pas forcément la différence brute) ;
  `Integer.compare` est idiomatique.
- `plusPrioritaire` d'un tableau vide renvoie `null`.

## Ce qui sera vérifié

- Les accesseurs de `Dossier`.
- `comparerA` : signe correct (priorité plus haute → positif ; égale → nul).
- L'appel **via le type `Ordonnable`** fonctionne (polymorphisme).
- `plusPrioritaire` (y compris tableau vide → `null`, et singleton).

## Pour aller plus loin (optionnel — non noté)

- Quelle différence entre une interface et une classe abstraite ici ?
- Ajoutez `moinsPrioritaire(...)`.
