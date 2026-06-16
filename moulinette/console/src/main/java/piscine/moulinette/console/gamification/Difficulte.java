package piscine.moulinette.console.gamification;

/** Difficulté d'un exercice et l'XP qu'il rapporte une fois validé. */
public enum Difficulte {
    TRES_FACILE(10),
    DEBUTANT(10),
    FACILE(15),
    MOYEN(25),
    DIFFICILE(40),
    TRES_DIFFICILE(60);

    private final int xp;

    Difficulte(int xp) {
        this.xp = xp;
    }

    public int xp() {
        return xp;
    }

    /** Convertit la valeur du metadata ({@code "tres-facile"}, {@code "moyen"}, …). FACILE par défaut. */
    public static Difficulte depuis(String valeur) {
        if (valeur == null) {
            return FACILE;
        }
        return switch (valeur.strip().toLowerCase()) {
            case "tres-facile", "très-facile" -> TRES_FACILE;
            case "debutant", "débutant" -> DEBUTANT;
            case "facile" -> FACILE;
            case "moyen" -> MOYEN;
            case "difficile" -> DIFFICILE;
            case "tres-difficile", "très-difficile" -> TRES_DIFFICILE;
            default -> FACILE;
        };
    }
}
