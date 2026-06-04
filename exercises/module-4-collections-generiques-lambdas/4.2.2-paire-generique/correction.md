# Correction — Exercice 4.2.2 Paire générique (deux paramètres de type)

## Démarche attendue

1. **En-tête** : `public final class Paire<A, B>`. Deux champs
   `private final A premier;` et `private final B second;`, affectés une seule
   fois dans le constructeur.
2. **Accesseurs** : `public A premier()` et `public B second()` renvoient
   directement les champs.
3. **`inverser`** : `return new Paire<>(second, premier);`. Le type de retour est
   `Paire<B, A>` (les paramètres de type sont échangés, comme les valeurs). On crée
   un **nouvel** objet : l'original est inchangé.
4. **`de`** : méthode **générique** `static <X, Y> Paire<X, Y> de(X x, Y y)` qui
   renvoie `new Paire<>(x, y)`. Les paramètres de type `X` et `Y` sont propres à la
   méthode (différents de `A`/`B` de la classe) et sont **inférés** à l'appel.

## Points clés

- **Deux paramètres de type** : `<A, B>` permet d'associer deux types indépendants.
  À l'`inverser()`, l'ordre s'inverse : `Paire<A, B>` → `Paire<B, A>`.
- **Immuabilité** : champs `final`, classe `final`, aucun mutateur. Toute
  « modification » produit un nouvel objet. C'est ce qui rend `inverser()` sûr.
- **Méthode générique** : `static <X, Y>` introduit ses propres paramètres de type,
  inférés depuis les arguments — d'où `Paire.de("x", 1)` sans diamant explicite.
- **Preuve de généricité par compilation** : `String s = p.premier();` et
  `Integer i = p.second();` ne compilent que si `<A, B>` est correctement propagé.
  Si on avait utilisé `Object`, ces affectations exigeraient un cast.

## Erreurs fréquentes observées

- Stocker en `Object` puis caster (perd la généricité et brise les tests).
- `inverser()` qui modifie les champs au lieu de créer une nouvelle paire
  (impossible ici car `final`, mais le réflexe est à corriger).
- Type de retour `Paire<A, B>` pour `inverser()` au lieu de `Paire<B, A>`.
- Oublier les `<X, Y>` devant le type de retour de `de` (la rend non générique).

## Pour approfondir

- `java.util.Map.Entry<K, V>` : la paire clé/valeur du JDK.
- Les `record` paramétrés (`record Paire<A, B>(A premier, B second) {}`) offrent la
  même immuabilité avec encore moins de code (vu plus loin dans le module).
