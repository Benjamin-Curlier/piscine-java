# Correction — Exercice 1.2.2 Calculs géométriques

## Démarche attendue

```java
double rayon = clavier.nextDouble();
double aire = Math.PI * rayon * rayon;
double perimetre = 2 * Math.PI * rayon;
System.out.println("Aire : " + aire);
System.out.println("Périmètre : " + perimetre);
```

Étapes intellectuelles :

1. **Lire le rayon** en `double`.
2. **Utiliser `Math.PI`** plutôt qu'une valeur écrite à la main : plus précis et plus lisible.
3. **Appliquer les deux formules** et afficher chaque résultat sur sa ligne.

## Points clés

- **`Math.PI`** est une constante de la bibliothèque standard : la valeur de π la plus précise disponible. La réécrire (`3.14`) fausserait le calcul.
- **Tout en `double`** : `Math.PI` est un `double`, donc l'ensemble des calculs reste à virgule.
- **Sortie exacte** : la moulinette compare la représentation produite par Java (`12.566370614359172`, etc.). Inutile d'arrondir.

## Erreurs fréquentes observées

- **Écrire π à la main** (`3.14`) → résultats imprécis, tests qui échouent.
- **Oublier un facteur** : aire `π × r × r`, périmètre `2 × π × r`. Ne pas confondre.
- **Format approximatif** (libellés, espaces, retour à la ligne entre les deux résultats).

## Variantes possibles

On peut factoriser avec une variable intermédiaire :

```java
double rayonCarre = rayon * rayon;
double aire = Math.PI * rayonCarre;
```

Utile si le calcul se complexifie, mais pas indispensable ici.

## Pour approfondir

- [La classe `Math` — Javadoc OpenJDK 25](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/lang/Math.html)
