/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

public abstract class Case extends Obj {

    protected Entite e;
    public abstract boolean peutEtreParcouru();

    public void recevoirEntite(Entite e) {
        this.setEntite(e); // Set the entity for this case
        e.setCase(this); // Update the entity's current case
    }
    
    public boolean entrerSurLaCase(Entite e) {
        setEntite(e);
        return true;
    }

    public void quitterLaCase() {
        e = null;
    }

    public Case(Jeu _jeu) {
        super(_jeu);
    }

    public Entite getEntite() {
        return e;
    }

    public void setEntite(Entite _e) {

        e = _e;
        e.setCase(this);}

   }
