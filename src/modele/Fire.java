package modele;

public class Fire extends Case {

    private boolean extinguished = false;

    public Fire(Jeu _jeu) {
        super(_jeu);
    }

    @Override
    public boolean peutEtreParcouru() {
        return extinguished;
    }

    public void extinguish() {
        extinguished = true;
    }

}