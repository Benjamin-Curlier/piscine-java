# Exercice 1.2.2 — Calculs géométriques

## Contexte

Tracer une zone de sécurité circulaire autour d'un point, estimer une surface : les calculs géométriques de base reviennent souvent. Vous allez écrire un programme qui, à partir du rayon d'un cercle, en calcule l'aire et le périmètre.

## Énoncé

Complétez la méthode `main` pour :

1. Afficher `Rayon du cercle ?` (déjà fait dans le squelette).
2. Lire le rayon (un nombre à virgule).
3. Calculer l'**aire** (`π × rayon × rayon`) et le **périmètre** (`2 × π × rayon`).
4. Afficher `Aire : <aire>` puis, sur une nouvelle ligne, `Périmètre : <perimetre>`.

Utilisez la constante `Math.PI` fournie par Java (n'écrivez pas une valeur approchée de π à la main).

## Exemple

**Exécution attendue** (l'utilisateur saisit `2`) :

```text
Rayon du cercle ?
Aire : 12.566370614359172
Périmètre : 12.566370614359172
```

(Pour un rayon de 2, l'aire et le périmètre tombent sur la même valeur — ce n'est qu'une coïncidence.)

## Contraintes

- La classe doit s'appeler `CalculsGeometriques` et rester dans le package `etnc.m1`.
- Utilisez `Math.PI`.
- Les calculs se font en `double`.
- Le format de sortie doit être exactement celui de l'exemple.

## Ce qui sera vérifié

- Votre programme **compile** sans erreur.
- L'aire et le périmètre sont **corrects** (testés sur plusieurs rayons).
- La sortie respecte le format attendu.

## Pour aller plus loin (optionnel — non noté)

- Calculez aussi le **diamètre** (`2 × rayon`) et affichez-le.
- Pour un rayon de 1, l'aire vaut π et le périmètre 2π : vérifiez-le.
