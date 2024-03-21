package modele;

public class Objectif extends Case{

    public Objectif(Jeu _jeu) {
        super(_jeu);
    }

    @Override
    public boolean peutEtreParcouru() {
        return e == null;
    }

    public boolean hasBlock() {
        // Check if the entity on this objective is a block
        return (getEntite() instanceof Bloc);
    }

    
    
}
