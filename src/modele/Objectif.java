package modele;

public class Objectif extends Case{

    public Objectif(Jeu _jeu) {
        super(_jeu);
    }

    @Override
    public boolean peutEtreParcouru() {
        return e == null;
    }

    
    
}
