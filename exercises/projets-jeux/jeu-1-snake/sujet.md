# Jeu capstone — Snake 🐍

## Contexte

Assez d'exercices « abstraits » : vous allez construire un **vrai jeu**, jouable au clavier.
Le rendu graphique (fenêtre, dessin, clavier, horloge) vous est **entièrement fourni** —
votre travail est le **cerveau du jeu** : faire avancer le serpent, gérer les collisions et le score.

C'est l'exercice où l'on voit son code prendre vie. Une fois les deux méthodes terminées,
lancez le jeu et jouez.

## Ce qui vous est fourni (ne pas modifier)

| Fichier | Rôle |
|---------|------|
| `Direction.java` | Enum `HAUT/BAS/GAUCHE/DROITE` avec `dLigne()`, `dColonne()`, `opposee()`. |
| `Position.java` | Record `(ligne, colonne)` 0-indexé, avec `plus(Direction)`. |
| `SnakeSwing.java` | La fenêtre de jeu (Swing) : dessin, flèches du clavier, horloge. **Le « wow ».** |
| `JeuSnake.java` | Le cœur du jeu — **champs, constructeur et accesseurs déjà écrits**. |

Le serpent est une file de cases (tête en premier). La **pomme suivante** est tirée d'un
**générateur** injecté (`Supplier<Position>`) : c'est ce qui rend la logique testable de façon
déterministe — et qui permet à la fenêtre de jeu d'utiliser des pommes aléatoires.

## Votre mission

Dans `JeuSnake.java`, implémentez **deux méthodes** (vos méthodes ont un accès direct aux
champs privés `serpent`, `direction`, `pomme`, `score`, `termine`, `largeur`, `hauteur`, `generateurPommes`) :

### 1. `changerDirection(Direction nouvelle)`
- Sans effet si la partie est terminée.
- **Demi-tour interdit** dès que le serpent a un corps : si `serpent.size() > 1` et
  `nouvelle == direction.opposee()`, on **ignore** le changement.
- Sinon, on met à jour `direction`.

### 2. `avancer()` — un pas de jeu
Sans effet si la partie est déjà terminée. Sinon, calculez la **nouvelle tête** =
`serpent.peekFirst().plus(direction)`, puis :

| Situation | Effet |
|-----------|-------|
| La nouvelle tête **sort du plateau** (`ligne`/`colonne` &lt; 0 ou ≥ `hauteur`/`largeur`) | partie terminée |
| La nouvelle tête **entre dans le corps** | partie terminée… |
| …**sauf** la case que la **queue vient de libérer** (quand on ne mange pas) | mouvement permis |
| La nouvelle tête tombe sur la **pomme** | grandit (la queue reste), `score++`, nouvelle pomme = `generateurPommes.get()` |
| sinon | avance : ajouter la tête en avant, retirer la queue |

> 💡 **Astuce collision** : quand vous **ne mangez pas**, la queue va bouger ; il faut donc
> tester la collision contre le corps **sans sa dernière case**. Quand vous **mangez**, la queue
> reste : testez contre **tout** le corps.

## Ce qui sera vérifié

- **Tests publics** : déplacement, prise en compte de la direction, manger (taille + score +
  pomme suivante), sortie de plateau.
- **Tests privés** : collision avec soi-même, le cas « suivre sa queue », aucun effet après la fin.
- Le programme **compile** et le **style** est propre.

## 🎮 Jouer

Une fois vos deux méthodes écrites :

```bash
# depuis starter/  (ou votre rendu)
mvn -q compile exec:java -Dexec.mainClass=piscine.jeux.SnakeSwing
# … ou compilez puis lancez la classe SnakeSwing depuis votre IDE
```

Flèches du clavier pour diriger. Mangez les pommes rouges, évitez les murs et votre propre corps.

## Pour aller plus loin (optionnel — non noté)

- Faites **accélérer** le jeu à mesure que le score grimpe.
- Empêchez la pomme d'apparaître **sur le serpent** (bouclez le tirage tant que la case est occupée).
- Ajoutez un **mode « mur qui téléporte »** (sortir d'un côté réapparaît de l'autre).
