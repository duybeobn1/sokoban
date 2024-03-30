package VueControleur;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

import modele.*;

/**
 * Cette classe a deux fonctions :
 * (1) Vue : proposer une représentation graphique de l'application (cases
 * graphiques, etc.)
 * (2) Controleur : écouter les évènements clavier et déclencher le traitement
 * adapté sur le modèle (flèches direction Pacman, etc.))
 *
 */
public class VueControleur extends JFrame implements Observer {
    private Jeu jeu; // référence sur une classe de modèle : permet d'accéder aux données du modèle
                     // pour le rafraichissement, permet de communiquer les actions clavier (ou
                     // souris)

    private int sizeX; // taille de la grille affichée
    private int sizeY;

    // icones affichées dans la grille
    private ImageIcon icoHero;
    private ImageIcon icoVide;
    private ImageIcon icoMur;
    private ImageIcon icoBloc;
    private ImageIcon icoObjectif;
    private ImageIcon icoBlocObj;

    private JLabel[][] tabJLabel; // cases graphique (au moment du rafraichissement, chaque case va être associée
                                  // à une icône, suivant ce qui est présent dans le modèle)


    public Timer timer;
    public int secondes;

    public VueControleur(Jeu _jeu) {

        sizeX = jeu.SIZE_X;
        sizeY = _jeu.SIZE_Y;
        jeu = _jeu;

        timer = new Timer();
        secondes = 0;


        chargerLesIcones();
        placerLesComposantsGraphiques();
        mettreAJourAffichage();
        ajouterEcouteurClavier();
        jeu.addObserver(this);
        pack();

        setLocationRelativeTo(null);

    }

    private void ajouterEcouteurClavier() {
        addKeyListener(new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un
                                          // objet qui correspond au controleur dans MVC
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) { // on regarde quelle touche a été pressée

                    case KeyEvent.VK_LEFT:
                        icoHero = chargerIcone("res/player/left.png");
                        jeu.deplacerHeros(Direction.gauche);
                        break;
                    case KeyEvent.VK_RIGHT:
                        icoHero = chargerIcone("res/player/right.png");
                        jeu.deplacerHeros(Direction.droite);
                        break;
                    case KeyEvent.VK_DOWN:
                        icoHero = chargerIcone("res/player/front.png");
                        jeu.deplacerHeros(Direction.bas);
                        break;
                    case KeyEvent.VK_UP:
                        icoHero = chargerIcone("res/player/back.png");
                        jeu.deplacerHeros(Direction.haut);
                        break;

                }
            }
        });
    }

    private void chargerLesIcones() {
        icoHero = chargerIcone("res/player/front.png");
        icoVide = chargerIcone("res/blocks/ground.png");
        icoMur = chargerIcone("res/blocks/redBrick.png");
        icoBloc = chargerIcone("res/blocks/boxOff.png");
        icoObjectif = chargerIcone("res/blocks/spot.png");
        icoBlocObj = chargerIcone("res/blocks/boxOn.png");
    }

    private ImageIcon chargerIcone(String urlIcone) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(new File(urlIcone));
        } catch (IOException ex) {
            Logger.getLogger(VueControleur.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        return new ImageIcon(image);
    }

    private void placerLesComposantsGraphiques() {

        setTitle("Sokoban");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de terminer l'application à la fermeture de la fenêtre

        JComponent grilleJLabels = new JPanel(new GridLayout(sizeY, sizeX)); // grilleJLabels va contenir les cases
                                                                             // graphiques et les positionner sous la
                                                                             // forme d'une grille
        tabJLabel = new JLabel[sizeX][sizeY];

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                JLabel jlab = new JLabel();
                tabJLabel[x][y] = jlab; // on conserve les cases graphiques dans tabJLabel pour avoir un accès pratique
                                        // à celles-ci (voir mettreAJourAffichage() )
                grilleJLabels.add(jlab);
            }
        }
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jeu.resetLevel();
            }
        });

        JButton backButton = new JButton("Back to choose level");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                LevelSelector levelSelector = new LevelSelector();
                levelSelector.setVisible(true);
            }
        });

        JPanel buttonPanel = new JPanel(); // create a panel for the buttons
        buttonPanel.add(resetButton);
        buttonPanel.add(backButton);

        resetButton.setFocusable(false);
        backButton.setFocusable(false);

        getContentPane().setLayout(new BorderLayout());

        getContentPane().add(buttonPanel, BorderLayout.NORTH);
        getContentPane().add(grilleJLabels, BorderLayout.CENTER);
    }

    /**
     * Il y a une grille du côté du modèle ( jeu.getGrille() ) et une grille du côté
     * de la vue (tabJLabel)
     */
    private void mettreAJourAffichage() {

        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {

                Case c = jeu.getGrille()[x][y];

                if (c != null) {

                    Entite e = c.getEntite();

                    if (e != null) {
                        if (c.getEntite() instanceof Heros) {
                            tabJLabel[x][y].setIcon(new CompoundIcon(icoVide, icoHero));
                        } else if (c.getEntite() instanceof Bloc) {
                            if (c instanceof Objectif) {
                                tabJLabel[x][y].setIcon(icoBlocObj);
                            } else {
                                tabJLabel[x][y].setIcon(icoBloc);
                            }
                        }
                    } else {
                        if (jeu.getGrille()[x][y] instanceof Mur) {
                            tabJLabel[x][y].setIcon(icoMur);
                        } else if (jeu.getGrille()[x][y] instanceof Vide) {
                            tabJLabel[x][y].setIcon(icoVide);
                        } else if (jeu.getGrille()[x][y] instanceof Objectif) {
                            tabJLabel[x][y].setIcon(new CompoundIcon(icoVide, icoObjectif));
                        }
                    }

                }

            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        mettreAJourAffichage();
        jeu.updateBoxesOnObjectifs(); // Update the count of boxes on objectives
        if (jeu.isWinConditionMet()) { // Check if the win condition is met
            timer.cancel();
            System.out.println("Congratulations! You won the game!");
        }

        /*
         * 
         * // récupérer le processus graphique pour rafraichir
         * // (normalement, à l'inverse, a l'appel du modèle depuis le contrôleur,
         * utiliser un autre processus, voir classe Executor)
         * 
         * 
         * SwingUtilities.invokeLater(new Runnable() {
         * 
         * @Override
         * public void run() {
         * mettreAJourAffichage();
         * }
         * });
         */

    }
}
