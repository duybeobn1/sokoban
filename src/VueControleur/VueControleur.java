package VueControleur;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;
import modele.*;
import modele.sound.SoundController;

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

    private JPanel buttonPanel;
    private JButton backButton;
    private JLabel timerLabel;
    private JLabel pas;
    public int undoCount = 0;

    private JButton resetButton;
    private JLabel[][] tabJLabel; // cases graphique (au moment du rafraichissement, chaque case va être associée
                                  // à une icône, suivant ce qui est présent dans le modèle)

    public Timer timer;
    public int secondes;

    private SoundController soundController;

    // Déclarer un Set pour suivre les objectifs pour lesquels le son a été joué
    private Set<String> objectifsAvecSonJoue = new HashSet<>();

    public VueControleur(Jeu _jeu) {
        sizeX = jeu.SIZE_X;
        sizeY = _jeu.SIZE_Y;
        jeu = _jeu;
        soundController = new SoundController();
        timer = new Timer();
        secondes = 0;
        chargerLesIcones();
        placerLesComposantsGraphiques();
        mettreAJourAffichage();
        ajouterEcouteurClavier();
        jeu.addObserver(this);
        setLocation(getX() + 400, getY() + 70);
        pack();
        // Initialiser le contrôleur audio
        // Charger et jouer la musique d'arrière-plan
        soundController.playBackgroundMusic("song.wav");
        soundController.loadSoundEffect("good.wav");
        soundController.loadSoundEffect("good2.wav");

    }

    public void startAndTrackTime() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                secondes++;
                timerLabel.setText("Time: " + secondes + " seconds");
            }
        }, 1000, 1000);
    }

    public void startTrackingPas() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                pas.setText("Pas: " + jeu.getCompteurPas());
            }
        }, 0, 1);
    }

    private void ajouterEcouteurClavier() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {

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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.GREEN);
        JPanel grilleJLabels = new JPanel(new GridBagLayout());

        int topInset = 100;
        int leftInset = 100;
        int bottomInset = 100;
        int rightInset = 100;
        grilleJLabels.setBorder(BorderFactory.createEmptyBorder(topInset, leftInset, bottomInset, rightInset));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        tabJLabel = new JLabel[sizeX][sizeY];

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                JLabel jlab = new JLabel();
                tabJLabel[x][y] = jlab;
                grilleJLabels.add(jlab, gbc);
                gbc.gridx++;
            }
            gbc.gridx = 0;
            gbc.gridy++;
        }

        getContentPane().setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(grilleJLabels, BorderLayout.CENTER);

        final int[] countStop = { 0, 1 };
        JButton musicButton = new JButton("Stop/Play Background Music");
        musicButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (countStop[1] <= 1) {
                    if (countStop[0] == 0) {
                        soundController.stopBackgroundMusic();
                        countStop[0] = 1;
                        countStop[1] = 0;
                    } else {
                        soundController.playBackgroundMusic("song.wav");
                        countStop[0] = 0;
                        countStop[1] = 1;
                    }
                }
            }
        });

        resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pas.setText("Pas : 0");
                timerLabel.setText("Time : 0 seconds");
                secondes = 0;
                undoCount = 0;
                jeu.resetLevel();
            }
        });

        backButton = new JButton("Back to choose level");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                soundController.stopBackgroundMusic();
                dispose();
                LevelSelector levelSelector = new LevelSelector();
                levelSelector.setVisible(true);
            }
        });
        timerLabel = new JLabel();
        pas = new JLabel();

        pas.setText("Pas : 0");
        timerLabel.setText("Time : 0 seconds");

        JButton undoButton = new JButton("Undo");

        undoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (jeu.abletoUndo()) {
                    if (undoCount < 1) {
                        jeu.undoHeroMove();
                        undoCount++;
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Cannot undo the move.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(resetButton);
        buttonPanel.add(backButton);
        buttonPanel.add(pas);
        buttonPanel.add(timerLabel);
        buttonPanel.add(undoButton);
        buttonPanel.add(musicButton);
        musicButton.setFocusable(false);
        undoButton.setFocusable(false);
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
                                if (!objectifsAvecSonJoue.contains(x + "," + y)) {
                                    soundController.loadSoundEffect("good.wav");
                                    soundController.playSoundEffect("good.wav");
                                    tabJLabel[x][y].setIcon(icoBlocObj);
                                    objectifsAvecSonJoue.add(x + "," + y); // Ajouter les coordonnées à l'ensemble
                                } else {
                                    tabJLabel[x][y].setIcon(icoBlocObj); // Si le son a déjà été joué, juste définir
                                                                         // l'icône sans le jouer à nouveau
                                }
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

    public void showEndGameOptions() {

        soundController.stopBackgroundMusic();
        writeHighscoreToFile();
        String[] options = new String[] { "Play Again", "Next Level", "Main Menu" };
        int response = JOptionPane.showOptionDialog(null,
                "Congratulations! You won the game! What would you like to do next?", "End Game",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);

        switch (response) {
            case 0:
                resetButton.doClick();
                timer.cancel();
                timer = new Timer();
                startAndTrackTime();
                startTrackingPas();
                soundController.playBackgroundMusic("song.wav");
                break;
            case 1:
                if (jeu.getLevel() >= 19) {
                    JOptionPane.showMessageDialog(null, "You finished all the game");
                    backButton.doClick();

                } else {
                    dispose();
                    LevelSelector vc = new LevelSelector();
                    vc.loadLevel(jeu.getLevel() + 1);
                }
                break;
            case 2:
                dispose();
                StartMenu startMenu = new StartMenu();
                startMenu.setVisible(true);
                break;
            default:
                break;
        }
    }

    private void writeHighscoreToFile() {
        try {
            String filename = "res/highscores.txt";
            File file = new File(filename);
            if (!file.exists()) {
                file.createNewFile();
            }

            BufferedReader reader = new BufferedReader(new FileReader(filename));
            Map<Integer, Score> scores = new HashMap<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length >= 4 && parts[0].equals("Level:")) {
                    int level = Integer.parseInt(parts[1]);
                    int seconds = Integer.parseInt(parts[3]);
                    int pas = Integer.parseInt(parts[5]);
                    Score score = new Score(seconds, pas);
                    scores.put(level, score);
                }
            }
            reader.close();

            int currentLevel = jeu.getLevel() + 1;
            Score currentScore = new Score(secondes, jeu.getCompteurPas());
            Score existingScore = scores.get(currentLevel);

            if (existingScore == null || currentScore.isBetterThan(existingScore)) {
                scores.put(currentLevel, currentScore);

                FileWriter writer = new FileWriter(filename);
                for (Map.Entry<Integer, Score> entry : scores.entrySet()) {
                    writer.write("Level: " + entry.getKey() + " Seconds: " + entry.getValue().getSeconds() + " Pas: "
                            + entry.getValue().getPas() + "\n");
                }
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to write highscore to file.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void update(Observable o, Object arg) {

        mettreAJourAffichage();
        jeu.updateBoxesOnObjectifs();
        if (jeu.isWinConditionMet()) {
            // soundController.playBackgroundMusic("sound/good2.wav");
            timer.cancel();
            showEndGameOptions();
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
