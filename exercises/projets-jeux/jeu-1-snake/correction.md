# Correction — Snake

Deux méthodes à écrire. L'essentiel est la gestion **fine de la collision** dans `avancer()`.

## `changerDirection`

```java
public void changerDirection(Direction nouvelle) {
    if (termine) {
        return;
    }
    if (serpent.size() > 1 && nouvelle == direction.opposee()) {
        return; // demi-tour interdit quand il y a un corps
    }
    this.direction = nouvelle;
}
```

## `avancer`

```java
public void avancer() {
    if (termine) {
        return;
    }
    Position prochaine = serpent.peekFirst().plus(direction);

    if (horsPlateau(prochaine)) {          // sort du plateau
        termine = true;
        return;
    }

    boolean mange = prochaine.equals(pomme);
    // Sans croissance, la queue libère sa case : y entrer est permis.
    boolean collision = mange ? serpent.contains(prochaine)
                              : corpsSansQueue().contains(prochaine);
    if (collision) {
        termine = true;
        return;
    }

    serpent.addFirst(prochaine);
    if (mange) {
        score++;
        pomme = generateurPommes.get();
    } else {
        serpent.removeLast();
    }
}
```

avec deux aides privées :

```java
private boolean horsPlateau(Position p) {
    return p.ligne() < 0 || p.ligne() >= hauteur
        || p.colonne() < 0 || p.colonne() >= largeur;
}

private List<Position> corpsSansQueue() {
    List<Position> liste = new ArrayList<>(serpent);
    if (!liste.isEmpty()) {
        liste.remove(liste.size() - 1);
    }
    return liste;
}
```

## Pièges fréquents

- **Tester la collision contre tout le corps même sans manger** → on meurt en suivant sa
  propre queue, ce qui rend le jeu injouable. D'où `corpsSansQueue()`.
- **Oublier le cas `mange`** dans le test de collision : en mangeant, la queue *reste*, donc
  il faut tester contre le corps **entier**.
- **Tirer la pomme suivante avant d'avoir confirmé qu'on a mangé** : ne tirez `generateurPommes.get()`
  que dans la branche `mange`.
- **Bouger après la fin** : toujours `if (termine) return;` en tête des deux méthodes.
