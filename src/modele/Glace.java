package modele;

public class Glace extends Case {
    public Glace(Jeu _jeu) {
        super(_jeu);
    }

    @Override
    public boolean peutEtreParcouru() {
        return true; // La glace peut être parcourue, mais avec un comportement spécial.
    }
}
