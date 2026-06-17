# Jeu capstone — Bomberman 💣

## Contexte

Deuxième jeu, et même promesse : vous écrivez le **cerveau**, on vous fournit le **corps**. Ici,
un jeu de grille façon *Bomberman* : un joueur se déplace, pose des **bombes à retardement**, qui
explosent en **croix** et détruisent les **caisses** (mais pas les murs).

C'est un cran au-dessus de Snake : il y a une **grille typée** (vide / mur / caisse) et une
**propagation** avec des règles d'arrêt. Une fois fini : on joue.

## Ce qui vous est fourni (ne pas modifier)

| Fichier | Rôle |
|---------|------|
| `Case.java` | Enum `VIDE` / `MUR` (indestructible) / `CAISSE` (destructible). |
| `Position.java`, `Direction.java` | Coordonnées et directions (comme dans Snake). |
| `Plateau.java` | La grille. `Plateau.depuis("...","...")` construit une carte (`#`=mur, `o`=caisse, `.`=vide). |
| `Bombe.java` | Record `(position, compteur, portee)`. |
| `BombermanSwing.java` | La fenêtre de jeu (flèches + Espace). **Le « wow ».** |
| `JeuBomberman.java` | Le cœur — **champs, constructeur, `poserBombe` et accesseurs fournis**. |

## Votre mission

Dans `JeuBomberman.java`, implémentez **deux méthodes** :

### 1. `deplacer(Direction d)`
- Sans effet si le joueur est mort (`!joueurVivant`).
- Calculez la case cible `positionJoueur.plus(d)`. Déplacez le joueur **seulement si** elle est
  `plateau.estDansPlateau(cible)` **et** `plateau.caseEn(cible) == Case.VIDE`.

### 2. `tick()` — un pas de temps
- **Décrémentez** le `compteur` de chaque bombe. Une bombe dont le compteur atteint **0**
  **explose** et disparaît de la liste.
- **Explosion** (souffle en croix depuis la case de la bombe, sur `portee` cases) : dans **chacune**
  des 4 directions, avancez case par case…
  - **hors plateau** → on s'arrête ;
  - **MUR** → on s'arrête **sans** toucher la case (le mur protège ce qu'il y a derrière) ;
  - **CAISSE** → on la **détruit** (`plateau.detruire(...)`) puis on s'arrête (la caisse absorbe le souffle) ;
  - **VIDE** → la case est touchée, on continue.
- Si la **case du joueur** est dans le souffle, il **meurt** (`joueurVivant = false`).

> 💡 Construisez d'abord l'ensemble des cases touchées (un `Set<Position>`), puis testez si le joueur
> y est. Et n'oubliez pas la case **centrale** (celle de la bombe) dans le souffle.

## Ce qui sera vérifié

- **Tests publics** : déplacement (bloqué par mur/caisse/bord), minuterie des bombes, destruction d'une caisse.
- **Tests privés** : mort/survie du joueur, souffle bloqué par un mur et absorbé par la première caisse, aucun effet après la mort.
- Le programme **compile** et le **style** est propre.

## 🎮 Jouer

```bash
mvn -q compile exec:java -Dexec.mainClass=piscine.jeux.BombermanSwing
```

Flèches pour bouger, **Espace** pour poser une bombe. Détruisez les caisses, ne restez pas dans le souffle !

## Pour aller plus loin (optionnel — non noté)

- **Réactions en chaîne** : si le souffle atteint une autre bombe, faites-la exploser tout de suite.
- Faites apparaître un **bonus** (portée +1) sous certaines caisses détruites.
- Ajoutez un **second joueur** (autres touches) et un score.
