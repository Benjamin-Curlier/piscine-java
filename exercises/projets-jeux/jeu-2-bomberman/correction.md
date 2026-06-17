# Correction — Bomberman

Deux méthodes. La difficulté est la **propagation du souffle** dans `tick()`.

## `deplacer`

```java
public void deplacer(Direction d) {
    if (!joueurVivant) {
        return;
    }
    Position cible = positionJoueur.plus(d);
    if (plateau.estDansPlateau(cible) && plateau.caseEn(cible) == Case.VIDE) {
        positionJoueur = cible;
    }
}
```

## `tick`

On sépare les bombes qui explosent de celles qui continuent, puis on déclenche les explosions.

```java
public void tick() {
    List<Bombe> restantes = new ArrayList<>();
    List<Bombe> aExploser = new ArrayList<>();
    for (Bombe b : bombes) {
        Bombe avancee = new Bombe(b.position(), b.compteur() - 1, b.portee());
        if (avancee.compteur() <= 0) {
            aExploser.add(avancee);
        } else {
            restantes.add(avancee);
        }
    }
    bombes = restantes;
    for (Bombe b : aExploser) {
        exploser(b);
    }
}

private void exploser(Bombe b) {
    Set<Position> souffle = new HashSet<>();
    souffle.add(b.position());                       // la case centrale !
    for (Direction d : Direction.values()) {
        Position p = b.position();
        for (int i = 1; i <= b.portee(); i++) {
            p = p.plus(d);
            if (!plateau.estDansPlateau(p)) {
                break;
            }
            Case c = plateau.caseEn(p);
            if (c == Case.MUR) {
                break;                               // bloqué, sans toucher le mur
            }
            souffle.add(p);
            if (c == Case.CAISSE) {
                plateau.detruire(p);
                break;                               // absorbé par la caisse
            }
        }
    }
    if (souffle.contains(positionJoueur)) {
        joueurVivant = false;
    }
}
```

## Pièges fréquents

- **Oublier la case centrale** dans le souffle → le joueur posé sur sa propre bombe survit à tort.
- **Détruire la caisse mais continuer** au lieu de s'arrêter → le souffle traverse les caisses.
- **Toucher la case du mur** → le mur devrait protéger ce qu'il y a derrière, lui compris.
- **Tirer le compteur côté record** : `Bombe` est immuable, on crée une **nouvelle** `Bombe` décrémentée.
