package etnc.m3;

/** Exercice 3.3.3 — MilitaireDuRang (solution de reference). */
public class MilitaireDuRang extends Personnel {

    public MilitaireDuRang(String nom) {
        super(nom);
    }

    @Override
    public String grade() {
        return "Militaire du rang";
    }

    @Override
    public double solde() {
        return 1600;
    }
}
