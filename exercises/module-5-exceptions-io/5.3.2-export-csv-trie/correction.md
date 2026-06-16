# Correction — Exercice 5.3.2 Export CSV trié (Comparator + String.join + Files.write)

## Démarche attendue

```java
public static void exporter(List<Personnel> personnels, Path csv) throws IOException {
    // Copie défensive : on ne trie jamais la liste passée en paramètre
    // (respecter le contrat "source non mutée" attendu par l'appelant).
    List<Personnel> copie = new ArrayList<>(personnels);

    // Tri idiomatique M4 : ancienneté décroissante (.reversed()),
    // puis nom alphabétique croissant pour égalité d'ancienneté.
    copie.sort(Comparator.comparingInt(Personnel::anciennete)
                         .reversed()
                         .thenComparing(Personnel::nom));

    // Construction des lignes CSV : en-tête obligatoire en première position,
    // puis une ligne par personnel avec String.join (pas de concaténation manuelle).
    List<String> lignes = new ArrayList<>();
    lignes.add("nom,niveau,anciennete");
    for (Personnel p : copie) {
        // niveau().name() renvoie le nom de la constante enum ("SENIOR", etc.)
        lignes.add(String.join(",", p.nom(), p.niveau().name(), String.valueOf(p.anciennete())));
    }

    // Files.write écrase le fichier s'il existe déjà (comportement par défaut).
    // UTF-8 explicite pour la portabilité (Windows peut avoir un charset par défaut différent).
    Files.write(csv, lignes, StandardCharsets.UTF_8);
}
```

## Points clés

- **Copie défensive avec `new ArrayList<>(personnels)`** : `List.sort` mute la
  liste sur laquelle on l'appelle. En triant la copie, la liste source de
  l'appelant reste dans son ordre d'origine. C'est un principe fondamental de
  conception : ne pas produire d'effets de bord invisibles.

- **`Comparator` chaîné** : `Comparator.comparingInt(Personnel::anciennete)` crée
  un comparateur par ancienneté croissante. `.reversed()` l'inverse (décroissant).
  `.thenComparing(Personnel::nom)` ajoute le tri alphabétique en critère secondaire
  (uniquement quand deux anciennetés sont égales). Ce chaînage est idiomatique M4.

- **`String.join(",", ...)`** : construit `"nom,niveau,ancienneté"` proprement.
  Évite les concaténations `+` et garantit que les séparateurs sont tous identiques.
  Avec une liste vide, `String.join` renverrait `""`, mais ici on passe des
  varargs non vides.

- **`p.niveau().name()`** : `name()` renvoie le nom exact de la constante enum
  (`"SENIOR"`, `"PRINCIPAL"`, etc.). `toString()` est souvent identique mais
  peut être surchargé — `name()` est garantit par l'API Java et est plus lisible
  pour indiquer l'intention.

- **`String.valueOf(p.anciennete())`** : convertit le `int` en `String` sans
  ambiguïté. Alternativement `Integer.toString(p.anciennete())` est équivalent.
  La concaténation `"" + p.anciennete()` fonctionne aussi mais est moins explicite.

- **`Files.write(csv, lignes, StandardCharsets.UTF_8)`** : écrit la `List<String>`
  ligne par ligne avec le séparateur de ligne de la plateforme (`\n` sur
  Unix/Linux, `\r\n` sur Windows selon l'implémentation JVM). Pour forcer `\n`
  sur toutes les plateformes, on pourrait passer `lines.stream().collect(joining("\n"))`
  comme `String` — mais pour cet exercice le comportement par défaut est acceptable.

## Erreurs fréquentes observées

- **Muter la liste source (`personnels.sort(...)` sans copie)** : le code passe
  les tests d'export, mais les tests « source non mutée » échouent. Le correcteur
  verra aussi la violation de contrat lors de la revue formateur (critère idiomatisme).

- **Concaténation `+` au lieu de `String.join`** : fonctionne mais manque
  d'idiomatisme ; le critère formateur signalera le point.

- **Oublier `StandardCharsets.UTF_8`** : les tests avec accents (Léonard, Éric,
  Noël…) échouent sur Windows où l'encodage par défaut est CP-1252.

- **Utiliser `niveau().toString()` au lieu de `niveau().name()`** : identiques pour
  les enums qui ne surchargent pas `toString`, mais `name()` est l'intention
  correcte ici — on veut le nom de la constante, pas une représentation arbitraire.

- **Utiliser `stream().map(...).collect(toList())` et `Files.write` correctement**:
  acceptable techniquement, mais la boucle `for` explicite est plus lisible pour
  des débutants et reste idiomatique.

## Pour approfondir

- `Files.write` prend une `Iterable<? extends CharSequence>` — une `List<String>`
  est un `Iterable<String>`, donc compatible directement. La JVM écrit chaque
  élément suivi du séparateur de ligne natif.

- Pour forcer `\n` (LF Unix) indépendamment de la plateforme, on peut écrire
  `String.join("\n", lignes)` puis `Files.writeString(csv, contenu, UTF_8)`.
  Utile pour les tests cross-plateforme.

- `Comparator.reversed()` crée un nouveau `Comparator` qui délègue à l'original
  en inversant le signe de comparaison. Le chaînage `.thenComparing(...)` est
  défini sur l'interface `Comparator` (méthode par défaut) et ne rompt pas la
  composition.
