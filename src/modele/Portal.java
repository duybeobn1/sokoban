package modele;

import java.util.HashSet;
import java.util.Set;

public class Portal extends Case {
    private int portalID; // Unique identifier to pair portals

    public Portal(Jeu _jeu, int portalID) {
        super(_jeu);
        this.portalID = portalID;
    }

    @Override
    public boolean peutEtreParcouru() {
        return true; // Portals can always be traversed
    }

    public int getPortalID() {
        return portalID;
    }

    @Override
public boolean entrerSurLaCase(Entite e) {
    Portal destination = jeu.findPortalPair(this);
    if (destination != null) {
        // Before attempting to teleport, reset or ensure the global state is ready
        // This might be done outside this method, depending on your game's structure

        Case destinationCase = jeu.findNextEmptyCaseAdjacentToPortal(destination);
        if (destinationCase != null) {
            // Assuming `recevoirEntite` properly places the entity
            destinationCase.recevoirEntite(e);
        } else {
            // Log or handle the case where no adjacent empty case is found
            System.out.println("No empty case found next to destination portal.");
            return false;
        }
    }
    return true;
}
    
}
