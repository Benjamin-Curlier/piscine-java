# Correction — Exercice 4.3.1 Filtrage et projection (streams)

## Démarche attendue

1. `nomsDesGradesAuMoins` : ouvrir un stream sur la liste, `filter` avec
   `s -> s.grade().compareTo(min) >= 0` (l'enum `Grade` est `Comparable` par
   ordre de déclaration), puis `map(Soldat::nom)` pour ne garder que les noms,
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

    public static List<String> nomsDesGradesAuMoins(List<Soldat> soldats, Grade min) {
        return soldats.stream()
                .filter(s -> s.grade().compareTo(min) >= 0)
                .map(Soldat::nom)
                .collect(Collectors.toList());
    }

    public static List<Soldat> filtrer(List<Soldat> soldats, Predicate<Soldat> critere) {
        return soldats.stream()
                .filter(critere)
                .collect(Collectors.toList());
    }
}
```

## Points clés

- **`Predicate<T>` en paramètre** : rend la méthode générique sans réflexion ;
  l'appelant passe n'importe quelle lambda `s -> ...`.
- **`compareTo` sur enum** : l'ordre naturel d'un enum est l'ordre de déclaration,
  ce qui donne ici SOLDAT < CAPORAL < SERGENT < ADJUDANT < LIEUTENANT.
- **Liste vide** : si aucun élément ne passe le filtre, `collect` renvoie une
  liste vide (jamais `null`) — pas besoin de traitement spécial.
- **Immutabilité de la source** : un stream ne modifie pas la collection d'origine.

## Erreurs fréquentes observées

- Retourner `null` plutôt qu'une liste vide quand aucun élément ne satisfait.
- Coder le critère en dur dans `filtrer` au lieu d'utiliser le `Predicate` reçu.
- Oublier `map(Soldat::nom)` et renvoyer des `Soldat` au lieu de `String`.
- Utiliser `==` au lieu de `compareTo` pour comparer les grades.

## Pour approfondir

- `Stream.toList()` (Java 16+) renvoie une liste **non modifiable** ;
  `Collectors.toList()` renvoie une liste modifiable (ArrayList).
- Les méthodes statiques des collecteurs (`Collectors.joining`, `counting`, etc.)
  permettent des agrégations plus avancées (exercice 4.3.3).
