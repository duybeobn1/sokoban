/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.awt.Point;
import java.util.HashMap;
import java.util.Observable;

public class Jeu extends Observable {

    public static final int SIZE_X = 10;
    public static final int SIZE_Y = 10;

    private int level;
    private int totalObjectifs = 0;
    private int boxesOnObjectifs = 0;

    private Heros heros;

    private HashMap<Case, Point> map = new HashMap<Case, Point>(); // permet de récupérer la position d'une case à
                                                                   // partir de sa référence
    private Case[][] grilleEntites = new Case[SIZE_X][SIZE_Y]; // permet de récupérer une case à partir de ses
                                                               // coordonnées

    public Jeu(int level) {
        this.level = level;
        initialisationNiveau();
    }

    public Case[][] getGrille() {
        return grilleEntites;
    }

    public Heros getHeros() {
        return heros;
    }

    public void deplacerHeros(Direction d) {
        heros.avancerDirectionChoisie(d);
        setChanged();
        notifyObservers();
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
                if (data[i][j] == 0) {
                    addCase(new Vide(this), i, j);
                } else if (data[i][j] == 1) {
                    addCase(new Mur(this), i, j);
                } else if (data[i][j] == 3) {
                    addCase(new Objectif(this), i, j);
                    totalObjectifs++;
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
            Entite eCible = caseALaPosition(pCible).getEntite();
            if (eCible != null) {
                eCible.pousser(d);
            }

            // si la case est libérée
            if (caseALaPosition(pCible).peutEtreParcouru()) {
                e.getCase().quitterLaCase();
                caseALaPosition(pCible).entrerSurLaCase(e);

            } else {
                retour = false;
            }

        } else {
            retour = false;
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
        totalObjectifs = 0;
        boxesOnObjectifs = 0;
        heros = null;
        map.clear();
        grilleEntites = new Case[SIZE_X][SIZE_Y];
        initialisationNiveau();
        setChanged();
        notifyObservers();
    }

}
