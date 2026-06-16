# Correction — Exercice 4.3.1 Filtrage et projection (streams)

## Démarche attendue

1. `nomsDesNiveauxAuMoins` : ouvrir un stream sur la liste, `filter` avec
   `s -> s.niveau().compareTo(min) >= 0` (l'enum `Niveau` est `Comparable` par
   ordre de déclaration), puis `map(Membre::nom)` pour ne garder que les noms,
   puis `collect(Collectors.toList())`.

2. `filtrer` : même squelette, mais le `Predicate` est reçu en paramètre —
   on l'utilise directement dans `.filter(critere)`. La liste source n'est pas
   altérée, le stream produit une nouvelle liste.

## Solution de référence

```java
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Effectifs {

    public static List<String> nomsDesNiveauxAuMoins(List<Membre> membres, Niveau min) {
        return membres.stream()
                .filter(s -> s.niveau().compareTo(min) >= 0)
                .map(Membre::nom)
                .collect(Collectors.toList());
    }

    public static List<Membre> filtrer(List<Membre> membres, Predicate<Membre> critere) {
        return membres.stream()
                .filter(critere)
                .collect(Collectors.toList());
    }
}
```

## Points clés

- **`Predicate<T>` en paramètre** : rend la méthode générique sans réflexion ;
  l'appelant passe n'importe quelle lambda `s -> ...`.
- **`compareTo` sur enum** : l'ordre naturel d'un enum est l'ordre de déclaration,
  ce qui donne ici JUNIOR < CONFIRME < SENIOR < LEAD < PRINCIPAL.
- **Liste vide** : si aucun élément ne passe le filtre, `collect` renvoie une
  liste vide (jamais `null`) — pas besoin de traitement spécial.
- **Immutabilité de la source** : un stream ne modifie pas la collection d'origine.

## Erreurs fréquentes observées

- Retourner `null` plutôt qu'une liste vide quand aucun élément ne satisfait.
- Coder le critère en dur dans `filtrer` au lieu d'utiliser le `Predicate` reçu.
- Oublier `map(Membre::nom)` et renvoyer des `Membre` au lieu de `String`.
- Utiliser `==` au lieu de `compareTo` pour comparer les niveaux.

## Pour approfondir

- `Stream.toList()` (Java 16+) renvoie une liste **non modifiable** ;
  `Collectors.toList()` renvoie une liste modifiable (ArrayList).
- Les méthodes statiques des collecteurs (`Collectors.joining`, `counting`, etc.)
  permettent des agrégations plus avancées (exercice 4.3.3).
