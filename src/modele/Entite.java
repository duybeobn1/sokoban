package modele;

/**
 * Entités amenées à bouger
 */
public abstract class Entite extends Obj {

    protected Case c;

    public Entite(Jeu _jeu, Case _c) { super(_jeu); c = _c; c.setEntite(this);}

    public void quitterCase() {
        if (this.c != null) {
            this.c.setEntite(null); // This line is crucial.
            this.c = null;
        }
    }
    

    public Case getCase() {
        return c;
    }

    public void setCase(Case _c) {
        c = _c;
    }

    public void allerSurCase(Case _c) {
        c = _c;
    }

    public boolean pousser(Direction d) {
        return false;
    }


    public boolean avancerDirectionChoisie(Direction d) {
        return jeu.deplacerEntite(this, d);
    }
    public void moveOpposite(Direction d) {
        avancerDirectionChoisie(d.opposite());
    }

}
