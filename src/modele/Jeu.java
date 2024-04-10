/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import java.util.Stack;

public class Jeu extends Observable {
    private Stack<Move> heroMoveStack = new Stack<>();
    private Stack<Move> blockMoveStack = new Stack<>(); // Stack for block movements

    public static final int SIZE_X = 10;
    public static final int SIZE_Y = 10;

    public int compteurPas = 0;

    private int level;
    private int totalObjectifs = 0;
    private int boxesOnObjectifs = 0;

    private Heros heros;

    private HashMap<Case, Point> map = new HashMap<Case, Point>(); // permet de récupérer la position d'une case à
                                                                   // partir de sa référence
    private Case[][] grilleEntites = new Case[SIZE_X][SIZE_Y]; // permet de récupérer une case à partir de ses
                                                               // coordonnées

    private Map<Portal, Set<Case>> portalOccupiedAdjacentCases = new HashMap<>();

    public void resetPortalOccupiedCases() {
        portalOccupiedAdjacentCases.clear();
    }

    public Jeu(int level) {
        this.level = level;
        initialisationNiveau();
    }

    public Case findNextEmptyCaseAdjacentToPortal(Portal portal) {
        Set<Case> occupiedCases = portalOccupiedAdjacentCases.getOrDefault(portal, new HashSet<>());
        Point portalPoint = map.get(portal);
        int[][] directions = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}}; // Up, Right, Down, Left
    
        for (int[] dir : directions) {
            int newX = portalPoint.x + dir[0];
            int newY = portalPoint.y + dir[1];
            if (isValidLocation(newX, newY)) {
                Case potentialCase = grilleEntites[newX][newY];
                if (potentialCase instanceof Vide && !occupiedCases.contains(potentialCase)) {
                    occupiedCases.add(potentialCase); // Mark this case as occupied for this event
                    portalOccupiedAdjacentCases.put(portal, occupiedCases);
                    return potentialCase;
                }
            }
        }
        return null; // No suitable empty case found
    }

    private boolean isValidLocation(int x, int y) {
        return x >= 0 && x < SIZE_X && y >= 0 && y < SIZE_Y;
    }

    public Portal findPortalPair(Portal portal) {
        for (Case[] row : grilleEntites) {
            for (Case c : row) {
                if (c instanceof Portal && ((Portal) c).getPortalID() == portal.getPortalID() && c != portal) {
                    return (Portal) c; // Found the matching portal
                }
            }
        }
        return null; // No matching portal found
    }

    public int getLevel() {
        return level;
    }

    public Case[][] getGrille() {
        return grilleEntites;
    }

    public Heros getHeros() {
        return heros;
    }

    public boolean abletoUndo() {
        return heroMoveStack.size() >= 1;
    }

    public void resetCompteur() {
        compteurPas = 0;
    }

    public int getCompteurPas() {
        return compteurPas;
    }

    public void deplacerHeros(Direction d) {
        if (deplacerEntite(heros, d)) {
            heroMoveStack.push(new Move(heros, d.opposite())); // Push the reverse of the move onto the stack
            setChanged();
            notifyObservers();
        }
    }

    public int[][] loadLevel(String fileName) {
        int[][] data = Level.loadLevel(fileName);
        return data;
    }

    private void initialisationNiveau() {

        int data[][] = loadLevel("res/levels/" + level + ".txt");
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                addCase(new Vide(this), i, j);
            }
        }
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if (data[i][j] == 1) {
                    addCase(new Mur(this), i, j);
                } else if (data[i][j] == 3) {
                    addCase(new Objectif(this), i, j);
                    totalObjectifs++;
                } else if (data[i][j] == 6) {
                    addCase(new Portal(this, 1), i, j);
                }else if (data[i][j] == 7) {
                    addCase(new Glace(this), i, j);
                } else {
                    addCase(new Vide(this), i, j);
                }
            }
        }
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {

                if (data[i][j] == 2) {
                    Bloc b = new Bloc(this, grilleEntites[i][j]);
                }
                if (data[i][j] == 4) {
                    addCase(new Objectif(this), i, j);
                    Bloc b = new Bloc(this, grilleEntites[i][j]);
                    totalObjectifs++;
                }
                if (data[i][j] == 5) {
                    heros = new Heros(this, grilleEntites[i][j]);
                }
            }
        }
    }

    private void addCase(Case e, int x, int y) {
        grilleEntites[x][y] = e;
        map.put(e, new Point(x, y));
    }

    public boolean isWinConditionMet() {
        return boxesOnObjectifs == totalObjectifs;
    }

    public void updateBoxesOnObjectifs() {
        boxesOnObjectifs = 0;
        for (int i = 0; i < SIZE_X; i++) {
            for (int j = 0; j < SIZE_Y; j++) {
                if (grilleEntites[i][j] instanceof Objectif) {
                    Objectif obj = (Objectif) grilleEntites[i][j];
                    if (obj.hasBlock()) {
                        boxesOnObjectifs++;
                    }
                }
            }
        }
    }

    /**
     * Si le déplacement de l'entité est autorisé (pas de mur ou autre entité), il
     * est réalisé
     * Sinon, rien n'est fait.
     */
    public boolean deplacerEntite(Entite e, Direction d) {
        boolean retour = true;
        Point pCourant = map.get(e.getCase());
        Point pCible = calculerPointCible(pCourant, d);
    
        if (contenuDansGrille(pCible)) {
            Case caseCible = grilleEntites[pCible.x][pCible.y];
            Entite eCible = caseCible.getEntite();
    
            // Tente de pousser l'entité cible si elle existe
            if (eCible != null) {
                Point pNext = calculerPointCible(pCible, d);
                if (contenuDansGrille(pNext) && grilleEntites[pNext.x][pNext.y].peutEtreParcouru()) {
                    retour = eCible.pousser(d); // Essaye de pousser l'entité cible
                } else {
                    retour = false;
                }
            }
    
            if (retour && caseCible.peutEtreParcouru()) {
                // Déplace l'entité et vérifie si la case est de la glace
                e.getCase().quitterLaCase();
                caseCible.entrerSurLaCase(e);
                if (!(e instanceof Bloc)) {
                    compteurPas++;
                }
    
                // Logique spécifique pour le glissement sur la glace
                while (caseCible instanceof Glace) {
                    pCourant = pCible;
                    pCible = calculerPointCible(pCourant, d);
                    if (!contenuDansGrille(pCible) || !grilleEntites[pCible.x][pCible.y].peutEtreParcouru()) {
                        break; // Arrête si la prochaine case n'est pas dans la grille ou ne peut pas être parcourue
                    }
    
                    caseCible = grilleEntites[pCible.x][pCible.y];
                    if (caseCible.getEntite() != null) {
                        break; // Arrête si la prochaine case est occupée par une autre entité
                    }
    
                    // Continue le glissement
                    e.getCase().quitterLaCase();
                    caseCible.entrerSurLaCase(e);
                    if (!(e instanceof Bloc)) {
                        compteurPas++; // Optionnel : décide si le glissement sur la glace compte comme des pas
                    }
                }
    
                if (e instanceof Bloc) {
                    blockMoveStack.push(new Move(e, d.opposite()));
                }
            } else {
                retour = false;
            }
        } else {
            retour = false;
        }
    
        if (retour) {
            setChanged();
            notifyObservers();
        }
    
        return retour;
    }
    

    private Point calculerPointCible(Point pCourant, Direction d) {
        Point pCible = null;

        switch (d) {
            case haut:
                pCible = new Point(pCourant.x, pCourant.y - 1);
                break;
            case bas:
                pCible = new Point(pCourant.x, pCourant.y + 1);
                break;
            case gauche:
                pCible = new Point(pCourant.x - 1, pCourant.y);
                break;
            case droite:
                pCible = new Point(pCourant.x + 1, pCourant.y);
                break;

        }

        return pCible;
    }

    /**
     * Indique si p est contenu dans la grille
     */
    private boolean contenuDansGrille(Point p) {
        return p.x >= 0 && p.x < SIZE_X && p.y >= 0 && p.y < SIZE_Y;
    }

    private Case caseALaPosition(Point p) {
        Case retour = null;

        if (contenuDansGrille(p)) {
            retour = grilleEntites[p.x][p.y];
        }

        return retour;
    }

    public void resetLevel() {
        compteurPas = 0;
        totalObjectifs = 0;
        boxesOnObjectifs = 0;
        heros = null;
        map.clear();
        grilleEntites = new Case[SIZE_X][SIZE_Y];
        initialisationNiveau();
        setChanged();
        notifyObservers();
    }

    public void undoHeroMove() {
        if (!heroMoveStack.isEmpty()) {
            Move lastHeroMove = heroMoveStack.pop();
            lastHeroMove.getDirection().opposite();
            deplacerEntite(lastHeroMove.getEntity(), lastHeroMove.direction);
            compteurPas = compteurPas - 2;
            // Undo blocs
            if (!blockMoveStack.isEmpty()) {
                Move lastBlockMove = blockMoveStack.pop();
                lastBlockMove.getDirection().opposite();
                deplacerEntite(lastBlockMove.getEntity(), lastBlockMove.direction);
            }
            setChanged();
            notifyObservers();
        }
    }

    private class Move {
        private Entite entity;
        private Direction direction;

        public Move(Entite entity, Direction direction) {
            this.entity = entity;
            this.direction = direction;
        }

        public Entite getEntity() {
            return entity;
        }

        public Direction getDirection() {
            return direction;
        }

    }
}
