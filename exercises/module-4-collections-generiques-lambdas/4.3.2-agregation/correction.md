# Correction — Exercice 4.3.2 Réductions numériques (streams)

## Démarche attendue

1. `total` : `soldats.size()` suffit (ou `.stream().count()` converti en `int`).
2. `ancienneteMoyenne` : `soldats.stream().mapToInt(Soldat::anciennete)` produit
   un `IntStream`, sur lequel `.average()` renvoie un `OptionalDouble`.
   `.orElse(0.0)` court-circuite le cas liste vide sans exception.
3. `ancienneteMax` : même principe avec `.max()` qui renvoie un `OptionalInt`,
   et `.orElse(0)` comme sentinelle.

## Solution de référence

```java
import java.util.List;

public class Statistiques {

    public static int total(List<Soldat> soldats) {
        return soldats.size();
    }

    public static double ancienneteMoyenne(List<Soldat> soldats) {
        return soldats.stream()
                .mapToInt(Soldat::anciennete)
                .average()
                .orElse(0.0);
    }

    public static int ancienneteMax(List<Soldat> soldats) {
        return soldats.stream()
                .mapToInt(Soldat::anciennete)
                .max()
                .orElse(0);
    }
}
```

## Points clés

- **`mapToInt`** : spécialise le stream en `IntStream` pour accéder aux
  opérations numériques (`average`, `max`, `sum`, etc.) sans boxing.
- **`OptionalDouble` / `OptionalInt`** : retournés par `average()` / `max()`
  quand le stream est vide. `.orElse(valeur)` extrait la valeur ou renvoie la
  sentinelle.
- **Valeur-sentinelle** : convention choisie ici (0 / 0.0) pour éviter toute
  exception sur liste vide. C'est un choix de conception explicite documenté
  dans les signatures.

## Erreurs fréquentes observées

- Appeler `.average().getAsDouble()` ou `.max().getAsInt()` sans `orElse` :
  lève `NoSuchElementException` sur liste vide.
- Utiliser un `for` / `if (soldats.isEmpty()) return 0.0;` à la place des
  streams — fonctionnel mais non idiomatique (noté par le formateur).
- Confondre `OptionalDouble` (de `IntStream.average()`) et `Optional<Double>`.

## Pour approfondir

- `IntStream.sum()`, `IntStream.min()` suivent le même patron.
- `Collectors.averagingInt(Soldat::anciennete)` est une alternative via
  `collect` — produit un `Double` (0.0 sur liste vide), donc équivalent ici.
